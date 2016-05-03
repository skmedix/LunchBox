package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerTeleportEvent extends PlayerMoveEvent {

    private static final HandlerList handlers = new HandlerList();
    private PlayerTeleportEvent.TeleportCause cause;

    public PlayerTeleportEvent(Player player, Location from, Location to) {
        super(player, from, to);
        this.cause = PlayerTeleportEvent.TeleportCause.UNKNOWN;
    }

    public PlayerTeleportEvent(Player player, Location from, Location to, PlayerTeleportEvent.TeleportCause cause) {
        this(player, from, to);
        this.cause = cause;
    }

    public PlayerTeleportEvent.TeleportCause getCause() {
        return this.cause;
    }

    public HandlerList getHandlers() {
        return PlayerTeleportEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerTeleportEvent.handlers;
    }

    public static enum TeleportCause {

        ENDER_PEARL, COMMAND, PLUGIN, NETHER_PORTAL, END_PORTAL, SPECTATE, UNKNOWN;
    }
}
