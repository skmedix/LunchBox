package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private String leaveMessage;
    private String kickReason;
    private Boolean cancel;

    public PlayerKickEvent(Player playerKicked, String kickReason, String leaveMessage) {
        super(playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cancel = Boolean.valueOf(false);
    }

    public String getReason() {
        return this.kickReason;
    }

    public String getLeaveMessage() {
        return this.leaveMessage;
    }

    public boolean isCancelled() {
        return this.cancel.booleanValue();
    }

    public void setCancelled(boolean cancel) {
        this.cancel = Boolean.valueOf(cancel);
    }

    public void setReason(String kickReason) {
        this.kickReason = kickReason;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public HandlerList getHandlers() {
        return PlayerKickEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerKickEvent.handlers;
    }
}
