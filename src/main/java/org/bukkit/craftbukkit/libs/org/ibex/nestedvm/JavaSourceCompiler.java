package org.bukkit.craftbukkit.libs.org.ibex.nestedvm;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.ELF;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;

public class JavaSourceCompiler extends Compiler {

    private StringBuffer runs = new StringBuffer();
    private StringBuffer inits = new StringBuffer();
    private StringBuffer classLevel = new StringBuffer();
    private PrintWriter out;
    private int indent;
    private static String[] indents = new String[16];
    private int startOfMethod = 0;
    private int endOfMethod = 0;
    private HashMap relativeAddrs = new HashMap();
    private boolean textDone;
    private int initDataCount = 0;
    private boolean unreachable = false;

    private void p() {
        this.out.println();
    }

    private void p(String s) {
        this.out.println(JavaSourceCompiler.indents[this.indent] + s);
    }

    private void pblock(StringBuffer stringbuffer) {
        this.out.print(stringbuffer.toString());
    }

    public JavaSourceCompiler(Seekable seekable, String s, Writer writer) throws IOException {
        super(seekable, s);
        this.out = new PrintWriter(writer);
    }

    protected void _go() throws Compiler.Exn, IOException {
        if (this.singleFloat) {
            throw new Compiler.Exn("JavaSourceCompiler doesn\'t support singleFloat");
        } else {
            String s;
            String s1;

            if (this.fullClassName.indexOf(46) != -1) {
                s = this.fullClassName.substring(0, this.fullClassName.lastIndexOf(46));
                s1 = this.fullClassName.substring(this.fullClassName.lastIndexOf(46) + 1);
            } else {
                s1 = this.fullClassName;
                s = null;
            }

            this.p("/* This file was generated from " + this.source + " by Mips2Java on " + dateTime() + " */");
            if (s != null) {
                this.p("package " + s + ";");
            }

            if (this.runtimeStats) {
                this.p("import java.util.*;");
            }

            this.p();
            this.p("public final class " + s1 + " extends " + this.runtimeClass + " {");
            ++this.indent;
            this.p("/* program counter */");
            this.p("private int pc = 0;");
            if (this.debugCompiler) {
                this.p("private int lastPC = 0;");
            }

            this.p();
            this.p("/* General Purpose registers */");
            this.p("private final static int r0 = 0;");
            this.p("private int      r1,  r2,  r3,  r4,  r5,  r6,  r7,");
            this.p("            r8,  r9,  r10, r11, r12, r13, r14, r15,");
            this.p("            r16, r17, r18, r19, r20, r21, r22, r23,");
            this.p("            r24, r25, r26, r27, r28, r29, r30, r31,");
            this.p("            hi = 0, lo = 0;");
            this.p("/* FP registers */");
            this.p("private int f0,  f1,  f2,  f3,  f4,  f5,  f6,  f7,");
            this.p("            f8,  f9,  f10, f11, f12, f13, f14, f15,");
            this.p("            f16, f17, f18, f19, f20, f21, f22, f23,");
            this.p("            f24, f25, f26, f27, f28, f29, f30, f31;");
            this.p("/* FP Control Register */");
            this.p("private int fcsr = 0;");
            this.p();
            if (this.onePage) {
                this.p("private final int[] page = readPages[0];");
            }

            int i = 0;

            int j;

            for (j = 0; j < this.elf.sheaders.length; ++j) {
                ELF.SHeader elf_sheader = this.elf.sheaders[j];
                String s2 = elf_sheader.name;

                if (elf_sheader.addr != 0) {
                    i = Math.max(i, elf_sheader.addr + elf_sheader.size);
                    if (s2.equals(".text")) {
                        this.emitText(elf_sheader.addr, new DataInputStream(elf_sheader.getInputStream()), elf_sheader.size);
                    } else if (!s2.equals(".data") && !s2.equals(".sdata") && !s2.equals(".rodata") && !s2.equals(".ctors") && !s2.equals(".dtors")) {
                        if (!s2.equals(".bss") && !s2.equals(".sbss")) {
                            throw new Compiler.Exn("Unknown segment: " + s2);
                        }

                        this.emitBSS(elf_sheader.addr, elf_sheader.size);
                    } else {
                        this.emitData(elf_sheader.addr, new DataInputStream(elf_sheader.getInputStream()), elf_sheader.size, s2.equals(".rodata"));
                    }
                }
            }

            this.p();
            this.pblock(this.classLevel);
            this.p();
            this.p("private final void trampoline() throws ExecutionException {");
            ++this.indent;
            this.p("while(state == RUNNING) {");
            ++this.indent;
            this.p("switch(pc>>>" + this.methodShift + ") {");
            ++this.indent;
            this.pblock(this.runs);
            this.p("default: throw new ExecutionException(\"invalid address 0x\" + Long.toString(this.pc&0xffffffffL,16) + \": r2: \" + r2);");
            --this.indent;
            this.p("}");
            --this.indent;
            this.p("}");
            --this.indent;
            this.p("}");
            this.p();
            this.p("public " + s1 + "() {");
            ++this.indent;
            this.p("super(" + this.pageSize + "," + this.totalPages + ");");
            this.pblock(this.inits);
            --this.indent;
            this.p("}");
            this.p();
            this.p("protected int entryPoint() { return " + toHex(this.elf.header.entry) + "; }");
            this.p("protected int heapStart() { return " + toHex(i) + "; }");
            this.p("protected int gp() { return " + toHex(this.gp.addr) + "; }");
            if (this.userInfo != null) {
                this.p("protected int userInfoBase() { return " + toHex(this.userInfo.addr) + "; }");
                this.p("protected int userInfoSize() { return " + toHex(this.userInfo.size) + "; }");
            }

            this.p("public static void main(String[] args) throws Exception {");
            ++this.indent;
            this.p("" + s1 + " me = new " + s1 + "();");
            this.p("int status = me.run(\"" + this.fullClassName + "\",args);");
            if (this.runtimeStats) {
                this.p("me.printStats();");
            }

            this.p("System.exit(status);");
            --this.indent;
            this.p("}");
            this.p();
            this.p("protected void _execute() throws ExecutionException { trampoline(); }");
            this.p();
            this.p("protected void setCPUState(CPUState state) {");
            ++this.indent;

            for (j = 1; j < 32; ++j) {
                this.p("r" + j + "=state.r[" + j + "];");
            }

            for (j = 0; j < 32; ++j) {
                this.p("f" + j + "=state.f[" + j + "];");
            }

            this.p("hi=state.hi; lo=state.lo; fcsr=state.fcsr;");
            this.p("pc=state.pc;");
            --this.indent;
            this.p("}");
            this.p("protected void getCPUState(CPUState state) {");
            ++this.indent;

            for (j = 1; j < 32; ++j) {
                this.p("state.r[" + j + "]=r" + j + ";");
            }

            for (j = 0; j < 32; ++j) {
                this.p("state.f[" + j + "]=f" + j + ";");
            }

            this.p("state.hi=hi; state.lo=lo; state.fcsr=fcsr;");
            this.p("state.pc=pc;");
            --this.indent;
            this.p("}");
            this.p();
            if (this.supportCall) {
                this.p("private static final " + this.hashClass + " symbols = new " + this.hashClass + "();");
                this.p("static {");
                ++this.indent;
                ELF.Symbol[] aelf_symbol = this.elf.getSymtab().symbols;

                for (int k = 0; k < aelf_symbol.length; ++k) {
                    ELF.Symbol elf_symbol = aelf_symbol[k];

                    if (elf_symbol.type == 2 && elf_symbol.binding == 1 && (elf_symbol.name.equals("_call_helper") || !elf_symbol.name.startsWith("_"))) {
                        this.p("symbols.put(\"" + elf_symbol.name + "\",new Integer(" + toHex(elf_symbol.addr) + "));");
                    }
                }

                --this.indent;
                this.p("}");
                this.p("public int lookupSymbol(String symbol) { Integer i = (Integer) symbols.get(symbol); return i==null ? -1 : i.intValue(); }");
                this.p();
            }

            if (this.runtimeStats) {
                this.p("private HashMap counters = new HashMap();");
                this.p("private void inc(String k) { Long i = (Long)counters.get(k); counters.put(k,new Long(i==null ? 1 : i.longValue() + 1)); }");
                this.p("private void printStats() {");
                this.p(" Iterator i = new TreeSet(counters.keySet()).iterator();");
                this.p(" while(i.hasNext()) { Object o = i.next(); System.err.println(\"\" + o + \": \" + counters.get(o)); }");
                this.p("}");
                this.p();
            }

            --this.indent;
            this.p("}");
        }
    }

    private void startMethod(int i) {
        i &= ~(this.maxBytesPerMethod - 1);
        this.startOfMethod = i;
        this.endOfMethod = i + this.maxBytesPerMethod;
        String s = "run_" + Long.toString((long) i & 4294967295L, 16);

        this.runs.append(JavaSourceCompiler.indents[4] + "case " + toHex(i >>> this.methodShift) + ": " + s + "(); break; \n");
        this.p("private final void " + s + "() throws ExecutionException { /" + "* " + toHex(i) + " - " + toHex(this.endOfMethod) + " *" + "/");
        ++this.indent;
        this.p("int addr, tmp;");
        this.p("for(;;) {");
        ++this.indent;
        this.p("switch(pc) {");
        ++this.indent;
    }

    private void endMethod() {
        this.endMethod(this.endOfMethod);
    }

    private void endMethod(int i) {
        if (this.startOfMethod != 0) {
            this.p("case " + toHex(i) + ":");
            ++this.indent;
            this.p("pc=" + this.constant(i) + ";");
            this.leaveMethod();
            --this.indent;
            if (this.debugCompiler) {
                this.p("default: throw new ExecutionException(\"invalid address 0x\" + Long.toString(pc&0xffffffffL,16)  + \" (got here from 0x\" + Long.toString(lastPC&0xffffffffL,16)+\")\");");
            } else {
                this.p("default: throw new ExecutionException(\"invalid address 0x\" + Long.toString(pc&0xffffffffL,16));");
            }

            --this.indent;
            this.p("}");
            this.p("/* NOT REACHED */");
            --this.indent;
            this.p("}");
            --this.indent;
            this.p("}");
            this.endOfMethod = this.startOfMethod = 0;
        }
    }

    private String constant(int i) {
        if (i >= 4096 && this.lessConstants) {
            int j = i & -1024;
            String s = "N_" + toHex8(j);

            if (this.relativeAddrs.get(new Integer(j)) == null) {
                this.relativeAddrs.put(new Integer(j), Boolean.TRUE);
                this.classLevel.append(JavaSourceCompiler.indents[1] + "private static int " + s + " = " + toHex(j) + ";\n");
            }

            return "(" + s + " + " + toHex(i - j) + ")";
        } else {
            return toHex(i);
        }
    }

    private void branch(int i, int j) {
        if (this.debugCompiler) {
            this.p("lastPC = " + toHex(i) + ";");
        }

        this.p("pc=" + this.constant(j) + ";");
        if (j == 0) {
            this.p("throw new ExecutionException(\"Branch to addr 0x0\");");
        } else if ((i & this.methodMask) == (j & this.methodMask)) {
            this.p("continue;");
        } else if (this.assumeTailCalls) {
            this.p("run_" + Long.toString((long) (j & this.methodMask) & 4294967295L, 16) + "(); return;");
        } else {
            this.leaveMethod();
        }

    }

    private void leaveMethod() {
        this.p("return;");
    }

    private void emitText(int i, DataInputStream datainputstream, int j) throws Compiler.Exn, IOException {
        if (this.textDone) {
            throw new Compiler.Exn("Multiple text segments");
        } else {
            this.textDone = true;
            if ((i & 3) == 0 && (j & 3) == 0) {
                int k = j / 4;
                int l = datainputstream.readInt();

                if (l == -1) {
                    throw new Error("Actually read -1 at " + toHex(i));
                } else {
                    for (int i1 = 0; i1 < k; i += 4) {
                        int j1 = l;

                        l = i1 == k - 1 ? -1 : datainputstream.readInt();
                        if (i >= this.endOfMethod) {
                            this.endMethod();
                            this.startMethod(i);
                        }

                        label63: {
                            if (this.jumpableAddresses != null && i != this.startOfMethod && this.jumpableAddresses.get(new Integer(i)) == null) {
                                if (this.unreachable) {
                                    break label63;
                                }

                                if (this.debugCompiler) {
                                    this.p("/* pc = " + toHex(i) + "*" + "/");
                                }
                            } else {
                                this.p("case " + toHex(i) + ":");
                                this.unreachable = false;
                            }

                            ++this.indent;
                            this.emitInstruction(i, j1, l);
                            --this.indent;
                        }

                        ++i1;
                    }

                    this.endMethod(i);
                    this.p();
                    datainputstream.close();
                }
            } else {
                throw new Compiler.Exn("Section on weird boundaries");
            }
        }
    }

    private void emitData(int i, DataInputStream datainputstream, int j, boolean flag) throws Compiler.Exn, IOException {
        if ((i & 3) == 0 && (j & 3) == 0) {
            int k;

            for (int l = i + j; i < l; j -= k) {
                k = Math.min(j, 28000);
                StringBuffer stringbuffer = new StringBuffer();

                for (int i1 = 0; i1 < k; i1 += 7) {
                    long j1 = 0L;

                    int k1;

                    for (k1 = 0; k1 < 7; ++k1) {
                        j1 <<= 8;
                        byte b0 = i1 + k1 < j ? datainputstream.readByte() : 1;

                        j1 |= (long) b0 & 255L;
                    }

                    for (k1 = 0; k1 < 8; ++k1) {
                        char c0 = (char) ((int) (j1 >>> 7 * (7 - k1) & 127L));

                        if (c0 == 10) {
                            stringbuffer.append("\\n");
                        } else if (c0 == 13) {
                            stringbuffer.append("\\r");
                        } else if (c0 == 92) {
                            stringbuffer.append("\\\\");
                        } else if (c0 == 34) {
                            stringbuffer.append("\\\"");
                        } else if (c0 >= 32 && c0 <= 126) {
                            stringbuffer.append(c0);
                        } else {
                            stringbuffer.append("\\" + toOctal3(c0));
                        }
                    }
                }

                String s = "_data" + ++this.initDataCount;

                this.p("private static final int[] " + s + " = decodeData(\"" + stringbuffer.toString() + "\"," + toHex(k / 4) + ");");
                this.inits.append(JavaSourceCompiler.indents[2] + "initPages(" + s + "," + toHex(i) + "," + (flag ? "true" : "false") + ");\n");
                i += k;
            }

            datainputstream.close();
        } else {
            throw new Compiler.Exn("Data section on weird boundaries");
        }
    }

    private void emitBSS(int i, int j) throws Compiler.Exn {
        if ((i & 3) != 0) {
            throw new Compiler.Exn("BSS section on weird boundaries");
        } else {
            j = j + 3 & -4;
            int k = j / 4;

            this.inits.append(JavaSourceCompiler.indents[2] + "clearPages(" + toHex(i) + "," + toHex(k) + ");\n");
        }
    }

    private void emitInstruction(int i, int j, int k) throws IOException, Compiler.Exn {
        if (j == -1) {
            throw new Error("insn is -1");
        } else {
            int l = j >>> 26 & 255;
            int i1 = j >>> 21 & 31;
            int j1 = j >>> 16 & 31;
            int k1 = j >>> 16 & 31;
            int l1 = j >>> 11 & 31;
            int i2 = j >>> 11 & 31;
            int j2 = j >>> 6 & 31;
            int k2 = j >>> 6 & 31;
            int l2 = j & 63;
            int i3 = j & 67108863;
            int j3 = j & '\uffff';
            int k3 = j << 16 >> 16;

            if (i == -1) {
                this.p("/* Next insn is delay slot */ ");
            }

            if (this.runtimeStats && l != 0) {
                this.p("inc(\"opcode: " + l + "\");");
            }

            switch (l) {
            case 0:
                if (this.runtimeStats && j != 0) {
                    this.p("inc(\"opcode: 0/" + l2 + "\");");
                }

                switch (l2) {
                case 0:
                    if (j != 0) {
                        this.p("r" + l1 + " = r" + j1 + " << " + j2 + ";");
                    }

                    return;

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
                    throw new RuntimeException("Illegal instruction 0/" + l2);

                case 2:
                    this.p("r" + l1 + " = r" + j1 + " >>> " + j2 + ";");
                    return;

                case 3:
                    this.p("r" + l1 + " = r" + j1 + " >> " + j2 + ";");
                    return;

                case 4:
                    this.p("r" + l1 + " = r" + j1 + " << (r" + i1 + "&0x1f);");
                    return;

                case 6:
                    this.p("r" + l1 + " = r" + j1 + " >>> (r" + i1 + "&0x1f);");
                    return;

                case 7:
                    this.p("r" + l1 + " = r" + j1 + " >> (r" + i1 + "&0x1f);");
                    return;

                case 8:
                    if (i == -1) {
                        throw new Error("pc modifying insn in delay slot");
                    }

                    this.emitInstruction(-1, k, -1);
                    if (this.debugCompiler) {
                        this.p("lastPC = " + toHex(i) + ";");
                    }

                    this.p("pc=r" + i1 + ";");
                    this.leaveMethod();
                    this.unreachable = true;
                    return;

                case 9:
                    if (i == -1) {
                        throw new Error("pc modifying insn in delay slot");
                    }

                    this.emitInstruction(-1, k, -1);
                    if (this.debugCompiler) {
                        this.p("lastPC = " + toHex(i) + ";");
                    }

                    this.p("pc=r" + i1 + ";");
                    this.p("r31=" + this.constant(i + 8) + ";");
                    this.leaveMethod();
                    this.unreachable = true;
                    return;

                case 12:
                    this.p("pc = " + toHex(i) + ";");
                    this.p("r2 = syscall(r2,r4,r5,r6,r7,r8,r9);");
                    this.p("if (state != RUNNING) {");
                    ++this.indent;
                    this.p("pc = " + toHex(i + 4) + ";");
                    this.leaveMethod();
                    --this.indent;
                    this.p("}");
                    return;

                case 13:
                    this.p("throw new ExecutionException(\"Break\");");
                    this.unreachable = true;
                    return;

                case 16:
                    this.p("r" + l1 + " = hi;");
                    return;

                case 17:
                    this.p("hi = r" + i1 + ";");
                    return;

                case 18:
                    this.p("r" + l1 + " = lo;");
                    return;

                case 19:
                    this.p("lo = r" + i1 + ";");
                    return;

                case 24:
                    this.p("{ long hilo = (long)(r" + i1 + ") * ((long)r" + j1 + "); " + "hi = (int) (hilo >>> 32); " + "lo = (int) hilo; }");
                    return;

                case 25:
                    this.p("{ long hilo = (r" + i1 + " & 0xffffffffL) * (r" + j1 + " & 0xffffffffL); " + "hi = (int) (hilo >>> 32); " + "lo = (int) hilo; } ");
                    return;

                case 26:
                    this.p("hi = r" + i1 + "%r" + j1 + "; lo = r" + i1 + "/r" + j1 + ";");
                    return;

                case 27:
                    this.p("if(r" + j1 + "!=0) {");
                    this.p("hi = (int)((r" + i1 + " & 0xffffffffL) % (r" + j1 + " & 0xffffffffL)); " + "lo = (int)((r" + i1 + " & 0xffffffffL) / (r" + j1 + " & 0xffffffffL));");
                    this.p("}");
                    return;

                case 32:
                    throw new Compiler.Exn("ADD (add with oveflow trap) not suported");

                case 33:
                    this.p("r" + l1 + " = r" + i1 + " + r" + j1 + ";");
                    return;

                case 34:
                    throw new Compiler.Exn("SUB (add with oveflow trap) not suported");

                case 35:
                    this.p("r" + l1 + " = r" + i1 + " - r" + j1 + ";");
                    return;

                case 36:
                    this.p("r" + l1 + " = r" + i1 + " & r" + j1 + ";");
                    return;

                case 37:
                    this.p("r" + l1 + " = r" + i1 + " | r" + j1 + ";");
                    return;

                case 38:
                    this.p("r" + l1 + " = r" + i1 + " ^ r" + j1 + ";");
                    return;

                case 39:
                    this.p("r" + l1 + " = ~(r" + i1 + " | r" + j1 + ");");
                    return;

                case 42:
                    this.p("r" + l1 + " = r" + i1 + " < r" + j1 + " ? 1 : 0;");
                    return;

                case 43:
                    this.p("r" + l1 + " = ((r" + i1 + " & 0xffffffffL) < (r" + j1 + " & 0xffffffffL)) ? 1 : 0;");
                    return;
                }

            case 1:
                switch (j1) {
                case 0:
                    if (i == -1) {
                        throw new Error("pc modifying insn in delay slot");
                    }

                    this.p("if(r" + i1 + " < 0) {");
                    ++this.indent;
                    this.emitInstruction(-1, k, -1);
                    this.branch(i, i + k3 * 4 + 4);
                    --this.indent;
                    this.p("}");
                    return;

                case 1:
                    if (i == -1) {
                        throw new Error("pc modifying insn in delay slot");
                    }

                    this.p("if(r" + i1 + " >= 0) {");
                    ++this.indent;
                    this.emitInstruction(-1, k, -1);
                    this.branch(i, i + k3 * 4 + 4);
                    --this.indent;
                    this.p("}");
                    return;

                case 16:
                    if (i == -1) {
                        throw new Error("pc modifying insn in delay slot");
                    }

                    this.p("if(r" + i1 + " < 0) {");
                    ++this.indent;
                    this.emitInstruction(-1, k, -1);
                    this.p("r31=" + this.constant(i + 8) + ";");
                    this.branch(i, i + k3 * 4 + 4);
                    --this.indent;
                    this.p("}");
                    return;

                case 17:
                    if (i == -1) {
                        throw new Error("pc modifying insn in delay slot");
                    }

                    this.p("if(r" + i1 + " >= 0) {");
                    ++this.indent;
                    this.emitInstruction(-1, k, -1);
                    this.p("r31=" + this.constant(i + 8) + ";");
                    this.branch(i, i + k3 * 4 + 4);
                    --this.indent;
                    this.p("}");
                    return;

                default:
                    throw new RuntimeException("Illegal Instruction 1/" + j1);
                }

            case 2:
                if (i == -1) {
                    throw new Error("pc modifying insn in delay slot");
                }

                this.emitInstruction(-1, k, -1);
                this.branch(i, i & -268435456 | i3 << 2);
                this.unreachable = true;
                break;

            case 3:
                if (i == -1) {
                    throw new Error("pc modifying insn in delay slot");
                }

                int l3 = i & -268435456 | i3 << 2;

                this.emitInstruction(-1, k, -1);
                this.p("r31=" + this.constant(i + 8) + ";");
                this.branch(i, l3);
                this.unreachable = true;
                break;

            case 4:
                if (i == -1) {
                    throw new Error("pc modifying insn in delay slot");
                }

                this.p("if(r" + i1 + " == r" + j1 + ") {");
                ++this.indent;
                this.emitInstruction(-1, k, -1);
                this.branch(i, i + k3 * 4 + 4);
                --this.indent;
                this.p("}");
                break;

            case 5:
                if (i == -1) {
                    throw new Error("pc modifying insn in delay slot");
                }

                this.p("if(r" + i1 + " != r" + j1 + ") {");
                ++this.indent;
                this.emitInstruction(-1, k, -1);
                this.branch(i, i + k3 * 4 + 4);
                --this.indent;
                this.p("}");
                break;

            case 6:
                if (i == -1) {
                    throw new Error("pc modifying insn in delay slot");
                }

                this.p("if(r" + i1 + " <= 0) {");
                ++this.indent;
                this.emitInstruction(-1, k, -1);
                this.branch(i, i + k3 * 4 + 4);
                --this.indent;
                this.p("}");
                break;

            case 7:
                if (i == -1) {
                    throw new Error("pc modifying insn in delay slot");
                }

                this.p("if(r" + i1 + " > 0) {");
                ++this.indent;
                this.emitInstruction(-1, k, -1);
                this.branch(i, i + k3 * 4 + 4);
                --this.indent;
                this.p("}");
                break;

            case 8:
                this.p("r" + j1 + " = r" + i1 + " + " + k3 + ";");
                break;

            case 9:
                this.p("r" + j1 + " = r" + i1 + " + " + k3 + ";");
                break;

            case 10:
                this.p("r" + j1 + " = r" + i1 + " < " + k3 + " ? 1 : 0;");
                break;

            case 11:
                this.p("r" + j1 + " = (r" + i1 + "&0xffffffffL) < (" + k3 + "&0xffffffffL) ? 1 : 0;");
                break;

            case 12:
                this.p("r" + j1 + " = r" + i1 + " & " + j3 + ";");
                break;

            case 13:
                this.p("r" + j1 + " = r" + i1 + " | " + j3 + ";");
                break;

            case 14:
                this.p("r" + j1 + " = r" + i1 + " ^ " + j3 + ";");
                break;

            case 15:
                this.p("r" + j1 + " = " + j3 + " << 16;");
                break;

            case 16:
                throw new Compiler.Exn("TLB/Exception support not implemented");

            case 17:
                switch (i1) {
                case 0:
                    this.p("r" + j1 + " = f" + l1 + ";");
                    return;

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
                    throw new Compiler.Exn("Invalid Instruction 17/" + i1);

                case 2:
                    if (i2 != 31) {
                        throw new Compiler.Exn("FCR " + i2 + " unavailable");
                    }

                    this.p("r" + j1 + " = fcsr;");
                    return;

                case 4:
                    this.p("f" + l1 + " = r" + j1 + ";");
                    return;

                case 6:
                    if (i2 != 31) {
                        throw new Compiler.Exn("FCR " + i2 + " unavailable");
                    }

                    this.p("fcsr = r" + j1 + ";");
                    return;

                case 8:
                    int i4 = j >>> 16 & 1;

                    this.p("if(((fcsr&0x800000)!=0) == (" + i4 + "!=0)) {");
                    ++this.indent;
                    this.emitInstruction(-1, k, -1);
                    this.branch(i, i + k3 * 4 + 4);
                    --this.indent;
                    this.p("}");
                    return;

                case 16:
                    switch (l2) {
                    case 0:
                        this.p(setFloat(k2, getFloat(i2) + "+" + getFloat(k1)));
                        return;

                    case 1:
                        this.p(setFloat(k2, getFloat(i2) + "-" + getFloat(k1)));
                        return;

                    case 2:
                        this.p(setFloat(k2, getFloat(i2) + "*" + getFloat(k1)));
                        return;

                    case 3:
                        this.p(setFloat(k2, getFloat(i2) + "/" + getFloat(k1)));
                        return;

                    case 5:
                        this.p(setFloat(k2, "Math.abs(" + getFloat(i2) + ")"));
                        return;

                    case 6:
                        this.p("f" + k2 + " = f" + i2 + "; // MOV.S");
                        return;

                    case 7:
                        this.p(setFloat(k2, "-" + getFloat(i2)));
                        return;

                    case 33:
                        this.p(setDouble(k2, "(float)" + getFloat(i2)));
                        return;

                    case 36:
                        this.p("switch(fcsr & 3) {");
                        ++this.indent;
                        this.p("case 0: f" + k2 + " = (int)Math.floor(" + getFloat(i2) + "+0.5); break; // Round to nearest");
                        this.p("case 1: f" + k2 + " = (int)" + getFloat(i2) + "; break; // Round towards zero");
                        this.p("case 2: f" + k2 + " = (int)Math.ceil(" + getFloat(i2) + "); break; // Round towards plus infinity");
                        this.p("case 3: f" + k2 + " = (int)Math.floor(" + getFloat(i2) + "); break; // Round towards minus infinity");
                        --this.indent;
                        this.p("}");
                        return;

                    case 50:
                        this.p("fcsr = (fcsr&~0x800000) | ((" + getFloat(i2) + "==" + getFloat(k1) + ") ? 0x800000 : 0x000000);");
                        return;

                    case 60:
                        this.p("fcsr = (fcsr&~0x800000) | ((" + getFloat(i2) + "<" + getFloat(k1) + ") ? 0x800000 : 0x000000);");
                        return;

                    case 62:
                        this.p("fcsr = (fcsr&~0x800000) | ((" + getFloat(i2) + "<=" + getFloat(k1) + ") ? 0x800000 : 0x000000);");
                        return;

                    default:
                        throw new Compiler.Exn("Invalid Instruction 17/" + i1 + "/" + l2);
                    }

                case 17:
                    switch (l2) {
                    case 0:
                        this.p(setDouble(k2, getDouble(i2) + "+" + getDouble(k1)));
                        return;

                    case 1:
                        this.p(setDouble(k2, getDouble(i2) + "-" + getDouble(k1)));
                        return;

                    case 2:
                        this.p(setDouble(k2, getDouble(i2) + "*" + getDouble(k1)));
                        return;

                    case 3:
                        this.p(setDouble(k2, getDouble(i2) + "/" + getDouble(k1)));
                        return;

                    case 5:
                        this.p(setDouble(k2, "Math.abs(" + getDouble(i2) + ")"));
                        return;

                    case 6:
                        this.p("f" + k2 + " = f" + i2 + ";");
                        this.p("f" + (k2 + 1) + " = f" + (i2 + 1) + ";");
                        return;

                    case 7:
                        this.p(setDouble(k2, "-" + getDouble(i2)));
                        return;

                    case 32:
                        this.p(setFloat(k2, "(float)" + getDouble(i2)));
                        return;

                    case 36:
                        this.p("switch(fcsr & 3) {");
                        ++this.indent;
                        this.p("case 0: f" + k2 + " = (int)Math.floor(" + getDouble(i2) + "+0.5); break; // Round to nearest");
                        this.p("case 1: f" + k2 + " = (int)" + getDouble(i2) + "; break; // Round towards zero");
                        this.p("case 2: f" + k2 + " = (int)Math.ceil(" + getDouble(i2) + "); break; // Round towards plus infinity");
                        this.p("case 3: f" + k2 + " = (int)Math.floor(" + getDouble(i2) + "); break; // Round towards minus infinity");
                        --this.indent;
                        this.p("}");
                        return;

                    case 50:
                        this.p("fcsr = (fcsr&~0x800000) | ((" + getDouble(i2) + "==" + getDouble(k1) + ") ? 0x800000 : 0x000000);");
                        return;

                    case 60:
                        this.p("fcsr = (fcsr&~0x800000) | ((" + getDouble(i2) + "<" + getDouble(k1) + ") ? 0x800000 : 0x000000);");
                        return;

                    case 62:
                        this.p("fcsr = (fcsr&~0x800000) | ((" + getDouble(i2) + "<=" + getDouble(k1) + ") ? 0x800000 : 0x000000);");
                        return;

                    default:
                        throw new Compiler.Exn("Invalid Instruction 17/" + i1 + "/" + l2);
                    }

                case 20:
                    switch (l2) {
                    case 32:
                        this.p(" // CVS.S.W");
                        this.p(setFloat(k2, "((float)f" + i2 + ")"));
                        return;

                    case 33:
                        this.p(setDouble(k2, "((double)f" + i2 + ")"));
                        return;

                    default:
                        throw new Compiler.Exn("Invalid Instruction 17/" + i1 + "/" + l2);
                    }
                }

            case 18:
            case 19:
                throw new Compiler.Exn("coprocessor 2 and 3 instructions not available");

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
                throw new Compiler.Exn("Invalid Instruction: " + l + " at " + toHex(i));

            case 32:
                if (this.runtimeStats) {
                    this.p("inc(\"LB\");");
                }

                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp>>>(((~addr)&3)<<3)) & 0xff;");
                this.p("if((tmp&0x80)!=0) tmp |= 0xffffff00; /* sign extend */");
                this.p("r" + j1 + " = tmp;");
                break;

            case 33:
                if (this.runtimeStats) {
                    this.p("inc(\"LH\");");
                }

                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp>>>(((~addr)&2)<<3)) & 0xffff;");
                this.p("if((tmp&0x8000)!=0) tmp |= 0xffff0000; /* sign extend */");
                this.p("r" + j1 + " = tmp;");
                break;

            case 34:
                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("r" + j1 + " = (r" + j1 + "&(0x00ffffff>>>(((~addr)&3)<<3)))|(tmp<<((addr&3)<<3));");
                break;

            case 35:
                if (this.runtimeStats) {
                    this.p("inc(\"LW\");");
                }

                this.memRead("r" + i1 + "+" + k3, "r" + j1);
                break;

            case 36:
                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp>>>(((~addr)&3)<<3)) & 0xff;");
                this.p("r" + j1 + " = tmp;");
                break;

            case 37:
                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp>>>(((~addr)&2)<<3)) & 0xffff;");
                this.p("r" + j1 + " = tmp;");
                break;

            case 38:
                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("r" + j1 + " = (r" + j1 + "&(0xffffff00<<((addr&3)<<3)))|(tmp>>>(((~addr)&3)<<3));");
                break;

            case 40:
                if (this.runtimeStats) {
                    this.p("inc(\"SB\");");
                }

                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp&~(0xff000000>>>((addr&3)<<3)))|((r" + j1 + "&0xff)<<(((~addr)&3)<<3));");
                this.memWrite("addr", "tmp");
                break;

            case 41:
                if (this.runtimeStats) {
                    this.p("inc(\"SH\");");
                }

                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp&(0xffff<<((addr&2)<<3)))|((r" + j1 + "&0xffff)<<(((~addr)&2)<<3));");
                this.memWrite("addr", "tmp");
                break;

            case 42:
                this.p(" // SWL");
                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp&(0xffffff00<<(((~addr)&3)<<3)))|(r" + j1 + ">>>((addr&3)<<3));");
                this.memWrite("addr", "tmp");
                break;

            case 43:
                if (this.runtimeStats) {
                    this.p("inc(\"SW\");");
                }

                this.memWrite("r" + i1 + "+" + k3, "r" + j1);
                break;

            case 46:
                this.p(" // SWR");
                this.p("addr=r" + i1 + "+" + k3 + ";");
                this.memRead("addr", "tmp");
                this.p("tmp = (tmp&(0x00ffffff>>>((addr&3)<<3)))|(r" + j1 + "<<(((~addr)&3)<<3));");
                this.memWrite("addr", "tmp");
                break;

            case 48:
                this.memRead("r" + i1 + "+" + k3, "r" + j1);
                break;

            case 49:
                this.memRead("r" + i1 + "+" + k3, "f" + j1);
                break;

            case 56:
                this.memWrite("r" + i1 + "+" + k3, "r" + j1);
                this.p("r" + j1 + "=1;");
                break;

            case 57:
                this.memWrite("r" + i1 + "+" + k3, "f" + j1);
            }

        }
    }

    private void memWrite(String s, String s1) {
        if (this.nullPointerCheck) {
            this.p("nullPointerCheck(" + s + ");");
        }

        if (this.onePage) {
            this.p("page[(" + s + ")>>>2] = " + s1 + ";");
        } else if (this.fastMem) {
            this.p("writePages[(" + s + ")>>>" + this.pageShift + "][((" + s + ")>>>2)&" + toHex((this.pageSize >> 2) - 1) + "] = " + s1 + ";");
        } else {
            this.p("unsafeMemWrite(" + s + "," + s1 + ");");
        }

    }

    private void memRead(String s, String s1) {
        if (this.nullPointerCheck) {
            this.p("nullPointerCheck(" + s + ");");
        }

        if (this.onePage) {
            this.p(s1 + "= page[(" + s + ")>>>2];");
        } else if (this.fastMem) {
            this.p(s1 + " = readPages[(" + s + ")>>>" + this.pageShift + "][((" + s + ")>>>2)&" + toHex((this.pageSize >> 2) - 1) + "];");
        } else {
            this.p(s1 + " = unsafeMemRead(" + s + ");");
        }

    }

    private static String getFloat(int i) {
        return "(Float.intBitsToFloat(f" + i + "))";
    }

    private static String getDouble(int i) {
        return "(Double.longBitsToDouble(((f" + (i + 1) + "&0xffffffffL) << 32) | (f" + i + "&0xffffffffL)))";
    }

    private static String setFloat(int i, String s) {
        return "f" + i + "=Float.floatToRawIntBits(" + s + ");";
    }

    private static String setDouble(int i, String s) {
        return "{ long l = Double.doubleToLongBits(" + s + "); " + "f" + (i + 1) + " = (int)(l >>> 32); f" + i + " = (int)l; }";
    }

    static {
        String s = "";

        for (int i = 0; i < JavaSourceCompiler.indents.length; s = s + "    ") {
            JavaSourceCompiler.indents[i] = s;
            ++i;
        }

    }
}
