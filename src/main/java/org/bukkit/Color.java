package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Color")
public final class Color implements ConfigurationSerializable {

    private static final int BIT_MASK = 255;
    public static final Color WHITE = fromRGB(16777215);
    public static final Color SILVER = fromRGB(12632256);
    public static final Color GRAY = fromRGB(8421504);
    public static final Color BLACK = fromRGB(0);
    public static final Color RED = fromRGB(16711680);
    public static final Color MAROON = fromRGB(8388608);
    public static final Color YELLOW = fromRGB(16776960);
    public static final Color OLIVE = fromRGB(8421376);
    public static final Color LIME = fromRGB('\uff00');
    public static final Color GREEN = fromRGB('耀');
    public static final Color AQUA = fromRGB('\uffff');
    public static final Color TEAL = fromRGB('肀');
    public static final Color BLUE = fromRGB(255);
    public static final Color NAVY = fromRGB(128);
    public static final Color FUCHSIA = fromRGB(16711935);
    public static final Color PURPLE = fromRGB(8388736);
    public static final Color ORANGE = fromRGB(16753920);
    private final byte red;
    private final byte green;
    private final byte blue;

    public static Color fromRGB(int red, int green, int blue) throws IllegalArgumentException {
        return new Color(red, green, blue);
    }

    public static Color fromBGR(int blue, int green, int red) throws IllegalArgumentException {
        return new Color(red, green, blue);
    }

    public static Color fromRGB(int rgb) throws IllegalArgumentException {
        Validate.isTrue(rgb >> 24 == 0, "Extrenuous data in: ", (long) rgb);
        return fromRGB(rgb >> 16 & 255, rgb >> 8 & 255, rgb >> 0 & 255);
    }

    public static Color fromBGR(int bgr) throws IllegalArgumentException {
        Validate.isTrue(bgr >> 24 == 0, "Extrenuous data in: ", (long) bgr);
        return fromBGR(bgr >> 16 & 255, bgr >> 8 & 255, bgr >> 0 & 255);
    }

    private Color(int red, int green, int blue) {
        Validate.isTrue(red >= 0 && red <= 255, "Red is not between 0-255: ", (long) red);
        Validate.isTrue(green >= 0 && green <= 255, "Green is not between 0-255: ", (long) green);
        Validate.isTrue(blue >= 0 && blue <= 255, "Blue is not between 0-255: ", (long) blue);
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    public int getRed() {
        return 255 & this.red;
    }

    public Color setRed(int red) {
        return fromRGB(red, this.getGreen(), this.getBlue());
    }

    public int getGreen() {
        return 255 & this.green;
    }

    public Color setGreen(int green) {
        return fromRGB(this.getRed(), green, this.getBlue());
    }

    public int getBlue() {
        return 255 & this.blue;
    }

    public Color setBlue(int blue) {
        return fromRGB(this.getRed(), this.getGreen(), blue);
    }

    public int asRGB() {
        return this.getRed() << 16 | this.getGreen() << 8 | this.getBlue() << 0;
    }

    public int asBGR() {
        return this.getBlue() << 16 | this.getGreen() << 8 | this.getRed() << 0;
    }

    public Color mixDyes(DyeColor... colors) {
        Validate.noNullElements((Object[]) colors, "Colors cannot be null");
        Color[] toPass = new Color[colors.length];

        for (int i = 0; i < colors.length; ++i) {
            toPass[i] = colors[i].getColor();
        }

        return this.mixColors(toPass);
    }

    public Color mixColors(Color... colors) {
        Validate.noNullElements((Object[]) colors, "Colors cannot be null");
        int totalRed = this.getRed();
        int totalGreen = this.getGreen();
        int totalBlue = this.getBlue();
        int totalMax = Math.max(Math.max(totalRed, totalGreen), totalBlue);
        Color[] averageMax = colors;
        int averageBlue = colors.length;

        for (int averageGreen = 0; averageGreen < averageBlue; ++averageGreen) {
            Color averageRed = averageMax[averageGreen];

            totalRed += averageRed.getRed();
            totalGreen += averageRed.getGreen();
            totalBlue += averageRed.getBlue();
            totalMax += Math.max(Math.max(averageRed.getRed(), averageRed.getGreen()), averageRed.getBlue());
        }

        float f = (float) (totalRed / (colors.length + 1));
        float f1 = (float) (totalGreen / (colors.length + 1));
        float f2 = (float) (totalBlue / (colors.length + 1));
        float f3 = (float) (totalMax / (colors.length + 1));
        float maximumOfAverages = Math.max(Math.max(f, f1), f2);
        float gainFactor = f3 / maximumOfAverages;

        return fromRGB((int) (f * gainFactor), (int) (f1 * gainFactor), (int) (f2 * gainFactor));
    }

    public boolean equals(Object o) {
        if (!(o instanceof Color)) {
            return false;
        } else {
            Color that = (Color) o;

            return this.blue == that.blue && this.green == that.green && this.red == that.red;
        }
    }

    public int hashCode() {
        return this.asRGB() ^ Color.class.hashCode();
    }

    public Map serialize() {
        return ImmutableMap.of("RED", Integer.valueOf(this.getRed()), "BLUE", Integer.valueOf(this.getBlue()), "GREEN", Integer.valueOf(this.getGreen()));
    }

    public static Color deserialize(Map map) {
        return fromRGB(asInt("RED", map), asInt("GREEN", map), asInt("BLUE", map));
    }

    private static int asInt(String string, Map map) {
        Object value = map.get(string);

        if (value == null) {
            throw new IllegalArgumentException(string + " not in map " + map);
        } else if (!(value instanceof Number)) {
            throw new IllegalArgumentException(string + '(' + value + ") is not a number");
        } else {
            return ((Number) value).intValue();
        }
    }

    public String toString() {
        return "Color:[rgb0x" + Integer.toHexString(this.getRed()).toUpperCase() + Integer.toHexString(this.getGreen()).toUpperCase() + Integer.toHexString(this.getBlue()).toUpperCase() + "]";
    }
}
