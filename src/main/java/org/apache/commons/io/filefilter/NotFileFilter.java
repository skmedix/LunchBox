package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class NotFileFilter extends AbstractFileFilter implements Serializable {

    private final IOFileFilter filter;

    public NotFileFilter(IOFileFilter iofilefilter) {
        if (iofilefilter == null) {
            throw new IllegalArgumentException("The filter must not be null");
        } else {
            this.filter = iofilefilter;
        }
    }

    public boolean accept(File file) {
        return !this.filter.accept(file);
    }

    public boolean accept(File file, String s) {
        return !this.filter.accept(file, s);
    }

    public String toString() {
        return super.toString() + "(" + this.filter.toString() + ")";
    }
}
