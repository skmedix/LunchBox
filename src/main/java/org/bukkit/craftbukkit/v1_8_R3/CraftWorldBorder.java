package org.bukkit.craftbukkit.v1_8_R3;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class CraftWorldBorder implements WorldBorder {

    private final World world;
    private final net.minecraft.world.border.WorldBorder handle;

    public CraftWorldBorder(CraftWorld world) {
        this.world = world;
        this.handle = world.getHandle().getWorldBorder();
    }

    public void reset() {
        this.setSize(6.0E7D);
        this.setDamageAmount(0.2D);
        this.setDamageBuffer(5.0D);
        this.setWarningDistance(5);
        this.setWarningTime(15);
        this.setCenter(0.0D, 0.0D);
    }

    public double getSize() {
        return this.handle.getSize();
    }

    public void setSize(double newSize) {
        this.setSize(newSize, 0L);
    }

    public void setSize(double newSize, long time) {
        newSize = Math.min(6.0E7D, Math.max(1.0D, newSize));
        time = Math.min(9223372036854775L, Math.max(0L, time));
        if (time > 0L) {
            this.handle.setTransition(this.handle.getSize(), newSize, time * 1000L);
        } else {
            this.handle.setTransition(newSize);
        }

    }

    public Location getCenter() {
        double x = this.handle.getCenterX();
        double z = this.handle.getCenterZ();

        return new Location(this.world, x, 0.0D, z);
    }

    public void setCenter(double x, double z) {
        x = Math.min(3.0E7D, Math.max(-3.0E7D, x));
        z = Math.min(3.0E7D, Math.max(-3.0E7D, z));
        this.handle.setCenter(x, z);
    }

    public void setCenter(Location location) {
        this.setCenter(location.getX(), location.getZ());
    }

    public double getDamageBuffer() {
        return this.handle.getDamageBuffer();
    }

    public void setDamageBuffer(double blocks) {
        this.handle.setDamageBuffer(blocks);
    }

    public double getDamageAmount() {
        return this.handle.getDamageAmount();
    }

    public void setDamageAmount(double damage) {
        this.handle.setDamageAmount(damage);
    }

    public int getWarningTime() {
        return this.handle.getWarningTime();
    }

    public void setWarningTime(int time) {
        this.handle.setWarningTime(time);
    }

    public int getWarningDistance() {
        return this.handle.getWarningDistance();
    }

    public void setWarningDistance(int distance) {
        this.handle.setWarningDistance(distance);
    }
}
