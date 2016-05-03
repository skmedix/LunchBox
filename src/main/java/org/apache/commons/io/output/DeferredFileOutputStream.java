package org.apache.commons.io.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class DeferredFileOutputStream extends ThresholdingOutputStream {

    private ByteArrayOutputStream memoryOutputStream;
    private OutputStream currentOutputStream;
    private File outputFile;
    private final String prefix;
    private final String suffix;
    private final File directory;
    private boolean closed;

    public DeferredFileOutputStream(int i, File file) {
        this(i, file, (String) null, (String) null, (File) null);
    }

    public DeferredFileOutputStream(int i, String s, String s1, File file) {
        this(i, (File) null, s, s1, file);
        if (s == null) {
            throw new IllegalArgumentException("Temporary file prefix is missing");
        }
    }

    private DeferredFileOutputStream(int i, File file, String s, String s1, File file1) {
        super(i);
        this.closed = false;
        this.outputFile = file;
        this.memoryOutputStream = new ByteArrayOutputStream();
        this.currentOutputStream = this.memoryOutputStream;
        this.prefix = s;
        this.suffix = s1;
        this.directory = file1;
    }

    protected OutputStream getStream() throws IOException {
        return this.currentOutputStream;
    }

    protected void thresholdReached() throws IOException {
        if (this.prefix != null) {
            this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
        }

        FileOutputStream fileoutputstream = new FileOutputStream(this.outputFile);

        this.memoryOutputStream.writeTo(fileoutputstream);
        this.currentOutputStream = fileoutputstream;
        this.memoryOutputStream = null;
    }

    public boolean isInMemory() {
        return !this.isThresholdExceeded();
    }

    public byte[] getData() {
        return this.memoryOutputStream != null ? this.memoryOutputStream.toByteArray() : null;
    }

    public File getFile() {
        return this.outputFile;
    }

    public void close() throws IOException {
        super.close();
        this.closed = true;
    }

    public void writeTo(OutputStream outputstream) throws IOException {
        if (!this.closed) {
            throw new IOException("Stream not closed");
        } else {
            if (this.isInMemory()) {
                this.memoryOutputStream.writeTo(outputstream);
            } else {
                FileInputStream fileinputstream = new FileInputStream(this.outputFile);

                try {
                    IOUtils.copy((InputStream) fileinputstream, outputstream);
                } finally {
                    IOUtils.closeQuietly((InputStream) fileinputstream);
                }
            }

        }
    }
}
