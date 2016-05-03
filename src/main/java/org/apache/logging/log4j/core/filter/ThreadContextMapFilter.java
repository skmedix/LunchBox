package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    name = "ThreadContextMapFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public class ThreadContextMapFilter extends MapFilter {

    private final String key;
    private final String value;
    private final boolean useMap;

    public ThreadContextMapFilter(Map map, boolean flag, Filter.Result filter_result, Filter.Result filter_result1) {
        super(map, flag, filter_result, filter_result1);
        if (map.size() == 1) {
            Iterator iterator = map.entrySet().iterator();
            Entry entry = (Entry) iterator.next();

            if (((List) entry.getValue()).size() == 1) {
                this.key = (String) entry.getKey();
                this.value = (String) ((List) entry.getValue()).get(0);
                this.useMap = false;
            } else {
                this.key = null;
                this.value = null;
                this.useMap = true;
            }
        } else {
            this.key = null;
            this.value = null;
            this.useMap = true;
        }

    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String s, Object... aobject) {
        return this.filter();
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable) {
        return this.filter();
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return this.filter();
    }

    private Filter.Result filter() {
        boolean flag = false;

        if (this.useMap) {
            Iterator iterator = this.getMap().entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                String s = ThreadContext.get((String) entry.getKey());

                if (s != null) {
                    flag = ((List) entry.getValue()).contains(s);
                } else {
                    flag = false;
                }

                if (!this.isAnd() && flag || this.isAnd() && !flag) {
                    break;
                }
            }
        } else {
            flag = this.value.equals(ThreadContext.get(this.key));
        }

        return flag ? this.onMatch : this.onMismatch;
    }

    public Filter.Result filter(LogEvent logevent) {
        return super.filter(logevent.getContextMap()) ? this.onMatch : this.onMismatch;
    }

    @PluginFactory
    public static ThreadContextMapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] akeyvaluepair, @PluginAttribute("operator") String s, @PluginAttribute("onMatch") String s1, @PluginAttribute("onMismatch") String s2) {
        if (akeyvaluepair != null && akeyvaluepair.length != 0) {
            HashMap hashmap = new HashMap();
            KeyValuePair[] akeyvaluepair1 = akeyvaluepair;
            int i = akeyvaluepair.length;

            for (int j = 0; j < i; ++j) {
                KeyValuePair keyvaluepair = akeyvaluepair1[j];
                String s3 = keyvaluepair.getKey();

                if (s3 == null) {
                    ThreadContextMapFilter.LOGGER.error("A null key is not valid in MapFilter");
                } else {
                    String s4 = keyvaluepair.getValue();

                    if (s4 == null) {
                        ThreadContextMapFilter.LOGGER.error("A null value for key " + s3 + " is not allowed in MapFilter");
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
                ThreadContextMapFilter.LOGGER.error("ThreadContextMapFilter is not configured with any valid key value pairs");
                return null;
            } else {
                boolean flag = s == null || !s.equalsIgnoreCase("or");
                Filter.Result filter_result = Filter.Result.toResult(s1);
                Filter.Result filter_result1 = Filter.Result.toResult(s2);

                return new ThreadContextMapFilter(hashmap, flag, filter_result, filter_result1);
            }
        } else {
            ThreadContextMapFilter.LOGGER.error("key and value pairs must be specified for the ThreadContextMapFilter");
            return null;
        }
    }
}
