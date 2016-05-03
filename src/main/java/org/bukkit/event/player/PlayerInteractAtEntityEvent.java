package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Vector position;

    public PlayerInteractAtEntityEvent(Player who, Entity clickedEntity, Vector position) {
        super(who, clickedEntity);
        this.position = position;
    }

    public Vector getClickedPosition() {
        return this.position.clone();
    }

    public HandlerList getHandlers() {
        return PlayerInteractAtEntityEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerInteractAtEntityEvent.handlers;
    }
}
