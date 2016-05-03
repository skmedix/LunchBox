package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerResourcePackStatusEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final PlayerResourcePackStatusEvent.Status status;

    public PlayerResourcePackStatusEvent(Player who, PlayerResourcePackStatusEvent.Status resourcePackStatus) {
        super(who);
        this.status = resourcePackStatus;
    }

    public PlayerResourcePackStatusEvent.Status getStatus() {
        return this.status;
    }

    public HandlerList getHandlers() {
        return PlayerResourcePackStatusEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerResourcePackStatusEvent.handlers;
    }

    public static enum Status {

        SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED;
    }
}
