package org.bukkit.event.player;

import net.minecraft.stats.AchievementList;
import org.bukkit.Achievement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerAchievementAwardedEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Achievement achievement;
    private boolean isCancelled = false;

    public PlayerAchievementAwardedEvent(Player player, Achievement achievement) {
        super(player);
        this.achievement = achievement;
    }

    public PlayerAchievementAwardedEvent(Player player, net.minecraft.stats.Achievement a) {
        super(player);
        if (a == AchievementList.acquireIron) {
            this.achievement = Achievement.ACQUIRE_IRON;
        }
        else if (a == AchievementList.bakeCake) {
            this.achievement = Achievement.BAKE_CAKE;
        }
        else if (a == AchievementList.blazeRod) {
            this.achievement = Achievement.GET_BLAZE_ROD;
        }
        else if (a == AchievementList.bookcase) {
            this.achievement = Achievement.BOOKCASE;
        }
        else if (a == AchievementList.breedCow) {
            this.achievement = Achievement.BREED_COW;
        }
        else if (a == AchievementList.buildBetterPickaxe) {
            this.achievement = Achievement.BUILD_BETTER_PICKAXE;
        }
        else if (a == AchievementList.buildFurnace) {
            this.achievement = Achievement.BUILD_FURNACE;
        }
        else if (a == AchievementList.buildHoe) {
            this.achievement = Achievement.BUILD_HOE;
        }
        else if (a == AchievementList.buildPickaxe) {
            this.achievement = Achievement.BUILD_PICKAXE;
        }
        else if (a == AchievementList.buildSword) {
            this.achievement = Achievement.BUILD_SWORD;
        }
        else if (a == AchievementList.buildWorkBench) {
            this.achievement = Achievement.BUILD_WORKBENCH;
        }
        else if (a == AchievementList.cookFish) {
            this.achievement = Achievement.COOK_FISH;
        }
        else if (a == AchievementList.diamonds) {
            this.achievement = Achievement.GET_DIAMONDS;
        }
        else if (a == AchievementList.diamondsToYou) {
            this.achievement = Achievement.DIAMONDS_TO_YOU;
        }
        else if (a == AchievementList.enchantments) {
            this.achievement = Achievement.ENCHANTMENTS;
        }
        else if (a == AchievementList.exploreAllBiomes) {
            this.achievement = Achievement.EXPLORE_ALL_BIOMES;
        }
        else if (a == AchievementList.flyPig) {
            this.achievement = Achievement.FLY_PIG;
        }
        else if (a == AchievementList.fullBeacon) {
            this.achievement = Achievement.FULL_BEACON;
        }
        else if (a == AchievementList.ghast) {
            this.achievement = Achievement.GHAST_RETURN;
        }
        else if (a == AchievementList.killCow) {
            this.achievement = Achievement.KILL_COW;
        }
        else if (a == AchievementList.killEnemy) {
            this.achievement = Achievement.KILL_ENEMY;
        }
        else if (a == AchievementList.killWither) {
            this.achievement = Achievement.KILL_WITHER;
        }
        else if (a == AchievementList.makeBread) {
            this.achievement = Achievement.MAKE_BREAD;
        }
        else if (a == AchievementList.onARail) {
            this.achievement = Achievement.ON_A_RAIL;
        }
        else if (a == AchievementList.mineWood) {
            this.achievement = Achievement.MINE_WOOD;
        }
        else if (a == AchievementList.openInventory) {
            this.achievement = Achievement.OPEN_INVENTORY;
        }
        else if (a == AchievementList.overkill) {
            this.achievement = Achievement.OVERKILL;
        }
        else if (a == AchievementList.overpowered) {
            this.achievement = Achievement.OVERPOWERED;
        }
        else if (a == AchievementList.portal) {
            this.achievement = Achievement.NETHER_PORTAL;
        }
        else if (a == AchievementList.potion) {
            this.achievement = Achievement.BREW_POTION;
        }
        else if (a == AchievementList.snipeSkeleton) {
            this.achievement = Achievement.SNIPE_SKELETON;
        }
        else if (a == AchievementList.spawnWither) {
            this.achievement = Achievement.SPAWN_WITHER;
        }
        else if (a == AchievementList.theEnd) {
            this.achievement = Achievement.END_PORTAL;
        }
        else if (a == AchievementList.theEnd2) {
            this.achievement = Achievement.THE_END;
        }
    }

    public Achievement getAchievement() {
        return this.achievement;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerAchievementAwardedEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerAchievementAwardedEvent.handlers;
    }
}
