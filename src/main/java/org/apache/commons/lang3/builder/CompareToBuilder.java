package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import org.apache.commons.lang3.ArrayUtils;

public class CompareToBuilder implements Builder {

    private int comparison = 0;

    public static int reflectionCompare(Object object, Object object1) {
        return reflectionCompare(object, object1, false, (Class) null, new String[0]);
    }

    public static int reflectionCompare(Object object, Object object1, boolean flag) {
        return reflectionCompare(object, object1, flag, (Class) null, new String[0]);
    }

    public static int reflectionCompare(Object object, Object object1, Collection collection) {
        return reflectionCompare(object, object1, ReflectionToStringBuilder.toNoNullStringArray(collection));
    }

    public static int reflectionCompare(Object object, Object object1, String... astring) {
        return reflectionCompare(object, object1, false, (Class) null, astring);
    }

    public static int reflectionCompare(Object object, Object object1, boolean flag, Class oclass, String... astring) {
        if (object == object1) {
            return 0;
        } else if (object != null && object1 != null) {
            Class oclass1 = object.getClass();

            if (!oclass1.isInstance(object1)) {
                throw new ClassCastException();
            } else {
                CompareToBuilder comparetobuilder = new CompareToBuilder();

                reflectionAppend(object, object1, oclass1, comparetobuilder, flag, astring);

                while (oclass1.getSuperclass() != null && oclass1 != oclass) {
                    oclass1 = oclass1.getSuperclass();
                    reflectionAppend(object, object1, oclass1, comparetobuilder, flag, astring);
                }

                return comparetobuilder.toComparison();
            }
        } else {
            throw new NullPointerException();
        }
    }

    private static void reflectionAppend(Object object, Object object1, Class oclass, CompareToBuilder comparetobuilder, boolean flag, String[] astring) {
        Field[] afield = oclass.getDeclaredFields();

        AccessibleObject.setAccessible(afield, true);

        for (int i = 0; i < afield.length && comparetobuilder.comparison == 0; ++i) {
            Field field = afield[i];

            if (!ArrayUtils.contains(astring, field.getName()) && field.getName().indexOf(36) == -1 && (flag || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers())) {
                try {
                    comparetobuilder.append(field.get(object), field.get(object1));
                } catch (IllegalAccessException illegalaccessexception) {
                    throw new InternalError("Unexpected IllegalAccessException");
                }
            }
        }

    }

    public CompareToBuilder appendSuper(int i) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = i;
            return this;
        }
    }

    public CompareToBuilder append(Object object, Object object1) {
        return this.append(object, object1, (Comparator) null);
    }

    public CompareToBuilder append(Object object, Object object1, Comparator comparator) {
        if (this.comparison != 0) {
            return this;
        } else if (object == object1) {
            return this;
        } else if (object == null) {
            this.comparison = -1;
            return this;
        } else if (object1 == null) {
            this.comparison = 1;
            return this;
        } else {
            if (object.getClass().isArray()) {
                if (object instanceof long[]) {
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
                    this.append((Object[]) ((Object[]) object), (Object[]) ((Object[]) object1), comparator);
                }
            } else if (comparator == null) {
                Comparable comparable = (Comparable) object;

                this.comparison = comparable.compareTo(object1);
            } else {
                this.comparison = comparator.compare(object, object1);
            }

            return this;
        }
    }

    public CompareToBuilder append(long i, long j) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = i < j ? -1 : (i > j ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(int i, int j) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = i < j ? -1 : (i > j ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(short short0, short short1) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = short0 < short1 ? -1 : (short0 > short1 ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(char c0, char c1) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = c0 < c1 ? -1 : (c0 > c1 ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(byte b0, byte b1) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = b0 < b1 ? -1 : (b0 > b1 ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(double d0, double d1) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = Double.compare(d0, d1);
            return this;
        }
    }

    public CompareToBuilder append(float f, float f1) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = Float.compare(f, f1);
            return this;
        }
    }

    public CompareToBuilder append(boolean flag, boolean flag1) {
        if (this.comparison != 0) {
            return this;
        } else if (flag == flag1) {
            return this;
        } else {
            if (!flag) {
                this.comparison = -1;
            } else {
                this.comparison = 1;
            }

            return this;
        }
    }

    public CompareToBuilder append(Object[] aobject, Object[] aobject1) {
        return this.append(aobject, aobject1, (Comparator) null);
    }

    public CompareToBuilder append(Object[] aobject, Object[] aobject1, Comparator comparator) {
        if (this.comparison != 0) {
            return this;
        } else if (aobject == aobject1) {
            return this;
        } else if (aobject == null) {
            this.comparison = -1;
            return this;
        } else if (aobject1 == null) {
            this.comparison = 1;
            return this;
        } else if (aobject.length != aobject1.length) {
            this.comparison = aobject.length < aobject1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < aobject.length && this.comparison == 0; ++i) {
                this.append(aobject[i], aobject1[i], comparator);
            }

            return this;
        }
    }

    public CompareToBuilder append(long[] along, long[] along1) {
        if (this.comparison != 0) {
            return this;
        } else if (along == along1) {
            return this;
        } else if (along == null) {
            this.comparison = -1;
            return this;
        } else if (along1 == null) {
            this.comparison = 1;
            return this;
        } else if (along.length != along1.length) {
            this.comparison = along.length < along1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < along.length && this.comparison == 0; ++i) {
                this.append(along[i], along1[i]);
            }

            return this;
        }
    }

    public CompareToBuilder append(int[] aint, int[] aint1) {
        if (this.comparison != 0) {
            return this;
        } else if (aint == aint1) {
            return this;
        } else if (aint == null) {
            this.comparison = -1;
            return this;
        } else if (aint1 == null) {
            this.comparison = 1;
            return this;
        } else if (aint.length != aint1.length) {
            this.comparison = aint.length < aint1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < aint.length && this.comparison == 0; ++i) {
                this.append(aint[i], aint1[i]);
            }

            return this;
        }
    }

    public CompareToBuilder append(short[] ashort, short[] ashort1) {
        if (this.comparison != 0) {
            return this;
        } else if (ashort == ashort1) {
            return this;
        } else if (ashort == null) {
            this.comparison = -1;
            return this;
        } else if (ashort1 == null) {
            this.comparison = 1;
            return this;
        } else if (ashort.length != ashort1.length) {
            this.comparison = ashort.length < ashort1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < ashort.length && this.comparison == 0; ++i) {
                this.append(ashort[i], ashort1[i]);
            }

            return this;
        }
    }

    public CompareToBuilder append(char[] achar, char[] achar1) {
        if (this.comparison != 0) {
            return this;
        } else if (achar == achar1) {
            return this;
        } else if (achar == null) {
            this.comparison = -1;
            return this;
        } else if (achar1 == null) {
            this.comparison = 1;
            return this;
        } else if (achar.length != achar1.length) {
            this.comparison = achar.length < achar1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < achar.length && this.comparison == 0; ++i) {
                this.append(achar[i], achar1[i]);
            }

            return this;
        }
    }

    public CompareToBuilder append(byte[] abyte, byte[] abyte1) {
        if (this.comparison != 0) {
            return this;
        } else if (abyte == abyte1) {
            return this;
        } else if (abyte == null) {
            this.comparison = -1;
            return this;
        } else if (abyte1 == null) {
            this.comparison = 1;
            return this;
        } else if (abyte.length != abyte1.length) {
            this.comparison = abyte.length < abyte1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < abyte.length && this.comparison == 0; ++i) {
                this.append(abyte[i], abyte1[i]);
            }

            return this;
        }
    }

    public CompareToBuilder append(double[] adouble, double[] adouble1) {
        if (this.comparison != 0) {
            return this;
        } else if (adouble == adouble1) {
            return this;
        } else if (adouble == null) {
            this.comparison = -1;
            return this;
        } else if (adouble1 == null) {
            this.comparison = 1;
            return this;
        } else if (adouble.length != adouble1.length) {
            this.comparison = adouble.length < adouble1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < adouble.length && this.comparison == 0; ++i) {
                this.append(adouble[i], adouble1[i]);
            }

            return this;
        }
    }

    public CompareToBuilder append(float[] afloat, float[] afloat1) {
        if (this.comparison != 0) {
            return this;
        } else if (afloat == afloat1) {
            return this;
        } else if (afloat == null) {
            this.comparison = -1;
            return this;
        } else if (afloat1 == null) {
            this.comparison = 1;
            return this;
        } else if (afloat.length != afloat1.length) {
            this.comparison = afloat.length < afloat1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < afloat.length && this.comparison == 0; ++i) {
                this.append(afloat[i], afloat1[i]);
            }

            return this;
        }
    }

    public CompareToBuilder append(boolean[] aboolean, boolean[] aboolean1) {
        if (this.comparison != 0) {
            return this;
        } else if (aboolean == aboolean1) {
            return this;
        } else if (aboolean == null) {
            this.comparison = -1;
            return this;
        } else if (aboolean1 == null) {
            this.comparison = 1;
            return this;
        } else if (aboolean.length != aboolean1.length) {
            this.comparison = aboolean.length < aboolean1.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < aboolean.length && this.comparison == 0; ++i) {
                this.append(aboolean[i], aboolean1[i]);
            }

            return this;
        }
    }

    public int toComparison() {
        return this.comparison;
    }

    public Integer build() {
        return Integer.valueOf(this.toComparison());
    }
}
