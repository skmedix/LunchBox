package org.bukkit;

public interface WorldBorder {

    void reset();

    double getSize();

    void setSize(double d0);

    void setSize(double d0, long i);

    Location getCenter();

    void setCenter(double d0, double d1);

    void setCenter(Location location);

    double getDamageBuffer();

    void setDamageBuffer(double d0);

    double getDamageAmount();

    void setDamageAmount(double d0);

    int getWarningTime();

    void setWarningTime(int i);

    int getWarningDistance();

    void setWarningDistance(int i);
}
