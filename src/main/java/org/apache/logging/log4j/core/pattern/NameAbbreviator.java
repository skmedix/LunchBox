package org.apache.logging.log4j.core.pattern;

import java.util.ArrayList;
import java.util.List;

public abstract class NameAbbreviator {

    private static final NameAbbreviator DEFAULT = new NameAbbreviator.NOPAbbreviator();

    public static NameAbbreviator getAbbreviator(String s) {
        if (s.length() <= 0) {
            return NameAbbreviator.DEFAULT;
        } else {
            String s1 = s.trim();

            if (s1.isEmpty()) {
                return NameAbbreviator.DEFAULT;
            } else {
                int i;

                for (i = 0; i < s1.length() && s1.charAt(i) >= 48 && s1.charAt(i) <= 57; ++i) {
                    ;
                }

                if (i == s1.length()) {
                    return new NameAbbreviator.MaxElementAbbreviator(Integer.parseInt(s1));
                } else {
                    ArrayList arraylist = new ArrayList(5);

                    for (int j = 0; j < s1.length() && j >= 0; ++j) {
                        int k = j;
                        int l;

                        if (s1.charAt(j) == 42) {
                            l = Integer.MAX_VALUE;
                            k = j + 1;
                        } else if (s1.charAt(j) >= 48 && s1.charAt(j) <= 57) {
                            l = s1.charAt(j) - 48;
                            k = j + 1;
                        } else {
                            l = 0;
                        }

                        char c0 = 0;

                        if (k < s1.length()) {
                            c0 = s1.charAt(k);
                            if (c0 == 46) {
                                c0 = 0;
                            }
                        }

                        arraylist.add(new NameAbbreviator.PatternAbbreviatorFragment(l, c0));
                        j = s1.indexOf(46, j);
                        if (j == -1) {
                            break;
                        }
                    }

                    return new NameAbbreviator.PatternAbbreviator(arraylist);
                }
            }
        }
    }

    public static NameAbbreviator getDefaultAbbreviator() {
        return NameAbbreviator.DEFAULT;
    }

    public abstract String abbreviate(String s);

    private static class PatternAbbreviator extends NameAbbreviator {

        private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;

        public PatternAbbreviator(List list) {
            if (list.size() == 0) {
                throw new IllegalArgumentException("fragments must have at least one element");
            } else {
                this.fragments = new NameAbbreviator.PatternAbbreviatorFragment[list.size()];
                list.toArray(this.fragments);
            }
        }

        public String abbreviate(String s) {
            int i = 0;
            StringBuilder stringbuilder = new StringBuilder(s);

            for (int j = 0; j < this.fragments.length - 1 && i < s.length(); ++j) {
                i = this.fragments[j].abbreviate(stringbuilder, i);
            }

            for (NameAbbreviator.PatternAbbreviatorFragment nameabbreviator_patternabbreviatorfragment = this.fragments[this.fragments.length - 1]; i < s.length() && i >= 0; i = nameabbreviator_patternabbreviatorfragment.abbreviate(stringbuilder, i)) {
                ;
            }

            return stringbuilder.toString();
        }
    }

    private static class PatternAbbreviatorFragment {

        private final int charCount;
        private final char ellipsis;

        public PatternAbbreviatorFragment(int i, char c0) {
            this.charCount = i;
            this.ellipsis = c0;
        }

        public int abbreviate(StringBuilder stringbuilder, int i) {
            int j = stringbuilder.toString().indexOf(46, i);

            if (j != -1) {
                if (j - i > this.charCount) {
                    stringbuilder.delete(i + this.charCount, j);
                    j = i + this.charCount;
                    if (this.ellipsis != 0) {
                        stringbuilder.insert(j, this.ellipsis);
                        ++j;
                    }
                }

                ++j;
            }

            return j;
        }
    }

    private static class MaxElementAbbreviator extends NameAbbreviator {

        private final int count;

        public MaxElementAbbreviator(int i) {
            this.count = i < 1 ? 1 : i;
        }

        public String abbreviate(String s) {
            int i = s.length() - 1;

            for (int j = this.count; j > 0; --j) {
                i = s.lastIndexOf(46, i - 1);
                if (i == -1) {
                    return s;
                }
            }

            return s.substring(i + 1);
        }
    }

    private static class NOPAbbreviator extends NameAbbreviator {

        public String abbreviate(String s) {
            return s;
        }
    }
}
