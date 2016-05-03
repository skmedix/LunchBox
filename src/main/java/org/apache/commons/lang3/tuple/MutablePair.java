package org.apache.commons.lang3.tuple;

public class MutablePair extends Pair {

    private static final long serialVersionUID = 4954918890077093841L;
    public Object left;
    public Object right;

    public static MutablePair of(Object object, Object object1) {
        return new MutablePair(object, object1);
    }

    public MutablePair() {}

    public MutablePair(Object object, Object object1) {
        this.left = object;
        this.right = object1;
    }

    public Object getLeft() {
        return this.left;
    }

    public void setLeft(Object object) {
        this.left = object;
    }

    public Object getRight() {
        return this.right;
    }

    public void setRight(Object object) {
        this.right = object;
    }

    public Object setValue(Object object) {
        Object object1 = this.getRight();

        this.setRight(object);
        return object1;
    }
}
