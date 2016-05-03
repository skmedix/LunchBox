package org.bukkit.craftbukkit.v1_8_R3.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class LongObjectHashMap implements Cloneable, Serializable {

    static final long serialVersionUID = 2841537710170573815L;
    private static final long EMPTY_KEY = Long.MIN_VALUE;
    private static final int BUCKET_SIZE = 4096;
    private transient long[][] keys;
    private transient Object[][] values;
    private transient int modCount;
    private transient int size;

    public LongObjectHashMap() {
        this.initialize();
    }

    public LongObjectHashMap(Map map) {
        this();
        this.putAll(map);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean containsKey(long key) {
        return this.get(key) != null;
    }

    public boolean containsValue(Object value) {
        Iterator iterator = this.values().iterator();

        Object val;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            val = (Object) iterator.next();
        } while (val != value && !val.equals(value));

        return true;
    }

    public Object get(long key) {
        int index = (int) (this.keyIndex(key) & 4095L);
        long[] inner = this.keys[index];

        if (inner == null) {
            return null;
        } else {
            for (int i = 0; i < inner.length; ++i) {
                long innerKey = inner[i];

                if (innerKey == Long.MIN_VALUE) {
                    return null;
                }

                if (innerKey == key) {
                    return this.values[index][i];
                }
            }

            return null;
        }
    }

    public Object put(long key, Object value) {
        int index = (int) (this.keyIndex(key) & 4095L);
        long[] innerKeys = this.keys[index];
        Object[] innerValues = this.values[index];

        ++this.modCount;
        if (innerKeys == null) {
            this.keys[index] = innerKeys = new long[8];
            Arrays.fill(innerKeys, Long.MIN_VALUE);
            this.values[index] = innerValues = new Object[8];
            innerKeys[0] = key;
            innerValues[0] = value;
            ++this.size;
        } else {
            int i;

            for (i = 0; i < innerKeys.length; ++i) {
                if (innerKeys[i] == Long.MIN_VALUE) {
                    ++this.size;
                    innerKeys[i] = key;
                    innerValues[i] = value;
                    return null;
                }

                if (innerKeys[i] == key) {
                    Object oldValue = innerValues[i];

                    innerKeys[i] = key;
                    innerValues[i] = value;
                    return oldValue;
                }
            }

            this.keys[index] = innerKeys = Arrays.copyOf(innerKeys, i << 1);
            Arrays.fill(innerKeys, i, innerKeys.length, Long.MIN_VALUE);
            this.values[index] = innerValues = Arrays.copyOf(innerValues, i << 1);
            innerKeys[i] = key;
            innerValues[i] = value;
            ++this.size;
        }

        return null;
    }

    public Object remove(long key) {
        int index = (int) (this.keyIndex(key) & 4095L);
        long[] inner = this.keys[index];

        if (inner == null) {
            return null;
        } else {
            for (int i = 0; i < inner.length && inner[i] != Long.MIN_VALUE; ++i) {
                if (inner[i] == key) {
                    Object value = this.values[index][i];

                    ++i;

                    while (i < inner.length && inner[i] != Long.MIN_VALUE) {
                        inner[i - 1] = inner[i];
                        this.values[index][i - 1] = this.values[index][i];
                        ++i;
                    }

                    inner[i - 1] = Long.MIN_VALUE;
                    this.values[index][i - 1] = null;
                    --this.size;
                    ++this.modCount;
                    return value;
                }
            }

            return null;
        }
    }

    public void putAll(Map map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();

            this.put(((Long) entry.getKey()).longValue(), entry.getValue());
        }

    }

    public void clear() {
        if (this.size != 0) {
            ++this.modCount;
            this.size = 0;
            Arrays.fill(this.keys, (Object) null);
            Arrays.fill(this.values, (Object) null);
        }
    }

    public Set keySet() {
        return new LongObjectHashMap.KeySet((LongObjectHashMap.KeySet) null);
    }

    public Collection values() {
        return new LongObjectHashMap.ValueCollection((LongObjectHashMap.ValueCollection) null);
    }

    /** @deprecated */
    @Deprecated
    public Set entrySet() {
        HashSet set = new HashSet();
        Iterator iterator = this.keySet().iterator();

        while (iterator.hasNext()) {
            long key = ((Long) iterator.next()).longValue();

            set.add(new LongObjectHashMap.Entry(key, this.get(key)));
        }

        return set;
    }

    public Object clone() throws CloneNotSupportedException {
        LongObjectHashMap clone = (LongObjectHashMap) super.clone();

        clone.clear();
        clone.initialize();
        Iterator iterator = this.keySet().iterator();

        while (iterator.hasNext()) {
            long key = ((Long) iterator.next()).longValue();
            Object value = this.get(key);

            clone.put(key, value);
        }

        return clone;
    }

    private void initialize() {
        this.keys = new long[4096][];
        this.values = new Object[4096][];
    }

    private long keyIndex(long key) {
        key ^= key >>> 33;
        key *= -49064778989728563L;
        key ^= key >>> 33;
        key *= -4265267296055464877L;
        key ^= key >>> 33;
        return key;
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        Iterator iterator = this.keySet().iterator();

        while (iterator.hasNext()) {
            long key = ((Long) iterator.next()).longValue();
            Object value = this.get(key);

            outputStream.writeLong(key);
            outputStream.writeObject(value);
        }

        outputStream.writeLong(Long.MIN_VALUE);
        outputStream.writeObject((Object) null);
    }

    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
        this.initialize();

        while (true) {
            long key = inputStream.readLong();
            Object value = inputStream.readObject();

            if (key == Long.MIN_VALUE && value == null) {
                return;
            }

            this.put(key, value);
        }
    }

    private class Entry implements java.util.Map.Entry {

        private final Long key;
        private Object value;

        Entry(long k, Object v) {
            this.key = Long.valueOf(k);
            this.value = v;
        }

        public Long getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public Object setValue(Object v) {
            Object old = this.value;

            this.value = v;
            LongObjectHashMap.this.put(this.key.longValue(), v);
            return old;
        }
    }

    private class KeyIterator implements Iterator {

        final LongObjectHashMap.ValueIterator iterator = LongObjectHashMap.this.new ValueIterator();

        public void remove() {
            this.iterator.remove();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Long next() {
            this.iterator.next();
            return Long.valueOf(this.iterator.prevKey);
        }
    }

    private class KeySet extends AbstractSet {

        private KeySet() {}

        public void clear() {
            LongObjectHashMap.this.clear();
        }

        public int size() {
            return LongObjectHashMap.this.size();
        }

        public boolean contains(Object key) {
            return key instanceof Long && LongObjectHashMap.this.containsKey(((Long) key).longValue());
        }

        public boolean remove(Object key) {
            return LongObjectHashMap.this.remove(((Long) key).longValue()) != null;
        }

        public Iterator iterator() {
            return LongObjectHashMap.this.new KeyIterator();
        }

        KeySet(LongObjectHashMap.KeySet longobjecthashmap_keyset) {
            this();
        }
    }

    private class ValueCollection extends AbstractCollection {

        private ValueCollection() {}

        public void clear() {
            LongObjectHashMap.this.clear();
        }

        public int size() {
            return LongObjectHashMap.this.size();
        }

        public boolean contains(Object value) {
            return LongObjectHashMap.this.containsValue(value);
        }

        public Iterator iterator() {
            return LongObjectHashMap.this.new ValueIterator();
        }

        ValueCollection(LongObjectHashMap.ValueCollection longobjecthashmap_valuecollection) {
            this();
        }
    }

    private class ValueIterator implements Iterator {

        private int count;
        private int index;
        private int innerIndex;
        private int expectedModCount;
        private long lastReturned = Long.MIN_VALUE;
        long prevKey = Long.MIN_VALUE;
        Object prevValue;

        ValueIterator() {
            this.expectedModCount = LongObjectHashMap.this.modCount;
        }

        public boolean hasNext() {
            return this.count < LongObjectHashMap.this.size;
        }

        public void remove() {
            if (LongObjectHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else if (this.lastReturned == Long.MIN_VALUE) {
                throw new IllegalStateException();
            } else {
                --this.count;
                LongObjectHashMap.this.remove(this.lastReturned);
                this.lastReturned = Long.MIN_VALUE;
                this.expectedModCount = LongObjectHashMap.this.modCount;
            }
        }

        public Object next() {
            if (LongObjectHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                long[][] keys = LongObjectHashMap.this.keys;

                ++this.count;
                if (this.prevKey != Long.MIN_VALUE) {
                    ++this.innerIndex;
                }

                for (; this.index < keys.length; ++this.index) {
                    if (keys[this.index] != null) {
                        if (this.innerIndex < keys[this.index].length) {
                            long key = keys[this.index][this.innerIndex];
                            Object value = LongObjectHashMap.this.values[this.index][this.innerIndex];

                            if (key != Long.MIN_VALUE) {
                                this.lastReturned = key;
                                this.prevKey = key;
                                this.prevValue = value;
                                return this.prevValue;
                            }
                        }

                        this.innerIndex = 0;
                    }
                }

                throw new NoSuchElementException();
            }
        }
    }
}
