package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileDeleteStrategy {

    public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy("Normal");
    public static final FileDeleteStrategy FORCE = new FileDeleteStrategy.ForceFileDeleteStrategy();
    private final String name;

    protected FileDeleteStrategy(String s) {
        this.name = s;
    }

    public boolean deleteQuietly(File file) {
        if (file != null && file.exists()) {
            try {
                return this.doDelete(file);
            } catch (IOException ioexception) {
                return false;
            }
        } else {
            return true;
        }
    }

    public void delete(File file) throws IOException {
        if (file.exists() && !this.doDelete(file)) {
            throw new IOException("Deletion failed: " + file);
        }
    }

    protected boolean doDelete(File file) throws IOException {
        return file.delete();
    }

    public String toString() {
        return "FileDeleteStrategy[" + this.name + "]";
    }

    static class ForceFileDeleteStrategy extends FileDeleteStrategy {

        ForceFileDeleteStrategy() {
            super("Force");
        }

        protected boolean doDelete(File file) throws IOException {
            FileUtils.forceDelete(file);
            return true;
        }
    }
}
