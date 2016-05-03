package org.apache.commons.lang3;

public class BitField {

    private final int _mask;
    private final int _shift_count;

    public BitField(int i) {
        this._mask = i;
        int j = 0;
        int k = i;

        if (i != 0) {
            while ((k & 1) == 0) {
                ++j;
                k >>= 1;
            }
        }

        this._shift_count = j;
    }

    public int getValue(int i) {
        return this.getRawValue(i) >> this._shift_count;
    }

    public short getShortValue(short short0) {
        return (short) this.getValue(short0);
    }

    public int getRawValue(int i) {
        return i & this._mask;
    }

    public short getShortRawValue(short short0) {
        return (short) this.getRawValue(short0);
    }

    public boolean isSet(int i) {
        return (i & this._mask) != 0;
    }

    public boolean isAllSet(int i) {
        return (i & this._mask) == this._mask;
    }

    public int setValue(int i, int j) {
        return i & ~this._mask | j << this._shift_count & this._mask;
    }

    public short setShortValue(short short0, short short1) {
        return (short) this.setValue(short0, short1);
    }

    public int clear(int i) {
        return i & ~this._mask;
    }

    public short clearShort(short short0) {
        return (short) this.clear(short0);
    }

    public byte clearByte(byte b0) {
        return (byte) this.clear(b0);
    }

    public int set(int i) {
        return i | this._mask;
    }

    public short setShort(short short0) {
        return (short) this.set(short0);
    }

    public byte setByte(byte b0) {
        return (byte) this.set(b0);
    }

    public int setBoolean(int i, boolean flag) {
        return flag ? this.set(i) : this.clear(i);
    }

    public short setShortBoolean(short short0, boolean flag) {
        return flag ? this.setShort(short0) : this.clearShort(short0);
    }

    public byte setByteBoolean(byte b0, boolean flag) {
        return flag ? this.setByte(b0) : this.clearByte(b0);
    }
}
