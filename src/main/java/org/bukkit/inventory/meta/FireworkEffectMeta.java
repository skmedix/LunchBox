package org.bukkit.inventory.meta;

import org.bukkit.FireworkEffect;

public interface FireworkEffectMeta extends ItemMeta {

    void setEffect(FireworkEffect fireworkeffect);

    boolean hasEffect();

    FireworkEffect getEffect();

    FireworkEffectMeta clone();
}
