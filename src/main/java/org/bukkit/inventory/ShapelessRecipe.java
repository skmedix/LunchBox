package org.bukkit.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class ShapelessRecipe implements Recipe {

    private ItemStack output;
    private List ingredients = new ArrayList();

    public ShapelessRecipe(ItemStack result) {
        this.output = new ItemStack(result);
    }

    public ShapelessRecipe addIngredient(MaterialData ingredient) {
        return this.addIngredient(1, ingredient);
    }

    public ShapelessRecipe addIngredient(Material ingredient) {
        return this.addIngredient(1, ingredient, 0);
    }

    /** @deprecated */
    @Deprecated
    public ShapelessRecipe addIngredient(Material ingredient, int rawdata) {
        return this.addIngredient(1, ingredient, rawdata);
    }

    public ShapelessRecipe addIngredient(int count, MaterialData ingredient) {
        return this.addIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    public ShapelessRecipe addIngredient(int count, Material ingredient) {
        return this.addIngredient(count, ingredient, 0);
    }

    /** @deprecated */
    @Deprecated
    public ShapelessRecipe addIngredient(int count, Material ingredient, int rawdata) {
        Validate.isTrue(this.ingredients.size() + count <= 9, "Shapeless recipes cannot have more than 9 ingredients");
        if (rawdata == -1) {
            rawdata = 32767;
        }

        while (count-- > 0) {
            this.ingredients.add(new ItemStack(ingredient, 1, (short) rawdata));
        }

        return this;
    }

    public ShapelessRecipe removeIngredient(Material ingredient) {
        return this.removeIngredient(ingredient, 0);
    }

    public ShapelessRecipe removeIngredient(MaterialData ingredient) {
        return this.removeIngredient(ingredient.getItemType(), ingredient.getData());
    }

    public ShapelessRecipe removeIngredient(int count, Material ingredient) {
        return this.removeIngredient(count, ingredient, 0);
    }

    public ShapelessRecipe removeIngredient(int count, MaterialData ingredient) {
        return this.removeIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    /** @deprecated */
    @Deprecated
    public ShapelessRecipe removeIngredient(Material ingredient, int rawdata) {
        return this.removeIngredient(1, ingredient, rawdata);
    }

    /** @deprecated */
    @Deprecated
    public ShapelessRecipe removeIngredient(int count, Material ingredient, int rawdata) {
        Iterator iterator = this.ingredients.iterator();

        while (count > 0 && iterator.hasNext()) {
            ItemStack stack = (ItemStack) iterator.next();

            if (stack.getType() == ingredient && stack.getDurability() == rawdata) {
                iterator.remove();
                --count;
            }
        }

        return this;
    }

    public ItemStack getResult() {
        return this.output.clone();
    }

    public List getIngredientList() {
        ArrayList result = new ArrayList(this.ingredients.size());
        Iterator iterator = this.ingredients.iterator();

        while (iterator.hasNext()) {
            ItemStack ingredient = (ItemStack) iterator.next();

            result.add(ingredient.clone());
        }

        return result;
    }
}
