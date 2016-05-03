package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

public interface Score {

    /** @deprecated */
    @Deprecated
    OfflinePlayer getPlayer();

    String getEntry();

    Objective getObjective();

    int getScore() throws IllegalStateException;

    void setScore(int i) throws IllegalStateException;

    boolean isScoreSet() throws IllegalStateException;

    Scoreboard getScoreboard();
}
