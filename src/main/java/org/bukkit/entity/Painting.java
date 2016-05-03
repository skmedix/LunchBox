package org.bukkit.entity;

import org.bukkit.Art;

public interface Painting extends Hanging {

    Art getArt();

    boolean setArt(Art art);

    boolean setArt(Art art, boolean flag);
}
