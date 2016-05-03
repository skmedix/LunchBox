package org.bukkit.util.io;

import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

class Wrapper implements Serializable {

    private static final long serialVersionUID = -986209235411767547L;
    final Map map;

    static Wrapper newWrapper(ConfigurationSerializable obj) {
        return new Wrapper(ImmutableMap.builder().put("==", ConfigurationSerialization.getAlias(obj.getClass())).putAll(obj.serialize()).build());
    }

    private Wrapper(Map map) {
        this.map = map;
    }
}
