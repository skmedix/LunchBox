package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class LockableFileWriter extends Writer {

    private static final String LCK = ".lck";
    private final Writer out;
    private final File lockFile;

    public LockableFileWriter(String s) throws IOException {
        this(s, false, (String) null);
    }

    public LockableFileWriter(String s, boolean flag) throws IOException {
        this(s, flag, (String) null);
    }

    public LockableFileWriter(String s, boolean flag, String s1) throws IOException {
        this(new File(s), flag, s1);
    }

    public LockableFileWriter(File file) throws IOException {
        this(file, false, (String) null);
    }

    public LockableFileWriter(File file, boolean flag) throws IOException {
        this(file, flag, (String) null);
    }

    public LockableFileWriter(File file, boolean flag, String s) throws IOException {
        this(file, Charset.defaultCharset(), flag, s);
    }

    public LockableFileWriter(File file, Charset charset) throws IOException {
        this(file, charset, false, (String) null);
    }

    public LockableFileWriter(File file, String s) throws IOException {
        this(file, s, false, (String) null);
    }

    public LockableFileWriter(File file, Charset charset, boolean flag, String s) throws IOException {
        file = file.getAbsoluteFile();
        if (file.getParentFile() != null) {
            FileUtils.forceMkdir(file.getParentFile());
        }

        if (file.isDirectory()) {
            throw new IOException("File specified is a directory");
        } else {
            if (s == null) {
                s = System.getProperty("java.io.tmpdir");
            }

            File file1 = new File(s);

            FileUtils.forceMkdir(file1);
            this.testLockDir(file1);
            this.lockFile = new File(file1, file.getName() + ".lck");
            this.createLock();
            this.out = this.initWriter(file, charset, flag);
        }
    }

    public LockableFileWriter(File file, String s, boolean flag, String s1) throws IOException {
        this(file, Charsets.toCharset(s), flag, s1);
    }

    private void testLockDir(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("Could not find lockDir: " + file.getAbsolutePath());
        } else if (!file.canWrite()) {
            throw new IOException("Could not write to lockDir: " + file.getAbsolutePath());
        }
    }

    private void createLock() throws IOException {
        Class oclass = LockableFileWriter.class;

        synchronized (LockableFileWriter.class) {
            if (!this.lockFile.createNewFile()) {
                throw new IOException("Can\'t write file, lock " + this.lockFile.getAbsolutePath() + " exists");
            } else {
                this.lockFile.deleteOnExit();
            }
        }
    }

    private Writer initWriter(File file, Charset charset, boolean flag) throws IOException {
        boolean flag1 = file.exists();
        FileOutputStream fileoutputstream = null;
        OutputStreamWriter outputstreamwriter = null;

        try {
            fileoutputstream = new FileOutputStream(file.getAbsolutePath(), flag);
            outputstreamwriter = new OutputStreamWriter(fileoutputstream, Charsets.toCharset(charset));
            return outputstreamwriter;
        } catch (IOException ioexception) {
            IOUtils.closeQuietly((Writer) outputstreamwriter);
            IOUtils.closeQuietly((OutputStream) fileoutputstream);
            FileUtils.deleteQuietly(this.lockFile);
            if (!flag1) {
                FileUtils.deleteQuietly(file);
            }

            throw ioexception;
        } catch (RuntimeException runtimeexception) {
            IOUtils.closeQuietly((Writer) outputstreamwriter);
            IOUtils.closeQuietly((OutputStream) fileoutputstream);
            FileUtils.deleteQuietly(this.lockFile);
            if (!flag1) {
                FileUtils.deleteQuietly(file);
            }

            throw runtimeexception;
        }
    }

    public void close() throws IOException {
        try {
            this.out.close();
        } finally {
            this.lockFile.delete();
        }

    }

    public void write(int i) throws IOException {
        this.out.write(i);
    }

    public void write(char[] achar) throws IOException {
        this.out.write(achar);
    }

    public void write(char[] achar, int i, int j) throws IOException {
        this.out.write(achar, i, j);
    }

    public void write(String s) throws IOException {
        this.out.write(s);
    }

    public void write(String s, int i, int j) throws IOException {
        this.out.write(s, i, j);
    }

    public void flush() throws IOException {
        this.out.flush();
    }
}
