package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityComplexPart;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;

public class CraftEnderDragonPart extends CraftComplexPart implements EnderDragonPart {

    public CraftEnderDragonPart(CraftServer server, EntityComplexPart entity) {
        super(server, entity);
    }

    public EnderDragon getParent() {
        return (EnderDragon) super.getParent();
    }

    public EntityComplexPart getHandle() {
        return (EntityComplexPart) this.entity;
    }

    public String toString() {
        return "CraftEnderDragonPart";
    }

    public void damage(double amount) {
        this.getParent().damage(amount);
    }

    public void damage(double amount, Entity source) {
        this.getParent().damage(amount, source);
    }

    public double getHealth() {
        return this.getParent().getHealth();
    }

    public void setHealth(double health) {
        this.getParent().setHealth(health);
    }

    public double getMaxHealth() {
        return this.getParent().getMaxHealth();
    }

    public void setMaxHealth(double health) {
        this.getParent().setMaxHealth(health);
    }

    public void resetMaxHealth() {
        this.getParent().resetMaxHealth();
    }

    /** @deprecated */
    @Deprecated
    public void damage(int amount) {
        this.damage((double) amount);
    }

    /** @deprecated */
    @Deprecated
    public void damage(int amount, Entity source) {
        this.damage((double) amount, source);
    }

    /** @deprecated */
    @Deprecated
    public int getHealth() {
        return NumberConversions.ceil(this.getHealth());
    }

    /** @deprecated */
    @Deprecated
    public void setHealth(int health) {
        this.setHealth((double) health);
    }

    /** @deprecated */
    @Deprecated
    public int getMaxHealth() {
        return NumberConversions.ceil(this.getMaxHealth());
    }

    /** @deprecated */
    @Deprecated
    public void setMaxHealth(int health) {
        this.setMaxHealth((double) health);
    }
}
