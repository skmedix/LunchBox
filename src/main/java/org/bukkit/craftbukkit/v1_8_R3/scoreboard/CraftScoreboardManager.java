package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.scoreboard.*;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.spigotmc.AsyncCatcher;

public final class CraftScoreboardManager implements ScoreboardManager {

    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection scoreboards = new WeakCollection();
    private final Map playerBoards = new HashMap();

    public CraftScoreboardManager(MinecraftServer minecraftserver, Scoreboard scoreboardServer) {
        this.mainScoreboard = new CraftScoreboard(scoreboardServer);
        this.server = minecraftserver;
        this.scoreboards.add(this.mainScoreboard);
    }

    public CraftScoreboard getMainScoreboard() {
        return this.mainScoreboard;
    }

    public CraftScoreboard getNewScoreboard() {
        AsyncCatcher.catchOp("scoreboard creation");
        CraftScoreboard scoreboard = new CraftScoreboard(new Scoreboard());
        this.scoreboards.add(scoreboard);
        return scoreboard;
    }

    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = (CraftScoreboard) this.playerBoards.get(player);

        return board == null ? this.getMainScoreboard() : board;
    }

    public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) throws IllegalArgumentException {
        Validate.isTrue(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");
        CraftScoreboard scoreboard = (CraftScoreboard) bukkitScoreboard;
        Scoreboard oldboard = this.getPlayerBoard(player).getHandle();
        Scoreboard newboard = scoreboard.getHandle();
        EntityPlayerMP entityplayer = player.getMPPlayer();

        if (oldboard != newboard) {
            if (scoreboard == this.mainScoreboard) {
                this.playerBoards.remove(player);
            } else {
                this.playerBoards.put(player, scoreboard);
            }

            HashSet removed = new HashSet();

            for (int iterator = 0; iterator < 3; ++iterator) {
                ScoreObjective scoreboardteam = oldboard.getObjectiveInDisplaySlot(iterator);

                if (scoreboardteam != null && !removed.contains(scoreboardteam)) {
                    entityplayer.playerNetServerHandler.sendPacket(new S3BPacketScoreboardObjective(scoreboardteam, 1));
                    removed.add(scoreboardteam);
                }
            }

            Iterator iterator = oldboard.getTeams().iterator();

            while (iterator.hasNext()) {
                ScorePlayerTeam scoreboardteam = (ScorePlayerTeam) iterator.next();

                entityplayer.playerNetServerHandler.sendPacket(new S3EPacketTeams(scoreboardteam, 1));
            }
            //will need to use access transformers to invoke the sendScoreboard method. todo
            this.server.getConfigurationManager().sendScoreboard((Scoreboard) newboard, player.getHandle());
        }
    }

    public void removePlayer(Player player) {
        this.playerBoards.remove(player);
    }

    public Collection getScoreboardScores(IScoreObjectiveCriteria criteria, String name, Collection collection) {
        Iterator iterator = this.scoreboards.iterator();

        while (iterator.hasNext()) {
            CraftScoreboard scoreboard = (CraftScoreboard) iterator.next();
            Scoreboard board = scoreboard.board;
            Iterator iterator1 = board.getObjectivesFromCriteria(criteria).iterator();

            while (iterator1.hasNext()) {
                ScoreObjective objective = (ScoreObjective) iterator1.next();

                collection.add(board.getValueFromObjective(name, objective));
            }
        }

        return collection;
    }

    public void updateAllScoresForList(IScoreObjectiveCriteria criteria, String name, List of) {
        Iterator iterator = this.getScoreboardScores(criteria, name, new ArrayList()).iterator();

        while (iterator.hasNext()) {
            Score score = (Score) iterator.next();

            score.func_96651_a(of);//not sure if this is correct todo
        }

    }
}
