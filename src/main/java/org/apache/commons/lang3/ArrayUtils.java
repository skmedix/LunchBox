package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.mutable.MutableInt;

public class ArrayUtils {

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
    public static final int INDEX_NOT_FOUND = -1;

    public static String toString(Object object) {
        return toString(object, "{}");
    }

    public static String toString(Object object, String s) {
        return object == null ? s : (new ToStringBuilder(object, ToStringStyle.SIMPLE_STYLE)).append(object).toString();
    }

    public static int hashCode(Object object) {
        return (new HashCodeBuilder()).append(object).toHashCode();
    }

    /** @deprecated */
    @Deprecated
    public static boolean isEquals(Object object, Object object1) {
        return (new EqualsBuilder()).append(object, object1).isEquals();
    }

    public static Map toMap(Object[] aobject) {
        if (aobject == null) {
            return null;
        } else {
            HashMap hashmap = new HashMap((int) ((double) aobject.length * 1.5D));

            for (int i = 0; i < aobject.length; ++i) {
                Object object = aobject[i];

                if (object instanceof Entry) {
                    Entry entry = (Entry) object;

                    hashmap.put(entry.getKey(), entry.getValue());
                } else {
                    if (!(object instanceof Object[])) {
                        throw new IllegalArgumentException("Array element " + i + ", \'" + object + "\', is neither of type Map.Entry nor an Array");
                    }

                    Object[] aobject1 = (Object[]) ((Object[]) object);

                    if (aobject1.length < 2) {
                        throw new IllegalArgumentException("Array element " + i + ", \'" + object + "\', has a length less than 2");
                    }

                    hashmap.put(aobject1[0], aobject1[1]);
                }
            }

            return hashmap;
        }
    }

    public static Object[] toArray(Object... aobject) {
        return aobject;
    }

    public static Object[] clone(Object[] aobject) {
        return aobject == null ? null : (Object[]) aobject.clone();
    }

    public static long[] clone(long[] along) {
        return along == null ? null : (long[]) along.clone();
    }

    public static int[] clone(int[] aint) {
        return aint == null ? null : (int[]) aint.clone();
    }

    public static short[] clone(short[] ashort) {
        return ashort == null ? null : (short[]) ashort.clone();
    }

    public static char[] clone(char[] achar) {
        return achar == null ? null : (char[]) achar.clone();
    }

    public static byte[] clone(byte[] abyte) {
        return abyte == null ? null : (byte[]) abyte.clone();
    }

    public static double[] clone(double[] adouble) {
        return adouble == null ? null : (double[]) adouble.clone();
    }

    public static float[] clone(float[] afloat) {
        return afloat == null ? null : (float[]) afloat.clone();
    }

    public static boolean[] clone(boolean[] aboolean) {
        return aboolean == null ? null : (boolean[]) aboolean.clone();
    }

    public static Object[] nullToEmpty(Object[] aobject) {
        return aobject != null && aobject.length != 0 ? aobject : ArrayUtils.EMPTY_OBJECT_ARRAY;
    }

    public static Class[] nullToEmpty(Class[] aclass) {
        return aclass != null && aclass.length != 0 ? aclass : ArrayUtils.EMPTY_CLASS_ARRAY;
    }

    public static String[] nullToEmpty(String[] astring) {
        return astring != null && astring.length != 0 ? astring : ArrayUtils.EMPTY_STRING_ARRAY;
    }

    public static long[] nullToEmpty(long[] along) {
        return along != null && along.length != 0 ? along : ArrayUtils.EMPTY_LONG_ARRAY;
    }

    public static int[] nullToEmpty(int[] aint) {
        return aint != null && aint.length != 0 ? aint : ArrayUtils.EMPTY_INT_ARRAY;
    }

    public static short[] nullToEmpty(short[] ashort) {
        return ashort != null && ashort.length != 0 ? ashort : ArrayUtils.EMPTY_SHORT_ARRAY;
    }

    public static char[] nullToEmpty(char[] achar) {
        return achar != null && achar.length != 0 ? achar : ArrayUtils.EMPTY_CHAR_ARRAY;
    }

    public static byte[] nullToEmpty(byte[] abyte) {
        return abyte != null && abyte.length != 0 ? abyte : ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    public static double[] nullToEmpty(double[] adouble) {
        return adouble != null && adouble.length != 0 ? adouble : ArrayUtils.EMPTY_DOUBLE_ARRAY;
    }

    public static float[] nullToEmpty(float[] afloat) {
        return afloat != null && afloat.length != 0 ? afloat : ArrayUtils.EMPTY_FLOAT_ARRAY;
    }

    public static boolean[] nullToEmpty(boolean[] aboolean) {
        return aboolean != null && aboolean.length != 0 ? aboolean : ArrayUtils.EMPTY_BOOLEAN_ARRAY;
    }

    public static Long[] nullToEmpty(Long[] along) {
        return along != null && along.length != 0 ? along : ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;
    }

    public static Integer[] nullToEmpty(Integer[] ainteger) {
        return ainteger != null && ainteger.length != 0 ? ainteger : ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
    }

    public static Short[] nullToEmpty(Short[] ashort) {
        return ashort != null && ashort.length != 0 ? ashort : ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY;
    }

    public static Character[] nullToEmpty(Character[] acharacter) {
        return acharacter != null && acharacter.length != 0 ? acharacter : ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY;
    }

    public static Byte[] nullToEmpty(Byte[] abyte) {
        return abyte != null && abyte.length != 0 ? abyte : ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY;
    }

    public static Double[] nullToEmpty(Double[] adouble) {
        return adouble != null && adouble.length != 0 ? adouble : ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY;
    }

    public static Float[] nullToEmpty(Float[] afloat) {
        return afloat != null && afloat.length != 0 ? afloat : ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY;
    }

    public static Boolean[] nullToEmpty(Boolean[] aboolean) {
        return aboolean != null && aboolean.length != 0 ? aboolean : ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY;
    }

    public static Object[] subarray(Object[] aobject, int i, int j) {
        if (aobject == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > aobject.length) {
                j = aobject.length;
            }

            int k = j - i;
            Class oclass = aobject.getClass().getComponentType();
            Object[] aobject1;

            if (k <= 0) {
                aobject1 = (Object[]) ((Object[]) Array.newInstance(oclass, 0));
                return aobject1;
            } else {
                aobject1 = (Object[]) ((Object[]) Array.newInstance(oclass, k));
                System.arraycopy(aobject, i, aobject1, 0, k);
                return aobject1;
            }
        }
    }

    public static long[] subarray(long[] along, int i, int j) {
        if (along == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > along.length) {
                j = along.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_LONG_ARRAY;
            } else {
                long[] along1 = new long[k];

                System.arraycopy(along, i, along1, 0, k);
                return along1;
            }
        }
    }

    public static int[] subarray(int[] aint, int i, int j) {
        if (aint == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > aint.length) {
                j = aint.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_INT_ARRAY;
            } else {
                int[] aint1 = new int[k];

                System.arraycopy(aint, i, aint1, 0, k);
                return aint1;
            }
        }
    }

    public static short[] subarray(short[] ashort, int i, int j) {
        if (ashort == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > ashort.length) {
                j = ashort.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_SHORT_ARRAY;
            } else {
                short[] ashort1 = new short[k];

                System.arraycopy(ashort, i, ashort1, 0, k);
                return ashort1;
            }
        }
    }

    public static char[] subarray(char[] achar, int i, int j) {
        if (achar == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > achar.length) {
                j = achar.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_CHAR_ARRAY;
            } else {
                char[] achar1 = new char[k];

                System.arraycopy(achar, i, achar1, 0, k);
                return achar1;
            }
        }
    }

    public static byte[] subarray(byte[] abyte, int i, int j) {
        if (abyte == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > abyte.length) {
                j = abyte.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_BYTE_ARRAY;
            } else {
                byte[] abyte1 = new byte[k];

                System.arraycopy(abyte, i, abyte1, 0, k);
                return abyte1;
            }
        }
    }

    public static double[] subarray(double[] adouble, int i, int j) {
        if (adouble == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > adouble.length) {
                j = adouble.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_DOUBLE_ARRAY;
            } else {
                double[] adouble1 = new double[k];

                System.arraycopy(adouble, i, adouble1, 0, k);
                return adouble1;
            }
        }
    }

    public static float[] subarray(float[] afloat, int i, int j) {
        if (afloat == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > afloat.length) {
                j = afloat.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_FLOAT_ARRAY;
            } else {
                float[] afloat1 = new float[k];

                System.arraycopy(afloat, i, afloat1, 0, k);
                return afloat1;
            }
        }
    }

    public static boolean[] subarray(boolean[] aboolean, int i, int j) {
        if (aboolean == null) {
            return null;
        } else {
            if (i < 0) {
                i = 0;
            }

            if (j > aboolean.length) {
                j = aboolean.length;
            }

            int k = j - i;

            if (k <= 0) {
                return ArrayUtils.EMPTY_BOOLEAN_ARRAY;
            } else {
                boolean[] aboolean1 = new boolean[k];

                System.arraycopy(aboolean, i, aboolean1, 0, k);
                return aboolean1;
            }
        }
    }

    public static boolean isSameLength(Object[] aobject, Object[] aobject1) {
        return (aobject != null || aobject1 == null || aobject1.length <= 0) && (aobject1 != null || aobject == null || aobject.length <= 0) && (aobject == null || aobject1 == null || aobject.length == aobject1.length);
    }

    public static boolean isSameLength(long[] along, long[] along1) {
        return (along != null || along1 == null || along1.length <= 0) && (along1 != null || along == null || along.length <= 0) && (along == null || along1 == null || along.length == along1.length);
    }

    public static boolean isSameLength(int[] aint, int[] aint1) {
        return (aint != null || aint1 == null || aint1.length <= 0) && (aint1 != null || aint == null || aint.length <= 0) && (aint == null || aint1 == null || aint.length == aint1.length);
    }

    public static boolean isSameLength(short[] ashort, short[] ashort1) {
        return (ashort != null || ashort1 == null || ashort1.length <= 0) && (ashort1 != null || ashort == null || ashort.length <= 0) && (ashort == null || ashort1 == null || ashort.length == ashort1.length);
    }

    public static boolean isSameLength(char[] achar, char[] achar1) {
        return (achar != null || achar1 == null || achar1.length <= 0) && (achar1 != null || achar == null || achar.length <= 0) && (achar == null || achar1 == null || achar.length == achar1.length);
    }

    public static boolean isSameLength(byte[] abyte, byte[] abyte1) {
        return (abyte != null || abyte1 == null || abyte1.length <= 0) && (abyte1 != null || abyte == null || abyte.length <= 0) && (abyte == null || abyte1 == null || abyte.length == abyte1.length);
    }

    public static boolean isSameLength(double[] adouble, double[] adouble1) {
        return (adouble != null || adouble1 == null || adouble1.length <= 0) && (adouble1 != null || adouble == null || adouble.length <= 0) && (adouble == null || adouble1 == null || adouble.length == adouble1.length);
    }

    public static boolean isSameLength(float[] afloat, float[] afloat1) {
        return (afloat != null || afloat1 == null || afloat1.length <= 0) && (afloat1 != null || afloat == null || afloat.length <= 0) && (afloat == null || afloat1 == null || afloat.length == afloat1.length);
    }

    public static boolean isSameLength(boolean[] aboolean, boolean[] aboolean1) {
        return (aboolean != null || aboolean1 == null || aboolean1.length <= 0) && (aboolean1 != null || aboolean == null || aboolean.length <= 0) && (aboolean == null || aboolean1 == null || aboolean.length == aboolean1.length);
    }

    public static int getLength(Object object) {
        return object == null ? 0 : Array.getLength(object);
    }

    public static boolean isSameType(Object object, Object object1) {
        if (object != null && object1 != null) {
            return object.getClass().getName().equals(object1.getClass().getName());
        } else {
            throw new IllegalArgumentException("The Array must not be null");
        }
    }

    public static void reverse(Object[] aobject) {
        if (aobject != null) {
            reverse(aobject, 0, aobject.length);
        }
    }

    public static void reverse(long[] along) {
        if (along != null) {
            reverse(along, 0, along.length);
        }
    }

    public static void reverse(int[] aint) {
        if (aint != null) {
            reverse(aint, 0, aint.length);
        }
    }

    public static void reverse(short[] ashort) {
        if (ashort != null) {
            reverse(ashort, 0, ashort.length);
        }
    }

    public static void reverse(char[] achar) {
        if (achar != null) {
            reverse(achar, 0, achar.length);
        }
    }

    public static void reverse(byte[] abyte) {
        if (abyte != null) {
            reverse(abyte, 0, abyte.length);
        }
    }

    public static void reverse(double[] adouble) {
        if (adouble != null) {
            reverse(adouble, 0, adouble.length);
        }
    }

    public static void reverse(float[] afloat) {
        if (afloat != null) {
            reverse(afloat, 0, afloat.length);
        }
    }

    public static void reverse(boolean[] aboolean) {
        if (aboolean != null) {
            reverse(aboolean, 0, aboolean.length);
        }
    }

    public static void reverse(boolean[] aboolean, int i, int j) {
        if (aboolean != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(aboolean.length, j) - 1; l > k; ++k) {
                boolean flag = aboolean[l];

                aboolean[l] = aboolean[k];
                aboolean[k] = flag;
                --l;
            }

        }
    }

    public static void reverse(byte[] abyte, int i, int j) {
        if (abyte != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(abyte.length, j) - 1; l > k; ++k) {
                byte b0 = abyte[l];

                abyte[l] = abyte[k];
                abyte[k] = b0;
                --l;
            }

        }
    }

    public static void reverse(char[] achar, int i, int j) {
        if (achar != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(achar.length, j) - 1; l > k; ++k) {
                char c0 = achar[l];

                achar[l] = achar[k];
                achar[k] = c0;
                --l;
            }

        }
    }

    public static void reverse(double[] adouble, int i, int j) {
        if (adouble != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(adouble.length, j) - 1; l > k; ++k) {
                double d0 = adouble[l];

                adouble[l] = adouble[k];
                adouble[k] = d0;
                --l;
            }

        }
    }

    public static void reverse(float[] afloat, int i, int j) {
        if (afloat != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(afloat.length, j) - 1; l > k; ++k) {
                float f = afloat[l];

                afloat[l] = afloat[k];
                afloat[k] = f;
                --l;
            }

        }
    }

    public static void reverse(int[] aint, int i, int j) {
        if (aint != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(aint.length, j) - 1; l > k; ++k) {
                int i1 = aint[l];

                aint[l] = aint[k];
                aint[k] = i1;
                --l;
            }

        }
    }

    public static void reverse(long[] along, int i, int j) {
        if (along != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(along.length, j) - 1; l > k; ++k) {
                long i1 = along[l];

                along[l] = along[k];
                along[k] = i1;
                --l;
            }

        }
    }

    public static void reverse(Object[] aobject, int i, int j) {
        if (aobject != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(aobject.length, j) - 1; l > k; ++k) {
                Object object = aobject[l];

                aobject[l] = aobject[k];
                aobject[k] = object;
                --l;
            }

        }
    }

    public static void reverse(short[] ashort, int i, int j) {
        if (ashort != null) {
            int k = i < 0 ? 0 : i;

            for (int l = Math.min(ashort.length, j) - 1; l > k; ++k) {
                short short0 = ashort[l];

                ashort[l] = ashort[k];
                ashort[k] = short0;
                --l;
            }

        }
    }

    public static int indexOf(Object[] aobject, Object object) {
        return indexOf(aobject, object, 0);
    }

    public static int indexOf(Object[] aobject, Object object, int i) {
        if (aobject == null) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            int j;

            if (object == null) {
                for (j = i; j < aobject.length; ++j) {
                    if (aobject[j] == null) {
                        return j;
                    }
                }
            } else if (aobject.getClass().getComponentType().isInstance(object)) {
                for (j = i; j < aobject.length; ++j) {
                    if (object.equals(aobject[j])) {
                        return j;
                    }
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(Object[] aobject, Object object) {
        return lastIndexOf(aobject, object, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(Object[] aobject, Object object, int i) {
        if (aobject == null) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= aobject.length) {
                i = aobject.length - 1;
            }

            int j;

            if (object == null) {
                for (j = i; j >= 0; --j) {
                    if (aobject[j] == null) {
                        return j;
                    }
                }
            } else if (aobject.getClass().getComponentType().isInstance(object)) {
                for (j = i; j >= 0; --j) {
                    if (object.equals(aobject[j])) {
                        return j;
                    }
                }
            }

            return -1;
        }
    }

    public static boolean contains(Object[] aobject, Object object) {
        return indexOf(aobject, object) != -1;
    }

    public static int indexOf(long[] along, long i) {
        return indexOf(along, i, 0);
    }

    public static int indexOf(long[] along, long i, int j) {
        if (along == null) {
            return -1;
        } else {
            if (j < 0) {
                j = 0;
            }

            for (int k = j; k < along.length; ++k) {
                if (i == along[k]) {
                    return k;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(long[] along, long i) {
        return lastIndexOf(along, i, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(long[] along, long i, int j) {
        if (along == null) {
            return -1;
        } else if (j < 0) {
            return -1;
        } else {
            if (j >= along.length) {
                j = along.length - 1;
            }

            for (int k = j; k >= 0; --k) {
                if (i == along[k]) {
                    return k;
                }
            }

            return -1;
        }
    }

    public static boolean contains(long[] along, long i) {
        return indexOf(along, i) != -1;
    }

    public static int indexOf(int[] aint, int i) {
        return indexOf(aint, i, 0);
    }

    public static int indexOf(int[] aint, int i, int j) {
        if (aint == null) {
            return -1;
        } else {
            if (j < 0) {
                j = 0;
            }

            for (int k = j; k < aint.length; ++k) {
                if (i == aint[k]) {
                    return k;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(int[] aint, int i) {
        return lastIndexOf(aint, i, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(int[] aint, int i, int j) {
        if (aint == null) {
            return -1;
        } else if (j < 0) {
            return -1;
        } else {
            if (j >= aint.length) {
                j = aint.length - 1;
            }

            for (int k = j; k >= 0; --k) {
                if (i == aint[k]) {
                    return k;
                }
            }

            return -1;
        }
    }

    public static boolean contains(int[] aint, int i) {
        return indexOf(aint, i) != -1;
    }

    public static int indexOf(short[] ashort, short short0) {
        return indexOf(ashort, short0, 0);
    }

    public static int indexOf(short[] ashort, short short0, int i) {
        if (ashort == null) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            for (int j = i; j < ashort.length; ++j) {
                if (short0 == ashort[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(short[] ashort, short short0) {
        return lastIndexOf(ashort, short0, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(short[] ashort, short short0, int i) {
        if (ashort == null) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= ashort.length) {
                i = ashort.length - 1;
            }

            for (int j = i; j >= 0; --j) {
                if (short0 == ashort[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static boolean contains(short[] ashort, short short0) {
        return indexOf(ashort, short0) != -1;
    }

    public static int indexOf(char[] achar, char c0) {
        return indexOf(achar, c0, 0);
    }

    public static int indexOf(char[] achar, char c0, int i) {
        if (achar == null) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            for (int j = i; j < achar.length; ++j) {
                if (c0 == achar[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(char[] achar, char c0) {
        return lastIndexOf(achar, c0, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(char[] achar, char c0, int i) {
        if (achar == null) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= achar.length) {
                i = achar.length - 1;
            }

            for (int j = i; j >= 0; --j) {
                if (c0 == achar[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static boolean contains(char[] achar, char c0) {
        return indexOf(achar, c0) != -1;
    }

    public static int indexOf(byte[] abyte, byte b0) {
        return indexOf(abyte, b0, 0);
    }

    public static int indexOf(byte[] abyte, byte b0, int i) {
        if (abyte == null) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            for (int j = i; j < abyte.length; ++j) {
                if (b0 == abyte[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(byte[] abyte, byte b0) {
        return lastIndexOf(abyte, b0, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(byte[] abyte, byte b0, int i) {
        if (abyte == null) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= abyte.length) {
                i = abyte.length - 1;
            }

            for (int j = i; j >= 0; --j) {
                if (b0 == abyte[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static boolean contains(byte[] abyte, byte b0) {
        return indexOf(abyte, b0) != -1;
    }

    public static int indexOf(double[] adouble, double d0) {
        return indexOf(adouble, d0, 0);
    }

    public static int indexOf(double[] adouble, double d0, double d1) {
        return indexOf(adouble, d0, 0, d1);
    }

    public static int indexOf(double[] adouble, double d0, int i) {
        if (isEmpty(adouble)) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            for (int j = i; j < adouble.length; ++j) {
                if (d0 == adouble[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int indexOf(double[] adouble, double d0, int i, double d1) {
        if (isEmpty(adouble)) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            double d2 = d0 - d1;
            double d3 = d0 + d1;

            for (int j = i; j < adouble.length; ++j) {
                if (adouble[j] >= d2 && adouble[j] <= d3) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(double[] adouble, double d0) {
        return lastIndexOf(adouble, d0, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(double[] adouble, double d0, double d1) {
        return lastIndexOf(adouble, d0, Integer.MAX_VALUE, d1);
    }

    public static int lastIndexOf(double[] adouble, double d0, int i) {
        if (isEmpty(adouble)) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= adouble.length) {
                i = adouble.length - 1;
            }

            for (int j = i; j >= 0; --j) {
                if (d0 == adouble[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(double[] adouble, double d0, int i, double d1) {
        if (isEmpty(adouble)) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= adouble.length) {
                i = adouble.length - 1;
            }

            double d2 = d0 - d1;
            double d3 = d0 + d1;

            for (int j = i; j >= 0; --j) {
                if (adouble[j] >= d2 && adouble[j] <= d3) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static boolean contains(double[] adouble, double d0) {
        return indexOf(adouble, d0) != -1;
    }

    public static boolean contains(double[] adouble, double d0, double d1) {
        return indexOf(adouble, d0, 0, d1) != -1;
    }

    public static int indexOf(float[] afloat, float f) {
        return indexOf(afloat, f, 0);
    }

    public static int indexOf(float[] afloat, float f, int i) {
        if (isEmpty(afloat)) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            for (int j = i; j < afloat.length; ++j) {
                if (f == afloat[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(float[] afloat, float f) {
        return lastIndexOf(afloat, f, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(float[] afloat, float f, int i) {
        if (isEmpty(afloat)) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= afloat.length) {
                i = afloat.length - 1;
            }

            for (int j = i; j >= 0; --j) {
                if (f == afloat[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static boolean contains(float[] afloat, float f) {
        return indexOf(afloat, f) != -1;
    }

    public static int indexOf(boolean[] aboolean, boolean flag) {
        return indexOf(aboolean, flag, 0);
    }

    public static int indexOf(boolean[] aboolean, boolean flag, int i) {
        if (isEmpty(aboolean)) {
            return -1;
        } else {
            if (i < 0) {
                i = 0;
            }

            for (int j = i; j < aboolean.length; ++j) {
                if (flag == aboolean[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(boolean[] aboolean, boolean flag) {
        return lastIndexOf(aboolean, flag, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(boolean[] aboolean, boolean flag, int i) {
        if (isEmpty(aboolean)) {
            return -1;
        } else if (i < 0) {
            return -1;
        } else {
            if (i >= aboolean.length) {
                i = aboolean.length - 1;
            }

            for (int j = i; j >= 0; --j) {
                if (flag == aboolean[j]) {
                    return j;
                }
            }

            return -1;
        }
    }

    public static boolean contains(boolean[] aboolean, boolean flag) {
        return indexOf(aboolean, flag) != -1;
    }

    public static char[] toPrimitive(Character[] acharacter) {
        if (acharacter == null) {
            return null;
        } else if (acharacter.length == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        } else {
            char[] achar = new char[acharacter.length];

            for (int i = 0; i < acharacter.length; ++i) {
                achar[i] = acharacter[i].charValue();
            }

            return achar;
        }
    }

    public static char[] toPrimitive(Character[] acharacter, char c0) {
        if (acharacter == null) {
            return null;
        } else if (acharacter.length == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        } else {
            char[] achar = new char[acharacter.length];

            for (int i = 0; i < acharacter.length; ++i) {
                Character character = acharacter[i];

                achar[i] = character == null ? c0 : character.charValue();
            }

            return achar;
        }
    }

    public static Character[] toObject(char[] achar) {
        if (achar == null) {
            return null;
        } else if (achar.length == 0) {
            return ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY;
        } else {
            Character[] acharacter = new Character[achar.length];

            for (int i = 0; i < achar.length; ++i) {
                acharacter[i] = Character.valueOf(achar[i]);
            }

            return acharacter;
        }
    }

    public static long[] toPrimitive(Long[] along) {
        if (along == null) {
            return null;
        } else if (along.length == 0) {
            return ArrayUtils.EMPTY_LONG_ARRAY;
        } else {
            long[] along1 = new long[along.length];

            for (int i = 0; i < along.length; ++i) {
                along1[i] = along[i].longValue();
            }

            return along1;
        }
    }

    public static long[] toPrimitive(Long[] along, long i) {
        if (along == null) {
            return null;
        } else if (along.length == 0) {
            return ArrayUtils.EMPTY_LONG_ARRAY;
        } else {
            long[] along1 = new long[along.length];

            for (int j = 0; j < along.length; ++j) {
                Long olong = along[j];

                along1[j] = olong == null ? i : olong.longValue();
            }

            return along1;
        }
    }

    public static Long[] toObject(long[] along) {
        if (along == null) {
            return null;
        } else if (along.length == 0) {
            return ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;
        } else {
            Long[] along1 = new Long[along.length];

            for (int i = 0; i < along.length; ++i) {
                along1[i] = Long.valueOf(along[i]);
            }

            return along1;
        }
    }

    public static int[] toPrimitive(Integer[] ainteger) {
        if (ainteger == null) {
            return null;
        } else if (ainteger.length == 0) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        } else {
            int[] aint = new int[ainteger.length];

            for (int i = 0; i < ainteger.length; ++i) {
                aint[i] = ainteger[i].intValue();
            }

            return aint;
        }
    }

    public static int[] toPrimitive(Integer[] ainteger, int i) {
        if (ainteger == null) {
            return null;
        } else if (ainteger.length == 0) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        } else {
            int[] aint = new int[ainteger.length];

            for (int j = 0; j < ainteger.length; ++j) {
                Integer integer = ainteger[j];

                aint[j] = integer == null ? i : integer.intValue();
            }

            return aint;
        }
    }

    public static Integer[] toObject(int[] aint) {
        if (aint == null) {
            return null;
        } else if (aint.length == 0) {
            return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
        } else {
            Integer[] ainteger = new Integer[aint.length];

            for (int i = 0; i < aint.length; ++i) {
                ainteger[i] = Integer.valueOf(aint[i]);
            }

            return ainteger;
        }
    }

    public static short[] toPrimitive(Short[] ashort) {
        if (ashort == null) {
            return null;
        } else if (ashort.length == 0) {
            return ArrayUtils.EMPTY_SHORT_ARRAY;
        } else {
            short[] ashort1 = new short[ashort.length];

            for (int i = 0; i < ashort.length; ++i) {
                ashort1[i] = ashort[i].shortValue();
            }

            return ashort1;
        }
    }

    public static short[] toPrimitive(Short[] ashort, short short0) {
        if (ashort == null) {
            return null;
        } else if (ashort.length == 0) {
            return ArrayUtils.EMPTY_SHORT_ARRAY;
        } else {
            short[] ashort1 = new short[ashort.length];

            for (int i = 0; i < ashort.length; ++i) {
                Short oshort = ashort[i];

                ashort1[i] = oshort == null ? short0 : oshort.shortValue();
            }

            return ashort1;
        }
    }

    public static Short[] toObject(short[] ashort) {
        if (ashort == null) {
            return null;
        } else if (ashort.length == 0) {
            return ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY;
        } else {
            Short[] ashort1 = new Short[ashort.length];

            for (int i = 0; i < ashort.length; ++i) {
                ashort1[i] = Short.valueOf(ashort[i]);
            }

            return ashort1;
        }
    }

    public static byte[] toPrimitive(Byte[] abyte) {
        if (abyte == null) {
            return null;
        } else if (abyte.length == 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        } else {
            byte[] abyte1 = new byte[abyte.length];

            for (int i = 0; i < abyte.length; ++i) {
                abyte1[i] = abyte[i].byteValue();
            }

            return abyte1;
        }
    }

    public static byte[] toPrimitive(Byte[] abyte, byte b0) {
        if (abyte == null) {
            return null;
        } else if (abyte.length == 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        } else {
            byte[] abyte1 = new byte[abyte.length];

            for (int i = 0; i < abyte.length; ++i) {
                Byte obyte = abyte[i];

                abyte1[i] = obyte == null ? b0 : obyte.byteValue();
            }

            return abyte1;
        }
    }

    public static Byte[] toObject(byte[] abyte) {
        if (abyte == null) {
            return null;
        } else if (abyte.length == 0) {
            return ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY;
        } else {
            Byte[] abyte1 = new Byte[abyte.length];

            for (int i = 0; i < abyte.length; ++i) {
                abyte1[i] = Byte.valueOf(abyte[i]);
            }

            return abyte1;
        }
    }

    public static double[] toPrimitive(Double[] adouble) {
        if (adouble == null) {
            return null;
        } else if (adouble.length == 0) {
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        } else {
            double[] adouble1 = new double[adouble.length];

            for (int i = 0; i < adouble.length; ++i) {
                adouble1[i] = adouble[i].doubleValue();
            }

            return adouble1;
        }
    }

    public static double[] toPrimitive(Double[] adouble, double d0) {
        if (adouble == null) {
            return null;
        } else if (adouble.length == 0) {
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        } else {
            double[] adouble1 = new double[adouble.length];

            for (int i = 0; i < adouble.length; ++i) {
                Double double = adouble[i];

                adouble1[i] = double == null ? d0 : double.doubleValue();
            }

            return adouble1;
        }
    }

    public static Double[] toObject(double[] adouble) {
        if (adouble == null) {
            return null;
        } else if (adouble.length == 0) {
            return ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY;
        } else {
            Double[] adouble1 = new Double[adouble.length];

            for (int i = 0; i < adouble.length; ++i) {
                adouble1[i] = Double.valueOf(adouble[i]);
            }

            return adouble1;
        }
    }

    public static float[] toPrimitive(Float[] afloat) {
        if (afloat == null) {
            return null;
        } else if (afloat.length == 0) {
            return ArrayUtils.EMPTY_FLOAT_ARRAY;
        } else {
            float[] afloat1 = new float[afloat.length];

            for (int i = 0; i < afloat.length; ++i) {
                afloat1[i] = afloat[i].floatValue();
            }

            return afloat1;
        }
    }

    public static float[] toPrimitive(Float[] afloat, float f) {
        if (afloat == null) {
            return null;
        } else if (afloat.length == 0) {
            return ArrayUtils.EMPTY_FLOAT_ARRAY;
        } else {
            float[] afloat1 = new float[afloat.length];

            for (int i = 0; i < afloat.length; ++i) {
                Float float = afloat[i];

                afloat1[i] = float == null ? f : float.floatValue();
            }

            return afloat1;
        }
    }

    public static Float[] toObject(float[] afloat) {
        if (afloat == null) {
            return null;
        } else if (afloat.length == 0) {
            return ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY;
        } else {
            Float[] afloat1 = new Float[afloat.length];

            for (int i = 0; i < afloat.length; ++i) {
                afloat1[i] = Float.valueOf(afloat[i]);
            }

            return afloat1;
        }
    }

    public static boolean[] toPrimitive(Boolean[] aboolean) {
        if (aboolean == null) {
            return null;
        } else if (aboolean.length == 0) {
            return ArrayUtils.EMPTY_BOOLEAN_ARRAY;
        } else {
            boolean[] aboolean1 = new boolean[aboolean.length];

            for (int i = 0; i < aboolean.length; ++i) {
                aboolean1[i] = aboolean[i].booleanValue();
            }

            return aboolean1;
        }
    }

    public static boolean[] toPrimitive(Boolean[] aboolean, boolean flag) {
        if (aboolean == null) {
            return null;
        } else if (aboolean.length == 0) {
            return ArrayUtils.EMPTY_BOOLEAN_ARRAY;
        } else {
            boolean[] aboolean1 = new boolean[aboolean.length];

            for (int i = 0; i < aboolean.length; ++i) {
                Boolean obool = aboolean[i];

                aboolean1[i] = obool == null ? flag : obool.booleanValue();
            }

            return aboolean1;
        }
    }

    public static Boolean[] toObject(boolean[] aboolean) {
        if (aboolean == null) {
            return null;
        } else if (aboolean.length == 0) {
            return ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY;
        } else {
            Boolean[] aboolean1 = new Boolean[aboolean.length];

            for (int i = 0; i < aboolean.length; ++i) {
                aboolean1[i] = aboolean[i] ? Boolean.TRUE : Boolean.FALSE;
            }

            return aboolean1;
        }
    }

    public static boolean isEmpty(Object[] aobject) {
        return aobject == null || aobject.length == 0;
    }

    public static boolean isEmpty(long[] along) {
        return along == null || along.length == 0;
    }

    public static boolean isEmpty(int[] aint) {
        return aint == null || aint.length == 0;
    }

    public static boolean isEmpty(short[] ashort) {
        return ashort == null || ashort.length == 0;
    }

    public static boolean isEmpty(char[] achar) {
        return achar == null || achar.length == 0;
    }

    public static boolean isEmpty(byte[] abyte) {
        return abyte == null || abyte.length == 0;
    }

    public static boolean isEmpty(double[] adouble) {
        return adouble == null || adouble.length == 0;
    }

    public static boolean isEmpty(float[] afloat) {
        return afloat == null || afloat.length == 0;
    }

    public static boolean isEmpty(boolean[] aboolean) {
        return aboolean == null || aboolean.length == 0;
    }

    public static boolean isNotEmpty(Object[] aobject) {
        return aobject != null && aobject.length != 0;
    }

    public static boolean isNotEmpty(long[] along) {
        return along != null && along.length != 0;
    }

    public static boolean isNotEmpty(int[] aint) {
        return aint != null && aint.length != 0;
    }

    public static boolean isNotEmpty(short[] ashort) {
        return ashort != null && ashort.length != 0;
    }

    public static boolean isNotEmpty(char[] achar) {
        return achar != null && achar.length != 0;
    }

    public static boolean isNotEmpty(byte[] abyte) {
        return abyte != null && abyte.length != 0;
    }

    public static boolean isNotEmpty(double[] adouble) {
        return adouble != null && adouble.length != 0;
    }

    public static boolean isNotEmpty(float[] afloat) {
        return afloat != null && afloat.length != 0;
    }

    public static boolean isNotEmpty(boolean[] aboolean) {
        return aboolean != null && aboolean.length != 0;
    }

    public static Object[] addAll(Object[] aobject, Object... aobject1) {
        if (aobject == null) {
            return clone(aobject1);
        } else if (aobject1 == null) {
            return clone(aobject);
        } else {
            Class oclass = aobject.getClass().getComponentType();
            Object[] aobject2 = (Object[]) ((Object[]) Array.newInstance(oclass, aobject.length + aobject1.length));

            System.arraycopy(aobject, 0, aobject2, 0, aobject.length);

            try {
                System.arraycopy(aobject1, 0, aobject2, aobject.length, aobject1.length);
                return aobject2;
            } catch (ArrayStoreException arraystoreexception) {
                Class oclass1 = aobject1.getClass().getComponentType();

                if (!oclass.isAssignableFrom(oclass1)) {
                    throw new IllegalArgumentException("Cannot store " + oclass1.getName() + " in an array of " + oclass.getName(), arraystoreexception);
                } else {
                    throw arraystoreexception;
                }
            }
        }
    }

    public static boolean[] addAll(boolean[] aboolean, boolean... aboolean1) {
        if (aboolean == null) {
            return clone(aboolean1);
        } else if (aboolean1 == null) {
            return clone(aboolean);
        } else {
            boolean[] aboolean2 = new boolean[aboolean.length + aboolean1.length];

            System.arraycopy(aboolean, 0, aboolean2, 0, aboolean.length);
            System.arraycopy(aboolean1, 0, aboolean2, aboolean.length, aboolean1.length);
            return aboolean2;
        }
    }

    public static char[] addAll(char[] achar, char... achar1) {
        if (achar == null) {
            return clone(achar1);
        } else if (achar1 == null) {
            return clone(achar);
        } else {
            char[] achar2 = new char[achar.length + achar1.length];

            System.arraycopy(achar, 0, achar2, 0, achar.length);
            System.arraycopy(achar1, 0, achar2, achar.length, achar1.length);
            return achar2;
        }
    }

    public static byte[] addAll(byte[] abyte, byte... abyte1) {
        if (abyte == null) {
            return clone(abyte1);
        } else if (abyte1 == null) {
            return clone(abyte);
        } else {
            byte[] abyte2 = new byte[abyte.length + abyte1.length];

            System.arraycopy(abyte, 0, abyte2, 0, abyte.length);
            System.arraycopy(abyte1, 0, abyte2, abyte.length, abyte1.length);
            return abyte2;
        }
    }

    public static short[] addAll(short[] ashort, short... ashort1) {
        if (ashort == null) {
            return clone(ashort1);
        } else if (ashort1 == null) {
            return clone(ashort);
        } else {
            short[] ashort2 = new short[ashort.length + ashort1.length];

            System.arraycopy(ashort, 0, ashort2, 0, ashort.length);
            System.arraycopy(ashort1, 0, ashort2, ashort.length, ashort1.length);
            return ashort2;
        }
    }

    public static int[] addAll(int[] aint, int... aint1) {
        if (aint == null) {
            return clone(aint1);
        } else if (aint1 == null) {
            return clone(aint);
        } else {
            int[] aint2 = new int[aint.length + aint1.length];

            System.arraycopy(aint, 0, aint2, 0, aint.length);
            System.arraycopy(aint1, 0, aint2, aint.length, aint1.length);
            return aint2;
        }
    }

    public static long[] addAll(long[] along, long... along1) {
        if (along == null) {
            return clone(along1);
        } else if (along1 == null) {
            return clone(along);
        } else {
            long[] along2 = new long[along.length + along1.length];

            System.arraycopy(along, 0, along2, 0, along.length);
            System.arraycopy(along1, 0, along2, along.length, along1.length);
            return along2;
        }
    }

    public static float[] addAll(float[] afloat, float... afloat1) {
        if (afloat == null) {
            return clone(afloat1);
        } else if (afloat1 == null) {
            return clone(afloat);
        } else {
            float[] afloat2 = new float[afloat.length + afloat1.length];

            System.arraycopy(afloat, 0, afloat2, 0, afloat.length);
            System.arraycopy(afloat1, 0, afloat2, afloat.length, afloat1.length);
            return afloat2;
        }
    }

    public static double[] addAll(double[] adouble, double... adouble1) {
        if (adouble == null) {
            return clone(adouble1);
        } else if (adouble1 == null) {
            return clone(adouble);
        } else {
            double[] adouble2 = new double[adouble.length + adouble1.length];

            System.arraycopy(adouble, 0, adouble2, 0, adouble.length);
            System.arraycopy(adouble1, 0, adouble2, adouble.length, adouble1.length);
            return adouble2;
        }
    }

    public static Object[] add(Object[] aobject, Object object) {
        Class oclass;

        if (aobject != null) {
            oclass = aobject.getClass();
        } else {
            if (object == null) {
                throw new IllegalArgumentException("Arguments cannot both be null");
            }

            oclass = object.getClass();
        }

        Object[] aobject1 = (Object[]) ((Object[]) copyArrayGrow1(aobject, oclass));

        aobject1[aobject1.length - 1] = object;
        return aobject1;
    }

    public static boolean[] add(boolean[] aboolean, boolean flag) {
        boolean[] aboolean1 = (boolean[]) ((boolean[]) copyArrayGrow1(aboolean, Boolean.TYPE));

        aboolean1[aboolean1.length - 1] = flag;
        return aboolean1;
    }

    public static byte[] add(byte[] abyte, byte b0) {
        byte[] abyte1 = (byte[]) ((byte[]) copyArrayGrow1(abyte, Byte.TYPE));

        abyte1[abyte1.length - 1] = b0;
        return abyte1;
    }

    public static char[] add(char[] achar, char c0) {
        char[] achar1 = (char[]) ((char[]) copyArrayGrow1(achar, Character.TYPE));

        achar1[achar1.length - 1] = c0;
        return achar1;
    }

    public static double[] add(double[] adouble, double d0) {
        double[] adouble1 = (double[]) ((double[]) copyArrayGrow1(adouble, Double.TYPE));

        adouble1[adouble1.length - 1] = d0;
        return adouble1;
    }

    public static float[] add(float[] afloat, float f) {
        float[] afloat1 = (float[]) ((float[]) copyArrayGrow1(afloat, Float.TYPE));

        afloat1[afloat1.length - 1] = f;
        return afloat1;
    }

    public static int[] add(int[] aint, int i) {
        int[] aint1 = (int[]) ((int[]) copyArrayGrow1(aint, Integer.TYPE));

        aint1[aint1.length - 1] = i;
        return aint1;
    }

    public static long[] add(long[] along, long i) {
        long[] along1 = (long[]) ((long[]) copyArrayGrow1(along, Long.TYPE));

        along1[along1.length - 1] = i;
        return along1;
    }

    public static short[] add(short[] ashort, short short0) {
        short[] ashort1 = (short[]) ((short[]) copyArrayGrow1(ashort, Short.TYPE));

        ashort1[ashort1.length - 1] = short0;
        return ashort1;
    }

    private static Object copyArrayGrow1(Object object, Class oclass) {
        if (object != null) {
            int i = Array.getLength(object);
            Object object1 = Array.newInstance(object.getClass().getComponentType(), i + 1);

            System.arraycopy(object, 0, object1, 0, i);
            return object1;
        } else {
            return Array.newInstance(oclass, 1);
        }
    }

    public static Object[] add(Object[] aobject, int i, Object object) {
        Class oclass = null;

        if (aobject != null) {
            oclass = aobject.getClass().getComponentType();
        } else {
            if (object == null) {
                throw new IllegalArgumentException("Array and element cannot both be null");
            }

            oclass = object.getClass();
        }

        Object[] aobject1 = (Object[]) ((Object[]) add(aobject, i, object, oclass));

        return aobject1;
    }

    public static boolean[] add(boolean[] aboolean, int i, boolean flag) {
        return (boolean[]) ((boolean[]) add(aboolean, i, Boolean.valueOf(flag), Boolean.TYPE));
    }

    public static char[] add(char[] achar, int i, char c0) {
        return (char[]) ((char[]) add(achar, i, Character.valueOf(c0), Character.TYPE));
    }

    public static byte[] add(byte[] abyte, int i, byte b0) {
        return (byte[]) ((byte[]) add(abyte, i, Byte.valueOf(b0), Byte.TYPE));
    }

    public static short[] add(short[] ashort, int i, short short0) {
        return (short[]) ((short[]) add(ashort, i, Short.valueOf(short0), Short.TYPE));
    }

    public static int[] add(int[] aint, int i, int j) {
        return (int[]) ((int[]) add(aint, i, Integer.valueOf(j), Integer.TYPE));
    }

    public static long[] add(long[] along, int i, long j) {
        return (long[]) ((long[]) add(along, i, Long.valueOf(j), Long.TYPE));
    }

    public static float[] add(float[] afloat, int i, float f) {
        return (float[]) ((float[]) add(afloat, i, Float.valueOf(f), Float.TYPE));
    }

    public static double[] add(double[] adouble, int i, double d0) {
        return (double[]) ((double[]) add(adouble, i, Double.valueOf(d0), Double.TYPE));
    }

    private static Object add(Object object, int i, Object object1, Class oclass) {
        if (object == null) {
            if (i != 0) {
                throw new IndexOutOfBoundsException("Index: " + i + ", Length: 0");
            } else {
                Object object2 = Array.newInstance(oclass, 1);

                Array.set(object2, 0, object1);
                return object2;
            }
        } else {
            int j = Array.getLength(object);

            if (i <= j && i >= 0) {
                Object object3 = Array.newInstance(oclass, j + 1);

                System.arraycopy(object, 0, object3, 0, i);
                Array.set(object3, i, object1);
                if (i < j) {
                    System.arraycopy(object, i, object3, i + 1, j - i);
                }

                return object3;
            } else {
                throw new IndexOutOfBoundsException("Index: " + i + ", Length: " + j);
            }
        }
    }

    public static Object[] remove(Object[] aobject, int i) {
        return (Object[]) ((Object[]) remove((Object) aobject, i));
    }

    public static Object[] removeElement(Object[] aobject, Object object) {
        int i = indexOf(aobject, object);

        return i == -1 ? clone(aobject) : remove(aobject, i);
    }

    public static boolean[] remove(boolean[] aboolean, int i) {
        return (boolean[]) ((boolean[]) remove((Object) aboolean, i));
    }

    public static boolean[] removeElement(boolean[] aboolean, boolean flag) {
        int i = indexOf(aboolean, flag);

        return i == -1 ? clone(aboolean) : remove(aboolean, i);
    }

    public static byte[] remove(byte[] abyte, int i) {
        return (byte[]) ((byte[]) remove((Object) abyte, i));
    }

    public static byte[] removeElement(byte[] abyte, byte b0) {
        int i = indexOf(abyte, b0);

        return i == -1 ? clone(abyte) : remove(abyte, i);
    }

    public static char[] remove(char[] achar, int i) {
        return (char[]) ((char[]) remove((Object) achar, i));
    }

    public static char[] removeElement(char[] achar, char c0) {
        int i = indexOf(achar, c0);

        return i == -1 ? clone(achar) : remove(achar, i);
    }

    public static double[] remove(double[] adouble, int i) {
        return (double[]) ((double[]) remove((Object) adouble, i));
    }

    public static double[] removeElement(double[] adouble, double d0) {
        int i = indexOf(adouble, d0);

        return i == -1 ? clone(adouble) : remove(adouble, i);
    }

    public static float[] remove(float[] afloat, int i) {
        return (float[]) ((float[]) remove((Object) afloat, i));
    }

    public static float[] removeElement(float[] afloat, float f) {
        int i = indexOf(afloat, f);

        return i == -1 ? clone(afloat) : remove(afloat, i);
    }

    public static int[] remove(int[] aint, int i) {
        return (int[]) ((int[]) remove((Object) aint, i));
    }

    public static int[] removeElement(int[] aint, int i) {
        int j = indexOf(aint, i);

        return j == -1 ? clone(aint) : remove(aint, j);
    }

    public static long[] remove(long[] along, int i) {
        return (long[]) ((long[]) remove((Object) along, i));
    }

    public static long[] removeElement(long[] along, long i) {
        int j = indexOf(along, i);

        return j == -1 ? clone(along) : remove(along, j);
    }

    public static short[] remove(short[] ashort, int i) {
        return (short[]) ((short[]) remove((Object) ashort, i));
    }

    public static short[] removeElement(short[] ashort, short short0) {
        int i = indexOf(ashort, short0);

        return i == -1 ? clone(ashort) : remove(ashort, i);
    }

    private static Object remove(Object object, int i) {
        int j = getLength(object);

        if (i >= 0 && i < j) {
            Object object1 = Array.newInstance(object.getClass().getComponentType(), j - 1);

            System.arraycopy(object, 0, object1, 0, i);
            if (i < j - 1) {
                System.arraycopy(object, i + 1, object1, i, j - i - 1);
            }

            return object1;
        } else {
            throw new IndexOutOfBoundsException("Index: " + i + ", Length: " + j);
        }
    }

    public static Object[] removeAll(Object[] aobject, int... aint) {
        return (Object[]) ((Object[]) removeAll((Object) aobject, clone(aint)));
    }

    public static Object[] removeElements(Object[] aobject, Object... aobject1) {
        if (!isEmpty(aobject) && !isEmpty(aobject1)) {
            HashMap hashmap = new HashMap(aobject1.length);
            Object[] aobject2 = aobject1;
            int i = aobject1.length;

            Object object;

            for (int j = 0; j < i; ++j) {
                object = aobject2[j];
                MutableInt mutableint = (MutableInt) hashmap.get(object);

                if (mutableint == null) {
                    hashmap.put(object, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                object = entry.getKey();
                int k = 0;
                int l = 0;

                for (int i1 = ((MutableInt) entry.getValue()).intValue(); l < i1; ++l) {
                    k = indexOf(aobject, object, k);
                    if (k < 0) {
                        break;
                    }

                    bitset.set(k++);
                }
            }

            Object[] aobject3 = (Object[]) ((Object[]) removeAll((Object) aobject, bitset));

            return aobject3;
        } else {
            return clone(aobject);
        }
    }

    public static byte[] removeAll(byte[] abyte, int... aint) {
        return (byte[]) ((byte[]) removeAll((Object) abyte, clone(aint)));
    }

    public static byte[] removeElements(byte[] abyte, byte... abyte1) {
        if (!isEmpty(abyte) && !isEmpty(abyte1)) {
            HashMap hashmap = new HashMap(abyte1.length);
            byte[] abyte2 = abyte1;
            int i = abyte1.length;

            for (int j = 0; j < i; ++j) {
                byte b0 = abyte2[j];
                Byte obyte = Byte.valueOf(b0);
                MutableInt mutableint = (MutableInt) hashmap.get(obyte);

                if (mutableint == null) {
                    hashmap.put(obyte, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Byte obyte1 = (Byte) entry.getKey();
                int k = 0;
                int l = 0;

                for (int i1 = ((MutableInt) entry.getValue()).intValue(); l < i1; ++l) {
                    k = indexOf(abyte, obyte1.byteValue(), k);
                    if (k < 0) {
                        break;
                    }

                    bitset.set(k++);
                }
            }

            return (byte[]) ((byte[]) removeAll((Object) abyte, bitset));
        } else {
            return clone(abyte);
        }
    }

    public static short[] removeAll(short[] ashort, int... aint) {
        return (short[]) ((short[]) removeAll((Object) ashort, clone(aint)));
    }

    public static short[] removeElements(short[] ashort, short... ashort1) {
        if (!isEmpty(ashort) && !isEmpty(ashort1)) {
            HashMap hashmap = new HashMap(ashort1.length);
            short[] ashort2 = ashort1;
            int i = ashort1.length;

            for (int j = 0; j < i; ++j) {
                short short0 = ashort2[j];
                Short oshort = Short.valueOf(short0);
                MutableInt mutableint = (MutableInt) hashmap.get(oshort);

                if (mutableint == null) {
                    hashmap.put(oshort, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Short oshort1 = (Short) entry.getKey();
                int k = 0;
                int l = 0;

                for (int i1 = ((MutableInt) entry.getValue()).intValue(); l < i1; ++l) {
                    k = indexOf(ashort, oshort1.shortValue(), k);
                    if (k < 0) {
                        break;
                    }

                    bitset.set(k++);
                }
            }

            return (short[]) ((short[]) removeAll((Object) ashort, bitset));
        } else {
            return clone(ashort);
        }
    }

    public static int[] removeAll(int[] aint, int... aint1) {
        return (int[]) ((int[]) removeAll((Object) aint, clone(aint1)));
    }

    public static int[] removeElements(int[] aint, int... aint1) {
        if (!isEmpty(aint) && !isEmpty(aint1)) {
            HashMap hashmap = new HashMap(aint1.length);
            int[] aint2 = aint1;
            int i = aint1.length;

            for (int j = 0; j < i; ++j) {
                int k = aint2[j];
                Integer integer = Integer.valueOf(k);
                MutableInt mutableint = (MutableInt) hashmap.get(integer);

                if (mutableint == null) {
                    hashmap.put(integer, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Integer integer1 = (Integer) entry.getKey();
                int l = 0;
                int i1 = 0;

                for (int j1 = ((MutableInt) entry.getValue()).intValue(); i1 < j1; ++i1) {
                    l = indexOf(aint, integer1.intValue(), l);
                    if (l < 0) {
                        break;
                    }

                    bitset.set(l++);
                }
            }

            return (int[]) ((int[]) removeAll((Object) aint, bitset));
        } else {
            return clone(aint);
        }
    }

    public static char[] removeAll(char[] achar, int... aint) {
        return (char[]) ((char[]) removeAll((Object) achar, clone(aint)));
    }

    public static char[] removeElements(char[] achar, char... achar1) {
        if (!isEmpty(achar) && !isEmpty(achar1)) {
            HashMap hashmap = new HashMap(achar1.length);
            char[] achar2 = achar1;
            int i = achar1.length;

            for (int j = 0; j < i; ++j) {
                char c0 = achar2[j];
                Character character = Character.valueOf(c0);
                MutableInt mutableint = (MutableInt) hashmap.get(character);

                if (mutableint == null) {
                    hashmap.put(character, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Character character1 = (Character) entry.getKey();
                int k = 0;
                int l = 0;

                for (int i1 = ((MutableInt) entry.getValue()).intValue(); l < i1; ++l) {
                    k = indexOf(achar, character1.charValue(), k);
                    if (k < 0) {
                        break;
                    }

                    bitset.set(k++);
                }
            }

            return (char[]) ((char[]) removeAll((Object) achar, bitset));
        } else {
            return clone(achar);
        }
    }

    public static long[] removeAll(long[] along, int... aint) {
        return (long[]) ((long[]) removeAll((Object) along, clone(aint)));
    }

    public static long[] removeElements(long[] along, long... along1) {
        if (!isEmpty(along) && !isEmpty(along1)) {
            HashMap hashmap = new HashMap(along1.length);
            long[] along2 = along1;
            int i = along1.length;

            for (int j = 0; j < i; ++j) {
                long k = along2[j];
                Long olong = Long.valueOf(k);
                MutableInt mutableint = (MutableInt) hashmap.get(olong);

                if (mutableint == null) {
                    hashmap.put(olong, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Long olong1 = (Long) entry.getKey();
                int l = 0;
                int i1 = 0;

                for (int j1 = ((MutableInt) entry.getValue()).intValue(); i1 < j1; ++i1) {
                    l = indexOf(along, olong1.longValue(), l);
                    if (l < 0) {
                        break;
                    }

                    bitset.set(l++);
                }
            }

            return (long[]) ((long[]) removeAll((Object) along, bitset));
        } else {
            return clone(along);
        }
    }

    public static float[] removeAll(float[] afloat, int... aint) {
        return (float[]) ((float[]) removeAll((Object) afloat, clone(aint)));
    }

    public static float[] removeElements(float[] afloat, float... afloat1) {
        if (!isEmpty(afloat) && !isEmpty(afloat1)) {
            HashMap hashmap = new HashMap(afloat1.length);
            float[] afloat2 = afloat1;
            int i = afloat1.length;

            for (int j = 0; j < i; ++j) {
                float f = afloat2[j];
                Float float = Float.valueOf(f);
                MutableInt mutableint = (MutableInt) hashmap.get(float);

                if (mutableint == null) {
                    hashmap.put(float, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Float float1 = (Float) entry.getKey();
                int k = 0;
                int l = 0;

                for (int i1 = ((MutableInt) entry.getValue()).intValue(); l < i1; ++l) {
                    k = indexOf(afloat, float1.floatValue(), k);
                    if (k < 0) {
                        break;
                    }

                    bitset.set(k++);
                }
            }

            return (float[]) ((float[]) removeAll((Object) afloat, bitset));
        } else {
            return clone(afloat);
        }
    }

    public static double[] removeAll(double[] adouble, int... aint) {
        return (double[]) ((double[]) removeAll((Object) adouble, clone(aint)));
    }

    public static double[] removeElements(double[] adouble, double... adouble1) {
        if (!isEmpty(adouble) && !isEmpty(adouble1)) {
            HashMap hashmap = new HashMap(adouble1.length);
            double[] adouble2 = adouble1;
            int i = adouble1.length;

            for (int j = 0; j < i; ++j) {
                double d0 = adouble2[j];
                Double double = Double.valueOf(d0);
                MutableInt mutableint = (MutableInt) hashmap.get(double);

                if (mutableint == null) {
                    hashmap.put(double, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Double double1 = (Double) entry.getKey();
                int k = 0;
                int l = 0;

                for (int i1 = ((MutableInt) entry.getValue()).intValue(); l < i1; ++l) {
                    k = indexOf(adouble, double1.doubleValue(), k);
                    if (k < 0) {
                        break;
                    }

                    bitset.set(k++);
                }
            }

            return (double[]) ((double[]) removeAll((Object) adouble, bitset));
        } else {
            return clone(adouble);
        }
    }

    public static boolean[] removeAll(boolean[] aboolean, int... aint) {
        return (boolean[]) ((boolean[]) removeAll((Object) aboolean, clone(aint)));
    }

    public static boolean[] removeElements(boolean[] aboolean, boolean... aboolean1) {
        if (!isEmpty(aboolean) && !isEmpty(aboolean1)) {
            HashMap hashmap = new HashMap(2);
            boolean[] aboolean2 = aboolean1;
            int i = aboolean1.length;

            for (int j = 0; j < i; ++j) {
                boolean flag = aboolean2[j];
                Boolean obool = Boolean.valueOf(flag);
                MutableInt mutableint = (MutableInt) hashmap.get(obool);

                if (mutableint == null) {
                    hashmap.put(obool, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            BitSet bitset = new BitSet();
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Boolean obool1 = (Boolean) entry.getKey();
                int k = 0;
                int l = 0;

                for (int i1 = ((MutableInt) entry.getValue()).intValue(); l < i1; ++l) {
                    k = indexOf(aboolean, obool1.booleanValue(), k);
                    if (k < 0) {
                        break;
                    }

                    bitset.set(k++);
                }
            }

            return (boolean[]) ((boolean[]) removeAll((Object) aboolean, bitset));
        } else {
            return clone(aboolean);
        }
    }

    static Object removeAll(Object object, int... aint) {
        int i = getLength(object);
        int j = 0;
        int k;
        int l;

        if (isNotEmpty(aint)) {
            Arrays.sort(aint);
            int i1 = aint.length;

            k = i;

            while (true) {
                --i1;
                if (i1 < 0) {
                    break;
                }

                l = aint[i1];
                if (l < 0 || l >= i) {
                    throw new IndexOutOfBoundsException("Index: " + l + ", Length: " + i);
                }

                if (l < k) {
                    ++j;
                    k = l;
                }
            }
        }

        Object object1 = Array.newInstance(object.getClass().getComponentType(), i - j);

        if (j < i) {
            k = i;
            l = i - j;

            for (int j1 = aint.length - 1; j1 >= 0; --j1) {
                int k1 = aint[j1];

                if (k - k1 > 1) {
                    int l1 = k - k1 - 1;

                    l -= l1;
                    System.arraycopy(object, k1 + 1, object1, l, l1);
                }

                k = k1;
            }

            if (k > 0) {
                System.arraycopy(object, 0, object1, 0, k);
            }
        }

        return object1;
    }

    static Object removeAll(Object object, BitSet bitset) {
        int i = getLength(object);
        int j = bitset.cardinality();
        Object object1 = Array.newInstance(object.getClass().getComponentType(), i - j);
        int k = 0;

        int l;
        int i1;
        int j1;

        for (l = 0; (i1 = bitset.nextSetBit(k)) != -1; k = bitset.nextClearBit(i1)) {
            j1 = i1 - k;
            if (j1 > 0) {
                System.arraycopy(object, k, object1, l, j1);
                l += j1;
            }
        }

        j1 = i - k;
        if (j1 > 0) {
            System.arraycopy(object, k, object1, l, j1);
        }

        return object1;
    }
}
