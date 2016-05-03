package org.bukkit.inventory;

public interface CraftingInventory extends Inventory {

    ItemStack getResult();

    ItemStack[] getMatrix();

    void setResult(ItemStack itemstack);

    void setMatrix(ItemStack[] aitemstack);

    Recipe getRecipe();
}
