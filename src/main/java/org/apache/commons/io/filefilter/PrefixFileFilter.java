package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

public class PrefixFileFilter extends AbstractFileFilter implements Serializable {

    private final String[] prefixes;
    private final IOCase caseSensitivity;

    public PrefixFileFilter(String s) {
        this(s, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(String s, IOCase iocase) {
        if (s == null) {
            throw new IllegalArgumentException("The prefix must not be null");
        } else {
            this.prefixes = new String[] { s};
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public PrefixFileFilter(String[] astring) {
        this(astring, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(String[] astring, IOCase iocase) {
        if (astring == null) {
            throw new IllegalArgumentException("The array of prefixes must not be null");
        } else {
            this.prefixes = new String[astring.length];
            System.arraycopy(astring, 0, this.prefixes, 0, astring.length);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public PrefixFileFilter(List list) {
        this(list, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(List list, IOCase iocase) {
        if (list == null) {
            throw new IllegalArgumentException("The list of prefixes must not be null");
        } else {
            this.prefixes = (String[]) list.toArray(new String[list.size()]);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public boolean accept(File file) {
        String s = file.getName();
        String[] astring = this.prefixes;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (this.caseSensitivity.checkStartsWith(s, s1)) {
                return true;
            }
        }

        return false;
    }

    public boolean accept(File file, String s) {
        String[] astring = this.prefixes;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (this.caseSensitivity.checkStartsWith(s, s1)) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append("(");
        if (this.prefixes != null) {
            for (int i = 0; i < this.prefixes.length; ++i) {
                if (i > 0) {
                    stringbuilder.append(",");
                }

                stringbuilder.append(this.prefixes[i]);
            }
        }

        stringbuilder.append(")");
        return stringbuilder.toString();
    }
}
