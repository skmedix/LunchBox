package org.bukkit.event.entity;

import org.bukkit.entity.Slime;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SlimeSplitEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private int count;

    public SlimeSplitEvent(Slime slime, int count) {
        super(slime);
        this.count = count;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Slime getEntity() {
        return (Slime) this.entity;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HandlerList getHandlers() {
        return SlimeSplitEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return SlimeSplitEvent.handlers;
    }
}
