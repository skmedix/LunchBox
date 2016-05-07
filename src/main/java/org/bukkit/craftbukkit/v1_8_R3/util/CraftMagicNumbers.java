package org.bukkit.craftbukkit.v1_8_R3.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.StatisticList;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.UnsafeValues;
import org.bukkit.craftbukkit.v1_8_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public final class CraftMagicNumbers implements UnsafeValues {

    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    public static Block getBlock(org.bukkit.block.Block block) {
        return getBlock(block.getType());
    }

    /** @deprecated */
    @Deprecated
    public static Block getBlock(int id) {
        return getBlock(Material.getMaterial(id));
    }

    /** @deprecated */
    @Deprecated
    public static int getId(Block block) {
        return Block.getStateId((IBlockState) block);
    }

    public static Material getMaterial(Block block) {
        return Material.getMaterial(Block.getId(block));
    }

    public static Item getItem(Material material) {
        Item item = Item.getById(material.getId());

        return item;
    }

    /** @deprecated */
    @Deprecated
    public static Item getItem(int id) {
        return Item.getById(id);
    }

    /** @deprecated */
    @Deprecated
    public static int getId(Item item) {
        return Item.getId(item);
    }

    public static Material getMaterial(Item item) {
        Material material = Material.getMaterial(Item.getId(item));

        return material == null ? Material.AIR : material;
    }

    public static Block getBlock(Material material) {
        Block block = Block.getById(material.getId());

        return block == null ? Blocks.AIR : block;
    }

    public Material getMaterialFromInternalName(String name) {
        return getMaterial((Item) Item.REGISTRY.get(new MinecraftKey(name)));
    }

    public List tabCompleteInternalMaterialName(String token, List completions) {
        ArrayList results = Lists.newArrayList();
        Iterator iterator = Item.REGISTRY.keySet().iterator();

        while (iterator.hasNext()) {
            MinecraftKey key = (MinecraftKey) iterator.next();

            results.add(key.toString());
        }

        return (List) StringUtil.copyPartialMatches(token, results, completions);
    }

    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try {
            nmsStack.setTag(MojangsonParser.parse(arguments));
        } catch (MojangsonParseException mojangsonparseexception) {
            Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, (String) null, mojangsonparseexception);
        }

        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
        return stack;
    }

    public Statistic getStatisticFromInternalName(String name) {
        return CraftStatistic.getBukkitStatisticByName(name);
    }

    public Achievement getAchievementFromInternalName(String name) {
        return CraftStatistic.getBukkitAchievementByName(name);
    }

    public List tabCompleteInternalStatisticOrAchievementName(String token, List completions) {
        ArrayList matches = new ArrayList();
        Iterator iterator = StatisticList.stats.iterator();

        while (iterator.hasNext()) {
            String statistic = ((net.minecraft.server.v1_8_R3.Statistic) iterator.next()).name;

            if (statistic.startsWith(token)) {
                matches.add(statistic);
            }
        }

        return matches;
    }
}
