package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DoubleMetaphone implements StringEncoder {

    private static final String VOWELS = "AEIOUY";
    private static final String[] SILENT_START = new String[] { "GN", "KN", "PN", "WR", "PS"};
    private static final String[] L_R_N_M_B_H_F_V_W_SPACE = new String[] { "L", "R", "N", "M", "B", "H", "F", "V", "W", " "};
    private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[] { "ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER"};
    private static final String[] L_T_K_S_N_M_B_Z = new String[] { "L", "T", "K", "S", "N", "M", "B", "Z"};
    private int maxCodeLen = 4;

    public String doubleMetaphone(String s) {
        return this.doubleMetaphone(s, false);
    }

    public String doubleMetaphone(String s, boolean flag) {
        s = this.cleanInput(s);
        if (s == null) {
            return null;
        } else {
            boolean flag1 = this.isSlavoGermanic(s);
            int i = this.isSilentStart(s) ? 1 : 0;
            DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult = new DoubleMetaphone.DoubleMetaphoneResult(this.getMaxCodeLen());

            while (!doublemetaphone_doublemetaphoneresult.isComplete() && i <= s.length() - 1) {
                switch (s.charAt(i)) {
                case 'A':
                case 'E':
                case 'I':
                case 'O':
                case 'U':
                case 'Y':
                    i = this.handleAEIOUY(doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'B':
                    doublemetaphone_doublemetaphoneresult.append('P');
                    i = this.charAt(s, i + 1) == 66 ? i + 2 : i + 1;
                    break;

                case 'C':
                    i = this.handleC(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'D':
                    i = this.handleD(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'F':
                    doublemetaphone_doublemetaphoneresult.append('F');
                    i = this.charAt(s, i + 1) == 70 ? i + 2 : i + 1;
                    break;

                case 'G':
                    i = this.handleG(s, doublemetaphone_doublemetaphoneresult, i, flag1);
                    break;

                case 'H':
                    i = this.handleH(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'J':
                    i = this.handleJ(s, doublemetaphone_doublemetaphoneresult, i, flag1);
                    break;

                case 'K':
                    doublemetaphone_doublemetaphoneresult.append('K');
                    i = this.charAt(s, i + 1) == 75 ? i + 2 : i + 1;
                    break;

                case 'L':
                    i = this.handleL(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'M':
                    doublemetaphone_doublemetaphoneresult.append('M');
                    i = this.conditionM0(s, i) ? i + 2 : i + 1;
                    break;

                case 'N':
                    doublemetaphone_doublemetaphoneresult.append('N');
                    i = this.charAt(s, i + 1) == 78 ? i + 2 : i + 1;
                    break;

                case 'P':
                    i = this.handleP(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'Q':
                    doublemetaphone_doublemetaphoneresult.append('K');
                    i = this.charAt(s, i + 1) == 81 ? i + 2 : i + 1;
                    break;

                case 'R':
                    i = this.handleR(s, doublemetaphone_doublemetaphoneresult, i, flag1);
                    break;

                case 'S':
                    i = this.handleS(s, doublemetaphone_doublemetaphoneresult, i, flag1);
                    break;

                case 'T':
                    i = this.handleT(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'V':
                    doublemetaphone_doublemetaphoneresult.append('F');
                    i = this.charAt(s, i + 1) == 86 ? i + 2 : i + 1;
                    break;

                case 'W':
                    i = this.handleW(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'X':
                    i = this.handleX(s, doublemetaphone_doublemetaphoneresult, i);
                    break;

                case 'Z':
                    i = this.handleZ(s, doublemetaphone_doublemetaphoneresult, i, flag1);
                    break;

                case 'Ç':
                    doublemetaphone_doublemetaphoneresult.append('S');
                    ++i;
                    break;

                case 'Ñ':
                    doublemetaphone_doublemetaphoneresult.append('N');
                    ++i;
                    break;

                default:
                    ++i;
                }
            }

            return flag ? doublemetaphone_doublemetaphoneresult.getAlternate() : doublemetaphone_doublemetaphoneresult.getPrimary();
        }
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
        } else {
            return this.doubleMetaphone((String) object);
        }
    }

    public String encode(String s) {
        return this.doubleMetaphone(s);
    }

    public boolean isDoubleMetaphoneEqual(String s, String s1) {
        return this.isDoubleMetaphoneEqual(s, s1, false);
    }

    public boolean isDoubleMetaphoneEqual(String s, String s1, boolean flag) {
        return this.doubleMetaphone(s, flag).equals(this.doubleMetaphone(s1, flag));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int i) {
        this.maxCodeLen = i;
    }

    private int handleAEIOUY(DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (i == 0) {
            doublemetaphone_doublemetaphoneresult.append('A');
        }

        return i + 1;
    }

    private int handleC(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (this.conditionC0(s, i)) {
            doublemetaphone_doublemetaphoneresult.append('K');
            i += 2;
        } else if (i == 0 && contains(s, i, 6, new String[] { "CAESAR"})) {
            doublemetaphone_doublemetaphoneresult.append('S');
            i += 2;
        } else if (contains(s, i, 2, new String[] { "CH"})) {
            i = this.handleCH(s, doublemetaphone_doublemetaphoneresult, i);
        } else if (contains(s, i, 2, new String[] { "CZ"}) && !contains(s, i - 2, 4, new String[] { "WICZ"})) {
            doublemetaphone_doublemetaphoneresult.append('S', 'X');
            i += 2;
        } else if (contains(s, i + 1, 3, new String[] { "CIA"})) {
            doublemetaphone_doublemetaphoneresult.append('X');
            i += 3;
        } else {
            if (contains(s, i, 2, new String[] { "CC"}) && (i != 1 || this.charAt(s, 0) != 77)) {
                return this.handleCC(s, doublemetaphone_doublemetaphoneresult, i);
            }

            if (contains(s, i, 2, new String[] { "CK", "CG", "CQ"})) {
                doublemetaphone_doublemetaphoneresult.append('K');
                i += 2;
            } else if (contains(s, i, 2, new String[] { "CI", "CE", "CY"})) {
                if (contains(s, i, 3, new String[] { "CIO", "CIE", "CIA"})) {
                    doublemetaphone_doublemetaphoneresult.append('S', 'X');
                } else {
                    doublemetaphone_doublemetaphoneresult.append('S');
                }

                i += 2;
            } else {
                doublemetaphone_doublemetaphoneresult.append('K');
                if (contains(s, i + 1, 2, new String[] { " C", " Q", " G"})) {
                    i += 3;
                } else if (contains(s, i + 1, 1, new String[] { "C", "K", "Q"}) && !contains(s, i + 1, 2, new String[] { "CE", "CI"})) {
                    i += 2;
                } else {
                    ++i;
                }
            }
        }

        return i;
    }

    private int handleCC(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (contains(s, i + 2, 1, new String[] { "I", "E", "H"}) && !contains(s, i + 2, 2, new String[] { "HU"})) {
            if ((i != 1 || this.charAt(s, i - 1) != 65) && !contains(s, i - 1, 5, new String[] { "UCCEE", "UCCES"})) {
                doublemetaphone_doublemetaphoneresult.append('X');
            } else {
                doublemetaphone_doublemetaphoneresult.append("KS");
            }

            i += 3;
        } else {
            doublemetaphone_doublemetaphoneresult.append('K');
            i += 2;
        }

        return i;
    }

    private int handleCH(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (i > 0 && contains(s, i, 4, new String[] { "CHAE"})) {
            doublemetaphone_doublemetaphoneresult.append('K', 'X');
            return i + 2;
        } else if (this.conditionCH0(s, i)) {
            doublemetaphone_doublemetaphoneresult.append('K');
            return i + 2;
        } else if (this.conditionCH1(s, i)) {
            doublemetaphone_doublemetaphoneresult.append('K');
            return i + 2;
        } else {
            if (i > 0) {
                if (contains(s, 0, 2, new String[] { "MC"})) {
                    doublemetaphone_doublemetaphoneresult.append('K');
                } else {
                    doublemetaphone_doublemetaphoneresult.append('X', 'K');
                }
            } else {
                doublemetaphone_doublemetaphoneresult.append('X');
            }

            return i + 2;
        }
    }

    private int handleD(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (contains(s, i, 2, new String[] { "DG"})) {
            if (contains(s, i + 2, 1, new String[] { "I", "E", "Y"})) {
                doublemetaphone_doublemetaphoneresult.append('J');
                i += 3;
            } else {
                doublemetaphone_doublemetaphoneresult.append("TK");
                i += 2;
            }
        } else if (contains(s, i, 2, new String[] { "DT", "DD"})) {
            doublemetaphone_doublemetaphoneresult.append('T');
            i += 2;
        } else {
            doublemetaphone_doublemetaphoneresult.append('T');
            ++i;
        }

        return i;
    }

    private int handleG(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i, boolean flag) {
        if (this.charAt(s, i + 1) == 72) {
            i = this.handleGH(s, doublemetaphone_doublemetaphoneresult, i);
        } else if (this.charAt(s, i + 1) == 78) {
            if (i == 1 && this.isVowel(this.charAt(s, 0)) && !flag) {
                doublemetaphone_doublemetaphoneresult.append("KN", "N");
            } else if (!contains(s, i + 2, 2, new String[] { "EY"}) && this.charAt(s, i + 1) != 89 && !flag) {
                doublemetaphone_doublemetaphoneresult.append("N", "KN");
            } else {
                doublemetaphone_doublemetaphoneresult.append("KN");
            }

            i += 2;
        } else if (contains(s, i + 1, 2, new String[] { "LI"}) && !flag) {
            doublemetaphone_doublemetaphoneresult.append("KL", "L");
            i += 2;
        } else if (i == 0 && (this.charAt(s, i + 1) == 89 || contains(s, i + 1, 2, DoubleMetaphone.ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
            doublemetaphone_doublemetaphoneresult.append('K', 'J');
            i += 2;
        } else if ((contains(s, i + 1, 2, new String[] { "ER"}) || this.charAt(s, i + 1) == 89) && !contains(s, 0, 6, new String[] { "DANGER", "RANGER", "MANGER"}) && !contains(s, i - 1, 1, new String[] { "E", "I"}) && !contains(s, i - 1, 3, new String[] { "RGY", "OGY"})) {
            doublemetaphone_doublemetaphoneresult.append('K', 'J');
            i += 2;
        } else if (!contains(s, i + 1, 1, new String[] { "E", "I", "Y"}) && !contains(s, i - 1, 4, new String[] { "AGGI", "OGGI"})) {
            if (this.charAt(s, i + 1) == 71) {
                i += 2;
                doublemetaphone_doublemetaphoneresult.append('K');
            } else {
                ++i;
                doublemetaphone_doublemetaphoneresult.append('K');
            }
        } else {
            if (!contains(s, 0, 4, new String[] { "VAN ", "VON "}) && !contains(s, 0, 3, new String[] { "SCH"}) && !contains(s, i + 1, 2, new String[] { "ET"})) {
                if (contains(s, i + 1, 3, new String[] { "IER"})) {
                    doublemetaphone_doublemetaphoneresult.append('J');
                } else {
                    doublemetaphone_doublemetaphoneresult.append('J', 'K');
                }
            } else {
                doublemetaphone_doublemetaphoneresult.append('K');
            }

            i += 2;
        }

        return i;
    }

    private int handleGH(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (i > 0 && !this.isVowel(this.charAt(s, i - 1))) {
            doublemetaphone_doublemetaphoneresult.append('K');
            i += 2;
        } else if (i == 0) {
            if (this.charAt(s, i + 2) == 73) {
                doublemetaphone_doublemetaphoneresult.append('J');
            } else {
                doublemetaphone_doublemetaphoneresult.append('K');
            }

            i += 2;
        } else if ((i <= 1 || !contains(s, i - 2, 1, new String[] { "B", "H", "D"})) && (i <= 2 || !contains(s, i - 3, 1, new String[] { "B", "H", "D"})) && (i <= 3 || !contains(s, i - 4, 1, new String[] { "B", "H"}))) {
            if (i > 2 && this.charAt(s, i - 1) == 85 && contains(s, i - 3, 1, new String[] { "C", "G", "L", "R", "T"})) {
                doublemetaphone_doublemetaphoneresult.append('F');
            } else if (i > 0 && this.charAt(s, i - 1) != 73) {
                doublemetaphone_doublemetaphoneresult.append('K');
            }

            i += 2;
        } else {
            i += 2;
        }

        return i;
    }

    private int handleH(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if ((i == 0 || this.isVowel(this.charAt(s, i - 1))) && this.isVowel(this.charAt(s, i + 1))) {
            doublemetaphone_doublemetaphoneresult.append('H');
            i += 2;
        } else {
            ++i;
        }

        return i;
    }

    private int handleJ(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i, boolean flag) {
        if (!contains(s, i, 4, new String[] { "JOSE"}) && !contains(s, 0, 4, new String[] { "SAN "})) {
            if (i == 0 && !contains(s, i, 4, new String[] { "JOSE"})) {
                doublemetaphone_doublemetaphoneresult.append('J', 'A');
            } else if (this.isVowel(this.charAt(s, i - 1)) && !flag && (this.charAt(s, i + 1) == 65 || this.charAt(s, i + 1) == 79)) {
                doublemetaphone_doublemetaphoneresult.append('J', 'H');
            } else if (i == s.length() - 1) {
                doublemetaphone_doublemetaphoneresult.append('J', ' ');
            } else if (!contains(s, i + 1, 1, DoubleMetaphone.L_T_K_S_N_M_B_Z) && !contains(s, i - 1, 1, new String[] { "S", "K", "L"})) {
                doublemetaphone_doublemetaphoneresult.append('J');
            }

            if (this.charAt(s, i + 1) == 74) {
                i += 2;
            } else {
                ++i;
            }
        } else {
            if ((i != 0 || this.charAt(s, i + 4) != 32) && s.length() != 4 && !contains(s, 0, 4, new String[] { "SAN "})) {
                doublemetaphone_doublemetaphoneresult.append('J', 'H');
            } else {
                doublemetaphone_doublemetaphoneresult.append('H');
            }

            ++i;
        }

        return i;
    }

    private int handleL(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (this.charAt(s, i + 1) == 76) {
            if (this.conditionL0(s, i)) {
                doublemetaphone_doublemetaphoneresult.appendPrimary('L');
            } else {
                doublemetaphone_doublemetaphoneresult.append('L');
            }

            i += 2;
        } else {
            ++i;
            doublemetaphone_doublemetaphoneresult.append('L');
        }

        return i;
    }

    private int handleP(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (this.charAt(s, i + 1) == 72) {
            doublemetaphone_doublemetaphoneresult.append('F');
            i += 2;
        } else {
            doublemetaphone_doublemetaphoneresult.append('P');
            i = contains(s, i + 1, 1, new String[] { "P", "B"}) ? i + 2 : i + 1;
        }

        return i;
    }

    private int handleR(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i, boolean flag) {
        if (i == s.length() - 1 && !flag && contains(s, i - 2, 2, new String[] { "IE"}) && !contains(s, i - 4, 2, new String[] { "ME", "MA"})) {
            doublemetaphone_doublemetaphoneresult.appendAlternate('R');
        } else {
            doublemetaphone_doublemetaphoneresult.append('R');
        }

        return this.charAt(s, i + 1) == 82 ? i + 2 : i + 1;
    }

    private int handleS(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i, boolean flag) {
        if (contains(s, i - 1, 3, new String[] { "ISL", "YSL"})) {
            ++i;
        } else if (i == 0 && contains(s, i, 5, new String[] { "SUGAR"})) {
            doublemetaphone_doublemetaphoneresult.append('X', 'S');
            ++i;
        } else if (contains(s, i, 2, new String[] { "SH"})) {
            if (contains(s, i + 1, 4, new String[] { "HEIM", "HOEK", "HOLM", "HOLZ"})) {
                doublemetaphone_doublemetaphoneresult.append('S');
            } else {
                doublemetaphone_doublemetaphoneresult.append('X');
            }

            i += 2;
        } else if (!contains(s, i, 3, new String[] { "SIO", "SIA"}) && !contains(s, i, 4, new String[] { "SIAN"})) {
            if ((i != 0 || !contains(s, i + 1, 1, new String[] { "M", "N", "L", "W"})) && !contains(s, i + 1, 1, new String[] { "Z"})) {
                if (contains(s, i, 2, new String[] { "SC"})) {
                    i = this.handleSC(s, doublemetaphone_doublemetaphoneresult, i);
                } else {
                    if (i == s.length() - 1 && contains(s, i - 2, 2, new String[] { "AI", "OI"})) {
                        doublemetaphone_doublemetaphoneresult.appendAlternate('S');
                    } else {
                        doublemetaphone_doublemetaphoneresult.append('S');
                    }

                    i = contains(s, i + 1, 1, new String[] { "S", "Z"}) ? i + 2 : i + 1;
                }
            } else {
                doublemetaphone_doublemetaphoneresult.append('S', 'X');
                i = contains(s, i + 1, 1, new String[] { "Z"}) ? i + 2 : i + 1;
            }
        } else {
            if (flag) {
                doublemetaphone_doublemetaphoneresult.append('S');
            } else {
                doublemetaphone_doublemetaphoneresult.append('S', 'X');
            }

            i += 3;
        }

        return i;
    }

    private int handleSC(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (this.charAt(s, i + 2) == 72) {
            if (contains(s, i + 3, 2, new String[] { "OO", "ER", "EN", "UY", "ED", "EM"})) {
                if (contains(s, i + 3, 2, new String[] { "ER", "EN"})) {
                    doublemetaphone_doublemetaphoneresult.append("X", "SK");
                } else {
                    doublemetaphone_doublemetaphoneresult.append("SK");
                }
            } else if (i == 0 && !this.isVowel(this.charAt(s, 3)) && this.charAt(s, 3) != 87) {
                doublemetaphone_doublemetaphoneresult.append('X', 'S');
            } else {
                doublemetaphone_doublemetaphoneresult.append('X');
            }
        } else if (contains(s, i + 2, 1, new String[] { "I", "E", "Y"})) {
            doublemetaphone_doublemetaphoneresult.append('S');
        } else {
            doublemetaphone_doublemetaphoneresult.append("SK");
        }

        return i + 3;
    }

    private int handleT(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (contains(s, i, 4, new String[] { "TION"})) {
            doublemetaphone_doublemetaphoneresult.append('X');
            i += 3;
        } else if (contains(s, i, 3, new String[] { "TIA", "TCH"})) {
            doublemetaphone_doublemetaphoneresult.append('X');
            i += 3;
        } else if (!contains(s, i, 2, new String[] { "TH"}) && !contains(s, i, 3, new String[] { "TTH"})) {
            doublemetaphone_doublemetaphoneresult.append('T');
            i = contains(s, i + 1, 1, new String[] { "T", "D"}) ? i + 2 : i + 1;
        } else {
            if (!contains(s, i + 2, 2, new String[] { "OM", "AM"}) && !contains(s, 0, 4, new String[] { "VAN ", "VON "}) && !contains(s, 0, 3, new String[] { "SCH"})) {
                doublemetaphone_doublemetaphoneresult.append('0', 'T');
            } else {
                doublemetaphone_doublemetaphoneresult.append('T');
            }

            i += 2;
        }

        return i;
    }

    private int handleW(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (contains(s, i, 2, new String[] { "WR"})) {
            doublemetaphone_doublemetaphoneresult.append('R');
            i += 2;
        } else if (i == 0 && (this.isVowel(this.charAt(s, i + 1)) || contains(s, i, 2, new String[] { "WH"}))) {
            if (this.isVowel(this.charAt(s, i + 1))) {
                doublemetaphone_doublemetaphoneresult.append('A', 'F');
            } else {
                doublemetaphone_doublemetaphoneresult.append('A');
            }

            ++i;
        } else if ((i != s.length() - 1 || !this.isVowel(this.charAt(s, i - 1))) && !contains(s, i - 1, 5, new String[] { "EWSKI", "EWSKY", "OWSKI", "OWSKY"}) && !contains(s, 0, 3, new String[] { "SCH"})) {
            if (contains(s, i, 4, new String[] { "WICZ", "WITZ"})) {
                doublemetaphone_doublemetaphoneresult.append("TS", "FX");
                i += 4;
            } else {
                ++i;
            }
        } else {
            doublemetaphone_doublemetaphoneresult.appendAlternate('F');
            ++i;
        }

        return i;
    }

    private int handleX(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i) {
        if (i == 0) {
            doublemetaphone_doublemetaphoneresult.append('S');
            ++i;
        } else {
            if (i != s.length() - 1 || !contains(s, i - 3, 3, new String[] { "IAU", "EAU"}) && !contains(s, i - 2, 2, new String[] { "AU", "OU"})) {
                doublemetaphone_doublemetaphoneresult.append("KS");
            }

            i = contains(s, i + 1, 1, new String[] { "C", "X"}) ? i + 2 : i + 1;
        }

        return i;
    }

    private int handleZ(String s, DoubleMetaphone.DoubleMetaphoneResult doublemetaphone_doublemetaphoneresult, int i, boolean flag) {
        if (this.charAt(s, i + 1) == 72) {
            doublemetaphone_doublemetaphoneresult.append('J');
            i += 2;
        } else {
            if (!contains(s, i + 1, 2, new String[] { "ZO", "ZI", "ZA"}) && (!flag || i <= 0 || this.charAt(s, i - 1) == 84)) {
                doublemetaphone_doublemetaphoneresult.append('S');
            } else {
                doublemetaphone_doublemetaphoneresult.append("S", "TS");
            }

            i = this.charAt(s, i + 1) == 90 ? i + 2 : i + 1;
        }

        return i;
    }

    private boolean conditionC0(String s, int i) {
        if (contains(s, i, 4, new String[] { "CHIA"})) {
            return true;
        } else if (i <= 1) {
            return false;
        } else if (this.isVowel(this.charAt(s, i - 2))) {
            return false;
        } else if (!contains(s, i - 1, 3, new String[] { "ACH"})) {
            return false;
        } else {
            char c0 = this.charAt(s, i + 2);

            return c0 != 73 && c0 != 69 || contains(s, i - 2, 6, new String[] { "BACHER", "MACHER"});
        }
    }

    private boolean conditionCH0(String s, int i) {
        return i != 0 ? false : (!contains(s, i + 1, 5, new String[] { "HARAC", "HARIS"}) && !contains(s, i + 1, 3, new String[] { "HOR", "HYM", "HIA", "HEM"}) ? false : !contains(s, 0, 5, new String[] { "CHORE"}));
    }

    private boolean conditionCH1(String s, int i) {
        return contains(s, 0, 4, new String[] { "VAN ", "VON "}) || contains(s, 0, 3, new String[] { "SCH"}) || contains(s, i - 2, 6, new String[] { "ORCHES", "ARCHIT", "ORCHID"}) || contains(s, i + 2, 1, new String[] { "T", "S"}) || (contains(s, i - 1, 1, new String[] { "A", "O", "U", "E"}) || i == 0) && (contains(s, i + 2, 1, DoubleMetaphone.L_R_N_M_B_H_F_V_W_SPACE) || i + 1 == s.length() - 1);
    }

    private boolean conditionL0(String s, int i) {
        return i == s.length() - 3 && contains(s, i - 1, 4, new String[] { "ILLO", "ILLA", "ALLE"}) ? true : (contains(s, s.length() - 2, 2, new String[] { "AS", "OS"}) || contains(s, s.length() - 1, 1, new String[] { "A", "O"})) && contains(s, i - 1, 4, new String[] { "ALLE"});
    }

    private boolean conditionM0(String s, int i) {
        return this.charAt(s, i + 1) == 77 ? true : contains(s, i - 1, 3, new String[] { "UMB"}) && (i + 1 == s.length() - 1 || contains(s, i + 2, 2, new String[] { "ER"}));
    }

    private boolean isSlavoGermanic(String s) {
        return s.indexOf(87) > -1 || s.indexOf(75) > -1 || s.indexOf("CZ") > -1 || s.indexOf("WITZ") > -1;
    }

    private boolean isVowel(char c0) {
        return "AEIOUY".indexOf(c0) != -1;
    }

    private boolean isSilentStart(String s) {
        boolean flag = false;
        String[] astring = DoubleMetaphone.SILENT_START;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (s.startsWith(s1)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    private String cleanInput(String s) {
        if (s == null) {
            return null;
        } else {
            s = s.trim();
            return s.length() == 0 ? null : s.toUpperCase(Locale.ENGLISH);
        }
    }

    protected char charAt(String s, int i) {
        return i >= 0 && i < s.length() ? s.charAt(i) : '\u0000';
    }

    protected static boolean contains(String s, int i, int j, String... astring) {
        boolean flag = false;

        if (i >= 0 && i + j <= s.length()) {
            String s1 = s.substring(i, i + j);
            String[] astring1 = astring;
            int k = astring.length;

            for (int l = 0; l < k; ++l) {
                String s2 = astring1[l];

                if (s1.equals(s2)) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    public class DoubleMetaphoneResult {

        private final StringBuilder primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
        private final StringBuilder alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
        private final int maxLength;

        public DoubleMetaphoneResult(int i) {
            this.maxLength = i;
        }

        public void append(char c0) {
            this.appendPrimary(c0);
            this.appendAlternate(c0);
        }

        public void append(char c0, char c1) {
            this.appendPrimary(c0);
            this.appendAlternate(c1);
        }

        public void appendPrimary(char c0) {
            if (this.primary.length() < this.maxLength) {
                this.primary.append(c0);
            }

        }

        public void appendAlternate(char c0) {
            if (this.alternate.length() < this.maxLength) {
                this.alternate.append(c0);
            }

        }

        public void append(String s) {
            this.appendPrimary(s);
            this.appendAlternate(s);
        }

        public void append(String s, String s1) {
            this.appendPrimary(s);
            this.appendAlternate(s1);
        }

        public void appendPrimary(String s) {
            int i = this.maxLength - this.primary.length();

            if (s.length() <= i) {
                this.primary.append(s);
            } else {
                this.primary.append(s.substring(0, i));
            }

        }

        public void appendAlternate(String s) {
            int i = this.maxLength - this.alternate.length();

            if (s.length() <= i) {
                this.alternate.append(s);
            } else {
                this.alternate.append(s.substring(0, i));
            }

        }

        public String getPrimary() {
            return this.primary.toString();
        }

        public String getAlternate() {
            return this.alternate.toString();
        }

        public boolean isComplete() {
            return this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength;
        }
    }
}
