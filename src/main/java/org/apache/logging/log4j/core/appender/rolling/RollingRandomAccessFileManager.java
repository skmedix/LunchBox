package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class RollingRandomAccessFileManager extends RollingFileManager {

    static final int DEFAULT_BUFFER_SIZE = 262144;
    private static final RollingRandomAccessFileManager.RollingRandomAccessFileManagerFactory FACTORY = new RollingRandomAccessFileManager.RollingRandomAccessFileManagerFactory((RollingRandomAccessFileManager.SyntheticClass_1) null);
    private final boolean isImmediateFlush;
    private RandomAccessFile randomAccessFile;
    private final ByteBuffer buffer;
    private final ThreadLocal isEndOfBatch = new ThreadLocal();

    public RollingRandomAccessFileManager(RandomAccessFile randomaccessfile, String s, String s1, OutputStream outputstream, boolean flag, boolean flag1, long i, long j, TriggeringPolicy triggeringpolicy, RolloverStrategy rolloverstrategy, String s2, Layout layout) {
        super(s, s1, outputstream, flag, i, j, triggeringpolicy, rolloverstrategy, s2, layout);
        this.isImmediateFlush = flag1;
        this.randomAccessFile = randomaccessfile;
        this.isEndOfBatch.set(Boolean.FALSE);
        this.buffer = ByteBuffer.allocate(262144);
    }

    public static RollingRandomAccessFileManager getRollingRandomAccessFileManager(String s, String s1, boolean flag, boolean flag1, TriggeringPolicy triggeringpolicy, RolloverStrategy rolloverstrategy, String s2, Layout layout) {
        return (RollingRandomAccessFileManager) getManager(s, (Object) (new RollingRandomAccessFileManager.FactoryData(s1, flag, flag1, triggeringpolicy, rolloverstrategy, s2, layout)), (ManagerFactory) RollingRandomAccessFileManager.FACTORY);
    }

    public Boolean isEndOfBatch() {
        return (Boolean) this.isEndOfBatch.get();
    }

    public void setEndOfBatch(boolean flag) {
        this.isEndOfBatch.set(Boolean.valueOf(flag));
    }

    protected synchronized void write(byte[] abyte, int i, int j) {
        super.write(abyte, i, j);
        boolean flag = false;

        do {
            if (j > this.buffer.remaining()) {
                this.flush();
            }

            int k = Math.min(j, this.buffer.remaining());

            this.buffer.put(abyte, i, k);
            i += k;
            j -= k;
        } while (j > 0);

        if (this.isImmediateFlush || this.isEndOfBatch.get() == Boolean.TRUE) {
            this.flush();
        }

    }

    protected void createFileAfterRollover() throws IOException {
        this.randomAccessFile = new RandomAccessFile(this.getFileName(), "rw");
        if (this.isAppend()) {
            this.randomAccessFile.seek(this.randomAccessFile.length());
        }

    }

    public synchronized void flush() {
        this.buffer.flip();

        try {
            this.randomAccessFile.write(this.buffer.array(), 0, this.buffer.limit());
        } catch (IOException ioexception) {
            String s = "Error writing to RandomAccessFile " + this.getName();

            throw new AppenderLoggingException(s, ioexception);
        }

        this.buffer.clear();
    }

    public synchronized void close() {
        this.flush();

        try {
            this.randomAccessFile.close();
        } catch (IOException ioexception) {
            RollingRandomAccessFileManager.LOGGER.error("Unable to close RandomAccessFile " + this.getName() + ". " + ioexception);
        }

    }

    static class SyntheticClass_1 {    }

    private static class FactoryData {

        private final String pattern;
        private final boolean append;
        private final boolean immediateFlush;
        private final TriggeringPolicy policy;
        private final RolloverStrategy strategy;
        private final String advertiseURI;
        private final Layout layout;

        public FactoryData(String s, boolean flag, boolean flag1, TriggeringPolicy triggeringpolicy, RolloverStrategy rolloverstrategy, String s1, Layout layout) {
            this.pattern = s;
            this.append = flag;
            this.immediateFlush = flag1;
            this.policy = triggeringpolicy;
            this.strategy = rolloverstrategy;
            this.advertiseURI = s1;
            this.layout = layout;
        }
    }

    static class DummyOutputStream extends OutputStream {

        public void write(int i) throws IOException {}

        public void write(byte[] abyte, int i, int j) throws IOException {}
    }

    private static class RollingRandomAccessFileManagerFactory implements ManagerFactory {

        private RollingRandomAccessFileManagerFactory() {}

        public RollingRandomAccessFileManager createManager(String s, RollingRandomAccessFileManager.FactoryData rollingrandomaccessfilemanager_factorydata) {
            File file = new File(s);
            File file1 = file.getParentFile();

            if (null != file1 && !file1.exists()) {
                file1.mkdirs();
            }

            if (!rollingrandomaccessfilemanager_factorydata.append) {
                file.delete();
            }

            long i = rollingrandomaccessfilemanager_factorydata.append ? file.length() : 0L;
            long j = file.exists() ? file.lastModified() : System.currentTimeMillis();
            RandomAccessFile randomaccessfile = null;

            try {
                randomaccessfile = new RandomAccessFile(s, "rw");
                if (rollingrandomaccessfilemanager_factorydata.append) {
                    long k = randomaccessfile.length();

                    RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} seek to {}", new Object[] { s, Long.valueOf(k)});
                    randomaccessfile.seek(k);
                } else {
                    RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} set length to 0", new Object[] { s});
                    randomaccessfile.setLength(0L);
                }

                return new RollingRandomAccessFileManager(randomaccessfile, s, rollingrandomaccessfilemanager_factorydata.pattern, new RollingRandomAccessFileManager.DummyOutputStream(), rollingrandomaccessfilemanager_factorydata.append, rollingrandomaccessfilemanager_factorydata.immediateFlush, i, j, rollingrandomaccessfilemanager_factorydata.policy, rollingrandomaccessfilemanager_factorydata.strategy, rollingrandomaccessfilemanager_factorydata.advertiseURI, rollingrandomaccessfilemanager_factorydata.layout);
            } catch (IOException ioexception) {
                RollingRandomAccessFileManager.LOGGER.error("Cannot access RandomAccessFile {}) " + ioexception);
                if (randomaccessfile != null) {
                    try {
                        randomaccessfile.close();
                    } catch (IOException ioexception1) {
                        RollingRandomAccessFileManager.LOGGER.error("Cannot close RandomAccessFile {}", new Object[] { s, ioexception1});
                    }
                }

                return null;
            }
        }

        RollingRandomAccessFileManagerFactory(RollingRandomAccessFileManager.SyntheticClass_1 rollingrandomaccessfilemanager_syntheticclass_1) {
            this();
        }
    }
}
