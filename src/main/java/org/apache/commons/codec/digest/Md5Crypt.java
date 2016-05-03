package org.apache.commons.codec.digest;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.Charsets;

public class Md5Crypt {

    static final String APR1_PREFIX = "$apr1$";
    private static final int BLOCKSIZE = 16;
    static final String MD5_PREFIX = "$1$";
    private static final int ROUNDS = 1000;

    public static String apr1Crypt(byte[] abyte) {
        return apr1Crypt(abyte, "$apr1$" + B64.getRandomSalt(8));
    }

    public static String apr1Crypt(byte[] abyte, String s) {
        if (s != null && !s.startsWith("$apr1$")) {
            s = "$apr1$" + s;
        }

        return md5Crypt(abyte, s, "$apr1$");
    }

    public static String apr1Crypt(String s) {
        return apr1Crypt(s.getBytes(Charsets.UTF_8));
    }

    public static String apr1Crypt(String s, String s1) {
        return apr1Crypt(s.getBytes(Charsets.UTF_8), s1);
    }

    public static String md5Crypt(byte[] abyte) {
        return md5Crypt(abyte, "$1$" + B64.getRandomSalt(8));
    }

    public static String md5Crypt(byte[] abyte, String s) {
        return md5Crypt(abyte, s, "$1$");
    }

    public static String md5Crypt(byte[] abyte, String s, String s1) {
        int i = abyte.length;
        String s2;

        if (s == null) {
            s2 = B64.getRandomSalt(8);
        } else {
            Pattern pattern = Pattern.compile("^" + s1.replace("$", "\\$") + "([\\.\\/a-zA-Z0-9]{1,8}).*");
            Matcher matcher = pattern.matcher(s);

            if (matcher == null || !matcher.find()) {
                throw new IllegalArgumentException("Invalid salt value: " + s);
            }

            s2 = matcher.group(1);
        }

        byte[] abyte1 = s2.getBytes(Charsets.UTF_8);
        MessageDigest messagedigest = DigestUtils.getMd5Digest();

        messagedigest.update(abyte);
        messagedigest.update(s1.getBytes(Charsets.UTF_8));
        messagedigest.update(abyte1);
        MessageDigest messagedigest1 = DigestUtils.getMd5Digest();

        messagedigest1.update(abyte);
        messagedigest1.update(abyte1);
        messagedigest1.update(abyte);
        byte[] abyte2 = messagedigest1.digest();

        int j;

        for (j = i; j > 0; j -= 16) {
            messagedigest.update(abyte2, 0, j > 16 ? 16 : j);
        }

        Arrays.fill(abyte2, (byte) 0);
        j = i;

        for (boolean flag = false; j > 0; j >>= 1) {
            if ((j & 1) == 1) {
                messagedigest.update(abyte2[0]);
            } else {
                messagedigest.update(abyte[0]);
            }
        }

        StringBuilder stringbuilder = new StringBuilder(s1 + s2 + "$");

        abyte2 = messagedigest.digest();

        for (int k = 0; k < 1000; ++k) {
            messagedigest1 = DigestUtils.getMd5Digest();
            if ((k & 1) != 0) {
                messagedigest1.update(abyte);
            } else {
                messagedigest1.update(abyte2, 0, 16);
            }

            if (k % 3 != 0) {
                messagedigest1.update(abyte1);
            }

            if (k % 7 != 0) {
                messagedigest1.update(abyte);
            }

            if ((k & 1) != 0) {
                messagedigest1.update(abyte2, 0, 16);
            } else {
                messagedigest1.update(abyte);
            }

            abyte2 = messagedigest1.digest();
        }

        B64.b64from24bit(abyte2[0], abyte2[6], abyte2[12], 4, stringbuilder);
        B64.b64from24bit(abyte2[1], abyte2[7], abyte2[13], 4, stringbuilder);
        B64.b64from24bit(abyte2[2], abyte2[8], abyte2[14], 4, stringbuilder);
        B64.b64from24bit(abyte2[3], abyte2[9], abyte2[15], 4, stringbuilder);
        B64.b64from24bit(abyte2[4], abyte2[10], abyte2[5], 4, stringbuilder);
        B64.b64from24bit((byte) 0, (byte) 0, abyte2[11], 2, stringbuilder);
        messagedigest.reset();
        messagedigest1.reset();
        Arrays.fill(abyte, (byte) 0);
        Arrays.fill(abyte1, (byte) 0);
        Arrays.fill(abyte2, (byte) 0);
        return stringbuilder.toString();
    }
}
