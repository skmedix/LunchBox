package org.apache.commons.io;

import java.io.IOException;
import java.io.OutputStream;

public class HexDump {

    public static final String EOL = System.getProperty("line.separator");
    private static final char[] _hexcodes = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int[] _shifts = new int[] { 28, 24, 20, 16, 12, 8, 4, 0};

    public static void dump(byte[] abyte, long i, OutputStream outputstream, int j) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (j >= 0 && j < abyte.length) {
            if (outputstream == null) {
                throw new IllegalArgumentException("cannot write to nullstream");
            } else {
                long k = i + (long) j;
                StringBuilder stringbuilder = new StringBuilder(74);

                for (int l = j; l < abyte.length; l += 16) {
                    int i1 = abyte.length - l;

                    if (i1 > 16) {
                        i1 = 16;
                    }

                    dump(stringbuilder, k).append(' ');

                    int j1;

                    for (j1 = 0; j1 < 16; ++j1) {
                        if (j1 < i1) {
                            dump(stringbuilder, abyte[j1 + l]);
                        } else {
                            stringbuilder.append("  ");
                        }

                        stringbuilder.append(' ');
                    }

                    for (j1 = 0; j1 < i1; ++j1) {
                        if (abyte[j1 + l] >= 32 && abyte[j1 + l] < 127) {
                            stringbuilder.append((char) abyte[j1 + l]);
                        } else {
                            stringbuilder.append('.');
                        }
                    }

                    stringbuilder.append(HexDump.EOL);
                    outputstream.write(stringbuilder.toString().getBytes());
                    outputstream.flush();
                    stringbuilder.setLength(0);
                    k += (long) i1;
                }

            }
        } else {
            throw new ArrayIndexOutOfBoundsException("illegal index: " + j + " into array of length " + abyte.length);
        }
    }

    private static StringBuilder dump(StringBuilder stringbuilder, long i) {
        for (int j = 0; j < 8; ++j) {
            stringbuilder.append(HexDump._hexcodes[(int) (i >> HexDump._shifts[j]) & 15]);
        }

        return stringbuilder;
    }

    private static StringBuilder dump(StringBuilder stringbuilder, byte b0) {
        for (int i = 0; i < 2; ++i) {
            stringbuilder.append(HexDump._hexcodes[b0 >> HexDump._shifts[i + 6] & 15]);
        }

        return stringbuilder;
    }
}
