package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

class ReverseComparator extends AbstractFileComparator implements Serializable {

    private final Comparator delegate;

    public ReverseComparator(Comparator comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Delegate comparator is missing");
        } else {
            this.delegate = comparator;
        }
    }

    public int compare(File file, File file1) {
        return this.delegate.compare(file1, file);
    }

    public String toString() {
        return super.toString() + "[" + this.delegate.toString() + "]";
    }
}
