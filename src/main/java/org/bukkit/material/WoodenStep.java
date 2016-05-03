package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

public class WoodenStep extends MaterialData {

    public WoodenStep() {
        super(Material.WOOD_STEP);
    }

    /** @deprecated */
    @Deprecated
    public WoodenStep(int type) {
        super(type);
    }

    public WoodenStep(TreeSpecies species) {
        this();
        this.setSpecies(species);
    }

    public WoodenStep(TreeSpecies species, boolean inv) {
        this();
        this.setSpecies(species);
        this.setInverted(inv);
    }

    /** @deprecated */
    @Deprecated
    public WoodenStep(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public WoodenStep(Material type, byte data) {
        super(type, data);
    }

    public TreeSpecies getSpecies() {
        return TreeSpecies.getByData((byte) (this.getData() & 3));
    }

    public void setSpecies(TreeSpecies species) {
        this.setData((byte) (this.getData() & 12 | species.getData()));
    }

    public boolean isInverted() {
        return (this.getData() & 8) != 0;
    }

    public void setInverted(boolean inv) {
        int dat = this.getData() & 7;

        if (inv) {
            dat |= 8;
        }

        this.setData((byte) dat);
    }

    public WoodenStep clone() {
        return (WoodenStep) super.clone();
    }

    public String toString() {
        return super.toString() + " " + this.getSpecies() + (this.isInverted() ? " inverted" : "");
    }
}
