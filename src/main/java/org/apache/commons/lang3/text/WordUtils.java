package org.apache.commons.lang3.text;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class WordUtils {

    public static String wrap(String s, int i) {
        return wrap(s, i, (String) null, false);
    }

    public static String wrap(String s, int i, String s1, boolean flag) {
        if (s == null) {
            return null;
        } else {
            if (s1 == null) {
                s1 = SystemUtils.LINE_SEPARATOR;
            }

            if (i < 1) {
                i = 1;
            }

            int j = s.length();
            int k = 0;
            StringBuilder stringbuilder = new StringBuilder(j + 32);

            while (j - k > i) {
                if (s.charAt(k) == 32) {
                    ++k;
                } else {
                    int l = s.lastIndexOf(32, i + k);

                    if (l >= k) {
                        stringbuilder.append(s.substring(k, l));
                        stringbuilder.append(s1);
                        k = l + 1;
                    } else if (flag) {
                        stringbuilder.append(s.substring(k, i + k));
                        stringbuilder.append(s1);
                        k += i;
                    } else {
                        l = s.indexOf(32, i + k);
                        if (l >= 0) {
                            stringbuilder.append(s.substring(k, l));
                            stringbuilder.append(s1);
                            k = l + 1;
                        } else {
                            stringbuilder.append(s.substring(k));
                            k = j;
                        }
                    }
                }
            }

            stringbuilder.append(s.substring(k));
            return stringbuilder.toString();
        }
    }

    public static String capitalize(String s) {
        return capitalize(s, (char[]) null);
    }

    public static String capitalize(String s, char... achar) {
        int i = achar == null ? -1 : achar.length;

        if (!StringUtils.isEmpty(s) && i != 0) {
            char[] achar1 = s.toCharArray();
            boolean flag = true;

            for (int j = 0; j < achar1.length; ++j) {
                char c0 = achar1[j];

                if (isDelimiter(c0, achar)) {
                    flag = true;
                } else if (flag) {
                    achar1[j] = Character.toTitleCase(c0);
                    flag = false;
                }
            }

            return new String(achar1);
        } else {
            return s;
        }
    }

    public static String capitalizeFully(String s) {
        return capitalizeFully(s, (char[]) null);
    }

    public static String capitalizeFully(String s, char... achar) {
        int i = achar == null ? -1 : achar.length;

        if (!StringUtils.isEmpty(s) && i != 0) {
            s = s.toLowerCase();
            return capitalize(s, achar);
        } else {
            return s;
        }
    }

    public static String uncapitalize(String s) {
        return uncapitalize(s, (char[]) null);
    }

    public static String uncapitalize(String s, char... achar) {
        int i = achar == null ? -1 : achar.length;

        if (!StringUtils.isEmpty(s) && i != 0) {
            char[] achar1 = s.toCharArray();
            boolean flag = true;

            for (int j = 0; j < achar1.length; ++j) {
                char c0 = achar1[j];

                if (isDelimiter(c0, achar)) {
                    flag = true;
                } else if (flag) {
                    achar1[j] = Character.toLowerCase(c0);
                    flag = false;
                }
            }

            return new String(achar1);
        } else {
            return s;
        }
    }

    public static String swapCase(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        } else {
            char[] achar = s.toCharArray();
            boolean flag = true;

            for (int i = 0; i < achar.length; ++i) {
                char c0 = achar[i];

                if (Character.isUpperCase(c0)) {
                    achar[i] = Character.toLowerCase(c0);
                    flag = false;
                } else if (Character.isTitleCase(c0)) {
                    achar[i] = Character.toLowerCase(c0);
                    flag = false;
                } else if (Character.isLowerCase(c0)) {
                    if (flag) {
                        achar[i] = Character.toTitleCase(c0);
                        flag = false;
                    } else {
                        achar[i] = Character.toUpperCase(c0);
                    }
                } else {
                    flag = Character.isWhitespace(c0);
                }
            }

            return new String(achar);
        }
    }

    public static String initials(String s) {
        return initials(s, (char[]) null);
    }

    public static String initials(String s, char... achar) {
        if (StringUtils.isEmpty(s)) {
            return s;
        } else if (achar != null && achar.length == 0) {
            return "";
        } else {
            int i = s.length();
            char[] achar1 = new char[i / 2 + 1];
            int j = 0;
            boolean flag = true;

            for (int k = 0; k < i; ++k) {
                char c0 = s.charAt(k);

                if (isDelimiter(c0, achar)) {
                    flag = true;
                } else if (flag) {
                    achar1[j++] = c0;
                    flag = false;
                }
            }

            return new String(achar1, 0, j);
        }
    }

    private static boolean isDelimiter(char c0, char[] achar) {
        if (achar == null) {
            return Character.isWhitespace(c0);
        } else {
            char[] achar1 = achar;
            int i = achar.length;

            for (int j = 0; j < i; ++j) {
                char c1 = achar1[j];

                if (c0 == c1) {
                    return true;
                }
            }

            return false;
        }
    }
}
