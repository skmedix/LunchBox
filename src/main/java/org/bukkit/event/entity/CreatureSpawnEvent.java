package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class CreatureSpawnEvent extends EntitySpawnEvent {

    private final CreatureSpawnEvent.SpawnReason spawnReason;

    public CreatureSpawnEvent(LivingEntity spawnee, CreatureSpawnEvent.SpawnReason spawnReason) {
        super(spawnee);
        this.spawnReason = spawnReason;
    }

    /** @deprecated */
    @Deprecated
    public CreatureSpawnEvent(Entity spawnee, CreatureType type, Location loc, CreatureSpawnEvent.SpawnReason reason) {
        super(spawnee);
        this.spawnReason = reason;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /** @deprecated */
    @Deprecated
    public CreatureType getCreatureType() {
        return CreatureType.fromEntityType(this.getEntityType());
    }

    public CreatureSpawnEvent.SpawnReason getSpawnReason() {
        return this.spawnReason;
    }

    public static enum SpawnReason {

        NATURAL, JOCKEY, CHUNK_GEN, SPAWNER, EGG, SPAWNER_EGG, LIGHTNING, /** @deprecated */
@Deprecated
BED, BUILD_SNOWMAN, BUILD_IRONGOLEM, BUILD_WITHER, VILLAGE_DEFENSE, VILLAGE_INVASION, BREEDING, SLIME_SPLIT, REINFORCEMENTS, NETHER_PORTAL, DISPENSE_EGG, INFECTION, CURED, OCELOT_BABY, SILVERFISH_BLOCK, MOUNT, CUSTOM, DEFAULT;
    }
}
