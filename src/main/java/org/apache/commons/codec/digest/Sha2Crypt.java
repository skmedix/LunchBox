package org.apache.commons.codec.digest;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.Charsets;

public class Sha2Crypt {

    private static final int ROUNDS_DEFAULT = 5000;
    private static final int ROUNDS_MAX = 999999999;
    private static final int ROUNDS_MIN = 1000;
    private static final String ROUNDS_PREFIX = "rounds=";
    private static final int SHA256_BLOCKSIZE = 32;
    static final String SHA256_PREFIX = "$5$";
    private static final int SHA512_BLOCKSIZE = 64;
    static final String SHA512_PREFIX = "$6$";
    private static final Pattern SALT_PATTERN = Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");

    public static String sha256Crypt(byte[] abyte) {
        return sha256Crypt(abyte, (String) null);
    }

    public static String sha256Crypt(byte[] abyte, String s) {
        if (s == null) {
            s = "$5$" + B64.getRandomSalt(8);
        }

        return sha2Crypt(abyte, s, "$5$", 32, "SHA-256");
    }

    private static String sha2Crypt(byte[] abyte, String s, String s1, int i, String s2) {
        int j = abyte.length;
        int k = 5000;
        boolean flag = false;

        if (s == null) {
            throw new IllegalArgumentException("Salt must not be null");
        } else {
            Matcher matcher = Sha2Crypt.SALT_PATTERN.matcher(s);

            if (matcher != null && matcher.find()) {
                if (matcher.group(3) != null) {
                    k = Integer.parseInt(matcher.group(3));
                    k = Math.max(1000, Math.min(999999999, k));
                    flag = true;
                }

                String s3 = matcher.group(4);
                byte[] abyte1 = s3.getBytes(Charsets.UTF_8);
                int l = abyte1.length;
                MessageDigest messagedigest = DigestUtils.getDigest(s2);

                messagedigest.update(abyte);
                messagedigest.update(abyte1);
                MessageDigest messagedigest1 = DigestUtils.getDigest(s2);

                messagedigest1.update(abyte);
                messagedigest1.update(abyte1);
                messagedigest1.update(abyte);
                byte[] abyte2 = messagedigest1.digest();

                int i1;

                for (i1 = abyte.length; i1 > i; i1 -= i) {
                    messagedigest.update(abyte2, 0, i);
                }

                messagedigest.update(abyte2, 0, i1);

                for (i1 = abyte.length; i1 > 0; i1 >>= 1) {
                    if ((i1 & 1) != 0) {
                        messagedigest.update(abyte2, 0, i);
                    } else {
                        messagedigest.update(abyte);
                    }
                }

                abyte2 = messagedigest.digest();
                messagedigest1 = DigestUtils.getDigest(s2);

                for (int j1 = 1; j1 <= j; ++j1) {
                    messagedigest1.update(abyte);
                }

                byte[] abyte3 = messagedigest1.digest();
                byte[] abyte4 = new byte[j];

                int k1;

                for (k1 = 0; k1 < j - i; k1 += i) {
                    System.arraycopy(abyte3, 0, abyte4, k1, i);
                }

                System.arraycopy(abyte3, 0, abyte4, k1, j - k1);
                messagedigest1 = DigestUtils.getDigest(s2);

                for (int l1 = 1; l1 <= 16 + (abyte2[0] & 255); ++l1) {
                    messagedigest1.update(abyte1);
                }

                abyte3 = messagedigest1.digest();
                byte[] abyte5 = new byte[l];

                for (k1 = 0; k1 < l - i; k1 += i) {
                    System.arraycopy(abyte3, 0, abyte5, k1, i);
                }

                System.arraycopy(abyte3, 0, abyte5, k1, l - k1);

                for (int i2 = 0; i2 <= k - 1; ++i2) {
                    messagedigest = DigestUtils.getDigest(s2);
                    if ((i2 & 1) != 0) {
                        messagedigest.update(abyte4, 0, j);
                    } else {
                        messagedigest.update(abyte2, 0, i);
                    }

                    if (i2 % 3 != 0) {
                        messagedigest.update(abyte5, 0, l);
                    }

                    if (i2 % 7 != 0) {
                        messagedigest.update(abyte4, 0, j);
                    }

                    if ((i2 & 1) != 0) {
                        messagedigest.update(abyte2, 0, i);
                    } else {
                        messagedigest.update(abyte4, 0, j);
                    }

                    abyte2 = messagedigest.digest();
                }

                StringBuilder stringbuilder = new StringBuilder(s1);

                if (flag) {
                    stringbuilder.append("rounds=");
                    stringbuilder.append(k);
                    stringbuilder.append("$");
                }

                stringbuilder.append(s3);
                stringbuilder.append("$");
                if (i == 32) {
                    B64.b64from24bit(abyte2[0], abyte2[10], abyte2[20], 4, stringbuilder);
                    B64.b64from24bit(abyte2[21], abyte2[1], abyte2[11], 4, stringbuilder);
                    B64.b64from24bit(abyte2[12], abyte2[22], abyte2[2], 4, stringbuilder);
                    B64.b64from24bit(abyte2[3], abyte2[13], abyte2[23], 4, stringbuilder);
                    B64.b64from24bit(abyte2[24], abyte2[4], abyte2[14], 4, stringbuilder);
                    B64.b64from24bit(abyte2[15], abyte2[25], abyte2[5], 4, stringbuilder);
                    B64.b64from24bit(abyte2[6], abyte2[16], abyte2[26], 4, stringbuilder);
                    B64.b64from24bit(abyte2[27], abyte2[7], abyte2[17], 4, stringbuilder);
                    B64.b64from24bit(abyte2[18], abyte2[28], abyte2[8], 4, stringbuilder);
                    B64.b64from24bit(abyte2[9], abyte2[19], abyte2[29], 4, stringbuilder);
                    B64.b64from24bit((byte) 0, abyte2[31], abyte2[30], 3, stringbuilder);
                } else {
                    B64.b64from24bit(abyte2[0], abyte2[21], abyte2[42], 4, stringbuilder);
                    B64.b64from24bit(abyte2[22], abyte2[43], abyte2[1], 4, stringbuilder);
                    B64.b64from24bit(abyte2[44], abyte2[2], abyte2[23], 4, stringbuilder);
                    B64.b64from24bit(abyte2[3], abyte2[24], abyte2[45], 4, stringbuilder);
                    B64.b64from24bit(abyte2[25], abyte2[46], abyte2[4], 4, stringbuilder);
                    B64.b64from24bit(abyte2[47], abyte2[5], abyte2[26], 4, stringbuilder);
                    B64.b64from24bit(abyte2[6], abyte2[27], abyte2[48], 4, stringbuilder);
                    B64.b64from24bit(abyte2[28], abyte2[49], abyte2[7], 4, stringbuilder);
                    B64.b64from24bit(abyte2[50], abyte2[8], abyte2[29], 4, stringbuilder);
                    B64.b64from24bit(abyte2[9], abyte2[30], abyte2[51], 4, stringbuilder);
                    B64.b64from24bit(abyte2[31], abyte2[52], abyte2[10], 4, stringbuilder);
                    B64.b64from24bit(abyte2[53], abyte2[11], abyte2[32], 4, stringbuilder);
                    B64.b64from24bit(abyte2[12], abyte2[33], abyte2[54], 4, stringbuilder);
                    B64.b64from24bit(abyte2[34], abyte2[55], abyte2[13], 4, stringbuilder);
                    B64.b64from24bit(abyte2[56], abyte2[14], abyte2[35], 4, stringbuilder);
                    B64.b64from24bit(abyte2[15], abyte2[36], abyte2[57], 4, stringbuilder);
                    B64.b64from24bit(abyte2[37], abyte2[58], abyte2[16], 4, stringbuilder);
                    B64.b64from24bit(abyte2[59], abyte2[17], abyte2[38], 4, stringbuilder);
                    B64.b64from24bit(abyte2[18], abyte2[39], abyte2[60], 4, stringbuilder);
                    B64.b64from24bit(abyte2[40], abyte2[61], abyte2[19], 4, stringbuilder);
                    B64.b64from24bit(abyte2[62], abyte2[20], abyte2[41], 4, stringbuilder);
                    B64.b64from24bit((byte) 0, (byte) 0, abyte2[63], 2, stringbuilder);
                }

                Arrays.fill(abyte3, (byte) 0);
                Arrays.fill(abyte4, (byte) 0);
                Arrays.fill(abyte5, (byte) 0);
                messagedigest.reset();
                messagedigest1.reset();
                Arrays.fill(abyte, (byte) 0);
                Arrays.fill(abyte1, (byte) 0);
                return stringbuilder.toString();
            } else {
                throw new IllegalArgumentException("Invalid salt value: " + s);
            }
        }
    }

    public static String sha512Crypt(byte[] abyte) {
        return sha512Crypt(abyte, (String) null);
    }

    public static String sha512Crypt(byte[] abyte, String s) {
        if (s == null) {
            s = "$6$" + B64.getRandomSalt(8);
        }

        return sha2Crypt(abyte, s, "$6$", 64, "SHA-512");
    }
}
