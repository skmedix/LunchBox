package org.apache.commons.lang3.builder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DiffResult implements Iterable {

    public static final String OBJECTS_SAME_STRING = "";
    private static final String DIFFERS_STRING = "differs from";
    private final List diffs;
    private final Object lhs;
    private final Object rhs;
    private final ToStringStyle style;

    DiffResult(Object object, Object object1, List list, ToStringStyle tostringstyle) {
        if (object == null) {
            throw new IllegalArgumentException("Left hand object cannot be null");
        } else if (object1 == null) {
            throw new IllegalArgumentException("Right hand object cannot be null");
        } else if (list == null) {
            throw new IllegalArgumentException("List of differences cannot be null");
        } else {
            this.diffs = list;
            this.lhs = object;
            this.rhs = object1;
            if (tostringstyle == null) {
                this.style = ToStringStyle.DEFAULT_STYLE;
            } else {
                this.style = tostringstyle;
            }

        }
    }

    public List getDiffs() {
        return Collections.unmodifiableList(this.diffs);
    }

    public int getNumberOfDiffs() {
        return this.diffs.size();
    }

    public ToStringStyle getToStringStyle() {
        return this.style;
    }

    public String toString() {
        return this.toString(this.style);
    }

    public String toString(ToStringStyle tostringstyle) {
        if (this.diffs.size() == 0) {
            return "";
        } else {
            ToStringBuilder tostringbuilder = new ToStringBuilder(this.lhs, tostringstyle);
            ToStringBuilder tostringbuilder1 = new ToStringBuilder(this.rhs, tostringstyle);
            Iterator iterator = this.diffs.iterator();

            while (iterator.hasNext()) {
                Diff diff = (Diff) iterator.next();

                tostringbuilder.append(diff.getFieldName(), diff.getLeft());
                tostringbuilder1.append(diff.getFieldName(), diff.getRight());
            }

            return String.format("%s %s %s", new Object[] { tostringbuilder.build(), "differs from", tostringbuilder1.build()});
        }
    }

    public Iterator iterator() {
        return this.diffs.iterator();
    }
}
