package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public abstract class CharSequenceTranslator {

    public abstract int translate(CharSequence charsequence, int i, Writer writer) throws IOException;

    public final String translate(CharSequence charsequence) {
        if (charsequence == null) {
            return null;
        } else {
            try {
                StringWriter stringwriter = new StringWriter(charsequence.length() * 2);

                this.translate(charsequence, stringwriter);
                return stringwriter.toString();
            } catch (IOException ioexception) {
                throw new RuntimeException(ioexception);
            }
        }
    }

    public final void translate(CharSequence charsequence, Writer writer) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (charsequence != null) {
            int i = 0;
            int j = charsequence.length();

            while (i < j) {
                int k = this.translate(charsequence, i, writer);

                if (k == 0) {
                    char[] achar = Character.toChars(Character.codePointAt(charsequence, i));

                    writer.write(achar);
                    i += achar.length;
                } else {
                    for (int l = 0; l < k; ++l) {
                        i += Character.charCount(Character.codePointAt(charsequence, i));
                    }
                }
            }

        }
    }

    public final CharSequenceTranslator with(CharSequenceTranslator... acharsequencetranslator) {
        CharSequenceTranslator[] acharsequencetranslator1 = new CharSequenceTranslator[acharsequencetranslator.length + 1];

        acharsequencetranslator1[0] = this;
        System.arraycopy(acharsequencetranslator, 0, acharsequencetranslator1, 1, acharsequencetranslator.length);
        return new AggregateTranslator(acharsequencetranslator1);
    }

    public static String hex(int i) {
        return Integer.toHexString(i).toUpperCase(Locale.ENGLISH);
    }
}
