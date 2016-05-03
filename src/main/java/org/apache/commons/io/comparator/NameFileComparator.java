package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.IOCase;

public class NameFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator NAME_COMPARATOR = new NameFileComparator();
    public static final Comparator NAME_REVERSE = new ReverseComparator(NameFileComparator.NAME_COMPARATOR);
    public static final Comparator NAME_INSENSITIVE_COMPARATOR = new NameFileComparator(IOCase.INSENSITIVE);
    public static final Comparator NAME_INSENSITIVE_REVERSE = new ReverseComparator(NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
    public static final Comparator NAME_SYSTEM_COMPARATOR = new NameFileComparator(IOCase.SYSTEM);
    public static final Comparator NAME_SYSTEM_REVERSE = new ReverseComparator(NameFileComparator.NAME_SYSTEM_COMPARATOR);
    private final IOCase caseSensitivity;

    public NameFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    public NameFileComparator(IOCase iocase) {
        this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
    }

    public int compare(File file, File file1) {
        return this.caseSensitivity.checkCompareTo(file.getName(), file1.getName());
    }

    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
}
