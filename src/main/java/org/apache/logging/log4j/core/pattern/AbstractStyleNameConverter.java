package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

public abstract class AbstractStyleNameConverter extends LogEventPatternConverter {

    private final List formatters;
    private final String style;

    protected AbstractStyleNameConverter(String s, List list, String s1) {
        super(s, "style");
        this.formatters = list;
        this.style = s1;
    }

    protected static AbstractStyleNameConverter newInstance(Class oclass, String s, Configuration configuration, String[] astring) {
        List list = toPatternFormatterList(configuration, astring);

        if (list == null) {
            return null;
        } else {
            try {
                Constructor constructor = oclass.getConstructor(new Class[] { List.class, String.class});

                return (AbstractStyleNameConverter) constructor.newInstance(new Object[] { list, AnsiEscape.createSequence(new String[] { s})});
            } catch (SecurityException securityexception) {
                AbstractStyleNameConverter.LOGGER.error(securityexception.toString(), (Throwable) securityexception);
            } catch (NoSuchMethodException nosuchmethodexception) {
                AbstractStyleNameConverter.LOGGER.error(nosuchmethodexception.toString(), (Throwable) nosuchmethodexception);
            } catch (IllegalArgumentException illegalargumentexception) {
                AbstractStyleNameConverter.LOGGER.error(illegalargumentexception.toString(), (Throwable) illegalargumentexception);
            } catch (InstantiationException instantiationexception) {
                AbstractStyleNameConverter.LOGGER.error(instantiationexception.toString(), (Throwable) instantiationexception);
            } catch (IllegalAccessException illegalaccessexception) {
                AbstractStyleNameConverter.LOGGER.error(illegalaccessexception.toString(), (Throwable) illegalaccessexception);
            } catch (InvocationTargetException invocationtargetexception) {
                AbstractStyleNameConverter.LOGGER.error(invocationtargetexception.toString(), (Throwable) invocationtargetexception);
            }

            return null;
        }
    }

    private static List toPatternFormatterList(Configuration configuration, String[] astring) {
        if (astring.length != 0 && astring[0] != null) {
            PatternParser patternparser = PatternLayout.createPatternParser(configuration);

            if (patternparser == null) {
                AbstractStyleNameConverter.LOGGER.error("No PatternParser created for config=" + configuration + ", options=" + Arrays.toString(astring));
                return null;
            } else {
                return patternparser.parse(astring[0]);
            }
        } else {
            AbstractStyleNameConverter.LOGGER.error("No pattern supplied on style for config=" + configuration);
            return null;
        }
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StringBuilder stringbuilder1 = new StringBuilder();
        Iterator iterator = this.formatters.iterator();

        while (iterator.hasNext()) {
            PatternFormatter patternformatter = (PatternFormatter) iterator.next();

            patternformatter.format(logevent, stringbuilder1);
        }

        if (stringbuilder1.length() > 0) {
            stringbuilder.append(this.style).append(stringbuilder1.toString()).append(AnsiEscape.getDefaultStyle());
        }

    }

    @Plugin(
        name = "yellow",
        category = "Converter"
    )
    @ConverterKeys({ "yellow"})
    public static final class Yellow extends AbstractStyleNameConverter {

        protected static final String NAME = "yellow";

        public Yellow(List list, String s) {
            super("yellow", list, s);
        }

        public static AbstractStyleNameConverter.Yellow newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.Yellow) newInstance(AbstractStyleNameConverter.Yellow.class, "yellow", configuration, astring);
        }
    }

    @Plugin(
        name = "white",
        category = "Converter"
    )
    @ConverterKeys({ "white"})
    public static final class White extends AbstractStyleNameConverter {

        protected static final String NAME = "white";

        public White(List list, String s) {
            super("white", list, s);
        }

        public static AbstractStyleNameConverter.White newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.White) newInstance(AbstractStyleNameConverter.White.class, "white", configuration, astring);
        }
    }

    @Plugin(
        name = "red",
        category = "Converter"
    )
    @ConverterKeys({ "red"})
    public static final class Red extends AbstractStyleNameConverter {

        protected static final String NAME = "red";

        public Red(List list, String s) {
            super("red", list, s);
        }

        public static AbstractStyleNameConverter.Red newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.Red) newInstance(AbstractStyleNameConverter.Red.class, "red", configuration, astring);
        }
    }

    @Plugin(
        name = "magenta",
        category = "Converter"
    )
    @ConverterKeys({ "magenta"})
    public static final class Magenta extends AbstractStyleNameConverter {

        protected static final String NAME = "magenta";

        public Magenta(List list, String s) {
            super("magenta", list, s);
        }

        public static AbstractStyleNameConverter.Magenta newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.Magenta) newInstance(AbstractStyleNameConverter.Magenta.class, "magenta", configuration, astring);
        }
    }

    @Plugin(
        name = "green",
        category = "Converter"
    )
    @ConverterKeys({ "green"})
    public static final class Green extends AbstractStyleNameConverter {

        protected static final String NAME = "green";

        public Green(List list, String s) {
            super("green", list, s);
        }

        public static AbstractStyleNameConverter.Green newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.Green) newInstance(AbstractStyleNameConverter.Green.class, "green", configuration, astring);
        }
    }

    @Plugin(
        name = "cyan",
        category = "Converter"
    )
    @ConverterKeys({ "cyan"})
    public static final class Cyan extends AbstractStyleNameConverter {

        protected static final String NAME = "cyan";

        public Cyan(List list, String s) {
            super("cyan", list, s);
        }

        public static AbstractStyleNameConverter.Cyan newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.Cyan) newInstance(AbstractStyleNameConverter.Cyan.class, "cyan", configuration, astring);
        }
    }

    @Plugin(
        name = "blue",
        category = "Converter"
    )
    @ConverterKeys({ "blue"})
    public static final class Blue extends AbstractStyleNameConverter {

        protected static final String NAME = "blue";

        public Blue(List list, String s) {
            super("blue", list, s);
        }

        public static AbstractStyleNameConverter.Blue newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.Blue) newInstance(AbstractStyleNameConverter.Blue.class, "blue", configuration, astring);
        }
    }

    @Plugin(
        name = "black",
        category = "Converter"
    )
    @ConverterKeys({ "black"})
    public static final class Black extends AbstractStyleNameConverter {

        protected static final String NAME = "black";

        public Black(List list, String s) {
            super("black", list, s);
        }

        public static AbstractStyleNameConverter.Black newInstance(Configuration configuration, String[] astring) {
            return (AbstractStyleNameConverter.Black) newInstance(AbstractStyleNameConverter.Black.class, "black", configuration, astring);
        }
    }
}
