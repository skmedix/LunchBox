package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/** @deprecated */
@Deprecated
public class WildcardFilter extends AbstractFileFilter implements Serializable {

    private final String[] wildcards;

    public WildcardFilter(String s) {
        if (s == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        } else {
            this.wildcards = new String[] { s};
        }
    }

    public WildcardFilter(String[] astring) {
        if (astring == null) {
            throw new IllegalArgumentException("The wildcard array must not be null");
        } else {
            this.wildcards = new String[astring.length];
            System.arraycopy(astring, 0, this.wildcards, 0, astring.length);
        }
    }

    public WildcardFilter(List list) {
        if (list == null) {
            throw new IllegalArgumentException("The wildcard list must not be null");
        } else {
            this.wildcards = (String[]) list.toArray(new String[list.size()]);
        }
    }

    public boolean accept(File file, String s) {
        if (file != null && (new File(file, s)).isDirectory()) {
            return false;
        } else {
            String[] astring = this.wildcards;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s1 = astring[j];

                if (FilenameUtils.wildcardMatch(s, s1)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return false;
        } else {
            String[] astring = this.wildcards;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring[j];

                if (FilenameUtils.wildcardMatch(file.getName(), s)) {
                    return true;
                }
            }

            return false;
        }
    }
}
