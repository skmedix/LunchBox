package org.apache.commons.lang.math;

import java.io.Serializable;
import org.apache.commons.lang.text.StrBuilder;

public final class IntRange extends Range implements Serializable {

    private static final long serialVersionUID = 71849363892730L;
    private final int min;
    private final int max;
    private transient Integer minObject = null;
    private transient Integer maxObject = null;
    private transient int hashCode = 0;
    private transient String toString = null;

    public IntRange(int number) {
        this.min = number;
        this.max = number;
    }

    public IntRange(Number number) {
        if (number == null) {
            throw new IllegalArgumentException("The number must not be null");
        } else {
            this.min = number.intValue();
            this.max = number.intValue();
            if (number instanceof Integer) {
                this.minObject = (Integer) number;
                this.maxObject = (Integer) number;
            }

        }
    }

    public IntRange(int number1, int number2) {
        if (number2 < number1) {
            this.min = number2;
            this.max = number1;
        } else {
            this.min = number1;
            this.max = number2;
        }

    }

    public IntRange(Number number1, Number number2) {
        if (number1 != null && number2 != null) {
            int number1val = number1.intValue();
            int number2val = number2.intValue();

            if (number2val < number1val) {
                this.min = number2val;
                this.max = number1val;
                if (number2 instanceof Integer) {
                    this.minObject = (Integer) number2;
                }

                if (number1 instanceof Integer) {
                    this.maxObject = (Integer) number1;
                }
            } else {
                this.min = number1val;
                this.max = number2val;
                if (number1 instanceof Integer) {
                    this.minObject = (Integer) number1;
                }

                if (number2 instanceof Integer) {
                    this.maxObject = (Integer) number2;
                }
            }

        } else {
            throw new IllegalArgumentException("The numbers must not be null");
        }
    }

    public Number getMinimumNumber() {
        if (this.minObject == null) {
            this.minObject = new Integer(this.min);
        }

        return this.minObject;
    }

    public long getMinimumLong() {
        return (long) this.min;
    }

    public int getMinimumInteger() {
        return this.min;
    }

    public double getMinimumDouble() {
        return (double) this.min;
    }

    public float getMinimumFloat() {
        return (float) this.min;
    }

    public Number getMaximumNumber() {
        if (this.maxObject == null) {
            this.maxObject = new Integer(this.max);
        }

        return this.maxObject;
    }

    public long getMaximumLong() {
        return (long) this.max;
    }

    public int getMaximumInteger() {
        return this.max;
    }

    public double getMaximumDouble() {
        return (double) this.max;
    }

    public float getMaximumFloat() {
        return (float) this.max;
    }

    public boolean containsNumber(Number number) {
        return number == null ? false : this.containsInteger(number.intValue());
    }

    public boolean containsInteger(int value) {
        return value >= this.min && value <= this.max;
    }

    public boolean containsRange(Range range) {
        return range == null ? false : this.containsInteger(range.getMinimumInteger()) && this.containsInteger(range.getMaximumInteger());
    }

    public boolean overlapsRange(Range range) {
        return range == null ? false : range.containsInteger(this.min) || range.containsInteger(this.max) || this.containsInteger(range.getMinimumInteger());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof IntRange)) {
            return false;
        } else {
            IntRange range = (IntRange) obj;

            return this.min == range.min && this.max == range.max;
        }
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 17;
            this.hashCode = 37 * this.hashCode + this.getClass().hashCode();
            this.hashCode = 37 * this.hashCode + this.min;
            this.hashCode = 37 * this.hashCode + this.max;
        }

        return this.hashCode;
    }

    public String toString() {
        if (this.toString == null) {
            StrBuilder buf = new StrBuilder(32);

            buf.append("Range[");
            buf.append(this.min);
            buf.append(',');
            buf.append(this.max);
            buf.append(']');
            this.toString = buf.toString();
        }

        return this.toString;
    }

    public int[] toArray() {
        int[] array = new int[this.max - this.min + 1];

        for (int i = 0; i < array.length; ++i) {
            array[i] = this.min + i;
        }

        return array;
    }
}
