package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/** @deprecated */
@Deprecated
public class SayCommand extends VanillaCommand {

    public SayCommand() {
        super("say");
        this.description = "Broadcasts the given message as the sender";
        this.usageMessage = "/say <message ...>";
        this.setPermission("bukkit.command.say");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            StringBuilder message = new StringBuilder();

            message.append(ChatColor.LIGHT_PURPLE).append("[");
            if (sender instanceof ConsoleCommandSender) {
                message.append("Server");
            } else if (sender instanceof Player) {
                message.append(((Player) sender).getDisplayName());
            } else {
                message.append(sender.getName());
            }

            message.append(ChatColor.LIGHT_PURPLE).append("] ");
            if (args.length > 0) {
                message.append(args[0]);

                for (int i = 1; i < args.length; ++i) {
                    message.append(" ").append(args[i]);
                }
            }

            Bukkit.broadcastMessage(message.toString());
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        return (List) (args.length >= 1 ? super.tabComplete(sender, alias, args) : ImmutableList.of());
    }
}
