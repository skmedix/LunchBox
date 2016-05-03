package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class CompositeFileComparator extends AbstractFileComparator implements Serializable {

    private static final Comparator[] NO_COMPARATORS = new Comparator[0];
    private final Comparator[] delegates;

    public CompositeFileComparator(Comparator... acomparator) {
        if (acomparator == null) {
            this.delegates = (Comparator[]) CompositeFileComparator.NO_COMPARATORS;
        } else {
            this.delegates = (Comparator[]) (new Comparator[acomparator.length]);
            System.arraycopy(acomparator, 0, this.delegates, 0, acomparator.length);
        }

    }

    public CompositeFileComparator(Iterable iterable) {
        if (iterable == null) {
            this.delegates = (Comparator[]) CompositeFileComparator.NO_COMPARATORS;
        } else {
            ArrayList arraylist = new ArrayList();
            Iterator iterator = iterable.iterator();

            while (iterator.hasNext()) {
                Comparator comparator = (Comparator) iterator.next();

                arraylist.add(comparator);
            }

            this.delegates = (Comparator[]) ((Comparator[]) arraylist.toArray(new Comparator[arraylist.size()]));
        }

    }

    public int compare(File file, File file1) {
        int i = 0;
        Comparator[] acomparator = this.delegates;
        int j = acomparator.length;

        for (int k = 0; k < j; ++k) {
            Comparator comparator = acomparator[k];

            i = comparator.compare(file, file1);
            if (i != 0) {
                break;
            }
        }

        return i;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append('{');

        for (int i = 0; i < this.delegates.length; ++i) {
            if (i > 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.delegates[i]);
        }

        stringbuilder.append('}');
        return stringbuilder.toString();
    }
}
