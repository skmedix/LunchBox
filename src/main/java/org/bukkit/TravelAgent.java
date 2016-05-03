package org.bukkit;

public interface TravelAgent {

    TravelAgent setSearchRadius(int i);

    int getSearchRadius();

    TravelAgent setCreationRadius(int i);

    int getCreationRadius();

    boolean getCanCreatePortal();

    void setCanCreatePortal(boolean flag);

    Location findOrCreate(Location location);

    Location findPortal(Location location);

    boolean createPortal(Location location);
}
