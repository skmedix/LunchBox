package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import com.google.common.collect.ImmutableBiMap;
import net.minecraft.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;

class CraftScoreboardTranslations {

    static final int MAX_DISPLAY_SLOT = 3;
    static ImmutableBiMap SLOTS = ImmutableBiMap.of(DisplaySlot.BELOW_NAME, "belowName", DisplaySlot.PLAYER_LIST, "list", DisplaySlot.SIDEBAR, "sidebar");

    static DisplaySlot toBukkitSlot(int i) {
        return (DisplaySlot) CraftScoreboardTranslations.SLOTS.inverse().get(Scoreboard.getObjectiveDisplaySlot(i));
    }

    static int fromBukkitSlot(DisplaySlot slot) {
        return Scoreboard.getObjectiveDisplaySlotNumber((String) CraftScoreboardTranslations.SLOTS.get(slot));
    }
}
