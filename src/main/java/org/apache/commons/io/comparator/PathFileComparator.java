package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.IOCase;

public class PathFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator PATH_COMPARATOR = new PathFileComparator();
    public static final Comparator PATH_REVERSE = new ReverseComparator(PathFileComparator.PATH_COMPARATOR);
    public static final Comparator PATH_INSENSITIVE_COMPARATOR = new PathFileComparator(IOCase.INSENSITIVE);
    public static final Comparator PATH_INSENSITIVE_REVERSE = new ReverseComparator(PathFileComparator.PATH_INSENSITIVE_COMPARATOR);
    public static final Comparator PATH_SYSTEM_COMPARATOR = new PathFileComparator(IOCase.SYSTEM);
    public static final Comparator PATH_SYSTEM_REVERSE = new ReverseComparator(PathFileComparator.PATH_SYSTEM_COMPARATOR);
    private final IOCase caseSensitivity;

    public PathFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    public PathFileComparator(IOCase iocase) {
        this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
    }

    public int compare(File file, File file1) {
        return this.caseSensitivity.checkCompareTo(file.getPath(), file1.getPath());
    }

    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
}
