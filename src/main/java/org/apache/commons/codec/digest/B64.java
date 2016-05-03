package org.apache.commons.codec.digest;

import java.util.Random;

class B64 {

    static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    static void b64from24bit(byte b0, byte b1, byte b2, int i, StringBuilder stringbuilder) {
        int j = b0 << 16 & 16777215 | b1 << 8 & '\uffff' | b2 & 255;

        for (int k = i; k-- > 0; j >>= 6) {
            stringbuilder.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(j & 63));
        }

    }

    static String getRandomSalt(int i) {
        StringBuilder stringbuilder = new StringBuilder();

        for (int j = 1; j <= i; ++j) {
            stringbuilder.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt((new Random()).nextInt("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
        }

        return stringbuilder.toString();
    }
}
