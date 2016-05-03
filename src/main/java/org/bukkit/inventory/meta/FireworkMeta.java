package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.FireworkEffect;

public interface FireworkMeta extends ItemMeta {

    void addEffect(FireworkEffect fireworkeffect) throws IllegalArgumentException;

    void addEffects(FireworkEffect... afireworkeffect) throws IllegalArgumentException;

    void addEffects(Iterable iterable) throws IllegalArgumentException;

    List getEffects();

    int getEffectsSize();

    void removeEffect(int i) throws IndexOutOfBoundsException;

    void clearEffects();

    boolean hasEffects();

    int getPower();

    void setPower(int i) throws IllegalArgumentException;

    FireworkMeta clone();
}
