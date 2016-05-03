package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeEscaper extends CodePointTranslator {

    private final int below;
    private final int above;
    private final boolean between;

    public UnicodeEscaper() {
        this(0, Integer.MAX_VALUE, true);
    }

    protected UnicodeEscaper(int i, int j, boolean flag) {
        this.below = i;
        this.above = j;
        this.between = flag;
    }

    public static UnicodeEscaper below(int i) {
        return outsideOf(i, Integer.MAX_VALUE);
    }

    public static UnicodeEscaper above(int i) {
        return outsideOf(0, i);
    }

    public static UnicodeEscaper outsideOf(int i, int j) {
        return new UnicodeEscaper(i, j, false);
    }

    public static UnicodeEscaper between(int i, int j) {
        return new UnicodeEscaper(i, j, true);
    }

    public boolean translate(int i, Writer writer) throws IOException {
        if (this.between) {
            if (i < this.below || i > this.above) {
                return false;
            }
        } else if (i >= this.below && i <= this.above) {
            return false;
        }

        if (i > '\uffff') {
            writer.write(this.toUtf16Escape(i));
        } else if (i > 4095) {
            writer.write("\\u" + hex(i));
        } else if (i > 255) {
            writer.write("\\u0" + hex(i));
        } else if (i > 15) {
            writer.write("\\u00" + hex(i));
        } else {
            writer.write("\\u000" + hex(i));
        }

        return true;
    }

    protected String toUtf16Escape(int i) {
        return "\\u" + hex(i);
    }
}
