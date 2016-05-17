package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/** @deprecated */
@Deprecated
public class SaveCommand extends VanillaCommand {

    public SaveCommand() {
        super("save-all");
        this.description = "Saves the server to disk";
        this.usageMessage = "/save-all";
        this.setPermission("bukkit.command.save.perform");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            Command.broadcastCommandMessage(sender, "Forcing save..");
            Bukkit.savePlayers();
            Iterator iterator = Bukkit.getWorlds().iterator();

            while (iterator.hasNext()) {
                World world = (World) iterator.next();

                world.save();
            }

            Command.broadcastCommandMessage(sender, "Save complete.");
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
