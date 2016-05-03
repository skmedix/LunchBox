package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
    autoApply = false
)
public class StackTraceElementAttributeConverter implements AttributeConverter {

    private static final int UNKNOWN_SOURCE = -1;
    private static final int NATIVE_METHOD = -2;

    public String convertToDatabaseColumn(StackTraceElement stacktraceelement) {
        return stacktraceelement == null ? null : stacktraceelement.toString();
    }

    public StackTraceElement convertToEntityAttribute(String s) {
        return Strings.isEmpty(s) ? null : convertString(s);
    }

    static StackTraceElement convertString(String s) {
        int i = s.indexOf("(");
        String s1 = s.substring(0, i);
        String s2 = s1.substring(0, s1.lastIndexOf("."));
        String s3 = s1.substring(s1.lastIndexOf(".") + 1);
        String s4 = s.substring(i + 1, s.indexOf(")"));
        String s5 = null;
        int j = -1;

        if ("Native Method".equals(s4)) {
            j = -2;
        } else if (!"Unknown Source".equals(s4)) {
            int k = s4.indexOf(":");

            if (k > -1) {
                s5 = s4.substring(0, k);

                try {
                    j = Integer.parseInt(s4.substring(k + 1));
                } catch (NumberFormatException numberformatexception) {
                    ;
                }
            } else {
                s5 = s4.substring(0);
            }
        }

        return new StackTraceElement(s2, s3, s5, j);
    }
}
