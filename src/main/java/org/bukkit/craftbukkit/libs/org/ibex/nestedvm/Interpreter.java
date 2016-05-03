package org.bukkit.craftbukkit.libs.org.ibex.nestedvm;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.ELF;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;

public class Interpreter extends UnixRuntime implements Cloneable {

    private int[] registers;
    private int hi;
    private int lo;
    private int[] fpregs;
    private int fcsr;
    private int pc;
    public String image;
    private ELF.Symtab symtab;
    private int gp;
    private ELF.Symbol userInfo;
    private int entryPoint;
    private int heapStart;
    private HashMap sourceLineCache;

    private final void setFC(boolean flag) {
        this.fcsr = this.fcsr & -8388609 | (flag ? 8388608 : 0);
    }

    private final int roundingMode() {
        return this.fcsr & 3;
    }

    private final double getDouble(int i) {
        return Double.longBitsToDouble(((long) this.fpregs[i + 1] & 4294967295L) << 32 | (long) this.fpregs[i] & 4294967295L);
    }

    private final void setDouble(int i, double d0) {
        long j = Double.doubleToLongBits(d0);

        this.fpregs[i + 1] = (int) (j >>> 32);
        this.fpregs[i] = (int) j;
    }

    private final float getFloat(int i) {
        return Float.intBitsToFloat(this.fpregs[i]);
    }

    private final void setFloat(int i, float f) {
        this.fpregs[i] = Float.floatToRawIntBits(f);
    }

    protected void _execute() throws Runtime.ExecutionException {
        try {
            this.runSome();
        } catch (Runtime.ExecutionException runtime_executionexception) {
            runtime_executionexception.setLocation(toHex(this.pc) + ": " + this.sourceLine(this.pc));
            throw runtime_executionexception;
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        Interpreter interpreter = (Interpreter) super.clone();

        interpreter.registers = (int[]) ((int[]) this.registers.clone());
        interpreter.fpregs = (int[]) ((int[]) this.fpregs.clone());
        return interpreter;
    }

    private final int runSome() throws Runtime.FaultException, Runtime.ExecutionException {
        int i = 1 << this.pageShift >> 2;
        int[] aint = this.registers;
        int[] aint1 = this.fpregs;
        int j = this.pc;
        int k = j + 4;

        try {
            while (true) {
                int l;

                try {
                    l = this.readPages[j >>> this.pageShift][j >>> 2 & i - 1];
                } catch (RuntimeException runtimeexception) {
                    if (j == -559038737) {
                        throw new Error("fell off cpu: r2: " + aint[2]);
                    }

                    l = this.memRead(j);
                }

                int i1 = l >>> 26 & 255;
                int j1 = l >>> 21 & 31;
                int k1 = l >>> 16 & 31;
                int l1 = l >>> 16 & 31;
                int i2 = l >>> 11 & 31;
                int j2 = l >>> 11 & 31;
                int k2 = l >>> 6 & 31;
                int l2 = l >>> 6 & 31;
                int i3 = l & 63;
                int j3 = l & 67108863;
                int k3 = l & '\uffff';
                int l3 = l << 16 >> 16;

                aint[0] = 0;
                int i4;
                int j4;

                label479:
                switch (i1) {
                case 0:
                    long k4;

                    switch (i3) {
                    case 0:
                        if (l != 0) {
                            aint[i2] = aint[k1] << k2;
                        }
                        break label479;

                    case 1:
                    case 5:
                    case 10:
                    case 11:
                    case 14:
                    case 15:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 40:
                    case 41:
                    default:
                        throw new Runtime.ExecutionException("Illegal instruction 0/" + i3);

                    case 2:
                        aint[i2] = aint[k1] >>> k2;
                        break label479;

                    case 3:
                        aint[i2] = aint[k1] >> k2;
                        break label479;

                    case 4:
                        aint[i2] = aint[k1] << (aint[j1] & 31);
                        break label479;

                    case 6:
                        aint[i2] = aint[k1] >>> (aint[j1] & 31);
                        break label479;

                    case 7:
                        aint[i2] = aint[k1] >> (aint[j1] & 31);
                        break label479;

                    case 8:
                        i4 = aint[j1];
                        j += 4;
                        k = i4;
                        continue;

                    case 9:
                        i4 = aint[j1];
                        j += 4;
                        aint[i2] = j + 4;
                        k = i4;
                        continue;

                    case 12:
                        this.pc = j;
                        aint[2] = this.syscall(aint[2], aint[4], aint[5], aint[6], aint[7], aint[8], aint[9]);
                        if (this.state != 0) {
                            this.pc = k;
                            return 0;
                        }
                        break label479;

                    case 13:
                        throw new Runtime.ExecutionException("Break");

                    case 16:
                        aint[i2] = this.hi;
                        break label479;

                    case 17:
                        this.hi = aint[j1];
                        break label479;

                    case 18:
                        aint[i2] = this.lo;
                        break label479;

                    case 19:
                        this.lo = aint[j1];
                        break label479;

                    case 24:
                        k4 = (long) aint[j1] * (long) aint[k1];
                        this.hi = (int) (k4 >>> 32);
                        this.lo = (int) k4;
                        break label479;

                    case 25:
                        k4 = ((long) aint[j1] & 4294967295L) * ((long) aint[k1] & 4294967295L);
                        this.hi = (int) (k4 >>> 32);
                        this.lo = (int) k4;
                        break label479;

                    case 26:
                        this.hi = aint[j1] % aint[k1];
                        this.lo = aint[j1] / aint[k1];
                        break label479;

                    case 27:
                        if (k1 != 0) {
                            this.hi = (int) (((long) aint[j1] & 4294967295L) % ((long) aint[k1] & 4294967295L));
                            this.lo = (int) (((long) aint[j1] & 4294967295L) / ((long) aint[k1] & 4294967295L));
                        }
                        break label479;

                    case 32:
                        throw new Runtime.ExecutionException("ADD (add with oveflow trap) not suported");

                    case 33:
                        aint[i2] = aint[j1] + aint[k1];
                        break label479;

                    case 34:
                        throw new Runtime.ExecutionException("SUB (sub with oveflow trap) not suported");

                    case 35:
                        aint[i2] = aint[j1] - aint[k1];
                        break label479;

                    case 36:
                        aint[i2] = aint[j1] & aint[k1];
                        break label479;

                    case 37:
                        aint[i2] = aint[j1] | aint[k1];
                        break label479;

                    case 38:
                        aint[i2] = aint[j1] ^ aint[k1];
                        break label479;

                    case 39:
                        aint[i2] = ~(aint[j1] | aint[k1]);
                        break label479;

                    case 42:
                        aint[i2] = aint[j1] < aint[k1] ? 1 : 0;
                        break label479;

                    case 43:
                        aint[i2] = ((long) aint[j1] & 4294967295L) < ((long) aint[k1] & 4294967295L) ? 1 : 0;
                        break label479;
                    }

                case 1:
                    switch (k1) {
                    case 0:
                        if (aint[j1] < 0) {
                            j += 4;
                            i4 = j + l3 * 4;
                            k = i4;
                            continue;
                        }
                        break label479;

                    case 1:
                        if (aint[j1] >= 0) {
                            j += 4;
                            i4 = j + l3 * 4;
                            k = i4;
                            continue;
                        }
                        break label479;

                    case 16:
                        if (aint[j1] < 0) {
                            j += 4;
                            aint[31] = j + 4;
                            i4 = j + l3 * 4;
                            k = i4;
                            continue;
                        }
                        break label479;

                    case 17:
                        if (aint[j1] >= 0) {
                            j += 4;
                            aint[31] = j + 4;
                            i4 = j + l3 * 4;
                            k = i4;
                            continue;
                        }
                        break label479;

                    default:
                        throw new Runtime.ExecutionException("Illegal Instruction");
                    }

                case 2:
                    i4 = j & -268435456 | j3 << 2;
                    j += 4;
                    k = i4;
                    continue;

                case 3:
                    i4 = j & -268435456 | j3 << 2;
                    j += 4;
                    aint[31] = j + 4;
                    k = i4;
                    continue;

                case 4:
                    if (aint[j1] == aint[k1]) {
                        j += 4;
                        i4 = j + l3 * 4;
                        k = i4;
                        continue;
                    }
                    break;

                case 5:
                    if (aint[j1] != aint[k1]) {
                        j += 4;
                        i4 = j + l3 * 4;
                        k = i4;
                        continue;
                    }
                    break;

                case 6:
                    if (aint[j1] <= 0) {
                        j += 4;
                        i4 = j + l3 * 4;
                        k = i4;
                        continue;
                    }
                    break;

                case 7:
                    if (aint[j1] > 0) {
                        j += 4;
                        i4 = j + l3 * 4;
                        k = i4;
                        continue;
                    }
                    break;

                case 8:
                    aint[k1] = aint[j1] + l3;
                    break;

                case 9:
                    aint[k1] = aint[j1] + l3;
                    break;

                case 10:
                    aint[k1] = aint[j1] < l3 ? 1 : 0;
                    break;

                case 11:
                    aint[k1] = ((long) aint[j1] & 4294967295L) < ((long) l3 & 4294967295L) ? 1 : 0;
                    break;

                case 12:
                    aint[k1] = aint[j1] & k3;
                    break;

                case 13:
                    aint[k1] = aint[j1] | k3;
                    break;

                case 14:
                    aint[k1] = aint[j1] ^ k3;
                    break;

                case 15:
                    aint[k1] = k3 << 16;
                    break;

                case 16:
                    throw new Runtime.ExecutionException("TLB/Exception support not implemented");

                case 17:
                    boolean flag = false;
                    String s = flag ? this.sourceLine(j) : "";
                    boolean flag1 = flag && (s.indexOf("dtoa.c:51") >= 0 || s.indexOf("dtoa.c:52") >= 0 || s.indexOf("test.c") >= 0);

                    if (j1 > 8 && flag1) {
                        System.out.println("               FP Op: " + i1 + "/" + j1 + "/" + i3 + " " + s);
                    }

                    if (this.roundingMode() != 0 && j1 != 6 && (j1 != 16 && j1 != 17 || i3 != 36)) {
                        throw new Runtime.ExecutionException("Non-cvt.w.z operation attempted with roundingMode != round to nearest");
                    }

                    switch (j1) {
                    case 0:
                        aint[k1] = aint1[i2];
                        break label479;

                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 18:
                    case 19:
                    default:
                        throw new Runtime.ExecutionException("Invalid Instruction 17/" + j1);

                    case 2:
                        if (j2 != 31) {
                            throw new Runtime.ExecutionException("FCR " + j2 + " unavailable");
                        }

                        aint[k1] = this.fcsr;
                        break label479;

                    case 4:
                        aint1[i2] = aint[k1];
                        break label479;

                    case 6:
                        if (j2 != 31) {
                            throw new Runtime.ExecutionException("FCR " + j2 + " unavailable");
                        }

                        this.fcsr = aint[k1];
                        break label479;

                    case 8:
                        if ((this.fcsr & 8388608) != 0 == ((l >>> 16 & 1) != 0)) {
                            j += 4;
                            i4 = j + l3 * 4;
                            k = i4;
                            continue;
                        }
                        break label479;

                    case 16:
                        switch (i3) {
                        case 0:
                            this.setFloat(l2, this.getFloat(j2) + this.getFloat(l1));
                            break label479;

                        case 1:
                            this.setFloat(l2, this.getFloat(j2) - this.getFloat(l1));
                            break label479;

                        case 2:
                            this.setFloat(l2, this.getFloat(j2) * this.getFloat(l1));
                            break label479;

                        case 3:
                            this.setFloat(l2, this.getFloat(j2) / this.getFloat(l1));
                            break label479;

                        case 5:
                            this.setFloat(l2, Math.abs(this.getFloat(j2)));
                            break label479;

                        case 6:
                            aint1[l2] = aint1[j2];
                            break label479;

                        case 7:
                            this.setFloat(l2, -this.getFloat(j2));
                            break label479;

                        case 33:
                            this.setDouble(l2, (double) this.getFloat(j2));
                            break label479;

                        case 36:
                            switch (this.roundingMode()) {
                            case 0:
                                aint1[l2] = (int) Math.floor((double) (this.getFloat(j2) + 0.5F));
                                break label479;

                            case 1:
                                aint1[l2] = (int) this.getFloat(j2);
                                break label479;

                            case 2:
                                aint1[l2] = (int) Math.ceil((double) this.getFloat(j2));
                                break label479;

                            case 3:
                                aint1[l2] = (int) Math.floor((double) this.getFloat(j2));

                            default:
                                break label479;
                            }

                        case 50:
                            this.setFC(this.getFloat(j2) == this.getFloat(l1));
                            break label479;

                        case 60:
                            this.setFC(this.getFloat(j2) < this.getFloat(l1));
                            break label479;

                        case 62:
                            this.setFC(this.getFloat(j2) <= this.getFloat(l1));
                            break label479;

                        default:
                            throw new Runtime.ExecutionException("Invalid Instruction 17/" + j1 + "/" + i3 + " at " + this.sourceLine(j));
                        }

                    case 17:
                        switch (i3) {
                        case 0:
                            this.setDouble(l2, this.getDouble(j2) + this.getDouble(l1));
                            break label479;

                        case 1:
                            if (flag1) {
                                System.out.println("f" + l2 + " = f" + j2 + " (" + this.getDouble(j2) + ") - f" + l1 + " (" + this.getDouble(l1) + ")");
                            }

                            this.setDouble(l2, this.getDouble(j2) - this.getDouble(l1));
                            break label479;

                        case 2:
                            if (flag1) {
                                System.out.println("f" + l2 + " = f" + j2 + " (" + this.getDouble(j2) + ") * f" + l1 + " (" + this.getDouble(l1) + ")");
                            }

                            this.setDouble(l2, this.getDouble(j2) * this.getDouble(l1));
                            if (flag1) {
                                System.out.println("f" + l2 + " = " + this.getDouble(l2));
                            }
                            break label479;

                        case 3:
                            this.setDouble(l2, this.getDouble(j2) / this.getDouble(l1));
                            break label479;

                        case 5:
                            this.setDouble(l2, Math.abs(this.getDouble(j2)));
                            break label479;

                        case 6:
                            aint1[l2] = aint1[j2];
                            aint1[l2 + 1] = aint1[j2 + 1];
                            break label479;

                        case 7:
                            this.setDouble(l2, -this.getDouble(j2));
                            break label479;

                        case 32:
                            this.setFloat(l2, (float) this.getDouble(j2));
                            break label479;

                        case 36:
                            if (flag1) {
                                System.out.println("CVT.W.D rm: " + this.roundingMode() + " f" + j2 + ":" + this.getDouble(j2));
                            }

                            switch (this.roundingMode()) {
                            case 0:
                                aint1[l2] = (int) Math.floor(this.getDouble(j2) + 0.5D);
                                break;

                            case 1:
                                aint1[l2] = (int) this.getDouble(j2);
                                break;

                            case 2:
                                aint1[l2] = (int) Math.ceil(this.getDouble(j2));
                                break;

                            case 3:
                                aint1[l2] = (int) Math.floor(this.getDouble(j2));
                            }

                            if (flag1) {
                                System.out.println("CVT.W.D: f" + l2 + ":" + aint1[l2]);
                            }
                            break label479;

                        case 50:
                            this.setFC(this.getDouble(j2) == this.getDouble(l1));
                            break label479;

                        case 60:
                            this.setFC(this.getDouble(j2) < this.getDouble(l1));
                            break label479;

                        case 62:
                            this.setFC(this.getDouble(j2) <= this.getDouble(l1));
                            break label479;

                        default:
                            throw new Runtime.ExecutionException("Invalid Instruction 17/" + j1 + "/" + i3 + " at " + this.sourceLine(j));
                        }

                    case 20:
                        switch (i3) {
                        case 32:
                            this.setFloat(l2, (float) aint1[j2]);
                            break label479;

                        case 33:
                            this.setDouble(l2, (double) aint1[j2]);
                            break label479;

                        default:
                            throw new Runtime.ExecutionException("Invalid Instruction 17/" + j1 + "/" + i3 + " at " + this.sourceLine(j));
                        }
                    }

                case 18:
                case 19:
                    throw new Runtime.ExecutionException("No coprocessor installed");

                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 39:
                case 44:
                case 45:
                case 47:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                default:
                    throw new Runtime.ExecutionException("Invalid Instruction: " + i1);

                case 32:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception1) {
                        i4 = this.memRead(j4 & -4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        i4 = i4 >>> 24 & 255;
                        break;

                    case 1:
                        i4 = i4 >>> 16 & 255;
                        break;

                    case 2:
                        i4 = i4 >>> 8 & 255;
                        break;

                    case 3:
                        i4 = i4 >>> 0 & 255;
                    }

                    if ((i4 & 128) != 0) {
                        i4 |= -256;
                    }

                    aint[k1] = i4;
                    break;

                case 33:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception2) {
                        i4 = this.memRead(j4 & -4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        i4 = i4 >>> 16 & '\uffff';
                        break;

                    case 2:
                        i4 = i4 >>> 0 & '\uffff';
                        break;

                    default:
                        throw new Runtime.ReadFaultException(j4);
                    }

                    if ((i4 & '耀') != 0) {
                        i4 |= -65536;
                    }

                    aint[k1] = i4;
                    break;

                case 34:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception3) {
                        i4 = this.memRead(j4 & -4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        aint[k1] = aint[k1] & 0 | i4 << 0;
                        break label479;

                    case 1:
                        aint[k1] = aint[k1] & 255 | i4 << 8;
                        break label479;

                    case 2:
                        aint[k1] = aint[k1] & '\uffff' | i4 << 16;
                        break label479;

                    case 3:
                        aint[k1] = aint[k1] & 16777215 | i4 << 24;

                    default:
                        break label479;
                    }

                case 35:
                    j4 = aint[j1] + l3;

                    try {
                        aint[k1] = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception4) {
                        aint[k1] = this.memRead(j4);
                    }
                    break;

                case 36:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception5) {
                        i4 = this.memRead(j4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        aint[k1] = i4 >>> 24 & 255;
                        break label479;

                    case 1:
                        aint[k1] = i4 >>> 16 & 255;
                        break label479;

                    case 2:
                        aint[k1] = i4 >>> 8 & 255;
                        break label479;

                    case 3:
                        aint[k1] = i4 >>> 0 & 255;

                    default:
                        break label479;
                    }

                case 37:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception6) {
                        i4 = this.memRead(j4 & -4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        aint[k1] = i4 >>> 16 & '\uffff';
                        break label479;

                    case 2:
                        aint[k1] = i4 >>> 0 & '\uffff';
                        break label479;

                    default:
                        throw new Runtime.ReadFaultException(j4);
                    }

                case 38:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception7) {
                        i4 = this.memRead(j4 & -4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        aint[k1] = aint[k1] & -256 | i4 >>> 24;
                        break label479;

                    case 1:
                        aint[k1] = aint[k1] & -65536 | i4 >>> 16;
                        break label479;

                    case 2:
                        aint[k1] = aint[k1] & -16777216 | i4 >>> 8;
                        break label479;

                    case 3:
                        aint[k1] = aint[k1] & 0 | i4 >>> 0;

                    default:
                        break label479;
                    }

                case 40:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception8) {
                        i4 = this.memRead(j4 & -4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        i4 = i4 & 16777215 | (aint[k1] & 255) << 24;
                        break;

                    case 1:
                        i4 = i4 & -16711681 | (aint[k1] & 255) << 16;
                        break;

                    case 2:
                        i4 = i4 & -65281 | (aint[k1] & 255) << 8;
                        break;

                    case 3:
                        i4 = i4 & -256 | (aint[k1] & 255) << 0;
                    }

                    try {
                        this.writePages[j4 >>> this.pageShift][j4 >>> 2 & i - 1] = i4;
                    } catch (RuntimeException runtimeexception9) {
                        this.memWrite(j4 & -4, i4);
                    }
                    break;

                case 41:
                    j4 = aint[j1] + l3;

                    try {
                        i4 = this.readPages[j4 >>> this.pageShift][j4 >>> 2 & i - 1];
                    } catch (RuntimeException runtimeexception10) {
                        i4 = this.memRead(j4 & -4);
                    }

                    switch (j4 & 3) {
                    case 0:
                        i4 = i4 & '\uffff' | (aint[k1] & '\uffff') << 16;
                        break;

                    case 2:
                        i4 = i4 & -65536 | (aint[k1] & '\uffff') << 0;
                        break;

                    default:
                        throw new Runtime.WriteFaultException(j4);
                    }

                    try {
                        this.writePages[j4 >>> this.pageShift][j4 >>> 2 & i - 1] = i4;
                    } catch (RuntimeException runtimeexception11) {
                        this.memWrite(j4 & -4, i4);
                    }
                    break;

                case 42:
                    j4 = aint[j1] + l3;
                    i4 = this.memRead(j4 & -4);
                    switch (j4 & 3) {
                    case 0:
                        i4 = i4 & 0 | aint[k1] >>> 0;
                        break;

                    case 1:
                        i4 = i4 & -16777216 | aint[k1] >>> 8;
                        break;

                    case 2:
                        i4 = i4 & -65536 | aint[k1] >>> 16;
                        break;

                    case 3:
                        i4 = i4 & -256 | aint[k1] >>> 24;
                    }

                    try {
                        this.writePages[j4 >>> this.pageShift][j4 >>> 2 & i - 1] = i4;
                    } catch (RuntimeException runtimeexception12) {
                        this.memWrite(j4 & -4, i4);
                    }
                    break;

                case 43:
                    j4 = aint[j1] + l3;

                    try {
                        this.writePages[j4 >>> this.pageShift][j4 >>> 2 & i - 1] = aint[k1];
                    } catch (RuntimeException runtimeexception13) {
                        this.memWrite(j4 & -4, aint[k1]);
                    }
                    break;

                case 46:
                    j4 = aint[j1] + l3;
                    i4 = this.memRead(j4 & -4);
                    switch (j4 & 3) {
                    case 0:
                        i4 = i4 & 16777215 | aint[k1] << 24;
                        break;

                    case 1:
                        i4 = i4 & '\uffff' | aint[k1] << 16;
                        break;

                    case 2:
                        i4 = i4 & 255 | aint[k1] << 8;
                        break;

                    case 3:
                        i4 = i4 & 0 | aint[k1] << 0;
                    }

                    this.memWrite(j4 & -4, i4);
                    break;

                case 48:
                    aint[k1] = this.memRead(aint[j1] + l3);
                    break;

                case 49:
                    aint1[k1] = this.memRead(aint[j1] + l3);
                    break;

                case 56:
                    this.memWrite(aint[j1] + l3, aint[k1]);
                    aint[k1] = 1;
                    break;

                case 57:
                    this.memWrite(aint[j1] + l3, aint1[k1]);
                }

                j = k;
                k += 4;
            }
        } catch (Runtime.ExecutionException runtime_executionexception) {
            this.pc = j;
            throw runtime_executionexception;
        }
    }

    public int lookupSymbol(String s) {
        ELF.Symbol elf_symbol = this.symtab.getGlobalSymbol(s);

        return elf_symbol == null ? -1 : elf_symbol.addr;
    }

    protected int gp() {
        return this.gp;
    }

    protected int userInfoBae() {
        return this.userInfo == null ? 0 : this.userInfo.addr;
    }

    protected int userInfoSize() {
        return this.userInfo == null ? 0 : this.userInfo.size;
    }

    protected int entryPoint() {
        return this.entryPoint;
    }

    protected int heapStart() {
        return this.heapStart;
    }

    private void loadImage(Seekable seekable) throws IOException {
        ELF elf = new ELF(seekable);

        this.symtab = elf.getSymtab();
        if (elf.header.type != 2) {
            throw new IOException("Binary is not an executable");
        } else if (elf.header.machine != 8) {
            throw new IOException("Binary is not for the MIPS I Architecture");
        } else if (elf.ident.data != 2) {
            throw new IOException("Binary is not big endian");
        } else {
            this.entryPoint = elf.header.entry;
            ELF.Symtab elf_symtab = elf.getSymtab();

            if (elf_symtab == null) {
                throw new IOException("No symtab in binary (did you strip it?)");
            } else {
                this.userInfo = elf_symtab.getGlobalSymbol("user_info");
                ELF.Symbol elf_symbol = elf_symtab.getGlobalSymbol("_gp");

                if (elf_symbol == null) {
                    throw new IOException("NO _gp symbol!");
                } else {
                    this.gp = elf_symbol.addr;
                    this.entryPoint = elf.header.entry;
                    ELF.PHeader[] aelf_pheader = elf.pheaders;
                    int i = 0;
                    int j = 1 << this.pageShift;
                    int k = 1 << this.pageShift >> 2;

                    for (int l = 0; l < aelf_pheader.length; ++l) {
                        ELF.PHeader elf_pheader = aelf_pheader[l];

                        if (elf_pheader.type == 1) {
                            int i1 = elf_pheader.memsz;
                            int j1 = elf_pheader.filesz;

                            if (i1 != 0) {
                                if (i1 < 0) {
                                    throw new IOException("pheader size too large");
                                }

                                int k1 = elf_pheader.vaddr;

                                if (k1 == 0) {
                                    throw new IOException("pheader vaddr == 0x0");
                                }

                                i = max(k1 + i1, i);

                                for (int l1 = 0; l1 < i1 + j - 1; l1 += j) {
                                    int i2 = l1 + k1 >>> this.pageShift;

                                    if (this.readPages[i2] == null) {
                                        this.readPages[i2] = new int[k];
                                    }

                                    if (elf_pheader.writable()) {
                                        this.writePages[i2] = this.readPages[i2];
                                    }
                                }

                                if (j1 != 0) {
                                    j1 &= -4;
                                    DataInputStream datainputstream = new DataInputStream(elf_pheader.getInputStream());

                                    do {
                                        this.readPages[k1 >>> this.pageShift][k1 >>> 2 & k - 1] = datainputstream.readInt();
                                        k1 += 4;
                                        j1 -= 4;
                                    } while (j1 > 0);

                                    datainputstream.close();
                                }
                            }
                        }
                    }

                    this.heapStart = i + j - 1 & ~(j - 1);
                }
            }
        }
    }

    protected void setCPUState(Runtime.CPUState runtime_cpustate) {
        int i;

        for (i = 1; i < 32; ++i) {
            this.registers[i] = runtime_cpustate.r[i];
        }

        for (i = 0; i < 32; ++i) {
            this.fpregs[i] = runtime_cpustate.f[i];
        }

        this.hi = runtime_cpustate.hi;
        this.lo = runtime_cpustate.lo;
        this.fcsr = runtime_cpustate.fcsr;
        this.pc = runtime_cpustate.pc;
    }

    protected void getCPUState(Runtime.CPUState runtime_cpustate) {
        int i;

        for (i = 1; i < 32; ++i) {
            runtime_cpustate.r[i] = this.registers[i];
        }

        for (i = 0; i < 32; ++i) {
            runtime_cpustate.f[i] = this.fpregs[i];
        }

        runtime_cpustate.hi = this.hi;
        runtime_cpustate.lo = this.lo;
        runtime_cpustate.fcsr = this.fcsr;
        runtime_cpustate.pc = this.pc;
    }

    public Interpreter(Seekable seekable) throws IOException {
        super(4096, 65536);
        this.registers = new int[32];
        this.fpregs = new int[32];
        this.loadImage(seekable);
    }

    public Interpreter(String s) throws IOException {
        this((Seekable) (new Seekable.File(s, false)));
        this.image = s;
    }

    public Interpreter(InputStream inputstream) throws IOException {
        this((Seekable) (new Seekable.InputStream(inputstream)));
    }

    public String sourceLine(int i) {
        String s = (String) (this.sourceLineCache == null ? null : this.sourceLineCache.get(new Integer(i)));

        if (s != null) {
            return s;
        } else if (this.image == null) {
            return null;
        } else {
            try {
                Process process = java.lang.Runtime.getRuntime().exec(new String[] { "mips-unknown-elf-addr2line", "-e", this.image, toHex(i)});

                s = (new BufferedReader(new InputStreamReader(process.getInputStream()))).readLine();
                if (s == null) {
                    return null;
                } else {
                    while (s.startsWith("../")) {
                        s = s.substring(3);
                    }

                    if (this.sourceLineCache == null) {
                        this.sourceLineCache = new HashMap();
                    }

                    this.sourceLineCache.put(new Integer(i), s);
                    return s;
                }
            } catch (IOException ioexception) {
                return null;
            }
        }
    }

    public static void main(String[] astring) throws Exception {
        String s = astring[0];
        Interpreter interpreter = new Interpreter(s);

        java.lang.Runtime.getRuntime().addShutdownHook(new Thread(interpreter.new DebugShutdownHook()));
        int i = interpreter.run(astring);

        System.err.println("Exit status: " + i);
        System.exit(i);
    }

    public class DebugShutdownHook implements Runnable {

        public void run() {
            int i = Interpreter.this.pc;

            if (Interpreter.this.getState() == 0) {
                System.err.print("\nCPU Executing " + Runtime.toHex(i) + ": " + Interpreter.this.sourceLine(i) + "\n");
            }

        }
    }
}
