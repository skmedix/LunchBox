package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Comparator;

public final class Range implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Comparator comparator;
    private final Object minimum;
    private final Object maximum;
    private transient int hashCode;
    private transient String toString;

    public static Range is(Comparable comparable) {
        return between(comparable, comparable, (Comparator) null);
    }

    public static Range is(Object object, Comparator comparator) {
        return between(object, object, comparator);
    }

    public static Range between(Comparable comparable, Comparable comparable1) {
        return between(comparable, comparable1, (Comparator) null);
    }

    public static Range between(Object object, Object object1, Comparator comparator) {
        return new Range(object, object1, comparator);
    }

    private Range(Object object, Object object1, Comparator comparator) {
        if (object != null && object1 != null) {
            if (comparator == null) {
                this.comparator = Range.ComparableComparator.INSTANCE;
            } else {
                this.comparator = comparator;
            }

            if (this.comparator.compare(object, object1) < 1) {
                this.minimum = object;
                this.maximum = object1;
            } else {
                this.minimum = object1;
                this.maximum = object;
            }

        } else {
            throw new IllegalArgumentException("Elements in a range must not be null: element1=" + object + ", element2=" + object1);
        }
    }

    public Object getMinimum() {
        return this.minimum;
    }

    public Object getMaximum() {
        return this.maximum;
    }

    public Comparator getComparator() {
        return this.comparator;
    }

    public boolean isNaturalOrdering() {
        return this.comparator == Range.ComparableComparator.INSTANCE;
    }

    public boolean contains(Object object) {
        return object == null ? false : this.comparator.compare(object, this.minimum) > -1 && this.comparator.compare(object, this.maximum) < 1;
    }

    public boolean isAfter(Object object) {
        return object == null ? false : this.comparator.compare(object, this.minimum) < 0;
    }

    public boolean isStartedBy(Object object) {
        return object == null ? false : this.comparator.compare(object, this.minimum) == 0;
    }

    public boolean isEndedBy(Object object) {
        return object == null ? false : this.comparator.compare(object, this.maximum) == 0;
    }

    public boolean isBefore(Object object) {
        return object == null ? false : this.comparator.compare(object, this.maximum) > 0;
    }

    public int elementCompareTo(Object object) {
        if (object == null) {
            throw new NullPointerException("Element is null");
        } else {
            return this.isAfter(object) ? -1 : (this.isBefore(object) ? 1 : 0);
        }
    }

    public boolean containsRange(Range range) {
        return range == null ? false : this.contains(range.minimum) && this.contains(range.maximum);
    }

    public boolean isAfterRange(Range range) {
        return range == null ? false : this.isAfter(range.maximum);
    }

    public boolean isOverlappedBy(Range range) {
        return range == null ? false : range.contains(this.minimum) || range.contains(this.maximum) || this.contains(range.minimum);
    }

    public boolean isBeforeRange(Range range) {
        return range == null ? false : this.isBefore(range.minimum);
    }

    public Range intersectionWith(Range range) {
        if (!this.isOverlappedBy(range)) {
            throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", new Object[] { range}));
        } else if (this.equals(range)) {
            return this;
        } else {
            Object object = this.getComparator().compare(this.minimum, range.minimum) < 0 ? range.minimum : this.minimum;
            Object object1 = this.getComparator().compare(this.maximum, range.maximum) < 0 ? this.maximum : range.maximum;

            return between(object, object1, this.getComparator());
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (object != null && object.getClass() == this.getClass()) {
            Range range = (Range) object;

            return this.minimum.equals(range.minimum) && this.maximum.equals(range.maximum);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = this.hashCode;

        if (this.hashCode == 0) {
            byte b0 = 17;

            i = 37 * b0 + this.getClass().hashCode();
            i = 37 * i + this.minimum.hashCode();
            i = 37 * i + this.maximum.hashCode();
            this.hashCode = i;
        }

        return i;
    }

    public String toString() {
        String s = this.toString;

        if (s == null) {
            StringBuilder stringbuilder = new StringBuilder(32);

            stringbuilder.append('[');
            stringbuilder.append(this.minimum);
            stringbuilder.append("..");
            stringbuilder.append(this.maximum);
            stringbuilder.append(']');
            s = stringbuilder.toString();
            this.toString = s;
        }

        return s;
    }

    public String toString(String s) {
        return String.format(s, new Object[] { this.minimum, this.maximum, this.comparator});
    }

    private static enum ComparableComparator implements Comparator {

        INSTANCE;

        public int compare(Object object, Object object1) {
            return ((Comparable) object).compareTo(object1);
        }
    }
}
