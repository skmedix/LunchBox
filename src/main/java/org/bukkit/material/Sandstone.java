package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.SandstoneType;

public class Sandstone extends MaterialData {

    public Sandstone() {
        super(Material.SANDSTONE);
    }

    public Sandstone(SandstoneType type) {
        this();
        this.setType(type);
    }

    /** @deprecated */
    @Deprecated
    public Sandstone(int type) {
        super(type);
    }

    public Sandstone(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Sandstone(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Sandstone(Material type, byte data) {
        super(type, data);
    }

    public SandstoneType getType() {
        return SandstoneType.getByData(this.getData());
    }

    public void setType(SandstoneType type) {
        this.setData(type.getData());
    }

    public String toString() {
        return this.getType() + " " + super.toString();
    }

    public Sandstone clone() {
        return (Sandstone) super.clone();
    }
}
