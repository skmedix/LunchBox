package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class EnchantCommand extends VanillaCommand {

    private static final List ENCHANTMENT_NAMES = new ArrayList();

    public EnchantCommand() {
        super("enchant");
        this.description = "Adds enchantments to the item the player is currently holding. Specify 0 for the level to remove an enchantment. Specify force to ignore normal enchantment restrictions";
        this.usageMessage = "/enchant <player> <enchantment> [level|max|0] [force]";
        this.setPermission("bukkit.command.enchant");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            boolean force = false;

            if (args.length > 2) {
                force = args[args.length > 3 ? 3 : 2].equalsIgnoreCase("force");
            }

            Player player = Bukkit.getPlayerExact(args[0]);

            if (player == null) {
                sender.sendMessage("Can\'t find player " + args[0]);
            } else {
                ItemStack item = player.getItemInHand();

                if (item.getType() == Material.AIR) {
                    sender.sendMessage("The player isn\'t holding an item");
                } else {
                    String itemName = item.getType().toString().replaceAll("_", " ");

                    itemName = WordUtils.capitalizeFully(itemName);
                    Enchantment enchantment = this.getEnchantment(args[1].toUpperCase());

                    if (enchantment == null) {
                        sender.sendMessage(String.format("Enchantment does not exist: %s", new Object[] { args[1]}));
                    } else {
                        String enchantmentName = enchantment.getName().replaceAll("_", " ");

                        enchantmentName = WordUtils.capitalizeFully(enchantmentName);
                        if (!force && !enchantment.canEnchantItem(item)) {
                            sender.sendMessage(String.format("%s cannot be applied to %s", new Object[] { enchantmentName, itemName}));
                        } else {
                            int level = 1;

                            if (args.length > 2) {
                                Integer enchantments = this.getInteger(args[2]);
                                int conflicts = enchantment.getStartLevel();
                                int entry = force ? 32767 : enchantment.getMaxLevel();

                                if (enchantments != null) {
                                    if (enchantments.intValue() == 0) {
                                        item.removeEnchantment(enchantment);
                                        Command.broadcastCommandMessage(sender, String.format("Removed %s on %s\'s %s", new Object[] { enchantmentName, player.getName(), itemName}));
                                        return true;
                                    }

                                    if (enchantments.intValue() < conflicts || enchantments.intValue() > entry) {
                                        sender.sendMessage(String.format("Level for enchantment %s must be between %d and %d", new Object[] { enchantmentName, Integer.valueOf(conflicts), Integer.valueOf(entry)}));
                                        sender.sendMessage("Specify 0 for level to remove an enchantment");
                                        return true;
                                    }

                                    level = enchantments.intValue();
                                }

                                if ("max".equals(args[2])) {
                                    level = entry;
                                }
                            }

                            Map enchantments1 = item.getEnchantments();
                            boolean conflicts1 = false;

                            if (!force && !enchantments1.isEmpty()) {
                                Iterator iterator = enchantments1.entrySet().iterator();

                                while (iterator.hasNext()) {
                                    Entry entry1 = (Entry) iterator.next();
                                    Enchantment enchant = (Enchantment) entry1.getKey();

                                    if (!enchant.equals(enchantment) && enchant.conflictsWith(enchantment)) {
                                        sender.sendMessage(String.format("Can\'t apply the enchantment %s on an item with the enchantment %s", new Object[] { enchantmentName, WordUtils.capitalizeFully(enchant.getName().replaceAll("_", " "))}));
                                        conflicts1 = true;
                                        break;
                                    }
                                }
                            }

                            if (!conflicts1) {
                                item.addUnsafeEnchantment(enchantment, level);
                                Command.broadcastCommandMessage(sender, String.format("Applied %s (Lvl %d) on %s\'s %s", new Object[] { enchantmentName, Integer.valueOf(level), player.getName(), itemName}), false);
                                sender.sendMessage(String.format("Enchanting succeeded, applied %s (Lvl %d) onto your %s", new Object[] { enchantmentName, Integer.valueOf(level), itemName}));
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? super.tabComplete(sender, alias, args) : (args.length == 2 ? (List) StringUtil.copyPartialMatches(args[1], EnchantCommand.ENCHANTMENT_NAMES, new ArrayList(EnchantCommand.ENCHANTMENT_NAMES.size())) : ((args.length == 3 || args.length == 4) && !args[args.length - 2].equalsIgnoreCase("force") ? ImmutableList.of("force") : ImmutableList.of())));
    }

    private Enchantment getEnchantment(String lookup) {
        Enchantment enchantment = Enchantment.getByName(lookup);

        if (enchantment == null) {
            Integer id = this.getInteger(lookup);

            if (id != null) {
                enchantment = Enchantment.getById(id.intValue());
            }
        }

        return enchantment;
    }

    public static void buildEnchantments() {
        if (!EnchantCommand.ENCHANTMENT_NAMES.isEmpty()) {
            throw new IllegalStateException("Enchantments have already been built!");
        } else {
            Enchantment[] aenchantment;
            int i = (aenchantment = Enchantment.values()).length;

            for (int j = 0; j < i; ++j) {
                Enchantment enchantment = aenchantment[j];

                EnchantCommand.ENCHANTMENT_NAMES.add(enchantment.getName());
            }

            Collections.sort(EnchantCommand.ENCHANTMENT_NAMES);
        }
    }
}
