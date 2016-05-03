package org.apache.logging.log4j.core.appender.rolling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.FileManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public class RollingFileManager extends FileManager {

    private static RollingFileManager.RollingFileManagerFactory factory = new RollingFileManager.RollingFileManagerFactory((RollingFileManager.SyntheticClass_1) null);
    private long size;
    private long initialTime;
    private final PatternProcessor patternProcessor;
    private final Semaphore semaphore = new Semaphore(1);
    private final TriggeringPolicy policy;
    private final RolloverStrategy strategy;

    protected RollingFileManager(String s, String s1, OutputStream outputstream, boolean flag, long i, long j, TriggeringPolicy triggeringpolicy, RolloverStrategy rolloverstrategy, String s2, Layout layout) {
        super(s, outputstream, flag, false, s2, layout);
        this.size = i;
        this.initialTime = j;
        this.policy = triggeringpolicy;
        this.strategy = rolloverstrategy;
        this.patternProcessor = new PatternProcessor(s1);
        triggeringpolicy.initialize(this);
    }

    public static RollingFileManager getFileManager(String s, String s1, boolean flag, boolean flag1, TriggeringPolicy triggeringpolicy, RolloverStrategy rolloverstrategy, String s2, Layout layout) {
        return (RollingFileManager) getManager(s, (Object) (new RollingFileManager.FactoryData(s1, flag, flag1, triggeringpolicy, rolloverstrategy, s2, layout)), (ManagerFactory) RollingFileManager.factory);
    }

    protected synchronized void write(byte[] abyte, int i, int j) {
        this.size += (long) j;
        super.write(abyte, i, j);
    }

    public long getFileSize() {
        return this.size;
    }

    public long getFileTime() {
        return this.initialTime;
    }

    public synchronized void checkRollover(LogEvent logevent) {
        if (this.policy.isTriggeringEvent(logevent) && this.rollover(this.strategy)) {
            try {
                this.size = 0L;
                this.initialTime = System.currentTimeMillis();
                this.createFileAfterRollover();
            } catch (IOException ioexception) {
                RollingFileManager.LOGGER.error("FileManager (" + this.getFileName() + ") " + ioexception);
            }
        }

    }

    protected void createFileAfterRollover() throws IOException {
        FileOutputStream fileoutputstream = new FileOutputStream(this.getFileName(), this.isAppend());

        this.setOutputStream(fileoutputstream);
    }

    public PatternProcessor getPatternProcessor() {
        return this.patternProcessor;
    }

    private boolean rollover(RolloverStrategy rolloverstrategy) {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException interruptedexception) {
            RollingFileManager.LOGGER.error("Thread interrupted while attempting to check rollover", (Throwable) interruptedexception);
            return false;
        }

        boolean flag = false;
        Thread thread = null;

        boolean flag1;

        try {
            RolloverDescription rolloverdescription = rolloverstrategy.rollover(this);

            if (rolloverdescription == null) {
                flag1 = false;
                return flag1;
            }

            this.close();
            if (rolloverdescription.getSynchronous() != null) {
                try {
                    flag = rolloverdescription.getSynchronous().execute();
                } catch (Exception exception) {
                    RollingFileManager.LOGGER.error("Error in synchronous task", (Throwable) exception);
                }
            }

            if (flag && rolloverdescription.getAsynchronous() != null) {
                thread = new Thread(new RollingFileManager.AsyncAction(rolloverdescription.getAsynchronous(), this));
                thread.start();
            }

            flag1 = true;
        } finally {
            if (thread == null) {
                this.semaphore.release();
            }

        }

        return flag1;
    }

    static class SyntheticClass_1 {    }

    private static class RollingFileManagerFactory implements ManagerFactory {

        private RollingFileManagerFactory() {}

        public RollingFileManager createManager(String s, RollingFileManager.FactoryData rollingfilemanager_factorydata) {
            File file = new File(s);
            File file1 = file.getParentFile();

            if (null != file1 && !file1.exists()) {
                file1.mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException ioexception) {
                RollingFileManager.LOGGER.error("Unable to create file " + s, (Throwable) ioexception);
                return null;
            }

            long i = rollingfilemanager_factorydata.append ? file.length() : 0L;
            long j = file.lastModified();

            try {
                Object object = new FileOutputStream(s, rollingfilemanager_factorydata.append);

                if (rollingfilemanager_factorydata.bufferedIO) {
                    object = new BufferedOutputStream((OutputStream) object);
                }

                return new RollingFileManager(s, rollingfilemanager_factorydata.pattern, (OutputStream) object, rollingfilemanager_factorydata.append, i, j, rollingfilemanager_factorydata.policy, rollingfilemanager_factorydata.strategy, rollingfilemanager_factorydata.advertiseURI, rollingfilemanager_factorydata.layout);
            } catch (FileNotFoundException filenotfoundexception) {
                RollingFileManager.LOGGER.error("FileManager (" + s + ") " + filenotfoundexception);
                return null;
            }
        }

        RollingFileManagerFactory(RollingFileManager.SyntheticClass_1 rollingfilemanager_syntheticclass_1) {
            this();
        }
    }

    private static class FactoryData {

        private final String pattern;
        private final boolean append;
        private final boolean bufferedIO;
        private final TriggeringPolicy policy;
        private final RolloverStrategy strategy;
        private final String advertiseURI;
        private final Layout layout;

        public FactoryData(String s, boolean flag, boolean flag1, TriggeringPolicy triggeringpolicy, RolloverStrategy rolloverstrategy, String s1, Layout layout) {
            this.pattern = s;
            this.append = flag;
            this.bufferedIO = flag1;
            this.policy = triggeringpolicy;
            this.strategy = rolloverstrategy;
            this.advertiseURI = s1;
            this.layout = layout;
        }
    }

    private static class AsyncAction extends AbstractAction {

        private final Action action;
        private final RollingFileManager manager;

        public AsyncAction(Action action, RollingFileManager rollingfilemanager) {
            this.action = action;
            this.manager = rollingfilemanager;
        }

        public boolean execute() throws IOException {
            boolean flag;

            try {
                flag = this.action.execute();
            } finally {
                this.manager.semaphore.release();
            }

            return flag;
        }

        public void close() {
            this.action.close();
        }

        public boolean isComplete() {
            return this.action.isComplete();
        }
    }
}
