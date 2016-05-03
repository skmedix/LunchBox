package org.bukkit.event.player;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerArmorStandManipulateEvent extends PlayerInteractEntityEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack playerItem;
    private final ItemStack armorStandItem;
    private final EquipmentSlot slot;

    public PlayerArmorStandManipulateEvent(Player who, ArmorStand clickedEntity, ItemStack playerItem, ItemStack armorStandItem, EquipmentSlot slot) {
        super(who, clickedEntity);
        this.playerItem = playerItem;
        this.armorStandItem = armorStandItem;
        this.slot = slot;
    }

    public ItemStack getPlayerItem() {
        return this.playerItem;
    }

    public ItemStack getArmorStandItem() {
        return this.armorStandItem;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public ArmorStand getRightClicked() {
        return (ArmorStand) this.clickedEntity;
    }

    public HandlerList getHandlers() {
        return PlayerArmorStandManipulateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerArmorStandManipulateEvent.handlers;
    }
}
