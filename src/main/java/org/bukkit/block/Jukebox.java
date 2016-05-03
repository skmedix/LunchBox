package org.bukkit.block;

import org.bukkit.Material;

public interface Jukebox extends BlockState {

    Material getPlaying();

    void setPlaying(Material material);

    boolean isPlaying();

    boolean eject();
}
