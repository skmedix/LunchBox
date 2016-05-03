package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerShearEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final Entity what;

    public PlayerShearEntityEvent(Player who, Entity what) {
        super(who);
        this.what = what;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Entity getEntity() {
        return this.what;
    }

    public HandlerList getHandlers() {
        return PlayerShearEntityEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerShearEntityEvent.handlers;
    }
}
