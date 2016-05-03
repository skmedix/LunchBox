package org.apache.logging.log4j.core.helpers;

import java.security.MessageDigest;

public final class NameUtil {

    private static final int MASK = 255;

    public static String getSubName(String s) {
        if (s.isEmpty()) {
            return null;
        } else {
            int i = s.lastIndexOf(46);

            return i > 0 ? s.substring(0, i) : "";
        }
    }

    public static String md5(String s) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");

            messagedigest.update(s.getBytes());
            byte[] abyte = messagedigest.digest();
            StringBuilder stringbuilder = new StringBuilder();
            byte[] abyte1 = abyte;
            int i = abyte.length;

            for (int j = 0; j < i; ++j) {
                byte b0 = abyte1[j];
                String s1 = Integer.toHexString(255 & b0);

                if (s1.length() == 1) {
                    stringbuilder.append('0');
                }

                stringbuilder.append(s1);
            }

            return stringbuilder.toString();
        } catch (Exception exception) {
            return s;
        }
    }
}
