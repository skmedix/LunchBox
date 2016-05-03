package org.apache.logging.log4j.core.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.Message;

@Plugin(
    name = "DynamicThresholdFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public final class DynamicThresholdFilter extends AbstractFilter {

    private Map levelMap = new HashMap();
    private Level defaultThreshold;
    private final String key;

    private DynamicThresholdFilter(String s, Map map, Level level, Filter.Result filter_result, Filter.Result filter_result1) {
        super(filter_result, filter_result1);
        this.defaultThreshold = Level.ERROR;
        if (s == null) {
            throw new NullPointerException("key cannot be null");
        } else {
            this.key = s;
            this.levelMap = map;
            this.defaultThreshold = level;
        }
    }

    public String getKey() {
        return this.key;
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String s, Object... aobject) {
        return this.filter(level);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable) {
        return this.filter(level);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return this.filter(level);
    }

    public Filter.Result filter(LogEvent logevent) {
        return this.filter(logevent.getLevel());
    }

    private Filter.Result filter(Level level) {
        String s = ThreadContext.get(this.key);

        if (s != null) {
            Level level1 = (Level) this.levelMap.get(s);

            if (level1 == null) {
                level1 = this.defaultThreshold;
            }

            return level.isAtLeastAsSpecificAs(level1) ? this.onMatch : this.onMismatch;
        } else {
            return Filter.Result.NEUTRAL;
        }
    }

    public Map getLevelMap() {
        return this.levelMap;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("key=").append(this.key);
        stringbuilder.append(", default=").append(this.defaultThreshold);
        if (this.levelMap.size() > 0) {
            stringbuilder.append("{");
            boolean flag = true;

            Entry entry;

            for (Iterator iterator = this.levelMap.entrySet().iterator(); iterator.hasNext(); stringbuilder.append((String) entry.getKey()).append("=").append(entry.getValue())) {
                entry = (Entry) iterator.next();
                if (!flag) {
                    stringbuilder.append(", ");
                    flag = false;
                }
            }

            stringbuilder.append("}");
        }

        return stringbuilder.toString();
    }

    @PluginFactory
    public static DynamicThresholdFilter createFilter(@PluginAttribute("key") String s, @PluginElement("Pairs") KeyValuePair[] akeyvaluepair, @PluginAttribute("defaultThreshold") String s1, @PluginAttribute("onMatch") String s2, @PluginAttribute("onMismatch") String s3) {
        Filter.Result filter_result = Filter.Result.toResult(s2);
        Filter.Result filter_result1 = Filter.Result.toResult(s3);
        HashMap hashmap = new HashMap();
        KeyValuePair[] akeyvaluepair1 = akeyvaluepair;
        int i = akeyvaluepair.length;

        for (int j = 0; j < i; ++j) {
            KeyValuePair keyvaluepair = akeyvaluepair1[j];

            hashmap.put(keyvaluepair.getKey(), Level.toLevel(keyvaluepair.getValue()));
        }

        Level level = Level.toLevel(s1, Level.ERROR);

        return new DynamicThresholdFilter(s, hashmap, level, filter_result, filter_result1);
    }
}
