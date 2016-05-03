package org.bukkit.event.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PotionSplashEvent extends ProjectileHitEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Map affectedEntities;

    public PotionSplashEvent(ThrownPotion potion, Map affectedEntities) {
        super((Projectile) potion);
        this.affectedEntities = affectedEntities;
    }

    public ThrownPotion getEntity() {
        return (ThrownPotion) this.entity;
    }

    public ThrownPotion getPotion() {
        return this.getEntity();
    }

    public Collection getAffectedEntities() {
        return new ArrayList(this.affectedEntities.keySet());
    }

    public double getIntensity(LivingEntity entity) {
        Double intensity = (Double) this.affectedEntities.get(entity);

        return intensity != null ? intensity.doubleValue() : 0.0D;
    }

    public void setIntensity(LivingEntity entity, double intensity) {
        Validate.notNull(entity, "You must specify a valid entity.");
        if (intensity <= 0.0D) {
            this.affectedEntities.remove(entity);
        } else {
            this.affectedEntities.put(entity, Double.valueOf(Math.min(intensity, 1.0D)));
        }

    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PotionSplashEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PotionSplashEvent.handlers;
    }
}
