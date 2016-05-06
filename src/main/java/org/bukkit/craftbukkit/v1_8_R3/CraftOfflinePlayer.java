package org.bukkit.craftbukkit.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.WorldNBTStorage;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.world.WorldSavedData;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

@SerializableAs("Player")
public class CraftOfflinePlayer implements OfflinePlayer, ConfigurationSerializable {

    private final GameProfile profile;
    private final CraftServer server;
    private final WorldSavedData storage;

    protected CraftOfflinePlayer(CraftServer server, GameProfile profile) {
        this.server = server;
        this.profile = profile;
        this.storage = (WorldSavedData) (server.console.worldServerForDimension(0)).getSaveHandler().getPlayerNBTManager();
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public String getName() {
        Player player = this.getPlayer();

        if (player != null) {
            return player.getName();
        } else if (this.profile.getName() != null) {
            return this.profile.getName();
        } else {
            NBTTagCompound data = this.getBukkitData();

            return data != null && data.hasKey("lastKnownName") ? data.getString("lastKnownName") : null;
        }
    }

    public UUID getUniqueId() {
        return this.profile.getId();
    }

    public Server getServer() {
        return this.server;
    }

    public boolean isOp() {
        return this.server.getHandle().getOppedPlayers().func_183026_b(this.profile);
    }

    public void setOp(boolean value) {
        if (value != this.isOp()) {
            if (value) {
                this.server.getHandle().addOp(this.profile);
            } else {
                this.server.getHandle().removeOp(this.profile);
            }

        }
    }

    public boolean isBanned() {
        return this.getName() == null ? false : this.server.getBanList(BanList.Type.NAME).isBanned(this.getName());
    }

    public void setBanned(boolean value) {
        if (this.getName() != null) {
            if (value) {
                this.server.getBanList(BanList.Type.NAME).addBan(this.getName(), (String) null, (Date) null, (String) null);
            } else {
                this.server.getBanList(BanList.Type.NAME).pardon(this.getName());
            }

        }
    }

    public boolean isWhitelisted() {
        return this.server.getHandle().getWhitelistedPlayers().isWhitelisted(this.profile);
    }

    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().addWhitelistedPlayer(this.profile);
        } else {
            this.server.getHandle().removePlayerFromWhitelist(this.profile);
        }

    }

    public Map serialize() {
        LinkedHashMap result = new LinkedHashMap();

        result.put("UUID", this.profile.getId().toString());
        return result;
    }

    public static OfflinePlayer deserialize(Map args) {
        return args.get("name") != null ? Bukkit.getServer().getOfflinePlayer((String) args.get("name")) : Bukkit.getServer().getOfflinePlayer(UUID.fromString((String) args.get("UUID")));
    }

    public String toString() {
        return this.getClass().getSimpleName() + "[UUID=" + this.profile.getId() + "]";
    }

    public Player getPlayer() {
        return this.server.getPlayer(this.getUniqueId());
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof OfflinePlayer) {
            OfflinePlayer other = (OfflinePlayer) obj;

            return this.getUniqueId() != null && other.getUniqueId() != null ? this.getUniqueId().equals(other.getUniqueId()) : false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        byte hash = 5;
        int hash1 = 97 * hash + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);

        return hash1;
    }

    private NBTTagCompound getData() {
        return this.storage.getPlayerData(this.getUniqueId().toString());
    }

    private NBTTagCompound getBukkitData() {
        NBTTagCompound result = this.getData();

        if (result != null) {
            if (!result.hasKey("bukkit")) {
                result.set("bukkit", new NBTTagCompound());
            }

            result = result.getCompound("bukkit");
        }

        return result;
    }

    private File getDataFile() {
        return new File(this.storage.getPlayerDir(), this.getUniqueId() + ".dat");
    }

    public long getFirstPlayed() {
        Player player = this.getPlayer();

        if (player != null) {
            return player.getFirstPlayed();
        } else {
            NBTTagCompound data = this.getBukkitData();

            if (data != null) {
                if (data.hasKey("firstPlayed")) {
                    return data.getLong("firstPlayed");
                } else {
                    File file = this.getDataFile();

                    return file.lastModified();
                }
            } else {
                return 0L;
            }
        }
    }

    public long getLastPlayed() {
        Player player = this.getPlayer();

        if (player != null) {
            return player.getLastPlayed();
        } else {
            NBTTagCompound data = this.getBukkitData();

            if (data != null) {
                if (data.hasKey("lastPlayed")) {
                    return data.getLong("lastPlayed");
                } else {
                    File file = this.getDataFile();

                    return file.lastModified();
                }
            } else {
                return 0L;
            }
        }
    }

    public boolean hasPlayedBefore() {
        return this.getData() != null;
    }

    public Location getBedSpawnLocation() {
        NBTTagCompound data = this.getData();

        if (data == null) {
            return null;
        } else if (data.hasKey("SpawnX") && data.hasKey("SpawnY") && data.hasKey("SpawnZ")) {
            String spawnWorld = data.getString("SpawnWorld");

            if (spawnWorld.equals("")) {
                spawnWorld = ((World) this.server.getWorlds().get(0)).getName();
            }

            return new Location(this.server.getWorld(spawnWorld), (double) data.getInt("SpawnX"), (double) data.getInt("SpawnY"), (double) data.getInt("SpawnZ"));
        } else {
            return null;
        }
    }

    public void setMetadata(String metadataKey, MetadataValue metadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, metadataValue);
    }

    public List getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin plugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, plugin);
    }
}
