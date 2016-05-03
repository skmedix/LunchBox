package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileRenameAction extends AbstractAction {

    private final File source;
    private final File destination;
    private final boolean renameEmptyFiles;

    public FileRenameAction(File file, File file1, boolean flag) {
        this.source = file;
        this.destination = file1;
        this.renameEmptyFiles = flag;
    }

    public boolean execute() {
        return execute(this.source, this.destination, this.renameEmptyFiles);
    }

    public static boolean execute(File file, File file1, boolean flag) {
        if (!flag && file.length() <= 0L) {
            try {
                file.delete();
            } catch (Exception exception) {
                FileRenameAction.LOGGER.error("Unable to delete empty file " + file.getAbsolutePath());
            }
        } else {
            File file2 = file1.getParentFile();

            if (file2 != null && !file2.exists() && !file2.mkdirs()) {
                FileRenameAction.LOGGER.error("Unable to create directory {}", new Object[] { file2.getAbsolutePath()});
                return false;
            }

            try {
                if (!file.renameTo(file1)) {
                    try {
                        copyFile(file, file1);
                        return file.delete();
                    } catch (IOException ioexception) {
                        FileRenameAction.LOGGER.error("Unable to rename file {} to {} - {}", new Object[] { file.getAbsolutePath(), file1.getAbsolutePath(), ioexception.getMessage()});
                    }
                }

                return true;
            } catch (Exception exception1) {
                try {
                    copyFile(file, file1);
                    return file.delete();
                } catch (IOException ioexception1) {
                    FileRenameAction.LOGGER.error("Unable to rename file {} to {} - {}", new Object[] { file.getAbsolutePath(), file1.getAbsolutePath(), ioexception1.getMessage()});
                }
            }
        }

        return false;
    }

    private static void copyFile(File file, File file1) throws IOException {
        if (!file1.exists()) {
            file1.createNewFile();
        }

        FileChannel filechannel = null;
        FileChannel filechannel1 = null;
        FileInputStream fileinputstream = null;
        FileOutputStream fileoutputstream = null;

        try {
            fileinputstream = new FileInputStream(file);
            fileoutputstream = new FileOutputStream(file1);
            filechannel = fileinputstream.getChannel();
            filechannel1 = fileoutputstream.getChannel();
            filechannel1.transferFrom(filechannel, 0L, filechannel.size());
        } finally {
            if (filechannel != null) {
                filechannel.close();
            }

            if (fileinputstream != null) {
                fileinputstream.close();
            }

            if (filechannel1 != null) {
                filechannel1.close();
            }

            if (fileoutputstream != null) {
                fileoutputstream.close();
            }

        }

    }
}
