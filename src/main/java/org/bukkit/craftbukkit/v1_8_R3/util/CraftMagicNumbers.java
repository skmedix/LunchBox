package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
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
        return Material.getMaterial(Block.getIdFromBlock(block));
    }

    public static Item getItem(Material material) {
        Item item = Item.getItemById(material.getId());

        return item;
    }

    /** @deprecated */
    @Deprecated
    public static Item getItem(int id) {
        return Item.getItemById(id);
    }

    /** @deprecated */
    @Deprecated
    public static int getId(Item item) {
        return Item.getIdFromItem(item);
    }

    public static Material getMaterial(Item item) {
        Material material = Material.getMaterial(Item.getIdFromItem(item));

        return material == null ? Material.AIR : material;
    }

    public static Block getBlock(Material material) {
        Block block = Block.getBlockById(material.getId());

        return block == null ? Blocks.air : block;
    }

    public Material getMaterialFromInternalName(String name) {
        return getMaterial(net.minecraft.item.Item.getByNameOrId(name));
    }

    public List<String> tabCompleteInternalMaterialName(String token, List completions) {
        return (List) StringUtil.copyPartialMatches(token, Item.itemRegistry.getKeys(), completions);
    }

    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try
        {
            nmsStack.setTagCompound((net.minecraft.nbt.NBTTagCompound) net.minecraft.nbt.JsonToNBT.getTagFromJson(arguments));
        }
        catch (net.minecraft.nbt.NBTException e)
        {
            e.printStackTrace();
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
        Iterator iterator = StatList.allStats.iterator();

        while (iterator.hasNext()) {
            String statistic = ((StatBase) iterator.next()).getStatName().toString();

            if (statistic.startsWith(token)) {
                matches.add(statistic);
            }
        }

        return matches;
    }
}
