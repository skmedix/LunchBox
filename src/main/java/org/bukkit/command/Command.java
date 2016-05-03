package org.bukkit.command;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.Permissible;
import org.bukkit.util.StringUtil;
import org.spigotmc.CustomTimingsHandler;

public abstract class Command {

    private String name;
    private String nextLabel;
    private String label;
    private List aliases;
    private List activeAliases;
    private CommandMap commandMap;
    protected String description;
    protected String usageMessage;
    private String permission;
    private String permissionMessage;
    public CustomTimingsHandler timings;

    protected Command(String name) {
        this(name, "", "/" + name, new ArrayList());
    }

    protected Command(String name, String description, String usageMessage, List aliases) {
        this.commandMap = null;
        this.description = "";
        this.name = name;
        this.nextLabel = name;
        this.label = name;
        this.description = description;
        this.usageMessage = usageMessage;
        this.aliases = aliases;
        this.activeAliases = new ArrayList(aliases);
        this.timings = new CustomTimingsHandler("** Command: " + name);
    }

    public abstract boolean execute(CommandSender commandsender, String s, String[] astring);

    /** @deprecated */
    @Deprecated
    public List tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        if (args.length == 0) {
            return ImmutableList.of();
        } else {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player) sender : null;
            ArrayList matchedPlayers = new ArrayList();
            Iterator iterator = sender.getServer().getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();
                String name = player.getName();

                if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                    matchedPlayers.add(name);
                }
            }

            Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
            return matchedPlayers;
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean setName(String name) {
        if (!this.isRegistered()) {
            this.name = name;
            return true;
        } else {
            return false;
        }
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean testPermission(CommandSender target) {
        if (this.testPermissionSilent(target)) {
            return true;
        } else {
            if (this.permissionMessage == null) {
                target.sendMessage(ChatColor.RED + "I\'m sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            } else if (this.permissionMessage.length() != 0) {
                String[] astring;
                int i = (astring = this.permissionMessage.replace("<permission>", this.permission).split("\n")).length;

                for (int j = 0; j < i; ++j) {
                    String line = astring[j];

                    target.sendMessage(line);
                }
            }

            return false;
        }
    }

    public boolean testPermissionSilent(CommandSender target) {
        if (this.permission != null && this.permission.length() != 0) {
            String[] astring;
            int i = (astring = this.permission.split(";")).length;

            for (int j = 0; j < i; ++j) {
                String p = astring[j];

                if (target.hasPermission(p)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public String getLabel() {
        return this.label;
    }

    public boolean setLabel(String name) {
        this.nextLabel = name;
        if (!this.isRegistered()) {
            this.timings = new CustomTimingsHandler("** Command: " + name);
            this.label = name;
            return true;
        } else {
            return false;
        }
    }

    public boolean register(CommandMap commandMap) {
        if (this.allowChangesFrom(commandMap)) {
            this.commandMap = commandMap;
            return true;
        } else {
            return false;
        }
    }

    public boolean unregister(CommandMap commandMap) {
        if (this.allowChangesFrom(commandMap)) {
            this.commandMap = null;
            this.activeAliases = new ArrayList(this.aliases);
            this.label = this.nextLabel;
            return true;
        } else {
            return false;
        }
    }

    private boolean allowChangesFrom(CommandMap commandMap) {
        return this.commandMap == null || this.commandMap == commandMap;
    }

    public boolean isRegistered() {
        return this.commandMap != null;
    }

    public List getAliases() {
        return this.activeAliases;
    }

    public String getPermissionMessage() {
        return this.permissionMessage;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usageMessage;
    }

    public Command setAliases(List aliases) {
        this.aliases = aliases;
        if (!this.isRegistered()) {
            this.activeAliases = new ArrayList(aliases);
        }

        return this;
    }

    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    public Command setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }

    public Command setUsage(String usage) {
        this.usageMessage = usage;
        return this;
    }

    public static void broadcastCommandMessage(CommandSender source, String message) {
        broadcastCommandMessage(source, message, true);
    }

    public static void broadcastCommandMessage(CommandSender source, String message, boolean sendToSource) {
        String result = source.getName() + ": " + message;

        if (source instanceof BlockCommandSender) {
            BlockCommandSender users = (BlockCommandSender) source;

            if (users.getBlock().getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        } else if (source instanceof CommandMinecart) {
            CommandMinecart users1 = (CommandMinecart) source;

            if (users1.getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        }

        Set users2 = Bukkit.getPluginManager().getPermissionSubscriptions("bukkit.broadcast.admin");
        String colored = "" + ChatColor.GRAY + ChatColor.ITALIC + "[" + result + ChatColor.GRAY + ChatColor.ITALIC + "]";

        if (sendToSource && !(source instanceof ConsoleCommandSender)) {
            source.sendMessage(message);
        }

        Iterator iterator = users2.iterator();

        while (iterator.hasNext()) {
            Permissible user = (Permissible) iterator.next();

            if (user instanceof CommandSender) {
                CommandSender target = (CommandSender) user;

                if (target instanceof ConsoleCommandSender) {
                    target.sendMessage(result);
                } else if (target != source) {
                    target.sendMessage(colored);
                }
            }
        }

    }

    public String toString() {
        return this.getClass().getName() + '(' + this.name + ')';
    }
}
