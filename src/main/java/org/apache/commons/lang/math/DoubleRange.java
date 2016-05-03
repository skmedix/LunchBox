package org.apache.commons.lang.math;

import java.io.Serializable;
import org.apache.commons.lang.text.StrBuilder;

public final class DoubleRange extends Range implements Serializable {

    private static final long serialVersionUID = 71849363892740L;
    private final double min;
    private final double max;
    private transient Double minObject = null;
    private transient Double maxObject = null;
    private transient int hashCode = 0;
    private transient String toString = null;

    public DoubleRange(double number) {
        if (Double.isNaN(number)) {
            throw new IllegalArgumentException("The number must not be NaN");
        } else {
            this.min = number;
            this.max = number;
        }
    }

    public DoubleRange(Number number) {
        if (number == null) {
            throw new IllegalArgumentException("The number must not be null");
        } else {
            this.min = number.doubleValue();
            this.max = number.doubleValue();
            if (!Double.isNaN(this.min) && !Double.isNaN(this.max)) {
                if (number instanceof Double) {
                    this.minObject = (Double) number;
                    this.maxObject = (Double) number;
                }

            } else {
                throw new IllegalArgumentException("The number must not be NaN");
            }
        }
    }

    public DoubleRange(double number1, double number2) {
        if (!Double.isNaN(number1) && !Double.isNaN(number2)) {
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

    public DoubleRange(Number number1, Number number2) {
        if (number1 != null && number2 != null) {
            double number1val = number1.doubleValue();
            double number2val = number2.doubleValue();

            if (!Double.isNaN(number1val) && !Double.isNaN(number2val)) {
                if (number2val < number1val) {
                    this.min = number2val;
                    this.max = number1val;
                    if (number2 instanceof Double) {
                        this.minObject = (Double) number2;
                    }

                    if (number1 instanceof Double) {
                        this.maxObject = (Double) number1;
                    }
                } else {
                    this.min = number1val;
                    this.max = number2val;
                    if (number1 instanceof Double) {
                        this.minObject = (Double) number1;
                    }

                    if (number2 instanceof Double) {
                        this.maxObject = (Double) number2;
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
            this.minObject = new Double(this.min);
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
        return this.min;
    }

    public float getMinimumFloat() {
        return (float) this.min;
    }

    public Number getMaximumNumber() {
        if (this.maxObject == null) {
            this.maxObject = new Double(this.max);
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
        return this.max;
    }

    public float getMaximumFloat() {
        return (float) this.max;
    }

    public boolean containsNumber(Number number) {
        return number == null ? false : this.containsDouble(number.doubleValue());
    }

    public boolean containsDouble(double value) {
        return value >= this.min && value <= this.max;
    }

    public boolean containsRange(Range range) {
        return range == null ? false : this.containsDouble(range.getMinimumDouble()) && this.containsDouble(range.getMaximumDouble());
    }

    public boolean overlapsRange(Range range) {
        return range == null ? false : range.containsDouble(this.min) || range.containsDouble(this.max) || this.containsDouble(range.getMinimumDouble());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof DoubleRange)) {
            return false;
        } else {
            DoubleRange range = (DoubleRange) obj;

            return Double.doubleToLongBits(this.min) == Double.doubleToLongBits(range.min) && Double.doubleToLongBits(this.max) == Double.doubleToLongBits(range.max);
        }
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 17;
            this.hashCode = 37 * this.hashCode + this.getClass().hashCode();
            long lng = Double.doubleToLongBits(this.min);

            this.hashCode = 37 * this.hashCode + (int) (lng ^ lng >> 32);
            lng = Double.doubleToLongBits(this.max);
            this.hashCode = 37 * this.hashCode + (int) (lng ^ lng >> 32);
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
}
