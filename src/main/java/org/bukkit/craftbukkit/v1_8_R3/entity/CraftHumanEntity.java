package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.v1_8_R3.BlockAnvil;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockWorkbench;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMinecartHopper;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.ITileEntityContainer;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.TileEntityBeacon;
import net.minecraft.server.v1_8_R3.TileEntityBrewingStand;
import net.minecraft.server.v1_8_R3.TileEntityDispenser;
import net.minecraft.server.v1_8_R3.TileEntityDropper;
import net.minecraft.server.v1_8_R3.TileEntityEnchantTable;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import net.minecraft.server.v1_8_R3.TileEntityHopper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.inventory.InventoryWrapper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {

    private CraftInventoryPlayer inventory;
    private final CraftInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;
    private static int[] $SWITCH_TABLE$org$bukkit$event$inventory$InventoryType;

    public CraftHumanEntity(CraftServer server, EntityPlayer entity) {
        super(server, (EntityLiving) entity);
        this.mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.inventory);
        this.enderChest = new CraftInventory(entity.getEnderChest());
    }

    public String getName() {
        return this.getHandle().getName();
    }

    public PlayerInventory getInventory() {
        return this.inventory;
    }

    public EntityEquipment getEquipment() {
        return this.inventory;
    }

    public Inventory getEnderChest() {
        return this.enderChest;
    }

    public ItemStack getItemInHand() {
        return this.getInventory().getItemInHand();
    }

    public void setItemInHand(ItemStack item) {
        this.getInventory().setItemInHand(item);
    }

    public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(this.getHandle().inventory.getCarried());
    }

    public void setItemOnCursor(ItemStack item) {
        net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);

        this.getHandle().setCurrentItemOrArmor(this.getHandle(),).setCarried(stack);
        if (this instanceof CraftPlayer) {
            ((EntityPlayer) this.getHandle()).broadcastCarriedItem();
        }

    }

    public boolean isSleeping() {
        return this.getHandle().sleeping;
    }

    public int getSleepTicks() {
        return this.getHandle().sleepTicks;
    }

    public boolean isOp() {
        return this.op;
    }

    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    public void setOp(boolean value) {
        this.op = value;
        this.perm.recalculatePermissions();
    }

    public Set getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    public GameMode getGameMode() {
        return this.mode;
    }

    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        } else {
            this.mode = mode;
        }
    }

    public EntityLiving getHandle() {
        return (EntityLiving) this.entity;
    }

    public void setHandle(EntityHuman entity) {
        super.setHandle((EntityLiving) entity);
        this.inventory = new CraftInventoryPlayer(entity.inventory);
    }

    public String toString() {
        return "CraftHumanEntity{id=" + this.getEntityId() + "name=" + this.getName() + '}';
    }

    public InventoryView getOpenInventory() {
        return this.getHandle().activeContainer.getBukkitView();
    }

    public InventoryView openInventory(Inventory inventory) {
        if (!(this.getHandle() instanceof EntityPlayer)) {
            return null;
        } else {
            EntityPlayer player = (EntityPlayer) this.getHandle();
            InventoryType type = inventory.getType();
            Container formerContainer = this.getHandle().activeContainer;
            Object iinventory = inventory instanceof CraftInventory ? ((CraftInventory) inventory).getInventory() : new InventoryWrapper(inventory);

            switch ($SWITCH_TABLE$org$bukkit$event$inventory$InventoryType()[type.ordinal()]) {
            case 1:
            case 9:
            case 12:
                this.getHandle().openContainer((IInventory) iinventory);
                break;

            case 2:
                if (iinventory instanceof TileEntityDispenser) {
                    this.getHandle().openContainer((TileEntityDispenser) iinventory);
                } else {
                    this.openCustomInventory(inventory, player, "minecraft:dispenser");
                }
                break;

            case 3:
                if (iinventory instanceof TileEntityDropper) {
                    this.getHandle().openContainer((TileEntityDropper) iinventory);
                } else {
                    this.openCustomInventory(inventory, player, "minecraft:dropper");
                }
                break;

            case 4:
                if (iinventory instanceof TileEntityFurnace) {
                    this.getHandle().openContainer((TileEntityFurnace) iinventory);
                } else {
                    this.openCustomInventory(inventory, player, "minecraft:furnace");
                }
                break;

            case 5:
                this.openCustomInventory(inventory, player, "minecraft:crafting_table");
                break;

            case 6:
            case 10:
                throw new IllegalArgumentException("Can\'t open a " + type + " inventory!");

            case 7:
                this.openCustomInventory(inventory, player, "minecraft:enchanting_table");
                break;

            case 8:
                if (iinventory instanceof TileEntityBrewingStand) {
                    this.getHandle().openContainer((TileEntityBrewingStand) iinventory);
                } else {
                    this.openCustomInventory(inventory, player, "minecraft:brewing_stand");
                }

            case 11:
            default:
                break;

            case 13:
                if (iinventory instanceof BlockAnvil.TileEntityContainerAnvil) {
                    this.getHandle().openTileEntity((BlockAnvil.TileEntityContainerAnvil) iinventory);
                } else {
                    this.openCustomInventory(inventory, player, "minecraft:anvil");
                }
                break;

            case 14:
                if (iinventory instanceof TileEntityBeacon) {
                    this.getHandle().openContainer((TileEntityBeacon) iinventory);
                } else {
                    this.openCustomInventory(inventory, player, "minecraft:beacon");
                }
                break;

            case 15:
                if (iinventory instanceof TileEntityHopper) {
                    this.getHandle().openContainer((TileEntityHopper) iinventory);
                } else if (iinventory instanceof EntityMinecartHopper) {
                    this.getHandle().openContainer((EntityMinecartHopper) iinventory);
                } else {
                    this.openCustomInventory(inventory, player, "minecraft:hopper");
                }
            }

            if (this.getHandle().activeContainer == formerContainer) {
                return null;
            } else {
                this.getHandle().activeContainer.checkReachable = false;
                return this.getHandle().activeContainer.getBukkitView();
            }
        }
    }

    private void openCustomInventory(Inventory inventory, EntityPlayer player, String windowType) {
        if (player.playerConnection != null) {
            CraftContainer container = new CraftContainer(inventory, this, player.nextContainerCounter());
            Container container1 = CraftEventFactory.callInventoryOpenEvent(player, container);

            if (container1 != null) {
                String title = container1.getBukkitView().getTitle();
                int size = container1.getBukkitView().getTopInventory().getSize();

                if (windowType.equals("minecraft:crafting_table") || windowType.equals("minecraft:anvil") || windowType.equals("minecraft:enchanting_table")) {
                    size = 0;
                }

                player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container1.windowId, windowType, new ChatComponentText(title), size));
                this.getHandle().activeContainer = container1;
                this.getHandle().activeContainer.addSlotListener(player);
            }
        }
    }

    public InventoryView openWorkbench(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();

            if (block.getType() != Material.WORKBENCH) {
                return null;
            }
        }

        if (location == null) {
            location = this.getLocation();
        }

        this.getHandle().openTileEntity(new BlockWorkbench.TileEntityContainerWorkbench(this.getHandle().world, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        if (force) {
            this.getHandle().activeContainer.checkReachable = false;
        }

        return this.getHandle().activeContainer.getBukkitView();
    }

    public InventoryView openEnchanting(Location location, boolean force) {
        if (!force) {
            Block container = location.getBlock();

            if (container.getType() != Material.ENCHANTMENT_TABLE) {
                return null;
            }
        }

        if (location == null) {
            location = this.getLocation();
        }

        Object container1 = this.getHandle().world.getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

        if (container1 == null && force) {
            container1 = new TileEntityEnchantTable();
        }

        this.getHandle().openTileEntity((ITileEntityContainer) container1);
        if (force) {
            this.getHandle().activeContainer.checkReachable = false;
        }

        return this.getHandle().activeContainer.getBukkitView();
    }

    public void openInventory(InventoryView inventory) {
        if (this.getHandle() instanceof EntityPlayer) {
            if (((EntityPlayer) this.getHandle()).playerConnection != null) {
                if (this.getHandle().activeContainer != this.getHandle().defaultContainer) {
                    ((EntityPlayer) this.getHandle()).playerConnection.a(new PacketPlayInCloseWindow(this.getHandle().activeContainer.windowId));
                }

                EntityPlayer player = (EntityPlayer) this.getHandle();
                Object container;

                if (inventory instanceof CraftInventoryView) {
                    container = ((CraftInventoryView) inventory).getHandle();
                } else {
                    container = new CraftContainer(inventory, player.nextContainerCounter());
                }

                Container container1 = CraftEventFactory.callInventoryOpenEvent(player, (Container) container);

                if (container1 != null) {
                    InventoryType type = inventory.getType();
                    String windowType = CraftContainer.getNotchInventoryType(type);
                    String title = inventory.getTitle();
                    int size = inventory.getTopInventory().getSize();

                    player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container1.windowId, windowType, new ChatComponentText(title), size));
                    player.activeContainer = container1;
                    player.activeContainer.addSlotListener(player);
                }
            }
        }
    }

    public void closeInventory() {
        this.getHandle().closeInventory();
    }

    public boolean isBlocking() {
        return this.getHandle().isBlocking();
    }

    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    public int getExpToLevel() {
        return this.getHandle().getExpToLevel();
    }

    static int[] $SWITCH_TABLE$org$bukkit$event$inventory$InventoryType() {
        int[] aint = CraftHumanEntity.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType;

        if (CraftHumanEntity.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType != null) {
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

            CraftHumanEntity.$SWITCH_TABLE$org$bukkit$event$inventory$InventoryType = aint1;
            return aint1;
        }
    }
}
