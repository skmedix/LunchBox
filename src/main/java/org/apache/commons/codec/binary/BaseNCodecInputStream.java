package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStream extends FilterInputStream {

    private final BaseNCodec baseNCodec;
    private final boolean doEncode;
    private final byte[] singleByte = new byte[1];
    private final BaseNCodec.Context context = new BaseNCodec.Context();

    protected BaseNCodecInputStream(InputStream inputstream, BaseNCodec basencodec, boolean flag) {
        super(inputstream);
        this.doEncode = flag;
        this.baseNCodec = basencodec;
    }

    public int available() throws IOException {
        return this.context.eof ? 0 : 1;
    }

    public synchronized void mark(int i) {}

    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        int i;

        for (i = this.read(this.singleByte, 0, 1); i == 0; i = this.read(this.singleByte, 0, 1)) {
            ;
        }

        if (i > 0) {
            byte b0 = this.singleByte[0];

            return b0 < 0 ? 256 + b0 : b0;
        } else {
            return -1;
        }
    }

    public int read(byte[] abyte, int i, int j) throws IOException {
        if (abyte == null) {
            throw new NullPointerException();
        } else if (i >= 0 && j >= 0) {
            if (i <= abyte.length && i + j <= abyte.length) {
                if (j == 0) {
                    return 0;
                } else {
                    int k;

                    for (k = 0; k == 0; k = this.baseNCodec.readResults(abyte, i, j, this.context)) {
                        if (!this.baseNCodec.hasData(this.context)) {
                            byte[] abyte1 = new byte[this.doEncode ? 4096 : 8192];
                            int l = this.in.read(abyte1);

                            if (this.doEncode) {
                                this.baseNCodec.encode(abyte1, 0, l, this.context);
                            } else {
                                this.baseNCodec.decode(abyte1, 0, l, this.context);
                            }
                        }
                    }

                    return k;
                }
            } else {
                throw new IndexOutOfBoundsException();
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public long skip(long i) throws IOException {
        if (i < 0L) {
            throw new IllegalArgumentException("Negative skip length: " + i);
        } else {
            byte[] abyte = new byte[512];

            long j;
            int k;

            for (j = i; j > 0L; j -= (long) k) {
                k = (int) Math.min((long) abyte.length, j);
                k = this.read(abyte, 0, k);
                if (k == -1) {
                    break;
                }
            }

            return i - j;
        }
    }
}
