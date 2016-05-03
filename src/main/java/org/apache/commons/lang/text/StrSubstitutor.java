package org.apache.commons.lang.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class StrSubstitutor {

    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private StrMatcher suffixMatcher;
    private StrLookup variableResolver;
    private boolean enableSubstitutionInVariables;

    public static String replace(Object source, Map valueMap) {
        return (new StrSubstitutor(valueMap)).replace(source);
    }

    public static String replace(Object source, Map valueMap, String prefix, String suffix) {
        return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
    }

    public static String replace(Object source, Properties valueProperties) {
        if (valueProperties == null) {
            return source.toString();
        } else {
            HashMap valueMap = new HashMap();
            Enumeration propNames = valueProperties.propertyNames();

            while (propNames.hasMoreElements()) {
                String propName = (String) propNames.nextElement();
                String propValue = valueProperties.getProperty(propName);

                valueMap.put(propName, propValue);
            }

            return replace(source, (Map) valueMap);
        }
    }

    public static String replaceSystemProperties(Object source) {
        return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(source);
    }

    public StrSubstitutor() {
        this((StrLookup) null, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map valueMap) {
        this(StrLookup.mapLookup(valueMap), StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map valueMap, String prefix, String suffix) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, '$');
    }

    public StrSubstitutor(Map valueMap, String prefix, String suffix, char escape) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
    }

    public StrSubstitutor(StrLookup strlookup) {
        this(strlookup, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(StrLookup strlookup, String prefix, String suffix, char escape) {
        this.setVariableResolver(strlookup);
        this.setVariablePrefix(prefix);
        this.setVariableSuffix(suffix);
        this.setEscapeChar(escape);
    }

    public StrSubstitutor(StrLookup strlookup, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
        this.setVariableResolver(strlookup);
        this.setVariablePrefixMatcher(prefixMatcher);
        this.setVariableSuffixMatcher(suffixMatcher);
        this.setEscapeChar(escape);
    }

    public String replace(String source) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = new StrBuilder(source);

            return !this.substitute(buf, 0, source.length()) ? source : buf.toString();
        }
    }

    public String replace(String source, int offset, int length) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);

            return !this.substitute(buf, 0, length) ? source.substring(offset, offset + length) : buf.toString();
        }
    }

    public String replace(char[] source) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder(source.length)).append(source);

            this.substitute(buf, 0, source.length);
            return buf.toString();
        }
    }

    public String replace(char[] source, int offset, int length) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);

            this.substitute(buf, 0, length);
            return buf.toString();
        }
    }

    public String replace(StringBuffer source) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder(source.length())).append(source);

            this.substitute(buf, 0, buf.length());
            return buf.toString();
        }
    }

    public String replace(StringBuffer source, int offset, int length) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);

            this.substitute(buf, 0, length);
            return buf.toString();
        }
    }

    public String replace(StrBuilder source) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder(source.length())).append(source);

            this.substitute(buf, 0, buf.length());
            return buf.toString();
        }
    }

    public String replace(StrBuilder source, int offset, int length) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);

            this.substitute(buf, 0, length);
            return buf.toString();
        }
    }

    public String replace(Object source) {
        if (source == null) {
            return null;
        } else {
            StrBuilder buf = (new StrBuilder()).append(source);

            this.substitute(buf, 0, buf.length());
            return buf.toString();
        }
    }

    public boolean replaceIn(StringBuffer source) {
        return source == null ? false : this.replaceIn(source, 0, source.length());
    }

    public boolean replaceIn(StringBuffer source, int offset, int length) {
        if (source == null) {
            return false;
        } else {
            StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);

            if (!this.substitute(buf, 0, length)) {
                return false;
            } else {
                source.replace(offset, offset + length, buf.toString());
                return true;
            }
        }
    }

    public boolean replaceIn(StrBuilder source) {
        return source == null ? false : this.substitute(source, 0, source.length());
    }

    public boolean replaceIn(StrBuilder source, int offset, int length) {
        return source == null ? false : this.substitute(source, offset, length);
    }

    protected boolean substitute(StrBuilder buf, int offset, int length) {
        return this.substitute(buf, offset, length, (List) null) > 0;
    }

    private int substitute(StrBuilder buf, int offset, int length, List priorVariables) {
        StrMatcher prefixMatcher = this.getVariablePrefixMatcher();
        StrMatcher suffixMatcher = this.getVariableSuffixMatcher();
        char escape = this.getEscapeChar();
        boolean top = priorVariables == null;
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = buf.buffer;
        int bufEnd = offset + length;
        int pos = offset;

        while (pos < bufEnd) {
            int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);

            if (startMatchLen == 0) {
                ++pos;
            } else if (pos > offset && chars[pos - 1] == escape) {
                buf.deleteCharAt(pos - 1);
                chars = buf.buffer;
                --lengthChange;
                altered = true;
                --bufEnd;
            } else {
                int startPos = pos;

                pos += startMatchLen;
                boolean endMatchLen = false;
                int nestedVarCount = 0;

                while (pos < bufEnd) {
                    int i;

                    if (this.isEnableSubstitutionInVariables() && (i = prefixMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
                        ++nestedVarCount;
                        pos += i;
                    } else {
                        i = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
                        if (i == 0) {
                            ++pos;
                        } else {
                            if (nestedVarCount == 0) {
                                String s = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);

                                if (this.isEnableSubstitutionInVariables()) {
                                    StrBuilder endPos = new StrBuilder(s);

                                    this.substitute(endPos, 0, endPos.length());
                                    s = endPos.toString();
                                }

                                pos += i;
                                if (priorVariables == null) {
                                    priorVariables = new ArrayList();
                                    ((List) priorVariables).add(new String(chars, offset, length));
                                }

                                this.checkCyclicSubstitution(s, (List) priorVariables);
                                ((List) priorVariables).add(s);
                                String s1 = this.resolveVariable(s, buf, startPos, pos);

                                if (s1 != null) {
                                    int j = s1.length();

                                    buf.replace(startPos, pos, s1);
                                    altered = true;
                                    int change = this.substitute(buf, startPos, j, (List) priorVariables);

                                    change += j - (pos - startPos);
                                    pos += change;
                                    bufEnd += change;
                                    lengthChange += change;
                                    chars = buf.buffer;
                                }

                                ((List) priorVariables).remove(((List) priorVariables).size() - 1);
                                break;
                            }

                            --nestedVarCount;
                            pos += i;
                        }
                    }
                }
            }
        }

        if (top) {
            return altered ? 1 : 0;
        } else {
            return lengthChange;
        }
    }

    private void checkCyclicSubstitution(String s, List priorVariables) {
        if (priorVariables.contains(s)) {
            StrBuilder buf = new StrBuilder(256);

            buf.append("Infinite loop in property interpolation of ");
            buf.append(priorVariables.remove(0));
            buf.append(": ");
            buf.appendWithSeparators((Collection) priorVariables, "->");
            throw new IllegalStateException(buf.toString());
        }
    }

    protected String resolveVariable(String s, StrBuilder buf, int startPos, int endPos) {
        StrLookup resolver = this.getVariableResolver();

        return resolver == null ? null : resolver.lookup(s);
    }

    public char getEscapeChar() {
        return this.escapeChar;
    }

    public void setEscapeChar(char escapeCharacter) {
        this.escapeChar = escapeCharacter;
    }

    public StrMatcher getVariablePrefixMatcher() {
        return this.prefixMatcher;
    }

    public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
        if (prefixMatcher == null) {
            throw new IllegalArgumentException("Variable prefix matcher must not be null!");
        } else {
            this.prefixMatcher = prefixMatcher;
            return this;
        }
    }

    public StrSubstitutor setVariablePrefix(char prefix) {
        return this.setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
    }

    public StrSubstitutor setVariablePrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Variable prefix must not be null!");
        } else {
            return this.setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
        }
    }

    public StrMatcher getVariableSuffixMatcher() {
        return this.suffixMatcher;
    }

    public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
        if (suffixMatcher == null) {
            throw new IllegalArgumentException("Variable suffix matcher must not be null!");
        } else {
            this.suffixMatcher = suffixMatcher;
            return this;
        }
    }

    public StrSubstitutor setVariableSuffix(char suffix) {
        return this.setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
    }

    public StrSubstitutor setVariableSuffix(String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("Variable suffix must not be null!");
        } else {
            return this.setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
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

    public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
        this.enableSubstitutionInVariables = enableSubstitutionInVariables;
    }
}
