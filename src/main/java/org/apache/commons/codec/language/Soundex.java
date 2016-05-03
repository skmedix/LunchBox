package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Soundex implements StringEncoder {

    public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
    private static final char[] US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();
    public static final Soundex US_ENGLISH = new Soundex();
    /** @deprecated */
    @Deprecated
    private int maxLength = 4;
    private final char[] soundexMapping;

    public Soundex() {
        this.soundexMapping = Soundex.US_ENGLISH_MAPPING;
    }

    public Soundex(char[] achar) {
        this.soundexMapping = new char[achar.length];
        System.arraycopy(achar, 0, this.soundexMapping, 0, achar.length);
    }

    public Soundex(String s) {
        this.soundexMapping = s.toCharArray();
    }

    public int difference(String s, String s1) throws EncoderException {
        return SoundexUtils.difference(this, s, s1);
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
        } else {
            return this.soundex((String) object);
        }
    }

    public String encode(String s) {
        return this.soundex(s);
    }

    private char getMappingCode(String s, int i) {
        char c0 = this.map(s.charAt(i));

        if (i > 1 && c0 != 48) {
            char c1 = s.charAt(i - 1);

            if (72 == c1 || 87 == c1) {
                char c2 = s.charAt(i - 2);
                char c3 = this.map(c2);

                if (c3 == c0 || 72 == c2 || 87 == c2) {
                    return '\u0000';
                }
            }
        }

        return c0;
    }

    /** @deprecated */
    @Deprecated
    public int getMaxLength() {
        return this.maxLength;
    }

    private char[] getSoundexMapping() {
        return this.soundexMapping;
    }

    private char map(char c0) {
        int i = c0 - 65;

        if (i >= 0 && i < this.getSoundexMapping().length) {
            return this.getSoundexMapping()[i];
        } else {
            throw new IllegalArgumentException("The character is not mapped: " + c0);
        }
    }

    /** @deprecated */
    @Deprecated
    public void setMaxLength(int i) {
        this.maxLength = i;
    }

    public String soundex(String s) {
        if (s == null) {
            return null;
        } else {
            s = SoundexUtils.clean(s);
            if (s.length() == 0) {
                return s;
            } else {
                char[] achar = new char[] { '0', '0', '0', '0'};
                int i = 1;
                int j = 1;

                achar[0] = s.charAt(0);
                char c0 = this.getMappingCode(s, 0);

                while (i < s.length() && j < achar.length) {
                    char c1 = this.getMappingCode(s, i++);

                    if (c1 != 0) {
                        if (c1 != 48 && c1 != c0) {
                            achar[j++] = c1;
                        }

                        c0 = c1;
                    }
                }

                return new String(achar);
            }
        }
    }
}
