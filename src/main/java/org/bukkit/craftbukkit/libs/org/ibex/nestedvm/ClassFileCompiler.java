package org.bukkit.craftbukkit.libs.org.ibex.nestedvm;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.CGConst;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.ClassFile;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.MethodGen;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.Type;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.ClassFile.Exn;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.MethodGen.Pair;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.MethodGen.PhantomTarget;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.MethodGen.Switch.Lookup;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.MethodGen.Switch.Table;
import org.bukkit.craftbukkit.libs.org.ibex.classgen.Type.Class;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.ELF;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;

public class ClassFileCompiler extends Compiler implements CGConst {

    private static final boolean OPTIMIZE_CP = true;
    private OutputStream os;
    private File outDir;
    private PrintStream warn;
    private final Class me;
    private ClassFile cg;
    private MethodGen clinit;
    private MethodGen init;
    private static int initDataCount;
    private int startOfMethod;
    private int endOfMethod;
    private PhantomTarget returnTarget;
    private PhantomTarget defaultTarget;
    private PhantomTarget[] insnTargets;
    private MethodGen mg;
    private static final int UNREACHABLE = 1;
    private static final int SKIP_NEXT = 2;
    private boolean textDone;
    private static final Float POINT_5_F = new Float(0.5F);
    private static final Double POINT_5_D = new Double(0.5D);
    private static final Long FFFFFFFF = new Long(4294967295L);
    private static final int R = 0;
    private static final int F = 32;
    private static final int HI = 64;
    private static final int LO = 65;
    private static final int FCSR = 66;
    private static final int REG_COUNT = 67;
    private static final String[] regField = new String[] { "r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15", "r16", "r17", "r18", "r19", "r20", "r21", "r22", "r23", "r24", "r25", "r26", "r27", "r28", "r29", "r30", "r31", "f0", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "f10", "f11", "f12", "f13", "f14", "f15", "f16", "f17", "f18", "f19", "f20", "f21", "f22", "f23", "f24", "f25", "f26", "f27", "f28", "f29", "f30", "f31", "hi", "lo", "fcsr"};
    private static final int MAX_LOCALS = 4;
    private static final int LOAD_LENGTH = 3;
    private int[] regLocalMapping;
    private boolean[] regLocalWritten;
    private int nextAvailLocal;
    private int loadsStart;
    private int preSetRegStackPos;
    private int[] preSetRegStack;
    private int memWriteStage;
    private boolean didPreMemRead;
    private boolean preMemReadDoPreWrite;

    public ClassFileCompiler(String s, String s1, OutputStream outputstream) throws IOException {
        this((Seekable) (new Seekable.File(s)), s1, outputstream);
    }

    public ClassFileCompiler(Seekable seekable, String s, OutputStream outputstream) throws IOException {
        this(seekable, s);
        if (outputstream == null) {
            throw new NullPointerException();
        } else {
            this.os = outputstream;
        }
    }

    public ClassFileCompiler(Seekable seekable, String s, File file) throws IOException {
        this(seekable, s);
        if (file == null) {
            throw new NullPointerException();
        } else {
            this.outDir = file;
        }
    }

    private ClassFileCompiler(Seekable seekable, String s) throws IOException {
        super(seekable, s);
        this.warn = System.err;
        this.startOfMethod = 0;
        this.endOfMethod = 0;
        this.regLocalMapping = new int[67];
        this.regLocalWritten = new boolean[67];
        this.preSetRegStack = new int[8];
        this.me = Class.instance(this.fullClassName);
    }

    public void setWarnWriter(PrintStream printstream) {
        this.warn = printstream;
    }

    protected void _go() throws Compiler.Exn, IOException {
        try {
            this.__go();
        } catch (Exn exn) {
            exn.printStackTrace(this.warn);
            throw new Compiler.Exn("Class generation exception: " + exn.toString());
        }
    }

    private void __go() throws Compiler.Exn, IOException {
        if (!this.pruneCases) {
            throw new Compiler.Exn("-o prunecases MUST be enabled for ClassFileCompiler");
        } else {
            Class oclass = Class.instance(this.runtimeClass);

            this.cg = new ClassFile(this.me, oclass, 49);
            if (this.source != null) {
                this.cg.setSourceFile(this.source);
            }

            this.cg.addField("pc", Type.INT, 2);
            this.cg.addField("hi", Type.INT, 2);
            this.cg.addField("lo", Type.INT, 2);
            this.cg.addField("fcsr", Type.INT, 2);

            int i;

            for (i = 1; i < 32; ++i) {
                this.cg.addField("r" + i, Type.INT, 2);
            }

            for (i = 0; i < 32; ++i) {
                this.cg.addField("f" + i, this.singleFloat ? Type.FLOAT : Type.INT, 2);
            }

            this.clinit = this.cg.addMethod("<clinit>", Type.VOID, Type.NO_ARGS, 10);
            this.init = this.cg.addMethod("<init>", Type.VOID, Type.NO_ARGS, 1);
            this.init.add((byte) 42);
            this.init.add((byte) 18, this.pageSize);
            this.init.add((byte) 18, this.totalPages);
            this.init.add((byte) -73, this.me.method("<init>", Type.VOID, new Type[] { Type.INT, Type.INT}));
            this.init.add((byte) -79);
            this.init = this.cg.addMethod("<init>", Type.VOID, new Type[] { Type.BOOLEAN}, 1);
            this.init.add((byte) 42);
            this.init.add((byte) 18, this.pageSize);
            this.init.add((byte) 18, this.totalPages);
            this.init.add((byte) 27);
            this.init.add((byte) -73, this.me.method("<init>", Type.VOID, new Type[] { Type.INT, Type.INT, Type.BOOLEAN}));
            this.init.add((byte) -79);
            this.init = this.cg.addMethod("<init>", Type.VOID, new Type[] { Type.INT, Type.INT}, 1);
            this.init.add((byte) 42);
            this.init.add((byte) 27);
            this.init.add((byte) 28);
            this.init.add((byte) 3);
            this.init.add((byte) -73, this.me.method("<init>", Type.VOID, new Type[] { Type.INT, Type.INT, Type.BOOLEAN}));
            this.init.add((byte) -79);
            this.init = this.cg.addMethod("<init>", Type.VOID, new Type[] { Type.INT, Type.INT, Type.BOOLEAN}, 1);
            this.init.add((byte) 42);
            this.init.add((byte) 27);
            this.init.add((byte) 28);
            this.init.add((byte) 29);
            this.init.add((byte) -73, oclass.method("<init>", Type.VOID, new Type[] { Type.INT, Type.INT, Type.BOOLEAN}));
            if (this.onePage) {
                this.cg.addField("page", Type.INT.makeArray(), 18);
                this.init.add((byte) 42);
                this.init.add((byte) 89);
                this.init.add((byte) -76, this.me.field("readPages", Type.INT.makeArray(2)));
                this.init.add((byte) 18, 0);
                this.init.add((byte) 50);
                this.init.add((byte) -75, this.me.field("page", Type.INT.makeArray()));
            }

            if (this.supportCall) {
                this.cg.addField("symbols", Class.instance(this.hashClass), 26);
            }

            i = 0;

            for (int j = 0; j < this.elf.sheaders.length; ++j) {
                ELF.SHeader elf_sheader = this.elf.sheaders[j];
                String s = elf_sheader.name;

                if (elf_sheader.addr != 0) {
                    i = Math.max(i, elf_sheader.addr + elf_sheader.size);
                    if (s.equals(".text")) {
                        this.emitText(elf_sheader.addr, new DataInputStream(elf_sheader.getInputStream()), elf_sheader.size);
                    } else if (!s.equals(".data") && !s.equals(".sdata") && !s.equals(".rodata") && !s.equals(".ctors") && !s.equals(".dtors")) {
                        if (!s.equals(".bss") && !s.equals(".sbss")) {
                            throw new Compiler.Exn("Unknown segment: " + s);
                        }

                        this.emitBSS(elf_sheader.addr, elf_sheader.size);
                    } else {
                        this.emitData(elf_sheader.addr, new DataInputStream(elf_sheader.getInputStream()), elf_sheader.size, s.equals(".rodata"));
                    }
                }
            }

            this.init.add((byte) -79);
            int k;

            if (this.supportCall) {
                Class oclass1 = Class.instance(this.hashClass);

                this.clinit.add((byte) -69, oclass1);
                this.clinit.add((byte) 89);
                this.clinit.add((byte) 89);
                this.clinit.add((byte) -73, oclass1.method("<init>", Type.VOID, Type.NO_ARGS));
                this.clinit.add((byte) -77, this.me.field("symbols", oclass1));
                ELF.Symbol[] aelf_symbol = this.elf.getSymtab().symbols;

                for (k = 0; k < aelf_symbol.length; ++k) {
                    ELF.Symbol elf_symbol = aelf_symbol[k];

                    if (elf_symbol.type == 2 && elf_symbol.binding == 1 && (elf_symbol.name.equals("_call_helper") || !elf_symbol.name.startsWith("_"))) {
                        this.clinit.add((byte) 89);
                        this.clinit.add((byte) 18, elf_symbol.name);
                        this.clinit.add((byte) -69, Type.INTEGER_OBJECT);
                        this.clinit.add((byte) 89);
                        this.clinit.add((byte) 18, elf_symbol.addr);
                        this.clinit.add((byte) -73, Type.INTEGER_OBJECT.method("<init>", Type.VOID, new Type[] { Type.INT}));
                        this.clinit.add((byte) -74, oclass1.method("put", Type.OBJECT, new Type[] { Type.OBJECT, Type.OBJECT}));
                        this.clinit.add((byte) 87);
                    }
                }

                this.clinit.add((byte) 87);
            }

            this.clinit.add((byte) -79);
            ELF.SHeader elf_sheader1 = this.elf.sectionWithName(".text");
            MethodGen methodgen = this.cg.addMethod("trampoline", Type.VOID, Type.NO_ARGS, 2);

            k = methodgen.size();
            methodgen.add((byte) 42);
            methodgen.add((byte) -76, this.me.field("state", Type.INT));
            methodgen.add((byte) -103, methodgen.size() + 2);
            methodgen.add((byte) -79);
            methodgen.add((byte) 42);
            methodgen.add((byte) 42);
            methodgen.add((byte) -76, this.me.field("pc", Type.INT));
            methodgen.add((byte) 18, this.methodShift);
            methodgen.add((byte) 124);
            int l = elf_sheader1.addr >>> this.methodShift;
            int i1 = elf_sheader1.addr + elf_sheader1.size + this.maxBytesPerMethod - 1 >>> this.methodShift;
            Table table = new Table(l, i1 - 1);

            methodgen.add((byte) -86, table);

            for (int j1 = l; j1 < i1; ++j1) {
                table.setTargetForVal(j1, methodgen.size());
                methodgen.add((byte) -73, this.me.method("run_" + toHex(j1 << this.methodShift), Type.VOID, Type.NO_ARGS));
                methodgen.add((byte) -89, k);
            }

            table.setDefaultTarget(methodgen.size());
            methodgen.add((byte) 87);
            methodgen.add((byte) -69, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException"));
            methodgen.add((byte) 89);
            methodgen.add((byte) -69, Type.STRINGBUFFER);
            methodgen.add((byte) 89);
            methodgen.add((byte) 18, "Jumped to invalid address in trampoline (r2: ");
            methodgen.add((byte) -73, Type.STRINGBUFFER.method("<init>", Type.VOID, new Type[] { Type.STRING}));
            methodgen.add((byte) 42);
            methodgen.add((byte) -76, this.me.field("r2", Type.INT));
            methodgen.add((byte) -74, Type.STRINGBUFFER.method("append", Type.STRINGBUFFER, new Type[] { Type.INT}));
            methodgen.add((byte) 18, " pc: ");
            methodgen.add((byte) -74, Type.STRINGBUFFER.method("append", Type.STRINGBUFFER, new Type[] { Type.STRING}));
            methodgen.add((byte) 42);
            methodgen.add((byte) -76, this.me.field("pc", Type.INT));
            methodgen.add((byte) -74, Type.STRINGBUFFER.method("append", Type.STRINGBUFFER, new Type[] { Type.INT}));
            methodgen.add((byte) 18, ")");
            methodgen.add((byte) -74, Type.STRINGBUFFER.method("append", Type.STRINGBUFFER, new Type[] { Type.STRING}));
            methodgen.add((byte) -74, Type.STRINGBUFFER.method("toString", Type.STRING, Type.NO_ARGS));
            methodgen.add((byte) -73, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException").method("<init>", Type.VOID, new Type[] { Type.STRING}));
            methodgen.add((byte) -65);
            this.addConstReturnMethod("gp", this.gp.addr);
            this.addConstReturnMethod("entryPoint", this.elf.header.entry);
            this.addConstReturnMethod("heapStart", i);
            if (this.userInfo != null) {
                this.addConstReturnMethod("userInfoBase", this.userInfo.addr);
                this.addConstReturnMethod("userInfoSize", this.userInfo.size);
            }

            MethodGen methodgen1;
            Class oclass2;

            if (this.supportCall) {
                oclass2 = Class.instance(this.hashClass);
                methodgen1 = this.cg.addMethod("lookupSymbol", Type.INT, new Type[] { Type.STRING}, 4);
                methodgen1.add((byte) -78, this.me.field("symbols", oclass2));
                methodgen1.add((byte) 43);
                methodgen1.add((byte) -74, oclass2.method("get", Type.OBJECT, new Type[] { Type.OBJECT}));
                methodgen1.add((byte) 89);
                int k1 = methodgen1.add((byte) -58);

                methodgen1.add((byte) -64, Type.INTEGER_OBJECT);
                methodgen1.add((byte) -74, Type.INTEGER_OBJECT.method("intValue", Type.INT, Type.NO_ARGS));
                methodgen1.add((byte) -84);
                methodgen1.setArg(k1, methodgen1.size());
                methodgen1.add((byte) 87);
                methodgen1.add((byte) 2);
                methodgen1.add((byte) -84);
            }

            oclass2 = Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$CPUState");
            methodgen1 = this.cg.addMethod("setCPUState", Type.VOID, new Type[] { oclass2}, 4);
            MethodGen methodgen2 = this.cg.addMethod("getCPUState", Type.VOID, new Type[] { oclass2}, 4);

            methodgen1.add((byte) 43);
            methodgen2.add((byte) 43);
            methodgen1.add((byte) -76, oclass2.field("r", Type.INT.makeArray()));
            methodgen2.add((byte) -76, oclass2.field("r", Type.INT.makeArray()));
            methodgen1.add((byte) 77);
            methodgen2.add((byte) 77);

            int l1;

            for (l1 = 1; l1 < 32; ++l1) {
                methodgen1.add((byte) 42);
                methodgen1.add((byte) 44);
                methodgen1.add((byte) 18, l1);
                methodgen1.add((byte) 46);
                methodgen1.add((byte) -75, this.me.field("r" + l1, Type.INT));
                methodgen2.add((byte) 44);
                methodgen2.add((byte) 18, l1);
                methodgen2.add((byte) 42);
                methodgen2.add((byte) -76, this.me.field("r" + l1, Type.INT));
                methodgen2.add((byte) 79);
            }

            methodgen1.add((byte) 43);
            methodgen2.add((byte) 43);
            methodgen1.add((byte) -76, oclass2.field("f", Type.INT.makeArray()));
            methodgen2.add((byte) -76, oclass2.field("f", Type.INT.makeArray()));
            methodgen1.add((byte) 77);
            methodgen2.add((byte) 77);

            for (l1 = 0; l1 < 32; ++l1) {
                methodgen1.add((byte) 42);
                methodgen1.add((byte) 44);
                methodgen1.add((byte) 18, l1);
                methodgen1.add((byte) 46);
                if (this.singleFloat) {
                    methodgen1.add((byte) -72, Type.FLOAT_OBJECT.method("intBitsToFloat", Type.FLOAT, new Type[] { Type.INT}));
                }

                methodgen1.add((byte) -75, this.me.field("f" + l1, this.singleFloat ? Type.FLOAT : Type.INT));
                methodgen2.add((byte) 44);
                methodgen2.add((byte) 18, l1);
                methodgen2.add((byte) 42);
                methodgen2.add((byte) -76, this.me.field("f" + l1, this.singleFloat ? Type.FLOAT : Type.INT));
                if (this.singleFloat) {
                    methodgen2.add((byte) -72, Type.FLOAT_OBJECT.method("floatToIntBits", Type.INT, new Type[] { Type.FLOAT}));
                }

                methodgen2.add((byte) 79);
            }

            String[] astring = new String[] { "hi", "lo", "fcsr", "pc"};

            for (int i2 = 0; i2 < astring.length; ++i2) {
                methodgen1.add((byte) 42);
                methodgen1.add((byte) 43);
                methodgen1.add((byte) -76, oclass2.field(astring[i2], Type.INT));
                methodgen1.add((byte) -75, this.me.field(astring[i2], Type.INT));
                methodgen2.add((byte) 43);
                methodgen2.add((byte) 42);
                methodgen2.add((byte) -76, this.me.field(astring[i2], Type.INT));
                methodgen2.add((byte) -75, oclass2.field(astring[i2], Type.INT));
            }

            methodgen1.add((byte) -79);
            methodgen2.add((byte) -79);
            MethodGen methodgen3 = this.cg.addMethod("_execute", Type.VOID, Type.NO_ARGS, 4);
            int j2 = methodgen3.size();

            methodgen3.add((byte) 42);
            methodgen3.add((byte) -73, this.me.method("trampoline", Type.VOID, Type.NO_ARGS));
            int k2 = methodgen3.size();

            methodgen3.add((byte) -79);
            int l2 = methodgen3.size();

            methodgen3.add((byte) 76);
            methodgen3.add((byte) -69, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$FaultException"));
            methodgen3.add((byte) 89);
            methodgen3.add((byte) 43);
            methodgen3.add((byte) -73, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$FaultException").method("<init>", Type.VOID, new Type[] { Class.instance("java.lang.RuntimeException")}));
            methodgen3.add((byte) -65);
            methodgen3.addExceptionHandler(j2, k2, l2, Class.instance("java.lang.RuntimeException"));
            methodgen3.addThrow(Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException"));
            MethodGen methodgen4 = this.cg.addMethod("main", Type.VOID, new Type[] { Type.STRING.makeArray()}, 9);

            methodgen4.add((byte) -69, this.me);
            methodgen4.add((byte) 89);
            methodgen4.add((byte) -73, this.me.method("<init>", Type.VOID, Type.NO_ARGS));
            methodgen4.add((byte) 18, this.fullClassName);
            methodgen4.add((byte) 42);
            if (this.unixRuntime) {
                Class oclass3 = Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.UnixRuntime");

                methodgen4.add((byte) -72, oclass3.method("runAndExec", Type.INT, new Type[] { oclass3, Type.STRING, Type.STRING.makeArray()}));
            } else {
                methodgen4.add((byte) -74, this.me.method("run", Type.INT, new Type[] { Type.STRING, Type.STRING.makeArray()}));
            }

            methodgen4.add((byte) -72, Class.instance("java.lang.System").method("exit", Type.VOID, new Type[] { Type.INT}));
            methodgen4.add((byte) -79);
            if (this.outDir != null) {
                if (!this.outDir.isDirectory()) {
                    throw new IOException("" + this.outDir + " isn\'t a directory");
                }

                this.cg.dump(this.outDir);
            } else {
                this.cg.dump(this.os);
            }

        }
    }

    private void addConstReturnMethod(String s, int i) {
        MethodGen methodgen = this.cg.addMethod(s, Type.INT, Type.NO_ARGS, 4);

        methodgen.add((byte) 18, i);
        methodgen.add((byte) -84);
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
                        stringbuffer.append((char) ((int) (j1 >>> 7 * (7 - k1) & 127L)));
                    }
                }

                String s = "_data" + ++ClassFileCompiler.initDataCount;

                this.cg.addField(s, Type.INT.makeArray(), 26);
                this.clinit.add((byte) 18, stringbuffer.toString());
                this.clinit.add((byte) 18, k / 4);
                this.clinit.add((byte) -72, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime").method("decodeData", Type.INT.makeArray(), new Type[] { Type.STRING, Type.INT}));
                this.clinit.add((byte) -77, this.me.field(s, Type.INT.makeArray()));
                this.init.add((byte) 42);
                this.init.add((byte) -78, this.me.field(s, Type.INT.makeArray()));
                this.init.add((byte) 18, i);
                this.init.add((byte) 18, flag ? 1 : 0);
                this.init.add((byte) -74, this.me.method("initPages", Type.VOID, new Type[] { Type.INT.makeArray(), Type.INT, Type.BOOLEAN}));
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

            this.init.add((byte) 42);
            this.init.add((byte) 18, i);
            this.init.add((byte) 18, k);
            this.init.add((byte) -74, this.me.method("clearPages", Type.VOID, new Type[] { Type.INT, Type.INT}));
        }
    }

    private boolean jumpable(int i) {
        return this.jumpableAddresses.get(new Integer(i)) != null;
    }

    private void emitText(int i, DataInputStream datainputstream, int j) throws Compiler.Exn, IOException {
        if (this.textDone) {
            throw new Compiler.Exn("Multiple text segments");
        } else {
            this.textDone = true;
            if ((i & 3) == 0 && (j & 3) == 0) {
                int k = j / 4;
                int l = -1;
                boolean flag = true;
                boolean flag1 = false;

                for (int i1 = 0; i1 < k; i += 4) {
                    int j1 = flag ? datainputstream.readInt() : l;

                    l = i1 == k - 1 ? -1 : datainputstream.readInt();
                    if (i >= this.endOfMethod) {
                        this.endMethod(i, flag1);
                        this.startMethod(i);
                    }

                    label70: {
                        if (this.insnTargets[i1 % this.maxInsnPerMethod] != null) {
                            this.insnTargets[i1 % this.maxInsnPerMethod].setTarget(this.mg.size());
                            flag1 = false;
                        } else if (flag1) {
                            break label70;
                        }

                        try {
                            int k1 = this.emitInstruction(i, j1, l);

                            flag1 = (k1 & 1) != 0;
                            flag = (k1 & 2) != 0;
                        } catch (Compiler.Exn compiler_exn) {
                            compiler_exn.printStackTrace(this.warn);
                            this.warn.println("Exception at " + toHex(i));
                            throw compiler_exn;
                        } catch (RuntimeException runtimeexception) {
                            this.warn.println("Exception at " + toHex(i));
                            throw runtimeexception;
                        }

                        if (flag) {
                            i += 4;
                            ++i1;
                        }
                    }

                    ++i1;
                }

                this.endMethod(0, flag1);
                datainputstream.close();
            } else {
                throw new Compiler.Exn("Section on weird boundaries");
            }
        }
    }

    private void startMethod(int i) {
        this.startOfMethod = i & this.methodMask;
        this.endOfMethod = this.startOfMethod + this.maxBytesPerMethod;
        this.mg = this.cg.addMethod("run_" + toHex(this.startOfMethod), Type.VOID, Type.NO_ARGS, 18);
        if (this.onePage) {
            this.mg.add((byte) 42);
            this.mg.add((byte) -76, this.me.field("page", Type.INT.makeArray()));
            this.mg.add((byte) 77);
        } else {
            this.mg.add((byte) 42);
            this.mg.add((byte) -76, this.me.field("readPages", Type.INT.makeArray(2)));
            this.mg.add((byte) 77);
            this.mg.add((byte) 42);
            this.mg.add((byte) -76, this.me.field("writePages", Type.INT.makeArray(2)));
            this.mg.add((byte) 78);
        }

        this.returnTarget = new PhantomTarget();
        this.insnTargets = new PhantomTarget[this.maxBytesPerMethod / 4];
        int[] aint = new int[this.maxBytesPerMethod / 4];
        Object[] aobject = new Object[this.maxBytesPerMethod / 4];
        int j = 0;

        for (int k = i; k < this.endOfMethod; k += 4) {
            if (this.jumpable(k)) {
                aobject[j] = this.insnTargets[(k - this.startOfMethod) / 4] = new PhantomTarget();
                aint[j] = k;
                ++j;
            }
        }

        Lookup lookup = new Lookup(j);

        System.arraycopy(aint, 0, lookup.vals, 0, j);
        System.arraycopy(aobject, 0, lookup.targets, 0, j);
        lookup.setDefaultTarget(this.defaultTarget = new PhantomTarget());
        this.fixupRegsStart();
        this.mg.add((byte) 42);
        this.mg.add((byte) -76, this.me.field("pc", Type.INT));
        this.mg.add((byte) -85, lookup);
    }

    private void endMethod(int i, boolean flag) {
        if (this.startOfMethod != 0) {
            if (!flag) {
                this.preSetPC();
                this.mg.add((byte) 18, i);
                this.setPC();
                this.jumpableAddresses.put(new Integer(i), Boolean.TRUE);
            }

            this.returnTarget.setTarget(this.mg.size());
            this.fixupRegsEnd();
            this.mg.add((byte) -79);
            this.defaultTarget.setTarget(this.mg.size());
            if (this.debugCompiler) {
                this.mg.add((byte) -69, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException"));
                this.mg.add((byte) 89);
                this.mg.add((byte) -69, Type.STRINGBUFFER);
                this.mg.add((byte) 89);
                this.mg.add((byte) 18, "Jumped to invalid address: ");
                this.mg.add((byte) -73, Type.STRINGBUFFER.method("<init>", Type.VOID, new Type[] { Type.STRING}));
                this.mg.add((byte) 42);
                this.mg.add((byte) -76, this.me.field("pc", Type.INT));
                this.mg.add((byte) -74, Type.STRINGBUFFER.method("append", Type.STRINGBUFFER, new Type[] { Type.INT}));
                this.mg.add((byte) -74, Type.STRINGBUFFER.method("toString", Type.STRING, Type.NO_ARGS));
                this.mg.add((byte) -73, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException").method("<init>", Type.VOID, new Type[] { Type.STRING}));
                this.mg.add((byte) -65);
            } else {
                this.mg.add((byte) -69, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException"));
                this.mg.add((byte) 89);
                this.mg.add((byte) 18, "Jumped to invalid address");
                this.mg.add((byte) -73, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException").method("<init>", Type.VOID, new Type[] { Type.STRING}));
                this.mg.add((byte) -65);
            }

            this.endOfMethod = this.startOfMethod = 0;
        }
    }

    private void leaveMethod() {
        this.mg.add((byte) -89, this.returnTarget);
    }

    private void link(int i) {
        this.preSetReg(31);
        if (this.lessConstants) {
            int j = i + 8 + '耀' & -65536;
            int k = i + 8 - j;

            if (k < -32768 || k > 32767) {
                throw new Error("should never happen " + k);
            }

            this.mg.add((byte) 18, j);
            this.mg.add((byte) 18, k);
            this.mg.add((byte) 96);
        } else {
            this.mg.add((byte) 18, i + 8);
        }

        this.setReg();
    }

    private void branch(int i, int j) {
        if ((i & this.methodMask) == (j & this.methodMask)) {
            this.mg.add((byte) -89, this.insnTargets[(j - this.startOfMethod) / 4]);
        } else {
            this.preSetPC();
            this.mg.add((byte) 18, j);
            this.setPC();
            this.leaveMethod();
        }

    }

    private int doIfInstruction(byte b0, int i, int j, int k) throws Compiler.Exn {
        this.emitInstruction(-1, k, -1);
        int l;

        if ((j & this.methodMask) == (i & this.methodMask)) {
            this.mg.add(b0, this.insnTargets[(j - this.startOfMethod) / 4]);
        } else {
            l = this.mg.add(MethodGen.negate(b0));
            this.branch(i, j);
            this.mg.setArg(l, this.mg.size());
        }

        if (!this.jumpable(i + 4)) {
            return 2;
        } else if (i + 4 == this.endOfMethod) {
            this.jumpableAddresses.put(new Integer(i + 8), Boolean.TRUE);
            this.branch(i, i + 8);
            return 1;
        } else {
            l = this.mg.add((byte) -89);
            this.insnTargets[(i + 4 - this.startOfMethod) / 4].setTarget(this.mg.size());
            this.emitInstruction(-1, k, 1);
            this.mg.setArg(l, this.mg.size());
            return 2;
        }
    }

    private int emitInstruction(int i, int j, int k) throws Compiler.Exn {
        MethodGen methodgen = this.mg;

        if (j == -1) {
            throw new Compiler.Exn("insn is -1");
        } else {
            int l = 0;
            int i1 = j >>> 26 & 255;
            int j1 = j >>> 21 & 31;
            int k1 = j >>> 16 & 31;
            int l1 = j >>> 16 & 31;
            int i2 = j >>> 11 & 31;
            int j2 = j >>> 11 & 31;
            int k2 = j >>> 6 & 31;
            int l2 = j >>> 6 & 31;
            int i3 = j & 63;
            int j3 = j >>> 6 & 1048575;
            int k3 = j & 67108863;
            int l3 = j & '\uffff';
            int i4 = j << 16 >> 16;
            int j4;
            int k4;

            switch (i1) {
            case 0:
                switch (i3) {
                case 0:
                    if (j != 0) {
                        this.preSetReg(0 + i2);
                        this.pushRegWZ(0 + k1);
                        methodgen.add((byte) 18, k2);
                        methodgen.add((byte) 120);
                        this.setReg();
                    }

                    return l;

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
                    throw new Compiler.Exn("Illegal instruction 0/" + i3);

                case 2:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) 18, k2);
                    methodgen.add((byte) 124);
                    this.setReg();
                    return l;

                case 3:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) 18, k2);
                    methodgen.add((byte) 122);
                    this.setReg();
                    return l;

                case 4:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + k1);
                    this.pushRegWZ(0 + j1);
                    methodgen.add((byte) 120);
                    this.setReg();
                    return l;

                case 6:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + k1);
                    this.pushRegWZ(0 + j1);
                    methodgen.add((byte) 124);
                    this.setReg();
                    return l;

                case 7:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + k1);
                    this.pushRegWZ(0 + j1);
                    methodgen.add((byte) 122);
                    this.setReg();
                    return l;

                case 8:
                    if (i == -1) {
                        throw new Compiler.Exn("pc modifying insn in delay slot");
                    }

                    this.emitInstruction(-1, k, -1);
                    this.preSetPC();
                    this.pushRegWZ(0 + j1);
                    this.setPC();
                    this.leaveMethod();
                    l |= 1;
                    return l;

                case 9:
                    if (i == -1) {
                        throw new Compiler.Exn("pc modifying insn in delay slot");
                    }

                    this.emitInstruction(-1, k, -1);
                    this.link(i);
                    this.preSetPC();
                    this.pushRegWZ(0 + j1);
                    this.setPC();
                    this.leaveMethod();
                    l |= 1;
                    return l;

                case 12:
                    this.preSetPC();
                    methodgen.add((byte) 18, i);
                    this.setPC();
                    this.restoreChangedRegs();
                    this.preSetReg(2);
                    methodgen.add((byte) 42);
                    this.pushRegZ(2);
                    this.pushRegZ(4);
                    this.pushRegZ(5);
                    this.pushRegZ(6);
                    this.pushRegZ(7);
                    this.pushRegZ(8);
                    this.pushRegZ(9);
                    methodgen.add((byte) -74, this.me.method("syscall", Type.INT, new Type[] { Type.INT, Type.INT, Type.INT, Type.INT, Type.INT, Type.INT, Type.INT}));
                    this.setReg();
                    methodgen.add((byte) 42);
                    methodgen.add((byte) -76, this.me.field("state", Type.INT));
                    j4 = methodgen.add((byte) -103);
                    this.preSetPC();
                    methodgen.add((byte) 18, i + 4);
                    this.setPC();
                    this.leaveMethod();
                    methodgen.setArg(j4, methodgen.size());
                    return l;

                case 13:
                    methodgen.add((byte) -69, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException"));
                    methodgen.add((byte) 89);
                    methodgen.add((byte) 18, "BREAK Code " + toHex(j3));
                    methodgen.add((byte) -73, Class.instance("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime$ExecutionException").method("<init>", Type.VOID, new Type[] { Type.STRING}));
                    methodgen.add((byte) -65);
                    l |= 1;
                    return l;

                case 16:
                    this.preSetReg(0 + i2);
                    this.pushReg(64);
                    this.setReg();
                    return l;

                case 17:
                    this.preSetReg(64);
                    this.pushRegZ(0 + j1);
                    this.setReg();
                    return l;

                case 18:
                    this.preSetReg(0 + i2);
                    this.pushReg(65);
                    this.setReg();
                    return l;

                case 19:
                    this.preSetReg(65);
                    this.pushRegZ(0 + j1);
                    this.setReg();
                    return l;

                case 24:
                    this.pushRegWZ(0 + j1);
                    methodgen.add((byte) -123);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) -123);
                    methodgen.add((byte) 105);
                    methodgen.add((byte) 92);
                    methodgen.add((byte) -120);
                    if (this.preSetReg(65)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    methodgen.add((byte) 18, 32);
                    methodgen.add((byte) 125);
                    methodgen.add((byte) -120);
                    if (this.preSetReg(64)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    return l;

                case 25:
                    this.pushRegWZ(0 + j1);
                    methodgen.add((byte) -123);
                    methodgen.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                    methodgen.add((byte) 127);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) -123);
                    methodgen.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                    methodgen.add((byte) 127);
                    methodgen.add((byte) 105);
                    methodgen.add((byte) 92);
                    methodgen.add((byte) -120);
                    if (this.preSetReg(65)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    methodgen.add((byte) 18, 32);
                    methodgen.add((byte) 125);
                    methodgen.add((byte) -120);
                    if (this.preSetReg(64)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    return l;

                case 26:
                    this.pushRegWZ(0 + j1);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) 92);
                    methodgen.add((byte) 108);
                    if (this.preSetReg(65)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    methodgen.add((byte) 112);
                    if (this.preSetReg(64)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    return l;

                case 27:
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) 89);
                    this.setTmp();
                    j4 = methodgen.add((byte) -103);
                    this.pushRegWZ(0 + j1);
                    methodgen.add((byte) -123);
                    methodgen.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                    methodgen.add((byte) 127);
                    methodgen.add((byte) 92);
                    this.pushTmp();
                    methodgen.add((byte) -123);
                    methodgen.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                    methodgen.add((byte) 127);
                    methodgen.add((byte) 94);
                    methodgen.add((byte) 109);
                    methodgen.add((byte) -120);
                    if (this.preSetReg(65)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    methodgen.add((byte) 113);
                    methodgen.add((byte) -120);
                    if (this.preSetReg(64)) {
                        methodgen.add((byte) 95);
                    }

                    this.setReg();
                    methodgen.setArg(j4, methodgen.size());
                    return l;

                case 32:
                    throw new Compiler.Exn("ADD (add with oveflow trap) not suported");

                case 33:
                    this.preSetReg(0 + i2);
                    if (k1 != 0 && j1 != 0) {
                        this.pushReg(0 + j1);
                        this.pushReg(0 + k1);
                        methodgen.add((byte) 96);
                    } else if (j1 != 0) {
                        this.pushReg(0 + j1);
                    } else {
                        this.pushRegZ(0 + k1);
                    }

                    this.setReg();
                    return l;

                case 34:
                    throw new Compiler.Exn("SUB (add with oveflow trap) not suported");

                case 35:
                    this.preSetReg(0 + i2);
                    if (k1 != 0 && j1 != 0) {
                        this.pushReg(0 + j1);
                        this.pushReg(0 + k1);
                        methodgen.add((byte) 100);
                    } else if (k1 != 0) {
                        this.pushReg(0 + k1);
                        methodgen.add((byte) 116);
                    } else {
                        this.pushRegZ(0 + j1);
                    }

                    this.setReg();
                    return l;

                case 36:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + j1);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) 126);
                    this.setReg();
                    return l;

                case 37:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + j1);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) -128);
                    this.setReg();
                    return l;

                case 38:
                    this.preSetReg(0 + i2);
                    this.pushRegWZ(0 + j1);
                    this.pushRegWZ(0 + k1);
                    methodgen.add((byte) -126);
                    this.setReg();
                    return l;

                case 39:
                    this.preSetReg(0 + i2);
                    if (j1 == 0 && k1 == 0) {
                        methodgen.add((byte) 18, -1);
                    } else {
                        if (j1 != 0 && k1 != 0) {
                            this.pushReg(0 + j1);
                            this.pushReg(0 + k1);
                            methodgen.add((byte) -128);
                        } else if (j1 != 0) {
                            this.pushReg(0 + j1);
                        } else {
                            this.pushReg(0 + k1);
                        }

                        methodgen.add((byte) 2);
                        methodgen.add((byte) -126);
                    }

                    this.setReg();
                    return l;

                case 42:
                    this.preSetReg(0 + i2);
                    if (j1 != k1) {
                        this.pushRegZ(0 + j1);
                        this.pushRegZ(0 + k1);
                        j4 = methodgen.add((byte) -95);
                        methodgen.add((byte) 3);
                        k4 = methodgen.add((byte) -89);
                        methodgen.setArg(j4, methodgen.add((byte) 4));
                        methodgen.setArg(k4, methodgen.size());
                    } else {
                        methodgen.add((byte) 18, 0);
                    }

                    this.setReg();
                    return l;

                case 43:
                    this.preSetReg(0 + i2);
                    if (j1 != k1) {
                        if (j1 != 0) {
                            this.pushReg(0 + j1);
                            methodgen.add((byte) -123);
                            methodgen.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                            methodgen.add((byte) 127);
                            this.pushReg(0 + k1);
                            methodgen.add((byte) -123);
                            methodgen.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                            methodgen.add((byte) 127);
                            methodgen.add((byte) -108);
                            j4 = methodgen.add((byte) -101);
                        } else {
                            this.pushReg(0 + k1);
                            j4 = methodgen.add((byte) -102);
                        }

                        methodgen.add((byte) 3);
                        k4 = methodgen.add((byte) -89);
                        methodgen.setArg(j4, methodgen.add((byte) 4));
                        methodgen.setArg(k4, methodgen.size());
                    } else {
                        methodgen.add((byte) 18, 0);
                    }

                    this.setReg();
                    return l;
                }

            case 1:
                switch (k1) {
                case 0:
                    if (i == -1) {
                        throw new Compiler.Exn("pc modifying insn in delay slot");
                    }

                    this.pushRegWZ(0 + j1);
                    return this.doIfInstruction((byte) -101, i, i + i4 * 4 + 4, k);

                case 1:
                    if (i == -1) {
                        throw new Compiler.Exn("pc modifying insn in delay slot");
                    }

                    this.pushRegWZ(0 + j1);
                    return this.doIfInstruction((byte) -100, i, i + i4 * 4 + 4, k);

                case 16:
                    if (i == -1) {
                        throw new Compiler.Exn("pc modifying insn in delay slot");
                    }

                    this.pushRegWZ(0 + j1);
                    j4 = methodgen.add((byte) -100);
                    this.emitInstruction(-1, k, -1);
                    this.link(i);
                    this.branch(i, i + i4 * 4 + 4);
                    methodgen.setArg(j4, methodgen.size());
                    return l;

                case 17:
                    if (i == -1) {
                        throw new Compiler.Exn("pc modifying insn in delay slot");
                    }

                    j4 = -1;
                    if (j1 != 0) {
                        this.pushRegWZ(0 + j1);
                        j4 = methodgen.add((byte) -101);
                    }

                    this.emitInstruction(-1, k, -1);
                    this.link(i);
                    this.branch(i, i + i4 * 4 + 4);
                    if (j4 != -1) {
                        methodgen.setArg(j4, methodgen.size());
                    }

                    if (j4 == -1) {
                        l |= 1;
                    }

                    return l;

                default:
                    throw new Compiler.Exn("Illegal Instruction 1/" + k1);
                }

            case 2:
                if (i == -1) {
                    throw new Compiler.Exn("pc modifying insn in delay slot");
                }

                this.emitInstruction(-1, k, -1);
                this.branch(i, i & -268435456 | k3 << 2);
                l |= 1;
                break;

            case 3:
                if (i == -1) {
                    throw new Compiler.Exn("pc modifying insn in delay slot");
                }

                int l4 = i & -268435456 | k3 << 2;

                this.emitInstruction(-1, k, -1);
                this.link(i);
                this.branch(i, l4);
                l |= 1;
                break;

            case 4:
                if (i == -1) {
                    throw new Compiler.Exn("pc modifying insn in delay slot");
                }

                if (j1 != k1) {
                    if (j1 != 0 && k1 != 0) {
                        this.pushReg(0 + j1);
                        this.pushReg(0 + k1);
                        return this.doIfInstruction((byte) -97, i, i + i4 * 4 + 4, k);
                    }

                    this.pushReg(k1 == 0 ? 0 + j1 : 0 + k1);
                    return this.doIfInstruction((byte) -103, i, i + i4 * 4 + 4, k);
                }

                this.emitInstruction(-1, k, -1);
                this.branch(i, i + i4 * 4 + 4);
                l |= 1;
                break;

            case 5:
                if (i == -1) {
                    throw new Compiler.Exn("pc modifying insn in delay slot");
                }

                this.pushRegWZ(0 + j1);
                if (k1 == 0) {
                    return this.doIfInstruction((byte) -102, i, i + i4 * 4 + 4, k);
                }

                this.pushReg(0 + k1);
                return this.doIfInstruction((byte) -96, i, i + i4 * 4 + 4, k);

            case 6:
                if (i == -1) {
                    throw new Compiler.Exn("pc modifying insn in delay slot");
                }

                this.pushRegWZ(0 + j1);
                return this.doIfInstruction((byte) -98, i, i + i4 * 4 + 4, k);

            case 7:
                if (i == -1) {
                    throw new Compiler.Exn("pc modifying insn in delay slot");
                }

                this.pushRegWZ(0 + j1);
                return this.doIfInstruction((byte) -99, i, i + i4 * 4 + 4, k);

            case 8:
                throw new Compiler.Exn("ADDI (add immediate with oveflow trap) not suported");

            case 9:
                if (j1 != 0 && i4 != 0 && j1 == k1 && this.doLocal(k1) && i4 >= -32768 && i4 <= 32767) {
                    this.regLocalWritten[k1] = true;
                    methodgen.add((byte) -124, new Pair(this.getLocalForReg(k1), i4));
                } else {
                    this.preSetReg(0 + k1);
                    this.addiu(j1, i4);
                    this.setReg();
                }
                break;

            case 10:
                this.preSetReg(0 + k1);
                this.pushRegWZ(0 + j1);
                methodgen.add((byte) 18, i4);
                j4 = methodgen.add((byte) -95);
                methodgen.add((byte) 3);
                k4 = methodgen.add((byte) -89);
                methodgen.setArg(j4, methodgen.add((byte) 4));
                methodgen.setArg(k4, methodgen.size());
                this.setReg();
                break;

            case 11:
                this.preSetReg(0 + k1);
                this.pushRegWZ(0 + j1);
                methodgen.add((byte) -123);
                methodgen.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                methodgen.add((byte) 127);
                methodgen.add((byte) 18, new Long((long) i4 & 4294967295L));
                methodgen.add((byte) -108);
                j4 = methodgen.add((byte) -101);
                methodgen.add((byte) 3);
                k4 = methodgen.add((byte) -89);
                methodgen.setArg(j4, methodgen.add((byte) 4));
                methodgen.setArg(k4, methodgen.size());
                this.setReg();
                break;

            case 12:
                this.preSetReg(0 + k1);
                this.pushRegWZ(0 + j1);
                methodgen.add((byte) 18, l3);
                methodgen.add((byte) 126);
                this.setReg();
                break;

            case 13:
                this.preSetReg(0 + k1);
                if (j1 != 0 && l3 != 0) {
                    this.pushReg(0 + j1);
                    methodgen.add((byte) 18, l3);
                    methodgen.add((byte) -128);
                } else if (j1 != 0) {
                    this.pushReg(0 + j1);
                } else {
                    methodgen.add((byte) 18, l3);
                }

                this.setReg();
                break;

            case 14:
                this.preSetReg(0 + k1);
                this.pushRegWZ(0 + j1);
                methodgen.add((byte) 18, l3);
                methodgen.add((byte) -126);
                this.setReg();
                break;

            case 15:
                this.preSetReg(0 + k1);
                methodgen.add((byte) 18, l3 << 16);
                this.setReg();
                break;

            case 16:
                throw new Compiler.Exn("TLB/Exception support not implemented");

            case 17:
                switch (j1) {
                case 0:
                    this.preSetReg(0 + k1);
                    this.pushReg(32 + i2);
                    this.setReg();
                    return l;

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
                    throw new Compiler.Exn("Invalid Instruction 17/" + j1);

                case 2:
                    if (j2 != 31) {
                        throw new Compiler.Exn("FCR " + j2 + " unavailable");
                    }

                    this.preSetReg(0 + k1);
                    this.pushReg(66);
                    this.setReg();
                    return l;

                case 4:
                    this.preSetReg(32 + i2);
                    if (k1 != 0) {
                        this.pushReg(0 + k1);
                    } else {
                        methodgen.add((byte) 3);
                    }

                    this.setReg();
                    return l;

                case 6:
                    if (j2 != 31) {
                        throw new Compiler.Exn("FCR " + j2 + " unavailable");
                    }

                    this.preSetReg(66);
                    this.pushReg(0 + k1);
                    this.setReg();
                    return l;

                case 8:
                    this.pushReg(66);
                    methodgen.add((byte) 18, 8388608);
                    methodgen.add((byte) 126);
                    return this.doIfInstruction((byte) ((j >>> 16 & 1) == 0 ? -103 : -102), i, i + i4 * 4 + 4, k);

                case 16:
                case 17:
                    boolean flag = j1 == 17;

                    switch (i3) {
                    case 0:
                        this.preSetDouble(32 + l2, flag);
                        this.pushDouble(32 + j2, flag);
                        this.pushDouble(32 + l1, flag);
                        methodgen.add((byte) (flag ? 99 : 98));
                        this.setDouble(flag);
                        return l;

                    case 1:
                        this.preSetDouble(32 + l2, flag);
                        this.pushDouble(32 + j2, flag);
                        this.pushDouble(32 + l1, flag);
                        methodgen.add((byte) (flag ? 103 : 102));
                        this.setDouble(flag);
                        return l;

                    case 2:
                        this.preSetDouble(32 + l2, flag);
                        this.pushDouble(32 + j2, flag);
                        this.pushDouble(32 + l1, flag);
                        methodgen.add((byte) (flag ? 107 : 106));
                        this.setDouble(flag);
                        return l;

                    case 3:
                        this.preSetDouble(32 + l2, flag);
                        this.pushDouble(32 + j2, flag);
                        this.pushDouble(32 + l1, flag);
                        methodgen.add((byte) (flag ? 111 : 110));
                        this.setDouble(flag);
                        return l;

                    case 5:
                        this.preSetDouble(32 + l2, flag);
                        this.pushDouble(32 + j2, flag);
                        methodgen.add((byte) (flag ? 92 : 89));
                        methodgen.add((byte) (flag ? 14 : 11));
                        methodgen.add((byte) (flag ? -104 : -106));
                        j4 = methodgen.add((byte) -99);
                        methodgen.add((byte) (flag ? 14 : 11));
                        if (flag) {
                            methodgen.add((byte) 94);
                            methodgen.add((byte) 88);
                        } else {
                            methodgen.add((byte) 95);
                        }

                        methodgen.add((byte) (flag ? 103 : 102));
                        methodgen.setArg(j4, methodgen.size());
                        this.setDouble(flag);
                        return l;

                    case 6:
                        this.preSetReg(32 + l2);
                        this.pushReg(32 + j2);
                        this.setReg();
                        if (flag) {
                            this.preSetReg(32 + l2 + 1);
                            this.pushReg(32 + j2 + 1);
                            this.setReg();
                        }

                        return l;

                    case 7:
                        this.preSetDouble(32 + l2, flag);
                        this.pushDouble(32 + j2, flag);
                        methodgen.add((byte) (flag ? 119 : 118));
                        this.setDouble(flag);
                        return l;

                    case 32:
                        this.preSetFloat(32 + l2);
                        this.pushDouble(32 + j2, flag);
                        if (flag) {
                            methodgen.add((byte) -112);
                        }

                        this.setFloat();
                        return l;

                    case 33:
                        this.preSetDouble(32 + l2);
                        this.pushDouble(32 + j2, flag);
                        if (!flag) {
                            methodgen.add((byte) -115);
                        }

                        this.setDouble();
                        return l;

                    case 36:
                        Table table = new Table(0, 3);

                        this.preSetReg(32 + l2);
                        this.pushDouble(32 + j2, flag);
                        this.pushReg(66);
                        methodgen.add((byte) 6);
                        methodgen.add((byte) 126);
                        methodgen.add((byte) -86, table);
                        table.setTarget(2, methodgen.size());
                        if (!flag) {
                            methodgen.add((byte) -115);
                        }

                        methodgen.add((byte) -72, Class.instance("java.lang.Math").method("ceil", Type.DOUBLE, new Type[] { Type.DOUBLE}));
                        if (!flag) {
                            methodgen.add((byte) -112);
                        }

                        j4 = methodgen.add((byte) -89);
                        table.setTarget(0, methodgen.size());
                        methodgen.add((byte) 18, flag ? ClassFileCompiler.POINT_5_D : ClassFileCompiler.POINT_5_F);
                        methodgen.add((byte) (flag ? 99 : 98));
                        table.setTarget(3, methodgen.size());
                        if (!flag) {
                            methodgen.add((byte) -115);
                        }

                        methodgen.add((byte) -72, Class.instance("java.lang.Math").method("floor", Type.DOUBLE, new Type[] { Type.DOUBLE}));
                        if (!flag) {
                            methodgen.add((byte) -112);
                        }

                        table.setTarget(1, methodgen.size());
                        table.setDefaultTarget(methodgen.size());
                        methodgen.setArg(j4, methodgen.size());
                        methodgen.add((byte) (flag ? -114 : -117));
                        this.setReg();
                        return l;

                    case 50:
                    case 60:
                    case 62:
                        this.preSetReg(66);
                        this.pushReg(66);
                        methodgen.add((byte) 18, -8388609);
                        methodgen.add((byte) 126);
                        this.pushDouble(32 + j2, flag);
                        this.pushDouble(32 + l1, flag);
                        methodgen.add((byte) (flag ? -104 : -106));
                        switch (i3) {
                        case 50:
                            j4 = methodgen.add((byte) -102);
                            break;

                        case 60:
                            j4 = methodgen.add((byte) -100);
                            break;

                        case 62:
                            j4 = methodgen.add((byte) -99);
                            break;

                        default:
                            j4 = -1;
                        }

                        methodgen.add((byte) 18, 8388608);
                        methodgen.add((byte) -128);
                        methodgen.setArg(j4, methodgen.size());
                        this.setReg();
                        return l;

                    default:
                        throw new Compiler.Exn("Invalid Instruction 17/" + j1 + "/" + i3);
                    }

                case 20:
                    switch (i3) {
                    case 32:
                        this.preSetFloat(32 + l2);
                        this.pushReg(32 + j2);
                        methodgen.add((byte) -122);
                        this.setFloat();
                        return l;

                    case 33:
                        this.preSetDouble(32 + l2);
                        this.pushReg(32 + j2);
                        methodgen.add((byte) -121);
                        this.setDouble();
                        return l;

                    default:
                        throw new Compiler.Exn("Invalid Instruction 17/" + j1 + "/" + i3);
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
                throw new Compiler.Exn("Invalid Instruction: " + i1 + " at " + toHex(i));

            case 32:
                this.preSetReg(0 + k1);
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead();
                this.pushTmp();
                this.memRead(true);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) -111);
                this.setReg();
                break;

            case 33:
                this.preSetReg(0 + k1);
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead();
                this.pushTmp();
                this.memRead(true);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 5);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) -109);
                this.setReg();
                break;

            case 34:
                this.preSetReg(0 + k1);
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.pushRegWZ(0 + k1);
                methodgen.add((byte) 18, 16777215);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) 126);
                this.preMemRead();
                this.pushTmp();
                this.memRead(true);
                this.pushTmp();
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 120);
                methodgen.add((byte) -128);
                this.setReg();
                break;

            case 35:
                this.preSetReg(0 + k1);
                this.memRead(0 + j1, i4);
                this.setReg();
                break;

            case 36:
                this.preSetReg(0 + k1);
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead();
                this.pushTmp();
                this.memRead(true);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) 18, 255);
                methodgen.add((byte) 126);
                this.setReg();
                break;

            case 37:
                this.preSetReg(0 + k1);
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead();
                this.pushTmp();
                this.memRead(true);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 5);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) -110);
                this.setReg();
                break;

            case 38:
                this.preSetReg(0 + k1);
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.pushRegWZ(0 + k1);
                methodgen.add((byte) 18, -256);
                this.pushTmp();
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 120);
                methodgen.add((byte) 126);
                this.preMemRead();
                this.pushTmp();
                this.memRead(true);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) -128);
                this.setReg();
                break;

            case 40:
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead(true);
                this.pushTmp();
                this.memRead(true);
                methodgen.add((byte) 18, -16777216);
                this.pushTmp();
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 126);
                if (k1 != 0) {
                    this.pushReg(0 + k1);
                    methodgen.add((byte) 18, 255);
                    methodgen.add((byte) 126);
                } else {
                    methodgen.add((byte) 18, 0);
                }

                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 120);
                methodgen.add((byte) -128);
                this.memWrite();
                break;

            case 41:
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead(true);
                this.pushTmp();
                this.memRead(true);
                methodgen.add((byte) 18, '\uffff');
                this.pushTmp();
                methodgen.add((byte) 5);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 120);
                methodgen.add((byte) 126);
                if (k1 != 0) {
                    this.pushReg(0 + k1);
                    methodgen.add((byte) 18, '\uffff');
                    methodgen.add((byte) 126);
                } else {
                    methodgen.add((byte) 18, 0);
                }

                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 5);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 120);
                methodgen.add((byte) -128);
                this.memWrite();
                break;

            case 42:
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead(true);
                this.pushTmp();
                this.memRead(true);
                methodgen.add((byte) 18, -256);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 120);
                methodgen.add((byte) 126);
                this.pushRegWZ(0 + k1);
                this.pushTmp();
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) -128);
                this.memWrite();
                break;

            case 43:
                this.preMemWrite1();
                this.preMemWrite2(0 + j1, i4);
                this.pushRegZ(0 + k1);
                this.memWrite();
                break;

            case 46:
                this.addiu(0 + j1, i4);
                this.setTmp();
                this.preMemRead(true);
                this.pushTmp();
                this.memRead(true);
                methodgen.add((byte) 18, 16777215);
                this.pushTmp();
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 124);
                methodgen.add((byte) 126);
                this.pushRegWZ(0 + k1);
                this.pushTmp();
                methodgen.add((byte) 2);
                methodgen.add((byte) -126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 126);
                methodgen.add((byte) 6);
                methodgen.add((byte) 120);
                methodgen.add((byte) 120);
                methodgen.add((byte) -128);
                this.memWrite();
                break;

            case 48:
                this.preSetReg(0 + k1);
                this.memRead(0 + j1, i4);
                this.setReg();
                break;

            case 49:
                this.preSetReg(32 + k1);
                this.memRead(0 + j1, i4);
                this.setReg();
                break;

            case 56:
                this.preSetReg(0 + k1);
                this.preMemWrite1();
                this.preMemWrite2(0 + j1, i4);
                this.pushReg(0 + k1);
                this.memWrite();
                methodgen.add((byte) 18, 1);
                this.setReg();
                break;

            case 57:
                this.preMemWrite1();
                this.preMemWrite2(0 + j1, i4);
                this.pushReg(32 + k1);
                this.memWrite();
            }

            return l;
        }
    }

    private boolean doLocal(int i) {
        return i == 2 || i == 3 || i == 4 || i == 29;
    }

    private int getLocalForReg(int i) {
        if (this.regLocalMapping[i] != 0) {
            return this.regLocalMapping[i];
        } else {
            this.regLocalMapping[i] = this.nextAvailLocal++;
            return this.regLocalMapping[i];
        }
    }

    private void fixupRegsStart() {
        int i;

        for (i = 0; i < 67; ++i) {
            this.regLocalMapping[i] = 0;
            this.regLocalWritten[i] = false;
        }

        this.nextAvailLocal = this.onePage ? 4 : 5;
        this.loadsStart = this.mg.size();

        for (i = 0; i < 12; ++i) {
            this.mg.add((byte) 0);
        }

    }

    private void fixupRegsEnd() {
        int i = this.loadsStart;

        for (int j = 0; j < 67; ++j) {
            if (this.regLocalMapping[j] != 0) {
                this.mg.set(i++, (byte) 42);
                this.mg.set(i++, (byte) -76, this.me.field(ClassFileCompiler.regField[j], Type.INT));
                this.mg.set(i++, (byte) 54, this.regLocalMapping[j]);
                if (this.regLocalWritten[j]) {
                    this.mg.add((byte) 42);
                    this.mg.add((byte) 21, this.regLocalMapping[j]);
                    this.mg.add((byte) -75, this.me.field(ClassFileCompiler.regField[j], Type.INT));
                }
            }
        }

    }

    private void restoreChangedRegs() {
        for (int i = 0; i < 67; ++i) {
            if (this.regLocalWritten[i]) {
                this.mg.add((byte) 42);
                this.mg.add((byte) 21, this.regLocalMapping[i]);
                this.mg.add((byte) -75, this.me.field(ClassFileCompiler.regField[i], Type.INT));
            }
        }

    }

    private int pushRegWZ(int i) {
        if (i == 0) {
            this.warn.println("Warning: Pushing r0!");
            (new Exception()).printStackTrace(this.warn);
        }

        return this.pushRegZ(i);
    }

    private int pushRegZ(int i) {
        return i == 0 ? this.mg.add((byte) 3) : this.pushReg(i);
    }

    private int pushReg(int i) {
        int j = this.mg.size();

        if (this.doLocal(i)) {
            this.mg.add((byte) 21, this.getLocalForReg(i));
        } else if (i >= 32 && i <= 63 && this.singleFloat) {
            this.mg.add((byte) 42);
            this.mg.add((byte) -76, this.me.field(ClassFileCompiler.regField[i], Type.FLOAT));
            this.mg.add((byte) -72, Type.FLOAT_OBJECT.method("floatToIntBits", Type.INT, new Type[] { Type.FLOAT}));
        } else {
            this.mg.add((byte) 42);
            this.mg.add((byte) -76, this.me.field(ClassFileCompiler.regField[i], Type.INT));
        }

        return j;
    }

    private boolean preSetReg(int i) {
        this.preSetRegStack[this.preSetRegStackPos] = i;
        ++this.preSetRegStackPos;
        if (this.doLocal(i)) {
            return false;
        } else {
            this.mg.add((byte) 42);
            return true;
        }
    }

    private int setReg() {
        if (this.preSetRegStackPos == 0) {
            throw new RuntimeException("didn\'t do preSetReg");
        } else {
            --this.preSetRegStackPos;
            int i = this.preSetRegStack[this.preSetRegStackPos];
            int j = this.mg.size();

            if (this.doLocal(i)) {
                this.mg.add((byte) 54, this.getLocalForReg(i));
                this.regLocalWritten[i] = true;
            } else if (i >= 32 && i <= 63 && this.singleFloat) {
                this.mg.add((byte) -72, Type.FLOAT_OBJECT.method("intBitsToFloat", Type.FLOAT, new Type[] { Type.INT}));
                this.mg.add((byte) -75, this.me.field(ClassFileCompiler.regField[i], Type.FLOAT));
            } else {
                this.mg.add((byte) -75, this.me.field(ClassFileCompiler.regField[i], Type.INT));
            }

            return j;
        }
    }

    private int preSetPC() {
        return this.mg.add((byte) 42);
    }

    private int setPC() {
        return this.mg.add((byte) -75, this.me.field("pc", Type.INT));
    }

    private int pushFloat(int i) throws Compiler.Exn {
        return this.pushDouble(i, false);
    }

    private int pushDouble(int i, boolean flag) throws Compiler.Exn {
        if (i >= 32 && i < 64) {
            int j = this.mg.size();

            if (flag) {
                if (this.singleFloat) {
                    throw new Compiler.Exn("Double operations not supported when singleFloat is enabled");
                }

                if (i == 63) {
                    throw new Compiler.Exn("Tried to use a double in f31");
                }

                this.pushReg(i + 1);
                this.mg.add((byte) -123);
                this.mg.add((byte) 18, 32);
                this.mg.add((byte) 121);
                this.pushReg(i);
                this.mg.add((byte) -123);
                this.mg.add((byte) 18, ClassFileCompiler.FFFFFFFF);
                this.mg.add((byte) 127);
                this.mg.add((byte) -127);
                this.mg.add((byte) -72, Type.DOUBLE_OBJECT.method("longBitsToDouble", Type.DOUBLE, new Type[] { Type.LONG}));
            } else if (this.singleFloat) {
                this.mg.add((byte) 42);
                this.mg.add((byte) -76, this.me.field(ClassFileCompiler.regField[i], Type.FLOAT));
            } else {
                this.pushReg(i);
                this.mg.add((byte) -72, Class.instance("java.lang.Float").method("intBitsToFloat", Type.FLOAT, new Type[] { Type.INT}));
            }

            return j;
        } else {
            throw new IllegalArgumentException("" + i);
        }
    }

    private void preSetFloat(int i) {
        this.preSetDouble(i, false);
    }

    private void preSetDouble(int i) {
        this.preSetDouble(i, true);
    }

    private void preSetDouble(int i, boolean flag) {
        this.preSetReg(i);
    }

    private int setFloat() throws Compiler.Exn {
        return this.setDouble(false);
    }

    private int setDouble() throws Compiler.Exn {
        return this.setDouble(true);
    }

    private int setDouble(boolean flag) throws Compiler.Exn {
        int i = this.preSetRegStack[this.preSetRegStackPos - 1];

        if (i >= 32 && i < 64) {
            int j = this.mg.size();

            if (flag) {
                if (this.singleFloat) {
                    throw new Compiler.Exn("Double operations not supported when singleFloat is enabled");
                }

                if (i == 63) {
                    throw new Compiler.Exn("Tried to use a double in f31");
                }

                this.mg.add((byte) -72, Type.DOUBLE_OBJECT.method("doubleToLongBits", Type.LONG, new Type[] { Type.DOUBLE}));
                this.mg.add((byte) 92);
                this.mg.add((byte) 18, 32);
                this.mg.add((byte) 125);
                this.mg.add((byte) -120);
                if (this.preSetReg(i + 1)) {
                    this.mg.add((byte) 95);
                }

                this.setReg();
                this.mg.add((byte) -120);
                this.setReg();
            } else if (this.singleFloat) {
                --this.preSetRegStackPos;
                this.mg.add((byte) -75, this.me.field(ClassFileCompiler.regField[i], Type.FLOAT));
            } else {
                this.mg.add((byte) -72, Type.FLOAT_OBJECT.method("floatToRawIntBits", Type.INT, new Type[] { Type.FLOAT}));
                this.setReg();
            }

            return j;
        } else {
            throw new IllegalArgumentException("" + i);
        }
    }

    private void pushTmp() {
        this.mg.add((byte) 27);
    }

    private void setTmp() {
        this.mg.add((byte) 60);
    }

    private void addiu(int i, int j) {
        if (i != 0 && j != 0) {
            this.pushReg(i);
            this.mg.add((byte) 18, j);
            this.mg.add((byte) 96);
        } else if (i != 0) {
            this.pushReg(i);
        } else {
            this.mg.add((byte) 18, j);
        }

    }

    private void preMemWrite1() {
        if (this.memWriteStage != 0) {
            throw new Error("pending preMemWrite1/2");
        } else {
            this.memWriteStage = 1;
            if (this.onePage) {
                this.mg.add((byte) 44);
            } else if (this.fastMem) {
                this.mg.add((byte) 25, 3);
            } else {
                this.mg.add((byte) 42);
            }

        }
    }

    private void preMemWrite2(int i, int j) {
        this.addiu(i, j);
        this.preMemWrite2();
    }

    private void preMemWrite2() {
        this.preMemWrite2(false);
    }

    private void preMemWrite2(boolean flag) {
        if (this.memWriteStage != 1) {
            throw new Error("pending preMemWrite2 or no preMemWrite1");
        } else {
            this.memWriteStage = 2;
            if (this.nullPointerCheck) {
                this.mg.add((byte) 89);
                this.mg.add((byte) 42);
                this.mg.add((byte) 95);
                this.mg.add((byte) -74, this.me.method("nullPointerCheck", Type.VOID, new Type[] { Type.INT}));
            }

            if (this.onePage) {
                this.mg.add((byte) 5);
                this.mg.add((byte) 124);
            } else if (this.fastMem) {
                if (!flag) {
                    this.mg.add((byte) 90);
                }

                this.mg.add((byte) 18, this.pageShift);
                this.mg.add((byte) 124);
                this.mg.add((byte) 50);
                if (flag) {
                    this.pushTmp();
                } else {
                    this.mg.add((byte) 95);
                }

                this.mg.add((byte) 5);
                this.mg.add((byte) 124);
                this.mg.add((byte) 18, (this.pageSize >> 2) - 1);
                this.mg.add((byte) 126);
            }

        }
    }

    private void memWrite() {
        if (this.memWriteStage != 2) {
            throw new Error("didn\'t do preMemWrite1 or preMemWrite2");
        } else {
            this.memWriteStage = 0;
            if (this.onePage) {
                this.mg.add((byte) 79);
            } else if (this.fastMem) {
                this.mg.add((byte) 79);
            } else {
                this.mg.add((byte) -74, this.me.method("unsafeMemWrite", Type.VOID, new Type[] { Type.INT, Type.INT}));
            }

        }
    }

    private void memRead(int i, int j) {
        this.preMemRead();
        this.addiu(i, j);
        this.memRead();
    }

    private void preMemRead() {
        this.preMemRead(false);
    }

    private void preMemRead(boolean flag) {
        if (this.didPreMemRead) {
            throw new Error("pending preMemRead");
        } else {
            this.didPreMemRead = true;
            this.preMemReadDoPreWrite = flag;
            if (this.onePage) {
                this.mg.add((byte) 44);
            } else if (this.fastMem) {
                this.mg.add((byte) 25, flag ? 3 : 2);
            } else {
                this.mg.add((byte) 42);
            }

        }
    }

    private void memRead() {
        this.memRead(false);
    }

    private void memRead(boolean flag) {
        if (!this.didPreMemRead) {
            throw new Error("didn\'t do preMemRead");
        } else {
            this.didPreMemRead = false;
            if (this.preMemReadDoPreWrite) {
                this.memWriteStage = 2;
            }

            if (this.nullPointerCheck) {
                this.mg.add((byte) 89);
                this.mg.add((byte) 42);
                this.mg.add((byte) 95);
                this.mg.add((byte) -74, this.me.method("nullPointerCheck", Type.VOID, new Type[] { Type.INT}));
            }

            if (this.onePage) {
                this.mg.add((byte) 5);
                this.mg.add((byte) 124);
                if (this.preMemReadDoPreWrite) {
                    this.mg.add((byte) 92);
                }

                this.mg.add((byte) 46);
            } else if (this.fastMem) {
                if (!flag) {
                    this.mg.add((byte) 90);
                }

                this.mg.add((byte) 18, this.pageShift);
                this.mg.add((byte) 124);
                this.mg.add((byte) 50);
                if (flag) {
                    this.pushTmp();
                } else {
                    this.mg.add((byte) 95);
                }

                this.mg.add((byte) 5);
                this.mg.add((byte) 124);
                this.mg.add((byte) 18, (this.pageSize >> 2) - 1);
                this.mg.add((byte) 126);
                if (this.preMemReadDoPreWrite) {
                    this.mg.add((byte) 92);
                }

                this.mg.add((byte) 46);
            } else {
                if (this.preMemReadDoPreWrite) {
                    this.mg.add((byte) 92);
                }

                this.mg.add((byte) -74, this.me.method("unsafeMemRead", Type.INT, new Type[] { Type.INT}));
            }

        }
    }
}
