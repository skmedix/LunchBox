package org.bukkit.craftbukkit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;

import org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal;
import org.bukkit.craftbukkit.libs.joptsimple.OptionException;
import org.bukkit.craftbukkit.libs.joptsimple.OptionParser;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.fusesource.jansi.AnsiConsole;

public class Main {

    public static boolean useJline = true;
    public static boolean useConsole = true;

    public static void main(String[] args) {
        OptionParser parser = new OptionParser() {
            {
                this.acceptsAll(Arrays.asList(new String[] { "?", "help"}), "Show the help");
                this.acceptsAll(Arrays.asList(new String[] { "c", "config"}), "Properties file to use").withRequiredArg().ofType(File.class).defaultsTo(new File("server.properties"), new File[0]).describedAs("Properties file");
                this.acceptsAll(Arrays.asList(new String[] { "P", "plugins"}), "Plugin directory to use").withRequiredArg().ofType(File.class).defaultsTo(new File("plugins"), new File[0]).describedAs("Plugin directory");
                this.acceptsAll(Arrays.asList(new String[] { "h", "host", "server-ip"}), "Host to listen on").withRequiredArg().ofType(String.class).describedAs("Hostname or IP");
                this.acceptsAll(Arrays.asList(new String[] { "W", "world-dir", "universe", "world-container"}), "World container").withRequiredArg().ofType(File.class).describedAs("Directory containing worlds");
                this.acceptsAll(Arrays.asList(new String[] { "w", "world", "level-name"}), "World name").withRequiredArg().ofType(String.class).describedAs("World name");
                this.acceptsAll(Arrays.asList(new String[] { "p", "port", "server-port"}), "Port to listen on").withRequiredArg().ofType(Integer.class).describedAs("Port");
                this.acceptsAll(Arrays.asList(new String[] { "o", "online-mode"}), "Whether to use online authentication").withRequiredArg().ofType(Boolean.class).describedAs("Authentication");
                this.acceptsAll(Arrays.asList(new String[] { "s", "size", "max-players"}), "Maximum amount of players").withRequiredArg().ofType(Integer.class).describedAs("Server size");
                this.acceptsAll(Arrays.asList(new String[] { "d", "date-format"}), "Format of the date to display in the console (for log entries)").withRequiredArg().ofType(SimpleDateFormat.class).describedAs("Log date format");
                this.acceptsAll(Arrays.asList(new String[] { "log-pattern"}), "Specfies the log filename pattern").withRequiredArg().ofType(String.class).defaultsTo("server.log", new String[0]).describedAs("Log filename");
                this.acceptsAll(Arrays.asList(new String[] { "log-limit"}), "Limits the maximum size of the log file (0 = unlimited)").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(0), new Integer[0]).describedAs("Max log size");
                this.acceptsAll(Arrays.asList(new String[] { "log-count"}), "Specified how many log files to cycle through").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(1), new Integer[0]).describedAs("Log count");
                this.acceptsAll(Arrays.asList(new String[] { "log-append"}), "Whether to append to the log file").withRequiredArg().ofType(Boolean.class).defaultsTo(Boolean.valueOf(true), new Boolean[0]).describedAs("Log append");
                this.acceptsAll(Arrays.asList(new String[] { "log-strip-color"}), "Strips color codes from log file");
                this.acceptsAll(Arrays.asList(new String[] { "b", "bukkit-settings"}), "File for bukkit settings").withRequiredArg().ofType(File.class).defaultsTo(new File("bukkit.yml"), new File[0]).describedAs("Yml file");
                this.acceptsAll(Arrays.asList(new String[] { "C", "commands-settings"}), "File for command settings").withRequiredArg().ofType(File.class).defaultsTo(new File("commands.yml"), new File[0]).describedAs("Yml file");
                this.acceptsAll(Arrays.asList(new String[] { "nojline"}), "Disables jline and emulates the vanilla console");
                this.acceptsAll(Arrays.asList(new String[] { "noconsole"}), "Disables the console");
                this.acceptsAll(Arrays.asList(new String[] { "v", "version"}), "Show the CraftBukkit Version");
                this.acceptsAll(Arrays.asList(new String[] { "demo"}), "Demo mode");
                this.acceptsAll(Arrays.asList(new String[] { "S", "spigot-settings"}), "File for spigot settings").withRequiredArg().ofType(File.class).defaultsTo(new File("spigot.yml"), new File[0]).describedAs("Yml file");
            }
        };
        OptionSet options = null;

        try {
            options = parser.parse(args);
        } catch (OptionException optionexception) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, optionexception.getLocalizedMessage());
        }

        if (options != null && !options.has("?")) {
            if (options.has("v")) {
                System.out.println(CraftServer.class.getPackage().getImplementationVersion());
            } else {
                String path = (new File(".")).getAbsolutePath();

                if (path.contains("!") || path.contains("+")) {
                    System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.");
                    return;
                }

                try {
                    String t = new String(new char[] { 'j', 'l', 'i', 'n', 'e', '.', 'U', 'n', 's', 'u', 'p', 'p', 'o', 'r', 't', 'e', 'd', 'T', 'e', 'r', 'm', 'i', 'n', 'a', 'l'});
                    String jline_terminal = new String(new char[] { 'j', 'l', 'i', 'n', 'e', '.', 't', 'e', 'r', 'm', 'i', 'n', 'a', 'l'});

                    Main.useJline = !t.equals(System.getProperty(jline_terminal));
                    if (options.has("nojline")) {
                        System.setProperty("user.language", "en");
                        Main.useJline = false;
                    }

                    if (Main.useJline) {
                        AnsiConsole.systemInstall();
                    } else {
                        System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", UnsupportedTerminal.class.getName());
                    }

                    if (options.has("noconsole")) {
                        Main.useConsole = false;
                    }

                    int maxPermGen = 0;
                    Iterator iterator = ManagementFactory.getRuntimeMXBean().getInputArguments().iterator();

                    while (iterator.hasNext()) {
                        String s = (String) iterator.next();

                        if (s.startsWith("-XX:MaxPermSize")) {
                            maxPermGen = Integer.parseInt(s.replaceAll("[^\\d]", ""));
                            maxPermGen <<= 10 * "kmg".indexOf(Character.toLowerCase(s.charAt(s.length() - 1)));
                        }
                    }

                    if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0F && maxPermGen < 131072) {
                        System.out.println("Warning, your max perm gen size is not set or less than 128mb. It is recommended you restart Java with the following argument: -XX:MaxPermSize=128M");
                        System.out.println("Please see http://www.spigotmc.org/wiki/changing-permgen-size/ for more details and more in-depth instructions.");
                    }

                    System.out.println("Loading libraries, please wait...");
                    //MinecraftServer.main(options.nonOptionArguments()); remove for now todo.
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } else {
            try {
                parser.printHelpOn((OutputStream) System.out);
            } catch (IOException ioexception) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String) null, ioexception);
            }
        }

    }

    private static List asList(String... params) {
        return Arrays.asList(params);
    }
}
