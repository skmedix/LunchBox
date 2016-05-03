package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

public class WildcardFileFilter extends AbstractFileFilter implements Serializable {

    private final String[] wildcards;
    private final IOCase caseSensitivity;

    public WildcardFileFilter(String s) {
        this(s, (IOCase) null);
    }

    public WildcardFileFilter(String s, IOCase iocase) {
        if (s == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        } else {
            this.wildcards = new String[] { s};
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public WildcardFileFilter(String[] astring) {
        this(astring, (IOCase) null);
    }

    public WildcardFileFilter(String[] astring, IOCase iocase) {
        if (astring == null) {
            throw new IllegalArgumentException("The wildcard array must not be null");
        } else {
            this.wildcards = new String[astring.length];
            System.arraycopy(astring, 0, this.wildcards, 0, astring.length);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public WildcardFileFilter(List list) {
        this(list, (IOCase) null);
    }

    public WildcardFileFilter(List list, IOCase iocase) {
        if (list == null) {
            throw new IllegalArgumentException("The wildcard list must not be null");
        } else {
            this.wildcards = (String[]) list.toArray(new String[list.size()]);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public boolean accept(File file, String s) {
        String[] astring = this.wildcards;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (FilenameUtils.wildcardMatch(s, s1, this.caseSensitivity)) {
                return true;
            }
        }

        return false;
    }

    public boolean accept(File file) {
        String s = file.getName();
        String[] astring = this.wildcards;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (FilenameUtils.wildcardMatch(s, s1, this.caseSensitivity)) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append("(");
        if (this.wildcards != null) {
            for (int i = 0; i < this.wildcards.length; ++i) {
                if (i > 0) {
                    stringbuilder.append(",");
                }

                stringbuilder.append(this.wildcards[i]);
            }
        }

        stringbuilder.append(")");
        return stringbuilder.toString();
    }
}
