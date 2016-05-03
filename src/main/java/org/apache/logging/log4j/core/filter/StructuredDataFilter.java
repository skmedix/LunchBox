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
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(
    name = "StructuredDataFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public final class StructuredDataFilter extends MapFilter {

    private StructuredDataFilter(Map map, boolean flag, Filter.Result filter_result, Filter.Result filter_result1) {
        super(map, flag, filter_result, filter_result1);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return message instanceof StructuredDataMessage ? this.filter((StructuredDataMessage) message) : Filter.Result.NEUTRAL;
    }

    public Filter.Result filter(LogEvent logevent) {
        Message message = logevent.getMessage();

        return message instanceof StructuredDataMessage ? this.filter((StructuredDataMessage) message) : super.filter(logevent);
    }

    protected Filter.Result filter(StructuredDataMessage structureddatamessage) {
        boolean flag = false;
        Iterator iterator = this.getMap().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            String s = this.getValue(structureddatamessage, (String) entry.getKey());

            if (s != null) {
                flag = ((List) entry.getValue()).contains(s);
            } else {
                flag = false;
            }

            if (!this.isAnd() && flag || this.isAnd() && !flag) {
                break;
            }
        }

        return flag ? this.onMatch : this.onMismatch;
    }

    private String getValue(StructuredDataMessage structureddatamessage, String s) {
        return s.equalsIgnoreCase("id") ? structureddatamessage.getId().toString() : (s.equalsIgnoreCase("id.name") ? structureddatamessage.getId().getName() : (s.equalsIgnoreCase("type") ? structureddatamessage.getType() : (s.equalsIgnoreCase("message") ? structureddatamessage.getFormattedMessage() : (String) structureddatamessage.getData().get(s))));
    }

    @PluginFactory
    public static StructuredDataFilter createFilter(@PluginElement("Pairs") KeyValuePair[] akeyvaluepair, @PluginAttribute("operator") String s, @PluginAttribute("onMatch") String s1, @PluginAttribute("onMismatch") String s2) {
        if (akeyvaluepair != null && akeyvaluepair.length != 0) {
            HashMap hashmap = new HashMap();
            KeyValuePair[] akeyvaluepair1 = akeyvaluepair;
            int i = akeyvaluepair.length;

            for (int j = 0; j < i; ++j) {
                KeyValuePair keyvaluepair = akeyvaluepair1[j];
                String s3 = keyvaluepair.getKey();

                if (s3 == null) {
                    StructuredDataFilter.LOGGER.error("A null key is not valid in MapFilter");
                } else {
                    String s4 = keyvaluepair.getValue();

                    if (s4 == null) {
                        StructuredDataFilter.LOGGER.error("A null value for key " + s3 + " is not allowed in MapFilter");
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
                StructuredDataFilter.LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
                return null;
            } else {
                boolean flag = s == null || !s.equalsIgnoreCase("or");
                Filter.Result filter_result = Filter.Result.toResult(s1);
                Filter.Result filter_result1 = Filter.Result.toResult(s2);

                return new StructuredDataFilter(hashmap, flag, filter_result, filter_result1);
            }
        } else {
            StructuredDataFilter.LOGGER.error("keys and values must be specified for the StructuredDataFilter");
            return null;
        }
    }
}
