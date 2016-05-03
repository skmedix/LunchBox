package org.apache.commons.codec.language;

import java.util.regex.Pattern;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Nysiis implements StringEncoder {

    private static final char[] CHARS_A = new char[] { 'A'};
    private static final char[] CHARS_AF = new char[] { 'A', 'F'};
    private static final char[] CHARS_C = new char[] { 'C'};
    private static final char[] CHARS_FF = new char[] { 'F', 'F'};
    private static final char[] CHARS_G = new char[] { 'G'};
    private static final char[] CHARS_N = new char[] { 'N'};
    private static final char[] CHARS_NN = new char[] { 'N', 'N'};
    private static final char[] CHARS_S = new char[] { 'S'};
    private static final char[] CHARS_SSS = new char[] { 'S', 'S', 'S'};
    private static final Pattern PAT_MAC = Pattern.compile("^MAC");
    private static final Pattern PAT_KN = Pattern.compile("^KN");
    private static final Pattern PAT_K = Pattern.compile("^K");
    private static final Pattern PAT_PH_PF = Pattern.compile("^(PH|PF)");
    private static final Pattern PAT_SCH = Pattern.compile("^SCH");
    private static final Pattern PAT_EE_IE = Pattern.compile("(EE|IE)$");
    private static final Pattern PAT_DT_ETC = Pattern.compile("(DT|RT|RD|NT|ND)$");
    private static final char SPACE = ' ';
    private static final int TRUE_LENGTH = 6;
    private final boolean strict;

    private static boolean isVowel(char c0) {
        return c0 == 65 || c0 == 69 || c0 == 73 || c0 == 79 || c0 == 85;
    }

    private static char[] transcodeRemaining(char c0, char c1, char c2, char c3) {
        return c1 == 69 && c2 == 86 ? Nysiis.CHARS_AF : (isVowel(c1) ? Nysiis.CHARS_A : (c1 == 81 ? Nysiis.CHARS_G : (c1 == 90 ? Nysiis.CHARS_S : (c1 == 77 ? Nysiis.CHARS_N : (c1 == 75 ? (c2 == 78 ? Nysiis.CHARS_NN : Nysiis.CHARS_C) : (c1 == 83 && c2 == 67 && c3 == 72 ? Nysiis.CHARS_SSS : (c1 == 80 && c2 == 72 ? Nysiis.CHARS_FF : (c1 == 72 && (!isVowel(c0) || !isVowel(c2)) ? new char[] { c0} : (c1 == 87 && isVowel(c0) ? new char[] { c0} : new char[] { c1})))))))));
    }

    public Nysiis() {
        this(true);
    }

    public Nysiis(boolean flag) {
        this.strict = flag;
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("Parameter supplied to Nysiis encode is not of type java.lang.String");
        } else {
            return this.nysiis((String) object);
        }
    }

    public String encode(String s) {
        return this.nysiis(s);
    }

    public boolean isStrict() {
        return this.strict;
    }

    public String nysiis(String s) {
        if (s == null) {
            return null;
        } else {
            s = SoundexUtils.clean(s);
            if (s.length() == 0) {
                return s;
            } else {
                s = Nysiis.PAT_MAC.matcher(s).replaceFirst("MCC");
                s = Nysiis.PAT_KN.matcher(s).replaceFirst("NN");
                s = Nysiis.PAT_K.matcher(s).replaceFirst("C");
                s = Nysiis.PAT_PH_PF.matcher(s).replaceFirst("FF");
                s = Nysiis.PAT_SCH.matcher(s).replaceFirst("SSS");
                s = Nysiis.PAT_EE_IE.matcher(s).replaceFirst("Y");
                s = Nysiis.PAT_DT_ETC.matcher(s).replaceFirst("D");
                StringBuilder stringbuilder = new StringBuilder(s.length());

                stringbuilder.append(s.charAt(0));
                char[] achar = s.toCharArray();
                int i = achar.length;

                char c0;

                for (int j = 1; j < i; ++j) {
                    c0 = j < i - 1 ? achar[j + 1] : 32;
                    char c1 = j < i - 2 ? achar[j + 2] : 32;
                    char[] achar1 = transcodeRemaining(achar[j - 1], achar[j], c0, c1);

                    System.arraycopy(achar1, 0, achar, j, achar1.length);
                    if (achar[j] != achar[j - 1]) {
                        stringbuilder.append(achar[j]);
                    }
                }

                if (stringbuilder.length() > 1) {
                    char c2 = stringbuilder.charAt(stringbuilder.length() - 1);

                    if (c2 == 83) {
                        stringbuilder.deleteCharAt(stringbuilder.length() - 1);
                        c2 = stringbuilder.charAt(stringbuilder.length() - 1);
                    }

                    if (stringbuilder.length() > 2) {
                        c0 = stringbuilder.charAt(stringbuilder.length() - 2);
                        if (c0 == 65 && c2 == 89) {
                            stringbuilder.deleteCharAt(stringbuilder.length() - 2);
                        }
                    }

                    if (c2 == 65) {
                        stringbuilder.deleteCharAt(stringbuilder.length() - 1);
                    }
                }

                String s1 = stringbuilder.toString();

                return this.isStrict() ? s1.substring(0, Math.min(6, s1.length())) : s1;
            }
        }
    }
}
