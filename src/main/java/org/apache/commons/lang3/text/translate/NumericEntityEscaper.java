package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class NumericEntityEscaper extends CodePointTranslator {

    private final int below;
    private final int above;
    private final boolean between;

    private NumericEntityEscaper(int i, int j, boolean flag) {
        this.below = i;
        this.above = j;
        this.between = flag;
    }

    public NumericEntityEscaper() {
        this(0, Integer.MAX_VALUE, true);
    }

    public static NumericEntityEscaper below(int i) {
        return outsideOf(i, Integer.MAX_VALUE);
    }

    public static NumericEntityEscaper above(int i) {
        return outsideOf(0, i);
    }

    public static NumericEntityEscaper between(int i, int j) {
        return new NumericEntityEscaper(i, j, true);
    }

    public static NumericEntityEscaper outsideOf(int i, int j) {
        return new NumericEntityEscaper(i, j, false);
    }

    public boolean translate(int i, Writer writer) throws IOException {
        if (this.between) {
            if (i < this.below || i > this.above) {
                return false;
            }
        } else if (i >= this.below && i <= this.above) {
            return false;
        }

        writer.write("&#");
        writer.write(Integer.toString(i, 10));
        writer.write(59);
        return true;
    }
}
