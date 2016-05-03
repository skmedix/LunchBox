package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "MarkerPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "marker"})
public final class MarkerPatternConverter extends LogEventPatternConverter {

    private MarkerPatternConverter(String[] astring) {
        super("Marker", "marker");
    }

    public static MarkerPatternConverter newInstance(String[] astring) {
        return new MarkerPatternConverter(astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        Marker marker = logevent.getMarker();

        if (marker != null) {
            stringbuilder.append(marker.toString());
        }

    }
}
