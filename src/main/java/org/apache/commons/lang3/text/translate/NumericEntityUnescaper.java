package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaper extends CharSequenceTranslator {

    private final EnumSet options;

    public NumericEntityUnescaper(NumericEntityUnescaper.OPTION... anumericentityunescaper_option) {
        if (anumericentityunescaper_option.length > 0) {
            this.options = EnumSet.copyOf(Arrays.asList(anumericentityunescaper_option));
        } else {
            this.options = EnumSet.copyOf(Arrays.asList(new NumericEntityUnescaper.OPTION[] { NumericEntityUnescaper.OPTION.semiColonRequired}));
        }

    }

    public boolean isSet(NumericEntityUnescaper.OPTION numericentityunescaper_option) {
        return this.options == null ? false : this.options.contains(numericentityunescaper_option);
    }

    public int translate(CharSequence charsequence, int i, Writer writer) throws IOException {
        int j = charsequence.length();

        if (charsequence.charAt(i) == 38 && i < j - 2 && charsequence.charAt(i + 1) == 35) {
            int k = i + 2;
            boolean flag = false;
            char c0 = charsequence.charAt(k);

            if (c0 == 120 || c0 == 88) {
                ++k;
                flag = true;
                if (k == j) {
                    return 0;
                }
            }

            int l;

            for (l = k; l < j && (charsequence.charAt(l) >= 48 && charsequence.charAt(l) <= 57 || charsequence.charAt(l) >= 97 && charsequence.charAt(l) <= 102 || charsequence.charAt(l) >= 65 && charsequence.charAt(l) <= 70); ++l) {
                ;
            }

            boolean flag1 = l != j && charsequence.charAt(l) == 59;

            if (!flag1) {
                if (this.isSet(NumericEntityUnescaper.OPTION.semiColonRequired)) {
                    return 0;
                }

                if (this.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon)) {
                    throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
                }
            }

            int i1;

            try {
                if (flag) {
                    i1 = Integer.parseInt(charsequence.subSequence(k, l).toString(), 16);
                } else {
                    i1 = Integer.parseInt(charsequence.subSequence(k, l).toString(), 10);
                }
            } catch (NumberFormatException numberformatexception) {
                return 0;
            }

            if (i1 > '\uffff') {
                char[] achar = Character.toChars(i1);

                writer.write(achar[0]);
                writer.write(achar[1]);
            } else {
                writer.write(i1);
            }

            return 2 + l - k + (flag ? 1 : 0) + (flag1 ? 1 : 0);
        } else {
            return 0;
        }
    }

    public static enum OPTION {

        semiColonRequired, semiColonOptional, errorIfNoSemiColon;
    }
}
