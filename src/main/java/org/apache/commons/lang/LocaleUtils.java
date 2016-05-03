package org.apache.commons.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtils {

    private static List cAvailableLocaleList;
    private static Set cAvailableLocaleSet;
    private static final Map cLanguagesByCountry = Collections.synchronizedMap(new HashMap());
    private static final Map cCountriesByLanguage = Collections.synchronizedMap(new HashMap());

    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();

            if (len != 2 && len != 5 && len < 7) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            } else {
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);

                if (ch0 >= 97 && ch0 <= 122 && ch1 >= 97 && ch1 <= 122) {
                    if (len == 2) {
                        return new Locale(str, "");
                    } else if (str.charAt(2) != 95) {
                        throw new IllegalArgumentException("Invalid locale format: " + str);
                    } else {
                        char ch3 = str.charAt(3);

                        if (ch3 == 95) {
                            return new Locale(str.substring(0, 2), "", str.substring(4));
                        } else {
                            char ch4 = str.charAt(4);

                            if (ch3 >= 65 && ch3 <= 90 && ch4 >= 65 && ch4 <= 90) {
                                if (len == 5) {
                                    return new Locale(str.substring(0, 2), str.substring(3, 5));
                                } else if (str.charAt(5) != 95) {
                                    throw new IllegalArgumentException("Invalid locale format: " + str);
                                } else {
                                    return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid locale format: " + str);
                            }
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
            }
        }
    }

    public static List localeLookupList(Locale locale) {
        return localeLookupList(locale, locale);
    }

    public static List localeLookupList(Locale locale, Locale defaultLocale) {
        ArrayList list = new ArrayList(4);

        if (locale != null) {
            list.add(locale);
            if (locale.getVariant().length() > 0) {
                list.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }

            if (locale.getCountry().length() > 0) {
                list.add(new Locale(locale.getLanguage(), ""));
            }

            if (!list.contains(defaultLocale)) {
                list.add(defaultLocale);
            }
        }

        return Collections.unmodifiableList(list);
    }

    public static List availableLocaleList() {
        if (LocaleUtils.cAvailableLocaleList == null) {
            initAvailableLocaleList();
        }

        return LocaleUtils.cAvailableLocaleList;
    }

    private static synchronized void initAvailableLocaleList() {
        if (LocaleUtils.cAvailableLocaleList == null) {
            List list = Arrays.asList(Locale.getAvailableLocales());

            LocaleUtils.cAvailableLocaleList = Collections.unmodifiableList(list);
        }

    }

    public static Set availableLocaleSet() {
        if (LocaleUtils.cAvailableLocaleSet == null) {
            initAvailableLocaleSet();
        }

        return LocaleUtils.cAvailableLocaleSet;
    }

    private static synchronized void initAvailableLocaleSet() {
        if (LocaleUtils.cAvailableLocaleSet == null) {
            LocaleUtils.cAvailableLocaleSet = Collections.unmodifiableSet(new HashSet(availableLocaleList()));
        }

    }

    public static boolean isAvailableLocale(Locale locale) {
        return availableLocaleList().contains(locale);
    }

    public static List languagesByCountry(String countryCode) {
        List langs = (List) LocaleUtils.cLanguagesByCountry.get(countryCode);

        if (langs == null) {
            if (countryCode != null) {
                ArrayList arraylist = new ArrayList();
                List locales = availableLocaleList();

                for (int i = 0; i < locales.size(); ++i) {
                    Locale locale = (Locale) locales.get(i);

                    if (countryCode.equals(locale.getCountry()) && locale.getVariant().length() == 0) {
                        arraylist.add(locale);
                    }
                }

                langs = Collections.unmodifiableList(arraylist);
            } else {
                langs = Collections.EMPTY_LIST;
            }

            LocaleUtils.cLanguagesByCountry.put(countryCode, langs);
        }

        return langs;
    }

    public static List countriesByLanguage(String languageCode) {
        List countries = (List) LocaleUtils.cCountriesByLanguage.get(languageCode);

        if (countries == null) {
            if (languageCode != null) {
                ArrayList arraylist = new ArrayList();
                List locales = availableLocaleList();

                for (int i = 0; i < locales.size(); ++i) {
                    Locale locale = (Locale) locales.get(i);

                    if (languageCode.equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().length() == 0) {
                        arraylist.add(locale);
                    }
                }

                countries = Collections.unmodifiableList(arraylist);
            } else {
                countries = Collections.EMPTY_LIST;
            }

            LocaleUtils.cCountriesByLanguage.put(languageCode, countries);
        }

        return countries;
    }
}
