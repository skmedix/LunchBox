package org.apache.commons.lang3;

public class CharUtils {

    private static final String[] CHAR_STRING_ARRAY = new String[128];
    public static final char LF = '\n';
    public static final char CR = '\r';

    /** @deprecated */
    @Deprecated
    public static Character toCharacterObject(char c0) {
        return Character.valueOf(c0);
    }

    public static Character toCharacterObject(String s) {
        return StringUtils.isEmpty(s) ? null : Character.valueOf(s.charAt(0));
    }

    public static char toChar(Character character) {
        if (character == null) {
            throw new IllegalArgumentException("The Character must not be null");
        } else {
            return character.charValue();
        }
    }

    public static char toChar(Character character, char c0) {
        return character == null ? c0 : character.charValue();
    }

    public static char toChar(String s) {
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException("The String must not be empty");
        } else {
            return s.charAt(0);
        }
    }

    public static char toChar(String s, char c0) {
        return StringUtils.isEmpty(s) ? c0 : s.charAt(0);
    }

    public static int toIntValue(char c0) {
        if (!isAsciiNumeric(c0)) {
            throw new IllegalArgumentException("The character " + c0 + " is not in the range \'0\' - \'9\'");
        } else {
            return c0 - 48;
        }
    }

    public static int toIntValue(char c0, int i) {
        return !isAsciiNumeric(c0) ? i : c0 - 48;
    }

    public static int toIntValue(Character character) {
        if (character == null) {
            throw new IllegalArgumentException("The character must not be null");
        } else {
            return toIntValue(character.charValue());
        }
    }

    public static int toIntValue(Character character, int i) {
        return character == null ? i : toIntValue(character.charValue(), i);
    }

    public static String toString(char c0) {
        return c0 < 128 ? CharUtils.CHAR_STRING_ARRAY[c0] : new String(new char[] { c0});
    }

    public static String toString(Character character) {
        return character == null ? null : toString(character.charValue());
    }

    public static String unicodeEscaped(char c0) {
        return c0 < 16 ? "\\u000" + Integer.toHexString(c0) : (c0 < 256 ? "\\u00" + Integer.toHexString(c0) : (c0 < 4096 ? "\\u0" + Integer.toHexString(c0) : "\\u" + Integer.toHexString(c0)));
    }

    public static String unicodeEscaped(Character character) {
        return character == null ? null : unicodeEscaped(character.charValue());
    }

    public static boolean isAscii(char c0) {
        return c0 < 128;
    }

    public static boolean isAsciiPrintable(char c0) {
        return c0 >= 32 && c0 < 127;
    }

    public static boolean isAsciiControl(char c0) {
        return c0 < 32 || c0 == 127;
    }

    public static boolean isAsciiAlpha(char c0) {
        return isAsciiAlphaUpper(c0) || isAsciiAlphaLower(c0);
    }

    public static boolean isAsciiAlphaUpper(char c0) {
        return c0 >= 65 && c0 <= 90;
    }

    public static boolean isAsciiAlphaLower(char c0) {
        return c0 >= 97 && c0 <= 122;
    }

    public static boolean isAsciiNumeric(char c0) {
        return c0 >= 48 && c0 <= 57;
    }

    public static boolean isAsciiAlphanumeric(char c0) {
        return isAsciiAlpha(c0) || isAsciiNumeric(c0);
    }

    static {
        for (char c0 = 0; c0 < CharUtils.CHAR_STRING_ARRAY.length; ++c0) {
            CharUtils.CHAR_STRING_ARRAY[c0] = String.valueOf(c0);
        }

    }
}
