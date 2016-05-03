package org.bukkit.potion;

import java.util.Collection;

public interface PotionBrewer {

    PotionEffect createEffect(PotionEffectType potioneffecttype, int i, int j);

    /** @deprecated */
    @Deprecated
    Collection getEffectsFromDamage(int i);
}
