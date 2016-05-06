package org.bukkit.craftbukkit.v1_8_R3;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

import net.minecraft.server.management.BanList;
import net.minecraft.server.management.IPBanEntry;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;

public final class CraftIpBanEntry implements BanEntry {

    private final BanList list;
    private final String target;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    public CraftIpBanEntry(String target, net.minecraft.server.management.IPBanEntry entry, BanList list) {
        this.list = list;
        this.target = target;
        this.created = entry.getBanEndDate() != null ? new Date(entry.getBanEndDate().getTime()) : null;
        this.source = entry.getBanReason();
        this.expiration = entry.getBanEndDate() != null ? new Date(entry.getBanEndDate().getTime()) : null;
        this.reason = entry.getBanReason();
    }

    public String getTarget() {
        return this.target;
    }

    public Date getCreated() {
        return this.created == null ? null : (Date) this.created.clone();
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getExpiration() {
        return this.expiration == null ? null : (Date) this.expiration.clone();
    }

    public void setExpiration(Date expiration) {
        if (expiration != null && expiration.getTime() == (new Date(0, 0, 0, 0, 0, 0)).getTime()) {
            expiration = null;
        }

        this.expiration = expiration;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void save() {
        IPBanEntry entry = new IPBanEntry(this.target, this.created, this.source, this.expiration, this.reason);

        this.list.addEntry(entry);

        try {
            this.list.writeChanges();
        } catch (IOException ioexception) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-ips.json, {0}", ioexception.getMessage());
        }

    }
}
