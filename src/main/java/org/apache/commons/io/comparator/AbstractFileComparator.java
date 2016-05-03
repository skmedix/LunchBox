package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

abstract class AbstractFileComparator implements Comparator {

    public File[] sort(File... afile) {
        if (afile != null) {
            Arrays.sort(afile, this);
        }

        return afile;
    }

    public List sort(List list) {
        if (list != null) {
            Collections.sort(list, this);
        }

        return list;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
