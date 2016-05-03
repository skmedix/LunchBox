package org.bukkit.plugin.messaging;

import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Messenger {

    int MAX_MESSAGE_SIZE = 32766;
    int MAX_CHANNEL_SIZE = 20;

    boolean isReservedChannel(String s);

    void registerOutgoingPluginChannel(Plugin plugin, String s);

    void unregisterOutgoingPluginChannel(Plugin plugin, String s);

    void unregisterOutgoingPluginChannel(Plugin plugin);

    PluginMessageListenerRegistration registerIncomingPluginChannel(Plugin plugin, String s, PluginMessageListener pluginmessagelistener);

    void unregisterIncomingPluginChannel(Plugin plugin, String s, PluginMessageListener pluginmessagelistener);

    void unregisterIncomingPluginChannel(Plugin plugin, String s);

    void unregisterIncomingPluginChannel(Plugin plugin);

    Set getOutgoingChannels();

    Set getOutgoingChannels(Plugin plugin);

    Set getIncomingChannels();

    Set getIncomingChannels(Plugin plugin);

    Set getIncomingChannelRegistrations(Plugin plugin);

    Set getIncomingChannelRegistrations(String s);

    Set getIncomingChannelRegistrations(Plugin plugin, String s);

    boolean isRegistrationValid(PluginMessageListenerRegistration pluginmessagelistenerregistration);

    boolean isIncomingChannelRegistered(Plugin plugin, String s);

    boolean isOutgoingChannelRegistered(Plugin plugin, String s);

    void dispatchIncomingMessage(Player player, String s, byte[] abyte);
}
