package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerToggleFlightEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final boolean isFlying;
    private boolean cancel = false;

    public PlayerToggleFlightEvent(Player player, boolean isFlying) {
        super(player);
        this.isFlying = isFlying;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerToggleFlightEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerToggleFlightEvent.handlers;
    }
}
