package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.Container;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryView extends InventoryView {

    private final Container container;
    private final CraftHumanEntity player;
    private final CraftInventory viewing;
    private static int[] $SWITCH_TABLE$org$bukkit$event$inventory$InventoryType;

    public CraftInventoryView(HumanEntity player, Inventory viewing, Container container) {
        this.player = (CraftHumanEntity) player;
        this.viewing = (CraftInventory) viewing;
        this.container = container;
    }

    public Inventory getTopInventory() {
        return this.viewing;
    }

    public Inventory getBottomInventory() {
        return this.player.getInventory();
    }

    public HumanEntity getPlayer() {
        return this.player;
    }

    public InventoryType getType() {
        InventoryType type = this.viewing.getType();

        return type == InventoryType.CRAFTING && this.player.getGameMode() == GameMode.CREATIVE ? InventoryType.CREATIVE : type;
    }

    public void setItem(int slot, ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);

        if (slot != -999) {
            this.container.getSlot(slot).set(stack);
        } else {
            this.player.getHandle().drop(stack, false);
        }

    }

    public ItemStack getItem(int slot) {
        return slot == -999 ? null : CraftItemStack.asCraftMirror(this.container.getSlot(slot).getItem());
    }

    public boolean isInTop(int rawSlot) {
        return rawSlot < this.viewing.getSize();
    }

    public Container getHandle() {
        return this.container;
    }

    public static InventoryType.SlotType getSlotType(InventoryView inventory, int slot) {
        InventoryType.SlotType type = InventoryType.SlotType.CONTAINER;

        if (slot >= 0 && slot < inventory.getTopInventory().getSize()) {
            switch ($SWITCH_TABLE$org$bukkit$event$inventory$InventoryType()[inventory.getType().ordinal()]) {
            case 4:
                if (slot == 2) {
                    type = InventoryType.SlotType.RESULT;
                } else if (slot == 1) {
                    type = InventoryType.SlotType.FUEL;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;

            case 5:
            case 6:
                if (slot == 0) {
                    type = InventoryType.SlotType.RESULT;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;

            case 7:
                type = InventoryType.SlotType.CRAFTING;
                break;

            case 8:
                if (slot == 3) {
                    type = InventoryType.SlotType.FUEL;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }

            case 9:
            case 10:
            case 12:
            default:
                break;

            case 11:
                if (slot == 2) {
                    type = InventoryType.SlotType.RESULT;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;

            case 13:
                if (slot == 2) {
                    type = InventoryType.SlotType.RESULT;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;

            case 14:
                type = InventoryType.SlotType.CRAFTING;
            }
        } else if (slot == -999) {
            type = InventoryType.SlotType.OUTSIDE;
        } else if (inventory.getType() == InventoryType.CRAFTING) {
            if (slot < 9) {
                type = InventoryType.SlotType.ARMOR;
            } else if (slot > 35) {
                type = InventoryType.SlotType.QUICKBAR;
            }
        } else if (slot >= inventory.countSlots() - 9) {
            type = InventoryType.SlotType.QUICKBAR;
        }

        return type;
    }

    static int[] $SWITCH_TABLE$org$bukkit$event$inventory$InventoryType() {
        int[] aint = CraftInventoryView.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType;

        if (CraftInventoryView.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType != null) {
            return aint;
        } else {
            int[] aint1 = new int[InventoryType.values().length];

            try {
                aint1[InventoryType.ANVIL.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[InventoryType.BEACON.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[InventoryType.BREWING.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[InventoryType.CHEST.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[InventoryType.CRAFTING.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[InventoryType.CREATIVE.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[InventoryType.DISPENSER.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[InventoryType.DROPPER.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[InventoryType.ENCHANTING.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[InventoryType.ENDER_CHEST.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[InventoryType.FURNACE.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[InventoryType.HOPPER.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[InventoryType.MERCHANT.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[InventoryType.PLAYER.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[InventoryType.WORKBENCH.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            CraftInventoryView.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType = aint1;
            return aint1;
        }
    }
}
