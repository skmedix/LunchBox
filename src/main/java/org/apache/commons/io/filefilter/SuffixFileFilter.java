package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

public class SuffixFileFilter extends AbstractFileFilter implements Serializable {

    private final String[] suffixes;
    private final IOCase caseSensitivity;

    public SuffixFileFilter(String s) {
        this(s, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String s, IOCase iocase) {
        if (s == null) {
            throw new IllegalArgumentException("The suffix must not be null");
        } else {
            this.suffixes = new String[] { s};
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public SuffixFileFilter(String[] astring) {
        this(astring, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String[] astring, IOCase iocase) {
        if (astring == null) {
            throw new IllegalArgumentException("The array of suffixes must not be null");
        } else {
            this.suffixes = new String[astring.length];
            System.arraycopy(astring, 0, this.suffixes, 0, astring.length);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public SuffixFileFilter(List list) {
        this(list, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(List list, IOCase iocase) {
        if (list == null) {
            throw new IllegalArgumentException("The list of suffixes must not be null");
        } else {
            this.suffixes = (String[]) list.toArray(new String[list.size()]);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public boolean accept(File file) {
        String s = file.getName();
        String[] astring = this.suffixes;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (this.caseSensitivity.checkEndsWith(s, s1)) {
                return true;
            }
        }

        return false;
    }

    public boolean accept(File file, String s) {
        String[] astring = this.suffixes;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (this.caseSensitivity.checkEndsWith(s, s1)) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append("(");
        if (this.suffixes != null) {
            for (int i = 0; i < this.suffixes.length; ++i) {
                if (i > 0) {
                    stringbuilder.append(",");
                }

                stringbuilder.append(this.suffixes[i]);
            }
        }

        stringbuilder.append(")");
        return stringbuilder.toString();
    }
}
