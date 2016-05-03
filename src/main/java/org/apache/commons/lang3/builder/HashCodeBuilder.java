package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;

public class HashCodeBuilder implements Builder {

    private static final ThreadLocal REGISTRY = new ThreadLocal();
    private final int iConstant;
    private int iTotal = 0;

    static Set getRegistry() {
        return (Set) HashCodeBuilder.REGISTRY.get();
    }

    static boolean isRegistered(Object object) {
        Set set = getRegistry();

        return set != null && set.contains(new IDKey(object));
    }

    private static void reflectionAppend(Object object, Class oclass, HashCodeBuilder hashcodebuilder, boolean flag, String[] astring) {
        if (!isRegistered(object)) {
            try {
                register(object);
                Field[] afield = oclass.getDeclaredFields();

                AccessibleObject.setAccessible(afield, true);
                Field[] afield1 = afield;
                int i = afield.length;

                for (int j = 0; j < i; ++j) {
                    Field field = afield1[j];

                    if (!ArrayUtils.contains(astring, field.getName()) && field.getName().indexOf(36) == -1 && (flag || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers())) {
                        try {
                            Object object1 = field.get(object);

                            hashcodebuilder.append(object1);
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

    public static int reflectionHashCode(int i, int j, Object object) {
        return reflectionHashCode(i, j, object, false, (Class) null, new String[0]);
    }

    public static int reflectionHashCode(int i, int j, Object object, boolean flag) {
        return reflectionHashCode(i, j, object, flag, (Class) null, new String[0]);
    }

    public static int reflectionHashCode(int i, int j, Object object, boolean flag, Class oclass, String... astring) {
        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        } else {
            HashCodeBuilder hashcodebuilder = new HashCodeBuilder(i, j);
            Class oclass1 = object.getClass();

            reflectionAppend(object, oclass1, hashcodebuilder, flag, astring);

            while (oclass1.getSuperclass() != null && oclass1 != oclass) {
                oclass1 = oclass1.getSuperclass();
                reflectionAppend(object, oclass1, hashcodebuilder, flag, astring);
            }

            return hashcodebuilder.toHashCode();
        }
    }

    public static int reflectionHashCode(Object object, boolean flag) {
        return reflectionHashCode(17, 37, object, flag, (Class) null, new String[0]);
    }

    public static int reflectionHashCode(Object object, Collection collection) {
        return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(collection));
    }

    public static int reflectionHashCode(Object object, String... astring) {
        return reflectionHashCode(17, 37, object, false, (Class) null, astring);
    }

    static void register(Object object) {
        Class oclass = HashCodeBuilder.class;

        synchronized (HashCodeBuilder.class) {
            if (getRegistry() == null) {
                HashCodeBuilder.REGISTRY.set(new HashSet());
            }
        }

        getRegistry().add(new IDKey(object));
    }

    static void unregister(Object object) {
        Set set = getRegistry();

        if (set != null) {
            set.remove(new IDKey(object));
            Class oclass = HashCodeBuilder.class;

            synchronized (HashCodeBuilder.class) {
                set = getRegistry();
                if (set != null && set.isEmpty()) {
                    HashCodeBuilder.REGISTRY.remove();
                }
            }
        }

    }

    public HashCodeBuilder() {
        this.iConstant = 37;
        this.iTotal = 17;
    }

    public HashCodeBuilder(int i, int j) {
        if (i % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
        } else if (j % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
        } else {
            this.iConstant = j;
            this.iTotal = i;
        }
    }

    public HashCodeBuilder append(boolean flag) {
        this.iTotal = this.iTotal * this.iConstant + (flag ? 0 : 1);
        return this;
    }

    public HashCodeBuilder append(boolean[] aboolean) {
        if (aboolean == null) {
            this.iTotal *= this.iConstant;
        } else {
            boolean[] aboolean1 = aboolean;
            int i = aboolean.length;

            for (int j = 0; j < i; ++j) {
                boolean flag = aboolean1[j];

                this.append(flag);
            }
        }

        return this;
    }

    public HashCodeBuilder append(byte b0) {
        this.iTotal = this.iTotal * this.iConstant + b0;
        return this;
    }

    public HashCodeBuilder append(byte[] abyte) {
        if (abyte == null) {
            this.iTotal *= this.iConstant;
        } else {
            byte[] abyte1 = abyte;
            int i = abyte.length;

            for (int j = 0; j < i; ++j) {
                byte b0 = abyte1[j];

                this.append(b0);
            }
        }

        return this;
    }

    public HashCodeBuilder append(char c0) {
        this.iTotal = this.iTotal * this.iConstant + c0;
        return this;
    }

    public HashCodeBuilder append(char[] achar) {
        if (achar == null) {
            this.iTotal *= this.iConstant;
        } else {
            char[] achar1 = achar;
            int i = achar.length;

            for (int j = 0; j < i; ++j) {
                char c0 = achar1[j];

                this.append(c0);
            }
        }

        return this;
    }

    public HashCodeBuilder append(double d0) {
        return this.append(Double.doubleToLongBits(d0));
    }

    public HashCodeBuilder append(double[] adouble) {
        if (adouble == null) {
            this.iTotal *= this.iConstant;
        } else {
            double[] adouble1 = adouble;
            int i = adouble.length;

            for (int j = 0; j < i; ++j) {
                double d0 = adouble1[j];

                this.append(d0);
            }
        }

        return this;
    }

    public HashCodeBuilder append(float f) {
        this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(f);
        return this;
    }

    public HashCodeBuilder append(float[] afloat) {
        if (afloat == null) {
            this.iTotal *= this.iConstant;
        } else {
            float[] afloat1 = afloat;
            int i = afloat.length;

            for (int j = 0; j < i; ++j) {
                float f = afloat1[j];

                this.append(f);
            }
        }

        return this;
    }

    public HashCodeBuilder append(int i) {
        this.iTotal = this.iTotal * this.iConstant + i;
        return this;
    }

    public HashCodeBuilder append(int[] aint) {
        if (aint == null) {
            this.iTotal *= this.iConstant;
        } else {
            int[] aint1 = aint;
            int i = aint.length;

            for (int j = 0; j < i; ++j) {
                int k = aint1[j];

                this.append(k);
            }
        }

        return this;
    }

    public HashCodeBuilder append(long i) {
        this.iTotal = this.iTotal * this.iConstant + (int) (i ^ i >> 32);
        return this;
    }

    public HashCodeBuilder append(long[] along) {
        if (along == null) {
            this.iTotal *= this.iConstant;
        } else {
            long[] along1 = along;
            int i = along.length;

            for (int j = 0; j < i; ++j) {
                long k = along1[j];

                this.append(k);
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

    public HashCodeBuilder append(Object[] aobject) {
        if (aobject == null) {
            this.iTotal *= this.iConstant;
        } else {
            Object[] aobject1 = aobject;
            int i = aobject.length;

            for (int j = 0; j < i; ++j) {
                Object object = aobject1[j];

                this.append(object);
            }
        }

        return this;
    }

    public HashCodeBuilder append(short short0) {
        this.iTotal = this.iTotal * this.iConstant + short0;
        return this;
    }

    public HashCodeBuilder append(short[] ashort) {
        if (ashort == null) {
            this.iTotal *= this.iConstant;
        } else {
            short[] ashort1 = ashort;
            int i = ashort.length;

            for (int j = 0; j < i; ++j) {
                short short0 = ashort1[j];

                this.append(short0);
            }
        }

        return this;
    }

    public HashCodeBuilder appendSuper(int i) {
        this.iTotal = this.iTotal * this.iConstant + i;
        return this;
    }

    public int toHashCode() {
        return this.iTotal;
    }

    public Integer build() {
        return Integer.valueOf(this.toHashCode());
    }

    public int hashCode() {
        return this.toHashCode();
    }
}
