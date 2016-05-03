package org.bukkit.craftbukkit.libs.org.ibex.nestedvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Platform;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;

public abstract class Runtime implements UsermodeConstants, Registers, Cloneable {

    public static final String VERSION = "1.0";
    static final boolean STDERR_DIAG = true;
    protected final int pageShift;
    private final int stackBottom;
    protected int[][] readPages;
    protected int[][] writePages;
    private int heapEnd;
    private static final int STACK_GUARD_PAGES = 4;
    private long startTime;
    public static final int RUNNING = 0;
    public static final int STOPPED = 1;
    public static final int PAUSED = 2;
    public static final int CALLJAVA = 3;
    public static final int EXITED = 4;
    public static final int EXECED = 5;
    protected int state;
    private int exitStatus;
    public Runtime.ExecutionException exitException;
    Runtime.FD[] fds;
    boolean[] closeOnExec;
    Runtime.SecurityManager sm;
    private Runtime.CallJavaCB callJavaCB;
    private byte[] _byteBuf;
    static final int MAX_CHUNK = 16776192;
    static final boolean win32Hacks;
    public static final int RD_ONLY = 0;
    public static final int WR_ONLY = 1;
    public static final int RDWR = 2;
    public static final int O_CREAT = 512;
    public static final int O_EXCL = 2048;
    public static final int O_APPEND = 8;
    public static final int O_TRUNC = 1024;
    public static final int O_NONBLOCK = 16384;
    public static final int O_NOCTTY = 32768;

    protected abstract int heapStart();

    protected abstract int entryPoint();

    protected int userInfoBase() {
        return 0;
    }

    protected int userInfoSize() {
        return 0;
    }

    protected abstract int gp();

    public final int getState() {
        return this.state;
    }

    public void setSecurityManager(Runtime.SecurityManager runtime_securitymanager) {
        this.sm = runtime_securitymanager;
    }

    public void setCallJavaCB(Runtime.CallJavaCB runtime_calljavacb) {
        this.callJavaCB = runtime_calljavacb;
    }

    protected abstract void _execute() throws Runtime.ExecutionException;

    public int lookupSymbol(String s) {
        return -1;
    }

    protected abstract void getCPUState(Runtime.CPUState runtime_cpustate);

    protected abstract void setCPUState(Runtime.CPUState runtime_cpustate);

    protected Object clone() throws CloneNotSupportedException {
        Runtime runtime = (Runtime) super.clone();

        runtime._byteBuf = null;
        runtime.startTime = 0L;
        runtime.fds = new Runtime.FD[64];

        int i;

        for (i = 0; i < 64; ++i) {
            if (this.fds[i] != null) {
                runtime.fds[i] = this.fds[i].dup();
            }
        }

        i = this.writePages.length;
        runtime.readPages = new int[i][];
        runtime.writePages = new int[i][];

        for (int j = 0; j < i; ++j) {
            if (this.readPages[j] != null) {
                if (this.writePages[j] == null) {
                    runtime.readPages[j] = this.readPages[j];
                } else {
                    runtime.readPages[j] = runtime.writePages[j] = (int[]) ((int[]) this.writePages[j].clone());
                }
            }
        }

        return runtime;
    }

    protected Runtime(int i, int j) {
        this(i, j, false);
    }

    protected Runtime(int i, int j, boolean flag) {
        this.state = 1;
        if (i <= 0) {
            throw new IllegalArgumentException("pageSize <= 0");
        } else if (j <= 0) {
            throw new IllegalArgumentException("totalPages <= 0");
        } else if ((i & i - 1) != 0) {
            throw new IllegalArgumentException("pageSize not a power of two");
        } else {
            int k;

            for (k = 0; i >>> k != 1; ++k) {
                ;
            }

            this.pageShift = k;
            int l = this.heapStart();
            int i1 = j * i;
            int j1 = max(i1 / 512, 131072);
            boolean flag1 = false;

            if (j > 1) {
                j1 = max(j1, i);
                j1 = j1 + i - 1 & ~(i - 1);
                int k1 = j1 >>> this.pageShift;

                l = l + i - 1 & ~(i - 1);
                if (k1 + 4 + (l >>> this.pageShift) >= j) {
                    throw new IllegalArgumentException("total pages too small");
                }
            } else {
                if (i < l + j1) {
                    throw new IllegalArgumentException("total memory too small");
                }

                l = l + 4095 & -4097;
            }

            this.stackBottom = i1 - j1;
            this.heapEnd = l;
            this.readPages = new int[j][];
            this.writePages = new int[j][];
            if (j == 1) {
                this.readPages[0] = this.writePages[0] = new int[i >> 2];
            } else {
                for (int l1 = this.stackBottom >>> this.pageShift; l1 < this.writePages.length; ++l1) {
                    this.readPages[l1] = this.writePages[l1] = new int[i >> 2];
                }
            }

            if (!flag) {
                this.fds = new Runtime.FD[64];
                this.closeOnExec = new boolean[64];
                Object object = Runtime.win32Hacks ? new Runtime.Win32ConsoleIS(System.in) : System.in;

                this.addFD(new Runtime.TerminalFD((InputStream) object));
                this.addFD(new Runtime.TerminalFD(System.out));
                this.addFD(new Runtime.TerminalFD(System.err));
            }

        }
    }

    protected final void initPages(int[] aint, int i, boolean flag) {
        int j = 1 << this.pageShift >>> 2;
        int k = (1 << this.pageShift) - 1;

        int l;

        for (int i1 = 0; i1 < aint.length; i += l * 4) {
            int j1 = i >>> this.pageShift;
            int k1 = (i & k) >> 2;

            l = min(j - k1, aint.length - i1);
            if (this.readPages[j1] == null) {
                this.initPage(j1, flag);
            } else if (!flag && this.writePages[j1] == null) {
                this.writePages[j1] = this.readPages[j1];
            }

            System.arraycopy(aint, i1, this.readPages[j1], k1, l);
            i1 += l;
        }

    }

    protected final void clearPages(int i, int j) {
        int k = 1 << this.pageShift >>> 2;
        int l = (1 << this.pageShift) - 1;

        int i1;

        for (int j1 = 0; j1 < j; i += i1 * 4) {
            int k1 = i >>> this.pageShift;
            int l1 = (i & l) >> 2;

            i1 = min(k - l1, j - j1);
            if (this.readPages[k1] == null) {
                this.readPages[k1] = this.writePages[k1] = new int[k];
            } else {
                if (this.writePages[k1] == null) {
                    this.writePages[k1] = this.readPages[k1];
                }

                for (int i2 = l1; i2 < l1 + i1; ++i2) {
                    this.writePages[k1][i2] = 0;
                }
            }

            j1 += i1;
        }

    }

    public final void copyin(int i, byte[] abyte, int j) throws Runtime.ReadFaultException {
        int k = 1 << this.pageShift >>> 2;
        int l = k - 1;
        int i1 = 0;

        if (j != 0) {
            int j1;

            if ((i & 3) != 0) {
                j1 = this.memRead(i & -4);
                switch (i & 3) {
                case 1:
                    abyte[i1++] = (byte) (j1 >>> 16 & 255);
                    --j;
                    if (j == 0) {
                        break;
                    }

                case 2:
                    abyte[i1++] = (byte) (j1 >>> 8 & 255);
                    --j;
                    if (j == 0) {
                        break;
                    }

                case 3:
                    abyte[i1++] = (byte) (j1 >>> 0 & 255);
                    --j;
                    if (j == 0) {
                        ;
                    }
                }

                i = (i & -4) + 4;
            }

            if ((j & -4) != 0) {
                j1 = j >>> 2;

                int k1;
                int l1;

                for (k1 = i >>> 2; j1 != 0; j1 -= l1) {
                    int[] aint = this.readPages[k1 >>> this.pageShift - 2];

                    if (aint == null) {
                        throw new Runtime.ReadFaultException(k1 << 2);
                    }

                    int i2 = k1 & l;

                    l1 = min(j1, k - i2);

                    for (int j2 = 0; j2 < l1; i1 += 4) {
                        int k2 = aint[i2 + j2];

                        abyte[i1 + 0] = (byte) (k2 >>> 24 & 255);
                        abyte[i1 + 1] = (byte) (k2 >>> 16 & 255);
                        abyte[i1 + 2] = (byte) (k2 >>> 8 & 255);
                        abyte[i1 + 3] = (byte) (k2 >>> 0 & 255);
                        ++j2;
                    }

                    k1 += l1;
                }

                i = k1 << 2;
                j &= 3;
            }

            if (j != 0) {
                j1 = this.memRead(i);
                switch (j) {
                case 3:
                    abyte[i1 + 2] = (byte) (j1 >>> 8 & 255);

                case 2:
                    abyte[i1 + 1] = (byte) (j1 >>> 16 & 255);

                case 1:
                    abyte[i1 + 0] = (byte) (j1 >>> 24 & 255);
                }
            }

        }
    }

    public final void copyout(byte[] abyte, int i, int j) throws Runtime.FaultException {
        int k = 1 << this.pageShift >>> 2;
        int l = k - 1;
        int i1 = 0;

        if (j != 0) {
            int j1;

            if ((i & 3) != 0) {
                j1 = this.memRead(i & -4);
                switch (i & 3) {
                case 1:
                    j1 = j1 & -16711681 | (abyte[i1++] & 255) << 16;
                    --j;
                    if (j == 0) {
                        break;
                    }

                case 2:
                    j1 = j1 & -65281 | (abyte[i1++] & 255) << 8;
                    --j;
                    if (j == 0) {
                        break;
                    }

                case 3:
                    j1 = j1 & -256 | (abyte[i1++] & 255) << 0;
                    --j;
                    if (j == 0) {
                        ;
                    }
                }

                this.memWrite(i & -4, j1);
                i += i1;
            }

            if ((j & -4) != 0) {
                j1 = j >>> 2;

                int k1;
                int l1;

                for (k1 = i >>> 2; j1 != 0; j1 -= l1) {
                    int[] aint = this.writePages[k1 >>> this.pageShift - 2];

                    if (aint == null) {
                        throw new Runtime.WriteFaultException(k1 << 2);
                    }

                    int i2 = k1 & l;

                    l1 = min(j1, k - i2);

                    for (int j2 = 0; j2 < l1; i1 += 4) {
                        aint[i2 + j2] = (abyte[i1 + 0] & 255) << 24 | (abyte[i1 + 1] & 255) << 16 | (abyte[i1 + 2] & 255) << 8 | (abyte[i1 + 3] & 255) << 0;
                        ++j2;
                    }

                    k1 += l1;
                }

                i = k1 << 2;
                j &= 3;
            }

            if (j != 0) {
                j1 = this.memRead(i);
                switch (j) {
                case 1:
                    j1 = j1 & 16777215 | (abyte[i1 + 0] & 255) << 24;
                    break;

                case 2:
                    j1 = j1 & '\uffff' | (abyte[i1 + 0] & 255) << 24 | (abyte[i1 + 1] & 255) << 16;
                    break;

                case 3:
                    j1 = j1 & 255 | (abyte[i1 + 0] & 255) << 24 | (abyte[i1 + 1] & 255) << 16 | (abyte[i1 + 2] & 255) << 8;
                }

                this.memWrite(i, j1);
            }

        }
    }

    public final void memcpy(int i, int j, int k) throws Runtime.FaultException {
        int l = 1 << this.pageShift >>> 2;
        int i1 = l - 1;
        int j1;

        if ((i & 3) == 0 && (j & 3) == 0) {
            int k1;

            if ((k & -4) != 0) {
                j1 = k >> 2;
                k1 = j >>> 2;

                int l1;
                int i2;

                for (l1 = i >>> 2; j1 != 0; j1 -= i2) {
                    int[] aint = this.readPages[k1 >>> this.pageShift - 2];

                    if (aint == null) {
                        throw new Runtime.ReadFaultException(k1 << 2);
                    }

                    int[] aint1 = this.writePages[l1 >>> this.pageShift - 2];

                    if (aint1 == null) {
                        throw new Runtime.WriteFaultException(l1 << 2);
                    }

                    int j2 = k1 & i1;
                    int k2 = l1 & i1;

                    i2 = min(j1, l - max(j2, k2));
                    System.arraycopy(aint, j2, aint1, k2, i2);
                    k1 += i2;
                    l1 += i2;
                }

                j = k1 << 2;
                i = l1 << 2;
                k &= 3;
            }

            if (k != 0) {
                j1 = this.memRead(j);
                k1 = this.memRead(i);
                switch (k) {
                case 1:
                    this.memWrite(i, j1 & -16777216 | k1 & 16777215);
                    break;

                case 2:
                    this.memWrite(i, j1 & -65536 | k1 & '\uffff');
                    break;

                case 3:
                    this.memWrite(i, j1 & -256 | k1 & 255);
                }
            }
        } else {
            while (k > 0) {
                j1 = min(k, 16776192);
                byte[] abyte = this.byteBuf(j1);

                this.copyin(j, abyte, j1);
                this.copyout(abyte, i, j1);
                k -= j1;
                j += j1;
                i += j1;
            }
        }

    }

    public final void memset(int i, int j, int k) throws Runtime.FaultException {
        int l = 1 << this.pageShift >>> 2;
        int i1 = l - 1;
        int j1 = (j & 255) << 24 | (j & 255) << 16 | (j & 255) << 8 | (j & 255) << 0;
        int k1;

        if ((i & 3) != 0) {
            k1 = this.memRead(i & -4);
            switch (i & 3) {
            case 1:
                k1 = k1 & -16711681 | (j & 255) << 16;
                --k;
                if (k == 0) {
                    break;
                }

            case 2:
                k1 = k1 & -65281 | (j & 255) << 8;
                --k;
                if (k == 0) {
                    break;
                }

            case 3:
                k1 = k1 & -256 | (j & 255) << 0;
                --k;
                if (k == 0) {
                    ;
                }
            }

            this.memWrite(i & -4, k1);
            i = (i & -4) + 4;
        }

        if ((k & -4) != 0) {
            k1 = k >> 2;

            int l1;
            int i2;

            for (l1 = i >>> 2; k1 != 0; k1 -= i2) {
                int[] aint = this.readPages[l1 >>> this.pageShift - 2];

                if (aint == null) {
                    throw new Runtime.WriteFaultException(l1 << 2);
                }

                int j2 = l1 & i1;

                i2 = min(k1, l - j2);

                for (int k2 = j2; k2 < j2 + i2; ++k2) {
                    aint[k2] = j1;
                }

                l1 += i2;
            }

            i = l1 << 2;
            k &= 3;
        }

        if (k != 0) {
            k1 = this.memRead(i);
            switch (k) {
            case 1:
                k1 = k1 & 16777215 | j1 & -16777216;
                break;

            case 2:
                k1 = k1 & '\uffff' | j1 & -65536;
                break;

            case 3:
                k1 = k1 & 255 | j1 & -256;
            }

            this.memWrite(i, k1);
        }

    }

    public final int memRead(int i) throws Runtime.ReadFaultException {
        if ((i & 3) != 0) {
            throw new Runtime.ReadFaultException(i);
        } else {
            return this.unsafeMemRead(i);
        }
    }

    protected final int unsafeMemRead(int i) throws Runtime.ReadFaultException {
        int j = i >>> this.pageShift;
        int k = (i & (1 << this.pageShift) - 1) >> 2;

        try {
            return this.readPages[j][k];
        } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            if (j >= 0 && j < this.readPages.length) {
                throw arrayindexoutofboundsexception;
            } else {
                throw new Runtime.ReadFaultException(i);
            }
        } catch (NullPointerException nullpointerexception) {
            throw new Runtime.ReadFaultException(i);
        }
    }

    public final void memWrite(int i, int j) throws Runtime.WriteFaultException {
        if ((i & 3) != 0) {
            throw new Runtime.WriteFaultException(i);
        } else {
            this.unsafeMemWrite(i, j);
        }
    }

    protected final void unsafeMemWrite(int i, int j) throws Runtime.WriteFaultException {
        int k = i >>> this.pageShift;
        int l = (i & (1 << this.pageShift) - 1) >> 2;

        try {
            this.writePages[k][l] = j;
        } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            if (k >= 0 && k < this.writePages.length) {
                throw arrayindexoutofboundsexception;
            } else {
                throw new Runtime.WriteFaultException(i);
            }
        } catch (NullPointerException nullpointerexception) {
            throw new Runtime.WriteFaultException(i);
        }
    }

    private final int[] initPage(int i) {
        return this.initPage(i, false);
    }

    private final int[] initPage(int i, boolean flag) {
        int[] aint = new int[1 << this.pageShift >>> 2];

        this.writePages[i] = flag ? null : aint;
        this.readPages[i] = aint;
        return aint;
    }

    public final int exitStatus() {
        if (this.state != 4) {
            throw new IllegalStateException("exitStatus() called in an inappropriate state");
        } else {
            return this.exitStatus;
        }
    }

    private int addStringArray(String[] astring, int i) throws Runtime.FaultException {
        int j = astring.length;
        int k = 0;

        int l;

        for (l = 0; l < j; ++l) {
            k += astring[l].length() + 1;
        }

        k += (j + 1) * 4;
        l = i - k & -4;
        int i1 = l + (j + 1) * 4;
        int[] aint = new int[j + 1];

        try {
            int j1;

            for (j1 = 0; j1 < j; ++j1) {
                byte[] abyte = getBytes(astring[j1]);

                aint[j1] = i1;
                this.copyout(abyte, i1, abyte.length);
                this.memset(i1 + abyte.length, 0, 1);
                i1 += abyte.length + 1;
            }

            i1 = l;

            for (j1 = 0; j1 < j + 1; ++j1) {
                this.memWrite(i1, aint[j1]);
                i1 += 4;
            }

            return l;
        } catch (Runtime.FaultException runtime_faultexception) {
            throw new RuntimeException(runtime_faultexception.toString());
        }
    }

    String[] createEnv(String[] astring) {
        if (astring == null) {
            astring = new String[0];
        }

        return astring;
    }

    public void setUserInfo(int i, int j) {
        if (i >= 0 && i < this.userInfoSize() / 4) {
            try {
                this.memWrite(this.userInfoBase() + i * 4, j);
            } catch (Runtime.FaultException runtime_faultexception) {
                throw new RuntimeException(runtime_faultexception.toString());
            }
        } else {
            throw new IndexOutOfBoundsException("setUserInfo called with index >= " + this.userInfoSize() / 4);
        }
    }

    public int getUserInfo(int i) {
        if (i >= 0 && i < this.userInfoSize() / 4) {
            try {
                return this.memRead(this.userInfoBase() + i * 4);
            } catch (Runtime.FaultException runtime_faultexception) {
                throw new RuntimeException(runtime_faultexception.toString());
            }
        } else {
            throw new IndexOutOfBoundsException("setUserInfo called with index >= " + this.userInfoSize() / 4);
        }
    }

    private void __execute() {
        try {
            this._execute();
        } catch (Runtime.FaultException runtime_faultexception) {
            runtime_faultexception.printStackTrace();
            this.exit(139, true);
            this.exitException = runtime_faultexception;
        } catch (Runtime.ExecutionException runtime_executionexception) {
            runtime_executionexception.printStackTrace();
            this.exit(132, true);
            this.exitException = runtime_executionexception;
        }

    }

    public final boolean execute() {
        if (this.state != 2) {
            throw new IllegalStateException("execute() called in inappropriate state");
        } else {
            if (this.startTime == 0L) {
                this.startTime = System.currentTimeMillis();
            }

            this.state = 0;
            this.__execute();
            if (this.state != 2 && this.state != 4 && this.state != 5) {
                throw new IllegalStateException("execute() ended up in an inappropriate state (" + this.state + ")");
            } else {
                return this.state != 2;
            }
        }
    }

    static String[] concatArgv(String s, String[] astring) {
        String[] astring1 = new String[astring.length + 1];

        System.arraycopy(astring, 0, astring1, 1, astring.length);
        astring1[0] = s;
        return astring1;
    }

    public final int run() {
        return this.run((String[]) null);
    }

    public final int run(String s, String[] astring) {
        return this.run(concatArgv(s, astring));
    }

    public final int run(String[] astring) {
        return this.run(astring, (String[]) null);
    }

    public final int run(String[] astring, String[] astring1) {
        this.start(astring, astring1);

        while (!this.execute()) {
            System.err.println("WARNING: Pause requested while executing run()");
        }

        if (this.state == 5) {
            System.err.println("WARNING: Process exec()ed while being run under run()");
        }

        return this.state == 4 ? this.exitStatus() : 0;
    }

    public final void start() {
        this.start((String[]) null);
    }

    public final void start(String[] astring) {
        this.start(astring, (String[]) null);
    }

    public final void start(String[] astring, String[] astring1) {
        if (this.state != 1) {
            throw new IllegalStateException("start() called in inappropriate state");
        } else {
            if (astring == null) {
                astring = new String[] { this.getClass().getName()};
            }

            int i;
            int j = i = this.writePages.length * (1 << this.pageShift);

            int k;
            int l;

            try {
                j = k = this.addStringArray(astring, j);
                j = l = this.addStringArray(this.createEnv(astring1), j);
            } catch (Runtime.FaultException runtime_faultexception) {
                throw new IllegalArgumentException("args/environ too big");
            }

            j &= -16;
            if (i - j > 65536) {
                throw new IllegalArgumentException("args/environ too big");
            } else {
                if (this.heapEnd == 0) {
                    this.heapEnd = this.heapStart();
                    if (this.heapEnd == 0) {
                        throw new Error("heapEnd == 0");
                    }

                    int i1 = this.writePages.length == 1 ? 4096 : 1 << this.pageShift;

                    this.heapEnd = this.heapEnd + i1 - 1 & ~(i1 - 1);
                }

                Runtime.CPUState runtime_cpustate = new Runtime.CPUState();

                runtime_cpustate.r[4] = k;
                runtime_cpustate.r[5] = l;
                runtime_cpustate.r[29] = j;
                runtime_cpustate.r[31] = -559038737;
                runtime_cpustate.r[28] = this.gp();
                runtime_cpustate.pc = this.entryPoint();
                this.setCPUState(runtime_cpustate);
                this.state = 2;
                this._started();
            }
        }
    }

    public final void stop() {
        if (this.state != 0 && this.state != 2) {
            throw new IllegalStateException("stop() called in inappropriate state");
        } else {
            this.exit(0, false);
        }
    }

    void _started() {}

    public final int call(String s, Object[] aobject) throws Runtime.CallException, Runtime.FaultException {
        if (this.state != 2 && this.state != 3) {
            throw new IllegalStateException("call() called in inappropriate state");
        } else if (aobject.length > 7) {
            throw new IllegalArgumentException("args.length > 7");
        } else {
            Runtime.CPUState runtime_cpustate = new Runtime.CPUState();

            this.getCPUState(runtime_cpustate);
            int i = runtime_cpustate.r[29];
            int[] aint = new int[aobject.length];

            int j;

            for (j = 0; j < aobject.length; ++j) {
                Object object = aobject[j];
                byte[] abyte = null;

                if (object instanceof String) {
                    abyte = getBytes((String) object);
                } else if (object instanceof byte[]) {
                    abyte = (byte[]) ((byte[]) object);
                } else if (object instanceof Number) {
                    aint[j] = ((Number) object).intValue();
                }

                if (abyte != null) {
                    i -= abyte.length;
                    this.copyout(abyte, i, abyte.length);
                    aint[j] = i;
                }
            }

            j = runtime_cpustate.r[29];
            if (j == i) {
                return this.call(s, aint);
            } else {
                runtime_cpustate.r[29] = i;
                this.setCPUState(runtime_cpustate);
                int k = this.call(s, aint);

                runtime_cpustate.r[29] = j;
                this.setCPUState(runtime_cpustate);
                return k;
            }
        }
    }

    public final int call(String s) throws Runtime.CallException {
        return this.call(s, new int[0]);
    }

    public final int call(String s, int i) throws Runtime.CallException {
        return this.call(s, new int[] { i});
    }

    public final int call(String s, int i, int j) throws Runtime.CallException {
        return this.call(s, new int[] { i, j});
    }

    public final int call(String s, int[] aint) throws Runtime.CallException {
        int i = this.lookupSymbol(s);

        if (i == -1) {
            throw new Runtime.CallException(s + " not found");
        } else {
            int j = this.lookupSymbol("_call_helper");

            if (j == -1) {
                throw new Runtime.CallException("_call_helper not found");
            } else {
                return this.call(j, i, aint);
            }
        }
    }

    public final int call(int i, int j, int[] aint) throws Runtime.CallException {
        if (aint.length > 7) {
            throw new IllegalArgumentException("rest.length > 7");
        } else if (this.state != 2 && this.state != 3) {
            throw new IllegalStateException("call() called in inappropriate state");
        } else {
            int k = this.state;
            Runtime.CPUState runtime_cpustate = new Runtime.CPUState();

            this.getCPUState(runtime_cpustate);
            Runtime.CPUState runtime_cpustate1 = runtime_cpustate.dup();

            runtime_cpustate1.r[29] &= -16;
            runtime_cpustate1.r[31] = -559038737;
            runtime_cpustate1.r[4] = j;
            switch (aint.length) {
            case 7:
                runtime_cpustate1.r[19] = aint[6];

            case 6:
                runtime_cpustate1.r[18] = aint[5];

            case 5:
                runtime_cpustate1.r[17] = aint[4];

            case 4:
                runtime_cpustate1.r[16] = aint[3];

            case 3:
                runtime_cpustate1.r[7] = aint[2];

            case 2:
                runtime_cpustate1.r[6] = aint[1];

            case 1:
                runtime_cpustate1.r[5] = aint[0];

            default:
                runtime_cpustate1.pc = i;
                this.state = 0;
                this.setCPUState(runtime_cpustate1);
                this.__execute();
                this.getCPUState(runtime_cpustate1);
                this.setCPUState(runtime_cpustate);
                if (this.state != 2) {
                    throw new Runtime.CallException("Process exit()ed while servicing a call() request");
                } else {
                    this.state = k;
                    return runtime_cpustate1.r[3];
                }
            }
        }
    }

    public final int addFD(Runtime.FD runtime_fd) {
        if (this.state != 4 && this.state != 5) {
            int i;

            for (i = 0; i < 64 && this.fds[i] != null; ++i) {
                ;
            }

            if (i == 64) {
                return -1;
            } else {
                this.fds[i] = runtime_fd;
                this.closeOnExec[i] = false;
                return i;
            }
        } else {
            throw new IllegalStateException("addFD called in inappropriate state");
        }
    }

    void _preCloseFD(Runtime.FD runtime_fd) {}

    void _postCloseFD(Runtime.FD runtime_fd) {}

    public final boolean closeFD(int i) {
        if (this.state != 4 && this.state != 5) {
            if (i >= 0 && i < 64) {
                if (this.fds[i] == null) {
                    return false;
                } else {
                    this._preCloseFD(this.fds[i]);
                    this.fds[i].close();
                    this._postCloseFD(this.fds[i]);
                    this.fds[i] = null;
                    return true;
                }
            } else {
                return false;
            }
        } else {
            throw new IllegalStateException("closeFD called in inappropriate state");
        }
    }

    public final int dupFD(int i) {
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -1;
            } else {
                int j;

                for (j = 0; j < 64 && this.fds[j] != null; ++j) {
                    ;
                }

                if (j == 64) {
                    return -1;
                } else {
                    this.fds[j] = this.fds[i].dup();
                    return j;
                }
            }
        } else {
            return -1;
        }
    }

    Runtime.FD hostFSOpen(final File file, final int i, int j, final Object object) throws Runtime.ErrnoException {
        if ((i & -3596) != 0) {
            System.err.println("WARNING: Unsupported flags passed to open(\"" + file + "\"): " + toHex(i & -3596));
            throw new Runtime.ErrnoException(134);
        } else {
            boolean flag = (i & 3) != 0;

            if (this.sm != null) {
                label62: {
                    if (flag) {
                        if (this.sm.allowWrite(file)) {
                            break label62;
                        }
                    } else if (this.sm.allowRead(file)) {
                        break label62;
                    }

                    throw new Runtime.ErrnoException(13);
                }
            }

            if ((i & 2560) == 2560) {
                try {
                    if (!Platform.atomicCreateFile(file)) {
                        throw new Runtime.ErrnoException(17);
                    }
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            } else if (!file.exists()) {
                if ((i & 512) == 0) {
                    return null;
                }
            } else if (file.isDirectory()) {
                return this.hostFSDirFD(file, object);
            }

            final Seekable.File seekable_file;

            try {
                seekable_file = new Seekable.File(file, flag, (i & 1024) != 0);
            } catch (FileNotFoundException filenotfoundexception) {
                if (filenotfoundexception.getMessage() != null && filenotfoundexception.getMessage().indexOf("Permission denied") >= 0) {
                    throw new Runtime.ErrnoException(13);
                }

                return null;
            } catch (IOException ioexception1) {
                throw new Runtime.ErrnoException(5);
            }

            return new Runtime.SeekableFD(seekable_file, i) {
                protected Runtime.FStat _fstat() {
                    return Runtime.this.hostFStat(file, seekable_file, object);
                }
            };
        }
    }

    Runtime.FStat hostFStat(File file, Seekable.File seekable_file, Object object) {
        return new Runtime.HostFStat(file, seekable_file);
    }

    Runtime.FD hostFSDirFD(File file, Object object) {
        return null;
    }

    Runtime.FD _open(String s, int i, int j) throws Runtime.ErrnoException {
        return this.hostFSOpen(new File(s), i, j, (Object) null);
    }

    private int sys_open(int i, int j, int k) throws Runtime.ErrnoException, Runtime.FaultException {
        String s = this.cstring(i);

        if (s.length() == 1024 && this.getClass().getName().equals("tests.TeX")) {
            s = s.trim();
        }

        j &= -32769;
        Runtime.FD runtime_fd = this._open(s, j, k);

        if (runtime_fd == null) {
            return -2;
        } else {
            int l = this.addFD(runtime_fd);

            if (l == -1) {
                runtime_fd.close();
                return -23;
            } else {
                return l;
            }
        }
    }

    private int sys_write(int i, int j, int k) throws Runtime.FaultException, Runtime.ErrnoException {
        k = Math.min(k, 16776192);
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                byte[] abyte = this.byteBuf(k);

                this.copyin(j, abyte, k);

                try {
                    return this.fds[i].write(abyte, 0, k);
                } catch (Runtime.ErrnoException runtime_errnoexception) {
                    if (runtime_errnoexception.errno == 32) {
                        this.sys_exit(141);
                    }

                    throw runtime_errnoexception;
                }
            }
        } else {
            return -81;
        }
    }

    private int sys_read(int i, int j, int k) throws Runtime.FaultException, Runtime.ErrnoException {
        k = Math.min(k, 16776192);
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                byte[] abyte = this.byteBuf(k);
                int l = this.fds[i].read(abyte, 0, k);

                this.copyout(abyte, j, l);
                return l;
            }
        } else {
            return -81;
        }
    }

    private int sys_ftruncate(int i, long j) {
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                Seekable seekable = this.fds[i].seekable();

                if (j >= 0L && seekable != null) {
                    try {
                        seekable.resize(j);
                        return 0;
                    } catch (IOException ioexception) {
                        return -5;
                    }
                } else {
                    return -22;
                }
            }
        } else {
            return -81;
        }
    }

    private int sys_close(int i) {
        return this.closeFD(i) ? 0 : -81;
    }

    private int sys_lseek(int i, int j, int k) throws Runtime.ErrnoException {
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else if (k != 0 && k != 1 && k != 2) {
                return -22;
            } else {
                int l = this.fds[i].seek(j, k);

                return l < 0 ? -29 : l;
            }
        } else {
            return -81;
        }
    }

    int stat(Runtime.FStat runtime_fstat, int i) throws Runtime.FaultException {
        this.memWrite(i + 0, runtime_fstat.dev() << 16 | runtime_fstat.inode() & '\uffff');
        this.memWrite(i + 4, runtime_fstat.type() & '\uf000' | runtime_fstat.mode() & 4095);
        this.memWrite(i + 8, runtime_fstat.nlink() << 16 | runtime_fstat.uid() & '\uffff');
        this.memWrite(i + 12, runtime_fstat.gid() << 16 | 0);
        this.memWrite(i + 16, runtime_fstat.size());
        this.memWrite(i + 20, runtime_fstat.atime());
        this.memWrite(i + 28, runtime_fstat.mtime());
        this.memWrite(i + 36, runtime_fstat.ctime());
        this.memWrite(i + 44, runtime_fstat.blksize());
        this.memWrite(i + 48, runtime_fstat.blocks());
        return 0;
    }

    private int sys_fstat(int i, int j) throws Runtime.FaultException {
        return i >= 0 && i < 64 ? (this.fds[i] == null ? -81 : this.stat(this.fds[i].fstat(), j)) : -81;
    }

    private int sys_gettimeofday(int i, int j) throws Runtime.FaultException {
        long k = System.currentTimeMillis();
        int l = (int) (k / 1000L);
        int i1 = (int) (k % 1000L * 1000L);

        this.memWrite(i + 0, l);
        this.memWrite(i + 4, i1);
        return 0;
    }

    private int sys_sleep(int i) {
        if (i < 0) {
            i = Integer.MAX_VALUE;
        }

        try {
            Thread.sleep((long) i * 1000L);
            return 0;
        } catch (InterruptedException interruptedexception) {
            return -1;
        }
    }

    private int sys_times(int i) {
        long j = System.currentTimeMillis();
        int k = (int) ((j - this.startTime) / 16L);
        int l = (int) ((j - this.startTime) / 16L);

        try {
            if (i != 0) {
                this.memWrite(i + 0, k);
                this.memWrite(i + 4, l);
                this.memWrite(i + 8, k);
                this.memWrite(i + 12, l);
            }
        } catch (Runtime.FaultException runtime_faultexception) {
            return -14;
        }

        return (int) j;
    }

    private int sys_sysconf(int i) {
        switch (i) {
        case 2:
            return 1000;

        case 8:
            return this.writePages.length == 1 ? 4096 : 1 << this.pageShift;

        case 11:
            return this.writePages.length == 1 ? (1 << this.pageShift) / 4096 : this.writePages.length;

        default:
            System.err.println("WARNING: Attempted to use unknown sysconf key: " + i);
            return -22;
        }
    }

    public final int sbrk(int i) {
        if (i < 0) {
            return -12;
        } else if (i == 0) {
            return this.heapEnd;
        } else {
            i = i + 3 & -4;
            int j = this.heapEnd;
            int k = j + i;

            if (k >= this.stackBottom) {
                return -12;
            } else {
                if (this.writePages.length > 1) {
                    int l = (1 << this.pageShift) - 1;
                    int i1 = 1 << this.pageShift >>> 2;
                    int j1 = j + l >>> this.pageShift;
                    int k1 = k + l >>> this.pageShift;

                    try {
                        for (int l1 = j1; l1 < k1; ++l1) {
                            this.readPages[l1] = this.writePages[l1] = new int[i1];
                        }
                    } catch (OutOfMemoryError outofmemoryerror) {
                        System.err.println("WARNING: Caught OOM Exception in sbrk: " + outofmemoryerror);
                        return -12;
                    }
                }

                this.heapEnd = k;
                return j;
            }
        }
    }

    private int sys_getpid() {
        return this.getPid();
    }

    int getPid() {
        return 1;
    }

    private int sys_calljava(int i, int j, int k, int l) {
        if (this.state != 0) {
            throw new IllegalStateException("wound up calling sys_calljava while not in RUNNING");
        } else if (this.callJavaCB != null) {
            this.state = 3;

            int i1;

            try {
                i1 = this.callJavaCB.call(i, j, k, l);
            } catch (RuntimeException runtimeexception) {
                System.err.println("Error while executing callJavaCB");
                runtimeexception.printStackTrace();
                i1 = 0;
            }

            this.state = 0;
            return i1;
        } else {
            System.err.println("WARNING: calljava syscall invoked without a calljava callback set");
            return 0;
        }
    }

    private int sys_pause() {
        this.state = 2;
        return 0;
    }

    private int sys_getpagesize() {
        return this.writePages.length == 1 ? 4096 : 1 << this.pageShift;
    }

    void _exited() {}

    void exit(int i, boolean flag) {
        if (flag && this.fds[2] != null) {
            try {
                byte[] abyte = getBytes("Process exited on signal " + (i - 128) + "\n");

                this.fds[2].write(abyte, 0, abyte.length);
            } catch (Runtime.ErrnoException runtime_errnoexception) {
                ;
            }
        }

        this.exitStatus = i;

        for (int j = 0; j < this.fds.length; ++j) {
            if (this.fds[j] != null) {
                this.closeFD(j);
            }
        }

        this.state = 4;
        this._exited();
    }

    private int sys_exit(int i) {
        this.exit(i, false);
        return 0;
    }

    final int sys_fcntl(int i, int j, int k) throws Runtime.FaultException {
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                Runtime.FD runtime_fd = this.fds[i];

                switch (j) {
                case 0:
                    if (k >= 0 && k < 64) {
                        int l;

                        for (l = k; l < 64 && this.fds[l] != null; ++l) {
                            ;
                        }

                        if (l == 64) {
                            return -24;
                        } else {
                            this.fds[l] = runtime_fd.dup();
                            return l;
                        }
                    } else {
                        return -22;
                    }

                case 1:
                    return this.closeOnExec[i] ? 1 : 0;

                case 2:
                    this.closeOnExec[i] = k != 0;
                    return 0;

                case 3:
                    return runtime_fd.flags();

                case 4:
                case 5:
                case 6:
                default:
                    System.err.println("WARNING: Unknown fcntl command: " + j);
                    return -88;

                case 7:
                case 8:
                    System.err.println("WARNING: file locking requires UnixRuntime");
                    return -88;
                }
            }
        } else {
            return -81;
        }
    }

    final int fsync(int i) {
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                Runtime.FD runtime_fd = this.fds[i];
                Seekable seekable = runtime_fd.seekable();

                if (seekable == null) {
                    return -22;
                } else {
                    try {
                        seekable.sync();
                        return 0;
                    } catch (IOException ioexception) {
                        return -5;
                    }
                }
            }
        } else {
            return -81;
        }
    }

    protected final int syscall(int i, int j, int k, int l, int i1, int j1, int k1) {
        try {
            int l1 = this._syscall(i, j, k, l, i1, j1, k1);

            return l1;
        } catch (Runtime.ErrnoException runtime_errnoexception) {
            return -runtime_errnoexception.errno;
        } catch (Runtime.FaultException runtime_faultexception) {
            return -14;
        } catch (RuntimeException runtimeexception) {
            runtimeexception.printStackTrace();
            throw new Error("Internal Error in _syscall()");
        }
    }

    int _syscall(int i, int j, int k, int l, int i1, int j1, int k1) throws Runtime.ErrnoException, Runtime.FaultException {
        switch (i) {
        case 0:
            return 0;

        case 1:
            return this.sys_exit(j);

        case 2:
            return this.sys_pause();

        case 3:
            return this.sys_open(j, k, l);

        case 4:
            return this.sys_close(j);

        case 5:
            return this.sys_read(j, k, l);

        case 6:
            return this.sys_write(j, k, l);

        case 7:
            return this.sbrk(j);

        case 8:
            return this.sys_fstat(j, k);

        case 9:
        case 20:
        case 21:
        case 28:
        case 30:
        case 32:
        case 33:
        case 34:
        case 35:
        case 36:
        case 39:
        case 40:
        case 41:
        case 42:
        case 43:
        case 45:
        case 46:
        case 47:
        case 48:
        case 49:
        case 50:
        case 51:
        case 52:
        case 53:
        case 54:
        case 55:
        case 56:
        case 57:
        case 58:
        case 59:
        case 60:
        case 61:
        case 62:
        case 63:
        case 64:
        case 65:
        case 66:
        case 67:
        case 72:
        case 73:
        case 74:
        case 75:
        case 76:
        case 77:
        case 78:
        case 79:
        case 80:
        case 81:
        case 82:
        case 83:
        case 84:
        case 85:
        case 86:
        case 87:
        case 88:
        case 89:
        case 90:
        default:
            System.err.println("Attempted to use unknown syscall: " + i);
            return -88;

        case 10:
            return this.sys_lseek(j, k, l);

        case 11:
        case 14:
        case 18:
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
        case 27:
            System.err.println("Attempted to use a UnixRuntime syscall in Runtime (" + i + ")");
            return -88;

        case 12:
            return this.sys_getpid();

        case 13:
            return this.sys_calljava(j, k, l, i1);

        case 15:
            return this.sys_gettimeofday(j, k);

        case 16:
            return this.sys_sleep(j);

        case 17:
            return this.sys_times(j);

        case 19:
            return this.sys_getpagesize();

        case 29:
            return this.sys_fcntl(j, k, l);

        case 31:
            return this.sys_sysconf(j);

        case 37:
            this.memcpy(j, k, l);
            return j;

        case 38:
            this.memset(j, k, l);
            return j;

        case 44:
            return this.sys_ftruncate(j, (long) k);

        case 68:
            return this.sys_getuid();

        case 69:
            return this.sys_getgid();

        case 70:
            return this.sys_geteuid();

        case 71:
            return this.sys_getegid();

        case 91:
            return this.fsync(j);
        }
    }

    private int sys_getuid() {
        return 0;
    }

    private int sys_geteuid() {
        return 0;
    }

    private int sys_getgid() {
        return 0;
    }

    private int sys_getegid() {
        return 0;
    }

    public int xmalloc(int i) {
        int j = this.malloc(i);

        if (j == 0) {
            throw new RuntimeException("malloc() failed");
        } else {
            return j;
        }
    }

    public int xrealloc(int i, int j) {
        int k = this.realloc(i, j);

        if (k == 0) {
            throw new RuntimeException("realloc() failed");
        } else {
            return k;
        }
    }

    public int realloc(int i, int j) {
        try {
            return this.call("realloc", i, j);
        } catch (Runtime.CallException runtime_callexception) {
            return 0;
        }
    }

    public int malloc(int i) {
        try {
            return this.call("malloc", i);
        } catch (Runtime.CallException runtime_callexception) {
            return 0;
        }
    }

    public void free(int i) {
        try {
            if (i != 0) {
                this.call("free", i);
            }
        } catch (Runtime.CallException runtime_callexception) {
            ;
        }

    }

    public int strdup(String s) {
        if (s == null) {
            s = "(null)";
        }

        byte[] abyte = getBytes(s);
        byte[] abyte1 = new byte[abyte.length + 1];

        System.arraycopy(abyte, 0, abyte1, 0, abyte.length);
        int i = this.malloc(abyte1.length);

        if (i == 0) {
            return 0;
        } else {
            try {
                this.copyout(abyte1, i, abyte1.length);
                return i;
            } catch (Runtime.FaultException runtime_faultexception) {
                this.free(i);
                return 0;
            }
        }
    }

    public final String utfstring(int i) throws Runtime.ReadFaultException {
        if (i == 0) {
            return null;
        } else {
            int j = i;

            for (int k = 1; k != 0; ++j) {
                k = this.memRead(j & -4);
                switch (j & 3) {
                case 0:
                    k = k >>> 24 & 255;
                    break;

                case 1:
                    k = k >>> 16 & 255;
                    break;

                case 2:
                    k = k >>> 8 & 255;
                    break;

                case 3:
                    k = k >>> 0 & 255;
                }
            }

            if (j > i) {
                --j;
            }

            byte[] abyte = new byte[j - i];

            this.copyin(i, abyte, abyte.length);

            try {
                return new String(abyte, "UTF-8");
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw new RuntimeException(unsupportedencodingexception);
            }
        }
    }

    public final String cstring(int i) throws Runtime.ReadFaultException {
        if (i == 0) {
            return null;
        } else {
            StringBuffer stringbuffer = new StringBuffer();

            while (true) {
                int j;

                label36:
                while (true) {
                    j = this.memRead(i & -4);
                    switch (i & 3) {
                    case 0:
                        if ((j >>> 24 & 255) == 0) {
                            return stringbuffer.toString();
                        }

                        stringbuffer.append((char) (j >>> 24 & 255));
                        ++i;

                    case 1:
                        if ((j >>> 16 & 255) == 0) {
                            return stringbuffer.toString();
                        }

                        stringbuffer.append((char) (j >>> 16 & 255));
                        ++i;

                    case 2:
                        if ((j >>> 8 & 255) == 0) {
                            return stringbuffer.toString();
                        }

                        stringbuffer.append((char) (j >>> 8 & 255));
                        ++i;

                    case 3:
                        break label36;
                    }
                }

                if ((j >>> 0 & 255) == 0) {
                    return stringbuffer.toString();
                }

                stringbuffer.append((char) (j >>> 0 & 255));
                ++i;
            }
        }
    }

    protected final void nullPointerCheck(int i) throws Runtime.ExecutionException {
        if (i < 65536) {
            throw new Runtime.ExecutionException("Attempted to dereference a null pointer " + toHex(i));
        }
    }

    byte[] byteBuf(int i) {
        if (this._byteBuf == null) {
            this._byteBuf = new byte[i];
        } else if (this._byteBuf.length < i) {
            this._byteBuf = new byte[min(max(this._byteBuf.length * 2, i), 16776192)];
        }

        return this._byteBuf;
    }

    protected static final int[] decodeData(String s, int i) {
        if (s.length() % 8 != 0) {
            throw new IllegalArgumentException("string length must be a multiple of 8");
        } else if (s.length() / 8 * 7 < i * 4) {
            throw new IllegalArgumentException("string isn\'t big enough");
        } else {
            int[] aint = new int[i];
            int j = 0;
            int k = 0;
            int l = 0;

            for (int i1 = 0; i1 < i; l += 8) {
                long j1 = 0L;

                for (int k1 = 0; k1 < 8; ++k1) {
                    j1 <<= 7;
                    j1 |= (long) (s.charAt(l + k1) & 127);
                }

                if (k > 0) {
                    aint[i1++] = j | (int) (j1 >>> 56 - k);
                }

                if (i1 < i) {
                    aint[i1++] = (int) (j1 >>> 24 - k);
                }

                k = k + 8 & 31;
                j = (int) (j1 << k);
            }

            return aint;
        }
    }

    static byte[] getBytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            return null;
        }
    }

    static byte[] getNullTerminatedBytes(String s) {
        byte[] abyte = getBytes(s);
        byte[] abyte1 = new byte[abyte.length + 1];

        System.arraycopy(abyte, 0, abyte1, 0, abyte.length);
        return abyte1;
    }

    static final String toHex(int i) {
        return "0x" + Long.toString((long) i & 4294967295L, 16);
    }

    static final int min(int i, int j) {
        return i < j ? i : j;
    }

    static final int max(int i, int j) {
        return i > j ? i : j;
    }

    static {
        String s = Platform.getProperty("os.name");
        String s1 = Platform.getProperty("nestedvm.win32hacks");

        if (s1 != null) {
            win32Hacks = Boolean.valueOf(s1).booleanValue();
        } else {
            win32Hacks = s != null && s.toLowerCase().indexOf("windows") != -1;
        }

    }

    public static class SecurityManager {

        public boolean allowRead(File file) {
            return true;
        }

        public boolean allowWrite(File file) {
            return true;
        }

        public boolean allowStat(File file) {
            return true;
        }

        public boolean allowUnlink(File file) {
            return true;
        }
    }

    protected static class CPUState {

        public int[] r = new int[32];
        public int[] f = new int[32];
        public int hi;
        public int lo;
        public int fcsr;
        public int pc;

        public Runtime.CPUState dup() {
            Runtime.CPUState runtime_cpustate = new Runtime.CPUState();

            runtime_cpustate.hi = this.hi;
            runtime_cpustate.lo = this.lo;
            runtime_cpustate.fcsr = this.fcsr;
            runtime_cpustate.pc = this.pc;

            for (int i = 0; i < 32; ++i) {
                runtime_cpustate.r[i] = this.r[i];
                runtime_cpustate.f[i] = this.f[i];
            }

            return runtime_cpustate;
        }
    }

    protected static class ErrnoException extends Exception {

        public int errno;

        public ErrnoException(int i) {
            super("Errno: " + i);
            this.errno = i;
        }
    }

    public static class CallException extends Exception {

        public CallException(String s) {
            super(s);
        }
    }

    public static class ExecutionException extends Exception {

        private String message = "(null)";
        private String location = "(unknown)";

        public ExecutionException() {}

        public ExecutionException(String s) {
            if (s != null) {
                this.message = s;
            }

        }

        void setLocation(String s) {
            this.location = s == null ? "(unknown)" : s;
        }

        public final String getMessage() {
            return this.message + " at " + this.location;
        }
    }

    public static class FaultException extends Runtime.ExecutionException {

        public final int addr;
        public final RuntimeException cause;

        public FaultException(int i) {
            super("fault at: " + Runtime.toHex(i));
            this.addr = i;
            this.cause = null;
        }

        public FaultException(RuntimeException runtimeexception) {
            super(runtimeexception.toString());
            this.addr = -1;
            this.cause = runtimeexception;
        }
    }

    public static class WriteFaultException extends Runtime.FaultException {

        public WriteFaultException(int i) {
            super(i);
        }
    }

    public static class ReadFaultException extends Runtime.FaultException {

        public ReadFaultException(int i) {
            super(i);
        }
    }

    static class HostFStat extends Runtime.FStat {

        private final File f;
        private final Seekable.File sf;
        private final boolean executable;

        public HostFStat(File file, Seekable.File seekable_file) {
            this(file, seekable_file, false);
        }

        public HostFStat(File file, boolean flag) {
            this(file, (Seekable.File) null, flag);
        }

        public HostFStat(File file, Seekable.File seekable_file, boolean flag) {
            this.f = file;
            this.sf = seekable_file;
            this.executable = flag;
        }

        public int dev() {
            return 1;
        }

        public int inode() {
            return this.f.getAbsolutePath().hashCode() & 32767;
        }

        public int type() {
            return this.f.isDirectory() ? 16384 : '耀';
        }

        public int nlink() {
            return 1;
        }

        public int mode() {
            int i = 0;
            boolean flag = this.f.canRead();

            if (flag && (this.executable || this.f.isDirectory())) {
                i |= 73;
            }

            if (flag) {
                i |= 292;
            }

            if (this.f.canWrite()) {
                i |= 146;
            }

            return i;
        }

        public int size() {
            try {
                return this.sf != null ? this.sf.length() : (int) this.f.length();
            } catch (Exception exception) {
                return (int) this.f.length();
            }
        }

        public int mtime() {
            return (int) (this.f.lastModified() / 1000L);
        }
    }

    public static class SocketFStat extends Runtime.FStat {

        public int dev() {
            return -1;
        }

        public int type() {
            return '쀀';
        }

        public int inode() {
            return this.hashCode() & 32767;
        }
    }

    public abstract static class FStat {

        public static final int S_IFIFO = 4096;
        public static final int S_IFCHR = 8192;
        public static final int S_IFDIR = 16384;
        public static final int S_IFREG = 32768;
        public static final int S_IFSOCK = 49152;

        public int mode() {
            return 0;
        }

        public int nlink() {
            return 0;
        }

        public int uid() {
            return 0;
        }

        public int gid() {
            return 0;
        }

        public int size() {
            return 0;
        }

        public int atime() {
            return 0;
        }

        public int mtime() {
            return 0;
        }

        public int ctime() {
            return 0;
        }

        public int blksize() {
            return 512;
        }

        public int blocks() {
            return (this.size() + this.blksize() - 1) / this.blksize();
        }

        public abstract int dev();

        public abstract int type();

        public abstract int inode();
    }

    static class Win32ConsoleIS extends InputStream {

        private int pushedBack = -1;
        private final InputStream parent;

        public Win32ConsoleIS(InputStream inputstream) {
            this.parent = inputstream;
        }

        public int read() throws IOException {
            int i;

            if (this.pushedBack != -1) {
                i = this.pushedBack;
                this.pushedBack = -1;
                return i;
            } else {
                i = this.parent.read();
                if (i == 13 && (i = this.parent.read()) != 10) {
                    this.pushedBack = i;
                    return 13;
                } else {
                    return i;
                }
            }
        }

        public int read(byte[] abyte, int i, int j) throws IOException {
            boolean flag = false;

            if (this.pushedBack != -1 && j > 0) {
                abyte[0] = (byte) this.pushedBack;
                this.pushedBack = -1;
                ++i;
                --j;
                flag = true;
            }

            int k = this.parent.read(abyte, i, j);

            if (k == -1) {
                return flag ? 1 : -1;
            } else {
                for (int l = 0; l < k; ++l) {
                    if (abyte[i + l] == 13) {
                        if (l == k - 1) {
                            int i1 = this.parent.read();

                            if (i1 == 10) {
                                abyte[i + l] = 10;
                            } else {
                                this.pushedBack = i1;
                            }
                        } else if (abyte[i + l + 1] == 10) {
                            System.arraycopy(abyte, i + l + 1, abyte, i + l, j - l - 1);
                            --k;
                        }
                    }
                }

                return k + (flag ? 1 : 0);
            }
        }
    }

    static class TerminalFD extends Runtime.InputOutputStreamFD {

        public TerminalFD(InputStream inputstream) {
            this(inputstream, (OutputStream) null);
        }

        public TerminalFD(OutputStream outputstream) {
            this((InputStream) null, outputstream);
        }

        public TerminalFD(InputStream inputstream, OutputStream outputstream) {
            super(inputstream, outputstream);
        }

        public void _close() {}

        public Runtime.FStat _fstat() {
            return new Runtime.SocketFStat() {
                public int type() {
                    return 8192;
                }

                public int mode() {
                    return 384;
                }
            };
        }
    }

    public static class InputOutputStreamFD extends Runtime.FD {

        private final InputStream is;
        private final OutputStream os;

        public InputOutputStreamFD(InputStream inputstream) {
            this(inputstream, (OutputStream) null);
        }

        public InputOutputStreamFD(OutputStream outputstream) {
            this((InputStream) null, outputstream);
        }

        public InputOutputStreamFD(InputStream inputstream, OutputStream outputstream) {
            this.is = inputstream;
            this.os = outputstream;
            if (inputstream == null && outputstream == null) {
                throw new IllegalArgumentException("at least one stream must be supplied");
            }
        }

        public int flags() {
            if (this.is != null && this.os != null) {
                return 2;
            } else if (this.is != null) {
                return 0;
            } else if (this.os != null) {
                return 1;
            } else {
                throw new Error("should never happen");
            }
        }

        public void _close() {
            if (this.is != null) {
                try {
                    this.is.close();
                } catch (IOException ioexception) {
                    ;
                }
            }

            if (this.os != null) {
                try {
                    this.os.close();
                } catch (IOException ioexception1) {
                    ;
                }
            }

        }

        public int read(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            if (this.is == null) {
                return super.read(abyte, i, j);
            } else {
                try {
                    int k = this.is.read(abyte, i, j);

                    return k < 0 ? 0 : k;
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        public int write(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            if (this.os == null) {
                return super.write(abyte, i, j);
            } else {
                try {
                    this.os.write(abyte, i, j);
                    return j;
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        public Runtime.FStat _fstat() {
            return new Runtime.SocketFStat();
        }
    }

    public abstract static class SeekableFD extends Runtime.FD {

        private final int flags;
        private final Seekable data;

        SeekableFD(Seekable seekable, int i) {
            this.data = seekable;
            this.flags = i;
        }

        protected abstract Runtime.FStat _fstat();

        public int flags() {
            return this.flags;
        }

        Seekable seekable() {
            return this.data;
        }

        public int seek(int i, int j) throws Runtime.ErrnoException {
            try {
                switch (j) {
                case 0:
                    break;

                case 1:
                    i += this.data.pos();
                    break;

                case 2:
                    i += this.data.length();
                    break;

                default:
                    return -1;
                }

                this.data.seek(i);
                return i;
            } catch (IOException ioexception) {
                throw new Runtime.ErrnoException(29);
            }
        }

        public int write(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            if ((this.flags & 3) == 0) {
                throw new Runtime.ErrnoException(81);
            } else {
                if ((this.flags & 8) != 0) {
                    this.seek(0, 2);
                }

                try {
                    return this.data.write(abyte, i, j);
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        public int read(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            if ((this.flags & 3) == 1) {
                throw new Runtime.ErrnoException(81);
            } else {
                try {
                    int k = this.data.read(abyte, i, j);

                    return k < 0 ? 0 : k;
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        protected void _close() {
            try {
                this.data.close();
            } catch (IOException ioexception) {
                ;
            }

        }
    }

    public abstract static class FD {

        private int refCount = 1;
        private String normalizedPath = null;
        private boolean deleteOnClose = false;
        private Runtime.FStat cachedFStat = null;

        public void setNormalizedPath(String s) {
            this.normalizedPath = s;
        }

        public String getNormalizedPath() {
            return this.normalizedPath;
        }

        public void markDeleteOnClose() {
            this.deleteOnClose = true;
        }

        public boolean isMarkedForDeleteOnClose() {
            return this.deleteOnClose;
        }

        public int read(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            throw new Runtime.ErrnoException(81);
        }

        public int write(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            throw new Runtime.ErrnoException(81);
        }

        public int seek(int i, int j) throws Runtime.ErrnoException {
            return -1;
        }

        public int getdents(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            throw new Runtime.ErrnoException(81);
        }

        Seekable seekable() {
            return null;
        }

        public final Runtime.FStat fstat() {
            if (this.cachedFStat == null) {
                this.cachedFStat = this._fstat();
            }

            return this.cachedFStat;
        }

        protected abstract Runtime.FStat _fstat();

        public abstract int flags();

        public final void close() {
            if (--this.refCount == 0) {
                this._close();
            }

        }

        protected void _close() {}

        Runtime.FD dup() {
            ++this.refCount;
            return this;
        }
    }

    public interface CallJavaCB {

        int call(int i, int j, int k, int l);
    }
}
