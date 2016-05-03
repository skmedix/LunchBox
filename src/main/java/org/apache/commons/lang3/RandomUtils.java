package org.apache.commons.lang3;

import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static byte[] nextBytes(int i) {
        Validate.isTrue(i >= 0, "Count cannot be negative.", new Object[0]);
        byte[] abyte = new byte[i];

        RandomUtils.RANDOM.nextBytes(abyte);
        return abyte;
    }

    public static int nextInt(int i, int j) {
        Validate.isTrue(j >= i, "Start value must be smaller or equal to end value.", new Object[0]);
        Validate.isTrue(i >= 0, "Both range values must be non-negative.", new Object[0]);
        return i == j ? i : i + RandomUtils.RANDOM.nextInt(j - i);
    }

    public static long nextLong(long i, long j) {
        Validate.isTrue(j >= i, "Start value must be smaller or equal to end value.", new Object[0]);
        Validate.isTrue(i >= 0L, "Both range values must be non-negative.", new Object[0]);
        return i == j ? i : (long) nextDouble((double) i, (double) j);
    }

    public static double nextDouble(double d0, double d1) {
        Validate.isTrue(d1 >= d0, "Start value must be smaller or equal to end value.", new Object[0]);
        Validate.isTrue(d0 >= 0.0D, "Both range values must be non-negative.", new Object[0]);
        return d0 == d1 ? d0 : d0 + (d1 - d0) * RandomUtils.RANDOM.nextDouble();
    }

    public static float nextFloat(float f, float f1) {
        Validate.isTrue(f1 >= f, "Start value must be smaller or equal to end value.", new Object[0]);
        Validate.isTrue(f >= 0.0F, "Both range values must be non-negative.", new Object[0]);
        return f == f1 ? f : f + (f1 - f) * RandomUtils.RANDOM.nextFloat();
    }
}
