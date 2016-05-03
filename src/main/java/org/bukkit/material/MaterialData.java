package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialData implements Cloneable {

    private final int type;
    private byte data;

    /** @deprecated */
    @Deprecated
    public MaterialData(int type) {
        this(type, (byte) 0);
    }

    public MaterialData(Material type) {
        this(type, (byte) 0);
    }

    /** @deprecated */
    @Deprecated
    public MaterialData(int type, byte data) {
        this.data = 0;
        this.type = type;
        this.data = data;
    }

    /** @deprecated */
    @Deprecated
    public MaterialData(Material type, byte data) {
        this(type.getId(), data);
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.data;
    }

    /** @deprecated */
    @Deprecated
    public void setData(byte data) {
        this.data = data;
    }

    public Material getItemType() {
        return Material.getMaterial(this.type);
    }

    /** @deprecated */
    @Deprecated
    public int getItemTypeId() {
        return this.type;
    }

    public ItemStack toItemStack() {
        return new ItemStack(this.type, 0, this.data);
    }

    public ItemStack toItemStack(int amount) {
        return new ItemStack(this.type, amount, this.data);
    }

    public String toString() {
        return this.getItemType() + "(" + this.getData() + ")";
    }

    public int hashCode() {
        return this.getItemTypeId() << 8 ^ this.getData();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MaterialData) {
            MaterialData md = (MaterialData) obj;

            return md.getItemTypeId() == this.getItemTypeId() && md.getData() == this.getData();
        } else {
            return false;
        }
    }

    public MaterialData clone() {
        try {
            return (MaterialData) super.clone();
        } catch (CloneNotSupportedException clonenotsupportedexception) {
            throw new Error(clonenotsupportedexception);
        }
    }
}
