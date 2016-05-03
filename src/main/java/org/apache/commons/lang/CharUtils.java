package org.apache.commons.lang;

public class CharUtils {

    private static final String CHAR_STRING = "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f";
    private static final String[] CHAR_STRING_ARRAY = new String[128];
    private static final Character[] CHAR_ARRAY = new Character[128];
    public static final char LF = '\n';
    public static final char CR = '\r';

    public static Character toCharacterObject(char ch) {
        return ch < CharUtils.CHAR_ARRAY.length ? CharUtils.CHAR_ARRAY[ch] : new Character(ch);
    }

    public static Character toCharacterObject(String str) {
        return StringUtils.isEmpty(str) ? null : toCharacterObject(str.charAt(0));
    }

    public static char toChar(Character ch) {
        if (ch == null) {
            throw new IllegalArgumentException("The Character must not be null");
        } else {
            return ch.charValue();
        }
    }

    public static char toChar(Character ch, char defaultValue) {
        return ch == null ? defaultValue : ch.charValue();
    }

    public static char toChar(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("The String must not be empty");
        } else {
            return str.charAt(0);
        }
    }

    public static char toChar(String str, char defaultValue) {
        return StringUtils.isEmpty(str) ? defaultValue : str.charAt(0);
    }

    public static int toIntValue(char ch) {
        if (!isAsciiNumeric(ch)) {
            throw new IllegalArgumentException("The character " + ch + " is not in the range \'0\' - \'9\'");
        } else {
            return ch - 48;
        }
    }

    public static int toIntValue(char ch, int defaultValue) {
        return !isAsciiNumeric(ch) ? defaultValue : ch - 48;
    }

    public static int toIntValue(Character ch) {
        if (ch == null) {
            throw new IllegalArgumentException("The character must not be null");
        } else {
            return toIntValue(ch.charValue());
        }
    }

    public static int toIntValue(Character ch, int defaultValue) {
        return ch == null ? defaultValue : toIntValue(ch.charValue(), defaultValue);
    }

    public static String toString(char ch) {
        return ch < 128 ? CharUtils.CHAR_STRING_ARRAY[ch] : new String(new char[] { ch});
    }

    public static String toString(Character ch) {
        return ch == null ? null : toString(ch.charValue());
    }

    public static String unicodeEscaped(char ch) {
        return ch < 16 ? "\\u000" + Integer.toHexString(ch) : (ch < 256 ? "\\u00" + Integer.toHexString(ch) : (ch < 4096 ? "\\u0" + Integer.toHexString(ch) : "\\u" + Integer.toHexString(ch)));
    }

    public static String unicodeEscaped(Character ch) {
        return ch == null ? null : unicodeEscaped(ch.charValue());
    }

    public static boolean isAscii(char ch) {
        return ch < 128;
    }

    public static boolean isAsciiPrintable(char ch) {
        return ch >= 32 && ch < 127;
    }

    public static boolean isAsciiControl(char ch) {
        return ch < 32 || ch == 127;
    }

    public static boolean isAsciiAlpha(char ch) {
        return ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122;
    }

    public static boolean isAsciiAlphaUpper(char ch) {
        return ch >= 65 && ch <= 90;
    }

    public static boolean isAsciiAlphaLower(char ch) {
        return ch >= 97 && ch <= 122;
    }

    public static boolean isAsciiNumeric(char ch) {
        return ch >= 48 && ch <= 57;
    }

    public static boolean isAsciiAlphanumeric(char ch) {
        return ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122 || ch >= 48 && ch <= 57;
    }

    static boolean isHighSurrogate(char ch) {
        return '\ud800' <= ch && '\udbff' >= ch;
    }

    static {
        for (int i = 127; i >= 0; --i) {
            CharUtils.CHAR_STRING_ARRAY[i] = "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f".substring(i, i + 1);
            CharUtils.CHAR_ARRAY[i] = new Character((char) i);
        }

    }
}
