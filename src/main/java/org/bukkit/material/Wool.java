package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public class Wool extends MaterialData implements Colorable {

    public Wool() {
        super(Material.WOOL);
    }

    public Wool(DyeColor color) {
        this();
        this.setColor(color);
    }

    /** @deprecated */
    @Deprecated
    public Wool(int type) {
        super(type);
    }

    public Wool(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Wool(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Wool(Material type, byte data) {
        super(type, data);
    }

    public DyeColor getColor() {
        return DyeColor.getByWoolData(this.getData());
    }

    public void setColor(DyeColor color) {
        this.setData(color.getWoolData());
    }

    public String toString() {
        return this.getColor() + " " + super.toString();
    }

    public Wool clone() {
        return (Wool) super.clone();
    }
}
