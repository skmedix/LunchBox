package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import java.util.Map;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

final class CraftScore implements Score {

    private final String entry;
    private final CraftObjective objective;

    CraftScore(CraftObjective objective, String entry) {
        this.objective = objective;
        this.entry = entry;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.entry);
    }

    public String getEntry() {
        return this.entry;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public int getScore() throws IllegalStateException {
        Scoreboard board = this.objective.checkState().board;

        if (board.getPlayers().contains(this.entry)) {
            Map scores = board.getPlayerObjectives(this.entry);
            ScoreboardScore score = (ScoreboardScore) scores.get(this.objective.getHandle());

            if (score != null) {
                return score.getScore();
            }
        }

        return 0;
    }

    public void setScore(int score) throws IllegalStateException {
        this.objective.checkState().board.getPlayerScoreForObjective(this.entry, this.objective.getHandle()).setScore(score);
    }

    public CraftScoreboard getScoreboard() {
        return this.objective.getScoreboard();
    }

    public boolean isScoreSet() throws IllegalStateException {
        Scoreboard board = this.objective.checkState().board;

        return board.getPlayers().contains(this.entry) && board.getPlayerObjectives(this.entry).containsKey(this.objective.getHandle());
    }
}
