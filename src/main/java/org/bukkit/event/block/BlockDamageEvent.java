package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BlockDamageEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean instaBreak;
    private boolean cancel;
    private final ItemStack itemstack;

    public BlockDamageEvent(Player player, Block block, ItemStack itemInHand, boolean instaBreak) {
        super(block);
        this.instaBreak = instaBreak;
        this.player = player;
        this.itemstack = itemInHand;
        this.cancel = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean getInstaBreak() {
        return this.instaBreak;
    }

    public void setInstaBreak(boolean bool) {
        this.instaBreak = bool;
    }

    public ItemStack getItemInHand() {
        return this.itemstack;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return BlockDamageEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockDamageEvent.handlers;
    }
}
