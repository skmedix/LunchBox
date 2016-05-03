package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {

    private static final char[] AEIJOUY = new char[] { 'A', 'E', 'I', 'J', 'O', 'U', 'Y'};
    private static final char[] SCZ = new char[] { 'S', 'C', 'Z'};
    private static final char[] WFPV = new char[] { 'W', 'F', 'P', 'V'};
    private static final char[] GKQ = new char[] { 'G', 'K', 'Q'};
    private static final char[] CKQ = new char[] { 'C', 'K', 'Q'};
    private static final char[] AHKLOQRUX = new char[] { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'};
    private static final char[] SZ = new char[] { 'S', 'Z'};
    private static final char[] AHOUKQX = new char[] { 'A', 'H', 'O', 'U', 'K', 'Q', 'X'};
    private static final char[] TDX = new char[] { 'T', 'D', 'X'};
    private static final char[][] PREPROCESS_MAP = new char[][] { { 'Ä', 'A'}, { 'Ü', 'U'}, { 'Ö', 'O'}, { 'ß', 'S'}};

    private static boolean arrayContains(char[] achar, char c0) {
        char[] achar1 = achar;
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c1 = achar1[j];

            if (c1 == c0) {
                return true;
            }
        }

        return false;
    }

    public String colognePhonetic(String s) {
        if (s == null) {
            return null;
        } else {
            s = this.preprocess(s);
            ColognePhonetic.CologneOutputBuffer colognephonetic_cologneoutputbuffer = new ColognePhonetic.CologneOutputBuffer(s.length() * 2);
            ColognePhonetic.CologneInputBuffer colognephonetic_cologneinputbuffer = new ColognePhonetic.CologneInputBuffer(s.toCharArray());
            char c0 = 45;
            char c1 = 47;
            int i = colognephonetic_cologneinputbuffer.length();

            while (i > 0) {
                char c2 = colognephonetic_cologneinputbuffer.removeNext();
                char c3;

                if ((i = colognephonetic_cologneinputbuffer.length()) > 0) {
                    c3 = colognephonetic_cologneinputbuffer.getNextChar();
                } else {
                    c3 = 45;
                }

                char c4;

                if (arrayContains(ColognePhonetic.AEIJOUY, c2)) {
                    c4 = 48;
                } else if (c2 != 72 && c2 >= 65 && c2 <= 90) {
                    if (c2 != 66 && (c2 != 80 || c3 == 72)) {
                        if ((c2 == 68 || c2 == 84) && !arrayContains(ColognePhonetic.SCZ, c3)) {
                            c4 = 50;
                        } else if (arrayContains(ColognePhonetic.WFPV, c2)) {
                            c4 = 51;
                        } else if (arrayContains(ColognePhonetic.GKQ, c2)) {
                            c4 = 52;
                        } else if (c2 == 88 && !arrayContains(ColognePhonetic.CKQ, c0)) {
                            c4 = 52;
                            colognephonetic_cologneinputbuffer.addLeft('S');
                            ++i;
                        } else if (c2 != 83 && c2 != 90) {
                            if (c2 == 67) {
                                if (c1 == 47) {
                                    if (arrayContains(ColognePhonetic.AHKLOQRUX, c3)) {
                                        c4 = 52;
                                    } else {
                                        c4 = 56;
                                    }
                                } else if (!arrayContains(ColognePhonetic.SZ, c0) && arrayContains(ColognePhonetic.AHOUKQX, c3)) {
                                    c4 = 52;
                                } else {
                                    c4 = 56;
                                }
                            } else if (arrayContains(ColognePhonetic.TDX, c2)) {
                                c4 = 56;
                            } else if (c2 == 82) {
                                c4 = 55;
                            } else if (c2 == 76) {
                                c4 = 53;
                            } else if (c2 != 77 && c2 != 78) {
                                c4 = c2;
                            } else {
                                c4 = 54;
                            }
                        } else {
                            c4 = 56;
                        }
                    } else {
                        c4 = 49;
                    }
                } else {
                    if (c1 == 47) {
                        continue;
                    }

                    c4 = 45;
                }

                if (c4 != 45 && (c1 != c4 && (c4 != 48 || c1 == 47) || c4 < 48 || c4 > 56)) {
                    colognephonetic_cologneoutputbuffer.addRight(c4);
                }

                c0 = c2;
                c1 = c4;
            }

            return colognephonetic_cologneoutputbuffer.toString();
        }
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("This method\'s parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
        } else {
            return this.encode((String) object);
        }
    }

    public String encode(String s) {
        return this.colognePhonetic(s);
    }

    public boolean isEncodeEqual(String s, String s1) {
        return this.colognePhonetic(s).equals(this.colognePhonetic(s1));
    }

    private String preprocess(String s) {
        s = s.toUpperCase(Locale.GERMAN);
        char[] achar = s.toCharArray();

        for (int i = 0; i < achar.length; ++i) {
            if (achar[i] > 90) {
                char[][] achar1 = ColognePhonetic.PREPROCESS_MAP;
                int j = achar1.length;

                for (int k = 0; k < j; ++k) {
                    char[] achar2 = achar1[k];

                    if (achar[i] == achar2[0]) {
                        achar[i] = achar2[1];
                        break;
                    }
                }
            }
        }

        return new String(achar);
    }

    private class CologneInputBuffer extends ColognePhonetic.CologneBuffer {

        public CologneInputBuffer(char[] achar) {
            super(achar);
        }

        public void addLeft(char c0) {
            ++this.length;
            this.data[this.getNextPos()] = c0;
        }

        protected char[] copyData(int i, int j) {
            char[] achar = new char[j];

            System.arraycopy(this.data, this.data.length - this.length + i, achar, 0, j);
            return achar;
        }

        public char getNextChar() {
            return this.data[this.getNextPos()];
        }

        protected int getNextPos() {
            return this.data.length - this.length;
        }

        public char removeNext() {
            char c0 = this.getNextChar();

            --this.length;
            return c0;
        }
    }

    private class CologneOutputBuffer extends ColognePhonetic.CologneBuffer {

        public CologneOutputBuffer(int i) {
            super(i);
        }

        public void addRight(char c0) {
            this.data[this.length] = c0;
            ++this.length;
        }

        protected char[] copyData(int i, int j) {
            char[] achar = new char[j];

            System.arraycopy(this.data, i, achar, 0, j);
            return achar;
        }
    }

    private abstract class CologneBuffer {

        protected final char[] data;
        protected int length = 0;

        public CologneBuffer(char[] achar) {
            this.data = achar;
            this.length = achar.length;
        }

        public CologneBuffer(int i) {
            this.data = new char[i];
            this.length = 0;
        }

        protected abstract char[] copyData(int i, int j);

        public int length() {
            return this.length;
        }

        public String toString() {
            return new String(this.copyData(0, this.length));
        }
    }
}
