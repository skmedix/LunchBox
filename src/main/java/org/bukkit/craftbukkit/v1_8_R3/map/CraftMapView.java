package org.bukkit.craftbukkit.v1_8_R3.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.world.storage.MapData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public final class CraftMapView implements MapView {

    private final Map renderCache = new HashMap();
    private final List renderers = new ArrayList();
    private final Map canvases = new HashMap();
    protected final MapData worldMap;

    public CraftMapView(MapData worldMap) {
        this.worldMap = worldMap;
        this.addRenderer(new CraftMapRenderer(this, worldMap));
    }

    public short getId() {
        String text = this.worldMap.mapName;

        if (text.startsWith("map_")) {
            try {
                return Short.parseShort(text.substring("map_".length()));
            } catch (NumberFormatException numberformatexception) {
                throw new IllegalStateException("Map has non-numeric ID");
            }
        } else {
            throw new IllegalStateException("Map has invalid ID");
        }
    }

    public boolean isVirtual() {
        return this.renderers.size() > 0 && !(this.renderers.get(0) instanceof CraftMapRenderer);
    }

    public MapView.Scale getScale() {
        return MapView.Scale.valueOf(this.worldMap.scale);
    }

    public void setScale(MapView.Scale scale) {
        this.worldMap.scale = scale.getValue();
    }

    public World getWorld() {
        byte dimension = (byte) this.worldMap.dimension;
        Iterator iterator = Bukkit.getServer().getWorlds().iterator();

        while (iterator.hasNext()) {
            World world = (World) iterator.next();

            if (((CraftWorld) world).getHandle().provider.getDimensionId() == dimension) {
                return world;
            }
        }

        return null;
    }

    public void setWorld(World world) {
        worldMap.dimension = (byte) ((CraftWorld) world).getHandle().provider.getDimensionId();
    }

    public int getCenterX() {
        return this.worldMap.xCenter;
    }

    public int getCenterZ() {
        return this.worldMap.zCenter;
    }

    public void setCenterX(int x) {
        this.worldMap.xCenter = x;
    }

    public void setCenterZ(int z) {
        this.worldMap.zCenter = z;
    }

    public List getRenderers() {
        return new ArrayList(this.renderers);
    }

    public void addRenderer(MapRenderer renderer) {
        if (!this.renderers.contains(renderer)) {
            this.renderers.add(renderer);
            this.canvases.put(renderer, new HashMap());
            renderer.initialize(this);
        }

    }

    public boolean removeRenderer(MapRenderer renderer) {
        if (!this.renderers.contains(renderer)) {
            return false;
        } else {
            this.renderers.remove(renderer);
            Iterator iterator = ((Map) this.canvases.get(renderer)).entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                for (int x = 0; x < 128; ++x) {
                    for (int y = 0; y < 128; ++y) {
                        ((CraftMapCanvas) entry.getValue()).setPixel(x, y, (byte) -1);
                    }
                }
            }

            this.canvases.remove(renderer);
            return true;
        }
    }

    private boolean isContextual() {
        Iterator iterator = this.renderers.iterator();

        while (iterator.hasNext()) {
            MapRenderer renderer = (MapRenderer) iterator.next();

            if (renderer.isContextual()) {
                return true;
            }
        }

        return false;
    }

    public RenderData render(CraftPlayer player) {
        boolean context = this.isContextual();
        RenderData render = (RenderData) this.renderCache.get(context ? player : null);

        if (render == null) {
            render = new RenderData();
            this.renderCache.put(context ? player : null, render);
        }

        if (context && this.renderCache.containsKey((Object) null)) {
            this.renderCache.remove((Object) null);
        }

        Arrays.fill(render.buffer, (byte) 0);
        render.cursors.clear();
        Iterator iterator = this.renderers.iterator();

        while (iterator.hasNext()) {
            MapRenderer renderer = (MapRenderer) iterator.next();
            CraftMapCanvas canvas = (CraftMapCanvas) ((Map) this.canvases.get(renderer)).get(renderer.isContextual() ? player : null);

            if (canvas == null) {
                canvas = new CraftMapCanvas(this);
                ((Map) this.canvases.get(renderer)).put(renderer.isContextual() ? player : null, canvas);
            }

            canvas.setBase(render.buffer);
            renderer.render(this, canvas, player);
            byte[] buf = canvas.getBuffer();

            int i;

            for (i = 0; i < buf.length; ++i) {
                byte color = buf[i];

                if (color >= 0 || color <= -113) {
                    render.buffer[i] = color;
                }
            }

            for (i = 0; i < canvas.getCursors().size(); ++i) {
                render.cursors.add(canvas.getCursors().getCursor(i));
            }
        }

        return render;
    }
}
