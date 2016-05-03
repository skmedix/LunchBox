package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum GrassSpecies {

    DEAD(0), NORMAL(1), FERN_LIKE(2);

    private final byte data;
    private static final Map BY_DATA = Maps.newHashMap();

    static {
        GrassSpecies[] agrassspecies;
        int i = (agrassspecies = values()).length;

        for (int j = 0; j < i; ++j) {
            GrassSpecies grassSpecies = agrassspecies[j];

            GrassSpecies.BY_DATA.put(Byte.valueOf(grassSpecies.getData()), grassSpecies);
        }

    }

    private GrassSpecies(int data) {
        this.data = (byte) data;
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.data;
    }

    /** @deprecated */
    @Deprecated
    public static GrassSpecies getByData(byte data) {
        return (GrassSpecies) GrassSpecies.BY_DATA.get(Byte.valueOf(data));
    }
}
