package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class DirectoryFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator DIRECTORY_COMPARATOR = new DirectoryFileComparator();
    public static final Comparator DIRECTORY_REVERSE = new ReverseComparator(DirectoryFileComparator.DIRECTORY_COMPARATOR);

    public int compare(File file, File file1) {
        return this.getType(file) - this.getType(file1);
    }

    private int getType(File file) {
        return file.isDirectory() ? 1 : 2;
    }
}
