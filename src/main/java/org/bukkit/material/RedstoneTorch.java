package org.bukkit.material;

import org.bukkit.Material;

public class RedstoneTorch extends Torch implements Redstone {

    public RedstoneTorch() {
        super(Material.REDSTONE_TORCH_ON);
    }

    /** @deprecated */
    @Deprecated
    public RedstoneTorch(int type) {
        super(type);
    }

    public RedstoneTorch(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public RedstoneTorch(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public RedstoneTorch(Material type, byte data) {
        super(type, data);
    }

    public boolean isPowered() {
        return this.getItemType() == Material.REDSTONE_TORCH_ON;
    }

    public String toString() {
        return super.toString() + " " + (this.isPowered() ? "" : "NOT ") + "POWERED";
    }

    public RedstoneTorch clone() {
        return (RedstoneTorch) super.clone();
    }
}
