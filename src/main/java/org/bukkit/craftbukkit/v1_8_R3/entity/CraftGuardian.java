package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, EntityGuardian entity) {
        super(server, (EntityMonster) entity);
    }

    public String toString() {
        return "CraftGuardian";
    }

    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    public boolean isElder() {
        return ((EntityGuardian) this.entity).isElder();
    }

    public void setElder(boolean shouldBeElder) {
        EntityGuardian entityGuardian = (EntityGuardian) this.entity;

        if (!this.isElder() && shouldBeElder) {
            entityGuardian.setElder(true);
        } else if (this.isElder() && !shouldBeElder) {
            entityGuardian.setElder(false);
            this.entity.setSize(0.85F, 0.85F);
            entityGuardian.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(6.0D);
            entityGuardian.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
            entityGuardian.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
            entityGuardian.getAttributeInstance(GenericAttributes.maxHealth).setValue(30.0D);
            entityGuardian.goalRandomStroll.setTimeBetweenMovement(80);
            entityGuardian.initAttributes();
        }

    }
}
