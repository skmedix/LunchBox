package org.bukkit.event.player;

import org.bukkit.Achievement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerAchievementAwardedEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Achievement achievement;
    private boolean isCancelled = false;

    public PlayerAchievementAwardedEvent(Player player, Achievement achievement) {
        super(player);
        this.achievement = achievement;
    }

    public Achievement getAchievement() {
        return this.achievement;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerAchievementAwardedEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerAchievementAwardedEvent.handlers;
    }
}
