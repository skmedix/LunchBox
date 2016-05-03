package org.apache.commons.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EndianUtils {

    public static short swapShort(short short0) {
        return (short) (((short0 >> 0 & 255) << 8) + ((short0 >> 8 & 255) << 0));
    }

    public static int swapInteger(int i) {
        return ((i >> 0 & 255) << 24) + ((i >> 8 & 255) << 16) + ((i >> 16 & 255) << 8) + ((i >> 24 & 255) << 0);
    }

    public static long swapLong(long i) {
        return ((i >> 0 & 255L) << 56) + ((i >> 8 & 255L) << 48) + ((i >> 16 & 255L) << 40) + ((i >> 24 & 255L) << 32) + ((i >> 32 & 255L) << 24) + ((i >> 40 & 255L) << 16) + ((i >> 48 & 255L) << 8) + ((i >> 56 & 255L) << 0);
    }

    public static float swapFloat(float f) {
        return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(f)));
    }

    public static double swapDouble(double d0) {
        return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(d0)));
    }

    public static void writeSwappedShort(byte[] abyte, int i, short short0) {
        abyte[i + 0] = (byte) (short0 >> 0 & 255);
        abyte[i + 1] = (byte) (short0 >> 8 & 255);
    }

    public static short readSwappedShort(byte[] abyte, int i) {
        return (short) (((abyte[i + 0] & 255) << 0) + ((abyte[i + 1] & 255) << 8));
    }

    public static int readSwappedUnsignedShort(byte[] abyte, int i) {
        return ((abyte[i + 0] & 255) << 0) + ((abyte[i + 1] & 255) << 8);
    }

    public static void writeSwappedInteger(byte[] abyte, int i, int j) {
        abyte[i + 0] = (byte) (j >> 0 & 255);
        abyte[i + 1] = (byte) (j >> 8 & 255);
        abyte[i + 2] = (byte) (j >> 16 & 255);
        abyte[i + 3] = (byte) (j >> 24 & 255);
    }

    public static int readSwappedInteger(byte[] abyte, int i) {
        return ((abyte[i + 0] & 255) << 0) + ((abyte[i + 1] & 255) << 8) + ((abyte[i + 2] & 255) << 16) + ((abyte[i + 3] & 255) << 24);
    }

    public static long readSwappedUnsignedInteger(byte[] abyte, int i) {
        long j = (long) (((abyte[i + 0] & 255) << 0) + ((abyte[i + 1] & 255) << 8) + ((abyte[i + 2] & 255) << 16));
        long k = (long) (abyte[i + 3] & 255);

        return (k << 24) + (4294967295L & j);
    }

    public static void writeSwappedLong(byte[] abyte, int i, long j) {
        abyte[i + 0] = (byte) ((int) (j >> 0 & 255L));
        abyte[i + 1] = (byte) ((int) (j >> 8 & 255L));
        abyte[i + 2] = (byte) ((int) (j >> 16 & 255L));
        abyte[i + 3] = (byte) ((int) (j >> 24 & 255L));
        abyte[i + 4] = (byte) ((int) (j >> 32 & 255L));
        abyte[i + 5] = (byte) ((int) (j >> 40 & 255L));
        abyte[i + 6] = (byte) ((int) (j >> 48 & 255L));
        abyte[i + 7] = (byte) ((int) (j >> 56 & 255L));
    }

    public static long readSwappedLong(byte[] abyte, int i) {
        long j = (long) (((abyte[i + 0] & 255) << 0) + ((abyte[i + 1] & 255) << 8) + ((abyte[i + 2] & 255) << 16) + ((abyte[i + 3] & 255) << 24));
        long k = (long) (((abyte[i + 4] & 255) << 0) + ((abyte[i + 5] & 255) << 8) + ((abyte[i + 6] & 255) << 16) + ((abyte[i + 7] & 255) << 24));

        return (k << 32) + (4294967295L & j);
    }

    public static void writeSwappedFloat(byte[] abyte, int i, float f) {
        writeSwappedInteger(abyte, i, Float.floatToIntBits(f));
    }

    public static float readSwappedFloat(byte[] abyte, int i) {
        return Float.intBitsToFloat(readSwappedInteger(abyte, i));
    }

    public static void writeSwappedDouble(byte[] abyte, int i, double d0) {
        writeSwappedLong(abyte, i, Double.doubleToLongBits(d0));
    }

    public static double readSwappedDouble(byte[] abyte, int i) {
        return Double.longBitsToDouble(readSwappedLong(abyte, i));
    }

    public static void writeSwappedShort(OutputStream outputstream, short short0) throws IOException {
        outputstream.write((byte) (short0 >> 0 & 255));
        outputstream.write((byte) (short0 >> 8 & 255));
    }

    public static short readSwappedShort(InputStream inputstream) throws IOException {
        return (short) (((read(inputstream) & 255) << 0) + ((read(inputstream) & 255) << 8));
    }

    public static int readSwappedUnsignedShort(InputStream inputstream) throws IOException {
        int i = read(inputstream);
        int j = read(inputstream);

        return ((i & 255) << 0) + ((j & 255) << 8);
    }

    public static void writeSwappedInteger(OutputStream outputstream, int i) throws IOException {
        outputstream.write((byte) (i >> 0 & 255));
        outputstream.write((byte) (i >> 8 & 255));
        outputstream.write((byte) (i >> 16 & 255));
        outputstream.write((byte) (i >> 24 & 255));
    }

    public static int readSwappedInteger(InputStream inputstream) throws IOException {
        int i = read(inputstream);
        int j = read(inputstream);
        int k = read(inputstream);
        int l = read(inputstream);

        return ((i & 255) << 0) + ((j & 255) << 8) + ((k & 255) << 16) + ((l & 255) << 24);
    }

    public static long readSwappedUnsignedInteger(InputStream inputstream) throws IOException {
        int i = read(inputstream);
        int j = read(inputstream);
        int k = read(inputstream);
        int l = read(inputstream);
        long i1 = (long) (((i & 255) << 0) + ((j & 255) << 8) + ((k & 255) << 16));
        long j1 = (long) (l & 255);

        return (j1 << 24) + (4294967295L & i1);
    }

    public static void writeSwappedLong(OutputStream outputstream, long i) throws IOException {
        outputstream.write((byte) ((int) (i >> 0 & 255L)));
        outputstream.write((byte) ((int) (i >> 8 & 255L)));
        outputstream.write((byte) ((int) (i >> 16 & 255L)));
        outputstream.write((byte) ((int) (i >> 24 & 255L)));
        outputstream.write((byte) ((int) (i >> 32 & 255L)));
        outputstream.write((byte) ((int) (i >> 40 & 255L)));
        outputstream.write((byte) ((int) (i >> 48 & 255L)));
        outputstream.write((byte) ((int) (i >> 56 & 255L)));
    }

    public static long readSwappedLong(InputStream inputstream) throws IOException {
        byte[] abyte = new byte[8];

        for (int i = 0; i < 8; ++i) {
            abyte[i] = (byte) read(inputstream);
        }

        return readSwappedLong(abyte, 0);
    }

    public static void writeSwappedFloat(OutputStream outputstream, float f) throws IOException {
        writeSwappedInteger(outputstream, Float.floatToIntBits(f));
    }

    public static float readSwappedFloat(InputStream inputstream) throws IOException {
        return Float.intBitsToFloat(readSwappedInteger(inputstream));
    }

    public static void writeSwappedDouble(OutputStream outputstream, double d0) throws IOException {
        writeSwappedLong(outputstream, Double.doubleToLongBits(d0));
    }

    public static double readSwappedDouble(InputStream inputstream) throws IOException {
        return Double.longBitsToDouble(readSwappedLong(inputstream));
    }

    private static int read(InputStream inputstream) throws IOException {
        int i = inputstream.read();

        if (-1 == i) {
            throw new EOFException("Unexpected EOF reached");
        } else {
            return i;
        }
    }
}
