package org.bukkit.entity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;

public interface HumanEntity extends LivingEntity, AnimalTamer, Permissible, InventoryHolder {

    String getName();

    PlayerInventory getInventory();

    Inventory getEnderChest();

    boolean setWindowProperty(InventoryView.Property inventoryview_property, int i);

    InventoryView getOpenInventory();

    InventoryView openInventory(Inventory inventory);

    InventoryView openWorkbench(Location location, boolean flag);

    InventoryView openEnchanting(Location location, boolean flag);

    void openInventory(InventoryView inventoryview);

    void closeInventory();

    ItemStack getItemInHand();

    void setItemInHand(ItemStack itemstack);

    ItemStack getItemOnCursor();

    void setItemOnCursor(ItemStack itemstack);

    boolean isSleeping();

    int getSleepTicks();

    GameMode getGameMode();

    void setGameMode(GameMode gamemode);

    boolean isBlocking();

    int getExpToLevel();
}
