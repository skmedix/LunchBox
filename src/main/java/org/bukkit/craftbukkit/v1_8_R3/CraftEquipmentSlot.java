package org.bukkit.craftbukkit.v1_8_R3;

import org.bukkit.inventory.EquipmentSlot;

public class CraftEquipmentSlot {

    private static final int[] slots = new int[EquipmentSlot.values().length];
    private static final EquipmentSlot[] enums = new EquipmentSlot[EquipmentSlot.values().length];

    static {
        set(EquipmentSlot.HAND, 0);
        set(EquipmentSlot.FEET, 1);
        set(EquipmentSlot.LEGS, 2);
        set(EquipmentSlot.CHEST, 3);
        set(EquipmentSlot.HEAD, 4);
    }

    private static void set(EquipmentSlot type, int value) {
        CraftEquipmentSlot.slots[type.ordinal()] = value;
        if (value < CraftEquipmentSlot.enums.length) {
            CraftEquipmentSlot.enums[value] = type;
        }

    }

    public static EquipmentSlot getSlot(int magic) {
        return magic > 0 && magic < CraftEquipmentSlot.enums.length ? CraftEquipmentSlot.enums[magic] : null;
    }

    public static int getSlotIndex(EquipmentSlot slot) {
        return CraftEquipmentSlot.slots[slot.ordinal()];
    }
}
