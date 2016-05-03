package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class MonsterEggs extends TexturedMaterial {

    private static final List textures = new ArrayList();

    static {
        MonsterEggs.textures.add(Material.STONE);
        MonsterEggs.textures.add(Material.COBBLESTONE);
        MonsterEggs.textures.add(Material.SMOOTH_BRICK);
    }

    public MonsterEggs() {
        super(Material.MONSTER_EGGS);
    }

    /** @deprecated */
    @Deprecated
    public MonsterEggs(int type) {
        super(type);
    }

    public MonsterEggs(Material type) {
        super(MonsterEggs.textures.contains(type) ? Material.MONSTER_EGGS : type);
        if (MonsterEggs.textures.contains(type)) {
            this.setMaterial(type);
        }

    }

    /** @deprecated */
    @Deprecated
    public MonsterEggs(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public MonsterEggs(Material type, byte data) {
        super(type, data);
    }

    public List getTextures() {
        return MonsterEggs.textures;
    }

    public MonsterEggs clone() {
        return (MonsterEggs) super.clone();
    }
}
