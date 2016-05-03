package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

public final class PatternParser {

    private static final char ESCAPE_CHAR = '%';
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final int BUF_SIZE = 32;
    private static final int DECIMAL = 10;
    private final Configuration config;
    private final Map converterRules;

    public PatternParser(String s) {
        this((Configuration) null, s, (Class) null, (Class) null);
    }

    public PatternParser(Configuration configuration, String s, Class oclass) {
        this(configuration, s, oclass, (Class) null);
    }

    public PatternParser(Configuration configuration, String s, Class oclass, Class oclass1) {
        this.config = configuration;
        PluginManager pluginmanager = new PluginManager(s, oclass);

        pluginmanager.collectPlugins();
        Map map = pluginmanager.getPlugins();
        HashMap hashmap = new HashMap();
        Iterator iterator = map.values().iterator();

        while (iterator.hasNext()) {
            PluginType plugintype = (PluginType) iterator.next();

            try {
                Class oclass2 = plugintype.getPluginClass();

                if (oclass1 == null || oclass1.isAssignableFrom(oclass2)) {
                    ConverterKeys converterkeys = (ConverterKeys) oclass2.getAnnotation(ConverterKeys.class);

                    if (converterkeys != null) {
                        String[] astring = converterkeys.value();
                        int i = astring.length;

                        for (int j = 0; j < i; ++j) {
                            String s1 = astring[j];

                            hashmap.put(s1, oclass2);
                        }
                    }
                }
            } catch (Exception exception) {
                PatternParser.LOGGER.error("Error processing plugin " + plugintype.getElementName(), (Throwable) exception);
            }
        }

        this.converterRules = hashmap;
    }

    public List parse(String s) {
        return this.parse(s, false);
    }

    public List parse(String s, boolean flag) {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();

        this.parse(s, arraylist1, arraylist2);
        Iterator iterator = arraylist2.iterator();
        boolean flag1 = false;

        Object object;
        FormattingInfo formattinginfo;

        for (Iterator iterator1 = arraylist1.iterator(); iterator1.hasNext(); arraylist.add(new PatternFormatter((LogEventPatternConverter) object, formattinginfo))) {
            PatternConverter patternconverter = (PatternConverter) iterator1.next();

            if (patternconverter instanceof LogEventPatternConverter) {
                object = (LogEventPatternConverter) patternconverter;
                flag1 |= ((LogEventPatternConverter) object).handlesThrowable();
            } else {
                object = new LiteralPatternConverter(this.config, "");
            }

            if (iterator.hasNext()) {
                formattinginfo = (FormattingInfo) iterator.next();
            } else {
                formattinginfo = FormattingInfo.getDefault();
            }
        }

        if (flag && !flag1) {
            ExtendedThrowablePatternConverter extendedthrowablepatternconverter = ExtendedThrowablePatternConverter.newInstance((String[]) null);

            arraylist.add(new PatternFormatter(extendedthrowablepatternconverter, FormattingInfo.getDefault()));
        }

        return arraylist;
    }

    private static int extractConverter(char c0, String s, int i, StringBuilder stringbuilder, StringBuilder stringbuilder1) {
        stringbuilder.setLength(0);
        if (!Character.isUnicodeIdentifierStart(c0)) {
            return i;
        } else {
            stringbuilder.append(c0);

            while (i < s.length() && Character.isUnicodeIdentifierPart(s.charAt(i))) {
                stringbuilder.append(s.charAt(i));
                stringbuilder1.append(s.charAt(i));
                ++i;
            }

            return i;
        }
    }

    private static int extractOptions(String s, int i, List list) {
        while (true) {
            if (i < s.length() && s.charAt(i) == 123) {
                int j = i++;
                int k = 0;

                int l;

                do {
                    l = s.indexOf(125, i);
                    if (l != -1) {
                        int i1 = s.indexOf("{", i);

                        if (i1 != -1 && i1 < l) {
                            i = l + 1;
                            ++k;
                        } else if (k > 0) {
                            --k;
                        }
                    }
                } while (k > 0);

                if (l != -1) {
                    String s1 = s.substring(j + 1, l);

                    list.add(s1);
                    i = l + 1;
                    continue;
                }
            }

            return i;
        }
    }

    public void parse(String s, List list, List list1) {
        if (s == null) {
            throw new NullPointerException("pattern");
        } else {
            StringBuilder stringbuilder = new StringBuilder(32);
            int i = s.length();
            PatternParser.ParserState patternparser_parserstate = PatternParser.ParserState.LITERAL_STATE;
            int j = 0;
            FormattingInfo formattinginfo = FormattingInfo.getDefault();

            while (j < i) {
                char c0 = s.charAt(j++);

                switch (PatternParser.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$pattern$PatternParser$ParserState[patternparser_parserstate.ordinal()]) {
                case 1:
                    if (j == i) {
                        stringbuilder.append(c0);
                    } else if (c0 == 37) {
                        switch (s.charAt(j)) {
                        case '%':
                            stringbuilder.append(c0);
                            ++j;
                            break;

                        default:
                            if (stringbuilder.length() != 0) {
                                list.add(new LiteralPatternConverter(this.config, stringbuilder.toString()));
                                list1.add(FormattingInfo.getDefault());
                            }

                            stringbuilder.setLength(0);
                            stringbuilder.append(c0);
                            patternparser_parserstate = PatternParser.ParserState.CONVERTER_STATE;
                            formattinginfo = FormattingInfo.getDefault();
                        }
                    } else {
                        stringbuilder.append(c0);
                    }
                    break;

                case 2:
                    stringbuilder.append(c0);
                    switch (c0) {
                    case '-':
                        formattinginfo = new FormattingInfo(true, formattinginfo.getMinLength(), formattinginfo.getMaxLength());
                        continue;

                    case '.':
                        patternparser_parserstate = PatternParser.ParserState.DOT_STATE;
                        continue;

                    default:
                        if (c0 >= 48 && c0 <= 57) {
                            formattinginfo = new FormattingInfo(formattinginfo.isLeftAligned(), c0 - 48, formattinginfo.getMaxLength());
                            patternparser_parserstate = PatternParser.ParserState.MIN_STATE;
                            continue;
                        }

                        j = this.finalizeConverter(c0, s, j, stringbuilder, formattinginfo, this.converterRules, list, list1);
                        patternparser_parserstate = PatternParser.ParserState.LITERAL_STATE;
                        formattinginfo = FormattingInfo.getDefault();
                        stringbuilder.setLength(0);
                        continue;
                    }

                case 3:
                    stringbuilder.append(c0);
                    if (c0 >= 48 && c0 <= 57) {
                        formattinginfo = new FormattingInfo(formattinginfo.isLeftAligned(), formattinginfo.getMinLength() * 10 + c0 - 48, formattinginfo.getMaxLength());
                    } else if (c0 == 46) {
                        patternparser_parserstate = PatternParser.ParserState.DOT_STATE;
                    } else {
                        j = this.finalizeConverter(c0, s, j, stringbuilder, formattinginfo, this.converterRules, list, list1);
                        patternparser_parserstate = PatternParser.ParserState.LITERAL_STATE;
                        formattinginfo = FormattingInfo.getDefault();
                        stringbuilder.setLength(0);
                    }
                    break;

                case 4:
                    stringbuilder.append(c0);
                    if (c0 >= 48 && c0 <= 57) {
                        formattinginfo = new FormattingInfo(formattinginfo.isLeftAligned(), formattinginfo.getMinLength(), c0 - 48);
                        patternparser_parserstate = PatternParser.ParserState.MAX_STATE;
                        break;
                    }

                    PatternParser.LOGGER.error("Error occurred in position " + j + ".\n Was expecting digit, instead got char \"" + c0 + "\".");
                    patternparser_parserstate = PatternParser.ParserState.LITERAL_STATE;
                    break;

                case 5:
                    stringbuilder.append(c0);
                    if (c0 >= 48 && c0 <= 57) {
                        formattinginfo = new FormattingInfo(formattinginfo.isLeftAligned(), formattinginfo.getMinLength(), formattinginfo.getMaxLength() * 10 + c0 - 48);
                    } else {
                        j = this.finalizeConverter(c0, s, j, stringbuilder, formattinginfo, this.converterRules, list, list1);
                        patternparser_parserstate = PatternParser.ParserState.LITERAL_STATE;
                        formattinginfo = FormattingInfo.getDefault();
                        stringbuilder.setLength(0);
                    }
                }
            }

            if (stringbuilder.length() != 0) {
                list.add(new LiteralPatternConverter(this.config, stringbuilder.toString()));
                list1.add(FormattingInfo.getDefault());
            }

        }
    }

    private PatternConverter createConverter(String s, StringBuilder stringbuilder, Map map, List list) {
        String s1 = s;
        Class oclass = null;

        for (int i = s.length(); i > 0 && oclass == null; --i) {
            s1 = s1.substring(0, i);
            if (oclass == null && map != null) {
                oclass = (Class) map.get(s1);
            }
        }

        if (oclass == null) {
            PatternParser.LOGGER.error("Unrecognized format specifier [" + s + "]");
            return null;
        } else {
            Method[] amethod = oclass.getDeclaredMethods();
            Method method = null;
            Method[] amethod1 = amethod;
            int j = amethod.length;

            int k;

            for (k = 0; k < j; ++k) {
                Method method1 = amethod1[k];

                if (Modifier.isStatic(method1.getModifiers()) && method1.getDeclaringClass().equals(oclass) && method1.getName().equals("newInstance")) {
                    if (method == null) {
                        method = method1;
                    } else if (method1.getReturnType().equals(method.getReturnType())) {
                        PatternParser.LOGGER.error("Class " + oclass + " cannot contain multiple static newInstance methods");
                        return null;
                    }
                }
            }

            if (method == null) {
                PatternParser.LOGGER.error("Class " + oclass + " does not contain a static newInstance method");
                return null;
            } else {
                Class[] aclass = method.getParameterTypes();
                Object[] aobject = aclass.length > 0 ? new Object[aclass.length] : null;

                if (aobject != null) {
                    k = 0;
                    boolean flag = false;
                    Class[] aclass1 = aclass;
                    int l = aclass.length;

                    for (int i1 = 0; i1 < l; ++i1) {
                        Class oclass1 = aclass1[i1];

                        if (oclass1.isArray() && oclass1.getName().equals("[Ljava.lang.String;")) {
                            String[] astring = (String[]) list.toArray(new String[list.size()]);

                            aobject[k] = astring;
                        } else if (oclass1.isAssignableFrom(Configuration.class)) {
                            aobject[k] = this.config;
                        } else {
                            PatternParser.LOGGER.error("Unknown parameter type " + oclass1.getName() + " for static newInstance method of " + oclass.getName());
                            flag = true;
                        }

                        ++k;
                    }

                    if (flag) {
                        return null;
                    }
                }

                try {
                    Object object = method.invoke((Object) null, aobject);

                    if (object instanceof PatternConverter) {
                        stringbuilder.delete(0, stringbuilder.length() - (s.length() - s1.length()));
                        return (PatternConverter) object;
                    }

                    PatternParser.LOGGER.warn("Class " + oclass.getName() + " does not extend PatternConverter.");
                } catch (Exception exception) {
                    PatternParser.LOGGER.error("Error creating converter for " + s, (Throwable) exception);
                }

                return null;
            }
        }
    }

    private int finalizeConverter(char c0, String s, int i, StringBuilder stringbuilder, FormattingInfo formattinginfo, Map map, List list, List list1) {
        StringBuilder stringbuilder1 = new StringBuilder();

        i = extractConverter(c0, s, i, stringbuilder1, stringbuilder);
        String s1 = stringbuilder1.toString();
        ArrayList arraylist = new ArrayList();

        i = extractOptions(s, i, arraylist);
        PatternConverter patternconverter = this.createConverter(s1, stringbuilder, map, arraylist);

        if (patternconverter == null) {
            StringBuilder stringbuilder2;

            if (Strings.isEmpty(s1)) {
                stringbuilder2 = new StringBuilder("Empty conversion specifier starting at position ");
            } else {
                stringbuilder2 = new StringBuilder("Unrecognized conversion specifier [");
                stringbuilder2.append(s1);
                stringbuilder2.append("] starting at position ");
            }

            stringbuilder2.append(Integer.toString(i));
            stringbuilder2.append(" in conversion pattern.");
            PatternParser.LOGGER.error(stringbuilder2.toString());
            list.add(new LiteralPatternConverter(this.config, stringbuilder.toString()));
            list1.add(FormattingInfo.getDefault());
        } else {
            list.add(patternconverter);
            list1.add(formattinginfo);
            if (stringbuilder.length() > 0) {
                list.add(new LiteralPatternConverter(this.config, stringbuilder.toString()));
                list1.add(FormattingInfo.getDefault());
            }
        }

        stringbuilder.setLength(0);
        return i;
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$apache$logging$log4j$core$pattern$PatternParser$ParserState = new int[PatternParser.ParserState.values().length];

        static {
            try {
                PatternParser.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$pattern$PatternParser$ParserState[PatternParser.ParserState.LITERAL_STATE.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                PatternParser.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$pattern$PatternParser$ParserState[PatternParser.ParserState.CONVERTER_STATE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                PatternParser.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$pattern$PatternParser$ParserState[PatternParser.ParserState.MIN_STATE.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                PatternParser.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$pattern$PatternParser$ParserState[PatternParser.ParserState.DOT_STATE.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                PatternParser.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$pattern$PatternParser$ParserState[PatternParser.ParserState.MAX_STATE.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

        }
    }

    private static enum ParserState {

        LITERAL_STATE, CONVERTER_STATE, DOT_STATE, MIN_STATE, MAX_STATE;
    }
}
