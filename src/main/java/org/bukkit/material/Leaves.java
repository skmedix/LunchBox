package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

public class Leaves extends MaterialData {

    public Leaves() {
        super(Material.LEAVES);
    }

    public Leaves(TreeSpecies species) {
        this();
        this.setSpecies(species);
    }

    /** @deprecated */
    @Deprecated
    public Leaves(int type) {
        super(type);
    }

    public Leaves(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Leaves(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Leaves(Material type, byte data) {
        super(type, data);
    }

    public TreeSpecies getSpecies() {
        return TreeSpecies.getByData((byte) (this.getData() & 3));
    }

    public void setSpecies(TreeSpecies species) {
        this.setData(species.getData());
    }

    public String toString() {
        return this.getSpecies() + " " + super.toString();
    }

    public Leaves clone() {
        return (Leaves) super.clone();
    }
}
