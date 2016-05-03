package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public class EntityDeathEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();
    private final List drops;
    private int dropExp;

    public EntityDeathEvent(LivingEntity entity, List drops) {
        this(entity, drops, 0);
    }

    public EntityDeathEvent(LivingEntity what, List drops, int droppedExp) {
        super(what);
        this.dropExp = 0;
        this.drops = drops;
        this.dropExp = droppedExp;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    public int getDroppedExp() {
        return this.dropExp;
    }

    public void setDroppedExp(int exp) {
        this.dropExp = exp;
    }

    public List getDrops() {
        return this.drops;
    }

    public HandlerList getHandlers() {
        return EntityDeathEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityDeathEvent.handlers;
    }
}
