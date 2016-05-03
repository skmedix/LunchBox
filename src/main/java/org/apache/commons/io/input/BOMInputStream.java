package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;

public class BOMInputStream extends ProxyInputStream {

    private final boolean include;
    private final List boms;
    private ByteOrderMark byteOrderMark;
    private int[] firstBytes;
    private int fbLength;
    private int fbIndex;
    private int markFbIndex;
    private boolean markedAtStart;
    private static final Comparator ByteOrderMarkLengthComparator = new Comparator() {
        public int compare(ByteOrderMark byteordermark, ByteOrderMark byteordermark1) {
            int i = byteordermark.length();
            int j = byteordermark1.length();

            return i > j ? -1 : (j > i ? 1 : 0);
        }
    };

    public BOMInputStream(InputStream inputstream) {
        this(inputstream, false, new ByteOrderMark[] { ByteOrderMark.UTF_8});
    }

    public BOMInputStream(InputStream inputstream, boolean flag) {
        this(inputstream, flag, new ByteOrderMark[] { ByteOrderMark.UTF_8});
    }

    public BOMInputStream(InputStream inputstream, ByteOrderMark... abyteordermark) {
        this(inputstream, false, abyteordermark);
    }

    public BOMInputStream(InputStream inputstream, boolean flag, ByteOrderMark... abyteordermark) {
        super(inputstream);
        if (abyteordermark != null && abyteordermark.length != 0) {
            this.include = flag;
            Arrays.sort(abyteordermark, BOMInputStream.ByteOrderMarkLengthComparator);
            this.boms = Arrays.asList(abyteordermark);
        } else {
            throw new IllegalArgumentException("No BOMs specified");
        }
    }

    public boolean hasBOM() throws IOException {
        return this.getBOM() != null;
    }

    public boolean hasBOM(ByteOrderMark byteordermark) throws IOException {
        if (!this.boms.contains(byteordermark)) {
            throw new IllegalArgumentException("Stream not configure to detect " + byteordermark);
        } else {
            return this.byteOrderMark != null && this.getBOM().equals(byteordermark);
        }
    }

    public ByteOrderMark getBOM() throws IOException {
        if (this.firstBytes == null) {
            this.fbLength = 0;
            int i = ((ByteOrderMark) this.boms.get(0)).length();

            this.firstBytes = new int[i];

            for (int j = 0; j < this.firstBytes.length; ++j) {
                this.firstBytes[j] = this.in.read();
                ++this.fbLength;
                if (this.firstBytes[j] < 0) {
                    break;
                }
            }

            this.byteOrderMark = this.find();
            if (this.byteOrderMark != null && !this.include) {
                if (this.byteOrderMark.length() < this.firstBytes.length) {
                    this.fbIndex = this.byteOrderMark.length();
                } else {
                    this.fbLength = 0;
                }
            }
        }

        return this.byteOrderMark;
    }

    public String getBOMCharsetName() throws IOException {
        this.getBOM();
        return this.byteOrderMark == null ? null : this.byteOrderMark.getCharsetName();
    }

    private int readFirstBytes() throws IOException {
        this.getBOM();
        return this.fbIndex < this.fbLength ? this.firstBytes[this.fbIndex++] : -1;
    }

    private ByteOrderMark find() {
        Iterator iterator = this.boms.iterator();

        ByteOrderMark byteordermark;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            byteordermark = (ByteOrderMark) iterator.next();
        } while (!this.matches(byteordermark));

        return byteordermark;
    }

    private boolean matches(ByteOrderMark byteordermark) {
        for (int i = 0; i < byteordermark.length(); ++i) {
            if (byteordermark.get(i) != this.firstBytes[i]) {
                return false;
            }
        }

        return true;
    }

    public int read() throws IOException {
        int i = this.readFirstBytes();

        return i >= 0 ? i : this.in.read();
    }

    public int read(byte[] abyte, int i, int j) throws IOException {
        int k = 0;
        int l = 0;

        while (j > 0 && l >= 0) {
            l = this.readFirstBytes();
            if (l >= 0) {
                abyte[i++] = (byte) (l & 255);
                --j;
                ++k;
            }
        }

        int i1 = this.in.read(abyte, i, j);

        return i1 < 0 ? (k > 0 ? k : -1) : k + i1;
    }

    public int read(byte[] abyte) throws IOException {
        return this.read(abyte, 0, abyte.length);
    }

    public synchronized void mark(int i) {
        this.markFbIndex = this.fbIndex;
        this.markedAtStart = this.firstBytes == null;
        this.in.mark(i);
    }

    public synchronized void reset() throws IOException {
        this.fbIndex = this.markFbIndex;
        if (this.markedAtStart) {
            this.firstBytes = null;
        }

        this.in.reset();
    }

    public long skip(long i) throws IOException {
        while (i > 0L && this.readFirstBytes() >= 0) {
            --i;
        }

        return this.in.skip(i);
    }
}
