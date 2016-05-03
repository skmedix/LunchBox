package org.apache.commons.lang.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;

public class ExtendedMessageFormat extends MessageFormat {

    private static final long serialVersionUID = -2362048321261811743L;
    private static final int HASH_SEED = 31;
    private static final String DUMMY_PATTERN = "";
    private static final String ESCAPED_QUOTE = "\'\'";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE = '{';
    private static final char QUOTE = '\'';
    private String toPattern;
    private final Map registry;

    public ExtendedMessageFormat(String pattern) {
        this(pattern, Locale.getDefault());
    }

    public ExtendedMessageFormat(String pattern, Locale locale) {
        this(pattern, locale, (Map) null);
    }

    public ExtendedMessageFormat(String pattern, Map registry) {
        this(pattern, Locale.getDefault(), registry);
    }

    public ExtendedMessageFormat(String pattern, Locale locale, Map registry) {
        super("");
        this.setLocale(locale);
        this.registry = registry;
        this.applyPattern(pattern);
    }

    public String toPattern() {
        return this.toPattern;
    }

    public final void applyPattern(String pattern) {
        if (this.registry == null) {
            super.applyPattern(pattern);
            this.toPattern = super.toPattern();
        } else {
            ArrayList foundFormats = new ArrayList();
            ArrayList foundDescriptions = new ArrayList();
            StrBuilder stripCustom = new StrBuilder(pattern.length());
            ParsePosition pos = new ParsePosition(0);
            char[] c = pattern.toCharArray();
            int fmtCount = 0;

            int i;

            while (pos.getIndex() < pattern.length()) {
                switch (c[pos.getIndex()]) {
                case '\'':
                    this.appendQuotedString(pattern, pos, stripCustom, true);
                    break;

                case '{':
                    ++fmtCount;
                    this.seekNonWs(pattern, pos);
                    int origFormats = pos.getIndex();

                    i = this.readArgumentIndex(pattern, this.next(pos));
                    stripCustom.append('{').append(i);
                    this.seekNonWs(pattern, pos);
                    Format it = null;
                    String f = null;

                    if (c[pos.getIndex()] == 44) {
                        f = this.parseFormatDescription(pattern, this.next(pos));
                        it = this.getFormat(f);
                        if (it == null) {
                            stripCustom.append(',').append(f);
                        }
                    }

                    foundFormats.add(it);
                    foundDescriptions.add(it == null ? null : f);
                    Validate.isTrue(foundFormats.size() == fmtCount);
                    Validate.isTrue(foundDescriptions.size() == fmtCount);
                    if (c[pos.getIndex()] != 125) {
                        throw new IllegalArgumentException("Unreadable format element at position " + origFormats);
                    }

                default:
                    stripCustom.append(c[pos.getIndex()]);
                    this.next(pos);
                }
            }

            super.applyPattern(stripCustom.toString());
            this.toPattern = this.insertFormats(super.toPattern(), foundDescriptions);
            if (this.containsElements(foundFormats)) {
                Format[] aformat = this.getFormats();

                i = 0;

                for (Iterator iterator = foundFormats.iterator(); iterator.hasNext(); ++i) {
                    Format format = (Format) iterator.next();

                    if (format != null) {
                        aformat[i] = format;
                    }
                }

                super.setFormats(aformat);
            }

        }
    }

    public void setFormat(int formatElementIndex, Format newFormat) {
        throw new UnsupportedOperationException();
    }

    public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
        throw new UnsupportedOperationException();
    }

    public void setFormats(Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    public void setFormatsByArgumentIndex(Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!super.equals(obj)) {
            return false;
        } else if (ObjectUtils.notEqual(this.getClass(), obj.getClass())) {
            return false;
        } else {
            ExtendedMessageFormat rhs = (ExtendedMessageFormat) obj;

            return ObjectUtils.notEqual(this.toPattern, rhs.toPattern) ? false : !ObjectUtils.notEqual(this.registry, rhs.registry);
        }
    }

    public int hashCode() {
        int result = super.hashCode();

        result = 31 * result + ObjectUtils.hashCode(this.registry);
        result = 31 * result + ObjectUtils.hashCode(this.toPattern);
        return result;
    }

    private Format getFormat(String desc) {
        if (this.registry != null) {
            String name = desc;
            String args = null;
            int i = desc.indexOf(44);

            if (i > 0) {
                name = desc.substring(0, i).trim();
                args = desc.substring(i + 1).trim();
            }

            FormatFactory factory = (FormatFactory) this.registry.get(name);

            if (factory != null) {
                return factory.getFormat(name, args, this.getLocale());
            }
        }

        return null;
    }

    private int readArgumentIndex(String pattern, ParsePosition pos) {
        int start = pos.getIndex();

        this.seekNonWs(pattern, pos);
        StrBuilder result = new StrBuilder();

        boolean error;

        for (error = false; !error && pos.getIndex() < pattern.length(); this.next(pos)) {
            char c = pattern.charAt(pos.getIndex());

            if (Character.isWhitespace(c)) {
                this.seekNonWs(pattern, pos);
                c = pattern.charAt(pos.getIndex());
                if (c != 44 && c != 125) {
                    error = true;
                    continue;
                }
            }

            if ((c == 44 || c == 125) && result.length() > 0) {
                try {
                    return Integer.parseInt(result.toString());
                } catch (NumberFormatException numberformatexception) {
                    ;
                }
            }

            error = !Character.isDigit(c);
            result.append(c);
        }

        if (error) {
            throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern.substring(start, pos.getIndex()));
        } else {
            throw new IllegalArgumentException("Unterminated format element at position " + start);
        }
    }

    private String parseFormatDescription(String pattern, ParsePosition pos) {
        int start = pos.getIndex();

        this.seekNonWs(pattern, pos);
        int text = pos.getIndex();

        for (int depth = 1; pos.getIndex() < pattern.length(); this.next(pos)) {
            switch (pattern.charAt(pos.getIndex())) {
            case '\'':
                this.getQuotedString(pattern, pos, false);
                break;

            case '{':
                ++depth;
                break;

            case '}':
                --depth;
                if (depth == 0) {
                    return pattern.substring(text, pos.getIndex());
                }
            }
        }

        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }

    private String insertFormats(String pattern, ArrayList customPatterns) {
        if (!this.containsElements(customPatterns)) {
            return pattern;
        } else {
            StrBuilder sb = new StrBuilder(pattern.length() * 2);
            ParsePosition pos = new ParsePosition(0);
            int fe = -1;
            int depth = 0;

            while (pos.getIndex() < pattern.length()) {
                char c = pattern.charAt(pos.getIndex());

                switch (c) {
                case '\'':
                    this.appendQuotedString(pattern, pos, sb, false);
                    break;

                case '{':
                    ++depth;
                    if (depth == 1) {
                        ++fe;
                        sb.append('{').append(this.readArgumentIndex(pattern, this.next(pos)));
                        String customPattern = (String) customPatterns.get(fe);

                        if (customPattern != null) {
                            sb.append(',').append(customPattern);
                        }
                    }
                    break;

                case '}':
                    --depth;

                default:
                    sb.append(c);
                    this.next(pos);
                }
            }

            return sb.toString();
        }
    }

    private void seekNonWs(String pattern, ParsePosition pos) {
        boolean len = false;
        char[] buffer = pattern.toCharArray();

        int len1;

        do {
            len1 = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
            pos.setIndex(pos.getIndex() + len1);
        } while (len1 > 0 && pos.getIndex() < pattern.length());

    }

    private ParsePosition next(ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
        return pos;
    }

    private StrBuilder appendQuotedString(String pattern, ParsePosition pos, StrBuilder appendTo, boolean escapingOn) {
        int start = pos.getIndex();
        char[] c = pattern.toCharArray();

        if (escapingOn && c[start] == 39) {
            this.next(pos);
            return appendTo == null ? null : appendTo.append('\'');
        } else {
            int lastHold = start;

            for (int i = pos.getIndex(); i < pattern.length(); ++i) {
                if (escapingOn && pattern.substring(i).startsWith("\'\'")) {
                    appendTo.append(c, lastHold, pos.getIndex() - lastHold).append('\'');
                    pos.setIndex(i + "\'\'".length());
                    lastHold = pos.getIndex();
                } else {
                    switch (c[pos.getIndex()]) {
                    case '\'':
                        this.next(pos);
                        return appendTo == null ? null : appendTo.append(c, lastHold, pos.getIndex() - lastHold);

                    default:
                        this.next(pos);
                    }
                }
            }

            throw new IllegalArgumentException("Unterminated quoted string at position " + start);
        }
    }

    private void getQuotedString(String pattern, ParsePosition pos, boolean escapingOn) {
        this.appendQuotedString(pattern, pos, (StrBuilder) null, escapingOn);
    }

    private boolean containsElements(Collection coll) {
        if (coll != null && coll.size() != 0) {
            Iterator iter = coll.iterator();

            do {
                if (!iter.hasNext()) {
                    return false;
                }
            } while (iter.next() == null);

            return true;
        } else {
            return false;
        }
    }
}
