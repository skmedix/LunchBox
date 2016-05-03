package org.bukkit;

import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.material.Banner;
import org.bukkit.material.Bed;
import org.bukkit.material.Button;
import org.bukkit.material.Cake;
import org.bukkit.material.Cauldron;
import org.bukkit.material.Chest;
import org.bukkit.material.Coal;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Command;
import org.bukkit.material.Crops;
import org.bukkit.material.DetectorRail;
import org.bukkit.material.Diode;
import org.bukkit.material.Dispenser;
import org.bukkit.material.Door;
import org.bukkit.material.Dye;
import org.bukkit.material.EnderChest;
import org.bukkit.material.FlowerPot;
import org.bukkit.material.Furnace;
import org.bukkit.material.Gate;
import org.bukkit.material.Ladder;
import org.bukkit.material.Lever;
import org.bukkit.material.LongGrass;
import org.bukkit.material.MaterialData;
import org.bukkit.material.MonsterEggs;
import org.bukkit.material.Mushroom;
import org.bukkit.material.NetherWarts;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.material.PistonExtensionMaterial;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.PressurePlate;
import org.bukkit.material.Pumpkin;
import org.bukkit.material.Rails;
import org.bukkit.material.RedstoneTorch;
import org.bukkit.material.RedstoneWire;
import org.bukkit.material.Sandstone;
import org.bukkit.material.Sign;
import org.bukkit.material.Skull;
import org.bukkit.material.SmoothBrick;
import org.bukkit.material.SpawnEgg;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.Torch;
import org.bukkit.material.TrapDoor;
import org.bukkit.material.Tree;
import org.bukkit.material.Tripwire;
import org.bukkit.material.TripwireHook;
import org.bukkit.material.Vine;
import org.bukkit.material.WoodenStep;
import org.bukkit.material.Wool;
import org.bukkit.util.Java15Compat;

public enum Material {

    AIR(0, 0), STONE(1), GRASS(2), DIRT(3), COBBLESTONE(4), WOOD(5, Tree.class), SAPLING(6, Tree.class), BEDROCK(7), WATER(8, MaterialData.class), STATIONARY_WATER(9, MaterialData.class), LAVA(10, MaterialData.class), STATIONARY_LAVA(11, MaterialData.class), SAND(12), GRAVEL(13), GOLD_ORE(14), IRON_ORE(15), COAL_ORE(16), LOG(17, Tree.class), LEAVES(18, Tree.class), SPONGE(19), GLASS(20), LAPIS_ORE(21), LAPIS_BLOCK(22), DISPENSER(23, Dispenser.class), SANDSTONE(24, Sandstone.class), NOTE_BLOCK(25), BED_BLOCK(26, Bed.class), POWERED_RAIL(27, PoweredRail.class), DETECTOR_RAIL(28, DetectorRail.class), PISTON_STICKY_BASE(29, PistonBaseMaterial.class), WEB(30), LONG_GRASS(31, LongGrass.class), DEAD_BUSH(32), PISTON_BASE(33, PistonBaseMaterial.class), PISTON_EXTENSION(34, PistonExtensionMaterial.class), WOOL(35, Wool.class), PISTON_MOVING_PIECE(36), YELLOW_FLOWER(37), RED_ROSE(38), BROWN_MUSHROOM(39), RED_MUSHROOM(40), GOLD_BLOCK(41), IRON_BLOCK(42), DOUBLE_STEP(43, Step.class), STEP(44, Step.class), BRICK(45), TNT(46), BOOKSHELF(47), MOSSY_COBBLESTONE(48), OBSIDIAN(49), TORCH(50, Torch.class), FIRE(51), MOB_SPAWNER(52), WOOD_STAIRS(53, Stairs.class), CHEST(54, Chest.class), REDSTONE_WIRE(55, RedstoneWire.class), DIAMOND_ORE(56), DIAMOND_BLOCK(57), WORKBENCH(58), CROPS(59, Crops.class), SOIL(60, MaterialData.class), FURNACE(61, Furnace.class), BURNING_FURNACE(62, Furnace.class), SIGN_POST(63, 64, Sign.class), WOODEN_DOOR(64, Door.class), LADDER(65, Ladder.class), RAILS(66, Rails.class), COBBLESTONE_STAIRS(67, Stairs.class), WALL_SIGN(68, 64, Sign.class), LEVER(69, Lever.class), STONE_PLATE(70, PressurePlate.class), IRON_DOOR_BLOCK(71, Door.class), WOOD_PLATE(72, PressurePlate.class), REDSTONE_ORE(73), GLOWING_REDSTONE_ORE(74), REDSTONE_TORCH_OFF(75, RedstoneTorch.class), REDSTONE_TORCH_ON(76, RedstoneTorch.class), STONE_BUTTON(77, Button.class), SNOW(78), ICE(79), SNOW_BLOCK(80), CACTUS(81, MaterialData.class), CLAY(82), SUGAR_CANE_BLOCK(83, MaterialData.class), JUKEBOX(84), FENCE(85), PUMPKIN(86, Pumpkin.class), NETHERRACK(87), SOUL_SAND(88), GLOWSTONE(89), PORTAL(90), JACK_O_LANTERN(91, Pumpkin.class), CAKE_BLOCK(92, 64, Cake.class), DIODE_BLOCK_OFF(93, Diode.class), DIODE_BLOCK_ON(94, Diode.class), STAINED_GLASS(95), TRAP_DOOR(96, TrapDoor.class), MONSTER_EGGS(97, MonsterEggs.class), SMOOTH_BRICK(98, SmoothBrick.class), HUGE_MUSHROOM_1(99, Mushroom.class), HUGE_MUSHROOM_2(100, Mushroom.class), IRON_FENCE(101), THIN_GLASS(102), MELON_BLOCK(103), PUMPKIN_STEM(104, MaterialData.class), MELON_STEM(105, MaterialData.class), VINE(106, Vine.class), FENCE_GATE(107, Gate.class), BRICK_STAIRS(108, Stairs.class), SMOOTH_STAIRS(109, Stairs.class), MYCEL(110), WATER_LILY(111), NETHER_BRICK(112), NETHER_FENCE(113), NETHER_BRICK_STAIRS(114, Stairs.class), NETHER_WARTS(115, NetherWarts.class), ENCHANTMENT_TABLE(116), BREWING_STAND(117, MaterialData.class), CAULDRON(118, Cauldron.class), ENDER_PORTAL(119), ENDER_PORTAL_FRAME(120), ENDER_STONE(121), DRAGON_EGG(122), REDSTONE_LAMP_OFF(123), REDSTONE_LAMP_ON(124), WOOD_DOUBLE_STEP(125, WoodenStep.class), WOOD_STEP(126, WoodenStep.class), COCOA(127, CocoaPlant.class), SANDSTONE_STAIRS(128, Stairs.class), EMERALD_ORE(129), ENDER_CHEST(130, EnderChest.class), TRIPWIRE_HOOK(131, TripwireHook.class), TRIPWIRE(132, Tripwire.class), EMERALD_BLOCK(133), SPRUCE_WOOD_STAIRS(134, Stairs.class), BIRCH_WOOD_STAIRS(135, Stairs.class), JUNGLE_WOOD_STAIRS(136, Stairs.class), COMMAND(137, Command.class), BEACON(138), COBBLE_WALL(139), FLOWER_POT(140, FlowerPot.class), CARROT(141), POTATO(142), WOOD_BUTTON(143, Button.class), SKULL(144, Skull.class), ANVIL(145), TRAPPED_CHEST(146, Chest.class), GOLD_PLATE(147), IRON_PLATE(148), REDSTONE_COMPARATOR_OFF(149), REDSTONE_COMPARATOR_ON(150), DAYLIGHT_DETECTOR(151), REDSTONE_BLOCK(152), QUARTZ_ORE(153), HOPPER(154), QUARTZ_BLOCK(155), QUARTZ_STAIRS(156, Stairs.class), ACTIVATOR_RAIL(157, PoweredRail.class), DROPPER(158, Dispenser.class), STAINED_CLAY(159), STAINED_GLASS_PANE(160), LEAVES_2(161), LOG_2(162), ACACIA_STAIRS(163, Stairs.class), DARK_OAK_STAIRS(164, Stairs.class), SLIME_BLOCK(165), BARRIER(166), IRON_TRAPDOOR(167, TrapDoor.class), PRISMARINE(168), SEA_LANTERN(169), HAY_BLOCK(170), CARPET(171), HARD_CLAY(172), COAL_BLOCK(173), PACKED_ICE(174), DOUBLE_PLANT(175), STANDING_BANNER(176, Banner.class), WALL_BANNER(177, Banner.class), DAYLIGHT_DETECTOR_INVERTED(178), RED_SANDSTONE(179), RED_SANDSTONE_STAIRS(180, Stairs.class), DOUBLE_STONE_SLAB2(181), STONE_SLAB2(182), SPRUCE_FENCE_GATE(183, Gate.class), BIRCH_FENCE_GATE(184, Gate.class), JUNGLE_FENCE_GATE(185, Gate.class), DARK_OAK_FENCE_GATE(186, Gate.class), ACACIA_FENCE_GATE(187, Gate.class), SPRUCE_FENCE(188), BIRCH_FENCE(189), JUNGLE_FENCE(190), DARK_OAK_FENCE(191), ACACIA_FENCE(192), SPRUCE_DOOR(193, Door.class), BIRCH_DOOR(194, Door.class), JUNGLE_DOOR(195, Door.class), ACACIA_DOOR(196, Door.class), DARK_OAK_DOOR(197, Door.class), IRON_SPADE(256, 1, 250), IRON_PICKAXE(257, 1, 250), IRON_AXE(258, 1, 250), FLINT_AND_STEEL(259, 1, 64), APPLE(260), BOW(261, 1, 384), ARROW(262), COAL(263, Coal.class), DIAMOND(264), IRON_INGOT(265), GOLD_INGOT(266), IRON_SWORD(267, 1, 250), WOOD_SWORD(268, 1, 59), WOOD_SPADE(269, 1, 59), WOOD_PICKAXE(270, 1, 59), WOOD_AXE(271, 1, 59), STONE_SWORD(272, 1, 131), STONE_SPADE(273, 1, 131), STONE_PICKAXE(274, 1, 131), STONE_AXE(275, 1, 131), DIAMOND_SWORD(276, 1, 1561), DIAMOND_SPADE(277, 1, 1561), DIAMOND_PICKAXE(278, 1, 1561), DIAMOND_AXE(279, 1, 1561), STICK(280), BOWL(281), MUSHROOM_SOUP(282, 1), GOLD_SWORD(283, 1, 32), GOLD_SPADE(284, 1, 32), GOLD_PICKAXE(285, 1, 32), GOLD_AXE(286, 1, 32), STRING(287), FEATHER(288), SULPHUR(289), WOOD_HOE(290, 1, 59), STONE_HOE(291, 1, 131), IRON_HOE(292, 1, 250), DIAMOND_HOE(293, 1, 1561), GOLD_HOE(294, 1, 32), SEEDS(295), WHEAT(296), BREAD(297), LEATHER_HELMET(298, 1, 55), LEATHER_CHESTPLATE(299, 1, 80), LEATHER_LEGGINGS(300, 1, 75), LEATHER_BOOTS(301, 1, 65), CHAINMAIL_HELMET(302, 1, 165), CHAINMAIL_CHESTPLATE(303, 1, 240), CHAINMAIL_LEGGINGS(304, 1, 225), CHAINMAIL_BOOTS(305, 1, 195), IRON_HELMET(306, 1, 165), IRON_CHESTPLATE(307, 1, 240), IRON_LEGGINGS(308, 1, 225), IRON_BOOTS(309, 1, 195), DIAMOND_HELMET(310, 1, 363), DIAMOND_CHESTPLATE(311, 1, 528), DIAMOND_LEGGINGS(312, 1, 495), DIAMOND_BOOTS(313, 1, 429), GOLD_HELMET(314, 1, 77), GOLD_CHESTPLATE(315, 1, 112), GOLD_LEGGINGS(316, 1, 105), GOLD_BOOTS(317, 1, 91), FLINT(318), PORK(319), GRILLED_PORK(320), PAINTING(321), GOLDEN_APPLE(322), SIGN(323, 16), WOOD_DOOR(324, 64), BUCKET(325, 16), WATER_BUCKET(326, 1), LAVA_BUCKET(327, 1), MINECART(328, 1), SADDLE(329, 1), IRON_DOOR(330, 64), REDSTONE(331), SNOW_BALL(332, 16), BOAT(333, 1), LEATHER(334), MILK_BUCKET(335, 1), CLAY_BRICK(336), CLAY_BALL(337), SUGAR_CANE(338), PAPER(339), BOOK(340), SLIME_BALL(341), STORAGE_MINECART(342, 1), POWERED_MINECART(343, 1), EGG(344, 16), COMPASS(345), FISHING_ROD(346, 1, 64), WATCH(347), GLOWSTONE_DUST(348), RAW_FISH(349), COOKED_FISH(350), INK_SACK(351, Dye.class), BONE(352), SUGAR(353), CAKE(354, 1), BED(355, 1), DIODE(356), COOKIE(357), MAP(358, MaterialData.class), SHEARS(359, 1, 238), MELON(360), PUMPKIN_SEEDS(361), MELON_SEEDS(362), RAW_BEEF(363), COOKED_BEEF(364), RAW_CHICKEN(365), COOKED_CHICKEN(366), ROTTEN_FLESH(367), ENDER_PEARL(368, 16), BLAZE_ROD(369), GHAST_TEAR(370), GOLD_NUGGET(371), NETHER_STALK(372), POTION(373, 1, MaterialData.class), GLASS_BOTTLE(374), SPIDER_EYE(375), FERMENTED_SPIDER_EYE(376), BLAZE_POWDER(377), MAGMA_CREAM(378), BREWING_STAND_ITEM(379), CAULDRON_ITEM(380), EYE_OF_ENDER(381), SPECKLED_MELON(382), MONSTER_EGG(383, 64, SpawnEgg.class), EXP_BOTTLE(384, 64), FIREBALL(385, 64), BOOK_AND_QUILL(386, 1), WRITTEN_BOOK(387, 16), EMERALD(388, 64), ITEM_FRAME(389), FLOWER_POT_ITEM(390), CARROT_ITEM(391), POTATO_ITEM(392), BAKED_POTATO(393), POISONOUS_POTATO(394), EMPTY_MAP(395), GOLDEN_CARROT(396), SKULL_ITEM(397), CARROT_STICK(398, 1, 25), NETHER_STAR(399), PUMPKIN_PIE(400), FIREWORK(401), FIREWORK_CHARGE(402), ENCHANTED_BOOK(403, 1), REDSTONE_COMPARATOR(404), NETHER_BRICK_ITEM(405), QUARTZ(406), EXPLOSIVE_MINECART(407, 1), HOPPER_MINECART(408, 1), PRISMARINE_SHARD(409), PRISMARINE_CRYSTALS(410), RABBIT(411), COOKED_RABBIT(412), RABBIT_STEW(413, 1), RABBIT_FOOT(414), RABBIT_HIDE(415), ARMOR_STAND(416, 16), IRON_BARDING(417, 1), GOLD_BARDING(418, 1), DIAMOND_BARDING(419, 1), LEASH(420), NAME_TAG(421), COMMAND_MINECART(422, 1), MUTTON(423), COOKED_MUTTON(424), BANNER(425, 16), SPRUCE_DOOR_ITEM(427), BIRCH_DOOR_ITEM(428), JUNGLE_DOOR_ITEM(429), ACACIA_DOOR_ITEM(430), DARK_OAK_DOOR_ITEM(431), GOLD_RECORD(2256, 1), GREEN_RECORD(2257, 1), RECORD_3(2258, 1), RECORD_4(2259, 1), RECORD_5(2260, 1), RECORD_6(2261, 1), RECORD_7(2262, 1), RECORD_8(2263, 1), RECORD_9(2264, 1), RECORD_10(2265, 1), RECORD_11(2266, 1), RECORD_12(2267, 1);

    private final int id;
    private final Constructor ctor;
    private static Material[] byId = new Material[383];
    private static final Map BY_NAME = Maps.newHashMap();
    private final int maxStack;
    private final short durability;
    private static int[] $SWITCH_TABLE$org$bukkit$Material;

    static {
        Material[] amaterial;
        int i = (amaterial = values()).length;

        for (int j = 0; j < i; ++j) {
            Material material = amaterial[j];

            if (Material.byId.length > material.id) {
                Material.byId[material.id] = material;
            } else {
                Material.byId = (Material[]) Java15Compat.Arrays_copyOfRange(Material.byId, 0, material.id + 2);
                Material.byId[material.id] = material;
            }

            Material.BY_NAME.put(material.name(), material);
        }

    }

    private Material(int id) {
        this(id, 64);
    }

    private Material(int id, int stack) {
        this(id, stack, MaterialData.class);
    }

    private Material(int id, int stack, int durability) {
        this(id, stack, durability, MaterialData.class);
    }

    private Material(int id, Class data) {
        this(id, 64, data);
    }

    private Material(int id, int stack, Class data) {
        this(id, stack, 0, data);
    }

    private Material(int id, int stack, int durability, Class data) {
        this.id = id;
        this.durability = (short) durability;
        this.maxStack = stack;

        try {
            this.ctor = data.getConstructor(new Class[] { Integer.TYPE, Byte.TYPE});
        } catch (NoSuchMethodException nosuchmethodexception) {
            throw new AssertionError(nosuchmethodexception);
        } catch (SecurityException securityexception) {
            throw new AssertionError(securityexception);
        }
    }

    /** @deprecated */
    @Deprecated
    public int getId() {
        return this.id;
    }

    public int getMaxStackSize() {
        return this.maxStack;
    }

    public short getMaxDurability() {
        return this.durability;
    }

    public Class getData() {
        return this.ctor.getDeclaringClass();
    }

    /** @deprecated */
    @Deprecated
    public MaterialData getNewData(byte raw) {
        try {
            return (MaterialData) this.ctor.newInstance(new Object[] { Integer.valueOf(this.id), Byte.valueOf(raw)});
        } catch (InstantiationException instantiationexception) {
            Throwable t = instantiationexception.getCause();

            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof Error) {
                throw (Error) t;
            } else {
                throw new AssertionError(t);
            }
        } catch (Throwable throwable) {
            throw new AssertionError(throwable);
        }
    }

    public boolean isBlock() {
        return this.id < 256;
    }

    public boolean isEdible() {
        switch ($SWITCH_TABLE$org$bukkit$Material()[this.ordinal()]) {
        case 203:
        case 225:
        case 240:
        case 262:
        case 263:
        case 265:
        case 292:
        case 293:
        case 300:
        case 303:
        case 306:
        case 307:
        case 308:
        case 309:
        case 310:
        case 318:
        case 334:
        case 335:
        case 336:
        case 337:
        case 339:
        case 343:
        case 354:
        case 355:
        case 356:
        case 366:
        case 367:
            return true;

        default:
            return false;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Material getMaterial(int id) {
        return Material.byId.length > id && id >= 0 ? Material.byId[id] : null;
    }

    public static Material getMaterial(String name) {
        return (Material) Material.BY_NAME.get(name);
    }

    public static Material matchMaterial(String name) {
        Validate.notNull(name, "Name cannot be null");
        Material result = null;

        try {
            result = getMaterial(Integer.parseInt(name));
        } catch (NumberFormatException numberformatexception) {
            ;
        }

        if (result == null) {
            String filtered = name.toUpperCase();

            filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
            result = (Material) Material.BY_NAME.get(filtered);
        }

        return result;
    }

    public boolean isRecord() {
        return this.id >= Material.GOLD_RECORD.id && this.id <= Material.RECORD_12.id;
    }

    public boolean isSolid() {
        if (this.isBlock() && this.id != 0) {
            switch ($SWITCH_TABLE$org$bukkit$Material()[this.ordinal()]) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 30:
            case 34:
            case 35:
            case 36:
            case 37:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 53:
            case 54:
            case 55:
            case 57:
            case 58:
            case 59:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 68:
            case 69:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 80:
            case 81:
            case 82:
            case 83:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 108:
            case 109:
            case 110:
            case 111:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 119:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 129:
            case 130:
            case 131:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 146:
            case 147:
            case 148:
            case 149:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 173:
            case 174:
            case 175:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            case 198:
                return true;

            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 38:
            case 39:
            case 40:
            case 41:
            case 51:
            case 52:
            case 56:
            case 60:
            case 66:
            case 67:
            case 70:
            case 76:
            case 77:
            case 78:
            case 79:
            case 84:
            case 91:
            case 94:
            case 95:
            case 105:
            case 106:
            case 107:
            case 112:
            case 116:
            case 120:
            case 128:
            case 132:
            case 133:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 150:
            case 151:
            case 158:
            case 172:
            case 176:
            default:
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isTransparent() {
        if (!this.isBlock()) {
            return false;
        } else {
            switch ($SWITCH_TABLE$org$bukkit$Material()[this.ordinal()]) {
            case 1:
            case 7:
            case 28:
            case 29:
            case 32:
            case 33:
            case 38:
            case 39:
            case 40:
            case 41:
            case 51:
            case 52:
            case 56:
            case 60:
            case 66:
            case 67:
            case 70:
            case 76:
            case 77:
            case 78:
            case 79:
            case 84:
            case 91:
            case 94:
            case 95:
            case 105:
            case 106:
            case 107:
            case 112:
            case 116:
            case 120:
            case 128:
            case 132:
            case 133:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 150:
            case 151:
            case 158:
            case 172:
            case 176:
                return true;

            default:
                return false;
            }
        }
    }

    public boolean isFlammable() {
        if (!this.isBlock()) {
            return false;
        } else {
            switch ($SWITCH_TABLE$org$bukkit$Material()[this.ordinal()]) {
            case 6:
            case 18:
            case 19:
            case 26:
            case 27:
            case 32:
            case 33:
            case 36:
            case 47:
            case 48:
            case 54:
            case 55:
            case 59:
            case 64:
            case 65:
            case 69:
            case 73:
            case 85:
            case 86:
            case 97:
            case 100:
            case 101:
            case 107:
            case 108:
            case 126:
            case 127:
            case 135:
            case 136:
            case 137:
            case 147:
            case 152:
            case 162:
            case 163:
            case 164:
            case 165:
            case 172:
            case 176:
            case 177:
            case 178:
            case 179:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            case 198:
                return true;

            default:
                return false;
            }
        }
    }

    public boolean isBurnable() {
        if (!this.isBlock()) {
            return false;
        } else {
            switch ($SWITCH_TABLE$org$bukkit$Material()[this.ordinal()]) {
            case 6:
            case 18:
            case 19:
            case 32:
            case 33:
            case 36:
            case 38:
            case 39:
            case 47:
            case 48:
            case 54:
            case 86:
            case 107:
            case 108:
            case 126:
            case 127:
            case 135:
            case 136:
            case 137:
            case 162:
            case 163:
            case 171:
            case 172:
            case 174:
            case 176:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
                return true;

            default:
                return false;
            }
        }
    }

    public boolean isOccluding() {
        if (!this.isBlock()) {
            return false;
        } else {
            switch ($SWITCH_TABLE$org$bukkit$Material()[this.ordinal()]) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 20:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 36:
            case 42:
            case 43:
            case 44:
            case 46:
            case 48:
            case 49:
            case 50:
            case 53:
            case 57:
            case 58:
            case 59:
            case 62:
            case 63:
            case 74:
            case 75:
            case 81:
            case 83:
            case 85:
            case 87:
            case 88:
            case 89:
            case 92:
            case 98:
            case 99:
            case 100:
            case 101:
            case 104:
            case 111:
            case 113:
            case 121:
            case 122:
            case 124:
            case 125:
            case 126:
            case 130:
            case 134:
            case 138:
            case 154:
            case 156:
            case 159:
            case 160:
            case 163:
            case 166:
            case 167:
            case 169:
            case 171:
            case 173:
            case 174:
            case 175:
            case 180:
            case 182:
                return true;

            default:
                return false;
            }
        }
    }

    public boolean hasGravity() {
        if (!this.isBlock()) {
            return false;
        } else {
            switch ($SWITCH_TABLE$org$bukkit$Material()[this.ordinal()]) {
            case 13:
            case 14:
            case 146:
                return true;

            default:
                return false;
            }
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$Material() {
        int[] aint = Material.$SWITCH_TABLE$org$bukkit$Material;

        if (Material.$SWITCH_TABLE$org$bukkit$Material != null) {
            return aint;
        } else {
            int[] aint1 = new int[values().length];

            try {
                aint1[Material.ACACIA_DOOR.ordinal()] = 197;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[Material.ACACIA_DOOR_ITEM.ordinal()] = 372;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[Material.ACACIA_FENCE.ordinal()] = 193;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[Material.ACACIA_FENCE_GATE.ordinal()] = 188;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[Material.ACACIA_STAIRS.ordinal()] = 164;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[Material.ACTIVATOR_RAIL.ordinal()] = 158;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[Material.AIR.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[Material.ANVIL.ordinal()] = 146;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[Material.APPLE.ordinal()] = 203;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[Material.ARMOR_STAND.ordinal()] = 359;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[Material.ARROW.ordinal()] = 205;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[Material.BAKED_POTATO.ordinal()] = 336;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[Material.BANNER.ordinal()] = 368;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[Material.BARRIER.ordinal()] = 167;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[Material.BEACON.ordinal()] = 139;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[Material.BED.ordinal()] = 298;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[Material.BEDROCK.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[Material.BED_BLOCK.ordinal()] = 27;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[Material.BIRCH_DOOR.ordinal()] = 195;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[Material.BIRCH_DOOR_ITEM.ordinal()] = 370;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[Material.BIRCH_FENCE.ordinal()] = 190;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[Material.BIRCH_FENCE_GATE.ordinal()] = 185;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[Material.BIRCH_WOOD_STAIRS.ordinal()] = 136;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[Material.BLAZE_POWDER.ordinal()] = 320;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[Material.BLAZE_ROD.ordinal()] = 312;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[Material.BOAT.ordinal()] = 276;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            try {
                aint1[Material.BONE.ordinal()] = 295;
            } catch (NoSuchFieldError nosuchfielderror26) {
                ;
            }

            try {
                aint1[Material.BOOK.ordinal()] = 283;
            } catch (NoSuchFieldError nosuchfielderror27) {
                ;
            }

            try {
                aint1[Material.BOOKSHELF.ordinal()] = 48;
            } catch (NoSuchFieldError nosuchfielderror28) {
                ;
            }

            try {
                aint1[Material.BOOK_AND_QUILL.ordinal()] = 329;
            } catch (NoSuchFieldError nosuchfielderror29) {
                ;
            }

            try {
                aint1[Material.BOW.ordinal()] = 204;
            } catch (NoSuchFieldError nosuchfielderror30) {
                ;
            }

            try {
                aint1[Material.BOWL.ordinal()] = 224;
            } catch (NoSuchFieldError nosuchfielderror31) {
                ;
            }

            try {
                aint1[Material.BREAD.ordinal()] = 240;
            } catch (NoSuchFieldError nosuchfielderror32) {
                ;
            }

            try {
                aint1[Material.BREWING_STAND.ordinal()] = 118;
            } catch (NoSuchFieldError nosuchfielderror33) {
                ;
            }

            try {
                aint1[Material.BREWING_STAND_ITEM.ordinal()] = 322;
            } catch (NoSuchFieldError nosuchfielderror34) {
                ;
            }

            try {
                aint1[Material.BRICK.ordinal()] = 46;
            } catch (NoSuchFieldError nosuchfielderror35) {
                ;
            }

            try {
                aint1[Material.BRICK_STAIRS.ordinal()] = 109;
            } catch (NoSuchFieldError nosuchfielderror36) {
                ;
            }

            try {
                aint1[Material.BROWN_MUSHROOM.ordinal()] = 40;
            } catch (NoSuchFieldError nosuchfielderror37) {
                ;
            }

            try {
                aint1[Material.BUCKET.ordinal()] = 268;
            } catch (NoSuchFieldError nosuchfielderror38) {
                ;
            }

            try {
                aint1[Material.BURNING_FURNACE.ordinal()] = 63;
            } catch (NoSuchFieldError nosuchfielderror39) {
                ;
            }

            try {
                aint1[Material.CACTUS.ordinal()] = 82;
            } catch (NoSuchFieldError nosuchfielderror40) {
                ;
            }

            try {
                aint1[Material.CAKE.ordinal()] = 297;
            } catch (NoSuchFieldError nosuchfielderror41) {
                ;
            }

            try {
                aint1[Material.CAKE_BLOCK.ordinal()] = 93;
            } catch (NoSuchFieldError nosuchfielderror42) {
                ;
            }

            try {
                aint1[Material.CARPET.ordinal()] = 172;
            } catch (NoSuchFieldError nosuchfielderror43) {
                ;
            }

            try {
                aint1[Material.CARROT.ordinal()] = 142;
            } catch (NoSuchFieldError nosuchfielderror44) {
                ;
            }

            try {
                aint1[Material.CARROT_ITEM.ordinal()] = 334;
            } catch (NoSuchFieldError nosuchfielderror45) {
                ;
            }

            try {
                aint1[Material.CARROT_STICK.ordinal()] = 341;
            } catch (NoSuchFieldError nosuchfielderror46) {
                ;
            }

            try {
                aint1[Material.CAULDRON.ordinal()] = 119;
            } catch (NoSuchFieldError nosuchfielderror47) {
                ;
            }

            try {
                aint1[Material.CAULDRON_ITEM.ordinal()] = 323;
            } catch (NoSuchFieldError nosuchfielderror48) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_BOOTS.ordinal()] = 248;
            } catch (NoSuchFieldError nosuchfielderror49) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_CHESTPLATE.ordinal()] = 246;
            } catch (NoSuchFieldError nosuchfielderror50) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_HELMET.ordinal()] = 245;
            } catch (NoSuchFieldError nosuchfielderror51) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_LEGGINGS.ordinal()] = 247;
            } catch (NoSuchFieldError nosuchfielderror52) {
                ;
            }

            try {
                aint1[Material.CHEST.ordinal()] = 55;
            } catch (NoSuchFieldError nosuchfielderror53) {
                ;
            }

            try {
                aint1[Material.CLAY.ordinal()] = 83;
            } catch (NoSuchFieldError nosuchfielderror54) {
                ;
            }

            try {
                aint1[Material.CLAY_BALL.ordinal()] = 280;
            } catch (NoSuchFieldError nosuchfielderror55) {
                ;
            }

            try {
                aint1[Material.CLAY_BRICK.ordinal()] = 279;
            } catch (NoSuchFieldError nosuchfielderror56) {
                ;
            }

            try {
                aint1[Material.COAL.ordinal()] = 206;
            } catch (NoSuchFieldError nosuchfielderror57) {
                ;
            }

            try {
                aint1[Material.COAL_BLOCK.ordinal()] = 174;
            } catch (NoSuchFieldError nosuchfielderror58) {
                ;
            }

            try {
                aint1[Material.COAL_ORE.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror59) {
                ;
            }

            try {
                aint1[Material.COBBLESTONE.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror60) {
                ;
            }

            try {
                aint1[Material.COBBLESTONE_STAIRS.ordinal()] = 68;
            } catch (NoSuchFieldError nosuchfielderror61) {
                ;
            }

            try {
                aint1[Material.COBBLE_WALL.ordinal()] = 140;
            } catch (NoSuchFieldError nosuchfielderror62) {
                ;
            }

            try {
                aint1[Material.COCOA.ordinal()] = 128;
            } catch (NoSuchFieldError nosuchfielderror63) {
                ;
            }

            try {
                aint1[Material.COMMAND.ordinal()] = 138;
            } catch (NoSuchFieldError nosuchfielderror64) {
                ;
            }

            try {
                aint1[Material.COMMAND_MINECART.ordinal()] = 365;
            } catch (NoSuchFieldError nosuchfielderror65) {
                ;
            }

            try {
                aint1[Material.COMPASS.ordinal()] = 288;
            } catch (NoSuchFieldError nosuchfielderror66) {
                ;
            }

            try {
                aint1[Material.COOKED_BEEF.ordinal()] = 307;
            } catch (NoSuchFieldError nosuchfielderror67) {
                ;
            }

            try {
                aint1[Material.COOKED_CHICKEN.ordinal()] = 309;
            } catch (NoSuchFieldError nosuchfielderror68) {
                ;
            }

            try {
                aint1[Material.COOKED_FISH.ordinal()] = 293;
            } catch (NoSuchFieldError nosuchfielderror69) {
                ;
            }

            try {
                aint1[Material.COOKED_MUTTON.ordinal()] = 367;
            } catch (NoSuchFieldError nosuchfielderror70) {
                ;
            }

            try {
                aint1[Material.COOKED_RABBIT.ordinal()] = 355;
            } catch (NoSuchFieldError nosuchfielderror71) {
                ;
            }

            try {
                aint1[Material.COOKIE.ordinal()] = 300;
            } catch (NoSuchFieldError nosuchfielderror72) {
                ;
            }

            try {
                aint1[Material.CROPS.ordinal()] = 60;
            } catch (NoSuchFieldError nosuchfielderror73) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_DOOR.ordinal()] = 198;
            } catch (NoSuchFieldError nosuchfielderror74) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_DOOR_ITEM.ordinal()] = 373;
            } catch (NoSuchFieldError nosuchfielderror75) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_FENCE.ordinal()] = 192;
            } catch (NoSuchFieldError nosuchfielderror76) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_FENCE_GATE.ordinal()] = 187;
            } catch (NoSuchFieldError nosuchfielderror77) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_STAIRS.ordinal()] = 165;
            } catch (NoSuchFieldError nosuchfielderror78) {
                ;
            }

            try {
                aint1[Material.DAYLIGHT_DETECTOR.ordinal()] = 152;
            } catch (NoSuchFieldError nosuchfielderror79) {
                ;
            }

            try {
                aint1[Material.DAYLIGHT_DETECTOR_INVERTED.ordinal()] = 179;
            } catch (NoSuchFieldError nosuchfielderror80) {
                ;
            }

            try {
                aint1[Material.DEAD_BUSH.ordinal()] = 33;
            } catch (NoSuchFieldError nosuchfielderror81) {
                ;
            }

            try {
                aint1[Material.DETECTOR_RAIL.ordinal()] = 29;
            } catch (NoSuchFieldError nosuchfielderror82) {
                ;
            }

            try {
                aint1[Material.DIAMOND.ordinal()] = 207;
            } catch (NoSuchFieldError nosuchfielderror83) {
                ;
            }

            try {
                aint1[Material.DIAMOND_AXE.ordinal()] = 222;
            } catch (NoSuchFieldError nosuchfielderror84) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BARDING.ordinal()] = 362;
            } catch (NoSuchFieldError nosuchfielderror85) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BLOCK.ordinal()] = 58;
            } catch (NoSuchFieldError nosuchfielderror86) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BOOTS.ordinal()] = 256;
            } catch (NoSuchFieldError nosuchfielderror87) {
                ;
            }

            try {
                aint1[Material.DIAMOND_CHESTPLATE.ordinal()] = 254;
            } catch (NoSuchFieldError nosuchfielderror88) {
                ;
            }

            try {
                aint1[Material.DIAMOND_HELMET.ordinal()] = 253;
            } catch (NoSuchFieldError nosuchfielderror89) {
                ;
            }

            try {
                aint1[Material.DIAMOND_HOE.ordinal()] = 236;
            } catch (NoSuchFieldError nosuchfielderror90) {
                ;
            }

            try {
                aint1[Material.DIAMOND_LEGGINGS.ordinal()] = 255;
            } catch (NoSuchFieldError nosuchfielderror91) {
                ;
            }

            try {
                aint1[Material.DIAMOND_ORE.ordinal()] = 57;
            } catch (NoSuchFieldError nosuchfielderror92) {
                ;
            }

            try {
                aint1[Material.DIAMOND_PICKAXE.ordinal()] = 221;
            } catch (NoSuchFieldError nosuchfielderror93) {
                ;
            }

            try {
                aint1[Material.DIAMOND_SPADE.ordinal()] = 220;
            } catch (NoSuchFieldError nosuchfielderror94) {
                ;
            }

            try {
                aint1[Material.DIAMOND_SWORD.ordinal()] = 219;
            } catch (NoSuchFieldError nosuchfielderror95) {
                ;
            }

            try {
                aint1[Material.DIODE.ordinal()] = 299;
            } catch (NoSuchFieldError nosuchfielderror96) {
                ;
            }

            try {
                aint1[Material.DIODE_BLOCK_OFF.ordinal()] = 94;
            } catch (NoSuchFieldError nosuchfielderror97) {
                ;
            }

            try {
                aint1[Material.DIODE_BLOCK_ON.ordinal()] = 95;
            } catch (NoSuchFieldError nosuchfielderror98) {
                ;
            }

            try {
                aint1[Material.DIRT.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror99) {
                ;
            }

            try {
                aint1[Material.DISPENSER.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror100) {
                ;
            }

            try {
                aint1[Material.DOUBLE_PLANT.ordinal()] = 176;
            } catch (NoSuchFieldError nosuchfielderror101) {
                ;
            }

            try {
                aint1[Material.DOUBLE_STEP.ordinal()] = 44;
            } catch (NoSuchFieldError nosuchfielderror102) {
                ;
            }

            try {
                aint1[Material.DOUBLE_STONE_SLAB2.ordinal()] = 182;
            } catch (NoSuchFieldError nosuchfielderror103) {
                ;
            }

            try {
                aint1[Material.DRAGON_EGG.ordinal()] = 123;
            } catch (NoSuchFieldError nosuchfielderror104) {
                ;
            }

            try {
                aint1[Material.DROPPER.ordinal()] = 159;
            } catch (NoSuchFieldError nosuchfielderror105) {
                ;
            }

            try {
                aint1[Material.EGG.ordinal()] = 287;
            } catch (NoSuchFieldError nosuchfielderror106) {
                ;
            }

            try {
                aint1[Material.EMERALD.ordinal()] = 331;
            } catch (NoSuchFieldError nosuchfielderror107) {
                ;
            }

            try {
                aint1[Material.EMERALD_BLOCK.ordinal()] = 134;
            } catch (NoSuchFieldError nosuchfielderror108) {
                ;
            }

            try {
                aint1[Material.EMERALD_ORE.ordinal()] = 130;
            } catch (NoSuchFieldError nosuchfielderror109) {
                ;
            }

            try {
                aint1[Material.EMPTY_MAP.ordinal()] = 338;
            } catch (NoSuchFieldError nosuchfielderror110) {
                ;
            }

            try {
                aint1[Material.ENCHANTED_BOOK.ordinal()] = 346;
            } catch (NoSuchFieldError nosuchfielderror111) {
                ;
            }

            try {
                aint1[Material.ENCHANTMENT_TABLE.ordinal()] = 117;
            } catch (NoSuchFieldError nosuchfielderror112) {
                ;
            }

            try {
                aint1[Material.ENDER_CHEST.ordinal()] = 131;
            } catch (NoSuchFieldError nosuchfielderror113) {
                ;
            }

            try {
                aint1[Material.ENDER_PEARL.ordinal()] = 311;
            } catch (NoSuchFieldError nosuchfielderror114) {
                ;
            }

            try {
                aint1[Material.ENDER_PORTAL.ordinal()] = 120;
            } catch (NoSuchFieldError nosuchfielderror115) {
                ;
            }

            try {
                aint1[Material.ENDER_PORTAL_FRAME.ordinal()] = 121;
            } catch (NoSuchFieldError nosuchfielderror116) {
                ;
            }

            try {
                aint1[Material.ENDER_STONE.ordinal()] = 122;
            } catch (NoSuchFieldError nosuchfielderror117) {
                ;
            }

            try {
                aint1[Material.EXPLOSIVE_MINECART.ordinal()] = 350;
            } catch (NoSuchFieldError nosuchfielderror118) {
                ;
            }

            try {
                aint1[Material.EXP_BOTTLE.ordinal()] = 327;
            } catch (NoSuchFieldError nosuchfielderror119) {
                ;
            }

            try {
                aint1[Material.EYE_OF_ENDER.ordinal()] = 324;
            } catch (NoSuchFieldError nosuchfielderror120) {
                ;
            }

            try {
                aint1[Material.FEATHER.ordinal()] = 231;
            } catch (NoSuchFieldError nosuchfielderror121) {
                ;
            }

            try {
                aint1[Material.FENCE.ordinal()] = 86;
            } catch (NoSuchFieldError nosuchfielderror122) {
                ;
            }

            try {
                aint1[Material.FENCE_GATE.ordinal()] = 108;
            } catch (NoSuchFieldError nosuchfielderror123) {
                ;
            }

            try {
                aint1[Material.FERMENTED_SPIDER_EYE.ordinal()] = 319;
            } catch (NoSuchFieldError nosuchfielderror124) {
                ;
            }

            try {
                aint1[Material.FIRE.ordinal()] = 52;
            } catch (NoSuchFieldError nosuchfielderror125) {
                ;
            }

            try {
                aint1[Material.FIREBALL.ordinal()] = 328;
            } catch (NoSuchFieldError nosuchfielderror126) {
                ;
            }

            try {
                aint1[Material.FIREWORK.ordinal()] = 344;
            } catch (NoSuchFieldError nosuchfielderror127) {
                ;
            }

            try {
                aint1[Material.FIREWORK_CHARGE.ordinal()] = 345;
            } catch (NoSuchFieldError nosuchfielderror128) {
                ;
            }

            try {
                aint1[Material.FISHING_ROD.ordinal()] = 289;
            } catch (NoSuchFieldError nosuchfielderror129) {
                ;
            }

            try {
                aint1[Material.FLINT.ordinal()] = 261;
            } catch (NoSuchFieldError nosuchfielderror130) {
                ;
            }

            try {
                aint1[Material.FLINT_AND_STEEL.ordinal()] = 202;
            } catch (NoSuchFieldError nosuchfielderror131) {
                ;
            }

            try {
                aint1[Material.FLOWER_POT.ordinal()] = 141;
            } catch (NoSuchFieldError nosuchfielderror132) {
                ;
            }

            try {
                aint1[Material.FLOWER_POT_ITEM.ordinal()] = 333;
            } catch (NoSuchFieldError nosuchfielderror133) {
                ;
            }

            try {
                aint1[Material.FURNACE.ordinal()] = 62;
            } catch (NoSuchFieldError nosuchfielderror134) {
                ;
            }

            try {
                aint1[Material.GHAST_TEAR.ordinal()] = 313;
            } catch (NoSuchFieldError nosuchfielderror135) {
                ;
            }

            try {
                aint1[Material.GLASS.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror136) {
                ;
            }

            try {
                aint1[Material.GLASS_BOTTLE.ordinal()] = 317;
            } catch (NoSuchFieldError nosuchfielderror137) {
                ;
            }

            try {
                aint1[Material.GLOWING_REDSTONE_ORE.ordinal()] = 75;
            } catch (NoSuchFieldError nosuchfielderror138) {
                ;
            }

            try {
                aint1[Material.GLOWSTONE.ordinal()] = 90;
            } catch (NoSuchFieldError nosuchfielderror139) {
                ;
            }

            try {
                aint1[Material.GLOWSTONE_DUST.ordinal()] = 291;
            } catch (NoSuchFieldError nosuchfielderror140) {
                ;
            }

            try {
                aint1[Material.GOLDEN_APPLE.ordinal()] = 265;
            } catch (NoSuchFieldError nosuchfielderror141) {
                ;
            }

            try {
                aint1[Material.GOLDEN_CARROT.ordinal()] = 339;
            } catch (NoSuchFieldError nosuchfielderror142) {
                ;
            }

            try {
                aint1[Material.GOLD_AXE.ordinal()] = 229;
            } catch (NoSuchFieldError nosuchfielderror143) {
                ;
            }

            try {
                aint1[Material.GOLD_BARDING.ordinal()] = 361;
            } catch (NoSuchFieldError nosuchfielderror144) {
                ;
            }

            try {
                aint1[Material.GOLD_BLOCK.ordinal()] = 42;
            } catch (NoSuchFieldError nosuchfielderror145) {
                ;
            }

            try {
                aint1[Material.GOLD_BOOTS.ordinal()] = 260;
            } catch (NoSuchFieldError nosuchfielderror146) {
                ;
            }

            try {
                aint1[Material.GOLD_CHESTPLATE.ordinal()] = 258;
            } catch (NoSuchFieldError nosuchfielderror147) {
                ;
            }

            try {
                aint1[Material.GOLD_HELMET.ordinal()] = 257;
            } catch (NoSuchFieldError nosuchfielderror148) {
                ;
            }

            try {
                aint1[Material.GOLD_HOE.ordinal()] = 237;
            } catch (NoSuchFieldError nosuchfielderror149) {
                ;
            }

            try {
                aint1[Material.GOLD_INGOT.ordinal()] = 209;
            } catch (NoSuchFieldError nosuchfielderror150) {
                ;
            }

            try {
                aint1[Material.GOLD_LEGGINGS.ordinal()] = 259;
            } catch (NoSuchFieldError nosuchfielderror151) {
                ;
            }

            try {
                aint1[Material.GOLD_NUGGET.ordinal()] = 314;
            } catch (NoSuchFieldError nosuchfielderror152) {
                ;
            }

            try {
                aint1[Material.GOLD_ORE.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror153) {
                ;
            }

            try {
                aint1[Material.GOLD_PICKAXE.ordinal()] = 228;
            } catch (NoSuchFieldError nosuchfielderror154) {
                ;
            }

            try {
                aint1[Material.GOLD_PLATE.ordinal()] = 148;
            } catch (NoSuchFieldError nosuchfielderror155) {
                ;
            }

            try {
                aint1[Material.GOLD_RECORD.ordinal()] = 374;
            } catch (NoSuchFieldError nosuchfielderror156) {
                ;
            }

            try {
                aint1[Material.GOLD_SPADE.ordinal()] = 227;
            } catch (NoSuchFieldError nosuchfielderror157) {
                ;
            }

            try {
                aint1[Material.GOLD_SWORD.ordinal()] = 226;
            } catch (NoSuchFieldError nosuchfielderror158) {
                ;
            }

            try {
                aint1[Material.GRASS.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror159) {
                ;
            }

            try {
                aint1[Material.GRAVEL.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror160) {
                ;
            }

            try {
                aint1[Material.GREEN_RECORD.ordinal()] = 375;
            } catch (NoSuchFieldError nosuchfielderror161) {
                ;
            }

            try {
                aint1[Material.GRILLED_PORK.ordinal()] = 263;
            } catch (NoSuchFieldError nosuchfielderror162) {
                ;
            }

            try {
                aint1[Material.HARD_CLAY.ordinal()] = 173;
            } catch (NoSuchFieldError nosuchfielderror163) {
                ;
            }

            try {
                aint1[Material.HAY_BLOCK.ordinal()] = 171;
            } catch (NoSuchFieldError nosuchfielderror164) {
                ;
            }

            try {
                aint1[Material.HOPPER.ordinal()] = 155;
            } catch (NoSuchFieldError nosuchfielderror165) {
                ;
            }

            try {
                aint1[Material.HOPPER_MINECART.ordinal()] = 351;
            } catch (NoSuchFieldError nosuchfielderror166) {
                ;
            }

            try {
                aint1[Material.HUGE_MUSHROOM_1.ordinal()] = 100;
            } catch (NoSuchFieldError nosuchfielderror167) {
                ;
            }

            try {
                aint1[Material.HUGE_MUSHROOM_2.ordinal()] = 101;
            } catch (NoSuchFieldError nosuchfielderror168) {
                ;
            }

            try {
                aint1[Material.ICE.ordinal()] = 80;
            } catch (NoSuchFieldError nosuchfielderror169) {
                ;
            }

            try {
                aint1[Material.INK_SACK.ordinal()] = 294;
            } catch (NoSuchFieldError nosuchfielderror170) {
                ;
            }

            try {
                aint1[Material.IRON_AXE.ordinal()] = 201;
            } catch (NoSuchFieldError nosuchfielderror171) {
                ;
            }

            try {
                aint1[Material.IRON_BARDING.ordinal()] = 360;
            } catch (NoSuchFieldError nosuchfielderror172) {
                ;
            }

            try {
                aint1[Material.IRON_BLOCK.ordinal()] = 43;
            } catch (NoSuchFieldError nosuchfielderror173) {
                ;
            }

            try {
                aint1[Material.IRON_BOOTS.ordinal()] = 252;
            } catch (NoSuchFieldError nosuchfielderror174) {
                ;
            }

            try {
                aint1[Material.IRON_CHESTPLATE.ordinal()] = 250;
            } catch (NoSuchFieldError nosuchfielderror175) {
                ;
            }

            try {
                aint1[Material.IRON_DOOR.ordinal()] = 273;
            } catch (NoSuchFieldError nosuchfielderror176) {
                ;
            }

            try {
                aint1[Material.IRON_DOOR_BLOCK.ordinal()] = 72;
            } catch (NoSuchFieldError nosuchfielderror177) {
                ;
            }

            try {
                aint1[Material.IRON_FENCE.ordinal()] = 102;
            } catch (NoSuchFieldError nosuchfielderror178) {
                ;
            }

            try {
                aint1[Material.IRON_HELMET.ordinal()] = 249;
            } catch (NoSuchFieldError nosuchfielderror179) {
                ;
            }

            try {
                aint1[Material.IRON_HOE.ordinal()] = 235;
            } catch (NoSuchFieldError nosuchfielderror180) {
                ;
            }

            try {
                aint1[Material.IRON_INGOT.ordinal()] = 208;
            } catch (NoSuchFieldError nosuchfielderror181) {
                ;
            }

            try {
                aint1[Material.IRON_LEGGINGS.ordinal()] = 251;
            } catch (NoSuchFieldError nosuchfielderror182) {
                ;
            }

            try {
                aint1[Material.IRON_ORE.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror183) {
                ;
            }

            try {
                aint1[Material.IRON_PICKAXE.ordinal()] = 200;
            } catch (NoSuchFieldError nosuchfielderror184) {
                ;
            }

            try {
                aint1[Material.IRON_PLATE.ordinal()] = 149;
            } catch (NoSuchFieldError nosuchfielderror185) {
                ;
            }

            try {
                aint1[Material.IRON_SPADE.ordinal()] = 199;
            } catch (NoSuchFieldError nosuchfielderror186) {
                ;
            }

            try {
                aint1[Material.IRON_SWORD.ordinal()] = 210;
            } catch (NoSuchFieldError nosuchfielderror187) {
                ;
            }

            try {
                aint1[Material.IRON_TRAPDOOR.ordinal()] = 168;
            } catch (NoSuchFieldError nosuchfielderror188) {
                ;
            }

            try {
                aint1[Material.ITEM_FRAME.ordinal()] = 332;
            } catch (NoSuchFieldError nosuchfielderror189) {
                ;
            }

            try {
                aint1[Material.JACK_O_LANTERN.ordinal()] = 92;
            } catch (NoSuchFieldError nosuchfielderror190) {
                ;
            }

            try {
                aint1[Material.JUKEBOX.ordinal()] = 85;
            } catch (NoSuchFieldError nosuchfielderror191) {
                ;
            }

            try {
                aint1[Material.JUNGLE_DOOR.ordinal()] = 196;
            } catch (NoSuchFieldError nosuchfielderror192) {
                ;
            }

            try {
                aint1[Material.JUNGLE_DOOR_ITEM.ordinal()] = 371;
            } catch (NoSuchFieldError nosuchfielderror193) {
                ;
            }

            try {
                aint1[Material.JUNGLE_FENCE.ordinal()] = 191;
            } catch (NoSuchFieldError nosuchfielderror194) {
                ;
            }

            try {
                aint1[Material.JUNGLE_FENCE_GATE.ordinal()] = 186;
            } catch (NoSuchFieldError nosuchfielderror195) {
                ;
            }

            try {
                aint1[Material.JUNGLE_WOOD_STAIRS.ordinal()] = 137;
            } catch (NoSuchFieldError nosuchfielderror196) {
                ;
            }

            try {
                aint1[Material.LADDER.ordinal()] = 66;
            } catch (NoSuchFieldError nosuchfielderror197) {
                ;
            }

            try {
                aint1[Material.LAPIS_BLOCK.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror198) {
                ;
            }

            try {
                aint1[Material.LAPIS_ORE.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror199) {
                ;
            }

            try {
                aint1[Material.LAVA.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror200) {
                ;
            }

            try {
                aint1[Material.LAVA_BUCKET.ordinal()] = 270;
            } catch (NoSuchFieldError nosuchfielderror201) {
                ;
            }

            try {
                aint1[Material.LEASH.ordinal()] = 363;
            } catch (NoSuchFieldError nosuchfielderror202) {
                ;
            }

            try {
                aint1[Material.LEATHER.ordinal()] = 277;
            } catch (NoSuchFieldError nosuchfielderror203) {
                ;
            }

            try {
                aint1[Material.LEATHER_BOOTS.ordinal()] = 244;
            } catch (NoSuchFieldError nosuchfielderror204) {
                ;
            }

            try {
                aint1[Material.LEATHER_CHESTPLATE.ordinal()] = 242;
            } catch (NoSuchFieldError nosuchfielderror205) {
                ;
            }

            try {
                aint1[Material.LEATHER_HELMET.ordinal()] = 241;
            } catch (NoSuchFieldError nosuchfielderror206) {
                ;
            }

            try {
                aint1[Material.LEATHER_LEGGINGS.ordinal()] = 243;
            } catch (NoSuchFieldError nosuchfielderror207) {
                ;
            }

            try {
                aint1[Material.LEAVES.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror208) {
                ;
            }

            try {
                aint1[Material.LEAVES_2.ordinal()] = 162;
            } catch (NoSuchFieldError nosuchfielderror209) {
                ;
            }

            try {
                aint1[Material.LEVER.ordinal()] = 70;
            } catch (NoSuchFieldError nosuchfielderror210) {
                ;
            }

            try {
                aint1[Material.LOG.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror211) {
                ;
            }

            try {
                aint1[Material.LOG_2.ordinal()] = 163;
            } catch (NoSuchFieldError nosuchfielderror212) {
                ;
            }

            try {
                aint1[Material.LONG_GRASS.ordinal()] = 32;
            } catch (NoSuchFieldError nosuchfielderror213) {
                ;
            }

            try {
                aint1[Material.MAGMA_CREAM.ordinal()] = 321;
            } catch (NoSuchFieldError nosuchfielderror214) {
                ;
            }

            try {
                aint1[Material.MAP.ordinal()] = 301;
            } catch (NoSuchFieldError nosuchfielderror215) {
                ;
            }

            try {
                aint1[Material.MELON.ordinal()] = 303;
            } catch (NoSuchFieldError nosuchfielderror216) {
                ;
            }

            try {
                aint1[Material.MELON_BLOCK.ordinal()] = 104;
            } catch (NoSuchFieldError nosuchfielderror217) {
                ;
            }

            try {
                aint1[Material.MELON_SEEDS.ordinal()] = 305;
            } catch (NoSuchFieldError nosuchfielderror218) {
                ;
            }

            try {
                aint1[Material.MELON_STEM.ordinal()] = 106;
            } catch (NoSuchFieldError nosuchfielderror219) {
                ;
            }

            try {
                aint1[Material.MILK_BUCKET.ordinal()] = 278;
            } catch (NoSuchFieldError nosuchfielderror220) {
                ;
            }

            try {
                aint1[Material.MINECART.ordinal()] = 271;
            } catch (NoSuchFieldError nosuchfielderror221) {
                ;
            }

            try {
                aint1[Material.MOB_SPAWNER.ordinal()] = 53;
            } catch (NoSuchFieldError nosuchfielderror222) {
                ;
            }

            try {
                aint1[Material.MONSTER_EGG.ordinal()] = 326;
            } catch (NoSuchFieldError nosuchfielderror223) {
                ;
            }

            try {
                aint1[Material.MONSTER_EGGS.ordinal()] = 98;
            } catch (NoSuchFieldError nosuchfielderror224) {
                ;
            }

            try {
                aint1[Material.MOSSY_COBBLESTONE.ordinal()] = 49;
            } catch (NoSuchFieldError nosuchfielderror225) {
                ;
            }

            try {
                aint1[Material.MUSHROOM_SOUP.ordinal()] = 225;
            } catch (NoSuchFieldError nosuchfielderror226) {
                ;
            }

            try {
                aint1[Material.MUTTON.ordinal()] = 366;
            } catch (NoSuchFieldError nosuchfielderror227) {
                ;
            }

            try {
                aint1[Material.MYCEL.ordinal()] = 111;
            } catch (NoSuchFieldError nosuchfielderror228) {
                ;
            }

            try {
                aint1[Material.NAME_TAG.ordinal()] = 364;
            } catch (NoSuchFieldError nosuchfielderror229) {
                ;
            }

            try {
                aint1[Material.NETHERRACK.ordinal()] = 88;
            } catch (NoSuchFieldError nosuchfielderror230) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK.ordinal()] = 113;
            } catch (NoSuchFieldError nosuchfielderror231) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK_ITEM.ordinal()] = 348;
            } catch (NoSuchFieldError nosuchfielderror232) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK_STAIRS.ordinal()] = 115;
            } catch (NoSuchFieldError nosuchfielderror233) {
                ;
            }

            try {
                aint1[Material.NETHER_FENCE.ordinal()] = 114;
            } catch (NoSuchFieldError nosuchfielderror234) {
                ;
            }

            try {
                aint1[Material.NETHER_STALK.ordinal()] = 315;
            } catch (NoSuchFieldError nosuchfielderror235) {
                ;
            }

            try {
                aint1[Material.NETHER_STAR.ordinal()] = 342;
            } catch (NoSuchFieldError nosuchfielderror236) {
                ;
            }

            try {
                aint1[Material.NETHER_WARTS.ordinal()] = 116;
            } catch (NoSuchFieldError nosuchfielderror237) {
                ;
            }

            try {
                aint1[Material.NOTE_BLOCK.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror238) {
                ;
            }

            try {
                aint1[Material.OBSIDIAN.ordinal()] = 50;
            } catch (NoSuchFieldError nosuchfielderror239) {
                ;
            }

            try {
                aint1[Material.PACKED_ICE.ordinal()] = 175;
            } catch (NoSuchFieldError nosuchfielderror240) {
                ;
            }

            try {
                aint1[Material.PAINTING.ordinal()] = 264;
            } catch (NoSuchFieldError nosuchfielderror241) {
                ;
            }

            try {
                aint1[Material.PAPER.ordinal()] = 282;
            } catch (NoSuchFieldError nosuchfielderror242) {
                ;
            }

            try {
                aint1[Material.PISTON_BASE.ordinal()] = 34;
            } catch (NoSuchFieldError nosuchfielderror243) {
                ;
            }

            try {
                aint1[Material.PISTON_EXTENSION.ordinal()] = 35;
            } catch (NoSuchFieldError nosuchfielderror244) {
                ;
            }

            try {
                aint1[Material.PISTON_MOVING_PIECE.ordinal()] = 37;
            } catch (NoSuchFieldError nosuchfielderror245) {
                ;
            }

            try {
                aint1[Material.PISTON_STICKY_BASE.ordinal()] = 30;
            } catch (NoSuchFieldError nosuchfielderror246) {
                ;
            }

            try {
                aint1[Material.POISONOUS_POTATO.ordinal()] = 337;
            } catch (NoSuchFieldError nosuchfielderror247) {
                ;
            }

            try {
                aint1[Material.PORK.ordinal()] = 262;
            } catch (NoSuchFieldError nosuchfielderror248) {
                ;
            }

            try {
                aint1[Material.PORTAL.ordinal()] = 91;
            } catch (NoSuchFieldError nosuchfielderror249) {
                ;
            }

            try {
                aint1[Material.POTATO.ordinal()] = 143;
            } catch (NoSuchFieldError nosuchfielderror250) {
                ;
            }

            try {
                aint1[Material.POTATO_ITEM.ordinal()] = 335;
            } catch (NoSuchFieldError nosuchfielderror251) {
                ;
            }

            try {
                aint1[Material.POTION.ordinal()] = 316;
            } catch (NoSuchFieldError nosuchfielderror252) {
                ;
            }

            try {
                aint1[Material.POWERED_MINECART.ordinal()] = 286;
            } catch (NoSuchFieldError nosuchfielderror253) {
                ;
            }

            try {
                aint1[Material.POWERED_RAIL.ordinal()] = 28;
            } catch (NoSuchFieldError nosuchfielderror254) {
                ;
            }

            try {
                aint1[Material.PRISMARINE.ordinal()] = 169;
            } catch (NoSuchFieldError nosuchfielderror255) {
                ;
            }

            try {
                aint1[Material.PRISMARINE_CRYSTALS.ordinal()] = 353;
            } catch (NoSuchFieldError nosuchfielderror256) {
                ;
            }

            try {
                aint1[Material.PRISMARINE_SHARD.ordinal()] = 352;
            } catch (NoSuchFieldError nosuchfielderror257) {
                ;
            }

            try {
                aint1[Material.PUMPKIN.ordinal()] = 87;
            } catch (NoSuchFieldError nosuchfielderror258) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_PIE.ordinal()] = 343;
            } catch (NoSuchFieldError nosuchfielderror259) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_SEEDS.ordinal()] = 304;
            } catch (NoSuchFieldError nosuchfielderror260) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_STEM.ordinal()] = 105;
            } catch (NoSuchFieldError nosuchfielderror261) {
                ;
            }

            try {
                aint1[Material.QUARTZ.ordinal()] = 349;
            } catch (NoSuchFieldError nosuchfielderror262) {
                ;
            }

            try {
                aint1[Material.QUARTZ_BLOCK.ordinal()] = 156;
            } catch (NoSuchFieldError nosuchfielderror263) {
                ;
            }

            try {
                aint1[Material.QUARTZ_ORE.ordinal()] = 154;
            } catch (NoSuchFieldError nosuchfielderror264) {
                ;
            }

            try {
                aint1[Material.QUARTZ_STAIRS.ordinal()] = 157;
            } catch (NoSuchFieldError nosuchfielderror265) {
                ;
            }

            try {
                aint1[Material.RABBIT.ordinal()] = 354;
            } catch (NoSuchFieldError nosuchfielderror266) {
                ;
            }

            try {
                aint1[Material.RABBIT_FOOT.ordinal()] = 357;
            } catch (NoSuchFieldError nosuchfielderror267) {
                ;
            }

            try {
                aint1[Material.RABBIT_HIDE.ordinal()] = 358;
            } catch (NoSuchFieldError nosuchfielderror268) {
                ;
            }

            try {
                aint1[Material.RABBIT_STEW.ordinal()] = 356;
            } catch (NoSuchFieldError nosuchfielderror269) {
                ;
            }

            try {
                aint1[Material.RAILS.ordinal()] = 67;
            } catch (NoSuchFieldError nosuchfielderror270) {
                ;
            }

            try {
                aint1[Material.RAW_BEEF.ordinal()] = 306;
            } catch (NoSuchFieldError nosuchfielderror271) {
                ;
            }

            try {
                aint1[Material.RAW_CHICKEN.ordinal()] = 308;
            } catch (NoSuchFieldError nosuchfielderror272) {
                ;
            }

            try {
                aint1[Material.RAW_FISH.ordinal()] = 292;
            } catch (NoSuchFieldError nosuchfielderror273) {
                ;
            }

            try {
                aint1[Material.RECORD_10.ordinal()] = 383;
            } catch (NoSuchFieldError nosuchfielderror274) {
                ;
            }

            try {
                aint1[Material.RECORD_11.ordinal()] = 384;
            } catch (NoSuchFieldError nosuchfielderror275) {
                ;
            }

            try {
                aint1[Material.RECORD_12.ordinal()] = 385;
            } catch (NoSuchFieldError nosuchfielderror276) {
                ;
            }

            try {
                aint1[Material.RECORD_3.ordinal()] = 376;
            } catch (NoSuchFieldError nosuchfielderror277) {
                ;
            }

            try {
                aint1[Material.RECORD_4.ordinal()] = 377;
            } catch (NoSuchFieldError nosuchfielderror278) {
                ;
            }

            try {
                aint1[Material.RECORD_5.ordinal()] = 378;
            } catch (NoSuchFieldError nosuchfielderror279) {
                ;
            }

            try {
                aint1[Material.RECORD_6.ordinal()] = 379;
            } catch (NoSuchFieldError nosuchfielderror280) {
                ;
            }

            try {
                aint1[Material.RECORD_7.ordinal()] = 380;
            } catch (NoSuchFieldError nosuchfielderror281) {
                ;
            }

            try {
                aint1[Material.RECORD_8.ordinal()] = 381;
            } catch (NoSuchFieldError nosuchfielderror282) {
                ;
            }

            try {
                aint1[Material.RECORD_9.ordinal()] = 382;
            } catch (NoSuchFieldError nosuchfielderror283) {
                ;
            }

            try {
                aint1[Material.REDSTONE.ordinal()] = 274;
            } catch (NoSuchFieldError nosuchfielderror284) {
                ;
            }

            try {
                aint1[Material.REDSTONE_BLOCK.ordinal()] = 153;
            } catch (NoSuchFieldError nosuchfielderror285) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR.ordinal()] = 347;
            } catch (NoSuchFieldError nosuchfielderror286) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR_OFF.ordinal()] = 150;
            } catch (NoSuchFieldError nosuchfielderror287) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR_ON.ordinal()] = 151;
            } catch (NoSuchFieldError nosuchfielderror288) {
                ;
            }

            try {
                aint1[Material.REDSTONE_LAMP_OFF.ordinal()] = 124;
            } catch (NoSuchFieldError nosuchfielderror289) {
                ;
            }

            try {
                aint1[Material.REDSTONE_LAMP_ON.ordinal()] = 125;
            } catch (NoSuchFieldError nosuchfielderror290) {
                ;
            }

            try {
                aint1[Material.REDSTONE_ORE.ordinal()] = 74;
            } catch (NoSuchFieldError nosuchfielderror291) {
                ;
            }

            try {
                aint1[Material.REDSTONE_TORCH_OFF.ordinal()] = 76;
            } catch (NoSuchFieldError nosuchfielderror292) {
                ;
            }

            try {
                aint1[Material.REDSTONE_TORCH_ON.ordinal()] = 77;
            } catch (NoSuchFieldError nosuchfielderror293) {
                ;
            }

            try {
                aint1[Material.REDSTONE_WIRE.ordinal()] = 56;
            } catch (NoSuchFieldError nosuchfielderror294) {
                ;
            }

            try {
                aint1[Material.RED_MUSHROOM.ordinal()] = 41;
            } catch (NoSuchFieldError nosuchfielderror295) {
                ;
            }

            try {
                aint1[Material.RED_ROSE.ordinal()] = 39;
            } catch (NoSuchFieldError nosuchfielderror296) {
                ;
            }

            try {
                aint1[Material.RED_SANDSTONE.ordinal()] = 180;
            } catch (NoSuchFieldError nosuchfielderror297) {
                ;
            }

            try {
                aint1[Material.RED_SANDSTONE_STAIRS.ordinal()] = 181;
            } catch (NoSuchFieldError nosuchfielderror298) {
                ;
            }

            try {
                aint1[Material.ROTTEN_FLESH.ordinal()] = 310;
            } catch (NoSuchFieldError nosuchfielderror299) {
                ;
            }

            try {
                aint1[Material.SADDLE.ordinal()] = 272;
            } catch (NoSuchFieldError nosuchfielderror300) {
                ;
            }

            try {
                aint1[Material.SAND.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror301) {
                ;
            }

            try {
                aint1[Material.SANDSTONE.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror302) {
                ;
            }

            try {
                aint1[Material.SANDSTONE_STAIRS.ordinal()] = 129;
            } catch (NoSuchFieldError nosuchfielderror303) {
                ;
            }

            try {
                aint1[Material.SAPLING.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror304) {
                ;
            }

            try {
                aint1[Material.SEA_LANTERN.ordinal()] = 170;
            } catch (NoSuchFieldError nosuchfielderror305) {
                ;
            }

            try {
                aint1[Material.SEEDS.ordinal()] = 238;
            } catch (NoSuchFieldError nosuchfielderror306) {
                ;
            }

            try {
                aint1[Material.SHEARS.ordinal()] = 302;
            } catch (NoSuchFieldError nosuchfielderror307) {
                ;
            }

            try {
                aint1[Material.SIGN.ordinal()] = 266;
            } catch (NoSuchFieldError nosuchfielderror308) {
                ;
            }

            try {
                aint1[Material.SIGN_POST.ordinal()] = 64;
            } catch (NoSuchFieldError nosuchfielderror309) {
                ;
            }

            try {
                aint1[Material.SKULL.ordinal()] = 145;
            } catch (NoSuchFieldError nosuchfielderror310) {
                ;
            }

            try {
                aint1[Material.SKULL_ITEM.ordinal()] = 340;
            } catch (NoSuchFieldError nosuchfielderror311) {
                ;
            }

            try {
                aint1[Material.SLIME_BALL.ordinal()] = 284;
            } catch (NoSuchFieldError nosuchfielderror312) {
                ;
            }

            try {
                aint1[Material.SLIME_BLOCK.ordinal()] = 166;
            } catch (NoSuchFieldError nosuchfielderror313) {
                ;
            }

            try {
                aint1[Material.SMOOTH_BRICK.ordinal()] = 99;
            } catch (NoSuchFieldError nosuchfielderror314) {
                ;
            }

            try {
                aint1[Material.SMOOTH_STAIRS.ordinal()] = 110;
            } catch (NoSuchFieldError nosuchfielderror315) {
                ;
            }

            try {
                aint1[Material.SNOW.ordinal()] = 79;
            } catch (NoSuchFieldError nosuchfielderror316) {
                ;
            }

            try {
                aint1[Material.SNOW_BALL.ordinal()] = 275;
            } catch (NoSuchFieldError nosuchfielderror317) {
                ;
            }

            try {
                aint1[Material.SNOW_BLOCK.ordinal()] = 81;
            } catch (NoSuchFieldError nosuchfielderror318) {
                ;
            }

            try {
                aint1[Material.SOIL.ordinal()] = 61;
            } catch (NoSuchFieldError nosuchfielderror319) {
                ;
            }

            try {
                aint1[Material.SOUL_SAND.ordinal()] = 89;
            } catch (NoSuchFieldError nosuchfielderror320) {
                ;
            }

            try {
                aint1[Material.SPECKLED_MELON.ordinal()] = 325;
            } catch (NoSuchFieldError nosuchfielderror321) {
                ;
            }

            try {
                aint1[Material.SPIDER_EYE.ordinal()] = 318;
            } catch (NoSuchFieldError nosuchfielderror322) {
                ;
            }

            try {
                aint1[Material.SPONGE.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror323) {
                ;
            }

            try {
                aint1[Material.SPRUCE_DOOR.ordinal()] = 194;
            } catch (NoSuchFieldError nosuchfielderror324) {
                ;
            }

            try {
                aint1[Material.SPRUCE_DOOR_ITEM.ordinal()] = 369;
            } catch (NoSuchFieldError nosuchfielderror325) {
                ;
            }

            try {
                aint1[Material.SPRUCE_FENCE.ordinal()] = 189;
            } catch (NoSuchFieldError nosuchfielderror326) {
                ;
            }

            try {
                aint1[Material.SPRUCE_FENCE_GATE.ordinal()] = 184;
            } catch (NoSuchFieldError nosuchfielderror327) {
                ;
            }

            try {
                aint1[Material.SPRUCE_WOOD_STAIRS.ordinal()] = 135;
            } catch (NoSuchFieldError nosuchfielderror328) {
                ;
            }

            try {
                aint1[Material.STAINED_CLAY.ordinal()] = 160;
            } catch (NoSuchFieldError nosuchfielderror329) {
                ;
            }

            try {
                aint1[Material.STAINED_GLASS.ordinal()] = 96;
            } catch (NoSuchFieldError nosuchfielderror330) {
                ;
            }

            try {
                aint1[Material.STAINED_GLASS_PANE.ordinal()] = 161;
            } catch (NoSuchFieldError nosuchfielderror331) {
                ;
            }

            try {
                aint1[Material.STANDING_BANNER.ordinal()] = 177;
            } catch (NoSuchFieldError nosuchfielderror332) {
                ;
            }

            try {
                aint1[Material.STATIONARY_LAVA.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror333) {
                ;
            }

            try {
                aint1[Material.STATIONARY_WATER.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror334) {
                ;
            }

            try {
                aint1[Material.STEP.ordinal()] = 45;
            } catch (NoSuchFieldError nosuchfielderror335) {
                ;
            }

            try {
                aint1[Material.STICK.ordinal()] = 223;
            } catch (NoSuchFieldError nosuchfielderror336) {
                ;
            }

            try {
                aint1[Material.STONE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror337) {
                ;
            }

            try {
                aint1[Material.STONE_AXE.ordinal()] = 218;
            } catch (NoSuchFieldError nosuchfielderror338) {
                ;
            }

            try {
                aint1[Material.STONE_BUTTON.ordinal()] = 78;
            } catch (NoSuchFieldError nosuchfielderror339) {
                ;
            }

            try {
                aint1[Material.STONE_HOE.ordinal()] = 234;
            } catch (NoSuchFieldError nosuchfielderror340) {
                ;
            }

            try {
                aint1[Material.STONE_PICKAXE.ordinal()] = 217;
            } catch (NoSuchFieldError nosuchfielderror341) {
                ;
            }

            try {
                aint1[Material.STONE_PLATE.ordinal()] = 71;
            } catch (NoSuchFieldError nosuchfielderror342) {
                ;
            }

            try {
                aint1[Material.STONE_SLAB2.ordinal()] = 183;
            } catch (NoSuchFieldError nosuchfielderror343) {
                ;
            }

            try {
                aint1[Material.STONE_SPADE.ordinal()] = 216;
            } catch (NoSuchFieldError nosuchfielderror344) {
                ;
            }

            try {
                aint1[Material.STONE_SWORD.ordinal()] = 215;
            } catch (NoSuchFieldError nosuchfielderror345) {
                ;
            }

            try {
                aint1[Material.STORAGE_MINECART.ordinal()] = 285;
            } catch (NoSuchFieldError nosuchfielderror346) {
                ;
            }

            try {
                aint1[Material.STRING.ordinal()] = 230;
            } catch (NoSuchFieldError nosuchfielderror347) {
                ;
            }

            try {
                aint1[Material.SUGAR.ordinal()] = 296;
            } catch (NoSuchFieldError nosuchfielderror348) {
                ;
            }

            try {
                aint1[Material.SUGAR_CANE.ordinal()] = 281;
            } catch (NoSuchFieldError nosuchfielderror349) {
                ;
            }

            try {
                aint1[Material.SUGAR_CANE_BLOCK.ordinal()] = 84;
            } catch (NoSuchFieldError nosuchfielderror350) {
                ;
            }

            try {
                aint1[Material.SULPHUR.ordinal()] = 232;
            } catch (NoSuchFieldError nosuchfielderror351) {
                ;
            }

            try {
                aint1[Material.THIN_GLASS.ordinal()] = 103;
            } catch (NoSuchFieldError nosuchfielderror352) {
                ;
            }

            try {
                aint1[Material.TNT.ordinal()] = 47;
            } catch (NoSuchFieldError nosuchfielderror353) {
                ;
            }

            try {
                aint1[Material.TORCH.ordinal()] = 51;
            } catch (NoSuchFieldError nosuchfielderror354) {
                ;
            }

            try {
                aint1[Material.TRAPPED_CHEST.ordinal()] = 147;
            } catch (NoSuchFieldError nosuchfielderror355) {
                ;
            }

            try {
                aint1[Material.TRAP_DOOR.ordinal()] = 97;
            } catch (NoSuchFieldError nosuchfielderror356) {
                ;
            }

            try {
                aint1[Material.TRIPWIRE.ordinal()] = 133;
            } catch (NoSuchFieldError nosuchfielderror357) {
                ;
            }

            try {
                aint1[Material.TRIPWIRE_HOOK.ordinal()] = 132;
            } catch (NoSuchFieldError nosuchfielderror358) {
                ;
            }

            try {
                aint1[Material.VINE.ordinal()] = 107;
            } catch (NoSuchFieldError nosuchfielderror359) {
                ;
            }

            try {
                aint1[Material.WALL_BANNER.ordinal()] = 178;
            } catch (NoSuchFieldError nosuchfielderror360) {
                ;
            }

            try {
                aint1[Material.WALL_SIGN.ordinal()] = 69;
            } catch (NoSuchFieldError nosuchfielderror361) {
                ;
            }

            try {
                aint1[Material.WATCH.ordinal()] = 290;
            } catch (NoSuchFieldError nosuchfielderror362) {
                ;
            }

            try {
                aint1[Material.WATER.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror363) {
                ;
            }

            try {
                aint1[Material.WATER_BUCKET.ordinal()] = 269;
            } catch (NoSuchFieldError nosuchfielderror364) {
                ;
            }

            try {
                aint1[Material.WATER_LILY.ordinal()] = 112;
            } catch (NoSuchFieldError nosuchfielderror365) {
                ;
            }

            try {
                aint1[Material.WEB.ordinal()] = 31;
            } catch (NoSuchFieldError nosuchfielderror366) {
                ;
            }

            try {
                aint1[Material.WHEAT.ordinal()] = 239;
            } catch (NoSuchFieldError nosuchfielderror367) {
                ;
            }

            try {
                aint1[Material.WOOD.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror368) {
                ;
            }

            try {
                aint1[Material.WOODEN_DOOR.ordinal()] = 65;
            } catch (NoSuchFieldError nosuchfielderror369) {
                ;
            }

            try {
                aint1[Material.WOOD_AXE.ordinal()] = 214;
            } catch (NoSuchFieldError nosuchfielderror370) {
                ;
            }

            try {
                aint1[Material.WOOD_BUTTON.ordinal()] = 144;
            } catch (NoSuchFieldError nosuchfielderror371) {
                ;
            }

            try {
                aint1[Material.WOOD_DOOR.ordinal()] = 267;
            } catch (NoSuchFieldError nosuchfielderror372) {
                ;
            }

            try {
                aint1[Material.WOOD_DOUBLE_STEP.ordinal()] = 126;
            } catch (NoSuchFieldError nosuchfielderror373) {
                ;
            }

            try {
                aint1[Material.WOOD_HOE.ordinal()] = 233;
            } catch (NoSuchFieldError nosuchfielderror374) {
                ;
            }

            try {
                aint1[Material.WOOD_PICKAXE.ordinal()] = 213;
            } catch (NoSuchFieldError nosuchfielderror375) {
                ;
            }

            try {
                aint1[Material.WOOD_PLATE.ordinal()] = 73;
            } catch (NoSuchFieldError nosuchfielderror376) {
                ;
            }

            try {
                aint1[Material.WOOD_SPADE.ordinal()] = 212;
            } catch (NoSuchFieldError nosuchfielderror377) {
                ;
            }

            try {
                aint1[Material.WOOD_STAIRS.ordinal()] = 54;
            } catch (NoSuchFieldError nosuchfielderror378) {
                ;
            }

            try {
                aint1[Material.WOOD_STEP.ordinal()] = 127;
            } catch (NoSuchFieldError nosuchfielderror379) {
                ;
            }

            try {
                aint1[Material.WOOD_SWORD.ordinal()] = 211;
            } catch (NoSuchFieldError nosuchfielderror380) {
                ;
            }

            try {
                aint1[Material.WOOL.ordinal()] = 36;
            } catch (NoSuchFieldError nosuchfielderror381) {
                ;
            }

            try {
                aint1[Material.WORKBENCH.ordinal()] = 59;
            } catch (NoSuchFieldError nosuchfielderror382) {
                ;
            }

            try {
                aint1[Material.WRITTEN_BOOK.ordinal()] = 330;
            } catch (NoSuchFieldError nosuchfielderror383) {
                ;
            }

            try {
                aint1[Material.YELLOW_FLOWER.ordinal()] = 38;
            } catch (NoSuchFieldError nosuchfielderror384) {
                ;
            }

            Material.$SWITCH_TABLE$org$bukkit$Material = aint1;
            return aint1;
        }
    }
}
