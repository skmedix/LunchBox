package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public abstract class CodePointTranslator extends CharSequenceTranslator {

    public final int translate(CharSequence charsequence, int i, Writer writer) throws IOException {
        int j = Character.codePointAt(charsequence, i);
        boolean flag = this.translate(j, writer);

        return flag ? 1 : 0;
    }

    public abstract boolean translate(int i, Writer writer) throws IOException;
}
