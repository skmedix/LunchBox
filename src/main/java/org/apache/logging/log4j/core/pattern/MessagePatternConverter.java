package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

@Plugin(
    name = "MessagePatternConverter",
    category = "Converter"
)
@ConverterKeys({ "m", "msg", "message"})
public final class MessagePatternConverter extends LogEventPatternConverter {

    private final String[] formats;
    private final Configuration config;

    private MessagePatternConverter(Configuration configuration, String[] astring) {
        super("Message", "message");
        this.formats = astring;
        this.config = configuration;
    }

    public static MessagePatternConverter newInstance(Configuration configuration, String[] astring) {
        return new MessagePatternConverter(configuration, astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        Message message = logevent.getMessage();

        if (message != null) {
            String s;

            if (message instanceof MultiformatMessage) {
                s = ((MultiformatMessage) message).getFormattedMessage(this.formats);
            } else {
                s = message.getFormattedMessage();
            }

            if (s != null) {
                stringbuilder.append(this.config != null && s.contains("${") ? this.config.getStrSubstitutor().replace(logevent, s) : s);
            } else {
                stringbuilder.append("null");
            }
        }

    }
}
