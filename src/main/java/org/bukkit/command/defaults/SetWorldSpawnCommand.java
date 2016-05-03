package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** @deprecated */
@Deprecated
public class SetWorldSpawnCommand extends VanillaCommand {

    public SetWorldSpawnCommand() {
        super("setworldspawn");
        this.description = "Sets a worlds\'s spawn point. If no coordinates are specified, the player\'s coordinates will be used.";
        this.usageMessage = "/setworldspawn OR /setworldspawn <x> <y> <z>";
        this.setPermission("bukkit.command.setworldspawn");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            Player player = null;
            World world;

            if (sender instanceof Player) {
                player = (Player) sender;
                world = player.getWorld();
            } else {
                world = (World) Bukkit.getWorlds().get(0);
            }

            int x;
            int y;
            int z;

            if (args.length == 0) {
                if (player == null) {
                    sender.sendMessage("You can only perform this command as a player");
                    return true;
                }

                Location ex = player.getLocation();

                x = ex.getBlockX();
                y = ex.getBlockY();
                z = ex.getBlockZ();
            } else {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
                    return false;
                }

                try {
                    x = this.getInteger(sender, args[0], -30000000, 30000000, true);
                    y = this.getInteger(sender, args[1], 0, world.getMaxHeight(), true);
                    z = this.getInteger(sender, args[2], -30000000, 30000000, true);
                } catch (NumberFormatException numberformatexception) {
                    sender.sendMessage(numberformatexception.getMessage());
                    return true;
                }
            }

            world.setSpawnLocation(x, y, z);
            Command.broadcastCommandMessage(sender, "Set world " + world.getName() + "\'s spawnpoint to (" + x + ", " + y + ", " + z + ")");
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return ImmutableList.of();
    }
}
