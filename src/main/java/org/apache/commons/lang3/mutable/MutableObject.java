package org.apache.commons.lang3.mutable;

import java.io.Serializable;

public class MutableObject implements Mutable, Serializable {

    private static final long serialVersionUID = 86241875189L;
    private Object value;

    public MutableObject() {}

    public MutableObject(Object object) {
        this.value = object;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object object) {
        this.value = object;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (this == object) {
            return true;
        } else if (this.getClass() == object.getClass()) {
            MutableObject mutableobject = (MutableObject) object;

            return this.value.equals(mutableobject.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.value == null ? 0 : this.value.hashCode();
    }

    public String toString() {
        return this.value == null ? "null" : this.value.toString();
    }
}
