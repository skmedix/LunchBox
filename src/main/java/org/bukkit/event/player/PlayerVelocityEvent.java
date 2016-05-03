package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class PlayerVelocityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private Vector velocity;

    public PlayerVelocityEvent(Player player, Vector velocity) {
        super(player);
        this.velocity = velocity;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public HandlerList getHandlers() {
        return PlayerVelocityEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerVelocityEvent.handlers;
    }
}
