package org.apache.logging.log4j.core.config.plugins;

import java.util.HashMap;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(
    name = "properties",
    category = "Core",
    printObject = true
)
public final class PropertiesPlugin {

    @PluginFactory
    public static StrLookup configureSubstitutor(@PluginElement("Properties") Property[] aproperty, @PluginConfiguration Configuration configuration) {
        if (aproperty == null) {
            return new Interpolator((StrLookup) null);
        } else {
            HashMap hashmap = new HashMap(configuration.getProperties());
            Property[] aproperty1 = aproperty;
            int i = aproperty.length;

            for (int j = 0; j < i; ++j) {
                Property property = aproperty1[j];

                hashmap.put(property.getName(), property.getValue());
            }

            return new Interpolator(new MapLookup(hashmap));
        }
    }
}
