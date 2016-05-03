package org.bukkit.configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public interface ConfigurationSection {

    Set getKeys(boolean flag);

    Map getValues(boolean flag);

    boolean contains(String s);

    boolean isSet(String s);

    String getCurrentPath();

    String getName();

    Configuration getRoot();

    ConfigurationSection getParent();

    Object get(String s);

    Object get(String s, Object object);

    void set(String s, Object object);

    ConfigurationSection createSection(String s);

    ConfigurationSection createSection(String s, Map map);

    String getString(String s);

    String getString(String s, String s1);

    boolean isString(String s);

    int getInt(String s);

    int getInt(String s, int i);

    boolean isInt(String s);

    boolean getBoolean(String s);

    boolean getBoolean(String s, boolean flag);

    boolean isBoolean(String s);

    double getDouble(String s);

    double getDouble(String s, double d0);

    boolean isDouble(String s);

    long getLong(String s);

    long getLong(String s, long i);

    boolean isLong(String s);

    List getList(String s);

    List getList(String s, List list);

    boolean isList(String s);

    List getStringList(String s);

    List getIntegerList(String s);

    List getBooleanList(String s);

    List getDoubleList(String s);

    List getFloatList(String s);

    List getLongList(String s);

    List getByteList(String s);

    List getCharacterList(String s);

    List getShortList(String s);

    List getMapList(String s);

    Vector getVector(String s);

    Vector getVector(String s, Vector vector);

    boolean isVector(String s);

    OfflinePlayer getOfflinePlayer(String s);

    OfflinePlayer getOfflinePlayer(String s, OfflinePlayer offlineplayer);

    boolean isOfflinePlayer(String s);

    ItemStack getItemStack(String s);

    ItemStack getItemStack(String s, ItemStack itemstack);

    boolean isItemStack(String s);

    Color getColor(String s);

    Color getColor(String s, Color color);

    boolean isColor(String s);

    ConfigurationSection getConfigurationSection(String s);

    boolean isConfigurationSection(String s);

    ConfigurationSection getDefaultSection();

    void addDefault(String s, Object object);
}
