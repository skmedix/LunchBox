package org.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.util.Vector;

public interface World extends PluginMessageRecipient, Metadatable {

    Block getBlockAt(int i, int j, int k);

    Block getBlockAt(Location location);

    /** @deprecated */
    @Deprecated
    int getBlockTypeIdAt(int i, int j, int k);

    /** @deprecated */
    @Deprecated
    int getBlockTypeIdAt(Location location);

    int getHighestBlockYAt(int i, int j);

    int getHighestBlockYAt(Location location);

    Block getHighestBlockAt(int i, int j);

    Block getHighestBlockAt(Location location);

    Chunk getChunkAt(int i, int j);

    Chunk getChunkAt(Location location);

    Chunk getChunkAt(Block block);

    boolean isChunkLoaded(Chunk chunk);

    Chunk[] getLoadedChunks();

    void loadChunk(Chunk chunk);

    boolean isChunkLoaded(int i, int j);

    boolean isChunkInUse(int i, int j);

    void loadChunk(int i, int j);

    boolean loadChunk(int i, int j, boolean flag);

    boolean unloadChunk(Chunk chunk);

    boolean unloadChunk(int i, int j);

    boolean unloadChunk(int i, int j, boolean flag);

    boolean unloadChunk(int i, int j, boolean flag, boolean flag1);

    boolean unloadChunkRequest(int i, int j);

    boolean unloadChunkRequest(int i, int j, boolean flag);

    boolean regenerateChunk(int i, int j);

    /** @deprecated */
    @Deprecated
    boolean refreshChunk(int i, int j);

    Item dropItem(Location location, ItemStack itemstack);

    Item dropItemNaturally(Location location, ItemStack itemstack);

    Arrow spawnArrow(Location location, Vector vector, float f, float f1);

    boolean generateTree(Location location, TreeType treetype);

    boolean generateTree(Location location, TreeType treetype, BlockChangeDelegate blockchangedelegate);

    Entity spawnEntity(Location location, EntityType entitytype);

    /** @deprecated */
    @Deprecated
    LivingEntity spawnCreature(Location location, EntityType entitytype);

    /** @deprecated */
    @Deprecated
    LivingEntity spawnCreature(Location location, CreatureType creaturetype);

    LightningStrike strikeLightning(Location location);

    LightningStrike strikeLightningEffect(Location location);

    List getEntities();

    List getLivingEntities();

    /** @deprecated */
    @Deprecated
    Collection getEntitiesByClass(Class... aclass);

    Collection getEntitiesByClass(Class oclass);

    Collection getEntitiesByClasses(Class... aclass);

    List getPlayers();

    Collection getNearbyEntities(Location location, double d0, double d1, double d2);

    String getName();

    UUID getUID();

    Location getSpawnLocation();

    boolean setSpawnLocation(int i, int j, int k);

    long getTime();

    void setTime(long i);

    long getFullTime();

    void setFullTime(long i);

    boolean hasStorm();

    void setStorm(boolean flag);

    int getWeatherDuration();

    void setWeatherDuration(int i);

    boolean isThundering();

    void setThundering(boolean flag);

    int getThunderDuration();

    void setThunderDuration(int i);

    boolean createExplosion(double d0, double d1, double d2, float f);

    boolean createExplosion(double d0, double d1, double d2, float f, boolean flag);

    boolean createExplosion(double d0, double d1, double d2, float f, boolean flag, boolean flag1);

    boolean createExplosion(Location location, float f);

    boolean createExplosion(Location location, float f, boolean flag);

    World.Environment getEnvironment();

    long getSeed();

    boolean getPVP();

    void setPVP(boolean flag);

    ChunkGenerator getGenerator();

    void save();

    List getPopulators();

    Entity spawn(Location location, Class oclass) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    FallingBlock spawnFallingBlock(Location location, Material material, byte b0) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    FallingBlock spawnFallingBlock(Location location, int i, byte b0) throws IllegalArgumentException;

    void playEffect(Location location, Effect effect, int i);

    void playEffect(Location location, Effect effect, int i, int j);

    void playEffect(Location location, Effect effect, Object object);

    void playEffect(Location location, Effect effect, Object object, int i);

    ChunkSnapshot getEmptyChunkSnapshot(int i, int j, boolean flag, boolean flag1);

    void setSpawnFlags(boolean flag, boolean flag1);

    boolean getAllowAnimals();

    boolean getAllowMonsters();

    Biome getBiome(int i, int j);

    void setBiome(int i, int j, Biome biome);

    double getTemperature(int i, int j);

    double getHumidity(int i, int j);

    int getMaxHeight();

    int getSeaLevel();

    boolean getKeepSpawnInMemory();

    void setKeepSpawnInMemory(boolean flag);

    boolean isAutoSave();

    void setAutoSave(boolean flag);

    void setDifficulty(Difficulty difficulty);

    Difficulty getDifficulty();

    File getWorldFolder();

    WorldType getWorldType();

    boolean canGenerateStructures();

    long getTicksPerAnimalSpawns();

    void setTicksPerAnimalSpawns(int i);

    long getTicksPerMonsterSpawns();

    void setTicksPerMonsterSpawns(int i);

    int getMonsterSpawnLimit();

    void setMonsterSpawnLimit(int i);

    int getAnimalSpawnLimit();

    void setAnimalSpawnLimit(int i);

    int getWaterAnimalSpawnLimit();

    void setWaterAnimalSpawnLimit(int i);

    int getAmbientSpawnLimit();

    void setAmbientSpawnLimit(int i);

    void playSound(Location location, Sound sound, float f, float f1);

    String[] getGameRules();

    String getGameRuleValue(String s);

    boolean setGameRuleValue(String s, String s1);

    boolean isGameRule(String s);

    //World.Spigot spigot(); todo later

    WorldBorder getWorldBorder();

    public static enum Environment {

        NORMAL(0), NETHER(-1), THE_END(1);

        private final int id;
        private static final Map lookup = new HashMap();

        static {
            World.Environment[] aworld_environment;
            int i = (aworld_environment = values()).length;

            for (int j = 0; j < i; ++j) {
                World.Environment env = aworld_environment[j];

                World.Environment.lookup.put(Integer.valueOf(env.getId()), env);
            }

        }

        private Environment(int id) {
            this.id = id;
        }

        /** @deprecated */
        @Deprecated
        public int getId() {
            return this.id;
        }

        /** @deprecated */
        @Deprecated
        public static World.Environment getEnvironment(int id) {
            return (World.Environment) World.Environment.lookup.get(Integer.valueOf(id));
        }
    }

    /* Todo later
    public static class Spigot {

        public void playEffect(Location location, Effect effect) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public LightningStrike strikeLightning(Location loc, boolean isSilent) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public LightningStrike strikeLightningEffect(Location loc, boolean isSilent) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    */
}
