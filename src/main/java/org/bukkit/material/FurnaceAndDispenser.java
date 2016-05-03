package org.bukkit.material;

import org.bukkit.Material;

public class FurnaceAndDispenser extends DirectionalContainer {

    /** @deprecated */
    @Deprecated
    public FurnaceAndDispenser(int type) {
        super(type);
    }

    public FurnaceAndDispenser(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public FurnaceAndDispenser(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public FurnaceAndDispenser(Material type, byte data) {
        super(type, data);
    }

    public FurnaceAndDispenser clone() {
        return (FurnaceAndDispenser) super.clone();
    }
}
