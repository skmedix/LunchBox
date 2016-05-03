package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;

public enum ChatColor {

    BLACK('0', 0) {;
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.BLACK;
        }
    }, DARK_BLUE('1', 1) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.DARK_BLUE;
    }
}, DARK_GREEN('2', 2) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.DARK_GREEN;
    }
}, DARK_AQUA('3', 3) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.DARK_AQUA;
    }
}, DARK_RED('4', 4) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.DARK_RED;
    }
}, DARK_PURPLE('5', 5) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.DARK_PURPLE;
    }
}, GOLD('6', 6) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.GOLD;
    }
}, GRAY('7', 7) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.GRAY;
    }
}, DARK_GRAY('8', 8) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.DARK_GRAY;
    }
}, BLUE('9', 9) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.BLUE;
    }
}, GREEN('a', 10) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.GREEN;
    }
}, AQUA('b', 11) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.AQUA;
    }
}, RED('c', 12) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.RED;
    }
}, LIGHT_PURPLE('d', 13) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;
    }
}, YELLOW('e', 14) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.YELLOW;
    }
}, WHITE('f', 15) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.WHITE;
    }
}, MAGIC('k', 16, true) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.MAGIC;
    }
}, BOLD('l', 17, true) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.BOLD;
    }
}, STRIKETHROUGH('m', 18, true) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.STRIKETHROUGH;
    }
}, UNDERLINE('n', 19, true) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.UNDERLINE;
    }
}, ITALIC('o', 20, true) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.ITALIC;
    }
}, RESET('r', 21) {;
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.RESET;
    }
};

    public static final char COLOR_CHAR = '§';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    private static final Map BY_ID = Maps.newHashMap();
    private static final Map BY_CHAR = Maps.newHashMap();

    static {
        ChatColor[] achatcolor;
        int i = (achatcolor = values()).length;

        for (int j = 0; j < i; ++j) {
            ChatColor color = achatcolor[j];

            ChatColor.BY_ID.put(Integer.valueOf(color.intCode), color);
            ChatColor.BY_CHAR.put(Character.valueOf(color.code), color);
        }

    }

    private ChatColor(char code, int intCode) {
        this(code, intCode, false);
    }

    private ChatColor(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[] { '§', code});
    }

    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.RESET;
    }

    public char getChar() {
        return this.code;
    }

    public String toString() {
        return this.toString;
    }

    public boolean isFormat() {
        return this.isFormat;
    }

    public boolean isColor() {
        return !this.isFormat && this != ChatColor.RESET;
    }

    public static ChatColor getByChar(char code) {
        return (ChatColor) ChatColor.BY_CHAR.get(Character.valueOf(code));
    }

    public static ChatColor getByChar(String code) {
        Validate.notNull(code, "Code cannot be null");
        Validate.isTrue(code.length() > 0, "Code must have at least one char");
        return (ChatColor) ChatColor.BY_CHAR.get(Character.valueOf(code.charAt(0)));
    }

    public static String stripColor(String input) {
        return input == null ? null : ChatColor.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    public static String getLastColors(String input) {
        String result = "";
        int length = input.length();

        for (int index = length - 1; index > -1; --index) {
            char section = input.charAt(index);

            if (section == 167 && index < length - 1) {
                char c = input.charAt(index + 1);
                ChatColor color = getByChar(c);

                if (color != null) {
                    result = color.toString() + result;
                    if (color.isColor() || color.equals(ChatColor.RESET)) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    ChatColor(char c0, int i, ChatColor chatcolor) {
        this(c0, i);
    }

    ChatColor(char c0, int i, boolean flag, ChatColor chatcolor) {
        this(c0, i, flag);
    }
}
