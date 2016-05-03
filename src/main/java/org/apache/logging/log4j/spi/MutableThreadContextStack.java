package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MutableThreadContextStack implements ThreadContextStack {

    private static final long serialVersionUID = 50505011L;
    private final List list;

    public MutableThreadContextStack(List list) {
        this.list = new ArrayList(list);
    }

    private MutableThreadContextStack(MutableThreadContextStack mutablethreadcontextstack) {
        this.list = new ArrayList(mutablethreadcontextstack.list);
    }

    public String pop() {
        if (this.list.isEmpty()) {
            return null;
        } else {
            int i = this.list.size() - 1;
            String s = (String) this.list.remove(i);

            return s;
        }
    }

    public String peek() {
        if (this.list.isEmpty()) {
            return null;
        } else {
            int i = this.list.size() - 1;

            return (String) this.list.get(i);
        }
    }

    public void push(String s) {
        this.list.add(s);
    }

    public int getDepth() {
        return this.list.size();
    }

    public List asList() {
        return this.list;
    }

    public void trim(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        } else if (this.list != null) {
            ArrayList arraylist = new ArrayList(this.list.size());
            int j = Math.min(i, this.list.size());

            for (int k = 0; k < j; ++k) {
                arraylist.add(this.list.get(k));
            }

            this.list.clear();
            this.list.addAll(arraylist);
        }
    }

    public ThreadContextStack copy() {
        return new MutableThreadContextStack(this);
    }

    public void clear() {
        this.list.clear();
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(Object object) {
        return this.list.contains(object);
    }

    public Iterator iterator() {
        return this.list.iterator();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public Object[] toArray(Object[] aobject) {
        return this.list.toArray(aobject);
    }

    public boolean add(String s) {
        return this.list.add(s);
    }

    public boolean remove(Object object) {
        return this.list.remove(object);
    }

    public boolean containsAll(Collection collection) {
        return this.list.containsAll(collection);
    }

    public boolean addAll(Collection collection) {
        return this.list.addAll(collection);
    }

    public boolean removeAll(Collection collection) {
        return this.list.removeAll(collection);
    }

    public boolean retainAll(Collection collection) {
        return this.list.retainAll(collection);
    }

    public String toString() {
        return String.valueOf(this.list);
    }
}
