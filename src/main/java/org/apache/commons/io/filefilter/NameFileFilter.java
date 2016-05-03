package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

public class NameFileFilter extends AbstractFileFilter implements Serializable {

    private final String[] names;
    private final IOCase caseSensitivity;

    public NameFileFilter(String s) {
        this(s, (IOCase) null);
    }

    public NameFileFilter(String s, IOCase iocase) {
        if (s == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        } else {
            this.names = new String[] { s};
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public NameFileFilter(String[] astring) {
        this(astring, (IOCase) null);
    }

    public NameFileFilter(String[] astring, IOCase iocase) {
        if (astring == null) {
            throw new IllegalArgumentException("The array of names must not be null");
        } else {
            this.names = new String[astring.length];
            System.arraycopy(astring, 0, this.names, 0, astring.length);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public NameFileFilter(List list) {
        this(list, (IOCase) null);
    }

    public NameFileFilter(List list, IOCase iocase) {
        if (list == null) {
            throw new IllegalArgumentException("The list of names must not be null");
        } else {
            this.names = (String[]) list.toArray(new String[list.size()]);
            this.caseSensitivity = iocase == null ? IOCase.SENSITIVE : iocase;
        }
    }

    public boolean accept(File file) {
        String s = file.getName();
        String[] astring = this.names;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (this.caseSensitivity.checkEquals(s, s1)) {
                return true;
            }
        }

        return false;
    }

    public boolean accept(File file, String s) {
        String[] astring = this.names;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (this.caseSensitivity.checkEquals(s, s1)) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append("(");
        if (this.names != null) {
            for (int i = 0; i < this.names.length; ++i) {
                if (i > 0) {
                    stringbuilder.append(",");
                }

                stringbuilder.append(this.names[i]);
            }
        }

        stringbuilder.append(")");
        return stringbuilder.toString();
    }
}
