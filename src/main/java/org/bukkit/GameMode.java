package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum GameMode {

    CREATIVE(1), SURVIVAL(0), ADVENTURE(2), SPECTATOR(3);

    private final int value;
    private static final Map BY_ID = Maps.newHashMap();

    static {
        GameMode[] agamemode;
        int i = (agamemode = values()).length;

        for (int j = 0; j < i; ++j) {
            GameMode mode = agamemode[j];

            GameMode.BY_ID.put(Integer.valueOf(mode.getValue()), mode);
        }

    }

    private GameMode(int value) {
        this.value = value;
    }

    /** @deprecated */
    @Deprecated
    public int getValue() {
        return this.value;
    }

    /** @deprecated */
    @Deprecated
    public static GameMode getByValue(int value) {
        return (GameMode) GameMode.BY_ID.get(Integer.valueOf(value));
    }
}
