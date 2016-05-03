package org.apache.commons.lang3;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class Validate {

    private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
    private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified inclusive range of %s to %s";
    private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";
    private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE = "The validated collection contains null element at index: %d";
    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE = "The validated collection index is invalid: %d";
    private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
    private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
    private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";

    public static void isTrue(boolean flag, String s, long i) {
        if (!flag) {
            throw new IllegalArgumentException(String.format(s, new Object[] { Long.valueOf(i)}));
        }
    }

    public static void isTrue(boolean flag, String s, double d0) {
        if (!flag) {
            throw new IllegalArgumentException(String.format(s, new Object[] { Double.valueOf(d0)}));
        }
    }

    public static void isTrue(boolean flag, String s, Object... aobject) {
        if (!flag) {
            throw new IllegalArgumentException(String.format(s, aobject));
        }
    }

    public static void isTrue(boolean flag) {
        if (!flag) {
            throw new IllegalArgumentException("The validated expression is false");
        }
    }

    public static Object notNull(Object object) {
        return notNull(object, "The validated object is null", new Object[0]);
    }

    public static Object notNull(Object object, String s, Object... aobject) {
        if (object == null) {
            throw new NullPointerException(String.format(s, aobject));
        } else {
            return object;
        }
    }

    public static Object[] notEmpty(Object[] aobject, String s, Object... aobject1) {
        if (aobject == null) {
            throw new NullPointerException(String.format(s, aobject1));
        } else if (aobject.length == 0) {
            throw new IllegalArgumentException(String.format(s, aobject1));
        } else {
            return aobject;
        }
    }

    public static Object[] notEmpty(Object[] aobject) {
        return notEmpty(aobject, "The validated array is empty", new Object[0]);
    }

    public static Collection notEmpty(Collection collection, String s, Object... aobject) {
        if (collection == null) {
            throw new NullPointerException(String.format(s, aobject));
        } else if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(s, aobject));
        } else {
            return collection;
        }
    }

    public static Collection notEmpty(Collection collection) {
        return notEmpty(collection, "The validated collection is empty", new Object[0]);
    }

    public static Map notEmpty(Map map, String s, Object... aobject) {
        if (map == null) {
            throw new NullPointerException(String.format(s, aobject));
        } else if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(s, aobject));
        } else {
            return map;
        }
    }

    public static Map notEmpty(Map map) {
        return notEmpty(map, "The validated map is empty", new Object[0]);
    }

    public static CharSequence notEmpty(CharSequence charsequence, String s, Object... aobject) {
        if (charsequence == null) {
            throw new NullPointerException(String.format(s, aobject));
        } else if (charsequence.length() == 0) {
            throw new IllegalArgumentException(String.format(s, aobject));
        } else {
            return charsequence;
        }
    }

    public static CharSequence notEmpty(CharSequence charsequence) {
        return notEmpty(charsequence, "The validated character sequence is empty", new Object[0]);
    }

    public static CharSequence notBlank(CharSequence charsequence, String s, Object... aobject) {
        if (charsequence == null) {
            throw new NullPointerException(String.format(s, aobject));
        } else if (StringUtils.isBlank(charsequence)) {
            throw new IllegalArgumentException(String.format(s, aobject));
        } else {
            return charsequence;
        }
    }

    public static CharSequence notBlank(CharSequence charsequence) {
        return notBlank(charsequence, "The validated character sequence is blank", new Object[0]);
    }

    public static Object[] noNullElements(Object[] aobject, String s, Object... aobject1) {
        notNull(aobject);

        for (int i = 0; i < aobject.length; ++i) {
            if (aobject[i] == null) {
                Object[] aobject2 = ArrayUtils.add(aobject1, Integer.valueOf(i));

                throw new IllegalArgumentException(String.format(s, aobject2));
            }
        }

        return aobject;
    }

    public static Object[] noNullElements(Object[] aobject) {
        return noNullElements(aobject, "The validated array contains null element at index: %d", new Object[0]);
    }

    public static Iterable noNullElements(Iterable iterable, String s, Object... aobject) {
        notNull(iterable);
        int i = 0;

        for (Iterator iterator = iterable.iterator(); iterator.hasNext(); ++i) {
            if (iterator.next() == null) {
                Object[] aobject1 = ArrayUtils.addAll(aobject, new Object[] { Integer.valueOf(i)});

                throw new IllegalArgumentException(String.format(s, aobject1));
            }
        }

        return iterable;
    }

    public static Iterable noNullElements(Iterable iterable) {
        return noNullElements(iterable, "The validated collection contains null element at index: %d", new Object[0]);
    }

    public static Object[] validIndex(Object[] aobject, int i, String s, Object... aobject1) {
        notNull(aobject);
        if (i >= 0 && i < aobject.length) {
            return aobject;
        } else {
            throw new IndexOutOfBoundsException(String.format(s, aobject1));
        }
    }

    public static Object[] validIndex(Object[] aobject, int i) {
        return validIndex(aobject, i, "The validated array index is invalid: %d", new Object[] { Integer.valueOf(i)});
    }

    public static Collection validIndex(Collection collection, int i, String s, Object... aobject) {
        notNull(collection);
        if (i >= 0 && i < collection.size()) {
            return collection;
        } else {
            throw new IndexOutOfBoundsException(String.format(s, aobject));
        }
    }

    public static Collection validIndex(Collection collection, int i) {
        return validIndex(collection, i, "The validated collection index is invalid: %d", new Object[] { Integer.valueOf(i)});
    }

    public static CharSequence validIndex(CharSequence charsequence, int i, String s, Object... aobject) {
        notNull(charsequence);
        if (i >= 0 && i < charsequence.length()) {
            return charsequence;
        } else {
            throw new IndexOutOfBoundsException(String.format(s, aobject));
        }
    }

    public static CharSequence validIndex(CharSequence charsequence, int i) {
        return validIndex(charsequence, i, "The validated character sequence index is invalid: %d", new Object[] { Integer.valueOf(i)});
    }

    public static void validState(boolean flag) {
        if (!flag) {
            throw new IllegalStateException("The validated state is false");
        }
    }

    public static void validState(boolean flag, String s, Object... aobject) {
        if (!flag) {
            throw new IllegalStateException(String.format(s, aobject));
        }
    }

    public static void matchesPattern(CharSequence charsequence, String s) {
        if (!Pattern.matches(s, charsequence)) {
            throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", new Object[] { charsequence, s}));
        }
    }

    public static void matchesPattern(CharSequence charsequence, String s, String s1, Object... aobject) {
        if (!Pattern.matches(s, charsequence)) {
            throw new IllegalArgumentException(String.format(s1, aobject));
        }
    }

    public static void inclusiveBetween(Object object, Object object1, Comparable comparable) {
        if (comparable.compareTo(object) < 0 || comparable.compareTo(object1) > 0) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", new Object[] { comparable, object, object1}));
        }
    }

    public static void inclusiveBetween(Object object, Object object1, Comparable comparable, String s, Object... aobject) {
        if (comparable.compareTo(object) < 0 || comparable.compareTo(object1) > 0) {
            throw new IllegalArgumentException(String.format(s, aobject));
        }
    }

    public static void inclusiveBetween(long i, long j, long k) {
        if (k < i || k > j) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", new Object[] { Long.valueOf(k), Long.valueOf(i), Long.valueOf(j)}));
        }
    }

    public static void inclusiveBetween(long i, long j, long k, String s) {
        if (k < i || k > j) {
            throw new IllegalArgumentException(String.format(s, new Object[0]));
        }
    }

    public static void inclusiveBetween(double d0, double d1, double d2) {
        if (d2 < d0 || d2 > d1) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", new Object[] { Double.valueOf(d2), Double.valueOf(d0), Double.valueOf(d1)}));
        }
    }

    public static void inclusiveBetween(double d0, double d1, double d2, String s) {
        if (d2 < d0 || d2 > d1) {
            throw new IllegalArgumentException(String.format(s, new Object[0]));
        }
    }

    public static void exclusiveBetween(Object object, Object object1, Comparable comparable) {
        if (comparable.compareTo(object) <= 0 || comparable.compareTo(object1) >= 0) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", new Object[] { comparable, object, object1}));
        }
    }

    public static void exclusiveBetween(Object object, Object object1, Comparable comparable, String s, Object... aobject) {
        if (comparable.compareTo(object) <= 0 || comparable.compareTo(object1) >= 0) {
            throw new IllegalArgumentException(String.format(s, aobject));
        }
    }

    public static void exclusiveBetween(long i, long j, long k) {
        if (k <= i || k >= j) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", new Object[] { Long.valueOf(k), Long.valueOf(i), Long.valueOf(j)}));
        }
    }

    public static void exclusiveBetween(long i, long j, long k, String s) {
        if (k <= i || k >= j) {
            throw new IllegalArgumentException(String.format(s, new Object[0]));
        }
    }

    public static void exclusiveBetween(double d0, double d1, double d2) {
        if (d2 <= d0 || d2 >= d1) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", new Object[] { Double.valueOf(d2), Double.valueOf(d0), Double.valueOf(d1)}));
        }
    }

    public static void exclusiveBetween(double d0, double d1, double d2, String s) {
        if (d2 <= d0 || d2 >= d1) {
            throw new IllegalArgumentException(String.format(s, new Object[0]));
        }
    }

    public static void isInstanceOf(Class oclass, Object object) {
        if (!oclass.isInstance(object)) {
            throw new IllegalArgumentException(String.format("Expected type: %s, actual: %s", new Object[] { oclass.getName(), object == null ? "null" : object.getClass().getName()}));
        }
    }

    public static void isInstanceOf(Class oclass, Object object, String s, Object... aobject) {
        if (!oclass.isInstance(object)) {
            throw new IllegalArgumentException(String.format(s, aobject));
        }
    }

    public static void isAssignableFrom(Class oclass, Class oclass1) {
        if (!oclass.isAssignableFrom(oclass1)) {
            throw new IllegalArgumentException(String.format("Cannot assign a %s to a %s", new Object[] { oclass1 == null ? "null" : oclass1.getName(), oclass.getName()}));
        }
    }

    public static void isAssignableFrom(Class oclass, Class oclass1, String s, Object... aobject) {
        if (!oclass.isAssignableFrom(oclass1)) {
            throw new IllegalArgumentException(String.format(s, aobject));
        }
    }
}
