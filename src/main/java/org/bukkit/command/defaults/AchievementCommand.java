package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

/** @deprecated */
@Deprecated
public class AchievementCommand extends VanillaCommand {

    public AchievementCommand() {
        super("achievement");
        this.description = "Gives the specified player an achievement or changes a statistic value. Use \'*\' to give all achievements.";
        this.usageMessage = "/achievement give <stat_name> [player]";
        this.setPermission("bukkit.command.achievement");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else if (!args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            String statisticString = args[1];
            Player player = null;

            if (args.length > 2) {
                player = Bukkit.getPlayer(args[1]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }

            if (player == null) {
                sender.sendMessage("You must specify which player you wish to perform this action on.");
                return true;
            } else {
                Achievement achievement;
                int i;

                if (statisticString.equals("*")) {
                    Achievement[] aachievement;

                    i = (aachievement = Achievement.values()).length;

                    for (int j = 0; j < i; ++j) {
                        achievement = aachievement[j];
                        if (!player.hasAchievement(achievement)) {
                            PlayerAchievementAwardedEvent playerachievementawardedevent = new PlayerAchievementAwardedEvent(player, achievement);

                            Bukkit.getServer().getPluginManager().callEvent(playerachievementawardedevent);
                            if (!playerachievementawardedevent.isCancelled()) {
                                player.awardAchievement(achievement);
                            }
                        }
                    }

                    Command.broadcastCommandMessage(sender, String.format("Successfully given all achievements to %s", new Object[] { player.getName()}));
                    return true;
                } else {
                    achievement = Bukkit.getUnsafe().getAchievementFromInternalName(statisticString);
                    Statistic statistic = Bukkit.getUnsafe().getStatisticFromInternalName(statisticString);

                    if (achievement != null) {
                        if (player.hasAchievement(achievement)) {
                            sender.sendMessage(String.format("%s already has achievement %s", new Object[] { player.getName(), statisticString}));
                            return true;
                        } else {
                            PlayerAchievementAwardedEvent playerachievementawardedevent1 = new PlayerAchievementAwardedEvent(player, achievement);

                            Bukkit.getServer().getPluginManager().callEvent(playerachievementawardedevent1);
                            if (playerachievementawardedevent1.isCancelled()) {
                                sender.sendMessage(String.format("Unable to award %s the achievement %s", new Object[] { player.getName(), statisticString}));
                                return true;
                            } else {
                                player.awardAchievement(achievement);
                                Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", new Object[] { player.getName(), statisticString}));
                                return true;
                            }
                        }
                    } else if (statistic == null) {
                        sender.sendMessage(String.format("Unknown achievement or statistic \'%s\'", new Object[] { statisticString}));
                        return true;
                    } else if (statistic.getType() == Statistic.Type.UNTYPED) {
                        PlayerStatisticIncrementEvent playerstatisticincrementevent = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1);

                        Bukkit.getServer().getPluginManager().callEvent(playerstatisticincrementevent);
                        if (playerstatisticincrementevent.isCancelled()) {
                            sender.sendMessage(String.format("Unable to increment %s for %s", new Object[] { statisticString, player.getName()}));
                            return true;
                        } else {
                            player.incrementStatistic(statistic);
                            Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", new Object[] { player.getName(), statisticString}));
                            return true;
                        }
                    } else {
                        if (statistic.getType() == Statistic.Type.ENTITY) {
                            EntityType id = EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));

                            if (id == null) {
                                sender.sendMessage(String.format("Unknown achievement or statistic \'%s\'", new Object[] { statisticString}));
                                return true;
                            }

                            PlayerStatisticIncrementEvent material = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1, id);

                            Bukkit.getServer().getPluginManager().callEvent(material);
                            if (material.isCancelled()) {
                                sender.sendMessage(String.format("Unable to increment %s for %s", new Object[] { statisticString, player.getName()}));
                                return true;
                            }

                            try {
                                player.incrementStatistic(statistic, id);
                            } catch (IllegalArgumentException illegalargumentexception) {
                                sender.sendMessage(String.format("Unknown achievement or statistic \'%s\'", new Object[] { statisticString}));
                                return true;
                            }
                        } else {
                            try {
                                i = this.getInteger(sender, statisticString.substring(statisticString.lastIndexOf(".") + 1), 0, Integer.MAX_VALUE, true);
                            } catch (NumberFormatException numberformatexception) {
                                sender.sendMessage(numberformatexception.getMessage());
                                return true;
                            }

                            Material material = Material.getMaterial(i);

                            if (material == null) {
                                sender.sendMessage(String.format("Unknown achievement or statistic \'%s\'", new Object[] { statisticString}));
                                return true;
                            }

                            PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1, material);

                            Bukkit.getServer().getPluginManager().callEvent(event);
                            if (event.isCancelled()) {
                                sender.sendMessage(String.format("Unable to increment %s for %s", new Object[] { statisticString, player.getName()}));
                                return true;
                            }

                            try {
                                player.incrementStatistic(statistic, material);
                            } catch (IllegalArgumentException illegalargumentexception1) {
                                sender.sendMessage(String.format("Unknown achievement or statistic \'%s\'", new Object[] { statisticString}));
                                return true;
                            }
                        }

                        Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", new Object[] { player.getName(), statisticString}));
                        return true;
                    }
                }
            }
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? Arrays.asList(new String[] { "give"}) : (args.length == 2 ? Bukkit.getUnsafe().tabCompleteInternalStatisticOrAchievementName(args[1], new ArrayList()) : (args.length == 3 ? super.tabComplete(sender, alias, args) : ImmutableList.of())));
    }
}
