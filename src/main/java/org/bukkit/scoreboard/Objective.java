package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

public interface Objective {

    String getName() throws IllegalStateException;

    String getDisplayName() throws IllegalStateException;

    void setDisplayName(String s) throws IllegalStateException, IllegalArgumentException;

    String getCriteria() throws IllegalStateException;

    boolean isModifiable() throws IllegalStateException;

    Scoreboard getScoreboard();

    void unregister() throws IllegalStateException;

    void setDisplaySlot(DisplaySlot displayslot) throws IllegalStateException;

    DisplaySlot getDisplaySlot() throws IllegalStateException;

    /** @deprecated */
    @Deprecated
    Score getScore(OfflinePlayer offlineplayer) throws IllegalArgumentException, IllegalStateException;

    Score getScore(String s) throws IllegalArgumentException, IllegalStateException;
}
