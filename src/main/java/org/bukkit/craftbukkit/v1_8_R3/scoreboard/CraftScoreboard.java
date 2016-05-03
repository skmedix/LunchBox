package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class CraftScoreboard implements Scoreboard {

    final net.minecraft.server.v1_8_R3.Scoreboard board;

    CraftScoreboard(net.minecraft.server.v1_8_R3.Scoreboard board) {
        this.board = board;
    }

    public CraftObjective registerNewObjective(String name, String criteria) throws IllegalArgumentException {
        Validate.notNull(name, "Objective name cannot be null");
        Validate.notNull(criteria, "Criteria cannot be null");
        Validate.isTrue(name.length() <= 16, "The name \'" + name + "\' is longer than the limit of 16 characters");
        Validate.isTrue(this.board.getObjective(name) == null, "An objective of name \'" + name + "\' already exists");
        CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
        ScoreboardObjective objective = this.board.registerObjective(name, craftCriteria.criteria);

        return new CraftObjective(this, objective);
    }

    public Objective getObjective(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Name cannot be null");
        ScoreboardObjective nms = this.board.getObjective(name);

        return nms == null ? null : new CraftObjective(this, nms);
    }

    public ImmutableSet getObjectivesByCriteria(String criteria) throws IllegalArgumentException {
        Validate.notNull(criteria, "Criteria cannot be null");
        ImmutableSet.Builder objectives = ImmutableSet.builder();
        Iterator iterator = this.board.getObjectives().iterator();

        while (iterator.hasNext()) {
            ScoreboardObjective netObjective = (ScoreboardObjective) iterator.next();
            CraftObjective objective = new CraftObjective(this, netObjective);

            if (objective.getCriteria().equals(criteria)) {
                objectives.add((Object) objective);
            }
        }

        return objectives.build();
    }

    public ImmutableSet getObjectives() {
        return ImmutableSet.copyOf(Iterables.transform(this.board.getObjectives(), new Function() {
            public Objective apply(ScoreboardObjective input) {
                return new CraftObjective(CraftScoreboard.this, input);
            }
        }));
    }

    public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Display slot cannot be null");
        ScoreboardObjective objective = this.board.getObjectiveForSlot(CraftScoreboardTranslations.fromBukkitSlot(slot));

        return objective == null ? null : new CraftObjective(this, objective);
    }

    public ImmutableSet getScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        return this.getScores(player.getName());
    }

    public ImmutableSet getScores(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");
        ImmutableSet.Builder scores = ImmutableSet.builder();
        Iterator iterator = this.board.getObjectives().iterator();

        while (iterator.hasNext()) {
            ScoreboardObjective objective = (ScoreboardObjective) iterator.next();

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
        Iterator iterator = this.board.getObjectives().iterator();

        while (iterator.hasNext()) {
            ScoreboardObjective objective = (ScoreboardObjective) iterator.next();

            this.board.resetPlayerScores(entry, objective);
        }

    }

    public Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        ScoreboardTeam team = this.board.getPlayerTeam(player.getName());

        return team == null ? null : new CraftTeam(this, team);
    }

    public Team getEntryTeam(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");
        ScoreboardTeam team = this.board.getPlayerTeam(entry);

        return team == null ? null : new CraftTeam(this, team);
    }

    public Team getTeam(String teamName) throws IllegalArgumentException {
        Validate.notNull(teamName, "Team name cannot be null");
        ScoreboardTeam team = this.board.getTeam(teamName);

        return team == null ? null : new CraftTeam(this, team);
    }

    public ImmutableSet getTeams() {
        return ImmutableSet.copyOf(Iterables.transform(this.board.getTeams(), new Function() {
            public Team apply(ScoreboardTeam input) {
                return new CraftTeam(CraftScoreboard.this, input);
            }
        }));
    }

    public Team registerNewTeam(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Team name cannot be null");
        Validate.isTrue(name.length() <= 16, "Team name \'" + name + "\' is longer than the limit of 16 characters");
        Validate.isTrue(this.board.getTeam(name) == null, "Team name \'" + name + "\' is already in use");
        return new CraftTeam(this, this.board.createTeam(name));
    }

    public ImmutableSet getPlayers() {
        ImmutableSet.Builder players = ImmutableSet.builder();
        Iterator iterator = this.board.getPlayers().iterator();

        while (iterator.hasNext()) {
            Object playerName = iterator.next();

            players.add((Object) Bukkit.getOfflinePlayer(playerName.toString()));
        }

        return players.build();
    }

    public ImmutableSet getEntries() {
        ImmutableSet.Builder entries = ImmutableSet.builder();
        Iterator iterator = this.board.getPlayers().iterator();

        while (iterator.hasNext()) {
            Object entry = iterator.next();

            entries.add((Object) entry.toString());
        }

        return entries.build();
    }

    public void clearSlot(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Slot cannot be null");
        this.board.setDisplaySlot(CraftScoreboardTranslations.fromBukkitSlot(slot), (ScoreboardObjective) null);
    }

    public net.minecraft.server.v1_8_R3.Scoreboard getHandle() {
        return this.board;
    }
}
