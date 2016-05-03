package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class SizeFileFilter extends AbstractFileFilter implements Serializable {

    private final long size;
    private final boolean acceptLarger;

    public SizeFileFilter(long i) {
        this(i, true);
    }

    public SizeFileFilter(long i, boolean flag) {
        if (i < 0L) {
            throw new IllegalArgumentException("The size must be non-negative");
        } else {
            this.size = i;
            this.acceptLarger = flag;
        }
    }

    public boolean accept(File file) {
        boolean flag = file.length() < this.size;

        return this.acceptLarger ? !flag : flag;
    }

    public String toString() {
        String s = this.acceptLarger ? ">=" : "<";

        return super.toString() + "(" + s + this.size + ")";
    }
}
