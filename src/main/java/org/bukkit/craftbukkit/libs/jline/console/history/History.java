package org.bukkit.craftbukkit.libs.jline.console.history;

import java.util.Iterator;
import java.util.ListIterator;

public interface History extends Iterable {

    int size();

    boolean isEmpty();

    int index();

    void clear();

    CharSequence get(int i);

    void add(CharSequence charsequence);

    void set(int i, CharSequence charsequence);

    CharSequence remove(int i);

    CharSequence removeFirst();

    CharSequence removeLast();

    void replace(CharSequence charsequence);

    ListIterator entries(int i);

    ListIterator entries();

    Iterator iterator();

    CharSequence current();

    boolean previous();

    boolean next();

    boolean moveToFirst();

    boolean moveToLast();

    boolean moveTo(int i);

    void moveToEnd();

    public interface Entry {

        int index();

        CharSequence value();
    }
}
