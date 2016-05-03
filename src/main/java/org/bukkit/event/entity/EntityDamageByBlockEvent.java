package org.bukkit.event.entity;

import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class EntityDamageByBlockEvent extends EntityDamageEvent {

    private final Block damager;

    /** @deprecated */
    @Deprecated
    public EntityDamageByBlockEvent(Block damager, Entity damagee, EntityDamageEvent.DamageCause cause, int damage) {
        this(damager, damagee, cause, (double) damage);
    }

    /** @deprecated */
    @Deprecated
    public EntityDamageByBlockEvent(Block damager, Entity damagee, EntityDamageEvent.DamageCause cause, double damage) {
        super(damagee, cause, damage);
        this.damager = damager;
    }

    public EntityDamageByBlockEvent(Block damager, Entity damagee, EntityDamageEvent.DamageCause cause, Map modifiers, Map modifierFunctions) {
        super(damagee, cause, modifiers, modifierFunctions);
        this.damager = damager;
    }

    public Block getDamager() {
        return this.damager;
    }
}
