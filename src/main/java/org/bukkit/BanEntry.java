package org.bukkit;

import java.util.Date;

public interface BanEntry {

    String getTarget();

    Date getCreated();

    void setCreated(Date date);

    String getSource();

    void setSource(String s);

    Date getExpiration();

    void setExpiration(Date date);

    String getReason();

    void setReason(String s);

    void save();
}
