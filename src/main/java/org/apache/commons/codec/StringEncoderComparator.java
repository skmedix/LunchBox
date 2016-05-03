package org.apache.commons.codec;

import java.util.Comparator;

public class StringEncoderComparator implements Comparator {

    private final StringEncoder stringEncoder;

    /** @deprecated */
    @Deprecated
    public StringEncoderComparator() {
        this.stringEncoder = null;
    }

    public StringEncoderComparator(StringEncoder stringencoder) {
        this.stringEncoder = stringencoder;
    }

    public int compare(Object object, Object object1) {
        boolean flag = false;

        int i;

        try {
            Comparable comparable = (Comparable) this.stringEncoder.encode(object);
            Comparable comparable1 = (Comparable) this.stringEncoder.encode(object1);

            i = comparable.compareTo(comparable1);
        } catch (EncoderException encoderexception) {
            i = 0;
        }

        return i;
    }
}
