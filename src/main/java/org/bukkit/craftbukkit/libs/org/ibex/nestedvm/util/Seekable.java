package org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Seekable {

    public abstract int read(byte[] abyte, int i, int j) throws IOException;

    public abstract int write(byte[] abyte, int i, int j) throws IOException;

    public abstract int length() throws IOException;

    public abstract void seek(int i) throws IOException;

    public abstract void close() throws IOException;

    public abstract int pos() throws IOException;

    public void sync() throws IOException {
        throw new IOException("sync not implemented for " + this.getClass());
    }

    public void resize(long i) throws IOException {
        throw new IOException("resize not implemented for " + this.getClass());
    }

    public Seekable.Lock lock(long i, long j, boolean flag) throws IOException {
        throw new IOException("lock not implemented for " + this.getClass());
    }

    public int read() throws IOException {
        byte[] abyte = new byte[1];
        int i = this.read(abyte, 0, 1);

        return i == -1 ? -1 : abyte[0] & 255;
    }

    public int tryReadFully(byte[] abyte, int i, int j) throws IOException {
        int k;
        int l;

        for (k = 0; j > 0; k += l) {
            l = this.read(abyte, i, j);
            if (l == -1) {
                break;
            }

            i += l;
            j -= l;
        }

        return k == 0 ? -1 : k;
    }

    public abstract static class Lock {

        private Object owner = null;

        public abstract Seekable seekable();

        public abstract boolean isShared();

        public abstract boolean isValid();

        public abstract void release() throws IOException;

        public abstract long position();

        public abstract long size();

        public void setOwner(Object object) {
            this.owner = object;
        }

        public Object getOwner() {
            return this.owner;
        }

        public final boolean contains(int i, int j) {
            return (long) i >= this.position() && this.position() + this.size() >= (long) (i + j);
        }

        public final boolean contained(int i, int j) {
            return (long) i < this.position() && this.position() + this.size() < (long) (i + j);
        }

        public final boolean overlaps(int i, int j) {
            return this.contains(i, j) || this.contained(i, j);
        }
    }

    public static class InputStream extends Seekable {

        private byte[] buffer = new byte[4096];
        private int bytesRead = 0;
        private boolean eof = false;
        private int pos;
        private java.io.InputStream is;

        public InputStream(java.io.InputStream java_io_inputstream) {
            this.is = java_io_inputstream;
        }

        public int read(byte[] abyte, int i, int j) throws IOException {
            if (this.pos >= this.bytesRead && !this.eof) {
                this.readTo(this.pos + 1);
            }

            j = Math.min(j, this.bytesRead - this.pos);
            if (j <= 0) {
                return -1;
            } else {
                System.arraycopy(this.buffer, this.pos, abyte, i, j);
                this.pos += j;
                return j;
            }
        }

        private void readTo(int i) throws IOException {
            if (i >= this.buffer.length) {
                byte[] abyte = new byte[Math.max(this.buffer.length + Math.min(this.buffer.length, 65536), i)];

                System.arraycopy(this.buffer, 0, abyte, 0, this.bytesRead);
                this.buffer = abyte;
            }

            while (this.bytesRead < i) {
                int j = this.is.read(this.buffer, this.bytesRead, this.buffer.length - this.bytesRead);

                if (j == -1) {
                    this.eof = true;
                    break;
                }

                this.bytesRead += j;
            }

        }

        public int length() throws IOException {
            while (!this.eof) {
                this.readTo(this.bytesRead + 4096);
            }

            return this.bytesRead;
        }

        public int write(byte[] abyte, int i, int j) throws IOException {
            throw new IOException("read-only");
        }

        public void seek(int i) {
            this.pos = i;
        }

        public int pos() {
            return this.pos;
        }

        public void close() throws IOException {
            this.is.close();
        }
    }

    public static class File extends Seekable {

        private final java.io.File file;
        private final RandomAccessFile raf;

        public File(String s) throws IOException {
            this(s, false);
        }

        public File(String s, boolean flag) throws IOException {
            this(new java.io.File(s), flag, false);
        }

        public File(java.io.File java_io_file, boolean flag, boolean flag1) throws IOException {
            this.file = java_io_file;
            String s = flag ? "rw" : "r";

            this.raf = new RandomAccessFile(java_io_file, s);
            if (flag1) {
                Platform.setFileLength(this.raf, 0);
            }

        }

        public int read(byte[] abyte, int i, int j) throws IOException {
            return this.raf.read(abyte, i, j);
        }

        public int write(byte[] abyte, int i, int j) throws IOException {
            this.raf.write(abyte, i, j);
            return j;
        }

        public void sync() throws IOException {
            this.raf.getFD().sync();
        }

        public void seek(int i) throws IOException {
            this.raf.seek((long) i);
        }

        public int pos() throws IOException {
            return (int) this.raf.getFilePointer();
        }

        public int length() throws IOException {
            return (int) this.raf.length();
        }

        public void close() throws IOException {
            this.raf.close();
        }

        public void resize(long i) throws IOException {
            Platform.setFileLength(this.raf, (int) i);
        }

        public boolean equals(Object object) {
            return object != null && object instanceof Seekable.File && this.file.equals(((Seekable.File) object).file);
        }

        public Seekable.Lock lock(long i, long j, boolean flag) throws IOException {
            return Platform.lockFile(this, this.raf, i, j, flag);
        }
    }

    public static class ByteArray extends Seekable {

        protected byte[] data;
        protected int pos;
        private final boolean writable;

        public ByteArray(byte[] abyte, boolean flag) {
            this.data = abyte;
            this.pos = 0;
            this.writable = flag;
        }

        public int read(byte[] abyte, int i, int j) {
            j = Math.min(j, this.data.length - this.pos);
            if (j <= 0) {
                return -1;
            } else {
                System.arraycopy(this.data, this.pos, abyte, i, j);
                this.pos += j;
                return j;
            }
        }

        public int write(byte[] abyte, int i, int j) throws IOException {
            if (!this.writable) {
                throw new IOException("read-only data");
            } else {
                j = Math.min(j, this.data.length - this.pos);
                if (j <= 0) {
                    throw new IOException("no space");
                } else {
                    System.arraycopy(abyte, i, this.data, this.pos, j);
                    this.pos += j;
                    return j;
                }
            }
        }

        public int length() {
            return this.data.length;
        }

        public int pos() {
            return this.pos;
        }

        public void seek(int i) {
            this.pos = i;
        }

        public void close() {}
    }
}
