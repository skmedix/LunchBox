package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {

    static final int RADIX = 16;
    /** @deprecated */
    @Deprecated
    protected String charset;
    protected static final byte ESCAPE_CHAR = 37;
    protected static final BitSet WWW_FORM_URL = new BitSet(256);

    public URLCodec() {
        this("UTF-8");
    }

    public URLCodec(String s) {
        this.charset = s;
    }

    public static final byte[] encodeUrl(BitSet bitset, byte[] abyte) {
        if (abyte == null) {
            return null;
        } else {
            if (bitset == null) {
                bitset = URLCodec.WWW_FORM_URL;
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
                    if (k == 32) {
                        k = 43;
                    }

                    bytearrayoutputstream.write(k);
                } else {
                    bytearrayoutputstream.write(37);
                    char c0 = Character.toUpperCase(Character.forDigit(k >> 4 & 15, 16));
                    char c1 = Character.toUpperCase(Character.forDigit(k & 15, 16));

                    bytearrayoutputstream.write(c0);
                    bytearrayoutputstream.write(c1);
                }
            }

            return bytearrayoutputstream.toByteArray();
        }
    }

    public static final byte[] decodeUrl(byte[] abyte) throws DecoderException {
        if (abyte == null) {
            return null;
        } else {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

            for (int i = 0; i < abyte.length; ++i) {
                byte b0 = abyte[i];

                if (b0 == 43) {
                    bytearrayoutputstream.write(32);
                } else if (b0 == 37) {
                    try {
                        ++i;
                        int j = Utils.digit16(abyte[i]);

                        ++i;
                        int k = Utils.digit16(abyte[i]);

                        bytearrayoutputstream.write((char) ((j << 4) + k));
                    } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
                        throw new DecoderException("Invalid URL encoding: ", arrayindexoutofboundsexception);
                    }
                } else {
                    bytearrayoutputstream.write(b0);
                }
            }

            return bytearrayoutputstream.toByteArray();
        }
    }

    public byte[] encode(byte[] abyte) {
        return encodeUrl(URLCodec.WWW_FORM_URL, abyte);
    }

    public byte[] decode(byte[] abyte) throws DecoderException {
        return decodeUrl(abyte);
    }

    public String encode(String s, String s1) throws UnsupportedEncodingException {
        return s == null ? null : StringUtils.newStringUsAscii(this.encode(s.getBytes(s1)));
    }

    public String encode(String s) throws EncoderException {
        if (s == null) {
            return null;
        } else {
            try {
                return this.encode(s, this.getDefaultCharset());
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw new EncoderException(unsupportedencodingexception.getMessage(), unsupportedencodingexception);
            }
        }
    }

    public String decode(String s, String s1) throws DecoderException, UnsupportedEncodingException {
        return s == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(s)), s1);
    }

    public String decode(String s) throws DecoderException {
        if (s == null) {
            return null;
        } else {
            try {
                return this.decode(s, this.getDefaultCharset());
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw new DecoderException(unsupportedencodingexception.getMessage(), unsupportedencodingexception);
            }
        }
    }

    public Object encode(Object object) throws EncoderException {
        if (object == null) {
            return null;
        } else if (object instanceof byte[]) {
            return this.encode((byte[]) ((byte[]) object));
        } else if (object instanceof String) {
            return this.encode((String) object);
        } else {
            throw new EncoderException("Objects of type " + object.getClass().getName() + " cannot be URL encoded");
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
            throw new DecoderException("Objects of type " + object.getClass().getName() + " cannot be URL decoded");
        }
    }

    public String getDefaultCharset() {
        return this.charset;
    }

    /** @deprecated */
    @Deprecated
    public String getEncoding() {
        return this.charset;
    }

    static {
        int i;

        for (i = 97; i <= 122; ++i) {
            URLCodec.WWW_FORM_URL.set(i);
        }

        for (i = 65; i <= 90; ++i) {
            URLCodec.WWW_FORM_URL.set(i);
        }

        for (i = 48; i <= 57; ++i) {
            URLCodec.WWW_FORM_URL.set(i);
        }

        URLCodec.WWW_FORM_URL.set(45);
        URLCodec.WWW_FORM_URL.set(95);
        URLCodec.WWW_FORM_URL.set(46);
        URLCodec.WWW_FORM_URL.set(42);
        URLCodec.WWW_FORM_URL.set(32);
    }
}
