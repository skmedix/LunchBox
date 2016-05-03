package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockFromToEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    protected Block to;
    protected BlockFace face;
    protected boolean cancel;

    public BlockFromToEvent(Block block, BlockFace face) {
        super(block);
        this.face = face;
        this.cancel = false;
    }

    public BlockFromToEvent(Block block, Block toBlock) {
        super(block);
        this.to = toBlock;
        this.face = BlockFace.SELF;
        this.cancel = false;
    }

    public BlockFace getFace() {
        return this.face;
    }

    public Block getToBlock() {
        if (this.to == null) {
            this.to = this.block.getRelative(this.face);
        }

        return this.to;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return BlockFromToEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockFromToEvent.handlers;
    }
}
