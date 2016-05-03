package org.apache.commons.lang3.text;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.Builder;

public class StrBuilder implements CharSequence, Appendable, Serializable, Builder {

    static final int CAPACITY = 32;
    private static final long serialVersionUID = 7628716375283629643L;
    protected char[] buffer;
    protected int size;
    private String newLine;
    private String nullText;

    public StrBuilder() {
        this(32);
    }

    public StrBuilder(int i) {
        if (i <= 0) {
            i = 32;
        }

        this.buffer = new char[i];
    }

    public StrBuilder(String s) {
        if (s == null) {
            this.buffer = new char[32];
        } else {
            this.buffer = new char[s.length() + 32];
            this.append(s);
        }

    }

    public String getNewLineText() {
        return this.newLine;
    }

    public StrBuilder setNewLineText(String s) {
        this.newLine = s;
        return this;
    }

    public String getNullText() {
        return this.nullText;
    }

    public StrBuilder setNullText(String s) {
        if (s != null && s.isEmpty()) {
            s = null;
        }

        this.nullText = s;
        return this;
    }

    public int length() {
        return this.size;
    }

    public StrBuilder setLength(int i) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        } else {
            if (i < this.size) {
                this.size = i;
            } else if (i > this.size) {
                this.ensureCapacity(i);
                int j = this.size;
                int k = i;

                this.size = i;

                for (int l = j; l < k; ++l) {
                    this.buffer[l] = 0;
                }
            }

            return this;
        }
    }

    public int capacity() {
        return this.buffer.length;
    }

    public StrBuilder ensureCapacity(int i) {
        if (i > this.buffer.length) {
            char[] achar = this.buffer;

            this.buffer = new char[i * 2];
            System.arraycopy(achar, 0, this.buffer, 0, this.size);
        }

        return this;
    }

    public StrBuilder minimizeCapacity() {
        if (this.buffer.length > this.length()) {
            char[] achar = this.buffer;

            this.buffer = new char[this.length()];
            System.arraycopy(achar, 0, this.buffer, 0, this.size);
        }

        return this;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public StrBuilder clear() {
        this.size = 0;
        return this;
    }

    public char charAt(int i) {
        if (i >= 0 && i < this.length()) {
            return this.buffer[i];
        } else {
            throw new StringIndexOutOfBoundsException(i);
        }
    }

    public StrBuilder setCharAt(int i, char c0) {
        if (i >= 0 && i < this.length()) {
            this.buffer[i] = c0;
            return this;
        } else {
            throw new StringIndexOutOfBoundsException(i);
        }
    }

    public StrBuilder deleteCharAt(int i) {
        if (i >= 0 && i < this.size) {
            this.deleteImpl(i, i + 1, 1);
            return this;
        } else {
            throw new StringIndexOutOfBoundsException(i);
        }
    }

    public char[] toCharArray() {
        if (this.size == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        } else {
            char[] achar = new char[this.size];

            System.arraycopy(this.buffer, 0, achar, 0, this.size);
            return achar;
        }
    }

    public char[] toCharArray(int i, int j) {
        j = this.validateRange(i, j);
        int k = j - i;

        if (k == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        } else {
            char[] achar = new char[k];

            System.arraycopy(this.buffer, i, achar, 0, k);
            return achar;
        }
    }

    public char[] getChars(char[] achar) {
        int i = this.length();

        if (achar == null || achar.length < i) {
            achar = new char[i];
        }

        System.arraycopy(this.buffer, 0, achar, 0, i);
        return achar;
    }

    public void getChars(int i, int j, char[] achar, int k) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        } else if (j >= 0 && j <= this.length()) {
            if (i > j) {
                throw new StringIndexOutOfBoundsException("end < start");
            } else {
                System.arraycopy(this.buffer, i, achar, k, j - i);
            }
        } else {
            throw new StringIndexOutOfBoundsException(j);
        }
    }

    public StrBuilder appendNewLine() {
        if (this.newLine == null) {
            this.append(SystemUtils.LINE_SEPARATOR);
            return this;
        } else {
            return this.append(this.newLine);
        }
    }

    public StrBuilder appendNull() {
        return this.nullText == null ? this : this.append(this.nullText);
    }

    public StrBuilder append(Object object) {
        return object == null ? this.appendNull() : this.append(object.toString());
    }

    public StrBuilder append(CharSequence charsequence) {
        return charsequence == null ? this.appendNull() : this.append(charsequence.toString());
    }

    public StrBuilder append(CharSequence charsequence, int i, int j) {
        return charsequence == null ? this.appendNull() : this.append(charsequence.toString(), i, j);
    }

    public StrBuilder append(String s) {
        if (s == null) {
            return this.appendNull();
        } else {
            int i = s.length();

            if (i > 0) {
                int j = this.length();

                this.ensureCapacity(j + i);
                s.getChars(0, i, this.buffer, j);
                this.size += i;
            }

            return this;
        }
    }

    public StrBuilder append(String s, int i, int j) {
        if (s == null) {
            return this.appendNull();
        } else if (i >= 0 && i <= s.length()) {
            if (j >= 0 && i + j <= s.length()) {
                if (j > 0) {
                    int k = this.length();

                    this.ensureCapacity(k + j);
                    s.getChars(i, i + j, this.buffer, k);
                    this.size += j;
                }

                return this;
            } else {
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        } else {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
    }

    public StrBuilder append(String s, Object... aobject) {
        return this.append(String.format(s, aobject));
    }

    public StrBuilder append(StringBuffer stringbuffer) {
        if (stringbuffer == null) {
            return this.appendNull();
        } else {
            int i = stringbuffer.length();

            if (i > 0) {
                int j = this.length();

                this.ensureCapacity(j + i);
                stringbuffer.getChars(0, i, this.buffer, j);
                this.size += i;
            }

            return this;
        }
    }

    public StrBuilder append(StringBuffer stringbuffer, int i, int j) {
        if (stringbuffer == null) {
            return this.appendNull();
        } else if (i >= 0 && i <= stringbuffer.length()) {
            if (j >= 0 && i + j <= stringbuffer.length()) {
                if (j > 0) {
                    int k = this.length();

                    this.ensureCapacity(k + j);
                    stringbuffer.getChars(i, i + j, this.buffer, k);
                    this.size += j;
                }

                return this;
            } else {
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        } else {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
    }

    public StrBuilder append(StringBuilder stringbuilder) {
        if (stringbuilder == null) {
            return this.appendNull();
        } else {
            int i = stringbuilder.length();

            if (i > 0) {
                int j = this.length();

                this.ensureCapacity(j + i);
                stringbuilder.getChars(0, i, this.buffer, j);
                this.size += i;
            }

            return this;
        }
    }

    public StrBuilder append(StringBuilder stringbuilder, int i, int j) {
        if (stringbuilder == null) {
            return this.appendNull();
        } else if (i >= 0 && i <= stringbuilder.length()) {
            if (j >= 0 && i + j <= stringbuilder.length()) {
                if (j > 0) {
                    int k = this.length();

                    this.ensureCapacity(k + j);
                    stringbuilder.getChars(i, i + j, this.buffer, k);
                    this.size += j;
                }

                return this;
            } else {
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        } else {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
    }

    public StrBuilder append(StrBuilder strbuilder) {
        if (strbuilder == null) {
            return this.appendNull();
        } else {
            int i = strbuilder.length();

            if (i > 0) {
                int j = this.length();

                this.ensureCapacity(j + i);
                System.arraycopy(strbuilder.buffer, 0, this.buffer, j, i);
                this.size += i;
            }

            return this;
        }
    }

    public StrBuilder append(StrBuilder strbuilder, int i, int j) {
        if (strbuilder == null) {
            return this.appendNull();
        } else if (i >= 0 && i <= strbuilder.length()) {
            if (j >= 0 && i + j <= strbuilder.length()) {
                if (j > 0) {
                    int k = this.length();

                    this.ensureCapacity(k + j);
                    strbuilder.getChars(i, i + j, this.buffer, k);
                    this.size += j;
                }

                return this;
            } else {
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        } else {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
    }

    public StrBuilder append(char[] achar) {
        if (achar == null) {
            return this.appendNull();
        } else {
            int i = achar.length;

            if (i > 0) {
                int j = this.length();

                this.ensureCapacity(j + i);
                System.arraycopy(achar, 0, this.buffer, j, i);
                this.size += i;
            }

            return this;
        }
    }

    public StrBuilder append(char[] achar, int i, int j) {
        if (achar == null) {
            return this.appendNull();
        } else if (i >= 0 && i <= achar.length) {
            if (j >= 0 && i + j <= achar.length) {
                if (j > 0) {
                    int k = this.length();

                    this.ensureCapacity(k + j);
                    System.arraycopy(achar, i, this.buffer, k, j);
                    this.size += j;
                }

                return this;
            } else {
                throw new StringIndexOutOfBoundsException("Invalid length: " + j);
            }
        } else {
            throw new StringIndexOutOfBoundsException("Invalid startIndex: " + j);
        }
    }

    public StrBuilder append(boolean flag) {
        if (flag) {
            this.ensureCapacity(this.size + 4);
            this.buffer[this.size++] = 116;
            this.buffer[this.size++] = 114;
            this.buffer[this.size++] = 117;
            this.buffer[this.size++] = 101;
        } else {
            this.ensureCapacity(this.size + 5);
            this.buffer[this.size++] = 102;
            this.buffer[this.size++] = 97;
            this.buffer[this.size++] = 108;
            this.buffer[this.size++] = 115;
            this.buffer[this.size++] = 101;
        }

        return this;
    }

    public StrBuilder append(char c0) {
        int i = this.length();

        this.ensureCapacity(i + 1);
        this.buffer[this.size++] = c0;
        return this;
    }

    public StrBuilder append(int i) {
        return this.append(String.valueOf(i));
    }

    public StrBuilder append(long i) {
        return this.append(String.valueOf(i));
    }

    public StrBuilder append(float f) {
        return this.append(String.valueOf(f));
    }

    public StrBuilder append(double d0) {
        return this.append(String.valueOf(d0));
    }

    public StrBuilder appendln(Object object) {
        return this.append(object).appendNewLine();
    }

    public StrBuilder appendln(String s) {
        return this.append(s).appendNewLine();
    }

    public StrBuilder appendln(String s, int i, int j) {
        return this.append(s, i, j).appendNewLine();
    }

    public StrBuilder appendln(String s, Object... aobject) {
        return this.append(s, aobject).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer stringbuffer) {
        return this.append(stringbuffer).appendNewLine();
    }

    public StrBuilder appendln(StringBuilder stringbuilder) {
        return this.append(stringbuilder).appendNewLine();
    }

    public StrBuilder appendln(StringBuilder stringbuilder, int i, int j) {
        return this.append(stringbuilder, i, j).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer stringbuffer, int i, int j) {
        return this.append(stringbuffer, i, j).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder strbuilder) {
        return this.append(strbuilder).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder strbuilder, int i, int j) {
        return this.append(strbuilder, i, j).appendNewLine();
    }

    public StrBuilder appendln(char[] achar) {
        return this.append(achar).appendNewLine();
    }

    public StrBuilder appendln(char[] achar, int i, int j) {
        return this.append(achar, i, j).appendNewLine();
    }

    public StrBuilder appendln(boolean flag) {
        return this.append(flag).appendNewLine();
    }

    public StrBuilder appendln(char c0) {
        return this.append(c0).appendNewLine();
    }

    public StrBuilder appendln(int i) {
        return this.append(i).appendNewLine();
    }

    public StrBuilder appendln(long i) {
        return this.append(i).appendNewLine();
    }

    public StrBuilder appendln(float f) {
        return this.append(f).appendNewLine();
    }

    public StrBuilder appendln(double d0) {
        return this.append(d0).appendNewLine();
    }

    public StrBuilder appendAll(Object... aobject) {
        if (aobject != null && aobject.length > 0) {
            Object[] aobject1 = aobject;
            int i = aobject.length;

            for (int j = 0; j < i; ++j) {
                Object object = aobject1[j];

                this.append(object);
            }
        }

        return this;
    }

    public StrBuilder appendAll(Iterable iterable) {
        if (iterable != null) {
            Iterator iterator = iterable.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                this.append(object);
            }
        }

        return this;
    }

    public StrBuilder appendAll(Iterator iterator) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                this.append(iterator.next());
            }
        }

        return this;
    }

    public StrBuilder appendWithSeparators(Object[] aobject, String s) {
        if (aobject != null && aobject.length > 0) {
            String s1 = ObjectUtils.toString(s);

            this.append(aobject[0]);

            for (int i = 1; i < aobject.length; ++i) {
                this.append(s1);
                this.append(aobject[i]);
            }
        }

        return this;
    }

    public StrBuilder appendWithSeparators(Iterable iterable, String s) {
        if (iterable != null) {
            String s1 = ObjectUtils.toString(s);
            Iterator iterator = iterable.iterator();

            while (iterator.hasNext()) {
                this.append(iterator.next());
                if (iterator.hasNext()) {
                    this.append(s1);
                }
            }
        }

        return this;
    }

    public StrBuilder appendWithSeparators(Iterator iterator, String s) {
        if (iterator != null) {
            String s1 = ObjectUtils.toString(s);

            while (iterator.hasNext()) {
                this.append(iterator.next());
                if (iterator.hasNext()) {
                    this.append(s1);
                }
            }
        }

        return this;
    }

    public StrBuilder appendSeparator(String s) {
        return this.appendSeparator(s, (String) null);
    }

    public StrBuilder appendSeparator(String s, String s1) {
        String s2 = this.isEmpty() ? s1 : s;

        if (s2 != null) {
            this.append(s2);
        }

        return this;
    }

    public StrBuilder appendSeparator(char c0) {
        if (this.size() > 0) {
            this.append(c0);
        }

        return this;
    }

    public StrBuilder appendSeparator(char c0, char c1) {
        if (this.size() > 0) {
            this.append(c0);
        } else {
            this.append(c1);
        }

        return this;
    }

    public StrBuilder appendSeparator(String s, int i) {
        if (s != null && i > 0) {
            this.append(s);
        }

        return this;
    }

    public StrBuilder appendSeparator(char c0, int i) {
        if (i > 0) {
            this.append(c0);
        }

        return this;
    }

    public StrBuilder appendPadding(int i, char c0) {
        if (i >= 0) {
            this.ensureCapacity(this.size + i);

            for (int j = 0; j < i; ++j) {
                this.buffer[this.size++] = c0;
            }
        }

        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(Object object, int i, char c0) {
        if (i > 0) {
            this.ensureCapacity(this.size + i);
            String s = object == null ? this.getNullText() : object.toString();

            if (s == null) {
                s = "";
            }

            int j = s.length();

            if (j >= i) {
                s.getChars(j - i, j, this.buffer, this.size);
            } else {
                int k = i - j;

                for (int l = 0; l < k; ++l) {
                    this.buffer[this.size + l] = c0;
                }

                s.getChars(0, j, this.buffer, this.size + k);
            }

            this.size += i;
        }

        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(int i, int j, char c0) {
        return this.appendFixedWidthPadLeft(String.valueOf(i), j, c0);
    }

    public StrBuilder appendFixedWidthPadRight(Object object, int i, char c0) {
        if (i > 0) {
            this.ensureCapacity(this.size + i);
            String s = object == null ? this.getNullText() : object.toString();

            if (s == null) {
                s = "";
            }

            int j = s.length();

            if (j >= i) {
                s.getChars(0, i, this.buffer, this.size);
            } else {
                int k = i - j;

                s.getChars(0, j, this.buffer, this.size);

                for (int l = 0; l < k; ++l) {
                    this.buffer[this.size + j + l] = c0;
                }
            }

            this.size += i;
        }

        return this;
    }

    public StrBuilder appendFixedWidthPadRight(int i, int j, char c0) {
        return this.appendFixedWidthPadRight(String.valueOf(i), j, c0);
    }

    public StrBuilder insert(int i, Object object) {
        return object == null ? this.insert(i, this.nullText) : this.insert(i, object.toString());
    }

    public StrBuilder insert(int i, String s) {
        this.validateIndex(i);
        if (s == null) {
            s = this.nullText;
        }

        if (s != null) {
            int j = s.length();

            if (j > 0) {
                int k = this.size + j;

                this.ensureCapacity(k);
                System.arraycopy(this.buffer, i, this.buffer, i + j, this.size - i);
                this.size = k;
                s.getChars(0, j, this.buffer, i);
            }
        }

        return this;
    }

    public StrBuilder insert(int i, char[] achar) {
        this.validateIndex(i);
        if (achar == null) {
            return this.insert(i, this.nullText);
        } else {
            int j = achar.length;

            if (j > 0) {
                this.ensureCapacity(this.size + j);
                System.arraycopy(this.buffer, i, this.buffer, i + j, this.size - i);
                System.arraycopy(achar, 0, this.buffer, i, j);
                this.size += j;
            }

            return this;
        }
    }

    public StrBuilder insert(int i, char[] achar, int j, int k) {
        this.validateIndex(i);
        if (achar == null) {
            return this.insert(i, this.nullText);
        } else if (j >= 0 && j <= achar.length) {
            if (k >= 0 && j + k <= achar.length) {
                if (k > 0) {
                    this.ensureCapacity(this.size + k);
                    System.arraycopy(this.buffer, i, this.buffer, i + k, this.size - i);
                    System.arraycopy(achar, j, this.buffer, i, k);
                    this.size += k;
                }

                return this;
            } else {
                throw new StringIndexOutOfBoundsException("Invalid length: " + k);
            }
        } else {
            throw new StringIndexOutOfBoundsException("Invalid offset: " + j);
        }
    }

    public StrBuilder insert(int i, boolean flag) {
        this.validateIndex(i);
        if (flag) {
            this.ensureCapacity(this.size + 4);
            System.arraycopy(this.buffer, i, this.buffer, i + 4, this.size - i);
            this.buffer[i++] = 116;
            this.buffer[i++] = 114;
            this.buffer[i++] = 117;
            this.buffer[i] = 101;
            this.size += 4;
        } else {
            this.ensureCapacity(this.size + 5);
            System.arraycopy(this.buffer, i, this.buffer, i + 5, this.size - i);
            this.buffer[i++] = 102;
            this.buffer[i++] = 97;
            this.buffer[i++] = 108;
            this.buffer[i++] = 115;
            this.buffer[i] = 101;
            this.size += 5;
        }

        return this;
    }

    public StrBuilder insert(int i, char c0) {
        this.validateIndex(i);
        this.ensureCapacity(this.size + 1);
        System.arraycopy(this.buffer, i, this.buffer, i + 1, this.size - i);
        this.buffer[i] = c0;
        ++this.size;
        return this;
    }

    public StrBuilder insert(int i, int j) {
        return this.insert(i, String.valueOf(j));
    }

    public StrBuilder insert(int i, long j) {
        return this.insert(i, String.valueOf(j));
    }

    public StrBuilder insert(int i, float f) {
        return this.insert(i, String.valueOf(f));
    }

    public StrBuilder insert(int i, double d0) {
        return this.insert(i, String.valueOf(d0));
    }

    private void deleteImpl(int i, int j, int k) {
        System.arraycopy(this.buffer, j, this.buffer, i, this.size - j);
        this.size -= k;
    }

    public StrBuilder delete(int i, int j) {
        j = this.validateRange(i, j);
        int k = j - i;

        if (k > 0) {
            this.deleteImpl(i, j, k);
        }

        return this;
    }

    public StrBuilder deleteAll(char c0) {
        for (int i = 0; i < this.size; ++i) {
            if (this.buffer[i] == c0) {
                int j = i;

                do {
                    ++i;
                } while (i < this.size && this.buffer[i] == c0);

                int k = i - j;

                this.deleteImpl(j, i, k);
                i -= k;
            }
        }

        return this;
    }

    public StrBuilder deleteFirst(char c0) {
        for (int i = 0; i < this.size; ++i) {
            if (this.buffer[i] == c0) {
                this.deleteImpl(i, i + 1, 1);
                break;
            }
        }

        return this;
    }

    public StrBuilder deleteAll(String s) {
        int i = s == null ? 0 : s.length();

        if (i > 0) {
            for (int j = this.indexOf(s, 0); j >= 0; j = this.indexOf(s, j)) {
                this.deleteImpl(j, j + i, i);
            }
        }

        return this;
    }

    public StrBuilder deleteFirst(String s) {
        int i = s == null ? 0 : s.length();

        if (i > 0) {
            int j = this.indexOf(s, 0);

            if (j >= 0) {
                this.deleteImpl(j, j + i, i);
            }
        }

        return this;
    }

    public StrBuilder deleteAll(StrMatcher strmatcher) {
        return this.replace(strmatcher, (String) null, 0, this.size, -1);
    }

    public StrBuilder deleteFirst(StrMatcher strmatcher) {
        return this.replace(strmatcher, (String) null, 0, this.size, 1);
    }

    private void replaceImpl(int i, int j, int k, String s, int l) {
        int i1 = this.size - k + l;

        if (l != k) {
            this.ensureCapacity(i1);
            System.arraycopy(this.buffer, j, this.buffer, i + l, this.size - j);
            this.size = i1;
        }

        if (l > 0) {
            s.getChars(0, l, this.buffer, i);
        }

    }

    public StrBuilder replace(int i, int j, String s) {
        j = this.validateRange(i, j);
        int k = s == null ? 0 : s.length();

        this.replaceImpl(i, j, j - i, s, k);
        return this;
    }

    public StrBuilder replaceAll(char c0, char c1) {
        if (c0 != c1) {
            for (int i = 0; i < this.size; ++i) {
                if (this.buffer[i] == c0) {
                    this.buffer[i] = c1;
                }
            }
        }

        return this;
    }

    public StrBuilder replaceFirst(char c0, char c1) {
        if (c0 != c1) {
            for (int i = 0; i < this.size; ++i) {
                if (this.buffer[i] == c0) {
                    this.buffer[i] = c1;
                    break;
                }
            }
        }

        return this;
    }

    public StrBuilder replaceAll(String s, String s1) {
        int i = s == null ? 0 : s.length();

        if (i > 0) {
            int j = s1 == null ? 0 : s1.length();

            for (int k = this.indexOf(s, 0); k >= 0; k = this.indexOf(s, k + j)) {
                this.replaceImpl(k, k + i, i, s1, j);
            }
        }

        return this;
    }

    public StrBuilder replaceFirst(String s, String s1) {
        int i = s == null ? 0 : s.length();

        if (i > 0) {
            int j = this.indexOf(s, 0);

            if (j >= 0) {
                int k = s1 == null ? 0 : s1.length();

                this.replaceImpl(j, j + i, i, s1, k);
            }
        }

        return this;
    }

    public StrBuilder replaceAll(StrMatcher strmatcher, String s) {
        return this.replace(strmatcher, s, 0, this.size, -1);
    }

    public StrBuilder replaceFirst(StrMatcher strmatcher, String s) {
        return this.replace(strmatcher, s, 0, this.size, 1);
    }

    public StrBuilder replace(StrMatcher strmatcher, String s, int i, int j, int k) {
        j = this.validateRange(i, j);
        return this.replaceImpl(strmatcher, s, i, j, k);
    }

    private StrBuilder replaceImpl(StrMatcher strmatcher, String s, int i, int j, int k) {
        if (strmatcher != null && this.size != 0) {
            int l = s == null ? 0 : s.length();
            char[] achar = this.buffer;

            for (int i1 = i; i1 < j && k != 0; ++i1) {
                int j1 = strmatcher.isMatch(achar, i1, i, j);

                if (j1 > 0) {
                    this.replaceImpl(i1, i1 + j1, j1, s, l);
                    j = j - j1 + l;
                    i1 = i1 + l - 1;
                    if (k > 0) {
                        --k;
                    }
                }
            }

            return this;
        } else {
            return this;
        }
    }

    public StrBuilder reverse() {
        if (this.size == 0) {
            return this;
        } else {
            int i = this.size / 2;
            char[] achar = this.buffer;
            int j = 0;

            for (int k = this.size - 1; j < i; --k) {
                char c0 = achar[j];

                achar[j] = achar[k];
                achar[k] = c0;
                ++j;
            }

            return this;
        }
    }

    public StrBuilder trim() {
        if (this.size == 0) {
            return this;
        } else {
            int i = this.size;
            char[] achar = this.buffer;

            int j;

            for (j = 0; j < i && achar[j] <= 32; ++j) {
                ;
            }

            while (j < i && achar[i - 1] <= 32) {
                --i;
            }

            if (i < this.size) {
                this.delete(i, this.size);
            }

            if (j > 0) {
                this.delete(0, j);
            }

            return this;
        }
    }

    public boolean startsWith(String s) {
        if (s == null) {
            return false;
        } else {
            int i = s.length();

            if (i == 0) {
                return true;
            } else if (i > this.size) {
                return false;
            } else {
                for (int j = 0; j < i; ++j) {
                    if (this.buffer[j] != s.charAt(j)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public boolean endsWith(String s) {
        if (s == null) {
            return false;
        } else {
            int i = s.length();

            if (i == 0) {
                return true;
            } else if (i > this.size) {
                return false;
            } else {
                int j = this.size - i;

                for (int k = 0; k < i; ++j) {
                    if (this.buffer[j] != s.charAt(k)) {
                        return false;
                    }

                    ++k;
                }

                return true;
            }
        }
    }

    public CharSequence subSequence(int i, int j) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        } else if (j > this.size) {
            throw new StringIndexOutOfBoundsException(j);
        } else if (i > j) {
            throw new StringIndexOutOfBoundsException(j - i);
        } else {
            return this.substring(i, j);
        }
    }

    public String substring(int i) {
        return this.substring(i, this.size);
    }

    public String substring(int i, int j) {
        j = this.validateRange(i, j);
        return new String(this.buffer, i, j - i);
    }

    public String leftString(int i) {
        return i <= 0 ? "" : (i >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, 0, i));
    }

    public String rightString(int i) {
        return i <= 0 ? "" : (i >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, this.size - i, i));
    }

    public String midString(int i, int j) {
        if (i < 0) {
            i = 0;
        }

        return j > 0 && i < this.size ? (this.size <= i + j ? new String(this.buffer, i, this.size - i) : new String(this.buffer, i, j)) : "";
    }

    public boolean contains(char c0) {
        char[] achar = this.buffer;

        for (int i = 0; i < this.size; ++i) {
            if (achar[i] == c0) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(String s) {
        return this.indexOf(s, 0) >= 0;
    }

    public boolean contains(StrMatcher strmatcher) {
        return this.indexOf(strmatcher, 0) >= 0;
    }

    public int indexOf(char c0) {
        return this.indexOf(c0, 0);
    }

    public int indexOf(char c0, int i) {
        i = i < 0 ? 0 : i;
        if (i >= this.size) {
            return -1;
        } else {
            char[] achar = this.buffer;

            for (int j = i; j < this.size; ++j) {
                if (achar[j] == c0) {
                    return j;
                }
            }

            return -1;
        }
    }

    public int indexOf(String s) {
        return this.indexOf(s, 0);
    }

    public int indexOf(String s, int i) {
        i = i < 0 ? 0 : i;
        if (s != null && i < this.size) {
            int j = s.length();

            if (j == 1) {
                return this.indexOf(s.charAt(0), i);
            } else if (j == 0) {
                return i;
            } else if (j > this.size) {
                return -1;
            } else {
                char[] achar = this.buffer;
                int k = this.size - j + 1;
                int l = i;

                label42:
                while (l < k) {
                    for (int i1 = 0; i1 < j; ++i1) {
                        if (s.charAt(i1) != achar[l + i1]) {
                            ++l;
                            continue label42;
                        }
                    }

                    return l;
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public int indexOf(StrMatcher strmatcher) {
        return this.indexOf(strmatcher, 0);
    }

    public int indexOf(StrMatcher strmatcher, int i) {
        i = i < 0 ? 0 : i;
        if (strmatcher != null && i < this.size) {
            int j = this.size;
            char[] achar = this.buffer;

            for (int k = i; k < j; ++k) {
                if (strmatcher.isMatch(achar, k, i, j) > 0) {
                    return k;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public int lastIndexOf(char c0) {
        return this.lastIndexOf(c0, this.size - 1);
    }

    public int lastIndexOf(char c0, int i) {
        i = i >= this.size ? this.size - 1 : i;
        if (i < 0) {
            return -1;
        } else {
            for (int j = i; j >= 0; --j) {
                if (this.buffer[j] == c0) {
                    return j;
                }
            }

            return -1;
        }
    }

    public int lastIndexOf(String s) {
        return this.lastIndexOf(s, this.size - 1);
    }

    public int lastIndexOf(String s, int i) {
        i = i >= this.size ? this.size - 1 : i;
        if (s != null && i >= 0) {
            int j = s.length();

            if (j > 0 && j <= this.size) {
                if (j == 1) {
                    return this.lastIndexOf(s.charAt(0), i);
                }

                int k = i - j + 1;

                label40:
                while (k >= 0) {
                    for (int l = 0; l < j; ++l) {
                        if (s.charAt(l) != this.buffer[k + l]) {
                            --k;
                            continue label40;
                        }
                    }

                    return k;
                }
            } else if (j == 0) {
                return i;
            }

            return -1;
        } else {
            return -1;
        }
    }

    public int lastIndexOf(StrMatcher strmatcher) {
        return this.lastIndexOf(strmatcher, this.size);
    }

    public int lastIndexOf(StrMatcher strmatcher, int i) {
        i = i >= this.size ? this.size - 1 : i;
        if (strmatcher != null && i >= 0) {
            char[] achar = this.buffer;
            int j = i + 1;

            for (int k = i; k >= 0; --k) {
                if (strmatcher.isMatch(achar, k, 0, j) > 0) {
                    return k;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public StrTokenizer asTokenizer() {
        return new StrBuilder.StrBuilderTokenizer();
    }

    public Reader asReader() {
        return new StrBuilder.StrBuilderReader();
    }

    public Writer asWriter() {
        return new StrBuilder.StrBuilderWriter();
    }

    public boolean equalsIgnoreCase(StrBuilder strbuilder) {
        if (this == strbuilder) {
            return true;
        } else if (this.size != strbuilder.size) {
            return false;
        } else {
            char[] achar = this.buffer;
            char[] achar1 = strbuilder.buffer;

            for (int i = this.size - 1; i >= 0; --i) {
                char c0 = achar[i];
                char c1 = achar1[i];

                if (c0 != c1 && Character.toUpperCase(c0) != Character.toUpperCase(c1)) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean equals(StrBuilder strbuilder) {
        if (this == strbuilder) {
            return true;
        } else if (this.size != strbuilder.size) {
            return false;
        } else {
            char[] achar = this.buffer;
            char[] achar1 = strbuilder.buffer;

            for (int i = this.size - 1; i >= 0; --i) {
                if (achar[i] != achar1[i]) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean equals(Object object) {
        return object instanceof StrBuilder ? this.equals((StrBuilder) object) : false;
    }

    public int hashCode() {
        char[] achar = this.buffer;
        int i = 0;

        for (int j = this.size - 1; j >= 0; --j) {
            i = 31 * i + achar[j];
        }

        return i;
    }

    public String toString() {
        return new String(this.buffer, 0, this.size);
    }

    public StringBuffer toStringBuffer() {
        return (new StringBuffer(this.size)).append(this.buffer, 0, this.size);
    }

    public StringBuilder toStringBuilder() {
        return (new StringBuilder(this.size)).append(this.buffer, 0, this.size);
    }

    public String build() {
        return this.toString();
    }

    protected int validateRange(int i, int j) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        } else {
            if (j > this.size) {
                j = this.size;
            }

            if (i > j) {
                throw new StringIndexOutOfBoundsException("end < start");
            } else {
                return j;
            }
        }
    }

    protected void validateIndex(int i) {
        if (i < 0 || i > this.size) {
            throw new StringIndexOutOfBoundsException(i);
        }
    }

    class StrBuilderWriter extends Writer {

        public void close() {}

        public void flush() {}

        public void write(int i) {
            StrBuilder.this.append((char) i);
        }

        public void write(char[] achar) {
            StrBuilder.this.append(achar);
        }

        public void write(char[] achar, int i, int j) {
            StrBuilder.this.append(achar, i, j);
        }

        public void write(String s) {
            StrBuilder.this.append(s);
        }

        public void write(String s, int i, int j) {
            StrBuilder.this.append(s, i, j);
        }
    }

    class StrBuilderReader extends Reader {

        private int pos;
        private int mark;

        public void close() {}

        public int read() {
            return !this.ready() ? -1 : StrBuilder.this.charAt(this.pos++);
        }

        public int read(char[] achar, int i, int j) {
            if (i >= 0 && j >= 0 && i <= achar.length && i + j <= achar.length && i + j >= 0) {
                if (j == 0) {
                    return 0;
                } else if (this.pos >= StrBuilder.this.size()) {
                    return -1;
                } else {
                    if (this.pos + j > StrBuilder.this.size()) {
                        j = StrBuilder.this.size() - this.pos;
                    }

                    StrBuilder.this.getChars(this.pos, this.pos + j, achar, i);
                    this.pos += j;
                    return j;
                }
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public long skip(long i) {
            if ((long) this.pos + i > (long) StrBuilder.this.size()) {
                i = (long) (StrBuilder.this.size() - this.pos);
            }

            if (i < 0L) {
                return 0L;
            } else {
                this.pos = (int) ((long) this.pos + i);
                return i;
            }
        }

        public boolean ready() {
            return this.pos < StrBuilder.this.size();
        }

        public boolean markSupported() {
            return true;
        }

        public void mark(int i) {
            this.mark = this.pos;
        }

        public void reset() {
            this.pos = this.mark;
        }
    }

    class StrBuilderTokenizer extends StrTokenizer {

        protected List tokenize(char[] achar, int i, int j) {
            return achar == null ? super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size()) : super.tokenize(achar, i, j);
        }

        public String getContent() {
            String s = super.getContent();

            return s == null ? StrBuilder.this.toString() : s;
        }
    }
}
