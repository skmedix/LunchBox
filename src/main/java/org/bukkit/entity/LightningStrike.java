package org.bukkit.entity;

public interface LightningStrike extends Weather {

    boolean isEffect();

    LightningStrike.Spigot spigot();

    public static class Spigot extends Entity.Spigot {

        public boolean isSilent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
