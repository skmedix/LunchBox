package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;

@Plugin(
    name = "ExtendedThrowablePatternConverter",
    category = "Converter"
)
@ConverterKeys({ "xEx", "xThrowable", "xException"})
public final class ExtendedThrowablePatternConverter extends ThrowablePatternConverter {

    private ExtendedThrowablePatternConverter(String[] astring) {
        super("ExtendedThrowable", "throwable", astring);
    }

    public static ExtendedThrowablePatternConverter newInstance(String[] astring) {
        return new ExtendedThrowablePatternConverter(astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        ThrowableProxy throwableproxy = null;

        if (logevent instanceof Log4jLogEvent) {
            throwableproxy = ((Log4jLogEvent) logevent).getThrownProxy();
        }

        Throwable throwable = logevent.getThrown();

        if (throwable != null && this.options.anyLines()) {
            if (throwableproxy == null) {
                super.format(logevent, stringbuilder);
                return;
            }

            String s = throwableproxy.getExtendedStackTrace(this.options.getPackages());
            int i = stringbuilder.length();

            if (i > 0 && !Character.isWhitespace(stringbuilder.charAt(i - 1))) {
                stringbuilder.append(" ");
            }

            if (this.options.allLines() && Constants.LINE_SEP.equals(this.options.getSeparator())) {
                stringbuilder.append(s);
            } else {
                StringBuilder stringbuilder1 = new StringBuilder();
                String[] astring = s.split(Constants.LINE_SEP);
                int j = this.options.minLines(astring.length) - 1;

                for (int k = 0; k <= j; ++k) {
                    stringbuilder1.append(astring[k]);
                    if (k < j) {
                        stringbuilder1.append(this.options.getSeparator());
                    }
                }

                stringbuilder.append(stringbuilder1.toString());
            }
        }

    }
}
