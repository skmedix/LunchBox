package org.apache.commons.codec.binary;

public class Base32 extends BaseNCodec {

    private static final int BITS_PER_ENCODED_BYTE = 5;
    private static final int BYTES_PER_ENCODED_BLOCK = 8;
    private static final int BYTES_PER_UNENCODED_BLOCK = 5;
    private static final byte[] CHUNK_SEPARATOR = new byte[] { (byte) 13, (byte) 10};
    private static final byte[] DECODE_TABLE = new byte[] { (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25};
    private static final byte[] ENCODE_TABLE = new byte[] { (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55};
    private static final byte[] HEX_DECODE_TABLE = new byte[] { (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, (byte) 32};
    private static final byte[] HEX_ENCODE_TABLE = new byte[] { (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86};
    private static final int MASK_5BITS = 31;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;

    public Base32() {
        this(false);
    }

    public Base32(boolean flag) {
        this(0, (byte[]) null, flag);
    }

    public Base32(int i) {
        this(i, Base32.CHUNK_SEPARATOR);
    }

    public Base32(int i, byte[] abyte) {
        this(i, abyte, false);
    }

    public Base32(int i, byte[] abyte, boolean flag) {
        super(5, 8, i, abyte == null ? 0 : abyte.length);
        if (flag) {
            this.encodeTable = Base32.HEX_ENCODE_TABLE;
            this.decodeTable = Base32.HEX_DECODE_TABLE;
        } else {
            this.encodeTable = Base32.ENCODE_TABLE;
            this.decodeTable = Base32.DECODE_TABLE;
        }

        if (i > 0) {
            if (abyte == null) {
                throw new IllegalArgumentException("lineLength " + i + " > 0, but lineSeparator is null");
            }

            if (this.containsAlphabetOrPad(abyte)) {
                String s = StringUtils.newStringUtf8(abyte);

                throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + s + "]");
            }

            this.encodeSize = 8 + abyte.length;
            this.lineSeparator = new byte[abyte.length];
            System.arraycopy(abyte, 0, this.lineSeparator, 0, abyte.length);
        } else {
            this.encodeSize = 8;
            this.lineSeparator = null;
        }

        this.decodeSize = this.encodeSize - 1;
    }

    void decode(byte[] abyte, int i, int j, BaseNCodec.Context basencodec_context) {
        if (!basencodec_context.eof) {
            if (j < 0) {
                basencodec_context.eof = true;
            }

            for (int k = 0; k < j; ++k) {
                byte b0 = abyte[i++];

                if (b0 == 61) {
                    basencodec_context.eof = true;
                    break;
                }

                byte[] abyte1 = this.ensureBufferSize(this.decodeSize, basencodec_context);

                if (b0 >= 0 && b0 < this.decodeTable.length) {
                    byte b1 = this.decodeTable[b0];

                    if (b1 >= 0) {
                        basencodec_context.modulus = (basencodec_context.modulus + 1) % 8;
                        basencodec_context.lbitWorkArea = (basencodec_context.lbitWorkArea << 5) + (long) b1;
                        if (basencodec_context.modulus == 0) {
                            abyte1[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 32 & 255L));
                            abyte1[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 24 & 255L));
                            abyte1[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 16 & 255L));
                            abyte1[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 8 & 255L));
                            abyte1[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea & 255L));
                        }
                    }
                }
            }

            if (basencodec_context.eof && basencodec_context.modulus >= 2) {
                byte[] abyte2 = this.ensureBufferSize(this.decodeSize, basencodec_context);

                switch (basencodec_context.modulus) {
                case 2:
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 2 & 255L));
                    break;

                case 3:
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 7 & 255L));
                    break;

                case 4:
                    basencodec_context.lbitWorkArea >>= 4;
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 8 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea & 255L));
                    break;

                case 5:
                    basencodec_context.lbitWorkArea >>= 1;
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 16 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 8 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea & 255L));
                    break;

                case 6:
                    basencodec_context.lbitWorkArea >>= 6;
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 16 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 8 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea & 255L));
                    break;

                case 7:
                    basencodec_context.lbitWorkArea >>= 3;
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 24 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 16 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea >> 8 & 255L));
                    abyte2[basencodec_context.pos++] = (byte) ((int) (basencodec_context.lbitWorkArea & 255L));
                    break;

                default:
                    throw new IllegalStateException("Impossible modulus " + basencodec_context.modulus);
                }
            }

        }
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
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 3) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea << 2) & 31];
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    break;

                case 2:
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 11) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 6) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 1) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea << 4) & 31];
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    break;

                case 3:
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 19) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 14) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 9) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 4) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea << 1) & 31];
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    abyte1[basencodec_context.pos++] = 61;
                    break;

                case 4:
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 27) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 22) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 17) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 12) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 7) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 2) & 31];
                    abyte1[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea << 3) & 31];
                    abyte1[basencodec_context.pos++] = 61;
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

                    basencodec_context.modulus = (basencodec_context.modulus + 1) % 5;
                    int i1 = abyte[i++];

                    if (i1 < 0) {
                        i1 += 256;
                    }

                    basencodec_context.lbitWorkArea = (basencodec_context.lbitWorkArea << 8) + (long) i1;
                    if (0 == basencodec_context.modulus) {
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 35) & 31];
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 30) & 31];
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 25) & 31];
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 20) & 31];
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 15) & 31];
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 10) & 31];
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) (basencodec_context.lbitWorkArea >> 5) & 31];
                        abyte2[basencodec_context.pos++] = this.encodeTable[(int) basencodec_context.lbitWorkArea & 31];
                        basencodec_context.currentLinePos += 8;
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

    public boolean isInAlphabet(byte b0) {
        return b0 >= 0 && b0 < this.decodeTable.length && this.decodeTable[b0] != -1;
    }
}
