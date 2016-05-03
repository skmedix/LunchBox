package org.apache.commons.lang3.mutable;

public class MutableFloat extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = 5787169186L;
    private float value;

    public MutableFloat() {}

    public MutableFloat(float f) {
        this.value = f;
    }

    public MutableFloat(Number number) {
        this.value = number.floatValue();
    }

    public MutableFloat(String s) throws NumberFormatException {
        this.value = Float.parseFloat(s);
    }

    public Float getValue() {
        return Float.valueOf(this.value);
    }

    public void setValue(float f) {
        this.value = f;
    }

    public void setValue(Number number) {
        this.value = number.floatValue();
    }

    public boolean isNaN() {
        return Float.isNaN(this.value);
    }

    public boolean isInfinite() {
        return Float.isInfinite(this.value);
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(float f) {
        this.value += f;
    }

    public void add(Number number) {
        this.value += number.floatValue();
    }

    public void subtract(float f) {
        this.value -= f;
    }

    public void subtract(Number number) {
        this.value -= number.floatValue();
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public Float toFloat() {
        return Float.valueOf(this.floatValue());
    }

    public boolean equals(Object object) {
        return object instanceof MutableFloat && Float.floatToIntBits(((MutableFloat) object).value) == Float.floatToIntBits(this.value);
    }

    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }

    public int compareTo(MutableFloat mutablefloat) {
        float f = mutablefloat.value;

        return Float.compare(this.value, f);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
