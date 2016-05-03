package org.apache.commons.lang3.tuple;

public final class ImmutableTriple extends Triple {

    private static final long serialVersionUID = 1L;
    public final Object left;
    public final Object middle;
    public final Object right;

    public static ImmutableTriple of(Object object, Object object1, Object object2) {
        return new ImmutableTriple(object, object1, object2);
    }

    public ImmutableTriple(Object object, Object object1, Object object2) {
        this.left = object;
        this.middle = object1;
        this.right = object2;
    }

    public Object getLeft() {
        return this.left;
    }

    public Object getMiddle() {
        return this.middle;
    }

    public Object getRight() {
        return this.right;
    }
}
