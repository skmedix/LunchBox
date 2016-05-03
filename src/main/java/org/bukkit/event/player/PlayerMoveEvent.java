package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private Location from;
    private Location to;

    public PlayerMoveEvent(Player player, Location from, Location to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location from) {
        this.validateLocation(from);
        this.from = from;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location to) {
        this.validateLocation(to);
        this.to = to;
    }

    private void validateLocation(Location loc) {
        Preconditions.checkArgument(loc != null, "Cannot use null location!");
        Preconditions.checkArgument(loc.getWorld() != null, "Cannot use null location with null world!");
    }

    public HandlerList getHandlers() {
        return PlayerMoveEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerMoveEvent.handlers;
    }
}
