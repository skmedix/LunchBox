package org.apache.commons.io.filefilter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -547733176983104172L;
    private final byte[] magicNumbers;
    private final long byteOffset;

    public MagicNumberFileFilter(byte[] abyte) {
        this(abyte, 0L);
    }

    public MagicNumberFileFilter(String s) {
        this(s, 0L);
    }

    public MagicNumberFileFilter(String s, long i) {
        if (s == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        } else if (s.length() == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        } else if (i < 0L) {
            throw new IllegalArgumentException("The offset cannot be negative");
        } else {
            this.magicNumbers = s.getBytes();
            this.byteOffset = i;
        }
    }

    public MagicNumberFileFilter(byte[] abyte, long i) {
        if (abyte == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        } else if (abyte.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        } else if (i < 0L) {
            throw new IllegalArgumentException("The offset cannot be negative");
        } else {
            this.magicNumbers = new byte[abyte.length];
            System.arraycopy(abyte, 0, this.magicNumbers, 0, abyte.length);
            this.byteOffset = i;
        }
    }

    public boolean accept(File file) {
        if (file != null && file.isFile() && file.canRead()) {
            RandomAccessFile randomaccessfile = null;

            boolean flag;

            try {
                byte[] abyte = new byte[this.magicNumbers.length];

                randomaccessfile = new RandomAccessFile(file, "r");
                randomaccessfile.seek(this.byteOffset);
                int i = randomaccessfile.read(abyte);

                if (i == this.magicNumbers.length) {
                    flag = Arrays.equals(this.magicNumbers, abyte);
                    return flag;
                }

                flag = false;
            } catch (IOException ioexception) {
                return false;
            } finally {
                IOUtils.closeQuietly((Closeable) randomaccessfile);
            }

            return flag;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder(super.toString());

        stringbuilder.append("(");
        stringbuilder.append(new String(this.magicNumbers));
        stringbuilder.append(",");
        stringbuilder.append(this.byteOffset);
        stringbuilder.append(")");
        return stringbuilder.toString();
    }
}
