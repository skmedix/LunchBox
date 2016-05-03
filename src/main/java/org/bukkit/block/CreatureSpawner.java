package org.bukkit.block;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;

public interface CreatureSpawner extends BlockState {

    /** @deprecated */
    @Deprecated
    CreatureType getCreatureType();

    EntityType getSpawnedType();

    void setSpawnedType(EntityType entitytype);

    /** @deprecated */
    @Deprecated
    void setCreatureType(CreatureType creaturetype);

    /** @deprecated */
    @Deprecated
    String getCreatureTypeId();

    void setCreatureTypeByName(String s);

    String getCreatureTypeName();

    /** @deprecated */
    @Deprecated
    void setCreatureTypeId(String s);

    int getDelay();

    void setDelay(int i);
}
