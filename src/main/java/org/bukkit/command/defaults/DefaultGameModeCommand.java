package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class DefaultGameModeCommand extends VanillaCommand {

    private static final List GAMEMODE_NAMES = ImmutableList.of("adventure", "creative", "survival");

    public DefaultGameModeCommand() {
        super("defaultgamemode");
        this.description = "Set the default gamemode";
        this.usageMessage = "/defaultgamemode <mode>";
        this.setPermission("bukkit.command.defaultgamemode");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 0) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return false;
        } else {
            String modeArg = args[0];
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
                        mode = GameMode.SURVIVAL;
                    } else {
                        mode = GameMode.ADVENTURE;
                    }
                } else {
                    mode = GameMode.CREATIVE;
                }
            }

            Bukkit.getServer().setDefaultGameMode(mode);
            Command.broadcastCommandMessage(sender, "Default game mode set to " + mode.toString().toLowerCase());
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? (List) StringUtil.copyPartialMatches(args[0], DefaultGameModeCommand.GAMEMODE_NAMES, new ArrayList(DefaultGameModeCommand.GAMEMODE_NAMES.size())) : ImmutableList.of());
    }
}
