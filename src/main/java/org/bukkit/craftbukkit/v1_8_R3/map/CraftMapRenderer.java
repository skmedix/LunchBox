package org.bukkit.craftbukkit.v1_8_R3.map;

import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.MapData;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
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
            //Player other = Bukkit.getPlayer(uuid);
            EntityPlayer other = (EntityPlayer) MinecraftServer.getServer().getEntityFromUuid(uuid);

            if (other == null || player.canSee((Player) other)) {
                MapData.MapInfo decoration = this.worldMap.getMapInfo(other);

                mapcursorcollection.addCursor((int) decoration.entityplayerObj.posX, (int) decoration.entityplayerObj.posZ, (byte) ((int) decoration.entityplayerObj.rotationYaw & 15));
            }
        }

    }
}
