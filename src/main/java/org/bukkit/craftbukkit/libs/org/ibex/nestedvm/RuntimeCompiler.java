package org.bukkit.craftbukkit.libs.org.ibex.nestedvm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;

public class RuntimeCompiler {

    public static Class compile(Seekable seekable) throws IOException, Compiler.Exn {
        return compile(seekable, (String) null);
    }

    public static Class compile(Seekable seekable, String s) throws IOException, Compiler.Exn {
        return compile(seekable, s, (String) null);
    }

    public static Class compile(Seekable seekable, String s, String s1) throws IOException, Compiler.Exn {
        String s2 = "nestedvm.runtimecompiled";

        byte[] abyte;

        try {
            abyte = runCompiler(seekable, s2, s, s1, (String) null);
        } catch (Compiler.Exn compiler_exn) {
            if (compiler_exn.getMessage() == null && compiler_exn.getMessage().indexOf("constant pool full") == -1) {
                throw compiler_exn;
            }

            abyte = runCompiler(seekable, s2, s, s1, "lessconstants");
        }

        return (new RuntimeCompiler.SingleClassLoader((RuntimeCompiler.SyntheticClass_1) null)).fromBytes(s2, abyte);
    }

    private static byte[] runCompiler(Seekable seekable, String s, String s1, String s2, String s3) throws IOException, Compiler.Exn {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

        try {
            ClassFileCompiler classfilecompiler = new ClassFileCompiler(seekable, s, bytearrayoutputstream);

            classfilecompiler.parseOptions("nosupportcall,maxinsnpermethod=256");
            classfilecompiler.setSource(s2);
            if (s1 != null) {
                classfilecompiler.parseOptions(s1);
            }

            if (s3 != null) {
                classfilecompiler.parseOptions(s3);
            }

            classfilecompiler.go();
        } finally {
            seekable.seek(0);
        }

        bytearrayoutputstream.close();
        return bytearrayoutputstream.toByteArray();
    }

    public static void main(String[] astring) throws Exception {
        if (astring.length == 0) {
            System.err.println("Usage: RuntimeCompiler mipsbinary");
            System.exit(1);
        }

        UnixRuntime unixruntime = (UnixRuntime) compile(new Seekable.File(astring[0]), "unixruntime").newInstance();

        System.err.println("Instansiated: " + unixruntime);
        System.exit(UnixRuntime.runAndExec(unixruntime, astring));
    }

    static class SyntheticClass_1 {    }

    private static class SingleClassLoader extends ClassLoader {

        private SingleClassLoader() {}

        public Class loadClass(String s, boolean flag) throws ClassNotFoundException {
            return super.loadClass(s, flag);
        }

        public Class fromBytes(String s, byte[] abyte) {
            return this.fromBytes(s, abyte, 0, abyte.length);
        }

        public Class fromBytes(String s, byte[] abyte, int i, int j) {
            Class oclass = super.defineClass(s, abyte, i, j);

            this.resolveClass(oclass);
            return oclass;
        }

        SingleClassLoader(RuntimeCompiler.SyntheticClass_1 runtimecompiler_syntheticclass_1) {
            this();
        }
    }
}
