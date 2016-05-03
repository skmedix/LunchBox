package org.apache.logging.log4j.core.appender;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;

public class FileManager extends OutputStreamManager {

    private static final FileManager.FileManagerFactory FACTORY = new FileManager.FileManagerFactory((FileManager.SyntheticClass_1) null);
    private final boolean isAppend;
    private final boolean isLocking;
    private final String advertiseURI;

    protected FileManager(String s, OutputStream outputstream, boolean flag, boolean flag1, String s1, Layout layout) {
        super(outputstream, s, layout);
        this.isAppend = flag;
        this.isLocking = flag1;
        this.advertiseURI = s1;
    }

    public static FileManager getFileManager(String s, boolean flag, boolean flag1, boolean flag2, String s1, Layout layout) {
        if (flag1 && flag2) {
            flag1 = false;
        }

        return (FileManager) getManager(s, (Object) (new FileManager.FactoryData(flag, flag1, flag2, s1, layout)), (ManagerFactory) FileManager.FACTORY);
    }

    protected synchronized void write(byte[] abyte, int i, int j) {
        if (this.isLocking) {
            FileChannel filechannel = ((FileOutputStream) this.getOutputStream()).getChannel();

            try {
                FileLock filelock = filechannel.lock(0L, Long.MAX_VALUE, false);

                try {
                    super.write(abyte, i, j);
                } finally {
                    filelock.release();
                }
            } catch (IOException ioexception) {
                throw new AppenderLoggingException("Unable to obtain lock on " + this.getName(), ioexception);
            }
        } else {
            super.write(abyte, i, j);
        }

    }

    public String getFileName() {
        return this.getName();
    }

    public boolean isAppend() {
        return this.isAppend;
    }

    public boolean isLocking() {
        return this.isLocking;
    }

    public Map getContentFormat() {
        HashMap hashmap = new HashMap(super.getContentFormat());

        hashmap.put("fileURI", this.advertiseURI);
        return hashmap;
    }

    static class SyntheticClass_1 {    }

    private static class FileManagerFactory implements ManagerFactory {

        private FileManagerFactory() {}

        public FileManager createManager(String s, FileManager.FactoryData filemanager_factorydata) {
            File file = new File(s);
            File file1 = file.getParentFile();

            if (null != file1 && !file1.exists()) {
                file1.mkdirs();
            }

            try {
                Object object = new FileOutputStream(s, filemanager_factorydata.append);

                if (filemanager_factorydata.bufferedIO) {
                    object = new BufferedOutputStream((OutputStream) object);
                }

                return new FileManager(s, (OutputStream) object, filemanager_factorydata.append, filemanager_factorydata.locking, filemanager_factorydata.advertiseURI, filemanager_factorydata.layout);
            } catch (FileNotFoundException filenotfoundexception) {
                AbstractManager.LOGGER.error("FileManager (" + s + ") " + filenotfoundexception);
                return null;
            }
        }

        FileManagerFactory(FileManager.SyntheticClass_1 filemanager_syntheticclass_1) {
            this();
        }
    }

    private static class FactoryData {

        private final boolean append;
        private final boolean locking;
        private final boolean bufferedIO;
        private final String advertiseURI;
        private final Layout layout;

        public FactoryData(boolean flag, boolean flag1, boolean flag2, String s, Layout layout) {
            this.append = flag;
            this.locking = flag1;
            this.bufferedIO = flag2;
            this.advertiseURI = s;
            this.layout = layout;
        }
    }
}
