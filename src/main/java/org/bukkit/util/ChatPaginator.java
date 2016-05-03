package org.bukkit.util;

import java.util.LinkedList;
import org.bukkit.ChatColor;

public class ChatPaginator {

    public static final int GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH = 55;
    public static final int AVERAGE_CHAT_PAGE_WIDTH = 65;
    public static final int UNBOUNDED_PAGE_WIDTH = Integer.MAX_VALUE;
    public static final int OPEN_CHAT_PAGE_HEIGHT = 20;
    public static final int CLOSED_CHAT_PAGE_HEIGHT = 10;
    public static final int UNBOUNDED_PAGE_HEIGHT = Integer.MAX_VALUE;

    public static ChatPaginator.ChatPage paginate(String unpaginatedString, int pageNumber) {
        return paginate(unpaginatedString, pageNumber, 55, 10);
    }

    public static ChatPaginator.ChatPage paginate(String unpaginatedString, int pageNumber, int lineLength, int pageHeight) {
        String[] lines = wordWrap(unpaginatedString, lineLength);
        int totalPages = lines.length / pageHeight + (lines.length % pageHeight == 0 ? 0 : 1);
        int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;
        int from = (actualPageNumber - 1) * pageHeight;
        int to = from + pageHeight <= lines.length ? from + pageHeight : lines.length;
        String[] selectedLines = (String[]) Java15Compat.Arrays_copyOfRange(lines, from, to);

        return new ChatPaginator.ChatPage(selectedLines, actualPageNumber, totalPages);
    }

    public static String[] wordWrap(String rawString, int lineLength) {
        if (rawString == null) {
            return new String[] { ""};
        } else if (rawString.length() <= lineLength && !rawString.contains("\n")) {
            return new String[] { rawString};
        } else {
            char[] rawChars = (rawString + ' ').toCharArray();
            StringBuilder word = new StringBuilder();
            StringBuilder line = new StringBuilder();
            LinkedList lines = new LinkedList();
            int lineColorChars = 0;

            int i;
            String subLine;

            for (i = 0; i < rawChars.length; ++i) {
                char pLine = rawChars[i];

                if (pLine == 167) {
                    word.append(ChatColor.getByChar(rawChars[i + 1]));
                    lineColorChars += 2;
                    ++i;
                } else if (pLine != 32 && pLine != 10) {
                    word.append(pLine);
                } else {
                    String[] astring;
                    int i;
                    int color;

                    if (line.length() == 0 && word.length() > lineLength) {
                        i = (astring = word.toString().split("(?<=\\G.{" + lineLength + "})")).length;

                        for (color = 0; color < i; ++color) {
                            subLine = astring[color];
                            lines.add(subLine);
                        }
                    } else if (line.length() + word.length() - lineColorChars == lineLength) {
                        line.append(word);
                        lines.add(line.toString());
                        line = new StringBuilder();
                        lineColorChars = 0;
                    } else if (line.length() + 1 + word.length() - lineColorChars > lineLength) {
                        i = (astring = word.toString().split("(?<=\\G.{" + lineLength + "})")).length;

                        for (color = 0; color < i; ++color) {
                            subLine = astring[color];
                            lines.add(line.toString());
                            line = new StringBuilder(subLine);
                        }

                        lineColorChars = 0;
                    } else {
                        if (line.length() > 0) {
                            line.append(' ');
                        }

                        line.append(word);
                    }

                    word = new StringBuilder();
                    if (pLine == 10) {
                        lines.add(line.toString());
                        line = new StringBuilder();
                    }
                }
            }

            if (line.length() > 0) {
                lines.add(line.toString());
            }

            if (((String) lines.get(0)).length() == 0 || ((String) lines.get(0)).charAt(0) != 167) {
                lines.set(0, ChatColor.WHITE + (String) lines.get(0));
            }

            for (i = 1; i < lines.size(); ++i) {
                String s = (String) lines.get(i - 1);

                subLine = (String) lines.get(i);
                char c0 = s.charAt(s.lastIndexOf(167) + 1);

                if (subLine.length() == 0 || subLine.charAt(0) != 167) {
                    lines.set(i, ChatColor.getByChar(c0) + subLine);
                }
            }

            return (String[]) lines.toArray(new String[lines.size()]);
        }
    }

    public static class ChatPage {

        private String[] lines;
        private int pageNumber;
        private int totalPages;

        public ChatPage(String[] lines, int pageNumber, int totalPages) {
            this.lines = lines;
            this.pageNumber = pageNumber;
            this.totalPages = totalPages;
        }

        public int getPageNumber() {
            return this.pageNumber;
        }

        public int getTotalPages() {
            return this.totalPages;
        }

        public String[] getLines() {
            return this.lines;
        }
    }
}
