package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class CharSequenceInputStream extends InputStream {

    private final CharsetEncoder encoder;
    private final CharBuffer cbuf;
    private final ByteBuffer bbuf;
    private int mark;

    public CharSequenceInputStream(CharSequence charsequence, Charset charset, int i) {
        this.encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        this.bbuf = ByteBuffer.allocate(i);
        this.bbuf.flip();
        this.cbuf = CharBuffer.wrap(charsequence);
        this.mark = -1;
    }

    public CharSequenceInputStream(CharSequence charsequence, String s, int i) {
        this(charsequence, Charset.forName(s), i);
    }

    public CharSequenceInputStream(CharSequence charsequence, Charset charset) {
        this(charsequence, charset, 2048);
    }

    public CharSequenceInputStream(CharSequence charsequence, String s) {
        this(charsequence, s, 2048);
    }

    private void fillBuffer() throws CharacterCodingException {
        this.bbuf.compact();
        CoderResult coderresult = this.encoder.encode(this.cbuf, this.bbuf, true);

        if (coderresult.isError()) {
            coderresult.throwException();
        }

        this.bbuf.flip();
    }

    public int read(byte[] abyte, int i, int j) throws IOException {
        if (abyte == null) {
            throw new NullPointerException("Byte array is null");
        } else if (j >= 0 && i + j <= abyte.length) {
            if (j == 0) {
                return 0;
            } else if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                return -1;
            } else {
                int k = 0;

                while (j > 0) {
                    if (this.bbuf.hasRemaining()) {
                        int l = Math.min(this.bbuf.remaining(), j);

                        this.bbuf.get(abyte, i, l);
                        i += l;
                        j -= l;
                        k += l;
                    } else {
                        this.fillBuffer();
                        if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                            break;
                        }
                    }
                }

                return k == 0 && !this.cbuf.hasRemaining() ? -1 : k;
            }
        } else {
            throw new IndexOutOfBoundsException("Array Size=" + abyte.length + ", offset=" + i + ", length=" + j);
        }
    }

    public int read() throws IOException {
        do {
            if (this.bbuf.hasRemaining()) {
                return this.bbuf.get() & 255;
            }

            this.fillBuffer();
        } while (this.bbuf.hasRemaining() || this.cbuf.hasRemaining());

        return -1;
    }

    public int read(byte[] abyte) throws IOException {
        return this.read(abyte, 0, abyte.length);
    }

    public long skip(long i) throws IOException {
        int j;

        for (j = 0; i > 0L && this.cbuf.hasRemaining(); ++j) {
            this.cbuf.get();
            --i;
        }

        return (long) j;
    }

    public int available() throws IOException {
        return this.cbuf.remaining();
    }

    public void close() throws IOException {}

    public synchronized void mark(int i) {
        this.mark = this.cbuf.position();
    }

    public synchronized void reset() throws IOException {
        if (this.mark != -1) {
            this.cbuf.position(this.mark);
            this.mark = -1;
        }

    }

    public boolean markSupported() {
        return true;
    }
}
