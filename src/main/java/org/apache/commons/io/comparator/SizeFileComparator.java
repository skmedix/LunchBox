package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.FileUtils;

public class SizeFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator SIZE_COMPARATOR = new SizeFileComparator();
    public static final Comparator SIZE_REVERSE = new ReverseComparator(SizeFileComparator.SIZE_COMPARATOR);
    public static final Comparator SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);
    public static final Comparator SIZE_SUMDIR_REVERSE = new ReverseComparator(SizeFileComparator.SIZE_SUMDIR_COMPARATOR);
    private final boolean sumDirectoryContents;

    public SizeFileComparator() {
        this.sumDirectoryContents = false;
    }

    public SizeFileComparator(boolean flag) {
        this.sumDirectoryContents = flag;
    }

    public int compare(File file, File file1) {
        long i = 0L;

        if (file.isDirectory()) {
            i = this.sumDirectoryContents && file.exists() ? FileUtils.sizeOfDirectory(file) : 0L;
        } else {
            i = file.length();
        }

        long j = 0L;

        if (file1.isDirectory()) {
            j = this.sumDirectoryContents && file1.exists() ? FileUtils.sizeOfDirectory(file1) : 0L;
        } else {
            j = file1.length();
        }

        long k = i - j;

        return k < 0L ? -1 : (k > 0L ? 1 : 0);
    }

    public String toString() {
        return super.toString() + "[sumDirectoryContents=" + this.sumDirectoryContents + "]";
    }
}
