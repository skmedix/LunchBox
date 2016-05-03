package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class ClearCommand extends VanillaCommand {

    private static List materials;

    static {
        ArrayList materialList = new ArrayList();
        Material[] amaterial;
        int i = (amaterial = Material.values()).length;

        for (int j = 0; j < i; ++j) {
            Material material = amaterial[j];

            materialList.add(material.name());
        }

        Collections.sort(materialList);
        ClearCommand.materials = ImmutableList.copyOf((Collection) materialList);
    }

    public ClearCommand() {
        super("clear");
        this.description = "Clears the player\'s inventory. Can specify item and data filters too.";
        this.usageMessage = "/clear <player> [item] [data]";
        this.setPermission("bukkit.command.clear");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            Player player = null;

            if (args.length > 0) {
                player = Bukkit.getPlayer(args[0]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }

            if (player != null) {
                int id;

                if (args.length > 1 && !args[1].equals("-1")) {
                    Material data = Material.matchMaterial(args[1]);

                    if (data == null) {
                        sender.sendMessage(ChatColor.RED + "There\'s no item called " + args[1]);
                        return false;
                    }

                    id = data.getId();
                } else {
                    id = -1;
                }

                int data1 = args.length >= 3 ? this.getInteger(sender, args[2], 0) : -1;
                int count = player.getInventory().clear(id, data1);

                Command.broadcastCommandMessage(sender, "Cleared the inventory of " + player.getDisplayName() + ", removing " + count + " items");
            } else if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Please provide a player!");
            } else {
                sender.sendMessage(ChatColor.RED + "Can\'t find player " + args[0]);
            }

            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        } else {
            if (args.length == 2) {
                String arg = args[1];
                List materials = ClearCommand.materials;
                ArrayList completion = null;
                int size = materials.size();
                int i = Collections.binarySearch(materials, arg, String.CASE_INSENSITIVE_ORDER);

                if (i < 0) {
                    i = -1 - i;
                }

                while (i < size) {
                    String material = (String) materials.get(i);

                    if (!StringUtil.startsWithIgnoreCase(material, arg)) {
                        break;
                    }

                    if (completion == null) {
                        completion = new ArrayList();
                    }

                    completion.add(material);
                    ++i;
                }

                if (completion != null) {
                    return completion;
                }
            }

            return ImmutableList.of();
        }
    }
}
