package org.bukkit.map;

import java.util.List;
import org.bukkit.World;

public interface MapView {

    /** @deprecated */
    @Deprecated
    short getId();

    boolean isVirtual();

    MapView.Scale getScale();

    void setScale(MapView.Scale mapview_scale);

    int getCenterX();

    int getCenterZ();

    void setCenterX(int i);

    void setCenterZ(int i);

    World getWorld();

    void setWorld(World world);

    List getRenderers();

    void addRenderer(MapRenderer maprenderer);

    boolean removeRenderer(MapRenderer maprenderer);

    public static enum Scale {

        CLOSEST(0), CLOSE(1), NORMAL(2), FAR(3), FARTHEST(4);

        private byte value;

        private Scale(int value) {
            this.value = (byte) value;
        }

        /** @deprecated */
        @Deprecated
        public static MapView.Scale valueOf(byte value) {
            switch (value) {
            case 0:
                return MapView.Scale.CLOSEST;

            case 1:
                return MapView.Scale.CLOSE;

            case 2:
                return MapView.Scale.NORMAL;

            case 3:
                return MapView.Scale.FAR;

            case 4:
                return MapView.Scale.FARTHEST;

            default:
                return null;
            }
        }

        /** @deprecated */
        @Deprecated
        public byte getValue() {
            return this.value;
        }
    }
}
