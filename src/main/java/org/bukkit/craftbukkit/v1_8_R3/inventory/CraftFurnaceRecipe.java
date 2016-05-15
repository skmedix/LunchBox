package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.item.crafting.FurnaceRecipes;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftFurnaceRecipe extends FurnaceRecipe implements CraftRecipe {

    public CraftFurnaceRecipe(ItemStack result, ItemStack source) {
        super(result, source.getType(), source.getDurability());
    }

    public static CraftFurnaceRecipe fromBukkitRecipe(FurnaceRecipe recipe) {
        return recipe instanceof CraftFurnaceRecipe ? (CraftFurnaceRecipe) recipe : new CraftFurnaceRecipe(recipe.getResult(), recipe.getInput());
    }

    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        ItemStack input = this.getInput();

        FurnaceRecipes.instance().addSmeltingRecipe(CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result), 0);
    }
}
