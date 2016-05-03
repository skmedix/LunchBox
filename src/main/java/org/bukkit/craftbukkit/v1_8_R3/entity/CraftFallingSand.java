package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingSand;

public class CraftFallingSand extends CraftEntity implements FallingSand {

    public CraftFallingSand(CraftServer server, EntityFallingBlock entity) {
        super(server, entity);
    }

    public EntityFallingBlock getHandle() {
        return (EntityFallingBlock) this.entity;
    }

    public String toString() {
        return "CraftFallingSand";
    }

    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }

    public Material getMaterial() {
        return Material.getMaterial(this.getBlockId());
    }

    public int getBlockId() {
        return CraftMagicNumbers.getId(this.getHandle().getBlock().getBlock());
    }

    public byte getBlockData() {
        return (byte) this.getHandle().getBlock().getBlock().toLegacyData(this.getHandle().getBlock());
    }

    public boolean getDropItem() {
        return this.getHandle().dropItem;
    }

    public void setDropItem(boolean drop) {
        this.getHandle().dropItem = drop;
    }

    public boolean canHurtEntities() {
        return this.getHandle().hurtEntities;
    }

    public void setHurtEntities(boolean hurtEntities) {
        this.getHandle().hurtEntities = hurtEntities;
    }
}
