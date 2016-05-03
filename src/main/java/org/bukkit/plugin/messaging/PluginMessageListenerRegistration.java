package org.bukkit.plugin.messaging;

import org.bukkit.plugin.Plugin;

public final class PluginMessageListenerRegistration {

    private final Messenger messenger;
    private final Plugin plugin;
    private final String channel;
    private final PluginMessageListener listener;

    public PluginMessageListenerRegistration(Messenger messenger, Plugin plugin, String channel, PluginMessageListener listener) {
        if (messenger == null) {
            throw new IllegalArgumentException("Messenger cannot be null!");
        } else if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        } else if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null!");
        } else if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null!");
        } else {
            this.messenger = messenger;
            this.plugin = plugin;
            this.channel = channel;
            this.listener = listener;
        }
    }

    public String getChannel() {
        return this.channel;
    }

    public PluginMessageListener getListener() {
        return this.listener;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public boolean isValid() {
        return this.messenger.isRegistrationValid(this);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            PluginMessageListenerRegistration other = (PluginMessageListenerRegistration) obj;

            if (this.messenger != other.messenger && (this.messenger == null || !this.messenger.equals(other.messenger))) {
                return false;
            } else if (this.plugin != other.plugin && (this.plugin == null || !this.plugin.equals(other.plugin))) {
                return false;
            } else {
                if (this.channel == null) {
                    if (other.channel != null) {
                        return false;
                    }
                } else if (!this.channel.equals(other.channel)) {
                    return false;
                }

                if (this.listener != other.listener && (this.listener == null || !this.listener.equals(other.listener))) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public int hashCode() {
        byte hash = 7;
        int hash1 = 53 * hash + (this.messenger != null ? this.messenger.hashCode() : 0);

        hash1 = 53 * hash1 + (this.plugin != null ? this.plugin.hashCode() : 0);
        hash1 = 53 * hash1 + (this.channel != null ? this.channel.hashCode() : 0);
        hash1 = 53 * hash1 + (this.listener != null ? this.listener.hashCode() : 0);
        return hash1;
    }
}
