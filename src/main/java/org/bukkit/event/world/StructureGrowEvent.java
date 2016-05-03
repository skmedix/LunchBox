package org.bukkit.event.world;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class StructureGrowEvent extends WorldEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private final Location location;
    private final TreeType species;
    private final boolean bonemeal;
    private final Player player;
    private final List blocks;

    public StructureGrowEvent(Location location, TreeType species, boolean bonemeal, Player player, List blocks) {
        super(location.getWorld());
        this.location = location;
        this.species = species;
        this.bonemeal = bonemeal;
        this.player = player;
        this.blocks = blocks;
    }

    public Location getLocation() {
        return this.location;
    }

    public TreeType getSpecies() {
        return this.species;
    }

    public boolean isFromBonemeal() {
        return this.bonemeal;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List getBlocks() {
        return this.blocks;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return StructureGrowEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return StructureGrowEvent.handlers;
    }
}
