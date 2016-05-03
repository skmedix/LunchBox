package org.apache.logging.log4j.core.lookup;

import java.util.Arrays;
import org.apache.logging.log4j.core.helpers.Strings;

public abstract class StrMatcher {

    private static final StrMatcher COMMA_MATCHER = new StrMatcher.CharMatcher(',');
    private static final StrMatcher TAB_MATCHER = new StrMatcher.CharMatcher('\t');
    private static final StrMatcher SPACE_MATCHER = new StrMatcher.CharMatcher(' ');
    private static final StrMatcher SPLIT_MATCHER = new StrMatcher.CharSetMatcher(" \t\n\r\f".toCharArray());
    private static final StrMatcher TRIM_MATCHER = new StrMatcher.TrimMatcher();
    private static final StrMatcher SINGLE_QUOTE_MATCHER = new StrMatcher.CharMatcher('\'');
    private static final StrMatcher DOUBLE_QUOTE_MATCHER = new StrMatcher.CharMatcher('\"');
    private static final StrMatcher QUOTE_MATCHER = new StrMatcher.CharSetMatcher("\'\"".toCharArray());
    private static final StrMatcher NONE_MATCHER = new StrMatcher.NoMatcher();

    public static StrMatcher commaMatcher() {
        return StrMatcher.COMMA_MATCHER;
    }

    public static StrMatcher tabMatcher() {
        return StrMatcher.TAB_MATCHER;
    }

    public static StrMatcher spaceMatcher() {
        return StrMatcher.SPACE_MATCHER;
    }

    public static StrMatcher splitMatcher() {
        return StrMatcher.SPLIT_MATCHER;
    }

    public static StrMatcher trimMatcher() {
        return StrMatcher.TRIM_MATCHER;
    }

    public static StrMatcher singleQuoteMatcher() {
        return StrMatcher.SINGLE_QUOTE_MATCHER;
    }

    public static StrMatcher doubleQuoteMatcher() {
        return StrMatcher.DOUBLE_QUOTE_MATCHER;
    }

    public static StrMatcher quoteMatcher() {
        return StrMatcher.QUOTE_MATCHER;
    }

    public static StrMatcher noneMatcher() {
        return StrMatcher.NONE_MATCHER;
    }

    public static StrMatcher charMatcher(char c0) {
        return new StrMatcher.CharMatcher(c0);
    }

    public static StrMatcher charSetMatcher(char[] achar) {
        return (StrMatcher) (achar != null && achar.length != 0 ? (achar.length == 1 ? new StrMatcher.CharMatcher(achar[0]) : new StrMatcher.CharSetMatcher(achar)) : StrMatcher.NONE_MATCHER);
    }

    public static StrMatcher charSetMatcher(String s) {
        return (StrMatcher) (Strings.isEmpty(s) ? StrMatcher.NONE_MATCHER : (s.length() == 1 ? new StrMatcher.CharMatcher(s.charAt(0)) : new StrMatcher.CharSetMatcher(s.toCharArray())));
    }

    public static StrMatcher stringMatcher(String s) {
        return (StrMatcher) (Strings.isEmpty(s) ? StrMatcher.NONE_MATCHER : new StrMatcher.StringMatcher(s));
    }

    public abstract int isMatch(char[] achar, int i, int j, int k);

    public int isMatch(char[] achar, int i) {
        return this.isMatch(achar, i, 0, achar.length);
    }

    static final class TrimMatcher extends StrMatcher {

        public int isMatch(char[] achar, int i, int j, int k) {
            return achar[i] <= 32 ? 1 : 0;
        }
    }

    static final class NoMatcher extends StrMatcher {

        public int isMatch(char[] achar, int i, int j, int k) {
            return 0;
        }
    }

    static final class StringMatcher extends StrMatcher {

        private final char[] chars;

        StringMatcher(String s) {
            this.chars = s.toCharArray();
        }

        public int isMatch(char[] achar, int i, int j, int k) {
            int l = this.chars.length;

            if (i + l > k) {
                return 0;
            } else {
                for (int i1 = 0; i1 < this.chars.length; ++i) {
                    if (this.chars[i1] != achar[i]) {
                        return 0;
                    }

                    ++i1;
                }

                return l;
            }
        }
    }

    static final class CharMatcher extends StrMatcher {

        private final char ch;

        CharMatcher(char c0) {
            this.ch = c0;
        }

        public int isMatch(char[] achar, int i, int j, int k) {
            return this.ch == achar[i] ? 1 : 0;
        }
    }

    static final class CharSetMatcher extends StrMatcher {

        private final char[] chars;

        CharSetMatcher(char[] achar) {
            this.chars = (char[]) achar.clone();
            Arrays.sort(this.chars);
        }

        public int isMatch(char[] achar, int i, int j, int k) {
            return Arrays.binarySearch(this.chars, achar[i]) >= 0 ? 1 : 0;
        }
    }
}
