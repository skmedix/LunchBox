package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityMobSpawner;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
//todo: need to come back and redo some methods in this class.
public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {

    private final TileEntityMobSpawner spawner;

    public CraftCreatureSpawner(Block block) {
        super(block);
        this.spawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftCreatureSpawner(Material material, TileEntityMobSpawner te) {
        super(material);
        this.spawner = te;
    }

    /** @deprecated */
    @Deprecated
    public CreatureType getCreatureType() {
        return CreatureType.fromName(this.spawner.getSpawner().getMobName());
    }

    public EntityType getSpawnedType() {
        return EntityType.fromName(this.spawner.getSpawner().getMobName());
    }

    /** @deprecated */
    @Deprecated
    public void setCreatureType(CreatureType creatureType) {
        this.spawner.getSpawnerBaseLogic().setEntityName(creatureType.getName());
    }

    public void setSpawnedType(EntityType entityType) {
        if (entityType != null && entityType.getName() != null) {
            this.spawner.getSpawnerBaseLogic().setEntityName(entityType.getName());
        } else {
            throw new IllegalArgumentException("Can\'t spawn EntityType " + entityType + " from mobspawners!");
        }
    }

    /** @deprecated */
    @Deprecated
    public String getCreatureTypeId() {
        return this.spawner.getSpawner().getMobName();
    }

    /** @deprecated */
    @Deprecated
    public void setCreatureTypeId(String creatureName) {
        this.setCreatureTypeByName(creatureName);
    }

    public String getCreatureTypeName() {
        return this.spawner.getSpagwner().getMobName();
    }

    public void setCreatureTypeByName(String creatureType) {
        EntityType type = EntityType.fromName(creatureType);

        if (type != null) {
            this.setSpawnedType(type);
        }
    }

    public int getDelay() {
        return this.spawner.getSpawner().spawnDelay;
    }

    public void setDelay(int delay) {
        this.spawner.getSpawnerBaseLogic().setDelayToMin(delay);
    }

    public TileEntityMobSpawner getTileEntity() {
        return this.spawner;
    }
}
