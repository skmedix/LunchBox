package org.apache.commons.lang.mutable;

public class MutableLong extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = 62986528375L;
    private long value;

    public MutableLong() {}

    public MutableLong(long value) {
        this.value = value;
    }

    public MutableLong(Number value) {
        this.value = value.longValue();
    }

    public MutableLong(String value) throws NumberFormatException {
        this.value = Long.parseLong(value);
    }

    public Object getValue() {
        return new Long(this.value);
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.setValue(((Number) value).longValue());
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(long operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.longValue();
    }

    public void subtract(long operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.longValue();
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
        return new Long(this.longValue());
    }

    public boolean equals(Object obj) {
        return obj instanceof MutableLong ? this.value == ((MutableLong) obj).longValue() : false;
    }

    public int hashCode() {
        return (int) (this.value ^ this.value >>> 32);
    }

    public int compareTo(Object obj) {
        MutableLong other = (MutableLong) obj;
        long anotherVal = other.value;

        return this.value < anotherVal ? -1 : (this.value == anotherVal ? 0 : 1);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
