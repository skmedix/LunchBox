package org.apache.logging.log4j.core.pattern;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;

@Plugin(
    name = "ThrowablePatternConverter",
    category = "Converter"
)
@ConverterKeys({ "ex", "throwable", "exception"})
public class ThrowablePatternConverter extends LogEventPatternConverter {

    private String rawOption;
    protected final ThrowableFormatOptions options;

    protected ThrowablePatternConverter(String s, String s1, String[] astring) {
        super(s, s1);
        this.options = ThrowableFormatOptions.newInstance(astring);
        if (astring != null && astring.length > 0) {
            this.rawOption = astring[0];
        }

    }

    public static ThrowablePatternConverter newInstance(String[] astring) {
        return new ThrowablePatternConverter("Throwable", "throwable", astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        Throwable throwable = logevent.getThrown();

        if (this.isSubShortOption()) {
            this.formatSubShortOption(throwable, stringbuilder);
        } else if (throwable != null && this.options.anyLines()) {
            this.formatOption(throwable, stringbuilder);
        }

    }

    private boolean isSubShortOption() {
        return "short.message".equalsIgnoreCase(this.rawOption) || "short.localizedMessage".equalsIgnoreCase(this.rawOption) || "short.fileName".equalsIgnoreCase(this.rawOption) || "short.lineNumber".equalsIgnoreCase(this.rawOption) || "short.methodName".equalsIgnoreCase(this.rawOption) || "short.className".equalsIgnoreCase(this.rawOption);
    }

    private void formatSubShortOption(Throwable throwable, StringBuilder stringbuilder) {
        StackTraceElement stacktraceelement = null;

        if (throwable != null) {
            StackTraceElement[] astacktraceelement = throwable.getStackTrace();

            if (astacktraceelement != null && astacktraceelement.length > 0) {
                stacktraceelement = astacktraceelement[0];
            }
        }

        if (throwable != null && stacktraceelement != null) {
            String s = "";

            if ("short.className".equalsIgnoreCase(this.rawOption)) {
                s = stacktraceelement.getClassName();
            } else if ("short.methodName".equalsIgnoreCase(this.rawOption)) {
                s = stacktraceelement.getMethodName();
            } else if ("short.lineNumber".equalsIgnoreCase(this.rawOption)) {
                s = String.valueOf(stacktraceelement.getLineNumber());
            } else if ("short.message".equalsIgnoreCase(this.rawOption)) {
                s = throwable.getMessage();
            } else if ("short.localizedMessage".equalsIgnoreCase(this.rawOption)) {
                s = throwable.getLocalizedMessage();
            } else if ("short.fileName".equalsIgnoreCase(this.rawOption)) {
                s = stacktraceelement.getFileName();
            }

            int i = stringbuilder.length();

            if (i > 0 && !Character.isWhitespace(stringbuilder.charAt(i - 1))) {
                stringbuilder.append(" ");
            }

            stringbuilder.append(s);
        }

    }

    private void formatOption(Throwable throwable, StringBuilder stringbuilder) {
        StringWriter stringwriter = new StringWriter();

        throwable.printStackTrace(new PrintWriter(stringwriter));
        int i = stringbuilder.length();

        if (i > 0 && !Character.isWhitespace(stringbuilder.charAt(i - 1))) {
            stringbuilder.append(' ');
        }

        if (this.options.allLines() && Constants.LINE_SEP.equals(this.options.getSeparator())) {
            stringbuilder.append(stringwriter.toString());
        } else {
            StringBuilder stringbuilder1 = new StringBuilder();
            String[] astring = stringwriter.toString().split(Constants.LINE_SEP);
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

    public boolean handlesThrowable() {
        return true;
    }
}
