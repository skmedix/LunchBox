package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;

@Plugin(
    name = "MapFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public class MapFilter extends AbstractFilter {

    private final Map map;
    private final boolean isAnd;

    protected MapFilter(Map map, boolean flag, Filter.Result filter_result, Filter.Result filter_result1) {
        super(filter_result, filter_result1);
        if (map == null) {
            throw new NullPointerException("key cannot be null");
        } else {
            this.isAnd = flag;
            this.map = map;
        }
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return message instanceof MapMessage ? (this.filter(((MapMessage) message).getData()) ? this.onMatch : this.onMismatch) : Filter.Result.NEUTRAL;
    }

    public Filter.Result filter(LogEvent logevent) {
        Message message = logevent.getMessage();

        return message instanceof MapMessage ? (this.filter(((MapMessage) message).getData()) ? this.onMatch : this.onMismatch) : Filter.Result.NEUTRAL;
    }

    protected boolean filter(Map map) {
        boolean flag = false;
        Iterator iterator = this.map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            String s = (String) map.get(entry.getKey());

            if (s != null) {
                flag = ((List) entry.getValue()).contains(s);
            } else {
                flag = false;
            }

            if (!this.isAnd && flag || this.isAnd && !flag) {
                break;
            }
        }

        return flag;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("isAnd=").append(this.isAnd);
        if (this.map.size() > 0) {
            stringbuilder.append(", {");
            boolean flag = true;
            Iterator iterator = this.map.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                if (!flag) {
                    stringbuilder.append(", ");
                }

                flag = false;
                List list = (List) entry.getValue();
                String s = list.size() > 1 ? (String) list.get(0) : list.toString();

                stringbuilder.append((String) entry.getKey()).append("=").append(s);
            }

            stringbuilder.append("}");
        }

        return stringbuilder.toString();
    }

    protected boolean isAnd() {
        return this.isAnd;
    }

    protected Map getMap() {
        return this.map;
    }

    @PluginFactory
    public static MapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] akeyvaluepair, @PluginAttribute("operator") String s, @PluginAttribute("onMatch") String s1, @PluginAttribute("onMismatch") String s2) {
        if (akeyvaluepair != null && akeyvaluepair.length != 0) {
            HashMap hashmap = new HashMap();
            KeyValuePair[] akeyvaluepair1 = akeyvaluepair;
            int i = akeyvaluepair.length;

            for (int j = 0; j < i; ++j) {
                KeyValuePair keyvaluepair = akeyvaluepair1[j];
                String s3 = keyvaluepair.getKey();

                if (s3 == null) {
                    MapFilter.LOGGER.error("A null key is not valid in MapFilter");
                } else {
                    String s4 = keyvaluepair.getValue();

                    if (s4 == null) {
                        MapFilter.LOGGER.error("A null value for key " + s3 + " is not allowed in MapFilter");
                    } else {
                        List list = (List) hashmap.get(keyvaluepair.getKey());

                        if (list != null) {
                            list.add(s4);
                        } else {
                            ArrayList arraylist = new ArrayList();

                            arraylist.add(s4);
                            hashmap.put(keyvaluepair.getKey(), arraylist);
                        }
                    }
                }
            }

            if (hashmap.size() == 0) {
                MapFilter.LOGGER.error("MapFilter is not configured with any valid key value pairs");
                return null;
            } else {
                boolean flag = s == null || !s.equalsIgnoreCase("or");
                Filter.Result filter_result = Filter.Result.toResult(s1);
                Filter.Result filter_result1 = Filter.Result.toResult(s2);

                return new MapFilter(hashmap, flag, filter_result, filter_result1);
            }
        } else {
            MapFilter.LOGGER.error("keys and values must be specified for the MapFilter");
            return null;
        }
    }
}
