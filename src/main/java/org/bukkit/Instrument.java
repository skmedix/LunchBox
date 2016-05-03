package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum Instrument {

    PIANO(0), BASS_DRUM(1), SNARE_DRUM(2), STICKS(3), BASS_GUITAR(4);

    private final byte type;
    private static final Map BY_DATA = Maps.newHashMap();

    static {
        Instrument[] ainstrument;
        int i = (ainstrument = values()).length;

        for (int j = 0; j < i; ++j) {
            Instrument instrument = ainstrument[j];

            Instrument.BY_DATA.put(Byte.valueOf(instrument.getType()), instrument);
        }

    }

    private Instrument(int type) {
        this.type = (byte) type;
    }

    /** @deprecated */
    @Deprecated
    public byte getType() {
        return this.type;
    }

    /** @deprecated */
    @Deprecated
    public static Instrument getByType(byte type) {
        return (Instrument) Instrument.BY_DATA.get(Byte.valueOf(type));
    }
}
