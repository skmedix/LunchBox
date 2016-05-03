package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class HashTreeSet implements Set {

    private HashSet hash = new HashSet();
    private TreeSet tree = new TreeSet();

    public int size() {
        return this.hash.size();
    }

    public boolean isEmpty() {
        return this.hash.isEmpty();
    }

    public boolean contains(Object o) {
        return this.hash.contains(o);
    }

    public Iterator iterator() {
        return new Iterator() {
            private Iterator it;
            private Object last;

            {
                this.it = HashTreeSet.this.tree.iterator();
            }

            public boolean hasNext() {
                return this.it.hasNext();
            }

            public Object next() {
                return this.last = this.it.next();
            }

            public void remove() {
                if (this.last == null) {
                    throw new IllegalStateException();
                } else {
                    this.it.remove();
                    HashTreeSet.this.hash.remove(this.last);
                    this.last = null;
                }
            }
        };
    }

    public Object[] toArray() {
        return this.hash.toArray();
    }

    public Object[] toArray(Object[] a) {
        return this.hash.toArray(a);
    }

    public boolean add(Object e) {
        this.hash.add(e);
        return this.tree.add(e);
    }

    public boolean remove(Object o) {
        this.hash.remove(o);
        return this.tree.remove(o);
    }

    public boolean containsAll(Collection c) {
        return this.hash.containsAll(c);
    }

    public boolean addAll(Collection c) {
        this.tree.addAll(c);
        return this.hash.addAll(c);
    }

    public boolean retainAll(Collection c) {
        this.tree.retainAll(c);
        return this.hash.retainAll(c);
    }

    public boolean removeAll(Collection c) {
        this.tree.removeAll(c);
        return this.hash.removeAll(c);
    }

    public void clear() {
        this.hash.clear();
        this.tree.clear();
    }

    public Object first() {
        return this.tree.first();
    }
}
