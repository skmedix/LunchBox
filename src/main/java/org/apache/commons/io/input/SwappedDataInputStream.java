package org.apache.commons.io.input;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.EndianUtils;

public class SwappedDataInputStream extends ProxyInputStream implements DataInput {

    public SwappedDataInputStream(InputStream inputstream) {
        super(inputstream);
    }

    public boolean readBoolean() throws IOException, EOFException {
        return 0 != this.readByte();
    }

    public byte readByte() throws IOException, EOFException {
        return (byte) this.in.read();
    }

    public char readChar() throws IOException, EOFException {
        return (char) this.readShort();
    }

    public double readDouble() throws IOException, EOFException {
        return EndianUtils.readSwappedDouble(this.in);
    }

    public float readFloat() throws IOException, EOFException {
        return EndianUtils.readSwappedFloat(this.in);
    }

    public void readFully(byte[] abyte) throws IOException, EOFException {
        this.readFully(abyte, 0, abyte.length);
    }

    public void readFully(byte[] abyte, int i, int j) throws IOException, EOFException {
        int k;

        for (int l = j; l > 0; l -= k) {
            int i1 = i + j - l;

            k = this.read(abyte, i1, l);
            if (-1 == k) {
                throw new EOFException();
            }
        }

    }

    public int readInt() throws IOException, EOFException {
        return EndianUtils.readSwappedInteger(this.in);
    }

    public String readLine() throws IOException, EOFException {
        throw new UnsupportedOperationException("Operation not supported: readLine()");
    }

    public long readLong() throws IOException, EOFException {
        return EndianUtils.readSwappedLong(this.in);
    }

    public short readShort() throws IOException, EOFException {
        return EndianUtils.readSwappedShort(this.in);
    }

    public int readUnsignedByte() throws IOException, EOFException {
        return this.in.read();
    }

    public int readUnsignedShort() throws IOException, EOFException {
        return EndianUtils.readSwappedUnsignedShort(this.in);
    }

    public String readUTF() throws IOException, EOFException {
        throw new UnsupportedOperationException("Operation not supported: readUTF()");
    }

    public int skipBytes(int i) throws IOException, EOFException {
        return (int) this.in.skip((long) i);
    }
}
