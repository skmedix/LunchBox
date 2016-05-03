package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class DefaultThreadContextStack implements ThreadContextStack {

    private static final long serialVersionUID = 5050501L;
    private static ThreadLocal stack = new ThreadLocal();
    private final boolean useStack;

    public DefaultThreadContextStack(boolean flag) {
        this.useStack = flag;
    }

    public String pop() {
        if (!this.useStack) {
            return "";
        } else {
            List list = (List) DefaultThreadContextStack.stack.get();

            if (list != null && list.size() != 0) {
                ArrayList arraylist = new ArrayList(list);
                int i = arraylist.size() - 1;
                String s = (String) arraylist.remove(i);

                DefaultThreadContextStack.stack.set(Collections.unmodifiableList(arraylist));
                return s;
            } else {
                throw new NoSuchElementException("The ThreadContext stack is empty");
            }
        }
    }

    public String peek() {
        List list = (List) DefaultThreadContextStack.stack.get();

        if (list != null && list.size() != 0) {
            int i = list.size() - 1;

            return (String) list.get(i);
        } else {
            return null;
        }
    }

    public void push(String s) {
        if (this.useStack) {
            this.add(s);
        }
    }

    public int getDepth() {
        List list = (List) DefaultThreadContextStack.stack.get();

        return list == null ? 0 : list.size();
    }

    public List asList() {
        List list = (List) DefaultThreadContextStack.stack.get();

        return list == null ? Collections.emptyList() : list;
    }

    public void trim(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        } else {
            List list = (List) DefaultThreadContextStack.stack.get();

            if (list != null) {
                ArrayList arraylist = new ArrayList();
                int j = Math.min(i, list.size());

                for (int k = 0; k < j; ++k) {
                    arraylist.add(list.get(k));
                }

                DefaultThreadContextStack.stack.set(arraylist);
            }
        }
    }

    public ThreadContextStack copy() {
        List list = null;

        return this.useStack && (list = (List) DefaultThreadContextStack.stack.get()) != null ? new MutableThreadContextStack(list) : new MutableThreadContextStack(new ArrayList());
    }

    public void clear() {
        DefaultThreadContextStack.stack.remove();
    }

    public int size() {
        List list = (List) DefaultThreadContextStack.stack.get();

        return list == null ? 0 : list.size();
    }

    public boolean isEmpty() {
        List list = (List) DefaultThreadContextStack.stack.get();

        return list == null || list.isEmpty();
    }

    public boolean contains(Object object) {
        List list = (List) DefaultThreadContextStack.stack.get();

        return list != null && list.contains(object);
    }

    public Iterator iterator() {
        List list = (List) DefaultThreadContextStack.stack.get();

        if (list == null) {
            List list1 = Collections.emptyList();

            return list1.iterator();
        } else {
            return list.iterator();
        }
    }

    public Object[] toArray() {
        List list = (List) DefaultThreadContextStack.stack.get();

        return (Object[]) (list == null ? new String[0] : list.toArray(new Object[list.size()]));
    }

    public Object[] toArray(Object[] aobject) {
        List list = (List) DefaultThreadContextStack.stack.get();

        if (list == null) {
            if (aobject.length > 0) {
                aobject[0] = null;
            }

            return aobject;
        } else {
            return list.toArray(aobject);
        }
    }

    public boolean add(String s) {
        if (!this.useStack) {
            return false;
        } else {
            List list = (List) DefaultThreadContextStack.stack.get();
            ArrayList arraylist = list == null ? new ArrayList() : new ArrayList(list);

            arraylist.add(s);
            DefaultThreadContextStack.stack.set(Collections.unmodifiableList(arraylist));
            return true;
        }
    }

    public boolean remove(Object object) {
        if (!this.useStack) {
            return false;
        } else {
            List list = (List) DefaultThreadContextStack.stack.get();

            if (list != null && list.size() != 0) {
                ArrayList arraylist = new ArrayList(list);
                boolean flag = arraylist.remove(object);

                DefaultThreadContextStack.stack.set(Collections.unmodifiableList(arraylist));
                return flag;
            } else {
                return false;
            }
        }
    }

    public boolean containsAll(Collection collection) {
        if (collection.isEmpty()) {
            return true;
        } else {
            List list = (List) DefaultThreadContextStack.stack.get();

            return list != null && list.containsAll(collection);
        }
    }

    public boolean addAll(Collection collection) {
        if (this.useStack && !collection.isEmpty()) {
            List list = (List) DefaultThreadContextStack.stack.get();
            ArrayList arraylist = list == null ? new ArrayList() : new ArrayList(list);

            arraylist.addAll(collection);
            DefaultThreadContextStack.stack.set(Collections.unmodifiableList(arraylist));
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAll(Collection collection) {
        if (this.useStack && !collection.isEmpty()) {
            List list = (List) DefaultThreadContextStack.stack.get();

            if (list != null && !list.isEmpty()) {
                ArrayList arraylist = new ArrayList(list);
                boolean flag = arraylist.removeAll(collection);

                DefaultThreadContextStack.stack.set(Collections.unmodifiableList(arraylist));
                return flag;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean retainAll(Collection collection) {
        if (this.useStack && !collection.isEmpty()) {
            List list = (List) DefaultThreadContextStack.stack.get();

            if (list != null && !list.isEmpty()) {
                ArrayList arraylist = new ArrayList(list);
                boolean flag = arraylist.retainAll(collection);

                DefaultThreadContextStack.stack.set(Collections.unmodifiableList(arraylist));
                return flag;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        List list = (List) DefaultThreadContextStack.stack.get();

        return list == null ? "[]" : list.toString();
    }
}
