package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class Step extends TexturedMaterial {

    private static final List textures = new ArrayList();

    static {
        Step.textures.add(Material.STONE);
        Step.textures.add(Material.SANDSTONE);
        Step.textures.add(Material.WOOD);
        Step.textures.add(Material.COBBLESTONE);
        Step.textures.add(Material.BRICK);
        Step.textures.add(Material.SMOOTH_BRICK);
        Step.textures.add(Material.NETHER_BRICK);
        Step.textures.add(Material.QUARTZ_BLOCK);
    }

    public Step() {
        super(Material.STEP);
    }

    /** @deprecated */
    @Deprecated
    public Step(int type) {
        super(type);
    }

    public Step(Material type) {
        super(Step.textures.contains(type) ? Material.STEP : type);
        if (Step.textures.contains(type)) {
            this.setMaterial(type);
        }

    }

    /** @deprecated */
    @Deprecated
    public Step(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Step(Material type, byte data) {
        super(type, data);
    }

    public List getTextures() {
        return Step.textures;
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

    /** @deprecated */
    @Deprecated
    protected int getTextureIndex() {
        return this.getData() & 7;
    }

    /** @deprecated */
    @Deprecated
    protected void setTextureIndex(int idx) {
        this.setData((byte) (this.getData() & 8 | idx));
    }

    public Step clone() {
        return (Step) super.clone();
    }

    public String toString() {
        return super.toString() + (this.isInverted() ? "inverted" : "");
    }
}
