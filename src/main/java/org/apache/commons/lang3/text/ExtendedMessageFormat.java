package org.apache.commons.lang3.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

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

    public ExtendedMessageFormat(String s) {
        this(s, Locale.getDefault());
    }

    public ExtendedMessageFormat(String s, Locale locale) {
        this(s, locale, (Map) null);
    }

    public ExtendedMessageFormat(String s, Map map) {
        this(s, Locale.getDefault(), map);
    }

    public ExtendedMessageFormat(String s, Locale locale, Map map) {
        super("");
        this.setLocale(locale);
        this.registry = map;
        this.applyPattern(s);
    }

    public String toPattern() {
        return this.toPattern;
    }

    public final void applyPattern(String s) {
        if (this.registry == null) {
            super.applyPattern(s);
            this.toPattern = super.toPattern();
        } else {
            ArrayList arraylist = new ArrayList();
            ArrayList arraylist1 = new ArrayList();
            StringBuilder stringbuilder = new StringBuilder(s.length());
            ParsePosition parseposition = new ParsePosition(0);
            char[] achar = s.toCharArray();
            int i = 0;

            int j;

            while (parseposition.getIndex() < s.length()) {
                switch (achar[parseposition.getIndex()]) {
                case '\'':
                    this.appendQuotedString(s, parseposition, stringbuilder, true);
                    break;

                case '{':
                    ++i;
                    this.seekNonWs(s, parseposition);
                    int k = parseposition.getIndex();

                    j = this.readArgumentIndex(s, this.next(parseposition));
                    stringbuilder.append('{').append(j);
                    this.seekNonWs(s, parseposition);
                    Format format = null;
                    String s1 = null;

                    if (achar[parseposition.getIndex()] == 44) {
                        s1 = this.parseFormatDescription(s, this.next(parseposition));
                        format = this.getFormat(s1);
                        if (format == null) {
                            stringbuilder.append(',').append(s1);
                        }
                    }

                    arraylist.add(format);
                    arraylist1.add(format == null ? null : s1);
                    Validate.isTrue(arraylist.size() == i);
                    Validate.isTrue(arraylist1.size() == i);
                    if (achar[parseposition.getIndex()] != 125) {
                        throw new IllegalArgumentException("Unreadable format element at position " + k);
                    }

                default:
                    stringbuilder.append(achar[parseposition.getIndex()]);
                    this.next(parseposition);
                }
            }

            super.applyPattern(stringbuilder.toString());
            this.toPattern = this.insertFormats(super.toPattern(), arraylist1);
            if (this.containsElements(arraylist)) {
                Format[] aformat = this.getFormats();

                j = 0;

                for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); ++j) {
                    Format format1 = (Format) iterator.next();

                    if (format1 != null) {
                        aformat[j] = format1;
                    }
                }

                super.setFormats(aformat);
            }

        }
    }

    public void setFormat(int i, Format format) {
        throw new UnsupportedOperationException();
    }

    public void setFormatByArgumentIndex(int i, Format format) {
        throw new UnsupportedOperationException();
    }

    public void setFormats(Format[] aformat) {
        throw new UnsupportedOperationException();
    }

    public void setFormatsByArgumentIndex(Format[] aformat) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (object == null) {
            return false;
        } else if (!super.equals(object)) {
            return false;
        } else if (ObjectUtils.notEqual(this.getClass(), object.getClass())) {
            return false;
        } else {
            ExtendedMessageFormat extendedmessageformat = (ExtendedMessageFormat) object;

            return ObjectUtils.notEqual(this.toPattern, extendedmessageformat.toPattern) ? false : !ObjectUtils.notEqual(this.registry, extendedmessageformat.registry);
        }
    }

    public int hashCode() {
        int i = super.hashCode();

        i = 31 * i + ObjectUtils.hashCode(this.registry);
        i = 31 * i + ObjectUtils.hashCode(this.toPattern);
        return i;
    }

    private Format getFormat(String s) {
        if (this.registry != null) {
            String s1 = s;
            String s2 = null;
            int i = s.indexOf(44);

            if (i > 0) {
                s1 = s.substring(0, i).trim();
                s2 = s.substring(i + 1).trim();
            }

            FormatFactory formatfactory = (FormatFactory) this.registry.get(s1);

            if (formatfactory != null) {
                return formatfactory.getFormat(s1, s2, this.getLocale());
            }
        }

        return null;
    }

    private int readArgumentIndex(String s, ParsePosition parseposition) {
        int i = parseposition.getIndex();

        this.seekNonWs(s, parseposition);
        StringBuilder stringbuilder = new StringBuilder();

        boolean flag;

        for (flag = false; !flag && parseposition.getIndex() < s.length(); this.next(parseposition)) {
            char c0 = s.charAt(parseposition.getIndex());

            if (Character.isWhitespace(c0)) {
                this.seekNonWs(s, parseposition);
                c0 = s.charAt(parseposition.getIndex());
                if (c0 != 44 && c0 != 125) {
                    flag = true;
                    continue;
                }
            }

            if ((c0 == 44 || c0 == 125) && stringbuilder.length() > 0) {
                try {
                    return Integer.parseInt(stringbuilder.toString());
                } catch (NumberFormatException numberformatexception) {
                    ;
                }
            }

            flag = !Character.isDigit(c0);
            stringbuilder.append(c0);
        }

        if (flag) {
            throw new IllegalArgumentException("Invalid format argument index at position " + i + ": " + s.substring(i, parseposition.getIndex()));
        } else {
            throw new IllegalArgumentException("Unterminated format element at position " + i);
        }
    }

    private String parseFormatDescription(String s, ParsePosition parseposition) {
        int i = parseposition.getIndex();

        this.seekNonWs(s, parseposition);
        int j = parseposition.getIndex();

        for (int k = 1; parseposition.getIndex() < s.length(); this.next(parseposition)) {
            switch (s.charAt(parseposition.getIndex())) {
            case '\'':
                this.getQuotedString(s, parseposition, false);
                break;

            case '{':
                ++k;
                break;

            case '}':
                --k;
                if (k == 0) {
                    return s.substring(j, parseposition.getIndex());
                }
            }
        }

        throw new IllegalArgumentException("Unterminated format element at position " + i);
    }

    private String insertFormats(String s, ArrayList arraylist) {
        if (!this.containsElements(arraylist)) {
            return s;
        } else {
            StringBuilder stringbuilder = new StringBuilder(s.length() * 2);
            ParsePosition parseposition = new ParsePosition(0);
            int i = -1;
            int j = 0;

            while (parseposition.getIndex() < s.length()) {
                char c0 = s.charAt(parseposition.getIndex());

                switch (c0) {
                case '\'':
                    this.appendQuotedString(s, parseposition, stringbuilder, false);
                    break;

                case '{':
                    ++j;
                    stringbuilder.append('{').append(this.readArgumentIndex(s, this.next(parseposition)));
                    if (j == 1) {
                        ++i;
                        String s1 = (String) arraylist.get(i);

                        if (s1 != null) {
                            stringbuilder.append(',').append(s1);
                        }
                    }
                    break;

                case '}':
                    --j;

                default:
                    stringbuilder.append(c0);
                    this.next(parseposition);
                }
            }

            return stringbuilder.toString();
        }
    }

    private void seekNonWs(String s, ParsePosition parseposition) {
        boolean flag = false;
        char[] achar = s.toCharArray();

        int i;

        do {
            i = StrMatcher.splitMatcher().isMatch(achar, parseposition.getIndex());
            parseposition.setIndex(parseposition.getIndex() + i);
        } while (i > 0 && parseposition.getIndex() < s.length());

    }

    private ParsePosition next(ParsePosition parseposition) {
        parseposition.setIndex(parseposition.getIndex() + 1);
        return parseposition;
    }

    private StringBuilder appendQuotedString(String s, ParsePosition parseposition, StringBuilder stringbuilder, boolean flag) {
        int i = parseposition.getIndex();
        char[] achar = s.toCharArray();

        if (flag && achar[i] == 39) {
            this.next(parseposition);
            return stringbuilder == null ? null : stringbuilder.append('\'');
        } else {
            int j = i;

            for (int k = parseposition.getIndex(); k < s.length(); ++k) {
                if (flag && s.substring(k).startsWith("\'\'")) {
                    stringbuilder.append(achar, j, parseposition.getIndex() - j).append('\'');
                    parseposition.setIndex(k + "\'\'".length());
                    j = parseposition.getIndex();
                } else {
                    switch (achar[parseposition.getIndex()]) {
                    case '\'':
                        this.next(parseposition);
                        return stringbuilder == null ? null : stringbuilder.append(achar, j, parseposition.getIndex() - j);

                    default:
                        this.next(parseposition);
                    }
                }
            }

            throw new IllegalArgumentException("Unterminated quoted string at position " + i);
        }
    }

    private void getQuotedString(String s, ParsePosition parseposition, boolean flag) {
        this.appendQuotedString(s, parseposition, (StringBuilder) null, flag);
    }

    private boolean containsElements(Collection collection) {
        if (collection != null && !collection.isEmpty()) {
            Iterator iterator = collection.iterator();

            Object object;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                object = iterator.next();
            } while (object == null);

            return true;
        } else {
            return false;
        }
    }
}
