package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.map.MapView;

public class MapInitializeEvent extends ServerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final MapView mapView;

    public MapInitializeEvent(MapView mapView) {
        this.mapView = mapView;
    }

    public MapView getMap() {
        return this.mapView;
    }

    public HandlerList getHandlers() {
        return MapInitializeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return MapInitializeEvent.handlers;
    }
}
