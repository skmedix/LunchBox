package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class DiffBuilder implements Builder {

    private final List diffs;
    private final boolean objectsTriviallyEqual;
    private final Object left;
    private final Object right;
    private final ToStringStyle style;

    public DiffBuilder(Object object, Object object1, ToStringStyle tostringstyle) {
        if (object == null) {
            throw new IllegalArgumentException("lhs cannot be null");
        } else if (object1 == null) {
            throw new IllegalArgumentException("rhs cannot be null");
        } else {
            this.diffs = new ArrayList();
            this.left = object;
            this.right = object1;
            this.style = tostringstyle;
            this.objectsTriviallyEqual = object == object1 || object.equals(object1);
        }
    }

    public DiffBuilder append(final String s, final boolean flag, final boolean flag1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (flag != flag1) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Boolean getLeft() {
                        return Boolean.valueOf(flag);
                    }

                    public Boolean getRight() {
                        return Boolean.valueOf(flag);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final boolean[] aboolean, final boolean[] aboolean1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(aboolean, aboolean1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Boolean[] getLeft() {
                        return ArrayUtils.toObject(aboolean);
                    }

                    public Boolean[] getRight() {
                        return ArrayUtils.toObject(aboolean);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final byte b0, final byte b1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (b0 != b1) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Byte getLeft() {
                        return Byte.valueOf(b0);
                    }

                    public Byte getRight() {
                        return Byte.valueOf(b0);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final byte[] abyte, final byte[] abyte1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(abyte, abyte1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Byte[] getLeft() {
                        return ArrayUtils.toObject(abyte);
                    }

                    public Byte[] getRight() {
                        return ArrayUtils.toObject(abyte);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final char c0, final char c1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (c0 != c1) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Character getLeft() {
                        return Character.valueOf(c0);
                    }

                    public Character getRight() {
                        return Character.valueOf(c0);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final char[] achar, final char[] achar1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(achar, achar1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Character[] getLeft() {
                        return ArrayUtils.toObject(achar);
                    }

                    public Character[] getRight() {
                        return ArrayUtils.toObject(achar);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final double d0, final double d1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (Double.doubleToLongBits(d0) != Double.doubleToLongBits(d1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Double getLeft() {
                        return Double.valueOf(d0);
                    }

                    public Double getRight() {
                        return Double.valueOf(d0);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final double[] adouble, final double[] adouble1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(adouble, adouble1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Double[] getLeft() {
                        return ArrayUtils.toObject(adouble);
                    }

                    public Double[] getRight() {
                        return ArrayUtils.toObject(adouble);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final float f, final float f1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (Float.floatToIntBits(f) != Float.floatToIntBits(f1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Float getLeft() {
                        return Float.valueOf(f);
                    }

                    public Float getRight() {
                        return Float.valueOf(f);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final float[] afloat, final float[] afloat1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(afloat, afloat1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Float[] getLeft() {
                        return ArrayUtils.toObject(afloat);
                    }

                    public Float[] getRight() {
                        return ArrayUtils.toObject(afloat);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final int i, final int j) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (i != j) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Integer getLeft() {
                        return Integer.valueOf(i);
                    }

                    public Integer getRight() {
                        return Integer.valueOf(i);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final int[] aint, final int[] aint1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(aint, aint1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Integer[] getLeft() {
                        return ArrayUtils.toObject(aint);
                    }

                    public Integer[] getRight() {
                        return ArrayUtils.toObject(aint);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final long i, final long j) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (i != j) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Long getLeft() {
                        return Long.valueOf(i);
                    }

                    public Long getRight() {
                        return Long.valueOf(i);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final long[] along, final long[] along1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(along, along1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Long[] getLeft() {
                        return ArrayUtils.toObject(along);
                    }

                    public Long[] getRight() {
                        return ArrayUtils.toObject(along);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final short short0, final short short1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (short0 != short1) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Short getLeft() {
                        return Short.valueOf(short0);
                    }

                    public Short getRight() {
                        return Short.valueOf(short0);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final short[] ashort, final short[] ashort1) {
        if (s == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(ashort, ashort1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Short[] getLeft() {
                        return ArrayUtils.toObject(ashort);
                    }

                    public Short[] getRight() {
                        return ArrayUtils.toObject(ashort);
                    }
                });
            }

            return this;
        }
    }

    public DiffBuilder append(final String s, final Object object, final Object object1) {
        if (this.objectsTriviallyEqual) {
            return this;
        } else if (object == object1) {
            return this;
        } else {
            Object object2;

            if (object != null) {
                object2 = object;
            } else {
                object2 = object1;
            }

            if (object2.getClass().isArray()) {
                return object2 instanceof boolean[] ? this.append(s, (boolean[]) ((boolean[]) object), (boolean[]) ((boolean[]) object1)) : (object2 instanceof byte[] ? this.append(s, (byte[]) ((byte[]) object), (byte[]) ((byte[]) object1)) : (object2 instanceof char[] ? this.append(s, (char[]) ((char[]) object), (char[]) ((char[]) object1)) : (object2 instanceof double[] ? this.append(s, (double[]) ((double[]) object), (double[]) ((double[]) object1)) : (object2 instanceof float[] ? this.append(s, (float[]) ((float[]) object), (float[]) ((float[]) object1)) : (object2 instanceof int[] ? this.append(s, (int[]) ((int[]) object), (int[]) ((int[]) object1)) : (object2 instanceof long[] ? this.append(s, (long[]) ((long[]) object), (long[]) ((long[]) object1)) : (object2 instanceof short[] ? this.append(s, (short[]) ((short[]) object), (short[]) ((short[]) object1)) : this.append(s, (Object[]) ((Object[]) object), (Object[]) ((Object[]) object1)))))))));
            } else {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Object getLeft() {
                        return object;
                    }

                    public Object getRight() {
                        return object;
                    }
                });
                return this;
            }
        }
    }

    public DiffBuilder append(final String s, final Object[] aobject, final Object[] aobject1) {
        if (this.objectsTriviallyEqual) {
            return this;
        } else {
            if (!Arrays.equals(aobject, aobject1)) {
                this.diffs.add(new Diff(s) {
                    private static final long serialVersionUID = 1L;

                    public Object[] getLeft() {
                        return aobject;
                    }

                    public Object[] getRight() {
                        return aobject;
                    }
                });
            }

            return this;
        }
    }

    public DiffResult build() {
        return new DiffResult(this.left, this.right, this.diffs, this.style);
    }
}
