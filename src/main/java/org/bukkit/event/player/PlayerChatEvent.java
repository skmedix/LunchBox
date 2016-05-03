package org.bukkit.event.player;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/** @deprecated */
@Deprecated
@Warning(
    reason = "Listening to this event forces chat to wait for the main thread, delaying chat messages."
)
public class PlayerChatEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private String format;
    private final Set recipients;

    public PlayerChatEvent(Player player, String message) {
        super(player);
        this.message = message;
        this.format = "<%1$s> %2$s";
        this.recipients = new HashSet(player.getServer().getOnlinePlayers());
    }

    public PlayerChatEvent(Player player, String message, String format, Set recipients) {
        super(player);
        this.message = message;
        this.format = format;
        this.recipients = recipients;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPlayer(Player player) {
        Validate.notNull(player, "Player cannot be null");
        this.player = player;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
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

    public HandlerList getHandlers() {
        return PlayerChatEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerChatEvent.handlers;
    }
}
