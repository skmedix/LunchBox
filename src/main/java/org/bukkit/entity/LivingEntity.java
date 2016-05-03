package org.bukkit.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public interface LivingEntity extends Entity, Damageable, ProjectileSource {

    double getEyeHeight();

    double getEyeHeight(boolean flag);

    Location getEyeLocation();

    /** @deprecated */
    @Deprecated
    List getLineOfSight(HashSet hashset, int i);

    List getLineOfSight(Set set, int i);

    /** @deprecated */
    @Deprecated
    Block getTargetBlock(HashSet hashset, int i);

    Block getTargetBlock(Set set, int i);

    /** @deprecated */
    @Deprecated
    List getLastTwoTargetBlocks(HashSet hashset, int i);

    List getLastTwoTargetBlocks(Set set, int i);

    /** @deprecated */
    @Deprecated
    Egg throwEgg();

    /** @deprecated */
    @Deprecated
    Snowball throwSnowball();

    /** @deprecated */
    @Deprecated
    Arrow shootArrow();

    int getRemainingAir();

    void setRemainingAir(int i);

    int getMaximumAir();

    void setMaximumAir(int i);

    int getMaximumNoDamageTicks();

    void setMaximumNoDamageTicks(int i);

    double getLastDamage();

    /** @deprecated */
    @Deprecated
    int getLastDamage();

    void setLastDamage(double d0);

    /** @deprecated */
    @Deprecated
    void setLastDamage(int i);

    int getNoDamageTicks();

    void setNoDamageTicks(int i);

    Player getKiller();

    boolean addPotionEffect(PotionEffect potioneffect);

    boolean addPotionEffect(PotionEffect potioneffect, boolean flag);

    boolean addPotionEffects(Collection collection);

    boolean hasPotionEffect(PotionEffectType potioneffecttype);

    void removePotionEffect(PotionEffectType potioneffecttype);

    Collection getActivePotionEffects();

    boolean hasLineOfSight(Entity entity);

    boolean getRemoveWhenFarAway();

    void setRemoveWhenFarAway(boolean flag);

    EntityEquipment getEquipment();

    void setCanPickupItems(boolean flag);

    boolean getCanPickupItems();

    boolean isLeashed();

    Entity getLeashHolder() throws IllegalStateException;

    boolean setLeashHolder(Entity entity);
}
