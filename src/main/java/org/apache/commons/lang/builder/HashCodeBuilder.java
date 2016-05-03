package org.apache.commons.lang.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;

public class HashCodeBuilder {

    private static final ThreadLocal REGISTRY = new ThreadLocal();
    private final int iConstant;
    private int iTotal = 0;
    static Class class$org$apache$commons$lang$builder$HashCodeBuilder;

    static Set getRegistry() {
        return (Set) HashCodeBuilder.REGISTRY.get();
    }

    static boolean isRegistered(Object value) {
        Set registry = getRegistry();

        return registry != null && registry.contains(new IDKey(value));
    }

    private static void reflectionAppend(Object object, Class clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) {
        if (!isRegistered(object)) {
            try {
                register(object);
                Field[] fields = clazz.getDeclaredFields();

                AccessibleObject.setAccessible(fields, true);

                for (int i = 0; i < fields.length; ++i) {
                    Field field = fields[i];

                    if (!ArrayUtils.contains(excludeFields, field.getName()) && field.getName().indexOf(36) == -1 && (useTransients || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers())) {
                        try {
                            Object e = field.get(object);

                            builder.append(e);
                        } catch (IllegalAccessException illegalaccessexception) {
                            throw new InternalError("Unexpected IllegalAccessException");
                        }
                    }
                }
            } finally {
                unregister(object);
            }

        }
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, (Class) null, (String[]) null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, (Class) null, (String[]) null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients, Class reflectUpToClass) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, (String[]) null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients, Class reflectUpToClass, String[] excludeFields) {
        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        } else {
            HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            Class clazz = object.getClass();

            reflectionAppend(object, clazz, builder, testTransients, excludeFields);

            while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
                clazz = clazz.getSuperclass();
                reflectionAppend(object, clazz, builder, testTransients, excludeFields);
            }

            return builder.toHashCode();
        }
    }

    public static int reflectionHashCode(Object object) {
        return reflectionHashCode(17, 37, object, false, (Class) null, (String[]) null);
    }

    public static int reflectionHashCode(Object object, boolean testTransients) {
        return reflectionHashCode(17, 37, object, testTransients, (Class) null, (String[]) null);
    }

    public static int reflectionHashCode(Object object, Collection excludeFields) {
        return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static int reflectionHashCode(Object object, String[] excludeFields) {
        return reflectionHashCode(17, 37, object, false, (Class) null, excludeFields);
    }

    static void register(Object value) {
        synchronized (HashCodeBuilder.class$org$apache$commons$lang$builder$HashCodeBuilder == null ? (HashCodeBuilder.class$org$apache$commons$lang$builder$HashCodeBuilder = class$("org.apache.commons.lang.builder.HashCodeBuilder")) : HashCodeBuilder.class$org$apache$commons$lang$builder$HashCodeBuilder) {
            if (getRegistry() == null) {
                HashCodeBuilder.REGISTRY.set(new HashSet());
            }
        }

        getRegistry().add(new IDKey(value));
    }

    static void unregister(Object value) {
        Set registry = getRegistry();

        if (registry != null) {
            registry.remove(new IDKey(value));
            synchronized (HashCodeBuilder.class$org$apache$commons$lang$builder$HashCodeBuilder == null ? (HashCodeBuilder.class$org$apache$commons$lang$builder$HashCodeBuilder = class$("org.apache.commons.lang.builder.HashCodeBuilder")) : HashCodeBuilder.class$org$apache$commons$lang$builder$HashCodeBuilder) {
                registry = getRegistry();
                if (registry != null && registry.isEmpty()) {
                    HashCodeBuilder.REGISTRY.set((Object) null);
                }
            }
        }

    }

    public HashCodeBuilder() {
        this.iConstant = 37;
        this.iTotal = 17;
    }

    public HashCodeBuilder(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
        if (initialNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero initial value");
        } else if (initialNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
        } else if (multiplierNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero multiplier");
        } else if (multiplierNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
        } else {
            this.iConstant = multiplierNonZeroOddNumber;
            this.iTotal = initialNonZeroOddNumber;
        }
    }

    public HashCodeBuilder append(boolean value) {
        this.iTotal = this.iTotal * this.iConstant + (value ? 0 : 1);
        return this;
    }

    public HashCodeBuilder append(boolean[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(byte value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(byte[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(char value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(char[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(double value) {
        return this.append(Double.doubleToLongBits(value));
    }

    public HashCodeBuilder append(double[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(float value) {
        this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(value);
        return this;
    }

    public HashCodeBuilder append(float[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(int value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(int[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(long value) {
        this.iTotal = this.iTotal * this.iConstant + (int) (value ^ value >> 32);
        return this;
    }

    public HashCodeBuilder append(long[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(Object object) {
        if (object == null) {
            this.iTotal *= this.iConstant;
        } else if (object.getClass().isArray()) {
            if (object instanceof long[]) {
                this.append((long[]) ((long[]) object));
            } else if (object instanceof int[]) {
                this.append((int[]) ((int[]) object));
            } else if (object instanceof short[]) {
                this.append((short[]) ((short[]) object));
            } else if (object instanceof char[]) {
                this.append((char[]) ((char[]) object));
            } else if (object instanceof byte[]) {
                this.append((byte[]) ((byte[]) object));
            } else if (object instanceof double[]) {
                this.append((double[]) ((double[]) object));
            } else if (object instanceof float[]) {
                this.append((float[]) ((float[]) object));
            } else if (object instanceof boolean[]) {
                this.append((boolean[]) ((boolean[]) object));
            } else {
                this.append((Object[]) ((Object[]) object));
            }
        } else {
            this.iTotal = this.iTotal * this.iConstant + object.hashCode();
        }

        return this;
    }

    public HashCodeBuilder append(Object[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder append(short value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(short[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int i = 0; i < array.length; ++i) {
                this.append(array[i]);
            }
        }

        return this;
    }

    public HashCodeBuilder appendSuper(int superHashCode) {
        this.iTotal = this.iTotal * this.iConstant + superHashCode;
        return this;
    }

    public int toHashCode() {
        return this.iTotal;
    }

    public int hashCode() {
        return this.toHashCode();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }
}
