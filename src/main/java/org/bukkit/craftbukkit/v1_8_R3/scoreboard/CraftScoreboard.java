package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Iterator;

import net.minecraft.scoreboard.ScorePlayerTeam;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class CraftScoreboard implements Scoreboard {

    final net.minecraft.scoreboard.Scoreboard board;

    CraftScoreboard(net.minecraft.scoreboard.Scoreboard board) {
        this.board = board;
    }

    public CraftObjective registerNewObjective(String name, String criteria) throws IllegalArgumentException {
        Validate.notNull(name, "Objective name cannot be null");
        Validate.notNull(criteria, "Criteria cannot be null");
        Validate.isTrue(name.length() <= 16, "The name \'" + name + "\' is longer than the limit of 16 characters");
        Validate.isTrue(this.board.getObjective(name) == null, "An objective of name \'" + name + "\' already exists");
        CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
        net.minecraft.scoreboard.ScoreObjective objective = this.board.addScoreObjective(name, craftCriteria.criteria);

        return new CraftObjective(this, objective);
    }

    public Objective getObjective(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Name cannot be null");
        net.minecraft.scoreboard.ScoreObjective nms = this.board.getObjective(name);

        return nms == null ? null : new CraftObjective(this, nms);
    }

    public ImmutableSet getObjectivesByCriteria(String criteria) throws IllegalArgumentException {
        Validate.notNull(criteria, "Criteria cannot be null");
        ImmutableSet.Builder objectives = ImmutableSet.builder();
        Iterator iterator = this.board.getScoreObjectives().iterator();

        while (iterator.hasNext()) {
            net.minecraft.scoreboard.ScoreObjective netObjective = (net.minecraft.scoreboard.ScoreObjective) iterator.next();
            CraftObjective objective = new CraftObjective(this, netObjective);

            if (objective.getCriteria().equals(criteria)) {
                objectives.add((Object) objective);
            }
        }

        return objectives.build();
    }

    public ImmutableSet getObjectives() {
        return ImmutableSet.copyOf((Collection<? extends Objective>) this.board.getScoreObjectives());
    }

    public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Display slot cannot be null");
        net.minecraft.scoreboard.ScoreObjective objective = this.board.getObjectiveInDisplaySlot(CraftScoreboardTranslations.fromBukkitSlot(slot));

        return objective == null ? null : new CraftObjective(this, objective);
    }

    public ImmutableSet getScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        return this.getScores(player.getName());
    }

    public ImmutableSet getScores(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");
        ImmutableSet.Builder scores = ImmutableSet.builder();
        Iterator iterator = this.board.getScoreObjectives().iterator();

        while (iterator.hasNext()) {
            net.minecraft.scoreboard.ScoreObjective objective = (net.minecraft.scoreboard.ScoreObjective) iterator.next();

            scores.add((Object) (new CraftScore(new CraftObjective(this, objective), entry)));
        }

        return scores.build();
    }

    public void resetScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        this.resetScores(player.getName());
    }

    public void resetScores(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");
        Iterator iterator = this.board.getScoreObjectives().iterator();

        while (iterator.hasNext()) {
            net.minecraft.scoreboard.ScoreObjective objective = (net.minecraft.scoreboard.ScoreObjective) iterator.next();

            this.board.func_178820_a(entry, objective);
        }

    }

    public Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        ScorePlayerTeam team = this.board.getTeam(player.getName());

        return team == null ? null : new CraftTeam(this, team);
    }

    public Team getEntryTeam(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");
        ScorePlayerTeam team = this.board.getTeam(entry);

        return team == null ? null : new CraftTeam(this, team);
    }

    public Team getTeam(String teamName) throws IllegalArgumentException {
        Validate.notNull(teamName, "Team name cannot be null");
        ScorePlayerTeam team = this.board.getTeam(teamName);

        return team == null ? null : new CraftTeam(this, team);
    }

    public ImmutableSet<ScorePlayerTeam> getTeams() {
        return ImmutableSet.copyOf(this.board.getTeams());
    }

    public Team registerNewTeam(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Team name cannot be null");
        Validate.isTrue(name.length() <= 16, "Team name \'" + name + "\' is longer than the limit of 16 characters");
        Validate.isTrue(this.board.getTeam(name) == null, "Team name \'" + name + "\' is already in use");
        return new CraftTeam(this, this.board.createTeam(name));
    }

    public ImmutableSet getPlayers() {
        ImmutableSet.Builder players = ImmutableSet.builder();
        Iterator iterator = this.board.getObjectiveNames().iterator();

        while (iterator.hasNext()) {
            Object playerName = iterator.next();

            players.add((Object) Bukkit.getOfflinePlayer(playerName.toString()));
        }

        return players.build();
    }

    public ImmutableSet getEntries() {
        ImmutableSet.Builder entries = ImmutableSet.builder();
        Iterator iterator = this.board.getObjectiveNames().iterator();

        while (iterator.hasNext()) {
            Object entry = iterator.next();

            entries.add((Object) entry.toString());
        }

        return entries.build();
    }

    public void clearSlot(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Slot cannot be null");
        this.board.setObjectiveInDisplaySlot(CraftScoreboardTranslations.fromBukkitSlot(slot), (net.minecraft.scoreboard.ScoreObjective) null);
    }

    public net.minecraft.scoreboard.Scoreboard getHandle() {
        return this.board;
    }
}
