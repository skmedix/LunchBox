package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.regex.Pattern;
import org.apache.commons.io.IOCase;

public class RegexFileFilter extends AbstractFileFilter implements Serializable {

    private final Pattern pattern;

    public RegexFileFilter(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Pattern is missing");
        } else {
            this.pattern = Pattern.compile(s);
        }
    }

    public RegexFileFilter(String s, IOCase iocase) {
        if (s == null) {
            throw new IllegalArgumentException("Pattern is missing");
        } else {
            byte b0 = 0;

            if (iocase != null && !iocase.isCaseSensitive()) {
                b0 = 2;
            }

            this.pattern = Pattern.compile(s, b0);
        }
    }

    public RegexFileFilter(String s, int i) {
        if (s == null) {
            throw new IllegalArgumentException("Pattern is missing");
        } else {
            this.pattern = Pattern.compile(s, i);
        }
    }

    public RegexFileFilter(Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is missing");
        } else {
            this.pattern = pattern;
        }
    }

    public boolean accept(File file, String s) {
        return this.pattern.matcher(s).matches();
    }
}
