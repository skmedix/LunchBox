package org.bukkit.craftbukkit.libs.org.ibex.nestedvm;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.ELF;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;

public abstract class Compiler implements Registers {

    ELF elf;
    final String fullClassName;
    String source = "unknown.mips.binary";
    boolean fastMem = true;
    int maxInsnPerMethod = 128;
    int maxBytesPerMethod;
    int methodMask;
    int methodShift;
    boolean pruneCases = true;
    boolean assumeTailCalls = true;
    boolean debugCompiler = false;
    boolean printStats = false;
    boolean runtimeStats = false;
    boolean supportCall = true;
    boolean nullPointerCheck = false;
    String runtimeClass = "org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Runtime";
    String hashClass = "java.util.Hashtable";
    boolean unixRuntime;
    boolean lessConstants;
    boolean singleFloat;
    int pageSize = 4096;
    int totalPages = 65536;
    int pageShift;
    boolean onePage;
    Hashtable jumpableAddresses;
    ELF.Symbol userInfo;
    ELF.Symbol gp;
    private boolean used;
    private static String[] options = new String[] { "fastMem", "Enable fast memory access - RuntimeExceptions will be thrown on faults", "nullPointerCheck", "Enables checking at runtime for null pointer accessses (slows things down a bit, only applicable with fastMem)", "maxInsnPerMethod", "Maximum number of MIPS instructions per java method (128 is optimal with Hotspot)", "pruneCases", "Remove unnecessary case 0xAABCCDD blocks from methods - may break some weird code", "assumeTailCalls", "Assume the JIT optimizes tail calls", "optimizedMemcpy", "Use an optimized java version of memcpy where possible", "debugCompiler", "Output information in the generated code for debugging the compiler - will slow down generated code significantly", "printStats", "Output some useful statistics about the compilation", "runtimeStats", "Keep track of some statistics at runtime in the generated code - will slow down generated code significantly", "supportCall", "Keep a stripped down version of the symbol table in the generated code to support the call() method", "runtimeClass", "Full classname of the Runtime class (default: Runtime) - use this is you put Runtime in a package", "hashClass", "Full classname of a Hashtable class (default: java.util.HashMap) - this must support get() and put()", "unixRuntime", "Use the UnixRuntime (has support for fork, wai, du, pipe, etc)", "pageSize", "The page size (must be a power of two)", "totalPages", "Total number of pages (total mem = pageSize*totalPages, must be a power of two)", "onePage", "One page hack (FIXME: document this better)", "lessConstants", "Use less constants at the cost of speed (FIXME: document this better)", "singleFloat", "Support single precision (32-bit) FP ops only"};
    static Class class$org$ibex$nestedvm$Compiler;
    static Class class$java$lang$String;

    public void setSource(String s) {
        this.source = s;
    }

    void maxInsnPerMethodInit() throws Compiler.Exn {
        if ((this.maxInsnPerMethod & this.maxInsnPerMethod - 1) != 0) {
            throw new Compiler.Exn("maxBytesPerMethod is not a power of two");
        } else {
            this.maxBytesPerMethod = this.maxInsnPerMethod * 4;

            for (this.methodMask = ~(this.maxBytesPerMethod - 1); this.maxBytesPerMethod >>> this.methodShift != 1; ++this.methodShift) {
                ;
            }

        }
    }

    void pageSizeInit() throws Compiler.Exn {
        if ((this.pageSize & this.pageSize - 1) != 0) {
            throw new Compiler.Exn("pageSize not a multiple of two");
        } else if ((this.totalPages & this.totalPages - 1) != 0) {
            throw new Compiler.Exn("totalPages not a multiple of two");
        } else {
            while (this.pageSize >>> this.pageShift != 1) {
                ++this.pageShift;
            }

        }
    }

    private static void usage() {
        System.err.println("Usage: java Compiler [-outfile output.java] [-o options] [-dumpoptions] <classname> <binary.mips>");
        System.err.println("-o takes mount(8) like options and can be specified multiple times");
        System.err.println("Available options:");

        for (int i = 0; i < Compiler.options.length; i += 2) {
            System.err.print(Compiler.options[i] + ": " + wrapAndIndent(Compiler.options[i + 1], 16 - Compiler.options[i].length(), 18, 62));
        }

        System.exit(1);
    }

    public static void main(String[] astring) throws IOException {
        String s = null;
        String s1 = null;
        String s2 = null;
        String s3 = null;
        String s4 = null;
        String s5 = null;
        boolean flag = false;

        for (int i = 0; astring.length - i > 0; ++i) {
            if (astring[i].equals("-outfile")) {
                ++i;
                if (i == astring.length) {
                    usage();
                }

                s = astring[i];
            } else if (astring[i].equals("-d")) {
                ++i;
                if (i == astring.length) {
                    usage();
                }

                s1 = astring[i];
            } else if (astring[i].equals("-outformat")) {
                ++i;
                if (i == astring.length) {
                    usage();
                }

                s5 = astring[i];
            } else if (astring[i].equals("-o")) {
                ++i;
                if (i == astring.length) {
                    usage();
                }

                if (s2 != null && s2.length() != 0) {
                    if (astring[i].length() != 0) {
                        s2 = s2 + "," + astring[i];
                    }
                } else {
                    s2 = astring[i];
                }
            } else if (astring[i].equals("-dumpoptions")) {
                flag = true;
            } else if (s3 == null) {
                s3 = astring[i];
            } else if (s4 == null) {
                s4 = astring[i];
            } else {
                usage();
            }
        }

        if (s3 == null || s4 == null) {
            usage();
        }

        Seekable.File seekable_file = new Seekable.File(s4);
        Object object = null;
        FileOutputStream fileoutputstream = null;
        Object object1 = null;

        if (s5 != null && !s5.equals("class")) {
            if (!s5.equals("javasource") && !s5.equals("java")) {
                System.err.println("Unknown output format: " + s5);
                System.exit(1);
            } else {
                object = s == null ? new OutputStreamWriter(System.out) : new FileWriter(s);
                object1 = new JavaSourceCompiler(seekable_file, s3, (Writer) object);
            }
        } else if (s != null) {
            fileoutputstream = new FileOutputStream(s);
            object1 = new ClassFileCompiler(seekable_file, s3, fileoutputstream);
        } else if (s1 != null) {
            File file = new File(s1);

            if (!file.isDirectory()) {
                System.err.println(s1 + " doesn\'t exist or is not a directory");
                System.exit(1);
            }

            object1 = new ClassFileCompiler(seekable_file, s3, file);
        } else {
            System.err.println("Refusing to write a classfile to stdout - use -outfile foo.class");
            System.exit(1);
        }

        ((Compiler) object1).parseOptions(s2);
        ((Compiler) object1).setSource(s4);
        if (flag) {
            System.err.println("== Options ==");

            for (int j = 0; j < Compiler.options.length; j += 2) {
                System.err.println(Compiler.options[j] + ": " + ((Compiler) object1).getOption(Compiler.options[j]).get());
            }

            System.err.println("== End Options ==");
        }

        try {
            ((Compiler) object1).go();
        } catch (Compiler.Exn compiler_exn) {
            System.err.println("Compiler Error: " + compiler_exn.getMessage());
            System.exit(1);
        } finally {
            if (object != null) {
                ((Writer) object).close();
            }

            if (fileoutputstream != null) {
                fileoutputstream.close();
            }

        }

    }

    public Compiler(Seekable seekable, String s) throws IOException {
        this.fullClassName = s;
        this.elf = new ELF(seekable);
        if (this.elf.header.type != 2) {
            throw new IOException("Binary is not an executable");
        } else if (this.elf.header.machine != 8) {
            throw new IOException("Binary is not for the MIPS I Architecture");
        } else if (this.elf.ident.data != 2) {
            throw new IOException("Binary is not big endian");
        }
    }

    abstract void _go() throws Compiler.Exn, IOException;

    public void go() throws Compiler.Exn, IOException {
        if (this.used) {
            throw new RuntimeException("Compiler instances are good for one shot only");
        } else {
            this.used = true;
            if (this.onePage && this.pageSize <= 4096) {
                this.pageSize = 4194304;
            }

            if (this.nullPointerCheck && !this.fastMem) {
                throw new Compiler.Exn("fastMem must be enabled for nullPointerCheck to be of any use");
            } else if (this.onePage && !this.fastMem) {
                throw new Compiler.Exn("fastMem must be enabled for onePage to be of any use");
            } else if (this.totalPages == 1 && !this.onePage) {
                throw new Compiler.Exn("totalPages == 1 and onePage is not set");
            } else {
                if (this.onePage) {
                    this.totalPages = 1;
                }

                this.maxInsnPerMethodInit();
                this.pageSizeInit();
                ELF.Symtab elf_symtab = this.elf.getSymtab();

                if (elf_symtab == null) {
                    throw new Compiler.Exn("Binary has no symtab (did you strip it?)");
                } else {
                    this.userInfo = elf_symtab.getGlobalSymbol("user_info");
                    this.gp = elf_symtab.getGlobalSymbol("_gp");
                    if (this.gp == null) {
                        throw new Compiler.Exn("no _gp symbol (did you strip the binary?)");
                    } else {
                        if (this.pruneCases) {
                            this.jumpableAddresses = new Hashtable();
                            this.jumpableAddresses.put(new Integer(this.elf.header.entry), Boolean.TRUE);
                            ELF.SHeader elf_sheader = this.elf.sectionWithName(".text");

                            if (elf_sheader == null) {
                                throw new Compiler.Exn("No .text segment");
                            }

                            this.findBranchesInSymtab(elf_symtab, this.jumpableAddresses);

                            for (int i = 0; i < this.elf.sheaders.length; ++i) {
                                ELF.SHeader elf_sheader1 = this.elf.sheaders[i];
                                String s = elf_sheader1.name;

                                if (elf_sheader1.addr != 0 && (s.equals(".data") || s.equals(".sdata") || s.equals(".rodata") || s.equals(".ctors") || s.equals(".dtors"))) {
                                    this.findBranchesInData(new DataInputStream(elf_sheader1.getInputStream()), elf_sheader1.size, this.jumpableAddresses, elf_sheader.addr, elf_sheader.addr + elf_sheader.size);
                                }
                            }

                            this.findBranchesInText(elf_sheader.addr, new DataInputStream(elf_sheader.getInputStream()), elf_sheader.size, this.jumpableAddresses);
                        }

                        if (this.unixRuntime && this.runtimeClass.startsWith("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.")) {
                            this.runtimeClass = "org.bukkit.craftbukkit.libs.org.ibex.nestedvm.UnixRuntime";
                        }

                        for (int j = 0; j < this.elf.sheaders.length; ++j) {
                            String s1 = this.elf.sheaders[j].name;

                            if ((this.elf.sheaders[j].flags & 2) != 0 && !s1.equals(".text") && !s1.equals(".data") && !s1.equals(".sdata") && !s1.equals(".rodata") && !s1.equals(".ctors") && !s1.equals(".dtors") && !s1.equals(".bss") && !s1.equals(".sbss")) {
                                throw new Compiler.Exn("Unknown section: " + s1);
                            }
                        }

                        this._go();
                    }
                }
            }
        }
    }

    private void findBranchesInSymtab(ELF.Symtab elf_symtab, Hashtable hashtable) {
        ELF.Symbol[] aelf_symbol = elf_symtab.symbols;
        int i = 0;

        for (int j = 0; j < aelf_symbol.length; ++j) {
            ELF.Symbol elf_symbol = aelf_symbol[j];

            if (elf_symbol.type == 2 && hashtable.put(new Integer(elf_symbol.addr), Boolean.TRUE) == null) {
                ++i;
            }
        }

        if (this.printStats) {
            System.err.println("Found " + i + " additional possible branch targets in Symtab");
        }

    }

    private void findBranchesInText(int i, DataInputStream datainputstream, int j, Hashtable hashtable) throws IOException {
        int k = j / 4;
        int l = i;
        int i1 = 0;
        int[] aint = new int[32];
        int[] aint1 = new int[32];

        for (int j1 = 0; j1 < k; l += 4) {
            int k1 = datainputstream.readInt();
            int l1 = k1 >>> 26 & 255;
            int i2 = k1 >>> 21 & 31;
            int j2 = k1 >>> 16 & 31;
            int k2 = k1 << 16 >> 16;
            int l2 = k1 & '\uffff';
            int i3 = k1 & 67108863;
            int j3 = k1 & 63;

            label69:
            switch (l1) {
            case 0:
                switch (j3) {
                case 9:
                    if (hashtable.put(new Integer(l + 8), Boolean.TRUE) == null) {
                        ++i1;
                    }
                    break label69;

                case 12:
                    if (hashtable.put(new Integer(l + 4), Boolean.TRUE) == null) {
                        ++i1;
                    }

                default:
                    break label69;
                }

            case 1:
                switch (j2) {
                case 16:
                case 17:
                    if (hashtable.put(new Integer(l + 8), Boolean.TRUE) == null) {
                        ++i1;
                    }

                case 0:
                case 1:
                    if (hashtable.put(new Integer(l + k2 * 4 + 4), Boolean.TRUE) == null) {
                        ++i1;
                    }

                default:
                    break label69;
                }

            case 3:
                if (hashtable.put(new Integer(l + 8), Boolean.TRUE) == null) {
                    ++i1;
                }

            case 2:
                if (hashtable.put(new Integer(l & -268435456 | i3 << 2), Boolean.TRUE) == null) {
                    ++i1;
                }
                break;

            case 4:
            case 5:
            case 6:
            case 7:
                if (hashtable.put(new Integer(l + k2 * 4 + 4), Boolean.TRUE) == null) {
                    ++i1;
                }

            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 16:
            default:
                break;

            case 9:
                if (l - aint1[i2] <= 128) {
                    int k3 = (aint[i2] << 16) + k2;

                    if ((k3 & 3) == 0 && k3 >= i && k3 < i + j && hashtable.put(new Integer(k3), Boolean.TRUE) == null) {
                        ++i1;
                    }

                    if (j2 == i2) {
                        aint1[i2] = 0;
                    }
                }
                break;

            case 15:
                aint[j2] = l2;
                aint1[j2] = l;
                break;

            case 17:
                switch (i2) {
                case 8:
                    if (hashtable.put(new Integer(l + k2 * 4 + 4), Boolean.TRUE) == null) {
                        ++i1;
                    }
                }
            }

            ++j1;
        }

        datainputstream.close();
        if (this.printStats) {
            System.err.println("Found " + i1 + " additional possible branch targets in Text segment");
        }

    }

    private void findBranchesInData(DataInputStream datainputstream, int i, Hashtable hashtable, int j, int k) throws IOException {
        int l = i / 4;
        int i1 = 0;

        for (int j1 = 0; j1 < l; ++j1) {
            int k1 = datainputstream.readInt();

            if ((k1 & 3) == 0 && k1 >= j && k1 < k && hashtable.put(new Integer(k1), Boolean.TRUE) == null) {
                ++i1;
            }
        }

        datainputstream.close();
        if (i1 > 0 && this.printStats) {
            System.err.println("Found " + i1 + " additional possible branch targets in Data segment");
        }

    }

    static final String toHex(int i) {
        return "0x" + Long.toString((long) i & 4294967295L, 16);
    }

    static final String toHex8(int i) {
        String s = Long.toString((long) i & 4294967295L, 16);
        StringBuffer stringbuffer = new StringBuffer("0x");

        for (int j = 8 - s.length(); j > 0; --j) {
            stringbuffer.append('0');
        }

        stringbuffer.append(s);
        return stringbuffer.toString();
    }

    static final String toOctal3(int i) {
        char[] achar = new char[3];

        for (int j = 2; j >= 0; --j) {
            achar[j] = (char) (48 + (i & 7));
            i >>= 3;
        }

        return new String(achar);
    }

    private Compiler.Option getOption(String s) {
        s = s.toLowerCase();

        try {
            for (int i = 0; i < Compiler.options.length; i += 2) {
                if (Compiler.options[i].toLowerCase().equals(s)) {
                    return new Compiler.Option(Compiler.options[i]);
                }
            }

            return null;
        } catch (NoSuchFieldException nosuchfieldexception) {
            return null;
        }
    }

    public void parseOptions(String s) {
        if (s != null && s.length() != 0) {
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",");

            while (stringtokenizer.hasMoreElements()) {
                String s1 = stringtokenizer.nextToken();
                String s2;
                String s3;

                if (s1.indexOf("=") != -1) {
                    s2 = s1.substring(0, s1.indexOf("="));
                    s3 = s1.substring(s1.indexOf("=") + 1);
                } else if (s1.startsWith("no")) {
                    s2 = s1.substring(2);
                    s3 = "false";
                } else {
                    s2 = s1;
                    s3 = "true";
                }

                Compiler.Option compiler_option = this.getOption(s2);

                if (compiler_option == null) {
                    System.err.println("WARNING: No such option: " + s2);
                } else if (compiler_option.getType() == (Compiler.class$java$lang$String == null ? (Compiler.class$java$lang$String = class$("java.lang.String")) : Compiler.class$java$lang$String)) {
                    compiler_option.set(s3);
                } else if (compiler_option.getType() == Integer.TYPE) {
                    try {
                        compiler_option.set(parseInt(s3));
                    } catch (NumberFormatException numberformatexception) {
                        System.err.println("WARNING: " + s3 + " is not an integer");
                    }
                } else {
                    if (compiler_option.getType() != Boolean.TYPE) {
                        throw new Error("Unknown type: " + compiler_option.getType());
                    }

                    compiler_option.set(new Boolean(s3.toLowerCase().equals("true") || s3.toLowerCase().equals("yes")));
                }
            }

        }
    }

    private static Integer parseInt(String s) {
        int i = 1;

        s = s.toLowerCase();
        if (!s.startsWith("0x") && s.endsWith("m")) {
            s = s.substring(0, s.length() - 1);
            i = 1048576;
        } else if (!s.startsWith("0x") && s.endsWith("k")) {
            s = s.substring(0, s.length() - 1);
            i = 1024;
        }

        int j;

        if (s.length() > 2 && s.startsWith("0x")) {
            j = Integer.parseInt(s.substring(2), 16);
        } else {
            j = Integer.parseInt(s);
        }

        return new Integer(j * i);
    }

    private static String wrapAndIndent(String s, int i, int j, int k) {
        StringTokenizer stringtokenizer = new StringTokenizer(s, " ");
        StringBuffer stringbuffer = new StringBuffer();

        int l;

        for (l = 0; l < i; ++l) {
            stringbuffer.append(' ');
        }

        String s1;

        for (l = 0; stringtokenizer.hasMoreTokens(); l += s1.length()) {
            s1 = stringtokenizer.nextToken();
            if (s1.length() + l + 1 > k && l > 0) {
                stringbuffer.append('\n');

                for (int i1 = 0; i1 < j; ++i1) {
                    stringbuffer.append(' ');
                }

                l = 0;
            } else if (l > 0) {
                stringbuffer.append(' ');
                ++l;
            }

            stringbuffer.append(s1);
        }

        stringbuffer.append('\n');
        return stringbuffer.toString();
    }

    static String dateTime() {
        try {
            return (new Date()).toString();
        } catch (RuntimeException runtimeexception) {
            return "<unknown>";
        }
    }

    static Class class$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private class Option {

        private Field field;

        public Option(String s) throws NoSuchFieldException {
            this.field = s == null ? null : (Compiler.class$org$ibex$nestedvm$Compiler == null ? (Compiler.class$org$ibex$nestedvm$Compiler = Compiler.class$("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.Compiler")) : Compiler.class$org$ibex$nestedvm$Compiler).getDeclaredField(s);
        }

        public void set(Object object) {
            if (this.field != null) {
                try {
                    this.field.set(Compiler.this, object);
                } catch (IllegalAccessException illegalaccessexception) {
                    System.err.println(illegalaccessexception);
                }

            }
        }

        public Object get() {
            if (this.field == null) {
                return null;
            } else {
                try {
                    return this.field.get(Compiler.this);
                } catch (IllegalAccessException illegalaccessexception) {
                    System.err.println(illegalaccessexception);
                    return null;
                }
            }
        }

        public Class getType() {
            return this.field == null ? null : this.field.getType();
        }
    }

    static class Exn extends Exception {

        public Exn(String s) {
            super(s);
        }
    }
}
