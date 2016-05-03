package org.apache.commons.lang3.mutable;

import java.io.Serializable;

public class MutableBoolean implements Mutable, Serializable, Comparable {

    private static final long serialVersionUID = -4830728138360036487L;
    private boolean value;

    public MutableBoolean() {}

    public MutableBoolean(boolean flag) {
        this.value = flag;
    }

    public MutableBoolean(Boolean obool) {
        this.value = obool.booleanValue();
    }

    public Boolean getValue() {
        return Boolean.valueOf(this.value);
    }

    public void setValue(boolean flag) {
        this.value = flag;
    }

    public void setFalse() {
        this.value = false;
    }

    public void setTrue() {
        this.value = true;
    }

    public void setValue(Boolean obool) {
        this.value = obool.booleanValue();
    }

    public boolean isTrue() {
        return this.value;
    }

    public boolean isFalse() {
        return !this.value;
    }

    public boolean booleanValue() {
        return this.value;
    }

    public Boolean toBoolean() {
        return Boolean.valueOf(this.booleanValue());
    }

    public boolean equals(Object object) {
        return object instanceof MutableBoolean ? this.value == ((MutableBoolean) object).booleanValue() : false;
    }

    public int hashCode() {
        return this.value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    public int compareTo(MutableBoolean mutableboolean) {
        boolean flag = mutableboolean.value;

        return this.value == flag ? 0 : (this.value ? 1 : -1);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
