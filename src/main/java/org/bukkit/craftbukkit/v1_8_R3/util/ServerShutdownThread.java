package org.bukkit.craftbukkit.v1_8_R3.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.world.MinecraftException;

public class ServerShutdownThread extends Thread {

    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    public void run() {
        try {
            this.server.stopServer();
        } catch (MinecraftException exceptionworldconflict) {
            exceptionworldconflict.printStackTrace();
        } finally {
            try {
                this.server.reader.getTerminal().restore();//todo
            } catch (Exception exception) {
                ;
            }

        }

    }
}
