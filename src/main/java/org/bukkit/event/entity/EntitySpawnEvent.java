package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntitySpawnEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;

    public EntitySpawnEvent(Entity spawnee) {
        super(spawnee);
    }

    public boolean isCancelled() {
        return this.canceled;
    }

    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    public Location getLocation() {
        return this.getEntity().getLocation();
    }

    public HandlerList getHandlers() {
        return EntitySpawnEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntitySpawnEvent.handlers;
    }
}
