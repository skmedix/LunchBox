package org.bukkit.craftbukkit.libs.jline.internal;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TerminalLineSettings {

    public static final String JLINE_STTY = "org.bukkit.craftbukkit.libs.jline.stty";
    public static final String DEFAULT_STTY = "stty";
    public static final String JLINE_SH = "org.bukkit.craftbukkit.libs.jline.sh";
    public static final String DEFAULT_SH = "sh";
    private String sttyCommand = Configuration.getString("org.bukkit.craftbukkit.libs.jline.stty", "stty");
    private String shCommand = Configuration.getString("org.bukkit.craftbukkit.libs.jline.sh", "sh");
    private String config = this.get("-a");
    private String initialConfig = this.get("-g").trim();
    private long configLastFetched = System.currentTimeMillis();

    public TerminalLineSettings() throws IOException, InterruptedException {
        Log.debug(new Object[] { "Config: ", this.config});
        if (this.config.length() == 0) {
            throw new IOException(MessageFormat.format("Unrecognized stty code: {0}", new Object[] { this.config}));
        }
    }

    public String getConfig() {
        return this.config;
    }

    public void restore() throws IOException, InterruptedException {
        this.set(this.initialConfig);
    }

    public String get(String args) throws IOException, InterruptedException {
        return this.stty(args);
    }

    public void set(String args) throws IOException, InterruptedException {
        this.stty(args);
    }

    public int getProperty(String name) {
        Preconditions.checkNotNull(name);
        long currentTime = System.currentTimeMillis();

        try {
            if (this.config == null || currentTime - this.configLastFetched > 1000L) {
                this.config = this.get("-a");
            }
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            Log.debug(new Object[] { "Failed to query stty ", name, "\n", exception});
            if (this.config == null) {
                return -1;
            }
        }

        if (currentTime - this.configLastFetched > 1000L) {
            this.configLastFetched = currentTime;
        }

        return getProperty(name, this.config);
    }

    protected static int getProperty(String name, String stty) {
        Pattern pattern = Pattern.compile(name + "\\s+=\\s+(.*?)[;\\n\\r]");
        Matcher matcher = pattern.matcher(stty);

        if (!matcher.find()) {
            pattern = Pattern.compile(name + "\\s+([^;]*)[;\\n\\r]");
            matcher = pattern.matcher(stty);
            if (!matcher.find()) {
                pattern = Pattern.compile("(\\S*)\\s+" + name);
                matcher = pattern.matcher(stty);
                if (!matcher.find()) {
                    return -1;
                }
            }
        }

        return parseControlChar(matcher.group(1));
    }

    private static int parseControlChar(String str) {
        return "<undef>".equals(str) ? -1 : (str.charAt(0) == 48 ? Integer.parseInt(str, 8) : (str.charAt(0) >= 49 && str.charAt(0) <= 57 ? Integer.parseInt(str, 10) : (str.charAt(0) == 94 ? (str.charAt(1) == 63 ? 127 : str.charAt(1) - 64) : (str.charAt(0) == 77 && str.charAt(1) == 45 ? (str.charAt(2) == 94 ? (str.charAt(3) == 63 ? 255 : str.charAt(3) - 64 + 128) : str.charAt(2) + 128) : str.charAt(0)))));
    }

    private String stty(String args) throws IOException, InterruptedException {
        Preconditions.checkNotNull(args);
        return this.exec(String.format("%s %s < /dev/tty", new Object[] { this.sttyCommand, args}));
    }

    private String exec(String cmd) throws IOException, InterruptedException {
        Preconditions.checkNotNull(cmd);
        return this.exec(new String[] { this.shCommand, "-c", cmd});
    }

    private String exec(String... cmd) throws IOException, InterruptedException {
        Preconditions.checkNotNull(cmd);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Log.trace(new Object[] { "Running: ", cmd});
        Process p = Runtime.getRuntime().exec(cmd);
        InputStream in = null;
        InputStream err = null;
        OutputStream out = null;

        try {
            in = p.getInputStream();

            int result;

            while ((result = in.read()) != -1) {
                bout.write(result);
            }

            err = p.getErrorStream();

            while (true) {
                if ((result = err.read()) == -1) {
                    out = p.getOutputStream();
                    p.waitFor();
                    break;
                }

                bout.write(result);
            }
        } finally {
            close(new Closeable[] { in, out, err});
        }

        String result1 = bout.toString();

        Log.trace(new Object[] { "Result: ", result1});
        return result1;
    }

    private static void close(Closeable... closeables) {
        Closeable[] arr$ = closeables;
        int len$ = closeables.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Closeable c = arr$[i$];

            try {
                c.close();
            } catch (Exception exception) {
                ;
            }
        }

    }
}
