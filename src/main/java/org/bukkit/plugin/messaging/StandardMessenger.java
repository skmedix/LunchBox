package org.bukkit.plugin.messaging;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class StandardMessenger implements Messenger {

    private final Map incomingByChannel = new HashMap();
    private final Map incomingByPlugin = new HashMap();
    private final Map outgoingByChannel = new HashMap();
    private final Map outgoingByPlugin = new HashMap();
    private final Object incomingLock = new Object();
    private final Object outgoingLock = new Object();

    private void addToOutgoing(Plugin plugin, String channel) {
        Object object = this.outgoingLock;

        synchronized (this.outgoingLock) {
            Object plugins = (Set) this.outgoingByChannel.get(channel);
            Object channels = (Set) this.outgoingByPlugin.get(plugin);

            if (plugins == null) {
                plugins = new HashSet();
                this.outgoingByChannel.put(channel, plugins);
            }

            if (channels == null) {
                channels = new HashSet();
                this.outgoingByPlugin.put(plugin, channels);
            }

            ((Set) plugins).add(plugin);
            ((Set) channels).add(channel);
        }
    }

    private void removeFromOutgoing(Plugin plugin, String channel) {
        Object object = this.outgoingLock;

        synchronized (this.outgoingLock) {
            Set plugins = (Set) this.outgoingByChannel.get(channel);
            Set channels = (Set) this.outgoingByPlugin.get(plugin);

            if (plugins != null) {
                plugins.remove(plugin);
                if (plugins.isEmpty()) {
                    this.outgoingByChannel.remove(channel);
                }
            }

            if (channels != null) {
                channels.remove(channel);
                if (channels.isEmpty()) {
                    this.outgoingByChannel.remove(channel);
                }
            }

        }
    }

    private void removeFromOutgoing(Plugin plugin) {
        Object object = this.outgoingLock;

        synchronized (this.outgoingLock) {
            Set channels = (Set) this.outgoingByPlugin.get(plugin);

            if (channels != null) {
                String[] toRemove = (String[]) channels.toArray(new String[0]);

                this.outgoingByPlugin.remove(plugin);
                String[] astring = toRemove;
                int i = toRemove.length;

                for (int j = 0; j < i; ++j) {
                    String channel = astring[j];

                    this.removeFromOutgoing(plugin, channel);
                }
            }

        }
    }

    private void addToIncoming(PluginMessageListenerRegistration registration) {
        Object object = this.incomingLock;

        synchronized (this.incomingLock) {
            Object registrations = (Set) this.incomingByChannel.get(registration.getChannel());

            if (registrations == null) {
                registrations = new HashSet();
                this.incomingByChannel.put(registration.getChannel(), registrations);
            } else if (((Set) registrations).contains(registration)) {
                throw new IllegalArgumentException("This registration already exists");
            }

            ((Set) registrations).add(registration);
            registrations = (Set) this.incomingByPlugin.get(registration.getPlugin());
            if (registrations == null) {
                registrations = new HashSet();
                this.incomingByPlugin.put(registration.getPlugin(), registrations);
            } else if (((Set) registrations).contains(registration)) {
                throw new IllegalArgumentException("This registration already exists");
            }

            ((Set) registrations).add(registration);
        }
    }

    private void removeFromIncoming(PluginMessageListenerRegistration registration) {
        Object object = this.incomingLock;

        synchronized (this.incomingLock) {
            Set registrations = (Set) this.incomingByChannel.get(registration.getChannel());

            if (registrations != null) {
                registrations.remove(registration);
                if (registrations.isEmpty()) {
                    this.incomingByChannel.remove(registration.getChannel());
                }
            }

            registrations = (Set) this.incomingByPlugin.get(registration.getPlugin());
            if (registrations != null) {
                registrations.remove(registration);
                if (registrations.isEmpty()) {
                    this.incomingByPlugin.remove(registration.getPlugin());
                }
            }

        }
    }

    private void removeFromIncoming(Plugin plugin, String channel) {
        Object object = this.incomingLock;

        synchronized (this.incomingLock) {
            Set registrations = (Set) this.incomingByPlugin.get(plugin);

            if (registrations != null) {
                PluginMessageListenerRegistration[] toRemove = (PluginMessageListenerRegistration[]) registrations.toArray(new PluginMessageListenerRegistration[0]);
                PluginMessageListenerRegistration[] apluginmessagelistenerregistration = toRemove;
                int i = toRemove.length;

                for (int j = 0; j < i; ++j) {
                    PluginMessageListenerRegistration registration = apluginmessagelistenerregistration[j];

                    if (registration.getChannel().equals(channel)) {
                        this.removeFromIncoming(registration);
                    }
                }
            }

        }
    }

    private void removeFromIncoming(Plugin plugin) {
        Object object = this.incomingLock;

        synchronized (this.incomingLock) {
            Set registrations = (Set) this.incomingByPlugin.get(plugin);

            if (registrations != null) {
                PluginMessageListenerRegistration[] toRemove = (PluginMessageListenerRegistration[]) registrations.toArray(new PluginMessageListenerRegistration[0]);

                this.incomingByPlugin.remove(plugin);
                PluginMessageListenerRegistration[] apluginmessagelistenerregistration = toRemove;
                int i = toRemove.length;

                for (int j = 0; j < i; ++j) {
                    PluginMessageListenerRegistration registration = apluginmessagelistenerregistration[j];

                    this.removeFromIncoming(registration);
                }
            }

        }
    }

    public boolean isReservedChannel(String channel) {
        validateChannel(channel);
        return channel.equals("REGISTER") || channel.equals("UNREGISTER");
    }

    public void registerOutgoingPluginChannel(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            validateChannel(channel);
            if (this.isReservedChannel(channel)) {
                throw new ReservedChannelException(channel);
            } else {
                this.addToOutgoing(plugin, channel);
            }
        }
    }

    public void unregisterOutgoingPluginChannel(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            validateChannel(channel);
            this.removeFromOutgoing(plugin, channel);
        }
    }

    public void unregisterOutgoingPluginChannel(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            this.removeFromOutgoing(plugin);
        }
    }

    public PluginMessageListenerRegistration registerIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            validateChannel(channel);
            if (this.isReservedChannel(channel)) {
                throw new ReservedChannelException(channel);
            } else if (listener == null) {
                throw new IllegalArgumentException("Listener cannot be null");
            } else {
                PluginMessageListenerRegistration result = new PluginMessageListenerRegistration(this, plugin, channel, listener);

                this.addToIncoming(result);
                return result;
            }
        }
    }

    public void unregisterIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        } else {
            validateChannel(channel);
            this.removeFromIncoming(new PluginMessageListenerRegistration(this, plugin, channel, listener));
        }
    }

    public void unregisterIncomingPluginChannel(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            validateChannel(channel);
            this.removeFromIncoming(plugin, channel);
        }
    }

    public void unregisterIncomingPluginChannel(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            this.removeFromIncoming(plugin);
        }
    }

    public Set getOutgoingChannels() {
        Object object = this.outgoingLock;

        synchronized (this.outgoingLock) {
            Set keys = this.outgoingByChannel.keySet();

            return ImmutableSet.copyOf((Collection) keys);
        }
    }

    public Set getOutgoingChannels(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            Object object = this.outgoingLock;

            synchronized (this.outgoingLock) {
                Set channels = (Set) this.outgoingByPlugin.get(plugin);

                return channels != null ? ImmutableSet.copyOf((Collection) channels) : ImmutableSet.of();
            }
        }
    }

    public Set getIncomingChannels() {
        Object object = this.incomingLock;

        synchronized (this.incomingLock) {
            Set keys = this.incomingByChannel.keySet();

            return ImmutableSet.copyOf((Collection) keys);
        }
    }

    public Set getIncomingChannels(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            Object object = this.incomingLock;

            synchronized (this.incomingLock) {
                Set registrations = (Set) this.incomingByPlugin.get(plugin);

                if (registrations == null) {
                    return ImmutableSet.of();
                } else {
                    ImmutableSet.Builder builder = ImmutableSet.builder();
                    Iterator iterator = registrations.iterator();

                    while (iterator.hasNext()) {
                        PluginMessageListenerRegistration registration = (PluginMessageListenerRegistration) iterator.next();

                        builder.add((Object) registration.getChannel());
                    }

                    return builder.build();
                }
            }
        }
    }

    public Set getIncomingChannelRegistrations(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            Object object = this.incomingLock;

            synchronized (this.incomingLock) {
                Set registrations = (Set) this.incomingByPlugin.get(plugin);

                return registrations != null ? ImmutableSet.copyOf((Collection) registrations) : ImmutableSet.of();
            }
        }
    }

    public Set getIncomingChannelRegistrations(String channel) {
        validateChannel(channel);
        Object object = this.incomingLock;

        synchronized (this.incomingLock) {
            Set registrations = (Set) this.incomingByChannel.get(channel);

            return registrations != null ? ImmutableSet.copyOf((Collection) registrations) : ImmutableSet.of();
        }
    }

    public Set getIncomingChannelRegistrations(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            validateChannel(channel);
            Object object = this.incomingLock;

            synchronized (this.incomingLock) {
                Set registrations = (Set) this.incomingByPlugin.get(plugin);

                if (registrations != null) {
                    ImmutableSet.Builder builder = ImmutableSet.builder();
                    Iterator iterator = registrations.iterator();

                    while (iterator.hasNext()) {
                        PluginMessageListenerRegistration registration = (PluginMessageListenerRegistration) iterator.next();

                        if (registration.getChannel().equals(channel)) {
                            builder.add((Object) registration);
                        }
                    }

                    return builder.build();
                } else {
                    return ImmutableSet.of();
                }
            }
        }
    }

    public boolean isRegistrationValid(PluginMessageListenerRegistration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Registration cannot be null");
        } else {
            Object object = this.incomingLock;

            synchronized (this.incomingLock) {
                Set registrations = (Set) this.incomingByPlugin.get(registration.getPlugin());

                return registrations != null ? registrations.contains(registration) : false;
            }
        }
    }

    public boolean isIncomingChannelRegistered(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            validateChannel(channel);
            Object object = this.incomingLock;

            synchronized (this.incomingLock) {
                Set registrations = (Set) this.incomingByPlugin.get(plugin);

                if (registrations != null) {
                    Iterator iterator = registrations.iterator();

                    while (iterator.hasNext()) {
                        PluginMessageListenerRegistration registration = (PluginMessageListenerRegistration) iterator.next();

                        if (registration.getChannel().equals(channel)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }
    }

    public boolean isOutgoingChannelRegistered(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else {
            validateChannel(channel);
            Object object = this.outgoingLock;

            synchronized (this.outgoingLock) {
                Set channels = (Set) this.outgoingByPlugin.get(plugin);

                return channels != null ? channels.contains(channel) : false;
            }
        }
    }

    public void dispatchIncomingMessage(Player source, String channel, byte[] message) {
        if (source == null) {
            throw new IllegalArgumentException("Player source cannot be null");
        } else if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        } else {
            validateChannel(channel);
            Set registrations = this.getIncomingChannelRegistrations(channel);
            Iterator iterator = registrations.iterator();

            while (iterator.hasNext()) {
                PluginMessageListenerRegistration registration = (PluginMessageListenerRegistration) iterator.next();

                try {
                    registration.getListener().onPluginMessageReceived(channel, source, message);
                } catch (Throwable throwable) {
                    Bukkit.getLogger().log(Level.WARNING, "Could not pass incoming plugin message to " + registration.getPlugin(), throwable);
                }
            }

        }
    }

    public static void validateChannel(String channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        } else if (channel.length() > 20) {
            throw new ChannelNameTooLongException(channel);
        }
    }

    public static void validatePluginMessage(Messenger messenger, Plugin source, String channel, byte[] message) {
        if (messenger == null) {
            throw new IllegalArgumentException("Messenger cannot be null");
        } else if (source == null) {
            throw new IllegalArgumentException("Plugin source cannot be null");
        } else if (!source.isEnabled()) {
            throw new IllegalArgumentException("Plugin must be enabled to send messages");
        } else if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        } else if (!messenger.isOutgoingChannelRegistered(source, channel)) {
            throw new ChannelNotRegisteredException(channel);
        } else if (message.length > 32766) {
            throw new MessageTooLargeException(message);
        } else {
            validateChannel(channel);
        }
    }
}
