package org.bukkit.material;

import org.bukkit.Material;

public class Tripwire extends MaterialData {

    public Tripwire() {
        super(Material.TRIPWIRE);
    }

    /** @deprecated */
    @Deprecated
    public Tripwire(int type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Tripwire(int type, byte data) {
        super(type, data);
    }

    public boolean isActivated() {
        return (this.getData() & 4) != 0;
    }

    public void setActivated(boolean act) {
        int dat = this.getData() & 11;

        if (act) {
            dat |= 4;
        }

        this.setData((byte) dat);
    }

    public boolean isObjectTriggering() {
        return (this.getData() & 1) != 0;
    }

    public void setObjectTriggering(boolean trig) {
        int dat = this.getData() & 14;

        if (trig) {
            dat |= 1;
        }

        this.setData((byte) dat);
    }

    public Tripwire clone() {
        return (Tripwire) super.clone();
    }

    public String toString() {
        return super.toString() + (this.isActivated() ? " Activated" : "") + (this.isObjectTriggering() ? " Triggered" : "");
    }
}
