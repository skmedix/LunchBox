package org.bukkit.material;

import org.bukkit.Material;

public class Command extends MaterialData implements Redstone {

    public Command() {
        super(Material.COMMAND);
    }

    /** @deprecated */
    @Deprecated
    public Command(int type) {
        super(type);
    }

    public Command(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Command(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Command(Material type, byte data) {
        super(type, data);
    }

    public boolean isPowered() {
        return (this.getData() & 1) != 0;
    }

    public void setPowered(boolean bool) {
        this.setData((byte) (bool ? this.getData() | 1 : this.getData() & -2));
    }

    public String toString() {
        return super.toString() + " " + (this.isPowered() ? "" : "NOT ") + "POWERED";
    }

    public Command clone() {
        return (Command) super.clone();
    }
}
