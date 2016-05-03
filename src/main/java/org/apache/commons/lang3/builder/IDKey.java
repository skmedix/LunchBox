package org.apache.commons.lang3.builder;

final class IDKey {

    private final Object value;
    private final int id;

    public IDKey(Object object) {
        this.id = System.identityHashCode(object);
        this.value = object;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object object) {
        if (!(object instanceof IDKey)) {
            return false;
        } else {
            IDKey idkey = (IDKey) object;

            return this.id != idkey.id ? false : this.value == idkey.value;
        }
    }
}
