package org.bukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ServerCommandEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private String command;
    private final CommandSender sender;
    private boolean cancel = false;

    public ServerCommandEvent(CommandSender sender, String command) {
        this.command = command;
        this.sender = sender;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String message) {
        this.command = message;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public HandlerList getHandlers() {
        return ServerCommandEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ServerCommandEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
