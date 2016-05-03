package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {

    public CraftMinecart(CraftServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    public void setDamage(double damage) {
        this.getHandle().setDamage((float) damage);
    }

    public double getDamage() {
        return (double) this.getHandle().getDamage();
    }

    public double getMaxSpeed() {
        return this.getHandle().maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0.0D) {
            this.getHandle().maxSpeed = speed;
        }

    }

    public boolean isSlowWhenEmpty() {
        return this.getHandle().slowWhenEmpty;
    }

    public void setSlowWhenEmpty(boolean slow) {
        this.getHandle().slowWhenEmpty = slow;
    }

    public Vector getFlyingVelocityMod() {
        return this.getHandle().getFlyingVelocityMod();
    }

    public void setFlyingVelocityMod(Vector flying) {
        this.getHandle().setFlyingVelocityMod(flying);
    }

    public Vector getDerailedVelocityMod() {
        return this.getHandle().getDerailedVelocityMod();
    }

    public void setDerailedVelocityMod(Vector derailed) {
        this.getHandle().setDerailedVelocityMod(derailed);
    }

    public EntityMinecartAbstract getHandle() {
        return (EntityMinecartAbstract) this.entity;
    }

    /** @deprecated */
    @Deprecated
    public void setDamage(int damage) {
        this.setDamage((double) damage);
    }

    /** @deprecated */
    @Deprecated
    public int getDamage() {
        return NumberConversions.ceil(this.getDamage());
    }

    public void setDisplayBlock(MaterialData material) {
        if (material != null) {
            IBlockData block = CraftMagicNumbers.getBlock(material.getItemTypeId()).fromLegacyData(material.getData());

            this.getHandle().setDisplayBlock(block);
        } else {
            this.getHandle().setDisplayBlock(Blocks.AIR.getBlockData());
            this.getHandle().a(false);
        }

    }

    public MaterialData getDisplayBlock() {
        IBlockData blockData = this.getHandle().getDisplayBlock();

        return CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte) blockData.getBlock().toLegacyData(blockData));
    }

    public void setDisplayBlockOffset(int offset) {
        this.getHandle().SetDisplayBlockOffset(offset);
    }

    public int getDisplayBlockOffset() {
        return this.getHandle().getDisplayBlockOffset();
    }
}
