package org.bukkit.command.defaults;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class GiveCommand extends VanillaCommand {

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
        GiveCommand.materials = ImmutableList.copyOf((Collection) materialList);
    }

    public GiveCommand() {
        super("give");
        this.description = "Gives the specified player a certain amount of items";
        this.usageMessage = "/give <player> <item> [amount [data]]";
        this.setPermission("bukkit.command.give");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            Player player = Bukkit.getPlayerExact(args[0]);

            if (player != null) {
                Material material = Material.matchMaterial(args[1]);

                if (material == null) {
                    material = Bukkit.getUnsafe().getMaterialFromInternalName(args[1]);
                }

                if (material != null) {
                    int amount = 1;
                    short data = 0;

                    if (args.length >= 3) {
                        amount = this.getInteger(sender, args[2], 1, 64);
                        if (args.length >= 4) {
                            try {
                                data = Short.parseShort(args[3]);
                            } catch (NumberFormatException numberformatexception) {
                                ;
                            }
                        }
                    }

                    ItemStack stack = new ItemStack(material, amount, data);

                    if (args.length >= 5) {
                        try {
                            stack = Bukkit.getUnsafe().modifyItemStack(stack, Joiner.on(' ').join((Iterable) Arrays.asList(args).subList(4, args.length)));
                        } catch (Throwable throwable) {
                            player.sendMessage("Not a valid tag");
                            return true;
                        }
                    }

                    player.getInventory().addItem(new ItemStack[] { stack});
                    Command.broadcastCommandMessage(sender, "Gave " + player.getName() + " some " + material.getId() + " (" + material + ")");
                } else {
                    sender.sendMessage("There\'s no item called " + args[1]);
                }
            } else {
                sender.sendMessage("Can\'t find player " + args[0]);
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
        } else if (args.length != 2) {
            return ImmutableList.of();
        } else {
            String arg = args[1];
            List materials = GiveCommand.materials;
            ArrayList completion = new ArrayList();
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

                completion.add(material);
                ++i;
            }

            return Bukkit.getUnsafe().tabCompleteInternalMaterialName(arg, completion);
        }
    }
}
