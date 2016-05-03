package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;

abstract class RFC1522Codec {

    protected static final char SEP = '?';
    protected static final String POSTFIX = "?=";
    protected static final String PREFIX = "=?";

    protected String encodeText(String s, Charset charset) throws EncoderException {
        if (s == null) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            stringbuilder.append("=?");
            stringbuilder.append(charset);
            stringbuilder.append('?');
            stringbuilder.append(this.getEncoding());
            stringbuilder.append('?');
            byte[] abyte = this.doEncoding(s.getBytes(charset));

            stringbuilder.append(StringUtils.newStringUsAscii(abyte));
            stringbuilder.append("?=");
            return stringbuilder.toString();
        }
    }

    protected String encodeText(String s, String s1) throws EncoderException, UnsupportedEncodingException {
        return s == null ? null : this.encodeText(s, Charset.forName(s1));
    }

    protected String decodeText(String s) throws DecoderException, UnsupportedEncodingException {
        if (s == null) {
            return null;
        } else if (s.startsWith("=?") && s.endsWith("?=")) {
            int i = s.length() - 2;
            byte b0 = 2;
            int j = s.indexOf(63, b0);

            if (j == i) {
                throw new DecoderException("RFC 1522 violation: charset token not found");
            } else {
                String s1 = s.substring(b0, j);

                if (s1.equals("")) {
                    throw new DecoderException("RFC 1522 violation: charset not specified");
                } else {
                    int k = j + 1;

                    j = s.indexOf(63, k);
                    if (j == i) {
                        throw new DecoderException("RFC 1522 violation: encoding token not found");
                    } else {
                        String s2 = s.substring(k, j);

                        if (!this.getEncoding().equalsIgnoreCase(s2)) {
                            throw new DecoderException("This codec cannot decode " + s2 + " encoded content");
                        } else {
                            k = j + 1;
                            j = s.indexOf(63, k);
                            byte[] abyte = StringUtils.getBytesUsAscii(s.substring(k, j));

                            abyte = this.doDecoding(abyte);
                            return new String(abyte, s1);
                        }
                    }
                }
            }
        } else {
            throw new DecoderException("RFC 1522 violation: malformed encoded content");
        }
    }

    protected abstract String getEncoding();

    protected abstract byte[] doEncoding(byte[] abyte) throws EncoderException;

    protected abstract byte[] doDecoding(byte[] abyte) throws DecoderException;
}
