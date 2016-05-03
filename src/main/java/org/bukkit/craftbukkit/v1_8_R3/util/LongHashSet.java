package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LongHashSet {

    private static final int INITIAL_SIZE = 3;
    private static final double LOAD_FACTOR = 0.75D;
    private static final long FREE = 0L;
    private static final long REMOVED = Long.MIN_VALUE;
    private int freeEntries;
    private int elements;
    private long[] values;
    private int modCount;

    public LongHashSet() {
        this(3);
    }

    public LongHashSet(int size) {
        this.values = new long[size == 0 ? 1 : size];
        this.elements = 0;
        this.freeEntries = this.values.length;
        this.modCount = 0;
    }

    public Iterator iterator() {
        return new LongHashSet.Itr();
    }

    public int size() {
        return this.elements;
    }

    public boolean isEmpty() {
        return this.elements == 0;
    }

    public boolean contains(int msw, int lsw) {
        return this.contains(LongHash.toLong(msw, lsw));
    }

    public boolean contains(long value) {
        int hash = this.hash(value);
        int index = (hash & Integer.MAX_VALUE) % this.values.length;
        int offset = 1;

        while (this.values[index] != 0L && (this.hash(this.values[index]) != hash || this.values[index] != value)) {
            index = (index + offset & Integer.MAX_VALUE) % this.values.length;
            offset = offset * 2 + 1;
            if (offset == -1) {
                offset = 2;
            }
        }

        return this.values[index] != 0L;
    }

    public boolean add(int msw, int lsw) {
        return this.add(LongHash.toLong(msw, lsw));
    }

    public boolean add(long value) {
        int hash = this.hash(value);
        int index = (hash & Integer.MAX_VALUE) % this.values.length;
        int offset = 1;
        int deletedix = -1;

        while (this.values[index] != 0L && (this.hash(this.values[index]) != hash || this.values[index] != value)) {
            if (this.values[index] == Long.MIN_VALUE) {
                deletedix = index;
            }

            index = (index + offset & Integer.MAX_VALUE) % this.values.length;
            offset = offset * 2 + 1;
            if (offset == -1) {
                offset = 2;
            }
        }

        if (this.values[index] == 0L) {
            if (deletedix != -1) {
                index = deletedix;
            } else {
                --this.freeEntries;
            }

            ++this.modCount;
            ++this.elements;
            this.values[index] = value;
            if (1.0D - (double) this.freeEntries / (double) this.values.length > 0.75D) {
                this.rehash();
            }

            return true;
        } else {
            return false;
        }
    }

    public void remove(int msw, int lsw) {
        this.remove(LongHash.toLong(msw, lsw));
    }

    public boolean remove(long value) {
        int hash = this.hash(value);
        int index = (hash & Integer.MAX_VALUE) % this.values.length;
        int offset = 1;

        while (this.values[index] != 0L && (this.hash(this.values[index]) != hash || this.values[index] != value)) {
            index = (index + offset & Integer.MAX_VALUE) % this.values.length;
            offset = offset * 2 + 1;
            if (offset == -1) {
                offset = 2;
            }
        }

        if (this.values[index] != 0L) {
            this.values[index] = Long.MIN_VALUE;
            ++this.modCount;
            --this.elements;
            return true;
        } else {
            return false;
        }
    }

    public void clear() {
        this.elements = 0;

        for (int ix = 0; ix < this.values.length; ++ix) {
            this.values[ix] = 0L;
        }

        this.freeEntries = this.values.length;
        ++this.modCount;
    }

    public long[] toArray() {
        long[] result = new long[this.elements];
        long[] values = Arrays.copyOf(this.values, this.values.length);
        int pos = 0;
        long[] along = values;
        int i = values.length;

        for (int j = 0; j < i; ++j) {
            long value = along[j];

            if (value != 0L && value != Long.MIN_VALUE) {
                result[pos++] = value;
            }
        }

        return result;
    }

    public long popFirst() {
        long[] along = this.values;
        int i = this.values.length;

        for (int j = 0; j < i; ++j) {
            long value = along[j];

            if (value != 0L && value != Long.MIN_VALUE) {
                this.remove(value);
                return value;
            }
        }

        return 0L;
    }

    public long[] popAll() {
        long[] ret = this.toArray();

        this.clear();
        return ret;
    }

    private int hash(long value) {
        value ^= value >>> 33;
        value *= -49064778989728563L;
        value ^= value >>> 33;
        value *= -4265267296055464877L;
        value ^= value >>> 33;
        return (int) value;
    }

    private void rehash() {
        int gargagecells = this.values.length - (this.elements + this.freeEntries);

        if ((double) gargagecells / (double) this.values.length > 0.05D) {
            this.rehash(this.values.length);
        } else {
            this.rehash(this.values.length * 2 + 1);
        }

    }

    private void rehash(int newCapacity) {
        long[] newValues = new long[newCapacity];
        long[] along = this.values;
        int i = this.values.length;

        for (int j = 0; j < i; ++j) {
            long value = along[j];

            if (value != 0L && value != Long.MIN_VALUE) {
                int hash = this.hash(value);
                int index = (hash & Integer.MAX_VALUE) % newCapacity;
                int offset = 1;

                while (newValues[index] != 0L) {
                    index = (index + offset & Integer.MAX_VALUE) % newCapacity;
                    offset = offset * 2 + 1;
                    if (offset == -1) {
                        offset = 2;
                    }
                }

                newValues[index] = value;
            }
        }

        this.values = newValues;
        this.freeEntries = this.values.length - this.elements;
    }

    private class Itr implements Iterator {

        private int index;
        private int lastReturned = -1;
        private int expectedModCount;

        public Itr() {
            for (this.index = 0; this.index < LongHashSet.this.values.length && (LongHashSet.this.values[this.index] == 0L || LongHashSet.this.values[this.index] == Long.MIN_VALUE); ++this.index) {
                ;
            }

            this.expectedModCount = LongHashSet.this.modCount;
        }

        public boolean hasNext() {
            return this.index != LongHashSet.this.values.length;
        }

        public Long next() {
            if (LongHashSet.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                int length = LongHashSet.this.values.length;

                if (this.index >= length) {
                    this.lastReturned = -2;
                    throw new NoSuchElementException();
                } else {
                    for (this.lastReturned = this.index++; this.index < length && (LongHashSet.this.values[this.index] == 0L || LongHashSet.this.values[this.index] == Long.MIN_VALUE); ++this.index) {
                        ;
                    }

                    return LongHashSet.this.values[this.lastReturned] == 0L ? Long.valueOf(0L) : Long.valueOf(LongHashSet.this.values[this.lastReturned]);
                }
            }
        }

        public void remove() {
            if (LongHashSet.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else if (this.lastReturned != -1 && this.lastReturned != -2) {
                if (LongHashSet.this.values[this.lastReturned] != 0L && LongHashSet.this.values[this.lastReturned] != Long.MIN_VALUE) {
                    LongHashSet.this.values[this.lastReturned] = Long.MIN_VALUE;
                    LongHashSet.this.elements = LongHashSet.this.elements - 1;
                    LongHashSet.this.modCount = LongHashSet.this.modCount + 1;
                    this.expectedModCount = LongHashSet.this.modCount;
                }

            } else {
                throw new IllegalStateException();
            }
        }
    }
}
