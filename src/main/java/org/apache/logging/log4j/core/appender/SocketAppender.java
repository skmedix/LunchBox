package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.DatagramSocketManager;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.net.TCPSocketManager;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(
    name = "Socket",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public class SocketAppender extends AbstractOutputStreamAppender {

    private Object advertisement;
    private final Advertiser advertiser;

    protected SocketAppender(String s, Layout layout, Filter filter, AbstractSocketManager abstractsocketmanager, boolean flag, boolean flag1, Advertiser advertiser) {
        super(s, layout, filter, flag, flag1, abstractsocketmanager);
        if (advertiser != null) {
            HashMap hashmap = new HashMap(layout.getContentFormat());

            hashmap.putAll(abstractsocketmanager.getContentFormat());
            hashmap.put("contentType", layout.getContentType());
            hashmap.put("name", s);
            this.advertisement = advertiser.advertise(hashmap);
        }

        this.advertiser = advertiser;
    }

    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }

    }

    @PluginFactory
    public static SocketAppender createAppender(@PluginAttribute("host") String s, @PluginAttribute("port") String s1, @PluginAttribute("protocol") String s2, @PluginAttribute("reconnectionDelay") String s3, @PluginAttribute("immediateFail") String s4, @PluginAttribute("name") String s5, @PluginAttribute("immediateFlush") String s6, @PluginAttribute("ignoreExceptions") String s7, @PluginElement("Layout") Layout layout, @PluginElement("Filters") Filter filter, @PluginAttribute("advertise") String s8, @PluginConfiguration Configuration configuration) {
        boolean flag = Booleans.parseBoolean(s6, true);
        boolean flag1 = Boolean.parseBoolean(s8);
        boolean flag2 = Booleans.parseBoolean(s7, true);
        boolean flag3 = Booleans.parseBoolean(s4, true);
        int i = AbstractAppender.parseInt(s3, 0);
        int j = AbstractAppender.parseInt(s1, 0);

        if (layout == null) {
            layout = SerializedLayout.createLayout();
        }

        if (s5 == null) {
            SocketAppender.LOGGER.error("No name provided for SocketAppender");
            return null;
        } else {
            Protocol protocol = (Protocol) EnglishEnums.valueOf(Protocol.class, s2 != null ? s2 : Protocol.TCP.name());

            if (protocol.equals(Protocol.UDP)) {
                flag = true;
            }

            AbstractSocketManager abstractsocketmanager = createSocketManager(protocol, s, j, i, flag3, (Layout) layout);

            return abstractsocketmanager == null ? null : new SocketAppender(s5, (Layout) layout, filter, abstractsocketmanager, flag2, flag, flag1 ? configuration.getAdvertiser() : null);
        }
    }

    protected static AbstractSocketManager createSocketManager(Protocol protocol, String s, int i, int j, boolean flag, Layout layout) {
        switch (SocketAppender.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$net$Protocol[protocol.ordinal()]) {
        case 1:
            return TCPSocketManager.getSocketManager(s, i, j, flag, layout);

        case 2:
            return DatagramSocketManager.getSocketManager(s, i, layout);

        default:
            return null;
        }
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$apache$logging$log4j$core$net$Protocol = new int[Protocol.values().length];

        static {
            try {
                SocketAppender.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$net$Protocol[Protocol.TCP.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                SocketAppender.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$net$Protocol[Protocol.UDP.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

        }
    }
}
