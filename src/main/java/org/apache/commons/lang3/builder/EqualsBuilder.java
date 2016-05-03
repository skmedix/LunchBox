package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

public class EqualsBuilder implements Builder {

    private static final ThreadLocal REGISTRY = new ThreadLocal();
    private boolean isEquals = true;

    static Set getRegistry() {
        return (Set) EqualsBuilder.REGISTRY.get();
    }

    static Pair getRegisterPair(Object object, Object object1) {
        IDKey idkey = new IDKey(object);
        IDKey idkey1 = new IDKey(object1);

        return Pair.of(idkey, idkey1);
    }

    static boolean isRegistered(Object object, Object object1) {
        Set set = getRegistry();
        Pair pair = getRegisterPair(object, object1);
        Pair pair1 = Pair.of(pair.getLeft(), pair.getRight());

        return set != null && (set.contains(pair) || set.contains(pair1));
    }

    static void register(Object object, Object object1) {
        Class oclass = EqualsBuilder.class;

        synchronized (EqualsBuilder.class) {
            if (getRegistry() == null) {
                EqualsBuilder.REGISTRY.set(new HashSet());
            }
        }

        Set set = getRegistry();
        Pair pair = getRegisterPair(object, object1);

        set.add(pair);
    }

    static void unregister(Object object, Object object1) {
        Set set = getRegistry();

        if (set != null) {
            Pair pair = getRegisterPair(object, object1);

            set.remove(pair);
            Class oclass = EqualsBuilder.class;

            synchronized (EqualsBuilder.class) {
                set = getRegistry();
                if (set != null && set.isEmpty()) {
                    EqualsBuilder.REGISTRY.remove();
                }
            }
        }

    }

    public static boolean reflectionEquals(Object object, Object object1, Collection collection) {
        return reflectionEquals(object, object1, ReflectionToStringBuilder.toNoNullStringArray(collection));
    }

    public static boolean reflectionEquals(Object object, Object object1, String... astring) {
        return reflectionEquals(object, object1, false, (Class) null, astring);
    }

    public static boolean reflectionEquals(Object object, Object object1, boolean flag) {
        return reflectionEquals(object, object1, flag, (Class) null, new String[0]);
    }

    public static boolean reflectionEquals(Object object, Object object1, boolean flag, Class oclass, String... astring) {
        if (object == object1) {
            return true;
        } else if (object != null && object1 != null) {
            Class oclass1 = object.getClass();
            Class oclass2 = object1.getClass();
            Class oclass3;

            if (oclass1.isInstance(object1)) {
                oclass3 = oclass1;
                if (!oclass2.isInstance(object)) {
                    oclass3 = oclass2;
                }
            } else {
                if (!oclass2.isInstance(object)) {
                    return false;
                }

                oclass3 = oclass2;
                if (!oclass1.isInstance(object1)) {
                    oclass3 = oclass1;
                }
            }

            EqualsBuilder equalsbuilder = new EqualsBuilder();

            try {
                if (oclass3.isArray()) {
                    equalsbuilder.append(object, object1);
                } else {
                    reflectionAppend(object, object1, oclass3, equalsbuilder, flag, astring);

                    while (oclass3.getSuperclass() != null && oclass3 != oclass) {
                        oclass3 = oclass3.getSuperclass();
                        reflectionAppend(object, object1, oclass3, equalsbuilder, flag, astring);
                    }
                }
            } catch (IllegalArgumentException illegalargumentexception) {
                return false;
            }

            return equalsbuilder.isEquals();
        } else {
            return false;
        }
    }

    private static void reflectionAppend(Object object, Object object1, Class oclass, EqualsBuilder equalsbuilder, boolean flag, String[] astring) {
        if (!isRegistered(object, object1)) {
            try {
                register(object, object1);
                Field[] afield = oclass.getDeclaredFields();

                AccessibleObject.setAccessible(afield, true);

                for (int i = 0; i < afield.length && equalsbuilder.isEquals; ++i) {
                    Field field = afield[i];

                    if (!ArrayUtils.contains(astring, field.getName()) && field.getName().indexOf(36) == -1 && (flag || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers())) {
                        try {
                            equalsbuilder.append(field.get(object), field.get(object1));
                        } catch (IllegalAccessException illegalaccessexception) {
                            throw new InternalError("Unexpected IllegalAccessException");
                        }
                    }
                }
            } finally {
                unregister(object, object1);
            }

        }
    }

    public EqualsBuilder appendSuper(boolean flag) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = flag;
            return this;
        }
    }

    public EqualsBuilder append(Object object, Object object1) {
        if (!this.isEquals) {
            return this;
        } else if (object == object1) {
            return this;
        } else if (object != null && object1 != null) {
            Class oclass = object.getClass();

            if (!oclass.isArray()) {
                this.isEquals = object.equals(object1);
            } else if (object.getClass() != object1.getClass()) {
                this.setEquals(false);
            } else if (object instanceof long[]) {
                this.append((long[]) ((long[]) object), (long[]) ((long[]) object1));
            } else if (object instanceof int[]) {
                this.append((int[]) ((int[]) object), (int[]) ((int[]) object1));
            } else if (object instanceof short[]) {
                this.append((short[]) ((short[]) object), (short[]) ((short[]) object1));
            } else if (object instanceof char[]) {
                this.append((char[]) ((char[]) object), (char[]) ((char[]) object1));
            } else if (object instanceof byte[]) {
                this.append((byte[]) ((byte[]) object), (byte[]) ((byte[]) object1));
            } else if (object instanceof double[]) {
                this.append((double[]) ((double[]) object), (double[]) ((double[]) object1));
            } else if (object instanceof float[]) {
                this.append((float[]) ((float[]) object), (float[]) ((float[]) object1));
            } else if (object instanceof boolean[]) {
                this.append((boolean[]) ((boolean[]) object), (boolean[]) ((boolean[]) object1));
            } else {
                this.append((Object[]) ((Object[]) object), (Object[]) ((Object[]) object1));
            }

            return this;
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(long i, long j) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = i == j;
            return this;
        }
    }

    public EqualsBuilder append(int i, int j) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = i == j;
            return this;
        }
    }

    public EqualsBuilder append(short short0, short short1) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = short0 == short1;
            return this;
        }
    }

    public EqualsBuilder append(char c0, char c1) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = c0 == c1;
            return this;
        }
    }

    public EqualsBuilder append(byte b0, byte b1) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = b0 == b1;
            return this;
        }
    }

    public EqualsBuilder append(double d0, double d1) {
        return !this.isEquals ? this : this.append(Double.doubleToLongBits(d0), Double.doubleToLongBits(d1));
    }

    public EqualsBuilder append(float f, float f1) {
        return !this.isEquals ? this : this.append(Float.floatToIntBits(f), Float.floatToIntBits(f1));
    }

    public EqualsBuilder append(boolean flag, boolean flag1) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = flag == flag1;
            return this;
        }
    }

    public EqualsBuilder append(Object[] aobject, Object[] aobject1) {
        if (!this.isEquals) {
            return this;
        } else if (aobject == aobject1) {
            return this;
        } else if (aobject != null && aobject1 != null) {
            if (aobject.length != aobject1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < aobject.length && this.isEquals; ++i) {
                    this.append(aobject[i], aobject1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(long[] along, long[] along1) {
        if (!this.isEquals) {
            return this;
        } else if (along == along1) {
            return this;
        } else if (along != null && along1 != null) {
            if (along.length != along1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < along.length && this.isEquals; ++i) {
                    this.append(along[i], along1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(int[] aint, int[] aint1) {
        if (!this.isEquals) {
            return this;
        } else if (aint == aint1) {
            return this;
        } else if (aint != null && aint1 != null) {
            if (aint.length != aint1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < aint.length && this.isEquals; ++i) {
                    this.append(aint[i], aint1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(short[] ashort, short[] ashort1) {
        if (!this.isEquals) {
            return this;
        } else if (ashort == ashort1) {
            return this;
        } else if (ashort != null && ashort1 != null) {
            if (ashort.length != ashort1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < ashort.length && this.isEquals; ++i) {
                    this.append(ashort[i], ashort1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(char[] achar, char[] achar1) {
        if (!this.isEquals) {
            return this;
        } else if (achar == achar1) {
            return this;
        } else if (achar != null && achar1 != null) {
            if (achar.length != achar1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < achar.length && this.isEquals; ++i) {
                    this.append(achar[i], achar1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(byte[] abyte, byte[] abyte1) {
        if (!this.isEquals) {
            return this;
        } else if (abyte == abyte1) {
            return this;
        } else if (abyte != null && abyte1 != null) {
            if (abyte.length != abyte1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < abyte.length && this.isEquals; ++i) {
                    this.append(abyte[i], abyte1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(double[] adouble, double[] adouble1) {
        if (!this.isEquals) {
            return this;
        } else if (adouble == adouble1) {
            return this;
        } else if (adouble != null && adouble1 != null) {
            if (adouble.length != adouble1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < adouble.length && this.isEquals; ++i) {
                    this.append(adouble[i], adouble1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(float[] afloat, float[] afloat1) {
        if (!this.isEquals) {
            return this;
        } else if (afloat == afloat1) {
            return this;
        } else if (afloat != null && afloat1 != null) {
            if (afloat.length != afloat1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < afloat.length && this.isEquals; ++i) {
                    this.append(afloat[i], afloat1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(boolean[] aboolean, boolean[] aboolean1) {
        if (!this.isEquals) {
            return this;
        } else if (aboolean == aboolean1) {
            return this;
        } else if (aboolean != null && aboolean1 != null) {
            if (aboolean.length != aboolean1.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < aboolean.length && this.isEquals; ++i) {
                    this.append(aboolean[i], aboolean1[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    public Boolean build() {
        return Boolean.valueOf(this.isEquals());
    }

    protected void setEquals(boolean flag) {
        this.isEquals = flag;
    }

    public void reset() {
        this.isEquals = true;
    }
}
