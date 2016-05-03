package org.bukkit.event.entity;

import java.util.Map;
import org.bukkit.entity.Entity;

public class EntityDamageByEntityEvent extends EntityDamageEvent {

    private final Entity damager;

    /** @deprecated */
    @Deprecated
    public EntityDamageByEntityEvent(Entity damager, Entity damagee, EntityDamageEvent.DamageCause cause, int damage) {
        this(damager, damagee, cause, (double) damage);
    }

    /** @deprecated */
    @Deprecated
    public EntityDamageByEntityEvent(Entity damager, Entity damagee, EntityDamageEvent.DamageCause cause, double damage) {
        super(damagee, cause, damage);
        this.damager = damager;
    }

    public EntityDamageByEntityEvent(Entity damager, Entity damagee, EntityDamageEvent.DamageCause cause, Map modifiers, Map modifierFunctions) {
        super(damagee, cause, modifiers, modifierFunctions);
        this.damager = damager;
    }

    public Entity getDamager() {
        return this.damager;
    }
}
