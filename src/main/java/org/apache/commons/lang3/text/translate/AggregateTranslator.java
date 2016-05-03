package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.ArrayUtils;

public class AggregateTranslator extends CharSequenceTranslator {

    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... acharsequencetranslator) {
        this.translators = (CharSequenceTranslator[]) ArrayUtils.clone((Object[]) acharsequencetranslator);
    }

    public int translate(CharSequence charsequence, int i, Writer writer) throws IOException {
        CharSequenceTranslator[] acharsequencetranslator = this.translators;
        int j = acharsequencetranslator.length;

        for (int k = 0; k < j; ++k) {
            CharSequenceTranslator charsequencetranslator = acharsequencetranslator[k];
            int l = charsequencetranslator.translate(charsequence, i, writer);

            if (l != 0) {
                return l;
            }
        }

        return 0;
    }
}
