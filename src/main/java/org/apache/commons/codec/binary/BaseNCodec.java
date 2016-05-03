package org.apache.commons.codec.binary;

import java.util.Arrays;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public abstract class BaseNCodec implements BinaryEncoder, BinaryDecoder {

    static final int EOF = -1;
    public static final int MIME_CHUNK_SIZE = 76;
    public static final int PEM_CHUNK_SIZE = 64;
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    protected static final int MASK_8BITS = 255;
    protected static final byte PAD_DEFAULT = 61;
    protected final byte PAD = 61;
    private final int unencodedBlockSize;
    private final int encodedBlockSize;
    protected final int lineLength;
    private final int chunkSeparatorLength;

    protected BaseNCodec(int i, int j, int k, int l) {
        this.unencodedBlockSize = i;
        this.encodedBlockSize = j;
        boolean flag = k > 0 && l > 0;

        this.lineLength = flag ? k / j * j : 0;
        this.chunkSeparatorLength = l;
    }

    boolean hasData(BaseNCodec.Context basencodec_context) {
        return basencodec_context.buffer != null;
    }

    int available(BaseNCodec.Context basencodec_context) {
        return basencodec_context.buffer != null ? basencodec_context.pos - basencodec_context.readPos : 0;
    }

    protected int getDefaultBufferSize() {
        return 8192;
    }

    private byte[] resizeBuffer(BaseNCodec.Context basencodec_context) {
        if (basencodec_context.buffer == null) {
            basencodec_context.buffer = new byte[this.getDefaultBufferSize()];
            basencodec_context.pos = 0;
            basencodec_context.readPos = 0;
        } else {
            byte[] abyte = new byte[basencodec_context.buffer.length * 2];

            System.arraycopy(basencodec_context.buffer, 0, abyte, 0, basencodec_context.buffer.length);
            basencodec_context.buffer = abyte;
        }

        return basencodec_context.buffer;
    }

    protected byte[] ensureBufferSize(int i, BaseNCodec.Context basencodec_context) {
        return basencodec_context.buffer != null && basencodec_context.buffer.length >= basencodec_context.pos + i ? basencodec_context.buffer : this.resizeBuffer(basencodec_context);
    }

    int readResults(byte[] abyte, int i, int j, BaseNCodec.Context basencodec_context) {
        if (basencodec_context.buffer != null) {
            int k = Math.min(this.available(basencodec_context), j);

            System.arraycopy(basencodec_context.buffer, basencodec_context.readPos, abyte, i, k);
            basencodec_context.readPos += k;
            if (basencodec_context.readPos >= basencodec_context.pos) {
                basencodec_context.buffer = null;
            }

            return k;
        } else {
            return basencodec_context.eof ? -1 : 0;
        }
    }

    protected static boolean isWhiteSpace(byte b0) {
        switch (b0) {
        case 9:
        case 10:
        case 13:
        case 32:
            return true;

        default:
            return false;
        }
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof byte[])) {
            throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
        } else {
            return this.encode((byte[]) ((byte[]) object));
        }
    }

    public String encodeToString(byte[] abyte) {
        return StringUtils.newStringUtf8(this.encode(abyte));
    }

    public String encodeAsString(byte[] abyte) {
        return StringUtils.newStringUtf8(this.encode(abyte));
    }

    public Object decode(Object object) throws DecoderException {
        if (object instanceof byte[]) {
            return this.decode((byte[]) ((byte[]) object));
        } else if (object instanceof String) {
            return this.decode((String) object);
        } else {
            throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
        }
    }

    public byte[] decode(String s) {
        return this.decode(StringUtils.getBytesUtf8(s));
    }

    public byte[] decode(byte[] abyte) {
        if (abyte != null && abyte.length != 0) {
            BaseNCodec.Context basencodec_context = new BaseNCodec.Context();

            this.decode(abyte, 0, abyte.length, basencodec_context);
            this.decode(abyte, 0, -1, basencodec_context);
            byte[] abyte1 = new byte[basencodec_context.pos];

            this.readResults(abyte1, 0, abyte1.length, basencodec_context);
            return abyte1;
        } else {
            return abyte;
        }
    }

    public byte[] encode(byte[] abyte) {
        if (abyte != null && abyte.length != 0) {
            BaseNCodec.Context basencodec_context = new BaseNCodec.Context();

            this.encode(abyte, 0, abyte.length, basencodec_context);
            this.encode(abyte, 0, -1, basencodec_context);
            byte[] abyte1 = new byte[basencodec_context.pos - basencodec_context.readPos];

            this.readResults(abyte1, 0, abyte1.length, basencodec_context);
            return abyte1;
        } else {
            return abyte;
        }
    }

    abstract void encode(byte[] abyte, int i, int j, BaseNCodec.Context basencodec_context);

    abstract void decode(byte[] abyte, int i, int j, BaseNCodec.Context basencodec_context);

    protected abstract boolean isInAlphabet(byte b0);

    public boolean isInAlphabet(byte[] abyte, boolean flag) {
        for (int i = 0; i < abyte.length; ++i) {
            if (!this.isInAlphabet(abyte[i]) && (!flag || abyte[i] != 61 && !isWhiteSpace(abyte[i]))) {
                return false;
            }
        }

        return true;
    }

    public boolean isInAlphabet(String s) {
        return this.isInAlphabet(StringUtils.getBytesUtf8(s), true);
    }

    protected boolean containsAlphabetOrPad(byte[] abyte) {
        if (abyte == null) {
            return false;
        } else {
            byte[] abyte1 = abyte;
            int i = abyte.length;

            for (int j = 0; j < i; ++j) {
                byte b0 = abyte1[j];

                if (61 == b0 || this.isInAlphabet(b0)) {
                    return true;
                }
            }

            return false;
        }
    }

    public long getEncodedLength(byte[] abyte) {
        long i = (long) ((abyte.length + this.unencodedBlockSize - 1) / this.unencodedBlockSize) * (long) this.encodedBlockSize;

        if (this.lineLength > 0) {
            i += (i + (long) this.lineLength - 1L) / (long) this.lineLength * (long) this.chunkSeparatorLength;
        }

        return i;
    }

    static class Context {

        int ibitWorkArea;
        long lbitWorkArea;
        byte[] buffer;
        int pos;
        int readPos;
        boolean eof;
        int currentLinePos;
        int modulus;

        public String toString() {
            return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", new Object[] { this.getClass().getSimpleName(), Arrays.toString(this.buffer), Integer.valueOf(this.currentLinePos), Boolean.valueOf(this.eof), Integer.valueOf(this.ibitWorkArea), Long.valueOf(this.lbitWorkArea), Integer.valueOf(this.modulus), Integer.valueOf(this.pos), Integer.valueOf(this.readPos)});
        }
    }
}
