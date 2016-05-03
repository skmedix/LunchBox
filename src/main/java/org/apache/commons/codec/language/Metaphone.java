package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Metaphone implements StringEncoder {

    private static final String VOWELS = "AEIOU";
    private static final String FRONTV = "EIY";
    private static final String VARSON = "CSPTG";
    private int maxCodeLen = 4;

    public String metaphone(String s) {
        boolean flag = false;

        if (s != null && s.length() != 0) {
            if (s.length() == 1) {
                return s.toUpperCase(Locale.ENGLISH);
            } else {
                char[] achar = s.toUpperCase(Locale.ENGLISH).toCharArray();
                StringBuilder stringbuilder = new StringBuilder(40);
                StringBuilder stringbuilder1 = new StringBuilder(10);

                switch (achar[0]) {
                case 'A':
                    if (achar[1] == 69) {
                        stringbuilder.append(achar, 1, achar.length - 1);
                    } else {
                        stringbuilder.append(achar);
                    }
                    break;

                case 'G':
                case 'K':
                case 'P':
                    if (achar[1] == 78) {
                        stringbuilder.append(achar, 1, achar.length - 1);
                    } else {
                        stringbuilder.append(achar);
                    }
                    break;

                case 'W':
                    if (achar[1] == 82) {
                        stringbuilder.append(achar, 1, achar.length - 1);
                    } else if (achar[1] == 72) {
                        stringbuilder.append(achar, 1, achar.length - 1);
                        stringbuilder.setCharAt(0, 'W');
                    } else {
                        stringbuilder.append(achar);
                    }
                    break;

                case 'X':
                    achar[0] = 83;
                    stringbuilder.append(achar);
                    break;

                default:
                    stringbuilder.append(achar);
                }

                int i = stringbuilder.length();
                int j = 0;

                while (stringbuilder1.length() < this.getMaxCodeLen() && j < i) {
                    char c0 = stringbuilder.charAt(j);

                    if (c0 != 67 && this.isPreviousChar(stringbuilder, j, c0)) {
                        ++j;
                    } else {
                        switch (c0) {
                        case 'A':
                        case 'E':
                        case 'I':
                        case 'O':
                        case 'U':
                            if (j == 0) {
                                stringbuilder1.append(c0);
                            }
                            break;

                        case 'B':
                            if (!this.isPreviousChar(stringbuilder, j, 'M') || !this.isLastChar(i, j)) {
                                stringbuilder1.append(c0);
                            }
                            break;

                        case 'C':
                            if (!this.isPreviousChar(stringbuilder, j, 'S') || this.isLastChar(i, j) || "EIY".indexOf(stringbuilder.charAt(j + 1)) < 0) {
                                if (this.regionMatch(stringbuilder, j, "CIA")) {
                                    stringbuilder1.append('X');
                                } else if (!this.isLastChar(i, j) && "EIY".indexOf(stringbuilder.charAt(j + 1)) >= 0) {
                                    stringbuilder1.append('S');
                                } else if (this.isPreviousChar(stringbuilder, j, 'S') && this.isNextChar(stringbuilder, j, 'H')) {
                                    stringbuilder1.append('K');
                                } else if (this.isNextChar(stringbuilder, j, 'H')) {
                                    if (j == 0 && i >= 3 && this.isVowel(stringbuilder, 2)) {
                                        stringbuilder1.append('K');
                                    } else {
                                        stringbuilder1.append('X');
                                    }
                                } else {
                                    stringbuilder1.append('K');
                                }
                            }
                            break;

                        case 'D':
                            if (!this.isLastChar(i, j + 1) && this.isNextChar(stringbuilder, j, 'G') && "EIY".indexOf(stringbuilder.charAt(j + 2)) >= 0) {
                                stringbuilder1.append('J');
                                j += 2;
                            } else {
                                stringbuilder1.append('T');
                            }
                            break;

                        case 'F':
                        case 'J':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'R':
                            stringbuilder1.append(c0);
                            break;

                        case 'G':
                            if ((!this.isLastChar(i, j + 1) || !this.isNextChar(stringbuilder, j, 'H')) && (this.isLastChar(i, j + 1) || !this.isNextChar(stringbuilder, j, 'H') || this.isVowel(stringbuilder, j + 2)) && (j <= 0 || !this.regionMatch(stringbuilder, j, "GN") && !this.regionMatch(stringbuilder, j, "GNED"))) {
                                if (this.isPreviousChar(stringbuilder, j, 'G')) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }

                                if (!this.isLastChar(i, j) && "EIY".indexOf(stringbuilder.charAt(j + 1)) >= 0 && !flag) {
                                    stringbuilder1.append('J');
                                } else {
                                    stringbuilder1.append('K');
                                }
                            }
                            break;

                        case 'H':
                            if (!this.isLastChar(i, j) && (j <= 0 || "CSPTG".indexOf(stringbuilder.charAt(j - 1)) < 0) && this.isVowel(stringbuilder, j + 1)) {
                                stringbuilder1.append('H');
                            }
                            break;

                        case 'K':
                            if (j > 0) {
                                if (!this.isPreviousChar(stringbuilder, j, 'C')) {
                                    stringbuilder1.append(c0);
                                }
                            } else {
                                stringbuilder1.append(c0);
                            }
                            break;

                        case 'P':
                            if (this.isNextChar(stringbuilder, j, 'H')) {
                                stringbuilder1.append('F');
                            } else {
                                stringbuilder1.append(c0);
                            }
                            break;

                        case 'Q':
                            stringbuilder1.append('K');
                            break;

                        case 'S':
                            if (!this.regionMatch(stringbuilder, j, "SH") && !this.regionMatch(stringbuilder, j, "SIO") && !this.regionMatch(stringbuilder, j, "SIA")) {
                                stringbuilder1.append('S');
                            } else {
                                stringbuilder1.append('X');
                            }
                            break;

                        case 'T':
                            if (!this.regionMatch(stringbuilder, j, "TIA") && !this.regionMatch(stringbuilder, j, "TIO")) {
                                if (!this.regionMatch(stringbuilder, j, "TCH")) {
                                    if (this.regionMatch(stringbuilder, j, "TH")) {
                                        stringbuilder1.append('0');
                                    } else {
                                        stringbuilder1.append('T');
                                    }
                                }
                            } else {
                                stringbuilder1.append('X');
                            }
                            break;

                        case 'V':
                            stringbuilder1.append('F');
                            break;

                        case 'W':
                        case 'Y':
                            if (!this.isLastChar(i, j) && this.isVowel(stringbuilder, j + 1)) {
                                stringbuilder1.append(c0);
                            }
                            break;

                        case 'X':
                            stringbuilder1.append('K');
                            stringbuilder1.append('S');
                            break;

                        case 'Z':
                            stringbuilder1.append('S');
                        }

                        ++j;
                    }

                    if (stringbuilder1.length() > this.getMaxCodeLen()) {
                        stringbuilder1.setLength(this.getMaxCodeLen());
                    }
                }

                return stringbuilder1.toString();
            }
        } else {
            return "";
        }
    }

    private boolean isVowel(StringBuilder stringbuilder, int i) {
        return "AEIOU".indexOf(stringbuilder.charAt(i)) >= 0;
    }

    private boolean isPreviousChar(StringBuilder stringbuilder, int i, char c0) {
        boolean flag = false;

        if (i > 0 && i < stringbuilder.length()) {
            flag = stringbuilder.charAt(i - 1) == c0;
        }

        return flag;
    }

    private boolean isNextChar(StringBuilder stringbuilder, int i, char c0) {
        boolean flag = false;

        if (i >= 0 && i < stringbuilder.length() - 1) {
            flag = stringbuilder.charAt(i + 1) == c0;
        }

        return flag;
    }

    private boolean regionMatch(StringBuilder stringbuilder, int i, String s) {
        boolean flag = false;

        if (i >= 0 && i + s.length() - 1 < stringbuilder.length()) {
            String s1 = stringbuilder.substring(i, i + s.length());

            flag = s1.equals(s);
        }

        return flag;
    }

    private boolean isLastChar(int i, int j) {
        return j + 1 == i;
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
        } else {
            return this.metaphone((String) object);
        }
    }

    public String encode(String s) {
        return this.metaphone(s);
    }

    public boolean isMetaphoneEqual(String s, String s1) {
        return this.metaphone(s).equals(this.metaphone(s1));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int i) {
        this.maxCodeLen = i;
    }
}
