package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class ScoreboardCommand extends VanillaCommand {

    private static final List MAIN_CHOICES = ImmutableList.of("objectives", "players", "teams");
    private static final List OBJECTIVES_CHOICES = ImmutableList.of("list", "add", "remove", "setdisplay");
    private static final List OBJECTIVES_CRITERIA = ImmutableList.of("health", "playerKillCount", "totalKillCount", "deathCount", "dummy");
    private static final List PLAYERS_CHOICES = ImmutableList.of("set", "add", "remove", "reset", "list");
    private static final List TEAMS_CHOICES = ImmutableList.of("add", "remove", "join", "leave", "empty", "list", "option");
    private static final List TEAMS_OPTION_CHOICES = ImmutableList.of("color", "friendlyfire", "seeFriendlyInvisibles");
    private static final Map OBJECTIVES_DISPLAYSLOT = ImmutableMap.of("belowName", DisplaySlot.BELOW_NAME, "list", DisplaySlot.PLAYER_LIST, "sidebar", DisplaySlot.SIDEBAR);
    private static final Map TEAMS_OPTION_COLOR = ImmutableMap.builder().put("aqua", ChatColor.AQUA).put("black", ChatColor.BLACK).put("blue", ChatColor.BLUE).put("bold", ChatColor.BOLD).put("dark_aqua", ChatColor.DARK_AQUA).put("dark_blue", ChatColor.DARK_BLUE).put("dark_gray", ChatColor.DARK_GRAY).put("dark_green", ChatColor.DARK_GREEN).put("dark_purple", ChatColor.DARK_PURPLE).put("dark_red", ChatColor.DARK_RED).put("gold", ChatColor.GOLD).put("gray", ChatColor.GRAY).put("green", ChatColor.GREEN).put("italic", ChatColor.ITALIC).put("light_purple", ChatColor.LIGHT_PURPLE).put("obfuscated", ChatColor.MAGIC).put("red", ChatColor.RED).put("reset", ChatColor.RESET).put("strikethrough", ChatColor.STRIKETHROUGH).put("underline", ChatColor.UNDERLINE).put("white", ChatColor.WHITE).put("yellow", ChatColor.YELLOW).build();
    private static final List BOOLEAN = ImmutableList.of("true", "false");

    public ScoreboardCommand() {
        super("scoreboard");
        this.description = "Scoreboard control";
        this.usageMessage = "/scoreboard";
        this.setPermission("bukkit.command.scoreboard");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length >= 1 && args[0].length() != 0) {
            Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Set teamName;
            Iterator option;
            Objective team;
            String s;
            String s1;
            String s2;

            if (args[0].equalsIgnoreCase("objectives")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /scoreboard objectives <list|add|remove|setdisplay>");
                    return false;
                }

                if (args[1].equalsIgnoreCase("list")) {
                    teamName = mainScoreboard.getObjectives();
                    if (teamName.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "There are no objectives on the scoreboard");
                        return false;
                    }

                    sender.sendMessage(ChatColor.DARK_GREEN + "Showing " + teamName.size() + " objective(s) on scoreboard");
                    option = teamName.iterator();

                    while (option.hasNext()) {
                        team = (Objective) option.next();
                        sender.sendMessage("- " + team.getName() + ": displays as \'" + team.getDisplayName() + "\' and is type \'" + team.getCriteria() + "\'");
                    }
                } else {
                    Objective value;

                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length < 4) {
                            sender.sendMessage(ChatColor.RED + "/scoreboard objectives add <name> <criteriaType> [display name ...]");
                            return false;
                        }

                        s = args[2];
                        s2 = args[3];
                        if (s2 == null) {
                            sender.sendMessage(ChatColor.RED + "Invalid objective criteria type. Valid types are: " + stringCollectionToString(ScoreboardCommand.OBJECTIVES_CRITERIA));
                        } else if (s.length() > 16) {
                            sender.sendMessage(ChatColor.RED + "The name \'" + s + "\' is too long for an objective, it can be at most 16 characters long");
                        } else if (mainScoreboard.getObjective(s) != null) {
                            sender.sendMessage(ChatColor.RED + "An objective with the name \'" + s + "\' already exists");
                        } else {
                            s1 = null;
                            if (args.length > 4) {
                                s1 = StringUtils.join(ArrayUtils.subarray((Object[]) args, 4, args.length), ' ');
                                if (s1.length() > 32) {
                                    sender.sendMessage(ChatColor.RED + "The name \'" + s1 + "\' is too long for an objective, it can be at most 32 characters long");
                                    return false;
                                }
                            }

                            value = mainScoreboard.registerNewObjective(s, s2);
                            if (s1 != null && s1.length() > 0) {
                                value.setDisplayName(s1);
                            }

                            sender.sendMessage("Added new objective \'" + s + "\' successfully");
                        }
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length != 3) {
                            sender.sendMessage(ChatColor.RED + "/scoreboard objectives remove <name>");
                            return false;
                        }

                        s = args[2];
                        team = mainScoreboard.getObjective(s);
                        if (team == null) {
                            sender.sendMessage(ChatColor.RED + "No objective was found by the name \'" + s + "\'");
                        } else {
                            team.unregister();
                            sender.sendMessage("Removed objective \'" + s + "\' successfully");
                        }
                    } else if (args[1].equalsIgnoreCase("setdisplay")) {
                        if (args.length != 3 && args.length != 4) {
                            sender.sendMessage(ChatColor.RED + "/scoreboard objectives setdisplay <slot> [objective]");
                            return false;
                        }

                        s = args[2];
                        DisplaySlot displayslot = (DisplaySlot) ScoreboardCommand.OBJECTIVES_DISPLAYSLOT.get(s);

                        if (displayslot == null) {
                            sender.sendMessage(ChatColor.RED + "No such display slot \'" + s + "\'");
                        } else if (args.length == 4) {
                            s1 = args[3];
                            value = mainScoreboard.getObjective(s1);
                            if (value == null) {
                                sender.sendMessage(ChatColor.RED + "No objective was found by the name \'" + s1 + "\'");
                                return false;
                            }

                            value.setDisplaySlot(displayslot);
                            sender.sendMessage("Set the display objective in slot \'" + s + "\' to \'" + value.getName() + "\'");
                        } else {
                            Objective objective = mainScoreboard.getObjective(displayslot);

                            if (objective != null) {
                                objective.setDisplaySlot((DisplaySlot) null);
                            }

                            sender.sendMessage("Cleared objective display slot \'" + s + "\'");
                        }
                    }
                }
            } else {
                String color;
                int i;

                if (args[0].equalsIgnoreCase("players")) {
                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "/scoreboard players <set|add|remove|reset|list>");
                        return false;
                    }

                    if (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove")) {
                        if (args[1].equalsIgnoreCase("reset")) {
                            if (args.length != 3) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard players reset <player>");
                                return false;
                            }

                            s = args[2];
                            if (s.length() > 16) {
                                sender.sendMessage(ChatColor.RED + "\'" + s + "\' is too long for a player name");
                                return false;
                            }

                            mainScoreboard.resetScores(s);
                            sender.sendMessage("Reset all scores of player " + s);
                        } else if (args[1].equalsIgnoreCase("list")) {
                            if (args.length > 3) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard players list <player>");
                                return false;
                            }

                            if (args.length == 2) {
                                teamName = mainScoreboard.getEntries();
                                if (teamName.isEmpty()) {
                                    sender.sendMessage(ChatColor.RED + "There are no tracked players on the scoreboard");
                                } else {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Showing " + teamName.size() + " tracked players on the scoreboard");
                                    sender.sendMessage(stringCollectionToString(teamName));
                                }
                            } else {
                                s = args[2];
                                if (s.length() > 16) {
                                    sender.sendMessage(ChatColor.RED + "\'" + s + "\' is too long for a player name");
                                    return false;
                                }

                                Set set = mainScoreboard.getScores(s);

                                if (set.isEmpty()) {
                                    sender.sendMessage(ChatColor.RED + "Player " + s + " has no scores recorded");
                                } else {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Showing " + set.size() + " tracked objective(s) for " + s);
                                    Iterator iterator = set.iterator();

                                    while (iterator.hasNext()) {
                                        Score score = (Score) iterator.next();

                                        sender.sendMessage("- " + score.getObjective().getDisplayName() + ": " + score.getScore() + " (" + score.getObjective().getName() + ")");
                                    }
                                }
                            }
                        }
                    } else {
                        if (args.length != 5) {
                            if (args[1].equalsIgnoreCase("set")) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard players set <player> <objective> <score>");
                            } else if (args[1].equalsIgnoreCase("add")) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard players add <player> <objective> <count>");
                            } else {
                                sender.sendMessage(ChatColor.RED + "/scoreboard players remove <player> <objective> <count>");
                            }

                            return false;
                        }

                        s = args[3];
                        team = mainScoreboard.getObjective(s);
                        if (team == null) {
                            sender.sendMessage(ChatColor.RED + "No objective was found by the name \'" + s + "\'");
                            return false;
                        }

                        if (!team.isModifiable()) {
                            sender.sendMessage(ChatColor.RED + "The objective \'" + s + "\' is read-only and cannot be set");
                            return false;
                        }

                        s1 = args[4];

                        try {
                            i = Integer.parseInt(s1);
                        } catch (NumberFormatException numberformatexception) {
                            sender.sendMessage(ChatColor.RED + "\'" + s1 + "\' is not a valid number");
                            return false;
                        }

                        if (i < 1 && !args[1].equalsIgnoreCase("set")) {
                            sender.sendMessage(ChatColor.RED + "The number you have entered (" + i + ") is too small, it must be at least 1");
                            return false;
                        }

                        color = args[2];
                        if (color.length() > 16) {
                            sender.sendMessage(ChatColor.RED + "\'" + color + "\' is too long for a player name");
                            return false;
                        }

                        Score player = team.getScore(color);
                        int team1;

                        if (args[1].equalsIgnoreCase("set")) {
                            team1 = i;
                        } else if (args[1].equalsIgnoreCase("add")) {
                            team1 = player.getScore() + i;
                        } else {
                            team1 = player.getScore() - i;
                        }

                        player.setScore(team1);
                        sender.sendMessage("Set score of " + s + " for player " + color + " to " + team1);
                    }
                } else {
                    if (!args[0].equalsIgnoreCase("teams")) {
                        sender.sendMessage(ChatColor.RED + "Usage: /scoreboard <objectives|players|teams>");
                        return false;
                    }

                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "/scoreboard teams <list|add|remove|empty|join|leave|option>");
                        return false;
                    }

                    Set set1;
                    Team team;

                    if (args[1].equalsIgnoreCase("list")) {
                        if (args.length == 2) {
                            teamName = mainScoreboard.getTeams();
                            if (teamName.isEmpty()) {
                                sender.sendMessage(ChatColor.RED + "There are no teams registered on the scoreboard");
                            } else {
                                sender.sendMessage(ChatColor.DARK_GREEN + "Showing " + teamName.size() + " teams on the scoreboard");
                                option = teamName.iterator();

                                while (option.hasNext()) {
                                    team = (Team) option.next();
                                    sender.sendMessage("- " + team.getName() + ": \'" + team.getDisplayName() + "\' has " + team.getSize() + " players");
                                }
                            }
                        } else {
                            if (args.length != 3) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard teams list [name]");
                                return false;
                            }

                            s = args[2];
                            team = mainScoreboard.getTeam(s);
                            if (team == null) {
                                sender.sendMessage(ChatColor.RED + "No team was found by the name \'" + s + "\'");
                            } else {
                                set1 = team.getPlayers();
                                if (set1.isEmpty()) {
                                    sender.sendMessage(ChatColor.RED + "Team " + team.getName() + " has no players");
                                } else {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Showing " + set1.size() + " player(s) in team " + team.getName());
                                    sender.sendMessage(offlinePlayerSetToString(set1));
                                }
                            }
                        }
                    } else {
                        Team team1;

                        if (args[1].equalsIgnoreCase("add")) {
                            if (args.length < 3) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard teams add <name> [display name ...]");
                                return false;
                            }

                            s = args[2];
                            if (s.length() > 16) {
                                sender.sendMessage(ChatColor.RED + "The name \'" + s + "\' is too long for a team, it can be at most 16 characters long");
                            } else if (mainScoreboard.getTeam(s) != null) {
                                sender.sendMessage(ChatColor.RED + "A team with the name \'" + s + "\' already exists");
                            } else {
                                s2 = null;
                                if (args.length > 3) {
                                    s2 = StringUtils.join(ArrayUtils.subarray((Object[]) args, 3, args.length), ' ');
                                    if (s2.length() > 32) {
                                        sender.sendMessage(ChatColor.RED + "The display name \'" + s2 + "\' is too long for a team, it can be at most 32 characters long");
                                        return false;
                                    }
                                }

                                team1 = mainScoreboard.registerNewTeam(s);
                                if (s2 != null && s2.length() > 0) {
                                    team1.setDisplayName(s2);
                                }

                                sender.sendMessage("Added new team \'" + team1.getName() + "\' successfully");
                            }
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (args.length != 3) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard teams remove <name>");
                                return false;
                            }

                            s = args[2];
                            team = mainScoreboard.getTeam(s);
                            if (team == null) {
                                sender.sendMessage(ChatColor.RED + "No team was found by the name \'" + s + "\'");
                            } else {
                                team.unregister();
                                sender.sendMessage("Removed team " + s);
                            }
                        } else if (args[1].equalsIgnoreCase("empty")) {
                            if (args.length != 3) {
                                sender.sendMessage(ChatColor.RED + "/scoreboard teams clear <name>");
                                return false;
                            }

                            s = args[2];
                            team = mainScoreboard.getTeam(s);
                            if (team == null) {
                                sender.sendMessage(ChatColor.RED + "No team was found by the name \'" + s + "\'");
                            } else {
                                set1 = team.getPlayers();
                                if (set1.isEmpty()) {
                                    sender.sendMessage(ChatColor.RED + "Team " + team.getName() + " is already empty, cannot remove nonexistant players");
                                } else {
                                    Iterator iterator1 = set1.iterator();

                                    while (iterator1.hasNext()) {
                                        OfflinePlayer offlineplayer = (OfflinePlayer) iterator1.next();

                                        team.removePlayer(offlineplayer);
                                    }

                                    sender.sendMessage("Removed all " + set1.size() + " player(s) from team " + team.getName());
                                }
                            }
                        } else if (args[1].equalsIgnoreCase("join")) {
                            label402: {
                                if (sender instanceof Player) {
                                    if (args.length >= 3) {
                                        break label402;
                                    }
                                } else if (args.length >= 4) {
                                    break label402;
                                }

                                sender.sendMessage(ChatColor.RED + "/scoreboard teams join <team> [player...]");
                                return false;
                            }

                            s = args[2];
                            team = mainScoreboard.getTeam(s);
                            if (team == null) {
                                sender.sendMessage(ChatColor.RED + "No team was found by the name \'" + s + "\'");
                            } else {
                                HashSet hashset = new HashSet();

                                if (sender instanceof Player && args.length == 3) {
                                    team.addPlayer((Player) sender);
                                    hashset.add(sender.getName());
                                } else {
                                    for (i = 3; i < args.length; ++i) {
                                        color = args[i];
                                        Player player = Bukkit.getPlayerExact(color);
                                        Object object;

                                        if (player != null) {
                                            object = player;
                                        } else {
                                            object = Bukkit.getOfflinePlayer(color);
                                        }

                                        team.addPlayer((OfflinePlayer) object);
                                        hashset.add(((OfflinePlayer) object).getName());
                                    }
                                }

                                sender.sendMessage("Added " + hashset.size() + " player(s) to team " + team.getName() + ": " + stringCollectionToString(hashset));
                            }
                        } else {
                            String s3;

                            if (args[1].equalsIgnoreCase("leave")) {
                                if (!(sender instanceof Player) && args.length < 3) {
                                    sender.sendMessage(ChatColor.RED + "/scoreboard teams leave [player...]");
                                    return false;
                                }

                                HashSet hashset1 = new HashSet();
                                HashSet hashset2 = new HashSet();

                                if (sender instanceof Player && args.length == 2) {
                                    team1 = mainScoreboard.getPlayerTeam((Player) sender);
                                    if (team1 != null) {
                                        team1.removePlayer((Player) sender);
                                        hashset1.add(sender.getName());
                                    } else {
                                        hashset2.add(sender.getName());
                                    }
                                } else {
                                    for (int j = 2; j < args.length; ++j) {
                                        s3 = args[j];
                                        Player player1 = Bukkit.getPlayerExact(s3);
                                        Object object1;

                                        if (player1 != null) {
                                            object1 = player1;
                                        } else {
                                            object1 = Bukkit.getOfflinePlayer(s3);
                                        }

                                        Team team2 = mainScoreboard.getPlayerTeam((OfflinePlayer) object1);

                                        if (team2 != null) {
                                            team2.removePlayer((OfflinePlayer) object1);
                                            hashset1.add(((OfflinePlayer) object1).getName());
                                        } else {
                                            hashset2.add(((OfflinePlayer) object1).getName());
                                        }
                                    }
                                }

                                if (!hashset1.isEmpty()) {
                                    sender.sendMessage("Removed " + hashset1.size() + " player(s) from their teams: " + stringCollectionToString(hashset1));
                                }

                                if (!hashset2.isEmpty()) {
                                    sender.sendMessage("Could not remove " + hashset2.size() + " player(s) from their teams: " + stringCollectionToString(hashset2));
                                }
                            } else if (args[1].equalsIgnoreCase("option")) {
                                if (args.length != 4 && args.length != 5) {
                                    sender.sendMessage(ChatColor.RED + "/scoreboard teams option <team> <friendlyfire|color|seefriendlyinvisibles> <value>");
                                    return false;
                                }

                                s = args[2];
                                team = mainScoreboard.getTeam(s);
                                if (team == null) {
                                    sender.sendMessage(ChatColor.RED + "No team was found by the name \'" + s + "\'");
                                    return false;
                                }

                                s1 = args[3].toLowerCase();
                                if (!s1.equals("friendlyfire") && !s1.equals("color") && !s1.equals("seefriendlyinvisibles")) {
                                    sender.sendMessage(ChatColor.RED + "/scoreboard teams option <team> <friendlyfire|color|seefriendlyinvisibles> <value>");
                                    return false;
                                }

                                if (args.length == 4) {
                                    if (s1.equals("color")) {
                                        sender.sendMessage(ChatColor.RED + "Valid values for option color are: " + stringCollectionToString(ScoreboardCommand.TEAMS_OPTION_COLOR.keySet()));
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Valid values for option " + s1 + " are: true and false");
                                    }
                                } else {
                                    s3 = args[4].toLowerCase();
                                    if (s1.equals("color")) {
                                        ChatColor chatcolor = (ChatColor) ScoreboardCommand.TEAMS_OPTION_COLOR.get(s3);

                                        if (chatcolor == null) {
                                            sender.sendMessage(ChatColor.RED + "Valid values for option color are: " + stringCollectionToString(ScoreboardCommand.TEAMS_OPTION_COLOR.keySet()));
                                            return false;
                                        }

                                        team.setPrefix(chatcolor.toString());
                                        team.setSuffix(ChatColor.RESET.toString());
                                    } else {
                                        if (!s3.equals("true") && !s3.equals("false")) {
                                            sender.sendMessage(ChatColor.RED + "Valid values for option " + s1 + " are: true and false");
                                            return false;
                                        }

                                        if (s1.equals("friendlyfire")) {
                                            team.setAllowFriendlyFire(s3.equals("true"));
                                        } else {
                                            team.setCanSeeFriendlyInvisibles(s3.equals("true"));
                                        }
                                    }

                                    sender.sendMessage("Set option " + s1 + " for team " + team.getName() + " to " + s3);
                                }
                            }
                        }
                    }
                }
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /scoreboard <objectives|players|teams>");
            return false;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        if (args.length == 1) {
            return (List) StringUtil.copyPartialMatches(args[0], ScoreboardCommand.MAIN_CHOICES, new ArrayList());
        } else {
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("objectives")) {
                    if (args.length == 2) {
                        return (List) StringUtil.copyPartialMatches(args[1], ScoreboardCommand.OBJECTIVES_CHOICES, new ArrayList());
                    }

                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length == 4) {
                            return (List) StringUtil.copyPartialMatches(args[3], ScoreboardCommand.OBJECTIVES_CRITERIA, new ArrayList());
                        }
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length == 3) {
                            return (List) StringUtil.copyPartialMatches(args[2], this.getCurrentObjectives(), new ArrayList());
                        }
                    } else if (args[1].equalsIgnoreCase("setdisplay")) {
                        if (args.length == 3) {
                            return (List) StringUtil.copyPartialMatches(args[2], ScoreboardCommand.OBJECTIVES_DISPLAYSLOT.keySet(), new ArrayList());
                        }

                        if (args.length == 4) {
                            return (List) StringUtil.copyPartialMatches(args[3], this.getCurrentObjectives(), new ArrayList());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("players")) {
                    if (args.length == 2) {
                        return (List) StringUtil.copyPartialMatches(args[1], ScoreboardCommand.PLAYERS_CHOICES, new ArrayList());
                    }

                    if (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove")) {
                        if (args.length == 3) {
                            return (List) StringUtil.copyPartialMatches(args[2], this.getCurrentEntries(), new ArrayList());
                        }
                    } else {
                        if (args.length == 3) {
                            return super.tabComplete(sender, alias, args);
                        }

                        if (args.length == 4) {
                            return (List) StringUtil.copyPartialMatches(args[3], this.getCurrentObjectives(), new ArrayList());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("teams")) {
                    if (args.length == 2) {
                        return (List) StringUtil.copyPartialMatches(args[1], ScoreboardCommand.TEAMS_CHOICES, new ArrayList());
                    }

                    if (args[1].equalsIgnoreCase("join")) {
                        if (args.length == 3) {
                            return (List) StringUtil.copyPartialMatches(args[2], this.getCurrentTeams(), new ArrayList());
                        }

                        if (args.length >= 4) {
                            return super.tabComplete(sender, alias, args);
                        }
                    } else {
                        if (args[1].equalsIgnoreCase("leave")) {
                            return super.tabComplete(sender, alias, args);
                        }

                        if (args[1].equalsIgnoreCase("option")) {
                            if (args.length == 3) {
                                return (List) StringUtil.copyPartialMatches(args[2], this.getCurrentTeams(), new ArrayList());
                            }

                            if (args.length == 4) {
                                return (List) StringUtil.copyPartialMatches(args[3], ScoreboardCommand.TEAMS_OPTION_CHOICES, new ArrayList());
                            }

                            if (args.length == 5) {
                                if (args[3].equalsIgnoreCase("color")) {
                                    return (List) StringUtil.copyPartialMatches(args[4], ScoreboardCommand.TEAMS_OPTION_COLOR.keySet(), new ArrayList());
                                }

                                return (List) StringUtil.copyPartialMatches(args[4], ScoreboardCommand.BOOLEAN, new ArrayList());
                            }
                        } else if (args.length == 3) {
                            return (List) StringUtil.copyPartialMatches(args[2], this.getCurrentTeams(), new ArrayList());
                        }
                    }
                }
            }

            return ImmutableList.of();
        }
    }

    private static String offlinePlayerSetToString(Set set) {
        StringBuilder string = new StringBuilder();
        String lastValue = null;
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            OfflinePlayer value = (OfflinePlayer) iterator.next();

            string.append(lastValue = value.getName()).append(", ");
        }

        string.delete(string.length() - 2, Integer.MAX_VALUE);
        if (string.length() != lastValue.length()) {
            string.insert(string.length() - lastValue.length(), "and ");
        }

        return string.toString();
    }

    private static String stringCollectionToString(Collection set) {
        StringBuilder string = new StringBuilder();
        String lastValue = null;
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            String value = (String) iterator.next();

            lastValue = value;
            string.append(value).append(", ");
        }

        string.delete(string.length() - 2, Integer.MAX_VALUE);
        if (string.length() != lastValue.length()) {
            string.insert(string.length() - lastValue.length(), "and ");
        }

        return string.toString();
    }

    private List getCurrentObjectives() {
        ArrayList list = new ArrayList();
        Iterator iterator = Bukkit.getScoreboardManager().getMainScoreboard().getObjectives().iterator();

        while (iterator.hasNext()) {
            Objective objective = (Objective) iterator.next();

            list.add(objective.getName());
        }

        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    private List getCurrentEntries() {
        ArrayList list = new ArrayList();
        Iterator iterator = Bukkit.getScoreboardManager().getMainScoreboard().getEntries().iterator();

        while (iterator.hasNext()) {
            String entry = (String) iterator.next();

            list.add(entry);
        }

        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    private List getCurrentTeams() {
        ArrayList list = new ArrayList();
        Iterator iterator = Bukkit.getScoreboardManager().getMainScoreboard().getTeams().iterator();

        while (iterator.hasNext()) {
            Team team = (Team) iterator.next();

            list.add(team.getName());
        }

        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }
}
