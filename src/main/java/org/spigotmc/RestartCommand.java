package org.spigotmc;

import java.io.File;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RestartCommand extends Command {

    public RestartCommand(String name) {
        super(name);
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission("bukkit.command.restart");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (this.testPermission(sender)) {
            MinecraftServer.getServer().processQueue.add(new Runnable() {
                public void run() {
                    RestartCommand.restart();
                }
            });
        }

        return true;
    }

    public static void restart() {
        restart(new File(SpigotConfig.restartScript));
    }

    public static void restart(final File script) {
        AsyncCatcher.enabled = false;

        try {
            if (script.isFile()) {
                System.out.println("Attempting to restart with " + SpigotConfig.restartScript);
                WatchdogThread.doStop();
                Iterator iterator = MinecraftServer.getServer().getPlayerList().players.iterator();

                while (iterator.hasNext()) {
                    EntityPlayer ex = (EntityPlayer) iterator.next();

                    ex.playerConnection.disconnect(SpigotConfig.restartMessage);
                }

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException interruptedexception) {
                    ;
                }

                MinecraftServer.getServer().getServerConnection().b();

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException interruptedexception1) {
                    ;
                }

                try {
                    MinecraftServer.getServer().stop();
                } catch (Throwable throwable) {
                    ;
                }

                Thread ex1 = new Thread() {
                    public void run() {
                        try {
                            String e = System.getProperty("os.name").toLowerCase();

                            if (e.contains("win")) {
                                Runtime.getRuntime().exec("cmd /c start " + script.getPath());
                            } else {
                                Runtime.getRuntime().exec(new String[] { "sh", script.getPath()});
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    }
                };

                ex1.setDaemon(true);
                Runtime.getRuntime().addShutdownHook(ex1);
            } else {
                System.out.println("Startup script \'" + SpigotConfig.restartScript + "\' does not exist! Stopping server.");
            }

            System.exit(0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
