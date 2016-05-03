package org.bukkit.event.entity;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class FoodLevelChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private int level;

    public FoodLevelChangeEvent(HumanEntity what, int level) {
        super(what);
        this.level = level;
    }

    public HumanEntity getEntity() {
        return (HumanEntity) this.entity;
    }

    public int getFoodLevel() {
        return this.level;
    }

    public void setFoodLevel(int level) {
        if (level > 20) {
            level = 20;
        } else if (level < 0) {
            level = 0;
        }

        this.level = level;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return FoodLevelChangeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return FoodLevelChangeEvent.handlers;
    }
}
