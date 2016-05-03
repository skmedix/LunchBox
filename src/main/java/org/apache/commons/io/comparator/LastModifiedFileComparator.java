package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class LastModifiedFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator LASTMODIFIED_COMPARATOR = new LastModifiedFileComparator();
    public static final Comparator LASTMODIFIED_REVERSE = new ReverseComparator(LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

    public int compare(File file, File file1) {
        long i = file.lastModified() - file1.lastModified();

        return i < 0L ? -1 : (i > 0L ? 1 : 0);
    }
}
