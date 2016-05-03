package org.bukkit.entity;

import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.InventoryHolder;

public interface Horse extends Animals, Vehicle, InventoryHolder, Tameable {

    Horse.Variant getVariant();

    void setVariant(Horse.Variant horse_variant);

    Horse.Color getColor();

    void setColor(Horse.Color horse_color);

    Horse.Style getStyle();

    void setStyle(Horse.Style horse_style);

    boolean isCarryingChest();

    void setCarryingChest(boolean flag);

    int getDomestication();

    void setDomestication(int i);

    int getMaxDomestication();

    void setMaxDomestication(int i);

    double getJumpStrength();

    void setJumpStrength(double d0);

    HorseInventory getInventory();

    public static enum Color {

        WHITE, CREAMY, CHESTNUT, BROWN, BLACK, GRAY, DARK_BROWN;
    }

    public static enum Style {

        NONE, WHITE, WHITEFIELD, WHITE_DOTS, BLACK_DOTS;
    }

    public static enum Variant {

        HORSE, DONKEY, MULE, UNDEAD_HORSE, SKELETON_HORSE;
    }
}
