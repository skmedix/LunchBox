package org.apache.commons.codec.binary;

import java.math.BigInteger;

public class Base64 extends BaseNCodec {

    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    static final byte[] CHUNK_SEPARATOR = new byte[] { (byte) 13, (byte) 10};
    private static final byte[] STANDARD_ENCODE_TABLE = new byte[] { (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[] { (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
    private static final byte[] DECODE_TABLE = new byte[] { (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 62, (byte) -1, (byte) 62, (byte) -1, (byte) 63, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 58, (byte) 59, (byte) 60, (byte) 61, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 63, (byte) -1, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51};
    private static final int MASK_6BITS = 63;
    private final byte[] encodeTable;
    private final byte[] decodeTable;
    private final byte[] lineSeparator;
    private final int decodeSize;
    private final int encodeSize;

    public Base64() {
        this(0);
    }

    public Base64(boolean flag) {
        this(76, Base64.CHUNK_SEPARATOR, flag);
    }

    public Base64(int i) {
        this(i, Base64.CHUNK_SEPARATOR);
    }

    public Base64(int i, byte[] abyte) {
        this(i, abyte, false);
    }

    public Base64(int i, byte[] abyte, boolean flag) {
        super(3, 4, i, abyte == null ? 0 : abyte.length);
        this.decodeTable = Base64.DECODE_TABLE;
        if (abyte != null) {
            if (this.containsAlphabetOrPad(abyte)) {
                String s = StringUtils.newStringUtf8(abyte);

                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + s + "]");
            }

            if (i > 0) {
                this.encodeSize = 4 + abyte.length;
                this.lineSeparator = new byte[abyte.length];
                System.arraycopy(abyte, 0, this.lineSeparator, 0, abyte.length);
            } else {
                this.encodeSize = 4;
                this.lineSeparator = null;
            }
        } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
        }

        this.decodeSize = this.encodeSize - 1;
        this.encodeTable = flag ? Base64.URL_SAFE_ENCODE_TABLE : Base64.STANDARD_ENCODE_TABLE;
    }

    public boolean isUrlSafe() {
        return this.encodeTable == Base64.URL_SAFE_ENCODE_TABLE;
    }

    void encode(byte[] abyte, int i, int j, BaseNCodec.Context basencodec_context) {
        if (!basencodec_context.eof) {
            if (j < 0) {
                basencodec_context.eof = true;
                if (0 == basencodec_context.modulus && this.lineLength == 0) {
                    return;
                }

                byte[] abyte1 = this.ensureBufferSize(this.encodeSize, basencodec_context);
                int k = basencodec_context.pos;

                switch (basencodec_context.modulus) {
                case 0:
                    break;

                case 1:
                    abyte1[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea >> 2 & 63];
                    abyte1[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea << 4 & 63];
                    if (this.encodeTable == Base64.STANDARD_ENCODE_TABLE) {
                        abyte1[basencodec_context.pos++] = 61;
                        abyte1[basencodec_context.pos++] = 61;
                    }
                    break;

                case 2:
                    abyte1[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea >> 10 & 63];
                    abyte1[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea >> 4 & 63];
                    abyte1[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea << 2 & 63];
                    if (this.encodeTable == Base64.STANDARD_ENCODE_TABLE) {
                        abyte1[basencodec_context.pos++] = 61;
                    }
                    break;

                default:
                    throw new IllegalStateException("Impossible modulus " + basencodec_context.modulus);
                }

                basencodec_context.currentLinePos += basencodec_context.pos - k;
                if (this.lineLength > 0 && basencodec_context.currentLinePos > 0) {
                    System.arraycopy(this.lineSeparator, 0, abyte1, basencodec_context.pos, this.lineSeparator.length);
                    basencodec_context.pos += this.lineSeparator.length;
                }
            } else {
                for (int l = 0; l < j; ++l) {
                    byte[] abyte2 = this.ensureBufferSize(this.encodeSize, basencodec_context);

                    basencodec_context.modulus = (basencodec_context.modulus + 1) % 3;
                    int i1 = abyte[i++];

                    if (i1 < 0) {
                        i1 += 256;
                    }

                    basencodec_context.ibitWorkArea = (basencodec_context.ibitWorkArea << 8) + i1;
                    if (0 == basencodec_context.modulus) {
                        abyte2[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea >> 18 & 63];
                        abyte2[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea >> 12 & 63];
                        abyte2[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea >> 6 & 63];
                        abyte2[basencodec_context.pos++] = this.encodeTable[basencodec_context.ibitWorkArea & 63];
                        basencodec_context.currentLinePos += 4;
                        if (this.lineLength > 0 && this.lineLength <= basencodec_context.currentLinePos) {
                            System.arraycopy(this.lineSeparator, 0, abyte2, basencodec_context.pos, this.lineSeparator.length);
                            basencodec_context.pos += this.lineSeparator.length;
                            basencodec_context.currentLinePos = 0;
                        }
                    }
                }
            }

        }
    }

    void decode(byte[] abyte, int i, int j, BaseNCodec.Context basencodec_context) {
        if (!basencodec_context.eof) {
            if (j < 0) {
                basencodec_context.eof = true;
            }

            for (int k = 0; k < j; ++k) {
                byte[] abyte1 = this.ensureBufferSize(this.decodeSize, basencodec_context);
                byte b0 = abyte[i++];

                if (b0 == 61) {
                    basencodec_context.eof = true;
                    break;
                }

                if (b0 >= 0 && b0 < Base64.DECODE_TABLE.length) {
                    byte b1 = Base64.DECODE_TABLE[b0];

                    if (b1 >= 0) {
                        basencodec_context.modulus = (basencodec_context.modulus + 1) % 4;
                        basencodec_context.ibitWorkArea = (basencodec_context.ibitWorkArea << 6) + b1;
                        if (basencodec_context.modulus == 0) {
                            abyte1[basencodec_context.pos++] = (byte) (basencodec_context.ibitWorkArea >> 16 & 255);
                            abyte1[basencodec_context.pos++] = (byte) (basencodec_context.ibitWorkArea >> 8 & 255);
                            abyte1[basencodec_context.pos++] = (byte) (basencodec_context.ibitWorkArea & 255);
                        }
                    }
                }
            }

            if (basencodec_context.eof && basencodec_context.modulus != 0) {
                byte[] abyte2 = this.ensureBufferSize(this.decodeSize, basencodec_context);

                switch (basencodec_context.modulus) {
                case 1:
                    break;

                case 2:
                    basencodec_context.ibitWorkArea >>= 4;
                    abyte2[basencodec_context.pos++] = (byte) (basencodec_context.ibitWorkArea & 255);
                    break;

                case 3:
                    basencodec_context.ibitWorkArea >>= 2;
                    abyte2[basencodec_context.pos++] = (byte) (basencodec_context.ibitWorkArea >> 8 & 255);
                    abyte2[basencodec_context.pos++] = (byte) (basencodec_context.ibitWorkArea & 255);
                    break;

                default:
                    throw new IllegalStateException("Impossible modulus " + basencodec_context.modulus);
                }
            }

        }
    }

    /** @deprecated */
    @Deprecated
    public static boolean isArrayByteBase64(byte[] abyte) {
        return isBase64(abyte);
    }

    public static boolean isBase64(byte b0) {
        return b0 == 61 || b0 >= 0 && b0 < Base64.DECODE_TABLE.length && Base64.DECODE_TABLE[b0] != -1;
    }

    public static boolean isBase64(String s) {
        return isBase64(StringUtils.getBytesUtf8(s));
    }

    public static boolean isBase64(byte[] abyte) {
        for (int i = 0; i < abyte.length; ++i) {
            if (!isBase64(abyte[i]) && !isWhiteSpace(abyte[i])) {
                return false;
            }
        }

        return true;
    }

    public static byte[] encodeBase64(byte[] abyte) {
        return encodeBase64(abyte, false);
    }

    public static String encodeBase64String(byte[] abyte) {
        return StringUtils.newStringUtf8(encodeBase64(abyte, false));
    }

    public static byte[] encodeBase64URLSafe(byte[] abyte) {
        return encodeBase64(abyte, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] abyte) {
        return StringUtils.newStringUtf8(encodeBase64(abyte, false, true));
    }

    public static byte[] encodeBase64Chunked(byte[] abyte) {
        return encodeBase64(abyte, true);
    }

    public static byte[] encodeBase64(byte[] abyte, boolean flag) {
        return encodeBase64(abyte, flag, false);
    }

    public static byte[] encodeBase64(byte[] abyte, boolean flag, boolean flag1) {
        return encodeBase64(abyte, flag, flag1, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] abyte, boolean flag, boolean flag1, int i) {
        if (abyte != null && abyte.length != 0) {
            Base64 base64 = flag ? new Base64(flag1) : new Base64(0, Base64.CHUNK_SEPARATOR, flag1);
            long j = base64.getEncodedLength(abyte);

            if (j > (long) i) {
                throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + j + ") than the specified maximum size of " + i);
            } else {
                return base64.encode(abyte);
            }
        } else {
            return abyte;
        }
    }

    public static byte[] decodeBase64(String s) {
        return (new Base64()).decode(s);
    }

    public static byte[] decodeBase64(byte[] abyte) {
        return (new Base64()).decode(abyte);
    }

    public static BigInteger decodeInteger(byte[] abyte) {
        return new BigInteger(1, decodeBase64(abyte));
    }

    public static byte[] encodeInteger(BigInteger biginteger) {
        if (biginteger == null) {
            throw new NullPointerException("encodeInteger called with null parameter");
        } else {
            return encodeBase64(toIntegerBytes(biginteger), false);
        }
    }

    static byte[] toIntegerBytes(BigInteger biginteger) {
        int i = biginteger.bitLength();

        i = i + 7 >> 3 << 3;
        byte[] abyte = biginteger.toByteArray();

        if (biginteger.bitLength() % 8 != 0 && biginteger.bitLength() / 8 + 1 == i / 8) {
            return abyte;
        } else {
            byte b0 = 0;
            int j = abyte.length;

            if (biginteger.bitLength() % 8 == 0) {
                b0 = 1;
                --j;
            }

            int k = i / 8 - j;
            byte[] abyte1 = new byte[i / 8];

            System.arraycopy(abyte, b0, abyte1, k, j);
            return abyte1;
        }
    }

    protected boolean isInAlphabet(byte b0) {
        return b0 >= 0 && b0 < this.decodeTable.length && this.decodeTable[b0] != -1;
    }
}
