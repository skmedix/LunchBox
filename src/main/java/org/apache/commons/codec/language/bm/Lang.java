package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class Lang {

    private static final Map Langs = new EnumMap(NameType.class);
    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
    private final Languages languages;
    private final List rules;

    public static Lang instance(NameType nametype) {
        return (Lang) Lang.Langs.get(nametype);
    }

    public static Lang loadFromResource(String s, Languages languages) {
        ArrayList arraylist = new ArrayList();
        InputStream inputstream = Lang.class.getClassLoader().getResourceAsStream(s);

        if (inputstream == null) {
            throw new IllegalStateException("Unable to resolve required resource:org/apache/commons/codec/language/bm/lang.txt");
        } else {
            Scanner scanner = new Scanner(inputstream, "UTF-8");

            try {
                boolean flag = false;

                while (scanner.hasNextLine()) {
                    String s1 = scanner.nextLine();
                    String s2 = s1;

                    if (flag) {
                        if (s1.endsWith("*/")) {
                            flag = false;
                        }
                    } else if (s1.startsWith("/*")) {
                        flag = true;
                    } else {
                        int i = s1.indexOf("//");

                        if (i >= 0) {
                            s2 = s1.substring(0, i);
                        }

                        s2 = s2.trim();
                        if (s2.length() != 0) {
                            String[] astring = s2.split("\\s+");

                            if (astring.length != 3) {
                                throw new IllegalArgumentException("Malformed line \'" + s1 + "\' in language resource \'" + s + "\'");
                            }

                            Pattern pattern = Pattern.compile(astring[0]);
                            String[] astring1 = astring[1].split("\\+");
                            boolean flag1 = astring[2].equals("true");

                            arraylist.add(new Lang.LangRule(pattern, new HashSet(Arrays.asList(astring1)), flag1, (Lang.SyntheticClass_1) null));
                        }
                    }
                }
            } finally {
                scanner.close();
            }

            return new Lang(arraylist, languages);
        }
    }

    private Lang(List list, Languages languages) {
        this.rules = Collections.unmodifiableList(list);
        this.languages = languages;
    }

    public String guessLanguage(String s) {
        Languages.LanguageSet languages_languageset = this.guessLanguages(s);

        return languages_languageset.isSingleton() ? languages_languageset.getAny() : "any";
    }

    public Languages.LanguageSet guessLanguages(String s) {
        String s1 = s.toLowerCase(Locale.ENGLISH);
        HashSet hashset = new HashSet(this.languages.getLanguages());
        Iterator iterator = this.rules.iterator();

        while (iterator.hasNext()) {
            Lang.LangRule lang_langrule = (Lang.LangRule) iterator.next();

            if (lang_langrule.matches(s1)) {
                if (lang_langrule.acceptOnMatch) {
                    hashset.retainAll(lang_langrule.languages);
                } else {
                    hashset.removeAll(lang_langrule.languages);
                }
            }
        }

        Languages.LanguageSet languages_languageset = Languages.LanguageSet.from(hashset);

        return languages_languageset.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : languages_languageset;
    }

    static {
        NameType[] anametype = NameType.values();
        int i = anametype.length;

        for (int j = 0; j < i; ++j) {
            NameType nametype = anametype[j];

            Lang.Langs.put(nametype, loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(nametype)));
        }

    }

    static class SyntheticClass_1 {    }

    private static final class LangRule {

        private final boolean acceptOnMatch;
        private final Set languages;
        private final Pattern pattern;

        private LangRule(Pattern pattern, Set set, boolean flag) {
            this.pattern = pattern;
            this.languages = set;
            this.acceptOnMatch = flag;
        }

        public boolean matches(String s) {
            return this.pattern.matcher(s).find();
        }

        LangRule(Pattern pattern, Set set, boolean flag, Lang.SyntheticClass_1 lang_syntheticclass_1) {
            this(pattern, set, flag);
        }
    }
}
