package org.bukkit.map;

import org.bukkit.entity.Player;

public abstract class MapRenderer {

    private boolean contextual;

    public MapRenderer() {
        this(false);
    }

    public MapRenderer(boolean contextual) {
        this.contextual = contextual;
    }

    public final boolean isContextual() {
        return this.contextual;
    }

    public void initialize(MapView map) {}

    public abstract void render(MapView mapview, MapCanvas mapcanvas, Player player);
}
