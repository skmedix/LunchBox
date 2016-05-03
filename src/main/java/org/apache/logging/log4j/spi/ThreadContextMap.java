package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ThreadContextMap {

    void put(String s, String s1);

    String get(String s);

    void remove(String s);

    void clear();

    boolean containsKey(String s);

    Map getCopy();

    Map getImmutableMapOrNull();

    boolean isEmpty();
}
