package org.bukkit.entity;

import java.util.List;
import java.util.UUID;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.Metadatable;
import org.bukkit.util.Vector;

public interface Entity extends Metadatable, CommandSender {

    Location getLocation();

    Location getLocation(Location location);

    void setVelocity(Vector vector);

    Vector getVelocity();

    boolean isOnGround();

    World getWorld();

    boolean teleport(Location location);

    boolean teleport(Location location, PlayerTeleportEvent.TeleportCause playerteleportevent_teleportcause);

    boolean teleport(Entity entity);

    boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause playerteleportevent_teleportcause);

    List getNearbyEntities(double d0, double d1, double d2);

    int getEntityId();

    int getFireTicks();

    int getMaxFireTicks();

    void setFireTicks(int i);

    void remove();

    boolean isDead();

    boolean isValid();

    Server getServer();

    Entity getPassenger();

    boolean setPassenger(Entity entity);

    boolean isEmpty();

    boolean eject();

    float getFallDistance();

    void setFallDistance(float f);

    void setLastDamageCause(EntityDamageEvent entitydamageevent);

    EntityDamageEvent getLastDamageCause();

    UUID getUniqueId();

    int getTicksLived();

    void setTicksLived(int i);

    void playEffect(EntityEffect entityeffect);

    EntityType getType();

    boolean isInsideVehicle();

    boolean leaveVehicle();

    Entity getVehicle();

    void setCustomName(String s);

    String getCustomName();

    void setCustomNameVisible(boolean flag);

    boolean isCustomNameVisible();

    Entity.Spigot spigot();

    public static class Spigot {

        public boolean isInvulnerable() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
