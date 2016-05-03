package org.apache.logging.log4j.core.appender.rewrite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "MapRewritePolicy",
    category = "Core",
    elementType = "rewritePolicy",
    printObject = true
)
public final class MapRewritePolicy implements RewritePolicy {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private final Map map;
    private final MapRewritePolicy.Mode mode;

    private MapRewritePolicy(Map map, MapRewritePolicy.Mode maprewritepolicy_mode) {
        this.map = map;
        this.mode = maprewritepolicy_mode;
    }

    public LogEvent rewrite(LogEvent logevent) {
        Message message = logevent.getMessage();

        if (message != null && message instanceof MapMessage) {
            HashMap hashmap = new HashMap(((MapMessage) message).getData());

            switch (MapRewritePolicy.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$appender$rewrite$MapRewritePolicy$Mode[this.mode.ordinal()]) {
            case 1:
                hashmap.putAll(this.map);
                break;

            default:
                Iterator iterator = this.map.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();

                    if (hashmap.containsKey(entry.getKey())) {
                        hashmap.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            MapMessage mapmessage = ((MapMessage) message).newInstance(hashmap);

            if (logevent instanceof Log4jLogEvent) {
                Log4jLogEvent log4jlogevent = (Log4jLogEvent) logevent;

                return Log4jLogEvent.createEvent(log4jlogevent.getLoggerName(), log4jlogevent.getMarker(), log4jlogevent.getFQCN(), log4jlogevent.getLevel(), mapmessage, log4jlogevent.getThrownProxy(), log4jlogevent.getContextMap(), log4jlogevent.getContextStack(), log4jlogevent.getThreadName(), log4jlogevent.getSource(), log4jlogevent.getMillis());
            } else {
                return new Log4jLogEvent(logevent.getLoggerName(), logevent.getMarker(), logevent.getFQCN(), logevent.getLevel(), mapmessage, logevent.getThrown(), logevent.getContextMap(), logevent.getContextStack(), logevent.getThreadName(), logevent.getSource(), logevent.getMillis());
            }
        } else {
            return logevent;
        }
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("mode=").append(this.mode);
        stringbuilder.append(" {");
        boolean flag = true;

        for (Iterator iterator = this.map.entrySet().iterator(); iterator.hasNext(); flag = false) {
            Entry entry = (Entry) iterator.next();

            if (!flag) {
                stringbuilder.append(", ");
            }

            stringbuilder.append((String) entry.getKey()).append("=").append((String) entry.getValue());
        }

        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    @PluginFactory
    public static MapRewritePolicy createPolicy(@PluginAttribute("mode") String s, @PluginElement("KeyValuePair") KeyValuePair[] akeyvaluepair) {
        MapRewritePolicy.Mode maprewritepolicy_mode;

        if (s == null) {
            maprewritepolicy_mode = MapRewritePolicy.Mode.Add;
        } else {
            maprewritepolicy_mode = MapRewritePolicy.Mode.valueOf(s);
            if (maprewritepolicy_mode == null) {
                MapRewritePolicy.LOGGER.error("Undefined mode " + s);
                return null;
            }
        }

        if (akeyvaluepair != null && akeyvaluepair.length != 0) {
            HashMap hashmap = new HashMap();
            KeyValuePair[] akeyvaluepair1 = akeyvaluepair;
            int i = akeyvaluepair.length;

            for (int j = 0; j < i; ++j) {
                KeyValuePair keyvaluepair = akeyvaluepair1[j];
                String s1 = keyvaluepair.getKey();

                if (s1 == null) {
                    MapRewritePolicy.LOGGER.error("A null key is not valid in MapRewritePolicy");
                } else {
                    String s2 = keyvaluepair.getValue();

                    if (s2 == null) {
                        MapRewritePolicy.LOGGER.error("A null value for key " + s1 + " is not allowed in MapRewritePolicy");
                    } else {
                        hashmap.put(keyvaluepair.getKey(), keyvaluepair.getValue());
                    }
                }
            }

            if (hashmap.size() == 0) {
                MapRewritePolicy.LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
                return null;
            } else {
                return new MapRewritePolicy(hashmap, maprewritepolicy_mode);
            }
        } else {
            MapRewritePolicy.LOGGER.error("keys and values must be specified for the MapRewritePolicy");
            return null;
        }
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$apache$logging$log4j$core$appender$rewrite$MapRewritePolicy$Mode = new int[MapRewritePolicy.Mode.values().length];

        static {
            try {
                MapRewritePolicy.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$appender$rewrite$MapRewritePolicy$Mode[MapRewritePolicy.Mode.Add.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

        }
    }

    public static enum Mode {

        Add, Update;
    }
}
