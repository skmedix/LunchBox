package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class MatchRatingApproachEncoder implements StringEncoder {

    private static final String SPACE = " ";
    private static final String EMPTY = "";
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int ELEVEN = 11;
    private static final int TWELVE = 12;
    private static final String PLAIN_ASCII = "AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu";
    private static final String UNICODE = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű";
    private static final String[] DOUBLE_CONSONANT = new String[] { "BB", "CC", "DD", "FF", "GG", "HH", "JJ", "KK", "LL", "MM", "NN", "PP", "QQ", "RR", "SS", "TT", "VV", "WW", "XX", "YY", "ZZ"};

    String cleanName(String s) {
        String s1 = s.toUpperCase(Locale.ENGLISH);
        String[] astring = new String[] { "\\-", "[&]", "\\\'", "\\.", "[\\,]"};
        String[] astring1 = astring;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s2 = astring1[j];

            s1 = s1.replaceAll(s2, "");
        }

        s1 = this.removeAccents(s1);
        s1 = s1.replaceAll("\\s+", "");
        return s1;
    }

    public final Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("Parameter supplied to Match Rating Approach encoder is not of type java.lang.String");
        } else {
            return this.encode((String) object);
        }
    }

    public final String encode(String s) {
        if (s != null && !"".equalsIgnoreCase(s) && !" ".equalsIgnoreCase(s) && s.length() != 1) {
            s = this.cleanName(s);
            s = this.removeVowels(s);
            s = this.removeDoubleConsonants(s);
            s = this.getFirst3Last3(s);
            return s;
        } else {
            return "";
        }
    }

    String getFirst3Last3(String s) {
        int i = s.length();

        if (i > 6) {
            String s1 = s.substring(0, 3);
            String s2 = s.substring(i - 3, i);

            return s1 + s2;
        } else {
            return s;
        }
    }

    int getMinRating(int i) {
        boolean flag = false;
        byte b0;

        if (i <= 4) {
            b0 = 5;
        } else if (i >= 5 && i <= 7) {
            b0 = 4;
        } else if (i >= 8 && i <= 11) {
            b0 = 3;
        } else if (i == 12) {
            b0 = 2;
        } else {
            b0 = 1;
        }

        return b0;
    }

    public boolean isEncodeEquals(String s, String s1) {
        if (s != null && !"".equalsIgnoreCase(s) && !" ".equalsIgnoreCase(s)) {
            if (s1 != null && !"".equalsIgnoreCase(s1) && !" ".equalsIgnoreCase(s1)) {
                if (s.length() != 1 && s1.length() != 1) {
                    if (s.equalsIgnoreCase(s1)) {
                        return true;
                    } else {
                        s = this.cleanName(s);
                        s1 = this.cleanName(s1);
                        s = this.removeVowels(s);
                        s1 = this.removeVowels(s1);
                        s = this.removeDoubleConsonants(s);
                        s1 = this.removeDoubleConsonants(s1);
                        s = this.getFirst3Last3(s);
                        s1 = this.getFirst3Last3(s1);
                        if (Math.abs(s.length() - s1.length()) >= 3) {
                            return false;
                        } else {
                            int i = Math.abs(s.length() + s1.length());
                            boolean flag = false;
                            int j = this.getMinRating(i);
                            int k = this.leftToRightThenRightToLeftProcessing(s, s1);

                            return k >= j;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    boolean isVowel(String s) {
        return s.equalsIgnoreCase("E") || s.equalsIgnoreCase("A") || s.equalsIgnoreCase("O") || s.equalsIgnoreCase("I") || s.equalsIgnoreCase("U");
    }

    int leftToRightThenRightToLeftProcessing(String s, String s1) {
        char[] achar = s.toCharArray();
        char[] achar1 = s1.toCharArray();
        int i = s.length() - 1;
        int j = s1.length() - 1;
        String s2 = "";
        String s3 = "";
        String s4 = "";
        String s5 = "";

        for (int k = 0; k < achar.length && k <= j; ++k) {
            s2 = s.substring(k, k + 1);
            s3 = s.substring(i - k, i - k + 1);
            s4 = s1.substring(k, k + 1);
            s5 = s1.substring(j - k, j - k + 1);
            if (s2.equals(s4)) {
                achar[k] = 32;
                achar1[k] = 32;
            }

            if (s3.equals(s5)) {
                achar[i - k] = 32;
                achar1[j - k] = 32;
            }
        }

        String s6 = (new String(achar)).replaceAll("\\s+", "");
        String s7 = (new String(achar1)).replaceAll("\\s+", "");

        return s6.length() > s7.length() ? Math.abs(6 - s6.length()) : Math.abs(6 - s7.length());
    }

    String removeAccents(String s) {
        if (s == null) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder();
            int i = s.length();

            for (int j = 0; j < i; ++j) {
                char c0 = s.charAt(j);
                int k = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű".indexOf(c0);

                if (k > -1) {
                    stringbuilder.append("AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu".charAt(k));
                } else {
                    stringbuilder.append(c0);
                }
            }

            return stringbuilder.toString();
        }
    }

    String removeDoubleConsonants(String s) {
        String s1 = s.toUpperCase();
        String[] astring = MatchRatingApproachEncoder.DOUBLE_CONSONANT;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s2 = astring[j];

            if (s1.contains(s2)) {
                String s3 = s2.substring(0, 1);

                s1 = s1.replace(s2, s3);
            }
        }

        return s1;
    }

    String removeVowels(String s) {
        String s1 = s.substring(0, 1);

        s = s.replaceAll("A", "");
        s = s.replaceAll("E", "");
        s = s.replaceAll("I", "");
        s = s.replaceAll("O", "");
        s = s.replaceAll("U", "");
        s = s.replaceAll("\\s{2,}\\b", " ");
        return this.isVowel(s1) ? s1 + s : s;
    }
}
