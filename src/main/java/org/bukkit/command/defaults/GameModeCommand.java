package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class GameModeCommand extends VanillaCommand {

    private static final List GAMEMODE_NAMES = ImmutableList.of("adventure", "creative", "survival", "spectator");

    public GameModeCommand() {
        super("gamemode");
        this.description = "Changes the player to a specific game mode";
        this.usageMessage = "/gamemode <mode> [player]";
        this.setPermission("bukkit.command.gamemode");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            String modeArg = args[0];
            String playerArg = sender.getName();

            if (args.length == 2) {
                playerArg = args[1];
            }

            Player player = Bukkit.getPlayerExact(playerArg);

            if (player != null) {
                int value = -1;

                try {
                    value = Integer.parseInt(modeArg);
                } catch (NumberFormatException numberformatexception) {
                    ;
                }

                GameMode mode = GameMode.getByValue(value);

                if (mode == null) {
                    if (!modeArg.equalsIgnoreCase("creative") && !modeArg.equalsIgnoreCase("c")) {
                        if (!modeArg.equalsIgnoreCase("adventure") && !modeArg.equalsIgnoreCase("a")) {
                            if (!modeArg.equalsIgnoreCase("spectator") && !modeArg.equalsIgnoreCase("sp")) {
                                mode = GameMode.SURVIVAL;
                            } else {
                                mode = GameMode.SPECTATOR;
                            }
                        } else {
                            mode = GameMode.ADVENTURE;
                        }
                    } else {
                        mode = GameMode.CREATIVE;
                    }
                }

                if (mode != player.getGameMode()) {
                    player.setGameMode(mode);
                    if (mode != player.getGameMode()) {
                        sender.sendMessage("Game mode change for " + player.getName() + " failed!");
                    } else if (player == sender) {
                        Command.broadcastCommandMessage(sender, "Set own game mode to " + mode.toString() + " mode");
                    } else {
                        Command.broadcastCommandMessage(sender, "Set " + player.getName() + "\'s game mode to " + mode.toString() + " mode");
                    }
                } else {
                    sender.sendMessage(player.getName() + " already has game mode " + mode.getValue());
                }
            } else {
                sender.sendMessage("Can\'t find player " + playerArg);
            }

            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? (List) StringUtil.copyPartialMatches(args[0], GameModeCommand.GAMEMODE_NAMES, new ArrayList(GameModeCommand.GAMEMODE_NAMES.size())) : (args.length == 2 ? super.tabComplete(sender, alias, args) : ImmutableList.of()));
    }
}
