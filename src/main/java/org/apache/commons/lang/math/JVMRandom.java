package org.apache.commons.lang.math;

import java.util.Random;

public final class JVMRandom extends Random {

    private static final long serialVersionUID = 1L;
    private static final Random SHARED_RANDOM = new Random();
    private boolean constructed = false;

    public JVMRandom() {
        this.constructed = true;
    }

    public synchronized void setSeed(long seed) {
        if (this.constructed) {
            throw new UnsupportedOperationException();
        }
    }

    public synchronized double nextGaussian() {
        throw new UnsupportedOperationException();
    }

    public void nextBytes(byte[] byteArray) {
        throw new UnsupportedOperationException();
    }

    public int nextInt() {
        return this.nextInt(Integer.MAX_VALUE);
    }

    public int nextInt(int n) {
        return JVMRandom.SHARED_RANDOM.nextInt(n);
    }

    public long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    public static long nextLong(long n) {
        if (n <= 0L) {
            throw new IllegalArgumentException("Upper bound for nextInt must be positive");
        } else if ((n & -n) == n) {
            return next63bits() >> 63 - bitsRequired(n - 1L);
        } else {
            long bits;
            long val;

            do {
                bits = next63bits();
                val = bits % n;
            } while (bits - val + (n - 1L) < 0L);

            return val;
        }
    }

    public boolean nextBoolean() {
        return JVMRandom.SHARED_RANDOM.nextBoolean();
    }

    public float nextFloat() {
        return JVMRandom.SHARED_RANDOM.nextFloat();
    }

    public double nextDouble() {
        return JVMRandom.SHARED_RANDOM.nextDouble();
    }

    private static long next63bits() {
        return JVMRandom.SHARED_RANDOM.nextLong() & Long.MAX_VALUE;
    }

    private static int bitsRequired(long num) {
        long y = num;

        int n;

        for (n = 0; num >= 0L; y >>= 1) {
            if (y == 0L) {
                return n;
            }

            ++n;
            num <<= 1;
        }

        return 64 - n;
    }
}
