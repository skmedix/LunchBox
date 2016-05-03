package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CharSet implements Serializable {

    private static final long serialVersionUID = 5947847346149275958L;
    public static final CharSet EMPTY = new CharSet(new String[] { (String) null});
    public static final CharSet ASCII_ALPHA = new CharSet(new String[] { "a-zA-Z"});
    public static final CharSet ASCII_ALPHA_LOWER = new CharSet(new String[] { "a-z"});
    public static final CharSet ASCII_ALPHA_UPPER = new CharSet(new String[] { "A-Z"});
    public static final CharSet ASCII_NUMERIC = new CharSet(new String[] { "0-9"});
    protected static final Map COMMON = Collections.synchronizedMap(new HashMap());
    private final Set set = Collections.synchronizedSet(new HashSet());

    public static CharSet getInstance(String... astring) {
        if (astring == null) {
            return null;
        } else {
            if (astring.length == 1) {
                CharSet charset = (CharSet) CharSet.COMMON.get(astring[0]);

                if (charset != null) {
                    return charset;
                }
            }

            return new CharSet(astring);
        }
    }

    protected CharSet(String... astring) {
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            this.add(astring[j]);
        }

    }

    protected void add(String s) {
        if (s != null) {
            int i = s.length();
            int j = 0;

            while (j < i) {
                int k = i - j;

                if (k >= 4 && s.charAt(j) == 94 && s.charAt(j + 2) == 45) {
                    this.set.add(CharRange.isNotIn(s.charAt(j + 1), s.charAt(j + 3)));
                    j += 4;
                } else if (k >= 3 && s.charAt(j + 1) == 45) {
                    this.set.add(CharRange.isIn(s.charAt(j), s.charAt(j + 2)));
                    j += 3;
                } else if (k >= 2 && s.charAt(j) == 94) {
                    this.set.add(CharRange.isNot(s.charAt(j + 1)));
                    j += 2;
                } else {
                    this.set.add(CharRange.is(s.charAt(j)));
                    ++j;
                }
            }

        }
    }

    CharRange[] getCharRanges() {
        return (CharRange[]) this.set.toArray(new CharRange[this.set.size()]);
    }

    public boolean contains(char c0) {
        Iterator iterator = this.set.iterator();

        CharRange charrange;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            charrange = (CharRange) iterator.next();
        } while (!charrange.contains(c0));

        return true;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof CharSet)) {
            return false;
        } else {
            CharSet charset = (CharSet) object;

            return this.set.equals(charset.set);
        }
    }

    public int hashCode() {
        return 89 + this.set.hashCode();
    }

    public String toString() {
        return this.set.toString();
    }

    static {
        CharSet.COMMON.put((Object) null, CharSet.EMPTY);
        CharSet.COMMON.put("", CharSet.EMPTY);
        CharSet.COMMON.put("a-zA-Z", CharSet.ASCII_ALPHA);
        CharSet.COMMON.put("A-Za-z", CharSet.ASCII_ALPHA);
        CharSet.COMMON.put("a-z", CharSet.ASCII_ALPHA_LOWER);
        CharSet.COMMON.put("A-Z", CharSet.ASCII_ALPHA_UPPER);
        CharSet.COMMON.put("0-9", CharSet.ASCII_NUMERIC);
    }
}
