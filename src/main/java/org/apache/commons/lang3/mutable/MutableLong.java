package org.apache.commons.lang3.mutable;

public class MutableLong extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = 62986528375L;
    private long value;

    public MutableLong() {}

    public MutableLong(long i) {
        this.value = i;
    }

    public MutableLong(Number number) {
        this.value = number.longValue();
    }

    public MutableLong(String s) throws NumberFormatException {
        this.value = Long.parseLong(s);
    }

    public Long getValue() {
        return Long.valueOf(this.value);
    }

    public void setValue(long i) {
        this.value = i;
    }

    public void setValue(Number number) {
        this.value = number.longValue();
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(long i) {
        this.value += i;
    }

    public void add(Number number) {
        this.value += number.longValue();
    }

    public void subtract(long i) {
        this.value -= i;
    }

    public void subtract(Number number) {
        this.value -= number.longValue();
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public Long toLong() {
        return Long.valueOf(this.longValue());
    }

    public boolean equals(Object object) {
        return object instanceof MutableLong ? this.value == ((MutableLong) object).longValue() : false;
    }

    public int hashCode() {
        return (int) (this.value ^ this.value >>> 32);
    }

    public int compareTo(MutableLong mutablelong) {
        long i = mutablelong.value;

        return this.value < i ? -1 : (this.value == i ? 0 : 1);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
