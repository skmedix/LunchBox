package org.apache.commons.io;

import java.io.Serializable;

public class ByteOrderMark implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final ByteOrderMark UTF_8 = new ByteOrderMark("UTF-8", new int[] { 239, 187, 191});
    public static final ByteOrderMark UTF_16BE = new ByteOrderMark("UTF-16BE", new int[] { 254, 255});
    public static final ByteOrderMark UTF_16LE = new ByteOrderMark("UTF-16LE", new int[] { 255, 254});
    public static final ByteOrderMark UTF_32BE = new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 254, 255});
    public static final ByteOrderMark UTF_32LE = new ByteOrderMark("UTF-32LE", new int[] { 255, 254, 0, 0});
    private final String charsetName;
    private final int[] bytes;

    public ByteOrderMark(String s, int... aint) {
        if (s != null && s.length() != 0) {
            if (aint != null && aint.length != 0) {
                this.charsetName = s;
                this.bytes = new int[aint.length];
                System.arraycopy(aint, 0, this.bytes, 0, aint.length);
            } else {
                throw new IllegalArgumentException("No bytes specified");
            }
        } else {
            throw new IllegalArgumentException("No charsetName specified");
        }
    }

    public String getCharsetName() {
        return this.charsetName;
    }

    public int length() {
        return this.bytes.length;
    }

    public int get(int i) {
        return this.bytes[i];
    }

    public byte[] getBytes() {
        byte[] abyte = new byte[this.bytes.length];

        for (int i = 0; i < this.bytes.length; ++i) {
            abyte[i] = (byte) this.bytes[i];
        }

        return abyte;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ByteOrderMark)) {
            return false;
        } else {
            ByteOrderMark byteordermark = (ByteOrderMark) object;

            if (this.bytes.length != byteordermark.length()) {
                return false;
            } else {
                for (int i = 0; i < this.bytes.length; ++i) {
                    if (this.bytes[i] != byteordermark.get(i)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int hashCode() {
        int i = this.getClass().hashCode();
        int[] aint = this.bytes;
        int j = aint.length;

        for (int k = 0; k < j; ++k) {
            int l = aint[k];

            i += l;
        }

        return i;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(this.getClass().getSimpleName());
        stringbuilder.append('[');
        stringbuilder.append(this.charsetName);
        stringbuilder.append(": ");

        for (int i = 0; i < this.bytes.length; ++i) {
            if (i > 0) {
                stringbuilder.append(",");
            }

            stringbuilder.append("0x");
            stringbuilder.append(Integer.toHexString(255 & this.bytes[i]).toUpperCase());
        }

        stringbuilder.append(']');
        return stringbuilder.toString();
    }
}
