package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class RefinedSoundex implements StringEncoder {

    public static final String US_ENGLISH_MAPPING_STRING = "01360240043788015936020505";
    private static final char[] US_ENGLISH_MAPPING = "01360240043788015936020505".toCharArray();
    private final char[] soundexMapping;
    public static final RefinedSoundex US_ENGLISH = new RefinedSoundex();

    public RefinedSoundex() {
        this.soundexMapping = RefinedSoundex.US_ENGLISH_MAPPING;
    }

    public RefinedSoundex(char[] achar) {
        this.soundexMapping = new char[achar.length];
        System.arraycopy(achar, 0, this.soundexMapping, 0, achar.length);
    }

    public RefinedSoundex(String s) {
        this.soundexMapping = s.toCharArray();
    }

    public int difference(String s, String s1) throws EncoderException {
        return SoundexUtils.difference(this, s, s1);
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("Parameter supplied to RefinedSoundex encode is not of type java.lang.String");
        } else {
            return this.soundex((String) object);
        }
    }

    public String encode(String s) {
        return this.soundex(s);
    }

    char getMappingCode(char c0) {
        return !Character.isLetter(c0) ? '\u0000' : this.soundexMapping[Character.toUpperCase(c0) - 65];
    }

    public String soundex(String s) {
        if (s == null) {
            return null;
        } else {
            s = SoundexUtils.clean(s);
            if (s.length() == 0) {
                return s;
            } else {
                StringBuilder stringbuilder = new StringBuilder();

                stringbuilder.append(s.charAt(0));
                char c0 = 42;

                for (int i = 0; i < s.length(); ++i) {
                    char c1 = this.getMappingCode(s.charAt(i));

                    if (c1 != c0) {
                        if (c1 != 0) {
                            stringbuilder.append(c1);
                        }

                        c0 = c1;
                    }
                }

                return stringbuilder.toString();
            }
        }
    }
}
