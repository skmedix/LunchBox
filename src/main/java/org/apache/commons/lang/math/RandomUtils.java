package org.apache.commons.lang.math;

import java.util.Random;

public class RandomUtils {

    public static final Random JVM_RANDOM = new JVMRandom();

    public static int nextInt() {
        return nextInt(RandomUtils.JVM_RANDOM);
    }

    public static int nextInt(Random random) {
        return random.nextInt();
    }

    public static int nextInt(int n) {
        return nextInt(RandomUtils.JVM_RANDOM, n);
    }

    public static int nextInt(Random random, int n) {
        return random.nextInt(n);
    }

    public static long nextLong() {
        return nextLong(RandomUtils.JVM_RANDOM);
    }

    public static long nextLong(Random random) {
        return random.nextLong();
    }

    public static boolean nextBoolean() {
        return nextBoolean(RandomUtils.JVM_RANDOM);
    }

    public static boolean nextBoolean(Random random) {
        return random.nextBoolean();
    }

    public static float nextFloat() {
        return nextFloat(RandomUtils.JVM_RANDOM);
    }

    public static float nextFloat(Random random) {
        return random.nextFloat();
    }

    public static double nextDouble() {
        return nextDouble(RandomUtils.JVM_RANDOM);
    }

    public static double nextDouble(Random random) {
        return random.nextDouble();
    }
}
