package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtils {

    private static final ConcurrentMap cLanguagesByCountry = new ConcurrentHashMap();
    private static final ConcurrentMap cCountriesByLanguage = new ConcurrentHashMap();

    public static Locale toLocale(String s) {
        if (s == null) {
            return null;
        } else if (s.isEmpty()) {
            return new Locale("", "");
        } else if (s.contains("#")) {
            throw new IllegalArgumentException("Invalid locale format: " + s);
        } else {
            int i = s.length();

            if (i < 2) {
                throw new IllegalArgumentException("Invalid locale format: " + s);
            } else {
                char c0 = s.charAt(0);

                if (c0 == 95) {
                    if (i < 3) {
                        throw new IllegalArgumentException("Invalid locale format: " + s);
                    } else {
                        char c1 = s.charAt(1);
                        char c2 = s.charAt(2);

                        if (Character.isUpperCase(c1) && Character.isUpperCase(c2)) {
                            if (i == 3) {
                                return new Locale("", s.substring(1, 3));
                            } else if (i < 5) {
                                throw new IllegalArgumentException("Invalid locale format: " + s);
                            } else if (s.charAt(3) != 95) {
                                throw new IllegalArgumentException("Invalid locale format: " + s);
                            } else {
                                return new Locale("", s.substring(1, 3), s.substring(4));
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid locale format: " + s);
                        }
                    }
                } else {
                    String[] astring = s.split("_", -1);
                    int j = astring.length - 1;

                    switch (j) {
                    case 0:
                        if (StringUtils.isAllLowerCase(s) && (i == 2 || i == 3)) {
                            return new Locale(s);
                        }

                        throw new IllegalArgumentException("Invalid locale format: " + s);

                    case 1:
                        if (StringUtils.isAllLowerCase(astring[0]) && (astring[0].length() == 2 || astring[0].length() == 3) && astring[1].length() == 2 && StringUtils.isAllUpperCase(astring[1])) {
                            return new Locale(astring[0], astring[1]);
                        }

                        throw new IllegalArgumentException("Invalid locale format: " + s);

                    case 2:
                        if (StringUtils.isAllLowerCase(astring[0]) && (astring[0].length() == 2 || astring[0].length() == 3) && (astring[1].length() == 0 || astring[1].length() == 2 && StringUtils.isAllUpperCase(astring[1])) && astring[2].length() > 0) {
                            return new Locale(astring[0], astring[1], astring[2]);
                        }

                    default:
                        throw new IllegalArgumentException("Invalid locale format: " + s);
                    }
                }
            }
        }
    }

    public static List localeLookupList(Locale locale) {
        return localeLookupList(locale, locale);
    }

    public static List localeLookupList(Locale locale, Locale locale1) {
        ArrayList arraylist = new ArrayList(4);

        if (locale != null) {
            arraylist.add(locale);
            if (locale.getVariant().length() > 0) {
                arraylist.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }

            if (locale.getCountry().length() > 0) {
                arraylist.add(new Locale(locale.getLanguage(), ""));
            }

            if (!arraylist.contains(locale1)) {
                arraylist.add(locale1);
            }
        }

        return Collections.unmodifiableList(arraylist);
    }

    public static List availableLocaleList() {
        return LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
    }

    public static Set availableLocaleSet() {
        return LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    public static boolean isAvailableLocale(Locale locale) {
        return availableLocaleList().contains(locale);
    }

    public static List languagesByCountry(String s) {
        if (s == null) {
            return Collections.emptyList();
        } else {
            List list = (List) LocaleUtils.cLanguagesByCountry.get(s);

            if (list == null) {
                ArrayList arraylist = new ArrayList();
                List list1 = availableLocaleList();

                for (int i = 0; i < list1.size(); ++i) {
                    Locale locale = (Locale) list1.get(i);

                    if (s.equals(locale.getCountry()) && locale.getVariant().isEmpty()) {
                        arraylist.add(locale);
                    }
                }

                list = Collections.unmodifiableList(arraylist);
                LocaleUtils.cLanguagesByCountry.putIfAbsent(s, list);
                list = (List) LocaleUtils.cLanguagesByCountry.get(s);
            }

            return list;
        }
    }

    public static List countriesByLanguage(String s) {
        if (s == null) {
            return Collections.emptyList();
        } else {
            List list = (List) LocaleUtils.cCountriesByLanguage.get(s);

            if (list == null) {
                ArrayList arraylist = new ArrayList();
                List list1 = availableLocaleList();

                for (int i = 0; i < list1.size(); ++i) {
                    Locale locale = (Locale) list1.get(i);

                    if (s.equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().isEmpty()) {
                        arraylist.add(locale);
                    }
                }

                list = Collections.unmodifiableList(arraylist);
                LocaleUtils.cCountriesByLanguage.putIfAbsent(s, list);
                list = (List) LocaleUtils.cCountriesByLanguage.get(s);
            }

            return list;
        }
    }

    static class SyncAvoid {

        private static final List AVAILABLE_LOCALE_LIST;
        private static final Set AVAILABLE_LOCALE_SET;

        static {
            ArrayList arraylist = new ArrayList(Arrays.asList(Locale.getAvailableLocales()));

            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(arraylist);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet(arraylist));
        }
    }
}
