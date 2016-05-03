package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum SandstoneType {

    CRACKED(0), GLYPHED(1), SMOOTH(2);

    private final byte data;
    private static final Map BY_DATA = Maps.newHashMap();

    static {
        SandstoneType[] asandstonetype;
        int i = (asandstonetype = values()).length;

        for (int j = 0; j < i; ++j) {
            SandstoneType type = asandstonetype[j];

            SandstoneType.BY_DATA.put(Byte.valueOf(type.data), type);
        }

    }

    private SandstoneType(int data) {
        this.data = (byte) data;
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.data;
    }

    /** @deprecated */
    @Deprecated
    public static SandstoneType getByData(byte data) {
        return (SandstoneType) SandstoneType.BY_DATA.get(Byte.valueOf(data));
    }
}
