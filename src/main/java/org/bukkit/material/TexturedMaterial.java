package org.bukkit.material;

import java.util.List;
import org.bukkit.Material;

public abstract class TexturedMaterial extends MaterialData {

    public TexturedMaterial(Material m) {
        super(m);
    }

    /** @deprecated */
    @Deprecated
    public TexturedMaterial(int type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public TexturedMaterial(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public TexturedMaterial(Material type, byte data) {
        super(type, data);
    }

    public abstract List getTextures();

    public Material getMaterial() {
        int n = this.getTextureIndex();

        if (n > this.getTextures().size() - 1) {
            n = 0;
        }

        return (Material) this.getTextures().get(n);
    }

    public void setMaterial(Material material) {
        if (this.getTextures().contains(material)) {
            this.setTextureIndex(this.getTextures().indexOf(material));
        } else {
            this.setTextureIndex(0);
        }

    }

    /** @deprecated */
    @Deprecated
    protected int getTextureIndex() {
        return this.getData();
    }

    /** @deprecated */
    @Deprecated
    protected void setTextureIndex(int idx) {
        this.setData((byte) idx);
    }

    public String toString() {
        return this.getMaterial() + " " + super.toString();
    }

    public TexturedMaterial clone() {
        return (TexturedMaterial) super.clone();
    }
}
