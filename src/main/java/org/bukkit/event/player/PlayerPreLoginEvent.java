package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** @deprecated */
@Deprecated
@Warning(
    reason = "This event causes a login thread to synchronize with the main thread"
)
public class PlayerPreLoginEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private PlayerPreLoginEvent.Result result;
    private String message;
    private final String name;
    private final InetAddress ipAddress;
    private final UUID uniqueId;

    /** @deprecated */
    @Deprecated
    public PlayerPreLoginEvent(String name, InetAddress ipAddress) {
        this(name, ipAddress, (UUID) null);
    }

    public PlayerPreLoginEvent(String name, InetAddress ipAddress, UUID uniqueId) {
        this.result = PlayerPreLoginEvent.Result.ALLOWED;
        this.message = "";
        this.name = name;
        this.ipAddress = ipAddress;
        this.uniqueId = uniqueId;
    }

    public PlayerPreLoginEvent.Result getResult() {
        return this.result;
    }

    public void setResult(PlayerPreLoginEvent.Result result) {
        this.result = result;
    }

    public String getKickMessage() {
        return this.message;
    }

    public void setKickMessage(String message) {
        this.message = message;
    }

    public void allow() {
        this.result = PlayerPreLoginEvent.Result.ALLOWED;
        this.message = "";
    }

    public void disallow(PlayerPreLoginEvent.Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public InetAddress getAddress() {
        return this.ipAddress;
    }

    public HandlerList getHandlers() {
        return PlayerPreLoginEvent.handlers;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public static HandlerList getHandlerList() {
        return PlayerPreLoginEvent.handlers;
    }

    public static enum Result {

        ALLOWED, KICK_FULL, KICK_BANNED, KICK_WHITELIST, KICK_OTHER;
    }
}
