package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class DefaultFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator DEFAULT_COMPARATOR = new DefaultFileComparator();
    public static final Comparator DEFAULT_REVERSE = new ReverseComparator(DefaultFileComparator.DEFAULT_COMPARATOR);

    public int compare(File file, File file1) {
        return file.compareTo(file1);
    }
}
