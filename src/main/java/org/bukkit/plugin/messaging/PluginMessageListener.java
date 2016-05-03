package org.bukkit.plugin.messaging;

import org.bukkit.entity.Player;

public interface PluginMessageListener {

    void onPluginMessageReceived(String s, Player player, byte[] abyte);
}
