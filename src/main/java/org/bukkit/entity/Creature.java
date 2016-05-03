package org.bukkit.entity;

public interface Creature extends LivingEntity {

    void setTarget(LivingEntity livingentity);

    LivingEntity getTarget();
}
