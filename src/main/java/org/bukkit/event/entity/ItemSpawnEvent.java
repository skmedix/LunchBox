package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Item;

public class ItemSpawnEvent extends EntitySpawnEvent {

    public ItemSpawnEvent(Item spawnee) {
        super(spawnee);
    }

    /** @deprecated */
    @Deprecated
    public ItemSpawnEvent(Item spawnee, Location loc) {
        this(spawnee);
    }

    public Item getEntity() {
        return (Item) this.entity;
    }
}
