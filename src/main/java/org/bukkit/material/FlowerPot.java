package org.bukkit.material;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;

public class FlowerPot extends MaterialData {

    public FlowerPot() {
        super(Material.FLOWER_POT);
    }

    /** @deprecated */
    @Deprecated
    public FlowerPot(int type) {
        super(type);
    }

    public FlowerPot(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public FlowerPot(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public FlowerPot(Material type, byte data) {
        super(type, data);
    }

    public MaterialData getContents() {
        switch (this.getData()) {
        case 1:
            return new MaterialData(Material.RED_ROSE);

        case 2:
            return new MaterialData(Material.YELLOW_FLOWER);

        case 3:
            return new Tree(TreeSpecies.GENERIC);

        case 4:
            return new Tree(TreeSpecies.REDWOOD);

        case 5:
            return new Tree(TreeSpecies.BIRCH);

        case 6:
            return new Tree(TreeSpecies.JUNGLE);

        case 7:
            return new MaterialData(Material.RED_MUSHROOM);

        case 8:
            return new MaterialData(Material.BROWN_MUSHROOM);

        case 9:
            return new MaterialData(Material.CACTUS);

        case 10:
            return new MaterialData(Material.DEAD_BUSH);

        case 11:
            return new LongGrass(GrassSpecies.FERN_LIKE);

        default:
            return null;
        }
    }

    public void setContents(MaterialData materialData) {
        Material mat = materialData.getItemType();

        if (mat == Material.RED_ROSE) {
            this.setData((byte) 1);
        } else if (mat == Material.YELLOW_FLOWER) {
            this.setData((byte) 2);
        } else if (mat == Material.RED_MUSHROOM) {
            this.setData((byte) 7);
        } else if (mat == Material.BROWN_MUSHROOM) {
            this.setData((byte) 8);
        } else if (mat == Material.CACTUS) {
            this.setData((byte) 9);
        } else if (mat == Material.DEAD_BUSH) {
            this.setData((byte) 10);
        } else if (mat == Material.SAPLING) {
            TreeSpecies species = ((Tree) materialData).getSpecies();

            if (species == TreeSpecies.GENERIC) {
                this.setData((byte) 3);
            } else if (species == TreeSpecies.REDWOOD) {
                this.setData((byte) 4);
            } else if (species == TreeSpecies.BIRCH) {
                this.setData((byte) 5);
            } else {
                this.setData((byte) 6);
            }
        } else if (mat == Material.LONG_GRASS) {
            GrassSpecies species1 = ((LongGrass) materialData).getSpecies();

            if (species1 == GrassSpecies.FERN_LIKE) {
                this.setData((byte) 11);
            }
        }

    }

    public String toString() {
        return super.toString() + " containing " + this.getContents();
    }

    public FlowerPot clone() {
        return (FlowerPot) super.clone();
    }
}
