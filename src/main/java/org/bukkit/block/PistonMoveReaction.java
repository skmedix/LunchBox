package org.bukkit.block;

import java.util.HashMap;
import java.util.Map;

public enum PistonMoveReaction {

    MOVE(0), BREAK(1), BLOCK(2);

    private int id;
    private static Map byId = new HashMap();

    static {
        PistonMoveReaction[] apistonmovereaction;
        int i = (apistonmovereaction = values()).length;

        for (int j = 0; j < i; ++j) {
            PistonMoveReaction reaction = apistonmovereaction[j];

            PistonMoveReaction.byId.put(Integer.valueOf(reaction.id), reaction);
        }

    }

    private PistonMoveReaction(int id) {
        this.id = id;
    }

    /** @deprecated */
    @Deprecated
    public int getId() {
        return this.id;
    }

    /** @deprecated */
    @Deprecated
    public static PistonMoveReaction getById(int id) {
        return (PistonMoveReaction) PistonMoveReaction.byId.get(Integer.valueOf(id));
    }
}
