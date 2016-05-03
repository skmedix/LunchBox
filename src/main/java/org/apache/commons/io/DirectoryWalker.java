package org.apache.commons.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public abstract class DirectoryWalker {

    private final FileFilter filter;
    private final int depthLimit;

    protected DirectoryWalker() {
        this((FileFilter) null, -1);
    }

    protected DirectoryWalker(FileFilter filefilter, int i) {
        this.filter = filefilter;
        this.depthLimit = i;
    }

    protected DirectoryWalker(IOFileFilter iofilefilter, IOFileFilter iofilefilter1, int i) {
        if (iofilefilter == null && iofilefilter1 == null) {
            this.filter = null;
        } else {
            iofilefilter = iofilefilter != null ? iofilefilter : TrueFileFilter.TRUE;
            iofilefilter1 = iofilefilter1 != null ? iofilefilter1 : TrueFileFilter.TRUE;
            iofilefilter = FileFilterUtils.makeDirectoryOnly(iofilefilter);
            iofilefilter1 = FileFilterUtils.makeFileOnly(iofilefilter1);
            this.filter = FileFilterUtils.or(new IOFileFilter[] { iofilefilter, iofilefilter1});
        }

        this.depthLimit = i;
    }

    protected final void walk(File file, Collection collection) throws IOException {
        if (file == null) {
            throw new NullPointerException("Start Directory is null");
        } else {
            try {
                this.handleStart(file, collection);
                this.walk(file, 0, collection);
                this.handleEnd(collection);
            } catch (DirectoryWalker.CancelException directorywalker_cancelexception) {
                this.handleCancelled(file, collection, directorywalker_cancelexception);
            }

        }
    }

    private void walk(File file, int i, Collection collection) throws IOException {
        this.checkIfCancelled(file, i, collection);
        if (this.handleDirectory(file, i, collection)) {
            this.handleDirectoryStart(file, i, collection);
            int j = i + 1;

            if (this.depthLimit < 0 || j <= this.depthLimit) {
                this.checkIfCancelled(file, i, collection);
                File[] afile = this.filter == null ? file.listFiles() : file.listFiles(this.filter);

                afile = this.filterDirectoryContents(file, i, afile);
                if (afile == null) {
                    this.handleRestricted(file, j, collection);
                } else {
                    File[] afile1 = afile;
                    int k = afile.length;

                    for (int l = 0; l < k; ++l) {
                        File file1 = afile1[l];

                        if (file1.isDirectory()) {
                            this.walk(file1, j, collection);
                        } else {
                            this.checkIfCancelled(file1, j, collection);
                            this.handleFile(file1, j, collection);
                            this.checkIfCancelled(file1, j, collection);
                        }
                    }
                }
            }

            this.handleDirectoryEnd(file, i, collection);
        }

        this.checkIfCancelled(file, i, collection);
    }

    protected final void checkIfCancelled(File file, int i, Collection collection) throws IOException {
        if (this.handleIsCancelled(file, i, collection)) {
            throw new DirectoryWalker.CancelException(file, i);
        }
    }

    protected boolean handleIsCancelled(File file, int i, Collection collection) throws IOException {
        return false;
    }

    protected void handleCancelled(File file, Collection collection, DirectoryWalker.CancelException directorywalker_cancelexception) throws IOException {
        throw directorywalker_cancelexception;
    }

    protected void handleStart(File file, Collection collection) throws IOException {}

    protected boolean handleDirectory(File file, int i, Collection collection) throws IOException {
        return true;
    }

    protected void handleDirectoryStart(File file, int i, Collection collection) throws IOException {}

    protected File[] filterDirectoryContents(File file, int i, File[] afile) throws IOException {
        return afile;
    }

    protected void handleFile(File file, int i, Collection collection) throws IOException {}

    protected void handleRestricted(File file, int i, Collection collection) throws IOException {}

    protected void handleDirectoryEnd(File file, int i, Collection collection) throws IOException {}

    protected void handleEnd(Collection collection) throws IOException {}

    public static class CancelException extends IOException {

        private static final long serialVersionUID = 1347339620135041008L;
        private final File file;
        private final int depth;

        public CancelException(File file, int i) {
            this("Operation Cancelled", file, i);
        }

        public CancelException(String s, File file, int i) {
            super(s);
            this.file = file;
            this.depth = i;
        }

        public File getFile() {
            return this.file;
        }

        public int getDepth() {
            return this.depth;
        }
    }
}
