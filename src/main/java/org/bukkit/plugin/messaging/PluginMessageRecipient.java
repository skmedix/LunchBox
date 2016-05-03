package org.bukkit.plugin.messaging;

import java.util.Set;
import org.bukkit.plugin.Plugin;

public interface PluginMessageRecipient {

    void sendPluginMessage(Plugin plugin, String s, byte[] abyte);

    Set getListeningPluginChannels();
}
