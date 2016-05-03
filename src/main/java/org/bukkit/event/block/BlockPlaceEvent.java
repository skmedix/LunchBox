package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    protected boolean cancel;
    protected boolean canBuild;
    protected Block placedAgainst;
    protected BlockState replacedBlockState;
    protected ItemStack itemInHand;
    protected Player player;

    public BlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst, ItemStack itemInHand, Player thePlayer, boolean canBuild) {
        super(placedBlock);
        this.placedAgainst = placedAgainst;
        this.itemInHand = itemInHand;
        this.player = thePlayer;
        this.replacedBlockState = replacedBlockState;
        this.canBuild = canBuild;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getBlockPlaced() {
        return this.getBlock();
    }

    public BlockState getBlockReplacedState() {
        return this.replacedBlockState;
    }

    public Block getBlockAgainst() {
        return this.placedAgainst;
    }

    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    public boolean canBuild() {
        return this.canBuild;
    }

    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    public HandlerList getHandlers() {
        return BlockPlaceEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockPlaceEvent.handlers;
    }
}
