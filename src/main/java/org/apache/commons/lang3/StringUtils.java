package org.apache.commons.lang3;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String LF = "\n";
    public static final String CR = "\r";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(?: |\\u00A0|\\s|[\\s&&[^ ]])\\s*");

    public static boolean isEmpty(CharSequence charsequence) {
        return charsequence == null || charsequence.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence charsequence) {
        return !isEmpty(charsequence);
    }

    public static boolean isAnyEmpty(CharSequence... acharsequence) {
        if (ArrayUtils.isEmpty((Object[]) acharsequence)) {
            return true;
        } else {
            CharSequence[] acharsequence1 = acharsequence;
            int i = acharsequence.length;

            for (int j = 0; j < i; ++j) {
                CharSequence charsequence = acharsequence1[j];

                if (isEmpty(charsequence)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isNoneEmpty(CharSequence... acharsequence) {
        return !isAnyEmpty(acharsequence);
    }

    public static boolean isBlank(CharSequence charsequence) {
        int i;

        if (charsequence != null && (i = charsequence.length()) != 0) {
            for (int j = 0; j < i; ++j) {
                if (!Character.isWhitespace(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence charsequence) {
        return !isBlank(charsequence);
    }

    public static boolean isAnyBlank(CharSequence... acharsequence) {
        if (ArrayUtils.isEmpty((Object[]) acharsequence)) {
            return true;
        } else {
            CharSequence[] acharsequence1 = acharsequence;
            int i = acharsequence.length;

            for (int j = 0; j < i; ++j) {
                CharSequence charsequence = acharsequence1[j];

                if (isBlank(charsequence)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isNoneBlank(CharSequence... acharsequence) {
        return !isAnyBlank(acharsequence);
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static String trimToNull(String s) {
        String s1 = trim(s);

        return isEmpty(s1) ? null : s1;
    }

    public static String trimToEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    public static String strip(String s) {
        return strip(s, (String) null);
    }

    public static String stripToNull(String s) {
        if (s == null) {
            return null;
        } else {
            s = strip(s, (String) null);
            return s.isEmpty() ? null : s;
        }
    }

    public static String stripToEmpty(String s) {
        return s == null ? "" : strip(s, (String) null);
    }

    public static String strip(String s, String s1) {
        if (isEmpty(s)) {
            return s;
        } else {
            s = stripStart(s, s1);
            return stripEnd(s, s1);
        }
    }

    public static String stripStart(String s, String s1) {
        int i;

        if (s != null && (i = s.length()) != 0) {
            int j = 0;

            if (s1 == null) {
                while (j != i && Character.isWhitespace(s.charAt(j))) {
                    ++j;
                }
            } else {
                if (s1.isEmpty()) {
                    return s;
                }

                while (j != i && s1.indexOf(s.charAt(j)) != -1) {
                    ++j;
                }
            }

            return s.substring(j);
        } else {
            return s;
        }
    }

    public static String stripEnd(String s, String s1) {
        int i;

        if (s != null && (i = s.length()) != 0) {
            if (s1 == null) {
                while (i != 0 && Character.isWhitespace(s.charAt(i - 1))) {
                    --i;
                }
            } else {
                if (s1.isEmpty()) {
                    return s;
                }

                while (i != 0 && s1.indexOf(s.charAt(i - 1)) != -1) {
                    --i;
                }
            }

            return s.substring(0, i);
        } else {
            return s;
        }
    }

    public static String[] stripAll(String... astring) {
        return stripAll(astring, (String) null);
    }

    public static String[] stripAll(String[] astring, String s) {
        int i;

        if (astring != null && (i = astring.length) != 0) {
            String[] astring1 = new String[i];

            for (int j = 0; j < i; ++j) {
                astring1[j] = strip(astring[j], s);
            }

            return astring1;
        } else {
            return astring;
        }
    }

    public static String stripAccents(String s) {
        if (s == null) {
            return null;
        } else {
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String s1 = Normalizer.normalize(s, Form.NFD);

            return pattern.matcher(s1).replaceAll("");
        }
    }

    public static boolean equals(CharSequence charsequence, CharSequence charsequence1) {
        return charsequence == charsequence1 ? true : (charsequence != null && charsequence1 != null ? (charsequence instanceof String && charsequence1 instanceof String ? charsequence.equals(charsequence1) : CharSequenceUtils.regionMatches(charsequence, false, 0, charsequence1, 0, Math.max(charsequence.length(), charsequence1.length()))) : false);
    }

    public static boolean equalsIgnoreCase(CharSequence charsequence, CharSequence charsequence1) {
        return charsequence != null && charsequence1 != null ? (charsequence == charsequence1 ? true : (charsequence.length() != charsequence1.length() ? false : CharSequenceUtils.regionMatches(charsequence, true, 0, charsequence1, 0, charsequence.length()))) : charsequence == charsequence1;
    }

    public static int indexOf(CharSequence charsequence, int i) {
        return isEmpty(charsequence) ? -1 : CharSequenceUtils.indexOf(charsequence, i, 0);
    }

    public static int indexOf(CharSequence charsequence, int i, int j) {
        return isEmpty(charsequence) ? -1 : CharSequenceUtils.indexOf(charsequence, i, j);
    }

    public static int indexOf(CharSequence charsequence, CharSequence charsequence1) {
        return charsequence != null && charsequence1 != null ? CharSequenceUtils.indexOf(charsequence, charsequence1, 0) : -1;
    }

    public static int indexOf(CharSequence charsequence, CharSequence charsequence1, int i) {
        return charsequence != null && charsequence1 != null ? CharSequenceUtils.indexOf(charsequence, charsequence1, i) : -1;
    }

    public static int ordinalIndexOf(CharSequence charsequence, CharSequence charsequence1, int i) {
        return ordinalIndexOf(charsequence, charsequence1, i, false);
    }

    private static int ordinalIndexOf(CharSequence charsequence, CharSequence charsequence1, int i, boolean flag) {
        if (charsequence != null && charsequence1 != null && i > 0) {
            if (charsequence1.length() == 0) {
                return flag ? charsequence.length() : 0;
            } else {
                int j = 0;
                int k = flag ? charsequence.length() : -1;

                do {
                    if (flag) {
                        k = CharSequenceUtils.lastIndexOf(charsequence, charsequence1, k - 1);
                    } else {
                        k = CharSequenceUtils.indexOf(charsequence, charsequence1, k + 1);
                    }

                    if (k < 0) {
                        return k;
                    }

                    ++j;
                } while (j < i);

                return k;
            }
        } else {
            return -1;
        }
    }

    public static int indexOfIgnoreCase(CharSequence charsequence, CharSequence charsequence1) {
        return indexOfIgnoreCase(charsequence, charsequence1, 0);
    }

    public static int indexOfIgnoreCase(CharSequence charsequence, CharSequence charsequence1, int i) {
        if (charsequence != null && charsequence1 != null) {
            if (i < 0) {
                i = 0;
            }

            int j = charsequence.length() - charsequence1.length() + 1;

            if (i > j) {
                return -1;
            } else if (charsequence1.length() == 0) {
                return i;
            } else {
                for (int k = i; k < j; ++k) {
                    if (CharSequenceUtils.regionMatches(charsequence, true, k, charsequence1, 0, charsequence1.length())) {
                        return k;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public static int lastIndexOf(CharSequence charsequence, int i) {
        return isEmpty(charsequence) ? -1 : CharSequenceUtils.lastIndexOf(charsequence, i, charsequence.length());
    }

    public static int lastIndexOf(CharSequence charsequence, int i, int j) {
        return isEmpty(charsequence) ? -1 : CharSequenceUtils.lastIndexOf(charsequence, i, j);
    }

    public static int lastIndexOf(CharSequence charsequence, CharSequence charsequence1) {
        return charsequence != null && charsequence1 != null ? CharSequenceUtils.lastIndexOf(charsequence, charsequence1, charsequence.length()) : -1;
    }

    public static int lastOrdinalIndexOf(CharSequence charsequence, CharSequence charsequence1, int i) {
        return ordinalIndexOf(charsequence, charsequence1, i, true);
    }

    public static int lastIndexOf(CharSequence charsequence, CharSequence charsequence1, int i) {
        return charsequence != null && charsequence1 != null ? CharSequenceUtils.lastIndexOf(charsequence, charsequence1, i) : -1;
    }

    public static int lastIndexOfIgnoreCase(CharSequence charsequence, CharSequence charsequence1) {
        return charsequence != null && charsequence1 != null ? lastIndexOfIgnoreCase(charsequence, charsequence1, charsequence.length()) : -1;
    }

    public static int lastIndexOfIgnoreCase(CharSequence charsequence, CharSequence charsequence1, int i) {
        if (charsequence != null && charsequence1 != null) {
            if (i > charsequence.length() - charsequence1.length()) {
                i = charsequence.length() - charsequence1.length();
            }

            if (i < 0) {
                return -1;
            } else if (charsequence1.length() == 0) {
                return i;
            } else {
                for (int j = i; j >= 0; --j) {
                    if (CharSequenceUtils.regionMatches(charsequence, true, j, charsequence1, 0, charsequence1.length())) {
                        return j;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public static boolean contains(CharSequence charsequence, int i) {
        return isEmpty(charsequence) ? false : CharSequenceUtils.indexOf(charsequence, i, 0) >= 0;
    }

    public static boolean contains(CharSequence charsequence, CharSequence charsequence1) {
        return charsequence != null && charsequence1 != null ? CharSequenceUtils.indexOf(charsequence, charsequence1, 0) >= 0 : false;
    }

    public static boolean containsIgnoreCase(CharSequence charsequence, CharSequence charsequence1) {
        if (charsequence != null && charsequence1 != null) {
            int i = charsequence1.length();
            int j = charsequence.length() - i;

            for (int k = 0; k <= j; ++k) {
                if (CharSequenceUtils.regionMatches(charsequence, true, k, charsequence1, 0, i)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean containsWhitespace(CharSequence charsequence) {
        if (isEmpty(charsequence)) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (Character.isWhitespace(charsequence.charAt(j))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static int indexOfAny(CharSequence charsequence, char... achar) {
        if (!isEmpty(charsequence) && !ArrayUtils.isEmpty(achar)) {
            int i = charsequence.length();
            int j = i - 1;
            int k = achar.length;
            int l = k - 1;

            for (int i1 = 0; i1 < i; ++i1) {
                char c0 = charsequence.charAt(i1);

                for (int j1 = 0; j1 < k; ++j1) {
                    if (achar[j1] == c0) {
                        if (i1 >= j || j1 >= l || !Character.isHighSurrogate(c0)) {
                            return i1;
                        }

                        if (achar[j1 + 1] == charsequence.charAt(i1 + 1)) {
                            return i1;
                        }
                    }
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static int indexOfAny(CharSequence charsequence, String s) {
        return !isEmpty(charsequence) && !isEmpty(s) ? indexOfAny(charsequence, s.toCharArray()) : -1;
    }

    public static boolean containsAny(CharSequence charsequence, char... achar) {
        if (!isEmpty(charsequence) && !ArrayUtils.isEmpty(achar)) {
            int i = charsequence.length();
            int j = achar.length;
            int k = i - 1;
            int l = j - 1;

            for (int i1 = 0; i1 < i; ++i1) {
                char c0 = charsequence.charAt(i1);

                for (int j1 = 0; j1 < j; ++j1) {
                    if (achar[j1] == c0) {
                        if (!Character.isHighSurrogate(c0)) {
                            return true;
                        }

                        if (j1 == l) {
                            return true;
                        }

                        if (i1 < k && achar[j1 + 1] == charsequence.charAt(i1 + 1)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean containsAny(CharSequence charsequence, CharSequence charsequence1) {
        return charsequence1 == null ? false : containsAny(charsequence, CharSequenceUtils.toCharArray(charsequence1));
    }

    public static int indexOfAnyBut(CharSequence charsequence, char... achar) {
        if (!isEmpty(charsequence) && !ArrayUtils.isEmpty(achar)) {
            int i = charsequence.length();
            int j = i - 1;
            int k = achar.length;
            int l = k - 1;
            int i1 = 0;

            label37:
            while (i1 < i) {
                char c0 = charsequence.charAt(i1);

                for (int j1 = 0; j1 < k; ++j1) {
                    if (achar[j1] == c0 && (i1 >= j || j1 >= l || !Character.isHighSurrogate(c0) || achar[j1 + 1] == charsequence.charAt(i1 + 1))) {
                        ++i1;
                        continue label37;
                    }
                }

                return i1;
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static int indexOfAnyBut(CharSequence charsequence, CharSequence charsequence1) {
        if (!isEmpty(charsequence) && !isEmpty(charsequence1)) {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                char c0 = charsequence.charAt(j);
                boolean flag = CharSequenceUtils.indexOf(charsequence1, c0, 0) >= 0;

                if (j + 1 < i && Character.isHighSurrogate(c0)) {
                    char c1 = charsequence.charAt(j + 1);

                    if (flag && CharSequenceUtils.indexOf(charsequence1, c1, 0) < 0) {
                        return j;
                    }
                } else if (!flag) {
                    return j;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static boolean containsOnly(CharSequence charsequence, char... achar) {
        return achar != null && charsequence != null ? (charsequence.length() == 0 ? true : (achar.length == 0 ? false : indexOfAnyBut(charsequence, achar) == -1)) : false;
    }

    public static boolean containsOnly(CharSequence charsequence, String s) {
        return charsequence != null && s != null ? containsOnly(charsequence, s.toCharArray()) : false;
    }

    public static boolean containsNone(CharSequence charsequence, char... achar) {
        if (charsequence != null && achar != null) {
            int i = charsequence.length();
            int j = i - 1;
            int k = achar.length;
            int l = k - 1;

            for (int i1 = 0; i1 < i; ++i1) {
                char c0 = charsequence.charAt(i1);

                for (int j1 = 0; j1 < k; ++j1) {
                    if (achar[j1] == c0) {
                        if (!Character.isHighSurrogate(c0)) {
                            return false;
                        }

                        if (j1 == l) {
                            return false;
                        }

                        if (i1 < j && achar[j1 + 1] == charsequence.charAt(i1 + 1)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean containsNone(CharSequence charsequence, String s) {
        return charsequence != null && s != null ? containsNone(charsequence, s.toCharArray()) : true;
    }

    public static int indexOfAny(CharSequence charsequence, CharSequence... acharsequence) {
        if (charsequence != null && acharsequence != null) {
            int i = acharsequence.length;
            int j = Integer.MAX_VALUE;
            boolean flag = false;

            for (int k = 0; k < i; ++k) {
                CharSequence charsequence1 = acharsequence[k];

                if (charsequence1 != null) {
                    int l = CharSequenceUtils.indexOf(charsequence, charsequence1, 0);

                    if (l != -1 && l < j) {
                        j = l;
                    }
                }
            }

            return j == Integer.MAX_VALUE ? -1 : j;
        } else {
            return -1;
        }
    }

    public static int lastIndexOfAny(CharSequence charsequence, CharSequence... acharsequence) {
        if (charsequence != null && acharsequence != null) {
            int i = acharsequence.length;
            int j = -1;
            boolean flag = false;

            for (int k = 0; k < i; ++k) {
                CharSequence charsequence1 = acharsequence[k];

                if (charsequence1 != null) {
                    int l = CharSequenceUtils.lastIndexOf(charsequence, charsequence1, charsequence.length());

                    if (l > j) {
                        j = l;
                    }
                }
            }

            return j;
        } else {
            return -1;
        }
    }

    public static String substring(String s, int i) {
        if (s == null) {
            return null;
        } else {
            if (i < 0) {
                i += s.length();
            }

            if (i < 0) {
                i = 0;
            }

            return i > s.length() ? "" : s.substring(i);
        }
    }

    public static String substring(String s, int i, int j) {
        if (s == null) {
            return null;
        } else {
            if (j < 0) {
                j += s.length();
            }

            if (i < 0) {
                i += s.length();
            }

            if (j > s.length()) {
                j = s.length();
            }

            if (i > j) {
                return "";
            } else {
                if (i < 0) {
                    i = 0;
                }

                if (j < 0) {
                    j = 0;
                }

                return s.substring(i, j);
            }
        }
    }

    public static String left(String s, int i) {
        return s == null ? null : (i < 0 ? "" : (s.length() <= i ? s : s.substring(0, i)));
    }

    public static String right(String s, int i) {
        return s == null ? null : (i < 0 ? "" : (s.length() <= i ? s : s.substring(s.length() - i)));
    }

    public static String mid(String s, int i, int j) {
        if (s == null) {
            return null;
        } else if (j >= 0 && i <= s.length()) {
            if (i < 0) {
                i = 0;
            }

            return s.length() <= i + j ? s.substring(i) : s.substring(i, i + j);
        } else {
            return "";
        }
    }

    public static String substringBefore(String s, String s1) {
        if (!isEmpty(s) && s1 != null) {
            if (s1.isEmpty()) {
                return "";
            } else {
                int i = s.indexOf(s1);

                return i == -1 ? s : s.substring(0, i);
            }
        } else {
            return s;
        }
    }

    public static String substringAfter(String s, String s1) {
        if (isEmpty(s)) {
            return s;
        } else if (s1 == null) {
            return "";
        } else {
            int i = s.indexOf(s1);

            return i == -1 ? "" : s.substring(i + s1.length());
        }
    }

    public static String substringBeforeLast(String s, String s1) {
        if (!isEmpty(s) && !isEmpty(s1)) {
            int i = s.lastIndexOf(s1);

            return i == -1 ? s : s.substring(0, i);
        } else {
            return s;
        }
    }

    public static String substringAfterLast(String s, String s1) {
        if (isEmpty(s)) {
            return s;
        } else if (isEmpty(s1)) {
            return "";
        } else {
            int i = s.lastIndexOf(s1);

            return i != -1 && i != s.length() - s1.length() ? s.substring(i + s1.length()) : "";
        }
    }

    public static String substringBetween(String s, String s1) {
        return substringBetween(s, s1, s1);
    }

    public static String substringBetween(String s, String s1, String s2) {
        if (s != null && s1 != null && s2 != null) {
            int i = s.indexOf(s1);

            if (i != -1) {
                int j = s.indexOf(s2, i + s1.length());

                if (j != -1) {
                    return s.substring(i + s1.length(), j);
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static String[] substringsBetween(String s, String s1, String s2) {
        if (s != null && !isEmpty(s1) && !isEmpty(s2)) {
            int i = s.length();

            if (i == 0) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            } else {
                int j = s2.length();
                int k = s1.length();
                ArrayList arraylist = new ArrayList();

                int l;

                for (int i1 = 0; i1 < i - j; i1 = l + j) {
                    int j1 = s.indexOf(s1, i1);

                    if (j1 < 0) {
                        break;
                    }

                    j1 += k;
                    l = s.indexOf(s2, j1);
                    if (l < 0) {
                        break;
                    }

                    arraylist.add(s.substring(j1, l));
                }

                return arraylist.isEmpty() ? null : (String[]) arraylist.toArray(new String[arraylist.size()]);
            }
        } else {
            return null;
        }
    }

    public static String[] split(String s) {
        return split(s, (String) null, -1);
    }

    public static String[] split(String s, char c0) {
        return splitWorker(s, c0, false);
    }

    public static String[] split(String s, String s1) {
        return splitWorker(s, s1, -1, false);
    }

    public static String[] split(String s, String s1, int i) {
        return splitWorker(s, s1, i, false);
    }

    public static String[] splitByWholeSeparator(String s, String s1) {
        return splitByWholeSeparatorWorker(s, s1, -1, false);
    }

    public static String[] splitByWholeSeparator(String s, String s1, int i) {
        return splitByWholeSeparatorWorker(s, s1, i, false);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String s, String s1) {
        return splitByWholeSeparatorWorker(s, s1, -1, true);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String s, String s1, int i) {
        return splitByWholeSeparatorWorker(s, s1, i, true);
    }

    private static String[] splitByWholeSeparatorWorker(String s, String s1, int i, boolean flag) {
        if (s == null) {
            return null;
        } else {
            int j = s.length();

            if (j == 0) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            } else if (s1 != null && !"".equals(s1)) {
                int k = s1.length();
                ArrayList arraylist = new ArrayList();
                int l = 0;
                int i1 = 0;
                int j1 = 0;

                while (j1 < j) {
                    j1 = s.indexOf(s1, i1);
                    if (j1 > -1) {
                        if (j1 > i1) {
                            ++l;
                            if (l == i) {
                                j1 = j;
                                arraylist.add(s.substring(i1));
                            } else {
                                arraylist.add(s.substring(i1, j1));
                                i1 = j1 + k;
                            }
                        } else {
                            if (flag) {
                                ++l;
                                if (l == i) {
                                    j1 = j;
                                    arraylist.add(s.substring(i1));
                                } else {
                                    arraylist.add("");
                                }
                            }

                            i1 = j1 + k;
                        }
                    } else {
                        arraylist.add(s.substring(i1));
                        j1 = j;
                    }
                }

                return (String[]) arraylist.toArray(new String[arraylist.size()]);
            } else {
                return splitWorker(s, (String) null, i, flag);
            }
        }
    }

    public static String[] splitPreserveAllTokens(String s) {
        return splitWorker(s, (String) null, -1, true);
    }

    public static String[] splitPreserveAllTokens(String s, char c0) {
        return splitWorker(s, c0, true);
    }

    private static String[] splitWorker(String s, char c0, boolean flag) {
        if (s == null) {
            return null;
        } else {
            int i = s.length();

            if (i == 0) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            } else {
                ArrayList arraylist = new ArrayList();
                int j = 0;
                int k = 0;
                boolean flag1 = false;
                boolean flag2 = false;

                while (j < i) {
                    if (s.charAt(j) == c0) {
                        if (flag1 || flag) {
                            arraylist.add(s.substring(k, j));
                            flag1 = false;
                            flag2 = true;
                        }

                        ++j;
                        k = j;
                    } else {
                        flag2 = false;
                        flag1 = true;
                        ++j;
                    }
                }

                if (flag1 || flag && flag2) {
                    arraylist.add(s.substring(k, j));
                }

                return (String[]) arraylist.toArray(new String[arraylist.size()]);
            }
        }
    }

    public static String[] splitPreserveAllTokens(String s, String s1) {
        return splitWorker(s, s1, -1, true);
    }

    public static String[] splitPreserveAllTokens(String s, String s1, int i) {
        return splitWorker(s, s1, i, true);
    }

    private static String[] splitWorker(String s, String s1, int i, boolean flag) {
        if (s == null) {
            return null;
        } else {
            int j = s.length();

            if (j == 0) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            } else {
                ArrayList arraylist = new ArrayList();
                int k = 1;
                int l = 0;
                int i1 = 0;
                boolean flag1 = false;
                boolean flag2 = false;

                if (s1 == null) {
                    while (l < j) {
                        if (!Character.isWhitespace(s.charAt(l))) {
                            flag2 = false;
                            flag1 = true;
                            ++l;
                        } else {
                            if (flag1 || flag) {
                                flag2 = true;
                                if (k++ == i) {
                                    l = j;
                                    flag2 = false;
                                }

                                arraylist.add(s.substring(i1, l));
                                flag1 = false;
                            }

                            ++l;
                            i1 = l;
                        }
                    }
                } else if (s1.length() == 1) {
                    char c0 = s1.charAt(0);

                    while (l < j) {
                        if (s.charAt(l) != c0) {
                            flag2 = false;
                            flag1 = true;
                            ++l;
                        } else {
                            if (flag1 || flag) {
                                flag2 = true;
                                if (k++ == i) {
                                    l = j;
                                    flag2 = false;
                                }

                                arraylist.add(s.substring(i1, l));
                                flag1 = false;
                            }

                            ++l;
                            i1 = l;
                        }
                    }
                } else {
                    while (l < j) {
                        if (s1.indexOf(s.charAt(l)) < 0) {
                            flag2 = false;
                            flag1 = true;
                            ++l;
                        } else {
                            if (flag1 || flag) {
                                flag2 = true;
                                if (k++ == i) {
                                    l = j;
                                    flag2 = false;
                                }

                                arraylist.add(s.substring(i1, l));
                                flag1 = false;
                            }

                            ++l;
                            i1 = l;
                        }
                    }
                }

                if (flag1 || flag && flag2) {
                    arraylist.add(s.substring(i1, l));
                }

                return (String[]) arraylist.toArray(new String[arraylist.size()]);
            }
        }
    }

    public static String[] splitByCharacterType(String s) {
        return splitByCharacterType(s, false);
    }

    public static String[] splitByCharacterTypeCamelCase(String s) {
        return splitByCharacterType(s, true);
    }

    private static String[] splitByCharacterType(String s, boolean flag) {
        if (s == null) {
            return null;
        } else if (s.isEmpty()) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        } else {
            char[] achar = s.toCharArray();
            ArrayList arraylist = new ArrayList();
            int i = 0;
            int j = Character.getType(achar[i]);

            for (int k = i + 1; k < achar.length; ++k) {
                int l = Character.getType(achar[k]);

                if (l != j) {
                    if (flag && l == 2 && j == 1) {
                        int i1 = k - 1;

                        if (i1 != i) {
                            arraylist.add(new String(achar, i, i1 - i));
                            i = i1;
                        }
                    } else {
                        arraylist.add(new String(achar, i, k - i));
                        i = k;
                    }

                    j = l;
                }
            }

            arraylist.add(new String(achar, i, achar.length - i));
            return (String[]) arraylist.toArray(new String[arraylist.size()]);
        }
    }

    public static String join(Object... aobject) {
        return join(aobject, (String) null);
    }

    public static String join(Object[] aobject, char c0) {
        return aobject == null ? null : join(aobject, c0, 0, aobject.length);
    }

    public static String join(long[] along, char c0) {
        return along == null ? null : join(along, c0, 0, along.length);
    }

    public static String join(int[] aint, char c0) {
        return aint == null ? null : join(aint, c0, 0, aint.length);
    }

    public static String join(short[] ashort, char c0) {
        return ashort == null ? null : join(ashort, c0, 0, ashort.length);
    }

    public static String join(byte[] abyte, char c0) {
        return abyte == null ? null : join(abyte, c0, 0, abyte.length);
    }

    public static String join(char[] achar, char c0) {
        return achar == null ? null : join(achar, c0, 0, achar.length);
    }

    public static String join(float[] afloat, char c0) {
        return afloat == null ? null : join(afloat, c0, 0, afloat.length);
    }

    public static String join(double[] adouble, char c0) {
        return adouble == null ? null : join(adouble, c0, 0, adouble.length);
    }

    public static String join(Object[] aobject, char c0, int i, int j) {
        if (aobject == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    if (aobject[l] != null) {
                        stringbuilder.append(aobject[l]);
                    }
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(long[] along, char c0, int i, int j) {
        if (along == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    stringbuilder.append(along[l]);
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(int[] aint, char c0, int i, int j) {
        if (aint == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    stringbuilder.append(aint[l]);
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(byte[] abyte, char c0, int i, int j) {
        if (abyte == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    stringbuilder.append(abyte[l]);
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(short[] ashort, char c0, int i, int j) {
        if (ashort == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    stringbuilder.append(ashort[l]);
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(char[] achar, char c0, int i, int j) {
        if (achar == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    stringbuilder.append(achar[l]);
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(double[] adouble, char c0, int i, int j) {
        if (adouble == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    stringbuilder.append(adouble[l]);
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(float[] afloat, char c0, int i, int j) {
        if (afloat == null) {
            return null;
        } else {
            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(c0);
                    }

                    stringbuilder.append(afloat[l]);
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(Object[] aobject, String s) {
        return aobject == null ? null : join(aobject, s, 0, aobject.length);
    }

    public static String join(Object[] aobject, String s, int i, int j) {
        if (aobject == null) {
            return null;
        } else {
            if (s == null) {
                s = "";
            }

            int k = j - i;

            if (k <= 0) {
                return "";
            } else {
                StringBuilder stringbuilder = new StringBuilder(k * 16);

                for (int l = i; l < j; ++l) {
                    if (l > i) {
                        stringbuilder.append(s);
                    }

                    if (aobject[l] != null) {
                        stringbuilder.append(aobject[l]);
                    }
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(Iterator iterator, char c0) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object object = iterator.next();

            if (!iterator.hasNext()) {
                String s = ObjectUtils.toString(object);

                return s;
            } else {
                StringBuilder stringbuilder = new StringBuilder(256);

                if (object != null) {
                    stringbuilder.append(object);
                }

                while (iterator.hasNext()) {
                    stringbuilder.append(c0);
                    Object object1 = iterator.next();

                    if (object1 != null) {
                        stringbuilder.append(object1);
                    }
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(Iterator iterator, String s) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object object = iterator.next();

            if (!iterator.hasNext()) {
                String s1 = ObjectUtils.toString(object);

                return s1;
            } else {
                StringBuilder stringbuilder = new StringBuilder(256);

                if (object != null) {
                    stringbuilder.append(object);
                }

                while (iterator.hasNext()) {
                    if (s != null) {
                        stringbuilder.append(s);
                    }

                    Object object1 = iterator.next();

                    if (object1 != null) {
                        stringbuilder.append(object1);
                    }
                }

                return stringbuilder.toString();
            }
        }
    }

    public static String join(Iterable iterable, char c0) {
        return iterable == null ? null : join(iterable.iterator(), c0);
    }

    public static String join(Iterable iterable, String s) {
        return iterable == null ? null : join(iterable.iterator(), s);
    }

    public static String deleteWhitespace(String s) {
        if (isEmpty(s)) {
            return s;
        } else {
            int i = s.length();
            char[] achar = new char[i];
            int j = 0;

            for (int k = 0; k < i; ++k) {
                if (!Character.isWhitespace(s.charAt(k))) {
                    achar[j++] = s.charAt(k);
                }
            }

            if (j == i) {
                return s;
            } else {
                return new String(achar, 0, j);
            }
        }
    }

    public static String removeStart(String s, String s1) {
        return !isEmpty(s) && !isEmpty(s1) ? (s.startsWith(s1) ? s.substring(s1.length()) : s) : s;
    }

    public static String removeStartIgnoreCase(String s, String s1) {
        return !isEmpty(s) && !isEmpty(s1) ? (startsWithIgnoreCase(s, s1) ? s.substring(s1.length()) : s) : s;
    }

    public static String removeEnd(String s, String s1) {
        return !isEmpty(s) && !isEmpty(s1) ? (s.endsWith(s1) ? s.substring(0, s.length() - s1.length()) : s) : s;
    }

    public static String removeEndIgnoreCase(String s, String s1) {
        return !isEmpty(s) && !isEmpty(s1) ? (endsWithIgnoreCase(s, s1) ? s.substring(0, s.length() - s1.length()) : s) : s;
    }

    public static String remove(String s, String s1) {
        return !isEmpty(s) && !isEmpty(s1) ? replace(s, s1, "", -1) : s;
    }

    public static String remove(String s, char c0) {
        if (!isEmpty(s) && s.indexOf(c0) != -1) {
            char[] achar = s.toCharArray();
            int i = 0;

            for (int j = 0; j < achar.length; ++j) {
                if (achar[j] != c0) {
                    achar[i++] = achar[j];
                }
            }

            return new String(achar, 0, i);
        } else {
            return s;
        }
    }

    public static String replaceOnce(String s, String s1, String s2) {
        return replace(s, s1, s2, 1);
    }

    public static String replacePattern(String s, String s1, String s2) {
        return Pattern.compile(s1, 32).matcher(s).replaceAll(s2);
    }

    public static String removePattern(String s, String s1) {
        return replacePattern(s, s1, "");
    }

    public static String replace(String s, String s1, String s2) {
        return replace(s, s1, s2, -1);
    }

    public static String replace(String s, String s1, String s2, int i) {
        if (!isEmpty(s) && !isEmpty(s1) && s2 != null && i != 0) {
            int j = 0;
            int k = s.indexOf(s1, j);

            if (k == -1) {
                return s;
            } else {
                int l = s1.length();
                int i1 = s2.length() - l;

                i1 = i1 < 0 ? 0 : i1;
                i1 *= i < 0 ? 16 : (i > 64 ? 64 : i);

                StringBuilder stringbuilder;

                for (stringbuilder = new StringBuilder(s.length() + i1); k != -1; k = s.indexOf(s1, j)) {
                    stringbuilder.append(s.substring(j, k)).append(s2);
                    j = k + l;
                    --i;
                    if (i == 0) {
                        break;
                    }
                }

                stringbuilder.append(s.substring(j));
                return stringbuilder.toString();
            }
        } else {
            return s;
        }
    }

    public static String replaceEach(String s, String[] astring, String[] astring1) {
        return replaceEach(s, astring, astring1, false, 0);
    }

    public static String replaceEachRepeatedly(String s, String[] astring, String[] astring1) {
        int i = astring == null ? 0 : astring.length;

        return replaceEach(s, astring, astring1, true, i);
    }

    private static String replaceEach(String s, String[] astring, String[] astring1, boolean flag, int i) {
        if (s != null && !s.isEmpty() && astring != null && astring.length != 0 && astring1 != null && astring1.length != 0) {
            if (i < 0) {
                throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
            } else {
                int j = astring.length;
                int k = astring1.length;

                if (j != k) {
                    throw new IllegalArgumentException("Search and Replace array lengths don\'t match: " + j + " vs " + k);
                } else {
                    boolean[] aboolean = new boolean[j];
                    int l = -1;
                    int i1 = -1;
                    boolean flag1 = true;

                    int j1;
                    int k1;

                    for (j1 = 0; j1 < j; ++j1) {
                        if (!aboolean[j1] && astring[j1] != null && !astring[j1].isEmpty() && astring1[j1] != null) {
                            k1 = s.indexOf(astring[j1]);
                            if (k1 == -1) {
                                aboolean[j1] = true;
                            } else if (l == -1 || k1 < l) {
                                l = k1;
                                i1 = j1;
                            }
                        }
                    }

                    if (l == -1) {
                        return s;
                    } else {
                        j1 = 0;
                        int l1 = 0;

                        int i2;

                        for (int j2 = 0; j2 < astring.length; ++j2) {
                            if (astring[j2] != null && astring1[j2] != null) {
                                i2 = astring1[j2].length() - astring[j2].length();
                                if (i2 > 0) {
                                    l1 += 3 * i2;
                                }
                            }
                        }

                        l1 = Math.min(l1, s.length() / 5);
                        StringBuilder stringbuilder = new StringBuilder(s.length() + l1);

                        while (l != -1) {
                            for (i2 = j1; i2 < l; ++i2) {
                                stringbuilder.append(s.charAt(i2));
                            }

                            stringbuilder.append(astring1[i1]);
                            j1 = l + astring[i1].length();
                            l = -1;
                            i1 = -1;
                            flag1 = true;

                            for (i2 = 0; i2 < j; ++i2) {
                                if (!aboolean[i2] && astring[i2] != null && !astring[i2].isEmpty() && astring1[i2] != null) {
                                    k1 = s.indexOf(astring[i2], j1);
                                    if (k1 == -1) {
                                        aboolean[i2] = true;
                                    } else if (l == -1 || k1 < l) {
                                        l = k1;
                                        i1 = i2;
                                    }
                                }
                            }
                        }

                        i2 = s.length();

                        for (int k2 = j1; k2 < i2; ++k2) {
                            stringbuilder.append(s.charAt(k2));
                        }

                        String s1 = stringbuilder.toString();

                        if (!flag) {
                            return s1;
                        } else {
                            return replaceEach(s1, astring, astring1, flag, i - 1);
                        }
                    }
                }
            }
        } else {
            return s;
        }
    }

    public static String replaceChars(String s, char c0, char c1) {
        return s == null ? null : s.replace(c0, c1);
    }

    public static String replaceChars(String s, String s1, String s2) {
        if (!isEmpty(s) && !isEmpty(s1)) {
            if (s2 == null) {
                s2 = "";
            }

            boolean flag = false;
            int i = s2.length();
            int j = s.length();
            StringBuilder stringbuilder = new StringBuilder(j);

            for (int k = 0; k < j; ++k) {
                char c0 = s.charAt(k);
                int l = s1.indexOf(c0);

                if (l >= 0) {
                    flag = true;
                    if (l < i) {
                        stringbuilder.append(s2.charAt(l));
                    }
                } else {
                    stringbuilder.append(c0);
                }
            }

            if (flag) {
                return stringbuilder.toString();
            } else {
                return s;
            }
        } else {
            return s;
        }
    }

    public static String overlay(String s, String s1, int i, int j) {
        if (s == null) {
            return null;
        } else {
            if (s1 == null) {
                s1 = "";
            }

            int k = s.length();

            if (i < 0) {
                i = 0;
            }

            if (i > k) {
                i = k;
            }

            if (j < 0) {
                j = 0;
            }

            if (j > k) {
                j = k;
            }

            if (i > j) {
                int l = i;

                i = j;
                j = l;
            }

            return (new StringBuilder(k + i - j + s1.length() + 1)).append(s.substring(0, i)).append(s1).append(s.substring(j)).toString();
        }
    }

    public static String chomp(String s) {
        if (isEmpty(s)) {
            return s;
        } else if (s.length() == 1) {
            char c0 = s.charAt(0);

            return c0 != 13 && c0 != 10 ? s : "";
        } else {
            int i = s.length() - 1;
            char c1 = s.charAt(i);

            if (c1 == 10) {
                if (s.charAt(i - 1) == 13) {
                    --i;
                }
            } else if (c1 != 13) {
                ++i;
            }

            return s.substring(0, i);
        }
    }

    /** @deprecated */
    @Deprecated
    public static String chomp(String s, String s1) {
        return removeEnd(s, s1);
    }

    public static String chop(String s) {
        if (s == null) {
            return null;
        } else {
            int i = s.length();

            if (i < 2) {
                return "";
            } else {
                int j = i - 1;
                String s1 = s.substring(0, j);
                char c0 = s.charAt(j);

                return c0 == 10 && s1.charAt(j - 1) == 13 ? s1.substring(0, j - 1) : s1;
            }
        }
    }

    public static String repeat(String s, int i) {
        if (s == null) {
            return null;
        } else if (i <= 0) {
            return "";
        } else {
            int j = s.length();

            if (i != 1 && j != 0) {
                if (j == 1 && i <= 8192) {
                    return repeat(s.charAt(0), i);
                } else {
                    int k = j * i;

                    switch (j) {
                    case 1:
                        return repeat(s.charAt(0), i);

                    case 2:
                        char c0 = s.charAt(0);
                        char c1 = s.charAt(1);
                        char[] achar = new char[k];

                        for (int l = i * 2 - 2; l >= 0; --l) {
                            achar[l] = c0;
                            achar[l + 1] = c1;
                            --l;
                        }

                        return new String(achar);

                    default:
                        StringBuilder stringbuilder = new StringBuilder(k);

                        for (int i1 = 0; i1 < i; ++i1) {
                            stringbuilder.append(s);
                        }

                        return stringbuilder.toString();
                    }
                }
            } else {
                return s;
            }
        }
    }

    public static String repeat(String s, String s1, int i) {
        if (s != null && s1 != null) {
            String s2 = repeat(s + s1, i);

            return removeEnd(s2, s1);
        } else {
            return repeat(s, i);
        }
    }

    public static String repeat(char c0, int i) {
        char[] achar = new char[i];

        for (int j = i - 1; j >= 0; --j) {
            achar[j] = c0;
        }

        return new String(achar);
    }

    public static String rightPad(String s, int i) {
        return rightPad(s, i, ' ');
    }

    public static String rightPad(String s, int i, char c0) {
        if (s == null) {
            return null;
        } else {
            int j = i - s.length();

            return j <= 0 ? s : (j > 8192 ? rightPad(s, i, String.valueOf(c0)) : s.concat(repeat(c0, j)));
        }
    }

    public static String rightPad(String s, int i, String s1) {
        if (s == null) {
            return null;
        } else {
            if (isEmpty(s1)) {
                s1 = " ";
            }

            int j = s1.length();
            int k = s.length();
            int l = i - k;

            if (l <= 0) {
                return s;
            } else if (j == 1 && l <= 8192) {
                return rightPad(s, i, s1.charAt(0));
            } else if (l == j) {
                return s.concat(s1);
            } else if (l < j) {
                return s.concat(s1.substring(0, l));
            } else {
                char[] achar = new char[l];
                char[] achar1 = s1.toCharArray();

                for (int i1 = 0; i1 < l; ++i1) {
                    achar[i1] = achar1[i1 % j];
                }

                return s.concat(new String(achar));
            }
        }
    }

    public static String leftPad(String s, int i) {
        return leftPad(s, i, ' ');
    }

    public static String leftPad(String s, int i, char c0) {
        if (s == null) {
            return null;
        } else {
            int j = i - s.length();

            return j <= 0 ? s : (j > 8192 ? leftPad(s, i, String.valueOf(c0)) : repeat(c0, j).concat(s));
        }
    }

    public static String leftPad(String s, int i, String s1) {
        if (s == null) {
            return null;
        } else {
            if (isEmpty(s1)) {
                s1 = " ";
            }

            int j = s1.length();
            int k = s.length();
            int l = i - k;

            if (l <= 0) {
                return s;
            } else if (j == 1 && l <= 8192) {
                return leftPad(s, i, s1.charAt(0));
            } else if (l == j) {
                return s1.concat(s);
            } else if (l < j) {
                return s1.substring(0, l).concat(s);
            } else {
                char[] achar = new char[l];
                char[] achar1 = s1.toCharArray();

                for (int i1 = 0; i1 < l; ++i1) {
                    achar[i1] = achar1[i1 % j];
                }

                return (new String(achar)).concat(s);
            }
        }
    }

    public static int length(CharSequence charsequence) {
        return charsequence == null ? 0 : charsequence.length();
    }

    public static String center(String s, int i) {
        return center(s, i, ' ');
    }

    public static String center(String s, int i, char c0) {
        if (s != null && i > 0) {
            int j = s.length();
            int k = i - j;

            if (k <= 0) {
                return s;
            } else {
                s = leftPad(s, j + k / 2, c0);
                s = rightPad(s, i, c0);
                return s;
            }
        } else {
            return s;
        }
    }

    public static String center(String s, int i, String s1) {
        if (s != null && i > 0) {
            if (isEmpty(s1)) {
                s1 = " ";
            }

            int j = s.length();
            int k = i - j;

            if (k <= 0) {
                return s;
            } else {
                s = leftPad(s, j + k / 2, s1);
                s = rightPad(s, i, s1);
                return s;
            }
        } else {
            return s;
        }
    }

    public static String upperCase(String s) {
        return s == null ? null : s.toUpperCase();
    }

    public static String upperCase(String s, Locale locale) {
        return s == null ? null : s.toUpperCase(locale);
    }

    public static String lowerCase(String s) {
        return s == null ? null : s.toLowerCase();
    }

    public static String lowerCase(String s, Locale locale) {
        return s == null ? null : s.toLowerCase(locale);
    }

    public static String capitalize(String s) {
        int i;

        if (s != null && (i = s.length()) != 0) {
            char c0 = s.charAt(0);

            return Character.isTitleCase(c0) ? s : (new StringBuilder(i)).append(Character.toTitleCase(c0)).append(s.substring(1)).toString();
        } else {
            return s;
        }
    }

    public static String uncapitalize(String s) {
        int i;

        if (s != null && (i = s.length()) != 0) {
            char c0 = s.charAt(0);

            return Character.isLowerCase(c0) ? s : (new StringBuilder(i)).append(Character.toLowerCase(c0)).append(s.substring(1)).toString();
        } else {
            return s;
        }
    }

    public static String swapCase(String s) {
        if (isEmpty(s)) {
            return s;
        } else {
            char[] achar = s.toCharArray();

            for (int i = 0; i < achar.length; ++i) {
                char c0 = achar[i];

                if (Character.isUpperCase(c0)) {
                    achar[i] = Character.toLowerCase(c0);
                } else if (Character.isTitleCase(c0)) {
                    achar[i] = Character.toLowerCase(c0);
                } else if (Character.isLowerCase(c0)) {
                    achar[i] = Character.toUpperCase(c0);
                }
            }

            return new String(achar);
        }
    }

    public static int countMatches(CharSequence charsequence, CharSequence charsequence1) {
        if (!isEmpty(charsequence) && !isEmpty(charsequence1)) {
            int i = 0;

            for (int j = 0; (j = CharSequenceUtils.indexOf(charsequence, charsequence1, j)) != -1; j += charsequence1.length()) {
                ++i;
            }

            return i;
        } else {
            return 0;
        }
    }

    public static boolean isAlpha(CharSequence charsequence) {
        if (isEmpty(charsequence)) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isLetter(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAlphaSpace(CharSequence charsequence) {
        if (charsequence == null) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isLetter(charsequence.charAt(j)) && charsequence.charAt(j) != 32) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAlphanumeric(CharSequence charsequence) {
        if (isEmpty(charsequence)) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isLetterOrDigit(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAlphanumericSpace(CharSequence charsequence) {
        if (charsequence == null) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isLetterOrDigit(charsequence.charAt(j)) && charsequence.charAt(j) != 32) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAsciiPrintable(CharSequence charsequence) {
        if (charsequence == null) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!CharUtils.isAsciiPrintable(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNumeric(CharSequence charsequence) {
        if (isEmpty(charsequence)) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isDigit(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNumericSpace(CharSequence charsequence) {
        if (charsequence == null) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isDigit(charsequence.charAt(j)) && charsequence.charAt(j) != 32) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isWhitespace(CharSequence charsequence) {
        if (charsequence == null) {
            return false;
        } else {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isWhitespace(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAllLowerCase(CharSequence charsequence) {
        if (charsequence != null && !isEmpty(charsequence)) {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isLowerCase(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isAllUpperCase(CharSequence charsequence) {
        if (charsequence != null && !isEmpty(charsequence)) {
            int i = charsequence.length();

            for (int j = 0; j < i; ++j) {
                if (!Character.isUpperCase(charsequence.charAt(j))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static String defaultString(String s) {
        return s == null ? "" : s;
    }

    public static String defaultString(String s, String s1) {
        return s == null ? s1 : s;
    }

    public static CharSequence defaultIfBlank(CharSequence charsequence, CharSequence charsequence1) {
        return isBlank(charsequence) ? charsequence1 : charsequence;
    }

    public static CharSequence defaultIfEmpty(CharSequence charsequence, CharSequence charsequence1) {
        return isEmpty(charsequence) ? charsequence1 : charsequence;
    }

    public static String reverse(String s) {
        return s == null ? null : (new StringBuilder(s)).reverse().toString();
    }

    public static String reverseDelimited(String s, char c0) {
        if (s == null) {
            return null;
        } else {
            String[] astring = split(s, c0);

            ArrayUtils.reverse((Object[]) astring);
            return join((Object[]) astring, c0);
        }
    }

    public static String abbreviate(String s, int i) {
        return abbreviate(s, 0, i);
    }

    public static String abbreviate(String s, int i, int j) {
        if (s == null) {
            return null;
        } else if (j < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        } else if (s.length() <= j) {
            return s;
        } else {
            if (i > s.length()) {
                i = s.length();
            }

            if (s.length() - i < j - 3) {
                i = s.length() - (j - 3);
            }

            String s1 = "...";

            if (i <= 4) {
                return s.substring(0, j - 3) + "...";
            } else if (j < 7) {
                throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
            } else {
                return i + j - 3 < s.length() ? "..." + abbreviate(s.substring(i), j - 3) : "..." + s.substring(s.length() - (j - 3));
            }
        }
    }

    public static String abbreviateMiddle(String s, String s1, int i) {
        if (!isEmpty(s) && !isEmpty(s1)) {
            if (i < s.length() && i >= s1.length() + 2) {
                int j = i - s1.length();
                int k = j / 2 + j % 2;
                int l = s.length() - j / 2;
                StringBuilder stringbuilder = new StringBuilder(i);

                stringbuilder.append(s.substring(0, k));
                stringbuilder.append(s1);
                stringbuilder.append(s.substring(l));
                return stringbuilder.toString();
            } else {
                return s;
            }
        } else {
            return s;
        }
    }

    public static String difference(String s, String s1) {
        if (s == null) {
            return s1;
        } else if (s1 == null) {
            return s;
        } else {
            int i = indexOfDifference(s, s1);

            return i == -1 ? "" : s1.substring(i);
        }
    }

    public static int indexOfDifference(CharSequence charsequence, CharSequence charsequence1) {
        if (charsequence == charsequence1) {
            return -1;
        } else if (charsequence != null && charsequence1 != null) {
            int i;

            for (i = 0; i < charsequence.length() && i < charsequence1.length() && charsequence.charAt(i) == charsequence1.charAt(i); ++i) {
                ;
            }

            return i >= charsequence1.length() && i >= charsequence.length() ? -1 : i;
        } else {
            return 0;
        }
    }

    public static int indexOfDifference(CharSequence... acharsequence) {
        if (acharsequence != null && acharsequence.length > 1) {
            boolean flag = false;
            boolean flag1 = true;
            int i = acharsequence.length;
            int j = Integer.MAX_VALUE;
            int k = 0;

            int l;

            for (l = 0; l < i; ++l) {
                if (acharsequence[l] == null) {
                    flag = true;
                    j = 0;
                } else {
                    flag1 = false;
                    j = Math.min(acharsequence[l].length(), j);
                    k = Math.max(acharsequence[l].length(), k);
                }
            }

            if (!flag1 && (k != 0 || flag)) {
                if (j == 0) {
                    return 0;
                } else {
                    l = -1;

                    for (int i1 = 0; i1 < j; ++i1) {
                        char c0 = acharsequence[0].charAt(i1);

                        for (int j1 = 1; j1 < i; ++j1) {
                            if (acharsequence[j1].charAt(i1) != c0) {
                                l = i1;
                                break;
                            }
                        }

                        if (l != -1) {
                            break;
                        }
                    }

                    return l == -1 && j != k ? j : l;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public static String getCommonPrefix(String... astring) {
        if (astring != null && astring.length != 0) {
            int i = indexOfDifference(astring);

            return i == -1 ? (astring[0] == null ? "" : astring[0]) : (i == 0 ? "" : astring[0].substring(0, i));
        } else {
            return "";
        }
    }

    public static int getLevenshteinDistance(CharSequence charsequence, CharSequence charsequence1) {
        if (charsequence != null && charsequence1 != null) {
            int i = charsequence.length();
            int j = charsequence1.length();

            if (i == 0) {
                return j;
            } else if (j == 0) {
                return i;
            } else {
                if (i > j) {
                    CharSequence charsequence2 = charsequence;

                    charsequence = charsequence1;
                    charsequence1 = charsequence2;
                    i = j;
                    j = charsequence2.length();
                }

                int[] aint = new int[i + 1];
                int[] aint1 = new int[i + 1];

                int k;

                for (k = 0; k <= i; aint[k] = k++) {
                    ;
                }

                for (int l = 1; l <= j; ++l) {
                    char c0 = charsequence1.charAt(l - 1);

                    aint1[0] = l;

                    for (k = 1; k <= i; ++k) {
                        int i1 = charsequence.charAt(k - 1) == c0 ? 0 : 1;

                        aint1[k] = Math.min(Math.min(aint1[k - 1] + 1, aint[k] + 1), aint[k - 1] + i1);
                    }

                    int[] aint2 = aint;

                    aint = aint1;
                    aint1 = aint2;
                }

                return aint[i];
            }
        } else {
            throw new IllegalArgumentException("Strings must not be null");
        }
    }

    public static int getLevenshteinDistance(CharSequence charsequence, CharSequence charsequence1, int i) {
        if (charsequence != null && charsequence1 != null) {
            if (i < 0) {
                throw new IllegalArgumentException("Threshold must not be negative");
            } else {
                int j = charsequence.length();
                int k = charsequence1.length();

                if (j == 0) {
                    return k <= i ? k : -1;
                } else if (k == 0) {
                    return j <= i ? j : -1;
                } else {
                    if (j > k) {
                        CharSequence charsequence2 = charsequence;

                        charsequence = charsequence1;
                        charsequence1 = charsequence2;
                        j = k;
                        k = charsequence2.length();
                    }

                    int[] aint = new int[j + 1];
                    int[] aint1 = new int[j + 1];
                    int l = Math.min(j, i) + 1;

                    int i1;

                    for (i1 = 0; i1 < l; aint[i1] = i1++) {
                        ;
                    }

                    Arrays.fill(aint, l, aint.length, Integer.MAX_VALUE);
                    Arrays.fill(aint1, Integer.MAX_VALUE);

                    for (i1 = 1; i1 <= k; ++i1) {
                        char c0 = charsequence1.charAt(i1 - 1);

                        aint1[0] = i1;
                        int j1 = Math.max(1, i1 - i);
                        int k1 = i1 > Integer.MAX_VALUE - i ? j : Math.min(j, i1 + i);

                        if (j1 > k1) {
                            return -1;
                        }

                        if (j1 > 1) {
                            aint1[j1 - 1] = Integer.MAX_VALUE;
                        }

                        for (int l1 = j1; l1 <= k1; ++l1) {
                            if (charsequence.charAt(l1 - 1) == c0) {
                                aint1[l1] = aint[l1 - 1];
                            } else {
                                aint1[l1] = 1 + Math.min(Math.min(aint1[l1 - 1], aint[l1]), aint[l1 - 1]);
                            }
                        }

                        int[] aint2 = aint;

                        aint = aint1;
                        aint1 = aint2;
                    }

                    if (aint[j] <= i) {
                        return aint[j];
                    } else {
                        return -1;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Strings must not be null");
        }
    }

    public static double getJaroWinklerDistance(CharSequence charsequence, CharSequence charsequence1) {
        double d0 = 0.1D;

        if (charsequence != null && charsequence1 != null) {
            double d1 = score(charsequence, charsequence1);
            int i = commonPrefixLength(charsequence, charsequence1);
            double d2 = (double) Math.round((d1 + 0.1D * (double) i * (1.0D - d1)) * 100.0D) / 100.0D;

            return d2;
        } else {
            throw new IllegalArgumentException("Strings must not be null");
        }
    }

    private static double score(CharSequence charsequence, CharSequence charsequence1) {
        String s;
        String s1;

        if (charsequence.length() > charsequence1.length()) {
            s = charsequence.toString().toLowerCase();
            s1 = charsequence1.toString().toLowerCase();
        } else {
            s = charsequence1.toString().toLowerCase();
            s1 = charsequence.toString().toLowerCase();
        }

        int i = s1.length() / 2 + 1;
        String s2 = getSetOfMatchingCharacterWithin(s1, s, i);
        String s3 = getSetOfMatchingCharacterWithin(s, s1, i);

        if (s2.length() != 0 && s3.length() != 0) {
            if (s2.length() != s3.length()) {
                return 0.0D;
            } else {
                int j = transpositions(s2, s3);
                double d0 = ((double) s2.length() / (double) s1.length() + (double) s3.length() / (double) s.length() + (double) (s2.length() - j) / (double) s2.length()) / 3.0D;

                return d0;
            }
        } else {
            return 0.0D;
        }
    }

    private static String getSetOfMatchingCharacterWithin(CharSequence charsequence, CharSequence charsequence1, int i) {
        StringBuilder stringbuilder = new StringBuilder();
        StringBuilder stringbuilder1 = new StringBuilder(charsequence1);

        for (int j = 0; j < charsequence.length(); ++j) {
            char c0 = charsequence.charAt(j);
            boolean flag = false;

            for (int k = Math.max(0, j - i); !flag && k < Math.min(j + i, charsequence1.length()); ++k) {
                if (stringbuilder1.charAt(k) == c0) {
                    flag = true;
                    stringbuilder.append(c0);
                    stringbuilder1.setCharAt(k, '*');
                }
            }
        }

        return stringbuilder.toString();
    }

    private static int transpositions(CharSequence charsequence, CharSequence charsequence1) {
        int i = 0;

        for (int j = 0; j < charsequence.length(); ++j) {
            if (charsequence.charAt(j) != charsequence1.charAt(j)) {
                ++i;
            }
        }

        return i / 2;
    }

    private static int commonPrefixLength(CharSequence charsequence, CharSequence charsequence1) {
        int i = getCommonPrefix(new String[] { charsequence.toString(), charsequence1.toString()}).length();

        return i > 4 ? 4 : i;
    }

    public static boolean startsWith(CharSequence charsequence, CharSequence charsequence1) {
        return startsWith(charsequence, charsequence1, false);
    }

    public static boolean startsWithIgnoreCase(CharSequence charsequence, CharSequence charsequence1) {
        return startsWith(charsequence, charsequence1, true);
    }

    private static boolean startsWith(CharSequence charsequence, CharSequence charsequence1, boolean flag) {
        return charsequence != null && charsequence1 != null ? (charsequence1.length() > charsequence.length() ? false : CharSequenceUtils.regionMatches(charsequence, flag, 0, charsequence1, 0, charsequence1.length())) : charsequence == null && charsequence1 == null;
    }

    public static boolean startsWithAny(CharSequence charsequence, CharSequence... acharsequence) {
        if (!isEmpty(charsequence) && !ArrayUtils.isEmpty((Object[]) acharsequence)) {
            CharSequence[] acharsequence1 = acharsequence;
            int i = acharsequence.length;

            for (int j = 0; j < i; ++j) {
                CharSequence charsequence1 = acharsequence1[j];

                if (startsWith(charsequence, charsequence1)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean endsWith(CharSequence charsequence, CharSequence charsequence1) {
        return endsWith(charsequence, charsequence1, false);
    }

    public static boolean endsWithIgnoreCase(CharSequence charsequence, CharSequence charsequence1) {
        return endsWith(charsequence, charsequence1, true);
    }

    private static boolean endsWith(CharSequence charsequence, CharSequence charsequence1, boolean flag) {
        if (charsequence != null && charsequence1 != null) {
            if (charsequence1.length() > charsequence.length()) {
                return false;
            } else {
                int i = charsequence.length() - charsequence1.length();

                return CharSequenceUtils.regionMatches(charsequence, flag, i, charsequence1, 0, charsequence1.length());
            }
        } else {
            return charsequence == null && charsequence1 == null;
        }
    }

    public static String normalizeSpace(String s) {
        return s == null ? null : StringUtils.WHITESPACE_PATTERN.matcher(trim(s)).replaceAll(" ");
    }

    public static boolean endsWithAny(CharSequence charsequence, CharSequence... acharsequence) {
        if (!isEmpty(charsequence) && !ArrayUtils.isEmpty((Object[]) acharsequence)) {
            CharSequence[] acharsequence1 = acharsequence;
            int i = acharsequence.length;

            for (int j = 0; j < i; ++j) {
                CharSequence charsequence1 = acharsequence1[j];

                if (endsWith(charsequence, charsequence1)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private static String appendIfMissing(String s, CharSequence charsequence, boolean flag, CharSequence... acharsequence) {
        if (s != null && !isEmpty(charsequence) && !endsWith(s, charsequence, flag)) {
            if (acharsequence != null && acharsequence.length > 0) {
                CharSequence[] acharsequence1 = acharsequence;
                int i = acharsequence.length;

                for (int j = 0; j < i; ++j) {
                    CharSequence charsequence1 = acharsequence1[j];

                    if (endsWith(s, charsequence1, flag)) {
                        return s;
                    }
                }
            }

            return s + charsequence.toString();
        } else {
            return s;
        }
    }

    public static String appendIfMissing(String s, CharSequence charsequence, CharSequence... acharsequence) {
        return appendIfMissing(s, charsequence, false, acharsequence);
    }

    public static String appendIfMissingIgnoreCase(String s, CharSequence charsequence, CharSequence... acharsequence) {
        return appendIfMissing(s, charsequence, true, acharsequence);
    }

    private static String prependIfMissing(String s, CharSequence charsequence, boolean flag, CharSequence... acharsequence) {
        if (s != null && !isEmpty(charsequence) && !startsWith(s, charsequence, flag)) {
            if (acharsequence != null && acharsequence.length > 0) {
                CharSequence[] acharsequence1 = acharsequence;
                int i = acharsequence.length;

                for (int j = 0; j < i; ++j) {
                    CharSequence charsequence1 = acharsequence1[j];

                    if (startsWith(s, charsequence1, flag)) {
                        return s;
                    }
                }
            }

            return charsequence.toString() + s;
        } else {
            return s;
        }
    }

    public static String prependIfMissing(String s, CharSequence charsequence, CharSequence... acharsequence) {
        return prependIfMissing(s, charsequence, false, acharsequence);
    }

    public static String prependIfMissingIgnoreCase(String s, CharSequence charsequence, CharSequence... acharsequence) {
        return prependIfMissing(s, charsequence, true, acharsequence);
    }

    /** @deprecated */
    @Deprecated
    public static String toString(byte[] abyte, String s) throws UnsupportedEncodingException {
        return s != null ? new String(abyte, s) : new String(abyte, Charset.defaultCharset());
    }

    public static String toEncodedString(byte[] abyte, Charset charset) {
        return new String(abyte, charset != null ? charset : Charset.defaultCharset());
    }
}
