package org.apache.commons.lang3.text;

import java.util.Map;
import java.util.Properties;

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
        Object object = null;

        try {
            Properties properties = System.getProperties();

            object = new StrLookup.MapStrLookup(properties);
        } catch (SecurityException securityexception) {
            object = StrLookup.NONE_LOOKUP;
        }

        SYSTEM_PROPERTIES_LOOKUP = (StrLookup) object;
    }

    static class MapStrLookup extends StrLookup {

        private final Map map;

        MapStrLookup(Map map) {
            this.map = map;
        }

        public String lookup(String s) {
            if (this.map == null) {
                return null;
            } else {
                Object object = this.map.get(s);

                return object == null ? null : object.toString();
            }
        }
    }
}
