package org.apache.commons.codec.language;

import java.util.Locale;

public class Caverphone2 extends AbstractCaverphone {

    private static final String TEN_1 = "1111111111";

    public String encode(String s) {
        if (s != null && s.length() != 0) {
            String s1 = s.toLowerCase(Locale.ENGLISH);

            s1 = s1.replaceAll("[^a-z]", "");
            s1 = s1.replaceAll("e$", "");
            s1 = s1.replaceAll("^cough", "cou2f");
            s1 = s1.replaceAll("^rough", "rou2f");
            s1 = s1.replaceAll("^tough", "tou2f");
            s1 = s1.replaceAll("^enough", "enou2f");
            s1 = s1.replaceAll("^trough", "trou2f");
            s1 = s1.replaceAll("^gn", "2n");
            s1 = s1.replaceAll("mb$", "m2");
            s1 = s1.replaceAll("cq", "2q");
            s1 = s1.replaceAll("ci", "si");
            s1 = s1.replaceAll("ce", "se");
            s1 = s1.replaceAll("cy", "sy");
            s1 = s1.replaceAll("tch", "2ch");
            s1 = s1.replaceAll("c", "k");
            s1 = s1.replaceAll("q", "k");
            s1 = s1.replaceAll("x", "k");
            s1 = s1.replaceAll("v", "f");
            s1 = s1.replaceAll("dg", "2g");
            s1 = s1.replaceAll("tio", "sio");
            s1 = s1.replaceAll("tia", "sia");
            s1 = s1.replaceAll("d", "t");
            s1 = s1.replaceAll("ph", "fh");
            s1 = s1.replaceAll("b", "p");
            s1 = s1.replaceAll("sh", "s2");
            s1 = s1.replaceAll("z", "s");
            s1 = s1.replaceAll("^[aeiou]", "A");
            s1 = s1.replaceAll("[aeiou]", "3");
            s1 = s1.replaceAll("j", "y");
            s1 = s1.replaceAll("^y3", "Y3");
            s1 = s1.replaceAll("^y", "A");
            s1 = s1.replaceAll("y", "3");
            s1 = s1.replaceAll("3gh3", "3kh3");
            s1 = s1.replaceAll("gh", "22");
            s1 = s1.replaceAll("g", "k");
            s1 = s1.replaceAll("s+", "S");
            s1 = s1.replaceAll("t+", "T");
            s1 = s1.replaceAll("p+", "P");
            s1 = s1.replaceAll("k+", "K");
            s1 = s1.replaceAll("f+", "F");
            s1 = s1.replaceAll("m+", "M");
            s1 = s1.replaceAll("n+", "N");
            s1 = s1.replaceAll("w3", "W3");
            s1 = s1.replaceAll("wh3", "Wh3");
            s1 = s1.replaceAll("w$", "3");
            s1 = s1.replaceAll("w", "2");
            s1 = s1.replaceAll("^h", "A");
            s1 = s1.replaceAll("h", "2");
            s1 = s1.replaceAll("r3", "R3");
            s1 = s1.replaceAll("r$", "3");
            s1 = s1.replaceAll("r", "2");
            s1 = s1.replaceAll("l3", "L3");
            s1 = s1.replaceAll("l$", "3");
            s1 = s1.replaceAll("l", "2");
            s1 = s1.replaceAll("2", "");
            s1 = s1.replaceAll("3$", "A");
            s1 = s1.replaceAll("3", "");
            s1 = s1 + "1111111111";
            return s1.substring(0, "1111111111".length());
        } else {
            return "1111111111";
        }
    }
}
