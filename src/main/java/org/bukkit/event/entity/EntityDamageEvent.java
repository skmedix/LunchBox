package org.bukkit.event.entity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import java.util.EnumMap;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.NumberConversions;

public class EntityDamageEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private static final EntityDamageEvent.DamageModifier[] MODIFIERS = EntityDamageEvent.DamageModifier.values();
    private static final Function ZERO = Functions.constant(Double.valueOf(-0.0D));
    private final Map modifiers;
    private final Map modifierFunctions;
    private final Map originals;
    private boolean cancelled;
    private final EntityDamageEvent.DamageCause cause;

    /** @deprecated */
    @Deprecated
    public EntityDamageEvent(Entity damagee, EntityDamageEvent.DamageCause cause, int damage) {
        this(damagee, cause, (double) damage);
    }

    /** @deprecated */
    @Deprecated
    public EntityDamageEvent(Entity damagee, EntityDamageEvent.DamageCause cause, double damage) {
        this(damagee, cause, new EnumMap(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(damage))), new EnumMap(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, EntityDamageEvent.ZERO)));
    }

    public EntityDamageEvent(Entity damagee, EntityDamageEvent.DamageCause cause, Map modifiers, Map modifierFunctions) {
        super(damagee);
        Validate.isTrue(modifiers.containsKey(EntityDamageEvent.DamageModifier.BASE), "BASE DamageModifier missing");
        Validate.isTrue(!modifiers.containsKey((Object) null), "Cannot have null DamageModifier");
        Validate.noNullElements(modifiers.values(), "Cannot have null modifier values");
        Validate.isTrue(modifiers.keySet().equals(modifierFunctions.keySet()), "Must have a modifier function for each DamageModifier");
        Validate.noNullElements(modifierFunctions.values(), "Cannot have null modifier function");
        this.originals = new EnumMap(modifiers);
        this.cause = cause;
        this.modifiers = modifiers;
        this.modifierFunctions = modifierFunctions;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public double getOriginalDamage(EntityDamageEvent.DamageModifier type) throws IllegalArgumentException {
        Double damage = (Double) this.originals.get(type);

        if (damage != null) {
            return damage.doubleValue();
        } else if (type == null) {
            throw new IllegalArgumentException("Cannot have null DamageModifier");
        } else {
            return 0.0D;
        }
    }

    public void setDamage(EntityDamageEvent.DamageModifier type, double damage) throws IllegalArgumentException, UnsupportedOperationException {
        if (!this.modifiers.containsKey(type)) {
            throw type == null ? new IllegalArgumentException("Cannot have null DamageModifier") : new UnsupportedOperationException(type + " is not applicable to " + this.getEntity());
        } else {
            this.modifiers.put(type, Double.valueOf(damage));
        }
    }

    public double getDamage(EntityDamageEvent.DamageModifier type) throws IllegalArgumentException {
        Validate.notNull(type, "Cannot have null DamageModifier");
        Double damage = (Double) this.modifiers.get(type);

        return damage == null ? 0.0D : damage.doubleValue();
    }

    public boolean isApplicable(EntityDamageEvent.DamageModifier type) throws IllegalArgumentException {
        Validate.notNull(type, "Cannot have null DamageModifier");
        return this.modifiers.containsKey(type);
    }

    public double getDamage() {
        return this.getDamage(EntityDamageEvent.DamageModifier.BASE);
    }

    public final double getFinalDamage() {
        double damage = 0.0D;
        EntityDamageEvent.DamageModifier[] aentitydamageevent_damagemodifier = EntityDamageEvent.MODIFIERS;
        int i = EntityDamageEvent.MODIFIERS.length;

        for (int j = 0; j < i; ++j) {
            EntityDamageEvent.DamageModifier modifier = aentitydamageevent_damagemodifier[j];

            damage += this.getDamage(modifier);
        }

        return damage;
    }

    /** @deprecated */
    @Deprecated
    public int getDamage() {
        return NumberConversions.ceil(this.getDamage());
    }

    public void setDamage(double damage) {
        double remaining = damage;
        double oldRemaining = this.getDamage(EntityDamageEvent.DamageModifier.BASE);
        EntityDamageEvent.DamageModifier[] aentitydamageevent_damagemodifier = EntityDamageEvent.MODIFIERS;
        int i = EntityDamageEvent.MODIFIERS.length;

        for (int j = 0; j < i; ++j) {
            EntityDamageEvent.DamageModifier modifier = aentitydamageevent_damagemodifier[j];

            if (this.isApplicable(modifier)) {
                Function modifierFunction = (Function) this.modifierFunctions.get(modifier);
                double newVanilla = ((Double) modifierFunction.apply(Double.valueOf(remaining))).doubleValue();
                double oldVanilla = ((Double) modifierFunction.apply(Double.valueOf(oldRemaining))).doubleValue();
                double difference = oldVanilla - newVanilla;
                double old = this.getDamage(modifier);

                if (old > 0.0D) {
                    this.setDamage(modifier, Math.max(0.0D, old - difference));
                } else {
                    this.setDamage(modifier, Math.min(0.0D, old - difference));
                }

                remaining += newVanilla;
                oldRemaining += oldVanilla;
            }
        }

        this.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
    }

    /** @deprecated */
    @Deprecated
    public void setDamage(int damage) {
        this.setDamage((double) damage);
    }

    public EntityDamageEvent.DamageCause getCause() {
        return this.cause;
    }

    public HandlerList getHandlers() {
        return EntityDamageEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityDamageEvent.handlers;
    }

    public static enum DamageCause {

        CONTACT, ENTITY_ATTACK, PROJECTILE, SUFFOCATION, FALL, FIRE, FIRE_TICK, MELTING, LAVA, DROWNING, BLOCK_EXPLOSION, ENTITY_EXPLOSION, VOID, LIGHTNING, SUICIDE, STARVATION, POISON, MAGIC, WITHER, FALLING_BLOCK, THORNS, CUSTOM;
    }

    public static enum DamageModifier {

        BASE, HARD_HAT, BLOCKING, ARMOR, RESISTANCE, MAGIC, ABSORPTION;
    }
}
