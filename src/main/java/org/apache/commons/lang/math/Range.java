package org.apache.commons.lang.math;

import org.apache.commons.lang.text.StrBuilder;

public abstract class Range {

    public abstract Number getMinimumNumber();

    public long getMinimumLong() {
        return this.getMinimumNumber().longValue();
    }

    public int getMinimumInteger() {
        return this.getMinimumNumber().intValue();
    }

    public double getMinimumDouble() {
        return this.getMinimumNumber().doubleValue();
    }

    public float getMinimumFloat() {
        return this.getMinimumNumber().floatValue();
    }

    public abstract Number getMaximumNumber();

    public long getMaximumLong() {
        return this.getMaximumNumber().longValue();
    }

    public int getMaximumInteger() {
        return this.getMaximumNumber().intValue();
    }

    public double getMaximumDouble() {
        return this.getMaximumNumber().doubleValue();
    }

    public float getMaximumFloat() {
        return this.getMaximumNumber().floatValue();
    }

    public abstract boolean containsNumber(Number number);

    public boolean containsLong(Number value) {
        return value == null ? false : this.containsLong(value.longValue());
    }

    public boolean containsLong(long value) {
        return value >= this.getMinimumLong() && value <= this.getMaximumLong();
    }

    public boolean containsInteger(Number value) {
        return value == null ? false : this.containsInteger(value.intValue());
    }

    public boolean containsInteger(int value) {
        return value >= this.getMinimumInteger() && value <= this.getMaximumInteger();
    }

    public boolean containsDouble(Number value) {
        return value == null ? false : this.containsDouble(value.doubleValue());
    }

    public boolean containsDouble(double value) {
        int compareMin = NumberUtils.compare(this.getMinimumDouble(), value);
        int compareMax = NumberUtils.compare(this.getMaximumDouble(), value);

        return compareMin <= 0 && compareMax >= 0;
    }

    public boolean containsFloat(Number value) {
        return value == null ? false : this.containsFloat(value.floatValue());
    }

    public boolean containsFloat(float value) {
        int compareMin = NumberUtils.compare(this.getMinimumFloat(), value);
        int compareMax = NumberUtils.compare(this.getMaximumFloat(), value);

        return compareMin <= 0 && compareMax >= 0;
    }

    public boolean containsRange(Range range) {
        return range == null ? false : this.containsNumber(range.getMinimumNumber()) && this.containsNumber(range.getMaximumNumber());
    }

    public boolean overlapsRange(Range range) {
        return range == null ? false : range.containsNumber(this.getMinimumNumber()) || range.containsNumber(this.getMaximumNumber()) || this.containsNumber(range.getMinimumNumber());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            Range range = (Range) obj;

            return this.getMinimumNumber().equals(range.getMinimumNumber()) && this.getMaximumNumber().equals(range.getMaximumNumber());
        } else {
            return false;
        }
    }

    public int hashCode() {
        byte result = 17;
        int result1 = 37 * result + this.getClass().hashCode();

        result1 = 37 * result1 + this.getMinimumNumber().hashCode();
        result1 = 37 * result1 + this.getMaximumNumber().hashCode();
        return result1;
    }

    public String toString() {
        StrBuilder buf = new StrBuilder(32);

        buf.append("Range[");
        buf.append((Object) this.getMinimumNumber());
        buf.append(',');
        buf.append((Object) this.getMaximumNumber());
        buf.append(']');
        return buf.toString();
    }
}
