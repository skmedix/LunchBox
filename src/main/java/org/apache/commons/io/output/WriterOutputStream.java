package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class WriterOutputStream extends OutputStream {

    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Writer writer;
    private final CharsetDecoder decoder;
    private final boolean writeImmediately;
    private final ByteBuffer decoderIn;
    private final CharBuffer decoderOut;

    public WriterOutputStream(Writer writer, CharsetDecoder charsetdecoder) {
        this(writer, charsetdecoder, 1024, false);
    }

    public WriterOutputStream(Writer writer, CharsetDecoder charsetdecoder, int i, boolean flag) {
        this.decoderIn = ByteBuffer.allocate(128);
        this.writer = writer;
        this.decoder = charsetdecoder;
        this.writeImmediately = flag;
        this.decoderOut = CharBuffer.allocate(i);
    }

    public WriterOutputStream(Writer writer, Charset charset, int i, boolean flag) {
        this(writer, charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), i, flag);
    }

    public WriterOutputStream(Writer writer, Charset charset) {
        this(writer, charset, 1024, false);
    }

    public WriterOutputStream(Writer writer, String s, int i, boolean flag) {
        this(writer, Charset.forName(s), i, flag);
    }

    public WriterOutputStream(Writer writer, String s) {
        this(writer, s, 1024, false);
    }

    public WriterOutputStream(Writer writer) {
        this(writer, Charset.defaultCharset(), 1024, false);
    }

    public void write(byte[] abyte, int i, int j) throws IOException {
        while (j > 0) {
            int k = Math.min(j, this.decoderIn.remaining());

            this.decoderIn.put(abyte, i, k);
            this.processInput(false);
            j -= k;
            i += k;
        }

        if (this.writeImmediately) {
            this.flushOutput();
        }

    }

    public void write(byte[] abyte) throws IOException {
        this.write(abyte, 0, abyte.length);
    }

    public void write(int i) throws IOException {
        this.write(new byte[] { (byte) i}, 0, 1);
    }

    public void flush() throws IOException {
        this.flushOutput();
        this.writer.flush();
    }

    public void close() throws IOException {
        this.processInput(true);
        this.flushOutput();
        this.writer.close();
    }

    private void processInput(boolean flag) throws IOException {
        this.decoderIn.flip();

        while (true) {
            CoderResult coderresult = this.decoder.decode(this.decoderIn, this.decoderOut, flag);

            if (!coderresult.isOverflow()) {
                if (coderresult.isUnderflow()) {
                    this.decoderIn.compact();
                    return;
                } else {
                    throw new IOException("Unexpected coder result");
                }
            }

            this.flushOutput();
        }
    }

    private void flushOutput() throws IOException {
        if (this.decoderOut.position() > 0) {
            this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
            this.decoderOut.rewind();
        }

    }
}
