package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Triple implements Comparable, Serializable {

    private static final long serialVersionUID = 1L;

    public static Triple of(Object object, Object object1, Object object2) {
        return new ImmutableTriple(object, object1, object2);
    }

    public abstract Object getLeft();

    public abstract Object getMiddle();

    public abstract Object getRight();

    public int compareTo(Triple triple) {
        return (new CompareToBuilder()).append(this.getLeft(), triple.getLeft()).append(this.getMiddle(), triple.getMiddle()).append(this.getRight(), triple.getRight()).toComparison();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof Triple)) {
            return false;
        } else {
            Triple triple = (Triple) object;

            return ObjectUtils.equals(this.getLeft(), triple.getLeft()) && ObjectUtils.equals(this.getMiddle(), triple.getMiddle()) && ObjectUtils.equals(this.getRight(), triple.getRight());
        }
    }

    public int hashCode() {
        return (this.getLeft() == null ? 0 : this.getLeft().hashCode()) ^ (this.getMiddle() == null ? 0 : this.getMiddle().hashCode()) ^ (this.getRight() == null ? 0 : this.getRight().hashCode());
    }

    public String toString() {
        return "" + '(' + this.getLeft() + ',' + this.getMiddle() + ',' + this.getRight() + ')';
    }

    public String toString(String s) {
        return String.format(s, new Object[] { this.getLeft(), this.getMiddle(), this.getRight()});
    }
}
