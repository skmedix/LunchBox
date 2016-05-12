package org.bukkit.craftbukkit.v1_8_R3.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.AttributeMapServer;
import net.minecraft.server.v1_8_R3.AttributeModifiable;
import net.minecraft.server.v1_8_R3.AttributeRanged;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.IAttribute;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.MapIcon;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateAttributes;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateHealth;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.Validate;
import org.bukkit.Achievement;
import org.bukkit.BanList;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.v1_8_R3.CraftEffect;
import org.bukkit.craftbukkit.v1_8_R3.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_8_R3.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_8_R3.map.CraftMapView;
import org.bukkit.craftbukkit.v1_8_R3.map.RenderData;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;
import org.spigotmc.AsyncCatcher;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {

    private long firstPlayed = 0L;
    private long lastPlayed = 0L;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set channels = new HashSet();
    private final Set hiddenPlayers = new HashSet();
    private int hash = 0;
    private double health = 20.0D;
    private boolean scaledHealth = false;
    private double healthScale = 20.0D;
    private final Player.Spigot spigot = new Player.Spigot() {
        public InetSocketAddress getRawAddress() {
            return (InetSocketAddress) CraftPlayer.this.getHandle();
        }

        public boolean getCollidesWithEntities() {
            return CraftPlayer.this.getHandle().collidesWithEntities;
        }

        public void setCollidesWithEntities(boolean collides) {
            CraftPlayer.this.getHandle().collidesWithEntities = collides;
            CraftPlayer.this.getHandle().k = collides;
        }

        public void respawn() {
            if (CraftPlayer.this.getHealth() <= 0.0D && CraftPlayer.this.isOnline()) {
                CraftPlayer.this.server.getServer().getPlayerList().moveToWorld(CraftPlayer.this.getHandle(), 0, false);
            }

        }

        public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
            Validate.notNull(location, "Location cannot be null");
            Validate.notNull(effect, "Effect cannot be null");
            Validate.notNull(location.getWorld(), "World cannot be null");
            int distance;
            Object packet;

            if (effect.getType() != Effect.Type.PARTICLE) {
                distance = effect.getId();
                packet = new PacketPlayOutWorldEvent(distance, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id, false);
            } else {
                EnumParticle enumparticle = null;
                int[] extra = null;
                EnumParticle[] aenumparticle;
                int i = (aenumparticle = EnumParticle.values()).length;

                for (int j = 0; j < i; ++j) {
                    EnumParticle p = aenumparticle[j];

                    if (effect.getName().startsWith(p.b().replace("_", ""))) {
                        enumparticle = p;
                        if (effect.getData() != null) {
                            if (effect.getData().equals(Material.class)) {
                                extra = new int[] { id};
                            } else {
                                extra = new int[] { data << 12 | id & 4095};
                            }
                        }
                        break;
                    }
                }

                if (extra == null) {
                    extra = new int[0];
                }

                packet = new PacketPlayOutWorldParticles(enumparticle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra);
            }

            radius *= radius;
            if (CraftPlayer.this.getHandle().playerConnection != null) {
                if (location.getWorld().equals(CraftPlayer.this.getWorld())) {
                    distance = (int) CraftPlayer.this.getLocation().distanceSquared(location);
                    if (distance <= radius) {
                        CraftPlayer.this.getHandle().playerConnection.sendPacket((Packet) packet);
                    }

                }
            }
        }

        public String getLocale() {
            return CraftPlayer.this.getHandle().locale;
        }

        public Set getHiddenPlayers() {
            HashSet ret = new HashSet();
            Iterator iterator = CraftPlayer.this.hiddenPlayers.iterator();

            while (iterator.hasNext()) {
                UUID u = (UUID) iterator.next();

                ret.add(CraftPlayer.this.getServer().getPlayer(u));
            }

            return Collections.unmodifiableSet(ret);
        }

        public void sendMessage(BaseComponent component) {
            this.sendMessage(new BaseComponent[] { component});
        }

        public void sendMessage(BaseComponent... components) {
            if (CraftPlayer.this.getHandle().playerConnection != null) {
                PacketPlayOutChat packet = new PacketPlayOutChat();

                packet.components = components;
                CraftPlayer.this.getHandle().playerConnection.sendPacket(packet);
            }
        }
    };

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, (EntityHuman) entity);
        this.firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return this.getHandle().getProfile();
    }

    public boolean isOp() {
        return this.server.getHandle().isOp(this.getProfile());
    }

    public void setOp(boolean value) {
        if (value != this.isOp()) {
            if (value) {
                this.server.getHandle().addOp(this.getProfile());
            } else {
                this.server.getHandle().removeOp(this.getProfile());
            }

            this.perm.recalculatePermissions();
        }
    }

    public boolean isOnline() {
        return this.server.getPlayer(this.getUniqueId()) != null;
    }

    public InetSocketAddress getAddress() {
        if (this.getHandle().playerConnection == null) {
            return null;
        } else {
            SocketAddress addr = this.getHandle().playerConnection.networkManager.getSocketAddress();

            return addr instanceof InetSocketAddress ? (InetSocketAddress) addr : null;
        }
    }

    public double getEyeHeight() {
        return this.getEyeHeight(false);
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        return ignoreSneaking ? 1.62D : (this.isSneaking() ? 1.54D : 1.62D);
    }

    public void sendRawMessage(String message) {
        if (this.getHandle().playerConnection != null) {
            IChatBaseComponent[] aichatbasecomponent;
            int i = (aichatbasecomponent = CraftChatMessage.fromString(message)).length;

            for (int j = 0; j < i; ++j) {
                IChatBaseComponent component = aichatbasecomponent[j];

                this.getHandle().playerConnection.sendPacket(new PacketPlayOutChat(component));
            }

        }
    }

    public void sendMessage(String message) {
        if (!this.conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }

    }

    public void sendMessage(String[] messages) {
        String[] astring = messages;
        int i = messages.length;

        for (int j = 0; j < i; ++j) {
            String message = astring[j];

            this.sendMessage(message);
        }

    }

    public String getDisplayName() {
        return this.getHandle().displayName;
    }

    public void setDisplayName(String name) {
        this.getHandle().displayName = name == null ? this.getName() : name;
    }

    public String getPlayerListName() {
        return this.getHandle().listName == null ? this.getName() : CraftChatMessage.fromComponent(this.getHandle().listName);
    }

    public void setPlayerListName(String name) {
        if (name == null) {
            name = this.getName();
        }

        this.getHandle().listName = name.equals(this.getName()) ? null : CraftChatMessage.fromString(name)[0];
        Iterator iterator = this.server.getHandle().players.iterator();

        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer) iterator.next();

            if (player.getBukkitEntity().canSee(this)) {
                player.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, new EntityPlayer[] { this.getHandle()}));
            }
        }

    }

    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        } else {
            OfflinePlayer other = (OfflinePlayer) obj;

            if (this.getUniqueId() != null && other.getUniqueId() != null) {
                boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
                boolean idEquals = true;

                if (other instanceof CraftPlayer) {
                    idEquals = this.getEntityId() == ((CraftPlayer) other).getEntityId();
                }

                return uuidEquals && idEquals;
            } else {
                return false;
            }
        }
    }

    public void kickPlayer(String message) {
        AsyncCatcher.catchOp("player kick");
        if (this.getHandle().playerConnection != null) {
            this.getHandle().playerConnection.disconnect(message == null ? "" : message);
        }
    }

    public void setCompassTarget(Location loc) {
        if (this.getHandle().playerConnection != null) {
            this.getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnPosition(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
        }
    }

    public Location getCompassTarget() {
        return this.getHandle().compassTarget;
    }

    public void chat(String msg) {
        if (this.getHandle().playerConnection != null) {
            this.getHandle().playerConnection.chat(msg, false);
        }
    }

    public boolean performCommand(String command) {
        return this.server.dispatchCommand(this, command);
    }

    public void playNote(Location loc, byte instrument, byte note) {
        if (this.getHandle().playerConnection != null) {
            String instrumentName = null;

            switch (instrument) {
            case 0:
                instrumentName = "harp";
                break;

            case 1:
                instrumentName = "bd";
                break;

            case 2:
                instrumentName = "snare";
                break;

            case 3:
                instrumentName = "hat";
                break;

            case 4:
                instrumentName = "bassattack";
            }

            float f = (float) Math.pow(2.0D, ((double) note - 12.0D) / 12.0D);

            this.getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("note." + instrumentName, (double) loc.getBlockX(), (double) loc.getBlockY(), (double) loc.getBlockZ(), 3.0F, f));
        }
    }

    public void playNote(Location loc, Instrument instrument, Note note) {
        if (this.getHandle().playerConnection != null) {
            String instrumentName = null;

            switch (instrument.ordinal()) {
            case 0:
                instrumentName = "harp";
                break;

            case 1:
                instrumentName = "bd";
                break;

            case 2:
                instrumentName = "snare";
                break;

            case 3:
                instrumentName = "hat";
                break;

            case 4:
                instrumentName = "bassattack";
            }

            float f = (float) Math.pow(2.0D, ((double) note.getId() - 12.0D) / 12.0D);

            this.getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("note." + instrumentName, (double) loc.getBlockX(), (double) loc.getBlockY(), (double) loc.getBlockZ(), 3.0F, f));
        }
    }

    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (sound != null) {
            this.playSound(loc, CraftSound.getSound(sound), volume, pitch);
        }
    }

    public void playSound(Location loc, String sound, float volume, float pitch) {
        if (loc != null && sound != null && this.getHandle().playerConnection != null) {
            double x = (double) loc.getBlockX() + 0.5D;
            double y = (double) loc.getBlockY() + 0.5D;
            double z = (double) loc.getBlockZ() + 0.5D;
            PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(sound, x, y, z, volume, pitch);

            this.getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void playEffect(Location loc, Effect effect, int data) {
        if (this.getHandle().playerConnection != null) {
            this.spigot().playEffect(loc, effect, data, 0, 0.0F, 0.0F, 0.0F, 1.0F, 1, 64);
        }
    }

    public void playEffect(Location loc, Effect effect, Object data) {
        if (data != null) {
            Validate.isTrue(data.getClass().isAssignableFrom(effect.getData()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);

        this.playEffect(loc, effect, datavalue);
    }

    public void sendBlockChange(Location loc, Material material, byte data) {
        this.sendBlockChange(loc, material.getId(), data);
    }

    public void sendBlockChange(Location loc, int material, byte data) {
        if (this.getHandle().playerConnection != null) {
            PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) loc.getWorld()).getHandle(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

            packet.block = CraftMagicNumbers.getBlock(material).fromLegacyData(data);
            this.getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void sendSignChange(Location loc, String[] lines) {
        if (this.getHandle().playerConnection != null) {
            if (lines == null) {
                lines = new String[4];
            }

            Validate.notNull(loc, "Location can not be null");
            if (lines.length < 4) {
                throw new IllegalArgumentException("Must have at least 4 lines");
            } else {
                IChatBaseComponent[] components = CraftSign.sanitizeLines(lines);

                this.getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateSign(this.getHandle().world, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), components));
            }
        }
    }

    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (this.getHandle().playerConnection == null) {
            return false;
        } else {
            throw new NotImplementedException("Chunk changes do not yet work");
        }
    }

    public void sendMap(MapView map) {
        if (this.getHandle().playerConnection != null) {
            RenderData data = ((CraftMapView) map).render(this);
            ArrayList icons = new ArrayList();
            Iterator iterator = data.cursors.iterator();

            while (iterator.hasNext()) {
                MapCursor packet = (MapCursor) iterator.next();

                if (packet.isVisible()) {
                    icons.add(new MapIcon(packet.getRawType(), packet.getX(), packet.getY(), packet.getDirection()));
                }
            }

            PacketPlayOutMap packet1 = new PacketPlayOutMap(map.getId(), map.getScale().getValue(), icons, data.buffer, 0, 0, 0, 0);

            this.getHandle().playerConnection.sendPacket(packet1);
        }
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        EntityPlayer entity = this.getHandle();

        if (this.getHealth() != 0.0D && !entity.dead) {
            if (entity.playerConnection != null && !entity.playerConnection.isDisconnected()) {
                if (entity.passenger != null) {
                    return false;
                } else {
                    Location from = this.getLocation();
                    PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, location, cause);

                    this.server.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return false;
                    } else {
                        entity.mount((Entity) null);
                        from = event.getFrom();
                        Location to = event.getTo();
                        WorldServer fromWorld = ((CraftWorld) from.getWorld()).getHandle();
                        WorldServer toWorld = ((CraftWorld) to.getWorld()).getHandle();

                        if (this.getHandle().activeContainer != this.getHandle().defaultContainer) {
                            this.getHandle().closeInventory();
                        }

                        if (fromWorld == toWorld) {
                            entity.playerConnection.teleport(to);
                        } else {
                            this.server.getHandle().moveToWorld(entity, toWorld.dimension, true, to, true);
                        }

                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setSneaking(boolean sneak) {
        this.getHandle().setSneaking(sneak);
    }

    public boolean isSneaking() {
        return this.getHandle().isSneaking();
    }

    public boolean isSprinting() {
        return this.getHandle().isSprinting();
    }

    public void setSprinting(boolean sprinting) {
        this.getHandle().setSprinting(sprinting);
    }

    public void loadData() {
        this.server.getHandle().playerFileData.load(this.getHandle());
    }

    public void saveData() {
        this.server.getHandle().playerFileData.save(this.getHandle());
    }

    /** @deprecated */
    @Deprecated
    public void updateInventory() {
        this.getHandle().updateInventory(this.getHandle().activeContainer);
    }

    public void setSleepingIgnored(boolean isSleeping) {
        this.getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) this.getWorld()).getHandle().checkSleepStatus();
    }

    public boolean isSleepingIgnored() {
        return this.getHandle().fauxSleeping;
    }

    public void awardAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        if (achievement.hasParent() && !this.hasAchievement(achievement.getParent())) {
            this.awardAchievement(achievement.getParent());
        }

        this.getHandle().getStatisticManager().setStatistic(this.getHandle(), CraftStatistic.getNMSAchievement(achievement), 1);
        this.getHandle().getStatisticManager().updateStatistics(this.getHandle());
    }

    public void removeAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        Achievement[] aachievement;
        int i = (aachievement = Achievement.values()).length;

        for (int j = 0; j < i; ++j) {
            Achievement achieve = aachievement[j];

            if (achieve.getParent() == achievement && this.hasAchievement(achieve)) {
                this.removeAchievement(achieve);
            }
        }

        this.getHandle().getStatisticManager().setStatistic(this.getHandle(), CraftStatistic.getNMSAchievement(achievement), 0);
    }

    public boolean hasAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        return this.getHandle().getStatisticManager().hasAchievement(CraftStatistic.getNMSAchievement(achievement));
    }

    public void incrementStatistic(Statistic statistic) {
        this.incrementStatistic(statistic, 1);
    }

    public void decrementStatistic(Statistic statistic) {
        this.decrementStatistic(statistic, 1);
    }

    public int getStatistic(Statistic statistic) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.UNTYPED, "Must supply additional paramater for this statistic");
        return this.getHandle().getStatisticManager().getStatisticValue(CraftStatistic.getNMSStatistic(statistic));
    }

    public void incrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, this.getStatistic(statistic) + amount);
    }

    public void decrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, this.getStatistic(statistic) - amount);
    }

    public void setStatistic(Statistic statistic, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.UNTYPED, "Must supply additional paramater for this statistic");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getNMSStatistic(statistic);

        this.getHandle().getStatisticManager().setStatistic(this.getHandle(), nmsStatistic, newValue);
    }

    public void incrementStatistic(Statistic statistic, Material material) {
        this.incrementStatistic(statistic, material, 1);
    }

    public void decrementStatistic(Statistic statistic, Material material) {
        this.decrementStatistic(statistic, material, 1);
    }

    public int getStatistic(Statistic statistic, Material material) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.BLOCK || statistic.getType() == Statistic.Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        return this.getHandle().getStatisticManager().getStatisticValue(nmsStatistic);
    }

    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, material, this.getStatistic(statistic, material) + amount);
    }

    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, material, this.getStatistic(statistic, material) - amount);
    }

    public void setStatistic(Statistic statistic, Material material, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Statistic.Type.BLOCK || statistic.getType() == Statistic.Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        this.getHandle().getStatisticManager().setStatistic(this.getHandle(), nmsStatistic, newValue);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        this.incrementStatistic(statistic, entityType, 1);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        this.decrementStatistic(statistic, entityType, 1);
    }

    public int getStatistic(Statistic statistic, EntityType entityType) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);

        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        return this.getHandle().getStatisticManager().getStatisticValue(nmsStatistic);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, entityType, this.getStatistic(statistic, entityType) + amount);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, entityType, this.getStatistic(statistic, entityType) - amount);
    }

    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Statistic.Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);

        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        this.getHandle().getStatisticManager().setStatistic(this.getHandle(), nmsStatistic, newValue);
    }

    public void setPlayerTime(long time, boolean relative) {
        this.getHandle().timeOffset = time;
        this.getHandle().relativeTime = relative;
    }

    public long getPlayerTimeOffset() {
        return this.getHandle().timeOffset;
    }

    public long getPlayerTime() {
        return this.getHandle().getPlayerTime();
    }

    public boolean isPlayerTimeRelative() {
        return this.getHandle().relativeTime;
    }

    public void resetPlayerTime() {
        this.setPlayerTime(0L, true);
    }

    public void setPlayerWeather(WeatherType type) {
        this.getHandle().setPlayerWeather(type, true);
    }

    public WeatherType getPlayerWeather() {
        return this.getHandle().getPlayerWeather();
    }

    public void resetPlayerWeather() {
        this.getHandle().resetPlayerWeather();
    }

    public boolean isBanned() {
        return this.server.getBanList(BanList.Type.NAME).isBanned(this.getName());
    }

    public void setBanned(boolean value) {
        if (value) {
            this.server.getBanList(BanList.Type.NAME).addBan(this.getName(), (String) null, (Date) null, (String) null);
        } else {
            this.server.getBanList(BanList.Type.NAME).pardon(this.getName());
        }

    }

    public boolean isWhitelisted() {
        return this.server.getHandle().getWhitelist().isWhitelisted(this.getProfile());
    }

    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().addWhitelist(this.getProfile());
        } else {
            this.server.getHandle().removeWhitelist(this.getProfile());
        }

    }

    public void setGameMode(GameMode mode) {
        if (this.getHandle().playerConnection != null) {
            if (mode == null) {
                throw new IllegalArgumentException("Mode cannot be null");
            } else {
                if (mode != this.getGameMode()) {
                    PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(this, mode);

                    this.server.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }

                    this.getHandle().setSpectatorTarget(this.getHandle());
                    this.getHandle().playerInteractManager.setGameMode(WorldSettings.EnumGamemode.getById(mode.getValue()));
                    this.getHandle().fallDistance = 0.0F;
                    this.getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, (float) mode.getValue()));
                }

            }
        }
    }

    public GameMode getGameMode() {
        return GameMode.getByValue(this.getHandle().playerInteractManager.getGameMode().getId());
    }

    public void giveExp(int exp) {
        this.getHandle().giveExp(exp);
    }

    public void giveExpLevels(int levels) {
        this.getHandle().levelDown(levels);
    }

    public float getExp() {
        return this.getHandle().exp;
    }

    public void setExp(float exp) {
        this.getHandle().exp = exp;
        this.getHandle().lastSentExp = -1;
    }

    public int getLevel() {
        return this.getHandle().expLevel;
    }

    public void setLevel(int level) {
        this.getHandle().expLevel = level;
        this.getHandle().lastSentExp = -1;
    }

    public int getTotalExperience() {
        return this.getHandle().expTotal;
    }

    public void setTotalExperience(int exp) {
        this.getHandle().expTotal = exp;
    }

    public float getExhaustion() {
        return this.getHandle().getFoodData().exhaustionLevel;
    }

    public void setExhaustion(float value) {
        this.getHandle().getFoodData().exhaustionLevel = value;
    }

    public float getSaturation() {
        return this.getHandle().getFoodData().saturationLevel;
    }

    public void setSaturation(float value) {
        this.getHandle().getFoodData().saturationLevel = value;
    }

    public int getFoodLevel() {
        return this.getHandle().getFoodData().foodLevel;
    }

    public void setFoodLevel(int value) {
        this.getHandle().getFoodData().foodLevel = value;
    }

    public Location getBedSpawnLocation() {
        World world = this.getServer().getWorld(this.getHandle().spawnWorld);
        BlockPosition bed = this.getHandle().getBed();

        if (world != null && bed != null) {
            bed = EntityHuman.getBed(((CraftWorld) world).getHandle(), bed, this.getHandle().isRespawnForced());
            if (bed != null) {
                return new Location(world, (double) bed.getX(), (double) bed.getY(), (double) bed.getZ());
            }
        }

        return null;
    }

    public void setBedSpawnLocation(Location location) {
        this.setBedSpawnLocation(location, false);
    }

    public void setBedSpawnLocation(Location location, boolean override) {
        if (location == null) {
            this.getHandle().setRespawnPosition((BlockPosition) null, override);
        } else {
            this.getHandle().setRespawnPosition(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
            this.getHandle().spawnWorld = location.getWorld().getName();
        }

    }

    public void hidePlayer(Player player) {
        Validate.notNull(player, "hidden player cannot be null");
        if (this.getHandle().playerConnection != null) {
            if (!this.equals(player)) {
                if (!this.hiddenPlayers.contains(player.getUniqueId())) {
                    this.hiddenPlayers.add(player.getUniqueId());
                    EntityTracker tracker = ((WorldServer) this.entity.world).tracker;
                    EntityPlayer other = ((CraftPlayer) player).getHandle();
                    EntityTrackerEntry entry = (EntityTrackerEntry) tracker.trackedEntities.get(other.getId());

                    if (entry != null) {
                        entry.clear(this.getHandle());
                    }

                    this.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { other}));
                }
            }
        }
    }

    public void showPlayer(Player player) {
        Validate.notNull(player, "shown player cannot be null");
        if (this.getHandle().playerConnection != null) {
            if (!this.equals(player)) {
                if (this.hiddenPlayers.contains(player.getUniqueId())) {
                    this.hiddenPlayers.remove(player.getUniqueId());
                    EntityTracker tracker = ((WorldServer) this.entity.world).tracker;
                    EntityPlayer other = ((CraftPlayer) player).getHandle();

                    this.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { other}));
                    EntityTrackerEntry entry = (EntityTrackerEntry) tracker.trackedEntities.get(other.getId());

                    if (entry != null && !entry.trackedPlayers.contains(this.getHandle())) {
                        entry.updatePlayer(this.getHandle());
                    }

                }
            }
        }
    }

    public void removeDisconnectingPlayer(Player player) {
        this.hiddenPlayers.remove(player.getUniqueId());
    }

    public boolean canSee(Player player) {
        return !this.hiddenPlayers.contains(player.getUniqueId());
    }

    public Map serialize() {
        LinkedHashMap result = new LinkedHashMap();

        result.put("name", this.getName());
        return result;
    }

    public Player getPlayer() {
        return this;
    }

    public EntityLiving getHandle() {
        return (EntityLiving) this.entity;
    }

    public void setHandle(EntityPlayer entity) {
        super.setHandle((EntityHuman) entity);
    }

    public String toString() {
        return "CraftPlayer{name=" + this.getName() + '}';
    }

    public int hashCode() {
        if (this.hash == 0 || this.hash == 485) {
            this.hash = 485 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }

        return this.hash;
    }

    public long getFirstPlayed() {
        return this.firstPlayed;
    }

    public long getLastPlayed() {
        return this.lastPlayed;
    }

    public boolean hasPlayedBefore() {
        return this.hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(NBTTagCompound nbttagcompound) {
        this.hasPlayedBefore = true;
        if (nbttagcompound.hasKey("bukkit")) {
            NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");

            if (data.hasKey("firstPlayed")) {
                this.firstPlayed = data.getLong("firstPlayed");
                this.lastPlayed = data.getLong("lastPlayed");
            }

            if (data.hasKey("newExp")) {
                EntityPlayer handle = (EntityPlayer) (EntityLivingBase) this.getHandle();

                handle.experience = data.getInteger("newExp");
                handle.experienceTotal = data.getInteger("newTotalExp");
                handle.experienceLevel = data.getInteger("newLevel");
                handle.expToDrop = data.getInteger("expToDrop");
                handle.captureDrops = data.getBoolean("keepLevel");
            }
        }

    }

    public void setExtraData(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("bukkit")) {
            nbttagcompound.setTag("bukkit", new NBTTagCompound());
        }

        NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");
        EntityPlayer handle = (EntityPlayer) (EntityLivingBase) this.getHandle();

        data.setInteger("newExp", (int) handle.experience);
        data.setInteger("newTotalExp", handle.experienceTotal);
        data.setInteger("newLevel", handle.experienceLevel);
        data.setInteger("expToDrop", handle.expToDrop);
        data.setBoolean("keepLevel", handle.captureDrops);
        data.setLong("firstPlayed", this.getFirstPlayed());
        data.setLong("lastPlayed", System.currentTimeMillis());
        data.setString("lastKnownName", handle.getName());
    }

    public boolean beginConversation(Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }

    public void abandonConversation(Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }

    public void acceptConversationInput(String input) {
        this.conversationTracker.acceptConversationInput(input);
    }

    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        if (((EntityPlayerMP) (EntityLivingBase) this.getHandle()).playerNetServerHandler != null) {
            if (this.channels.contains(channel)) {
                S3FPacketCustomPayload packet = new S3FPacketCustomPayload(channel, new PacketBuffer(Unpooled.wrappedBuffer(message)));

                ((EntityPlayerMP) (EntityLivingBase) this.getHandle()).playerNetServerHandler.sendPacket(packet);
            }

        }
    }

    public void setTexturePack(String url) {
        this.setResourcePack(url);
    }

    public void setResourcePack(String url) {
        Validate.notNull(url, "Resource pack URL cannot be null");
        ((EntityPlayerMP) (EntityLivingBase) this.getHandle()).loadResourcePack(url, "null");
    }

    public void addChannel(String channel) {
        Preconditions.checkState(this.channels.size() < 128, "Too many channels registered");
        if (this.channels.add(channel)) {
            this.server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }

    }

    public void removeChannel(String channel) {
        if (this.channels.remove(channel)) {
            this.server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }

    }

    public Set getListeningPluginChannels() {
        return ImmutableSet.copyOf((Collection) this.channels);
    }

    public void sendSupportedChannels() {
        if (((EntityPlayerMP) (EntityLivingBase) this.getHandle()).playerNetServerHandler != null) {
            Set listening = this.server.getMessenger().getIncomingChannels();

            if (!listening.isEmpty()) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Iterator iterator = listening.iterator();

                while (iterator.hasNext()) {
                    String channel = (String) iterator.next();

                    try {
                        stream.write(channel.getBytes("UTF8"));
                        stream.write(0);
                    } catch (IOException ioexception) {
                        Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + this.getName(), ioexception);
                    }
                }

                ((EntityPlayerMP) (EntityLivingBase) this.getHandle()).playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.wrappedBuffer(stream.toByteArray()))));
            }

        }
    }

    public EntityType getType() {
        return EntityType.PLAYER;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        Container container = ((EntityPlayerMP) (EntityLivingBase) this.getHandle()).openContainer;

        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        } else {
            ((EntityPlayerMP) (EntityLivingBase) this.getHandle()).setContainerData(container, prop.getId(), value);
            return true;
        }
    }

    public void disconnect(String reason) {
        this.conversationTracker.abandonAllConversations();
        this.perm.clearPermissions();
    }

    public boolean isFlying() {
        return this.getHandle().abilities.isFlying;
    }

    public void setFlying(boolean value) {
        if (!this.getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        } else {
            this.getHandle().abilities.isFlying = value;
            this.getHandle().updateAbilities();
        }
    }

    public boolean getAllowFlight() {
        return this.getHandle().abilities.canFly;
    }

    public void setAllowFlight(boolean value) {
        if (this.isFlying() && !value) {
            this.getHandle().abilities.isFlying = false;
        }

        this.getHandle().abilities.canFly = value;
        this.getHandle().updateAbilities();
    }

    public int getNoDamageTicks() {
        return this.getHandle().invulnerableTicks > 0 ? Math.max(this.getHandle().invulnerableTicks, this.getHandle().noDamageTicks) : this.getHandle().noDamageTicks;
    }

    public void setFlySpeed(float value) {
        this.validateSpeed(value);
        EntityPlayer player = this.getHandle();

        player.abilities.flySpeed = Math.max(value, 1.0E-4F) / 2.0F;
        player.updateAbilities();
    }

    public void setWalkSpeed(float value) {
        this.validateSpeed(value);
        EntityPlayer player = this.getHandle();

        player.abilities.walkSpeed = Math.max(value, 1.0E-4F) / 2.0F;
        player.updateAbilities();
    }

    public float getFlySpeed() {
        return this.getHandle().abilities.flySpeed * 2.0F;
    }

    public float getWalkSpeed() {
        return this.getHandle().abilities.walkSpeed * 2.0F;
    }

    private void validateSpeed(float value) {
        if (value < 0.0F) {
            if (value < -1.0F) {
                throw new IllegalArgumentException(value + " is too low");
            }
        } else if (value > 1.0F) {
            throw new IllegalArgumentException(value + " is too high");
        }

    }

    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, this.health);
        this.getHandle().triggerHealthUpdate();
    }

    public void resetMaxHealth() {
        super.resetMaxHealth();
        this.getHandle().triggerHealthUpdate();
    }

    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        PlayerConnection playerConnection = this.getHandle().playerConnection;

        if (playerConnection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        } else {
            playerConnection.isDisconnected();
            this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
        }
    }

    public void setHealthScale(double value) {
        Validate.isTrue((float) value > 0.0F, "Must be greater than 0");
        this.healthScale = value;
        this.scaledHealth = true;
        this.updateScaledHealth();
    }

    public double getHealthScale() {
        return this.healthScale;
    }

    public void setHealthScaled(boolean scale) {
        if (this.scaledHealth != (this.scaledHealth = scale)) {
            this.updateScaledHealth();
        }

    }

    public boolean isHealthScaled() {
        return this.scaledHealth;
    }

    public float getScaledHealth() {
        return (float) (this.isHealthScaled() ? this.getHealth() * this.getHealthScale() / this.getMaxHealth() : this.getHealth());
    }

    public double getHealth() {
        return this.health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }

    public void updateScaledHealth() {
        AttributeMapServer attributemapserver = (AttributeMapServer) this.getHandle().getAttributeMap();
        Set set = attributemapserver.getAttributes();

        this.injectScaledMaxHealth(set, true);
        this.getHandle().getDataWatcher().watch(6, Float.valueOf(this.getScaledHealth()));
        this.getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateHealth(this.getScaledHealth(), this.getHandle().getFoodData().getFoodLevel(), this.getHandle().getFoodData().getSaturationLevel()));
        this.getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateAttributes(this.getHandle().getId(), set));
        set.clear();
        this.getHandle().maxHealthCache = this.getMaxHealth();
    }

    public void injectScaledMaxHealth(Collection collection, boolean force) {
        if (this.scaledHealth || force) {
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                Object genericInstance = iterator.next();
                IAttribute attribute = ((AttributeInstance) genericInstance).getAttribute();

                if (attribute.getName().equals("generic.maxHealth")) {
                    collection.remove(genericInstance);
                    break;
                }
            }

            double healthMod = this.scaledHealth ? this.healthScale : this.getMaxHealth();

            if (healthMod >= 3.4028234663852886E38D || healthMod <= 0.0D) {
                healthMod = 20.0D;
                this.getServer().getLogger().warning(this.getName() + " tried to crash the server with a large health attribute");
            }

            collection.add(new AttributeModifiable(this.getHandle().getAttributeMap(), (new AttributeRanged((IAttribute) null, "generic.maxHealth", healthMod, 0.0D, 3.4028234663852886E38D)).a("Max Health").a(true)));
        }
    }

    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = this.getHandle().C();

        return followed == this.getHandle() ? null : followed.getBukkitEntity();
    }

    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(this.getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        this.getHandle().setSpectatorTarget(entity == null ? null : ((CraftEntity) entity).getHandle());
    }

    public void sendTitle(String title, String subtitle) {
        PacketPlayOutTitle packetSubtitle;

        if (title != null) {
            packetSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, CraftChatMessage.fromString(title)[0]);
            this.getHandle().playerConnection.sendPacket(packetSubtitle);
        }

        if (subtitle != null) {
            packetSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, CraftChatMessage.fromString(subtitle)[0]);
            this.getHandle().playerConnection.sendPacket(packetSubtitle);
        }

    }

    public void resetTitle() {
        PacketPlayOutTitle packetReset = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, (IChatBaseComponent) null);

        this.getHandle().playerConnection.sendPacket(packetReset);
    }

    public Player.Spigot spigot() {
        return this.spigot;
    }
}
