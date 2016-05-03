package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Map.Entry;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Pair implements Entry, Comparable, Serializable {

    private static final long serialVersionUID = 4954918890077093841L;

    public static Pair of(Object object, Object object1) {
        return new ImmutablePair(object, object1);
    }

    public abstract Object getLeft();

    public abstract Object getRight();

    public final Object getKey() {
        return this.getLeft();
    }

    public Object getValue() {
        return this.getRight();
    }

    public int compareTo(Pair pair) {
        return (new CompareToBuilder()).append(this.getLeft(), pair.getLeft()).append(this.getRight(), pair.getRight()).toComparison();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof Entry)) {
            return false;
        } else {
            Entry entry = (Entry) object;

            return ObjectUtils.equals(this.getKey(), entry.getKey()) && ObjectUtils.equals(this.getValue(), entry.getValue());
        }
    }

    public int hashCode() {
        return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
    }

    public String toString() {
        return "" + '(' + this.getLeft() + ',' + this.getRight() + ')';
    }

    public String toString(String s) {
        return String.format(s, new Object[] { this.getLeft(), this.getRight()});
    }
}
