package org.apache.commons.lang.text;

import java.util.Map;

public abstract class StrLookup {

    private static final StrLookup NONE_LOOKUP = new StrLookup.MapStrLookup((Map) null);
    private static final StrLookup SYSTEM_PROPERTIES_LOOKUP;

    public static StrLookup noneLookup() {
        return StrLookup.NONE_LOOKUP;
    }

    public static StrLookup systemPropertiesLookup() {
        return StrLookup.SYSTEM_PROPERTIES_LOOKUP;
    }

    public static StrLookup mapLookup(Map map) {
        return new StrLookup.MapStrLookup(map);
    }

    public abstract String lookup(String s);

    static {
        Object lookup = null;

        try {
            lookup = new StrLookup.MapStrLookup(System.getProperties());
        } catch (SecurityException securityexception) {
            lookup = StrLookup.NONE_LOOKUP;
        }

        SYSTEM_PROPERTIES_LOOKUP = (StrLookup) lookup;
    }

    static class MapStrLookup extends StrLookup {

        private final Map map;

        MapStrLookup(Map map) {
            this.map = map;
        }

        public String lookup(String key) {
            if (this.map == null) {
                return null;
            } else {
                Object obj = this.map.get(key);

                return obj == null ? null : obj.toString();
            }
        }
    }
}
