package org.apache.logging.log4j.core.helpers;

import java.util.Locale;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class OptionConverter {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String DELIM_START = "${";
    private static final char DELIM_STOP = '}';
    private static final int DELIM_START_LEN = 2;
    private static final int DELIM_STOP_LEN = 1;
    private static final int ONE_K = 1024;

    public static String[] concatenateArrays(String[] astring, String[] astring1) {
        int i = astring.length + astring1.length;
        String[] astring2 = new String[i];

        System.arraycopy(astring, 0, astring2, 0, astring.length);
        System.arraycopy(astring1, 0, astring2, astring.length, astring1.length);
        return astring2;
    }

    public static String convertSpecialChars(String s) {
        int i = s.length();
        StringBuilder stringbuilder = new StringBuilder(i);

        char c0;

        for (int j = 0; j < i; stringbuilder.append(c0)) {
            c0 = s.charAt(j++);
            if (c0 == 92) {
                c0 = s.charAt(j++);
                if (c0 == 110) {
                    c0 = 10;
                } else if (c0 == 114) {
                    c0 = 13;
                } else if (c0 == 116) {
                    c0 = 9;
                } else if (c0 == 102) {
                    c0 = 12;
                } else if (c0 == 8) {
                    c0 = 8;
                } else if (c0 == 34) {
                    c0 = 34;
                } else if (c0 == 39) {
                    c0 = 39;
                } else if (c0 == 92) {
                    c0 = 92;
                }
            }
        }

        return stringbuilder.toString();
    }

    public static Object instantiateByKey(Properties properties, String s, Class oclass, Object object) {
        String s1 = findAndSubst(s, properties);

        if (s1 == null) {
            OptionConverter.LOGGER.error("Could not find value for key " + s);
            return object;
        } else {
            return instantiateByClassName(s1.trim(), oclass, object);
        }
    }

    public static boolean toBoolean(String s, boolean flag) {
        if (s == null) {
            return flag;
        } else {
            String s1 = s.trim();

            return "true".equalsIgnoreCase(s1) ? true : ("false".equalsIgnoreCase(s1) ? false : flag);
        }
    }

    public static int toInt(String s, int i) {
        if (s != null) {
            String s1 = s.trim();

            try {
                return Integer.parseInt(s1);
            } catch (NumberFormatException numberformatexception) {
                OptionConverter.LOGGER.error("[" + s1 + "] is not in proper int form.");
                numberformatexception.printStackTrace();
            }
        }

        return i;
    }

    public static long toFileSize(String s, long i) {
        if (s == null) {
            return i;
        } else {
            String s1 = s.trim().toUpperCase(Locale.ENGLISH);
            long j = 1L;
            int k;

            if ((k = s1.indexOf("KB")) != -1) {
                j = 1024L;
                s1 = s1.substring(0, k);
            } else if ((k = s1.indexOf("MB")) != -1) {
                j = 1048576L;
                s1 = s1.substring(0, k);
            } else if ((k = s1.indexOf("GB")) != -1) {
                j = 1073741824L;
                s1 = s1.substring(0, k);
            }

            if (s1 != null) {
                try {
                    return Long.parseLong(s1) * j;
                } catch (NumberFormatException numberformatexception) {
                    OptionConverter.LOGGER.error("[" + s1 + "] is not in proper int form.");
                    OptionConverter.LOGGER.error("[" + s + "] not in expected format.", (Throwable) numberformatexception);
                }
            }

            return i;
        }
    }

    public static String findAndSubst(String s, Properties properties) {
        String s1 = properties.getProperty(s);

        if (s1 == null) {
            return null;
        } else {
            try {
                return substVars(s1, properties);
            } catch (IllegalArgumentException illegalargumentexception) {
                OptionConverter.LOGGER.error("Bad option value [" + s1 + "].", (Throwable) illegalargumentexception);
                return s1;
            }
        }
    }

    public static Object instantiateByClassName(String s, Class oclass, Object object) {
        if (s != null) {
            try {
                Class oclass1 = Loader.loadClass(s);

                if (!oclass.isAssignableFrom(oclass1)) {
                    OptionConverter.LOGGER.error("A \"" + s + "\" object is not assignable to a \"" + oclass.getName() + "\" variable.");
                    OptionConverter.LOGGER.error("The class \"" + oclass.getName() + "\" was loaded by ");
                    OptionConverter.LOGGER.error("[" + oclass.getClassLoader() + "] whereas object of type ");
                    OptionConverter.LOGGER.error("\"" + oclass1.getName() + "\" was loaded by [" + oclass1.getClassLoader() + "].");
                    return object;
                }

                return oclass1.newInstance();
            } catch (ClassNotFoundException classnotfoundexception) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + s + "].", (Throwable) classnotfoundexception);
            } catch (IllegalAccessException illegalaccessexception) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + s + "].", (Throwable) illegalaccessexception);
            } catch (InstantiationException instantiationexception) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + s + "].", (Throwable) instantiationexception);
            } catch (RuntimeException runtimeexception) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + s + "].", (Throwable) runtimeexception);
            }
        }

        return object;
    }

    public static String substVars(String s, Properties properties) throws IllegalArgumentException {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;

        while (true) {
            int j = s.indexOf("${", i);

            if (j == -1) {
                if (i == 0) {
                    return s;
                } else {
                    stringbuilder.append(s.substring(i, s.length()));
                    return stringbuilder.toString();
                }
            }

            stringbuilder.append(s.substring(i, j));
            int k = s.indexOf(125, j);

            if (k == -1) {
                throw new IllegalArgumentException('\"' + s + "\" has no closing brace. Opening brace at position " + j + '.');
            }

            j += 2;
            String s1 = s.substring(j, k);
            String s2 = PropertiesUtil.getProperties().getStringProperty(s1, (String) null);

            if (s2 == null && properties != null) {
                s2 = properties.getProperty(s1);
            }

            if (s2 != null) {
                String s3 = substVars(s2, properties);

                stringbuilder.append(s3);
            }

            i = k + 1;
        }
    }
}
