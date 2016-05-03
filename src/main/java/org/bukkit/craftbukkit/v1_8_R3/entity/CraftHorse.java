package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.UUID;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.HorseInventory;

public class CraftHorse extends CraftAnimals implements Horse {

    public CraftHorse(CraftServer server, EntityHorse entity) {
        super(server, (EntityAnimal) entity);
    }

    public EntityHorse getHandle() {
        return (EntityHorse) this.entity;
    }

    public Horse.Variant getVariant() {
        return Horse.Variant.values()[this.getHandle().getType()];
    }

    public void setVariant(Horse.Variant horse_variant) {
        Validate.notNull(horse_variant, "Variant cannot be null");
        this.getHandle().setType(horse_variant.ordinal());
    }

    public Horse.Color getColor() {
        return Horse.Color.values()[this.getHandle().getVariant() & 255];
    }

    public void setColor(Horse.Color color) {
        Validate.notNull(color, "Color cannot be null");
        this.getHandle().setVariant(color.ordinal() & 255 | this.getStyle().ordinal() << 8);
    }

    public Horse.Style getStyle() {
        return Horse.Style.values()[this.getHandle().getVariant() >>> 8];
    }

    public void setStyle(Horse.Style style) {
        Validate.notNull(style, "Style cannot be null");
        this.getHandle().setVariant(this.getColor().ordinal() & 255 | style.ordinal() << 8);
    }

    public boolean isCarryingChest() {
        return this.getHandle().hasChest();
    }

    public void setCarryingChest(boolean chest) {
        if (chest != this.isCarryingChest()) {
            this.getHandle().setHasChest(chest);
            this.getHandle().loadChest();
        }
    }

    public int getDomestication() {
        return this.getHandle().getTemper();
    }

    public void setDomestication(int value) {
        Validate.isTrue(value >= 0, "Domestication cannot be less than zero");
        Validate.isTrue(value <= this.getMaxDomestication(), "Domestication cannot be greater than the max domestication");
        this.getHandle().setTemper(value);
    }

    public int getMaxDomestication() {
        return this.getHandle().getMaxDomestication();
    }

    public void setMaxDomestication(int value) {
        Validate.isTrue(value > 0, "Max domestication cannot be zero or less");
        this.getHandle().maxDomestication = value;
    }

    public double getJumpStrength() {
        return this.getHandle().getJumpStrength();
    }

    public void setJumpStrength(double strength) {
        Validate.isTrue(strength >= 0.0D, "Jump strength cannot be less than zero");
        this.getHandle().getAttributeInstance(EntityHorse.attributeJumpStrength).setValue(strength);
    }

    public boolean isTamed() {
        return this.getHandle().isTame();
    }

    public void setTamed(boolean tamed) {
        this.getHandle().setTame(tamed);
    }

    public AnimalTamer getOwner() {
        return this.getOwnerUUID() == null ? null : this.getServer().getOfflinePlayer(this.getOwnerUUID());
    }

    public void setOwner(AnimalTamer owner) {
        if (owner != null) {
            this.setTamed(true);
            this.getHandle().setGoalTarget((EntityLiving) null, (EntityTargetEvent.TargetReason) null, false);
            this.setOwnerUUID(owner.getUniqueId());
        } else {
            this.setTamed(false);
            this.setOwnerUUID((UUID) null);
        }

    }

    public UUID getOwnerUUID() {
        try {
            return UUID.fromString(this.getHandle().getOwnerUUID());
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        if (uuid == null) {
            this.getHandle().setOwnerUUID("");
        } else {
            this.getHandle().setOwnerUUID(uuid.toString());
        }

    }

    public HorseInventory getInventory() {
        return new CraftInventoryHorse(this.getHandle().inventoryChest);
    }

    public String toString() {
        return "CraftHorse{variant=" + this.getVariant() + ", owner=" + this.getOwner() + '}';
    }

    public EntityType getType() {
        return EntityType.HORSE;
    }
}
