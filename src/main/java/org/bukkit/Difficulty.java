package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum Difficulty {

    PEACEFUL(0), EASY(1), NORMAL(2), HARD(3);

    private final int value;
    private static final Map BY_ID = Maps.newHashMap();

    static {
        Difficulty[] adifficulty;
        int i = (adifficulty = values()).length;

        for (int j = 0; j < i; ++j) {
            Difficulty diff = adifficulty[j];

            Difficulty.BY_ID.put(Integer.valueOf(diff.value), diff);
        }

    }

    private Difficulty(int value) {
        this.value = value;
    }

    /** @deprecated */
    @Deprecated
    public int getValue() {
        return this.value;
    }

    /** @deprecated */
    @Deprecated
    public static Difficulty getByValue(int value) {
        return (Difficulty) Difficulty.BY_ID.get(Integer.valueOf(value));
    }
}
