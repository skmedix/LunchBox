package org.bukkit.material;

import org.bukkit.CropState;
import org.bukkit.Material;

public class Crops extends MaterialData {

    public Crops() {
        super(Material.CROPS);
    }

    public Crops(CropState state) {
        this();
        this.setState(state);
    }

    /** @deprecated */
    @Deprecated
    public Crops(int type) {
        super(type);
    }

    public Crops(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Crops(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Crops(Material type, byte data) {
        super(type, data);
    }

    public CropState getState() {
        return CropState.getByData(this.getData());
    }

    public void setState(CropState state) {
        this.setData(state.getData());
    }

    public String toString() {
        return this.getState() + " " + super.toString();
    }

    public Crops clone() {
        return (Crops) super.clone();
    }
}
