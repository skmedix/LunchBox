package org.spigotmc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Metrics {

    private static final int REVISION = 6;
    private static final String BASE_URL = "http://mcstats.org";
    private static final String REPORT_URL = "/report/%s";
    private static final String CUSTOM_DATA_SEPARATOR = "~~";
    private static final int PING_INTERVAL = 10;
    private final Set graphs = Collections.synchronizedSet(new HashSet());
    private final Metrics.Graph defaultGraph = new Metrics.Graph("Default", (Metrics.Graph) null);
    private final YamlConfiguration configuration;
    private final File configurationFile = this.getConfigFile();
    private final String guid;
    private final boolean debug;
    private final Object optOutLock = new Object();
    private volatile Timer task = null;

    public Metrics() throws IOException {
        this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);
        this.configuration.addDefault("opt-out", Boolean.valueOf(false));
        this.configuration.addDefault("guid", UUID.randomUUID().toString());
        this.configuration.addDefault("debug", Boolean.valueOf(false));
        if (this.configuration.get("guid", (Object) null) == null) {
            this.configuration.options().header("http://mcstats.org").copyDefaults(true);
            this.configuration.save(this.configurationFile);
        }

        this.guid = this.configuration.getString("guid");
        this.debug = this.configuration.getBoolean("debug", false);
    }

    public Metrics.Graph createGraph(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Graph name cannot be null");
        } else {
            Metrics.Graph graph = new Metrics.Graph(name, (Metrics.Graph) null);

            this.graphs.add(graph);
            return graph;
        }
    }

    public void addGraph(Metrics.Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        } else {
            this.graphs.add(graph);
        }
    }

    public void addCustomData(Metrics.Plotter plotter) {
        if (plotter == null) {
            throw new IllegalArgumentException("Plotter cannot be null");
        } else {
            this.defaultGraph.addPlotter(plotter);
            this.graphs.add(this.defaultGraph);
        }
    }

    public boolean start() {
        Object object = this.optOutLock;

        synchronized (this.optOutLock) {
            if (this.isOptOut()) {
                return false;
            } else if (this.task != null) {
                return true;
            } else {
                this.task = new Timer("Spigot Metrics Thread", true);
                this.task.scheduleAtFixedRate(new TimerTask() {
                    private boolean firstPost = true;

                    public void run() {
                        try {
                            synchronized (Metrics.this.optOutLock) {
                                if (Metrics.this.isOptOut() && Metrics.this.task != null) {
                                    Metrics.this.task.cancel();
                                    Metrics.this.task = null;
                                    Iterator iterator = Metrics.this.graphs.iterator();

                                    while (iterator.hasNext()) {
                                        Metrics.Graph graph = (Metrics.Graph) iterator.next();

                                        graph.onOptOut();
                                    }
                                }
                            }

                            Metrics.this.postPlugin(!this.firstPost);
                            this.firstPost = false;
                        } catch (IOException ioexception) {
                            if (Metrics.this.debug) {
                                Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ioexception.getMessage());
                            }
                        }

                    }
                }, 0L, TimeUnit.MINUTES.toMillis(10L));
                return true;
            }
        }
    }

    public boolean isOptOut() {
        Object object = this.optOutLock;

        synchronized (this.optOutLock) {
            try {
                this.configuration.load(this.getConfigFile());
            } catch (IOException ioexception) {
                if (this.debug) {
                    Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ioexception.getMessage());
                }

                return true;
            } catch (InvalidConfigurationException invalidconfigurationexception) {
                if (this.debug) {
                    Bukkit.getLogger().log(Level.INFO, "[Metrics] " + invalidconfigurationexception.getMessage());
                }

                return true;
            }

            return this.configuration.getBoolean("opt-out", false);
        }
    }

    public void enable() throws IOException {
        Object object = this.optOutLock;

        synchronized (this.optOutLock) {
            if (this.isOptOut()) {
                this.configuration.set("opt-out", Boolean.valueOf(false));
                this.configuration.save(this.configurationFile);
            }

            if (this.task == null) {
                this.start();
            }

        }
    }

    public void disable() throws IOException {
        Object object = this.optOutLock;

        synchronized (this.optOutLock) {
            if (!this.isOptOut()) {
                this.configuration.set("opt-out", Boolean.valueOf(true));
                this.configuration.save(this.configurationFile);
            }

            if (this.task != null) {
                this.task.cancel();
                this.task = null;
            }

        }
    }

    public File getConfigFile() {
        return new File(new File((File) MinecraftServer.getServer().options.valueOf("plugins"), "PluginMetrics"), "config.yml");
    }

    private void postPlugin(boolean isPing) throws IOException {
        String pluginName = "Spigot";
        boolean onlineMode = Bukkit.getServer().getOnlineMode();
        String pluginVersion = Metrics.class.getPackage().getImplementationVersion() != null ? Metrics.class.getPackage().getImplementationVersion() : "unknown";
        String serverVersion = Bukkit.getVersion();
        int playersOnline = Bukkit.getServer().getOnlinePlayers().size();
        StringBuilder data = new StringBuilder();

        data.append(encode("guid")).append('=').append(encode(this.guid));
        encodeDataPair(data, "version", pluginVersion);
        encodeDataPair(data, "server", serverVersion);
        encodeDataPair(data, "players", Integer.toString(playersOnline));
        encodeDataPair(data, "revision", String.valueOf(6));
        String osname = System.getProperty("os.name");
        String osarch = System.getProperty("os.arch");
        String osversion = System.getProperty("os.version");
        String java_version = System.getProperty("java.version");
        int coreCount = Runtime.getRuntime().availableProcessors();

        if (osarch.equals("amd64")) {
            osarch = "x86_64";
        }

        encodeDataPair(data, "osname", osname);
        encodeDataPair(data, "osarch", osarch);
        encodeDataPair(data, "osversion", osversion);
        encodeDataPair(data, "cores", Integer.toString(coreCount));
        encodeDataPair(data, "online-mode", Boolean.toString(onlineMode));
        encodeDataPair(data, "java_version", java_version);
        if (isPing) {
            encodeDataPair(data, "ping", "true");
        }

        Set url = this.graphs;

        synchronized (this.graphs) {
            Iterator connection = this.graphs.iterator();

            while (true) {
                if (!connection.hasNext()) {
                    break;
                }

                Metrics.Graph writer = (Metrics.Graph) connection.next();
                Iterator response = writer.getPlotters().iterator();

                while (response.hasNext()) {
                    Metrics.Plotter reader = (Metrics.Plotter) response.next();
                    String key = String.format("C%s%s%s%s", new Object[] { "~~", writer.getName(), "~~", reader.getColumnName()});
                    String iter = Integer.toString(reader.getValue());

                    encodeDataPair(data, key, iter);
                }
            }
        }

        URL url1 = new URL("http://mcstats.org" + String.format("/report/%s", new Object[] { encode(pluginName)}));
        URLConnection connection1;

        if (this.isMineshafterPresent()) {
            connection1 = url1.openConnection(Proxy.NO_PROXY);
        } else {
            connection1 = url1.openConnection();
        }

        connection1.setDoOutput(true);
        OutputStreamWriter writer1 = new OutputStreamWriter(connection1.getOutputStream());

        writer1.write(data.toString());
        writer1.flush();
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
        String response1 = reader1.readLine();

        writer1.close();
        reader1.close();
        if (response1 != null && !response1.startsWith("ERR")) {
            if (response1.contains("OK This is your first update this hour")) {
                Set key1 = this.graphs;

                synchronized (this.graphs) {
                    Iterator iter1 = this.graphs.iterator();

                    while (iter1.hasNext()) {
                        Metrics.Graph graph = (Metrics.Graph) iter1.next();
                        Iterator iterator = graph.getPlotters().iterator();

                        while (iterator.hasNext()) {
                            Metrics.Plotter plotter = (Metrics.Plotter) iterator.next();

                            plotter.reset();
                        }
                    }
                }
            }

        } else {
            throw new IOException(response1);
        }
    }

    private boolean isMineshafterPresent() {
        try {
            Class.forName("mineshafter.MineServer");
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private static void encodeDataPair(StringBuilder buffer, String key, String value) throws UnsupportedEncodingException {
        buffer.append('&').append(encode(key)).append('=').append(encode(value));
    }

    private static String encode(String text) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, "UTF-8");
    }

    public static class Graph {

        private final String name;
        private final Set plotters;

        private Graph(String name) {
            this.plotters = new LinkedHashSet();
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void addPlotter(Metrics.Plotter plotter) {
            this.plotters.add(plotter);
        }

        public void removePlotter(Metrics.Plotter plotter) {
            this.plotters.remove(plotter);
        }

        public Set getPlotters() {
            return Collections.unmodifiableSet(this.plotters);
        }

        public int hashCode() {
            return this.name.hashCode();
        }

        public boolean equals(Object object) {
            if (!(object instanceof Metrics.Graph)) {
                return false;
            } else {
                Metrics.Graph graph = (Metrics.Graph) object;

                return graph.name.equals(this.name);
            }
        }

        protected void onOptOut() {}

        Graph(String s, Metrics.Graph metrics_graph) {
            this(s);
        }
    }

    public abstract static class Plotter {

        private final String name;

        public Plotter() {
            this("Default");
        }

        public Plotter(String name) {
            this.name = name;
        }

        public abstract int getValue();

        public String getColumnName() {
            return this.name;
        }

        public void reset() {}

        public int hashCode() {
            return this.getColumnName().hashCode();
        }

        public boolean equals(Object object) {
            if (!(object instanceof Metrics.Plotter)) {
                return false;
            } else {
                Metrics.Plotter plotter = (Metrics.Plotter) object;

                return plotter.name.equals(this.name) && plotter.getValue() == this.getValue();
            }
        }
    }
}
