package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SpawnEgg extends MaterialData {

    public SpawnEgg() {
        super(Material.MONSTER_EGG);
    }

    /** @deprecated */
    @Deprecated
    public SpawnEgg(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public SpawnEgg(byte data) {
        super(Material.MONSTER_EGG, data);
    }

    public SpawnEgg(EntityType type) {
        this();
        this.setSpawnedType(type);
    }

    public EntityType getSpawnedType() {
        return EntityType.fromId(this.getData());
    }

    public void setSpawnedType(EntityType type) {
        this.setData((byte) type.getTypeId());
    }

    public String toString() {
        return "SPAWN EGG{" + this.getSpawnedType() + "}";
    }

    public SpawnEgg clone() {
        return (SpawnEgg) super.clone();
    }
}
