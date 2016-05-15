package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.util.ChatComponentText;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftContainer extends Container {

    private final InventoryView view;
    private InventoryType cachedType;
    private String cachedTitle;
    private final int cachedSize;
    private static int[] $SWITCH_TABLE$org$bukkit$event$inventory$InventoryType;

    public CraftContainer(InventoryView view, int id) {
        this.view = view;
        this.windowId = id;
        IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
        IInventory bottom = ((CraftInventory) view.getBottomInventory()).getInventory();

        this.cachedType = view.getType();
        this.cachedTitle = view.getTitle();
        this.cachedSize = this.getSize();
        this.setupSlots(top, bottom);
    }

    public CraftContainer(final Inventory inventory, final HumanEntity player, int id) {
        this(new InventoryView() {
            public Inventory getTopInventory() {
                return inventory;
            }

            public Inventory getBottomInventory() {
                return player.getInventory();
            }

            public HumanEntity getPlayer() {
                return player;
            }

            public InventoryType getType() {
                return inventory.getType();
            }
        }, id);
    }

    public InventoryView getBukkitView() {
        return this.view;
    }

    private int getSize() {
        return this.view.getTopInventory().getSize();
    }

    public boolean c(HumanEntity entityhuman) {
        if (this.cachedType == this.view.getType() && this.cachedSize == this.getSize() && this.cachedTitle.equals(this.view.getTitle())) {
            return true;
        } else {
            boolean typeChanged = this.cachedType != this.view.getType();

            this.cachedType = this.view.getType();
            this.cachedTitle = this.view.getTitle();
            if (this.view.getPlayer() instanceof CraftPlayer) {
                CraftPlayer player = (CraftPlayer) this.view.getPlayer();
                String type = getNotchInventoryType(this.cachedType);
                IInventory top = ((CraftInventory) this.view.getTopInventory()).getInventory();
                IInventory bottom = ((CraftInventory) this.view.getBottomInventory()).getInventory();

                this.inventoryItemStacks.clear();
                this.inventorySlots.clear();
                if (typeChanged) {
                    this.setupSlots(top, bottom);
                }

                int size = this.getSize();

                ((EntityPlayerMP) (EntityLivingBase) player.getHandle()).playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.windowId, type, new ChatComponentText(this.cachedTitle), size));
                player.updateInventory();
            }

            return true;
        }
    }

    public static String getNotchInventoryType(InventoryType type) {
        switch ($SWITCH_TABLE$org$bukkit$event$inventory$InventoryType()[type.ordinal()]) {
        case 2:
            return "minecraft:dispenser";

        case 3:
        case 6:
        case 9:
        case 10:
        case 11:
        case 12:
        default:
            return "minecraft:chest";

        case 4:
            return "minecraft:furnace";

        case 5:
            return "minecraft:crafting_table";

        case 7:
            return "minecraft:enchanting_table";

        case 8:
            return "minecraft:brewing_stand";

        case 13:
            return "minecraft:anvil";

        case 14:
            return "minecraft:beacon";

        case 15:
            return "minecraft:hopper";
        }
    }

    private void setupSlots(IInventory top, IInventory bottom) {
        switch ($SWITCH_TABLE$org$bukkit$event$inventory$InventoryType()[this.cachedType.ordinal()]) {
        case 1:
        case 9:
            this.setupChest(top, bottom);
            break;

        case 2:
            this.setupDispenser(top, bottom);

        case 3:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        default:
            break;

        case 4:
            this.setupFurnace(top, bottom);
            break;

        case 5:
        case 6:
            this.setupWorkbench(top, bottom);
            break;

        case 7:
            this.setupEnchanting(top, bottom);
            break;

        case 8:
            this.setupBrewing(top, bottom);
            break;

        case 15:
            this.setupHopper(top, bottom);
        }

    }

    private void setupChest(IInventory top, IInventory bottom) {
        int rows = top.getSizeInventory() / 9;
        int i = (rows - 4) * 18;

        int row;
        int col;

        for (row = 0; row < rows; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(top, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + i));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 161 + i));
        }

    }

    private void setupWorkbench(IInventory top, IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 124, 35));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlotToContainer(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 142));
        }

    }

    private void setupFurnace(IInventory top, IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 56, 17));
        this.addSlotToContainer(new Slot(top, 1, 56, 53));
        this.addSlotToContainer(new Slot(top, 2, 116, 35));

        int col;

        for (int row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 142));
        }

    }

    private void setupDispenser(IInventory top, IInventory bottom) {
        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlotToContainer(new Slot(top, col + row * 3, 61 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 142));
        }

    }

    private void setupEnchanting(IInventory top, IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 25, 47));

        int row;

        for (row = 0; row < 3; ++row) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(bottom, i1 + row * 9 + 9, 8 + i1 * 18, 84 + row * 18));
            }
        }

        for (row = 0; row < 9; ++row) {
            this.addSlotToContainer(new Slot(bottom, row, 8 + row * 18, 142));
        }

    }

    private void setupBrewing(IInventory top, IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 56, 46));
        this.addSlotToContainer(new Slot(top, 1, 79, 53));
        this.addSlotToContainer(new Slot(top, 2, 102, 46));
        this.addSlotToContainer(new Slot(top, 3, 79, 17));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(bottom, i, 8 + i * 18, 142));
        }

    }

    private void setupHopper(IInventory top, IInventory bottom) {
        byte b0 = 51;

        int i;

        for (i = 0; i < top.getSizeInventory(); ++i) {
            this.addSlotToContainer(new Slot(top, i, 44 + i * 18, 20));
        }

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(bottom, i, 8 + i * 18, 58 + b0));
        }

    }

    public boolean a(EntityPlayerMP entity) {
        return true;
    }

    static int[] $SWITCH_TABLE$org$bukkit$event$inventory$InventoryType() {
        int[] aint = CraftContainer.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType;

        if (CraftContainer.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType != null) {
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

            CraftContainer.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType = aint1;
            return aint1;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
