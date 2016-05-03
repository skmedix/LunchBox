package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    protected ItemStack item;
    protected Action action;
    protected Block blockClicked;
    protected BlockFace blockFace;
    private Event.Result useClickedBlock;
    private Event.Result useItemInHand;

    public PlayerInteractEvent(Player who, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace) {
        super(who);
        this.action = action;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;
        this.useItemInHand = Event.Result.DEFAULT;
        this.useClickedBlock = clickedBlock == null ? Event.Result.DENY : Event.Result.ALLOW;
    }

    public Action getAction() {
        return this.action;
    }

    public boolean isCancelled() {
        return this.useInteractedBlock() == Event.Result.DENY;
    }

    public void setCancelled(boolean cancel) {
        this.setUseInteractedBlock(cancel ? Event.Result.DENY : (this.useInteractedBlock() == Event.Result.DENY ? Event.Result.DEFAULT : this.useInteractedBlock()));
        this.setUseItemInHand(cancel ? Event.Result.DENY : (this.useItemInHand() == Event.Result.DENY ? Event.Result.DEFAULT : this.useItemInHand()));
    }

    public ItemStack getItem() {
        return this.item;
    }

    public Material getMaterial() {
        return !this.hasItem() ? Material.AIR : this.item.getType();
    }

    public boolean hasBlock() {
        return this.blockClicked != null;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    public boolean isBlockInHand() {
        return !this.hasItem() ? false : this.item.getType().isBlock();
    }

    public Block getClickedBlock() {
        return this.blockClicked;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    public Event.Result useInteractedBlock() {
        return this.useClickedBlock;
    }

    public void setUseInteractedBlock(Event.Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    public Event.Result useItemInHand() {
        return this.useItemInHand;
    }

    public void setUseItemInHand(Event.Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    public HandlerList getHandlers() {
        return PlayerInteractEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerInteractEvent.handlers;
    }
}
