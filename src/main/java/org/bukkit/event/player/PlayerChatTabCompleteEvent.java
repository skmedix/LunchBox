package org.bukkit.event.player;

import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerChatTabCompleteEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final String message;
    private final String lastToken;
    private final Collection completions;

    public PlayerChatTabCompleteEvent(Player who, String message, Collection completions) {
        super(who);
        Validate.notNull(message, "Message cannot be null");
        Validate.notNull(completions, "Completions cannot be null");
        this.message = message;
        int i = message.lastIndexOf(32);

        if (i < 0) {
            this.lastToken = message;
        } else {
            this.lastToken = message.substring(i + 1);
        }

        this.completions = completions;
    }

    public String getChatMessage() {
        return this.message;
    }

    public String getLastToken() {
        return this.lastToken;
    }

    public Collection getTabCompletions() {
        return this.completions;
    }

    public HandlerList getHandlers() {
        return PlayerChatTabCompleteEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerChatTabCompleteEvent.handlers;
    }
}
