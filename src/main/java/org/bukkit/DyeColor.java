package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public enum DyeColor {

    WHITE(0, 15, Color.WHITE, Color.fromRGB(15790320)), ORANGE(1, 14, Color.fromRGB(14188339), Color.fromRGB(15435844)), MAGENTA(2, 13, Color.fromRGB(11685080), Color.fromRGB(12801229)), LIGHT_BLUE(3, 12, Color.fromRGB(6724056), Color.fromRGB(6719955)), YELLOW(4, 11, Color.fromRGB(15066419), Color.fromRGB(14602026)), LIME(5, 10, Color.fromRGB(8375321), Color.fromRGB(4312372)), PINK(6, 9, Color.fromRGB(15892389), Color.fromRGB(14188952)), GRAY(7, 8, Color.fromRGB(5000268), Color.fromRGB(4408131)), SILVER(8, 7, Color.fromRGB(10066329), Color.fromRGB(11250603)), CYAN(9, 6, Color.fromRGB(5013401), Color.fromRGB(2651799)), PURPLE(10, 5, Color.fromRGB(8339378), Color.fromRGB(8073150)), BLUE(11, 4, Color.fromRGB(3361970), Color.fromRGB(2437522)), BROWN(12, 3, Color.fromRGB(6704179), Color.fromRGB(5320730)), GREEN(13, 2, Color.fromRGB(6717235), Color.fromRGB(3887386)), RED(14, 1, Color.fromRGB(10040115), Color.fromRGB(11743532)), BLACK(15, 0, Color.fromRGB(1644825), Color.fromRGB(1973019));

    private final byte woolData;
    private final byte dyeData;
    private final Color color;
    private final Color firework;
    private static final DyeColor[] BY_WOOL_DATA = values();
    private static final DyeColor[] BY_DYE_DATA = values();
    private static final Map BY_COLOR;
    private static final Map BY_FIREWORK;

    static {
        ImmutableMap.Builder byColor = ImmutableMap.builder();
        ImmutableMap.Builder byFirework = ImmutableMap.builder();
        DyeColor[] adyecolor;
        int i = (adyecolor = values()).length;

        for (int j = 0; j < i; ++j) {
            DyeColor color = adyecolor[j];

            DyeColor.BY_WOOL_DATA[color.woolData & 255] = color;
            DyeColor.BY_DYE_DATA[color.dyeData & 255] = color;
            byColor.put(color.getColor(), color);
            byFirework.put(color.getFireworkColor(), color);
        }

        BY_COLOR = byColor.build();
        BY_FIREWORK = byFirework.build();
    }

    private DyeColor(int woolData, int dyeData, Color color, Color firework) {
        this.woolData = (byte) woolData;
        this.dyeData = (byte) dyeData;
        this.color = color;
        this.firework = firework;
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.getWoolData();
    }

    /** @deprecated */
    @Deprecated
    public byte getWoolData() {
        return this.woolData;
    }

    /** @deprecated */
    @Deprecated
    public byte getDyeData() {
        return this.dyeData;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getFireworkColor() {
        return this.firework;
    }

    /** @deprecated */
    @Deprecated
    public static DyeColor getByData(byte data) {
        return getByWoolData(data);
    }

    /** @deprecated */
    @Deprecated
    public static DyeColor getByWoolData(byte data) {
        int i = 255 & data;

        return i >= DyeColor.BY_WOOL_DATA.length ? null : DyeColor.BY_WOOL_DATA[i];
    }

    /** @deprecated */
    @Deprecated
    public static DyeColor getByDyeData(byte data) {
        int i = 255 & data;

        return i >= DyeColor.BY_DYE_DATA.length ? null : DyeColor.BY_DYE_DATA[i];
    }

    public static DyeColor getByColor(Color color) {
        return (DyeColor) DyeColor.BY_COLOR.get(color);
    }

    public static DyeColor getByFireworkColor(Color color) {
        return (DyeColor) DyeColor.BY_FIREWORK.get(color);
    }
}
