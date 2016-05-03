package org.bukkit;

import java.util.Date;
import java.util.Set;

public interface BanList {

    BanEntry getBanEntry(String s);

    BanEntry addBan(String s, String s1, Date date, String s2);

    Set getBanEntries();

    boolean isBanned(String s);

    void pardon(String s);

    public static enum Type {

        NAME, IP;
    }
}
