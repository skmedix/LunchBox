package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipCompressAction extends AbstractAction {

    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    private final int level;

    public ZipCompressAction(File file, File file1, boolean flag, int i) {
        if (file == null) {
            throw new NullPointerException("source");
        } else if (file1 == null) {
            throw new NullPointerException("destination");
        } else {
            this.source = file;
            this.destination = file1;
            this.deleteSource = flag;
            this.level = i;
        }
    }

    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource, this.level);
    }

    public static boolean execute(File file, File file1, boolean flag, int i) throws IOException {
        if (!file.exists()) {
            return false;
        } else {
            FileInputStream fileinputstream = new FileInputStream(file);
            FileOutputStream fileoutputstream = new FileOutputStream(file1);
            ZipOutputStream zipoutputstream = new ZipOutputStream(fileoutputstream);

            zipoutputstream.setLevel(i);
            ZipEntry zipentry = new ZipEntry(file.getName());

            zipoutputstream.putNextEntry(zipentry);
            byte[] abyte = new byte[8102];

            int j;

            while ((j = fileinputstream.read(abyte)) != -1) {
                zipoutputstream.write(abyte, 0, j);
            }

            zipoutputstream.close();
            fileinputstream.close();
            if (flag && !file.delete()) {
                ZipCompressAction.LOGGER.warn("Unable to delete " + file.toString() + '.');
            }

            return true;
        }
    }

    protected void reportException(Exception exception) {
        ZipCompressAction.LOGGER.warn("Exception during compression of \'" + this.source.toString() + "\'.", (Throwable) exception);
    }
}
