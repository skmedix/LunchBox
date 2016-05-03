package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** @deprecated */
@Deprecated
public class ListCommand extends VanillaCommand {

    public ListCommand() {
        super("list");
        this.description = "Lists all online players";
        this.usageMessage = "/list";
        this.setPermission("bukkit.command.list");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            StringBuilder online = new StringBuilder();
            Collection players = Bukkit.getOnlinePlayers();
            Iterator iterator = players.iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();

                if (!(sender instanceof Player) || ((Player) sender).canSee(player)) {
                    if (online.length() > 0) {
                        online.append(", ");
                    }

                    online.append(player.getDisplayName());
                }
            }

            sender.sendMessage("There are " + players.size() + "/" + Bukkit.getMaxPlayers() + " players online:\n" + online.toString());
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return ImmutableList.of();
    }
}
