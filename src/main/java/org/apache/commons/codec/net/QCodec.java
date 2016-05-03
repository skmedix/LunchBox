package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

public class QCodec extends RFC1522Codec implements StringEncoder, StringDecoder {

    private final Charset charset;
    private static final BitSet PRINTABLE_CHARS = new BitSet(256);
    private static final byte BLANK = 32;
    private static final byte UNDERSCORE = 95;
    private boolean encodeBlanks;

    public QCodec() {
        this(Charsets.UTF_8);
    }

    public QCodec(Charset charset) {
        this.encodeBlanks = false;
        this.charset = charset;
    }

    public QCodec(String s) {
        this(Charset.forName(s));
    }

    protected String getEncoding() {
        return "Q";
    }

    protected byte[] doEncoding(byte[] abyte) {
        if (abyte == null) {
            return null;
        } else {
            byte[] abyte1 = QuotedPrintableCodec.encodeQuotedPrintable(QCodec.PRINTABLE_CHARS, abyte);

            if (this.encodeBlanks) {
                for (int i = 0; i < abyte1.length; ++i) {
                    if (abyte1[i] == 32) {
                        abyte1[i] = 95;
                    }
                }
            }

            return abyte1;
        }
    }

    protected byte[] doDecoding(byte[] abyte) throws DecoderException {
        if (abyte == null) {
            return null;
        } else {
            boolean flag = false;
            byte[] abyte1 = abyte;
            int i = abyte.length;

            for (int j = 0; j < i; ++j) {
                byte b0 = abyte1[j];

                if (b0 == 95) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                abyte1 = new byte[abyte.length];

                for (i = 0; i < abyte.length; ++i) {
                    byte b1 = abyte[i];

                    if (b1 != 95) {
                        abyte1[i] = b1;
                    } else {
                        abyte1[i] = 32;
                    }
                }

                return QuotedPrintableCodec.decodeQuotedPrintable(abyte1);
            } else {
                return QuotedPrintableCodec.decodeQuotedPrintable(abyte);
            }
        }
    }

    public String encode(String s, Charset charset) throws EncoderException {
        return s == null ? null : this.encodeText(s, charset);
    }

    public String encode(String s, String s1) throws EncoderException {
        if (s == null) {
            return null;
        } else {
            try {
                return this.encodeText(s, s1);
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw new EncoderException(unsupportedencodingexception.getMessage(), unsupportedencodingexception);
            }
        }
    }

    public String encode(String s) throws EncoderException {
        return s == null ? null : this.encode(s, this.getCharset());
    }

    public String decode(String s) throws DecoderException {
        if (s == null) {
            return null;
        } else {
            try {
                return this.decodeText(s);
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw new DecoderException(unsupportedencodingexception.getMessage(), unsupportedencodingexception);
            }
        }
    }

    public Object encode(Object object) throws EncoderException {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return this.encode((String) object);
        } else {
            throw new EncoderException("Objects of type " + object.getClass().getName() + " cannot be encoded using Q codec");
        }
    }

    public Object decode(Object object) throws DecoderException {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return this.decode((String) object);
        } else {
            throw new DecoderException("Objects of type " + object.getClass().getName() + " cannot be decoded using Q codec");
        }
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }

    public boolean isEncodeBlanks() {
        return this.encodeBlanks;
    }

    public void setEncodeBlanks(boolean flag) {
        this.encodeBlanks = flag;
    }

    static {
        QCodec.PRINTABLE_CHARS.set(32);
        QCodec.PRINTABLE_CHARS.set(33);
        QCodec.PRINTABLE_CHARS.set(34);
        QCodec.PRINTABLE_CHARS.set(35);
        QCodec.PRINTABLE_CHARS.set(36);
        QCodec.PRINTABLE_CHARS.set(37);
        QCodec.PRINTABLE_CHARS.set(38);
        QCodec.PRINTABLE_CHARS.set(39);
        QCodec.PRINTABLE_CHARS.set(40);
        QCodec.PRINTABLE_CHARS.set(41);
        QCodec.PRINTABLE_CHARS.set(42);
        QCodec.PRINTABLE_CHARS.set(43);
        QCodec.PRINTABLE_CHARS.set(44);
        QCodec.PRINTABLE_CHARS.set(45);
        QCodec.PRINTABLE_CHARS.set(46);
        QCodec.PRINTABLE_CHARS.set(47);

        int i;

        for (i = 48; i <= 57; ++i) {
            QCodec.PRINTABLE_CHARS.set(i);
        }

        QCodec.PRINTABLE_CHARS.set(58);
        QCodec.PRINTABLE_CHARS.set(59);
        QCodec.PRINTABLE_CHARS.set(60);
        QCodec.PRINTABLE_CHARS.set(62);
        QCodec.PRINTABLE_CHARS.set(64);

        for (i = 65; i <= 90; ++i) {
            QCodec.PRINTABLE_CHARS.set(i);
        }

        QCodec.PRINTABLE_CHARS.set(91);
        QCodec.PRINTABLE_CHARS.set(92);
        QCodec.PRINTABLE_CHARS.set(93);
        QCodec.PRINTABLE_CHARS.set(94);
        QCodec.PRINTABLE_CHARS.set(96);

        for (i = 97; i <= 122; ++i) {
            QCodec.PRINTABLE_CHARS.set(i);
        }

        QCodec.PRINTABLE_CHARS.set(123);
        QCodec.PRINTABLE_CHARS.set(124);
        QCodec.PRINTABLE_CHARS.set(125);
        QCodec.PRINTABLE_CHARS.set(126);
    }
}
