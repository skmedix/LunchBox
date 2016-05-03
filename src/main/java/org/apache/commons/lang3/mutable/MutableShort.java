package org.apache.commons.lang3.mutable;

public class MutableShort extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = -2135791679L;
    private short value;

    public MutableShort() {}

    public MutableShort(short short0) {
        this.value = short0;
    }

    public MutableShort(Number number) {
        this.value = number.shortValue();
    }

    public MutableShort(String s) throws NumberFormatException {
        this.value = Short.parseShort(s);
    }

    public Short getValue() {
        return Short.valueOf(this.value);
    }

    public void setValue(short short0) {
        this.value = short0;
    }

    public void setValue(Number number) {
        this.value = number.shortValue();
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(short short0) {
        this.value += short0;
    }

    public void add(Number number) {
        this.value += number.shortValue();
    }

    public void subtract(short short0) {
        this.value -= short0;
    }

    public void subtract(Number number) {
        this.value -= number.shortValue();
    }

    public short shortValue() {
        return this.value;
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public Short toShort() {
        return Short.valueOf(this.shortValue());
    }

    public boolean equals(Object object) {
        return object instanceof MutableShort ? this.value == ((MutableShort) object).shortValue() : false;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(MutableShort mutableshort) {
        short short0 = mutableshort.value;

        return this.value < short0 ? -1 : (this.value == short0 ? 0 : 1);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
