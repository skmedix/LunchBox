package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;

public class DelegateFileFilter extends AbstractFileFilter implements Serializable {

    private final FilenameFilter filenameFilter;
    private final FileFilter fileFilter;

    public DelegateFileFilter(FilenameFilter filenamefilter) {
        if (filenamefilter == null) {
            throw new IllegalArgumentException("The FilenameFilter must not be null");
        } else {
            this.filenameFilter = filenamefilter;
            this.fileFilter = null;
        }
    }

    public DelegateFileFilter(FileFilter filefilter) {
        if (filefilter == null) {
            throw new IllegalArgumentException("The FileFilter must not be null");
        } else {
            this.fileFilter = filefilter;
            this.filenameFilter = null;
        }
    }

    public boolean accept(File file) {
        return this.fileFilter != null ? this.fileFilter.accept(file) : super.accept(file);
    }

    public boolean accept(File file, String s) {
        return this.filenameFilter != null ? this.filenameFilter.accept(file, s) : super.accept(file, s);
    }

    public String toString() {
        String s = this.fileFilter != null ? this.fileFilter.toString() : this.filenameFilter.toString();

        return super.toString() + "(" + s + ")";
    }
}
