package org.apache.logging.log4j;

import java.util.Locale;

public enum Level {

    OFF(0), FATAL(1), ERROR(2), WARN(3), INFO(4), DEBUG(5), TRACE(6), ALL(Integer.MAX_VALUE);

    private final int intLevel;

    private Level(int i) {
        this.intLevel = i;
    }

    public static Level toLevel(String s) {
        return toLevel(s, Level.DEBUG);
    }

    public static Level toLevel(String s, Level level) {
        if (s == null) {
            return level;
        } else {
            String s1 = s.toUpperCase(Locale.ENGLISH);
            Level[] alevel = values();
            int i = alevel.length;

            for (int j = 0; j < i; ++j) {
                Level level1 = alevel[j];

                if (level1.name().equals(s1)) {
                    return level1;
                }
            }

            return level;
        }
    }

    public boolean isAtLeastAsSpecificAs(Level level) {
        return this.intLevel <= level.intLevel;
    }

    public boolean isAtLeastAsSpecificAs(int i) {
        return this.intLevel <= i;
    }

    public boolean lessOrEqual(Level level) {
        return this.intLevel <= level.intLevel;
    }

    public boolean lessOrEqual(int i) {
        return this.intLevel <= i;
    }

    public int intLevel() {
        return this.intLevel;
    }
}
