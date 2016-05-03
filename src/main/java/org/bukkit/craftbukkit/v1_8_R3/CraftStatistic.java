package org.bukkit.craftbukkit.v1_8_R3;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.StatisticList;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

public class CraftStatistic {

    private static final BiMap statistics;
    private static final BiMap achievements;

    static {
        ImmutableMap specialCases = ImmutableMap.builder().put("achievement.buildWorkBench", Achievement.BUILD_WORKBENCH).put("achievement.diamonds", Achievement.GET_DIAMONDS).put("achievement.portal", Achievement.NETHER_PORTAL).put("achievement.ghast", Achievement.GHAST_RETURN).put("achievement.theEnd", Achievement.END_PORTAL).put("achievement.theEnd2", Achievement.THE_END).put("achievement.blazeRod", Achievement.GET_BLAZE_ROD).put("achievement.potion", Achievement.BREW_POTION).build();
        ImmutableBiMap.Builder statisticBuilder = ImmutableBiMap.builder();
        ImmutableBiMap.Builder achievementBuilder = ImmutableBiMap.builder();
        Statistic[] astatistic;
        int i = (astatistic = Statistic.values()).length;

        int j;

        for (j = 0; j < i; ++j) {
            Statistic achievement = astatistic[j];

            if (achievement == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", achievement);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name()), achievement);
            }
        }

        Achievement[] aachievement;

        i = (aachievement = Achievement.values()).length;

        for (j = 0; j < i; ++j) {
            Achievement achievement = aachievement[j];

            if (!specialCases.values().contains(achievement)) {
                achievementBuilder.put("achievement." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name()), achievement);
            }
        }

        achievementBuilder.putAll(specialCases);
        statistics = statisticBuilder.build();
        achievements = achievementBuilder.build();
    }

    public static Achievement getBukkitAchievement(net.minecraft.server.v1_8_R3.Achievement achievement) {
        return getBukkitAchievementByName(achievement.name);
    }

    public static Achievement getBukkitAchievementByName(String name) {
        return (Achievement) CraftStatistic.achievements.get(name);
    }

    public static Statistic getBukkitStatistic(net.minecraft.server.v1_8_R3.Statistic statistic) {
        return getBukkitStatisticByName(statistic.name);
    }

    public static Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity")) {
            name = "stat.killEntity";
        }

        if (name.startsWith("stat.entityKilledBy")) {
            name = "stat.entityKilledBy";
        }

        if (name.startsWith("stat.breakItem")) {
            name = "stat.breakItem";
        }

        if (name.startsWith("stat.useItem")) {
            name = "stat.useItem";
        }

        if (name.startsWith("stat.mineBlock")) {
            name = "stat.mineBlock";
        }

        if (name.startsWith("stat.craftItem")) {
            name = "stat.craftItem";
        }

        return (Statistic) CraftStatistic.statistics.get(name);
    }

    public static net.minecraft.server.v1_8_R3.Statistic getNMSStatistic(Statistic statistic) {
        return StatisticList.getStatistic((String) CraftStatistic.statistics.inverse().get(statistic));
    }

    public static net.minecraft.server.v1_8_R3.Achievement getNMSAchievement(Achievement achievement) {
        return (net.minecraft.server.v1_8_R3.Achievement) StatisticList.getStatistic((String) CraftStatistic.achievements.inverse().get(achievement));
    }

    public static net.minecraft.server.v1_8_R3.Statistic getMaterialStatistic(Statistic stat, Material material) {
        try {
            return stat == Statistic.MINE_BLOCK ? StatisticList.MINE_BLOCK_COUNT[material.getId()] : (stat == Statistic.CRAFT_ITEM ? StatisticList.CRAFT_BLOCK_COUNT[material.getId()] : (stat == Statistic.USE_ITEM ? StatisticList.USE_ITEM_COUNT[material.getId()] : (stat == Statistic.BREAK_ITEM ? StatisticList.BREAK_ITEM_COUNT[material.getId()] : null)));
        } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            return null;
        }
    }

    public static net.minecraft.server.v1_8_R3.Statistic getEntityStatistic(Statistic stat, EntityType entity) {
        EntityTypes.MonsterEggInfo monsteregginfo = (EntityTypes.MonsterEggInfo) EntityTypes.eggInfo.get(Integer.valueOf(entity.getTypeId()));

        return monsteregginfo != null ? monsteregginfo.killEntityStatistic : null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.server.v1_8_R3.Statistic statistic) {
        String statisticString = statistic.name;

        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(net.minecraft.server.v1_8_R3.Statistic statistic) {
        String statisticString = statistic.name;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        Item item = (Item) Item.REGISTRY.get(new MinecraftKey(val));

        if (item != null) {
            return Material.getMaterial(Item.getId(item));
        } else {
            Block block = (Block) Block.REGISTRY.get(new MinecraftKey(val));

            if (block != null) {
                return Material.getMaterial(Block.getId(block));
            } else {
                try {
                    return Material.getMaterial(Integer.parseInt(val));
                } catch (NumberFormatException numberformatexception) {
                    return null;
                }
            }
        }
    }
}
