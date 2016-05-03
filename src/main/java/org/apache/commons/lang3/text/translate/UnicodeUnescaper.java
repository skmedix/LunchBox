package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeUnescaper extends CharSequenceTranslator {

    public int translate(CharSequence charsequence, int i, Writer writer) throws IOException {
        if (charsequence.charAt(i) == 92 && i + 1 < charsequence.length() && charsequence.charAt(i + 1) == 117) {
            int j;

            for (j = 2; i + j < charsequence.length() && charsequence.charAt(i + j) == 117; ++j) {
                ;
            }

            if (i + j < charsequence.length() && charsequence.charAt(i + j) == 43) {
                ++j;
            }

            if (i + j + 4 <= charsequence.length()) {
                CharSequence charsequence1 = charsequence.subSequence(i + j, i + j + 4);

                try {
                    int k = Integer.parseInt(charsequence1.toString(), 16);

                    writer.write((char) k);
                } catch (NumberFormatException numberformatexception) {
                    throw new IllegalArgumentException("Unable to parse unicode value: " + charsequence1, numberformatexception);
                }

                return j + 4;
            } else {
                throw new IllegalArgumentException("Less than 4 hex digits in unicode value: \'" + charsequence.subSequence(i, charsequence.length()) + "\' due to end of CharSequence");
            }
        } else {
            return 0;
        }
    }
}
