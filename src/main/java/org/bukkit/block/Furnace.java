package org.bukkit.block;

import org.bukkit.inventory.FurnaceInventory;

public interface Furnace extends BlockState, ContainerBlock {

    short getBurnTime();

    void setBurnTime(short short0);

    short getCookTime();

    void setCookTime(short short0);

    FurnaceInventory getInventory();
}
