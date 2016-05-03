package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

final class CraftTeam extends CraftScoreboardComponent implements Team {

    private final ScoreboardTeam team;
    private static int[] $SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility;
    private static int[] $SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility;

    CraftTeam(CraftScoreboard scoreboard, ScoreboardTeam team) {
        super(scoreboard);
        this.team = team;
    }

    public String getName() throws IllegalStateException {
        this.checkState();
        return this.team.getName();
    }

    public String getDisplayName() throws IllegalStateException {
        this.checkState();
        return this.team.getDisplayName();
    }

    public void setDisplayName(String displayName) throws IllegalStateException {
        Validate.notNull(displayName, "Display name cannot be null");
        Validate.isTrue(displayName.length() <= 32, "Display name \'" + displayName + "\' is longer than the limit of 32 characters");
        this.checkState();
        this.team.setDisplayName(displayName);
    }

    public String getPrefix() throws IllegalStateException {
        this.checkState();
        return this.team.getPrefix();
    }

    public void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(prefix, "Prefix cannot be null");
        Validate.isTrue(prefix.length() <= 32, "Prefix \'" + prefix + "\' is longer than the limit of 32 characters");
        this.checkState();
        this.team.setPrefix(prefix);
    }

    public String getSuffix() throws IllegalStateException {
        this.checkState();
        return this.team.getSuffix();
    }

    public void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(suffix, "Suffix cannot be null");
        Validate.isTrue(suffix.length() <= 32, "Suffix \'" + suffix + "\' is longer than the limit of 32 characters");
        this.checkState();
        this.team.setSuffix(suffix);
    }

    public boolean allowFriendlyFire() throws IllegalStateException {
        this.checkState();
        return this.team.allowFriendlyFire();
    }

    public void setAllowFriendlyFire(boolean enabled) throws IllegalStateException {
        this.checkState();
        this.team.setAllowFriendlyFire(enabled);
    }

    public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
        this.checkState();
        return this.team.canSeeFriendlyInvisibles();
    }

    public void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException {
        this.checkState();
        this.team.setCanSeeFriendlyInvisibles(enabled);
    }

    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        this.checkState();
        return notchToBukkit(this.team.getNameTagVisibility());
    }

    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
        this.checkState();
        this.team.setNameTagVisibility(bukkitToNotch(visibility));
    }

    public Set getPlayers() throws IllegalStateException {
        this.checkState();
        ImmutableSet.Builder players = ImmutableSet.builder();
        Iterator iterator = this.team.getPlayerNameSet().iterator();

        while (iterator.hasNext()) {
            String playerName = (String) iterator.next();

            players.add((Object) Bukkit.getOfflinePlayer(playerName));
        }

        return players.build();
    }

    public Set getEntries() throws IllegalStateException {
        this.checkState();
        ImmutableSet.Builder entries = ImmutableSet.builder();
        Iterator iterator = this.team.getPlayerNameSet().iterator();

        while (iterator.hasNext()) {
            String playerName = (String) iterator.next();

            entries.add((Object) playerName);
        }

        return entries.build();
    }

    public int getSize() throws IllegalStateException {
        this.checkState();
        return this.team.getPlayerNameSet().size();
    }

    public void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        this.addEntry(player.getName());
    }

    public void addEntry(String entry) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");
        CraftScoreboard scoreboard = this.checkState();

        scoreboard.board.addPlayerToTeam(entry, this.team.getName());
    }

    public boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        return this.removeEntry(player.getName());
    }

    public boolean removeEntry(String entry) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");
        CraftScoreboard scoreboard = this.checkState();

        if (!this.team.getPlayerNameSet().contains(entry)) {
            return false;
        } else {
            scoreboard.board.removePlayerFromTeam(entry, this.team);
            return true;
        }
    }

    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        return this.hasEntry(player.getName());
    }

    public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull("Entry cannot be null");
        this.checkState();
        return this.team.getPlayerNameSet().contains(entry);
    }

    public void unregister() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();

        scoreboard.board.removeTeam(this.team);
    }

    public static ScoreboardTeamBase.EnumNameTagVisibility bukkitToNotch(NameTagVisibility visibility) {
        switch ($SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility()[visibility.ordinal()]) {
        case 1:
            return ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS;

        case 2:
            return ScoreboardTeamBase.EnumNameTagVisibility.NEVER;

        case 3:
            return ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS;

        case 4:
            return ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM;

        default:
            throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    public static NameTagVisibility notchToBukkit(ScoreboardTeamBase.EnumNameTagVisibility visibility) {
        switch ($SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility()[visibility.ordinal()]) {
        case 1:
            return NameTagVisibility.ALWAYS;

        case 2:
            return NameTagVisibility.NEVER;

        case 3:
            return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;

        case 4:
            return NameTagVisibility.HIDE_FOR_OWN_TEAM;

        default:
            throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    CraftScoreboard checkState() throws IllegalStateException {
        if (this.getScoreboard().board.getTeam(this.team.getName()) == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        } else {
            return this.getScoreboard();
        }
    }

    public int hashCode() {
        byte hash = 7;
        int hash1 = 43 * hash + (this.team != null ? this.team.hashCode() : 0);

        return hash1;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CraftTeam other = (CraftTeam) obj;

            return this.team == other.team || this.team != null && this.team.equals(other.team);
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility() {
        int[] aint = CraftTeam.$SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility;

        if (CraftTeam.$SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility != null) {
            return aint;
        } else {
            int[] aint1 = new int[NameTagVisibility.values().length];

            try {
                aint1[NameTagVisibility.ALWAYS.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[NameTagVisibility.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[NameTagVisibility.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[NameTagVisibility.NEVER.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            CraftTeam.$SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility() {
        int[] aint = CraftTeam.$SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility;

        if (CraftTeam.$SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility != null) {
            return aint;
        } else {
            int[] aint1 = new int[ScoreboardTeamBase.EnumNameTagVisibility.values().length];

            try {
                aint1[ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[ScoreboardTeamBase.EnumNameTagVisibility.NEVER.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            CraftTeam.$SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility = aint1;
            return aint1;
        }
    }
}
