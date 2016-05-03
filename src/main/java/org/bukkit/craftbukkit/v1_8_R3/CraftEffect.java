package org.bukkit.craftbukkit.v1_8_R3;

import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.potion.Potion;

public class CraftEffect {

    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;
    private static int[] $SWITCH_TABLE$org$bukkit$Effect;

    public static int getDataValue(Effect effect, Object data) {
        int datavalue;

        switch ($SWITCH_TABLE$org$bukkit$Effect()[effect.ordinal()]) {
        case 6:
            Validate.isTrue(((Material) data).isRecord(), "Invalid record type!");
            datavalue = ((Material) data).getId();
            break;

        case 13:
            switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[((BlockFace) data).ordinal()]) {
            case 1:
                datavalue = 7;
                return datavalue;

            case 2:
                datavalue = 3;
                return datavalue;

            case 3:
                datavalue = 1;
                return datavalue;

            case 4:
                datavalue = 5;
                return datavalue;

            case 5:
            case 19:
                datavalue = 4;
                return datavalue;

            case 6:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            default:
                throw new IllegalArgumentException("Bad smoke direction!");

            case 7:
                datavalue = 6;
                return datavalue;

            case 8:
                datavalue = 8;
                return datavalue;

            case 9:
                datavalue = 0;
                return datavalue;

            case 10:
                datavalue = 2;
                return datavalue;
            }

        case 14:
            Validate.isTrue(((Material) data).isBlock(), "Material is not a block!");
            datavalue = ((Material) data).getId();
            break;

        case 15:
            datavalue = ((Potion) data).toDamageValue() & 63;
            break;

        case 50:
            datavalue = ((Material) data).getId();
            break;

        default:
            datavalue = 0;
        }

        return datavalue;
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = CraftEffect.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (CraftEffect.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
            return aint;
        } else {
            int[] aint1 = new int[BlockFace.values().length];

            try {
                aint1[BlockFace.DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[BlockFace.EAST.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[BlockFace.EAST_NORTH_EAST.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[BlockFace.EAST_SOUTH_EAST.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[BlockFace.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_EAST.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_NORTH_EAST.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_NORTH_WEST.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_WEST.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[BlockFace.SELF.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_EAST.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_SOUTH_EAST.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_SOUTH_WEST.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_WEST.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[BlockFace.UP.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[BlockFace.WEST.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[BlockFace.WEST_NORTH_WEST.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[BlockFace.WEST_SOUTH_WEST.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            CraftEffect.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$Effect() {
        int[] aint = CraftEffect.$SWITCH_TABLE$org$bukkit$Effect;

        if (CraftEffect.$SWITCH_TABLE$org$bukkit$Effect != null) {
            return aint;
        } else {
            int[] aint1 = new int[Effect.values().length];

            try {
                aint1[Effect.BLAZE_SHOOT.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[Effect.BOW_FIRE.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[Effect.CLICK1.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[Effect.CLICK2.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[Effect.CLOUD.ordinal()] = 39;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[Effect.COLOURED_DUST.ordinal()] = 40;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[Effect.CRIT.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[Effect.DOOR_TOGGLE.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[Effect.ENDER_SIGNAL.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[Effect.EXPLOSION.ordinal()] = 36;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[Effect.EXPLOSION_HUGE.ordinal()] = 34;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[Effect.EXPLOSION_LARGE.ordinal()] = 35;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[Effect.EXTINGUISH.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[Effect.FIREWORKS_SPARK.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[Effect.FLAME.ordinal()] = 29;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[Effect.FLYING_GLYPH.ordinal()] = 28;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[Effect.FOOTSTEP.ordinal()] = 31;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[Effect.GHAST_SHOOT.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[Effect.GHAST_SHRIEK.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[Effect.HAPPY_VILLAGER.ordinal()] = 48;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[Effect.HEART.ordinal()] = 46;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[Effect.INSTANT_SPELL.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[Effect.ITEM_BREAK.ordinal()] = 50;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[Effect.LARGE_SMOKE.ordinal()] = 49;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[Effect.LAVADRIP.ordinal()] = 43;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[Effect.LAVA_POP.ordinal()] = 30;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            try {
                aint1[Effect.MAGIC_CRIT.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror26) {
                ;
            }

            try {
                aint1[Effect.MOBSPAWNER_FLAMES.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror27) {
                ;
            }

            try {
                aint1[Effect.NOTE.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror28) {
                ;
            }

            try {
                aint1[Effect.PARTICLE_SMOKE.ordinal()] = 33;
            } catch (NoSuchFieldError nosuchfielderror29) {
                ;
            }

            try {
                aint1[Effect.PORTAL.ordinal()] = 27;
            } catch (NoSuchFieldError nosuchfielderror30) {
                ;
            }

            try {
                aint1[Effect.POTION_BREAK.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror31) {
                ;
            }

            try {
                aint1[Effect.POTION_SWIRL.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror32) {
                ;
            }

            try {
                aint1[Effect.POTION_SWIRL_TRANSPARENT.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror33) {
                ;
            }

            try {
                aint1[Effect.RECORD_PLAY.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror34) {
                ;
            }

            try {
                aint1[Effect.SLIME.ordinal()] = 45;
            } catch (NoSuchFieldError nosuchfielderror35) {
                ;
            }

            try {
                aint1[Effect.SMALL_SMOKE.ordinal()] = 38;
            } catch (NoSuchFieldError nosuchfielderror36) {
                ;
            }

            try {
                aint1[Effect.SMOKE.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror37) {
                ;
            }

            try {
                aint1[Effect.SNOWBALL_BREAK.ordinal()] = 41;
            } catch (NoSuchFieldError nosuchfielderror38) {
                ;
            }

            try {
                aint1[Effect.SNOW_SHOVEL.ordinal()] = 44;
            } catch (NoSuchFieldError nosuchfielderror39) {
                ;
            }

            try {
                aint1[Effect.SPELL.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror40) {
                ;
            }

            try {
                aint1[Effect.SPLASH.ordinal()] = 32;
            } catch (NoSuchFieldError nosuchfielderror41) {
                ;
            }

            try {
                aint1[Effect.STEP_SOUND.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror42) {
                ;
            }

            try {
                aint1[Effect.TILE_BREAK.ordinal()] = 51;
            } catch (NoSuchFieldError nosuchfielderror43) {
                ;
            }

            try {
                aint1[Effect.TILE_DUST.ordinal()] = 52;
            } catch (NoSuchFieldError nosuchfielderror44) {
                ;
            }

            try {
                aint1[Effect.VILLAGER_THUNDERCLOUD.ordinal()] = 47;
            } catch (NoSuchFieldError nosuchfielderror45) {
                ;
            }

            try {
                aint1[Effect.VOID_FOG.ordinal()] = 37;
            } catch (NoSuchFieldError nosuchfielderror46) {
                ;
            }

            try {
                aint1[Effect.WATERDRIP.ordinal()] = 42;
            } catch (NoSuchFieldError nosuchfielderror47) {
                ;
            }

            try {
                aint1[Effect.WITCH_MAGIC.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror48) {
                ;
            }

            try {
                aint1[Effect.ZOMBIE_CHEW_IRON_DOOR.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror49) {
                ;
            }

            try {
                aint1[Effect.ZOMBIE_CHEW_WOODEN_DOOR.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror50) {
                ;
            }

            try {
                aint1[Effect.ZOMBIE_DESTROY_DOOR.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror51) {
                ;
            }

            CraftEffect.$SWITCH_TABLE$org$bukkit$Effect = aint1;
            return aint1;
        }
    }
}
