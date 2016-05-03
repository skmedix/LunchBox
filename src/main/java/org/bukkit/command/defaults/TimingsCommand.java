package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.util.StringUtil;
import org.spigotmc.CustomTimingsHandler;

public class TimingsCommand extends BukkitCommand {

    private static final List TIMINGS_SUBCOMMANDS = ImmutableList.of("report", "reset", "on", "off", "paste");
    public static long timingStart = 0L;

    public TimingsCommand(String name) {
        super(name);
        this.description = "Manages Spigot Timings data to see performance of the server.";
        this.usageMessage = "/timings <reset|report|on|off|paste>";
        this.setPermission("bukkit.command.timings");
    }

    public void executeSpigotTimings(CommandSender sender, String[] args) {
        if ("on".equals(args[0])) {
            ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(true);
            CustomTimingsHandler.reload();
            sender.sendMessage("Enabled Timings & Reset");
        } else if ("off".equals(args[0])) {
            ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(false);
            sender.sendMessage("Disabled Timings");
        } else if (!Bukkit.getPluginManager().useTimings()) {
            sender.sendMessage("Please enable timings by typing /timings on");
        } else {
            boolean paste = "paste".equals(args[0]);

            if ("reset".equals(args[0])) {
                CustomTimingsHandler.reload();
                sender.sendMessage("Timings reset");
            } else if ("merged".equals(args[0]) || "report".equals(args[0]) || paste) {
                long sampleTime = System.nanoTime() - TimingsCommand.timingStart;
                int index = 0;
                File timingFolder = new File("timings");

                timingFolder.mkdirs();
                File timings = new File(timingFolder, "timings.txt");

                ByteArrayOutputStream bout;
                StringBuilder stringbuilder;

                for (bout = paste ? new ByteArrayOutputStream() : null; timings.exists(); timings = new File(timingFolder, stringbuilder.append(index).append(".txt").toString())) {
                    stringbuilder = new StringBuilder("timings");
                    ++index;
                }

                PrintStream fileTimings = null;

                try {
                    fileTimings = paste ? new PrintStream(bout) : new PrintStream(timings);
                    CustomTimingsHandler.printTimings(fileTimings);
                    fileTimings.println("Sample time " + sampleTime + " (" + (double) sampleTime / 1.0E9D + "s)");
                    fileTimings.println("<spigotConfig>");
                    fileTimings.println(Bukkit.spigot().getConfig().saveToString());
                    fileTimings.println("</spigotConfig>");
                    if (!paste) {
                        sender.sendMessage("Timings written to " + timings.getPath());
                        sender.sendMessage("Paste contents of file into form at http://www.spigotmc.org/go/timings to read results.");
                        return;
                    }

                    (new TimingsCommand.PasteThread(sender, bout)).start();
                } catch (IOException ioexception) {
                    return;
                } finally {
                    if (fileTimings != null) {
                        fileTimings.close();
                    }

                }

                return;
            }

        }
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            this.executeSpigotTimings(sender, args);
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? (List) StringUtil.copyPartialMatches(args[0], TimingsCommand.TIMINGS_SUBCOMMANDS, new ArrayList(TimingsCommand.TIMINGS_SUBCOMMANDS.size())) : ImmutableList.of());
    }

    private static class PasteThread extends Thread {

        private final CommandSender sender;
        private final ByteArrayOutputStream bout;

        public PasteThread(CommandSender sender, ByteArrayOutputStream bout) {
            super("Timings paste thread");
            this.sender = sender;
            this.bout = bout;
        }

        public synchronized void start() {
            if (this.sender instanceof RemoteConsoleCommandSender) {
                this.run();
            } else {
                super.start();
            }

        }

        public void run() {
            try {
                HttpURLConnection ex = (HttpURLConnection) (new URL("http://paste.ubuntu.com/")).openConnection();

                ex.setDoOutput(true);
                ex.setRequestMethod("POST");
                ex.setInstanceFollowRedirects(false);
                OutputStream out = ex.getOutputStream();

                out.write("poster=Spigot&syntax=text&content=".getBytes("UTF-8"));
                out.write(URLEncoder.encode(this.bout.toString("UTF-8"), "UTF-8").getBytes("UTF-8"));
                out.close();
                ex.getInputStream().close();
                String location = ex.getHeaderField("Location");
                String pasteID = location.substring("http://paste.ubuntu.com/".length(), location.length() - 1);

                this.sender.sendMessage(ChatColor.GREEN + "Timings results can be viewed at http://www.spigotmc.org/go/timings?url=" + pasteID);
            } catch (IOException ioexception) {
                this.sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
                Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ioexception);
            }

        }
    }
}
