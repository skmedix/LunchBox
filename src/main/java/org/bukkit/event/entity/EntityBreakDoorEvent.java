package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class EntityBreakDoorEvent extends EntityChangeBlockEvent {

    public EntityBreakDoorEvent(LivingEntity entity, Block targetBlock) {
        super(entity, targetBlock, Material.AIR, (byte) 0);
    }

    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }
}
