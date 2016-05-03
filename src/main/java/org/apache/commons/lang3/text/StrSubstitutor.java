package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class StrSubstitutor {

    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private StrMatcher suffixMatcher;
    private StrMatcher valueDelimiterMatcher;
    private StrLookup variableResolver;
    private boolean enableSubstitutionInVariables;

    public static String replace(Object object, Map map) {
        return (new StrSubstitutor(map)).replace(object);
    }

    public static String replace(Object object, Map map, String s, String s1) {
        return (new StrSubstitutor(map, s, s1)).replace(object);
    }

    public static String replace(Object object, Properties properties) {
        if (properties == null) {
            return object.toString();
        } else {
            HashMap hashmap = new HashMap();
            Enumeration enumeration = properties.propertyNames();

            while (enumeration.hasMoreElements()) {
                String s = (String) enumeration.nextElement();
                String s1 = properties.getProperty(s);

                hashmap.put(s, s1);
            }

            return replace(object, (Map) hashmap);
        }
    }

    public static String replaceSystemProperties(Object object) {
        return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(object);
    }

    public StrSubstitutor() {
        this((StrLookup) null, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map map) {
        this(StrLookup.mapLookup(map), StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map map, String s, String s1) {
        this(StrLookup.mapLookup(map), s, s1, '$');
    }

    public StrSubstitutor(Map map, String s, String s1, char c0) {
        this(StrLookup.mapLookup(map), s, s1, c0);
    }

    public StrSubstitutor(Map map, String s, String s1, char c0, String s2) {
        this(StrLookup.mapLookup(map), s, s1, c0, s2);
    }

    public StrSubstitutor(StrLookup strlookup) {
        this(strlookup, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(StrLookup strlookup, String s, String s1, char c0) {
        this.setVariableResolver(strlookup);
        this.setVariablePrefix(s);
        this.setVariableSuffix(s1);
        this.setEscapeChar(c0);
        this.setValueDelimiterMatcher(StrSubstitutor.DEFAULT_VALUE_DELIMITER);
    }

    public StrSubstitutor(StrLookup strlookup, String s, String s1, char c0, String s2) {
        this.setVariableResolver(strlookup);
        this.setVariablePrefix(s);
        this.setVariableSuffix(s1);
        this.setEscapeChar(c0);
        this.setValueDelimiter(s2);
    }

    public StrSubstitutor(StrLookup strlookup, StrMatcher strmatcher, StrMatcher strmatcher1, char c0) {
        this(strlookup, strmatcher, strmatcher1, c0, StrSubstitutor.DEFAULT_VALUE_DELIMITER);
    }

    public StrSubstitutor(StrLookup strlookup, StrMatcher strmatcher, StrMatcher strmatcher1, char c0, StrMatcher strmatcher2) {
        this.setVariableResolver(strlookup);
        this.setVariablePrefixMatcher(strmatcher);
        this.setVariableSuffixMatcher(strmatcher1);
        this.setEscapeChar(c0);
        this.setValueDelimiterMatcher(strmatcher2);
    }

    public String replace(String s) {
        if (s == null) {
            return null;
        } else {
            StrBuilder strbuilder = new StrBuilder(s);

            return !this.substitute(strbuilder, 0, s.length()) ? s : strbuilder.toString();
        }
    }

    public String replace(String s, int i, int j) {
        if (s == null) {
            return null;
        } else {
            StrBuilder strbuilder = (new StrBuilder(j)).append(s, i, j);

            return !this.substitute(strbuilder, 0, j) ? s.substring(i, i + j) : strbuilder.toString();
        }
    }

    public String replace(char[] achar) {
        if (achar == null) {
            return null;
        } else {
            StrBuilder strbuilder = (new StrBuilder(achar.length)).append(achar);

            this.substitute(strbuilder, 0, achar.length);
            return strbuilder.toString();
        }
    }

    public String replace(char[] achar, int i, int j) {
        if (achar == null) {
            return null;
        } else {
            StrBuilder strbuilder = (new StrBuilder(j)).append(achar, i, j);

            this.substitute(strbuilder, 0, j);
            return strbuilder.toString();
        }
    }

    public String replace(StringBuffer stringbuffer) {
        if (stringbuffer == null) {
            return null;
        } else {
            StrBuilder strbuilder = (new StrBuilder(stringbuffer.length())).append(stringbuffer);

            this.substitute(strbuilder, 0, strbuilder.length());
            return strbuilder.toString();
        }
    }

    public String replace(StringBuffer stringbuffer, int i, int j) {
        if (stringbuffer == null) {
            return null;
        } else {
            StrBuilder strbuilder = (new StrBuilder(j)).append(stringbuffer, i, j);

            this.substitute(strbuilder, 0, j);
            return strbuilder.toString();
        }
    }

    public String replace(CharSequence charsequence) {
        return charsequence == null ? null : this.replace(charsequence, 0, charsequence.length());
    }

    public String replace(CharSequence charsequence, int i, int j) {
        if (charsequence == null) {
            return null;
        } else {
            StrBuilder strbuilder = (new StrBuilder(j)).append(charsequence, i, j);

            this.substitute(strbuilder, 0, j);
            return strbuilder.toString();
        }
    }

    public String replace(StrBuilder strbuilder) {
        if (strbuilder == null) {
            return null;
        } else {
            StrBuilder strbuilder1 = (new StrBuilder(strbuilder.length())).append(strbuilder);

            this.substitute(strbuilder1, 0, strbuilder1.length());
            return strbuilder1.toString();
        }
    }

    public String replace(StrBuilder strbuilder, int i, int j) {
        if (strbuilder == null) {
            return null;
        } else {
            StrBuilder strbuilder1 = (new StrBuilder(j)).append(strbuilder, i, j);

            this.substitute(strbuilder1, 0, j);
            return strbuilder1.toString();
        }
    }

    public String replace(Object object) {
        if (object == null) {
            return null;
        } else {
            StrBuilder strbuilder = (new StrBuilder()).append(object);

            this.substitute(strbuilder, 0, strbuilder.length());
            return strbuilder.toString();
        }
    }

    public boolean replaceIn(StringBuffer stringbuffer) {
        return stringbuffer == null ? false : this.replaceIn(stringbuffer, 0, stringbuffer.length());
    }

    public boolean replaceIn(StringBuffer stringbuffer, int i, int j) {
        if (stringbuffer == null) {
            return false;
        } else {
            StrBuilder strbuilder = (new StrBuilder(j)).append(stringbuffer, i, j);

            if (!this.substitute(strbuilder, 0, j)) {
                return false;
            } else {
                stringbuffer.replace(i, i + j, strbuilder.toString());
                return true;
            }
        }
    }

    public boolean replaceIn(StringBuilder stringbuilder) {
        return stringbuilder == null ? false : this.replaceIn(stringbuilder, 0, stringbuilder.length());
    }

    public boolean replaceIn(StringBuilder stringbuilder, int i, int j) {
        if (stringbuilder == null) {
            return false;
        } else {
            StrBuilder strbuilder = (new StrBuilder(j)).append(stringbuilder, i, j);

            if (!this.substitute(strbuilder, 0, j)) {
                return false;
            } else {
                stringbuilder.replace(i, i + j, strbuilder.toString());
                return true;
            }
        }
    }

    public boolean replaceIn(StrBuilder strbuilder) {
        return strbuilder == null ? false : this.substitute(strbuilder, 0, strbuilder.length());
    }

    public boolean replaceIn(StrBuilder strbuilder, int i, int j) {
        return strbuilder == null ? false : this.substitute(strbuilder, i, j);
    }

    protected boolean substitute(StrBuilder strbuilder, int i, int j) {
        return this.substitute(strbuilder, i, j, (List) null) > 0;
    }

    private int substitute(StrBuilder strbuilder, int i, int j, List list) {
        StrMatcher strmatcher = this.getVariablePrefixMatcher();
        StrMatcher strmatcher1 = this.getVariableSuffixMatcher();
        char c0 = this.getEscapeChar();
        StrMatcher strmatcher2 = this.getValueDelimiterMatcher();
        boolean flag = this.isEnableSubstitutionInVariables();
        boolean flag1 = list == null;
        boolean flag2 = false;
        int k = 0;
        char[] achar = strbuilder.buffer;
        int l = i + j;
        int i1 = i;

        while (i1 < l) {
            int j1 = strmatcher.isMatch(achar, i1, i, l);

            if (j1 == 0) {
                ++i1;
            } else if (i1 > i && achar[i1 - 1] == c0) {
                strbuilder.deleteCharAt(i1 - 1);
                achar = strbuilder.buffer;
                --k;
                flag2 = true;
                --l;
            } else {
                int k1 = i1;

                i1 += j1;
                boolean flag3 = false;
                int l1 = 0;

                while (i1 < l) {
                    int i2;

                    if (flag && (i2 = strmatcher.isMatch(achar, i1, i, l)) != 0) {
                        ++l1;
                        i1 += i2;
                    } else {
                        i2 = strmatcher1.isMatch(achar, i1, i, l);
                        if (i2 == 0) {
                            ++i1;
                        } else {
                            if (l1 == 0) {
                                String s = new String(achar, k1 + j1, i1 - k1 - j1);

                                if (flag) {
                                    StrBuilder strbuilder1 = new StrBuilder(s);

                                    this.substitute(strbuilder1, 0, strbuilder1.length());
                                    s = strbuilder1.toString();
                                }

                                i1 += i2;
                                String s1 = s;
                                String s2 = null;
                                int j2;
                                int k2;

                                if (strmatcher2 != null) {
                                    char[] achar1 = s.toCharArray();
                                    boolean flag4 = false;

                                    for (j2 = 0; j2 < achar1.length && (flag || strmatcher.isMatch(achar1, j2, j2, achar1.length) == 0); ++j2) {
                                        if ((k2 = strmatcher2.isMatch(achar1, j2)) != 0) {
                                            s1 = s.substring(0, j2);
                                            s2 = s.substring(j2 + k2);
                                            break;
                                        }
                                    }
                                }

                                if (list == null) {
                                    list = new ArrayList();
                                    ((List) list).add(new String(achar, i, j));
                                }

                                this.checkCyclicSubstitution(s1, (List) list);
                                ((List) list).add(s1);
                                String s3 = this.resolveVariable(s1, strbuilder, k1, i1);

                                if (s3 == null) {
                                    s3 = s2;
                                }

                                if (s3 != null) {
                                    k2 = s3.length();
                                    strbuilder.replace(k1, i1, s3);
                                    flag2 = true;
                                    j2 = this.substitute(strbuilder, k1, k2, (List) list);
                                    j2 = j2 + k2 - (i1 - k1);
                                    i1 += j2;
                                    l += j2;
                                    k += j2;
                                    achar = strbuilder.buffer;
                                }

                                ((List) list).remove(((List) list).size() - 1);
                                break;
                            }

                            --l1;
                            i1 += i2;
                        }
                    }
                }
            }
        }

        if (flag1) {
            return flag2 ? 1 : 0;
        } else {
            return k;
        }
    }

    private void checkCyclicSubstitution(String s, List list) {
        if (list.contains(s)) {
            StrBuilder strbuilder = new StrBuilder(256);

            strbuilder.append("Infinite loop in property interpolation of ");
            strbuilder.append((String) list.remove(0));
            strbuilder.append(": ");
            strbuilder.appendWithSeparators((Iterable) list, "->");
            throw new IllegalStateException(strbuilder.toString());
        }
    }

    protected String resolveVariable(String s, StrBuilder strbuilder, int i, int j) {
        StrLookup strlookup = this.getVariableResolver();

        return strlookup == null ? null : strlookup.lookup(s);
    }

    public char getEscapeChar() {
        return this.escapeChar;
    }

    public void setEscapeChar(char c0) {
        this.escapeChar = c0;
    }

    public StrMatcher getVariablePrefixMatcher() {
        return this.prefixMatcher;
    }

    public StrSubstitutor setVariablePrefixMatcher(StrMatcher strmatcher) {
        if (strmatcher == null) {
            throw new IllegalArgumentException("Variable prefix matcher must not be null!");
        } else {
            this.prefixMatcher = strmatcher;
            return this;
        }
    }

    public StrSubstitutor setVariablePrefix(char c0) {
        return this.setVariablePrefixMatcher(StrMatcher.charMatcher(c0));
    }

    public StrSubstitutor setVariablePrefix(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Variable prefix must not be null!");
        } else {
            return this.setVariablePrefixMatcher(StrMatcher.stringMatcher(s));
        }
    }

    public StrMatcher getVariableSuffixMatcher() {
        return this.suffixMatcher;
    }

    public StrSubstitutor setVariableSuffixMatcher(StrMatcher strmatcher) {
        if (strmatcher == null) {
            throw new IllegalArgumentException("Variable suffix matcher must not be null!");
        } else {
            this.suffixMatcher = strmatcher;
            return this;
        }
    }

    public StrSubstitutor setVariableSuffix(char c0) {
        return this.setVariableSuffixMatcher(StrMatcher.charMatcher(c0));
    }

    public StrSubstitutor setVariableSuffix(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Variable suffix must not be null!");
        } else {
            return this.setVariableSuffixMatcher(StrMatcher.stringMatcher(s));
        }
    }

    public StrMatcher getValueDelimiterMatcher() {
        return this.valueDelimiterMatcher;
    }

    public StrSubstitutor setValueDelimiterMatcher(StrMatcher strmatcher) {
        this.valueDelimiterMatcher = strmatcher;
        return this;
    }

    public StrSubstitutor setValueDelimiter(char c0) {
        return this.setValueDelimiterMatcher(StrMatcher.charMatcher(c0));
    }

    public StrSubstitutor setValueDelimiter(String s) {
        if (StringUtils.isEmpty(s)) {
            this.setValueDelimiterMatcher((StrMatcher) null);
            return this;
        } else {
            return this.setValueDelimiterMatcher(StrMatcher.stringMatcher(s));
        }
    }

    public StrLookup getVariableResolver() {
        return this.variableResolver;
    }

    public void setVariableResolver(StrLookup strlookup) {
        this.variableResolver = strlookup;
    }

    public boolean isEnableSubstitutionInVariables() {
        return this.enableSubstitutionInVariables;
    }

    public void setEnableSubstitutionInVariables(boolean flag) {
        this.enableSubstitutionInVariables = flag;
    }
}
