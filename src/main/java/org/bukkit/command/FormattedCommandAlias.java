package org.bukkit.command;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class FormattedCommandAlias extends Command {

    private final String[] formatStrings;

    public FormattedCommandAlias(String alias, String[] formatStrings) {
        super(alias);
        this.formatStrings = formatStrings;
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean result = false;
        ArrayList commands = new ArrayList();
        String[] astring = this.formatStrings;
        int i = this.formatStrings.length;

        String command;

        for (int j = 0; j < i; ++j) {
            command = astring[j];

            try {
                commands.add(this.buildCommand(command, args));
            } catch (Throwable throwable) {
                if (throwable instanceof IllegalArgumentException) {
                    sender.sendMessage(throwable.getMessage());
                } else {
                    sender.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
                }

                return false;
            }
        }

        for (Iterator iterator = commands.iterator(); iterator.hasNext(); result |= Bukkit.dispatchCommand(sender, command)) {
            command = (String) iterator.next();
        }

        return result;
    }

    private String buildCommand(String formatString, String[] args) {
        int index = formatString.indexOf("$");

        while (true) {
            while (index != -1) {
                int start = index;

                if (index <= 0 || formatString.charAt(index - 1) != 92) {
                    boolean required = false;

                    if (formatString.charAt(index + 1) == 36) {
                        required = true;
                        ++index;
                    }

                    ++index;

                    int argStart;

                    for (argStart = index; index < formatString.length() && inRange(formatString.charAt(index) - 48, 0, 9); ++index) {
                        ;
                    }

                    if (argStart == index) {
                        throw new IllegalArgumentException("Invalid replacement token");
                    }

                    int position = Integer.valueOf(formatString.substring(argStart, index)).intValue();

                    if (position == 0) {
                        throw new IllegalArgumentException("Invalid replacement token");
                    }

                    --position;
                    boolean rest = false;

                    if (index < formatString.length() && formatString.charAt(index) == 45) {
                        rest = true;
                        ++index;
                    }

                    if (required && position >= args.length) {
                        throw new IllegalArgumentException("Missing required argument " + (position + 1));
                    }

                    StringBuilder replacement = new StringBuilder();

                    if (rest && position < args.length) {
                        for (int i = position; i < args.length; ++i) {
                            if (i != position) {
                                replacement.append(' ');
                            }

                            replacement.append(args[i]);
                        }
                    } else if (position < args.length) {
                        replacement.append(args[position]);
                    }

                    formatString = formatString.substring(0, start) + replacement.toString() + formatString.substring(index);
                    index = start + replacement.length();
                    index = formatString.indexOf("$", index);
                } else {
                    formatString = formatString.substring(0, index - 1) + formatString.substring(index);
                    index = formatString.indexOf("$", index);
                }
            }

            return formatString;
        }
    }

    private static boolean inRange(int i, int j, int k) {
        return i >= j && i <= k;
    }
}
