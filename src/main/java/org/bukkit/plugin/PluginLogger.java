package org.bukkit.plugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PluginLogger extends Logger {

    private String pluginName;

    public PluginLogger(Plugin context) {
        super(context.getClass().getCanonicalName(), (String) null);
        String prefix = context.getDescription().getPrefix();

        this.pluginName = prefix != null ? "[" + prefix + "] " : "[" + context.getDescription().getName() + "] ";
        this.setParent(context.getServer().getLogger());
        this.setLevel(Level.ALL);
    }

    public void log(LogRecord logRecord) {
        logRecord.setMessage(this.pluginName + logRecord.getMessage());
        super.log(logRecord);
    }
}
