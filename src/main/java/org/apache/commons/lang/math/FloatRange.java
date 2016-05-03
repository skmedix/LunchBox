package org.apache.commons.lang.math;

import java.io.Serializable;

public final class FloatRange extends Range implements Serializable {

    private static final long serialVersionUID = 71849363892750L;
    private final float min;
    private final float max;
    private transient Float minObject = null;
    private transient Float maxObject = null;
    private transient int hashCode = 0;
    private transient String toString = null;

    public FloatRange(float number) {
        if (Float.isNaN(number)) {
            throw new IllegalArgumentException("The number must not be NaN");
        } else {
            this.min = number;
            this.max = number;
        }
    }

    public FloatRange(Number number) {
        if (number == null) {
            throw new IllegalArgumentException("The number must not be null");
        } else {
            this.min = number.floatValue();
            this.max = number.floatValue();
            if (!Float.isNaN(this.min) && !Float.isNaN(this.max)) {
                if (number instanceof Float) {
                    this.minObject = (Float) number;
                    this.maxObject = (Float) number;
                }

            } else {
                throw new IllegalArgumentException("The number must not be NaN");
            }
        }
    }

    public FloatRange(float number1, float number2) {
        if (!Float.isNaN(number1) && !Float.isNaN(number2)) {
            if (number2 < number1) {
                this.min = number2;
                this.max = number1;
            } else {
                this.min = number1;
                this.max = number2;
            }

        } else {
            throw new IllegalArgumentException("The numbers must not be NaN");
        }
    }

    public FloatRange(Number number1, Number number2) {
        if (number1 != null && number2 != null) {
            float number1val = number1.floatValue();
            float number2val = number2.floatValue();

            if (!Float.isNaN(number1val) && !Float.isNaN(number2val)) {
                if (number2val < number1val) {
                    this.min = number2val;
                    this.max = number1val;
                    if (number2 instanceof Float) {
                        this.minObject = (Float) number2;
                    }

                    if (number1 instanceof Float) {
                        this.maxObject = (Float) number1;
                    }
                } else {
                    this.min = number1val;
                    this.max = number2val;
                    if (number1 instanceof Float) {
                        this.minObject = (Float) number1;
                    }

                    if (number2 instanceof Float) {
                        this.maxObject = (Float) number2;
                    }
                }

            } else {
                throw new IllegalArgumentException("The numbers must not be NaN");
            }
        } else {
            throw new IllegalArgumentException("The numbers must not be null");
        }
    }

    public Number getMinimumNumber() {
        if (this.minObject == null) {
            this.minObject = new Float(this.min);
        }

        return this.minObject;
    }

    public long getMinimumLong() {
        return (long) this.min;
    }

    public int getMinimumInteger() {
        return (int) this.min;
    }

    public double getMinimumDouble() {
        return (double) this.min;
    }

    public float getMinimumFloat() {
        return this.min;
    }

    public Number getMaximumNumber() {
        if (this.maxObject == null) {
            this.maxObject = new Float(this.max);
        }

        return this.maxObject;
    }

    public long getMaximumLong() {
        return (long) this.max;
    }

    public int getMaximumInteger() {
        return (int) this.max;
    }

    public double getMaximumDouble() {
        return (double) this.max;
    }

    public float getMaximumFloat() {
        return this.max;
    }

    public boolean containsNumber(Number number) {
        return number == null ? false : this.containsFloat(number.floatValue());
    }

    public boolean containsFloat(float value) {
        return value >= this.min && value <= this.max;
    }

    public boolean containsRange(Range range) {
        return range == null ? false : this.containsFloat(range.getMinimumFloat()) && this.containsFloat(range.getMaximumFloat());
    }

    public boolean overlapsRange(Range range) {
        return range == null ? false : range.containsFloat(this.min) || range.containsFloat(this.max) || this.containsFloat(range.getMinimumFloat());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof FloatRange)) {
            return false;
        } else {
            FloatRange range = (FloatRange) obj;

            return Float.floatToIntBits(this.min) == Float.floatToIntBits(range.min) && Float.floatToIntBits(this.max) == Float.floatToIntBits(range.max);
        }
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 17;
            this.hashCode = 37 * this.hashCode + this.getClass().hashCode();
            this.hashCode = 37 * this.hashCode + Float.floatToIntBits(this.min);
            this.hashCode = 37 * this.hashCode + Float.floatToIntBits(this.max);
        }

        return this.hashCode;
    }

    public String toString() {
        if (this.toString == null) {
            StringBuffer buf = new StringBuffer(32);

            buf.append("Range[");
            buf.append(this.min);
            buf.append(',');
            buf.append(this.max);
            buf.append(']');
            this.toString = buf.toString();
        }

        return this.toString;
    }
}
