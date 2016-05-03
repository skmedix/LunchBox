package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;

public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder {

    private final Charset charset;

    public BCodec() {
        this(Charsets.UTF_8);
    }

    public BCodec(Charset charset) {
        this.charset = charset;
    }

    public BCodec(String s) {
        this(Charset.forName(s));
    }

    protected String getEncoding() {
        return "B";
    }

    protected byte[] doEncoding(byte[] abyte) {
        return abyte == null ? null : Base64.encodeBase64(abyte);
    }

    protected byte[] doDecoding(byte[] abyte) {
        return abyte == null ? null : Base64.decodeBase64(abyte);
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
            throw new EncoderException("Objects of type " + object.getClass().getName() + " cannot be encoded using BCodec");
        }
    }

    public Object decode(Object object) throws DecoderException {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return this.decode((String) object);
        } else {
            throw new DecoderException("Objects of type " + object.getClass().getName() + " cannot be decoded using BCodec");
        }
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }
}
