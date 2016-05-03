package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.NetherWartsState;

public class NetherWarts extends MaterialData {

    private static int[] $SWITCH_TABLE$org$bukkit$NetherWartsState;

    public NetherWarts() {
        super(Material.NETHER_WARTS);
    }

    public NetherWarts(NetherWartsState state) {
        this();
        this.setState(state);
    }

    /** @deprecated */
    @Deprecated
    public NetherWarts(int type) {
        super(type);
    }

    public NetherWarts(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public NetherWarts(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public NetherWarts(Material type, byte data) {
        super(type, data);
    }

    public NetherWartsState getState() {
        switch (this.getData()) {
        case 0:
            return NetherWartsState.SEEDED;

        case 1:
            return NetherWartsState.STAGE_ONE;

        case 2:
            return NetherWartsState.STAGE_TWO;

        default:
            return NetherWartsState.RIPE;
        }
    }

    public void setState(NetherWartsState state) {
        switch ($SWITCH_TABLE$org$bukkit$NetherWartsState()[state.ordinal()]) {
        case 1:
            this.setData((byte) 0);
            return;

        case 2:
            this.setData((byte) 1);
            return;

        case 3:
            this.setData((byte) 2);
            return;

        case 4:
            this.setData((byte) 3);
            return;

        default:
        }
    }

    public String toString() {
        return this.getState() + " " + super.toString();
    }

    public NetherWarts clone() {
        return (NetherWarts) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$NetherWartsState() {
        int[] aint = NetherWarts.$SWITCH_TABLE$org$bukkit$NetherWartsState;

        if (NetherWarts.$SWITCH_TABLE$org$bukkit$NetherWartsState != null) {
            return aint;
        } else {
            int[] aint1 = new int[NetherWartsState.values().length];

            try {
                aint1[NetherWartsState.RIPE.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[NetherWartsState.SEEDED.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[NetherWartsState.STAGE_ONE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[NetherWartsState.STAGE_TWO.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            NetherWarts.$SWITCH_TABLE$org$bukkit$NetherWartsState = aint1;
            return aint1;
        }
    }
}
