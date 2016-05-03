package org.bukkit.material;

import org.bukkit.Material;

public class DetectorRail extends ExtendedRails implements PressureSensor {

    public DetectorRail() {
        super(Material.DETECTOR_RAIL);
    }

    /** @deprecated */
    @Deprecated
    public DetectorRail(int type) {
        super(type);
    }

    public DetectorRail(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public DetectorRail(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public DetectorRail(Material type, byte data) {
        super(type, data);
    }

    public boolean isPressed() {
        return (this.getData() & 8) == 8;
    }

    public void setPressed(boolean isPressed) {
        this.setData((byte) (isPressed ? this.getData() | 8 : this.getData() & -9));
    }

    public DetectorRail clone() {
        return (DetectorRail) super.clone();
    }
}
