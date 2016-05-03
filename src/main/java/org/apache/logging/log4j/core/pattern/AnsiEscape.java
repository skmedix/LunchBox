package org.apache.logging.log4j.core.pattern;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum AnsiEscape {

    PREFIX("\u001b["), SUFFIX("m"), SEPARATOR(";"), NORMAL("0"), BRIGHT("1"), DIM("2"), UNDERLINE("3"), BLINK("5"), REVERSE("7"), HIDDEN("8"), BLACK("30"), FG_BLACK("30"), RED("31"), FG_RED("31"), GREEN("32"), FG_GREEN("32"), YELLOW("33"), FG_YELLOW("33"), BLUE("34"), FG_BLUE("34"), MAGENTA("35"), FG_MAGENTA("35"), CYAN("36"), FG_CYAN("36"), WHITE("37"), FG_WHITE("37"), DEFAULT("39"), FG_DEFAULT("39"), BG_BLACK("40"), BG_RED("41"), BG_GREEN("42"), BG_YELLOW("43"), BG_BLUE("44"), BG_MAGENTA("45"), BG_CYAN("46"), BG_WHITE("47");

    private static final String WHITESPACE_REGEX = "\\s*";
    private final String code;

    private AnsiEscape(String s) {
        this.code = s;
    }

    public static String getDefaultStyle() {
        return AnsiEscape.PREFIX.getCode() + AnsiEscape.SUFFIX.getCode();
    }

    private static String toRegexSeparator(String s) {
        return "\\s*" + s + "\\s*";
    }

    public String getCode() {
        return this.code;
    }

    public static Map createMap(String s, String[] astring) {
        return createMap(s.split(toRegexSeparator(",")), astring);
    }

    public static Map createMap(String[] astring, String[] astring1) {
        String[] astring2 = astring1 != null ? (String[]) astring1.clone() : new String[0];

        Arrays.sort(astring2);
        HashMap hashmap = new HashMap();
        String[] astring3 = astring;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s = astring3[j];
            String[] astring4 = s.split(toRegexSeparator("="));

            if (astring4.length > 1) {
                String s1 = astring4[0].toUpperCase(Locale.ENGLISH);
                String s2 = astring4[1];
                boolean flag = Arrays.binarySearch(astring2, s1) < 0;

                hashmap.put(s1, flag ? createSequence(s2.split("\\s")) : s2);
            }
        }

        return hashmap;
    }

    public static String createSequence(String... astring) {
        if (astring == null) {
            return getDefaultStyle();
        } else {
            StringBuilder stringbuilder = new StringBuilder(AnsiEscape.PREFIX.getCode());
            boolean flag = true;
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                try {
                    AnsiEscape ansiescape = valueOf(s.trim().toUpperCase(Locale.ENGLISH));

                    if (!flag) {
                        stringbuilder.append(AnsiEscape.SEPARATOR.getCode());
                    }

                    flag = false;
                    stringbuilder.append(ansiescape.getCode());
                } catch (Exception exception) {
                    ;
                }
            }

            stringbuilder.append(AnsiEscape.SUFFIX.getCode());
            return stringbuilder.toString();
        }
    }
}
