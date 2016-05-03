package org.bukkit.craftbukkit.v1_8_R3.util;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.server.v1_8_R3.ChatClickable;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.ChatModifier;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;

public final class CraftChatMessage {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))");

    public static IChatBaseComponent[] fromString(String message) {
        return fromString(message, false);
    }

    public static IChatBaseComponent[] fromString(String message, boolean keepNewlines) {
        return (new CraftChatMessage.StringMessage(message, keepNewlines, (CraftChatMessage.StringMessage) null)).getOutput();
    }

    public static String fromComponent(IChatBaseComponent component) {
        return fromComponent(component, EnumChatFormat.BLACK);
    }

    public static String fromComponent(IChatBaseComponent component, EnumChatFormat defaultColor) {
        if (component == null) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();

            IChatBaseComponent c;

            for (Iterator iterator = component.iterator(); iterator.hasNext(); out.append(c.getText())) {
                c = (IChatBaseComponent) iterator.next();
                ChatModifier modi = c.getChatModifier();

                out.append(modi.getColor() == null ? defaultColor : modi.getColor());
                if (modi.isBold()) {
                    out.append(EnumChatFormat.BOLD);
                }

                if (modi.isItalic()) {
                    out.append(EnumChatFormat.ITALIC);
                }

                if (modi.isUnderlined()) {
                    out.append(EnumChatFormat.UNDERLINE);
                }

                if (modi.isStrikethrough()) {
                    out.append(EnumChatFormat.STRIKETHROUGH);
                }

                if (modi.isRandom()) {
                    out.append(EnumChatFormat.OBFUSCATED);
                }
            }

            return out.toString().replaceFirst("^(" + defaultColor + ")*", "");
        }
    }

    public static IChatBaseComponent fixComponent(IChatBaseComponent component) {
        Matcher matcher = CraftChatMessage.LINK_PATTERN.matcher("");

        return fixComponent(component, matcher);
    }

    private static IChatBaseComponent fixComponent(IChatBaseComponent component, Matcher matcher) {
        if (component instanceof ChatComponentText) {
            ChatComponentText extras = (ChatComponentText) component;
            String subs = extras.g();

            if (matcher.reset(subs).find()) {
                matcher.reset();
                ChatModifier i = extras.getChatModifier() != null ? extras.getChatModifier() : new ChatModifier();
                ArrayList comp = new ArrayList();
                ArrayList c = new ArrayList(extras.a());

                component = extras = new ChatComponentText("");

                int pos;

                for (pos = 0; matcher.find(); pos = matcher.end()) {
                    String prev = matcher.group();

                    if (!prev.startsWith("http://") && !prev.startsWith("https://")) {
                        prev = "http://" + prev;
                    }

                    ChatComponentText c1 = new ChatComponentText(subs.substring(pos, matcher.start()));

                    c1.setChatModifier(i);
                    comp.add(c1);
                    ChatComponentText link = new ChatComponentText(matcher.group());
                    ChatModifier linkModi = i.clone();

                    linkModi.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.OPEN_URL, prev));
                    link.setChatModifier(linkModi);
                    comp.add(link);
                }

                ChatComponentText chatcomponenttext = new ChatComponentText(subs.substring(pos));

                chatcomponenttext.setChatModifier(i);
                comp.add(chatcomponenttext);
                comp.addAll(c);
                Iterator iterator = comp.iterator();

                while (iterator.hasNext()) {
                    IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

                    extras.addSibling(ichatbasecomponent);
                }
            }
        }

        List list = ((IChatBaseComponent) component).a();

        for (int i = 0; i < list.size(); ++i) {
            IChatBaseComponent ichatbasecomponent1 = (IChatBaseComponent) list.get(i);

            if (ichatbasecomponent1.getChatModifier() != null && ichatbasecomponent1.getChatModifier().h() == null) {
                list.set(i, fixComponent(ichatbasecomponent1, matcher));
            }
        }

        if (component instanceof ChatMessage) {
            Object[] aobject = ((ChatMessage) component).j();

            for (int j = 0; j < aobject.length; ++j) {
                Object object = aobject[j];

                if (object instanceof IChatBaseComponent) {
                    IChatBaseComponent ichatbasecomponent2 = (IChatBaseComponent) object;

                    if (ichatbasecomponent2.getChatModifier() != null && ichatbasecomponent2.getChatModifier().h() == null) {
                        aobject[j] = fixComponent(ichatbasecomponent2, matcher);
                    }
                } else if (object instanceof String && matcher.reset((String) object).find()) {
                    aobject[j] = fixComponent(new ChatComponentText((String) object), matcher);
                }
            }
        }

        return (IChatBaseComponent) component;
    }

    private static class StringMessage {

        private static final Map formatMap;
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + '§' + " \\n]|$))))", 2);
        private final List list;
        private IChatBaseComponent currentChatComponent;
        private ChatModifier modifier;
        private final IChatBaseComponent[] output;
        private int currentIndex;
        private final String message;
        private static int[] $SWITCH_TABLE$net$minecraft$server$EnumChatFormat;

        static {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            EnumChatFormat[] aenumchatformat;
            int i = (aenumchatformat = EnumChatFormat.values()).length;

            for (int j = 0; j < i; ++j) {
                EnumChatFormat format = aenumchatformat[j];

                builder.put(Character.valueOf(Character.toLowerCase(format.toString().charAt(1))), format);
            }

            formatMap = builder.build();
        }

        private StringMessage(String message, boolean keepNewlines) {
            this.list = new ArrayList();
            this.currentChatComponent = new ChatComponentText("");
            this.modifier = new ChatModifier();
            this.message = message;
            if (message == null) {
                this.output = new IChatBaseComponent[] { this.currentChatComponent};
            } else {
                this.list.add(this.currentChatComponent);
                Matcher matcher = CraftChatMessage.StringMessage.INCREMENTAL_PATTERN.matcher(message);

                int groupId;

                for (String match = null; matcher.find(); this.currentIndex = matcher.end(groupId)) {
                    groupId = 0;

                    do {
                        ++groupId;
                    } while ((match = matcher.group(groupId)) == null);

                    this.appendNewComponent(matcher.start(groupId));
                    switch (groupId) {
                    case 1:
                        EnumChatFormat format = (EnumChatFormat) CraftChatMessage.StringMessage.formatMap.get(Character.valueOf(match.toLowerCase().charAt(1)));

                        if (format == EnumChatFormat.RESET) {
                            this.modifier = new ChatModifier();
                        } else if (format.isFormat()) {
                            switch ($SWITCH_TABLE$net$minecraft$server$EnumChatFormat()[format.ordinal()]) {
                            case 17:
                                this.modifier.setRandom(Boolean.TRUE);
                                break;

                            case 18:
                                this.modifier.setBold(Boolean.TRUE);
                                break;

                            case 19:
                                this.modifier.setStrikethrough(Boolean.TRUE);
                                break;

                            case 20:
                                this.modifier.setUnderline(Boolean.TRUE);
                                break;

                            case 21:
                                this.modifier.setItalic(Boolean.TRUE);
                                break;

                            default:
                                throw new AssertionError("Unexpected message format");
                            }
                        } else {
                            this.modifier = (new ChatModifier()).setColor(format);
                        }
                        break;

                    case 2:
                        if (keepNewlines) {
                            this.currentChatComponent.addSibling(new ChatComponentText("\n"));
                        } else {
                            this.currentChatComponent = null;
                        }
                        break;

                    case 3:
                        if (!match.startsWith("http://") && !match.startsWith("https://")) {
                            match = "http://" + match;
                        }

                        this.modifier.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.OPEN_URL, match));
                        this.appendNewComponent(matcher.end(groupId));
                        this.modifier.setChatClickable((ChatClickable) null);
                    }
                }

                if (this.currentIndex < message.length()) {
                    this.appendNewComponent(message.length());
                }

                this.output = (IChatBaseComponent[]) this.list.toArray(new IChatBaseComponent[this.list.size()]);
            }
        }

        private void appendNewComponent(int index) {
            if (index > this.currentIndex) {
                IChatBaseComponent addition = (new ChatComponentText(this.message.substring(this.currentIndex, index))).setChatModifier(this.modifier);

                this.currentIndex = index;
                this.modifier = this.modifier.clone();
                if (this.currentChatComponent == null) {
                    this.currentChatComponent = new ChatComponentText("");
                    this.list.add(this.currentChatComponent);
                }

                this.currentChatComponent.addSibling(addition);
            }
        }

        private IChatBaseComponent[] getOutput() {
            return this.output;
        }

        static int[] $SWITCH_TABLE$net$minecraft$server$EnumChatFormat() {
            int[] aint = CraftChatMessage.StringMessage.$SWITCH_TABLE$net$minecraft$server$EnumChatFormat;

            if (CraftChatMessage.StringMessage.$SWITCH_TABLE$net$minecraft$server$EnumChatFormat != null) {
                return aint;
            } else {
                int[] aint1 = new int[EnumChatFormat.values().length];

                try {
                    aint1[EnumChatFormat.AQUA.ordinal()] = 12;
                } catch (NoSuchFieldError nosuchfielderror) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.BLACK.ordinal()] = 1;
                } catch (NoSuchFieldError nosuchfielderror1) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.BLUE.ordinal()] = 10;
                } catch (NoSuchFieldError nosuchfielderror2) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.BOLD.ordinal()] = 18;
                } catch (NoSuchFieldError nosuchfielderror3) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.DARK_AQUA.ordinal()] = 4;
                } catch (NoSuchFieldError nosuchfielderror4) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.DARK_BLUE.ordinal()] = 2;
                } catch (NoSuchFieldError nosuchfielderror5) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.DARK_GRAY.ordinal()] = 9;
                } catch (NoSuchFieldError nosuchfielderror6) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.DARK_GREEN.ordinal()] = 3;
                } catch (NoSuchFieldError nosuchfielderror7) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.DARK_PURPLE.ordinal()] = 6;
                } catch (NoSuchFieldError nosuchfielderror8) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.DARK_RED.ordinal()] = 5;
                } catch (NoSuchFieldError nosuchfielderror9) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.GOLD.ordinal()] = 7;
                } catch (NoSuchFieldError nosuchfielderror10) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.GRAY.ordinal()] = 8;
                } catch (NoSuchFieldError nosuchfielderror11) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.GREEN.ordinal()] = 11;
                } catch (NoSuchFieldError nosuchfielderror12) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.ITALIC.ordinal()] = 21;
                } catch (NoSuchFieldError nosuchfielderror13) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.LIGHT_PURPLE.ordinal()] = 14;
                } catch (NoSuchFieldError nosuchfielderror14) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.OBFUSCATED.ordinal()] = 17;
                } catch (NoSuchFieldError nosuchfielderror15) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.RED.ordinal()] = 13;
                } catch (NoSuchFieldError nosuchfielderror16) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.RESET.ordinal()] = 22;
                } catch (NoSuchFieldError nosuchfielderror17) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.STRIKETHROUGH.ordinal()] = 19;
                } catch (NoSuchFieldError nosuchfielderror18) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.UNDERLINE.ordinal()] = 20;
                } catch (NoSuchFieldError nosuchfielderror19) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.WHITE.ordinal()] = 16;
                } catch (NoSuchFieldError nosuchfielderror20) {
                    ;
                }

                try {
                    aint1[EnumChatFormat.YELLOW.ordinal()] = 15;
                } catch (NoSuchFieldError nosuchfielderror21) {
                    ;
                }

                CraftChatMessage.StringMessage.$SWITCH_TABLE$net$minecraft$server$EnumChatFormat = aint1;
                return aint1;
            }
        }

        StringMessage(String s, boolean flag, CraftChatMessage.StringMessage craftchatmessage_stringmessage) {
            this(s, flag);
        }
    }
}
