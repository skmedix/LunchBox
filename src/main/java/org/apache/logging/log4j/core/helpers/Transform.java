package org.apache.logging.log4j.core.helpers;

public final class Transform {

    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";
    private static final String CDATA_PSEUDO_END = "]]&gt;";
    private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
    private static final int CDATA_END_LEN = "]]>".length();

    public static String escapeHtmlTags(String s) {
        if (!Strings.isEmpty(s) && (s.indexOf(34) != -1 || s.indexOf(38) != -1 || s.indexOf(60) != -1 || s.indexOf(62) != -1)) {
            StringBuilder stringbuilder = new StringBuilder(s.length() + 6);
            boolean flag = true;
            int i = s.length();

            for (int j = 0; j < i; ++j) {
                char c0 = s.charAt(j);

                if (c0 > 62) {
                    stringbuilder.append(c0);
                } else if (c0 == 60) {
                    stringbuilder.append("&lt;");
                } else if (c0 == 62) {
                    stringbuilder.append("&gt;");
                } else if (c0 == 38) {
                    stringbuilder.append("&amp;");
                } else if (c0 == 34) {
                    stringbuilder.append("&quot;");
                } else {
                    stringbuilder.append(c0);
                }
            }

            return stringbuilder.toString();
        } else {
            return s;
        }
    }

    public static void appendEscapingCDATA(StringBuilder stringbuilder, String s) {
        if (s != null) {
            int i = s.indexOf("]]>");

            if (i < 0) {
                stringbuilder.append(s);
            } else {
                int j;

                for (j = 0; i > -1; i = s.indexOf("]]>", j)) {
                    stringbuilder.append(s.substring(j, i));
                    stringbuilder.append("]]>]]&gt;<![CDATA[");
                    j = i + Transform.CDATA_END_LEN;
                    if (j >= s.length()) {
                        return;
                    }
                }

                stringbuilder.append(s.substring(j));
            }
        }

    }

    public static String escapeJsonControlCharacters(String s) {
        if (!Strings.isEmpty(s) && (s.indexOf(34) != -1 || s.indexOf(92) != -1 || s.indexOf(47) != -1 || s.indexOf(8) != -1 || s.indexOf(12) != -1 || s.indexOf(10) != -1 || s.indexOf(13) != -1 || s.indexOf(9) != -1)) {
            StringBuilder stringbuilder = new StringBuilder(s.length() + 6);
            int i = s.length();

            for (int j = 0; j < i; ++j) {
                char c0 = s.charAt(j);
                String s1 = "\\\\";

                switch (c0) {
                case '\b':
                    stringbuilder.append("\\\\");
                    stringbuilder.append('b');
                    break;

                case '\t':
                    stringbuilder.append("\\\\");
                    stringbuilder.append('t');
                    break;

                case '\n':
                    stringbuilder.append("\\\\");
                    stringbuilder.append('n');
                    break;

                case '\f':
                    stringbuilder.append("\\\\");
                    stringbuilder.append('f');
                    break;

                case '\r':
                    stringbuilder.append("\\\\");
                    stringbuilder.append('r');
                    break;

                case '\"':
                    stringbuilder.append("\\\\");
                    stringbuilder.append(c0);
                    break;

                case '/':
                    stringbuilder.append("\\\\");
                    stringbuilder.append(c0);
                    break;

                case '\\':
                    stringbuilder.append("\\\\");
                    stringbuilder.append(c0);
                    break;

                default:
                    stringbuilder.append(c0);
                }
            }

            return stringbuilder.toString();
        } else {
            return s;
        }
    }
}
