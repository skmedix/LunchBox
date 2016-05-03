package org.bukkit.material;

import org.bukkit.Material;

public class PoweredRail extends ExtendedRails implements Redstone {

    public PoweredRail() {
        super(Material.POWERED_RAIL);
    }

    /** @deprecated */
    @Deprecated
    public PoweredRail(int type) {
        super(type);
    }

    public PoweredRail(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public PoweredRail(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public PoweredRail(Material type, byte data) {
        super(type, data);
    }

    public boolean isPowered() {
        return (this.getData() & 8) == 8;
    }

    public void setPowered(boolean isPowered) {
        this.setData((byte) (isPowered ? this.getData() | 8 : this.getData() & -9));
    }

    public PoweredRail clone() {
        return (PoweredRail) super.clone();
    }
}
