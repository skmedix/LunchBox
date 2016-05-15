package com.kookykraftmc.lunchbox;

import net.minecraft.item.crafting.IRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class BukkitRecipe implements Recipe {
    private IRecipe iRecipe;

    public BukkitRecipe(IRecipe iRecipe) {
        this.iRecipe = iRecipe;
    }

    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(iRecipe.getRecipeOutput());
    }

    public IRecipe getHandle() {
        return iRecipe;
    }
}
