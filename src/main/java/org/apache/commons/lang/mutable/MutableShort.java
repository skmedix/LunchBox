package org.apache.commons.lang.mutable;

public class MutableShort extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = -2135791679L;
    private short value;

    public MutableShort() {}

    public MutableShort(short value) {
        this.value = value;
    }

    public MutableShort(Number value) {
        this.value = value.shortValue();
    }

    public MutableShort(String value) throws NumberFormatException {
        this.value = Short.parseShort(value);
    }

    public Object getValue() {
        return new Short(this.value);
    }

    public void setValue(short value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.setValue(((Number) value).shortValue());
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(short operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.shortValue();
    }

    public void subtract(short operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.shortValue();
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
        return new Short(this.shortValue());
    }

    public boolean equals(Object obj) {
        return obj instanceof MutableShort ? this.value == ((MutableShort) obj).shortValue() : false;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(Object obj) {
        MutableShort other = (MutableShort) obj;
        short anotherVal = other.value;

        return this.value < anotherVal ? -1 : (this.value == anotherVal ? 0 : 1);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
