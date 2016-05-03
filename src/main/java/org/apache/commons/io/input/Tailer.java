package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Tailer implements Runnable {

    private static final int DEFAULT_DELAY_MILLIS = 1000;
    private static final String RAF_MODE = "r";
    private static final int DEFAULT_BUFSIZE = 4096;
    private final byte[] inbuf;
    private final File file;
    private final long delayMillis;
    private final boolean end;
    private final TailerListener listener;
    private final boolean reOpen;
    private volatile boolean run;

    public Tailer(File file, TailerListener tailerlistener) {
        this(file, tailerlistener, 1000L);
    }

    public Tailer(File file, TailerListener tailerlistener, long i) {
        this(file, tailerlistener, i, false);
    }

    public Tailer(File file, TailerListener tailerlistener, long i, boolean flag) {
        this(file, tailerlistener, i, flag, 4096);
    }

    public Tailer(File file, TailerListener tailerlistener, long i, boolean flag, boolean flag1) {
        this(file, tailerlistener, i, flag, flag1, 4096);
    }

    public Tailer(File file, TailerListener tailerlistener, long i, boolean flag, int j) {
        this(file, tailerlistener, i, flag, false, j);
    }

    public Tailer(File file, TailerListener tailerlistener, long i, boolean flag, boolean flag1, int j) {
        this.run = true;
        this.file = file;
        this.delayMillis = i;
        this.end = flag;
        this.inbuf = new byte[j];
        this.listener = tailerlistener;
        tailerlistener.init(this);
        this.reOpen = flag1;
    }

    public static Tailer create(File file, TailerListener tailerlistener, long i, boolean flag, int j) {
        Tailer tailer = new Tailer(file, tailerlistener, i, flag, j);
        Thread thread = new Thread(tailer);

        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener tailerlistener, long i, boolean flag, boolean flag1, int j) {
        Tailer tailer = new Tailer(file, tailerlistener, i, flag, flag1, j);
        Thread thread = new Thread(tailer);

        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener tailerlistener, long i, boolean flag) {
        return create(file, tailerlistener, i, flag, 4096);
    }

    public static Tailer create(File file, TailerListener tailerlistener, long i, boolean flag, boolean flag1) {
        return create(file, tailerlistener, i, flag, flag1, 4096);
    }

    public static Tailer create(File file, TailerListener tailerlistener, long i) {
        return create(file, tailerlistener, i, false);
    }

    public static Tailer create(File file, TailerListener tailerlistener) {
        return create(file, tailerlistener, 1000L, false);
    }

    public File getFile() {
        return this.file;
    }

    public long getDelay() {
        return this.delayMillis;
    }

    public void run() {
        RandomAccessFile randomaccessfile = null;

        try {
            long i = 0L;
            long j = 0L;

            while (this.run && randomaccessfile == null) {
                try {
                    randomaccessfile = new RandomAccessFile(this.file, "r");
                } catch (FileNotFoundException filenotfoundexception) {
                    this.listener.fileNotFound();
                }

                if (randomaccessfile == null) {
                    try {
                        Thread.sleep(this.delayMillis);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }
                } else {
                    j = this.end ? this.file.length() : 0L;
                    i = System.currentTimeMillis();
                    randomaccessfile.seek(j);
                }
            }

            while (this.run) {
                boolean flag = FileUtils.isFileNewer(this.file, i);
                long k = this.file.length();

                if (k < j) {
                    this.listener.fileRotated();

                    try {
                        RandomAccessFile randomaccessfile1 = randomaccessfile;

                        randomaccessfile = new RandomAccessFile(this.file, "r");
                        j = 0L;
                        IOUtils.closeQuietly((Closeable) randomaccessfile1);
                    } catch (FileNotFoundException filenotfoundexception1) {
                        this.listener.fileNotFound();
                    }
                } else {
                    if (k > j) {
                        j = this.readLines(randomaccessfile);
                        i = System.currentTimeMillis();
                    } else if (flag) {
                        j = 0L;
                        randomaccessfile.seek(j);
                        j = this.readLines(randomaccessfile);
                        i = System.currentTimeMillis();
                    }

                    if (this.reOpen) {
                        IOUtils.closeQuietly((Closeable) randomaccessfile);
                    }

                    try {
                        Thread.sleep(this.delayMillis);
                    } catch (InterruptedException interruptedexception1) {
                        ;
                    }

                    if (this.run && this.reOpen) {
                        randomaccessfile = new RandomAccessFile(this.file, "r");
                        randomaccessfile.seek(j);
                    }
                }
            }
        } catch (Exception exception) {
            this.listener.handle(exception);
        } finally {
            IOUtils.closeQuietly((Closeable) randomaccessfile);
        }

    }

    public void stop() {
        this.run = false;
    }

    private long readLines(RandomAccessFile randomaccessfile) throws IOException {
        StringBuilder stringbuilder = new StringBuilder();
        long i = randomaccessfile.getFilePointer();
        long j = i;

        int k;

        for (boolean flag = false; this.run && (k = randomaccessfile.read(this.inbuf)) != -1; i = randomaccessfile.getFilePointer()) {
            for (int l = 0; l < k; ++l) {
                byte b0 = this.inbuf[l];

                switch (b0) {
                case 10:
                    flag = false;
                    this.listener.handle(stringbuilder.toString());
                    stringbuilder.setLength(0);
                    j = i + (long) l + 1L;
                    break;

                case 13:
                    if (flag) {
                        stringbuilder.append('\r');
                    }

                    flag = true;
                    break;

                default:
                    if (flag) {
                        flag = false;
                        this.listener.handle(stringbuilder.toString());
                        stringbuilder.setLength(0);
                        j = i + (long) l + 1L;
                    }

                    stringbuilder.append((char) b0);
                }
            }
        }

        randomaccessfile.seek(j);
        return j;
    }
}
