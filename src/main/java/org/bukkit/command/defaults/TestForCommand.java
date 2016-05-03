package org.bukkit.command.defaults;

import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/** @deprecated */
@Deprecated
public class TestForCommand extends VanillaCommand {

    public TestForCommand() {
        super("testfor");
        this.description = "Tests whether a specifed player is online";
        this.usageMessage = "/testfor <player>";
        this.setPermission("bukkit.command.testfor");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            sender.sendMessage(ChatColor.RED + "/testfor is only usable by commandblocks with analog output.");
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return args.length == 0 ? super.tabComplete(sender, alias, args) : Collections.emptyList();
    }
}
