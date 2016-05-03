package org.bukkit.block;

import org.bukkit.inventory.InventoryHolder;

public interface Dropper extends BlockState, InventoryHolder {

    void drop();
}
