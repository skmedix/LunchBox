package org.bukkit.craftbukkit.libs.jline.internal;

import java.io.IOException;
import java.io.InputStream;

public class NonBlockingInputStream extends InputStream implements Runnable {

    private InputStream in;
    private int ch = -2;
    private boolean threadIsReading = false;
    private boolean isShutdown = false;
    private IOException exception = null;
    private boolean nonBlockingEnabled;

    public NonBlockingInputStream(InputStream in, boolean isNonBlockingEnabled) {
        this.in = in;
        this.nonBlockingEnabled = isNonBlockingEnabled;
        if (isNonBlockingEnabled) {
            Thread t = new Thread(this);

            t.setName("NonBlockingInputStreamThread");
            t.setDaemon(true);
            t.start();
        }

    }

    public synchronized void shutdown() {
        if (!this.isShutdown && this.nonBlockingEnabled) {
            this.isShutdown = true;
            this.notify();
        }

    }

    public boolean isNonBlockingEnabled() {
        return this.nonBlockingEnabled && !this.isShutdown;
    }

    public void close() throws IOException {
        this.in.close();
        this.shutdown();
    }

    public int read() throws IOException {
        return this.nonBlockingEnabled ? this.read(0L, false) : this.in.read();
    }

    public int peek(long timeout) throws IOException {
        if (this.nonBlockingEnabled && !this.isShutdown) {
            return this.read(timeout, true);
        } else {
            throw new UnsupportedOperationException("peek() cannot be called as non-blocking operation is disabled");
        }
    }

    public int read(long timeout) throws IOException {
        if (this.nonBlockingEnabled && !this.isShutdown) {
            return this.read(timeout, false);
        } else {
            throw new UnsupportedOperationException("read() with timeout cannot be called as non-blocking operation is disabled");
        }
    }

    private synchronized int read(long timeout, boolean isPeek) throws IOException {
        if (this.exception != null) {
            assert this.ch == -2;

            IOException ret2 = this.exception;

            if (!isPeek) {
                this.exception = null;
            }

            throw ret2;
        } else {
            if (this.ch >= -1) {
                assert this.exception == null;
            } else if ((timeout == 0L || this.isShutdown) && !this.threadIsReading) {
                this.ch = this.in.read();
            } else {
                if (!this.threadIsReading) {
                    this.threadIsReading = true;
                    this.notify();
                }

                boolean ret = timeout <= 0L;

                while (ret || timeout > 0L) {
                    long start = System.currentTimeMillis();

                    try {
                        this.wait(timeout);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }

                    if (this.exception != null) {
                        assert this.ch == -2;

                        IOException toBeThrown = this.exception;

                        if (!isPeek) {
                            this.exception = null;
                        }

                        throw toBeThrown;
                    }

                    if (this.ch >= -1) {
                        assert this.exception == null;
                        break;
                    }

                    if (!ret) {
                        timeout -= System.currentTimeMillis() - start;
                    }
                }
            }

            int ret1 = this.ch;

            if (!isPeek) {
                this.ch = -2;
            }

            return ret1;
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off >= 0 && len >= 0 && len <= b.length - off) {
            if (len == 0) {
                return 0;
            } else {
                int c;

                if (this.nonBlockingEnabled) {
                    c = this.read(0L);
                } else {
                    c = this.in.read();
                }

                if (c == -1) {
                    return -1;
                } else {
                    b[off] = (byte) c;
                    return 1;
                }
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void run() {
        Log.debug(new Object[] { "NonBlockingInputStream start"});
        boolean needToShutdown = false;
        boolean needToRead = false;

        while (!needToShutdown) {
            synchronized (this) {
                needToShutdown = this.isShutdown;
                needToRead = this.threadIsReading;

                try {
                    if (!needToShutdown && !needToRead) {
                        this.wait(0L);
                    }
                } catch (InterruptedException interruptedexception) {
                    ;
                }
            }

            if (!needToShutdown && needToRead) {
                int charRead = -2;
                IOException failure = null;

                try {
                    charRead = this.in.read();
                } catch (IOException ioexception) {
                    failure = ioexception;
                }

                synchronized (this) {
                    this.exception = failure;
                    this.ch = charRead;
                    this.threadIsReading = false;
                    this.notify();
                }
            }
        }

        Log.debug(new Object[] { "NonBlockingInputStream shutdown"});
    }
}
