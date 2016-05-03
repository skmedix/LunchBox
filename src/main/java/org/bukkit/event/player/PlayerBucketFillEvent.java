package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerBucketFillEvent extends PlayerBucketEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerBucketFillEvent(Player who, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand) {
        super(who, blockClicked, blockFace, bucket, itemInHand);
    }

    public HandlerList getHandlers() {
        return PlayerBucketFillEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerBucketFillEvent.handlers;
    }
}
