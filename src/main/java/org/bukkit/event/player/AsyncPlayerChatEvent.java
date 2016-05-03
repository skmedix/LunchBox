package org.bukkit.event.player;

import java.util.IllegalFormatException;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class AsyncPlayerChatEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private String format = "<%1$s> %2$s";
    private final Set recipients;

    public AsyncPlayerChatEvent(boolean async, Player who, String message, Set players) {
        super(who, async);
        this.message = message;
        this.recipients = players;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) throws IllegalFormatException, NullPointerException {
        try {
            String.format(format, new Object[] { this.player, this.message});
        } catch (RuntimeException runtimeexception) {
            runtimeexception.fillInStackTrace();
            throw runtimeexception;
        }

        this.format = format;
    }

    public Set getRecipients() {
        return this.recipients;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return AsyncPlayerChatEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return AsyncPlayerChatEvent.handlers;
    }
}
