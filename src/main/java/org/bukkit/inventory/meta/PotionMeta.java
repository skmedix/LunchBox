package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public interface PotionMeta extends ItemMeta {

    boolean hasCustomEffects();

    List getCustomEffects();

    boolean addCustomEffect(PotionEffect potioneffect, boolean flag);

    boolean removeCustomEffect(PotionEffectType potioneffecttype);

    boolean hasCustomEffect(PotionEffectType potioneffecttype);

    boolean setMainEffect(PotionEffectType potioneffecttype);

    boolean clearCustomEffects();

    PotionMeta clone();
}
