package org.bukkit.command.defaults;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VersionCommand extends BukkitCommand {

    private final ReentrantLock versionLock = new ReentrantLock();
    private boolean hasVersion = false;
    private String versionMessage = null;
    private final Set versionWaiters = new HashSet();
    private boolean versionTaskStarted = false;
    private long lastCheck = 0L;

    public VersionCommand(String name) {
        super(name);
        this.description = "Gets the version of this server including any plugins in use";
        this.usageMessage = "/version [plugin name]";
        this.setPermission("bukkit.command.version");
        this.setAliases(Arrays.asList(new String[] { "ver", "about"}));
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            if (args.length == 0) {
                sender.sendMessage("This server is running " + Bukkit.getName() + " version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")");
                this.sendVersion(sender);
            } else {
                StringBuilder name = new StringBuilder();
                String[] plugin = args;
                int found = args.length;

                String pluginName;

                for (int exactPlugin = 0; exactPlugin < found; ++exactPlugin) {
                    pluginName = plugin[exactPlugin];
                    if (name.length() > 0) {
                        name.append(' ');
                    }

                    name.append(pluginName);
                }

                pluginName = name.toString();
                Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);

                if (plugin != null) {
                    this.describeToSender(plugin, sender);
                    return true;
                }

                boolean flag = false;

                pluginName = pluginName.toLowerCase();
                Plugin[] aplugin;
                int i = (aplugin = Bukkit.getPluginManager().getPlugins()).length;

                for (int j = 0; j < i; ++j) {
                    Plugin plugin1 = aplugin[j];

                    if (plugin1.getName().toLowerCase().contains(pluginName)) {
                        this.describeToSender(plugin1, sender);
                        flag = true;
                    }
                }

                if (!flag) {
                    sender.sendMessage("This server is not running any plugin by that name.");
                    sender.sendMessage("Use /plugins to get a list of plugins.");
                }
            }

            return true;
        }
    }

    private void describeToSender(Plugin plugin, CommandSender sender) {
        PluginDescriptionFile desc = plugin.getDescription();

        sender.sendMessage(ChatColor.GREEN + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());
        if (desc.getDescription() != null) {
            sender.sendMessage(desc.getDescription());
        }

        if (desc.getWebsite() != null) {
            sender.sendMessage("Website: " + ChatColor.GREEN + desc.getWebsite());
        }

        if (!desc.getAuthors().isEmpty()) {
            if (desc.getAuthors().size() == 1) {
                sender.sendMessage("Author: " + this.getAuthors(desc));
            } else {
                sender.sendMessage("Authors: " + this.getAuthors(desc));
            }
        }

    }

    private String getAuthors(PluginDescriptionFile desc) {
        StringBuilder result = new StringBuilder();
        List authors = desc.getAuthors();

        for (int i = 0; i < authors.size(); ++i) {
            if (result.length() > 0) {
                result.append(ChatColor.WHITE);
                if (i < authors.size() - 1) {
                    result.append(", ");
                } else {
                    result.append(" and ");
                }
            }

            result.append(ChatColor.GREEN);
            result.append((String) authors.get(i));
        }

        return result.toString();
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        if (args.length == 1) {
            ArrayList completions = new ArrayList();
            String toComplete = args[0].toLowerCase();
            Plugin[] aplugin;
            int i = (aplugin = Bukkit.getPluginManager().getPlugins()).length;

            for (int j = 0; j < i; ++j) {
                Plugin plugin = aplugin[j];

                if (StringUtil.startsWithIgnoreCase(plugin.getName(), toComplete)) {
                    completions.add(plugin.getName());
                }
            }

            return completions;
        } else {
            return ImmutableList.of();
        }
    }

    private void sendVersion(CommandSender sender) {
        if (this.hasVersion) {
            if (System.currentTimeMillis() - this.lastCheck <= 21600000L) {
                sender.sendMessage(this.versionMessage);
                return;
            }

            this.lastCheck = System.currentTimeMillis();
            this.hasVersion = false;
        }

        this.versionLock.lock();

        try {
            if (!this.hasVersion) {
                this.versionWaiters.add(sender);
                sender.sendMessage("Checking version, please wait...");
                if (!this.versionTaskStarted) {
                    this.versionTaskStarted = true;
                    (new Thread(new Runnable() {
                        public void run() {
                            VersionCommand.this.obtainVersion();
                        }
                    })).start();
                }

                return;
            }

            sender.sendMessage(this.versionMessage);
        } finally {
            this.versionLock.unlock();
        }

    }

    private void obtainVersion() {
        String version = Bukkit.getVersion();

        if (version == null) {
            version = "Custom";
        }

        if (version.startsWith("git-Spigot-")) {
            String[] cbVersions = version.substring("git-Spigot-".length()).split("-");
            int cbVersions1 = getDistance("craftbukkit", cbVersions[1].substring(0, cbVersions[1].indexOf(32)));
            int spigotVersions = getDistance("spigot", cbVersions[0]);

            if (cbVersions1 != -1 && spigotVersions != -1) {
                if (cbVersions1 == 0 && spigotVersions == 0) {
                    this.setVersionMessage("You are running the latest version");
                } else {
                    this.setVersionMessage("You are " + (cbVersions1 + spigotVersions) + " version(s) behind");
                }
            } else {
                this.setVersionMessage("Error obtaining version information");
            }
        } else if (version.startsWith("git-Bukkit-")) {
            version = version.substring("git-Bukkit-".length());
            int cbVersions2 = getDistance("craftbukkit", version.substring(0, version.indexOf(32)));

            if (cbVersions2 == -1) {
                this.setVersionMessage("Error obtaining version information");
            } else if (cbVersions2 == 0) {
                this.setVersionMessage("You are running the latest version");
            } else {
                this.setVersionMessage("You are " + cbVersions2 + " version(s) behind");
            }
        } else {
            this.setVersionMessage("Unknown version, custom build?");
        }

    }

    private void setVersionMessage(String msg) {
        this.lastCheck = System.currentTimeMillis();
        this.versionMessage = msg;
        this.versionLock.lock();

        try {
            this.hasVersion = true;
            this.versionTaskStarted = false;
            Iterator iterator = this.versionWaiters.iterator();

            while (iterator.hasNext()) {
                CommandSender sender = (CommandSender) iterator.next();

                sender.sendMessage(this.versionMessage);
            }

            this.versionWaiters.clear();
        } finally {
            this.versionLock.unlock();
        }
    }

    private static int getDistance(String repo, String hash) {
        try {
            BufferedReader e = Resources.asCharSource(new URL("https://hub.spigotmc.org/stash/rest/api/1.0/projects/SPIGOT/repos/" + repo + "/commits?since=" + URLEncoder.encode(hash, "UTF-8") + "&withCounts=true"), Charsets.UTF_8).openBufferedStream();

            try {
                JSONObject ex = (JSONObject) (new JSONParser()).parse((Reader) e);
                int i = ((Number) ex.get("totalCount")).intValue();

                return i;
            } catch (ParseException parseexception) {
                parseexception.printStackTrace();
            } finally {
                e.close();
            }

            return -1;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return -1;
        }
    }
}
