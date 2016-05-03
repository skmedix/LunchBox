package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StrTokenizer implements ListIterator, Cloneable {

    private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
    private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE;
    private char[] chars;
    private String[] tokens;
    private int tokenPos;
    private StrMatcher delimMatcher;
    private StrMatcher quoteMatcher;
    private StrMatcher ignoredMatcher;
    private StrMatcher trimmerMatcher;
    private boolean emptyAsNull;
    private boolean ignoreEmptyTokens;

    private static StrTokenizer getCSVClone() {
        return (StrTokenizer) StrTokenizer.CSV_TOKENIZER_PROTOTYPE.clone();
    }

    public static StrTokenizer getCSVInstance() {
        return getCSVClone();
    }

    public static StrTokenizer getCSVInstance(String s) {
        StrTokenizer strtokenizer = getCSVClone();

        strtokenizer.reset(s);
        return strtokenizer;
    }

    public static StrTokenizer getCSVInstance(char[] achar) {
        StrTokenizer strtokenizer = getCSVClone();

        strtokenizer.reset(achar);
        return strtokenizer;
    }

    private static StrTokenizer getTSVClone() {
        return (StrTokenizer) StrTokenizer.TSV_TOKENIZER_PROTOTYPE.clone();
    }

    public static StrTokenizer getTSVInstance() {
        return getTSVClone();
    }

    public static StrTokenizer getTSVInstance(String s) {
        StrTokenizer strtokenizer = getTSVClone();

        strtokenizer.reset(s);
        return strtokenizer;
    }

    public static StrTokenizer getTSVInstance(char[] achar) {
        StrTokenizer strtokenizer = getTSVClone();

        strtokenizer.reset(achar);
        return strtokenizer;
    }

    public StrTokenizer() {
        this.delimMatcher = StrMatcher.splitMatcher();
        this.quoteMatcher = StrMatcher.noneMatcher();
        this.ignoredMatcher = StrMatcher.noneMatcher();
        this.trimmerMatcher = StrMatcher.noneMatcher();
        this.emptyAsNull = false;
        this.ignoreEmptyTokens = true;
        this.chars = null;
    }

    public StrTokenizer(String s) {
        this.delimMatcher = StrMatcher.splitMatcher();
        this.quoteMatcher = StrMatcher.noneMatcher();
        this.ignoredMatcher = StrMatcher.noneMatcher();
        this.trimmerMatcher = StrMatcher.noneMatcher();
        this.emptyAsNull = false;
        this.ignoreEmptyTokens = true;
        if (s != null) {
            this.chars = s.toCharArray();
        } else {
            this.chars = null;
        }

    }

    public StrTokenizer(String s, char c0) {
        this(s);
        this.setDelimiterChar(c0);
    }

    public StrTokenizer(String s, String s1) {
        this(s);
        this.setDelimiterString(s1);
    }

    public StrTokenizer(String s, StrMatcher strmatcher) {
        this(s);
        this.setDelimiterMatcher(strmatcher);
    }

    public StrTokenizer(String s, char c0, char c1) {
        this(s, c0);
        this.setQuoteChar(c1);
    }

    public StrTokenizer(String s, StrMatcher strmatcher, StrMatcher strmatcher1) {
        this(s, strmatcher);
        this.setQuoteMatcher(strmatcher1);
    }

    public StrTokenizer(char[] achar) {
        this.delimMatcher = StrMatcher.splitMatcher();
        this.quoteMatcher = StrMatcher.noneMatcher();
        this.ignoredMatcher = StrMatcher.noneMatcher();
        this.trimmerMatcher = StrMatcher.noneMatcher();
        this.emptyAsNull = false;
        this.ignoreEmptyTokens = true;
        this.chars = ArrayUtils.clone(achar);
    }

    public StrTokenizer(char[] achar, char c0) {
        this(achar);
        this.setDelimiterChar(c0);
    }

    public StrTokenizer(char[] achar, String s) {
        this(achar);
        this.setDelimiterString(s);
    }

    public StrTokenizer(char[] achar, StrMatcher strmatcher) {
        this(achar);
        this.setDelimiterMatcher(strmatcher);
    }

    public StrTokenizer(char[] achar, char c0, char c1) {
        this(achar, c0);
        this.setQuoteChar(c1);
    }

    public StrTokenizer(char[] achar, StrMatcher strmatcher, StrMatcher strmatcher1) {
        this(achar, strmatcher);
        this.setQuoteMatcher(strmatcher1);
    }

    public int size() {
        this.checkTokenized();
        return this.tokens.length;
    }

    public String nextToken() {
        return this.hasNext() ? this.tokens[this.tokenPos++] : null;
    }

    public String previousToken() {
        return this.hasPrevious() ? this.tokens[--this.tokenPos] : null;
    }

    public String[] getTokenArray() {
        this.checkTokenized();
        return (String[]) this.tokens.clone();
    }

    public List getTokenList() {
        this.checkTokenized();
        ArrayList arraylist = new ArrayList(this.tokens.length);
        String[] astring = this.tokens;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            arraylist.add(s);
        }

        return arraylist;
    }

    public StrTokenizer reset() {
        this.tokenPos = 0;
        this.tokens = null;
        return this;
    }

    public StrTokenizer reset(String s) {
        this.reset();
        if (s != null) {
            this.chars = s.toCharArray();
        } else {
            this.chars = null;
        }

        return this;
    }

    public StrTokenizer reset(char[] achar) {
        this.reset();
        this.chars = ArrayUtils.clone(achar);
        return this;
    }

    public boolean hasNext() {
        this.checkTokenized();
        return this.tokenPos < this.tokens.length;
    }

    public String next() {
        if (this.hasNext()) {
            return this.tokens[this.tokenPos++];
        } else {
            throw new NoSuchElementException();
        }
    }

    public int nextIndex() {
        return this.tokenPos;
    }

    public boolean hasPrevious() {
        this.checkTokenized();
        return this.tokenPos > 0;
    }

    public String previous() {
        if (this.hasPrevious()) {
            return this.tokens[--this.tokenPos];
        } else {
            throw new NoSuchElementException();
        }
    }

    public int previousIndex() {
        return this.tokenPos - 1;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove() is unsupported");
    }

    public void set(String s) {
        throw new UnsupportedOperationException("set() is unsupported");
    }

    public void add(String s) {
        throw new UnsupportedOperationException("add() is unsupported");
    }

    private void checkTokenized() {
        if (this.tokens == null) {
            List list;

            if (this.chars == null) {
                list = this.tokenize((char[]) null, 0, 0);
                this.tokens = (String[]) list.toArray(new String[list.size()]);
            } else {
                list = this.tokenize(this.chars, 0, this.chars.length);
                this.tokens = (String[]) list.toArray(new String[list.size()]);
            }
        }

    }

    protected List tokenize(char[] achar, int i, int j) {
        if (achar != null && j != 0) {
            StrBuilder strbuilder = new StrBuilder();
            ArrayList arraylist = new ArrayList();
            int k = i;

            while (k >= 0 && k < j) {
                k = this.readNextToken(achar, k, j, strbuilder, arraylist);
                if (k >= j) {
                    this.addToken(arraylist, "");
                }
            }

            return arraylist;
        } else {
            return Collections.emptyList();
        }
    }

    private void addToken(List list, String s) {
        if (StringUtils.isEmpty(s)) {
            if (this.isIgnoreEmptyTokens()) {
                return;
            }

            if (this.isEmptyTokenAsNull()) {
                s = null;
            }
        }

        list.add(s);
    }

    private int readNextToken(char[] achar, int i, int j, StrBuilder strbuilder, List list) {
        while (true) {
            int k;

            if (i < j) {
                k = Math.max(this.getIgnoredMatcher().isMatch(achar, i, i, j), this.getTrimmerMatcher().isMatch(achar, i, i, j));
                if (k != 0 && this.getDelimiterMatcher().isMatch(achar, i, i, j) <= 0 && this.getQuoteMatcher().isMatch(achar, i, i, j) <= 0) {
                    i += k;
                    continue;
                }
            }

            if (i >= j) {
                this.addToken(list, "");
                return -1;
            }

            k = this.getDelimiterMatcher().isMatch(achar, i, i, j);
            if (k > 0) {
                this.addToken(list, "");
                return i + k;
            }

            int l = this.getQuoteMatcher().isMatch(achar, i, i, j);

            if (l > 0) {
                return this.readWithQuotes(achar, i + l, j, strbuilder, list, i, l);
            }

            return this.readWithQuotes(achar, i, j, strbuilder, list, 0, 0);
        }
    }

    private int readWithQuotes(char[] achar, int i, int j, StrBuilder strbuilder, List list, int k, int l) {
        strbuilder.clear();
        int i1 = i;
        boolean flag = l > 0;
        int j1 = 0;

        while (i1 < j) {
            if (flag) {
                if (this.isQuote(achar, i1, j, k, l)) {
                    if (this.isQuote(achar, i1 + l, j, k, l)) {
                        strbuilder.append(achar, i1, l);
                        i1 += l * 2;
                        j1 = strbuilder.size();
                    } else {
                        flag = false;
                        i1 += l;
                    }
                } else {
                    strbuilder.append(achar[i1++]);
                    j1 = strbuilder.size();
                }
            } else {
                int k1 = this.getDelimiterMatcher().isMatch(achar, i1, i, j);

                if (k1 > 0) {
                    this.addToken(list, strbuilder.substring(0, j1));
                    return i1 + k1;
                }

                if (l > 0 && this.isQuote(achar, i1, j, k, l)) {
                    flag = true;
                    i1 += l;
                } else {
                    int l1 = this.getIgnoredMatcher().isMatch(achar, i1, i, j);

                    if (l1 > 0) {
                        i1 += l1;
                    } else {
                        int i2 = this.getTrimmerMatcher().isMatch(achar, i1, i, j);

                        if (i2 > 0) {
                            strbuilder.append(achar, i1, i2);
                            i1 += i2;
                        } else {
                            strbuilder.append(achar[i1++]);
                            j1 = strbuilder.size();
                        }
                    }
                }
            }
        }

        this.addToken(list, strbuilder.substring(0, j1));
        return -1;
    }

    private boolean isQuote(char[] achar, int i, int j, int k, int l) {
        for (int i1 = 0; i1 < l; ++i1) {
            if (i + i1 >= j || achar[i + i1] != achar[k + i1]) {
                return false;
            }
        }

        return true;
    }

    public StrMatcher getDelimiterMatcher() {
        return this.delimMatcher;
    }

    public StrTokenizer setDelimiterMatcher(StrMatcher strmatcher) {
        if (strmatcher == null) {
            this.delimMatcher = StrMatcher.noneMatcher();
        } else {
            this.delimMatcher = strmatcher;
        }

        return this;
    }

    public StrTokenizer setDelimiterChar(char c0) {
        return this.setDelimiterMatcher(StrMatcher.charMatcher(c0));
    }

    public StrTokenizer setDelimiterString(String s) {
        return this.setDelimiterMatcher(StrMatcher.stringMatcher(s));
    }

    public StrMatcher getQuoteMatcher() {
        return this.quoteMatcher;
    }

    public StrTokenizer setQuoteMatcher(StrMatcher strmatcher) {
        if (strmatcher != null) {
            this.quoteMatcher = strmatcher;
        }

        return this;
    }

    public StrTokenizer setQuoteChar(char c0) {
        return this.setQuoteMatcher(StrMatcher.charMatcher(c0));
    }

    public StrMatcher getIgnoredMatcher() {
        return this.ignoredMatcher;
    }

    public StrTokenizer setIgnoredMatcher(StrMatcher strmatcher) {
        if (strmatcher != null) {
            this.ignoredMatcher = strmatcher;
        }

        return this;
    }

    public StrTokenizer setIgnoredChar(char c0) {
        return this.setIgnoredMatcher(StrMatcher.charMatcher(c0));
    }

    public StrMatcher getTrimmerMatcher() {
        return this.trimmerMatcher;
    }

    public StrTokenizer setTrimmerMatcher(StrMatcher strmatcher) {
        if (strmatcher != null) {
            this.trimmerMatcher = strmatcher;
        }

        return this;
    }

    public boolean isEmptyTokenAsNull() {
        return this.emptyAsNull;
    }

    public StrTokenizer setEmptyTokenAsNull(boolean flag) {
        this.emptyAsNull = flag;
        return this;
    }

    public boolean isIgnoreEmptyTokens() {
        return this.ignoreEmptyTokens;
    }

    public StrTokenizer setIgnoreEmptyTokens(boolean flag) {
        this.ignoreEmptyTokens = flag;
        return this;
    }

    public String getContent() {
        return this.chars == null ? null : new String(this.chars);
    }

    public Object clone() {
        try {
            return this.cloneReset();
        } catch (CloneNotSupportedException clonenotsupportedexception) {
            return null;
        }
    }

    Object cloneReset() throws CloneNotSupportedException {
        StrTokenizer strtokenizer = (StrTokenizer) super.clone();

        if (strtokenizer.chars != null) {
            strtokenizer.chars = (char[]) strtokenizer.chars.clone();
        }

        strtokenizer.reset();
        return strtokenizer;
    }

    public String toString() {
        return this.tokens == null ? "StrTokenizer[not tokenized yet]" : "StrTokenizer" + this.getTokenList();
    }

    static {
        StrTokenizer.CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
        StrTokenizer.CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
        StrTokenizer.CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
        StrTokenizer.CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
        StrTokenizer.CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
        StrTokenizer.CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
        TSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
        StrTokenizer.TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
        StrTokenizer.TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
        StrTokenizer.TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
        StrTokenizer.TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
        StrTokenizer.TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
        StrTokenizer.TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
    }
}
