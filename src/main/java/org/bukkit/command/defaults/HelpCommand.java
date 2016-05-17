package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.util.ChatPaginator;

public class HelpCommand extends VanillaCommand {

    public HelpCommand() {
        super("help");
        this.description = "Shows the help menu";
        this.usageMessage = "/help <pageNumber>\n/help <topic>\n/help <topic> <pageNumber>";
        this.setAliases(Arrays.asList(new String[] { "?"}));
        this.setPermission("bukkit.command.help");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            String command;
            int pageNumber;

            if (args.length == 0) {
                command = "";
                pageNumber = 1;
            } else if (NumberUtils.isDigits(args[args.length - 1])) {
                command = StringUtils.join(ArrayUtils.subarray((Object[]) args, 0, args.length - 1), " ");

                try {
                    pageNumber = NumberUtils.createInteger(args[args.length - 1]).intValue();
                } catch (NumberFormatException numberformatexception) {
                    pageNumber = 1;
                }

                if (pageNumber <= 0) {
                    pageNumber = 1;
                }
            } else {
                command = StringUtils.join((Object[]) args, " ");
                pageNumber = 1;
            }

            int pageHeight;
            int pageWidth;

            if (sender instanceof ConsoleCommandSender) {
                pageHeight = Integer.MAX_VALUE;
                pageWidth = Integer.MAX_VALUE;
            } else {
                pageHeight = 9;
                pageWidth = 55;
            }

            HelpMap helpMap = Bukkit.getServer().getHelpMap();
            HelpTopic topic = helpMap.getHelpTopic(command);

            if (topic == null) {
                topic = helpMap.getHelpTopic("/" + command);
            }

            if (topic == null) {
                topic = this.findPossibleMatches(command);
            }

            if (topic != null && topic.canSee(sender)) {
                ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);
                StringBuilder header = new StringBuilder();

                header.append(ChatColor.YELLOW);
                header.append("--------- ");
                header.append(ChatColor.WHITE);
                header.append("Help: ");
                header.append(topic.getName());
                header.append(" ");
                if (page.getTotalPages() > 1) {
                    header.append("(");
                    header.append(page.getPageNumber());
                    header.append("/");
                    header.append(page.getTotalPages());
                    header.append(") ");
                }

                header.append(ChatColor.YELLOW);

                for (int i = header.length(); i < 55; ++i) {
                    header.append("-");
                }

                sender.sendMessage(header.toString());
                sender.sendMessage(page.getLines());
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "No help for " + command);
                return true;
            }
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        if (args.length == 1) {
            ArrayList matchedTopics = new ArrayList();
            String searchString = args[0];
            Iterator iterator = Bukkit.getServer().getHelpMap().getHelpTopics().iterator();

            while (iterator.hasNext()) {
                HelpTopic topic = (HelpTopic) iterator.next();
                String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();

                if (trimmedTopic.startsWith(searchString)) {
                    matchedTopics.add(trimmedTopic);
                }
            }

            return matchedTopics;
        } else {
            return ImmutableList.of();
        }
    }

    protected HelpTopic findPossibleMatches(String searchString) {
        int maxDistance = searchString.length() / 5 + 3;
        TreeSet possibleMatches = new TreeSet(HelpTopicComparator.helpTopicComparatorInstance());

        if (searchString.startsWith("/")) {
            searchString = searchString.substring(1);
        }

        Iterator iterator = Bukkit.getServer().getHelpMap().getHelpTopics().iterator();

        while (iterator.hasNext()) {
            HelpTopic topic = (HelpTopic) iterator.next();
            String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();

            if (trimmedTopic.length() >= searchString.length() && Character.toLowerCase(trimmedTopic.charAt(0)) == Character.toLowerCase(searchString.charAt(0)) && damerauLevenshteinDistance(searchString, trimmedTopic.substring(0, searchString.length())) < maxDistance) {
                possibleMatches.add(topic);
            }
        }

        if (possibleMatches.size() > 0) {
            return new IndexHelpTopic("Search", (String) null, (String) null, possibleMatches, "Search for: " + searchString);
        } else {
            return null;
        }
    }

    protected static int damerauLevenshteinDistance(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        } else if (s1 != null && s2 == null) {
            return s1.length();
        } else if (s1 == null && s2 != null) {
            return s2.length();
        } else {
            int s1Len = s1.length();
            int s2Len = s2.length();
            int[][] H = new int[s1Len + 2][s2Len + 2];
            int INF = s1Len + s2Len;

            H[0][0] = INF;

            int sd;

            for (sd = 0; sd <= s1Len; ++sd) {
                H[sd + 1][1] = sd;
                H[sd + 1][0] = INF;
            }

            for (sd = 0; sd <= s2Len; ++sd) {
                H[1][sd + 1] = sd;
                H[0][sd + 1] = INF;
            }

            HashMap hashmap = new HashMap();
            char[] i1;
            int j = (i1 = (s1 + s2).toCharArray()).length;

            int DB;

            for (DB = 0; DB < j; ++DB) {
                char i = i1[DB];

                if (!hashmap.containsKey(Character.valueOf(i))) {
                    hashmap.put(Character.valueOf(i), Integer.valueOf(0));
                }
            }

            for (int i = 1; i <= s1Len; ++i) {
                DB = 0;

                for (j = 1; j <= s2Len; ++j) {
                    int j = ((Integer) hashmap.get(Character.valueOf(s2.charAt(j - 1)))).intValue();

                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        H[i + 1][j + 1] = H[i][j];
                        DB = j;
                    } else {
                        H[i + 1][j + 1] = Math.min(H[i][j], Math.min(H[i + 1][j], H[i][j + 1])) + 1;
                    }

                    H[i + 1][j + 1] = Math.min(H[i + 1][j + 1], H[j][DB] + (i - j - 1) + 1 + (j - DB - 1));
                }

                hashmap.put(Character.valueOf(s1.charAt(i - 1)), Integer.valueOf(i));
            }

            return H[s1Len + 1][s2Len + 1];
        }
    }
}
