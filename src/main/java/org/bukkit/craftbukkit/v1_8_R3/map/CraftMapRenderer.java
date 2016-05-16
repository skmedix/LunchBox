package org.bukkit.craftbukkit.v1_8_R3.map;

import java.util.Iterator;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.MapIcon;
import net.minecraft.server.v1_8_R3.WorldMap;
import net.minecraft.world.storage.MapData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
//todo: maybe remove all the map stuff and leave it to forge?
public class CraftMapRenderer extends MapRenderer {

    private final MapData worldMap;

    public CraftMapRenderer(CraftMapView mapView, MapData worldMap) {
        super(false);
        this.worldMap = worldMap;
    }

    public void render(MapView map, MapCanvas canvas, Player player) {
        for (int cursors = 0; cursors < 128; ++cursors) {
            for (int key = 0; key < 128; ++key) {
                canvas.setPixel(cursors, key, this.worldMap.colors[key * 128 + cursors]);
            }
        }

        MapCursorCollection mapcursorcollection = canvas.getCursors();

        while (mapcursorcollection.size() > 0) {
            mapcursorcollection.removeCursor(mapcursorcollection.getCursor(0));
        }

        Iterator iterator = this.worldMap.mapDecorations.keySet().iterator();

        while (iterator.hasNext()) {
            UUID uuid = (UUID) iterator.next();
            Player other = Bukkit.getPlayer(uuid);

            if (other == null || player.canSee(other)) {
                MapData.MapInfo decoration = (MapData.MapInfo) this.worldMap.mapDecorations.get(uuid);

                mapcursorcollection.addCursor(decoration.getX(), decoration.getY(), (byte) (decoration.getRotation() & 15), decoration.getType());
            }
        }

    }
}
