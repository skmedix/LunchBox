package org.bukkit.craftbukkit.v1_8_R3;

import net.minecraft.server.v1_8_R3.EntityPainting;
import org.bukkit.Art;

public class CraftArt {

    private static int[] $SWITCH_TABLE$net$minecraft$server$EntityPainting$EnumArt;
    private static int[] $SWITCH_TABLE$org$bukkit$Art;

    public static Art NotchToBukkit(EntityPainting.EnumArt art) {
        switch ($SWITCH_TABLE$net$minecraft$server$EntityPainting$EnumArt()[art.ordinal()]) {
        case 1:
            return Art.KEBAB;

        case 2:
            return Art.AZTEC;

        case 3:
            return Art.ALBAN;

        case 4:
            return Art.AZTEC2;

        case 5:
            return Art.BOMB;

        case 6:
            return Art.PLANT;

        case 7:
            return Art.WASTELAND;

        case 8:
            return Art.POOL;

        case 9:
            return Art.COURBET;

        case 10:
            return Art.SEA;

        case 11:
            return Art.SUNSET;

        case 12:
            return Art.CREEBET;

        case 13:
            return Art.WANDERER;

        case 14:
            return Art.GRAHAM;

        case 15:
            return Art.MATCH;

        case 16:
            return Art.BUST;

        case 17:
            return Art.STAGE;

        case 18:
            return Art.VOID;

        case 19:
            return Art.SKULL_AND_ROSES;

        case 20:
            return Art.WITHER;

        case 21:
            return Art.FIGHTERS;

        case 22:
            return Art.POINTER;

        case 23:
            return Art.PIGSCENE;

        case 24:
            return Art.BURNINGSKULL;

        case 25:
            return Art.SKELETON;

        case 26:
            return Art.DONKEYKONG;

        default:
            throw new AssertionError(art);
        }
    }

    public static EntityPainting.EnumArt BukkitToNotch(Art art) {
        switch ($SWITCH_TABLE$org$bukkit$Art()[art.ordinal()]) {
        case 1:
            return EntityPainting.EnumArt.KEBAB;

        case 2:
            return EntityPainting.EnumArt.AZTEC;

        case 3:
            return EntityPainting.EnumArt.ALBAN;

        case 4:
            return EntityPainting.EnumArt.AZTEC_2;

        case 5:
            return EntityPainting.EnumArt.BOMB;

        case 6:
            return EntityPainting.EnumArt.PLANT;

        case 7:
            return EntityPainting.EnumArt.WASTELAND;

        case 8:
            return EntityPainting.EnumArt.POOL;

        case 9:
            return EntityPainting.EnumArt.COURBET;

        case 10:
            return EntityPainting.EnumArt.SEA;

        case 11:
            return EntityPainting.EnumArt.SUNSET;

        case 12:
            return EntityPainting.EnumArt.CREEBET;

        case 13:
            return EntityPainting.EnumArt.WANDERER;

        case 14:
            return EntityPainting.EnumArt.GRAHAM;

        case 15:
            return EntityPainting.EnumArt.MATCH;

        case 16:
            return EntityPainting.EnumArt.BUST;

        case 17:
            return EntityPainting.EnumArt.STAGE;

        case 18:
            return EntityPainting.EnumArt.VOID;

        case 19:
            return EntityPainting.EnumArt.SKULL_AND_ROSES;

        case 20:
            return EntityPainting.EnumArt.WITHER;

        case 21:
            return EntityPainting.EnumArt.FIGHTERS;

        case 22:
            return EntityPainting.EnumArt.POINTER;

        case 23:
            return EntityPainting.EnumArt.PIGSCENE;

        case 24:
            return EntityPainting.EnumArt.BURNING_SKULL;

        case 25:
            return EntityPainting.EnumArt.SKELETON;

        case 26:
            return EntityPainting.EnumArt.DONKEY_KONG;

        default:
            throw new AssertionError(art);
        }
    }

    static int[] $SWITCH_TABLE$net$minecraft$server$EntityPainting$EnumArt() {
        int[] aint = CraftArt.$SWITCH_TABLE$net$minecraft$server$EntityPainting$EnumArt;

        if (CraftArt.$SWITCH_TABLE$net$minecraft$server$EntityPainting$EnumArt != null) {
            return aint;
        } else {
            int[] aint1 = new int[EntityPainting.EnumArt.values().length];

            try {
                aint1[EntityPainting.EnumArt.ALBAN.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.AZTEC.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.AZTEC_2.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.BOMB.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.BURNING_SKULL.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.BUST.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.COURBET.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.CREEBET.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.DONKEY_KONG.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.FIGHTERS.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.GRAHAM.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.KEBAB.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.MATCH.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.PIGSCENE.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.PLANT.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.POINTER.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.POOL.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.SEA.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.SKELETON.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.SKULL_AND_ROSES.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.STAGE.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.SUNSET.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.VOID.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.WANDERER.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.WASTELAND.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[EntityPainting.EnumArt.WITHER.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            CraftArt.$SWITCH_TABLE$net$minecraft$server$EntityPainting$EnumArt = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$Art() {
        int[] aint = CraftArt.$SWITCH_TABLE$org$bukkit$Art;

        if (CraftArt.$SWITCH_TABLE$org$bukkit$Art != null) {
            return aint;
        } else {
            int[] aint1 = new int[Art.values().length];

            try {
                aint1[Art.ALBAN.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[Art.AZTEC.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[Art.AZTEC2.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[Art.BOMB.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[Art.BURNINGSKULL.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[Art.BUST.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[Art.COURBET.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[Art.CREEBET.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[Art.DONKEYKONG.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[Art.FIGHTERS.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[Art.GRAHAM.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[Art.KEBAB.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[Art.MATCH.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[Art.PIGSCENE.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[Art.PLANT.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[Art.POINTER.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[Art.POOL.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[Art.SEA.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[Art.SKELETON.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[Art.SKULL_AND_ROSES.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[Art.STAGE.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[Art.SUNSET.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[Art.VOID.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[Art.WANDERER.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[Art.WASTELAND.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[Art.WITHER.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            CraftArt.$SWITCH_TABLE$org$bukkit$Art = aint1;
            return aint1;
        }
    }
}
