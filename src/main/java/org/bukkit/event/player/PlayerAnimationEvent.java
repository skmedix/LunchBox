package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerAnimationEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final PlayerAnimationType animationType;
    private boolean isCancelled = false;

    public PlayerAnimationEvent(Player player) {
        super(player);
        this.animationType = PlayerAnimationType.ARM_SWING;
    }

    public PlayerAnimationType getAnimationType() {
        return this.animationType;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerAnimationEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerAnimationEvent.handlers;
    }
}
