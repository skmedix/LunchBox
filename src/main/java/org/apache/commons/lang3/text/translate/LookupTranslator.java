package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class LookupTranslator extends CharSequenceTranslator {

    private final HashMap lookupMap = new HashMap();
    private final int shortest;
    private final int longest;

    public LookupTranslator(CharSequence[]... acharsequence) {
        int i = Integer.MAX_VALUE;
        int j = 0;

        if (acharsequence != null) {
            CharSequence[][] acharsequence1 = acharsequence;
            int k = acharsequence.length;

            for (int l = 0; l < k; ++l) {
                CharSequence[] acharsequence2 = acharsequence1[l];

                this.lookupMap.put(acharsequence2[0].toString(), acharsequence2[1]);
                int i1 = acharsequence2[0].length();

                if (i1 < i) {
                    i = i1;
                }

                if (i1 > j) {
                    j = i1;
                }
            }
        }

        this.shortest = i;
        this.longest = j;
    }

    public int translate(CharSequence charsequence, int i, Writer writer) throws IOException {
        int j = this.longest;

        if (i + this.longest > charsequence.length()) {
            j = charsequence.length() - i;
        }

        for (int k = j; k >= this.shortest; --k) {
            CharSequence charsequence1 = charsequence.subSequence(i, i + k);
            CharSequence charsequence2 = (CharSequence) this.lookupMap.get(charsequence1.toString());

            if (charsequence2 != null) {
                writer.write(charsequence2.toString());
                return k;
            }
        }

        return 0;
    }
}
