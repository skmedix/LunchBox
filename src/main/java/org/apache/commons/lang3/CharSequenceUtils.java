package org.apache.commons.lang3;

public class CharSequenceUtils {

    public static CharSequence subSequence(CharSequence charsequence, int i) {
        return charsequence == null ? null : charsequence.subSequence(i, charsequence.length());
    }

    static int indexOf(CharSequence charsequence, int i, int j) {
        if (charsequence instanceof String) {
            return ((String) charsequence).indexOf(i, j);
        } else {
            int k = charsequence.length();

            if (j < 0) {
                j = 0;
            }

            for (int l = j; l < k; ++l) {
                if (charsequence.charAt(l) == i) {
                    return l;
                }
            }

            return -1;
        }
    }

    static int indexOf(CharSequence charsequence, CharSequence charsequence1, int i) {
        return charsequence.toString().indexOf(charsequence1.toString(), i);
    }

    static int lastIndexOf(CharSequence charsequence, int i, int j) {
        if (charsequence instanceof String) {
            return ((String) charsequence).lastIndexOf(i, j);
        } else {
            int k = charsequence.length();

            if (j < 0) {
                return -1;
            } else {
                if (j >= k) {
                    j = k - 1;
                }

                for (int l = j; l >= 0; --l) {
                    if (charsequence.charAt(l) == i) {
                        return l;
                    }
                }

                return -1;
            }
        }
    }

    static int lastIndexOf(CharSequence charsequence, CharSequence charsequence1, int i) {
        return charsequence.toString().lastIndexOf(charsequence1.toString(), i);
    }

    static char[] toCharArray(CharSequence charsequence) {
        if (charsequence instanceof String) {
            return ((String) charsequence).toCharArray();
        } else {
            int i = charsequence.length();
            char[] achar = new char[charsequence.length()];

            for (int j = 0; j < i; ++j) {
                achar[j] = charsequence.charAt(j);
            }

            return achar;
        }
    }

    static boolean regionMatches(CharSequence charsequence, boolean flag, int i, CharSequence charsequence1, int j, int k) {
        if (charsequence instanceof String && charsequence1 instanceof String) {
            return ((String) charsequence).regionMatches(flag, i, (String) charsequence1, j, k);
        } else {
            int l = i;
            int i1 = j;
            int j1 = k;

            while (j1-- > 0) {
                char c0 = charsequence.charAt(l++);
                char c1 = charsequence1.charAt(i1++);

                if (c0 != c1) {
                    if (!flag) {
                        return false;
                    }

                    if (Character.toUpperCase(c0) != Character.toUpperCase(c1) && Character.toLowerCase(c0) != Character.toLowerCase(c1)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
