package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileWriterWithEncoding extends Writer {

    private final Writer out;

    public FileWriterWithEncoding(String s, String s1) throws IOException {
        this(new File(s), s1, false);
    }

    public FileWriterWithEncoding(String s, String s1, boolean flag) throws IOException {
        this(new File(s), s1, flag);
    }

    public FileWriterWithEncoding(String s, Charset charset) throws IOException {
        this(new File(s), charset, false);
    }

    public FileWriterWithEncoding(String s, Charset charset, boolean flag) throws IOException {
        this(new File(s), charset, flag);
    }

    public FileWriterWithEncoding(String s, CharsetEncoder charsetencoder) throws IOException {
        this(new File(s), charsetencoder, false);
    }

    public FileWriterWithEncoding(String s, CharsetEncoder charsetencoder, boolean flag) throws IOException {
        this(new File(s), charsetencoder, flag);
    }

    public FileWriterWithEncoding(File file, String s) throws IOException {
        this(file, s, false);
    }

    public FileWriterWithEncoding(File file, String s, boolean flag) throws IOException {
        this.out = initWriter(file, s, flag);
    }

    public FileWriterWithEncoding(File file, Charset charset) throws IOException {
        this(file, charset, false);
    }

    public FileWriterWithEncoding(File file, Charset charset, boolean flag) throws IOException {
        this.out = initWriter(file, charset, flag);
    }

    public FileWriterWithEncoding(File file, CharsetEncoder charsetencoder) throws IOException {
        this(file, charsetencoder, false);
    }

    public FileWriterWithEncoding(File file, CharsetEncoder charsetencoder, boolean flag) throws IOException {
        this.out = initWriter(file, charsetencoder, flag);
    }

    private static Writer initWriter(File file, Object object, boolean flag) throws IOException {
        if (file == null) {
            throw new NullPointerException("File is missing");
        } else if (object == null) {
            throw new NullPointerException("Encoding is missing");
        } else {
            boolean flag1 = file.exists();
            FileOutputStream fileoutputstream = null;
            OutputStreamWriter outputstreamwriter = null;

            try {
                fileoutputstream = new FileOutputStream(file, flag);
                if (object instanceof Charset) {
                    outputstreamwriter = new OutputStreamWriter(fileoutputstream, (Charset) object);
                } else if (object instanceof CharsetEncoder) {
                    outputstreamwriter = new OutputStreamWriter(fileoutputstream, (CharsetEncoder) object);
                } else {
                    outputstreamwriter = new OutputStreamWriter(fileoutputstream, (String) object);
                }

                return outputstreamwriter;
            } catch (IOException ioexception) {
                IOUtils.closeQuietly((Writer) outputstreamwriter);
                IOUtils.closeQuietly((OutputStream) fileoutputstream);
                if (!flag1) {
                    FileUtils.deleteQuietly(file);
                }

                throw ioexception;
            } catch (RuntimeException runtimeexception) {
                IOUtils.closeQuietly((Writer) outputstreamwriter);
                IOUtils.closeQuietly((OutputStream) fileoutputstream);
                if (!flag1) {
                    FileUtils.deleteQuietly(file);
                }

                throw runtimeexception;
            }
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

    public void close() throws IOException {
        this.out.close();
    }
}
