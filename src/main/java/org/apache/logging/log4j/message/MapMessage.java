package org.apache.logging.log4j.message;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.logging.log4j.util.EnglishEnums;

public class MapMessage implements MultiformatMessage {

    private static final long serialVersionUID = -5031471831131487120L;
    private final SortedMap data;

    public MapMessage() {
        this.data = new TreeMap();
    }

    public MapMessage(Map map) {
        this.data = (SortedMap) (map instanceof SortedMap ? (SortedMap) map : new TreeMap(map));
    }

    public String[] getFormats() {
        String[] astring = new String[MapMessage.MapFormat.values().length];
        int i = 0;
        MapMessage.MapFormat[] amapmessage_mapformat = MapMessage.MapFormat.values();
        int j = amapmessage_mapformat.length;

        for (int k = 0; k < j; ++k) {
            MapMessage.MapFormat mapmessage_mapformat = amapmessage_mapformat[k];

            astring[i++] = mapmessage_mapformat.name();
        }

        return astring;
    }

    public Object[] getParameters() {
        return this.data.values().toArray();
    }

    public String getFormat() {
        return "";
    }

    public Map getData() {
        return Collections.unmodifiableMap(this.data);
    }

    public void clear() {
        this.data.clear();
    }

    public void put(String s, String s1) {
        if (s1 == null) {
            throw new IllegalArgumentException("No value provided for key " + s);
        } else {
            this.validate(s, s1);
            this.data.put(s, s1);
        }
    }

    protected void validate(String s, String s1) {}

    public void putAll(Map map) {
        this.data.putAll(map);
    }

    public String get(String s) {
        return (String) this.data.get(s);
    }

    public String remove(String s) {
        return (String) this.data.remove(s);
    }

    public String asString() {
        return this.asString((MapMessage.MapFormat) null);
    }

    public String asString(String s) {
        try {
            return this.asString((MapMessage.MapFormat) EnglishEnums.valueOf(MapMessage.MapFormat.class, s));
        } catch (IllegalArgumentException illegalargumentexception) {
            return this.asString();
        }
    }

    private String asString(MapMessage.MapFormat mapmessage_mapformat) {
        StringBuilder stringbuilder = new StringBuilder();

        if (mapmessage_mapformat == null) {
            this.appendMap(stringbuilder);
        } else {
            switch (MapMessage.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$message$MapMessage$MapFormat[mapmessage_mapformat.ordinal()]) {
            case 1:
                this.asXML(stringbuilder);
                break;

            case 2:
                this.asJSON(stringbuilder);
                break;

            case 3:
                this.asJava(stringbuilder);
                break;

            default:
                this.appendMap(stringbuilder);
            }
        }

        return stringbuilder.toString();
    }

    public void asXML(StringBuilder stringbuilder) {
        stringbuilder.append("<Map>\n");
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            stringbuilder.append("  <Entry key=\"").append((String) entry.getKey()).append("\">").append((String) entry.getValue()).append("</Entry>\n");
        }

        stringbuilder.append("</Map>");
    }

    public String getFormattedMessage() {
        return this.asString();
    }

    public String getFormattedMessage(String[] astring) {
        if (astring != null && astring.length != 0) {
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];
                MapMessage.MapFormat[] amapmessage_mapformat = MapMessage.MapFormat.values();
                int k = amapmessage_mapformat.length;

                for (int l = 0; l < k; ++l) {
                    MapMessage.MapFormat mapmessage_mapformat = amapmessage_mapformat[l];

                    if (mapmessage_mapformat.name().equalsIgnoreCase(s)) {
                        return this.asString(mapmessage_mapformat);
                    }
                }
            }

            return this.asString();
        } else {
            return this.asString();
        }
    }

    protected void appendMap(StringBuilder stringbuilder) {
        boolean flag = true;
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!flag) {
                stringbuilder.append(" ");
            }

            flag = false;
            stringbuilder.append((String) entry.getKey()).append("=\"").append((String) entry.getValue()).append("\"");
        }

    }

    protected void asJSON(StringBuilder stringbuilder) {
        boolean flag = true;

        stringbuilder.append("{");
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!flag) {
                stringbuilder.append(", ");
            }

            flag = false;
            stringbuilder.append("\"").append((String) entry.getKey()).append("\":");
            stringbuilder.append("\"").append((String) entry.getValue()).append("\"");
        }

        stringbuilder.append("}");
    }

    protected void asJava(StringBuilder stringbuilder) {
        boolean flag = true;

        stringbuilder.append("{");
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!flag) {
                stringbuilder.append(", ");
            }

            flag = false;
            stringbuilder.append((String) entry.getKey()).append("=\"").append((String) entry.getValue()).append("\"");
        }

        stringbuilder.append("}");
    }

    public MapMessage newInstance(Map map) {
        return new MapMessage(map);
    }

    public String toString() {
        return this.asString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            MapMessage mapmessage = (MapMessage) object;

            return this.data.equals(mapmessage.data);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.data.hashCode();
    }

    public Throwable getThrowable() {
        return null;
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$apache$logging$log4j$message$MapMessage$MapFormat = new int[MapMessage.MapFormat.values().length];

        static {
            try {
                MapMessage.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$message$MapMessage$MapFormat[MapMessage.MapFormat.XML.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                MapMessage.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$message$MapMessage$MapFormat[MapMessage.MapFormat.JSON.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                MapMessage.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$message$MapMessage$MapFormat[MapMessage.MapFormat.JAVA.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

        }
    }

    public static enum MapFormat {

        XML, JSON, JAVA;
    }
}
