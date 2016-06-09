package org.bukkit.craftbukkit.v1_8_R3.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.*;
import net.minecraft.stats.StatBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.NotImplementedException;
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
    private boolean fauxSleeping = false;
    /* LunchBox - remove spigot for now todo
    private final Player.Spigot spigot = new Player.Spigot() {
        public InetSocketAddress getRawAddress() {
            return (InetSocketAddress) CraftPlayer.this.getMPPlayer().playerNetServerHandler.netManager.getRemoteAddress();
        }

        public boolean getCollidesWithEntities() {
            return CraftPlayer.this.getHandle().canBeCollidedWith();
        }

        public void setCollidesWithEntities(boolean collides) {
            CraftPlayer.this.getHandle().isCollided = collides;
            //CraftPlayer.this.getHandle().k = collides;
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
                packet = new S28PacketEffect(distance, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id, false);
            } else {
                EnumParticleTypes enumparticle = null;
                int[] extra = null;
                EnumParticleTypes[] aenumparticle;
                int i = (aenumparticle = EnumParticleTypes.values()).length;

                for (int j = 0; j < i; ++j) {
                    EnumParticleTypes p = aenumparticle[j];

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

                packet = new S2APacketParticles(enumparticle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra);
            }

            radius *= radius;
            if (CraftPlayer.this.getMPPlayer().playerNetServerHandler != null) {
                if (location.getWorld().equals(CraftPlayer.this.getWorld())) {
                    distance = (int) CraftPlayer.this.getLocation().distanceSquared(location);
                    if (distance <= radius) {
                        CraftPlayer.this.getMPPlayer().playerNetServerHandler.sendPacket((Packet) packet);
                    }

                }
            }
        }

        public String getLocale() {
            return CraftPlayer.this.getMPPlayer().;
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
            if (CraftPlayer.this.getMPPlayer().playerNetServerHandler != null) {
                PacketPlayOutChat packet = new PacketPlayOutChat();

                packet.components = components;
                CraftPlayer.this.getMPPlayer().playerNetServerHandler.sendPacket(packet);
            }
        }
    };*/

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
        this.firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return this.getMPPlayer().getGameProfile();
    }

    public boolean isOp() {
        List ops = Arrays.asList(this.server.getHandle().getOppedPlayers());
        return ops.contains(this.getProfile());
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
        if (this.getMPPlayer().playerNetServerHandler == null) {
            return null;
        } else {
            SocketAddress addr = this.getMPPlayer().playerNetServerHandler.netManager.getRemoteAddress();

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
        if (this.getMPPlayer().playerNetServerHandler != null) {
            IChatComponent[] aichatbasecomponent;
            int i = (aichatbasecomponent = CraftChatMessage.fromString(message)).length;

            for (int j = 0; j < i; ++j) {
                IChatComponent component = aichatbasecomponent[j];

                this.getMPPlayer().playerNetServerHandler.sendPacket(new S02PacketChat(component));
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
        return this.getHandle().getDisplayName().toString();
    }

    public void setDisplayName(String name) {
        this.getMPPlayer().setCustomNameTag(name);
    }

    public String getPlayerListName() {
        return this.getMPPlayer().getTabListDisplayName() == null ? this.getName() : CraftChatMessage.fromComponent(this.getMPPlayer().getTabListDisplayName());
    }

    public void setPlayerListName(String name) {
        if (name == null) {
            name = this.getName();
        }

        this.getMPPlayer().setCustomNameTag(name);
        Iterator iterator = this.server.getHandle().playerEntityList.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP player = (EntityPlayerMP) iterator.next();

            if (player.canEntityBeSeen(this.getMPPlayer())) {
                player.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME, (EntityPlayerMP[]) new EntityPlayer[] { this.getMPPlayer() }));
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
        if (this.getMPPlayer().playerNetServerHandler != null) {
            this.getMPPlayer().playerNetServerHandler.onDisconnect(IChatComponent.Serializer.jsonToComponent(message == null ? "" : message));
        }
    }

    public void setCompassTarget(Location loc) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
            this.getMPPlayer().playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
        }
    }
    public Location getCompassTarget() {
        S05PacketSpawnPosition packet = new S05PacketSpawnPosition();
        return new Location(this.getWorld(), packet.getSpawnPos().getX(), packet.getSpawnPos().getY(), packet.getSpawnPos().getZ());
    }

    public void chat(String msg) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
            this.getMPPlayer().playerNetServerHandler.processChatMessage(new C01PacketChatMessage(msg));
        }
    }

    public boolean performCommand(String command) {
        return this.server.dispatchCommand(this, command);
    }

    public void playNote(Location loc, byte instrument, byte note) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
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

            this.getMPPlayer().playerNetServerHandler.sendPacket(new S29PacketSoundEffect("note." + instrumentName, (double) loc.getBlockX(), (double) loc.getBlockY(), (double) loc.getBlockZ(), 3.0F, f));
        }
    }

    public void playNote(Location loc, Instrument instrument, Note note) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
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

            this.getMPPlayer().playerNetServerHandler.sendPacket(new S29PacketSoundEffect("note." + instrumentName, (double) loc.getBlockX(), (double) loc.getBlockY(), (double) loc.getBlockZ(), 3.0F, f));
        }
    }

    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (sound != null) {
            this.playSound(loc, CraftSound.getSound(sound), volume, pitch);
        }
    }

    public void playSound(Location loc, String sound, float volume, float pitch) {
        if (loc != null && sound != null && this.getMPPlayer().playerNetServerHandler != null) {
            double x = (double) loc.getBlockX() + 0.5D;
            double y = (double) loc.getBlockY() + 0.5D;
            double z = (double) loc.getBlockZ() + 0.5D;
            S29PacketSoundEffect packet = new S29PacketSoundEffect(sound, x, y, z, volume, pitch);

            this.getMPPlayer().playerNetServerHandler.sendPacket(packet);
        }
    }
/* LunchBox - Remove for now todo
    public void playEffect(Location loc, Effect effect, int data) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
            this.spigot().playEffect(loc, effect, data, 0, 0.0F, 0.0F, 0.0F, 1.0F, 1, 64);
        }
    }
*/
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
        if (this.getMPPlayer().playerNetServerHandler != null) {
            S23PacketBlockChange packet = new S23PacketBlockChange(((CraftWorld) loc.getWorld()).getHandle(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

            packet.blockState = CraftMagicNumbers.getBlock(material).getStateFromMeta(data);
            this.getMPPlayer().playerNetServerHandler.sendPacket(packet);
        }
    }

    public void sendSignChange(Location loc, String[] lines) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
            if (lines == null) {
                lines = new String[4];
            }

            Validate.notNull(loc, "Location can not be null");
            if (lines.length < 4) {
                throw new IllegalArgumentException("Must have at least 4 lines");
            } else {
                IChatComponent[] components = CraftSign.sanitizeLines(lines);

                this.getMPPlayer().playerNetServerHandler.sendPacket(new S33PacketUpdateSign(this.getHandle().getEntityWorld(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), components));
            }
        }
    }

    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (this.getMPPlayer().playerNetServerHandler == null) {
            return false;
        } else {
            throw new NotImplementedException("Chunk changes do not yet work");
        }
    }

    public void sendMap(MapView map) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
            RenderData data = ((CraftMapView) map).render(this);
            ArrayList icons = new ArrayList();
            Iterator iterator = data.cursors.iterator();

            while (iterator.hasNext()) {
                MapCursor packet = (MapCursor) iterator.next();

                if (packet.isVisible()) {
                    icons.add(new MapCursor(packet.getX(), packet.getY(), packet.getDirection(), packet.getRawType(), true));
                }
            }

            S34PacketMaps packet1 = new S34PacketMaps(map.getId(), map.getScale().getValue(), icons, data.buffer, 0, 0, 0, 0);

            this.getMPPlayer().playerNetServerHandler.sendPacket(packet1);
        }
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        EntityPlayerMP entity = this.getMPPlayer();

        if (this.getHealth() != 0.0D && !entity.isDead) {
            if (entity.playerNetServerHandler != null) {
                if (entity.ridingEntity != null) {
                    return false;
                } else {
                    Location from = this.getLocation();
                    PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, location, cause);

                    this.server.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return false;
                    } else {
                        entity.mountEntity((Entity) null);
                        from = event.getFrom();
                        Location to = event.getTo();
                        net.minecraft.world.World fromWorld = ((CraftWorld) from.getWorld()).getHandle();
                        WorldServer toWorld = ((CraftWorld) to.getWorld()).getHandle();

                        if (this.getMPPlayer().openContainer != this.getMPPlayer().inventoryContainer) {
                            this.getMPPlayer().closeContainer();
                        }

                        if (fromWorld == toWorld) {
                            entity.setWorld(((CraftWorld) to.getWorld()).getHandle());
                            entity.setLocationAndAngles(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                        } else {
                            this.server.getHandle().transferPlayerToDimension(entity, toWorld.provider.getDimensionId());
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
    //todo: need to come back and rework loadData nad saveData.
    public void loadData() {
        server.getHandle().playerNBTManagerObj.readPlayerData(this.getMPPlayer());;
    }

    public void saveData() {
        this.server.getHandle().playerNBTManagerObj.writePlayerData(this.getMPPlayer());
    }

    /** @deprecated */
    @Deprecated
    public void updateInventory() {
        this.getMPPlayer().updateHeldItem();
    }
    //todo
    public void setSleepingIgnored(boolean isSleeping) {
        this.fauxSleeping = isSleeping;
        ((CraftWorld) this.getWorld()).getHandle().updateAllPlayersSleepingFlag();
    }
    //todo
    public boolean isSleepingIgnored() {
        return this.fauxSleeping;
    }

    public void awardAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        if (achievement.hasParent() && !this.hasAchievement(achievement.getParent())) {
            this.awardAchievement(achievement.getParent());
        }

        this.getMPPlayer().getStatFile().unlockAchievement(this.getMPPlayer(), CraftStatistic.getNMSAchievement(achievement), 1);
        this.getMPPlayer().getStatFile().func_150876_a(this.getMPPlayer());//LunchBox - not sure if this is correct todo
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

        this.getMPPlayer().getStatFile().unlockAchievement(this.getMPPlayer(), CraftStatistic.getNMSAchievement(achievement), 0);
    }

    public boolean hasAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        return this.getMPPlayer().getStatFile().hasAchievementUnlocked(CraftStatistic.getNMSAchievement(achievement));
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
        return this.getMPPlayer().getStatFile().readStat(CraftStatistic.getNMSStatistic(statistic));
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
        StatBase nmsStatistic = CraftStatistic.getNMSStatistic(statistic);

        this.getMPPlayer().getStatFile().unlockAchievement(this.getMPPlayer(), nmsStatistic, newValue);
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
        StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        return this.getMPPlayer().getStatFile().readStat(nmsStatistic);
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
        StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        this.getMPPlayer().getStatFile().unlockAchievement(this.getMPPlayer(), nmsStatistic, newValue);
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
        StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);

        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        return this.getMPPlayer().getStatFile().readStat(nmsStatistic);
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
        StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);

        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        this.getMPPlayer().getStatFile().unlockAchievement(this.getMPPlayer(), nmsStatistic, newValue);
    }

    public void setPlayerTime(long time, boolean relative) {
        /*
        this.getMPPlayer().timeOffset = time;
        this.getHandle().relativeTime = relative;
        */
        throw new NotImplementedException("Is not done yet.");
    }

    public long getPlayerTimeOffset() {
        //return this.getHandle().timeOffset;
        throw new NotImplementedException("Is not done yet.");
    }

    public long getPlayerTime() {
        return this.getMPPlayer().getLastActiveTime();
    }

    public boolean isPlayerTimeRelative() {
        //return this.getHandle().relativeTime;
        throw new NotImplementedException("Is not done yet.");
    }

    public void resetPlayerTime() {
        this.setPlayerTime(0L, true);
    }

    public WeatherType weather = null;

    public void setPlayerWeather(WeatherType type, boolean plugin) {
        if (!plugin && this.weather != null) {
            return;
        }
        if (plugin) {
            this.weather = type;
        }
        if (type == WeatherType.DOWNFALL) {
            getMPPlayer().playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(2, 0));

        }
        else
        {
            getMPPlayer().playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0));
        }
    }

    public WeatherType getPlayerWeather() {
        return this.weather;
    }

    public void resetPlayerWeather() {
        this.weather = null;
        this.setPlayerWeather(this.getMPPlayer().worldObj.getWorldInfo().isRaining() ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
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
        return this.server.getHandle().getWhitelistedPlayers().isWhitelisted(this.getProfile());
    }

    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().addWhitelistedPlayer(this.getProfile());
        } else {
            this.server.getHandle().removePlayerFromWhitelist(this.getProfile());
        }

    }

    public void setGameMode(GameMode mode) {
        if (this.getMPPlayer().playerNetServerHandler != null) {
            if (mode == null) {
                throw new IllegalArgumentException("Mode cannot be null");
            } else {
                if (mode != this.getGameMode()) {
                    PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(this, mode);

                    this.server.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }

                    this.getMPPlayer().setSpectatingEntity(this.getHandle());
                    this.getMPPlayer().setGameType(WorldSettings.GameType.getByID(mode.getValue()));
                    this.getHandle().fallDistance = 0.0F;
                    this.getMPPlayer().playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(3, (float) mode.getValue()));
                }

            }
        }
    }

    public GameMode getGameMode() {
        return GameMode.getByValue(this.getMPPlayer().theItemInWorldManager.getGameType().getID());
    }

    public void giveExp(int exp) {
        this.getMPPlayer().addExperience(exp);
    }

    public void giveExpLevels(int levels) {
        this.getMPPlayer().addExperienceLevel(levels);
    }

    public float getExp() {
        return this.getMPPlayer().experience;
    }

    public void setExp(float exp) {
        this.getMPPlayer().experience = exp;
        //his.getHandle().lastSentExp = -1;//LunchBox
    }

    public int getLevel() {
        return this.getMPPlayer().experienceLevel;
    }

    public void setLevel(int level) {
        this.getMPPlayer().experienceLevel = level;
        //his.getHandle().lastSentExp = -1;//LunchBox
    }

    public int getTotalExperience() {
        return this.getMPPlayer().experienceTotal;
    }

    public void setTotalExperience(int exp) {
        this.getMPPlayer().experienceTotal = exp;
    }

    public float getExhaustion() {
        return this.getMPPlayer().getEntityData().getFloat("foodExhaustionLevel");
    }

    public void setExhaustion(float value) {
        this.getMPPlayer().getEntityData().setFloat("foodExhaustionLevel", value);
    }

    public float getSaturation() {
        return this.getMPPlayer().getFoodStats().getSaturationLevel();
    }

    public void setSaturation(float value) {
        this.getMPPlayer().getFoodStats().setFoodSaturationLevel(value);
    }

    public int getFoodLevel() {
        return this.getMPPlayer().getFoodStats().getFoodLevel();
    }

    public void setFoodLevel(int value) {
        this.getMPPlayer().getFoodStats().setFoodLevel(value);
    }

    public Location getBedSpawnLocation() {
        World world = (World) DimensionManager.getWorld(0);
        BlockPos bed = this.getMPPlayer().getBedLocation();

        if (world != null && bed != null) {
            bed = this.getMPPlayer().getBedLocation();
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
            this.getMPPlayer().setSpawnPoint((BlockPos) null, override);
        } else {
            this.getMPPlayer().setSpawnPoint(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
            //this.getMPPlayer().spawnWorld = location.getWorld().getName();
        }

    }

    public void hidePlayer(Player player) {
        Validate.notNull(player, "hidden player cannot be null");
        if (this.getMPPlayer().playerNetServerHandler != null) {
            if (!this.equals(player)) {
                if (!this.hiddenPlayers.contains(player.getUniqueId())) {
                    this.hiddenPlayers.add(player.getUniqueId());
                    EntityTracker tracker = ((WorldServer) this.entity.getEntityWorld()).getEntityTracker();
                    EntityPlayer other = ((CraftPlayer) player).getMPPlayer();
                    EntityTrackerEntry entry = (EntityTrackerEntry) tracker.getTrackingPlayers(other);

                    if (entry != null) {
                        entry.removeFromTrackedPlayers(this.getMPPlayer());
                    }

                    this.getMPPlayer().playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, (EntityPlayerMP[]) new EntityPlayer[] { other }));
                }
            }
        }
    }

    public void showPlayer(Player player) {
        Validate.notNull(player, "shown player cannot be null");
        if (this.getMPPlayer().playerNetServerHandler != null) {
            if (!this.equals(player)) {
                if (this.hiddenPlayers.contains(player.getUniqueId())) {
                    this.hiddenPlayers.remove(player.getUniqueId());
                    EntityTracker tracker = ((WorldServer) this.entity.worldObj).getEntityTracker();
                    EntityPlayer other = ((CraftPlayer) player).getMPPlayer();
                    this.getMPPlayer().playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, (EntityPlayerMP[]) new EntityPlayer[] { other }));
                    EntityTrackerEntry entry = (EntityTrackerEntry) tracker.getTrackingPlayers(other);

                    if (entry != null && !entry.trackingPlayers.contains(this.getHandle())) {
                        entry.updatePlayerEntity(this.getMPPlayer());
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
        super.setHandle((EntityPlayerMP) (entity));
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
                //handle.expToDrop = data.getInteger("expToDrop"); // TODO: 6/8/2016  
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
        //data.setInteger("expToDrop", handle.expToDrop); // TODO: 6/8/2016  
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
    //todo: redo this method
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        Container container = ((EntityPlayerMP) (EntityLivingBase) this.getHandle()).openContainer;

        if (container.get.getType() != prop.getType()) {
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
        return this.getMPPlayer().capabilities.isFlying;
    }

    public void setFlying(boolean value) {
        if (!this.getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        } else {
            this.getMPPlayer().capabilities.isFlying = value;
            this.getMPPlayer().updateHeldItem();
        }
    }

    public boolean getAllowFlight() {
        return this.getMPPlayer().capabilities.allowFlying;
    }

    public void setAllowFlight(boolean value) {
        if (this.isFlying() && !value) {
            this.getMPPlayer().capabilities.isFlying = false;
        }

        this.getMPPlayer().capabilities.allowFlying = value;
        this.getMPPlayer().updateHeldItem();
    }

    public int getNoDamageTicks() {
        return this.getMPPlayer().getLastAttackerTime() > 0 ? Math.max(this.getMPPlayer().getLastAttackerTime(), this.getMPPlayer().getLastAttackerTime()) : this.getMPPlayer().getLastAttackerTime();
    }

    public void setFlySpeed(float value) {
        this.validateSpeed(value);
        EntityPlayer player = this.getMPPlayer();

        player.capabilities.setFlySpeed(Math.max(value, 1.0E-4F) / 2.0F);
        player.updateRidden();
    }

    public void setWalkSpeed(float value) {
        this.validateSpeed(value);
        EntityPlayer player = this.getMPPlayer();

        player.capabilities.setPlayerWalkSpeed(Math.max(value, 1.0E-4F) / 2.0F);
        player.updateRidden();
    }

    public float getFlySpeed() {
        return this.getMPPlayer().capabilities.getFlySpeed() * 2.0F;
    }

    public float getWalkSpeed() {
        return this.getMPPlayer().capabilities.getWalkSpeed() * 2.0F;
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
    //todo: redo setMaxHealth and resetMaxHealth
    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, this.health);
        this.getMPPlayer().setPlayerHealthUpdated();
    }

    public void resetMaxHealth() {
        super.resetMaxHealth();
        this.getMPPlayer().setPlayerHealthUpdated();
    }

    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        NetHandlerPlayServer playerConnection = this.getMPPlayer().playerNetServerHandler;

        if (playerConnection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        } else {
            playerConnection.netManager.checkDisconnected();
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
    @Override
    public double getHealth() {
        return this.health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }
    //todo
    public void updateScaledHealth() {

        BaseAttributeMap attributemapserver = (BaseAttributeMap) this.getHandle().getAttributeMap();
        Set set = (Set) attributemapserver.getAllAttributes();

        this.injectScaledMaxHealth(set, true);
        this.getHandle().getDataWatcher().addObject(6, Float.valueOf(this.getScaledHealth()));
        this.getMPPlayer().playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(this.getScaledHealth(), this.getMPPlayer().getFoodStats().getFoodLevel(), this.getMPPlayer().getFoodStats().getSaturationLevel()));
        this.getMPPlayer().playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.getMPPlayer().getEntityId(), set));
        set.clear();
        this.getMPPlayer().setPlayerHealthUpdated();
    }

    public void injectScaledMaxHealth(Collection collection, boolean force) {
        if (this.scaledHealth || force) {
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                Object genericInstance = iterator.next();
                IAttribute attribute = ((BaseAttribute) genericInstance);

                if (attribute.getAttributeUnlocalizedName().equals("generic.maxHealth")) {
                    collection.remove(genericInstance);
                    break;
                }
            }

            double healthMod = this.scaledHealth ? this.healthScale : this.getMaxHealth();

            if (healthMod >= 3.4028234663852886E38D || healthMod <= 0.0D) {
                healthMod = 20.0D;
                this.getServer().getLogger().warning(this.getName() + " tried to crash the server with a large health attribute");
            }

            collection.add(new ModifiableAttributeInstance(this.getHandle().getAttributeMap(), (new RangedAttribute((IAttribute) null, "generic.maxHealth", healthMod, 0.0D, 3.4028234663852886E38D)).setDescription("Max Health").setShouldWatch(true)));
        }
    }

    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = this.getMPPlayer().getSpectatingEntity();

        return followed == this.getHandle() ? null : CraftEntity.getEntity(this.server, followed);
    }

    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(this.getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        this.getMPPlayer().setSpectatingEntity(entity == null ? null : ((CraftEntity) entity).getHandle());
    }

    public void sendTitle(String title, String subtitle) {
        S45PacketTitle packetSubtitle;

        if (title != null) {
            packetSubtitle = new S45PacketTitle(S45PacketTitle.Type.TITLE, CraftChatMessage.fromString(title)[0]);
            this.getMPPlayer().playerNetServerHandler.sendPacket(packetSubtitle);
        }

        if (subtitle != null) {
            packetSubtitle = new S45PacketTitle(S45PacketTitle.Type.SUBTITLE, CraftChatMessage.fromString(subtitle)[0]);
            this.getMPPlayer().playerNetServerHandler.sendPacket(packetSubtitle);
        }

    }

    public void resetTitle() {
        S45PacketTitle packetReset = new S45PacketTitle(S45PacketTitle.Type.RESET, (IChatComponent) null);

        this.getMPPlayer().playerNetServerHandler.sendPacket(packetReset);
    }

    public EntityPlayerMP getMPPlayer() {
        return (EntityPlayerMP) (EntityLivingBase) this.getHandle();
    }

    /*public Player.Spigot spigot() {
        return this.spigot;
    }*/
}
