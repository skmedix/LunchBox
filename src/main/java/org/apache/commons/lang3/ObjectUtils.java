package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.text.StrBuilder;

public class ObjectUtils {

    public static final ObjectUtils.Null NULL = new ObjectUtils.Null();

    public static Object defaultIfNull(Object object, Object object1) {
        return object != null ? object : object1;
    }

    public static Object firstNonNull(Object... aobject) {
        if (aobject != null) {
            Object[] aobject1 = aobject;
            int i = aobject.length;

            for (int j = 0; j < i; ++j) {
                Object object = aobject1[j];

                if (object != null) {
                    return object;
                }
            }
        }

        return null;
    }

    /** @deprecated */
    @Deprecated
    public static boolean equals(Object object, Object object1) {
        return object == object1 ? true : (object != null && object1 != null ? object.equals(object1) : false);
    }

    public static boolean notEqual(Object object, Object object1) {
        return !equals(object, object1);
    }

    /** @deprecated */
    @Deprecated
    public static int hashCode(Object object) {
        return object == null ? 0 : object.hashCode();
    }

    public String toString() {
        return super.toString();
    }

    /** @deprecated */
    @Deprecated
    public static int hashCodeMulti(Object... aobject) {
        int i = 1;

        if (aobject != null) {
            Object[] aobject1 = aobject;
            int j = aobject.length;

            for (int k = 0; k < j; ++k) {
                Object object = aobject1[k];
                int l = hashCode(object);

                i = i * 31 + l;
            }
        }

        return i;
    }

    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            identityToString(stringbuilder, object);
            return stringbuilder.toString();
        }
    }

    public static void identityToString(Appendable appendable, Object object) throws IOException {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        } else {
            appendable.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
        }
    }

    public static void identityToString(StrBuilder strbuilder, Object object) {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        } else {
            strbuilder.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
        }
    }

    public static void identityToString(StringBuffer stringbuffer, Object object) {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        } else {
            stringbuffer.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
        }
    }

    public static void identityToString(StringBuilder stringbuilder, Object object) {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        } else {
            stringbuilder.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
        }
    }

    /** @deprecated */
    @Deprecated
    public static String toString(Object object) {
        return object == null ? "" : object.toString();
    }

    /** @deprecated */
    @Deprecated
    public static String toString(Object object, String s) {
        return object == null ? s : object.toString();
    }

    public static Comparable min(Comparable... acomparable) {
        Comparable comparable = null;

        if (acomparable != null) {
            Comparable[] acomparable1 = acomparable;
            int i = acomparable.length;

            for (int j = 0; j < i; ++j) {
                Comparable comparable1 = acomparable1[j];

                if (compare(comparable1, comparable, true) < 0) {
                    comparable = comparable1;
                }
            }
        }

        return comparable;
    }

    public static Comparable max(Comparable... acomparable) {
        Comparable comparable = null;

        if (acomparable != null) {
            Comparable[] acomparable1 = acomparable;
            int i = acomparable.length;

            for (int j = 0; j < i; ++j) {
                Comparable comparable1 = acomparable1[j];

                if (compare(comparable1, comparable, false) > 0) {
                    comparable = comparable1;
                }
            }
        }

        return comparable;
    }

    public static int compare(Comparable comparable, Comparable comparable1) {
        return compare(comparable, comparable1, false);
    }

    public static int compare(Comparable comparable, Comparable comparable1, boolean flag) {
        return comparable == comparable1 ? 0 : (comparable == null ? (flag ? 1 : -1) : (comparable1 == null ? (flag ? -1 : 1) : comparable.compareTo(comparable1)));
    }

    public static Comparable median(Comparable... acomparable) {
        Validate.notEmpty((Object[]) acomparable);
        Validate.noNullElements((Object[]) acomparable);
        TreeSet treeset = new TreeSet();

        Collections.addAll(treeset, acomparable);
        Comparable comparable = (Comparable) treeset.toArray()[(treeset.size() - 1) / 2];

        return comparable;
    }

    public static Object median(Comparator comparator, Object... aobject) {
        Validate.notEmpty(aobject, "null/empty items", new Object[0]);
        Validate.noNullElements(aobject);
        Validate.notNull(comparator, "null comparator", new Object[0]);
        TreeSet treeset = new TreeSet(comparator);

        Collections.addAll(treeset, aobject);
        Object object = treeset.toArray()[(treeset.size() - 1) / 2];

        return object;
    }

    public static Object mode(Object... aobject) {
        if (ArrayUtils.isNotEmpty(aobject)) {
            HashMap hashmap = new HashMap(aobject.length);
            Object[] aobject1 = aobject;
            int i = aobject.length;

            for (int j = 0; j < i; ++j) {
                Object object = aobject1[j];
                MutableInt mutableint = (MutableInt) hashmap.get(object);

                if (mutableint == null) {
                    hashmap.put(object, new MutableInt(1));
                } else {
                    mutableint.increment();
                }
            }

            Object object1 = null;

            i = 0;
            Iterator iterator = hashmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                int k = ((MutableInt) entry.getValue()).intValue();

                if (k == i) {
                    object1 = null;
                } else if (k > i) {
                    i = k;
                    object1 = entry.getKey();
                }
            }

            return object1;
        } else {
            return null;
        }
    }

    public static Object clone(Object object) {
        if (!(object instanceof Cloneable)) {
            return null;
        } else {
            Object object1;

            if (object.getClass().isArray()) {
                Class oclass = object.getClass().getComponentType();

                if (!oclass.isPrimitive()) {
                    object1 = ((Object[]) ((Object[]) object)).clone();
                } else {
                    int i = Array.getLength(object);

                    object1 = Array.newInstance(oclass, i);

                    while (i-- > 0) {
                        Array.set(object1, i, Array.get(object, i));
                    }
                }
            } else {
                try {
                    Method method = object.getClass().getMethod("clone", new Class[0]);

                    object1 = method.invoke(object, new Object[0]);
                } catch (NoSuchMethodException nosuchmethodexception) {
                    throw new CloneFailedException("Cloneable type " + object.getClass().getName() + " has no clone method", nosuchmethodexception);
                } catch (IllegalAccessException illegalaccessexception) {
                    throw new CloneFailedException("Cannot clone Cloneable type " + object.getClass().getName(), illegalaccessexception);
                } catch (InvocationTargetException invocationtargetexception) {
                    throw new CloneFailedException("Exception cloning Cloneable type " + object.getClass().getName(), invocationtargetexception.getCause());
                }
            }

            return object1;
        }
    }

    public static Object cloneIfPossible(Object object) {
        Object object1 = clone(object);

        return object1 == null ? object : object1;
    }

    public static boolean CONST(boolean flag) {
        return flag;
    }

    public static byte CONST(byte b0) {
        return b0;
    }

    public static byte CONST_BYTE(int i) throws IllegalArgumentException {
        if (i >= -128 && i <= 127) {
            return (byte) i;
        } else {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + i + "]");
        }
    }

    public static char CONST(char c0) {
        return c0;
    }

    public static short CONST(short short0) {
        return short0;
    }

    public static short CONST_SHORT(int i) throws IllegalArgumentException {
        if (i >= -32768 && i <= 32767) {
            return (short) i;
        } else {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + i + "]");
        }
    }

    public static int CONST(int i) {
        return i;
    }

    public static long CONST(long i) {
        return i;
    }

    public static float CONST(float f) {
        return f;
    }

    public static double CONST(double d0) {
        return d0;
    }

    public static Object CONST(Object object) {
        return object;
    }

    public static class Null implements Serializable {

        private static final long serialVersionUID = 7092611880189329093L;

        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }
}
