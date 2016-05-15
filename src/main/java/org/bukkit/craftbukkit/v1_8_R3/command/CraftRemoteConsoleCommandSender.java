package org.bukkit.craftbukkit.v1_8_R3.command;

import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.bukkit.command.RemoteConsoleCommandSender;

public class CraftRemoteConsoleCommandSender extends ServerCommandSender implements RemoteConsoleCommandSender {

    public void sendMessage(String message) {
        RConConsoleSource.getInstance().addChatMessage((IChatComponent) (new ChatComponentText(message + "\n")));
    }

    public void sendMessage(String[] messages) {
        String[] astring = messages;
        int i = messages.length;

        for (int j = 0; j < i; ++j) {
            String message = astring[j];

            this.sendMessage(message);
        }

    }

    public String getName() {
        return "Rcon";
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
    }
}
