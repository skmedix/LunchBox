package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class AgeFileFilter extends AbstractFileFilter implements Serializable {

    private final long cutoff;
    private final boolean acceptOlder;

    public AgeFileFilter(long i) {
        this(i, true);
    }

    public AgeFileFilter(long i, boolean flag) {
        this.acceptOlder = flag;
        this.cutoff = i;
    }

    public AgeFileFilter(Date date) {
        this(date, true);
    }

    public AgeFileFilter(Date date, boolean flag) {
        this(date.getTime(), flag);
    }

    public AgeFileFilter(File file) {
        this(file, true);
    }

    public AgeFileFilter(File file, boolean flag) {
        this(file.lastModified(), flag);
    }

    public boolean accept(File file) {
        boolean flag = FileUtils.isFileNewer(file, this.cutoff);

        return this.acceptOlder ? !flag : flag;
    }

    public String toString() {
        String s = this.acceptOlder ? "<=" : ">";

        return super.toString() + "(" + s + this.cutoff + ")";
    }
}
