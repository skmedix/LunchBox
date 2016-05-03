package org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util;

public class InodeCache {

    private static final Object PLACEHOLDER = new Object();
    private static final short SHORT_PLACEHOLDER = -2;
    private static final short SHORT_NULL = -1;
    private static final int LOAD_FACTOR = 2;
    private final int maxSize;
    private final int totalSlots;
    private final int maxUsedSlots;
    private final Object[] keys;
    private final short[] next;
    private final short[] prev;
    private final short[] inodes;
    private final short[] reverse;
    private int size;
    private int usedSlots;
    private short mru;
    private short lru;

    public InodeCache() {
        this(1024);
    }

    public InodeCache(int i) {
        this.maxSize = i;
        this.totalSlots = i * 2 * 2 + 3;
        this.maxUsedSlots = this.totalSlots / 2;
        if (this.totalSlots > 32767) {
            throw new IllegalArgumentException("cache size too large");
        } else {
            this.keys = new Object[this.totalSlots];
            this.next = new short[this.totalSlots];
            this.prev = new short[this.totalSlots];
            this.inodes = new short[this.totalSlots];
            this.reverse = new short[this.totalSlots];
            this.clear();
        }
    }

    private static void fill(Object[] aobject, Object object) {
        for (int i = 0; i < aobject.length; ++i) {
            aobject[i] = object;
        }

    }

    private static void fill(short[] ashort, short short0) {
        for (int i = 0; i < ashort.length; ++i) {
            ashort[i] = short0;
        }

    }

    public final void clear() {
        this.size = this.usedSlots = 0;
        this.mru = this.lru = -1;
        fill(this.keys, (Object) null);
        fill(this.inodes, (short) -1);
        fill(this.reverse, (short) -1);
    }

    public final short get(Object object) {
        int i = object.hashCode() & Integer.MAX_VALUE;
        int j = i % this.totalSlots;
        int k = j;
        int l = 1;
        boolean flag = true;

        int i1;
        Object object1;
        short short0;

        for (i1 = -1; (object1 = this.keys[j]) != null; flag = !flag) {
            if (object1 == InodeCache.PLACEHOLDER) {
                if (i1 == -1) {
                    i1 = j;
                }
            } else if (object1.equals(object)) {
                short short1 = this.inodes[j];

                if (j == this.mru) {
                    return short1;
                }

                if (this.lru == j) {
                    this.lru = this.next[this.lru];
                } else {
                    short short2 = this.prev[j];

                    short0 = this.next[j];
                    this.next[short2] = short0;
                    this.prev[short0] = short2;
                }

                this.prev[j] = this.mru;
                this.next[this.mru] = (short) j;
                this.mru = (short) j;
                return short1;
            }

            j = Math.abs((k + (flag ? 1 : -1) * l * l) % this.totalSlots);
            if (!flag) {
                ++l;
            }
        }

        int j1;

        if (i1 == -1) {
            j1 = j;
            if (this.usedSlots == this.maxUsedSlots) {
                this.clear();
                return this.get(object);
            }

            ++this.usedSlots;
        } else {
            j1 = i1;
        }

        if (this.size == this.maxSize) {
            this.keys[this.lru] = InodeCache.PLACEHOLDER;
            this.inodes[this.lru] = -2;
            this.lru = this.next[this.lru];
        } else {
            if (this.size == 0) {
                this.lru = (short) j1;
            }

            ++this.size;
        }

        int k1 = i & 32767;

        label99:
        while (true) {
            j = k1 % this.totalSlots;
            k = j;
            l = 1;
            flag = true;

            for (i1 = -1; (short0 = this.reverse[j]) != -1; flag = !flag) {
                short short3 = this.inodes[short0];

                if (short3 == -2) {
                    if (i1 == -1) {
                        i1 = j;
                    }
                } else if (short3 == k1) {
                    ++k1;
                    continue label99;
                }

                j = Math.abs((k + (flag ? 1 : -1) * l * l) % this.totalSlots);
                if (!flag) {
                    ++l;
                }
            }

            if (i1 != -1) {
                j = i1;
            }

            this.keys[j1] = object;
            this.reverse[j] = (short) j1;
            this.inodes[j1] = (short) k1;
            if (this.mru != -1) {
                this.prev[j1] = this.mru;
                this.next[this.mru] = (short) j1;
            }

            this.mru = (short) j1;
            return (short) k1;
        }
    }

    public Object reverse(short short0) {
        int i = short0 % this.totalSlots;
        int j = i;
        int k = 1;

        short short1;

        for (boolean flag = true; (short1 = this.reverse[i]) != -1; flag = !flag) {
            if (this.inodes[short1] == short0) {
                return this.keys[short1];
            }

            i = Math.abs((j + (flag ? 1 : -1) * k * k) % this.totalSlots);
            if (!flag) {
                ++k;
            }
        }

        return null;
    }
}
