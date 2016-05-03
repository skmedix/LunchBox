package org.apache.commons.codec.digest;

import org.apache.commons.codec.Charsets;

public class Crypt {

    public static String crypt(byte[] abyte) {
        return crypt(abyte, (String) null);
    }

    public static String crypt(byte[] abyte, String s) {
        return s == null ? Sha2Crypt.sha512Crypt(abyte) : (s.startsWith("$6$") ? Sha2Crypt.sha512Crypt(abyte, s) : (s.startsWith("$5$") ? Sha2Crypt.sha256Crypt(abyte, s) : (s.startsWith("$1$") ? Md5Crypt.md5Crypt(abyte, s) : UnixCrypt.crypt(abyte, s))));
    }

    public static String crypt(String s) {
        return crypt(s, (String) null);
    }

    public static String crypt(String s, String s1) {
        return crypt(s.getBytes(Charsets.UTF_8), s1);
    }
}
