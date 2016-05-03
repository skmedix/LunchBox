package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class CharRange implements Iterable, Serializable {

    private static final long serialVersionUID = 8270183163158333422L;
    private final char start;
    private final char end;
    private final boolean negated;
    private transient String iToString;

    private CharRange(char c0, char c1, boolean flag) {
        if (c0 > c1) {
            char c2 = c0;

            c0 = c1;
            c1 = c2;
        }

        this.start = c0;
        this.end = c1;
        this.negated = flag;
    }

    public static CharRange is(char c0) {
        return new CharRange(c0, c0, false);
    }

    public static CharRange isNot(char c0) {
        return new CharRange(c0, c0, true);
    }

    public static CharRange isIn(char c0, char c1) {
        return new CharRange(c0, c1, false);
    }

    public static CharRange isNotIn(char c0, char c1) {
        return new CharRange(c0, c1, true);
    }

    public char getStart() {
        return this.start;
    }

    public char getEnd() {
        return this.end;
    }

    public boolean isNegated() {
        return this.negated;
    }

    public boolean contains(char c0) {
        return (c0 >= this.start && c0 <= this.end) != this.negated;
    }

    public boolean contains(CharRange charrange) {
        if (charrange == null) {
            throw new IllegalArgumentException("The Range must not be null");
        } else {
            return this.negated ? (charrange.negated ? this.start >= charrange.start && this.end <= charrange.end : charrange.end < this.start || charrange.start > this.end) : (charrange.negated ? this.start == 0 && this.end == '\uffff' : this.start <= charrange.start && this.end >= charrange.end);
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof CharRange)) {
            return false;
        } else {
            CharRange charrange = (CharRange) object;

            return this.start == charrange.start && this.end == charrange.end && this.negated == charrange.negated;
        }
    }

    public int hashCode() {
        return 83 + this.start + 7 * this.end + (this.negated ? 1 : 0);
    }

    public String toString() {
        if (this.iToString == null) {
            StringBuilder stringbuilder = new StringBuilder(4);

            if (this.isNegated()) {
                stringbuilder.append('^');
            }

            stringbuilder.append(this.start);
            if (this.start != this.end) {
                stringbuilder.append('-');
                stringbuilder.append(this.end);
            }

            this.iToString = stringbuilder.toString();
        }

        return this.iToString;
    }

    public Iterator iterator() {
        return new CharRange.CharacterIterator(this, (CharRange.SyntheticClass_1) null);
    }

    static class SyntheticClass_1 {    }

    private static class CharacterIterator implements Iterator {

        private char current;
        private final CharRange range;
        private boolean hasNext;

        private CharacterIterator(CharRange charrange) {
            this.range = charrange;
            this.hasNext = true;
            if (this.range.negated) {
                if (this.range.start == 0) {
                    if (this.range.end == '\uffff') {
                        this.hasNext = false;
                    } else {
                        this.current = (char) (this.range.end + 1);
                    }
                } else {
                    this.current = 0;
                }
            } else {
                this.current = this.range.start;
            }

        }

        private void prepareNext() {
            if (this.range.negated) {
                if (this.current == '\uffff') {
                    this.hasNext = false;
                } else if (this.current + 1 == this.range.start) {
                    if (this.range.end == '\uffff') {
                        this.hasNext = false;
                    } else {
                        this.current = (char) (this.range.end + 1);
                    }
                } else {
                    ++this.current;
                }
            } else if (this.current < this.range.end) {
                ++this.current;
            } else {
                this.hasNext = false;
            }

        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public Character next() {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            } else {
                char c0 = this.current;

                this.prepareNext();
                return Character.valueOf(c0);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        CharacterIterator(CharRange charrange, CharRange.SyntheticClass_1 charrange_syntheticclass_1) {
            this(charrange);
        }
    }
}
