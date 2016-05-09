package org.bukkit.craftbukkit.v1_8_R3.util;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public final class CraftChatMessage {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))");

    public static IChatComponent[] fromString(String message) {
        return fromString(message, false);
    }

    public static IChatComponent[] fromString(String message, boolean keepNewlines) {
        return (new CraftChatMessage.StringMessage(message, keepNewlines,null)).getOutput();
    }

    public static String fromComponent(IChatComponent component) {
        return fromComponent(component, EnumChatFormatting.BLACK);
    }

    public static String fromComponent(IChatComponent component, EnumChatFormatting defaultColor) {
        if (component == null) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();

            IChatComponent c;

            for (Iterator iterator = component.iterator(); iterator.hasNext(); out.append(c.getUnformattedText())) {
                c = (IChatComponent) iterator.next();
                ChatStyle modi = c.getChatStyle();

                out.append(modi.getColor() == null ? defaultColor : modi.getColor());
                if (modi.getBold()) {
                    out.append(EnumChatFormatting.BOLD);
                }

                if (modi.getItalic()) {
                    out.append(EnumChatFormatting.ITALIC);
                }

                if (modi.getUnderlined()) {
                    out.append(EnumChatFormatting.UNDERLINE);
                }

                if (modi.getStrikethrough()) {
                    out.append(EnumChatFormatting.STRIKETHROUGH);
                }

                if (modi.getObfuscated()) {
                    out.append(EnumChatFormatting.OBFUSCATED);
                }
            }

            return out.toString().replaceFirst("^(" + defaultColor + ")*", "");
        }
    }

    public static IChatComponent fixComponent(IChatComponent component) {
        Matcher matcher = CraftChatMessage.LINK_PATTERN.matcher("");

        return fixComponent(component, matcher);
    }

    private static IChatComponent fixComponent(IChatComponent component, Matcher matcher) {
        if (component instanceof ChatComponentText) {
            ChatComponentText extras = (ChatComponentText) component;
            String subs = extras.getChatComponentText_TextValue();

            if (matcher.reset(subs).find()) {
                matcher.reset();
                ChatStyle i = extras.getChatStyle() != null ? extras.getChatStyle() : new ChatStyle();
                ArrayList comp = new ArrayList();
                ArrayList c = new ArrayList(extras.getSiblings());

                component = extras = new ChatComponentText("");

                int pos;

                for (pos = 0; matcher.find(); pos = matcher.end()) {
                    String prev = matcher.group();

                    if (!prev.startsWith("http://") && !prev.startsWith("https://")) {
                        prev = "http://" + prev;
                    }

                    ChatComponentText c1 = new ChatComponentText(subs.substring(pos, matcher.start()));

                    c1.setChatStyle(i);
                    comp.add(c1);
                    ChatComponentText link = new ChatComponentText(matcher.group());
                    ChatStyle linkModi = i;

                    linkModi.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, prev));
                    link.setChatStyle(linkModi);
                    comp.add(link);
                }

                ChatComponentText chatcomponenttext = new ChatComponentText(subs.substring(pos));

                chatcomponenttext.setChatStyle(i);
                comp.add(chatcomponenttext);
                comp.addAll(c);
                Iterator iterator = comp.iterator();

                while (iterator.hasNext()) {
                    IChatComponent IChatComponent = (IChatComponent) iterator.next();

                    extras.appendSibling(IChatComponent);
                }
            }
        }

        List list = ((IChatComponent) component).getSiblings();

        for (int i = 0; i < list.size(); ++i) {
            IChatComponent IChatComponent1 = (IChatComponent) list.get(i);

            if (IChatComponent1.getChatStyle() != null && IChatComponent1.getChatStyle().getColor() == null) {
                list.set(i, fixComponent(IChatComponent1, matcher));
            }
        }

        if (component instanceof ChatComponentText) {
            Object[] aobject = (component).getSiblings().toArray();

            for (int j = 0; j < aobject.length; ++j) {
                Object object = aobject[j];

                if (object instanceof IChatComponent) {
                    IChatComponent IChatComponent2 = (IChatComponent) object;

                    if (IChatComponent2.getChatStyle() != null && IChatComponent2.getChatStyle().getColor() == null) {
                        aobject[j] = fixComponent(IChatComponent2, matcher);
                    }
                } else if (object instanceof String && matcher.reset((String) object).find()) {
                    aobject[j] = fixComponent(new ChatComponentText((String) object), matcher);
                }
            }
        }

        return (IChatComponent) component;
    }

    private static class StringMessage {

        private static final Map formatMap;
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + '§' + " \\n]|$))))", 2);
        private final List list;
        private IChatComponent currentChatComponent;
        private ChatStyle modifier;
        private final IChatComponent[] output;
        private int currentIndex;
        private final String message;
        private static int[] $SWITCH_TABLE$net$minecraft$server$EnumChatFormat;

        static {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            EnumChatFormatting[] aenumchatformat;
            int i = (aenumchatformat = EnumChatFormatting.values()).length;

            for (int j = 0; j < i; ++j) {
                EnumChatFormatting format = aenumchatformat[j];

                builder.put(Character.valueOf(Character.toLowerCase(format.toString().charAt(1))), format);
            }

            formatMap = builder.build();
        }

        private StringMessage(String message, boolean keepNewlines) {
            this.list = new ArrayList();
            this.currentChatComponent = new ChatComponentText("");
            this.modifier = new ChatStyle();
            this.message = message;
            if (message == null) {
                this.output = new IChatComponent[] { this.currentChatComponent};
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
                        EnumChatFormatting format = (EnumChatFormatting) CraftChatMessage.StringMessage.formatMap.get(Character.valueOf(match.toLowerCase().charAt(1)));

                        if (format == EnumChatFormatting.RESET) {
                            this.modifier = new ChatStyle();
                        } else if (format.isFancyStyling()) {
                            switch ($SWITCH_TABLE$net$minecraft$server$EnumChatFormat()[format.ordinal()]) {
                            case 17:
                                this.modifier.setObfuscated(Boolean.TRUE);
                                break;

                            case 18:
                                this.modifier.setBold(Boolean.TRUE);
                                break;

                            case 19:
                                this.modifier.setStrikethrough(Boolean.TRUE);
                                break;

                            case 20:
                                this.modifier.setUnderlined(Boolean.TRUE);
                                break;

                            case 21:
                                this.modifier.setItalic(Boolean.TRUE);
                                break;

                            default:
                                throw new AssertionError("Unexpected message format");
                            }
                        } else {
                            this.modifier = (new ChatStyle()).setColor(format);
                        }
                        break;

                    case 2:
                        if (keepNewlines) {
                            this.currentChatComponent.appendSibling(new ChatComponentText("\n"));
                        } else {
                            this.currentChatComponent = null;
                        }
                        break;

                    case 3:
                        if (!match.startsWith("http://") && !match.startsWith("https://")) {
                            match = "http://" + match;
                        }

                        this.modifier.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                        this.appendNewComponent(matcher.end(groupId));
                        this.modifier.setChatClickEvent((ClickEvent) null);
                    }
                }

                if (this.currentIndex < message.length()) {
                    this.appendNewComponent(message.length());
                }

                this.output = (IChatComponent[]) this.list.toArray(new IChatComponent[this.list.size()]);
            }
        }

        private void appendNewComponent(int index) {
            if (index > this.currentIndex) {
                IChatComponent addition = (new ChatComponentText(this.message.substring(this.currentIndex, index))).setChatStyle(this.modifier);

                this.currentIndex = index;
                this.modifier = this.modifier.createDeepCopy();
                if (this.currentChatComponent == null) {
                    this.currentChatComponent = new ChatComponentText("");
                    this.list.add(this.currentChatComponent);
                }

                this.currentChatComponent.appendSibling(addition);
            }
        }

        private IChatComponent[] getOutput() {
            return this.output;
        }

        static int[] $SWITCH_TABLE$net$minecraft$server$EnumChatFormat() {
            int[] aint = CraftChatMessage.StringMessage.$SWITCH_TABLE$net$minecraft$server$EnumChatFormat;

            if (CraftChatMessage.StringMessage.$SWITCH_TABLE$net$minecraft$server$EnumChatFormat != null) {
                return aint;
            } else {
                int[] aint1 = new int[EnumChatFormatting.values().length];
                try {
                    aint1[EnumChatFormatting.AQUA.ordinal()] = 12;
                } catch (NoSuchFieldError nosuchfielderror) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.BLACK.ordinal()] = 1;
                } catch (NoSuchFieldError nosuchfielderror1) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.BLUE.ordinal()] = 10;
                } catch (NoSuchFieldError nosuchfielderror2) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.BOLD.ordinal()] = 18;
                } catch (NoSuchFieldError nosuchfielderror3) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.DARK_AQUA.ordinal()] = 4;
                } catch (NoSuchFieldError nosuchfielderror4) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.DARK_BLUE.ordinal()] = 2;
                } catch (NoSuchFieldError nosuchfielderror5) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.DARK_GRAY.ordinal()] = 9;
                } catch (NoSuchFieldError nosuchfielderror6) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.DARK_GREEN.ordinal()] = 3;
                } catch (NoSuchFieldError nosuchfielderror7) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.DARK_PURPLE.ordinal()] = 6;
                } catch (NoSuchFieldError nosuchfielderror8) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.DARK_RED.ordinal()] = 5;
                } catch (NoSuchFieldError nosuchfielderror9) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.GOLD.ordinal()] = 7;
                } catch (NoSuchFieldError nosuchfielderror10) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.GRAY.ordinal()] = 8;
                } catch (NoSuchFieldError nosuchfielderror11) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.GREEN.ordinal()] = 11;
                } catch (NoSuchFieldError nosuchfielderror12) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.ITALIC.ordinal()] = 21;
                } catch (NoSuchFieldError nosuchfielderror13) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.LIGHT_PURPLE.ordinal()] = 14;
                } catch (NoSuchFieldError nosuchfielderror14) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.OBFUSCATED.ordinal()] = 17;
                } catch (NoSuchFieldError nosuchfielderror15) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.RED.ordinal()] = 13;
                } catch (NoSuchFieldError nosuchfielderror16) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.RESET.ordinal()] = 22;
                } catch (NoSuchFieldError nosuchfielderror17) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.STRIKETHROUGH.ordinal()] = 19;
                } catch (NoSuchFieldError nosuchfielderror18) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.UNDERLINE.ordinal()] = 20;
                } catch (NoSuchFieldError nosuchfielderror19) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.WHITE.ordinal()] = 16;
                } catch (NoSuchFieldError nosuchfielderror20) {
                    ;
                }

                try {
                    aint1[EnumChatFormatting.YELLOW.ordinal()] = 15;
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
