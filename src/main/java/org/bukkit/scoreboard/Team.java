package org.bukkit.scoreboard;

import java.util.Set;
import org.bukkit.OfflinePlayer;

public interface Team {

    String getName() throws IllegalStateException;

    String getDisplayName() throws IllegalStateException;

    void setDisplayName(String s) throws IllegalStateException, IllegalArgumentException;

    String getPrefix() throws IllegalStateException;

    void setPrefix(String s) throws IllegalStateException, IllegalArgumentException;

    String getSuffix() throws IllegalStateException;

    void setSuffix(String s) throws IllegalStateException, IllegalArgumentException;

    boolean allowFriendlyFire() throws IllegalStateException;

    void setAllowFriendlyFire(boolean flag) throws IllegalStateException;

    boolean canSeeFriendlyInvisibles() throws IllegalStateException;

    void setCanSeeFriendlyInvisibles(boolean flag) throws IllegalStateException;

    NameTagVisibility getNameTagVisibility() throws IllegalArgumentException;

    void setNameTagVisibility(NameTagVisibility nametagvisibility) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    Set getPlayers() throws IllegalStateException;

    Set getEntries() throws IllegalStateException;

    int getSize() throws IllegalStateException;

    Scoreboard getScoreboard();

    /** @deprecated */
    @Deprecated
    void addPlayer(OfflinePlayer offlineplayer) throws IllegalStateException, IllegalArgumentException;

    void addEntry(String s) throws IllegalStateException, IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    boolean removePlayer(OfflinePlayer offlineplayer) throws IllegalStateException, IllegalArgumentException;

    boolean removeEntry(String s) throws IllegalStateException, IllegalArgumentException;

    void unregister() throws IllegalStateException;

    /** @deprecated */
    @Deprecated
    boolean hasPlayer(OfflinePlayer offlineplayer) throws IllegalArgumentException, IllegalStateException;

    boolean hasEntry(String s) throws IllegalArgumentException, IllegalStateException;
}
