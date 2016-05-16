package org.bukkit.craftbukkit.v1_8_R3.potion;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.potion.PotionHelper;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftPotionBrewer implements PotionBrewer {

    private static final Map cache = Maps.newHashMap();

    public Collection getEffectsFromDamage(int damage) {
        if (CraftPotionBrewer.cache.containsKey(Integer.valueOf(damage))) {
            return (Collection) CraftPotionBrewer.cache.get(Integer.valueOf(damage));
        } else {
            List mcEffects = PotionHelper.getPotionEffects(damage, false);
            ArrayList effects = new ArrayList();

            if (mcEffects == null) {
                return effects;
            } else {
                Iterator iterator = mcEffects.iterator();

                while (iterator.hasNext()) {
                    Object raw = iterator.next();

                    if (raw != null && raw instanceof net.minecraft.potion.PotionEffect) {
                        net.minecraft.potion.PotionEffect mcEffect = (net.minecraft.potion.PotionEffect) raw;
                        PotionEffect effect = new PotionEffect(PotionEffectType.getById(mcEffect.getPotionID()), mcEffect.getDuration(), mcEffect.getAmplifier());

                        effects.add(effect);
                    }
                }

                CraftPotionBrewer.cache.put(Integer.valueOf(damage), effects);
                return effects;
            }
        }
    }

    public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier) {
        return new PotionEffect(potion, potion.isInstant() ? 1 : (int) ((double) duration * potion.getDurationModifier()), amplifier);
    }
}
