package org.bukkit.entity;

public interface Arrow extends Projectile {

    int getKnockbackStrength();

    void setKnockbackStrength(int i);

    boolean isCritical();

    void setCritical(boolean flag);

    Arrow.Spigot spigot();

    public static class Spigot extends Entity.Spigot {

        public double getDamage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setDamage(double damage) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
