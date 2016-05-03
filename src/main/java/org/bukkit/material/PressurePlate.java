package org.bukkit.material;

import org.bukkit.Material;

public class PressurePlate extends MaterialData implements PressureSensor {

    public PressurePlate() {
        super(Material.WOOD_PLATE);
    }

    /** @deprecated */
    @Deprecated
    public PressurePlate(int type) {
        super(type);
    }

    public PressurePlate(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public PressurePlate(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public PressurePlate(Material type, byte data) {
        super(type, data);
    }

    public boolean isPressed() {
        return this.getData() == 1;
    }

    public String toString() {
        return super.toString() + (this.isPressed() ? " PRESSED" : "");
    }

    public PressurePlate clone() {
        return (PressurePlate) super.clone();
    }
}
