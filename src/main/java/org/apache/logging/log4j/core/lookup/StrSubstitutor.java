package org.apache.logging.log4j.core.lookup;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.core.LogEvent;

public class StrSubstitutor {

    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    private static final int BUF_SIZE = 256;
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private StrMatcher suffixMatcher;
    private StrLookup variableResolver;
    private boolean enableSubstitutionInVariables;

    public StrSubstitutor() {
        this((StrLookup) null, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map map) {
        this((StrLookup) (new MapLookup(map)), StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map map, String s, String s1) {
        this((StrLookup) (new MapLookup(map)), s, s1, '$');
    }

    public StrSubstitutor(Map map, String s, String s1, char c0) {
        this((StrLookup) (new MapLookup(map)), s, s1, c0);
    }

    public StrSubstitutor(StrLookup strlookup) {
        this(strlookup, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(StrLookup strlookup, String s, String s1, char c0) {
        this.setVariableResolver(strlookup);
        this.setVariablePrefix(s);
        this.setVariableSuffix(s1);
        this.setEscapeChar(c0);
    }

    public StrSubstitutor(StrLookup strlookup, StrMatcher strmatcher, StrMatcher strmatcher1, char c0) {
        this.setVariableResolver(strlookup);
        this.setVariablePrefixMatcher(strmatcher);
        this.setVariableSuffixMatcher(strmatcher1);
        this.setEscapeChar(c0);
    }

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

    public String replace(String s) {
        return this.replace((LogEvent) null, s);
    }

    public String replace(LogEvent logevent, String s) {
        if (s == null) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder(s);

            return !this.substitute(logevent, stringbuilder, 0, s.length()) ? s : stringbuilder.toString();
        }
    }

    public String replace(String s, int i, int j) {
        return this.replace((LogEvent) null, s, i, j);
    }

    public String replace(LogEvent logevent, String s, int i, int j) {
        if (s == null) {
            return null;
        } else {
            StringBuilder stringbuilder = (new StringBuilder(j)).append(s, i, j);

            return !this.substitute(logevent, stringbuilder, 0, j) ? s.substring(i, i + j) : stringbuilder.toString();
        }
    }

    public String replace(char[] achar) {
        return this.replace((LogEvent) null, achar);
    }

    public String replace(LogEvent logevent, char[] achar) {
        if (achar == null) {
            return null;
        } else {
            StringBuilder stringbuilder = (new StringBuilder(achar.length)).append(achar);

            this.substitute(logevent, stringbuilder, 0, achar.length);
            return stringbuilder.toString();
        }
    }

    public String replace(char[] achar, int i, int j) {
        return this.replace((LogEvent) null, achar, i, j);
    }

    public String replace(LogEvent logevent, char[] achar, int i, int j) {
        if (achar == null) {
            return null;
        } else {
            StringBuilder stringbuilder = (new StringBuilder(j)).append(achar, i, j);

            this.substitute(logevent, stringbuilder, 0, j);
            return stringbuilder.toString();
        }
    }

    public String replace(StringBuffer stringbuffer) {
        return this.replace((LogEvent) null, stringbuffer);
    }

    public String replace(LogEvent logevent, StringBuffer stringbuffer) {
        if (stringbuffer == null) {
            return null;
        } else {
            StringBuilder stringbuilder = (new StringBuilder(stringbuffer.length())).append(stringbuffer);

            this.substitute(logevent, stringbuilder, 0, stringbuilder.length());
            return stringbuilder.toString();
        }
    }

    public String replace(StringBuffer stringbuffer, int i, int j) {
        return this.replace((LogEvent) null, stringbuffer, i, j);
    }

    public String replace(LogEvent logevent, StringBuffer stringbuffer, int i, int j) {
        if (stringbuffer == null) {
            return null;
        } else {
            StringBuilder stringbuilder = (new StringBuilder(j)).append(stringbuffer, i, j);

            this.substitute(logevent, stringbuilder, 0, j);
            return stringbuilder.toString();
        }
    }

    public String replace(StringBuilder stringbuilder) {
        return this.replace((LogEvent) null, stringbuilder);
    }

    public String replace(LogEvent logevent, StringBuilder stringbuilder) {
        if (stringbuilder == null) {
            return null;
        } else {
            StringBuilder stringbuilder1 = (new StringBuilder(stringbuilder.length())).append(stringbuilder);

            this.substitute(logevent, stringbuilder1, 0, stringbuilder1.length());
            return stringbuilder1.toString();
        }
    }

    public String replace(StringBuilder stringbuilder, int i, int j) {
        return this.replace((LogEvent) null, stringbuilder, i, j);
    }

    public String replace(LogEvent logevent, StringBuilder stringbuilder, int i, int j) {
        if (stringbuilder == null) {
            return null;
        } else {
            StringBuilder stringbuilder1 = (new StringBuilder(j)).append(stringbuilder, i, j);

            this.substitute(logevent, stringbuilder1, 0, j);
            return stringbuilder1.toString();
        }
    }

    public String replace(Object object) {
        return this.replace((LogEvent) null, object);
    }

    public String replace(LogEvent logevent, Object object) {
        if (object == null) {
            return null;
        } else {
            StringBuilder stringbuilder = (new StringBuilder()).append(object);

            this.substitute(logevent, stringbuilder, 0, stringbuilder.length());
            return stringbuilder.toString();
        }
    }

    public boolean replaceIn(StringBuffer stringbuffer) {
        return stringbuffer == null ? false : this.replaceIn(stringbuffer, 0, stringbuffer.length());
    }

    public boolean replaceIn(StringBuffer stringbuffer, int i, int j) {
        return this.replaceIn((LogEvent) null, stringbuffer, i, j);
    }

    public boolean replaceIn(LogEvent logevent, StringBuffer stringbuffer, int i, int j) {
        if (stringbuffer == null) {
            return false;
        } else {
            StringBuilder stringbuilder = (new StringBuilder(j)).append(stringbuffer, i, j);

            if (!this.substitute(logevent, stringbuilder, 0, j)) {
                return false;
            } else {
                stringbuffer.replace(i, i + j, stringbuilder.toString());
                return true;
            }
        }
    }

    public boolean replaceIn(StringBuilder stringbuilder) {
        return this.replaceIn((LogEvent) null, stringbuilder);
    }

    public boolean replaceIn(LogEvent logevent, StringBuilder stringbuilder) {
        return stringbuilder == null ? false : this.substitute(logevent, stringbuilder, 0, stringbuilder.length());
    }

    public boolean replaceIn(StringBuilder stringbuilder, int i, int j) {
        return this.replaceIn((LogEvent) null, stringbuilder, i, j);
    }

    public boolean replaceIn(LogEvent logevent, StringBuilder stringbuilder, int i, int j) {
        return stringbuilder == null ? false : this.substitute(logevent, stringbuilder, i, j);
    }

    protected boolean substitute(LogEvent logevent, StringBuilder stringbuilder, int i, int j) {
        return this.substitute(logevent, stringbuilder, i, j, (List) null) > 0;
    }

    private int substitute(LogEvent logevent, StringBuilder stringbuilder, int i, int j, List list) {
        StrMatcher strmatcher = this.getVariablePrefixMatcher();
        StrMatcher strmatcher1 = this.getVariableSuffixMatcher();
        char c0 = this.getEscapeChar();
        boolean flag = list == null;
        boolean flag1 = false;
        int k = 0;
        char[] achar = this.getChars(stringbuilder);
        int l = i + j;
        int i1 = i;

        while (i1 < l) {
            int j1 = strmatcher.isMatch(achar, i1, i, l);

            if (j1 == 0) {
                ++i1;
            } else if (i1 > i && achar[i1 - 1] == c0) {
                stringbuilder.deleteCharAt(i1 - 1);
                achar = this.getChars(stringbuilder);
                --k;
                flag1 = true;
                --l;
            } else {
                int k1 = i1;

                i1 += j1;
                boolean flag2 = false;
                int l1 = 0;

                while (i1 < l) {
                    int i2;

                    if (this.isEnableSubstitutionInVariables() && (i2 = strmatcher.isMatch(achar, i1, i, l)) != 0) {
                        ++l1;
                        i1 += i2;
                    } else {
                        i2 = strmatcher1.isMatch(achar, i1, i, l);
                        if (i2 == 0) {
                            ++i1;
                        } else {
                            if (l1 == 0) {
                                String s = new String(achar, k1 + j1, i1 - k1 - j1);

                                if (this.isEnableSubstitutionInVariables()) {
                                    StringBuilder stringbuilder1 = new StringBuilder(s);

                                    this.substitute(logevent, stringbuilder1, 0, stringbuilder1.length());
                                    s = stringbuilder1.toString();
                                }

                                i1 += i2;
                                if (list == null) {
                                    list = new ArrayList();
                                    ((List) list).add(new String(achar, i, j));
                                }

                                this.checkCyclicSubstitution(s, (List) list);
                                ((List) list).add(s);
                                String s1 = this.resolveVariable(logevent, s, stringbuilder, k1, i1);

                                if (s1 != null) {
                                    int j2 = s1.length();

                                    stringbuilder.replace(k1, i1, s1);
                                    flag1 = true;
                                    int k2 = this.substitute(logevent, stringbuilder, k1, j2, (List) list);

                                    k2 += j2 - (i1 - k1);
                                    i1 += k2;
                                    l += k2;
                                    k += k2;
                                    achar = this.getChars(stringbuilder);
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

        if (flag) {
            return flag1 ? 1 : 0;
        } else {
            return k;
        }
    }

    private void checkCyclicSubstitution(String s, List list) {
        if (list.contains(s)) {
            StringBuilder stringbuilder = new StringBuilder(256);

            stringbuilder.append("Infinite loop in property interpolation of ");
            stringbuilder.append((String) list.remove(0));
            stringbuilder.append(": ");
            this.appendWithSeparators(stringbuilder, list, "->");
            throw new IllegalStateException(stringbuilder.toString());
        }
    }

    protected String resolveVariable(LogEvent logevent, String s, StringBuilder stringbuilder, int i, int j) {
        StrLookup strlookup = this.getVariableResolver();

        return strlookup == null ? null : strlookup.lookup(logevent, s);
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

    private char[] getChars(StringBuilder stringbuilder) {
        char[] achar = new char[stringbuilder.length()];

        stringbuilder.getChars(0, stringbuilder.length(), achar, 0);
        return achar;
    }

    public void appendWithSeparators(StringBuilder stringbuilder, Iterable iterable, String s) {
        if (iterable != null) {
            s = s == null ? "" : s;
            Iterator iterator = iterable.iterator();

            while (iterator.hasNext()) {
                stringbuilder.append(iterator.next());
                if (iterator.hasNext()) {
                    stringbuilder.append(s);
                }
            }
        }

    }

    public String toString() {
        return "StrSubstitutor(" + this.variableResolver.toString() + ")";
    }
}
