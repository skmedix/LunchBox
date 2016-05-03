package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class FilenameUtils {

    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    private static final char OTHER_SEPARATOR;

    static boolean isSystemWindows() {
        return FilenameUtils.SYSTEM_SEPARATOR == 92;
    }

    private static boolean isSeparator(char c0) {
        return c0 == 47 || c0 == 92;
    }

    public static String normalize(String s) {
        return doNormalize(s, FilenameUtils.SYSTEM_SEPARATOR, true);
    }

    public static String normalize(String s, boolean flag) {
        int i = flag ? 47 : 92;

        return doNormalize(s, (char) i, true);
    }

    public static String normalizeNoEndSeparator(String s) {
        return doNormalize(s, FilenameUtils.SYSTEM_SEPARATOR, false);
    }

    public static String normalizeNoEndSeparator(String s, boolean flag) {
        int i = flag ? 47 : 92;

        return doNormalize(s, (char) i, false);
    }

    private static String doNormalize(String s, char c0, boolean flag) {
        if (s == null) {
            return null;
        } else {
            int i = s.length();

            if (i == 0) {
                return s;
            } else {
                int j = getPrefixLength(s);

                if (j < 0) {
                    return null;
                } else {
                    char[] achar = new char[i + 2];

                    s.getChars(0, s.length(), achar, 0);
                    char c1 = c0 == FilenameUtils.SYSTEM_SEPARATOR ? FilenameUtils.OTHER_SEPARATOR : FilenameUtils.SYSTEM_SEPARATOR;

                    for (int k = 0; k < achar.length; ++k) {
                        if (achar[k] == c1) {
                            achar[k] = c0;
                        }
                    }

                    boolean flag1 = true;

                    if (achar[i - 1] != c0) {
                        achar[i++] = c0;
                        flag1 = false;
                    }

                    int l;

                    for (l = j + 1; l < i; ++l) {
                        if (achar[l] == c0 && achar[l - 1] == c0) {
                            System.arraycopy(achar, l, achar, l - 1, i - l);
                            --i;
                            --l;
                        }
                    }

                    for (l = j + 1; l < i; ++l) {
                        if (achar[l] == c0 && achar[l - 1] == 46 && (l == j + 1 || achar[l - 2] == c0)) {
                            if (l == i - 1) {
                                flag1 = true;
                            }

                            System.arraycopy(achar, l + 1, achar, l - 1, i - l);
                            i -= 2;
                            --l;
                        }
                    }

                    label106:
                    for (l = j + 2; l < i; ++l) {
                        if (achar[l] == c0 && achar[l - 1] == 46 && achar[l - 2] == 46 && (l == j + 2 || achar[l - 3] == c0)) {
                            if (l == j + 2) {
                                return null;
                            }

                            if (l == i - 1) {
                                flag1 = true;
                            }

                            for (int i1 = l - 4; i1 >= j; --i1) {
                                if (achar[i1] == c0) {
                                    System.arraycopy(achar, l + 1, achar, i1 + 1, i - l);
                                    i -= l - i1;
                                    l = i1 + 1;
                                    continue label106;
                                }
                            }

                            System.arraycopy(achar, l + 1, achar, j, i - l);
                            i -= l + 1 - j;
                            l = j + 1;
                        }
                    }

                    if (i <= 0) {
                        return "";
                    } else if (i <= j) {
                        return new String(achar, 0, i);
                    } else if (flag1 && flag) {
                        return new String(achar, 0, i);
                    } else {
                        return new String(achar, 0, i - 1);
                    }
                }
            }
        }
    }

    public static String concat(String s, String s1) {
        int i = getPrefixLength(s1);

        if (i < 0) {
            return null;
        } else if (i > 0) {
            return normalize(s1);
        } else if (s == null) {
            return null;
        } else {
            int j = s.length();

            if (j == 0) {
                return normalize(s1);
            } else {
                char c0 = s.charAt(j - 1);

                return isSeparator(c0) ? normalize(s + s1) : normalize(s + '/' + s1);
            }
        }
    }

    public static boolean directoryContains(String s, String s1) throws IOException {
        if (s == null) {
            throw new IllegalArgumentException("Directory must not be null");
        } else {
            return s1 == null ? false : (IOCase.SYSTEM.checkEquals(s, s1) ? false : IOCase.SYSTEM.checkStartsWith(s1, s));
        }
    }

    public static String separatorsToUnix(String s) {
        return s != null && s.indexOf(92) != -1 ? s.replace('\\', '/') : s;
    }

    public static String separatorsToWindows(String s) {
        return s != null && s.indexOf(47) != -1 ? s.replace('/', '\\') : s;
    }

    public static String separatorsToSystem(String s) {
        return s == null ? null : (isSystemWindows() ? separatorsToWindows(s) : separatorsToUnix(s));
    }

    public static int getPrefixLength(String s) {
        if (s == null) {
            return -1;
        } else {
            int i = s.length();

            if (i == 0) {
                return 0;
            } else {
                char c0 = s.charAt(0);

                if (c0 == 58) {
                    return -1;
                } else if (i == 1) {
                    return c0 == 126 ? 2 : (isSeparator(c0) ? 1 : 0);
                } else {
                    int j;

                    if (c0 == 126) {
                        int k = s.indexOf(47, 1);

                        j = s.indexOf(92, 1);
                        if (k == -1 && j == -1) {
                            return i + 1;
                        } else {
                            k = k == -1 ? j : k;
                            j = j == -1 ? k : j;
                            return Math.min(k, j) + 1;
                        }
                    } else {
                        char c1 = s.charAt(1);

                        if (c1 == 58) {
                            c0 = Character.toUpperCase(c0);
                            return c0 >= 65 && c0 <= 90 ? (i != 2 && isSeparator(s.charAt(2)) ? 3 : 2) : -1;
                        } else if (isSeparator(c0) && isSeparator(c1)) {
                            j = s.indexOf(47, 2);
                            int l = s.indexOf(92, 2);

                            if ((j != -1 || l != -1) && j != 2 && l != 2) {
                                j = j == -1 ? l : j;
                                l = l == -1 ? j : l;
                                return Math.min(j, l) + 1;
                            } else {
                                return -1;
                            }
                        } else {
                            return isSeparator(c0) ? 1 : 0;
                        }
                    }
                }
            }
        }
    }

    public static int indexOfLastSeparator(String s) {
        if (s == null) {
            return -1;
        } else {
            int i = s.lastIndexOf(47);
            int j = s.lastIndexOf(92);

            return Math.max(i, j);
        }
    }

    public static int indexOfExtension(String s) {
        if (s == null) {
            return -1;
        } else {
            int i = s.lastIndexOf(46);
            int j = indexOfLastSeparator(s);

            return j > i ? -1 : i;
        }
    }

    public static String getPrefix(String s) {
        if (s == null) {
            return null;
        } else {
            int i = getPrefixLength(s);

            return i < 0 ? null : (i > s.length() ? s + '/' : s.substring(0, i));
        }
    }

    public static String getPath(String s) {
        return doGetPath(s, 1);
    }

    public static String getPathNoEndSeparator(String s) {
        return doGetPath(s, 0);
    }

    private static String doGetPath(String s, int i) {
        if (s == null) {
            return null;
        } else {
            int j = getPrefixLength(s);

            if (j < 0) {
                return null;
            } else {
                int k = indexOfLastSeparator(s);
                int l = k + i;

                return j < s.length() && k >= 0 && j < l ? s.substring(j, l) : "";
            }
        }
    }

    public static String getFullPath(String s) {
        return doGetFullPath(s, true);
    }

    public static String getFullPathNoEndSeparator(String s) {
        return doGetFullPath(s, false);
    }

    private static String doGetFullPath(String s, boolean flag) {
        if (s == null) {
            return null;
        } else {
            int i = getPrefixLength(s);

            if (i < 0) {
                return null;
            } else if (i >= s.length()) {
                return flag ? getPrefix(s) : s;
            } else {
                int j = indexOfLastSeparator(s);

                if (j < 0) {
                    return s.substring(0, i);
                } else {
                    int k = j + (flag ? 1 : 0);

                    if (k == 0) {
                        ++k;
                    }

                    return s.substring(0, k);
                }
            }
        }
    }

    public static String getName(String s) {
        if (s == null) {
            return null;
        } else {
            int i = indexOfLastSeparator(s);

            return s.substring(i + 1);
        }
    }

    public static String getBaseName(String s) {
        return removeExtension(getName(s));
    }

    public static String getExtension(String s) {
        if (s == null) {
            return null;
        } else {
            int i = indexOfExtension(s);

            return i == -1 ? "" : s.substring(i + 1);
        }
    }

    public static String removeExtension(String s) {
        if (s == null) {
            return null;
        } else {
            int i = indexOfExtension(s);

            return i == -1 ? s : s.substring(0, i);
        }
    }

    public static boolean equals(String s, String s1) {
        return equals(s, s1, false, IOCase.SENSITIVE);
    }

    public static boolean equalsOnSystem(String s, String s1) {
        return equals(s, s1, false, IOCase.SYSTEM);
    }

    public static boolean equalsNormalized(String s, String s1) {
        return equals(s, s1, true, IOCase.SENSITIVE);
    }

    public static boolean equalsNormalizedOnSystem(String s, String s1) {
        return equals(s, s1, true, IOCase.SYSTEM);
    }

    public static boolean equals(String s, String s1, boolean flag, IOCase iocase) {
        if (s != null && s1 != null) {
            if (flag) {
                s = normalize(s);
                s1 = normalize(s1);
                if (s == null || s1 == null) {
                    throw new NullPointerException("Error normalizing one or both of the file names");
                }
            }

            if (iocase == null) {
                iocase = IOCase.SENSITIVE;
            }

            return iocase.checkEquals(s, s1);
        } else {
            return s == null && s1 == null;
        }
    }

    public static boolean isExtension(String s, String s1) {
        if (s == null) {
            return false;
        } else if (s1 != null && s1.length() != 0) {
            String s2 = getExtension(s);

            return s2.equals(s1);
        } else {
            return indexOfExtension(s) == -1;
        }
    }

    public static boolean isExtension(String s, String[] astring) {
        if (s == null) {
            return false;
        } else if (astring != null && astring.length != 0) {
            String s1 = getExtension(s);
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s2 = astring1[j];

                if (s1.equals(s2)) {
                    return true;
                }
            }

            return false;
        } else {
            return indexOfExtension(s) == -1;
        }
    }

    public static boolean isExtension(String s, Collection collection) {
        if (s == null) {
            return false;
        } else if (collection != null && !collection.isEmpty()) {
            String s1 = getExtension(s);
            Iterator iterator = collection.iterator();

            String s2;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                s2 = (String) iterator.next();
            } while (!s1.equals(s2));

            return true;
        } else {
            return indexOfExtension(s) == -1;
        }
    }

    public static boolean wildcardMatch(String s, String s1) {
        return wildcardMatch(s, s1, IOCase.SENSITIVE);
    }

    public static boolean wildcardMatchOnSystem(String s, String s1) {
        return wildcardMatch(s, s1, IOCase.SYSTEM);
    }

    public static boolean wildcardMatch(String s, String s1, IOCase iocase) {
        if (s == null && s1 == null) {
            return true;
        } else if (s != null && s1 != null) {
            if (iocase == null) {
                iocase = IOCase.SENSITIVE;
            }

            String[] astring = splitOnTokens(s1);
            boolean flag = false;
            int i = 0;
            int j = 0;
            Stack stack = new Stack();

            while (true) {
                if (stack.size() > 0) {
                    int[] aint = (int[]) stack.pop();

                    j = aint[0];
                    i = aint[1];
                    flag = true;
                }

                while (true) {
                    if (j < astring.length) {
                        label60: {
                            if (astring[j].equals("?")) {
                                ++i;
                                if (i > s.length()) {
                                    break label60;
                                }

                                flag = false;
                            } else if (astring[j].equals("*")) {
                                flag = true;
                                if (j == astring.length - 1) {
                                    i = s.length();
                                }
                            } else {
                                if (flag) {
                                    i = iocase.checkIndexOf(s, i, astring[j]);
                                    if (i == -1) {
                                        break label60;
                                    }

                                    int k = iocase.checkIndexOf(s, i + 1, astring[j]);

                                    if (k >= 0) {
                                        stack.push(new int[] { j, k});
                                    }
                                } else if (!iocase.checkRegionMatches(s, i, astring[j])) {
                                    break label60;
                                }

                                i += astring[j].length();
                                flag = false;
                            }

                            ++j;
                            continue;
                        }
                    }

                    if (j == astring.length && i == s.length()) {
                        return true;
                    }

                    if (stack.size() <= 0) {
                        return false;
                    }
                    break;
                }
            }
        } else {
            return false;
        }
    }

    static String[] splitOnTokens(String s) {
        if (s.indexOf(63) == -1 && s.indexOf(42) == -1) {
            return new String[] { s};
        } else {
            char[] achar = s.toCharArray();
            ArrayList arraylist = new ArrayList();
            StringBuilder stringbuilder = new StringBuilder();

            for (int i = 0; i < achar.length; ++i) {
                if (achar[i] != 63 && achar[i] != 42) {
                    stringbuilder.append(achar[i]);
                } else {
                    if (stringbuilder.length() != 0) {
                        arraylist.add(stringbuilder.toString());
                        stringbuilder.setLength(0);
                    }

                    if (achar[i] == 63) {
                        arraylist.add("?");
                    } else if (arraylist.isEmpty() || i > 0 && !((String) arraylist.get(arraylist.size() - 1)).equals("*")) {
                        arraylist.add("*");
                    }
                }
            }

            if (stringbuilder.length() != 0) {
                arraylist.add(stringbuilder.toString());
            }

            return (String[]) arraylist.toArray(new String[arraylist.size()]);
        }
    }

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = 47;
        } else {
            OTHER_SEPARATOR = 92;
        }

    }
}
