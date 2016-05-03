package org.apache.commons.lang3.mutable;

public class MutableDouble extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = 1587163916L;
    private double value;

    public MutableDouble() {}

    public MutableDouble(double d0) {
        this.value = d0;
    }

    public MutableDouble(Number number) {
        this.value = number.doubleValue();
    }

    public MutableDouble(String s) throws NumberFormatException {
        this.value = Double.parseDouble(s);
    }

    public Double getValue() {
        return Double.valueOf(this.value);
    }

    public void setValue(double d0) {
        this.value = d0;
    }

    public void setValue(Number number) {
        this.value = number.doubleValue();
    }

    public boolean isNaN() {
        return Double.isNaN(this.value);
    }

    public boolean isInfinite() {
        return Double.isInfinite(this.value);
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(double d0) {
        this.value += d0;
    }

    public void add(Number number) {
        this.value += number.doubleValue();
    }

    public void subtract(double d0) {
        this.value -= d0;
    }

    public void subtract(Number number) {
        this.value -= number.doubleValue();
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return this.value;
    }

    public Double toDouble() {
        return Double.valueOf(this.doubleValue());
    }

    public boolean equals(Object object) {
        return object instanceof MutableDouble && Double.doubleToLongBits(((MutableDouble) object).value) == Double.doubleToLongBits(this.value);
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.value);

        return (int) (i ^ i >>> 32);
    }

    public int compareTo(MutableDouble mutabledouble) {
        double d0 = mutabledouble.value;

        return Double.compare(this.value, d0);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
