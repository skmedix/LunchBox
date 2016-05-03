package org.bukkit.material;

import org.bukkit.Material;

public class Cake extends MaterialData {

    public Cake() {
        super(Material.CAKE_BLOCK);
    }

    /** @deprecated */
    @Deprecated
    public Cake(int type) {
        super(type);
    }

    public Cake(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Cake(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Cake(Material type, byte data) {
        super(type, data);
    }

    public int getSlicesEaten() {
        return this.getData();
    }

    public int getSlicesRemaining() {
        return 6 - this.getData();
    }

    public void setSlicesEaten(int n) {
        if (n < 6) {
            this.setData((byte) n);
        }

    }

    public void setSlicesRemaining(int n) {
        if (n > 6) {
            n = 6;
        }

        this.setData((byte) (6 - n));
    }

    public String toString() {
        return super.toString() + " " + this.getSlicesEaten() + "/" + this.getSlicesRemaining() + " slices eaten/remaining";
    }

    public Cake clone() {
        return (Cake) super.clone();
    }
}
