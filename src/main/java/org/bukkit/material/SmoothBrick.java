package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class SmoothBrick extends TexturedMaterial {

    private static final List textures = new ArrayList();

    static {
        SmoothBrick.textures.add(Material.STONE);
        SmoothBrick.textures.add(Material.MOSSY_COBBLESTONE);
        SmoothBrick.textures.add(Material.COBBLESTONE);
        SmoothBrick.textures.add(Material.SMOOTH_BRICK);
    }

    public SmoothBrick() {
        super(Material.SMOOTH_BRICK);
    }

    /** @deprecated */
    @Deprecated
    public SmoothBrick(int type) {
        super(type);
    }

    public SmoothBrick(Material type) {
        super(SmoothBrick.textures.contains(type) ? Material.SMOOTH_BRICK : type);
        if (SmoothBrick.textures.contains(type)) {
            this.setMaterial(type);
        }

    }

    /** @deprecated */
    @Deprecated
    public SmoothBrick(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public SmoothBrick(Material type, byte data) {
        super(type, data);
    }

    public List getTextures() {
        return SmoothBrick.textures;
    }

    public SmoothBrick clone() {
        return (SmoothBrick) super.clone();
    }
}
