package org.bukkit.craftbukkit.v1_8_R3;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;

public class CraftProfileBanList implements BanList {

    private final UserListBans list;

    public CraftProfileBanList(UserListBans list) {
        this.list = list;
    }

    public BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(target);

        if (profile == null) {
            return null;
        } else {
            UserListBansEntry entry = this.list.getEntry(profile);

            return entry == null ? null : new CraftProfileBanEntry(profile, entry, this.list);
        }
    }

    public BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(target);

        if (profile == null) {
            return null;
        } else {
            UserListBansEntry entry = new UserListBansEntry(profile, new Date(), StringUtils.isBlank(source) ? null : source, expires, StringUtils.isBlank(reason) ? null : reason);

            this.list.addEntry(entry);

            try {
                this.list.writeChanges();
            } catch (IOException ioexception) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ioexception.getMessage());
            }

            return new CraftProfileBanEntry(profile, entry, this.list);
        }
    }

    public Set<BanEntry> getBanEntries() {
        ImmutableSet.Builder builder = ImmutableSet.builder();

        for (UserListEntry entry : list.values.values()) {
            GameProfile profile = (GameProfile) entry.value;
            builder.add((Object) (new CraftProfileBanEntry(profile, (UserListBansEntry) entry, this.list)));
        }

        return builder.build();
    }

    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(target);

        return profile == null ? false : this.list.isBanned(profile);
    }

    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(target);

        this.list.removeEntry(profile);
    }
}
