package org.bukkit.command.defaults;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/** @deprecated */
@Deprecated
public class SpreadPlayersCommand extends VanillaCommand {

    private static final Random random = new Random();

    public SpreadPlayersCommand() {
        super("spreadplayers");
        this.description = "Spreads players around a point";
        this.usageMessage = "/spreadplayers <x> <z> <spreadDistance> <maxRange> <respectTeams true|false> <player ...>";
        this.setPermission("bukkit.command.spreadplayers");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 6) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            double x = getDouble(sender, args[0], -3.0E7D, 3.0E7D);
            double z = getDouble(sender, args[1], -3.0E7D, 3.0E7D);
            double distance = getDouble(sender, args[2]);
            double range = getDouble(sender, args[3]);

            if (distance < 0.0D) {
                sender.sendMessage(ChatColor.RED + "Distance is too small.");
                return false;
            } else if (range < distance + 1.0D) {
                sender.sendMessage(ChatColor.RED + "Max range is too small.");
                return false;
            } else {
                String respectTeams = args[4];
                boolean teams = false;

                if (respectTeams.equalsIgnoreCase("true")) {
                    teams = true;
                } else if (!respectTeams.equalsIgnoreCase("false")) {
                    sender.sendMessage(String.format(ChatColor.RED + "\'%s\' is not true or false", new Object[] { args[4]}));
                    return false;
                }

                ArrayList players = Lists.newArrayList();
                World world = null;

                for (int i = 5; i < args.length; ++i) {
                    Player player = Bukkit.getPlayerExact(args[i]);

                    if (player != null) {
                        if (world == null) {
                            world = player.getWorld();
                        }

                        players.add(player);
                    }
                }

                if (world == null) {
                    return true;
                } else {
                    double xRangeMin = x - range;
                    double zRangeMin = z - range;
                    double xRangeMax = x + range;
                    double zRangeMax = z + range;
                    int spreadSize = teams ? this.getTeams(players) : players.size();
                    Location[] locations = this.getSpreadLocations(world, spreadSize, xRangeMin, zRangeMin, xRangeMax, zRangeMax);
                    int rangeSpread = this.range(world, distance, xRangeMin, zRangeMin, xRangeMax, zRangeMax, locations);

                    if (rangeSpread == -1) {
                        sender.sendMessage(String.format("Could not spread %d %s around %s,%s (too many players for space - try using spread of at most %s)", new Object[] { Integer.valueOf(spreadSize), teams ? "teams" : "players", Double.valueOf(x), Double.valueOf(z)}));
                        return false;
                    } else {
                        double distanceSpread = this.spread(world, players, locations, teams);

                        sender.sendMessage(String.format("Succesfully spread %d %s around %s,%s", new Object[] { Integer.valueOf(locations.length), teams ? "teams" : "players", Double.valueOf(x), Double.valueOf(z)}));
                        if (locations.length > 1) {
                            sender.sendMessage(String.format("(Average distance between %s is %s blocks apart after %s iterations)", new Object[] { teams ? "teams" : "players", String.format("%.2f", new Object[] { Double.valueOf(distanceSpread)}), Integer.valueOf(rangeSpread)}));
                        }

                        return true;
                    }
                }
            }
        }
    }

    private int range(World world, double distance, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax, Location[] locations) {
        boolean flag = true;

        int i;

        for (i = 0; i < 10000 && flag; ++i) {
            flag = false;
            double max = 3.4028234663852886E38D;

            int j;
            Location loc1;
            double z;
            double x;

            for (int locs = 0; locs < locations.length; ++locs) {
                Location i1 = locations[locs];

                j = 0;
                loc1 = new Location(world, 0.0D, 0.0D, 0.0D);

                for (int swap = 0; swap < locations.length; ++swap) {
                    if (locs != swap) {
                        Location loc3 = locations[swap];

                        z = i1.distanceSquared(loc3);
                        max = Math.min(z, max);
                        if (z < distance) {
                            ++j;
                            loc1.add(loc3.getX() - i1.getX(), 0.0D, 0.0D);
                            loc1.add(loc3.getZ() - i1.getZ(), 0.0D, 0.0D);
                        }
                    }
                }

                if (j > 0) {
                    i1.setX(i1.getX() / (double) j);
                    i1.setZ(i1.getZ() / (double) j);
                    x = Math.sqrt(loc1.getX() * loc1.getX() + loc1.getZ() * loc1.getZ());
                    if (x > 0.0D) {
                        loc1.setX(loc1.getX() / x);
                        i1.add(-loc1.getX(), 0.0D, -loc1.getZ());
                    } else {
                        z = xRangeMin >= xRangeMax ? xRangeMin : SpreadPlayersCommand.random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
                        double z1 = zRangeMin >= zRangeMax ? zRangeMin : SpreadPlayersCommand.random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;

                        i1.setX(z);
                        i1.setZ(z1);
                    }

                    flag = true;
                }

                boolean flag = false;

                if (i1.getX() < xRangeMin) {
                    i1.setX(xRangeMin);
                    flag = true;
                } else if (i1.getX() > xRangeMax) {
                    i1.setX(xRangeMax);
                    flag = true;
                }

                if (i1.getZ() < zRangeMin) {
                    i1.setZ(zRangeMin);
                    flag = true;
                } else if (i1.getZ() > zRangeMax) {
                    i1.setZ(zRangeMax);
                    flag = true;
                }

                if (flag) {
                    flag = true;
                }
            }

            if (!flag) {
                Location[] alocation = locations;
                int i = locations.length;

                for (j = 0; j < i; ++j) {
                    loc1 = alocation[j];
                    if (world.getHighestBlockYAt(loc1) == 0) {
                        x = xRangeMin >= xRangeMax ? xRangeMin : SpreadPlayersCommand.random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
                        z = zRangeMin >= zRangeMax ? zRangeMin : SpreadPlayersCommand.random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
                        locations[i] = new Location(world, x, 0.0D, z);
                        loc1.setX(x);
                        loc1.setZ(z);
                        flag = true;
                    }
                }
            }
        }

        return i >= 10000 ? -1 : i;
    }

    private double spread(World world, List list, Location[] locations, boolean teams) {
        double distance = 0.0D;
        int i = 0;
        HashMap hashmap = Maps.newHashMap();

        for (int j = 0; j < list.size(); ++j) {
            Player player = (Player) list.get(j);
            Location location;

            if (teams) {
                Team team = player.getScoreboard().getPlayerTeam(player);

                if (!hashmap.containsKey(team)) {
                    hashmap.put(team, locations[i++]);
                }

                location = (Location) hashmap.get(team);
            } else {
                location = locations[i++];
            }

            player.teleport(new Location(world, Math.floor(location.getX()) + 0.5D, (double) world.getHighestBlockYAt((int) location.getX(), (int) location.getZ()), Math.floor(location.getZ()) + 0.5D));
            double value = Double.MAX_VALUE;

            for (int k = 0; k < locations.length; ++k) {
                if (location != locations[k]) {
                    double d = location.distanceSquared(locations[k]);

                    value = Math.min(d, value);
                }
            }

            distance += value;
        }

        distance /= (double) list.size();
        return distance;
    }

    private int getTeams(List players) {
        HashSet teams = Sets.newHashSet();
        Iterator iterator = players.iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            teams.add(player.getScoreboard().getPlayerTeam(player));
        }

        return teams.size();
    }

    private Location[] getSpreadLocations(World world, int size, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax) {
        Location[] locations = new Location[size];

        for (int i = 0; i < size; ++i) {
            double x = xRangeMin >= xRangeMax ? xRangeMin : SpreadPlayersCommand.random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
            double z = zRangeMin >= zRangeMax ? zRangeMin : SpreadPlayersCommand.random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;

            locations[i] = new Location(world, x, 0.0D, z);
        }

        return locations;
    }
}
