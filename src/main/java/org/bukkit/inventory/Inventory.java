package org.bukkit.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public interface Inventory extends Iterable {

    int getSize();

    int getMaxStackSize();

    void setMaxStackSize(int i);

    String getName();

    ItemStack getItem(int i);

    void setItem(int i, ItemStack itemstack);

    HashMap addItem(ItemStack... aitemstack) throws IllegalArgumentException;

    HashMap removeItem(ItemStack... aitemstack) throws IllegalArgumentException;

    ItemStack[] getContents();

    void setContents(ItemStack[] aitemstack) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    boolean contains(int i);

    boolean contains(Material material) throws IllegalArgumentException;

    boolean contains(ItemStack itemstack);

    /** @deprecated */
    @Deprecated
    boolean contains(int i, int j);

    boolean contains(Material material, int i) throws IllegalArgumentException;

    boolean contains(ItemStack itemstack, int i);

    boolean containsAtLeast(ItemStack itemstack, int i);

    /** @deprecated */
    @Deprecated
    HashMap all(int i);

    HashMap all(Material material) throws IllegalArgumentException;

    HashMap all(ItemStack itemstack);

    /** @deprecated */
    @Deprecated
    int first(int i);

    int first(Material material) throws IllegalArgumentException;

    int first(ItemStack itemstack);

    int firstEmpty();

    /** @deprecated */
    @Deprecated
    void remove(int i);

    void remove(Material material) throws IllegalArgumentException;

    void remove(ItemStack itemstack);

    void clear(int i);

    void clear();

    List getViewers();

    String getTitle();

    InventoryType getType();

    InventoryHolder getHolder();

    ListIterator iterator();

    ListIterator iterator(int i);
}
