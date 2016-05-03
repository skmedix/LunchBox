package org.apache.commons.lang3.text.translate;

public class JavaUnicodeEscaper extends UnicodeEscaper {

    public static JavaUnicodeEscaper above(int i) {
        return outsideOf(0, i);
    }

    public static JavaUnicodeEscaper below(int i) {
        return outsideOf(i, Integer.MAX_VALUE);
    }

    public static JavaUnicodeEscaper between(int i, int j) {
        return new JavaUnicodeEscaper(i, j, true);
    }

    public static JavaUnicodeEscaper outsideOf(int i, int j) {
        return new JavaUnicodeEscaper(i, j, false);
    }

    public JavaUnicodeEscaper(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    protected String toUtf16Escape(int i) {
        char[] achar = Character.toChars(i);

        return "\\u" + hex(achar[0]) + "\\u" + hex(achar[1]);
    }
}
