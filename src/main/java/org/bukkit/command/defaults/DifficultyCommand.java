package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class DifficultyCommand extends VanillaCommand {

    private static final List DIFFICULTY_NAMES = ImmutableList.of("peaceful", "easy", "normal", "hard");

    public DifficultyCommand() {
        super("difficulty");
        this.description = "Sets the game difficulty";
        this.usageMessage = "/difficulty <new difficulty> ";
        this.setPermission("bukkit.command.difficulty");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 1 && args[0].length() != 0) {
            Difficulty difficulty = Difficulty.getByValue(this.getDifficultyForString(sender, args[0]));

            if (Bukkit.isHardcore()) {
                difficulty = Difficulty.HARD;
            }

            ((World) Bukkit.getWorlds().get(0)).setDifficulty(difficulty);
            int levelCount = 1;

            if (Bukkit.getAllowNether()) {
                ((World) Bukkit.getWorlds().get(levelCount)).setDifficulty(difficulty);
                ++levelCount;
            }

            if (Bukkit.getAllowEnd()) {
                ((World) Bukkit.getWorlds().get(levelCount)).setDifficulty(difficulty);
            }

            Command.broadcastCommandMessage(sender, "Set difficulty to " + difficulty.toString());
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        }
    }

    protected int getDifficultyForString(CommandSender sender, String name) {
        return !name.equalsIgnoreCase("peaceful") && !name.equalsIgnoreCase("p") ? (!name.equalsIgnoreCase("easy") && !name.equalsIgnoreCase("e") ? (!name.equalsIgnoreCase("normal") && !name.equalsIgnoreCase("n") ? (!name.equalsIgnoreCase("hard") && !name.equalsIgnoreCase("h") ? this.getInteger(sender, name, 0, 3) : 3) : 2) : 1) : 0;
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? (List) StringUtil.copyPartialMatches(args[0], DifficultyCommand.DIFFICULTY_NAMES, new ArrayList(DifficultyCommand.DIFFICULTY_NAMES.size())) : ImmutableList.of());
    }
}
