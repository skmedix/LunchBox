package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public final class GZCompressAction extends AbstractAction {

    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;

    public GZCompressAction(File file, File file1, boolean flag) {
        if (file == null) {
            throw new NullPointerException("source");
        } else if (file1 == null) {
            throw new NullPointerException("destination");
        } else {
            this.source = file;
            this.destination = file1;
            this.deleteSource = flag;
        }
    }

    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource);
    }

    public static boolean execute(File file, File file1, boolean flag) throws IOException {
        if (!file.exists()) {
            return false;
        } else {
            FileInputStream fileinputstream = new FileInputStream(file);
            FileOutputStream fileoutputstream = new FileOutputStream(file1);
            GZIPOutputStream gzipoutputstream = new GZIPOutputStream(fileoutputstream);
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(gzipoutputstream);
            byte[] abyte = new byte[8102];

            int i;

            while ((i = fileinputstream.read(abyte)) != -1) {
                bufferedoutputstream.write(abyte, 0, i);
            }

            bufferedoutputstream.close();
            fileinputstream.close();
            if (flag && !file.delete()) {
                GZCompressAction.LOGGER.warn("Unable to delete " + file.toString() + '.');
            }

            return true;
        }
    }

    protected void reportException(Exception exception) {
        GZCompressAction.LOGGER.warn("Exception during compression of \'" + this.source.toString() + "\'.", (Throwable) exception);
    }
}
