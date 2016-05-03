package org.bukkit.scoreboard;

import java.util.Set;
import org.bukkit.OfflinePlayer;

public interface Scoreboard {

    Objective registerNewObjective(String s, String s1) throws IllegalArgumentException;

    Objective getObjective(String s) throws IllegalArgumentException;

    Set getObjectivesByCriteria(String s) throws IllegalArgumentException;

    Set getObjectives();

    Objective getObjective(DisplaySlot displayslot) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    Set getScores(OfflinePlayer offlineplayer) throws IllegalArgumentException;

    Set getScores(String s) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    void resetScores(OfflinePlayer offlineplayer) throws IllegalArgumentException;

    void resetScores(String s) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    Team getPlayerTeam(OfflinePlayer offlineplayer) throws IllegalArgumentException;

    Team getEntryTeam(String s) throws IllegalArgumentException;

    Team getTeam(String s) throws IllegalArgumentException;

    Set getTeams();

    Team registerNewTeam(String s) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    Set getPlayers();

    Set getEntries();

    void clearSlot(DisplaySlot displayslot) throws IllegalArgumentException;
}
