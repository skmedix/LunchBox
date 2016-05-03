package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class GameRuleCommand extends VanillaCommand {

    private static final List GAMERULE_STATES = ImmutableList.of("true", "false");

    public GameRuleCommand() {
        super("gamerule");
        this.description = "Sets a server\'s game rules";
        this.usageMessage = "/gamerule <rule name> <value> OR /gamerule <rule name>";
        this.setPermission("bukkit.command.gamerule");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length > 0) {
            String rule = args[0];
            World world = this.getGameWorld(sender);

            if (world.isGameRule(rule)) {
                String value;

                if (args.length > 1) {
                    value = args[1];
                    world.setGameRuleValue(rule, value);
                    Command.broadcastCommandMessage(sender, "Game rule " + rule + " has been set to: " + value);
                } else {
                    value = world.getGameRuleValue(rule);
                    sender.sendMessage(rule + " = " + value);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "No game rule called " + rule + " is available");
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            sender.sendMessage("Rules: " + this.createString(this.getGameWorld(sender).getGameRules(), 0, ", "));
            return true;
        }
    }

    private World getGameWorld(CommandSender sender) {
        if (sender instanceof HumanEntity) {
            World world = ((HumanEntity) sender).getWorld();

            if (world != null) {
                return world;
            }
        } else if (sender instanceof BlockCommandSender) {
            return ((BlockCommandSender) sender).getBlock().getWorld();
        }

        return (World) Bukkit.getWorlds().get(0);
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? (List) StringUtil.copyPartialMatches(args[0], Arrays.asList(this.getGameWorld(sender).getGameRules()), new ArrayList()) : (args.length == 2 ? (List) StringUtil.copyPartialMatches(args[1], GameRuleCommand.GAMERULE_STATES, new ArrayList(GameRuleCommand.GAMERULE_STATES.size())) : ImmutableList.of()));
    }
}
