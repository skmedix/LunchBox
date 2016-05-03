package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockIgniteEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final BlockIgniteEvent.IgniteCause cause;
    private final Entity ignitingEntity;
    private final Block ignitingBlock;
    private boolean cancel;

    /** @deprecated */
    @Deprecated
    public BlockIgniteEvent(Block theBlock, BlockIgniteEvent.IgniteCause cause, Player thePlayer) {
        this(theBlock, cause, (Entity) thePlayer);
    }

    public BlockIgniteEvent(Block theBlock, BlockIgniteEvent.IgniteCause cause, Entity ignitingEntity) {
        this(theBlock, cause, ignitingEntity, (Block) null);
    }

    public BlockIgniteEvent(Block theBlock, BlockIgniteEvent.IgniteCause cause, Block ignitingBlock) {
        this(theBlock, cause, (Entity) null, ignitingBlock);
    }

    public BlockIgniteEvent(Block theBlock, BlockIgniteEvent.IgniteCause cause, Entity ignitingEntity, Block ignitingBlock) {
        super(theBlock);
        this.cause = cause;
        this.ignitingEntity = ignitingEntity;
        this.ignitingBlock = ignitingBlock;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public BlockIgniteEvent.IgniteCause getCause() {
        return this.cause;
    }

    public Player getPlayer() {
        return this.ignitingEntity instanceof Player ? (Player) this.ignitingEntity : null;
    }

    public Entity getIgnitingEntity() {
        return this.ignitingEntity;
    }

    public Block getIgnitingBlock() {
        return this.ignitingBlock;
    }

    public HandlerList getHandlers() {
        return BlockIgniteEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockIgniteEvent.handlers;
    }

    public static enum IgniteCause {

        LAVA, FLINT_AND_STEEL, SPREAD, LIGHTNING, FIREBALL, ENDER_CRYSTAL, EXPLOSION;
    }
}
