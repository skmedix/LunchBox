package org.bukkit.event.block;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

public class BlockPistonRetractEvent extends BlockPistonEvent {

    private static final HandlerList handlers = new HandlerList();
    private List blocks;

    public BlockPistonRetractEvent(Block block, List blocks, BlockFace direction) {
        super(block, direction);
        this.blocks = blocks;
    }

    /** @deprecated */
    @Deprecated
    public Location getRetractLocation() {
        return this.getBlock().getRelative(this.getDirection(), 2).getLocation();
    }

    public List getBlocks() {
        return this.blocks;
    }

    public HandlerList getHandlers() {
        return BlockPistonRetractEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockPistonRetractEvent.handlers;
    }
}
