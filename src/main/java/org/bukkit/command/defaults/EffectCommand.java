package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class EffectCommand extends VanillaCommand {

    private static final List effects;

    static {
        ImmutableList.Builder builder = ImmutableList.builder();
        PotionEffectType[] apotioneffecttype;
        int i = (apotioneffecttype = PotionEffectType.values()).length;

        for (int j = 0; j < i; ++j) {
            PotionEffectType type = apotioneffecttype[j];

            if (type != null) {
                builder.add((Object) type.getName());
            }
        }

        effects = builder.build();
    }

    public EffectCommand() {
        super("effect");
        this.description = "Adds/Removes effects on players";
        this.usageMessage = "/effect <player> <effect|clear> [seconds] [amplifier]";
        this.setPermission("bukkit.command.effect");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(this.getUsage());
            return true;
        } else {
            Player player = sender.getServer().getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + String.format("Player, %s, not found", new Object[] { args[0]}));
                return true;
            } else if (!"clear".equalsIgnoreCase(args[1])) {
                PotionEffectType effect1 = PotionEffectType.getByName(args[1]);

                if (effect1 == null) {
                    effect1 = PotionEffectType.getById(this.getInteger(sender, args[1], 0));
                }

                if (effect1 == null) {
                    sender.sendMessage(ChatColor.RED + String.format("Effect, %s, not found", new Object[] { args[1]}));
                    return true;
                } else {
                    int duration1 = 600;
                    int duration_temp = 30;
                    int amplification = 0;

                    if (args.length >= 3) {
                        duration_temp = this.getInteger(sender, args[2], 0, 1000000);
                        if (effect1.isInstant()) {
                            duration1 = duration_temp;
                        } else {
                            duration1 = duration_temp * 20;
                        }
                    } else if (effect1.isInstant()) {
                        duration1 = 1;
                    }

                    if (args.length >= 4) {
                        amplification = this.getInteger(sender, args[3], 0, 255);
                    }

                    if (duration_temp == 0) {
                        if (!player.hasPotionEffect(effect1)) {
                            sender.sendMessage(String.format("Couldn\'t take %s from %s as they do not have the effect", new Object[] { effect1.getName(), args[0]}));
                            return true;
                        }

                        player.removePotionEffect(effect1);
                        broadcastCommandMessage(sender, String.format("Took %s from %s", new Object[] { effect1.getName(), args[0]}));
                    } else {
                        PotionEffect applyEffect = new PotionEffect(effect1, duration1, amplification);

                        player.addPotionEffect(applyEffect, true);
                        broadcastCommandMessage(sender, String.format("Given %s (ID %d) * %d to %s for %d seconds", new Object[] { effect1.getName(), Integer.valueOf(effect1.getId()), Integer.valueOf(amplification), args[0], Integer.valueOf(duration_temp)}));
                    }

                    return true;
                }
            } else {
                Iterator duration = player.getActivePotionEffects().iterator();

                while (duration.hasNext()) {
                    PotionEffect effect = (PotionEffect) duration.next();

                    player.removePotionEffect(effect.getType());
                }

                sender.sendMessage(String.format("Took all effects from %s", new Object[] { args[0]}));
                return true;
            }
        }
    }

    public List tabComplete(CommandSender sender, String commandLabel, String[] args) {
        return (List) (args.length == 1 ? super.tabComplete(sender, commandLabel, args) : (args.length == 2 ? (List) StringUtil.copyPartialMatches(args[1], EffectCommand.effects, new ArrayList(EffectCommand.effects.size())) : ImmutableList.of()));
    }
}
