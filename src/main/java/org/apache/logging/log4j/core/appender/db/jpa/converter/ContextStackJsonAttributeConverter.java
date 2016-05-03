package org.apache.logging.log4j.core.appender.db.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;

@Converter(
    autoApply = false
)
public class ContextStackJsonAttributeConverter implements AttributeConverter {

    public String convertToDatabaseColumn(ThreadContext.ContextStack threadcontext_contextstack) {
        if (threadcontext_contextstack == null) {
            return null;
        } else {
            try {
                return ContextMapJsonAttributeConverter.OBJECT_MAPPER.writeValueAsString(threadcontext_contextstack.asList());
            } catch (IOException ioexception) {
                throw new PersistenceException("Failed to convert stack list to JSON string.", ioexception);
            }
        }
    }

    public ThreadContext.ContextStack convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        } else {
            List list;

            try {
                list = (List) ContextMapJsonAttributeConverter.OBJECT_MAPPER.readValue(s, new TypeReference() {
                });
            } catch (IOException ioexception) {
                throw new PersistenceException("Failed to convert JSON string to list for stack.", ioexception);
            }

            DefaultThreadContextStack defaultthreadcontextstack = new DefaultThreadContextStack(true);

            defaultthreadcontextstack.addAll(list);
            return defaultthreadcontextstack;
        }
    }
}
