package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum CoalType {

    COAL(0), CHARCOAL(1);

    private final byte data;
    private static final Map BY_DATA = Maps.newHashMap();

    static {
        CoalType[] acoaltype;
        int i = (acoaltype = values()).length;

        for (int j = 0; j < i; ++j) {
            CoalType type = acoaltype[j];

            CoalType.BY_DATA.put(Byte.valueOf(type.data), type);
        }

    }

    private CoalType(int data) {
        this.data = (byte) data;
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.data;
    }

    /** @deprecated */
    @Deprecated
    public static CoalType getByData(byte data) {
        return (CoalType) CoalType.BY_DATA.get(Byte.valueOf(data));
    }
}
