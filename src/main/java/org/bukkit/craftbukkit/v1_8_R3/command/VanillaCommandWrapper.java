package org.bukkit.craftbukkit.v1_8_R3.command;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.*;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.Validate;
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

import javax.activation.CommandObject;

import static net.minecraft.command.CommandResultStats.Type.AFFECTED_ENTITIES;
import static net.minecraft.command.CommandResultStats.Type.SUCCESS_COUNT;

public final class VanillaCommandWrapper extends VanillaCommand {

    protected final ICommand vanillaCommand;
    public static CommandSender lastSender = null;

    public VanillaCommandWrapper(ICommand vanillaCommand) {
        super(vanillaCommand.getCommandName());
        this.vanillaCommand = vanillaCommand;
    }

    public VanillaCommandWrapper(ICommand vanillaCommand, String usage) {
        super(vanillaCommand.getCommandName());
        this.vanillaCommand = vanillaCommand;
        this.description = "A Mojang provided command.";
        this.usageMessage = usage;
        this.setPermission("minecraft.command." + vanillaCommand.getCommandName());
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            ICommandSender icommandlistener = this.getListener(sender);

            this.dispatchVanillaCommand(sender, icommandlistener, args);
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return this.vanillaCommand.addTabCompletionOptions(this.getListener(sender), args, new BlockPos(0, 0, 0));
    }

    public final int dispatchVanillaCommand(CommandSender bSender, ICommandSender icommandlistener, String[] as) {
        int i = this.getPlayerListSize(as);
        int j = 0;
        WorldServer[] prev = MinecraftServer.getServer().worldServers;
        MinecraftServer server = MinecraftServer.getServer();

        server.worldServers = new WorldServer[server.worldServers.length];
        server.worldServers[0] = (WorldServer) icommandlistener.getEntityWorld();
        int bpos = 0;

        for (int throwable = 1; throwable < server.worldServers.length; ++throwable) {
            WorldServer chatmessage3 = server.worldServers[bpos++];

            if (server.worldServers[0] == chatmessage3) {
                --throwable;
            } else {
                server.worldServers[throwable] = chatmessage3;
            }
        }

        try {
            ChatComponentTranslation chatmessage;
            try {
                if (this.vanillaCommand.canCommandSenderUseCommand(icommandlistener)) {
                    if (i > -1) {
                        List list = PlayerSelector.matchEntities(icommandlistener, as[i], Entity.class);
                        String s = as[i];

                        icommandlistener.setCommandStat(AFFECTED_ENTITIES, list.size());
                        Iterator iterator = list.iterator();

                        while (iterator.hasNext()) {
                            Entity entity = (Entity) iterator.next();
                            CommandSender oldSender = VanillaCommandWrapper.lastSender;

                            VanillaCommandWrapper.lastSender = bSender;

                            try {
                                as[i] = entity.getUniqueID().toString();
                                this.vanillaCommand.processCommand(icommandlistener, as);
                                ++j;
                            } catch (CommandException exceptionusage) {
                                net.minecraft.util.ChatComponentTranslation chatmessage4 = new net.minecraft.util.ChatComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorObjects());

                                chatmessage4.getChatStyle().setColor(EnumChatFormatting.RED);
                                icommandlistener.addChatMessage(chatmessage4);
                            } finally {
                                VanillaCommandWrapper.lastSender = oldSender;
                            }
                        }

                        as[i] = s;
                    } else {
                        icommandlistener.setCommandStat(AFFECTED_ENTITIES, 1);
                        this.vanillaCommand.processCommand(icommandlistener, as);
                        ++j;
                    }
                } else {
                    ChatComponentTranslation chatmessage1 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);

                    chatmessage1.getChatStyle().setColor(EnumChatFormatting.RED);
                    icommandlistener.addChatMessage(chatmessage1);
                }
            } catch (WrongUsageException exceptionusage1) {
                chatmessage = new ChatComponentTranslation("commands.generic.usage", new Object[] { new ChatComponentTranslation(exceptionusage1.getMessage(), exceptionusage1.getErrorObjects())});
                chatmessage.getChatStyle().setColor(EnumChatFormatting.RED);
                icommandlistener.addChatMessage(chatmessage);
            } catch (CommandException commandexception1) {
                //CommandAbstract.a(icommandlistener, this.vanillaCommand, 1, commandexception1.getMessage(), commandexception1.getArgs()); LunchBox remove for now todo
            } catch (Throwable throwable) {
                chatmessage = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
                chatmessage.getChatStyle().setColor(EnumChatFormatting.RED);
                icommandlistener.addChatMessage(chatmessage);
                if (icommandlistener.getCommandSenderEntity() instanceof EntityMinecartCommandBlock) {
                    MinecraftServer.getServer().logWarning(String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", new Object[] { Integer.valueOf(icommandlistener.getPosition().getX()), Integer.valueOf(icommandlistener.getPosition().getY()), Integer.valueOf(icommandlistener.getPosition().getZ())}));
                } else if (icommandlistener instanceof CommandBlockLogic) {
                    CommandBlockLogic listener = (CommandBlockLogic) icommandlistener;

                    MinecraftServer.getServer().logWarning(String.format("CommandBlock at (%d,%d,%d) failed to handle command", new Object[] { Integer.valueOf(listener.getPosition().getX()), Integer.valueOf(listener.getPosition().getY()), Integer.valueOf(listener.getPosition().getZ())}));
                } else {
                    MinecraftServer.getServer().logWarning(String.format("Unknown CommandBlock failed to handle command", new Object[0]));
                }
            }
        } finally {
            MinecraftServer.getServer().worldServers = prev;
        }

        icommandlistener.setCommandStat(SUCCESS_COUNT, j);
        return j;
    }

    private ICommandSender getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        } else if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        } else if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlockLogic();
        } else if (sender instanceof RemoteConsoleCommandSender) {
            return RConConsoleSource.getInstance();
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
            if (this.vanillaCommand.isUsernameIndex(as, i) && PlayerSelector.hasArguments(as[i])) {
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
