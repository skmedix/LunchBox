package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerLevelChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final int oldLevel;
    private final int newLevel;

    public PlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
        super(player);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public int getOldLevel() {
        return this.oldLevel;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public HandlerList getHandlers() {
        return PlayerLevelChangeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerLevelChangeEvent.handlers;
    }
}
