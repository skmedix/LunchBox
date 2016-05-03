package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum WorldType {

    NORMAL("DEFAULT"), FLAT("FLAT"), VERSION_1_1("DEFAULT_1_1"), LARGE_BIOMES("LARGEBIOMES"), AMPLIFIED("AMPLIFIED"), CUSTOMIZED("CUSTOMIZED");

    private static final Map BY_NAME = Maps.newHashMap();
    private final String name;

    static {
        WorldType[] aworldtype;
        int i = (aworldtype = values()).length;

        for (int j = 0; j < i; ++j) {
            WorldType type = aworldtype[j];

            WorldType.BY_NAME.put(type.name, type);
        }

    }

    private WorldType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static WorldType getByName(String name) {
        return (WorldType) WorldType.BY_NAME.get(name.toUpperCase());
    }
}
