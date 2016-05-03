package org.apache.commons.lang3.mutable;

public class MutableInt extends Number implements Comparable, Mutable {

    private static final long serialVersionUID = 512176391864L;
    private int value;

    public MutableInt() {}

    public MutableInt(int i) {
        this.value = i;
    }

    public MutableInt(Number number) {
        this.value = number.intValue();
    }

    public MutableInt(String s) throws NumberFormatException {
        this.value = Integer.parseInt(s);
    }

    public Integer getValue() {
        return Integer.valueOf(this.value);
    }

    public void setValue(int i) {
        this.value = i;
    }

    public void setValue(Number number) {
        this.value = number.intValue();
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public void add(int i) {
        this.value += i;
    }

    public void add(Number number) {
        this.value += number.intValue();
    }

    public void subtract(int i) {
        this.value -= i;
    }

    public void subtract(Number number) {
        this.value -= number.intValue();
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

    public Integer toInteger() {
        return Integer.valueOf(this.intValue());
    }

    public boolean equals(Object object) {
        return object instanceof MutableInt ? this.value == ((MutableInt) object).intValue() : false;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(MutableInt mutableint) {
        int i = mutableint.value;

        return this.value < i ? -1 : (this.value == i ? 0 : 1);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
