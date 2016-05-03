package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;

public class NumberUtils {

    public static final Long LONG_ZERO = Long.valueOf(0L);
    public static final Long LONG_ONE = Long.valueOf(1L);
    public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    public static final Integer INTEGER_ONE = Integer.valueOf(1);
    public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
    public static final Short SHORT_ZERO = Short.valueOf((short) 0);
    public static final Short SHORT_ONE = Short.valueOf((short) 1);
    public static final Short SHORT_MINUS_ONE = Short.valueOf((short) -1);
    public static final Byte BYTE_ZERO = Byte.valueOf((byte) 0);
    public static final Byte BYTE_ONE = Byte.valueOf((byte) 1);
    public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte) -1);
    public static final Double DOUBLE_ZERO = Double.valueOf(0.0D);
    public static final Double DOUBLE_ONE = Double.valueOf(1.0D);
    public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0D);
    public static final Float FLOAT_ZERO = Float.valueOf(0.0F);
    public static final Float FLOAT_ONE = Float.valueOf(1.0F);
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0F);

    public static int toInt(String s) {
        return toInt(s, 0);
    }

    public static int toInt(String s, int i) {
        if (s == null) {
            return i;
        } else {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException numberformatexception) {
                return i;
            }
        }
    }

    public static long toLong(String s) {
        return toLong(s, 0L);
    }

    public static long toLong(String s, long i) {
        if (s == null) {
            return i;
        } else {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException numberformatexception) {
                return i;
            }
        }
    }

    public static float toFloat(String s) {
        return toFloat(s, 0.0F);
    }

    public static float toFloat(String s, float f) {
        if (s == null) {
            return f;
        } else {
            try {
                return Float.parseFloat(s);
            } catch (NumberFormatException numberformatexception) {
                return f;
            }
        }
    }

    public static double toDouble(String s) {
        return toDouble(s, 0.0D);
    }

    public static double toDouble(String s, double d0) {
        if (s == null) {
            return d0;
        } else {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException numberformatexception) {
                return d0;
            }
        }
    }

    public static byte toByte(String s) {
        return toByte(s, (byte) 0);
    }

    public static byte toByte(String s, byte b0) {
        if (s == null) {
            return b0;
        } else {
            try {
                return Byte.parseByte(s);
            } catch (NumberFormatException numberformatexception) {
                return b0;
            }
        }
    }

    public static short toShort(String s) {
        return toShort(s, (short) 0);
    }

    public static short toShort(String s, short short0) {
        if (s == null) {
            return short0;
        } else {
            try {
                return Short.parseShort(s);
            } catch (NumberFormatException numberformatexception) {
                return short0;
            }
        }
    }

    public static Number createNumber(String s) throws NumberFormatException {
        if (s == null) {
            return null;
        } else if (StringUtils.isBlank(s)) {
            throw new NumberFormatException("A blank string is not a valid number");
        } else {
            String[] astring = new String[] { "0x", "0X", "-0x", "-0X", "#", "-#"};
            int i = 0;
            String[] astring1 = astring;
            int j = astring.length;

            String s1;

            for (int k = 0; k < j; ++k) {
                s1 = astring1[k];
                if (s.startsWith(s1)) {
                    i += s1.length();
                    break;
                }
            }

            char c0;

            if (i > 0) {
                c0 = 0;

                for (j = i; j < s.length(); ++j) {
                    c0 = s.charAt(j);
                    if (c0 != 48) {
                        break;
                    }

                    ++i;
                }

                j = s.length() - i;
                return (Number) (j <= 16 && (j != 16 || c0 <= 55) ? (j <= 8 && (j != 8 || c0 <= 55) ? createInteger(s) : createLong(s)) : createBigInteger(s));
            } else {
                c0 = s.charAt(s.length() - 1);
                int l = s.indexOf(46);
                int i1 = s.indexOf(101) + s.indexOf(69) + 1;
                int j1 = 0;
                String s2;
                String s3;

                if (l > -1) {
                    if (i1 > -1) {
                        if (i1 < l || i1 > s.length()) {
                            throw new NumberFormatException(s + " is not a valid number.");
                        }

                        s3 = s.substring(l + 1, i1);
                    } else {
                        s3 = s.substring(l + 1);
                    }

                    s2 = s.substring(0, l);
                    j1 = s3.length();
                } else {
                    if (i1 > -1) {
                        if (i1 > s.length()) {
                            throw new NumberFormatException(s + " is not a valid number.");
                        }

                        s2 = s.substring(0, i1);
                    } else {
                        s2 = s;
                    }

                    s3 = null;
                }

                if (!Character.isDigit(c0) && c0 != 46) {
                    if (i1 > -1 && i1 < s.length() - 1) {
                        s1 = s.substring(i1 + 1, s.length() - 1);
                    } else {
                        s1 = null;
                    }

                    String s4 = s.substring(0, s.length() - 1);
                    boolean flag = isAllZeros(s2) && isAllZeros(s1);

                    switch (c0) {
                    case 'F':
                    case 'f':
                        try {
                            Float float = createFloat(s4);

                            if (!float.isInfinite() && (float.floatValue() != 0.0F || flag)) {
                                return float;
                            }
                        } catch (NumberFormatException numberformatexception) {
                            ;
                        }

                    case 'D':
                    case 'd':
                        try {
                            Double double = createDouble(s4);

                            if (!double.isInfinite() && ((double) double.floatValue() != 0.0D || flag)) {
                                return double;
                            }
                        } catch (NumberFormatException numberformatexception1) {
                            ;
                        }

                        try {
                            return createBigDecimal(s4);
                        } catch (NumberFormatException numberformatexception2) {
                            ;
                        }

                    default:
                        throw new NumberFormatException(s + " is not a valid number.");

                    case 'L':
                    case 'l':
                        if (s3 == null && s1 == null && (s4.charAt(0) == 45 && isDigits(s4.substring(1)) || isDigits(s4))) {
                            try {
                                return createLong(s4);
                            } catch (NumberFormatException numberformatexception3) {
                                return createBigInteger(s4);
                            }
                        } else {
                            throw new NumberFormatException(s + " is not a valid number.");
                        }
                    }
                } else {
                    if (i1 > -1 && i1 < s.length() - 1) {
                        s1 = s.substring(i1 + 1, s.length());
                    } else {
                        s1 = null;
                    }

                    if (s3 == null && s1 == null) {
                        try {
                            return createInteger(s);
                        } catch (NumberFormatException numberformatexception4) {
                            try {
                                return createLong(s);
                            } catch (NumberFormatException numberformatexception5) {
                                return createBigInteger(s);
                            }
                        }
                    } else {
                        boolean flag1 = isAllZeros(s2) && isAllZeros(s1);

                        try {
                            if (j1 <= 7) {
                                Float float1 = createFloat(s);

                                if (!float1.isInfinite() && (float1.floatValue() != 0.0F || flag1)) {
                                    return float1;
                                }
                            }
                        } catch (NumberFormatException numberformatexception6) {
                            ;
                        }

                        try {
                            if (j1 <= 16) {
                                Double double1 = createDouble(s);

                                if (!double1.isInfinite() && (double1.doubleValue() != 0.0D || flag1)) {
                                    return double1;
                                }
                            }
                        } catch (NumberFormatException numberformatexception7) {
                            ;
                        }

                        return createBigDecimal(s);
                    }
                }
            }
        }
    }

    private static boolean isAllZeros(String s) {
        if (s == null) {
            return true;
        } else {
            for (int i = s.length() - 1; i >= 0; --i) {
                if (s.charAt(i) != 48) {
                    return false;
                }
            }

            return s.length() > 0;
        }
    }

    public static Float createFloat(String s) {
        return s == null ? null : Float.valueOf(s);
    }

    public static Double createDouble(String s) {
        return s == null ? null : Double.valueOf(s);
    }

    public static Integer createInteger(String s) {
        return s == null ? null : Integer.decode(s);
    }

    public static Long createLong(String s) {
        return s == null ? null : Long.decode(s);
    }

    public static BigInteger createBigInteger(String s) {
        if (s == null) {
            return null;
        } else {
            int i = 0;
            byte b0 = 10;
            boolean flag = false;

            if (s.startsWith("-")) {
                flag = true;
                i = 1;
            }

            if (!s.startsWith("0x", i) && !s.startsWith("0x", i)) {
                if (s.startsWith("#", i)) {
                    b0 = 16;
                    ++i;
                } else if (s.startsWith("0", i) && s.length() > i + 1) {
                    b0 = 8;
                    ++i;
                }
            } else {
                b0 = 16;
                i += 2;
            }

            BigInteger biginteger = new BigInteger(s.substring(i), b0);

            return flag ? biginteger.negate() : biginteger;
        }
    }

    public static BigDecimal createBigDecimal(String s) {
        if (s == null) {
            return null;
        } else if (StringUtils.isBlank(s)) {
            throw new NumberFormatException("A blank string is not a valid number");
        } else if (s.trim().startsWith("--")) {
            throw new NumberFormatException(s + " is not a valid number.");
        } else {
            return new BigDecimal(s);
        }
    }

    public static long min(long[] along) {
        validateArray(along);
        long i = along[0];

        for (int j = 1; j < along.length; ++j) {
            if (along[j] < i) {
                i = along[j];
            }
        }

        return i;
    }

    public static int min(int[] aint) {
        validateArray(aint);
        int i = aint[0];

        for (int j = 1; j < aint.length; ++j) {
            if (aint[j] < i) {
                i = aint[j];
            }
        }

        return i;
    }

    public static short min(short[] ashort) {
        validateArray(ashort);
        short short0 = ashort[0];

        for (int i = 1; i < ashort.length; ++i) {
            if (ashort[i] < short0) {
                short0 = ashort[i];
            }
        }

        return short0;
    }

    public static byte min(byte[] abyte) {
        validateArray(abyte);
        byte b0 = abyte[0];

        for (int i = 1; i < abyte.length; ++i) {
            if (abyte[i] < b0) {
                b0 = abyte[i];
            }
        }

        return b0;
    }

    public static double min(double[] adouble) {
        validateArray(adouble);
        double d0 = adouble[0];

        for (int i = 1; i < adouble.length; ++i) {
            if (Double.isNaN(adouble[i])) {
                return Double.NaN;
            }

            if (adouble[i] < d0) {
                d0 = adouble[i];
            }
        }

        return d0;
    }

    public static float min(float[] afloat) {
        validateArray(afloat);
        float f = afloat[0];

        for (int i = 1; i < afloat.length; ++i) {
            if (Float.isNaN(afloat[i])) {
                return Float.NaN;
            }

            if (afloat[i] < f) {
                f = afloat[i];
            }
        }

        return f;
    }

    public static long max(long[] along) {
        validateArray(along);
        long i = along[0];

        for (int j = 1; j < along.length; ++j) {
            if (along[j] > i) {
                i = along[j];
            }
        }

        return i;
    }

    public static int max(int[] aint) {
        validateArray(aint);
        int i = aint[0];

        for (int j = 1; j < aint.length; ++j) {
            if (aint[j] > i) {
                i = aint[j];
            }
        }

        return i;
    }

    public static short max(short[] ashort) {
        validateArray(ashort);
        short short0 = ashort[0];

        for (int i = 1; i < ashort.length; ++i) {
            if (ashort[i] > short0) {
                short0 = ashort[i];
            }
        }

        return short0;
    }

    public static byte max(byte[] abyte) {
        validateArray(abyte);
        byte b0 = abyte[0];

        for (int i = 1; i < abyte.length; ++i) {
            if (abyte[i] > b0) {
                b0 = abyte[i];
            }
        }

        return b0;
    }

    public static double max(double[] adouble) {
        validateArray(adouble);
        double d0 = adouble[0];

        for (int i = 1; i < adouble.length; ++i) {
            if (Double.isNaN(adouble[i])) {
                return Double.NaN;
            }

            if (adouble[i] > d0) {
                d0 = adouble[i];
            }
        }

        return d0;
    }

    public static float max(float[] afloat) {
        validateArray(afloat);
        float f = afloat[0];

        for (int i = 1; i < afloat.length; ++i) {
            if (Float.isNaN(afloat[i])) {
                return Float.NaN;
            }

            if (afloat[i] > f) {
                f = afloat[i];
            }
        }

        return f;
    }

    private static void validateArray(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (Array.getLength(object) == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    }

    public static long min(long i, long j, long k) {
        if (j < i) {
            i = j;
        }

        if (k < i) {
            i = k;
        }

        return i;
    }

    public static int min(int i, int j, int k) {
        if (j < i) {
            i = j;
        }

        if (k < i) {
            i = k;
        }

        return i;
    }

    public static short min(short short0, short short1, short short2) {
        if (short1 < short0) {
            short0 = short1;
        }

        if (short2 < short0) {
            short0 = short2;
        }

        return short0;
    }

    public static byte min(byte b0, byte b1, byte b2) {
        if (b1 < b0) {
            b0 = b1;
        }

        if (b2 < b0) {
            b0 = b2;
        }

        return b0;
    }

    public static double min(double d0, double d1, double d2) {
        return Math.min(Math.min(d0, d1), d2);
    }

    public static float min(float f, float f1, float f2) {
        return Math.min(Math.min(f, f1), f2);
    }

    public static long max(long i, long j, long k) {
        if (j > i) {
            i = j;
        }

        if (k > i) {
            i = k;
        }

        return i;
    }

    public static int max(int i, int j, int k) {
        if (j > i) {
            i = j;
        }

        if (k > i) {
            i = k;
        }

        return i;
    }

    public static short max(short short0, short short1, short short2) {
        if (short1 > short0) {
            short0 = short1;
        }

        if (short2 > short0) {
            short0 = short2;
        }

        return short0;
    }

    public static byte max(byte b0, byte b1, byte b2) {
        if (b1 > b0) {
            b0 = b1;
        }

        if (b2 > b0) {
            b0 = b2;
        }

        return b0;
    }

    public static double max(double d0, double d1, double d2) {
        return Math.max(Math.max(d0, d1), d2);
    }

    public static float max(float f, float f1, float f2) {
        return Math.max(Math.max(f, f1), f2);
    }

    public static boolean isDigits(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        } else {
            for (int i = 0; i < s.length(); ++i) {
                if (!Character.isDigit(s.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNumber(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        } else {
            char[] achar = s.toCharArray();
            int i = achar.length;
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            int j = achar[0] == 45 ? 1 : 0;
            int k;

            if (i > j + 1 && achar[j] == 48) {
                if (achar[j + 1] == 120 || achar[j + 1] == 88) {
                    k = j + 2;
                    if (k == i) {
                        return false;
                    }

                    while (k < achar.length) {
                        if ((achar[k] < 48 || achar[k] > 57) && (achar[k] < 97 || achar[k] > 102) && (achar[k] < 65 || achar[k] > 70)) {
                            return false;
                        }

                        ++k;
                    }

                    return true;
                }

                if (Character.isDigit(achar[j + 1])) {
                    for (k = j + 1; k < achar.length; ++k) {
                        if (achar[k] < 48 || achar[k] > 55) {
                            return false;
                        }
                    }

                    return true;
                }
            }

            --i;

            for (k = j; k < i || k < i + 1 && flag2 && !flag3; ++k) {
                if (achar[k] >= 48 && achar[k] <= 57) {
                    flag3 = true;
                    flag2 = false;
                } else if (achar[k] == 46) {
                    if (flag1 || flag) {
                        return false;
                    }

                    flag1 = true;
                } else if (achar[k] != 101 && achar[k] != 69) {
                    if (achar[k] != 43 && achar[k] != 45) {
                        return false;
                    }

                    if (!flag2) {
                        return false;
                    }

                    flag2 = false;
                    flag3 = false;
                } else {
                    if (flag) {
                        return false;
                    }

                    if (!flag3) {
                        return false;
                    }

                    flag = true;
                    flag2 = true;
                }
            }

            if (k < achar.length) {
                if (achar[k] >= 48 && achar[k] <= 57) {
                    return true;
                } else if (achar[k] != 101 && achar[k] != 69) {
                    if (achar[k] == 46) {
                        if (!flag1 && !flag) {
                            return flag3;
                        } else {
                            return false;
                        }
                    } else if (!flag2 && (achar[k] == 100 || achar[k] == 68 || achar[k] == 102 || achar[k] == 70)) {
                        return flag3;
                    } else if (achar[k] != 108 && achar[k] != 76) {
                        return false;
                    } else {
                        return flag3 && !flag && !flag1;
                    }
                } else {
                    return false;
                }
            } else {
                return !flag2 && flag3;
            }
        }
    }
}
