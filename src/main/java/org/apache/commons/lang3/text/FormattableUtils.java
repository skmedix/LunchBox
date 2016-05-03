package org.apache.commons.lang3.text;

import java.util.Formattable;
import java.util.Formatter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

public class FormattableUtils {

    private static final String SIMPLEST_FORMAT = "%s";

    public static String toString(Formattable formattable) {
        return String.format("%s", new Object[] { formattable});
    }

    public static Formatter append(CharSequence charsequence, Formatter formatter, int i, int j, int k) {
        return append(charsequence, formatter, i, j, k, ' ', (CharSequence) null);
    }

    public static Formatter append(CharSequence charsequence, Formatter formatter, int i, int j, int k, char c0) {
        return append(charsequence, formatter, i, j, k, c0, (CharSequence) null);
    }

    public static Formatter append(CharSequence charsequence, Formatter formatter, int i, int j, int k, CharSequence charsequence1) {
        return append(charsequence, formatter, i, j, k, ' ', charsequence1);
    }

    public static Formatter append(CharSequence charsequence, Formatter formatter, int i, int j, int k, char c0, CharSequence charsequence1) {
        Validate.isTrue(charsequence1 == null || k < 0 || charsequence1.length() <= k, "Specified ellipsis \'%1$s\' exceeds precision of %2$s", new Object[] { charsequence1, Integer.valueOf(k)});
        StringBuilder stringbuilder = new StringBuilder(charsequence);

        if (k >= 0 && k < charsequence.length()) {
            CharSequence charsequence2 = (CharSequence) ObjectUtils.defaultIfNull(charsequence1, "");

            stringbuilder.replace(k - charsequence2.length(), charsequence.length(), charsequence2.toString());
        }

        boolean flag = (i & 1) == 1;

        for (int l = stringbuilder.length(); l < j; ++l) {
            stringbuilder.insert(flag ? l : 0, c0);
        }

        formatter.format(stringbuilder.toString(), new Object[0]);
        return formatter;
    }
}
