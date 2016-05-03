package org.bukkit.event.entity;

import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.HandlerList;

public class ExpBottleEvent extends ProjectileHitEvent {

    private static final HandlerList handlers = new HandlerList();
    private int exp;
    private boolean showEffect = true;

    public ExpBottleEvent(ThrownExpBottle bottle, int exp) {
        super((Projectile) bottle);
        this.exp = exp;
    }

    public ThrownExpBottle getEntity() {
        return (ThrownExpBottle) this.entity;
    }

    public boolean getShowEffect() {
        return this.showEffect;
    }

    public void setShowEffect(boolean showEffect) {
        this.showEffect = showEffect;
    }

    public int getExperience() {
        return this.exp;
    }

    public void setExperience(int exp) {
        this.exp = exp;
    }

    public HandlerList getHandlers() {
        return ExpBottleEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ExpBottleEvent.handlers;
    }
}
