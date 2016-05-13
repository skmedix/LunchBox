package org.bukkit.entity;

import java.net.InetSocketAddress;
import java.util.Set;
//import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.map.MapView;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scoreboard.Scoreboard;

public interface Player extends HumanEntity, Conversable, CommandSender, OfflinePlayer, PluginMessageRecipient {

    String getDisplayName();

    void setDisplayName(String s);

    String getPlayerListName();

    void setPlayerListName(String s);

    void setCompassTarget(Location location);

    Location getCompassTarget();

    InetSocketAddress getAddress();

    void sendRawMessage(String s);

    void kickPlayer(String s);

    void chat(String s);

    boolean performCommand(String s);

    boolean isSneaking();

    void setSneaking(boolean flag);

    boolean isSprinting();

    void setSprinting(boolean flag);

    void saveData();

    void loadData();

    void setSleepingIgnored(boolean flag);

    boolean isSleepingIgnored();

    /** @deprecated */
    @Deprecated
    void playNote(Location location, byte b0, byte b1);

    void playNote(Location location, Instrument instrument, Note note);

    void playSound(Location location, Sound sound, float f, float f1);

    void playSound(Location location, String s, float f, float f1);
    //LunchBox - remove for now
    ///** @deprecated */
    //@Deprecated
    //void playEffect(Location location, Effect effect, int i);

    //void playEffect(Location location, Effect effect, Object object);

    /** @deprecated */
    @Deprecated
    void sendBlockChange(Location location, Material material, byte b0);

    /** @deprecated */
    @Deprecated
    boolean sendChunkChange(Location location, int i, int j, int k, byte[] abyte);

    /** @deprecated */
    @Deprecated
    void sendBlockChange(Location location, int i, byte b0);

    void sendSignChange(Location location, String[] astring) throws IllegalArgumentException;

    void sendMap(MapView mapview);

    void updateInventory();

    void awardAchievement(Achievement achievement);

    void removeAchievement(Achievement achievement);

    boolean hasAchievement(Achievement achievement);

    void incrementStatistic(Statistic statistic) throws IllegalArgumentException;

    void decrementStatistic(Statistic statistic) throws IllegalArgumentException;

    void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException;

    void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException;

    void setStatistic(Statistic statistic, int i) throws IllegalArgumentException;

    int getStatistic(Statistic statistic) throws IllegalArgumentException;

    void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

    void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

    int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

    void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException;

    void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException;

    void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException;

    void incrementStatistic(Statistic statistic, EntityType entitytype) throws IllegalArgumentException;

    void decrementStatistic(Statistic statistic, EntityType entitytype) throws IllegalArgumentException;

    int getStatistic(Statistic statistic, EntityType entitytype) throws IllegalArgumentException;

    void incrementStatistic(Statistic statistic, EntityType entitytype, int i) throws IllegalArgumentException;

    void decrementStatistic(Statistic statistic, EntityType entitytype, int i);

    void setStatistic(Statistic statistic, EntityType entitytype, int i);

    void setPlayerTime(long i, boolean flag);

    long getPlayerTime();

    long getPlayerTimeOffset();

    boolean isPlayerTimeRelative();

    void resetPlayerTime();

    void setPlayerWeather(WeatherType weathertype);

    WeatherType getPlayerWeather();

    void resetPlayerWeather();

    void giveExp(int i);

    void giveExpLevels(int i);

    float getExp();

    void setExp(float f);

    int getLevel();

    void setLevel(int i);

    int getTotalExperience();

    void setTotalExperience(int i);

    float getExhaustion();

    void setExhaustion(float f);

    float getSaturation();

    void setSaturation(float f);

    int getFoodLevel();

    void setFoodLevel(int i);

    Location getBedSpawnLocation();

    void setBedSpawnLocation(Location location);

    void setBedSpawnLocation(Location location, boolean flag);

    boolean getAllowFlight();

    void setAllowFlight(boolean flag);

    void hidePlayer(Player player);

    void showPlayer(Player player);

    boolean canSee(Player player);

    /** @deprecated */
    @Deprecated
    boolean isOnGround();

    boolean isFlying();

    void setFlying(boolean flag);

    void setFlySpeed(float f) throws IllegalArgumentException;

    void setWalkSpeed(float f) throws IllegalArgumentException;

    float getFlySpeed();

    float getWalkSpeed();

    /** @deprecated */
    @Deprecated
    void setTexturePack(String s);

    void setResourcePack(String s);

    Scoreboard getScoreboard();

    void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException;

    boolean isHealthScaled();

    void setHealthScaled(boolean flag);

    void setHealthScale(double d0) throws IllegalArgumentException;

    double getHealthScale();

    Entity getSpectatorTarget();

    void setSpectatorTarget(Entity entity);

    /** @deprecated */
    @Deprecated
    void sendTitle(String s, String s1);

    /** @deprecated */
    @Deprecated
    void resetTitle();

    /*Player.Spigot spigot();

    public static class Spigot extends Entity.Spigot {

        public InetSocketAddress getRawAddress() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean getCollidesWithEntities() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setCollidesWithEntities(boolean collides) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void respawn() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getLocale() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Set getHiddenPlayers() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void sendMessage(BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void sendMessage(BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }*/
}
