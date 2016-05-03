package org.apache.commons.codec.binary;

import java.nio.charset.Charset;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class Hex implements BinaryEncoder, BinaryDecoder {

    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final char[] DIGITS_LOWER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final Charset charset;

    public static byte[] decodeHex(char[] achar) throws DecoderException {
        int i = achar.length;

        if ((i & 1) != 0) {
            throw new DecoderException("Odd number of characters.");
        } else {
            byte[] abyte = new byte[i >> 1];
            int j = 0;

            for (int k = 0; k < i; ++j) {
                int l = toDigit(achar[k], k) << 4;

                ++k;
                l |= toDigit(achar[k], k);
                ++k;
                abyte[j] = (byte) (l & 255);
            }

            return abyte;
        }
    }

    public static char[] encodeHex(byte[] abyte) {
        return encodeHex(abyte, true);
    }

    public static char[] encodeHex(byte[] abyte, boolean flag) {
        return encodeHex(abyte, flag ? Hex.DIGITS_LOWER : Hex.DIGITS_UPPER);
    }

    protected static char[] encodeHex(byte[] abyte, char[] achar) {
        int i = abyte.length;
        char[] achar1 = new char[i << 1];
        int j = 0;

        for (int k = 0; j < i; ++j) {
            achar1[k++] = achar[(240 & abyte[j]) >>> 4];
            achar1[k++] = achar[15 & abyte[j]];
        }

        return achar1;
    }

    public static String encodeHexString(byte[] abyte) {
        return new String(encodeHex(abyte));
    }

    protected static int toDigit(char c0, int i) throws DecoderException {
        int j = Character.digit(c0, 16);

        if (j == -1) {
            throw new DecoderException("Illegal hexadecimal character " + c0 + " at index " + i);
        } else {
            return j;
        }
    }

    public Hex() {
        this.charset = Hex.DEFAULT_CHARSET;
    }

    public Hex(Charset charset) {
        this.charset = charset;
    }

    public Hex(String s) {
        this(Charset.forName(s));
    }

    public byte[] decode(byte[] abyte) throws DecoderException {
        return decodeHex((new String(abyte, this.getCharset())).toCharArray());
    }

    public Object decode(Object object) throws DecoderException {
        try {
            char[] achar = object instanceof String ? ((String) object).toCharArray() : (char[]) ((char[]) object);

            return decodeHex(achar);
        } catch (ClassCastException classcastexception) {
            throw new DecoderException(classcastexception.getMessage(), classcastexception);
        }
    }

    public byte[] encode(byte[] abyte) {
        return encodeHexString(abyte).getBytes(this.getCharset());
    }

    public Object encode(Object object) throws EncoderException {
        try {
            byte[] abyte = object instanceof String ? ((String) object).getBytes(this.getCharset()) : (byte[]) ((byte[]) object);

            return encodeHex(abyte);
        } catch (ClassCastException classcastexception) {
            throw new EncoderException(classcastexception.getMessage(), classcastexception);
        }
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getCharsetName() {
        return this.charset.name();
    }

    public String toString() {
        return super.toString() + "[charsetName=" + this.charset + "]";
    }
}
