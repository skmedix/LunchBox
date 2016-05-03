package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class BinaryCodec implements BinaryDecoder, BinaryEncoder {

    private static final char[] EMPTY_CHAR_ARRAY = new char[0];
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final int BIT_0 = 1;
    private static final int BIT_1 = 2;
    private static final int BIT_2 = 4;
    private static final int BIT_3 = 8;
    private static final int BIT_4 = 16;
    private static final int BIT_5 = 32;
    private static final int BIT_6 = 64;
    private static final int BIT_7 = 128;
    private static final int[] BITS = new int[] { 1, 2, 4, 8, 16, 32, 64, 128};

    public byte[] encode(byte[] abyte) {
        return toAsciiBytes(abyte);
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof byte[])) {
            throw new EncoderException("argument not a byte array");
        } else {
            return toAsciiChars((byte[]) ((byte[]) object));
        }
    }

    public Object decode(Object object) throws DecoderException {
        if (object == null) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        } else if (object instanceof byte[]) {
            return fromAscii((byte[]) ((byte[]) object));
        } else if (object instanceof char[]) {
            return fromAscii((char[]) ((char[]) object));
        } else if (object instanceof String) {
            return fromAscii(((String) object).toCharArray());
        } else {
            throw new DecoderException("argument not a byte array");
        }
    }

    public byte[] decode(byte[] abyte) {
        return fromAscii(abyte);
    }

    public byte[] toByteArray(String s) {
        return s == null ? BinaryCodec.EMPTY_BYTE_ARRAY : fromAscii(s.toCharArray());
    }

    public static byte[] fromAscii(char[] achar) {
        if (achar != null && achar.length != 0) {
            byte[] abyte = new byte[achar.length >> 3];
            int i = 0;

            for (int j = achar.length - 1; i < abyte.length; j -= 8) {
                for (int k = 0; k < BinaryCodec.BITS.length; ++k) {
                    if (achar[j - k] == 49) {
                        abyte[i] = (byte) (abyte[i] | BinaryCodec.BITS[k]);
                    }
                }

                ++i;
            }

            return abyte;
        } else {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        }
    }

    public static byte[] fromAscii(byte[] abyte) {
        if (isEmpty(abyte)) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        } else {
            byte[] abyte1 = new byte[abyte.length >> 3];
            int i = 0;

            for (int j = abyte.length - 1; i < abyte1.length; j -= 8) {
                for (int k = 0; k < BinaryCodec.BITS.length; ++k) {
                    if (abyte[j - k] == 49) {
                        abyte1[i] = (byte) (abyte1[i] | BinaryCodec.BITS[k]);
                    }
                }

                ++i;
            }

            return abyte1;
        }
    }

    private static boolean isEmpty(byte[] abyte) {
        return abyte == null || abyte.length == 0;
    }

    public static byte[] toAsciiBytes(byte[] abyte) {
        if (isEmpty(abyte)) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        } else {
            byte[] abyte1 = new byte[abyte.length << 3];
            int i = 0;

            for (int j = abyte1.length - 1; i < abyte.length; j -= 8) {
                for (int k = 0; k < BinaryCodec.BITS.length; ++k) {
                    if ((abyte[i] & BinaryCodec.BITS[k]) == 0) {
                        abyte1[j - k] = 48;
                    } else {
                        abyte1[j - k] = 49;
                    }
                }

                ++i;
            }

            return abyte1;
        }
    }

    public static char[] toAsciiChars(byte[] abyte) {
        if (isEmpty(abyte)) {
            return BinaryCodec.EMPTY_CHAR_ARRAY;
        } else {
            char[] achar = new char[abyte.length << 3];
            int i = 0;

            for (int j = achar.length - 1; i < abyte.length; j -= 8) {
                for (int k = 0; k < BinaryCodec.BITS.length; ++k) {
                    if ((abyte[i] & BinaryCodec.BITS[k]) == 0) {
                        achar[j - k] = 48;
                    } else {
                        achar[j - k] = 49;
                    }
                }

                ++i;
            }

            return achar;
        }
    }

    public static String toAsciiString(byte[] abyte) {
        return new String(toAsciiChars(abyte));
    }
}
