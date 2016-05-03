package org.apache.commons.lang3;

public class CharSetUtils {

    public static String squeeze(String s, String... astring) {
        if (!StringUtils.isEmpty(s) && !deepEmpty(astring)) {
            CharSet charset = CharSet.getInstance(astring);
            StringBuilder stringbuilder = new StringBuilder(s.length());
            char[] achar = s.toCharArray();
            int i = achar.length;
            char c0 = 32;
            boolean flag = true;

            for (int j = 0; j < i; ++j) {
                char c1 = achar[j];

                if (c1 != c0 || j == 0 || !charset.contains(c1)) {
                    stringbuilder.append(c1);
                    c0 = c1;
                }
            }

            return stringbuilder.toString();
        } else {
            return s;
        }
    }

    public static boolean containsAny(String s, String... astring) {
        if (!StringUtils.isEmpty(s) && !deepEmpty(astring)) {
            CharSet charset = CharSet.getInstance(astring);
            char[] achar = s.toCharArray();
            int i = achar.length;

            for (int j = 0; j < i; ++j) {
                char c0 = achar[j];

                if (charset.contains(c0)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static int count(String s, String... astring) {
        if (!StringUtils.isEmpty(s) && !deepEmpty(astring)) {
            CharSet charset = CharSet.getInstance(astring);
            int i = 0;
            char[] achar = s.toCharArray();
            int j = achar.length;

            for (int k = 0; k < j; ++k) {
                char c0 = achar[k];

                if (charset.contains(c0)) {
                    ++i;
                }
            }

            return i;
        } else {
            return 0;
        }
    }

    public static String keep(String s, String... astring) {
        return s == null ? null : (!s.isEmpty() && !deepEmpty(astring) ? modify(s, astring, true) : "");
    }

    public static String delete(String s, String... astring) {
        return !StringUtils.isEmpty(s) && !deepEmpty(astring) ? modify(s, astring, false) : s;
    }

    private static String modify(String s, String[] astring, boolean flag) {
        CharSet charset = CharSet.getInstance(astring);
        StringBuilder stringbuilder = new StringBuilder(s.length());
        char[] achar = s.toCharArray();
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            if (charset.contains(achar[j]) == flag) {
                stringbuilder.append(achar[j]);
            }
        }

        return stringbuilder.toString();
    }

    private static boolean deepEmpty(String[] astring) {
        if (astring != null) {
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                if (StringUtils.isNotEmpty(s)) {
                    return false;
                }
            }
        }

        return true;
    }
}
