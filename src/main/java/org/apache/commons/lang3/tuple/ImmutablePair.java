package org.apache.commons.lang3.tuple;

public final class ImmutablePair extends Pair {

    private static final long serialVersionUID = 4954918890077093841L;
    public final Object left;
    public final Object right;

    public static ImmutablePair of(Object object, Object object1) {
        return new ImmutablePair(object, object1);
    }

    public ImmutablePair(Object object, Object object1) {
        this.left = object;
        this.right = object1;
    }

    public Object getLeft() {
        return this.left;
    }

    public Object getRight() {
        return this.right;
    }

    public Object setValue(Object object) {
        throw new UnsupportedOperationException();
    }
}
