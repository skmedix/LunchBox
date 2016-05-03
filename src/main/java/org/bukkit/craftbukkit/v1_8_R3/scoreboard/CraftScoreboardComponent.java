package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

abstract class CraftScoreboardComponent {

    private CraftScoreboard scoreboard;

    CraftScoreboardComponent(CraftScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    abstract CraftScoreboard checkState() throws IllegalStateException;

    public CraftScoreboard getScoreboard() {
        return this.scoreboard;
    }

    abstract void unregister() throws IllegalStateException;
}
