package org.bukkit.material;

import org.bukkit.CoalType;
import org.bukkit.Material;

public class Coal extends MaterialData {

    public Coal() {
        super(Material.COAL);
    }

    public Coal(CoalType type) {
        this();
        this.setType(type);
    }

    /** @deprecated */
    @Deprecated
    public Coal(int type) {
        super(type);
    }

    public Coal(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Coal(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Coal(Material type, byte data) {
        super(type, data);
    }

    public CoalType getType() {
        return CoalType.getByData(this.getData());
    }

    public void setType(CoalType type) {
        this.setData(type.getData());
    }

    public String toString() {
        return this.getType() + " " + super.toString();
    }

    public Coal clone() {
        return (Coal) super.clone();
    }
}
