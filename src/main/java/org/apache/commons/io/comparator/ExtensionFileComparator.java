package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

public class ExtensionFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator EXTENSION_COMPARATOR = new ExtensionFileComparator();
    public static final Comparator EXTENSION_REVERSE = new ReverseComparator(ExtensionFileComparator.EXTENSION_COMPARATOR);
    public static final Comparator EXTENSION_INSENSITIVE_COMPARATOR = new ExtensionFileComparator(IOCase.INSENSITIVE);
    public static final Comparator EXTENSION_INSENSITIVE_REVERSE = new ReverseComparator(ExtensionFileComparator.EXTENSION_INSENSITIVE_COMPARATOR);
    public static final Comparator EXTENSION_SYSTEM_COMPARATOR = new ExtensionFileComparator(IOCase.SYSTEM);
    public static final Comparator EXTENSION_SYSTEM_REVERSE = new ReverseComparator(ExtensionFileComparator.EXTENSION_SYSTEM_COMPARATOR);
    private final IOCase caseSensitivity;

    public ExtensionFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    public ExtensionFileComparator(IOCase iocase) {
        this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
    }

    public int compare(File file, File file1) {
        String s = FilenameUtils.getExtension(file.getName());
        String s1 = FilenameUtils.getExtension(file1.getName());

        return this.caseSensitivity.checkCompareTo(s, s1);
    }

    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
}
