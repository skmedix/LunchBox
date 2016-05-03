package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public class Dye extends MaterialData implements Colorable {

    public Dye() {
        super(Material.INK_SACK);
    }

    /** @deprecated */
    @Deprecated
    public Dye(int type) {
        super(type);
    }

    public Dye(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Dye(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Dye(Material type, byte data) {
        super(type, data);
    }

    public Dye(DyeColor color) {
        super(Material.INK_SACK, color.getDyeData());
    }

    public DyeColor getColor() {
        return DyeColor.getByDyeData(this.getData());
    }

    public void setColor(DyeColor color) {
        this.setData(color.getDyeData());
    }

    public String toString() {
        return this.getColor() + " DYE(" + this.getData() + ")";
    }

    public Dye clone() {
        return (Dye) super.clone();
    }
}
