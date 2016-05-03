package org.apache.commons.lang3;

import java.util.Random;

public class RandomStringUtils {

    private static final Random RANDOM = new Random();

    public static String random(int i) {
        return random(i, false, false);
    }

    public static String randomAscii(int i) {
        return random(i, 32, 127, false, false);
    }

    public static String randomAlphabetic(int i) {
        return random(i, true, false);
    }

    public static String randomAlphanumeric(int i) {
        return random(i, true, true);
    }

    public static String randomNumeric(int i) {
        return random(i, false, true);
    }

    public static String random(int i, boolean flag, boolean flag1) {
        return random(i, 0, 0, flag, flag1);
    }

    public static String random(int i, int j, int k, boolean flag, boolean flag1) {
        return random(i, j, k, flag, flag1, (char[]) null, RandomStringUtils.RANDOM);
    }

    public static String random(int i, int j, int k, boolean flag, boolean flag1, char... achar) {
        return random(i, j, k, flag, flag1, achar, RandomStringUtils.RANDOM);
    }

    public static String random(int i, int j, int k, boolean flag, boolean flag1, char[] achar, Random random) {
        if (i == 0) {
            return "";
        } else if (i < 0) {
            throw new IllegalArgumentException("Requested random string length " + i + " is less than 0.");
        } else if (achar != null && achar.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        } else {
            if (j == 0 && k == 0) {
                if (achar != null) {
                    k = achar.length;
                } else if (!flag && !flag1) {
                    k = Integer.MAX_VALUE;
                } else {
                    k = 123;
                    j = 32;
                }
            } else if (k <= j) {
                throw new IllegalArgumentException("Parameter end (" + k + ") must be greater than start (" + j + ")");
            }

            char[] achar1 = new char[i];
            int l = k - j;

            while (i-- != 0) {
                char c0;

                if (achar == null) {
                    c0 = (char) (random.nextInt(l) + j);
                } else {
                    c0 = achar[random.nextInt(l) + j];
                }

                if ((!flag || !Character.isLetter(c0)) && (!flag1 || !Character.isDigit(c0)) && (flag || flag1)) {
                    ++i;
                } else if (c0 >= '\udc00' && c0 <= '\udfff') {
                    if (i == 0) {
                        ++i;
                    } else {
                        achar1[i] = c0;
                        --i;
                        achar1[i] = (char) ('\ud800' + random.nextInt(128));
                    }
                } else if (c0 >= '\ud800' && c0 <= '\udb7f') {
                    if (i == 0) {
                        ++i;
                    } else {
                        achar1[i] = (char) ('\udc00' + random.nextInt(128));
                        --i;
                        achar1[i] = c0;
                    }
                } else if (c0 >= '\udb80' && c0 <= '\udbff') {
                    ++i;
                } else {
                    achar1[i] = c0;
                }
            }

            return new String(achar1);
        }
    }

    public static String random(int i, String s) {
        return s == null ? random(i, 0, 0, false, false, (char[]) null, RandomStringUtils.RANDOM) : random(i, s.toCharArray());
    }

    public static String random(int i, char... achar) {
        return achar == null ? random(i, 0, 0, false, false, (char[]) null, RandomStringUtils.RANDOM) : random(i, 0, achar.length, false, false, achar, RandomStringUtils.RANDOM);
    }
}
