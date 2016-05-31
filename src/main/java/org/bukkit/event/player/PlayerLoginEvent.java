package org.bukkit.event.player;

import java.net.InetAddress;
import java.net.SocketAddress;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerLoginEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final InetAddress address;
    private final String hostname;
    private PlayerLoginEvent.Result result;
    private String message;
    private final InetAddress realAddress;

    /** @deprecated */
    @Deprecated
    public PlayerLoginEvent(Player player) {
        this(player, "", (InetAddress) null);
    }

    /** @deprecated */
    @Deprecated
    public PlayerLoginEvent(Player player, String hostname) {
        this(player, hostname, (InetAddress) null);
    }

    public PlayerLoginEvent(Player player, String hostname, InetAddress address, InetAddress realAddress) {
        super(player);
        this.result = PlayerLoginEvent.Result.ALLOWED;
        this.message = "";
        this.hostname = hostname;
        this.address = address;
        this.realAddress = realAddress;
    }

    public PlayerLoginEvent(Player player, String hostname, InetAddress address) {
        this(player, hostname, address, address);
    }

    /** @deprecated */
    @Deprecated
    public PlayerLoginEvent(Player player, PlayerLoginEvent.Result result, String message) {
        this(player, "", (InetAddress) null, result, message, (InetAddress) null);
    }

    public PlayerLoginEvent(Player player, String hostname, InetAddress address, PlayerLoginEvent.Result result, String message, InetAddress realAddress) {
        this(player, hostname, address, realAddress);
        this.result = result;
        this.message = message;
    }

    public InetAddress getRealAddress() {
        return this.realAddress;
    }

    public PlayerLoginEvent.Result getResult() {
        return this.result;
    }

    public void setResult(PlayerLoginEvent.Result result) {
        this.result = result;
    }

    public String getKickMessage() {
        return this.message;
    }

    public void setKickMessage(String message) {
        this.message = message;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void allow() {
        this.result = PlayerLoginEvent.Result.ALLOWED;
        this.message = "";
    }

    public void disallow(PlayerLoginEvent.Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public HandlerList getHandlers() {
        return PlayerLoginEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerLoginEvent.handlers;
    }

    public static enum Result {

        ALLOWED, KICK_FULL, KICK_BANNED, KICK_WHITELIST, KICK_OTHER;
    }
}
