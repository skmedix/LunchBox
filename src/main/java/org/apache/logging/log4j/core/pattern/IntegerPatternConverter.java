package org.apache.logging.log4j.core.pattern;

import java.util.Date;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "IntegerPatternConverter",
    category = "FileConverter"
)
@ConverterKeys({ "i", "index"})
public final class IntegerPatternConverter extends AbstractPatternConverter implements ArrayPatternConverter {

    private static final IntegerPatternConverter INSTANCE = new IntegerPatternConverter();

    private IntegerPatternConverter() {
        super("Integer", "integer");
    }

    public static IntegerPatternConverter newInstance(String[] astring) {
        return IntegerPatternConverter.INSTANCE;
    }

    public void format(StringBuilder stringbuilder, Object... aobject) {
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof Integer) {
                this.format(object, stringbuilder);
                break;
            }
        }

    }

    public void format(Object object, StringBuilder stringbuilder) {
        if (object instanceof Integer) {
            stringbuilder.append(object.toString());
        }

        if (object instanceof Date) {
            stringbuilder.append(Long.toString(((Date) object).getTime()));
        }

    }
}
