package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Player;

public class PlayerDeathEvent extends EntityDeathEvent {

    private int newExp;
    private String deathMessage;
    private int newLevel;
    private int newTotalExp;
    private boolean keepLevel;
    private boolean keepInventory;

    public PlayerDeathEvent(Player player, List drops, int droppedExp, String deathMessage) {
        this(player, drops, droppedExp, 0, deathMessage);
    }

    public PlayerDeathEvent(Player player, List drops, int droppedExp, int newExp, String deathMessage) {
        this(player, drops, droppedExp, newExp, 0, 0, deathMessage);
    }

    public PlayerDeathEvent(Player player, List drops, int droppedExp, int newExp, int newTotalExp, int newLevel, String deathMessage) {
        super(player, drops, droppedExp);
        this.newExp = 0;
        this.deathMessage = "";
        this.newLevel = 0;
        this.newTotalExp = 0;
        this.keepLevel = false;
        this.keepInventory = false;
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = deathMessage;
    }

    public Player getEntity() {
        return (Player) this.entity;
    }

    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    public String getDeathMessage() {
        return this.deathMessage;
    }

    public int getNewExp() {
        return this.newExp;
    }

    public void setNewExp(int exp) {
        this.newExp = exp;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public void setNewLevel(int level) {
        this.newLevel = level;
    }

    public int getNewTotalExp() {
        return this.newTotalExp;
    }

    public void setNewTotalExp(int totalExp) {
        this.newTotalExp = totalExp;
    }

    public boolean getKeepLevel() {
        return this.keepLevel;
    }

    public void setKeepLevel(boolean keepLevel) {
        this.keepLevel = keepLevel;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public boolean getKeepInventory() {
        return this.keepInventory;
    }
}
