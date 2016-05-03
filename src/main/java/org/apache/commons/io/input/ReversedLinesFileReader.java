package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.Charsets;

public class ReversedLinesFileReader implements Closeable {

    private final int blockSize;
    private final Charset encoding;
    private final RandomAccessFile randomAccessFile;
    private final long totalByteLength;
    private final long totalBlockCount;
    private final byte[][] newLineSequences;
    private final int avoidNewlineSplitBufferSize;
    private final int byteDecrement;
    private ReversedLinesFileReader.FilePart currentFilePart;
    private boolean trailingNewlineOfFileSkipped;

    public ReversedLinesFileReader(File file) throws IOException {
        this(file, 4096, Charset.defaultCharset().toString());
    }

    public ReversedLinesFileReader(File file, int i, Charset charset) throws IOException {
        this.trailingNewlineOfFileSkipped = false;
        this.blockSize = i;
        this.encoding = charset;
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.totalByteLength = this.randomAccessFile.length();
        int j = (int) (this.totalByteLength % (long) i);

        if (j > 0) {
            this.totalBlockCount = this.totalByteLength / (long) i + 1L;
        } else {
            this.totalBlockCount = this.totalByteLength / (long) i;
            if (this.totalByteLength > 0L) {
                j = i;
            }
        }

        this.currentFilePart = new ReversedLinesFileReader.FilePart(this.totalBlockCount, j, (byte[]) null, (ReversedLinesFileReader.SyntheticClass_1) null);
        Charset charset1 = Charsets.toCharset(charset);
        CharsetEncoder charsetencoder = charset1.newEncoder();
        float f = charsetencoder.maxBytesPerChar();

        if (f == 1.0F) {
            this.byteDecrement = 1;
        } else if (charset1 == Charset.forName("UTF-8")) {
            this.byteDecrement = 1;
        } else if (charset1 == Charset.forName("Shift_JIS")) {
            this.byteDecrement = 1;
        } else {
            if (charset1 != Charset.forName("UTF-16BE") && charset1 != Charset.forName("UTF-16LE")) {
                if (charset1 == Charset.forName("UTF-16")) {
                    throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
                }

                throw new UnsupportedEncodingException("Encoding " + charset + " is not supported yet (feel free to submit a patch)");
            }

            this.byteDecrement = 2;
        }

        this.newLineSequences = new byte[][] { "\r\n".getBytes(charset), "\n".getBytes(charset), "\r".getBytes(charset)};
        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
    }

    public ReversedLinesFileReader(File file, int i, String s) throws IOException {
        this(file, i, Charsets.toCharset(s));
    }

    public String readLine() throws IOException {
        String s;

        for (s = this.currentFilePart.readLine(); s == null; s = this.currentFilePart.readLine()) {
            this.currentFilePart = this.currentFilePart.rollOver();
            if (this.currentFilePart == null) {
                break;
            }
        }

        if ("".equals(s) && !this.trailingNewlineOfFileSkipped) {
            this.trailingNewlineOfFileSkipped = true;
            s = this.readLine();
        }

        return s;
    }

    public void close() throws IOException {
        this.randomAccessFile.close();
    }

    static class SyntheticClass_1 {    }

    private class FilePart {

        private final long no;
        private final byte[] data;
        private byte[] leftOver;
        private int currentLastBytePos;

        private FilePart(long i, int j, byte[] abyte) throws IOException {
            this.no = i;
            int k = j + (abyte != null ? abyte.length : 0);

            this.data = new byte[k];
            long l = (i - 1L) * (long) ReversedLinesFileReader.this.blockSize;

            if (i > 0L) {
                ReversedLinesFileReader.this.randomAccessFile.seek(l);
                int i1 = ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, j);

                if (i1 != j) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don\'t match");
                }
            }

            if (abyte != null) {
                System.arraycopy(abyte, 0, this.data, j, abyte.length);
            }

            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }

        private ReversedLinesFileReader.FilePart rollOver() throws IOException {
            if (this.currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
            } else if (this.no > 1L) {
                return ReversedLinesFileReader.this.new FilePart(this.no - 1L, ReversedLinesFileReader.this.blockSize, this.leftOver);
            } else if (this.leftOver != null) {
                throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
            } else {
                return null;
            }
        }

        private String readLine() throws IOException {
            String s = null;
            boolean flag = this.no == 1L;
            int i = this.currentLastBytePos;

            while (i > -1) {
                if (!flag && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
                    this.createLeftOver();
                    break;
                }

                int j;

                if ((j = this.getNewLineMatchByteCount(this.data, i)) > 0) {
                    int k = i + 1;
                    int l = this.currentLastBytePos - k + 1;

                    if (l < 0) {
                        throw new IllegalStateException("Unexpected negative line length=" + l);
                    }

                    byte[] abyte = new byte[l];

                    System.arraycopy(this.data, k, abyte, 0, l);
                    s = new String(abyte, ReversedLinesFileReader.this.encoding);
                    this.currentLastBytePos = i - j;
                    break;
                }

                i -= ReversedLinesFileReader.this.byteDecrement;
                if (i < 0) {
                    this.createLeftOver();
                    break;
                }
            }

            if (flag && this.leftOver != null) {
                s = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
                this.leftOver = null;
            }

            return s;
        }

        private void createLeftOver() {
            int i = this.currentLastBytePos + 1;

            if (i > 0) {
                this.leftOver = new byte[i];
                System.arraycopy(this.data, 0, this.leftOver, 0, i);
            } else {
                this.leftOver = null;
            }

            this.currentLastBytePos = -1;
        }

        private int getNewLineMatchByteCount(byte[] abyte, int i) {
            byte[][] abyte1 = ReversedLinesFileReader.this.newLineSequences;
            int j = abyte1.length;

            for (int k = 0; k < j; ++k) {
                byte[] abyte2 = abyte1[k];
                boolean flag = true;

                for (int l = abyte2.length - 1; l >= 0; --l) {
                    int i1 = i + l - (abyte2.length - 1);

                    flag &= i1 >= 0 && abyte[i1] == abyte2[l];
                }

                if (flag) {
                    return abyte2.length;
                }
            }

            return 0;
        }

        FilePart(long i, int j, byte[] abyte, ReversedLinesFileReader.SyntheticClass_1 reversedlinesfilereader_syntheticclass_1) throws IOException {
            this(i, j, abyte);
        }
    }
}
