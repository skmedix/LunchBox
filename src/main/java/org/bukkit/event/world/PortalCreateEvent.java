package org.bukkit.event.world;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PortalCreateEvent extends WorldEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final ArrayList blocks = new ArrayList();
    private PortalCreateEvent.CreateReason reason;

    public PortalCreateEvent(Collection blocks, World world, PortalCreateEvent.CreateReason reason) {
        super(world);
        this.reason = PortalCreateEvent.CreateReason.FIRE;
        this.blocks.addAll(blocks);
        this.reason = reason;
    }

    public ArrayList getBlocks() {
        return this.blocks;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public PortalCreateEvent.CreateReason getReason() {
        return this.reason;
    }

    public HandlerList getHandlers() {
        return PortalCreateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PortalCreateEvent.handlers;
    }

    public static enum CreateReason {

        FIRE, OBC_DESTINATION;
    }
}
