package org.bukkit.craftbukkit.v1_8_R3;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import com.mojang.authlib.GameProfileRepository;
import net.minecraft.server.v1_8_R3.GameProfileBanEntry;
import net.minecraft.server.v1_8_R3.GameProfileBanList;
import net.minecraft.server.v1_8_R3.JsonListEntry;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;

public class CraftProfileBanList implements BanList {

    private final net.minecraft.server.management.BanList list;

    public CraftProfileBanList(BanList list) {
        this.list = list;
    }

    public BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);

        if (profile == null) {
            return null;
        } else {
            GameProfileBanEntry entry = (GameProfileBanEntry) this.list.get(profile);

            return entry == null ? null : new CraftProfileBanEntry(profile, entry, this.list);
        }
    }

    public BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);

        if (profile == null) {
            return null;
        } else {
            GameProfileBanEntry entry = new GameProfileBanEntry(profile, new Date(), StringUtils.isBlank(source) ? null : source, expires, StringUtils.isBlank(reason) ? null : reason);

            this.list.add(entry);

            try {
                this.list.save();
            } catch (IOException ioexception) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ioexception.getMessage());
            }

            return new CraftProfileBanEntry(profile, entry, this.list);
        }
    }

    public Set getBanEntries() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        Iterator iterator = this.list.getValues().iterator();

        while (iterator.hasNext()) {
            JsonListEntry entry = (JsonListEntry) iterator.next();
            GameProfile profile = (GameProfile) entry.getKey();

            builder.add((Object) (new CraftProfileBanEntry(profile, (GameProfileBanEntry) entry, this.list)));
        }

        return builder.build();
    }

    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);

        return profile == null ? false : this.list.isBanned(profile);
    }

    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");
        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);

        this.list.remove(profile);
    }
}
