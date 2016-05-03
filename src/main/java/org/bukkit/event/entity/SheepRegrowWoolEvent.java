package org.bukkit.event.entity;

import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SheepRegrowWoolEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    public SheepRegrowWoolEvent(Sheep sheep) {
        super(sheep);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Sheep getEntity() {
        return (Sheep) this.entity;
    }

    public HandlerList getHandlers() {
        return SheepRegrowWoolEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return SheepRegrowWoolEvent.handlers;
    }
}
