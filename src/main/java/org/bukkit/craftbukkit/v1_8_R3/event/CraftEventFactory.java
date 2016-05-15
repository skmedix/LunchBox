package org.bukkit.craftbukkit.v1_8_R3.event;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.server.v1_8_R3.Achievement;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatClickable;
import net.minecraft.server.v1_8_R3.ChatHoverable;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.ChatModifier;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityDamageSource;
import net.minecraft.server.v1_8_R3.EntityDamageSourceIndirect;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.Explosion;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.Slot;
import net.minecraft.server.v1_8_R3.Statistic;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftDamageSource;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.BookMeta;

public class CraftEventFactory {

    public static final DamageSource MELTING = CraftDamageSource.copyOf(DamageSource.BURN);
    public static final DamageSource POISON = CraftDamageSource.copyOf(DamageSource.MAGIC);
    public static Block blockDamage;
    public static Entity entityDamage;
    private static final Function ZERO = Functions.constant(Double.valueOf(-0.0D));
    private static int[] $SWITCH_TABLE$org$bukkit$event$block$Action;
    private static int[] $SWITCH_TABLE$org$bukkit$Material;
    private static int[] $SWITCH_TABLE$org$bukkit$entity$EntityType;
    private static int[] $SWITCH_TABLE$org$bukkit$Statistic;

    private static boolean canBuild(CraftWorld world, Player player, int x, int z) {
        WorldServer worldServer = world.getHandle();
        int spawnSize = Bukkit.getServer().getSpawnRadius();

        if (world.getHandle().dimension != 0) {
            return true;
        } else if (spawnSize <= 0) {
            return true;
        } else if (((CraftServer) Bukkit.getServer()).getHandle().getOPs().isEmpty()) {
            return true;
        } else if (player.isOp()) {
            return true;
        } else {
            BlockPosition chunkcoordinates = worldServer.getSpawn();
            int distanceFromSpawn = Math.max(Math.abs(x - chunkcoordinates.getX()), Math.abs(z - chunkcoordinates.getY()));

            return distanceFromSpawn > spawnSize;
        }
    }

    public static Event callEvent(Event event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockMultiPlaceEvent callBlockMultiPlaceEvent(World world, EntityHuman who, List blockStates, int clickedX, int clickedY, int clickedZ) {
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getServer();
        Player player = who == null ? null : (Player) who.getBukkitEntity();
        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        boolean canBuild = true;

        for (int event = 0; event < blockStates.size(); ++event) {
            if (!canBuild(craftWorld, player, ((BlockState) blockStates.get(event)).getX(), ((BlockState) blockStates.get(event)).getZ())) {
                canBuild = false;
                break;
            }
        }

        BlockMultiPlaceEvent blockmultiplaceevent = new BlockMultiPlaceEvent(blockStates, blockClicked, player.getItemInHand(), player, canBuild);

        craftServer.getPluginManager().callEvent(blockmultiplaceevent);
        return blockmultiplaceevent;
    }

    public static BlockPlaceEvent callBlockPlaceEvent(World world, EntityHuman who, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ) {
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getServer();
        Player player = who == null ? null : (Player) who.getBukkitEntity();
        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        Block placedBlock = replacedBlockState.getBlock();
        boolean canBuild = canBuild(craftWorld, player, placedBlock.getX(), placedBlock.getZ());
        BlockPlaceEvent event = new BlockPlaceEvent(placedBlock, replacedBlockState, blockClicked, player.getItemInHand(), player, canBuild);

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static SpawnerSpawnEvent callSpawnerSpawnEvent(Entity spawnee, int spawnerX, int spawnerY, int spawnerZ) {
        CraftEntity entity = spawnee.getBukkitEntity();
        BlockState state = entity.getWorld().getBlockAt(spawnerX, spawnerY, spawnerZ).getState();

        if (!(state instanceof CreatureSpawner)) {
            state = null;
        }

        SpawnerSpawnEvent event = new SpawnerSpawnEvent(entity, (CreatureSpawner) state);

        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(EntityHuman who, int clickedX, int clickedY, int clickedZ, EnumDirection clickedFace, ItemStack itemInHand) {
        return (PlayerBucketEmptyEvent) getPlayerBucketEvent(false, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, Items.BUCKET);
    }

    public static PlayerBucketFillEvent callPlayerBucketFillEvent(EntityHuman who, int clickedX, int clickedY, int clickedZ, EnumDirection clickedFace, ItemStack itemInHand, Item bucket) {
        return (PlayerBucketFillEvent) getPlayerBucketEvent(true, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, bucket);
    }

    private static PlayerEvent getPlayerBucketEvent(boolean isFilling, EntityHuman who, int clickedX, int clickedY, int clickedZ, EnumDirection clickedFace, ItemStack itemstack, Item item) {
        Player player = who == null ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asNewCraftStack(item);
        Material bucket = CraftMagicNumbers.getMaterial(itemstack.getItem());
        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();
        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);
        Object event = null;

        if (isFilling) {
            event = new PlayerBucketFillEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketFillEvent) event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        } else {
            event = new PlayerBucketEmptyEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketEmptyEvent) event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        }

        craftServer.getPluginManager().callEvent((Event) event);
        return (PlayerEvent) event;
    }

    public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, ItemStack itemstack) {
        if (action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR) {
            throw new IllegalArgumentException(String.format("%s performing %s with %s", new Object[] { who, action, itemstack}));
        } else {
            return callPlayerInteractEvent(who, action, new BlockPosition(0, 256, 0), EnumDirection.SOUTH, itemstack);
        }
    }

    public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, BlockPosition position, EnumDirection direction, ItemStack itemstack) {
        return callPlayerInteractEvent(who, action, position, direction, itemstack, false);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, BlockPosition position, EnumDirection direction, ItemStack itemstack, boolean cancelledBlock) {
        Player player = who == null ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();
        Block blockClicked = craftWorld.getBlockAt(position.getX(), position.getY(), position.getZ());
        BlockFace blockFace = CraftBlock.notchToBlockFace(direction);

        if (position.getY() > 255) {
            blockClicked = null;
            switch ($SWITCH_TABLE$org$bukkit$event$block$Action()[action.ordinal()]) {
            case 1:
                action = Action.LEFT_CLICK_AIR;
                break;

            case 2:
                action = Action.RIGHT_CLICK_AIR;
            }
        }

        if (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0) {
            itemInHand = null;
        }

        PlayerInteractEvent event = new PlayerInteractEvent(player, action, itemInHand, blockClicked, blockFace);

        if (cancelledBlock) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityShootBowEvent callEntityShootBowEvent(EntityLiving who, ItemStack itemstack, EntityArrow entityArrow, float force) {
        LivingEntity shooter = (LivingEntity) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        Arrow arrow = (Arrow) entityArrow.getBukkitEntity();

        if (itemInHand != null && (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0)) {
            itemInHand = null;
        }

        EntityShootBowEvent event = new EntityShootBowEvent(shooter, itemInHand, arrow, force);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockDamageEvent callBlockDamageEvent(EntityHuman who, int x, int y, int z, ItemStack itemstack, boolean instaBreak) {
        Player player = who == null ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();
        Block blockClicked = craftWorld.getBlockAt(x, y, z);
        BlockDamageEvent event = new BlockDamageEvent(player, blockClicked, itemInHand, instaBreak);

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static CreatureSpawnEvent callCreatureSpawnEvent(EntityLiving entityliving, CreatureSpawnEvent.SpawnReason spawnReason) {
        LivingEntity entity = (LivingEntity) entityliving.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();
        CreatureSpawnEvent event = new CreatureSpawnEvent(entity, spawnReason);

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTameEvent callEntityTameEvent(EntityInsentient entity, EntityHuman tamer) {
        CraftEntity bukkitEntity = entity.getBukkitEntity();
        CraftHumanEntity bukkitTamer = tamer != null ? tamer.getBukkitEntity() : null;
        CraftServer craftServer = (CraftServer) bukkitEntity.getServer();

        entity.persistent = true;
        EntityTameEvent event = new EntityTameEvent((LivingEntity) bukkitEntity, bukkitTamer);

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static ItemSpawnEvent callItemSpawnEvent(EntityItem entityitem) {
        org.bukkit.entity.Item entity = (org.bukkit.entity.Item) entityitem.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();
        ItemSpawnEvent event = new ItemSpawnEvent(entity, entity.getLocation());

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static ItemDespawnEvent callItemDespawnEvent(EntityItem entityitem) {
        org.bukkit.entity.Item entity = (org.bukkit.entity.Item) entityitem.getBukkitEntity();
        ItemDespawnEvent event = new ItemDespawnEvent(entity, entity.getLocation());

        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static ItemMergeEvent callItemMergeEvent(EntityItem merging, EntityItem mergingWith) {
        org.bukkit.entity.Item entityMerging = (org.bukkit.entity.Item) merging.getBukkitEntity();
        org.bukkit.entity.Item entityMergingWith = (org.bukkit.entity.Item) mergingWith.getBukkitEntity();
        ItemMergeEvent event = new ItemMergeEvent(entityMerging, entityMergingWith);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PotionSplashEvent callPotionSplashEvent(EntityPotion potion, Map affectedEntities) {
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();
        PotionSplashEvent event = new PotionSplashEvent(thrownPotion, affectedEntities);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockFadeEvent callBlockFadeEvent(Block block, net.minecraft.server.v1_8_R3.Block type) {
        BlockState state = block.getState();

        state.setTypeId(net.minecraft.server.v1_8_R3.Block.getId(type));
        BlockFadeEvent event = new BlockFadeEvent(block, state);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static void handleBlockSpreadEvent(Block block, Block source, net.minecraft.server.v1_8_R3.Block type, int data) {
        BlockState state = block.getState();

        state.setTypeId(net.minecraft.server.v1_8_R3.Block.getId(type));
        state.setRawData((byte) data);
        BlockSpreadEvent event = new BlockSpreadEvent(block, source, state);

        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            state.update(true);
        }

    }

    public static EntityDeathEvent callEntityDeathEvent(EntityLiving victim) {
        return callEntityDeathEvent(victim, new ArrayList(0));
    }

    public static EntityDeathEvent callEntityDeathEvent(EntityLiving victim, List drops) {
        CraftLivingEntity entity = (CraftLivingEntity) victim.getBukkitEntity();
        EntityDeathEvent event = new EntityDeathEvent(entity, drops, victim.getExpReward());
        CraftWorld world = (CraftWorld) entity.getWorld();

        Bukkit.getServer().getPluginManager().callEvent(event);
        victim.expToDrop = event.getDroppedExp();
        Iterator iterator = event.getDrops().iterator();

        while (iterator.hasNext()) {
            org.bukkit.inventory.ItemStack stack = (org.bukkit.inventory.ItemStack) iterator.next();

            if (stack != null && stack.getType() != Material.AIR && stack.getAmount() != 0) {
                world.dropItemNaturally(entity.getLocation(), stack);
            }
        }

        return event;
    }

    public static PlayerDeathEvent callPlayerDeathEvent(EntityPlayer victim, List drops, String deathMessage, boolean keepInventory) {
        CraftPlayer entity = victim.getBukkitEntity();
        PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, victim.getExpReward(), 0, deathMessage);

        event.setKeepInventory(keepInventory);
        org.bukkit.World world = entity.getWorld();

        Bukkit.getServer().getPluginManager().callEvent(event);
        victim.keepLevel = event.getKeepLevel();
        victim.newLevel = event.getNewLevel();
        victim.newTotalExp = event.getNewTotalExp();
        victim.expToDrop = event.getDroppedExp();
        victim.newExp = event.getNewExp();
        if (event.getKeepInventory()) {
            return event;
        } else {
            Iterator iterator = event.getDrops().iterator();

            while (iterator.hasNext()) {
                org.bukkit.inventory.ItemStack stack = (org.bukkit.inventory.ItemStack) iterator.next();

                if (stack != null && stack.getType() != Material.AIR) {
                    world.dropItemNaturally(entity.getLocation(), stack);
                }
            }

            return event;
        }
    }

    public static ServerListPingEvent callServerListPingEvent(Server craftServer, InetAddress address, String motd, int numPlayers, int maxPlayers) {
        ServerListPingEvent event = new ServerListPingEvent(address, motd, numPlayers, maxPlayers);

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    private static EntityDamageEvent handleEntityDamageEvent(Entity entity, DamageSource source, Map modifiers, Map modifierFunctions) {
        EntityDamageEvent.DamageCause cause;

        if (source.isExplosion()) {
            Entity damager3 = CraftEventFactory.entityDamage;

            CraftEventFactory.entityDamage = null;
            Object event1;

            if (damager3 == null) {
                event1 = new EntityDamageByBlockEvent((Block) null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, modifiers, modifierFunctions);
            } else if (entity instanceof EntityEnderDragon && ((EntityEnderDragon) entity).target == damager3) {
                event1 = new EntityDamageEvent(entity.getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, modifiers, modifierFunctions);
            } else {
                if (damager3 instanceof TNTPrimed) {
                    cause = EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
                } else {
                    cause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
                }

                event1 = new EntityDamageByEntityEvent(damager3.getBukkitEntity(), entity.getBukkitEntity(), cause, modifiers, modifierFunctions);
            }

            callEvent((Event) event1);
            if (!((EntityDamageEvent) event1).isCancelled()) {
                ((EntityDamageEvent) event1).getEntity().setLastDamageCause((EntityDamageEvent) event1);
            }

            return (EntityDamageEvent) event1;
        } else if (source instanceof EntityDamageSource) {
            Entity cause2 = source.getEntity();
            EntityDamageEvent.DamageCause damager2 = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

            if (source instanceof EntityDamageSourceIndirect) {
                cause2 = ((EntityDamageSourceIndirect) source).getProximateDamageSource();
                if (cause2.getBukkitEntity() instanceof ThrownPotion) {
                    damager2 = EntityDamageEvent.DamageCause.MAGIC;
                } else if (cause2.getBukkitEntity() instanceof Projectile) {
                    damager2 = EntityDamageEvent.DamageCause.PROJECTILE;
                }
            } else if ("thorns".equals(source.translationIndex)) {
                damager2 = EntityDamageEvent.DamageCause.THORNS;
            }

            return callEntityDamageEvent(cause2, entity, damager2, modifiers, modifierFunctions);
        } else {
            EntityDamageEvent cause1;

            if (source == DamageSource.OUT_OF_WORLD) {
                cause1 = (EntityDamageEvent) callEvent(new EntityDamageByBlockEvent((Block) null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, modifiers, modifierFunctions));
                if (!cause1.isCancelled()) {
                    cause1.getEntity().setLastDamageCause(cause1);
                }

                return cause1;
            } else if (source == DamageSource.LAVA) {
                cause1 = (EntityDamageEvent) callEvent(new EntityDamageByBlockEvent((Block) null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.LAVA, modifiers, modifierFunctions));
                if (!cause1.isCancelled()) {
                    cause1.getEntity().setLastDamageCause(cause1);
                }

                return cause1;
            } else {
                EntityDamageEvent event;

                if (CraftEventFactory.blockDamage != null) {
                    cause = null;
                    Block damager1 = CraftEventFactory.blockDamage;

                    CraftEventFactory.blockDamage = null;
                    if (source == DamageSource.CACTUS) {
                        cause = EntityDamageEvent.DamageCause.CONTACT;
                        event = (EntityDamageEvent) callEvent(new EntityDamageByBlockEvent(damager1, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
                        if (!event.isCancelled()) {
                            event.getEntity().setLastDamageCause(event);
                        }

                        return event;
                    } else {
                        throw new RuntimeException(String.format("Unhandled damage of %s by %s from %s", new Object[] { entity, damager1, source.translationIndex}));
                    }
                } else if (CraftEventFactory.entityDamage == null) {
                    cause = null;
                    if (source == DamageSource.FIRE) {
                        cause = EntityDamageEvent.DamageCause.FIRE;
                    } else if (source == DamageSource.STARVE) {
                        cause = EntityDamageEvent.DamageCause.STARVATION;
                    } else if (source == DamageSource.WITHER) {
                        cause = EntityDamageEvent.DamageCause.WITHER;
                    } else if (source == DamageSource.STUCK) {
                        cause = EntityDamageEvent.DamageCause.SUFFOCATION;
                    } else if (source == DamageSource.DROWN) {
                        cause = EntityDamageEvent.DamageCause.DROWNING;
                    } else if (source == DamageSource.BURN) {
                        cause = EntityDamageEvent.DamageCause.FIRE_TICK;
                    } else if (source == CraftEventFactory.MELTING) {
                        cause = EntityDamageEvent.DamageCause.MELTING;
                    } else if (source == CraftEventFactory.POISON) {
                        cause = EntityDamageEvent.DamageCause.POISON;
                    } else if (source == DamageSource.MAGIC) {
                        cause = EntityDamageEvent.DamageCause.MAGIC;
                    } else if (source == DamageSource.FALL) {
                        cause = EntityDamageEvent.DamageCause.FALL;
                    } else if (source == DamageSource.GENERIC) {
                        return new EntityDamageEvent(entity.getBukkitEntity(), (EntityDamageEvent.DamageCause) null, modifiers, modifierFunctions);
                    }

                    if (cause != null) {
                        return callEntityDamageEvent((Entity) null, entity, cause, modifiers, modifierFunctions);
                    } else {
                        throw new RuntimeException(String.format("Unhandled damage of %s from %s", new Object[] { entity, source.translationIndex}));
                    }
                } else {
                    cause = null;
                    CraftEntity damager = CraftEventFactory.entityDamage.getBukkitEntity();

                    CraftEventFactory.entityDamage = null;
                    if (source != DamageSource.ANVIL && source != DamageSource.FALLING_BLOCK) {
                        if (damager instanceof LightningStrike) {
                            cause = EntityDamageEvent.DamageCause.LIGHTNING;
                        } else {
                            if (source != DamageSource.FALL) {
                                throw new RuntimeException(String.format("Unhandled damage of %s by %s from %s", new Object[] { entity, damager.getHandle(), source.translationIndex}));
                            }

                            cause = EntityDamageEvent.DamageCause.FALL;
                        }
                    } else {
                        cause = EntityDamageEvent.DamageCause.FALLING_BLOCK;
                    }

                    event = (EntityDamageEvent) callEvent(new EntityDamageByEntityEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
                    if (!event.isCancelled()) {
                        event.getEntity().setLastDamageCause(event);
                    }

                    return event;
                }
            }
        }
    }

    private static EntityDamageEvent callEntityDamageEvent(Entity damager, Entity damagee, EntityDamageEvent.DamageCause cause, Map modifiers, Map modifierFunctions) {
        Object event;

        if (damager != null) {
            event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
        } else {
            event = new EntityDamageEvent(damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
        }

        callEvent((Event) event);
        if (!((EntityDamageEvent) event).isCancelled()) {
            ((EntityDamageEvent) event).getEntity().setLastDamageCause((EntityDamageEvent) event);
        }

        return (EntityDamageEvent) event;
    }

    public static EntityDamageEvent handleLivingEntityDamageEvent(Entity damagee, DamageSource source, double rawDamage, double hardHatModifier, double blockingModifier, double armorModifier, double resistanceModifier, double magicModifier, double absorptionModifier, Function hardHat, Function blocking, Function armor, Function resistance, Function magic, Function absorption) {
        EnumMap modifiers = new EnumMap(EntityDamageEvent.DamageModifier.class);
        EnumMap modifierFunctions = new EnumMap(EntityDamageEvent.DamageModifier.class);

        modifiers.put(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(rawDamage));
        modifierFunctions.put(EntityDamageEvent.DamageModifier.BASE, CraftEventFactory.ZERO);
        if (source == DamageSource.FALLING_BLOCK || source == DamageSource.ANVIL) {
            modifiers.put(EntityDamageEvent.DamageModifier.HARD_HAT, Double.valueOf(hardHatModifier));
            modifierFunctions.put(EntityDamageEvent.DamageModifier.HARD_HAT, hardHat);
        }

        if (damagee instanceof EntityHuman) {
            modifiers.put(EntityDamageEvent.DamageModifier.BLOCKING, Double.valueOf(blockingModifier));
            modifierFunctions.put(EntityDamageEvent.DamageModifier.BLOCKING, blocking);
        }

        modifiers.put(EntityDamageEvent.DamageModifier.ARMOR, Double.valueOf(armorModifier));
        modifierFunctions.put(EntityDamageEvent.DamageModifier.ARMOR, armor);
        modifiers.put(EntityDamageEvent.DamageModifier.RESISTANCE, Double.valueOf(resistanceModifier));
        modifierFunctions.put(EntityDamageEvent.DamageModifier.RESISTANCE, resistance);
        modifiers.put(EntityDamageEvent.DamageModifier.MAGIC, Double.valueOf(magicModifier));
        modifierFunctions.put(EntityDamageEvent.DamageModifier.MAGIC, magic);
        modifiers.put(EntityDamageEvent.DamageModifier.ABSORPTION, Double.valueOf(absorptionModifier));
        modifierFunctions.put(EntityDamageEvent.DamageModifier.ABSORPTION, absorption);
        return handleEntityDamageEvent(damagee, source, modifiers, modifierFunctions);
    }

    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage) {
        return handleNonLivingEntityDamageEvent(entity, source, damage, true);
    }

    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage, boolean cancelOnZeroDamage) {
        if (entity instanceof EntityEnderCrystal && !(source instanceof EntityDamageSource)) {
            return false;
        } else {
            EnumMap modifiers = new EnumMap(EntityDamageEvent.DamageModifier.class);
            EnumMap functions = new EnumMap(EntityDamageEvent.DamageModifier.class);

            modifiers.put(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(damage));
            functions.put(EntityDamageEvent.DamageModifier.BASE, CraftEventFactory.ZERO);
            EntityDamageEvent event = handleEntityDamageEvent(entity, source, modifiers, functions);

            return event == null ? false : event.isCancelled() || cancelOnZeroDamage && event.getDamage() == 0.0D;
        }
    }

    public static PlayerLevelChangeEvent callPlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(player, oldLevel, newLevel);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerExpChangeEvent callPlayerExpChangeEvent(EntityHuman entity, int expAmount) {
        Player player = (Player) entity.getBukkitEntity();
        PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, expAmount);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static void handleBlockGrowEvent(World world, int x, int y, int z, net.minecraft.server.v1_8_R3.Block type, int data) {
        Block block = world.getWorld().getBlockAt(x, y, z);
        CraftBlockState state = (CraftBlockState) block.getState();

        state.setTypeId(net.minecraft.server.v1_8_R3.Block.getId(type));
        state.setRawData((byte) data);
        BlockGrowEvent event = new BlockGrowEvent(block, state);

        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            state.update(true);
        }

    }

    public static FoodLevelChangeEvent callFoodLevelChangeEvent(EntityHuman entity, int level) {
        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entity.getBukkitEntity(), level);

        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PigZapEvent callPigZapEvent(Entity pig, Entity lightning, Entity pigzombie) {
        PigZapEvent event = new PigZapEvent((Pig) pig.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), (PigZombie) pigzombie.getBukkitEntity());

        pig.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static HorseJumpEvent callHorseJumpEvent(Entity horse, float power) {
        HorseJumpEvent event = new HorseJumpEvent((Horse) horse.getBukkitEntity(), power);

        horse.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, Block block, Material material) {
        return callEntityChangeBlockEvent(entity, block, material, 0);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, Block block, Material material) {
        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, Block block, Material material, boolean cancelled) {
        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0, cancelled);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, int x, int y, int z, net.minecraft.server.v1_8_R3.Block type, int data) {
        Block block = entity.world.getWorld().getBlockAt(x, y, z);
        Material material = CraftMagicNumbers.getMaterial(type);

        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, data);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, Block block, Material material, int data) {
        return callEntityChangeBlockEvent(entity, block, material, data, false);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, Block block, Material material, int data, boolean cancelled) {
        EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity, block, material, (byte) data);

        event.setCancelled(cancelled);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static CreeperPowerEvent callCreeperPowerEvent(Entity creeper, Entity lightning, CreeperPowerEvent.PowerCause cause) {
        CreeperPowerEvent event = new CreeperPowerEvent((Creeper) creeper.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), cause);

        creeper.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetEvent callEntityTargetEvent(Entity entity, Entity target, EntityTargetEvent.TargetReason reason) {
        EntityTargetEvent event = new EntityTargetEvent(entity.getBukkitEntity(), target == null ? null : target.getBukkitEntity(), reason);

        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetLivingEntityEvent callEntityTargetLivingEvent(Entity entity, EntityLiving target, EntityTargetEvent.TargetReason reason) {
        EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(entity.getBukkitEntity(), (LivingEntity) target.getBukkitEntity(), reason);

        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreakDoorEvent callEntityBreakDoorEvent(Entity entity, int x, int y, int z) {
        CraftEntity entity1 = entity.getBukkitEntity();
        Block block = entity1.getWorld().getBlockAt(x, y, z);
        EntityBreakDoorEvent event = new EntityBreakDoorEvent((LivingEntity) entity1, block);

        entity1.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static Container callInventoryOpenEvent(EntityPlayer player, Container container) {
        return callInventoryOpenEvent(player, container, false);
    }

    public static Container callInventoryOpenEvent(EntityPlayer player, Container container, boolean cancelled) {
        if (player.activeContainer != player.defaultContainer) {
            player.playerConnection.a(new PacketPlayInCloseWindow(player.activeContainer.windowId));
        }

        CraftServer server = player.world.getServer();
        CraftPlayer craftPlayer = player.getBukkitEntity();

        player.activeContainer.transferTo(container, craftPlayer);
        InventoryOpenEvent event = new InventoryOpenEvent(container.getBukkitView());

        event.setCancelled(cancelled);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            container.transferTo(player.activeContainer, craftPlayer);
            return null;
        } else {
            return container;
        }
    }

    public static ItemStack callPreCraftEvent(InventoryCrafting matrix, InventoryCraftResult resultInv, ItemStack result, InventoryView lastCraftView, boolean isRepair) {
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(matrix, resultInv);

        inventory.setResult(CraftItemStack.asCraftMirror(result));
        PrepareItemCraftEvent event = new PrepareItemCraftEvent(inventory, lastCraftView, isRepair);

        Bukkit.getPluginManager().callEvent(event);
        org.bukkit.inventory.ItemStack bitem = event.getInventory().getResult();

        return CraftItemStack.asNMSCopy(bitem);
    }

    public static ProjectileLaunchEvent callProjectileLaunchEvent(Entity entity) {
        Projectile bukkitEntity = (Projectile) entity.getBukkitEntity();
        ProjectileLaunchEvent event = new ProjectileLaunchEvent(bukkitEntity);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static ProjectileHitEvent callProjectileHitEvent(Entity entity) {
        ProjectileHitEvent event = new ProjectileHitEvent((Projectile) entity.getBukkitEntity());

        entity.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static ExpBottleEvent callExpBottleEvent(Entity entity, int exp) {
        ThrownExpBottle bottle = (ThrownExpBottle) entity.getBukkitEntity();
        ExpBottleEvent event = new ExpBottleEvent(bottle, exp);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockRedstoneEvent callRedstoneChange(World world, int x, int y, int z, int oldCurrent, int newCurrent) {
        BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(x, y, z), oldCurrent, newCurrent);

        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static NotePlayEvent callNotePlayEvent(World world, int x, int y, int z, byte instrument, byte note) {
        NotePlayEvent event = new NotePlayEvent(world.getWorld().getBlockAt(x, y, z), Instrument.getByType(instrument), new Note(note));

        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void callPlayerItemBreakEvent(EntityHuman human, ItemStack brokenItem) {
        CraftItemStack item = CraftItemStack.asCraftMirror(brokenItem);
        PlayerItemBreakEvent event = new PlayerItemBreakEvent((Player) human.getBukkitEntity(), item);

        Bukkit.getPluginManager().callEvent(event);
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, int igniterX, int igniterY, int igniterZ) {
        CraftWorld bukkitWorld = world.getWorld();
        Block igniter = bukkitWorld.getBlockAt(igniterX, igniterY, igniterZ);
        BlockIgniteEvent.IgniteCause cause;

        switch ($SWITCH_TABLE$org$bukkit$Material()[igniter.getType().ordinal()]) {
        case 11:
        case 12:
            cause = BlockIgniteEvent.IgniteCause.LAVA;
            break;

        case 24:
            cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
            break;

        case 52:
        default:
            cause = BlockIgniteEvent.IgniteCause.SPREAD;
        }

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, igniter);

        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, Entity igniter) {
        CraftWorld bukkitWorld = world.getWorld();
        CraftEntity bukkitIgniter = igniter.getBukkitEntity();
        BlockIgniteEvent.IgniteCause cause;

        switch ($SWITCH_TABLE$org$bukkit$entity$EntityType()[bukkitIgniter.getType().ordinal()]) {
        case 7:
        case 8:
            cause = BlockIgniteEvent.IgniteCause.FIREBALL;
            break;

        case 58:
            cause = BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL;
            break;

        case 62:
            cause = BlockIgniteEvent.IgniteCause.LIGHTNING;
            break;

        default:
            cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
        }

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, bukkitIgniter);

        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, Explosion explosion) {
        CraftWorld bukkitWorld = world.getWorld();
        CraftEntity igniter = explosion.source == null ? null : explosion.source.getBukkitEntity();
        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), BlockIgniteEvent.IgniteCause.EXPLOSION, igniter);

        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, BlockIgniteEvent.IgniteCause cause, Entity igniter) {
        BlockIgniteEvent event = new BlockIgniteEvent(world.getWorld().getBlockAt(x, y, z), cause, igniter.getBukkitEntity());

        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void handleInventoryCloseEvent(EntityHuman human) {
        InventoryCloseEvent event = new InventoryCloseEvent(human.activeContainer.getBukkitView());

        human.world.getServer().getPluginManager().callEvent(event);
        human.activeContainer.transferTo(human.defaultContainer, human.getBukkitEntity());
    }

    public static void handleEditBookEvent(EntityPlayer player, ItemStack newBookItem) {
        int itemInHandIndex = player.inventory.itemInHandIndex;
        PlayerEditBookEvent editBookEvent = new PlayerEditBookEvent(player.getBukkitEntity(), player.inventory.itemInHandIndex, (BookMeta) CraftItemStack.getItemMeta(player.inventory.getItemInHand()), (BookMeta) CraftItemStack.getItemMeta(newBookItem), newBookItem.getItem() == Items.WRITTEN_BOOK);

        player.world.getServer().getPluginManager().callEvent(editBookEvent);
        ItemStack itemInHand = player.inventory.getItem(itemInHandIndex);

        if (itemInHand != null && itemInHand.getItem() == Items.WRITABLE_BOOK) {
            if (!editBookEvent.isCancelled()) {
                if (editBookEvent.isSigning()) {
                    itemInHand.setItem(Items.WRITTEN_BOOK);
                }

                CraftMetaBook slot = (CraftMetaBook) editBookEvent.getNewBookMeta();
                List pages = slot.pages;

                for (int i = 0; i < pages.size(); ++i) {
                    pages.set(i, stripEvents((IChatBaseComponent) pages.get(i)));
                }

                CraftItemStack.setItemMeta(itemInHand, slot);
            }

            Slot slot = player.activeContainer.getSlot(player.inventory, itemInHandIndex);

            player.playerConnection.sendPacket(new PacketPlayOutSetSlot(player.activeContainer.windowId, slot.rawSlotIndex, itemInHand));
        }

    }

    private static IChatBaseComponent stripEvents(IChatBaseComponent c) {
        ChatModifier modi = c.getChatModifier();

        if (modi != null) {
            modi.setChatClickable((ChatClickable) null);
            modi.setChatHoverable((ChatHoverable) null);
        }

        c.setChatModifier(modi);
        if (c instanceof ChatMessage) {
            ChatMessage ls = (ChatMessage) c;
            Object[] i = ls.j();

            for (int i1 = 0; i1 < i.length; ++i1) {
                Object o = i[i1];

                if (o instanceof IChatBaseComponent) {
                    i[i1] = stripEvents((IChatBaseComponent) o);
                }
            }
        }

        List list = c.a();

        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                list.set(i, stripEvents((IChatBaseComponent) list.get(i)));
            }
        }

        return c;
    }

    public static PlayerUnleashEntityEvent callPlayerUnleashEntityEvent(EntityInsentient entity, EntityHuman player) {
        PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(entity.getBukkitEntity(), (Player) player.getBukkitEntity());

        entity.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(EntityInsentient entity, Entity leashHolder, EntityHuman player) {
        PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player) player.getBukkitEntity());

        entity.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static Cancellable handleStatisticsIncrease(EntityHuman entityHuman, Statistic statistic, int current, int incrementation) {
        CraftPlayer player = ((EntityPlayer) entityHuman).getBukkitEntity();
        Object event;

        if (statistic instanceof Achievement) {
            if (current != 0) {
                return null;
            }

            event = new PlayerAchievementAwardedEvent(player, CraftStatistic.getBukkitAchievement((Achievement) statistic));
        } else {
            org.bukkit.Statistic stat = CraftStatistic.getBukkitStatistic(statistic);

            if (stat == null) {
                System.err.println("Unhandled statistic: " + statistic);
                return null;
            }

            switch ($SWITCH_TABLE$org$bukkit$Statistic()[stat.ordinal()]) {
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 32:
                return null;

            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            default:
                if (stat.getType() == org.bukkit.Statistic.UNTYPED) {
                    event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation);
                } else if (stat.getType() == org.bukkit.Statistic.ENTITY) {
                    EntityType material = CraftStatistic.getEntityTypeFromStatistic(statistic);

                    event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, material);
                } else {
                    Material material1 = CraftStatistic.getMaterialFromStatistic(statistic);

                    event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, material1);
                }
            }
        }

        entityHuman.world.getServer().getPluginManager().callEvent((Event) event);
        return (Cancellable) event;
    }

    public static FireworkExplodeEvent callFireworkExplodeEvent(EntityFireworks firework) {
        FireworkExplodeEvent event = new FireworkExplodeEvent((Firework) firework.getBukkitEntity());

        firework.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    static int[] $SWITCH_TABLE$org$bukkit$event$block$Action() {
        int[] aint = CraftEventFactory.$SWITCH_TABLE$org$bukkit$event$block$Action;

        if (CraftEventFactory.$SWITCH_TABLE$org$bukkit$event$block$Action != null) {
            return aint;
        } else {
            int[] aint1 = new int[Action.values().length];

            try {
                aint1[Action.LEFT_CLICK_AIR.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[Action.LEFT_CLICK_BLOCK.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[Action.PHYSICAL.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[Action.RIGHT_CLICK_AIR.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[Action.RIGHT_CLICK_BLOCK.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            CraftEventFactory.$SWITCH_TABLE$org$bukkit$event$block$Action = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$Material() {
        int[] aint = CraftEventFactory.$SWITCH_TABLE$org$bukkit$Material;

        if (CraftEventFactory.$SWITCH_TABLE$org$bukkit$Material != null) {
            return aint;
        } else {
            int[] aint1 = new int[Material.values().length];

            try {
                aint1[Material.ACACIA_DOOR.ordinal()] = 197;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[Material.ACACIA_DOOR_ITEM.ordinal()] = 372;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[Material.ACACIA_FENCE.ordinal()] = 193;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[Material.ACACIA_FENCE_GATE.ordinal()] = 188;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[Material.ACACIA_STAIRS.ordinal()] = 164;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[Material.ACTIVATOR_RAIL.ordinal()] = 158;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[Material.AIR.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[Material.ANVIL.ordinal()] = 146;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[Material.APPLE.ordinal()] = 203;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[Material.ARMOR_STAND.ordinal()] = 359;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[Material.ARROW.ordinal()] = 205;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[Material.BAKED_POTATO.ordinal()] = 336;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[Material.BANNER.ordinal()] = 368;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[Material.BARRIER.ordinal()] = 167;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[Material.BEACON.ordinal()] = 139;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[Material.BED.ordinal()] = 298;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[Material.BEDROCK.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[Material.BED_BLOCK.ordinal()] = 27;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[Material.BIRCH_DOOR.ordinal()] = 195;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[Material.BIRCH_DOOR_ITEM.ordinal()] = 370;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[Material.BIRCH_FENCE.ordinal()] = 190;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[Material.BIRCH_FENCE_GATE.ordinal()] = 185;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[Material.BIRCH_WOOD_STAIRS.ordinal()] = 136;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[Material.BLAZE_POWDER.ordinal()] = 320;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[Material.BLAZE_ROD.ordinal()] = 312;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[Material.BOAT.ordinal()] = 276;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            try {
                aint1[Material.BONE.ordinal()] = 295;
            } catch (NoSuchFieldError nosuchfielderror26) {
                ;
            }

            try {
                aint1[Material.BOOK.ordinal()] = 283;
            } catch (NoSuchFieldError nosuchfielderror27) {
                ;
            }

            try {
                aint1[Material.BOOKSHELF.ordinal()] = 48;
            } catch (NoSuchFieldError nosuchfielderror28) {
                ;
            }

            try {
                aint1[Material.BOOK_AND_QUILL.ordinal()] = 329;
            } catch (NoSuchFieldError nosuchfielderror29) {
                ;
            }

            try {
                aint1[Material.BOW.ordinal()] = 204;
            } catch (NoSuchFieldError nosuchfielderror30) {
                ;
            }

            try {
                aint1[Material.BOWL.ordinal()] = 224;
            } catch (NoSuchFieldError nosuchfielderror31) {
                ;
            }

            try {
                aint1[Material.BREAD.ordinal()] = 240;
            } catch (NoSuchFieldError nosuchfielderror32) {
                ;
            }

            try {
                aint1[Material.BREWING_STAND.ordinal()] = 118;
            } catch (NoSuchFieldError nosuchfielderror33) {
                ;
            }

            try {
                aint1[Material.BREWING_STAND_ITEM.ordinal()] = 322;
            } catch (NoSuchFieldError nosuchfielderror34) {
                ;
            }

            try {
                aint1[Material.BRICK.ordinal()] = 46;
            } catch (NoSuchFieldError nosuchfielderror35) {
                ;
            }

            try {
                aint1[Material.BRICK_STAIRS.ordinal()] = 109;
            } catch (NoSuchFieldError nosuchfielderror36) {
                ;
            }

            try {
                aint1[Material.BROWN_MUSHROOM.ordinal()] = 40;
            } catch (NoSuchFieldError nosuchfielderror37) {
                ;
            }

            try {
                aint1[Material.BUCKET.ordinal()] = 268;
            } catch (NoSuchFieldError nosuchfielderror38) {
                ;
            }

            try {
                aint1[Material.BURNING_FURNACE.ordinal()] = 63;
            } catch (NoSuchFieldError nosuchfielderror39) {
                ;
            }

            try {
                aint1[Material.CACTUS.ordinal()] = 82;
            } catch (NoSuchFieldError nosuchfielderror40) {
                ;
            }

            try {
                aint1[Material.CAKE.ordinal()] = 297;
            } catch (NoSuchFieldError nosuchfielderror41) {
                ;
            }

            try {
                aint1[Material.CAKE_BLOCK.ordinal()] = 93;
            } catch (NoSuchFieldError nosuchfielderror42) {
                ;
            }

            try {
                aint1[Material.CARPET.ordinal()] = 172;
            } catch (NoSuchFieldError nosuchfielderror43) {
                ;
            }

            try {
                aint1[Material.CARROT.ordinal()] = 142;
            } catch (NoSuchFieldError nosuchfielderror44) {
                ;
            }

            try {
                aint1[Material.CARROT_ITEM.ordinal()] = 334;
            } catch (NoSuchFieldError nosuchfielderror45) {
                ;
            }

            try {
                aint1[Material.CARROT_STICK.ordinal()] = 341;
            } catch (NoSuchFieldError nosuchfielderror46) {
                ;
            }

            try {
                aint1[Material.CAULDRON.ordinal()] = 119;
            } catch (NoSuchFieldError nosuchfielderror47) {
                ;
            }

            try {
                aint1[Material.CAULDRON_ITEM.ordinal()] = 323;
            } catch (NoSuchFieldError nosuchfielderror48) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_BOOTS.ordinal()] = 248;
            } catch (NoSuchFieldError nosuchfielderror49) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_CHESTPLATE.ordinal()] = 246;
            } catch (NoSuchFieldError nosuchfielderror50) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_HELMET.ordinal()] = 245;
            } catch (NoSuchFieldError nosuchfielderror51) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_LEGGINGS.ordinal()] = 247;
            } catch (NoSuchFieldError nosuchfielderror52) {
                ;
            }

            try {
                aint1[Material.CHEST.ordinal()] = 55;
            } catch (NoSuchFieldError nosuchfielderror53) {
                ;
            }

            try {
                aint1[Material.CLAY.ordinal()] = 83;
            } catch (NoSuchFieldError nosuchfielderror54) {
                ;
            }

            try {
                aint1[Material.CLAY_BALL.ordinal()] = 280;
            } catch (NoSuchFieldError nosuchfielderror55) {
                ;
            }

            try {
                aint1[Material.CLAY_BRICK.ordinal()] = 279;
            } catch (NoSuchFieldError nosuchfielderror56) {
                ;
            }

            try {
                aint1[Material.COAL.ordinal()] = 206;
            } catch (NoSuchFieldError nosuchfielderror57) {
                ;
            }

            try {
                aint1[Material.COAL_BLOCK.ordinal()] = 174;
            } catch (NoSuchFieldError nosuchfielderror58) {
                ;
            }

            try {
                aint1[Material.COAL_ORE.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror59) {
                ;
            }

            try {
                aint1[Material.COBBLESTONE.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror60) {
                ;
            }

            try {
                aint1[Material.COBBLESTONE_STAIRS.ordinal()] = 68;
            } catch (NoSuchFieldError nosuchfielderror61) {
                ;
            }

            try {
                aint1[Material.COBBLE_WALL.ordinal()] = 140;
            } catch (NoSuchFieldError nosuchfielderror62) {
                ;
            }

            try {
                aint1[Material.COCOA.ordinal()] = 128;
            } catch (NoSuchFieldError nosuchfielderror63) {
                ;
            }

            try {
                aint1[Material.COMMAND.ordinal()] = 138;
            } catch (NoSuchFieldError nosuchfielderror64) {
                ;
            }

            try {
                aint1[Material.COMMAND_MINECART.ordinal()] = 365;
            } catch (NoSuchFieldError nosuchfielderror65) {
                ;
            }

            try {
                aint1[Material.COMPASS.ordinal()] = 288;
            } catch (NoSuchFieldError nosuchfielderror66) {
                ;
            }

            try {
                aint1[Material.COOKED_BEEF.ordinal()] = 307;
            } catch (NoSuchFieldError nosuchfielderror67) {
                ;
            }

            try {
                aint1[Material.COOKED_CHICKEN.ordinal()] = 309;
            } catch (NoSuchFieldError nosuchfielderror68) {
                ;
            }

            try {
                aint1[Material.COOKED_FISH.ordinal()] = 293;
            } catch (NoSuchFieldError nosuchfielderror69) {
                ;
            }

            try {
                aint1[Material.COOKED_MUTTON.ordinal()] = 367;
            } catch (NoSuchFieldError nosuchfielderror70) {
                ;
            }

            try {
                aint1[Material.COOKED_RABBIT.ordinal()] = 355;
            } catch (NoSuchFieldError nosuchfielderror71) {
                ;
            }

            try {
                aint1[Material.COOKIE.ordinal()] = 300;
            } catch (NoSuchFieldError nosuchfielderror72) {
                ;
            }

            try {
                aint1[Material.CROPS.ordinal()] = 60;
            } catch (NoSuchFieldError nosuchfielderror73) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_DOOR.ordinal()] = 198;
            } catch (NoSuchFieldError nosuchfielderror74) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_DOOR_ITEM.ordinal()] = 373;
            } catch (NoSuchFieldError nosuchfielderror75) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_FENCE.ordinal()] = 192;
            } catch (NoSuchFieldError nosuchfielderror76) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_FENCE_GATE.ordinal()] = 187;
            } catch (NoSuchFieldError nosuchfielderror77) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_STAIRS.ordinal()] = 165;
            } catch (NoSuchFieldError nosuchfielderror78) {
                ;
            }

            try {
                aint1[Material.DAYLIGHT_DETECTOR.ordinal()] = 152;
            } catch (NoSuchFieldError nosuchfielderror79) {
                ;
            }

            try {
                aint1[Material.DAYLIGHT_DETECTOR_INVERTED.ordinal()] = 179;
            } catch (NoSuchFieldError nosuchfielderror80) {
                ;
            }

            try {
                aint1[Material.DEAD_BUSH.ordinal()] = 33;
            } catch (NoSuchFieldError nosuchfielderror81) {
                ;
            }

            try {
                aint1[Material.DETECTOR_RAIL.ordinal()] = 29;
            } catch (NoSuchFieldError nosuchfielderror82) {
                ;
            }

            try {
                aint1[Material.DIAMOND.ordinal()] = 207;
            } catch (NoSuchFieldError nosuchfielderror83) {
                ;
            }

            try {
                aint1[Material.DIAMOND_AXE.ordinal()] = 222;
            } catch (NoSuchFieldError nosuchfielderror84) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BARDING.ordinal()] = 362;
            } catch (NoSuchFieldError nosuchfielderror85) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BLOCK.ordinal()] = 58;
            } catch (NoSuchFieldError nosuchfielderror86) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BOOTS.ordinal()] = 256;
            } catch (NoSuchFieldError nosuchfielderror87) {
                ;
            }

            try {
                aint1[Material.DIAMOND_CHESTPLATE.ordinal()] = 254;
            } catch (NoSuchFieldError nosuchfielderror88) {
                ;
            }

            try {
                aint1[Material.DIAMOND_HELMET.ordinal()] = 253;
            } catch (NoSuchFieldError nosuchfielderror89) {
                ;
            }

            try {
                aint1[Material.DIAMOND_HOE.ordinal()] = 236;
            } catch (NoSuchFieldError nosuchfielderror90) {
                ;
            }

            try {
                aint1[Material.DIAMOND_LEGGINGS.ordinal()] = 255;
            } catch (NoSuchFieldError nosuchfielderror91) {
                ;
            }

            try {
                aint1[Material.DIAMOND_ORE.ordinal()] = 57;
            } catch (NoSuchFieldError nosuchfielderror92) {
                ;
            }

            try {
                aint1[Material.DIAMOND_PICKAXE.ordinal()] = 221;
            } catch (NoSuchFieldError nosuchfielderror93) {
                ;
            }

            try {
                aint1[Material.DIAMOND_SPADE.ordinal()] = 220;
            } catch (NoSuchFieldError nosuchfielderror94) {
                ;
            }

            try {
                aint1[Material.DIAMOND_SWORD.ordinal()] = 219;
            } catch (NoSuchFieldError nosuchfielderror95) {
                ;
            }

            try {
                aint1[Material.DIODE.ordinal()] = 299;
            } catch (NoSuchFieldError nosuchfielderror96) {
                ;
            }

            try {
                aint1[Material.DIODE_BLOCK_OFF.ordinal()] = 94;
            } catch (NoSuchFieldError nosuchfielderror97) {
                ;
            }

            try {
                aint1[Material.DIODE_BLOCK_ON.ordinal()] = 95;
            } catch (NoSuchFieldError nosuchfielderror98) {
                ;
            }

            try {
                aint1[Material.DIRT.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror99) {
                ;
            }

            try {
                aint1[Material.DISPENSER.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror100) {
                ;
            }

            try {
                aint1[Material.DOUBLE_PLANT.ordinal()] = 176;
            } catch (NoSuchFieldError nosuchfielderror101) {
                ;
            }

            try {
                aint1[Material.DOUBLE_STEP.ordinal()] = 44;
            } catch (NoSuchFieldError nosuchfielderror102) {
                ;
            }

            try {
                aint1[Material.DOUBLE_STONE_SLAB2.ordinal()] = 182;
            } catch (NoSuchFieldError nosuchfielderror103) {
                ;
            }

            try {
                aint1[Material.DRAGON_EGG.ordinal()] = 123;
            } catch (NoSuchFieldError nosuchfielderror104) {
                ;
            }

            try {
                aint1[Material.DROPPER.ordinal()] = 159;
            } catch (NoSuchFieldError nosuchfielderror105) {
                ;
            }

            try {
                aint1[Material.EGG.ordinal()] = 287;
            } catch (NoSuchFieldError nosuchfielderror106) {
                ;
            }

            try {
                aint1[Material.EMERALD.ordinal()] = 331;
            } catch (NoSuchFieldError nosuchfielderror107) {
                ;
            }

            try {
                aint1[Material.EMERALD_BLOCK.ordinal()] = 134;
            } catch (NoSuchFieldError nosuchfielderror108) {
                ;
            }

            try {
                aint1[Material.EMERALD_ORE.ordinal()] = 130;
            } catch (NoSuchFieldError nosuchfielderror109) {
                ;
            }

            try {
                aint1[Material.EMPTY_MAP.ordinal()] = 338;
            } catch (NoSuchFieldError nosuchfielderror110) {
                ;
            }

            try {
                aint1[Material.ENCHANTED_BOOK.ordinal()] = 346;
            } catch (NoSuchFieldError nosuchfielderror111) {
                ;
            }

            try {
                aint1[Material.ENCHANTMENT_TABLE.ordinal()] = 117;
            } catch (NoSuchFieldError nosuchfielderror112) {
                ;
            }

            try {
                aint1[Material.ENDER_CHEST.ordinal()] = 131;
            } catch (NoSuchFieldError nosuchfielderror113) {
                ;
            }

            try {
                aint1[Material.ENDER_PEARL.ordinal()] = 311;
            } catch (NoSuchFieldError nosuchfielderror114) {
                ;
            }

            try {
                aint1[Material.ENDER_PORTAL.ordinal()] = 120;
            } catch (NoSuchFieldError nosuchfielderror115) {
                ;
            }

            try {
                aint1[Material.ENDER_PORTAL_FRAME.ordinal()] = 121;
            } catch (NoSuchFieldError nosuchfielderror116) {
                ;
            }

            try {
                aint1[Material.ENDER_STONE.ordinal()] = 122;
            } catch (NoSuchFieldError nosuchfielderror117) {
                ;
            }

            try {
                aint1[Material.EXPLOSIVE_MINECART.ordinal()] = 350;
            } catch (NoSuchFieldError nosuchfielderror118) {
                ;
            }

            try {
                aint1[Material.EXP_BOTTLE.ordinal()] = 327;
            } catch (NoSuchFieldError nosuchfielderror119) {
                ;
            }

            try {
                aint1[Material.EYE_OF_ENDER.ordinal()] = 324;
            } catch (NoSuchFieldError nosuchfielderror120) {
                ;
            }

            try {
                aint1[Material.FEATHER.ordinal()] = 231;
            } catch (NoSuchFieldError nosuchfielderror121) {
                ;
            }

            try {
                aint1[Material.FENCE.ordinal()] = 86;
            } catch (NoSuchFieldError nosuchfielderror122) {
                ;
            }

            try {
                aint1[Material.FENCE_GATE.ordinal()] = 108;
            } catch (NoSuchFieldError nosuchfielderror123) {
                ;
            }

            try {
                aint1[Material.FERMENTED_SPIDER_EYE.ordinal()] = 319;
            } catch (NoSuchFieldError nosuchfielderror124) {
                ;
            }

            try {
                aint1[Material.FIRE.ordinal()] = 52;
            } catch (NoSuchFieldError nosuchfielderror125) {
                ;
            }

            try {
                aint1[Material.FIREBALL.ordinal()] = 328;
            } catch (NoSuchFieldError nosuchfielderror126) {
                ;
            }

            try {
                aint1[Material.FIREWORK.ordinal()] = 344;
            } catch (NoSuchFieldError nosuchfielderror127) {
                ;
            }

            try {
                aint1[Material.FIREWORK_CHARGE.ordinal()] = 345;
            } catch (NoSuchFieldError nosuchfielderror128) {
                ;
            }

            try {
                aint1[Material.FISHING_ROD.ordinal()] = 289;
            } catch (NoSuchFieldError nosuchfielderror129) {
                ;
            }

            try {
                aint1[Material.FLINT.ordinal()] = 261;
            } catch (NoSuchFieldError nosuchfielderror130) {
                ;
            }

            try {
                aint1[Material.FLINT_AND_STEEL.ordinal()] = 202;
            } catch (NoSuchFieldError nosuchfielderror131) {
                ;
            }

            try {
                aint1[Material.FLOWER_POT.ordinal()] = 141;
            } catch (NoSuchFieldError nosuchfielderror132) {
                ;
            }

            try {
                aint1[Material.FLOWER_POT_ITEM.ordinal()] = 333;
            } catch (NoSuchFieldError nosuchfielderror133) {
                ;
            }

            try {
                aint1[Material.FURNACE.ordinal()] = 62;
            } catch (NoSuchFieldError nosuchfielderror134) {
                ;
            }

            try {
                aint1[Material.GHAST_TEAR.ordinal()] = 313;
            } catch (NoSuchFieldError nosuchfielderror135) {
                ;
            }

            try {
                aint1[Material.GLASS.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror136) {
                ;
            }

            try {
                aint1[Material.GLASS_BOTTLE.ordinal()] = 317;
            } catch (NoSuchFieldError nosuchfielderror137) {
                ;
            }

            try {
                aint1[Material.GLOWING_REDSTONE_ORE.ordinal()] = 75;
            } catch (NoSuchFieldError nosuchfielderror138) {
                ;
            }

            try {
                aint1[Material.GLOWSTONE.ordinal()] = 90;
            } catch (NoSuchFieldError nosuchfielderror139) {
                ;
            }

            try {
                aint1[Material.GLOWSTONE_DUST.ordinal()] = 291;
            } catch (NoSuchFieldError nosuchfielderror140) {
                ;
            }

            try {
                aint1[Material.GOLDEN_APPLE.ordinal()] = 265;
            } catch (NoSuchFieldError nosuchfielderror141) {
                ;
            }

            try {
                aint1[Material.GOLDEN_CARROT.ordinal()] = 339;
            } catch (NoSuchFieldError nosuchfielderror142) {
                ;
            }

            try {
                aint1[Material.GOLD_AXE.ordinal()] = 229;
            } catch (NoSuchFieldError nosuchfielderror143) {
                ;
            }

            try {
                aint1[Material.GOLD_BARDING.ordinal()] = 361;
            } catch (NoSuchFieldError nosuchfielderror144) {
                ;
            }

            try {
                aint1[Material.GOLD_BLOCK.ordinal()] = 42;
            } catch (NoSuchFieldError nosuchfielderror145) {
                ;
            }

            try {
                aint1[Material.GOLD_BOOTS.ordinal()] = 260;
            } catch (NoSuchFieldError nosuchfielderror146) {
                ;
            }

            try {
                aint1[Material.GOLD_CHESTPLATE.ordinal()] = 258;
            } catch (NoSuchFieldError nosuchfielderror147) {
                ;
            }

            try {
                aint1[Material.GOLD_HELMET.ordinal()] = 257;
            } catch (NoSuchFieldError nosuchfielderror148) {
                ;
            }

            try {
                aint1[Material.GOLD_HOE.ordinal()] = 237;
            } catch (NoSuchFieldError nosuchfielderror149) {
                ;
            }

            try {
                aint1[Material.GOLD_INGOT.ordinal()] = 209;
            } catch (NoSuchFieldError nosuchfielderror150) {
                ;
            }

            try {
                aint1[Material.GOLD_LEGGINGS.ordinal()] = 259;
            } catch (NoSuchFieldError nosuchfielderror151) {
                ;
            }

            try {
                aint1[Material.GOLD_NUGGET.ordinal()] = 314;
            } catch (NoSuchFieldError nosuchfielderror152) {
                ;
            }

            try {
                aint1[Material.GOLD_ORE.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror153) {
                ;
            }

            try {
                aint1[Material.GOLD_PICKAXE.ordinal()] = 228;
            } catch (NoSuchFieldError nosuchfielderror154) {
                ;
            }

            try {
                aint1[Material.GOLD_PLATE.ordinal()] = 148;
            } catch (NoSuchFieldError nosuchfielderror155) {
                ;
            }

            try {
                aint1[Material.GOLD_RECORD.ordinal()] = 374;
            } catch (NoSuchFieldError nosuchfielderror156) {
                ;
            }

            try {
                aint1[Material.GOLD_SPADE.ordinal()] = 227;
            } catch (NoSuchFieldError nosuchfielderror157) {
                ;
            }

            try {
                aint1[Material.GOLD_SWORD.ordinal()] = 226;
            } catch (NoSuchFieldError nosuchfielderror158) {
                ;
            }

            try {
                aint1[Material.GRASS.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror159) {
                ;
            }

            try {
                aint1[Material.GRAVEL.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror160) {
                ;
            }

            try {
                aint1[Material.GREEN_RECORD.ordinal()] = 375;
            } catch (NoSuchFieldError nosuchfielderror161) {
                ;
            }

            try {
                aint1[Material.GRILLED_PORK.ordinal()] = 263;
            } catch (NoSuchFieldError nosuchfielderror162) {
                ;
            }

            try {
                aint1[Material.HARD_CLAY.ordinal()] = 173;
            } catch (NoSuchFieldError nosuchfielderror163) {
                ;
            }

            try {
                aint1[Material.HAY_BLOCK.ordinal()] = 171;
            } catch (NoSuchFieldError nosuchfielderror164) {
                ;
            }

            try {
                aint1[Material.HOPPER.ordinal()] = 155;
            } catch (NoSuchFieldError nosuchfielderror165) {
                ;
            }

            try {
                aint1[Material.HOPPER_MINECART.ordinal()] = 351;
            } catch (NoSuchFieldError nosuchfielderror166) {
                ;
            }

            try {
                aint1[Material.HUGE_MUSHROOM_1.ordinal()] = 100;
            } catch (NoSuchFieldError nosuchfielderror167) {
                ;
            }

            try {
                aint1[Material.HUGE_MUSHROOM_2.ordinal()] = 101;
            } catch (NoSuchFieldError nosuchfielderror168) {
                ;
            }

            try {
                aint1[Material.ICE.ordinal()] = 80;
            } catch (NoSuchFieldError nosuchfielderror169) {
                ;
            }

            try {
                aint1[Material.INK_SACK.ordinal()] = 294;
            } catch (NoSuchFieldError nosuchfielderror170) {
                ;
            }

            try {
                aint1[Material.IRON_AXE.ordinal()] = 201;
            } catch (NoSuchFieldError nosuchfielderror171) {
                ;
            }

            try {
                aint1[Material.IRON_BARDING.ordinal()] = 360;
            } catch (NoSuchFieldError nosuchfielderror172) {
                ;
            }

            try {
                aint1[Material.IRON_BLOCK.ordinal()] = 43;
            } catch (NoSuchFieldError nosuchfielderror173) {
                ;
            }

            try {
                aint1[Material.IRON_BOOTS.ordinal()] = 252;
            } catch (NoSuchFieldError nosuchfielderror174) {
                ;
            }

            try {
                aint1[Material.IRON_CHESTPLATE.ordinal()] = 250;
            } catch (NoSuchFieldError nosuchfielderror175) {
                ;
            }

            try {
                aint1[Material.IRON_DOOR.ordinal()] = 273;
            } catch (NoSuchFieldError nosuchfielderror176) {
                ;
            }

            try {
                aint1[Material.IRON_DOOR_BLOCK.ordinal()] = 72;
            } catch (NoSuchFieldError nosuchfielderror177) {
                ;
            }

            try {
                aint1[Material.IRON_FENCE.ordinal()] = 102;
            } catch (NoSuchFieldError nosuchfielderror178) {
                ;
            }

            try {
                aint1[Material.IRON_HELMET.ordinal()] = 249;
            } catch (NoSuchFieldError nosuchfielderror179) {
                ;
            }

            try {
                aint1[Material.IRON_HOE.ordinal()] = 235;
            } catch (NoSuchFieldError nosuchfielderror180) {
                ;
            }

            try {
                aint1[Material.IRON_INGOT.ordinal()] = 208;
            } catch (NoSuchFieldError nosuchfielderror181) {
                ;
            }

            try {
                aint1[Material.IRON_LEGGINGS.ordinal()] = 251;
            } catch (NoSuchFieldError nosuchfielderror182) {
                ;
            }

            try {
                aint1[Material.IRON_ORE.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror183) {
                ;
            }

            try {
                aint1[Material.IRON_PICKAXE.ordinal()] = 200;
            } catch (NoSuchFieldError nosuchfielderror184) {
                ;
            }

            try {
                aint1[Material.IRON_PLATE.ordinal()] = 149;
            } catch (NoSuchFieldError nosuchfielderror185) {
                ;
            }

            try {
                aint1[Material.IRON_SPADE.ordinal()] = 199;
            } catch (NoSuchFieldError nosuchfielderror186) {
                ;
            }

            try {
                aint1[Material.IRON_SWORD.ordinal()] = 210;
            } catch (NoSuchFieldError nosuchfielderror187) {
                ;
            }

            try {
                aint1[Material.IRON_TRAPDOOR.ordinal()] = 168;
            } catch (NoSuchFieldError nosuchfielderror188) {
                ;
            }

            try {
                aint1[Material.ITEM_FRAME.ordinal()] = 332;
            } catch (NoSuchFieldError nosuchfielderror189) {
                ;
            }

            try {
                aint1[Material.JACK_O_LANTERN.ordinal()] = 92;
            } catch (NoSuchFieldError nosuchfielderror190) {
                ;
            }

            try {
                aint1[Material.JUKEBOX.ordinal()] = 85;
            } catch (NoSuchFieldError nosuchfielderror191) {
                ;
            }

            try {
                aint1[Material.JUNGLE_DOOR.ordinal()] = 196;
            } catch (NoSuchFieldError nosuchfielderror192) {
                ;
            }

            try {
                aint1[Material.JUNGLE_DOOR_ITEM.ordinal()] = 371;
            } catch (NoSuchFieldError nosuchfielderror193) {
                ;
            }

            try {
                aint1[Material.JUNGLE_FENCE.ordinal()] = 191;
            } catch (NoSuchFieldError nosuchfielderror194) {
                ;
            }

            try {
                aint1[Material.JUNGLE_FENCE_GATE.ordinal()] = 186;
            } catch (NoSuchFieldError nosuchfielderror195) {
                ;
            }

            try {
                aint1[Material.JUNGLE_WOOD_STAIRS.ordinal()] = 137;
            } catch (NoSuchFieldError nosuchfielderror196) {
                ;
            }

            try {
                aint1[Material.LADDER.ordinal()] = 66;
            } catch (NoSuchFieldError nosuchfielderror197) {
                ;
            }

            try {
                aint1[Material.LAPIS_BLOCK.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror198) {
                ;
            }

            try {
                aint1[Material.LAPIS_ORE.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror199) {
                ;
            }

            try {
                aint1[Material.LAVA.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror200) {
                ;
            }

            try {
                aint1[Material.LAVA_BUCKET.ordinal()] = 270;
            } catch (NoSuchFieldError nosuchfielderror201) {
                ;
            }

            try {
                aint1[Material.LEASH.ordinal()] = 363;
            } catch (NoSuchFieldError nosuchfielderror202) {
                ;
            }

            try {
                aint1[Material.LEATHER.ordinal()] = 277;
            } catch (NoSuchFieldError nosuchfielderror203) {
                ;
            }

            try {
                aint1[Material.LEATHER_BOOTS.ordinal()] = 244;
            } catch (NoSuchFieldError nosuchfielderror204) {
                ;
            }

            try {
                aint1[Material.LEATHER_CHESTPLATE.ordinal()] = 242;
            } catch (NoSuchFieldError nosuchfielderror205) {
                ;
            }

            try {
                aint1[Material.LEATHER_HELMET.ordinal()] = 241;
            } catch (NoSuchFieldError nosuchfielderror206) {
                ;
            }

            try {
                aint1[Material.LEATHER_LEGGINGS.ordinal()] = 243;
            } catch (NoSuchFieldError nosuchfielderror207) {
                ;
            }

            try {
                aint1[Material.LEAVES.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror208) {
                ;
            }

            try {
                aint1[Material.LEAVES_2.ordinal()] = 162;
            } catch (NoSuchFieldError nosuchfielderror209) {
                ;
            }

            try {
                aint1[Material.LEVER.ordinal()] = 70;
            } catch (NoSuchFieldError nosuchfielderror210) {
                ;
            }

            try {
                aint1[Material.LOG.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror211) {
                ;
            }

            try {
                aint1[Material.LOG_2.ordinal()] = 163;
            } catch (NoSuchFieldError nosuchfielderror212) {
                ;
            }

            try {
                aint1[Material.LONG_GRASS.ordinal()] = 32;
            } catch (NoSuchFieldError nosuchfielderror213) {
                ;
            }

            try {
                aint1[Material.MAGMA_CREAM.ordinal()] = 321;
            } catch (NoSuchFieldError nosuchfielderror214) {
                ;
            }

            try {
                aint1[Material.MAP.ordinal()] = 301;
            } catch (NoSuchFieldError nosuchfielderror215) {
                ;
            }

            try {
                aint1[Material.MELON.ordinal()] = 303;
            } catch (NoSuchFieldError nosuchfielderror216) {
                ;
            }

            try {
                aint1[Material.MELON_BLOCK.ordinal()] = 104;
            } catch (NoSuchFieldError nosuchfielderror217) {
                ;
            }

            try {
                aint1[Material.MELON_SEEDS.ordinal()] = 305;
            } catch (NoSuchFieldError nosuchfielderror218) {
                ;
            }

            try {
                aint1[Material.MELON_STEM.ordinal()] = 106;
            } catch (NoSuchFieldError nosuchfielderror219) {
                ;
            }

            try {
                aint1[Material.MILK_BUCKET.ordinal()] = 278;
            } catch (NoSuchFieldError nosuchfielderror220) {
                ;
            }

            try {
                aint1[Material.MINECART.ordinal()] = 271;
            } catch (NoSuchFieldError nosuchfielderror221) {
                ;
            }

            try {
                aint1[Material.MOB_SPAWNER.ordinal()] = 53;
            } catch (NoSuchFieldError nosuchfielderror222) {
                ;
            }

            try {
                aint1[Material.MONSTER_EGG.ordinal()] = 326;
            } catch (NoSuchFieldError nosuchfielderror223) {
                ;
            }

            try {
                aint1[Material.MONSTER_EGGS.ordinal()] = 98;
            } catch (NoSuchFieldError nosuchfielderror224) {
                ;
            }

            try {
                aint1[Material.MOSSY_COBBLESTONE.ordinal()] = 49;
            } catch (NoSuchFieldError nosuchfielderror225) {
                ;
            }

            try {
                aint1[Material.MUSHROOM_SOUP.ordinal()] = 225;
            } catch (NoSuchFieldError nosuchfielderror226) {
                ;
            }

            try {
                aint1[Material.MUTTON.ordinal()] = 366;
            } catch (NoSuchFieldError nosuchfielderror227) {
                ;
            }

            try {
                aint1[Material.MYCEL.ordinal()] = 111;
            } catch (NoSuchFieldError nosuchfielderror228) {
                ;
            }

            try {
                aint1[Material.NAME_TAG.ordinal()] = 364;
            } catch (NoSuchFieldError nosuchfielderror229) {
                ;
            }

            try {
                aint1[Material.NETHERRACK.ordinal()] = 88;
            } catch (NoSuchFieldError nosuchfielderror230) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK.ordinal()] = 113;
            } catch (NoSuchFieldError nosuchfielderror231) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK_ITEM.ordinal()] = 348;
            } catch (NoSuchFieldError nosuchfielderror232) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK_STAIRS.ordinal()] = 115;
            } catch (NoSuchFieldError nosuchfielderror233) {
                ;
            }

            try {
                aint1[Material.NETHER_FENCE.ordinal()] = 114;
            } catch (NoSuchFieldError nosuchfielderror234) {
                ;
            }

            try {
                aint1[Material.NETHER_STALK.ordinal()] = 315;
            } catch (NoSuchFieldError nosuchfielderror235) {
                ;
            }

            try {
                aint1[Material.NETHER_STAR.ordinal()] = 342;
            } catch (NoSuchFieldError nosuchfielderror236) {
                ;
            }

            try {
                aint1[Material.NETHER_WARTS.ordinal()] = 116;
            } catch (NoSuchFieldError nosuchfielderror237) {
                ;
            }

            try {
                aint1[Material.NOTE_BLOCK.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror238) {
                ;
            }

            try {
                aint1[Material.OBSIDIAN.ordinal()] = 50;
            } catch (NoSuchFieldError nosuchfielderror239) {
                ;
            }

            try {
                aint1[Material.PACKED_ICE.ordinal()] = 175;
            } catch (NoSuchFieldError nosuchfielderror240) {
                ;
            }

            try {
                aint1[Material.PAINTING.ordinal()] = 264;
            } catch (NoSuchFieldError nosuchfielderror241) {
                ;
            }

            try {
                aint1[Material.PAPER.ordinal()] = 282;
            } catch (NoSuchFieldError nosuchfielderror242) {
                ;
            }

            try {
                aint1[Material.PISTON_BASE.ordinal()] = 34;
            } catch (NoSuchFieldError nosuchfielderror243) {
                ;
            }

            try {
                aint1[Material.PISTON_EXTENSION.ordinal()] = 35;
            } catch (NoSuchFieldError nosuchfielderror244) {
                ;
            }

            try {
                aint1[Material.PISTON_MOVING_PIECE.ordinal()] = 37;
            } catch (NoSuchFieldError nosuchfielderror245) {
                ;
            }

            try {
                aint1[Material.PISTON_STICKY_BASE.ordinal()] = 30;
            } catch (NoSuchFieldError nosuchfielderror246) {
                ;
            }

            try {
                aint1[Material.POISONOUS_POTATO.ordinal()] = 337;
            } catch (NoSuchFieldError nosuchfielderror247) {
                ;
            }

            try {
                aint1[Material.PORK.ordinal()] = 262;
            } catch (NoSuchFieldError nosuchfielderror248) {
                ;
            }

            try {
                aint1[Material.PORTAL.ordinal()] = 91;
            } catch (NoSuchFieldError nosuchfielderror249) {
                ;
            }

            try {
                aint1[Material.POTATO.ordinal()] = 143;
            } catch (NoSuchFieldError nosuchfielderror250) {
                ;
            }

            try {
                aint1[Material.POTATO_ITEM.ordinal()] = 335;
            } catch (NoSuchFieldError nosuchfielderror251) {
                ;
            }

            try {
                aint1[Material.POTION.ordinal()] = 316;
            } catch (NoSuchFieldError nosuchfielderror252) {
                ;
            }

            try {
                aint1[Material.POWERED_MINECART.ordinal()] = 286;
            } catch (NoSuchFieldError nosuchfielderror253) {
                ;
            }

            try {
                aint1[Material.POWERED_RAIL.ordinal()] = 28;
            } catch (NoSuchFieldError nosuchfielderror254) {
                ;
            }

            try {
                aint1[Material.PRISMARINE.ordinal()] = 169;
            } catch (NoSuchFieldError nosuchfielderror255) {
                ;
            }

            try {
                aint1[Material.PRISMARINE_CRYSTALS.ordinal()] = 353;
            } catch (NoSuchFieldError nosuchfielderror256) {
                ;
            }

            try {
                aint1[Material.PRISMARINE_SHARD.ordinal()] = 352;
            } catch (NoSuchFieldError nosuchfielderror257) {
                ;
            }

            try {
                aint1[Material.PUMPKIN.ordinal()] = 87;
            } catch (NoSuchFieldError nosuchfielderror258) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_PIE.ordinal()] = 343;
            } catch (NoSuchFieldError nosuchfielderror259) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_SEEDS.ordinal()] = 304;
            } catch (NoSuchFieldError nosuchfielderror260) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_STEM.ordinal()] = 105;
            } catch (NoSuchFieldError nosuchfielderror261) {
                ;
            }

            try {
                aint1[Material.QUARTZ.ordinal()] = 349;
            } catch (NoSuchFieldError nosuchfielderror262) {
                ;
            }

            try {
                aint1[Material.QUARTZ_BLOCK.ordinal()] = 156;
            } catch (NoSuchFieldError nosuchfielderror263) {
                ;
            }

            try {
                aint1[Material.QUARTZ_ORE.ordinal()] = 154;
            } catch (NoSuchFieldError nosuchfielderror264) {
                ;
            }

            try {
                aint1[Material.QUARTZ_STAIRS.ordinal()] = 157;
            } catch (NoSuchFieldError nosuchfielderror265) {
                ;
            }

            try {
                aint1[Material.RABBIT.ordinal()] = 354;
            } catch (NoSuchFieldError nosuchfielderror266) {
                ;
            }

            try {
                aint1[Material.RABBIT_FOOT.ordinal()] = 357;
            } catch (NoSuchFieldError nosuchfielderror267) {
                ;
            }

            try {
                aint1[Material.RABBIT_HIDE.ordinal()] = 358;
            } catch (NoSuchFieldError nosuchfielderror268) {
                ;
            }

            try {
                aint1[Material.RABBIT_STEW.ordinal()] = 356;
            } catch (NoSuchFieldError nosuchfielderror269) {
                ;
            }

            try {
                aint1[Material.RAILS.ordinal()] = 67;
            } catch (NoSuchFieldError nosuchfielderror270) {
                ;
            }

            try {
                aint1[Material.RAW_BEEF.ordinal()] = 306;
            } catch (NoSuchFieldError nosuchfielderror271) {
                ;
            }

            try {
                aint1[Material.RAW_CHICKEN.ordinal()] = 308;
            } catch (NoSuchFieldError nosuchfielderror272) {
                ;
            }

            try {
                aint1[Material.RAW_FISH.ordinal()] = 292;
            } catch (NoSuchFieldError nosuchfielderror273) {
                ;
            }

            try {
                aint1[Material.RECORD_10.ordinal()] = 383;
            } catch (NoSuchFieldError nosuchfielderror274) {
                ;
            }

            try {
                aint1[Material.RECORD_11.ordinal()] = 384;
            } catch (NoSuchFieldError nosuchfielderror275) {
                ;
            }

            try {
                aint1[Material.RECORD_12.ordinal()] = 385;
            } catch (NoSuchFieldError nosuchfielderror276) {
                ;
            }

            try {
                aint1[Material.RECORD_3.ordinal()] = 376;
            } catch (NoSuchFieldError nosuchfielderror277) {
                ;
            }

            try {
                aint1[Material.RECORD_4.ordinal()] = 377;
            } catch (NoSuchFieldError nosuchfielderror278) {
                ;
            }

            try {
                aint1[Material.RECORD_5.ordinal()] = 378;
            } catch (NoSuchFieldError nosuchfielderror279) {
                ;
            }

            try {
                aint1[Material.RECORD_6.ordinal()] = 379;
            } catch (NoSuchFieldError nosuchfielderror280) {
                ;
            }

            try {
                aint1[Material.RECORD_7.ordinal()] = 380;
            } catch (NoSuchFieldError nosuchfielderror281) {
                ;
            }

            try {
                aint1[Material.RECORD_8.ordinal()] = 381;
            } catch (NoSuchFieldError nosuchfielderror282) {
                ;
            }

            try {
                aint1[Material.RECORD_9.ordinal()] = 382;
            } catch (NoSuchFieldError nosuchfielderror283) {
                ;
            }

            try {
                aint1[Material.REDSTONE.ordinal()] = 274;
            } catch (NoSuchFieldError nosuchfielderror284) {
                ;
            }

            try {
                aint1[Material.REDSTONE_BLOCK.ordinal()] = 153;
            } catch (NoSuchFieldError nosuchfielderror285) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR.ordinal()] = 347;
            } catch (NoSuchFieldError nosuchfielderror286) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR_OFF.ordinal()] = 150;
            } catch (NoSuchFieldError nosuchfielderror287) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR_ON.ordinal()] = 151;
            } catch (NoSuchFieldError nosuchfielderror288) {
                ;
            }

            try {
                aint1[Material.REDSTONE_LAMP_OFF.ordinal()] = 124;
            } catch (NoSuchFieldError nosuchfielderror289) {
                ;
            }

            try {
                aint1[Material.REDSTONE_LAMP_ON.ordinal()] = 125;
            } catch (NoSuchFieldError nosuchfielderror290) {
                ;
            }

            try {
                aint1[Material.REDSTONE_ORE.ordinal()] = 74;
            } catch (NoSuchFieldError nosuchfielderror291) {
                ;
            }

            try {
                aint1[Material.REDSTONE_TORCH_OFF.ordinal()] = 76;
            } catch (NoSuchFieldError nosuchfielderror292) {
                ;
            }

            try {
                aint1[Material.REDSTONE_TORCH_ON.ordinal()] = 77;
            } catch (NoSuchFieldError nosuchfielderror293) {
                ;
            }

            try {
                aint1[Material.REDSTONE_WIRE.ordinal()] = 56;
            } catch (NoSuchFieldError nosuchfielderror294) {
                ;
            }

            try {
                aint1[Material.RED_MUSHROOM.ordinal()] = 41;
            } catch (NoSuchFieldError nosuchfielderror295) {
                ;
            }

            try {
                aint1[Material.RED_ROSE.ordinal()] = 39;
            } catch (NoSuchFieldError nosuchfielderror296) {
                ;
            }

            try {
                aint1[Material.RED_SANDSTONE.ordinal()] = 180;
            } catch (NoSuchFieldError nosuchfielderror297) {
                ;
            }

            try {
                aint1[Material.RED_SANDSTONE_STAIRS.ordinal()] = 181;
            } catch (NoSuchFieldError nosuchfielderror298) {
                ;
            }

            try {
                aint1[Material.ROTTEN_FLESH.ordinal()] = 310;
            } catch (NoSuchFieldError nosuchfielderror299) {
                ;
            }

            try {
                aint1[Material.SADDLE.ordinal()] = 272;
            } catch (NoSuchFieldError nosuchfielderror300) {
                ;
            }

            try {
                aint1[Material.SAND.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror301) {
                ;
            }

            try {
                aint1[Material.SANDSTONE.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror302) {
                ;
            }

            try {
                aint1[Material.SANDSTONE_STAIRS.ordinal()] = 129;
            } catch (NoSuchFieldError nosuchfielderror303) {
                ;
            }

            try {
                aint1[Material.SAPLING.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror304) {
                ;
            }

            try {
                aint1[Material.SEA_LANTERN.ordinal()] = 170;
            } catch (NoSuchFieldError nosuchfielderror305) {
                ;
            }

            try {
                aint1[Material.SEEDS.ordinal()] = 238;
            } catch (NoSuchFieldError nosuchfielderror306) {
                ;
            }

            try {
                aint1[Material.SHEARS.ordinal()] = 302;
            } catch (NoSuchFieldError nosuchfielderror307) {
                ;
            }

            try {
                aint1[Material.SIGN.ordinal()] = 266;
            } catch (NoSuchFieldError nosuchfielderror308) {
                ;
            }

            try {
                aint1[Material.SIGN_POST.ordinal()] = 64;
            } catch (NoSuchFieldError nosuchfielderror309) {
                ;
            }

            try {
                aint1[Material.SKULL.ordinal()] = 145;
            } catch (NoSuchFieldError nosuchfielderror310) {
                ;
            }

            try {
                aint1[Material.SKULL_ITEM.ordinal()] = 340;
            } catch (NoSuchFieldError nosuchfielderror311) {
                ;
            }

            try {
                aint1[Material.SLIME_BALL.ordinal()] = 284;
            } catch (NoSuchFieldError nosuchfielderror312) {
                ;
            }

            try {
                aint1[Material.SLIME_BLOCK.ordinal()] = 166;
            } catch (NoSuchFieldError nosuchfielderror313) {
                ;
            }

            try {
                aint1[Material.SMOOTH_BRICK.ordinal()] = 99;
            } catch (NoSuchFieldError nosuchfielderror314) {
                ;
            }

            try {
                aint1[Material.SMOOTH_STAIRS.ordinal()] = 110;
            } catch (NoSuchFieldError nosuchfielderror315) {
                ;
            }

            try {
                aint1[Material.SNOW.ordinal()] = 79;
            } catch (NoSuchFieldError nosuchfielderror316) {
                ;
            }

            try {
                aint1[Material.SNOW_BALL.ordinal()] = 275;
            } catch (NoSuchFieldError nosuchfielderror317) {
                ;
            }

            try {
                aint1[Material.SNOW_BLOCK.ordinal()] = 81;
            } catch (NoSuchFieldError nosuchfielderror318) {
                ;
            }

            try {
                aint1[Material.SOIL.ordinal()] = 61;
            } catch (NoSuchFieldError nosuchfielderror319) {
                ;
            }

            try {
                aint1[Material.SOUL_SAND.ordinal()] = 89;
            } catch (NoSuchFieldError nosuchfielderror320) {
                ;
            }

            try {
                aint1[Material.SPECKLED_MELON.ordinal()] = 325;
            } catch (NoSuchFieldError nosuchfielderror321) {
                ;
            }

            try {
                aint1[Material.SPIDER_EYE.ordinal()] = 318;
            } catch (NoSuchFieldError nosuchfielderror322) {
                ;
            }

            try {
                aint1[Material.SPONGE.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror323) {
                ;
            }

            try {
                aint1[Material.SPRUCE_DOOR.ordinal()] = 194;
            } catch (NoSuchFieldError nosuchfielderror324) {
                ;
            }

            try {
                aint1[Material.SPRUCE_DOOR_ITEM.ordinal()] = 369;
            } catch (NoSuchFieldError nosuchfielderror325) {
                ;
            }

            try {
                aint1[Material.SPRUCE_FENCE.ordinal()] = 189;
            } catch (NoSuchFieldError nosuchfielderror326) {
                ;
            }

            try {
                aint1[Material.SPRUCE_FENCE_GATE.ordinal()] = 184;
            } catch (NoSuchFieldError nosuchfielderror327) {
                ;
            }

            try {
                aint1[Material.SPRUCE_WOOD_STAIRS.ordinal()] = 135;
            } catch (NoSuchFieldError nosuchfielderror328) {
                ;
            }

            try {
                aint1[Material.STAINED_CLAY.ordinal()] = 160;
            } catch (NoSuchFieldError nosuchfielderror329) {
                ;
            }

            try {
                aint1[Material.STAINED_GLASS.ordinal()] = 96;
            } catch (NoSuchFieldError nosuchfielderror330) {
                ;
            }

            try {
                aint1[Material.STAINED_GLASS_PANE.ordinal()] = 161;
            } catch (NoSuchFieldError nosuchfielderror331) {
                ;
            }

            try {
                aint1[Material.STANDING_BANNER.ordinal()] = 177;
            } catch (NoSuchFieldError nosuchfielderror332) {
                ;
            }

            try {
                aint1[Material.STATIONARY_LAVA.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror333) {
                ;
            }

            try {
                aint1[Material.STATIONARY_WATER.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror334) {
                ;
            }

            try {
                aint1[Material.STEP.ordinal()] = 45;
            } catch (NoSuchFieldError nosuchfielderror335) {
                ;
            }

            try {
                aint1[Material.STICK.ordinal()] = 223;
            } catch (NoSuchFieldError nosuchfielderror336) {
                ;
            }

            try {
                aint1[Material.STONE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror337) {
                ;
            }

            try {
                aint1[Material.STONE_AXE.ordinal()] = 218;
            } catch (NoSuchFieldError nosuchfielderror338) {
                ;
            }

            try {
                aint1[Material.STONE_BUTTON.ordinal()] = 78;
            } catch (NoSuchFieldError nosuchfielderror339) {
                ;
            }

            try {
                aint1[Material.STONE_HOE.ordinal()] = 234;
            } catch (NoSuchFieldError nosuchfielderror340) {
                ;
            }

            try {
                aint1[Material.STONE_PICKAXE.ordinal()] = 217;
            } catch (NoSuchFieldError nosuchfielderror341) {
                ;
            }

            try {
                aint1[Material.STONE_PLATE.ordinal()] = 71;
            } catch (NoSuchFieldError nosuchfielderror342) {
                ;
            }

            try {
                aint1[Material.STONE_SLAB2.ordinal()] = 183;
            } catch (NoSuchFieldError nosuchfielderror343) {
                ;
            }

            try {
                aint1[Material.STONE_SPADE.ordinal()] = 216;
            } catch (NoSuchFieldError nosuchfielderror344) {
                ;
            }

            try {
                aint1[Material.STONE_SWORD.ordinal()] = 215;
            } catch (NoSuchFieldError nosuchfielderror345) {
                ;
            }

            try {
                aint1[Material.STORAGE_MINECART.ordinal()] = 285;
            } catch (NoSuchFieldError nosuchfielderror346) {
                ;
            }

            try {
                aint1[Material.STRING.ordinal()] = 230;
            } catch (NoSuchFieldError nosuchfielderror347) {
                ;
            }

            try {
                aint1[Material.SUGAR.ordinal()] = 296;
            } catch (NoSuchFieldError nosuchfielderror348) {
                ;
            }

            try {
                aint1[Material.SUGAR_CANE.ordinal()] = 281;
            } catch (NoSuchFieldError nosuchfielderror349) {
                ;
            }

            try {
                aint1[Material.SUGAR_CANE_BLOCK.ordinal()] = 84;
            } catch (NoSuchFieldError nosuchfielderror350) {
                ;
            }

            try {
                aint1[Material.SULPHUR.ordinal()] = 232;
            } catch (NoSuchFieldError nosuchfielderror351) {
                ;
            }

            try {
                aint1[Material.THIN_GLASS.ordinal()] = 103;
            } catch (NoSuchFieldError nosuchfielderror352) {
                ;
            }

            try {
                aint1[Material.TNT.ordinal()] = 47;
            } catch (NoSuchFieldError nosuchfielderror353) {
                ;
            }

            try {
                aint1[Material.TORCH.ordinal()] = 51;
            } catch (NoSuchFieldError nosuchfielderror354) {
                ;
            }

            try {
                aint1[Material.TRAPPED_CHEST.ordinal()] = 147;
            } catch (NoSuchFieldError nosuchfielderror355) {
                ;
            }

            try {
                aint1[Material.TRAP_DOOR.ordinal()] = 97;
            } catch (NoSuchFieldError nosuchfielderror356) {
                ;
            }

            try {
                aint1[Material.TRIPWIRE.ordinal()] = 133;
            } catch (NoSuchFieldError nosuchfielderror357) {
                ;
            }

            try {
                aint1[Material.TRIPWIRE_HOOK.ordinal()] = 132;
            } catch (NoSuchFieldError nosuchfielderror358) {
                ;
            }

            try {
                aint1[Material.VINE.ordinal()] = 107;
            } catch (NoSuchFieldError nosuchfielderror359) {
                ;
            }

            try {
                aint1[Material.WALL_BANNER.ordinal()] = 178;
            } catch (NoSuchFieldError nosuchfielderror360) {
                ;
            }

            try {
                aint1[Material.WALL_SIGN.ordinal()] = 69;
            } catch (NoSuchFieldError nosuchfielderror361) {
                ;
            }

            try {
                aint1[Material.WATCH.ordinal()] = 290;
            } catch (NoSuchFieldError nosuchfielderror362) {
                ;
            }

            try {
                aint1[Material.WATER.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror363) {
                ;
            }

            try {
                aint1[Material.WATER_BUCKET.ordinal()] = 269;
            } catch (NoSuchFieldError nosuchfielderror364) {
                ;
            }

            try {
                aint1[Material.WATER_LILY.ordinal()] = 112;
            } catch (NoSuchFieldError nosuchfielderror365) {
                ;
            }

            try {
                aint1[Material.WEB.ordinal()] = 31;
            } catch (NoSuchFieldError nosuchfielderror366) {
                ;
            }

            try {
                aint1[Material.WHEAT.ordinal()] = 239;
            } catch (NoSuchFieldError nosuchfielderror367) {
                ;
            }

            try {
                aint1[Material.WOOD.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror368) {
                ;
            }

            try {
                aint1[Material.WOODEN_DOOR.ordinal()] = 65;
            } catch (NoSuchFieldError nosuchfielderror369) {
                ;
            }

            try {
                aint1[Material.WOOD_AXE.ordinal()] = 214;
            } catch (NoSuchFieldError nosuchfielderror370) {
                ;
            }

            try {
                aint1[Material.WOOD_BUTTON.ordinal()] = 144;
            } catch (NoSuchFieldError nosuchfielderror371) {
                ;
            }

            try {
                aint1[Material.WOOD_DOOR.ordinal()] = 267;
            } catch (NoSuchFieldError nosuchfielderror372) {
                ;
            }

            try {
                aint1[Material.WOOD_DOUBLE_STEP.ordinal()] = 126;
            } catch (NoSuchFieldError nosuchfielderror373) {
                ;
            }

            try {
                aint1[Material.WOOD_HOE.ordinal()] = 233;
            } catch (NoSuchFieldError nosuchfielderror374) {
                ;
            }

            try {
                aint1[Material.WOOD_PICKAXE.ordinal()] = 213;
            } catch (NoSuchFieldError nosuchfielderror375) {
                ;
            }

            try {
                aint1[Material.WOOD_PLATE.ordinal()] = 73;
            } catch (NoSuchFieldError nosuchfielderror376) {
                ;
            }

            try {
                aint1[Material.WOOD_SPADE.ordinal()] = 212;
            } catch (NoSuchFieldError nosuchfielderror377) {
                ;
            }

            try {
                aint1[Material.WOOD_STAIRS.ordinal()] = 54;
            } catch (NoSuchFieldError nosuchfielderror378) {
                ;
            }

            try {
                aint1[Material.WOOD_STEP.ordinal()] = 127;
            } catch (NoSuchFieldError nosuchfielderror379) {
                ;
            }

            try {
                aint1[Material.WOOD_SWORD.ordinal()] = 211;
            } catch (NoSuchFieldError nosuchfielderror380) {
                ;
            }

            try {
                aint1[Material.WOOL.ordinal()] = 36;
            } catch (NoSuchFieldError nosuchfielderror381) {
                ;
            }

            try {
                aint1[Material.WORKBENCH.ordinal()] = 59;
            } catch (NoSuchFieldError nosuchfielderror382) {
                ;
            }

            try {
                aint1[Material.WRITTEN_BOOK.ordinal()] = 330;
            } catch (NoSuchFieldError nosuchfielderror383) {
                ;
            }

            try {
                aint1[Material.YELLOW_FLOWER.ordinal()] = 38;
            } catch (NoSuchFieldError nosuchfielderror384) {
                ;
            }

            CraftEventFactory.$SWITCH_TABLE$org$bukkit$Material = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$entity$EntityType() {
        int[] aint = CraftEventFactory.$SWITCH_TABLE$org$bukkit$entity$EntityType;

        if (CraftEventFactory.$SWITCH_TABLE$org$bukkit$entity$EntityType != null) {
            return aint;
        } else {
            int[] aint1 = new int[EntityType.values().length];

            try {
                aint1[EntityType.ARMOR_STAND.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[EntityType.ARROW.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[EntityType.BAT.ordinal()] = 41;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[EntityType.BLAZE.ordinal()] = 37;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[EntityType.BOAT.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[EntityType.CAVE_SPIDER.ordinal()] = 35;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[EntityType.CHICKEN.ordinal()] = 48;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[EntityType.COMPLEX_PART.ordinal()] = 65;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[EntityType.COW.ordinal()] = 47;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[EntityType.CREEPER.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[EntityType.DROPPED_ITEM.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[EntityType.EGG.ordinal()] = 60;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[EntityType.ENDERMAN.ordinal()] = 34;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[EntityType.ENDERMITE.ordinal()] = 43;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[EntityType.ENDER_CRYSTAL.ordinal()] = 58;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[EntityType.ENDER_DRAGON.ordinal()] = 39;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[EntityType.ENDER_PEARL.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[EntityType.ENDER_SIGNAL.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[EntityType.EXPERIENCE_ORB.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[EntityType.FALLING_BLOCK.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[EntityType.FIREBALL.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[EntityType.FIREWORK.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[EntityType.FISHING_HOOK.ordinal()] = 61;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[EntityType.GHAST.ordinal()] = 32;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[EntityType.GIANT.ordinal()] = 29;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[EntityType.GUARDIAN.ordinal()] = 44;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            try {
                aint1[EntityType.HORSE.ordinal()] = 55;
            } catch (NoSuchFieldError nosuchfielderror26) {
                ;
            }

            try {
                aint1[EntityType.IRON_GOLEM.ordinal()] = 54;
            } catch (NoSuchFieldError nosuchfielderror27) {
                ;
            }

            try {
                aint1[EntityType.ITEM_FRAME.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror28) {
                ;
            }

            try {
                aint1[EntityType.LEASH_HITCH.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror29) {
                ;
            }

            try {
                aint1[EntityType.LIGHTNING.ordinal()] = 62;
            } catch (NoSuchFieldError nosuchfielderror30) {
                ;
            }

            try {
                aint1[EntityType.MAGMA_CUBE.ordinal()] = 38;
            } catch (NoSuchFieldError nosuchfielderror31) {
                ;
            }

            try {
                aint1[EntityType.MINECART.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror32) {
                ;
            }

            try {
                aint1[EntityType.MINECART_CHEST.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror33) {
                ;
            }

            try {
                aint1[EntityType.MINECART_COMMAND.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror34) {
                ;
            }

            try {
                aint1[EntityType.MINECART_FURNACE.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror35) {
                ;
            }

            try {
                aint1[EntityType.MINECART_HOPPER.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror36) {
                ;
            }

            try {
                aint1[EntityType.MINECART_MOB_SPAWNER.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror37) {
                ;
            }

            try {
                aint1[EntityType.MINECART_TNT.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror38) {
                ;
            }

            try {
                aint1[EntityType.MUSHROOM_COW.ordinal()] = 51;
            } catch (NoSuchFieldError nosuchfielderror39) {
                ;
            }

            try {
                aint1[EntityType.OCELOT.ordinal()] = 53;
            } catch (NoSuchFieldError nosuchfielderror40) {
                ;
            }

            try {
                aint1[EntityType.PAINTING.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror41) {
                ;
            }

            try {
                aint1[EntityType.PIG.ordinal()] = 45;
            } catch (NoSuchFieldError nosuchfielderror42) {
                ;
            }

            try {
                aint1[EntityType.PIG_ZOMBIE.ordinal()] = 33;
            } catch (NoSuchFieldError nosuchfielderror43) {
                ;
            }

            try {
                aint1[EntityType.PLAYER.ordinal()] = 64;
            } catch (NoSuchFieldError nosuchfielderror44) {
                ;
            }

            try {
                aint1[EntityType.PRIMED_TNT.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror45) {
                ;
            }

            try {
                aint1[EntityType.RABBIT.ordinal()] = 56;
            } catch (NoSuchFieldError nosuchfielderror46) {
                ;
            }

            try {
                aint1[EntityType.SHEEP.ordinal()] = 46;
            } catch (NoSuchFieldError nosuchfielderror47) {
                ;
            }

            try {
                aint1[EntityType.SILVERFISH.ordinal()] = 36;
            } catch (NoSuchFieldError nosuchfielderror48) {
                ;
            }

            try {
                aint1[EntityType.SKELETON.ordinal()] = 27;
            } catch (NoSuchFieldError nosuchfielderror49) {
                ;
            }

            try {
                aint1[EntityType.SLIME.ordinal()] = 31;
            } catch (NoSuchFieldError nosuchfielderror50) {
                ;
            }

            try {
                aint1[EntityType.SMALL_FIREBALL.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror51) {
                ;
            }

            try {
                aint1[EntityType.SNOWBALL.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror52) {
                ;
            }

            try {
                aint1[EntityType.SNOWMAN.ordinal()] = 52;
            } catch (NoSuchFieldError nosuchfielderror53) {
                ;
            }

            try {
                aint1[EntityType.SPIDER.ordinal()] = 28;
            } catch (NoSuchFieldError nosuchfielderror54) {
                ;
            }

            try {
                aint1[EntityType.SPLASH_POTION.ordinal()] = 59;
            } catch (NoSuchFieldError nosuchfielderror55) {
                ;
            }

            try {
                aint1[EntityType.SQUID.ordinal()] = 49;
            } catch (NoSuchFieldError nosuchfielderror56) {
                ;
            }

            try {
                aint1[EntityType.THROWN_EXP_BOTTLE.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror57) {
                ;
            }

            try {
                aint1[EntityType.UNKNOWN.ordinal()] = 66;
            } catch (NoSuchFieldError nosuchfielderror58) {
                ;
            }

            try {
                aint1[EntityType.VILLAGER.ordinal()] = 57;
            } catch (NoSuchFieldError nosuchfielderror59) {
                ;
            }

            try {
                aint1[EntityType.WEATHER.ordinal()] = 63;
            } catch (NoSuchFieldError nosuchfielderror60) {
                ;
            }

            try {
                aint1[EntityType.WITCH.ordinal()] = 42;
            } catch (NoSuchFieldError nosuchfielderror61) {
                ;
            }

            try {
                aint1[EntityType.WITHER.ordinal()] = 40;
            } catch (NoSuchFieldError nosuchfielderror62) {
                ;
            }

            try {
                aint1[EntityType.WITHER_SKULL.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror63) {
                ;
            }

            try {
                aint1[EntityType.WOLF.ordinal()] = 50;
            } catch (NoSuchFieldError nosuchfielderror64) {
                ;
            }

            try {
                aint1[EntityType.ZOMBIE.ordinal()] = 30;
            } catch (NoSuchFieldError nosuchfielderror65) {
                ;
            }

            CraftEventFactory.$SWITCH_TABLE$org$bukkit$entity$EntityType = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$Statistic() {
        int[] aint = CraftEventFactory.$SWITCH_TABLE$org$bukkit$Statistic;

        if (CraftEventFactory.$SWITCH_TABLE$org$bukkit$Statistic != null) {
            return aint;
        } else {
            int[] aint1 = new int[org.bukkit.Statistic.values().length];

            try {
                aint1[org.bukkit.Statistic.ANIMALS_BRED.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.ARMOR_CLEANED.ordinal()] = 38;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.BANNER_CLEANED.ordinal()] = 39;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.BEACON_INTERACTION.ordinal()] = 41;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.BOAT_ONE_CM.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.BREAK_ITEM.ordinal()] = 28;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.BREWINGSTAND_INTERACTION.ordinal()] = 40;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CAKE_SLICES_EATEN.ordinal()] = 35;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CAULDRON_FILLED.ordinal()] = 36;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CAULDRON_USED.ordinal()] = 37;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CHEST_OPENED.ordinal()] = 54;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CLIMB_ONE_CM.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CRAFTING_TABLE_INTERACTION.ordinal()] = 53;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CRAFT_ITEM.ordinal()] = 29;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.CROUCH_ONE_CM.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.DAMAGE_DEALT.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.DAMAGE_TAKEN.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.DEATHS.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.DISPENSER_INSPECTED.ordinal()] = 44;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.DIVE_ONE_CM.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.DROP.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.DROPPER_INSPECTED.ordinal()] = 42;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.ENDERCHEST_OPENED.ordinal()] = 49;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.ENTITY_KILLED_BY.ordinal()] = 31;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.FALL_ONE_CM.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.FISH_CAUGHT.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.FLOWER_POTTED.ordinal()] = 47;
            } catch (NoSuchFieldError nosuchfielderror26) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.FLY_ONE_CM.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror27) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.FURNACE_INTERACTION.ordinal()] = 52;
            } catch (NoSuchFieldError nosuchfielderror28) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.HOPPER_INSPECTED.ordinal()] = 43;
            } catch (NoSuchFieldError nosuchfielderror29) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.HORSE_ONE_CM.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror30) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.ITEM_ENCHANTED.ordinal()] = 50;
            } catch (NoSuchFieldError nosuchfielderror31) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.JUMP.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror32) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.JUNK_FISHED.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror33) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.KILL_ENTITY.ordinal()] = 30;
            } catch (NoSuchFieldError nosuchfielderror34) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.LEAVE_GAME.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror35) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.MINECART_ONE_CM.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror36) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.MINE_BLOCK.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror37) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.MOB_KILLS.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror38) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.NOTEBLOCK_PLAYED.ordinal()] = 45;
            } catch (NoSuchFieldError nosuchfielderror39) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.NOTEBLOCK_TUNED.ordinal()] = 46;
            } catch (NoSuchFieldError nosuchfielderror40) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.PIG_ONE_CM.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror41) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.PLAYER_KILLS.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror42) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.PLAY_ONE_TICK.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror43) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.RECORD_PLAYED.ordinal()] = 51;
            } catch (NoSuchFieldError nosuchfielderror44) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.SPRINT_ONE_CM.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror45) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.SWIM_ONE_CM.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror46) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.TALKED_TO_VILLAGER.ordinal()] = 33;
            } catch (NoSuchFieldError nosuchfielderror47) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.TIME_SINCE_DEATH.ordinal()] = 32;
            } catch (NoSuchFieldError nosuchfielderror48) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.TRADED_WITH_VILLAGER.ordinal()] = 34;
            } catch (NoSuchFieldError nosuchfielderror49) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.TRAPPED_CHEST_TRIGGERED.ordinal()] = 48;
            } catch (NoSuchFieldError nosuchfielderror50) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.TREASURE_FISHED.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror51) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.USE_ITEM.ordinal()] = 27;
            } catch (NoSuchFieldError nosuchfielderror52) {
                ;
            }

            try {
                aint1[org.bukkit.Statistic.WALK_ONE_CM.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror53) {
                ;
            }

            CraftEventFactory.$SWITCH_TABLE$org$bukkit$Statistic = aint1;
            return aint1;
        }
    }
}
