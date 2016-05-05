package org.bukkit.craftbukkit.v1_8_R3.command;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.CommandAbstract;
import net.minecraft.server.v1_8_R3.CommandBlockListenerAbstract;
import net.minecraft.server.v1_8_R3.CommandException;
import net.minecraft.server.v1_8_R3.CommandObjectiveExecutor;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityMinecartCommandBlock;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import net.minecraft.server.v1_8_R3.ExceptionUsage;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerSelector;
import net.minecraft.server.v1_8_R3.RemoteControlCommandListener;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper extends VanillaCommand {

    protected final CommandAbstract vanillaCommand;
    public static CommandSender lastSender = null;

    public VanillaCommandWrapper(CommandAbstract vanillaCommand) {
        super(vanillaCommand.getCommand());
        this.vanillaCommand = vanillaCommand;
    }

    public VanillaCommandWrapper(ICommand vanillaCommand, String usage) {
        super(vanillaCommand.getCommand());
        this.vanillaCommand = vanillaCommand;
        this.description = "A Mojang provided command.";
        this.usageMessage = usage;
        this.setPermission("minecraft.command." + vanillaCommand.getCommand());
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            ICommandListener icommandlistener = this.getListener(sender);

            this.dispatchVanillaCommand(sender, icommandlistener, args);
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return this.vanillaCommand.tabComplete(this.getListener(sender), args, new BlockPosition(0, 0, 0));
    }

    public final int dispatchVanillaCommand(CommandSender bSender, ICommandListener icommandlistener, String[] as) {
        int i = this.getPlayerListSize(as);
        int j = 0;
        WorldServer[] prev = MinecraftServer.getServer().worldServer;
        MinecraftServer server = MinecraftServer.getServer();

        server.worldServer = new WorldServer[server.worlds.size()];
        server.worldServer[0] = (WorldServer) icommandlistener.getWorld();
        int bpos = 0;

        for (int throwable = 1; throwable < server.worldServer.length; ++throwable) {
            WorldServer chatmessage3 = (WorldServer) server.worlds.get(bpos++);

            if (server.worldServer[0] == chatmessage3) {
                --throwable;
            } else {
                server.worldServer[throwable] = chatmessage3;
            }
        }

        try {
            ChatMessage chatmessage;

            try {
                if (this.vanillaCommand.canUse(icommandlistener)) {
                    if (i > -1) {
                        List list = PlayerSelector.getPlayers(icommandlistener, as[i], Entity.class);
                        String s = as[i];

                        icommandlistener.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, list.size());
                        Iterator iterator = list.iterator();

                        while (iterator.hasNext()) {
                            Entity entity = (Entity) iterator.next();
                            CommandSender oldSender = VanillaCommandWrapper.lastSender;

                            VanillaCommandWrapper.lastSender = bSender;

                            try {
                                as[i] = entity.getUniqueID().toString();
                                this.vanillaCommand.execute(icommandlistener, as);
                                ++j;
                            } catch (ExceptionUsage exceptionusage) {
                                ChatMessage chatmessage = new ChatMessage("commands.generic.usage", new Object[] { new ChatMessage(exceptionusage.getMessage(), exceptionusage.getArgs())});

                                chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
                                icommandlistener.sendMessage(chatmessage);
                            } catch (CommandException commandexception) {
                                CommandAbstract.a(icommandlistener, this.vanillaCommand, 1, commandexception.getMessage(), commandexception.getArgs());
                            } finally {
                                VanillaCommandWrapper.lastSender = oldSender;
                            }
                        }

                        as[i] = s;
                    } else {
                        icommandlistener.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, 1);
                        this.vanillaCommand.execute(icommandlistener, as);
                        ++j;
                    }
                } else {
                    ChatMessage chatmessage1 = new ChatMessage("commands.generic.permission", new Object[0]);

                    chatmessage1.getChatModifier().setColor(EnumChatFormat.RED);
                    icommandlistener.sendMessage(chatmessage1);
                }
            } catch (ExceptionUsage exceptionusage1) {
                chatmessage = new ChatMessage("commands.generic.usage", new Object[] { new ChatMessage(exceptionusage1.getMessage(), exceptionusage1.getArgs())});
                chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
                icommandlistener.sendMessage(chatmessage);
            } catch (CommandException commandexception1) {
                CommandAbstract.a(icommandlistener, this.vanillaCommand, 1, commandexception1.getMessage(), commandexception1.getArgs());
            } catch (Throwable throwable) {
                chatmessage = new ChatMessage("commands.generic.exception", new Object[0]);
                chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
                icommandlistener.sendMessage(chatmessage);
                if (icommandlistener.f() instanceof EntityMinecartCommandBlock) {
                    MinecraftServer.LOGGER.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", new Object[] { Integer.valueOf(icommandlistener.getChunkCoordinates().getX()), Integer.valueOf(icommandlistener.getChunkCoordinates().getY()), Integer.valueOf(icommandlistener.getChunkCoordinates().getZ())}), throwable);
                } else if (icommandlistener instanceof CommandBlockListenerAbstract) {
                    CommandBlockListenerAbstract listener = (CommandBlockListenerAbstract) icommandlistener;

                    MinecraftServer.LOGGER.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", new Object[] { Integer.valueOf(listener.getChunkCoordinates().getX()), Integer.valueOf(listener.getChunkCoordinates().getY()), Integer.valueOf(listener.getChunkCoordinates().getZ())}), throwable);
                } else {
                    MinecraftServer.LOGGER.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command", new Object[0]), throwable);
                }
            }
        } finally {
            MinecraftServer.getServer().worldServer = prev;
        }

        icommandlistener.a(CommandObjectiveExecutor.EnumCommandResult.SUCCESS_COUNT, j);
        return j;
    }

    private ICommandListener getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        } else if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        } else if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock();
        } else if (sender instanceof RemoteConsoleCommandSender) {
            return RemoteControlCommandListener.getInstance();
        } else if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        } else if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        } else {
            throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
        }
    }

    private int getPlayerListSize(String[] as) {
        for (int i = 0; i < as.length; ++i) {
            if (this.vanillaCommand.isListStart(as, i) && PlayerSelector.isList(as[i])) {
                return i;
            }
        }

        return -1;
    }

    public static String[] dropFirstArgument(String[] as) {
        String[] as1 = new String[as.length - 1];

        for (int i = 1; i < as.length; ++i) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
