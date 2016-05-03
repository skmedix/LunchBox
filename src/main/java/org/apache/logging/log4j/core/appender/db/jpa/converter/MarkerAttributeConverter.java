package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
    autoApply = false
)
public class MarkerAttributeConverter implements AttributeConverter {

    public String convertToDatabaseColumn(Marker marker) {
        if (marker == null) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder(marker.getName());
            Marker marker1 = marker.getParent();
            int i = 0;

            boolean flag;

            for (flag = false; marker1 != null; marker1 = marker1.getParent()) {
                ++i;
                flag = true;
                stringbuilder.append("[ ").append(marker1.getName());
            }

            for (int j = 0; j < i; ++j) {
                stringbuilder.append(" ]");
            }

            if (flag) {
                stringbuilder.append(" ]");
            }

            return stringbuilder.toString();
        }
    }

    public Marker convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        } else {
            int i = s.indexOf("[");

            return i < 1 ? MarkerManager.getMarker(s) : MarkerManager.getMarker(s.substring(0, i));
        }
    }
}
