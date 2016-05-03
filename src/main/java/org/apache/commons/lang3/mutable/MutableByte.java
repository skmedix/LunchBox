package org.apache.commons.lang3.mutable;

public class MutableByte extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = -1585823265L;
    private byte value;

    public MutableByte() {}

    public MutableByte(byte b0) {
        this.value = b0;
    }

    public MutableByte(Number number) {
        this.value = number.byteValue();
    }

    public MutableByte(String s) throws NumberFormatException {
        this.value = Byte.parseByte(s);
    }

    public Byte getValue() {
        return Byte.valueOf(this.value);
    }

    public void setValue(byte b0) {
        this.value = b0;
    }

    public void setValue(Number number) {
        this.value = number.byteValue();
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(byte b0) {
        this.value += b0;
    }

    public void add(Number number) {
        this.value += number.byteValue();
    }

    public void subtract(byte b0) {
        this.value -= b0;
    }

    public void subtract(Number number) {
        this.value -= number.byteValue();
    }

    public byte byteValue() {
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

    public Byte toByte() {
        return Byte.valueOf(this.byteValue());
    }

    public boolean equals(Object object) {
        return object instanceof MutableByte ? this.value == ((MutableByte) object).byteValue() : false;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(MutableByte mutablebyte) {
        byte b0 = mutablebyte.value;

        return this.value < b0 ? -1 : (this.value == b0 ? 0 : 1);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
