package org.bukkit.craftbukkit.v1_8_R3;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class CraftCrashReport implements Callable {

    public Object call() throws Exception {
        StringWriter value = new StringWriter();

        try {
            value.append("\n   Running: ").append(Bukkit.getName()).append(" version ").append(Bukkit.getVersion()).append(" (Implementing API version ").append(Bukkit.getBukkitVersion()).append(") ").append(String.valueOf(MinecraftServer.getServer().isServerInOnlineMode()));
            value.append("\n   Plugins: {");
            Plugin[] aplugin;
            int i = (aplugin = Bukkit.getPluginManager().getPlugins()).length;

            for (int j = 0; j < i; ++j) {
                Plugin t = aplugin[j];
                PluginDescriptionFile description = t.getDescription();

                value.append(' ').append(description.getFullName()).append(' ').append(description.getMain()).append(' ').append(Arrays.toString(description.getAuthors().toArray())).append(',');
            }

            value.append("}\n   Warnings: ").append(Bukkit.getWarningState().name());
            //value.append("\n   Reload Count: ").append(String.valueOf(MinecraftServer.getServer().reloadCount));//LunchBox - Comment out because reload command will be disabled.
            value.append("\n   Threads: {");
            Iterator iterator = Thread.getAllStackTraces().entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                value.append(' ').append(((Thread) entry.getKey()).getState().name()).append(' ').append(((Thread) entry.getKey()).getName()).append(": ").append(Arrays.toString((Object[]) entry.getValue())).append(',');
            }

            value.append("}\n   ").append(Bukkit.getScheduler().toString());
        } catch (Throwable throwable) {
            value.append("\n   Failed to handle CraftCrashReport:\n");
            PrintWriter writer = new PrintWriter(value);

            throwable.printStackTrace(writer);
            writer.flush();
        }

        return value.toString();
    }
}
