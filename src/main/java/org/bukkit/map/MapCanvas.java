package org.bukkit.map;

import java.awt.Image;

public interface MapCanvas {

    MapView getMapView();

    MapCursorCollection getCursors();

    void setCursors(MapCursorCollection mapcursorcollection);

    void setPixel(int i, int j, byte b0);

    byte getPixel(int i, int j);

    byte getBasePixel(int i, int j);

    void drawImage(int i, int j, Image image);

    void drawText(int i, int j, MapFont mapfont, String s);
}
