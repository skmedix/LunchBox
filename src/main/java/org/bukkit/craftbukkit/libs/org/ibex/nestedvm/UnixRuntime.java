package org.bukkit.craftbukkit.libs.org.ibex.nestedvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.InodeCache;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Platform;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Sort;

public abstract class UnixRuntime extends Runtime implements Cloneable {

    private int pid;
    private UnixRuntime parent;
    private static final UnixRuntime.GlobalState defaultGS = new UnixRuntime.GlobalState();
    private UnixRuntime.GlobalState gs;
    private String cwd;
    private UnixRuntime execedRuntime;
    private Object children;
    private Vector activeChildren;
    private Vector exitedChildren;
    private static final Method runtimeCompilerCompile;
    static Class class$org$ibex$nestedvm$util$Seekable;
    static Class class$java$lang$String;

    public final int getPid() {
        return this.pid;
    }

    public void setGlobalState(UnixRuntime.GlobalState unixruntime_globalstate) {
        if (this.state != 1) {
            throw new IllegalStateException("can\'t change GlobalState when running");
        } else if (unixruntime_globalstate == null) {
            throw new NullPointerException("gs is null");
        } else {
            this.gs = unixruntime_globalstate;
        }
    }

    protected UnixRuntime(int i, int j) {
        this(i, j, false);
    }

    protected UnixRuntime(int i, int j, boolean flag) {
        super(i, j, flag);
        if (!flag) {
            this.gs = UnixRuntime.defaultGS;
            String s = Platform.getProperty("user.dir");

            this.cwd = s == null ? null : this.gs.mapHostPath(s);
            if (this.cwd == null) {
                this.cwd = "/";
            }

            this.cwd = this.cwd.substring(1);
        }

    }

    private static String posixTZ() {
        StringBuffer stringbuffer = new StringBuffer();
        TimeZone timezone = TimeZone.getDefault();
        int i = timezone.getRawOffset() / 1000;

        stringbuffer.append(Platform.timeZoneGetDisplayName(timezone, false, false));
        if (i > 0) {
            stringbuffer.append("-");
        } else {
            i = -i;
        }

        stringbuffer.append(i / 3600);
        i %= 3600;
        if (i > 0) {
            stringbuffer.append(":").append(i / 60);
        }

        i %= 60;
        if (i > 0) {
            stringbuffer.append(":").append(i);
        }

        if (timezone.useDaylightTime()) {
            stringbuffer.append(Platform.timeZoneGetDisplayName(timezone, true, false));
        }

        return stringbuffer.toString();
    }

    private static boolean envHas(String s, String[] astring) {
        for (int i = 0; i < astring.length; ++i) {
            if (astring[i] != null && astring[i].startsWith(s + "=")) {
                return true;
            }
        }

        return false;
    }

    String[] createEnv(String[] astring) {
        String[] astring1 = new String[7];
        int i = 0;

        if (astring == null) {
            astring = new String[0];
        }

        if (!envHas("USER", astring) && Platform.getProperty("user.name") != null) {
            astring1[i++] = "USER=" + Platform.getProperty("user.name");
        }

        String s;

        if (!envHas("HOME", astring) && (s = Platform.getProperty("user.home")) != null && (s = this.gs.mapHostPath(s)) != null) {
            astring1[i++] = "HOME=" + s;
        }

        if (!envHas("TMPDIR", astring) && (s = Platform.getProperty("java.io.tmpdir")) != null && (s = this.gs.mapHostPath(s)) != null) {
            astring1[i++] = "TMPDIR=" + s;
        }

        if (!envHas("SHELL", astring)) {
            astring1[i++] = "SHELL=/bin/sh";
        }

        if (!envHas("TERM", astring) && !UnixRuntime.win32Hacks) {
            astring1[i++] = "TERM=vt100";
        }

        if (!envHas("TZ", astring)) {
            astring1[i++] = "TZ=" + posixTZ();
        }

        if (!envHas("PATH", astring)) {
            astring1[i++] = "PATH=/usr/local/bin:/usr/bin:/bin:/usr/local/sbin:/usr/sbin:/sbin";
        }

        String[] astring2 = new String[astring.length + i];

        int j;

        for (j = 0; j < i; ++j) {
            astring2[j] = astring1[j];
        }

        for (j = 0; j < astring.length; ++j) {
            astring2[i++] = astring[j];
        }

        return astring2;
    }

    void _started() {
        UnixRuntime[] aunixruntime = this.gs.tasks;
        UnixRuntime.GlobalState unixruntime_globalstate = this.gs;

        synchronized (this.gs) {
            int i;

            if (this.pid != 0) {
                UnixRuntime unixruntime = aunixruntime[this.pid];

                if (unixruntime == null || unixruntime == this || unixruntime.pid != this.pid || unixruntime.parent != this.parent) {
                    throw new Error("should never happen");
                }

                Object object = this.parent.children;

                synchronized (this.parent.children) {
                    i = this.parent.activeChildren.indexOf(unixruntime);
                    if (i == -1) {
                        throw new Error("should never happen");
                    }

                    this.parent.activeChildren.setElementAt(this, i);
                }
            } else {
                int j = -1;
                int k = this.gs.nextPID;

                for (i = k; i < aunixruntime.length; ++i) {
                    if (aunixruntime[i] == null) {
                        j = i;
                        break;
                    }
                }

                if (j == -1) {
                    for (i = 1; i < k; ++i) {
                        if (aunixruntime[i] == null) {
                            j = i;
                            break;
                        }
                    }
                }

                if (j == -1) {
                    throw new UnixRuntime.ProcessTableFullExn(null);
                }

                this.pid = j;
                this.gs.nextPID = j + 1;
            }

            aunixruntime[this.pid] = this;
        }
    }

    int _syscall(int i, int j, int k, int l, int i1, int j1, int k1) throws Runtime.ErrnoException, Runtime.FaultException {
        switch (i) {
        case 11:
            return this.sys_kill(j, k);

        case 12:
        case 13:
        case 15:
        case 16:
        case 17:
        case 19:
        case 21:
        case 30:
        case 31:
        case 32:
        case 34:
        case 35:
        case 37:
        case 38:
        case 40:
        case 41:
        case 42:
        case 44:
        case 45:
        case 47:
        case 48:
        case 49:
        case 50:
        case 51:
        case 54:
        case 55:
        case 68:
        case 69:
        case 70:
        case 71:
        case 72:
        default:
            return super._syscall(i, j, k, l, i1, j1, k1);

        case 14:
            return this.sys_stat(j, k);

        case 18:
            return this.sys_mkdir(j, k);

        case 20:
            return this.sys_unlink(j);

        case 22:
            return this.sys_chdir(j);

        case 23:
            return this.sys_pipe(j);

        case 24:
            return this.sys_dup2(j, k);

        case 25:
            return this.sys_fork();

        case 26:
            return this.sys_waitpid(j, k, l);

        case 27:
            return this.sys_getcwd(j, k);

        case 28:
            return this.sys_exec(j, k, l);

        case 29:
            return this.sys_fcntl_lock(j, k, l);

        case 33:
            return this.sys_lstat(j, k);

        case 36:
            return this.sys_getdents(j, k, l, i1);

        case 39:
            return this.sys_dup(j);

        case 43:
            return this.sys_chown(j, k, l);

        case 46:
            return this.sys_getppid();

        case 52:
            return this.sys_realpath(j, k);

        case 53:
            return this.sys_sysctl(j, k, l, i1, j1, k1);

        case 56:
            return this.sys_socket(j, k, l);

        case 57:
            return this.sys_connect(j, k, l);

        case 58:
            return this.sys_resolve_hostname(j, k, l);

        case 59:
            return this.sys_accept(j, k, l);

        case 60:
            return this.sys_setsockopt(j, k, l, i1, j1);

        case 61:
            return this.sys_getsockopt(j, k, l, i1, j1);

        case 62:
            return this.sys_listen(j, k);

        case 63:
            return this.sys_bind(j, k, l);

        case 64:
            return this.sys_shutdown(j, k);

        case 65:
            return this.sys_sendto(j, k, l, i1, j1, k1);

        case 66:
            return this.sys_recvfrom(j, k, l, i1, j1, k1);

        case 67:
            return this.sys_select(j, k, l, i1, j1);

        case 73:
            return this.sys_umask(j);

        case 74:
            return this.sys_chmod(j, k, l);

        case 75:
            return this.sys_fchmod(j, k, l);

        case 76:
            return this.sys_chown(j, k, l);

        case 77:
            return this.sys_fchown(j, k, l);

        case 78:
            return this.sys_access(j, k);
        }
    }

    Runtime.FD _open(String s, int i, int j) throws Runtime.ErrnoException {
        s = this.normalizePath(s);
        Runtime.FD runtime_fd = this.gs.open(this, s, i, j);

        if (runtime_fd != null && s != null) {
            runtime_fd.setNormalizedPath(s);
        }

        return runtime_fd;
    }

    private int sys_getppid() {
        return this.parent == null ? 1 : this.parent.pid;
    }

    private int sys_chown(int i, int j, int k) {
        return 0;
    }

    private int sys_lchown(int i, int j, int k) {
        return 0;
    }

    private int sys_fchown(int i, int j, int k) {
        return 0;
    }

    private int sys_chmod(int i, int j, int k) {
        return 0;
    }

    private int sys_fchmod(int i, int j, int k) {
        return 0;
    }

    private int sys_umask(int i) {
        return 0;
    }

    private int sys_access(int i, int j) throws Runtime.ErrnoException, Runtime.ReadFaultException {
        return this.gs.stat(this, this.cstring(i)) == null ? -2 : 0;
    }

    private int sys_realpath(int i, int j) throws Runtime.FaultException {
        String s = this.normalizePath(this.cstring(i));
        byte[] abyte = getNullTerminatedBytes(s);

        if (abyte.length > 1024) {
            return -34;
        } else {
            this.copyout(abyte, j, abyte.length);
            return 0;
        }
    }

    private int sys_kill(int i, int j) {
        if (i != i) {
            return -3;
        } else if (j >= 0 && j < 32) {
            switch (j) {
            case 0:
                return 0;

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
                this.exit(128 + j, true);

            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 28:
                return 0;
            }
        } else {
            return -22;
        }
    }

    private int sys_waitpid(int i, int j, int k) throws Runtime.FaultException, Runtime.ErrnoException {
        if ((k & -2) != 0) {
            return -22;
        } else if (i != 0 && i >= -1) {
            boolean flag = (k & 1) == 0;

            if (i != -1 && (i <= 0 || i >= this.gs.tasks.length)) {
                return -10;
            } else if (this.children == null) {
                return flag ? -10 : 0;
            } else {
                UnixRuntime unixruntime = null;
                Object object = this.children;

                synchronized (this.children) {
                    while (true) {
                        if (i == -1) {
                            if (this.exitedChildren.size() > 0) {
                                unixruntime = (UnixRuntime) this.exitedChildren.elementAt(this.exitedChildren.size() - 1);
                                this.exitedChildren.removeElementAt(this.exitedChildren.size() - 1);
                            }
                        } else {
                            if (i <= 0) {
                                throw new Error("should never happen");
                            }

                            if (i >= this.gs.tasks.length) {
                                return -10;
                            }

                            UnixRuntime unixruntime1 = this.gs.tasks[i];

                            if (unixruntime1.parent != this) {
                                return -10;
                            }

                            if (unixruntime1.state == 4) {
                                if (!this.exitedChildren.removeElement(unixruntime1)) {
                                    throw new Error("should never happen");
                                }

                                unixruntime = unixruntime1;
                            }
                        }

                        if (unixruntime != null) {
                            this.gs.tasks[unixruntime.pid] = null;
                            break;
                        }

                        if (!flag) {
                            return 0;
                        }

                        try {
                            this.children.wait();
                        } catch (InterruptedException interruptedexception) {
                            ;
                        }
                    }
                }

                if (j != 0) {
                    this.memWrite(j, unixruntime.exitStatus() << 8);
                }

                return unixruntime.pid;
            }
        } else {
            System.err.println("WARNING: waitpid called with a pid of " + i);
            return -10;
        }
    }

    void _exited() {
        if (this.children != null) {
            Object object = this.children;

            synchronized (this.children) {
                Enumeration enumeration;
                UnixRuntime unixruntime;

                for (enumeration = this.exitedChildren.elements(); enumeration.hasMoreElements(); this.gs.tasks[unixruntime.pid] = null) {
                    unixruntime = (UnixRuntime) enumeration.nextElement();
                }

                this.exitedChildren.removeAllElements();

                for (enumeration = this.activeChildren.elements(); enumeration.hasMoreElements(); unixruntime.parent = null) {
                    unixruntime = (UnixRuntime) enumeration.nextElement();
                }

                this.activeChildren.removeAllElements();
            }
        }

        UnixRuntime unixruntime1 = this.parent;

        if (unixruntime1 == null) {
            this.gs.tasks[this.pid] = null;
        } else {
            Object object1 = unixruntime1.children;

            synchronized (unixruntime1.children) {
                if (this.parent == null) {
                    this.gs.tasks[this.pid] = null;
                } else {
                    if (!this.parent.activeChildren.removeElement(this)) {
                        throw new Error("should never happen _exited: pid: " + this.pid);
                    }

                    this.parent.exitedChildren.addElement(this);
                    this.parent.children.notify();
                }
            }
        }

    }

    protected Object clone() throws CloneNotSupportedException {
        UnixRuntime unixruntime = (UnixRuntime) super.clone();

        unixruntime.pid = 0;
        unixruntime.parent = null;
        unixruntime.children = null;
        unixruntime.activeChildren = unixruntime.exitedChildren = null;
        return unixruntime;
    }

    private int sys_fork() {
        UnixRuntime unixruntime;

        try {
            unixruntime = (UnixRuntime) this.clone();
        } catch (Exception exception) {
            exception.printStackTrace();
            return -12;
        }

        unixruntime.parent = this;

        try {
            unixruntime._started();
        } catch (UnixRuntime.ProcessTableFullExn unixruntime_processtablefullexn) {
            return -12;
        }

        if (this.children == null) {
            this.children = new Object();
            this.activeChildren = new Vector();
            this.exitedChildren = new Vector();
        }

        this.activeChildren.addElement(unixruntime);
        Runtime.CPUState runtime_cpustate = new Runtime.CPUState();

        this.getCPUState(runtime_cpustate);
        runtime_cpustate.r[2] = 0;
        runtime_cpustate.pc += 4;
        unixruntime.setCPUState(runtime_cpustate);
        unixruntime.state = 2;

        new UnixRuntime.ForkedProcess(unixruntime);
        return unixruntime.pid;
    }

    public static int runAndExec(UnixRuntime unixruntime, String s, String[] astring) {
        return runAndExec(unixruntime, concatArgv(s, astring));
    }

    public static int runAndExec(UnixRuntime unixruntime, String[] astring) {
        unixruntime.start(astring);
        return executeAndExec(unixruntime);
    }

    public static int executeAndExec(UnixRuntime unixruntime) {
        while (true) {
            if (unixruntime.execute()) {
                if (unixruntime.state != 5) {
                    return unixruntime.exitStatus();
                }

                unixruntime = unixruntime.execedRuntime;
            } else {
                System.err.println("WARNING: Pause requested while executing runAndExec()");
            }
        }
    }

    private String[] readStringArray(int i) throws Runtime.ReadFaultException {
        int j = 0;

        for (int k = i; this.memRead(k) != 0; k += 4) {
            ++j;
        }

        String[] astring = new String[j];
        int l = 0;

        for (int i1 = i; l < j; i1 += 4) {
            astring[l] = this.cstring(this.memRead(i1));
            ++l;
        }

        return astring;
    }

    private int sys_exec(int i, int j, int k) throws Runtime.ErrnoException, Runtime.FaultException {
        return this.exec(this.normalizePath(this.cstring(i)), this.readStringArray(j), this.readStringArray(k));
    }

    public Class runtimeCompile(Seekable seekable, String s) throws IOException {
        if (UnixRuntime.runtimeCompilerCompile == null) {
            System.err.println("WARNING: Exec attempted but RuntimeCompiler not found!");
            return null;
        } else {
            try {
                return (Class) UnixRuntime.runtimeCompilerCompile.invoke((Object) null, new Object[] { seekable, "unixruntime,maxinsnpermethod=256,lessconstants", s});
            } catch (IllegalAccessException illegalaccessexception) {
                illegalaccessexception.printStackTrace();
                return null;
            } catch (InvocationTargetException invocationtargetexception) {
                Throwable throwable = invocationtargetexception.getTargetException();

                if (throwable instanceof IOException) {
                    throw (IOException) throwable;
                } else if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else if (throwable instanceof Error) {
                    throw (Error) throwable;
                } else {
                    throwable.printStackTrace();
                    return null;
                }
            }
        }
    }

    private int exec(String s, String[] astring, String[] astring1) throws Runtime.ErrnoException {
        if (astring.length == 0) {
            astring = new String[] { ""};
        }

        if (s.equals("bin/busybox") && this.getClass().getName().endsWith("BusyBox")) {
            return this.execClass(this.getClass(), astring, astring1);
        } else {
            Runtime.FStat runtime_fstat = this.gs.stat(this, s);

            if (runtime_fstat == null) {
                return -2;
            } else {
                UnixRuntime.GlobalState.CacheEnt unixruntime_globalstate_cacheent = (UnixRuntime.GlobalState.CacheEnt) this.gs.execCache.get(s);
                long i = (long) runtime_fstat.mtime();
                long j = (long) runtime_fstat.size();

                if (unixruntime_globalstate_cacheent != null) {
                    if (unixruntime_globalstate_cacheent.time == i && unixruntime_globalstate_cacheent.size == j) {
                        if (unixruntime_globalstate_cacheent.o instanceof Class) {
                            return this.execClass((Class) unixruntime_globalstate_cacheent.o, astring, astring1);
                        }

                        if (unixruntime_globalstate_cacheent.o instanceof String[]) {
                            return this.execScript(s, (String[]) ((String[]) unixruntime_globalstate_cacheent.o), astring, astring1);
                        }

                        throw new Error("should never happen");
                    }

                    this.gs.execCache.remove(s);
                }

                Runtime.FD runtime_fd = this.gs.open(this, s, 0, 0);

                if (runtime_fd == null) {
                    throw new Runtime.ErrnoException(2);
                } else {
                    Seekable seekable = runtime_fd.seekable();

                    if (seekable == null) {
                        throw new Runtime.ErrnoException(13);
                    } else {
                        byte[] abyte = new byte[4096];

                        try {
                            byte b0;

                            try {
                                int k = seekable.read(abyte, 0, abyte.length);

                                if (k == -1) {
                                    throw new Runtime.ErrnoException(8);
                                } else {
                                    int l;

                                    switch (abyte[0]) {
                                    case 35:
                                        if (k == 1) {
                                            l = seekable.read(abyte, 1, abyte.length - 1);
                                            if (l == -1) {
                                                byte b1 = -8;

                                                return b1;
                                            }

                                            k += l;
                                        }

                                        if (abyte[1] != 33) {
                                            byte b2 = -8;

                                            return b2;
                                        } else {
                                            l = 2;
                                            k -= 2;

                                            int i1;

                                            label345:
                                            while (true) {
                                                for (i1 = l; i1 < l + k; ++i1) {
                                                    if (abyte[i1] == 10) {
                                                        l = i1;
                                                        break label345;
                                                    }
                                                }

                                                l += k;
                                                if (l == abyte.length) {
                                                    break;
                                                }

                                                k = seekable.read(abyte, l, abyte.length - l);
                                            }

                                            for (i1 = 2; i1 < l && abyte[i1] == 32; ++i1) {
                                                ;
                                            }

                                            if (i1 == l) {
                                                throw new Runtime.ErrnoException(8);
                                            }

                                            int j1;

                                            for (j1 = i1; j1 < l && abyte[j1] != 32; ++j1) {
                                                ;
                                            }

                                            int k1;

                                            for (k1 = j1; j1 < l && abyte[j1] == 32; ++j1) {
                                                ;
                                            }

                                            String[] astring2 = new String[] { new String(abyte, i1, k1 - i1), j1 < l ? new String(abyte, j1, l - j1) : null};

                                            this.gs.execCache.put(s, new UnixRuntime.GlobalState.CacheEnt(i, j, astring2));
                                            int l1 = this.execScript(s, astring2, astring, astring1);

                                            return l1;
                                        }

                                    case 127:
                                        if (k < 4) {
                                            seekable.tryReadFully(abyte, k, 4 - k);
                                        }

                                        if (abyte[1] == 69 && abyte[2] == 76 && abyte[3] == 70) {
                                            seekable.seek(0);
                                            System.err.println("Running RuntimeCompiler for " + s);
                                            Class oclass = this.runtimeCompile(seekable, s);

                                            System.err.println("RuntimeCompiler finished for " + s);
                                            if (oclass == null) {
                                                throw new Runtime.ErrnoException(8);
                                            }

                                            this.gs.execCache.put(s, new UnixRuntime.GlobalState.CacheEnt(i, j, oclass));
                                            l = this.execClass(oclass, astring, astring1);
                                            return l;
                                        }

                                        b0 = -8;
                                        return b0;

                                    default:
                                        byte b3 = -8;

                                        return b3;
                                    }
                                }
                            } catch (IOException ioexception) {
                                b0 = -5;
                                return b0;
                            }
                        } finally {
                            runtime_fd.close();
                        }
                    }
                }
            }
        }
    }

    public int execScript(String s, String[] astring, String[] astring1, String[] astring2) throws Runtime.ErrnoException {
        String[] astring3 = new String[astring1.length - 1 + (astring[1] != null ? 3 : 2)];
        int i = astring[0].lastIndexOf(47);

        astring3[0] = i == -1 ? astring[0] : astring[0].substring(i + 1);
        astring3[1] = "/" + s;
        i = 2;
        if (astring[1] != null) {
            astring3[i++] = astring[1];
        }

        int j;

        for (j = 1; j < astring1.length; ++j) {
            astring3[i++] = astring1[j];
        }

        if (i != astring3.length) {
            throw new Error("p != newArgv.length");
        } else {
            System.err.println("Execing: " + astring[0]);

            for (j = 0; j < astring3.length; ++j) {
                System.err.println("execing [" + j + "] " + astring3[j]);
            }

            return this.exec(astring[0], astring3, astring2);
        }
    }

    public int execClass(Class oclass, String[] astring, String[] astring1) {
        try {
            UnixRuntime unixruntime = (UnixRuntime) oclass.getDeclaredConstructor(new Class[] { Boolean.TYPE}).newInstance(new Object[] { Boolean.TRUE});

            return this.exec(unixruntime, astring, astring1);
        } catch (Exception exception) {
            exception.printStackTrace();
            return -8;
        }
    }

    private int exec(UnixRuntime unixruntime, String[] astring, String[] astring1) {
        for (int i = 0; i < 64; ++i) {
            if (this.closeOnExec[i]) {
                this.closeFD(i);
            }
        }

        unixruntime.fds = this.fds;
        unixruntime.closeOnExec = this.closeOnExec;
        this.fds = null;
        this.closeOnExec = null;
        unixruntime.gs = this.gs;
        unixruntime.sm = this.sm;
        unixruntime.cwd = this.cwd;
        unixruntime.pid = this.pid;
        unixruntime.parent = this.parent;
        unixruntime.start(astring, astring1);
        this.state = 5;
        this.execedRuntime = unixruntime;
        return 0;
    }

    private int sys_pipe(int i) {
        UnixRuntime.Pipe unixruntime_pipe = new UnixRuntime.Pipe();
        int j = this.addFD(unixruntime_pipe.reader);

        if (j < 0) {
            return -23;
        } else {
            int k = this.addFD(unixruntime_pipe.writer);

            if (k < 0) {
                this.closeFD(j);
                return -23;
            } else {
                try {
                    this.memWrite(i, j);
                    this.memWrite(i + 4, k);
                    return 0;
                } catch (Runtime.FaultException runtime_faultexception) {
                    this.closeFD(j);
                    this.closeFD(k);
                    return -14;
                }
            }
        }
    }

    private int sys_dup2(int i, int j) {
        if (i == j) {
            return 0;
        } else if (i >= 0 && i < 64) {
            if (j >= 0 && j < 64) {
                if (this.fds[i] == null) {
                    return -81;
                } else {
                    if (this.fds[j] != null) {
                        this.fds[j].close();
                    }

                    this.fds[j] = this.fds[i].dup();
                    return 0;
                }
            } else {
                return -81;
            }
        } else {
            return -81;
        }
    }

    private int sys_dup(int i) {
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                Runtime.FD runtime_fd = this.fds[i].dup();
                int j = this.addFD(runtime_fd);

                if (j < 0) {
                    runtime_fd.close();
                    return -23;
                } else {
                    return j;
                }
            }
        } else {
            return -81;
        }
    }

    private int sys_stat(int i, int j) throws Runtime.FaultException, Runtime.ErrnoException {
        Runtime.FStat runtime_fstat = this.gs.stat(this, this.normalizePath(this.cstring(i)));

        return runtime_fstat == null ? -2 : this.stat(runtime_fstat, j);
    }

    private int sys_lstat(int i, int j) throws Runtime.FaultException, Runtime.ErrnoException {
        Runtime.FStat runtime_fstat = this.gs.lstat(this, this.normalizePath(this.cstring(i)));

        return runtime_fstat == null ? -2 : this.stat(runtime_fstat, j);
    }

    private int sys_mkdir(int i, int j) throws Runtime.FaultException, Runtime.ErrnoException {
        this.gs.mkdir(this, this.normalizePath(this.cstring(i)), j);
        return 0;
    }

    private int sys_unlink(int i) throws Runtime.FaultException, Runtime.ErrnoException {
        this.gs.unlink(this, this.normalizePath(this.cstring(i)));
        return 0;
    }

    private int sys_getcwd(int i, int j) throws Runtime.FaultException, Runtime.ErrnoException {
        byte[] abyte = getBytes(this.cwd);

        if (j == 0) {
            return -22;
        } else if (j < abyte.length + 2) {
            return -34;
        } else {
            this.memset(i, 47, 1);
            this.copyout(abyte, i + 1, abyte.length);
            this.memset(i + abyte.length + 1, 0, 1);
            return i;
        }
    }

    private int sys_chdir(int i) throws Runtime.ErrnoException, Runtime.FaultException {
        String s = this.normalizePath(this.cstring(i));
        Runtime.FStat runtime_fstat = this.gs.stat(this, s);

        if (runtime_fstat == null) {
            return -2;
        } else if (runtime_fstat.type() != 16384) {
            return -20;
        } else {
            this.cwd = s;
            return 0;
        }
    }

    private int sys_getdents(int i, int j, int k, int l) throws Runtime.FaultException, Runtime.ErrnoException {
        k = Math.min(k, 16776192);
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                byte[] abyte = this.byteBuf(k);
                int i1 = this.fds[i].getdents(abyte, 0, k);

                this.copyout(abyte, j, i1);
                return i1;
            }
        } else {
            return -81;
        }
    }

    void _preCloseFD(Runtime.FD runtime_fd) {
        Seekable seekable = runtime_fd.seekable();

        if (seekable != null) {
            try {
                for (int i = 0; i < this.gs.locks.length; ++i) {
                    Seekable.Lock seekable_lock = this.gs.locks[i];

                    if (seekable_lock != null && seekable.equals(seekable_lock.seekable()) && seekable_lock.getOwner() == this) {
                        seekable_lock.release();
                        this.gs.locks[i] = null;
                    }
                }

            } catch (IOException ioexception) {
                throw new RuntimeException(ioexception);
            }
        }
    }

    void _postCloseFD(Runtime.FD runtime_fd) {
        if (runtime_fd.isMarkedForDeleteOnClose()) {
            try {
                this.gs.unlink(this, runtime_fd.getNormalizedPath());
            } catch (Throwable throwable) {
                ;
            }
        }

    }

    private int sys_fcntl_lock(int i, int j, int k) throws Runtime.FaultException {
        if (j != 7 && j != 8) {
            return this.sys_fcntl(i, j, k);
        } else if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                return -81;
            } else {
                Runtime.FD runtime_fd = this.fds[i];

                if (k == 0) {
                    return -22;
                } else {
                    int l = this.memRead(k);
                    int i1 = this.memRead(k + 4);
                    int j1 = this.memRead(k + 8);
                    int k1 = l >> 16;
                    int l1 = l & 255;
                    Seekable.Lock[] aseekable_lock = this.gs.locks;
                    Seekable seekable = runtime_fd.seekable();

                    if (seekable == null) {
                        return -22;
                    } else {
                        try {
                            switch (l1) {
                            case 0:
                                break;

                            case 1:
                                i1 += seekable.pos();
                                break;

                            case 2:
                                i1 += seekable.length();
                                break;

                            default:
                                return -1;
                            }

                            int i2;
                            Seekable.Lock seekable_lock;

                            if (j == 7) {
                                for (i2 = 0; i2 < aseekable_lock.length; ++i2) {
                                    if (aseekable_lock[i2] != null && seekable.equals(aseekable_lock[i2].seekable()) && aseekable_lock[i2].overlaps(i1, j1) && aseekable_lock[i2].getOwner() != this && (!aseekable_lock[i2].isShared() || k1 != 1)) {
                                        return 0;
                                    }
                                }

                                seekable_lock = seekable.lock((long) i1, (long) j1, k1 == 1);
                                if (seekable_lock != null) {
                                    this.memWrite(k, 196608);
                                    seekable_lock.release();
                                }

                                return 0;
                            } else if (j != 8) {
                                return -22;
                            } else {
                                int j2;

                                if (k1 == 3) {
                                    for (i2 = 0; i2 < aseekable_lock.length; ++i2) {
                                        if (aseekable_lock[i2] != null && seekable.equals(aseekable_lock[i2].seekable()) && aseekable_lock[i2].getOwner() == this) {
                                            j2 = (int) aseekable_lock[i2].position();
                                            if (j2 >= i1 && (i1 == 0 || j1 == 0 || (long) j2 + aseekable_lock[i2].size() <= (long) (i1 + j1))) {
                                                aseekable_lock[i2].release();
                                                aseekable_lock[i2] = null;
                                            }
                                        }
                                    }

                                    return 0;
                                } else if (k1 != 1 && k1 != 2) {
                                    return -22;
                                } else {
                                    for (i2 = 0; i2 < aseekable_lock.length; ++i2) {
                                        if (aseekable_lock[i2] != null && seekable.equals(aseekable_lock[i2].seekable())) {
                                            if (aseekable_lock[i2].getOwner() == this) {
                                                if (aseekable_lock[i2].contained(i1, j1)) {
                                                    aseekable_lock[i2].release();
                                                    aseekable_lock[i2] = null;
                                                } else if (aseekable_lock[i2].contains(i1, j1)) {
                                                    if (aseekable_lock[i2].isShared() == (k1 == 1)) {
                                                        this.memWrite(k + 4, (int) aseekable_lock[i2].position());
                                                        this.memWrite(k + 8, (int) aseekable_lock[i2].size());
                                                        return 0;
                                                    }

                                                    aseekable_lock[i2].release();
                                                    aseekable_lock[i2] = null;
                                                }
                                            } else if (aseekable_lock[i2].overlaps(i1, j1) && (!aseekable_lock[i2].isShared() || k1 == 2)) {
                                                return -11;
                                            }
                                        }
                                    }

                                    seekable_lock = seekable.lock((long) i1, (long) j1, k1 == 1);
                                    if (seekable_lock == null) {
                                        return -11;
                                    } else {
                                        seekable_lock.setOwner(this);

                                        for (j2 = 0; j2 < aseekable_lock.length && aseekable_lock[j2] != null; ++j2) {
                                            ;
                                        }

                                        if (j2 == aseekable_lock.length) {
                                            return -46;
                                        } else {
                                            aseekable_lock[j2] = seekable_lock;
                                            return 0;
                                        }
                                    }
                                }
                            }
                        } catch (IOException ioexception) {
                            throw new RuntimeException(ioexception);
                        }
                    }
                }
            }
        } else {
            return -81;
        }
    }

    private int sys_socket(int i, int j, int k) {
        return i == 2 && (j == 1 || j == 2) ? this.addFD(new UnixRuntime.SocketFD(j == 1 ? 0 : 1)) : -123;
    }

    private UnixRuntime.SocketFD getSocketFD(int i) throws Runtime.ErrnoException {
        if (i >= 0 && i < 64) {
            if (this.fds[i] == null) {
                throw new Runtime.ErrnoException(81);
            } else if (!(this.fds[i] instanceof UnixRuntime.SocketFD)) {
                throw new Runtime.ErrnoException(108);
            } else {
                return (UnixRuntime.SocketFD) this.fds[i];
            }
        } else {
            throw new Runtime.ErrnoException(81);
        }
    }

    private int sys_connect(int i, int j, int k) throws Runtime.ErrnoException, Runtime.FaultException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        if (unixruntime_socketfd.type() == 0 && (unixruntime_socketfd.s != null || unixruntime_socketfd.ss != null)) {
            return -127;
        } else {
            int l = this.memRead(j);

            if ((l >>> 16 & 255) != 2) {
                return -106;
            } else {
                int i1 = l & '\uffff';
                byte[] abyte = new byte[4];

                this.copyin(j + 4, abyte, 4);

                InetAddress inetaddress;

                try {
                    inetaddress = Platform.inetAddressFromBytes(abyte);
                } catch (UnknownHostException unknownhostexception) {
                    return -125;
                }

                unixruntime_socketfd.connectAddr = inetaddress;
                unixruntime_socketfd.connectPort = i1;

                try {
                    switch (unixruntime_socketfd.type()) {
                    case 0:
                        Socket socket = new Socket(inetaddress, i1);

                        unixruntime_socketfd.s = socket;
                        unixruntime_socketfd.setOptions();
                        unixruntime_socketfd.is = socket.getInputStream();
                        unixruntime_socketfd.os = socket.getOutputStream();

                    case 1:
                        return 0;

                    default:
                        throw new Error("should never happen");
                    }
                } catch (IOException ioexception) {
                    return -111;
                }
            }
        }
    }

    private int sys_resolve_hostname(int i, int j, int k) throws Runtime.FaultException {
        String s = this.cstring(i);
        int l = this.memRead(k);

        InetAddress[] ainetaddress;

        try {
            ainetaddress = InetAddress.getAllByName(s);
        } catch (UnknownHostException unknownhostexception) {
            return 1;
        }

        int i1 = min(l / 4, ainetaddress.length);

        for (int j1 = 0; j1 < i1; j += 4) {
            byte[] abyte = ainetaddress[j1].getAddress();

            this.copyout(abyte, j, 4);
            ++j1;
        }

        this.memWrite(k, i1 * 4);
        return 0;
    }

    private int sys_setsockopt(int i, int j, int k, int l, int i1) throws Runtime.ReadFaultException, Runtime.ErrnoException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        switch (j) {
        case 65535:
            switch (k) {
            case 4:
            case 8:
                if (i1 != 4) {
                    return -22;
                }

                int j1 = this.memRead(l);

                if (j1 != 0) {
                    unixruntime_socketfd.options |= k;
                } else {
                    unixruntime_socketfd.options &= ~k;
                }

                unixruntime_socketfd.setOptions();
                return 0;

            default:
                System.err.println("Unknown setsockopt name passed: " + k);
                return -109;
            }

        default:
            System.err.println("Unknown setsockopt leve passed: " + j);
            return -109;
        }
    }

    private int sys_getsockopt(int i, int j, int k, int l, int i1) throws Runtime.ErrnoException, Runtime.FaultException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        switch (j) {
        case 65535:
            switch (k) {
            case 4:
            case 8:
                int j1 = this.memRead(i1);

                if (j1 < 4) {
                    return -22;
                }

                int k1 = (unixruntime_socketfd.options & k) != 0 ? 1 : 0;

                this.memWrite(l, k1);
                this.memWrite(i1, 4);
                return 0;

            default:
                System.err.println("Unknown setsockopt name passed: " + k);
                return -109;
            }

        default:
            System.err.println("Unknown setsockopt leve passed: " + j);
            return -109;
        }
    }

    private int sys_bind(int i, int j, int k) throws Runtime.FaultException, Runtime.ErrnoException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        if (unixruntime_socketfd.type() == 0 && (unixruntime_socketfd.s != null || unixruntime_socketfd.ss != null)) {
            return -127;
        } else {
            int l = this.memRead(j);

            if ((l >>> 16 & 255) != 2) {
                return -106;
            } else {
                int i1 = l & '\uffff';
                InetAddress inetaddress = null;

                if (this.memRead(j + 4) != 0) {
                    byte[] abyte = new byte[4];

                    this.copyin(j + 4, abyte, 4);

                    try {
                        inetaddress = Platform.inetAddressFromBytes(abyte);
                    } catch (UnknownHostException unknownhostexception) {
                        return -125;
                    }
                }

                switch (unixruntime_socketfd.type()) {
                case 0:
                    unixruntime_socketfd.bindAddr = inetaddress;
                    unixruntime_socketfd.bindPort = i1;
                    return 0;

                case 1:
                    if (unixruntime_socketfd.ds != null) {
                        unixruntime_socketfd.ds.close();
                    }

                    try {
                        unixruntime_socketfd.ds = inetaddress != null ? new DatagramSocket(i1, inetaddress) : new DatagramSocket(i1);
                        return 0;
                    } catch (IOException ioexception) {
                        return -112;
                    }

                default:
                    throw new Error("should never happen");
                }
            }
        }
    }

    private int sys_listen(int i, int j) throws Runtime.ErrnoException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        if (unixruntime_socketfd.type() != 0) {
            return -95;
        } else if (unixruntime_socketfd.ss == null && unixruntime_socketfd.s == null) {
            if (unixruntime_socketfd.bindPort < 0) {
                return -95;
            } else {
                try {
                    unixruntime_socketfd.ss = new ServerSocket(unixruntime_socketfd.bindPort, j, unixruntime_socketfd.bindAddr);
                    unixruntime_socketfd.flags |= 2;
                    return 0;
                } catch (IOException ioexception) {
                    return -112;
                }
            }
        } else {
            return -127;
        }
    }

    private int sys_accept(int i, int j, int k) throws Runtime.ErrnoException, Runtime.FaultException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        if (unixruntime_socketfd.type() != 0) {
            return -95;
        } else if (!unixruntime_socketfd.listen()) {
            return -95;
        } else {
            int l = this.memRead(k);
            ServerSocket serversocket = unixruntime_socketfd.ss;

            Socket socket;

            try {
                socket = serversocket.accept();
            } catch (IOException ioexception) {
                return -5;
            }

            if (l >= 8) {
                this.memWrite(j, 100794368 | socket.getPort());
                byte[] abyte = socket.getInetAddress().getAddress();

                this.copyout(abyte, j + 4, 4);
                this.memWrite(k, 8);
            }

            UnixRuntime.SocketFD unixruntime_socketfd1 = new UnixRuntime.SocketFD(0);

            unixruntime_socketfd1.s = socket;

            try {
                unixruntime_socketfd1.is = socket.getInputStream();
                unixruntime_socketfd1.os = socket.getOutputStream();
            } catch (IOException ioexception1) {
                return -5;
            }

            int i1 = this.addFD(unixruntime_socketfd1);

            if (i1 == -1) {
                unixruntime_socketfd1.close();
                return -23;
            } else {
                return i1;
            }
        }
    }

    private int sys_shutdown(int i, int j) throws Runtime.ErrnoException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        if (unixruntime_socketfd.type() == 0 && !unixruntime_socketfd.listen()) {
            if (unixruntime_socketfd.s == null) {
                return -128;
            } else {
                Socket socket = unixruntime_socketfd.s;

                try {
                    if (j == 0 || j == 2) {
                        Platform.socketHalfClose(socket, false);
                    }

                    if (j == 1 || j == 2) {
                        Platform.socketHalfClose(socket, true);
                    }

                    return 0;
                } catch (IOException ioexception) {
                    return -5;
                }
            }
        } else {
            return -95;
        }
    }

    private int sys_sendto(int i, int j, int k, int l, int i1, int j1) throws Runtime.ErrnoException, Runtime.ReadFaultException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        if (l != 0) {
            throw new Runtime.ErrnoException(22);
        } else {
            int k1 = this.memRead(i1);

            if ((k1 >>> 16 & 255) != 2) {
                return -106;
            } else {
                int l1 = k1 & '\uffff';
                byte[] abyte = new byte[4];

                this.copyin(i1 + 4, abyte, 4);

                InetAddress inetaddress;

                try {
                    inetaddress = Platform.inetAddressFromBytes(abyte);
                } catch (UnknownHostException unknownhostexception) {
                    return -125;
                }

                k = Math.min(k, 16776192);
                byte[] abyte1 = this.byteBuf(k);

                this.copyin(j, abyte1, k);

                try {
                    return unixruntime_socketfd.sendto(abyte1, 0, k, inetaddress, l1);
                } catch (Runtime.ErrnoException runtime_errnoexception) {
                    if (runtime_errnoexception.errno == 32) {
                        this.exit(141, true);
                    }

                    throw runtime_errnoexception;
                }
            }
        }
    }

    private int sys_recvfrom(int i, int j, int k, int l, int i1, int j1) throws Runtime.ErrnoException, Runtime.FaultException {
        UnixRuntime.SocketFD unixruntime_socketfd = this.getSocketFD(i);

        if (l != 0) {
            throw new Runtime.ErrnoException(22);
        } else {
            InetAddress[] ainetaddress = i1 == 0 ? null : new InetAddress[1];
            int[] aint = i1 == 0 ? null : new int[1];

            k = Math.min(k, 16776192);
            byte[] abyte = this.byteBuf(k);
            int k1 = unixruntime_socketfd.recvfrom(abyte, 0, k, ainetaddress, aint);

            this.copyout(abyte, j, k1);
            if (i1 != 0) {
                this.memWrite(i1, 131072 | aint[0]);
                byte[] abyte1 = ainetaddress[0].getAddress();

                this.copyout(abyte1, i1 + 4, 4);
            }

            return k1;
        }
    }

    private int sys_select(int i, int j, int k, int l, int i1) throws Runtime.ReadFaultException, Runtime.ErrnoException {
        return -88;
    }

    private static String hostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException unknownhostexception) {
            return "darkstar";
        }
    }

    private int sys_sysctl(int i, int j, int k, int l, int i1, int j1) throws Runtime.FaultException {
        if (i1 != 0) {
            return -1;
        } else if (j == 0) {
            return -2;
        } else if (k == 0) {
            return 0;
        } else {
            String s = null;

            switch (this.memRead(i)) {
            case 1:
                if (j == 2) {
                    switch (this.memRead(i + 4)) {
                    case 1:
                        s = "NestedVM";
                        break;

                    case 2:
                        s = "1.0";

                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;

                    case 4:
                        s = "NestedVM Kernel Version 1.0";
                        break;

                    case 10:
                        s = hostName();
                    }
                }
                break;

            case 6:
                if (j == 2) {
                    switch (this.memRead(i + 4)) {
                    case 1:
                        s = "NestedVM Virtual Machine";
                    }
                }
            }

            if (s == null) {
                return -2;
            } else {
                int k1 = this.memRead(l);

                if (s instanceof String) {
                    byte[] abyte = getNullTerminatedBytes((String) s);

                    if (k1 < abyte.length) {
                        return -12;
                    }

                    k1 = abyte.length;
                    this.copyout(abyte, k, k1);
                    this.memWrite(l, k1);
                } else {
                    if (!(s instanceof Integer)) {
                        throw new Error("should never happen");
                    }

                    if (k1 < 4) {
                        return -12;
                    }

                    this.memWrite(k, ((Integer) s).intValue());
                }

                return 0;
            }
        }
    }

    private String normalizePath(String s) {
        boolean flag = s.startsWith("/");
        int i = this.cwd.length();

        if (!s.startsWith(".") && s.indexOf("./") == -1 && s.indexOf("//") == -1 && !s.endsWith(".")) {
            return flag ? s.substring(1) : (i == 0 ? s : (s.length() == 0 ? this.cwd : this.cwd + "/" + s));
        } else {
            char[] achar = new char[s.length() + 1];
            char[] achar1 = new char[achar.length + (flag ? -1 : this.cwd.length())];

            s.getChars(0, s.length(), achar, 0);
            int j = 0;
            int k = 0;

            if (flag) {
                do {
                    ++j;
                } while (achar[j] == 47);
            } else if (i != 0) {
                this.cwd.getChars(0, i, achar1, 0);
                k = i;
            }

            while (achar[j] != 0) {
                if (j != 0) {
                    while (achar[j] != 0 && achar[j] != 47) {
                        achar1[k++] = achar[j++];
                    }

                    if (achar[j] == 0) {
                        break;
                    }

                    while (achar[j] == 47) {
                        ++j;
                    }
                }

                if (achar[j] == 0) {
                    break;
                }

                if (achar[j] != 46) {
                    achar1[k++] = 47;
                    achar1[k++] = achar[j++];
                } else if (achar[j + 1] != 0 && achar[j + 1] != 47) {
                    if (achar[j + 1] == 46 && (achar[j + 2] == 0 || achar[j + 2] == 47)) {
                        j += 2;
                        if (k > 0) {
                            --k;
                        }

                        while (k > 0 && achar1[k] != 47) {
                            --k;
                        }
                    } else {
                        ++j;
                        achar1[k++] = 47;
                        achar1[k++] = 46;
                    }
                } else {
                    ++j;
                }
            }

            if (k > 0 && achar1[k - 1] == 47) {
                --k;
            }

            int l = achar1[0] == 47 ? 1 : 0;

            return new String(achar1, l, k - l);
        }
    }

    Runtime.FStat hostFStat(final File file, Object object) {
        final boolean flag = false;

        try {
            FileInputStream fileinputstream = new FileInputStream(file);

            switch (fileinputstream.read()) {
            case 35:
                flag = fileinputstream.read() == 33;
                break;

            case 127:
                flag = fileinputstream.read() == 69 && fileinputstream.read() == 76 && fileinputstream.read() == 70;
            }

            fileinputstream.close();
        } catch (IOException ioexception) {
            ;
        }

        UnixRuntime.HostFS unixruntime_hostfs = (UnixRuntime.HostFS) object;
        final short short0 = unixruntime_hostfs.inodes.get(file.getAbsolutePath());
        final int i = unixruntime_hostfs.devno;

        return new Runtime.HostFStat(file, flag) {
            public int inode() {
                return short0;
            }

            public int dev() {
                return i;
            }
        };
    }

    Runtime.FD hostFSDirFD(File file, Object object) {
        UnixRuntime.HostFS unixruntime_hostfs = (UnixRuntime.HostFS) object;

        return unixruntime_hostfs.new HostDirFD(file);
    }

    private static void putInt(byte[] abyte, int i, int j) {
        abyte[i + 0] = (byte) (j >>> 24 & 255);
        abyte[i + 1] = (byte) (j >>> 16 & 255);
        abyte[i + 2] = (byte) (j >>> 8 & 255);
        abyte[i + 3] = (byte) (j >>> 0 & 255);
    }

    static Class class$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static {
        Method method;

        try {
            method = Class.forName("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.RuntimeCompiler").getMethod("compile", new Class[] { UnixRuntime.class$org$ibex$nestedvm$util$Seekable == null ? (UnixRuntime.class$org$ibex$nestedvm$util$Seekable = class$("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable")) : UnixRuntime.class$org$ibex$nestedvm$util$Seekable, UnixRuntime.class$java$lang$String == null ? (UnixRuntime.class$java$lang$String = class$("java.lang.String")) : UnixRuntime.class$java$lang$String, UnixRuntime.class$java$lang$String == null ? (UnixRuntime.class$java$lang$String = class$("java.lang.String")) : UnixRuntime.class$java$lang$String});
        } catch (NoSuchMethodException nosuchmethodexception) {
            method = null;
        } catch (ClassNotFoundException classnotfoundexception) {
            method = null;
        }

        runtimeCompilerCompile = method;
    }

    public static class ResourceFS extends UnixRuntime.FS {

        final InodeCache inodes = new InodeCache(500);

        public Runtime.FStat lstat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            return this.stat(unixruntime, s);
        }

        public void mkdir(UnixRuntime unixruntime, String s, int i) throws Runtime.ErrnoException {
            throw new Runtime.ErrnoException(30);
        }

        public void unlink(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            throw new Runtime.ErrnoException(30);
        }

        Runtime.FStat connFStat(final URLConnection urlconnection) {
            return new Runtime.FStat() {
                public int type() {
                    return '耀';
                }

                public int nlink() {
                    return 1;
                }

                public int mode() {
                    return 292;
                }

                public int size() {
                    return urlconnection.getContentLength();
                }

                public int mtime() {
                    return (int) (urlconnection.getDate() / 1000L);
                }

                public int inode() {
                    return ResourceFS.this.inodes.get(urlconnection.getURL().toString());
                }

                public int dev() {
                    return ResourceFS.this.devno;
                }
            };
        }

        public Runtime.FStat stat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            URL url = unixruntime.getClass().getResource("/" + s);

            if (url == null) {
                return null;
            } else {
                try {
                    return this.connFStat(url.openConnection());
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        public Runtime.FD open(UnixRuntime unixruntime, String s, final int i, int j) throws Runtime.ErrnoException {
            if ((i & -4) != 0) {
                System.err.println("WARNING: Unsupported flags passed to ResourceFS.open(\"" + s + "\"): " + Runtime.toHex(i & -4));
                throw new Runtime.ErrnoException(134);
            } else if ((i & 3) != 0) {
                throw new Runtime.ErrnoException(30);
            } else {
                URL url = unixruntime.getClass().getResource("/" + s);

                if (url == null) {
                    return null;
                } else {
                    try {
                        final URLConnection urlconnection = url.openConnection();
                        final Seekable.InputStream seekable_inputstream = new Seekable.InputStream(urlconnection.getInputStream());

                        return new Runtime.SeekableFD(seekable_inputstream, i) {
                            protected Runtime.FStat _fstat() {
                                return ResourceFS.this.connFStat(urlconnection);
                            }
                        };
                    } catch (FileNotFoundException filenotfoundexception) {
                        if (filenotfoundexception.getMessage() != null && filenotfoundexception.getMessage().indexOf("Permission denied") >= 0) {
                            throw new Runtime.ErrnoException(13);
                        } else {
                            return null;
                        }
                    } catch (IOException ioexception) {
                        throw new Runtime.ErrnoException(5);
                    }
                }
            }
        }
    }

    public static class DevFS extends UnixRuntime.FS {

        private static final int ROOT_INODE = 1;
        private static final int NULL_INODE = 2;
        private static final int ZERO_INODE = 3;
        private static final int FD_INODE = 4;
        private static final int FD_INODES = 32;
        private Runtime.FD devZeroFD = new Runtime.FD() {
            public int read(byte[] abyte, int i, int j) {
                for (int k = i; k < i + j; ++k) {
                    abyte[k] = 0;
                }

                return j;
            }

            public int write(byte[] abyte, int i, int j) {
                return j;
            }

            public int seek(int i, int j) {
                return 0;
            }

            public Runtime.FStat _fstat() {
                return new UnixRuntime.DevFS.DevFStat(null) {
                    public int inode() {
                        return 3;
                    }
                };
            }

            public int flags() {
                return 2;
            }

            static UnixRuntime.DevFS access$700(Object object) {
                return DevFS.this;
            }
        };
        private Runtime.FD devNullFD = new Runtime.FD() {
            public int read(byte[] abyte, int i, int j) {
                return 0;
            }

            public int write(byte[] abyte, int i, int j) {
                return j;
            }

            public int seek(int i, int j) {
                return 0;
            }

            public Runtime.FStat _fstat() {
                return new UnixRuntime.DevFS.DevFStat(null) {
                    public int inode() {
                        return 2;
                    }
                };
            }

            public int flags() {
                return 2;
            }

            static UnixRuntime.DevFS access$800(Object object) {
                return DevFS.this;
            }
        };

        public Runtime.FD open(UnixRuntime unixruntime, String s, int i, int j) throws Runtime.ErrnoException {
            if (s.equals("null")) {
                return this.devNullFD;
            } else if (s.equals("zero")) {
                return this.devZeroFD;
            } else {
                int k;

                if (s.startsWith("fd/")) {
                    try {
                        k = Integer.parseInt(s.substring(4));
                    } catch (NumberFormatException numberformatexception) {
                        return null;
                    }

                    return k >= 0 && k < 64 ? (unixruntime.fds[k] == null ? null : unixruntime.fds[k].dup()) : null;
                } else if (s.equals("fd")) {
                    k = 0;

                    for (int l = 0; l < 64; ++l) {
                        if (unixruntime.fds[l] != null) {
                            ++k;
                        }
                    }

                    final int[] aint = new int[k];

                    k = 0;

                    for (int i1 = 0; i1 < 64; ++i1) {
                        if (unixruntime.fds[i1] != null) {
                            aint[k++] = i1;
                        }
                    }

                    return new UnixRuntime.DevFS.DevDirFD(null) {
                        public int myInode() {
                            return 4;
                        }

                        public int parentInode() {
                            return 1;
                        }

                        public int inode(int i) {
                            return 32 + i;
                        }

                        public String name(int i) {
                            return Integer.toString(aint[i]);
                        }

                        public int size() {
                            return aint.length;
                        }
                    };
                } else {
                    return s.equals("") ? new UnixRuntime.DevFS.DevDirFD(null) {
                        public int myInode() {
                            return 1;
                        }

                        public int parentInode() {
                            return 1;
                        }

                        public int inode(int i) {
                            switch (i) {
                            case 0:
                                return 2;

                            case 1:
                                return 3;

                            case 2:
                                return 4;

                            default:
                                return -1;
                            }
                        }

                        public String name(int i) {
                            switch (i) {
                            case 0:
                                return "null";

                            case 1:
                                return "zero";

                            case 2:
                                return "fd";

                            default:
                                return null;
                            }
                        }

                        public int size() {
                            return 3;
                        }
                    } : null;
                }
            }
        }

        public Runtime.FStat stat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            if (s.equals("null")) {
                return this.devNullFD.fstat();
            } else if (s.equals("zero")) {
                return this.devZeroFD.fstat();
            } else if (s.startsWith("fd/")) {
                int i;

                try {
                    i = Integer.parseInt(s.substring(3));
                } catch (NumberFormatException numberformatexception) {
                    return null;
                }

                return i >= 0 && i < 64 ? (unixruntime.fds[i] == null ? null : unixruntime.fds[i].fstat()) : null;
            } else {
                return s.equals("fd") ? new Runtime.FStat() {
                    public int inode() {
                        return 4;
                    }

                    public int dev() {
                        return DevFS.this.devno;
                    }

                    public int type() {
                        return 16384;
                    }

                    public int mode() {
                        return 292;
                    }
                } : (s.equals("") ? new Runtime.FStat() {
                    public int inode() {
                        return 1;
                    }

                    public int dev() {
                        return DevFS.this.devno;
                    }

                    public int type() {
                        return 16384;
                    }

                    public int mode() {
                        return 292;
                    }
                } : null);
            }
        }

        public void mkdir(UnixRuntime unixruntime, String s, int i) throws Runtime.ErrnoException {
            throw new Runtime.ErrnoException(30);
        }

        public void unlink(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            throw new Runtime.ErrnoException(30);
        }

        private abstract class DevDirFD extends UnixRuntime.DirFD {

            private DevDirFD() {}

            public int myDev() {
                return DevFS.this.devno;
            }

            DevDirFD(Object object) {
                this();
            }
        }

        private abstract class DevFStat extends Runtime.FStat {

            private DevFStat() {}

            public int dev() {
                return DevFS.this.devno;
            }

            public int mode() {
                return 438;
            }

            public int type() {
                return 8192;
            }

            public int nlink() {
                return 1;
            }

            public abstract int inode();

            DevFStat(Object object) {
                this();
            }
        }
    }

    public abstract static class DirFD extends Runtime.FD {

        private int pos = -2;

        protected abstract int size();

        protected abstract String name(int i);

        protected abstract int inode(int i);

        protected abstract int myDev();

        protected abstract int parentInode();

        protected abstract int myInode();

        public int flags() {
            return 0;
        }

        public int getdents(byte[] abyte, int i, int j) {
            int k;

            for (k = i; j > 0 && this.pos < this.size(); ++this.pos) {
                int l;
                int i1;

                switch (this.pos) {
                case -2:
                case -1:
                    l = this.pos == -1 ? this.parentInode() : this.myInode();
                    if (l == -1) {
                        continue;
                    }

                    i1 = 9 + (this.pos == -1 ? 2 : 1);
                    if (i1 > j) {
                        return i - k;
                    }

                    abyte[i + 8] = 46;
                    if (this.pos == -1) {
                        abyte[i + 9] = 46;
                    }
                    break;

                default:
                    String s = this.name(this.pos);
                    byte[] abyte1 = Runtime.getBytes(s);

                    i1 = abyte1.length + 9;
                    if (i1 > j) {
                        return i - k;
                    }

                    l = this.inode(this.pos);
                    System.arraycopy(abyte1, 0, abyte, i + 8, abyte1.length);
                }

                abyte[i + i1 - 1] = 0;
                i1 = i1 + 3 & -4;
                UnixRuntime.putInt(abyte, i, i1);
                UnixRuntime.putInt(abyte, i + 4, l);
                i += i1;
                j -= i1;
            }

            return i - k;
        }

        protected Runtime.FStat _fstat() {
            return new Runtime.FStat() {
                public int type() {
                    return 16384;
                }

                public int inode() {
                    return DirFD.this.myInode();
                }

                public int dev() {
                    return DirFD.this.myDev();
                }
            };
        }
    }

    public static class CygdriveFS extends UnixRuntime.HostFS {

        protected File hostFile(String s) {
            char c0 = s.charAt(0);

            if (c0 >= 97 && c0 <= 122 && s.charAt(1) == 47) {
                s = c0 + ":" + s.substring(1).replace('/', '\\');
                return new File(s);
            } else {
                return null;
            }
        }

        public CygdriveFS() {
            super("/");
        }
    }

    public static class HostFS extends UnixRuntime.FS {

        InodeCache inodes;
        protected File root;

        public File getRoot() {
            return this.root;
        }

        protected File hostFile(String s) {
            char c0 = File.separatorChar;

            if (c0 != 47) {
                char[] achar = s.toCharArray();

                for (int i = 0; i < achar.length; ++i) {
                    char c1 = achar[i];

                    if (c1 == 47) {
                        achar[i] = c0;
                    } else if (c1 == c0) {
                        achar[i] = 47;
                    }
                }

                s = new String(achar);
            }

            return new File(this.root, s);
        }

        public HostFS(String s) {
            this(new File(s));
        }

        public HostFS(File file) {
            this.inodes = new InodeCache(4000);
            this.root = file;
        }

        public Runtime.FD open(UnixRuntime unixruntime, String s, int i, int j) throws Runtime.ErrnoException {
            File file = this.hostFile(s);

            return unixruntime.hostFSOpen(file, i, j, this);
        }

        public void unlink(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            File file = this.hostFile(s);

            if (unixruntime.sm != null && !unixruntime.sm.allowUnlink(file)) {
                throw new Runtime.ErrnoException(1);
            } else if (!file.exists()) {
                throw new Runtime.ErrnoException(2);
            } else {
                if (!file.delete()) {
                    boolean flag = false;

                    for (int i = 0; i < 64; ++i) {
                        if (unixruntime.fds[i] != null) {
                            String s1 = unixruntime.fds[i].getNormalizedPath();

                            if (s1 != null && s1.equals(s)) {
                                unixruntime.fds[i].markDeleteOnClose();
                                flag = true;
                            }
                        }
                    }

                    if (!flag) {
                        throw new Runtime.ErrnoException(1);
                    }
                }

            }
        }

        public Runtime.FStat stat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            File file = this.hostFile(s);

            if (unixruntime.sm != null && !unixruntime.sm.allowStat(file)) {
                throw new Runtime.ErrnoException(13);
            } else {
                return !file.exists() ? null : unixruntime.hostFStat(file, this);
            }
        }

        public void mkdir(UnixRuntime unixruntime, String s, int i) throws Runtime.ErrnoException {
            File file = this.hostFile(s);

            if (unixruntime.sm != null && !unixruntime.sm.allowWrite(file)) {
                throw new Runtime.ErrnoException(13);
            } else if (file.exists() && file.isDirectory()) {
                throw new Runtime.ErrnoException(17);
            } else if (file.exists()) {
                throw new Runtime.ErrnoException(20);
            } else {
                File file1 = getParentFile(file);

                if (file1 != null && (!file1.exists() || !file1.isDirectory())) {
                    throw new Runtime.ErrnoException(20);
                } else if (!file.mkdir()) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        private static File getParentFile(File file) {
            String s = file.getParent();

            return s == null ? null : new File(s);
        }

        public class HostDirFD extends UnixRuntime.DirFD {

            private final File f;
            private final File[] children;

            public HostDirFD(File file) {
                this.f = file;
                String[] astring = file.list();

                this.children = new File[astring.length];

                for (int i = 0; i < astring.length; ++i) {
                    this.children[i] = new File(file, astring[i]);
                }

            }

            public int size() {
                return this.children.length;
            }

            public String name(int i) {
                return this.children[i].getName();
            }

            public int inode(int i) {
                return HostFS.this.inodes.get(this.children[i].getAbsolutePath());
            }

            public int parentInode() {
                File file = UnixRuntime.HostFS.getParentFile(this.f);

                return file == null ? this.myInode() : HostFS.this.inodes.get(file.getAbsolutePath());
            }

            public int myInode() {
                return HostFS.this.inodes.get(this.f.getAbsolutePath());
            }

            public int myDev() {
                return HostFS.this.devno;
            }
        }
    }

    public abstract static class FS {

        static final int OPEN = 1;
        static final int STAT = 2;
        static final int LSTAT = 3;
        static final int MKDIR = 4;
        static final int UNLINK = 5;
        UnixRuntime.GlobalState owner;
        int devno;

        Object dispatch(int i, UnixRuntime unixruntime, String s, int j, int k) throws Runtime.ErrnoException {
            switch (i) {
            case 1:
                return this.open(unixruntime, s, j, k);

            case 2:
                return this.stat(unixruntime, s);

            case 3:
                return this.lstat(unixruntime, s);

            case 4:
                this.mkdir(unixruntime, s, j);
                return null;

            case 5:
                this.unlink(unixruntime, s);
                return null;

            default:
                throw new Error("should never happen");
            }
        }

        public Runtime.FStat lstat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            return this.stat(unixruntime, s);
        }

        public abstract Runtime.FD open(UnixRuntime unixruntime, String s, int i, int j) throws Runtime.ErrnoException;

        public abstract Runtime.FStat stat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException;

        public abstract void mkdir(UnixRuntime unixruntime, String s, int i) throws Runtime.ErrnoException;

        public abstract void unlink(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException;
    }

    public static final class GlobalState {

        Hashtable execCache;
        final UnixRuntime[] tasks;
        int nextPID;
        Seekable.Lock[] locks;
        private UnixRuntime.GlobalState.MP[] mps;
        private UnixRuntime.FS root;

        public GlobalState() {
            this(255);
        }

        public GlobalState(int i) {
            this(i, true);
        }

        public GlobalState(int i, boolean flag) {
            this.execCache = new Hashtable();
            this.nextPID = 1;
            this.locks = new Seekable.Lock[16];
            this.mps = new UnixRuntime.GlobalState.MP[0];
            this.tasks = new UnixRuntime[i + 1];
            if (flag) {
                File file = null;

                if (Platform.getProperty("nestedvm.root") != null) {
                    file = new File(Platform.getProperty("nestedvm.root"));
                    if (!file.isDirectory()) {
                        throw new IllegalArgumentException("nestedvm.root is not a directory");
                    }
                } else {
                    String s = Platform.getProperty("user.dir");

                    file = Platform.getRoot(new File(s != null ? s : "."));
                }

                this.addMount("/", new UnixRuntime.HostFS(file));
                if (Platform.getProperty("nestedvm.root") == null) {
                    File[] afile = Platform.listRoots();

                    for (int j = 0; j < afile.length; ++j) {
                        String s1 = afile[j].getPath();

                        if (s1.endsWith(File.separator)) {
                            s1 = s1.substring(0, s1.length() - 1);
                        }

                        if (s1.length() != 0 && s1.indexOf(47) == -1) {
                            this.addMount("/" + s1.toLowerCase(), new UnixRuntime.HostFS(afile[j]));
                        }
                    }
                }

                this.addMount("/dev", new UnixRuntime.DevFS());
                this.addMount("/resource", new UnixRuntime.ResourceFS());
                this.addMount("/cygdrive", new UnixRuntime.CygdriveFS());
            }

        }

        public String mapHostPath(String s) {
            return this.mapHostPath(new File(s));
        }

        public String mapHostPath(File file) {
            UnixRuntime.FS unixruntime_fs;

            synchronized (this) {
                this.mps = this.mps;
                unixruntime_fs = this.root;
            }

            if (!file.isAbsolute()) {
                file = new File(file.getAbsolutePath());
            }

            for (int i = this.mps.length; i >= 0; --i) {
                UnixRuntime.FS unixruntime_fs1 = i == this.mps.length ? unixruntime_fs : this.mps[i].fs;
                String s = i == this.mps.length ? "" : this.mps[i].path;

                if (unixruntime_fs1 instanceof UnixRuntime.HostFS) {
                    File file1 = ((UnixRuntime.HostFS) unixruntime_fs1).getRoot();

                    if (!file1.isAbsolute()) {
                        file1 = new File(file1.getAbsolutePath());
                    }

                    if (file.getPath().startsWith(file1.getPath())) {
                        char c0 = File.separatorChar;
                        String s1 = file.getPath().substring(file1.getPath().length());

                        if (c0 != 47) {
                            char[] achar = s1.toCharArray();

                            for (int j = 0; j < achar.length; ++j) {
                                if (achar[j] == 47) {
                                    achar[j] = c0;
                                } else if (achar[j] == c0) {
                                    achar[j] = 47;
                                }
                            }

                            s1 = new String(achar);
                        }

                        String s2 = "/" + (s.length() == 0 ? "" : s + "/") + s1;

                        return s2;
                    }
                }
            }

            return null;
        }

        public synchronized UnixRuntime.FS getMount(String s) {
            if (!s.startsWith("/")) {
                throw new IllegalArgumentException("Mount point doesn\'t start with a /");
            } else if (s.equals("/")) {
                return this.root;
            } else {
                s = s.substring(1);

                for (int i = 0; i < this.mps.length; ++i) {
                    if (this.mps[i].path.equals(s)) {
                        return this.mps[i].fs;
                    }
                }

                return null;
            }
        }

        public synchronized void addMount(String s, UnixRuntime.FS unixruntime_fs) {
            if (this.getMount(s) != null) {
                throw new IllegalArgumentException("mount point already exists");
            } else if (!s.startsWith("/")) {
                throw new IllegalArgumentException("Mount point doesn\'t start with a /");
            } else {
                if (unixruntime_fs.owner != null) {
                    unixruntime_fs.owner.removeMount(unixruntime_fs);
                }

                unixruntime_fs.owner = this;
                if (s.equals("/")) {
                    this.root = unixruntime_fs;
                    unixruntime_fs.devno = 1;
                } else {
                    s = s.substring(1);
                    int i = this.mps.length;
                    UnixRuntime.GlobalState.MP[] aunixruntime_globalstate_mp = new UnixRuntime.GlobalState.MP[i + 1];

                    if (i != 0) {
                        System.arraycopy(this.mps, 0, aunixruntime_globalstate_mp, 0, i);
                    }

                    aunixruntime_globalstate_mp[i] = new UnixRuntime.GlobalState.MP(s, unixruntime_fs);
                    Sort.sort(aunixruntime_globalstate_mp);
                    this.mps = aunixruntime_globalstate_mp;
                    int j = 0;

                    for (int k = 0; k < this.mps.length; ++k) {
                        j = Runtime.max(j, this.mps[k].fs.devno);
                    }

                    unixruntime_fs.devno = j + 2;
                }
            }
        }

        public synchronized void removeMount(UnixRuntime.FS unixruntime_fs) {
            for (int i = 0; i < this.mps.length; ++i) {
                if (this.mps[i].fs == unixruntime_fs) {
                    this.removeMount(i);
                    return;
                }
            }

            throw new IllegalArgumentException("mount point doesn\'t exist");
        }

        public synchronized void removeMount(String s) {
            if (!s.startsWith("/")) {
                throw new IllegalArgumentException("Mount point doesn\'t start with a /");
            } else {
                if (s.equals("/")) {
                    this.removeMount(-1);
                } else {
                    s = s.substring(1);

                    int i;

                    for (i = 0; i < this.mps.length && !this.mps[i].path.equals(s); ++i) {
                        ;
                    }

                    if (i == this.mps.length) {
                        throw new IllegalArgumentException("mount point doesn\'t exist");
                    }

                    this.removeMount(i);
                }

            }
        }

        private void removeMount(int i) {
            if (i == -1) {
                this.root.owner = null;
                this.root = null;
            } else {
                UnixRuntime.GlobalState.MP[] aunixruntime_globalstate_mp = new UnixRuntime.GlobalState.MP[this.mps.length - 1];

                System.arraycopy(this.mps, 0, aunixruntime_globalstate_mp, 0, i);
                System.arraycopy(this.mps, 0, aunixruntime_globalstate_mp, i, this.mps.length - i - 1);
                this.mps = aunixruntime_globalstate_mp;
            }
        }

        private Object fsop(int i, UnixRuntime unixruntime, String s, int j, int k) throws Runtime.ErrnoException {
            int l = s.length();

            if (l != 0) {
                UnixRuntime.GlobalState.MP[] aunixruntime_globalstate_mp;

                synchronized (this) {
                    aunixruntime_globalstate_mp = this.mps;
                }

                for (int i1 = 0; i1 < aunixruntime_globalstate_mp.length; ++i1) {
                    UnixRuntime.GlobalState.MP unixruntime_globalstate_mp = aunixruntime_globalstate_mp[i1];
                    int j1 = unixruntime_globalstate_mp.path.length();

                    if (s.startsWith(unixruntime_globalstate_mp.path) && (l == j1 || s.charAt(j1) == 47)) {
                        return unixruntime_globalstate_mp.fs.dispatch(i, unixruntime, l == j1 ? "" : s.substring(j1 + 1), j, k);
                    }
                }
            }

            return this.root.dispatch(i, unixruntime, s, j, k);
        }

        public final Runtime.FD open(UnixRuntime unixruntime, String s, int i, int j) throws Runtime.ErrnoException {
            return (Runtime.FD) this.fsop(1, unixruntime, s, i, j);
        }

        public final Runtime.FStat stat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            return (Runtime.FStat) this.fsop(2, unixruntime, s, 0, 0);
        }

        public final Runtime.FStat lstat(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            return (Runtime.FStat) this.fsop(3, unixruntime, s, 0, 0);
        }

        public final void mkdir(UnixRuntime unixruntime, String s, int i) throws Runtime.ErrnoException {
            this.fsop(4, unixruntime, s, i, 0);
        }

        public final void unlink(UnixRuntime unixruntime, String s) throws Runtime.ErrnoException {
            this.fsop(5, unixruntime, s, 0, 0);
        }

        private static class CacheEnt {

            public final long time;
            public final long size;
            public final Object o;

            public CacheEnt(long i, long j, Object object) {
                this.time = i;
                this.size = j;
                this.o = object;
            }
        }

        static class MP implements Sort.Comparable {

            public String path;
            public UnixRuntime.FS fs;

            public MP(String s, UnixRuntime.FS unixruntime_fs) {
                this.path = s;
                this.fs = unixruntime_fs;
            }

            public int compareTo(Object object) {
                return !(object instanceof UnixRuntime.GlobalState.MP) ? 1 : -this.path.compareTo(((UnixRuntime.GlobalState.MP) object).path);
            }
        }
    }

    static class SocketFD extends Runtime.FD {

        public static final int TYPE_STREAM = 0;
        public static final int TYPE_DGRAM = 1;
        public static final int LISTEN = 2;
        int flags;
        int options;
        Socket s;
        ServerSocket ss;
        DatagramSocket ds;
        InetAddress bindAddr;
        int bindPort = -1;
        InetAddress connectAddr;
        int connectPort = -1;
        DatagramPacket dp;
        InputStream is;
        OutputStream os;
        private static final byte[] EMPTY = new byte[0];

        public int type() {
            return this.flags & 1;
        }

        public boolean listen() {
            return (this.flags & 2) != 0;
        }

        public SocketFD(int i) {
            this.flags = i;
            if (i == 1) {
                this.dp = new DatagramPacket(UnixRuntime.SocketFD.EMPTY, 0);
            }

        }

        public void setOptions() {
            try {
                if (this.s != null && this.type() == 0 && !this.listen()) {
                    Platform.socketSetKeepAlive(this.s, (this.options & 8) != 0);
                }
            } catch (SocketException socketexception) {
                socketexception.printStackTrace();
            }

        }

        public void _close() {
            try {
                if (this.s != null) {
                    this.s.close();
                }

                if (this.ss != null) {
                    this.ss.close();
                }

                if (this.ds != null) {
                    this.ds.close();
                }
            } catch (IOException ioexception) {
                ;
            }

        }

        public int read(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            if (this.type() == 1) {
                return this.recvfrom(abyte, i, j, (InetAddress[]) null, (int[]) null);
            } else if (this.is == null) {
                throw new Runtime.ErrnoException(32);
            } else {
                try {
                    int k = this.is.read(abyte, i, j);

                    return k < 0 ? 0 : k;
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        public int recvfrom(byte[] abyte, int i, int j, InetAddress[] ainetaddress, int[] aint) throws Runtime.ErrnoException {
            if (this.type() == 0) {
                return this.read(abyte, i, j);
            } else if (i != 0) {
                throw new IllegalArgumentException("off must be 0");
            } else {
                this.dp.setData(abyte);
                this.dp.setLength(j);

                try {
                    if (this.ds == null) {
                        this.ds = new DatagramSocket();
                    }

                    this.ds.receive(this.dp);
                } catch (IOException ioexception) {
                    ioexception.printStackTrace();
                    throw new Runtime.ErrnoException(5);
                }

                if (ainetaddress != null) {
                    ainetaddress[0] = this.dp.getAddress();
                    aint[0] = this.dp.getPort();
                }

                return this.dp.getLength();
            }
        }

        public int write(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
            if (this.type() == 1) {
                return this.sendto(abyte, i, j, (InetAddress) null, -1);
            } else if (this.os == null) {
                throw new Runtime.ErrnoException(32);
            } else {
                try {
                    this.os.write(abyte, i, j);
                    return j;
                } catch (IOException ioexception) {
                    throw new Runtime.ErrnoException(5);
                }
            }
        }

        public int sendto(byte[] abyte, int i, int j, InetAddress inetaddress, int k) throws Runtime.ErrnoException {
            if (i != 0) {
                throw new IllegalArgumentException("off must be 0");
            } else if (this.type() == 0) {
                return this.write(abyte, i, j);
            } else {
                if (inetaddress == null) {
                    inetaddress = this.connectAddr;
                    k = this.connectPort;
                    if (inetaddress == null) {
                        throw new Runtime.ErrnoException(128);
                    }
                }

                this.dp.setAddress(inetaddress);
                this.dp.setPort(k);
                this.dp.setData(abyte);
                this.dp.setLength(j);

                try {
                    if (this.ds == null) {
                        this.ds = new DatagramSocket();
                    }

                    this.ds.send(this.dp);
                } catch (IOException ioexception) {
                    ioexception.printStackTrace();
                    if ("Network is unreachable".equals(ioexception.getMessage())) {
                        throw new Runtime.ErrnoException(118);
                    }

                    throw new Runtime.ErrnoException(5);
                }

                return this.dp.getLength();
            }
        }

        public int flags() {
            return 2;
        }

        public Runtime.FStat _fstat() {
            return new Runtime.SocketFStat();
        }
    }

    static class Pipe {

        private final byte[] pipebuf = new byte[2048];
        private int readPos;
        private int writePos;
        public final Runtime.FD reader = new UnixRuntime.Pipe.Reader();
        public final Runtime.FD writer = new UnixRuntime.Pipe.Writer();

        public class Writer extends Runtime.FD {

            protected Runtime.FStat _fstat() {
                return new Runtime.SocketFStat();
            }

            public int write(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
                if (j == 0) {
                    return 0;
                } else {
                    UnixRuntime.Pipe unixruntime_pipe = Pipe.this;

                    synchronized (Pipe.this) {
                        if (Pipe.this.readPos == -1) {
                            throw new Runtime.ErrnoException(32);
                        } else {
                            if (Pipe.this.pipebuf.length - Pipe.this.writePos < Math.min(j, 512)) {
                                while (Pipe.this.readPos != -1 && Pipe.this.readPos != Pipe.this.writePos) {
                                    try {
                                        Pipe.this.wait();
                                    } catch (InterruptedException interruptedexception) {
                                        ;
                                    }
                                }

                                if (Pipe.this.readPos == -1) {
                                    throw new Runtime.ErrnoException(32);
                                }

                                Pipe.this.readPos = Pipe.this.writePos = 0;
                            }

                            j = Math.min(j, Pipe.this.pipebuf.length - Pipe.this.writePos);
                            System.arraycopy(abyte, i, Pipe.this.pipebuf, Pipe.this.writePos, j);
                            if (Pipe.this.readPos == Pipe.this.writePos) {
                                Pipe.this.notify();
                            }

                            Pipe.this.writePos = j;
                            return j;
                        }
                    }
                }
            }

            public int flags() {
                return 1;
            }

            public void _close() {
                UnixRuntime.Pipe unixruntime_pipe = Pipe.this;

                synchronized (Pipe.this) {
                    Pipe.this.writePos = -1;
                    Pipe.this.notify();
                }
            }
        }

        public class Reader extends Runtime.FD {

            protected Runtime.FStat _fstat() {
                return new Runtime.SocketFStat();
            }

            public int read(byte[] abyte, int i, int j) throws Runtime.ErrnoException {
                if (j == 0) {
                    return 0;
                } else {
                    UnixRuntime.Pipe unixruntime_pipe = Pipe.this;

                    synchronized (Pipe.this) {
                        while (Pipe.this.writePos != -1 && Pipe.this.readPos == Pipe.this.writePos) {
                            try {
                                Pipe.this.wait();
                            } catch (InterruptedException interruptedexception) {
                                ;
                            }
                        }

                        if (Pipe.this.writePos == -1) {
                            return 0;
                        } else {
                            j = Math.min(j, Pipe.this.writePos - Pipe.this.readPos);
                            System.arraycopy(Pipe.this.pipebuf, Pipe.this.readPos, abyte, i, j);
                            Pipe.this.readPos = j;
                            if (Pipe.this.readPos == Pipe.this.writePos) {
                                Pipe.this.notify();
                            }

                            return j;
                        }
                    }
                }
            }

            public int flags() {
                return 0;
            }

            public void _close() {
                UnixRuntime.Pipe unixruntime_pipe = Pipe.this;

                synchronized (Pipe.this) {
                    Pipe.this.readPos = -1;
                    Pipe.this.notify();
                }
            }
        }
    }

    public static final class ForkedProcess extends Thread {

        private final UnixRuntime initial;

        public ForkedProcess(UnixRuntime unixruntime) {
            this.initial = unixruntime;
            this.start();
        }

        public void run() {
            UnixRuntime.executeAndExec(this.initial);
        }
    }

    private static class ProcessTableFullExn extends RuntimeException {

        private ProcessTableFullExn() {}

        ProcessTableFullExn(Object object) {
            this();
        }
    }
}
