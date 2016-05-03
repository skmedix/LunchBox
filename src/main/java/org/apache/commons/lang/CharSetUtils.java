package org.apache.commons.lang;

import org.apache.commons.lang.text.StrBuilder;

public class CharSetUtils {

    /** @deprecated */
    public static CharSet evaluateSet(String[] set) {
        return set == null ? null : new CharSet(set);
    }

    public static String squeeze(String str, String set) {
        if (!StringUtils.isEmpty(str) && !StringUtils.isEmpty(set)) {
            String[] strs = new String[] { set};

            return squeeze(str, strs);
        } else {
            return str;
        }
    }

    public static String squeeze(String str, String[] set) {
        if (!StringUtils.isEmpty(str) && !ArrayUtils.isEmpty((Object[]) set)) {
            CharSet chars = CharSet.getInstance(set);
            StrBuilder buffer = new StrBuilder(str.length());
            char[] chrs = str.toCharArray();
            int sz = chrs.length;
            char lastChar = 32;
            boolean ch = true;

            for (int i = 0; i < sz; ++i) {
                char c0 = chrs[i];

                if (!chars.contains(c0) || c0 != lastChar || i == 0) {
                    buffer.append(c0);
                    lastChar = c0;
                }
            }

            return buffer.toString();
        } else {
            return str;
        }
    }

    public static int count(String str, String set) {
        if (!StringUtils.isEmpty(str) && !StringUtils.isEmpty(set)) {
            String[] strs = new String[] { set};

            return count(str, strs);
        } else {
            return 0;
        }
    }

    public static int count(String str, String[] set) {
        if (!StringUtils.isEmpty(str) && !ArrayUtils.isEmpty((Object[]) set)) {
            CharSet chars = CharSet.getInstance(set);
            int count = 0;
            char[] chrs = str.toCharArray();
            int sz = chrs.length;

            for (int i = 0; i < sz; ++i) {
                if (chars.contains(chrs[i])) {
                    ++count;
                }
            }

            return count;
        } else {
            return 0;
        }
    }

    public static String keep(String str, String set) {
        if (str == null) {
            return null;
        } else if (str.length() != 0 && !StringUtils.isEmpty(set)) {
            String[] strs = new String[] { set};

            return keep(str, strs);
        } else {
            return "";
        }
    }

    public static String keep(String str, String[] set) {
        return str == null ? null : (str.length() != 0 && !ArrayUtils.isEmpty((Object[]) set) ? modify(str, set, true) : "");
    }

    public static String delete(String str, String set) {
        if (!StringUtils.isEmpty(str) && !StringUtils.isEmpty(set)) {
            String[] strs = new String[] { set};

            return delete(str, strs);
        } else {
            return str;
        }
    }

    public static String delete(String str, String[] set) {
        return !StringUtils.isEmpty(str) && !ArrayUtils.isEmpty((Object[]) set) ? modify(str, set, false) : str;
    }

    private static String modify(String str, String[] set, boolean expect) {
        CharSet chars = CharSet.getInstance(set);
        StrBuilder buffer = new StrBuilder(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;

        for (int i = 0; i < sz; ++i) {
            if (chars.contains(chrs[i]) == expect) {
                buffer.append(chrs[i]);
            }
        }

        return buffer.toString();
    }

    /** @deprecated */
    public static String translate(String str, String searchChars, String replaceChars) {
        if (StringUtils.isEmpty(str)) {
            return str;
        } else {
            StrBuilder buffer = new StrBuilder(str.length());
            char[] chrs = str.toCharArray();
            char[] withChrs = replaceChars.toCharArray();
            int sz = chrs.length;
            int withMax = replaceChars.length() - 1;

            for (int i = 0; i < sz; ++i) {
                int idx = searchChars.indexOf(chrs[i]);

                if (idx != -1) {
                    if (idx > withMax) {
                        idx = withMax;
                    }

                    buffer.append(withChrs[idx]);
                } else {
                    buffer.append(chrs[i]);
                }
            }

            return buffer.toString();
        }
    }
}
