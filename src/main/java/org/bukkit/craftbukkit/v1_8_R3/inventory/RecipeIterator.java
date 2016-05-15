package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.Iterator;

import com.kookykraftmc.lunchbox.BukkitRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator {

    private final Iterator recipes = CraftingManager.getInstance().getRecipeList().iterator();
    private final Iterator smeltingCustom;
    private final Iterator smeltingVanilla;
    private Iterator removeFrom = null;

    public RecipeIterator() {
        this.smeltingCustom = FurnaceRecipes.instance().getSmeltingList().keySet().iterator();
        this.smeltingVanilla = FurnaceRecipes.instance().getSmeltingList().keySet().iterator();
    }

    public boolean hasNext() {
        return this.recipes.hasNext() || this.smeltingCustom.hasNext() || this.smeltingVanilla.hasNext();
    }

    public Recipe next() {
        if (this.recipes.hasNext()) {
            this.removeFrom = this.recipes;
            return new BukkitRecipe((IRecipe) this.recipes.next());
        } else {
            ItemStack item;

            if (this.smeltingCustom.hasNext()) {
                this.removeFrom = this.smeltingCustom;
                item = (ItemStack) this.smeltingCustom.next();
            } else {
                this.removeFrom = this.smeltingVanilla;
                item = (ItemStack) this.smeltingVanilla.next();
            }

            CraftItemStack stack = CraftItemStack.asCraftMirror(FurnaceRecipes.instance().getSmeltingResult(item));

            return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
        }
    }


    public void remove() {
        if (this.removeFrom == null) {
            throw new IllegalStateException();
        } else {
            this.removeFrom.remove();
        }
    }
}
