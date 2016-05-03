package org.bukkit.event.player;

import org.bukkit.entity.Player;

public class PlayerUnregisterChannelEvent extends PlayerChannelEvent {

    public PlayerUnregisterChannelEvent(Player player, String channel) {
        super(player, channel);
    }
}
