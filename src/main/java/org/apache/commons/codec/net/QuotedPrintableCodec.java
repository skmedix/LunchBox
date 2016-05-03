package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class QuotedPrintableCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {

    private final Charset charset;
    private static final BitSet PRINTABLE_CHARS = new BitSet(256);
    private static final byte ESCAPE_CHAR = 61;
    private static final byte TAB = 9;
    private static final byte SPACE = 32;

    public QuotedPrintableCodec() {
        this(Charsets.UTF_8);
    }

    public QuotedPrintableCodec(Charset charset) {
        this.charset = charset;
    }

    public QuotedPrintableCodec(String s) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
        this(Charset.forName(s));
    }

    private static final void encodeQuotedPrintable(int i, ByteArrayOutputStream bytearrayoutputstream) {
        bytearrayoutputstream.write(61);
        char c0 = Character.toUpperCase(Character.forDigit(i >> 4 & 15, 16));
        char c1 = Character.toUpperCase(Character.forDigit(i & 15, 16));

        bytearrayoutputstream.write(c0);
        bytearrayoutputstream.write(c1);
    }

    public static final byte[] encodeQuotedPrintable(BitSet bitset, byte[] abyte) {
        if (abyte == null) {
            return null;
        } else {
            if (bitset == null) {
                bitset = QuotedPrintableCodec.PRINTABLE_CHARS;
            }

            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            byte[] abyte1 = abyte;
            int i = abyte.length;

            for (int j = 0; j < i; ++j) {
                byte b0 = abyte1[j];
                int k = b0;

                if (b0 < 0) {
                    k = 256 + b0;
                }

                if (bitset.get(k)) {
                    bytearrayoutputstream.write(k);
                } else {
                    encodeQuotedPrintable(k, bytearrayoutputstream);
                }
            }

            return bytearrayoutputstream.toByteArray();
        }
    }

    public static final byte[] decodeQuotedPrintable(byte[] abyte) throws DecoderException {
        if (abyte == null) {
            return null;
        } else {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

            for (int i = 0; i < abyte.length; ++i) {
                byte b0 = abyte[i];

                if (b0 == 61) {
                    try {
                        ++i;
                        int j = Utils.digit16(abyte[i]);

                        ++i;
                        int k = Utils.digit16(abyte[i]);

                        bytearrayoutputstream.write((char) ((j << 4) + k));
                    } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
                        throw new DecoderException("Invalid quoted-printable encoding", arrayindexoutofboundsexception);
                    }
                } else {
                    bytearrayoutputstream.write(b0);
                }
            }

            return bytearrayoutputstream.toByteArray();
        }
    }

    public byte[] encode(byte[] abyte) {
        return encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, abyte);
    }

    public byte[] decode(byte[] abyte) throws DecoderException {
        return decodeQuotedPrintable(abyte);
    }

    public String encode(String s) throws EncoderException {
        return this.encode(s, this.getCharset());
    }

    public String decode(String s, Charset charset) throws DecoderException {
        return s == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(s)), charset);
    }

    public String decode(String s, String s1) throws DecoderException, UnsupportedEncodingException {
        return s == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(s)), s1);
    }

    public String decode(String s) throws DecoderException {
        return this.decode(s, this.getCharset());
    }

    public Object encode(Object object) throws EncoderException {
        if (object == null) {
            return null;
        } else if (object instanceof byte[]) {
            return this.encode((byte[]) ((byte[]) object));
        } else if (object instanceof String) {
            return this.encode((String) object);
        } else {
            throw new EncoderException("Objects of type " + object.getClass().getName() + " cannot be quoted-printable encoded");
        }
    }

    public Object decode(Object object) throws DecoderException {
        if (object == null) {
            return null;
        } else if (object instanceof byte[]) {
            return this.decode((byte[]) ((byte[]) object));
        } else if (object instanceof String) {
            return this.decode((String) object);
        } else {
            throw new DecoderException("Objects of type " + object.getClass().getName() + " cannot be quoted-printable decoded");
        }
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }

    public String encode(String s, Charset charset) {
        return s == null ? null : StringUtils.newStringUsAscii(this.encode(s.getBytes(charset)));
    }

    public String encode(String s, String s1) throws UnsupportedEncodingException {
        return s == null ? null : StringUtils.newStringUsAscii(this.encode(s.getBytes(s1)));
    }

    static {
        int i;

        for (i = 33; i <= 60; ++i) {
            QuotedPrintableCodec.PRINTABLE_CHARS.set(i);
        }

        for (i = 62; i <= 126; ++i) {
            QuotedPrintableCodec.PRINTABLE_CHARS.set(i);
        }

        QuotedPrintableCodec.PRINTABLE_CHARS.set(9);
        QuotedPrintableCodec.PRINTABLE_CHARS.set(32);
    }
}
