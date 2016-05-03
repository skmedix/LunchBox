package org.apache.commons.codec.binary;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BaseNCodecOutputStream extends FilterOutputStream {

    private final boolean doEncode;
    private final BaseNCodec baseNCodec;
    private final byte[] singleByte = new byte[1];
    private final BaseNCodec.Context context = new BaseNCodec.Context();

    public BaseNCodecOutputStream(OutputStream outputstream, BaseNCodec basencodec, boolean flag) {
        super(outputstream);
        this.baseNCodec = basencodec;
        this.doEncode = flag;
    }

    public void write(int i) throws IOException {
        this.singleByte[0] = (byte) i;
        this.write(this.singleByte, 0, 1);
    }

    public void write(byte[] abyte, int i, int j) throws IOException {
        if (abyte == null) {
            throw new NullPointerException();
        } else if (i >= 0 && j >= 0) {
            if (i <= abyte.length && i + j <= abyte.length) {
                if (j > 0) {
                    if (this.doEncode) {
                        this.baseNCodec.encode(abyte, i, j, this.context);
                    } else {
                        this.baseNCodec.decode(abyte, i, j, this.context);
                    }

                    this.flush(false);
                }

            } else {
                throw new IndexOutOfBoundsException();
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private void flush(boolean flag) throws IOException {
        int i = this.baseNCodec.available(this.context);

        if (i > 0) {
            byte[] abyte = new byte[i];
            int j = this.baseNCodec.readResults(abyte, 0, i, this.context);

            if (j > 0) {
                this.out.write(abyte, 0, j);
            }
        }

        if (flag) {
            this.out.flush();
        }

    }

    public void flush() throws IOException {
        this.flush(true);
    }

    public void close() throws IOException {
        if (this.doEncode) {
            this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
        } else {
            this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
        }

        this.flush();
        this.out.close();
    }
}
