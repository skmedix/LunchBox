package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.util.Iterator;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.ThreadContext;

@Converter(
    autoApply = false
)
public class ContextStackAttributeConverter implements AttributeConverter {

    public String convertToDatabaseColumn(ThreadContext.ContextStack threadcontext_contextstack) {
        if (threadcontext_contextstack == null) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            String s;

            for (Iterator iterator = threadcontext_contextstack.asList().iterator(); iterator.hasNext(); stringbuilder.append(s)) {
                s = (String) iterator.next();
                if (stringbuilder.length() > 0) {
                    stringbuilder.append('\n');
                }
            }

            return stringbuilder.toString();
        }
    }

    public ThreadContext.ContextStack convertToEntityAttribute(String s) {
        throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
    }
}
