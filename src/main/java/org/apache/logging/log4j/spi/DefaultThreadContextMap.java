package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultThreadContextMap implements ThreadContextMap {

    private final boolean useMap;
    private final ThreadLocal localMap = new InheritableThreadLocal() {
        protected Map childValue(Map map) {
            return map != null && DefaultThreadContextMap.this.useMap ? Collections.unmodifiableMap(new HashMap(map)) : null;
        }
    };

    public DefaultThreadContextMap(boolean flag) {
        this.useMap = flag;
    }

    public void put(String s, String s1) {
        if (this.useMap) {
            Map map = (Map) this.localMap.get();
            HashMap hashmap = map == null ? new HashMap() : new HashMap(map);

            hashmap.put(s, s1);
            this.localMap.set(Collections.unmodifiableMap(hashmap));
        }
    }

    public String get(String s) {
        Map map = (Map) this.localMap.get();

        return map == null ? null : (String) map.get(s);
    }

    public void remove(String s) {
        Map map = (Map) this.localMap.get();

        if (map != null) {
            HashMap hashmap = new HashMap(map);

            hashmap.remove(s);
            this.localMap.set(Collections.unmodifiableMap(hashmap));
        }

    }

    public void clear() {
        this.localMap.remove();
    }

    public boolean containsKey(String s) {
        Map map = (Map) this.localMap.get();

        return map != null && map.containsKey(s);
    }

    public Map getCopy() {
        Map map = (Map) this.localMap.get();

        return map == null ? new HashMap() : new HashMap(map);
    }

    public Map getImmutableMapOrNull() {
        return (Map) this.localMap.get();
    }

    public boolean isEmpty() {
        Map map = (Map) this.localMap.get();

        return map == null || map.size() == 0;
    }

    public String toString() {
        Map map = (Map) this.localMap.get();

        return map == null ? "{}" : map.toString();
    }
}
