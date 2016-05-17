package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** @deprecated */
@Deprecated
public class StopCommand extends VanillaCommand {

    public StopCommand() {
        super("stop");
        this.description = "Stops the server with optional reason";
        this.usageMessage = "/stop [reason]";
        this.setPermission("bukkit.command.stop");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            Command.broadcastCommandMessage(sender, "Stopping the server..");
            Bukkit.shutdown();
            String reason = this.createString(args, 0);

            if (StringUtils.isNotEmpty(reason)) {
                Iterator iterator = Bukkit.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player = (Player) iterator.next();

                    player.kickPlayer(reason);
                }
            }

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
