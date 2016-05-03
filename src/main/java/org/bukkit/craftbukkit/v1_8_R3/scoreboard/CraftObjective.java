package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

final class CraftObjective extends CraftScoreboardComponent implements Objective {

    private final ScoreboardObjective objective;
    private final CraftCriteria criteria;

    CraftObjective(CraftScoreboard scoreboard, ScoreboardObjective objective) {
        super(scoreboard);
        this.objective = objective;
        this.criteria = CraftCriteria.getFromNMS(objective);
    }

    ScoreboardObjective getHandle() {
        return this.objective;
    }

    public String getName() throws IllegalStateException {
        this.checkState();
        return this.objective.getName();
    }

    public String getDisplayName() throws IllegalStateException {
        this.checkState();
        return this.objective.getDisplayName();
    }

    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(displayName, "Display name cannot be null");
        Validate.isTrue(displayName.length() <= 32, "Display name \'" + displayName + "\' is longer than the limit of 32 characters");
        this.checkState();
        this.objective.setDisplayName(displayName);
    }

    public String getCriteria() throws IllegalStateException {
        this.checkState();
        return this.criteria.bukkitName;
    }

    public boolean isModifiable() throws IllegalStateException {
        this.checkState();
        return !this.criteria.criteria.isReadOnly();
    }

    public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        int slotNumber;

        for (slotNumber = 0; slotNumber < 3; ++slotNumber) {
            if (board.getObjectiveForSlot(slotNumber) == objective) {
                board.setDisplaySlot(slotNumber, (ScoreboardObjective) null);
            }
        }

        if (slot != null) {
            slotNumber = CraftScoreboardTranslations.fromBukkitSlot(slot);
            board.setDisplaySlot(slotNumber, this.getHandle());
        }

    }

    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        for (int i = 0; i < 3; ++i) {
            if (board.getObjectiveForSlot(i) == objective) {
                return CraftScoreboardTranslations.toBukkitSlot(i);
            }
        }

        return null;
    }

    public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(player, "Player cannot be null");
        this.checkState();
        return new CraftScore(this, player.getName());
    }

    public Score getScore(String entry) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(entry, "Entry cannot be null");
        if (entry.length() > 40) {
            throw new IllegalArgumentException("Entry cannot be longer than 40 characters!");
        } else {
            this.checkState();
            return new CraftScore(this, entry);
        }
    }

    public void unregister() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();

        scoreboard.board.unregisterObjective(this.objective);
    }

    CraftScoreboard checkState() throws IllegalStateException {
        if (this.getScoreboard().board.getObjective(this.objective.getName()) == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        } else {
            return this.getScoreboard();
        }
    }

    public int hashCode() {
        byte hash = 7;
        int hash1 = 31 * hash + (this.objective != null ? this.objective.hashCode() : 0);

        return hash1;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CraftObjective other = (CraftObjective) obj;

            return this.objective == other.objective || this.objective != null && this.objective.equals(other.objective);
        }
    }
}
