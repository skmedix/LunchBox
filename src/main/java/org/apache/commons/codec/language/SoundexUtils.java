package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

final class SoundexUtils {

    static String clean(String s) {
        if (s != null && s.length() != 0) {
            int i = s.length();
            char[] achar = new char[i];
            int j = 0;

            for (int k = 0; k < i; ++k) {
                if (Character.isLetter(s.charAt(k))) {
                    achar[j++] = s.charAt(k);
                }
            }

            if (j == i) {
                return s.toUpperCase(Locale.ENGLISH);
            } else {
                return (new String(achar, 0, j)).toUpperCase(Locale.ENGLISH);
            }
        } else {
            return s;
        }
    }

    static int difference(StringEncoder stringencoder, String s, String s1) throws EncoderException {
        return differenceEncoded(stringencoder.encode(s), stringencoder.encode(s1));
    }

    static int differenceEncoded(String s, String s1) {
        if (s != null && s1 != null) {
            int i = Math.min(s.length(), s1.length());
            int j = 0;

            for (int k = 0; k < i; ++k) {
                if (s.charAt(k) == s1.charAt(k)) {
                    ++j;
                }
            }

            return j;
        } else {
            return 0;
        }
    }
}
