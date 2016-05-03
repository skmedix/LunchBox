package org.bukkit.event.player;

import org.bukkit.entity.Player;

public class PlayerRegisterChannelEvent extends PlayerChannelEvent {

    public PlayerRegisterChannelEvent(Player player, String channel) {
        super(player, channel);
    }
}
