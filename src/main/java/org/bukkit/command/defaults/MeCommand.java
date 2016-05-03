package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/** @deprecated */
@Deprecated
public class MeCommand extends VanillaCommand {

    public MeCommand() {
        super("me");
        this.description = "Performs the specified action in chat";
        this.usageMessage = "/me <action>";
        this.setPermission("bukkit.command.me");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            StringBuilder message = new StringBuilder();

            message.append(sender.getName());
            String[] astring = args;
            int i = args.length;

            for (int j = 0; j < i; ++j) {
                String arg = astring[j];

                message.append(" ");
                message.append(arg);
            }

            Bukkit.broadcastMessage("* " + message.toString());
            return true;
        }
    }
}
