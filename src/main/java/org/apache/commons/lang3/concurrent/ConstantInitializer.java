package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.ObjectUtils;

public class ConstantInitializer implements ConcurrentInitializer {

    private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";
    private final Object object;

    public ConstantInitializer(Object object) {
        this.object = object;
    }

    public final Object getObject() {
        return this.object;
    }

    public Object get() throws ConcurrentException {
        return this.getObject();
    }

    public int hashCode() {
        return this.getObject() != null ? this.getObject().hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ConstantInitializer)) {
            return false;
        } else {
            ConstantInitializer constantinitializer = (ConstantInitializer) object;

            return ObjectUtils.equals(this.getObject(), constantinitializer.getObject());
        }
    }

    public String toString() {
        return String.format("ConstantInitializer@%d [ object = %s ]", new Object[] { Integer.valueOf(System.identityHashCode(this)), String.valueOf(this.getObject())});
    }
}
