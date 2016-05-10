package org.bukkit.craftbukkit.v1_8_R3;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.kookykraftmc.lunchbox.LunchBox;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockDiodeAbstract;
import net.minecraft.server.v1_8_R3.BlockLeaves;
import net.minecraft.server.v1_8_R3.BlockLeaves1;
import net.minecraft.server.v1_8_R3.BlockLog1;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockWood;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChunkProviderServer;
import net.minecraft.server.v1_8_R3.EmptyChunk;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityEnderSignal;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityEndermite;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHanging;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLeash;
import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntityMinecartChest;
import net.minecraft.server.v1_8_R3.EntityMinecartFurnace;
import net.minecraft.server.v1_8_R3.EntityMinecartHopper;
import net.minecraft.server.v1_8_R3.EntityMinecartMobSpawner;
import net.minecraft.server.v1_8_R3.EntityMinecartRideable;
import net.minecraft.server.v1_8_R3.EntityMinecartTNT;
import net.minecraft.server.v1_8_R3.EntityMushroomCow;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.GroupDataEntity;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateTime;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.WorldGenAcaciaTree;
import net.minecraft.server.v1_8_R3.WorldGenBigTree;
import net.minecraft.server.v1_8_R3.WorldGenForest;
import net.minecraft.server.v1_8_R3.WorldGenForestTree;
import net.minecraft.server.v1_8_R3.WorldGenGroundBush;
import net.minecraft.server.v1_8_R3.WorldGenHugeMushroom;
import net.minecraft.server.v1_8_R3.WorldGenJungleTree;
import net.minecraft.server.v1_8_R3.WorldGenMegaTree;
import net.minecraft.server.v1_8_R3.WorldGenSwampTree;
import net.minecraft.server.v1_8_R3.WorldGenTaiga1;
import net.minecraft.server.v1_8_R3.WorldGenTaiga2;
import net.minecraft.server.v1_8_R3.WorldGenTrees;
import net.minecraft.server.v1_8_R3.WorldGenerator;
import net.minecraft.server.v1_8_R3.WorldNBTStorage;
import net.minecraft.server.v1_8_R3.WorldProvider;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.gen.feature.*;
import org.apache.commons.lang.Validate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLightningStrike;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Cow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Horse;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Weather;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.util.Vector;
import org.spigotmc.AsyncCatcher;

public class CraftWorld implements World {

    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private final WorldServer world;
    private WorldBorder worldBorder;
    private World.Environment environment;
    private final CraftServer server = (CraftServer) Bukkit.getServer();
    private final ChunkGenerator generator;
    private final List populators = new ArrayList();
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int ambientSpawn = -1;
    private int chunkLoadCount = 0;
    private int chunkGCTickCount;
    private static final Random rand = new Random();
    /* LunchBox - Remove all spigot stuff for now.
    private final World.Spigot spigot = new World.Spigot() {
        public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
            Validate.notNull(location, "Location cannot be null");
            Validate.notNull(effect, "Effect cannot be null");
            Validate.notNull(location.getWorld(), "World cannot be null");
            int distance;
            Object packet;

            if (effect.getType() != Effect.Type.PARTICLE) {
                distance = effect.getId();
                packet = new PacketPlayOutWorldEvent(distance, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id, false);
            } else {
                EnumParticleTypes enumparticle = null;
                int[] player = null;
                EnumParticleTypes[] aenumparticle;
                int i = (aenumparticle = EnumParticleTypes.values()).length;

                for (int j = 0; j < i; ++j) {
                    EnumParticleTypes p = aenumparticle[j];

                    if (effect.getName().startsWith(p.getParticleName().replace("_", ""))) {
                        enumparticle = p;
                        if (effect.getData() != null) {
                            if (effect.getData().equals(Material.class)) {
                                player = new int[] { id};
                            } else {
                                player = new int[] { data << 12 | id & 4095};
                            }
                        }
                        break;
                    }
                }

                if (player == null) {
                    player = new int[0];
                }

                packet = new PacketPlayOutWorldParticles(enumparticle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, player);
            }

            radius *= radius;
            Iterator iterator = CraftWorld.this.getPlayers().iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();

                if (((CraftPlayer) player).getHandle().playerConnection != null && location.getWorld().equals(player.getWorld())) {
                    distance = (int) player.getLocation().distanceSquared(location);
                    if (distance <= radius) {
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
                    }
                }
            }

        }

        public void playEffect(Location location, Effect effect) {
            CraftWorld.this.playEffect(location, effect, 0);
        }

        public LightningStrike strikeLightning(Location loc, boolean isSilent) {
            EntityLightning lightning = new EntityLightning(CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), false, isSilent);

            CraftWorld.this.world.strikeLightning(lightning);
            return new CraftLightningStrike(CraftWorld.this.server, lightning);
        }

        public LightningStrike strikeLightningEffect(Location loc, boolean isSilent) {
            EntityLightning lightning = new EntityLightning(CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), true, isSilent);

            CraftWorld.this.world.strikeLightning(lightning);
            return new CraftLightningStrike(CraftWorld.this.server, lightning);
        }
    };
    */
    private static int[] $SWITCH_TABLE$org$bukkit$TreeType;

    public CraftWorld(WorldServer world, ChunkGenerator gen, World.Environment env) {
        this.world = world;
        this.generator = gen;
        this.environment = env;
        if (this.server.chunkGCPeriod > 0) {
            this.chunkGCTickCount = CraftWorld.rand.nextInt(this.server.chunkGCPeriod);
        }

    }

    public Block getBlockAt(int x, int y, int z) {
        return this.getChunkAt(x >> 4, z >> 4).getBlock(x & 15, y, z & 15);
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return CraftMagicNumbers.getId(this.world.getBlockState(new BlockPos(x, y, z)).getBlock());
    }

    public int getHighestBlockYAt(int x, int z) {
        if (!this.isChunkLoaded(x >> 4, z >> 4)) {
            this.loadChunk(x >> 4, z >> 4);
        }

        return this.world.getActualHeight();
    }

    public Location getSpawnLocation() {
        BlockPos spawn = this.world.getSpawnPoint();

        return new Location(this, (double) spawn.getX(), (double) spawn.getY(), (double) spawn.getZ());
    }

    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            Location previousLocation = this.getSpawnLocation();

            this.world.getWorldInfo().setSpawn(new BlockPos(x, y, z));
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);

            this.server.getPluginManager().callEvent(event);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public Chunk getChunkAt(int x, int z) {
        return (Chunk) this.world.theChunkProviderServer.provideChunk(x, z);
    }

    public Chunk getChunkAt(Block block) {
        return this.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    public boolean isChunkLoaded(int x, int z) {
        return this.world.theChunkProviderServer.chunkExists(x, z);
    }

    public Chunk[] getLoadedChunks() {
        Object[] chunks = this.world.theChunkProviderServer.loadedChunks.toArray();
        CraftChunk[] craftChunks = new CraftChunk[chunks.length];

        for (int i = 0; i < chunks.length; ++i) {
            net.minecraft.world.chunk.Chunk chunk = (net.minecraft.world.chunk.Chunk) chunks[i];

            craftChunks[i] = new CraftChunk(chunk);
        }

        return craftChunks;
    }

    public void loadChunk(int x, int z) {
        this.loadChunk(x, z, true);
    }

    public boolean unloadChunk(Chunk chunk) {
        return this.unloadChunk(chunk.getX(), chunk.getZ());
    }

    public boolean unloadChunk(int x, int z) {
        return this.unloadChunk(x, z, true);
    }

    public boolean unloadChunk(int x, int z, boolean save) {
        return this.unloadChunk(x, z, save, false);
    }

    public boolean unloadChunkRequest(int x, int z) {
        return this.unloadChunkRequest(x, z, true);
    }

    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        AsyncCatcher.catchOp("chunk unload");
        if (safe && this.isChunkInUse(x, z)) {
            return false;
        } else {
            this.world.theChunkProviderServer.dropChunk(x, z);//TODO: not sure if drop chunk is the correct method for unloading chunks
            return true;
        }
    }

    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        if (isChunkInUse(x, z)) {
            return false;
        }
        if (world.theChunkProviderServer.loadedChunks.contains(getChunkAt(x, z))) {//TODO: not sure if there is a way to find chunks that are to be unloaded. Will look into later.
            return true;
        }
        world.theChunkProviderServer.dropChunk(x, z);
        return true;
    }
    //TODO: Rework most of this method to work with forge.
    public boolean regenerateChunk(int x, int z) {
        unloadChunk(x, z, false, false);

        net.minecraft.world.chunk.Chunk chunk = null;

        if (world.theChunkProviderServer.serverChunkGenerator == null) {
            chunk = world.theChunkProviderServer.defaultEmptyChunk;
        } else {
            chunk = world.theChunkProviderServer.serverChunkGenerator.provideChunk(x, z);
        }

        chunkLoadPostProcess(chunk, x, z);

        refreshChunk(x, z);

        return chunk != null;
    }

    public boolean refreshChunk(int x, int z) {
        if (!this.isChunkLoaded(x, z)) {
            return false;
        } else {
            int px = x << 4;
            int pz = z << 4;
            int height = this.getMaxHeight() / 16;
            BlockPos pos;
            for (int idx = 0; idx < 64; ++idx) {
                pos = new BlockPos((double) (px + idx / height), 0, (double) idx % height * 16);
                this.world.markBlockForUpdate(pos);
            }

            this.world.markBlockForUpdate(new BlockPos(px + 15, height * 16 - 1, pz + 15));
            return true;
        }
    }

    public boolean isChunkInUse(int x, int z) {
        if (world.theChunkProviderServer.chunkExists(x, z)) {
            return true;
        }
        return false;
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        AsyncCatcher.catchOp("chunk load");
        ++this.chunkLoadCount;
        if (generate) {
            return this.world.theChunkProviderServer.provideChunk(x, z) != null;
        } else {
            //this.world.theChunkProviderServer.unloadQueue.remove(x, z);//TODO: removed this for now, need to find a method to replace it later.
            net.minecraft.world.chunk.Chunk chunk = (net.minecraft.world.chunk.Chunk) this.world.theChunkProviderServer.loadedChunks.get((int) LongHash.toLong(x, z));
            /* LunchBox - remove timings for now.
            if (chunk == null) {
                this.world.timings.syncChunkLoadTimer.startTiming();
                chunk = this.world.theChunkProviderServer.loadChunk(x, z);
                this.chunkLoadPostProcess(chunk, x, z);
                this.world.timings.syncChunkLoadTimer.stopTiming();
            }*/

            return chunk != null;
        }
    }

    private void chunkLoadPostProcess(net.minecraft.world.chunk.Chunk chunk, int cx, int cz) {
        if (chunk != null) {
            this.world.theChunkProviderServer.loadedChunks.add(chunk);
            //chunk.addEntities(); //LunchBox - remove for now.

            for (int x = -2; x < 3; ++x) {
                for (int z = -2; z < 3; ++z) {
                    if (x != 0 || z != 0) {
                        net.minecraft.world.chunk.Chunk neighbor = this.world.theChunkProviderServer.provideChunk(chunk.xPosition + x, chunk.zPosition + z);
                        if (neighbor != null) {
                            //TODO: not sure if this is correct or not.
                            neighbor.setChunkLoaded(true);
                            chunk.setChunkLoaded(true);
                        }
                    }
                }
            }
            //TODO: rework this.
            chunk.loadNearby(this.world.theChunkProviderServer, this.world.theChunkProviderServer, cx, cz);
        }

    }

    public boolean isChunkLoaded(Chunk chunk) {
        return this.isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    public void loadChunk(net.minecraft.world.chunk.Chunk chunk) {
        this.loadChunk(chunk.xPosition, chunk.zPosition);
    }

    public WorldServer getHandle() {
        return this.world;
    }

    public Item dropItem(Location loc, ItemStack item) {
        Validate.notNull(item, "Cannot drop a Null item.");
        Validate.isTrue(item.getTypeId() != 0, "Cannot drop AIR.");
        EntityItem entity = new EntityItem(this.world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));

        entity.setPickupDelay(10);
        this.world.spawnEntityInWorld(entity);
        return new CraftItem(LunchBox.getServer(), entity);
    }

    private static void randomLocationWithinBlock(Location loc, double xs, double ys, double zs) {
        double prevX = loc.getX();
        double prevY = loc.getY();
        double prevZ = loc.getZ();

        loc.add(xs, ys, zs);
        if (loc.getX() < Math.floor(prevX)) {
            loc.setX(Math.floor(prevX));
        }

        if (loc.getX() >= Math.ceil(prevX)) {
            loc.setX(Math.ceil(prevX - 0.01D));
        }

        if (loc.getY() < Math.floor(prevY)) {
            loc.setY(Math.floor(prevY));
        }

        if (loc.getY() >= Math.ceil(prevY)) {
            loc.setY(Math.ceil(prevY - 0.01D));
        }

        if (loc.getZ() < Math.floor(prevZ)) {
            loc.setZ(Math.floor(prevZ));
        }

        if (loc.getZ() >= Math.ceil(prevZ)) {
            loc.setZ(Math.ceil(prevZ - 0.01D));
        }

    }

    public Item dropItemNaturally(Location loc, ItemStack item) {
        double xs = (double) (this.world.rand.nextFloat() * 0.7F) - 0.35D;
        double ys = (double) (this.world.rand.nextFloat() * 0.7F) - 0.35D;
        double zs = (double) (this.world.rand.nextFloat() * 0.7F) - 0.35D;

        loc = loc.clone();
        randomLocationWithinBlock(loc, xs, ys, zs);
        return this.dropItem(loc, item);
    }

    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        Validate.notNull(loc, "Can not spawn arrow with a null location");
        Validate.notNull(velocity, "Can not spawn arrow with a null velocity");
        EntityArrow arrow = new EntityArrow(this.world);

        arrow.setPositionAndRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());

        this.world.spawnEntityInWorld(arrow);
        return (Arrow) CraftEntity.getEntity(this.server, arrow);
    }

    /** @deprecated */
    @Deprecated
    public LivingEntity spawnCreature(Location loc, CreatureType creatureType) {
        return this.spawnCreature(loc, creatureType.toEntityType());
    }

    /** @deprecated */
    @Deprecated
    public LivingEntity spawnCreature(Location loc, EntityType creatureType) {
        Validate.isTrue(creatureType.isAlive(), "EntityType not instance of LivingEntity");
        return (LivingEntity) this.spawnEntity(loc, creatureType);
    }

    public Entity spawnEntity(Location loc, EntityType entityType) {
        return this.spawn(loc, entityType.getEntityClass());
    }

    public LightningStrike strikeLightning(Location loc) {
        EntityLightningBolt lightning = new EntityLightningBolt(this.world, loc.getX(), loc.getY(), loc.getZ());

        this.world.spawnEntityInWorld(lightning);
        return new CraftLightningStrike(this.server, lightning);
    }

    public LightningStrike strikeLightningEffect(Location loc) {
        EntityLightningBolt lightning = new EntityLightningBolt(this.world, loc.getX(), loc.getY(), loc.getZ());

        this.world.spawnEntityInWorld(lightning);
        return new CraftLightningStrike(this.server, lightning);
    }

    public boolean generateTree(Location loc, TreeType type) {
        Object gen;
        IBlockState iblockdata1;
        IBlockState iblockdata2;

        switch ($SWITCH_TABLE$org$bukkit$TreeType()[type.ordinal()]) {
        case 1:
        default:
            gen = new WorldGenTrees(true);
            break;

        case 2:
            gen = new WorldGenBigTree(true);
            break;

        case 3:
            gen = new WorldGenTaiga2(true);
            break;

        case 4:
            gen = new WorldGenTaiga1();
            break;

        case 5:
            gen = new WorldGenForest(true, false);
            break;

        case 6:
            //TODO: get wood types.
            iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + CraftWorld.rand.nextInt(7), iblockdata1, iblockdata2, false);
            break;

        case 7:
            iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + CraftWorld.rand.nextInt(7), iblockdata1, iblockdata2, false);
            break;

        case 8:
            iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + CraftWorld.rand.nextInt(7), iblockdata1, iblockdata2, true);
            break;

        case 9:
            iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenGroundBush(iblockdata1, iblockdata2);
            break;

        case 10:
            gen = new WorldGenBigMushroom(Blocks.red_mushroom_block);
            break;

        case 11:
            gen = new WorldGenBigMushroom(Blocks.brown_mushroom_block);
            break;

        case 12:
            gen = new WorldGenSwamp();//todo: not sure if this is worldGenSwampTrees
            break;

        case 13:
            gen = new WorldGenSavannaTree(true);
            break;

        case 14:
            gen = new WorldGenTrees(true);
            break;

        case 15:
            gen = new WorldGenBigTree(CraftWorld.rand.nextBoolean());
            break;

        case 16:
            gen = new WorldGenForest(true, true);
        }

        return ((WorldGenerator) gen).generate(this.world, CraftWorld.rand, new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        this.world.captureTreeGeneration = true;
        this.world.captureBlockStates = true;
        boolean grownTree = this.generateTree(loc, type);

        this.world.captureBlockStates = false;
        this.world.captureTreeGeneration = false;
        if (!grownTree) {
            this.world.capturedBlockStates.clear();
            return false;
        } else {
            Iterator iterator = this.world.capturedBlockStates.iterator();

            while (iterator.hasNext()) {
                BlockState blockstate = (BlockState) iterator.next();
                int x = blockstate.getX();
                int y = blockstate.getY();
                int z = blockstate.getZ();
                BlockPosition position = new BlockPosition(x, y, z);
                net.minecraft.server.v1_8_R3.Block oldBlock = this.world.getType(position).getBlock();
                int typeId = blockstate.getTypeId();
                byte data = blockstate.getRawData();
                int flag = ((CraftBlockState) blockstate).getFlag();

                delegate.setTypeIdAndData(x, y, z, typeId, data);
                net.minecraft.server.v1_8_R3.Block newBlock = this.world.getType(position).getBlock();

                this.world.notifyAndUpdatePhysics(position, (net.minecraft.server.v1_8_R3.Chunk) null, oldBlock, newBlock, flag);
            }

            this.world.capturedBlockStates.clear();
            return true;
        }
    }

    public TileEntity getTileEntityAt(int x, int y, int z) {
        return this.world.getTileEntity(new BlockPosition(x, y, z));
    }

    public String getName() {
        return this.world.worldData.getName();
    }

    /** @deprecated */
    @Deprecated
    public long getId() {
        return this.world.worldData.getSeed();
    }

    public UUID getUID() {
        return this.world.getDataManager().getUUID();
    }

    public String toString() {
        return "CraftWorld{name=" + this.getName() + '}';
    }

    public long getTime() {
        long time = this.getFullTime() % 24000L;

        if (time < 0L) {
            time += 24000L;
        }

        return time;
    }

    public void setTime(long time) {
        long margin = (time - this.getFullTime()) % 24000L;

        if (margin < 0L) {
            margin += 24000L;
        }

        this.setFullTime(this.getFullTime() + margin);
    }

    public long getFullTime() {
        return this.world.getDayTime();
    }

    public void setFullTime(long time) {
        this.world.setDayTime(time);
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            Player p = (Player) iterator.next();
            CraftPlayer cp = (CraftPlayer) p;

            if (cp.getHandle().playerConnection != null) {
                cp.getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateTime(cp.getHandle().world.getTime(), cp.getHandle().getPlayerTime(), cp.getHandle().world.getGameRules().getBoolean("doDaylightCycle")));
            }
        }

    }

    public boolean createExplosion(double x, double y, double z, float power) {
        return this.createExplosion(x, y, z, power, false, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return this.createExplosion(x, y, z, power, setFire, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return !this.world.createExplosion((net.minecraft.server.v1_8_R3.Entity) null, x, y, z, power, setFire, breakBlocks).wasCanceled;
    }

    public boolean createExplosion(Location loc, float power) {
        return this.createExplosion(loc, power, false);
    }

    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }

    public World.Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(World.Environment env) {
        if (this.environment != env) {
            this.environment = env;
            this.world.worldProvider = WorldProvider.byDimension(this.environment.getId());
        }

    }

    public Block getBlockAt(Location location) {
        return this.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getBlockTypeIdAt(Location location) {
        return this.getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getHighestBlockYAt(Location location) {
        return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    public Chunk getChunkAt(Location location) {
        return this.getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public ChunkGenerator getGenerator() {
        return this.generator;
    }

    public List getPopulators() {
        return this.populators;
    }

    public Block getHighestBlockAt(int x, int z) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z), z);
    }

    public Block getHighestBlockAt(Location location) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    public Biome getBiome(int x, int z) {
        return CraftBlock.biomeBaseToBiome(this.world.getBiome(new BlockPosition(x, 0, z)));
    }

    public void setBiome(int x, int z, Biome bio) {
        BiomeBase bb = CraftBlock.biomeToBiomeBase(bio);

        if (this.world.isLoaded(new BlockPosition(x, 0, z))) {
            net.minecraft.server.v1_8_R3.Chunk chunk = this.world.getChunkAtWorldCoords(new BlockPosition(x, 0, z));

            if (chunk != null) {
                byte[] biomevals = chunk.getBiomeIndex();

                biomevals[(z & 15) << 4 | x & 15] = (byte) bb.id;
            }
        }

    }

    public double getTemperature(int x, int z) {
        return (double) this.world.getBiome(new BlockPosition(x, 0, z)).temperature;
    }

    public double getHumidity(int x, int z) {
        return (double) this.world.getBiome(new BlockPosition(x, 0, z)).humidity;
    }

    public List getEntities() {
        ArrayList list = new ArrayList();
        Iterator iterator = this.world.entityList.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();

            if (o instanceof net.minecraft.server.v1_8_R3.Entity) {
                net.minecraft.server.v1_8_R3.Entity mcEnt = (net.minecraft.server.v1_8_R3.Entity) o;
                CraftEntity bukkitEntity = mcEnt.getBukkitEntity();

                if (bukkitEntity != null) {
                    list.add(bukkitEntity);
                }
            }
        }

        return list;
    }

    public List getLivingEntities() {
        ArrayList list = new ArrayList();
        Iterator iterator = this.world.entityList.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();

            if (o instanceof net.minecraft.server.v1_8_R3.Entity) {
                net.minecraft.server.v1_8_R3.Entity mcEnt = (net.minecraft.server.v1_8_R3.Entity) o;
                CraftEntity bukkitEntity = mcEnt.getBukkitEntity();

                if (bukkitEntity != null && bukkitEntity instanceof LivingEntity) {
                    list.add((LivingEntity) bukkitEntity);
                }
            }
        }

        return list;
    }

    /** @deprecated */
    @Deprecated
    public Collection getEntitiesByClass(Class... classes) {
        return this.getEntitiesByClasses(classes);
    }

    public Collection getEntitiesByClass(Class clazz) {
        ArrayList list = new ArrayList();
        Iterator iterator = this.world.entityList.iterator();

        while (iterator.hasNext()) {
            Object entity = iterator.next();

            if (entity instanceof net.minecraft.server.v1_8_R3.Entity) {
                CraftEntity bukkitEntity = ((net.minecraft.server.v1_8_R3.Entity) entity).getBukkitEntity();

                if (bukkitEntity != null) {
                    Class bukkitClass = bukkitEntity.getClass();

                    if (clazz.isAssignableFrom(bukkitClass)) {
                        list.add(bukkitEntity);
                    }
                }
            }
        }

        return list;
    }

    public Collection getEntitiesByClasses(Class... classes) {
        ArrayList list = new ArrayList();
        Iterator iterator = this.world.entityList.iterator();

        while (iterator.hasNext()) {
            Object entity = iterator.next();

            if (entity instanceof net.minecraft.server.v1_8_R3.Entity) {
                CraftEntity bukkitEntity = ((net.minecraft.server.v1_8_R3.Entity) entity).getBukkitEntity();

                if (bukkitEntity != null) {
                    Class bukkitClass = bukkitEntity.getClass();
                    Class[] aclass = classes;
                    int i = classes.length;

                    for (int j = 0; j < i; ++j) {
                        Class clazz = aclass[j];

                        if (clazz.isAssignableFrom(bukkitClass)) {
                            list.add(bukkitEntity);
                            break;
                        }
                    }
                }
            }
        }

        return list;
    }

    public Collection getNearbyEntities(Location location, double x, double y, double z) {
        if (location != null && location.getWorld().equals(this)) {
            AxisAlignedBB bb = new AxisAlignedBB(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
            List entityList = this.getHandle().a((net.minecraft.server.v1_8_R3.Entity) null, bb, (Predicate) null);
            ArrayList bukkitEntityList = new ArrayList(entityList.size());
            Iterator iterator = entityList.iterator();

            while (iterator.hasNext()) {
                Object entity = iterator.next();

                bukkitEntityList.add(((net.minecraft.server.v1_8_R3.Entity) entity).getBukkitEntity());
            }

            return bukkitEntityList;
        } else {
            return Collections.emptyList();
        }
    }

    public List getPlayers() {
        ArrayList list = new ArrayList(this.world.players.size());
        Iterator iterator = this.world.players.iterator();

        while (iterator.hasNext()) {
            EntityHuman human = (EntityHuman) iterator.next();
            CraftHumanEntity bukkitEntity = human.getBukkitEntity();

            if (bukkitEntity != null && bukkitEntity instanceof Player) {
                list.add((Player) bukkitEntity);
            }
        }

        return list;
    }

    public void save() {
        this.save(true);
    }

    public void save(boolean forceSave) {
        this.server.checkSaveState();

        try {
            boolean ex = this.world.savingDisabled;

            this.world.savingDisabled = false;
            this.world.save(forceSave, (IProgressUpdate) null);
            this.world.savingDisabled = ex;
        } catch (ExceptionWorldConflict exceptionworldconflict) {
            exceptionworldconflict.printStackTrace();
        }

    }

    public boolean isAutoSave() {
        return !this.world.savingDisabled;
    }

    public void setAutoSave(boolean value) {
        this.world.savingDisabled = !value;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().worldData.setDifficulty(EnumDifficulty.getById(difficulty.getValue()));
    }

    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    public BlockMetadataStore getBlockMetadata() {
        return this.blockMetadata;
    }

    public boolean hasStorm() {
        return this.world.worldData.hasStorm();
    }

    public void setStorm(boolean hasStorm) {
        this.world.worldData.setStorm(hasStorm);
    }

    public int getWeatherDuration() {
        return this.world.worldData.getWeatherDuration();
    }

    public void setWeatherDuration(int duration) {
        this.world.worldData.setWeatherDuration(duration);
    }

    public boolean isThundering() {
        return this.world.worldData.isThundering();
    }

    public void setThundering(boolean thundering) {
        this.world.worldData.setThundering(thundering);
    }

    public int getThunderDuration() {
        return this.world.worldData.getThunderDuration();
    }

    public void setThunderDuration(int duration) {
        this.world.worldData.setThunderDuration(duration);
    }

    public long getSeed() {
        return this.world.worldData.getSeed();
    }

    public boolean getPVP() {
        return this.world.pvpMode;
    }

    public void setPVP(boolean pvp) {
        this.world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        this.playEffect(player.getLocation(), effect, data, 0);
    }

    public void playEffect(Location location, Effect effect, int data) {
        this.playEffect(location, effect, data, 64);
    }

    public void playEffect(Location loc, Effect effect, Object data) {
        this.playEffect(loc, effect, data, 64);
    }

    public void playEffect(Location loc, Effect effect, Object data, int radius) {
        if (data != null) {
            Validate.isTrue(data.getClass().isAssignableFrom(effect.getData()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        if (data != null && data.getClass().equals(MaterialData.class)) {
            MaterialData dataValue1 = (MaterialData) data;

            Validate.isTrue(dataValue1.getItemType().isBlock(), "Material must be block");
            this.spigot().playEffect(loc, effect, dataValue1.getItemType().getId(), dataValue1.getData(), 0.0F, 0.0F, 0.0F, 1.0F, 1, radius);
        } else {
            int dataValue = data == null ? 0 : CraftEffect.getDataValue(effect, data);

            this.playEffect(loc, effect, dataValue, radius);
        }

    }

    public void playEffect(Location location, Effect effect, int data, int radius) {
        this.spigot().playEffect(location, effect, data, 0, 0.0F, 0.0F, 0.0F, 1.0F, 1, radius);
    }

    public Entity spawn(Location location, Class clazz) throws IllegalArgumentException {
        return this.spawn(location, clazz, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public FallingBlock spawnFallingBlock(Location location, Material material, byte data) throws IllegalArgumentException {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");
        double x = (double) location.getBlockX() + 0.5D;
        double y = (double) location.getBlockY() + 0.5D;
        double z = (double) location.getBlockZ() + 0.5D;
        EntityFallingBlock entity = new EntityFallingBlock(this.world, x, y, z, net.minecraft.server.v1_8_R3.Block.getById(material.getId()).fromLegacyData(data));

        entity.ticksLived = 1;
        this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (FallingBlock) entity.getBukkitEntity();
    }

    public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData) throws IllegalArgumentException {
        return this.spawnFallingBlock(location, Material.getMaterial(blockId), blockData);
    }

    public net.minecraft.server.v1_8_R3.Entity createEntity(Location location, Class clazz) throws IllegalArgumentException {
        if (location != null && clazz != null) {
            Object entity = null;
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            float pitch = location.getPitch();
            float yaw = location.getYaw();

            if (Boat.class.isAssignableFrom(clazz)) {
                entity = new EntityBoat(this.world, x, y, z);
            } else if (FallingBlock.class.isAssignableFrom(clazz)) {
                x = (double) location.getBlockX();
                y = (double) location.getBlockY();
                z = (double) location.getBlockZ();
                IBlockData block = this.world.getType(new BlockPosition(x, y, z));
                int face = CraftMagicNumbers.getId(block.getBlock());
                int width = block.getBlock().toLegacyData(block);

                entity = new EntityFallingBlock(this.world, x + 0.5D, y + 0.5D, z + 0.5D, net.minecraft.server.v1_8_R3.Block.getById(face).fromLegacyData(width));
            } else if (Projectile.class.isAssignableFrom(clazz)) {
                if (Snowball.class.isAssignableFrom(clazz)) {
                    entity = new EntitySnowball(this.world, x, y, z);
                } else if (Egg.class.isAssignableFrom(clazz)) {
                    entity = new EntityEgg(this.world, x, y, z);
                } else if (Arrow.class.isAssignableFrom(clazz)) {
                    entity = new EntityArrow(this.world);
                    ((net.minecraft.server.v1_8_R3.Entity) entity).setPositionRotation(x, y, z, 0.0F, 0.0F);
                } else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                    entity = new EntityThrownExpBottle(this.world);
                    ((net.minecraft.server.v1_8_R3.Entity) entity).setPositionRotation(x, y, z, 0.0F, 0.0F);
                } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                    entity = new EntityEnderPearl(this.world, (EntityLiving) null);
                    ((net.minecraft.server.v1_8_R3.Entity) entity).setPositionRotation(x, y, z, 0.0F, 0.0F);
                } else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                    entity = new EntityPotion(this.world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
                } else if (Fireball.class.isAssignableFrom(clazz)) {
                    if (SmallFireball.class.isAssignableFrom(clazz)) {
                        entity = new EntitySmallFireball(this.world);
                    } else if (WitherSkull.class.isAssignableFrom(clazz)) {
                        entity = new EntityWitherSkull(this.world);
                    } else {
                        entity = new EntityLargeFireball(this.world);
                    }

                    ((net.minecraft.server.v1_8_R3.Entity) entity).setPositionRotation(x, y, z, yaw, pitch);
                    Vector vector = location.getDirection().multiply(10);

                    ((EntityFireball) entity).setDirection(vector.getX(), vector.getY(), vector.getZ());
                }
            } else if (Minecart.class.isAssignableFrom(clazz)) {
                if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                    entity = new EntityMinecartFurnace(this.world, x, y, z);
                } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                    entity = new EntityMinecartChest(this.world, x, y, z);
                } else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                    entity = new EntityMinecartTNT(this.world, x, y, z);
                } else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                    entity = new EntityMinecartHopper(this.world, x, y, z);
                } else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                    entity = new EntityMinecartMobSpawner(this.world, x, y, z);
                } else {
                    entity = new EntityMinecartRideable(this.world, x, y, z);
                }
            } else if (EnderSignal.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderSignal(this.world, x, y, z);
            } else if (EnderCrystal.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderCrystal(this.world);
                ((net.minecraft.server.v1_8_R3.Entity) entity).setPositionRotation(x, y, z, 0.0F, 0.0F);
            } else if (LivingEntity.class.isAssignableFrom(clazz)) {
                if (Chicken.class.isAssignableFrom(clazz)) {
                    entity = new EntityChicken(this.world);
                } else if (Cow.class.isAssignableFrom(clazz)) {
                    if (MushroomCow.class.isAssignableFrom(clazz)) {
                        entity = new EntityMushroomCow(this.world);
                    } else {
                        entity = new EntityCow(this.world);
                    }
                } else if (Golem.class.isAssignableFrom(clazz)) {
                    if (Snowman.class.isAssignableFrom(clazz)) {
                        entity = new EntitySnowman(this.world);
                    } else if (IronGolem.class.isAssignableFrom(clazz)) {
                        entity = new EntityIronGolem(this.world);
                    }
                } else if (Creeper.class.isAssignableFrom(clazz)) {
                    entity = new EntityCreeper(this.world);
                } else if (Ghast.class.isAssignableFrom(clazz)) {
                    entity = new EntityGhast(this.world);
                } else if (Pig.class.isAssignableFrom(clazz)) {
                    entity = new EntityPig(this.world);
                } else if (!Player.class.isAssignableFrom(clazz)) {
                    if (Sheep.class.isAssignableFrom(clazz)) {
                        entity = new EntitySheep(this.world);
                    } else if (Horse.class.isAssignableFrom(clazz)) {
                        entity = new EntityHorse(this.world);
                    } else if (Skeleton.class.isAssignableFrom(clazz)) {
                        entity = new EntitySkeleton(this.world);
                    } else if (Slime.class.isAssignableFrom(clazz)) {
                        if (MagmaCube.class.isAssignableFrom(clazz)) {
                            entity = new EntityMagmaCube(this.world);
                        } else {
                            entity = new EntitySlime(this.world);
                        }
                    } else if (Spider.class.isAssignableFrom(clazz)) {
                        if (CaveSpider.class.isAssignableFrom(clazz)) {
                            entity = new EntityCaveSpider(this.world);
                        } else {
                            entity = new EntitySpider(this.world);
                        }
                    } else if (Squid.class.isAssignableFrom(clazz)) {
                        entity = new EntitySquid(this.world);
                    } else if (Tameable.class.isAssignableFrom(clazz)) {
                        if (Wolf.class.isAssignableFrom(clazz)) {
                            entity = new EntityWolf(this.world);
                        } else if (Ocelot.class.isAssignableFrom(clazz)) {
                            entity = new EntityOcelot(this.world);
                        }
                    } else if (PigZombie.class.isAssignableFrom(clazz)) {
                        entity = new EntityPigZombie(this.world);
                    } else if (Zombie.class.isAssignableFrom(clazz)) {
                        entity = new EntityZombie(this.world);
                    } else if (Giant.class.isAssignableFrom(clazz)) {
                        entity = new EntityGiantZombie(this.world);
                    } else if (Silverfish.class.isAssignableFrom(clazz)) {
                        entity = new EntitySilverfish(this.world);
                    } else if (Enderman.class.isAssignableFrom(clazz)) {
                        entity = new EntityEnderman(this.world);
                    } else if (Blaze.class.isAssignableFrom(clazz)) {
                        entity = new EntityBlaze(this.world);
                    } else if (Villager.class.isAssignableFrom(clazz)) {
                        entity = new EntityVillager(this.world);
                    } else if (Witch.class.isAssignableFrom(clazz)) {
                        entity = new EntityWitch(this.world);
                    } else if (Wither.class.isAssignableFrom(clazz)) {
                        entity = new EntityWither(this.world);
                    } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                        if (EnderDragon.class.isAssignableFrom(clazz)) {
                            entity = new EntityEnderDragon(this.world);
                        }
                    } else if (Ambient.class.isAssignableFrom(clazz)) {
                        if (Bat.class.isAssignableFrom(clazz)) {
                            entity = new EntityBat(this.world);
                        }
                    } else if (Rabbit.class.isAssignableFrom(clazz)) {
                        entity = new EntityRabbit(this.world);
                    } else if (Endermite.class.isAssignableFrom(clazz)) {
                        entity = new EntityEndermite(this.world);
                    } else if (Guardian.class.isAssignableFrom(clazz)) {
                        entity = new EntityGuardian(this.world);
                    } else if (ArmorStand.class.isAssignableFrom(clazz)) {
                        entity = new EntityArmorStand(this.world, x, y, z);
                    }
                }

                if (entity != null) {
                    ((net.minecraft.server.v1_8_R3.Entity) entity).setLocation(x, y, z, yaw, pitch);
                }
            } else if (Hanging.class.isAssignableFrom(clazz)) {
                Block block = this.getBlockAt(location);
                BlockFace blockface = BlockFace.SELF;
                byte b0 = 16;
                byte height = 16;

                if (ItemFrame.class.isAssignableFrom(clazz)) {
                    b0 = 12;
                    height = 12;
                } else if (LeashHitch.class.isAssignableFrom(clazz)) {
                    b0 = 9;
                    height = 9;
                }

                BlockFace[] faces = new BlockFace[] { BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
                BlockPosition pos = new BlockPosition((int) x, (int) y, (int) z);
                BlockFace[] ablockface = faces;
                int i = faces.length;

                for (int j = 0; j < i; ++j) {
                    BlockFace dir = ablockface[j];
                    net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));

                    if (nmsBlock.getMaterial().isBuildable() || BlockDiodeAbstract.d(nmsBlock)) {
                        boolean taken = false;
                        AxisAlignedBB bb = EntityHanging.calculateBoundingBox(pos, CraftBlock.blockFaceToNotch(dir).opposite(), b0, height);
                        List list = this.world.getEntities((net.minecraft.server.v1_8_R3.Entity) null, bb);
                        Iterator it = list.iterator();

                        while (!taken && it.hasNext()) {
                            net.minecraft.server.v1_8_R3.Entity e = (net.minecraft.server.v1_8_R3.Entity) it.next();

                            if (e instanceof EntityHanging) {
                                taken = true;
                            }
                        }

                        if (!taken) {
                            blockface = dir;
                            break;
                        }
                    }
                }

                EnumDirection enumdirection = CraftBlock.blockFaceToNotch(blockface).opposite();

                if (Painting.class.isAssignableFrom(clazz)) {
                    entity = new EntityPainting(this.world, new BlockPosition((int) x, (int) y, (int) z), enumdirection);
                } else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    entity = new EntityItemFrame(this.world, new BlockPosition((int) x, (int) y, (int) z), enumdirection);
                } else if (LeashHitch.class.isAssignableFrom(clazz)) {
                    entity = new EntityLeash(this.world, new BlockPosition((int) x, (int) y, (int) z));
                    ((net.minecraft.server.v1_8_R3.Entity) entity).attachedToPlayer = true;
                }

                if (entity != null && !((EntityHanging) entity).survives()) {
                    throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
                }
            } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
                entity = new EntityTNTPrimed(this.world, x, y, z, (EntityLiving) null);
            } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
                entity = new EntityExperienceOrb(this.world, x, y, z, 0);
            } else if (Weather.class.isAssignableFrom(clazz)) {
                if (LightningStrike.class.isAssignableFrom(clazz)) {
                    entity = new EntityLightning(this.world, x, y, z);
                }
            } else if (Firework.class.isAssignableFrom(clazz)) {
                entity = new EntityFireworks(this.world, x, y, z, (net.minecraft.server.v1_8_R3.ItemStack) null);
            }

            if (entity != null) {
                if (entity instanceof EntityOcelot) {
                    ((EntityOcelot) entity).spawnBonus = false;
                }

                return (net.minecraft.server.v1_8_R3.Entity) entity;
            } else {
                throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
            }
        } else {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }
    }

    public Entity addEntity(net.minecraft.server.v1_8_R3.Entity entity, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");
        if (entity instanceof EntityInsentient) {
            ((EntityInsentient) entity).prepare(this.getHandle().E(new BlockPosition(entity)), (GroupDataEntity) null);
        }

        this.world.addEntity(entity, reason);
        return entity.getBukkitEntity();
    }

    public Entity spawn(Location location, Class clazz, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        net.minecraft.server.v1_8_R3.Entity entity = this.createEntity(location, clazz);

        return this.addEntity(entity, reason);
    }

    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }

    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        this.world.setSpawnFlags(allowMonsters, allowAnimals);
    }

    public boolean getAllowAnimals() {
        return this.world.allowAnimals;
    }

    public boolean getAllowMonsters() {
        return this.world.allowMonsters;
    }

    public int getMaxHeight() {
        return this.world.getHeight();
    }

    public int getSeaLevel() {
        return 64;
    }

    public boolean getKeepSpawnInMemory() {
        return this.world.keepSpawnInMemory;
    }

    public void setKeepSpawnInMemory(boolean keepLoaded) {
        this.world.keepSpawnInMemory = keepLoaded;
        BlockPosition chunkcoordinates = this.world.getSpawn();
        int chunkCoordX = chunkcoordinates.getX() >> 4;
        int chunkCoordZ = chunkcoordinates.getZ() >> 4;

        for (int x = -12; x <= 12; ++x) {
            for (int z = -12; z <= 12; ++z) {
                if (keepLoaded) {
                    this.loadChunk(chunkCoordX + x, chunkCoordZ + z);
                } else if (this.isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
                    if (this.getHandle().getChunkAt(chunkCoordX + x, chunkCoordZ + z) instanceof EmptyChunk) {
                        this.unloadChunk(chunkCoordX + x, chunkCoordZ + z, false);
                    } else {
                        this.unloadChunk(chunkCoordX + x, chunkCoordZ + z);
                    }
                }
            }
        }

    }

    public int hashCode() {
        return this.getUID().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CraftWorld other = (CraftWorld) obj;

            return this.getUID() == other.getUID();
        }
    }

    public File getWorldFolder() {
        return ((WorldNBTStorage) this.world.getDataManager()).getDirectory();
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            player.sendPluginMessage(source, channel, message);
        }

    }

    public Set getListeningPluginChannels() {
        HashSet result = new HashSet();
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    public WorldType getWorldType() {
        return WorldType.getByName(this.world.getWorldData().getType().name());
    }

    public boolean canGenerateStructures() {
        return this.world.getWorldData().shouldGenerateMapFeatures();
    }

    public long getTicksPerAnimalSpawns() {
        return this.world.ticksPerAnimalSpawns;
    }

    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        this.world.ticksPerAnimalSpawns = (long) ticksPerAnimalSpawns;
    }

    public long getTicksPerMonsterSpawns() {
        return this.world.ticksPerMonsterSpawns;
    }

    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        this.world.ticksPerMonsterSpawns = (long) ticksPerMonsterSpawns;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List getMetadata(String metadataKey) {
        return this.server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public int getMonsterSpawnLimit() {
        return this.monsterSpawn < 0 ? this.server.getMonsterSpawnLimit() : this.monsterSpawn;
    }

    public void setMonsterSpawnLimit(int limit) {
        this.monsterSpawn = limit;
    }

    public int getAnimalSpawnLimit() {
        return this.animalSpawn < 0 ? this.server.getAnimalSpawnLimit() : this.animalSpawn;
    }

    public void setAnimalSpawnLimit(int limit) {
        this.animalSpawn = limit;
    }

    public int getWaterAnimalSpawnLimit() {
        return this.waterAnimalSpawn < 0 ? this.server.getWaterAnimalSpawnLimit() : this.waterAnimalSpawn;
    }

    public void setWaterAnimalSpawnLimit(int limit) {
        this.waterAnimalSpawn = limit;
    }

    public int getAmbientSpawnLimit() {
        return this.ambientSpawn < 0 ? this.server.getAmbientSpawnLimit() : this.ambientSpawn;
    }

    public void setAmbientSpawnLimit(int limit) {
        this.ambientSpawn = limit;
    }

    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (loc != null && sound != null) {
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();

            this.getHandle().makeSound(x, y, z, CraftSound.getSound(sound), volume, pitch);
        }
    }

    public String getGameRuleValue(String rule) {
        return this.getHandle().getGameRules().get(rule);
    }

    public boolean setGameRuleValue(String rule, String value) {
        if (rule != null && value != null) {
            if (!this.isGameRule(rule)) {
                return false;
            } else {
                this.getHandle().getGameRules().set(rule, value);
                return true;
            }
        } else {
            return false;
        }
    }

    public String[] getGameRules() {
        return this.getHandle().getGameRules().getGameRules();
    }

    public boolean isGameRule(String rule) {
        return this.getHandle().getGameRules().contains(rule);
    }

    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = new CraftWorldBorder(this);
        }

        return this.worldBorder;
    }

    public void processChunkGC() {
        ++this.chunkGCTickCount;
        if (this.chunkLoadCount >= this.server.chunkGCLoadThresh && this.server.chunkGCLoadThresh > 0) {
            this.chunkLoadCount = 0;
        } else {
            if (this.chunkGCTickCount < this.server.chunkGCPeriod || this.server.chunkGCPeriod <= 0) {
                return;
            }

            this.chunkGCTickCount = 0;
        }

        ChunkProviderServer cps = this.world.chunkProviderServer;
        Iterator iterator = cps.chunks.values().iterator();

        while (iterator.hasNext()) {
            net.minecraft.server.v1_8_R3.Chunk chunk = (net.minecraft.server.v1_8_R3.Chunk) iterator.next();

            if (!this.isChunkInUse(chunk.locX, chunk.locZ) && !cps.unloadQueue.contains(chunk.locX, chunk.locZ)) {
                cps.queueUnload(chunk.locX, chunk.locZ);
            }
        }

    }

    public World.Spigot spigot() {
        return this.spigot;
    }

    static int[] $SWITCH_TABLE$org$bukkit$TreeType() {
        int[] aint = CraftWorld.$SWITCH_TABLE$org$bukkit$TreeType;

        if (CraftWorld.$SWITCH_TABLE$org$bukkit$TreeType != null) {
            return aint;
        } else {
            int[] aint1 = new int[TreeType.values().length];

            try {
                aint1[TreeType.ACACIA.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[TreeType.BIG_TREE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[TreeType.BIRCH.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[TreeType.BROWN_MUSHROOM.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[TreeType.COCOA_TREE.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[TreeType.DARK_OAK.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[TreeType.JUNGLE.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[TreeType.JUNGLE_BUSH.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[TreeType.MEGA_REDWOOD.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[TreeType.REDWOOD.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[TreeType.RED_MUSHROOM.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[TreeType.SMALL_JUNGLE.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[TreeType.SWAMP.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[TreeType.TALL_BIRCH.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[TreeType.TALL_REDWOOD.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[TreeType.TREE.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            CraftWorld.$SWITCH_TABLE$org$bukkit$TreeType = aint1;
            return aint1;
        }
    }
}
