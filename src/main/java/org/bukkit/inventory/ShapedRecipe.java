package org.bukkit.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class ShapedRecipe implements Recipe {

    private ItemStack output;
    private String[] rows;
    private Map ingredients = new HashMap();

    public ShapedRecipe(ItemStack result) {
        this.output = new ItemStack(result);
    }

    public ShapedRecipe shape(String... shape) {
        Validate.notNull(shape, "Must provide a shape");
        Validate.isTrue(shape.length > 0 && shape.length < 4, "Crafting recipes should be 1, 2, 3 rows, not ", (long) shape.length);
        String[] astring = shape;
        int i = shape.length;

        for (int row = 0; row < i; ++row) {
            String newIngredients = astring[row];

            Validate.notNull(newIngredients, "Shape cannot have null rows");
            Validate.isTrue(newIngredients.length() > 0 && newIngredients.length() < 4, "Crafting rows should be 1, 2, or 3 characters, not ", (long) newIngredients.length());
        }

        this.rows = new String[shape.length];

        for (int j = 0; j < shape.length; ++j) {
            this.rows[j] = shape[j];
        }

        HashMap hashmap = new HashMap();
        String[] astring1 = shape;
        int k = shape.length;

        for (i = 0; i < k; ++i) {
            String s = astring1[i];
            char[] achar;
            int l = (achar = s.toCharArray()).length;

            for (int i1 = 0; i1 < l; ++i1) {
                Character c = Character.valueOf(achar[i1]);

                hashmap.put(c, (ItemStack) this.ingredients.get(c));
            }
        }

        this.ingredients = hashmap;
        return this;
    }

    public ShapedRecipe setIngredient(char key, MaterialData ingredient) {
        return this.setIngredient(key, ingredient.getItemType(), ingredient.getData());
    }

    public ShapedRecipe setIngredient(char key, Material ingredient) {
        return this.setIngredient(key, ingredient, 0);
    }

    /** @deprecated */
    @Deprecated
    public ShapedRecipe setIngredient(char key, Material ingredient, int raw) {
        Validate.isTrue(this.ingredients.containsKey(Character.valueOf(key)), "Symbol does not appear in the shape:", (long) key);
        if (raw == -1) {
            raw = 32767;
        }

        this.ingredients.put(Character.valueOf(key), new ItemStack(ingredient, 1, (short) raw));
        return this;
    }

    public Map getIngredientMap() {
        HashMap result = new HashMap();
        Iterator iterator = this.ingredients.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry ingredient = (Entry) iterator.next();

            if (ingredient.getValue() == null) {
                result.put((Character) ingredient.getKey(), (Object) null);
            } else {
                result.put((Character) ingredient.getKey(), ((ItemStack) ingredient.getValue()).clone());
            }
        }

        return result;
    }

    public String[] getShape() {
        return (String[]) this.rows.clone();
    }

    public ItemStack getResult() {
        return this.output.clone();
    }
}
