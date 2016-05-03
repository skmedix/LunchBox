package org.apache.commons.lang3.tuple;

public class MutableTriple extends Triple {

    private static final long serialVersionUID = 1L;
    public Object left;
    public Object middle;
    public Object right;

    public static MutableTriple of(Object object, Object object1, Object object2) {
        return new MutableTriple(object, object1, object2);
    }

    public MutableTriple() {}

    public MutableTriple(Object object, Object object1, Object object2) {
        this.left = object;
        this.middle = object1;
        this.right = object2;
    }

    public Object getLeft() {
        return this.left;
    }

    public void setLeft(Object object) {
        this.left = object;
    }

    public Object getMiddle() {
        return this.middle;
    }

    public void setMiddle(Object object) {
        this.middle = object;
    }

    public Object getRight() {
        return this.right;
    }

    public void setRight(Object object) {
        this.right = object;
    }
}
