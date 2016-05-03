package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum TreeSpecies {

    GENERIC(0), REDWOOD(1), BIRCH(2), JUNGLE(3), ACACIA(4), DARK_OAK(5);

    private final byte data;
    private static final Map BY_DATA = Maps.newHashMap();

    static {
        TreeSpecies[] atreespecies;
        int i = (atreespecies = values()).length;

        for (int j = 0; j < i; ++j) {
            TreeSpecies species = atreespecies[j];

            TreeSpecies.BY_DATA.put(Byte.valueOf(species.data), species);
        }

    }

    private TreeSpecies(int data) {
        this.data = (byte) data;
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.data;
    }

    /** @deprecated */
    @Deprecated
    public static TreeSpecies getByData(byte data) {
        return (TreeSpecies) TreeSpecies.BY_DATA.get(Byte.valueOf(data));
    }
}
