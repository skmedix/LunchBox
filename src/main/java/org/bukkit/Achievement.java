package org.bukkit;

public enum Achievement {

    OPEN_INVENTORY, MINE_WOOD(Achievement.OPEN_INVENTORY), BUILD_WORKBENCH(Achievement.MINE_WOOD), BUILD_PICKAXE(Achievement.BUILD_WORKBENCH), BUILD_FURNACE(Achievement.BUILD_PICKAXE), ACQUIRE_IRON(Achievement.BUILD_FURNACE), BUILD_HOE(Achievement.BUILD_WORKBENCH), MAKE_BREAD(Achievement.BUILD_HOE), BAKE_CAKE(Achievement.BUILD_HOE), BUILD_BETTER_PICKAXE(Achievement.BUILD_PICKAXE), COOK_FISH(Achievement.BUILD_FURNACE), ON_A_RAIL(Achievement.ACQUIRE_IRON), BUILD_SWORD(Achievement.BUILD_WORKBENCH), KILL_ENEMY(Achievement.BUILD_SWORD), KILL_COW(Achievement.BUILD_SWORD), FLY_PIG(Achievement.KILL_COW), SNIPE_SKELETON(Achievement.KILL_ENEMY), GET_DIAMONDS(Achievement.ACQUIRE_IRON), NETHER_PORTAL(Achievement.GET_DIAMONDS), GHAST_RETURN(Achievement.NETHER_PORTAL), GET_BLAZE_ROD(Achievement.NETHER_PORTAL), BREW_POTION(Achievement.GET_BLAZE_ROD), END_PORTAL(Achievement.GET_BLAZE_ROD), THE_END(Achievement.END_PORTAL), ENCHANTMENTS(Achievement.GET_DIAMONDS), OVERKILL(Achievement.ENCHANTMENTS), BOOKCASE(Achievement.ENCHANTMENTS), EXPLORE_ALL_BIOMES(Achievement.END_PORTAL), SPAWN_WITHER(Achievement.THE_END), KILL_WITHER(Achievement.SPAWN_WITHER), FULL_BEACON(Achievement.KILL_WITHER), BREED_COW(Achievement.KILL_COW), DIAMONDS_TO_YOU(Achievement.GET_DIAMONDS), OVERPOWERED(Achievement.BUILD_BETTER_PICKAXE);

    private final Achievement parent;

    private Achievement() {
        this.parent = null;
    }

    private Achievement(Achievement parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public Achievement getParent() {
        return this.parent;
    }
}
