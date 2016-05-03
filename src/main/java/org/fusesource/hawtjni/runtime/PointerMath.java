package org.fusesource.hawtjni.runtime;

public class PointerMath {

    private static final boolean bits32 = Library.getBitModel() == 32;

    public static final long add(long ptr, long n) {
        return PointerMath.bits32 ? (long) ((int) (ptr + n)) : ptr + n;
    }
}
