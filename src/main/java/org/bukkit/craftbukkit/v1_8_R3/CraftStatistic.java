package org.bukkit.craftbukkit.v1_8_R3;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
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

    public static Achievement getBukkitAchievement(net.minecraft.stats.Achievement achievement) {
        return getBukkitAchievementByName(achievement.getStatName().toString());
    }

    public static Achievement getBukkitAchievementByName(String name) {
        return (Achievement) CraftStatistic.achievements.get(name);
    }

    public static Statistic getBukkitStatistic(StatBase statistic) {
        return getBukkitStatisticByName(statistic.getStatName().toString());
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
    //TODO: rework this
    public static StatBase getNMSStatistic(Statistic statistic) {
        return StatList.getOneShotStat((String) CraftStatistic.statistics.inverse().get(statistic));
    }

    public static net.minecraft.stats.Achievement getNMSAchievement(Achievement achievement) {
        return (net.minecraft.stats.Achievement) StatList.getOneShotStat((String) CraftStatistic.achievements.inverse().get(achievement));
    }

    public static StatBase getMaterialStatistic(Statistic stat, Material material) {
        try {
            return stat == Statistic.MINE_BLOCK ? StatList.mineBlockStatArray[material.getId()] : (stat == Statistic.CRAFT_ITEM ? StatList.objectCraftStats[material.getId()] : (stat == Statistic.USE_ITEM ? StatList.objectUseStats[material.getId()] : (stat == Statistic.BREAK_ITEM ? StatList.objectBreakStats[material.getId()] : null)));
        } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            return null;
        }
    }

    public static StatBase getEntityStatistic(Statistic stat, EntityType entity) {
        EntityList.EntityEggInfo monsteregginfo = (EntityList.EntityEggInfo) EntityList.entityEggs.get(Integer.valueOf(entity.getTypeId()));

        if (monsteregginfo != null) {
            return monsteregginfo.field_151512_d;
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(StatBase statistic) {
        String statisticString = statistic.getStatName().toString();

        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(StatBase statistic) {
        String statisticString = statistic.statId;
        int id;
        try {
            id = Integer.valueOf(statisticString.substring(statisticString.lastIndexOf(".") + 1));
        } catch (NumberFormatException e) {
            return null;
        }
        return Material.getMaterial(id);
    }
}
