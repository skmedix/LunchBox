package org.apache.commons.io.filefilter;

import java.io.File;

public abstract class AbstractFileFilter implements IOFileFilter {

    public boolean accept(File file) {
        return this.accept(file.getParentFile(), file.getName());
    }

    public boolean accept(File file, String s) {
        return this.accept(new File(file, s));
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
