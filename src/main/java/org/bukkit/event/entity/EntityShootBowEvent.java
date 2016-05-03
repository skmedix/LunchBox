package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class EntityShootBowEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack bow;
    private Entity projectile;
    private final float force;
    private boolean cancelled;

    public EntityShootBowEvent(LivingEntity shooter, ItemStack bow, Projectile projectile, float force) {
        super(shooter);
        this.bow = bow;
        this.projectile = projectile;
        this.force = force;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    public ItemStack getBow() {
        return this.bow;
    }

    public Entity getProjectile() {
        return this.projectile;
    }

    public void setProjectile(Entity projectile) {
        this.projectile = projectile;
    }

    public float getForce() {
        return this.force;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return EntityShootBowEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityShootBowEvent.handlers;
    }
}
