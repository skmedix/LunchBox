package org.apache.commons.lang3;

import java.util.UUID;

public class Conversion {

    public static int hexDigitToInt(char c0) {
        int i = Character.digit(c0, 16);

        if (i < 0) {
            throw new IllegalArgumentException("Cannot interpret \'" + c0 + "\' as a hexadecimal digit");
        } else {
            return i;
        }
    }

    public static int hexDigitMsb0ToInt(char c0) {
        switch (c0) {
        case '0':
            return 0;

        case '1':
            return 8;

        case '2':
            return 4;

        case '3':
            return 12;

        case '4':
            return 2;

        case '5':
            return 10;

        case '6':
            return 6;

        case '7':
            return 14;

        case '8':
            return 1;

        case '9':
            return 9;

        case ':':
        case ';':
        case '<':
        case '=':
        case '>':
        case '?':
        case '@':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '[':
        case '\\':
        case ']':
        case '^':
        case '_':
        case '`':
        default:
            throw new IllegalArgumentException("Cannot interpret \'" + c0 + "\' as a hexadecimal digit");

        case 'A':
        case 'a':
            return 5;

        case 'B':
        case 'b':
            return 13;

        case 'C':
        case 'c':
            return 3;

        case 'D':
        case 'd':
            return 11;

        case 'E':
        case 'e':
            return 7;

        case 'F':
        case 'f':
            return 15;
        }
    }

    public static boolean[] hexDigitToBinary(char c0) {
        switch (c0) {
        case '0':
            return new boolean[] { false, false, false, false};

        case '1':
            return new boolean[] { true, false, false, false};

        case '2':
            return new boolean[] { false, true, false, false};

        case '3':
            return new boolean[] { true, true, false, false};

        case '4':
            return new boolean[] { false, false, true, false};

        case '5':
            return new boolean[] { true, false, true, false};

        case '6':
            return new boolean[] { false, true, true, false};

        case '7':
            return new boolean[] { true, true, true, false};

        case '8':
            return new boolean[] { false, false, false, true};

        case '9':
            return new boolean[] { true, false, false, true};

        case ':':
        case ';':
        case '<':
        case '=':
        case '>':
        case '?':
        case '@':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '[':
        case '\\':
        case ']':
        case '^':
        case '_':
        case '`':
        default:
            throw new IllegalArgumentException("Cannot interpret \'" + c0 + "\' as a hexadecimal digit");

        case 'A':
        case 'a':
            return new boolean[] { false, true, false, true};

        case 'B':
        case 'b':
            return new boolean[] { true, true, false, true};

        case 'C':
        case 'c':
            return new boolean[] { false, false, true, true};

        case 'D':
        case 'd':
            return new boolean[] { true, false, true, true};

        case 'E':
        case 'e':
            return new boolean[] { false, true, true, true};

        case 'F':
        case 'f':
            return new boolean[] { true, true, true, true};
        }
    }

    public static boolean[] hexDigitMsb0ToBinary(char c0) {
        switch (c0) {
        case '0':
            return new boolean[] { false, false, false, false};

        case '1':
            return new boolean[] { false, false, false, true};

        case '2':
            return new boolean[] { false, false, true, false};

        case '3':
            return new boolean[] { false, false, true, true};

        case '4':
            return new boolean[] { false, true, false, false};

        case '5':
            return new boolean[] { false, true, false, true};

        case '6':
            return new boolean[] { false, true, true, false};

        case '7':
            return new boolean[] { false, true, true, true};

        case '8':
            return new boolean[] { true, false, false, false};

        case '9':
            return new boolean[] { true, false, false, true};

        case ':':
        case ';':
        case '<':
        case '=':
        case '>':
        case '?':
        case '@':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '[':
        case '\\':
        case ']':
        case '^':
        case '_':
        case '`':
        default:
            throw new IllegalArgumentException("Cannot interpret \'" + c0 + "\' as a hexadecimal digit");

        case 'A':
        case 'a':
            return new boolean[] { true, false, true, false};

        case 'B':
        case 'b':
            return new boolean[] { true, false, true, true};

        case 'C':
        case 'c':
            return new boolean[] { true, true, false, false};

        case 'D':
        case 'd':
            return new boolean[] { true, true, false, true};

        case 'E':
        case 'e':
            return new boolean[] { true, true, true, false};

        case 'F':
        case 'f':
            return new boolean[] { true, true, true, true};
        }
    }

    public static char binaryToHexDigit(boolean[] aboolean) {
        return binaryToHexDigit(aboolean, 0);
    }

    public static char binaryToHexDigit(boolean[] aboolean, int i) {
        if (aboolean.length == 0) {
            throw new IllegalArgumentException("Cannot convert an empty array.");
        } else {
            return (char) (aboolean.length > i + 3 && aboolean[i + 3] ? (aboolean.length > i + 2 && aboolean[i + 2] ? (aboolean.length > i + 1 && aboolean[i + 1] ? (aboolean[i] ? 'f' : 'e') : (aboolean[i] ? 'd' : 'c')) : (aboolean.length > i + 1 && aboolean[i + 1] ? (aboolean[i] ? 'b' : 'a') : (aboolean[i] ? '9' : '8'))) : (aboolean.length > i + 2 && aboolean[i + 2] ? (aboolean.length > i + 1 && aboolean[i + 1] ? (aboolean[i] ? '7' : '6') : (aboolean[i] ? '5' : '4')) : (aboolean.length > i + 1 && aboolean[i + 1] ? (aboolean[i] ? '3' : '2') : (aboolean[i] ? '1' : '0'))));
        }
    }

    public static char binaryToHexDigitMsb0_4bits(boolean[] aboolean) {
        return binaryToHexDigitMsb0_4bits(aboolean, 0);
    }

    public static char binaryToHexDigitMsb0_4bits(boolean[] aboolean, int i) {
        if (aboolean.length > 8) {
            throw new IllegalArgumentException("src.length>8: src.length=" + aboolean.length);
        } else if (aboolean.length - i < 4) {
            throw new IllegalArgumentException("src.length-srcPos<4: src.length=" + aboolean.length + ", srcPos=" + i);
        } else {
            return (char) (aboolean[i + 3] ? (aboolean[i + 2] ? (aboolean[i + 1] ? (aboolean[i] ? 'f' : '7') : (aboolean[i] ? 'b' : '3')) : (aboolean[i + 1] ? (aboolean[i] ? 'd' : '5') : (aboolean[i] ? '9' : '1'))) : (aboolean[i + 2] ? (aboolean[i + 1] ? (aboolean[i] ? 'e' : '6') : (aboolean[i] ? 'a' : '2')) : (aboolean[i + 1] ? (aboolean[i] ? 'c' : '4') : (aboolean[i] ? '8' : '0'))));
        }
    }

    public static char binaryBeMsb0ToHexDigit(boolean[] aboolean) {
        return binaryBeMsb0ToHexDigit(aboolean, 0);
    }

    public static char binaryBeMsb0ToHexDigit(boolean[] aboolean, int i) {
        if (aboolean.length == 0) {
            throw new IllegalArgumentException("Cannot convert an empty array.");
        } else {
            int j = aboolean.length - 1 - i;
            int k = Math.min(4, j + 1);
            boolean[] aboolean1 = new boolean[4];

            System.arraycopy(aboolean, j + 1 - k, aboolean1, 4 - k, k);
            byte b0 = 0;

            return (char) (aboolean1[b0] ? (aboolean1.length > b0 + 1 && aboolean1[b0 + 1] ? (aboolean1.length > b0 + 2 && aboolean1[b0 + 2] ? (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? 'f' : 'e') : (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? 'd' : 'c')) : (aboolean1.length > b0 + 2 && aboolean1[b0 + 2] ? (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? 'b' : 'a') : (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? '9' : '8'))) : (aboolean1.length > b0 + 1 && aboolean1[b0 + 1] ? (aboolean1.length > b0 + 2 && aboolean1[b0 + 2] ? (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? '7' : '6') : (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? '5' : '4')) : (aboolean1.length > b0 + 2 && aboolean1[b0 + 2] ? (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? '3' : '2') : (aboolean1.length > b0 + 3 && aboolean1[b0 + 3] ? '1' : '0'))));
        }
    }

    public static char intToHexDigit(int i) {
        char c0 = Character.forDigit(i, 16);

        if (c0 == 0) {
            throw new IllegalArgumentException("nibble value not between 0 and 15: " + i);
        } else {
            return c0;
        }
    }

    public static char intToHexDigitMsb0(int i) {
        switch (i) {
        case 0:
            return '0';

        case 1:
            return '8';

        case 2:
            return '4';

        case 3:
            return 'c';

        case 4:
            return '2';

        case 5:
            return 'a';

        case 6:
            return '6';

        case 7:
            return 'e';

        case 8:
            return '1';

        case 9:
            return '9';

        case 10:
            return '5';

        case 11:
            return 'd';

        case 12:
            return '3';

        case 13:
            return 'b';

        case 14:
            return '7';

        case 15:
            return 'f';

        default:
            throw new IllegalArgumentException("nibble value not between 0 and 15: " + i);
        }
    }

    public static long intArrayToLong(int[] aint, int i, long j, int k, int l) {
        if ((aint.length != 0 || i != 0) && 0 != l) {
            if ((l - 1) * 32 + k >= 64) {
                throw new IllegalArgumentException("(nInts-1)*32+dstPos is greather or equal to than 64");
            } else {
                long i1 = j;
                boolean flag = false;

                for (int j1 = 0; j1 < l; ++j1) {
                    int k1 = j1 * 32 + k;
                    long l1 = (4294967295L & (long) aint[j1 + i]) << k1;
                    long i2 = 4294967295L << k1;

                    i1 = i1 & ~i2 | l1;
                }

                return i1;
            }
        } else {
            return j;
        }
    }

    public static long shortArrayToLong(short[] ashort, int i, long j, int k, int l) {
        if ((ashort.length != 0 || i != 0) && 0 != l) {
            if ((l - 1) * 16 + k >= 64) {
                throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 64");
            } else {
                long i1 = j;
                boolean flag = false;

                for (int j1 = 0; j1 < l; ++j1) {
                    int k1 = j1 * 16 + k;
                    long l1 = (65535L & (long) ashort[j1 + i]) << k1;
                    long i2 = 65535L << k1;

                    i1 = i1 & ~i2 | l1;
                }

                return i1;
            }
        } else {
            return j;
        }
    }

    public static int shortArrayToInt(short[] ashort, int i, int j, int k, int l) {
        if ((ashort.length != 0 || i != 0) && 0 != l) {
            if ((l - 1) * 16 + k >= 32) {
                throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 32");
            } else {
                int i1 = j;
                boolean flag = false;

                for (int j1 = 0; j1 < l; ++j1) {
                    int k1 = j1 * 16 + k;
                    int l1 = ('\uffff' & ashort[j1 + i]) << k1;
                    int i2 = '\uffff' << k1;

                    i1 = i1 & ~i2 | l1;
                }

                return i1;
            }
        } else {
            return j;
        }
    }

    public static long byteArrayToLong(byte[] abyte, int i, long j, int k, int l) {
        if ((abyte.length != 0 || i != 0) && 0 != l) {
            if ((l - 1) * 8 + k >= 64) {
                throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 64");
            } else {
                long i1 = j;
                boolean flag = false;

                for (int j1 = 0; j1 < l; ++j1) {
                    int k1 = j1 * 8 + k;
                    long l1 = (255L & (long) abyte[j1 + i]) << k1;
                    long i2 = 255L << k1;

                    i1 = i1 & ~i2 | l1;
                }

                return i1;
            }
        } else {
            return j;
        }
    }

    public static int byteArrayToInt(byte[] abyte, int i, int j, int k, int l) {
        if ((abyte.length != 0 || i != 0) && 0 != l) {
            if ((l - 1) * 8 + k >= 32) {
                throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 32");
            } else {
                int i1 = j;
                boolean flag = false;

                for (int j1 = 0; j1 < l; ++j1) {
                    int k1 = j1 * 8 + k;
                    int l1 = (255 & abyte[j1 + i]) << k1;
                    int i2 = 255 << k1;

                    i1 = i1 & ~i2 | l1;
                }

                return i1;
            }
        } else {
            return j;
        }
    }

    public static short byteArrayToShort(byte[] abyte, int i, short short0, int j, int k) {
        if ((abyte.length != 0 || i != 0) && 0 != k) {
            if ((k - 1) * 8 + j >= 16) {
                throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 16");
            } else {
                short short1 = short0;
                boolean flag = false;

                for (int l = 0; l < k; ++l) {
                    int i1 = l * 8 + j;
                    int j1 = (255 & abyte[l + i]) << i1;
                    int k1 = 255 << i1;

                    short1 = (short) (short1 & ~k1 | j1);
                }

                return short1;
            }
        } else {
            return short0;
        }
    }

    public static long hexToLong(String s, int i, long j, int k, int l) {
        if (0 == l) {
            return j;
        } else if ((l - 1) * 4 + k >= 64) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 64");
        } else {
            long i1 = j;
            boolean flag = false;

            for (int j1 = 0; j1 < l; ++j1) {
                int k1 = j1 * 4 + k;
                long l1 = (15L & (long) hexDigitToInt(s.charAt(j1 + i))) << k1;
                long i2 = 15L << k1;

                i1 = i1 & ~i2 | l1;
            }

            return i1;
        }
    }

    public static int hexToInt(String s, int i, int j, int k, int l) {
        if (0 == l) {
            return j;
        } else if ((l - 1) * 4 + k >= 32) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 32");
        } else {
            int i1 = j;
            boolean flag = false;

            for (int j1 = 0; j1 < l; ++j1) {
                int k1 = j1 * 4 + k;
                int l1 = (15 & hexDigitToInt(s.charAt(j1 + i))) << k1;
                int i2 = 15 << k1;

                i1 = i1 & ~i2 | l1;
            }

            return i1;
        }
    }

    public static short hexToShort(String s, int i, short short0, int j, int k) {
        if (0 == k) {
            return short0;
        } else if ((k - 1) * 4 + j >= 16) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 16");
        } else {
            short short1 = short0;
            boolean flag = false;

            for (int l = 0; l < k; ++l) {
                int i1 = l * 4 + j;
                int j1 = (15 & hexDigitToInt(s.charAt(l + i))) << i1;
                int k1 = 15 << i1;

                short1 = (short) (short1 & ~k1 | j1);
            }

            return short1;
        }
    }

    public static byte hexToByte(String s, int i, byte b0, int j, int k) {
        if (0 == k) {
            return b0;
        } else if ((k - 1) * 4 + j >= 8) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 8");
        } else {
            byte b1 = b0;
            boolean flag = false;

            for (int l = 0; l < k; ++l) {
                int i1 = l * 4 + j;
                int j1 = (15 & hexDigitToInt(s.charAt(l + i))) << i1;
                int k1 = 15 << i1;

                b1 = (byte) (b1 & ~k1 | j1);
            }

            return b1;
        }
    }

    public static long binaryToLong(boolean[] aboolean, int i, long j, int k, int l) {
        if ((aboolean.length != 0 || i != 0) && 0 != l) {
            if (l - 1 + k >= 64) {
                throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 64");
            } else {
                long i1 = j;
                boolean flag = false;

                for (int j1 = 0; j1 < l; ++j1) {
                    int k1 = j1 * 1 + k;
                    long l1 = (aboolean[j1 + i] ? 1L : 0L) << k1;
                    long i2 = 1L << k1;

                    i1 = i1 & ~i2 | l1;
                }

                return i1;
            }
        } else {
            return j;
        }
    }

    public static int binaryToInt(boolean[] aboolean, int i, int j, int k, int l) {
        if ((aboolean.length != 0 || i != 0) && 0 != l) {
            if (l - 1 + k >= 32) {
                throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 32");
            } else {
                int i1 = j;
                boolean flag = false;

                for (int j1 = 0; j1 < l; ++j1) {
                    int k1 = j1 * 1 + k;
                    int l1 = (aboolean[j1 + i] ? 1 : 0) << k1;
                    int i2 = 1 << k1;

                    i1 = i1 & ~i2 | l1;
                }

                return i1;
            }
        } else {
            return j;
        }
    }

    public static short binaryToShort(boolean[] aboolean, int i, short short0, int j, int k) {
        if ((aboolean.length != 0 || i != 0) && 0 != k) {
            if (k - 1 + j >= 16) {
                throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 16");
            } else {
                short short1 = short0;
                boolean flag = false;

                for (int l = 0; l < k; ++l) {
                    int i1 = l * 1 + j;
                    int j1 = (aboolean[l + i] ? 1 : 0) << i1;
                    int k1 = 1 << i1;

                    short1 = (short) (short1 & ~k1 | j1);
                }

                return short1;
            }
        } else {
            return short0;
        }
    }

    public static byte binaryToByte(boolean[] aboolean, int i, byte b0, int j, int k) {
        if ((aboolean.length != 0 || i != 0) && 0 != k) {
            if (k - 1 + j >= 8) {
                throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 8");
            } else {
                byte b1 = b0;
                boolean flag = false;

                for (int l = 0; l < k; ++l) {
                    int i1 = l * 1 + j;
                    int j1 = (aboolean[l + i] ? 1 : 0) << i1;
                    int k1 = 1 << i1;

                    b1 = (byte) (b1 & ~k1 | j1);
                }

                return b1;
            }
        } else {
            return b0;
        }
    }

    public static int[] longToIntArray(long i, int j, int[] aint, int k, int l) {
        if (0 == l) {
            return aint;
        } else if ((l - 1) * 32 + j >= 64) {
            throw new IllegalArgumentException("(nInts-1)*32+srcPos is greather or equal to than 64");
        } else {
            boolean flag = false;

            for (int i1 = 0; i1 < l; ++i1) {
                int j1 = i1 * 32 + j;

                aint[k + i1] = (int) (-1L & i >> j1);
            }

            return aint;
        }
    }

    public static short[] longToShortArray(long i, int j, short[] ashort, int k, int l) {
        if (0 == l) {
            return ashort;
        } else if ((l - 1) * 16 + j >= 64) {
            throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 64");
        } else {
            boolean flag = false;

            for (int i1 = 0; i1 < l; ++i1) {
                int j1 = i1 * 16 + j;

                ashort[k + i1] = (short) ((int) (65535L & i >> j1));
            }

            return ashort;
        }
    }

    public static short[] intToShortArray(int i, int j, short[] ashort, int k, int l) {
        if (0 == l) {
            return ashort;
        } else if ((l - 1) * 16 + j >= 32) {
            throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 32");
        } else {
            boolean flag = false;

            for (int i1 = 0; i1 < l; ++i1) {
                int j1 = i1 * 16 + j;

                ashort[k + i1] = (short) ('\uffff' & i >> j1);
            }

            return ashort;
        }
    }

    public static byte[] longToByteArray(long i, int j, byte[] abyte, int k, int l) {
        if (0 == l) {
            return abyte;
        } else if ((l - 1) * 8 + j >= 64) {
            throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 64");
        } else {
            boolean flag = false;

            for (int i1 = 0; i1 < l; ++i1) {
                int j1 = i1 * 8 + j;

                abyte[k + i1] = (byte) ((int) (255L & i >> j1));
            }

            return abyte;
        }
    }

    public static byte[] intToByteArray(int i, int j, byte[] abyte, int k, int l) {
        if (0 == l) {
            return abyte;
        } else if ((l - 1) * 8 + j >= 32) {
            throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 32");
        } else {
            boolean flag = false;

            for (int i1 = 0; i1 < l; ++i1) {
                int j1 = i1 * 8 + j;

                abyte[k + i1] = (byte) (255 & i >> j1);
            }

            return abyte;
        }
    }

    public static byte[] shortToByteArray(short short0, int i, byte[] abyte, int j, int k) {
        if (0 == k) {
            return abyte;
        } else if ((k - 1) * 8 + i >= 16) {
            throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 16");
        } else {
            boolean flag = false;

            for (int l = 0; l < k; ++l) {
                int i1 = l * 8 + i;

                abyte[j + l] = (byte) (255 & short0 >> i1);
            }

            return abyte;
        }
    }

    public static String longToHex(long i, int j, String s, int k, int l) {
        if (0 == l) {
            return s;
        } else if ((l - 1) * 4 + j >= 64) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 64");
        } else {
            StringBuilder stringbuilder = new StringBuilder(s);
            boolean flag = false;
            int i1 = stringbuilder.length();

            for (int j1 = 0; j1 < l; ++j1) {
                int k1 = j1 * 4 + j;
                int l1 = (int) (15L & i >> k1);

                if (k + j1 == i1) {
                    ++i1;
                    stringbuilder.append(intToHexDigit(l1));
                } else {
                    stringbuilder.setCharAt(k + j1, intToHexDigit(l1));
                }
            }

            return stringbuilder.toString();
        }
    }

    public static String intToHex(int i, int j, String s, int k, int l) {
        if (0 == l) {
            return s;
        } else if ((l - 1) * 4 + j >= 32) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 32");
        } else {
            StringBuilder stringbuilder = new StringBuilder(s);
            boolean flag = false;
            int i1 = stringbuilder.length();

            for (int j1 = 0; j1 < l; ++j1) {
                int k1 = j1 * 4 + j;
                int l1 = 15 & i >> k1;

                if (k + j1 == i1) {
                    ++i1;
                    stringbuilder.append(intToHexDigit(l1));
                } else {
                    stringbuilder.setCharAt(k + j1, intToHexDigit(l1));
                }
            }

            return stringbuilder.toString();
        }
    }

    public static String shortToHex(short short0, int i, String s, int j, int k) {
        if (0 == k) {
            return s;
        } else if ((k - 1) * 4 + i >= 16) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 16");
        } else {
            StringBuilder stringbuilder = new StringBuilder(s);
            boolean flag = false;
            int l = stringbuilder.length();

            for (int i1 = 0; i1 < k; ++i1) {
                int j1 = i1 * 4 + i;
                int k1 = 15 & short0 >> j1;

                if (j + i1 == l) {
                    ++l;
                    stringbuilder.append(intToHexDigit(k1));
                } else {
                    stringbuilder.setCharAt(j + i1, intToHexDigit(k1));
                }
            }

            return stringbuilder.toString();
        }
    }

    public static String byteToHex(byte b0, int i, String s, int j, int k) {
        if (0 == k) {
            return s;
        } else if ((k - 1) * 4 + i >= 8) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 8");
        } else {
            StringBuilder stringbuilder = new StringBuilder(s);
            boolean flag = false;
            int l = stringbuilder.length();

            for (int i1 = 0; i1 < k; ++i1) {
                int j1 = i1 * 4 + i;
                int k1 = 15 & b0 >> j1;

                if (j + i1 == l) {
                    ++l;
                    stringbuilder.append(intToHexDigit(k1));
                } else {
                    stringbuilder.setCharAt(j + i1, intToHexDigit(k1));
                }
            }

            return stringbuilder.toString();
        }
    }

    public static boolean[] longToBinary(long i, int j, boolean[] aboolean, int k, int l) {
        if (0 == l) {
            return aboolean;
        } else if (l - 1 + j >= 64) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 64");
        } else {
            boolean flag = false;

            for (int i1 = 0; i1 < l; ++i1) {
                int j1 = i1 * 1 + j;

                aboolean[k + i1] = (1L & i >> j1) != 0L;
            }

            return aboolean;
        }
    }

    public static boolean[] intToBinary(int i, int j, boolean[] aboolean, int k, int l) {
        if (0 == l) {
            return aboolean;
        } else if (l - 1 + j >= 32) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 32");
        } else {
            boolean flag = false;

            for (int i1 = 0; i1 < l; ++i1) {
                int j1 = i1 * 1 + j;

                aboolean[k + i1] = (1 & i >> j1) != 0;
            }

            return aboolean;
        }
    }

    public static boolean[] shortToBinary(short short0, int i, boolean[] aboolean, int j, int k) {
        if (0 == k) {
            return aboolean;
        } else if (k - 1 + i >= 16) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 16");
        } else {
            boolean flag = false;

            assert (k - 1) * 1 < 16 - i;

            for (int l = 0; l < k; ++l) {
                int i1 = l * 1 + i;

                aboolean[j + l] = (1 & short0 >> i1) != 0;
            }

            return aboolean;
        }
    }

    public static boolean[] byteToBinary(byte b0, int i, boolean[] aboolean, int j, int k) {
        if (0 == k) {
            return aboolean;
        } else if (k - 1 + i >= 8) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 8");
        } else {
            boolean flag = false;

            for (int l = 0; l < k; ++l) {
                int i1 = l * 1 + i;

                aboolean[j + l] = (1 & b0 >> i1) != 0;
            }

            return aboolean;
        }
    }

    public static byte[] uuidToByteArray(UUID uuid, byte[] abyte, int i, int j) {
        if (0 == j) {
            return abyte;
        } else if (j > 16) {
            throw new IllegalArgumentException("nBytes is greather than 16");
        } else {
            longToByteArray(uuid.getMostSignificantBits(), 0, abyte, i, j > 8 ? 8 : j);
            if (j >= 8) {
                longToByteArray(uuid.getLeastSignificantBits(), 0, abyte, i + 8, j - 8);
            }

            return abyte;
        }
    }

    public static UUID byteArrayToUuid(byte[] abyte, int i) {
        if (abyte.length - i < 16) {
            throw new IllegalArgumentException("Need at least 16 bytes for UUID");
        } else {
            return new UUID(byteArrayToLong(abyte, i, 0L, 0, 8), byteArrayToLong(abyte, i + 8, 0L, 0, 8));
        }
    }
}
