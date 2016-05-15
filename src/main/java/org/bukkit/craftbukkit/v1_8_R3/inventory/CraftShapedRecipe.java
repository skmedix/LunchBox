package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftShapedRecipe extends ShapedRecipe implements CraftRecipe {

    private ShapedRecipes recipe;

    public CraftShapedRecipe(ItemStack result) {
        super(result);
    }

    public CraftShapedRecipe(ItemStack result, ShapedRecipes recipe) {
        this(result);
        this.recipe = recipe;
    }

    public static CraftShapedRecipe fromBukkitRecipe(ShapedRecipe recipe) {
        if (recipe instanceof CraftShapedRecipe) {
            return (CraftShapedRecipe) recipe;
        } else {
            CraftShapedRecipe ret = new CraftShapedRecipe(recipe.getResult());
            String[] shape = recipe.getShape();

            ret.shape(shape);
            Map ingredientMap = recipe.getIngredientMap();
            Iterator iterator = ingredientMap.keySet().iterator();

            while (iterator.hasNext()) {
                char c = ((Character) iterator.next()).charValue();
                ItemStack stack = (ItemStack) ingredientMap.get(Character.valueOf(c));

                if (stack != null) {
                    ret.setIngredient(c, stack.getType(), stack.getDurability());
                }
            }

            return ret;
        }
    }

    public void addToCraftingManager() {
        String[] shape = this.getShape();
        Map ingred = this.getIngredientMap();
        int datalen = shape.length;

        datalen += ingred.size() * 2;
        int i = 0;

        Object[] data;

        for (data = new Object[datalen]; i < shape.length; ++i) {
            data[i] = shape[i];
        }

        Iterator iterator = ingred.keySet().iterator();

        while (iterator.hasNext()) {
            char c = ((Character) iterator.next()).charValue();
            ItemStack mdata = (ItemStack) ingred.get(Character.valueOf(c));

            if (mdata != null) {
                data[i] = Character.valueOf(c);
                ++i;
                int id = mdata.getTypeId();
                short dmg = mdata.getDurability();

                data[i] = new net.minecraft.item.ItemStack(CraftMagicNumbers.getItem(id), 1, dmg);
                ++i;
            }
        }

        CraftingManager.getInstance().addShapelessRecipe(CraftItemStack.asNMSCopy(this.getResult()), data);
    }
}
