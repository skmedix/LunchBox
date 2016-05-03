package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class EmptyFileFilter extends AbstractFileFilter implements Serializable {

    public static final IOFileFilter EMPTY = new EmptyFileFilter();
    public static final IOFileFilter NOT_EMPTY = new NotFileFilter(EmptyFileFilter.EMPTY);

    public boolean accept(File file) {
        if (!file.isDirectory()) {
            return file.length() == 0L;
        } else {
            File[] afile = file.listFiles();

            return afile == null || afile.length == 0;
        }
    }
}
