package org.apache.commons.codec.binary;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.Charsets;

public class StringUtils {

    private static byte[] getBytes(String s, Charset charset) {
        return s == null ? null : s.getBytes(charset);
    }

    public static byte[] getBytesIso8859_1(String s) {
        return getBytes(s, Charsets.ISO_8859_1);
    }

    public static byte[] getBytesUnchecked(String s, String s1) {
        if (s == null) {
            return null;
        } else {
            try {
                return s.getBytes(s1);
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw newIllegalStateException(s1, unsupportedencodingexception);
            }
        }
    }

    public static byte[] getBytesUsAscii(String s) {
        return getBytes(s, Charsets.US_ASCII);
    }

    public static byte[] getBytesUtf16(String s) {
        return getBytes(s, Charsets.UTF_16);
    }

    public static byte[] getBytesUtf16Be(String s) {
        return getBytes(s, Charsets.UTF_16BE);
    }

    public static byte[] getBytesUtf16Le(String s) {
        return getBytes(s, Charsets.UTF_16LE);
    }

    public static byte[] getBytesUtf8(String s) {
        return getBytes(s, Charsets.UTF_8);
    }

    private static IllegalStateException newIllegalStateException(String s, UnsupportedEncodingException unsupportedencodingexception) {
        return new IllegalStateException(s + ": " + unsupportedencodingexception);
    }

    private static String newString(byte[] abyte, Charset charset) {
        return abyte == null ? null : new String(abyte, charset);
    }

    public static String newString(byte[] abyte, String s) {
        if (abyte == null) {
            return null;
        } else {
            try {
                return new String(abyte, s);
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw newIllegalStateException(s, unsupportedencodingexception);
            }
        }
    }

    public static String newStringIso8859_1(byte[] abyte) {
        return new String(abyte, Charsets.ISO_8859_1);
    }

    public static String newStringUsAscii(byte[] abyte) {
        return new String(abyte, Charsets.US_ASCII);
    }

    public static String newStringUtf16(byte[] abyte) {
        return new String(abyte, Charsets.UTF_16);
    }

    public static String newStringUtf16Be(byte[] abyte) {
        return new String(abyte, Charsets.UTF_16BE);
    }

    public static String newStringUtf16Le(byte[] abyte) {
        return new String(abyte, Charsets.UTF_16LE);
    }

    public static String newStringUtf8(byte[] abyte) {
        return newString(abyte, Charsets.UTF_8);
    }
}
