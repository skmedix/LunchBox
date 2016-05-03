package org.apache.logging.log4j.core.layout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.StructuredDataId;

@Plugin(
    name = "LoggerFields",
    category = "Core",
    printObject = true
)
public final class LoggerFields {

    private final Map map;
    private final String sdId;
    private final String enterpriseId;
    private final boolean discardIfAllFieldsAreEmpty;

    private LoggerFields(Map map, String s, String s1, boolean flag) {
        this.sdId = s;
        this.enterpriseId = s1;
        this.map = Collections.unmodifiableMap(map);
        this.discardIfAllFieldsAreEmpty = flag;
    }

    public Map getMap() {
        return this.map;
    }

    public String toString() {
        return this.map.toString();
    }

    @PluginFactory
    public static LoggerFields createLoggerFields(@PluginElement("LoggerFields") KeyValuePair[] akeyvaluepair, @PluginAttribute("sdId") String s, @PluginAttribute("enterpriseId") String s1, @PluginAttribute("discardIfAllFieldsAreEmpty") String s2) {
        HashMap hashmap = new HashMap();
        KeyValuePair[] akeyvaluepair1 = akeyvaluepair;
        int i = akeyvaluepair.length;

        for (int j = 0; j < i; ++j) {
            KeyValuePair keyvaluepair = akeyvaluepair1[j];

            hashmap.put(keyvaluepair.getKey(), keyvaluepair.getValue());
        }

        boolean flag = Booleans.parseBoolean(s2, false);

        return new LoggerFields(hashmap, s, s1, flag);
    }

    public StructuredDataId getSdId() {
        if (this.enterpriseId != null && this.sdId != null) {
            int i = Integer.parseInt(this.enterpriseId);

            return new StructuredDataId(this.sdId, i, (String[]) null, (String[]) null);
        } else {
            return null;
        }
    }

    public boolean getDiscardIfAllFieldsAreEmpty() {
        return this.discardIfAllFieldsAreEmpty;
    }
}
