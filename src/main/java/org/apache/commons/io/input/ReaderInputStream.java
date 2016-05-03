package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class ReaderInputStream extends InputStream {

    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Reader reader;
    private final CharsetEncoder encoder;
    private final CharBuffer encoderIn;
    private final ByteBuffer encoderOut;
    private CoderResult lastCoderResult;
    private boolean endOfInput;

    public ReaderInputStream(Reader reader, CharsetEncoder charsetencoder) {
        this(reader, charsetencoder, 1024);
    }

    public ReaderInputStream(Reader reader, CharsetEncoder charsetencoder, int i) {
        this.reader = reader;
        this.encoder = charsetencoder;
        this.encoderIn = CharBuffer.allocate(i);
        this.encoderIn.flip();
        this.encoderOut = ByteBuffer.allocate(128);
        this.encoderOut.flip();
    }

    public ReaderInputStream(Reader reader, Charset charset, int i) {
        this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), i);
    }

    public ReaderInputStream(Reader reader, Charset charset) {
        this(reader, charset, 1024);
    }

    public ReaderInputStream(Reader reader, String s, int i) {
        this(reader, Charset.forName(s), i);
    }

    public ReaderInputStream(Reader reader, String s) {
        this(reader, s, 1024);
    }

    public ReaderInputStream(Reader reader) {
        this(reader, Charset.defaultCharset());
    }

    private void fillBuffer() throws IOException {
        if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
            this.encoderIn.compact();
            int i = this.encoderIn.position();
            int j = this.reader.read(this.encoderIn.array(), i, this.encoderIn.remaining());

            if (j == -1) {
                this.endOfInput = true;
            } else {
                this.encoderIn.position(i + j);
            }

            this.encoderIn.flip();
        }

        this.encoderOut.compact();
        this.lastCoderResult = this.encoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
        this.encoderOut.flip();
    }

    public int read(byte[] abyte, int i, int j) throws IOException {
        if (abyte == null) {
            throw new NullPointerException("Byte array must not be null");
        } else if (j >= 0 && i >= 0 && i + j <= abyte.length) {
            int k = 0;

            if (j == 0) {
                return 0;
            } else {
                while (j > 0) {
                    if (this.encoderOut.hasRemaining()) {
                        int l = Math.min(this.encoderOut.remaining(), j);

                        this.encoderOut.get(abyte, i, l);
                        i += l;
                        j -= l;
                        k += l;
                    } else {
                        this.fillBuffer();
                        if (this.endOfInput && !this.encoderOut.hasRemaining()) {
                            break;
                        }
                    }
                }

                return k == 0 && this.endOfInput ? -1 : k;
            }
        } else {
            throw new IndexOutOfBoundsException("Array Size=" + abyte.length + ", offset=" + i + ", length=" + j);
        }
    }

    public int read(byte[] abyte) throws IOException {
        return this.read(abyte, 0, abyte.length);
    }

    public int read() throws IOException {
        do {
            if (this.encoderOut.hasRemaining()) {
                return this.encoderOut.get() & 255;
            }

            this.fillBuffer();
        } while (!this.endOfInput || this.encoderOut.hasRemaining());

        return -1;
    }

    public void close() throws IOException {
        this.reader.close();
    }
}
