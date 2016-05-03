package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {

    public static final Rule.RPattern ALL_STRINGS_RMATCHER = new Rule.RPattern() {
        public boolean isMatch(CharSequence charsequence) {
            return true;
        }
    };
    public static final String ALL = "ALL";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String HASH_INCLUDE = "#include";
    private static final Map RULES = new EnumMap(NameType.class);
    private final Rule.RPattern lContext;
    private final String pattern;
    private final Rule.PhonemeExpr phoneme;
    private final Rule.RPattern rContext;

    private static boolean contains(CharSequence charsequence, char c0) {
        for (int i = 0; i < charsequence.length(); ++i) {
            if (charsequence.charAt(i) == c0) {
                return true;
            }
        }

        return false;
    }

    private static String createResourceName(NameType nametype, RuleType ruletype, String s) {
        return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", new Object[] { nametype.getName(), ruletype.getName(), s});
    }

    private static Scanner createScanner(NameType nametype, RuleType ruletype, String s) {
        String s1 = createResourceName(nametype, ruletype, s);
        InputStream inputstream = Languages.class.getClassLoader().getResourceAsStream(s1);

        if (inputstream == null) {
            throw new IllegalArgumentException("Unable to load resource: " + s1);
        } else {
            return new Scanner(inputstream, "UTF-8");
        }
    }

    private static Scanner createScanner(String s) {
        String s1 = String.format("org/apache/commons/codec/language/bm/%s.txt", new Object[] { s});
        InputStream inputstream = Languages.class.getClassLoader().getResourceAsStream(s1);

        if (inputstream == null) {
            throw new IllegalArgumentException("Unable to load resource: " + s1);
        } else {
            return new Scanner(inputstream, "UTF-8");
        }
    }

    private static boolean endsWith(CharSequence charsequence, CharSequence charsequence1) {
        if (charsequence1.length() > charsequence.length()) {
            return false;
        } else {
            int i = charsequence.length() - 1;

            for (int j = charsequence1.length() - 1; j >= 0; --j) {
                if (charsequence.charAt(i) != charsequence1.charAt(j)) {
                    return false;
                }

                --i;
            }

            return true;
        }
    }

    public static List getInstance(NameType nametype, RuleType ruletype, Languages.LanguageSet languages_languageset) {
        Map map = getInstanceMap(nametype, ruletype, languages_languageset);
        ArrayList arraylist = new ArrayList();
        Iterator iterator = map.values().iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();

            arraylist.addAll(list);
        }

        return arraylist;
    }

    public static List getInstance(NameType nametype, RuleType ruletype, String s) {
        return getInstance(nametype, ruletype, Languages.LanguageSet.from(new HashSet(Arrays.asList(new String[] { s}))));
    }

    public static Map getInstanceMap(NameType nametype, RuleType ruletype, Languages.LanguageSet languages_languageset) {
        return languages_languageset.isSingleton() ? getInstanceMap(nametype, ruletype, languages_languageset.getAny()) : getInstanceMap(nametype, ruletype, "any");
    }

    public static Map getInstanceMap(NameType nametype, RuleType ruletype, String s) {
        Map map = (Map) ((Map) ((Map) Rule.RULES.get(nametype)).get(ruletype)).get(s);

        if (map == null) {
            throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", new Object[] { nametype.getName(), ruletype.getName(), s}));
        } else {
            return map;
        }
    }

    private static Rule.Phoneme parsePhoneme(String s) {
        int i = s.indexOf("[");

        if (i >= 0) {
            if (!s.endsWith("]")) {
                throw new IllegalArgumentException("Phoneme expression contains a \'[\' but does not end in \']\'");
            } else {
                String s1 = s.substring(0, i);
                String s2 = s.substring(i + 1, s.length() - 1);
                HashSet hashset = new HashSet(Arrays.asList(s2.split("[+]")));

                return new Rule.Phoneme(s1, Languages.LanguageSet.from(hashset));
            }
        } else {
            return new Rule.Phoneme(s, Languages.ANY_LANGUAGE);
        }
    }

    private static Rule.PhonemeExpr parsePhonemeExpr(String s) {
        if (!s.startsWith("(")) {
            return parsePhoneme(s);
        } else if (!s.endsWith(")")) {
            throw new IllegalArgumentException("Phoneme starts with \'(\' so must end with \')\'");
        } else {
            ArrayList arraylist = new ArrayList();
            String s1 = s.substring(1, s.length() - 1);
            String[] astring = s1.split("[|]");
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s2 = astring[j];

                arraylist.add(parsePhoneme(s2));
            }

            if (s1.startsWith("|") || s1.endsWith("|")) {
                arraylist.add(new Rule.Phoneme("", Languages.ANY_LANGUAGE));
            }

            return new Rule.PhonemeList(arraylist);
        }
    }

    private static Map parseRules(Scanner scanner, final String s) {
        HashMap hashmap = new HashMap();
        final int i = 0;
        boolean flag = false;

        while (scanner.hasNextLine()) {
            ++i;
            String s1 = scanner.nextLine();
            String s2 = s1;

            if (flag) {
                if (s1.endsWith("*/")) {
                    flag = false;
                }
            } else if (s1.startsWith("/*")) {
                flag = true;
            } else {
                int j = s1.indexOf("//");

                if (j >= 0) {
                    s2 = s1.substring(0, j);
                }

                s2 = s2.trim();
                if (s2.length() != 0) {
                    if (s2.startsWith("#include")) {
                        String s3 = s2.substring("#include".length()).trim();

                        if (s3.contains(" ")) {
                            throw new IllegalArgumentException("Malformed import statement \'" + s1 + "\' in " + s);
                        }

                        hashmap.putAll(parseRules(createScanner(s3), s + "->" + s3));
                    } else {
                        String[] astring = s2.split("\\s+");

                        if (astring.length != 4) {
                            throw new IllegalArgumentException("Malformed rule statement split into " + astring.length + " parts: " + s1 + " in " + s);
                        }

                        try {
                            final String s4 = stripQuotes(astring[0]);
                            final String s5 = stripQuotes(astring[1]);
                            final String s6 = stripQuotes(astring[2]);
                            final Rule.PhonemeExpr rule_phonemeexpr = parsePhonemeExpr(stripQuotes(astring[3]));
                            Rule rule = new Rule(s4, s5, s6, rule_phonemeexpr) {
                                private final int myLine = i;
                                private final String loc = s;

                                public String toString() {
                                    StringBuilder stringbuilder = new StringBuilder();

                                    stringbuilder.append("Rule");
                                    stringbuilder.append("{line=").append(this.myLine);
                                    stringbuilder.append(", loc=\'").append(this.loc).append('\'');
                                    stringbuilder.append('}');
                                    return stringbuilder.toString();
                                }
                            };
                            String s7 = rule.pattern.substring(0, 1);
                            Object object = (List) hashmap.get(s7);

                            if (object == null) {
                                object = new ArrayList();
                                hashmap.put(s7, object);
                            }

                            ((List) object).add(rule);
                        } catch (IllegalArgumentException illegalargumentexception) {
                            throw new IllegalStateException("Problem parsing line \'" + i + "\' in " + s, illegalargumentexception);
                        }
                    }
                }
            }
        }

        return hashmap;
    }

    private static Rule.RPattern pattern(final String s) {
        boolean flag = s.startsWith("^");
        boolean flag1 = s.endsWith("$");
        final String s1 = s.substring(flag ? 1 : 0, flag1 ? s.length() - 1 : s.length());
        boolean flag2 = s1.contains("[");

        if (!flag2) {
            if (flag && flag1) {
                if (s1.length() == 0) {
                    return new Rule.RPattern() {
                        public boolean isMatch(CharSequence charsequence) {
                            return charsequence.length() == 0;
                        }
                    };
                }

                return new Rule.RPattern() {
                    public boolean isMatch(CharSequence charsequence) {
                        return charsequence.equals(s);
                    }
                };
            }

            if ((flag || flag1) && s1.length() == 0) {
                return Rule.ALL_STRINGS_RMATCHER;
            }

            if (flag) {
                return new Rule.RPattern() {
                    public boolean isMatch(CharSequence charsequence) {
                        return Rule.startsWith(charsequence, s);
                    }
                };
            }

            if (flag1) {
                return new Rule.RPattern() {
                    public boolean isMatch(CharSequence charsequence) {
                        return Rule.endsWith(charsequence, s);
                    }
                };
            }
        } else {
            boolean flag3 = s1.startsWith("[");
            boolean flag4 = s1.endsWith("]");

            if (flag3 && flag4) {
                final String s2 = s1.substring(1, s1.length() - 1);

                if (!s2.contains("[")) {
                    boolean flag5 = s2.startsWith("^");

                    if (flag5) {
                        s2 = s2.substring(1);
                    }

                    final boolean flag6 = !flag5;

                    if (flag && flag1) {
                        return new Rule.RPattern() {
                            public boolean isMatch(CharSequence charsequence) {
                                return charsequence.length() == 1 && Rule.contains(s, charsequence.charAt(0)) == flag;
                            }
                        };
                    }

                    if (flag) {
                        return new Rule.RPattern() {
                            public boolean isMatch(CharSequence charsequence) {
                                return charsequence.length() > 0 && Rule.contains(s, charsequence.charAt(0)) == flag;
                            }
                        };
                    }

                    if (flag1) {
                        return new Rule.RPattern() {
                            public boolean isMatch(CharSequence charsequence) {
                                return charsequence.length() > 0 && Rule.contains(s, charsequence.charAt(charsequence.length() - 1)) == flag;
                            }
                        };
                    }
                }
            }
        }

        return new Rule.RPattern() {
            Pattern pattern = Pattern.compile(s);

            public boolean isMatch(CharSequence charsequence) {
                Matcher matcher = this.pattern.matcher(charsequence);

                return matcher.find();
            }
        };
    }

    private static boolean startsWith(CharSequence charsequence, CharSequence charsequence1) {
        if (charsequence1.length() > charsequence.length()) {
            return false;
        } else {
            for (int i = 0; i < charsequence1.length(); ++i) {
                if (charsequence.charAt(i) != charsequence1.charAt(i)) {
                    return false;
                }
            }

            return true;
        }
    }

    private static String stripQuotes(String s) {
        if (s.startsWith("\"")) {
            s = s.substring(1);
        }

        if (s.endsWith("\"")) {
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }

    public Rule(String s, String s1, String s2, Rule.PhonemeExpr rule_phonemeexpr) {
        this.pattern = s;
        this.lContext = pattern(s1 + "$");
        this.rContext = pattern("^" + s2);
        this.phoneme = rule_phonemeexpr;
    }

    public Rule.RPattern getLContext() {
        return this.lContext;
    }

    public String getPattern() {
        return this.pattern;
    }

    public Rule.PhonemeExpr getPhoneme() {
        return this.phoneme;
    }

    public Rule.RPattern getRContext() {
        return this.rContext;
    }

    public boolean patternAndContextMatches(CharSequence charsequence, int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
        } else {
            int j = this.pattern.length();
            int k = i + j;

            return k > charsequence.length() ? false : (!charsequence.subSequence(i, k).equals(this.pattern) ? false : (!this.rContext.isMatch(charsequence.subSequence(k, charsequence.length())) ? false : this.lContext.isMatch(charsequence.subSequence(0, i))));
        }
    }

    static {
        NameType[] anametype = NameType.values();
        int i = anametype.length;

        for (int j = 0; j < i; ++j) {
            NameType nametype = anametype[j];
            EnumMap enummap = new EnumMap(RuleType.class);
            RuleType[] aruletype = RuleType.values();
            int k = aruletype.length;

            for (int l = 0; l < k; ++l) {
                RuleType ruletype = aruletype[l];
                HashMap hashmap = new HashMap();
                Languages languages = Languages.getInstance(nametype);
                Iterator iterator = languages.getLanguages().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();

                    try {
                        hashmap.put(s, parseRules(createScanner(nametype, ruletype, s), createResourceName(nametype, ruletype, s)));
                    } catch (IllegalStateException illegalstateexception) {
                        throw new IllegalStateException("Problem processing " + createResourceName(nametype, ruletype, s), illegalstateexception);
                    }
                }

                if (!ruletype.equals(RuleType.RULES)) {
                    hashmap.put("common", parseRules(createScanner(nametype, ruletype, "common"), createResourceName(nametype, ruletype, "common")));
                }

                enummap.put(ruletype, Collections.unmodifiableMap(hashmap));
            }

            Rule.RULES.put(nametype, Collections.unmodifiableMap(enummap));
        }

    }

    public interface RPattern {

        boolean isMatch(CharSequence charsequence);
    }

    public static final class PhonemeList implements Rule.PhonemeExpr {

        private final List phonemes;

        public PhonemeList(List list) {
            this.phonemes = list;
        }

        public List getPhonemes() {
            return this.phonemes;
        }
    }

    public interface PhonemeExpr {

        Iterable getPhonemes();
    }

    public static final class Phoneme implements Rule.PhonemeExpr {

        public static final Comparator COMPARATOR = new Comparator() {
            public int compare(Rule.Phoneme rule_phoneme, Rule.Phoneme rule_phoneme1) {
                for (int i = 0; i < rule_phoneme.phonemeText.length(); ++i) {
                    if (i >= rule_phoneme1.phonemeText.length()) {
                        return 1;
                    }

                    int j = rule_phoneme.phonemeText.charAt(i) - rule_phoneme1.phonemeText.charAt(i);

                    if (j != 0) {
                        return j;
                    }
                }

                if (rule_phoneme.phonemeText.length() < rule_phoneme1.phonemeText.length()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
        private final StringBuilder phonemeText;
        private final Languages.LanguageSet languages;

        public Phoneme(CharSequence charsequence, Languages.LanguageSet languages_languageset) {
            this.phonemeText = new StringBuilder(charsequence);
            this.languages = languages_languageset;
        }

        public Phoneme(Rule.Phoneme rule_phoneme, Rule.Phoneme rule_phoneme1) {
            this((CharSequence) rule_phoneme.phonemeText, rule_phoneme.languages);
            this.phonemeText.append(rule_phoneme1.phonemeText);
        }

        public Phoneme(Rule.Phoneme rule_phoneme, Rule.Phoneme rule_phoneme1, Languages.LanguageSet languages_languageset) {
            this((CharSequence) rule_phoneme.phonemeText, languages_languageset);
            this.phonemeText.append(rule_phoneme1.phonemeText);
        }

        public Rule.Phoneme append(CharSequence charsequence) {
            this.phonemeText.append(charsequence);
            return this;
        }

        public Languages.LanguageSet getLanguages() {
            return this.languages;
        }

        public Iterable getPhonemes() {
            return Collections.singleton(this);
        }

        public CharSequence getPhonemeText() {
            return this.phonemeText;
        }

        /** @deprecated */
        @Deprecated
        public Rule.Phoneme join(Rule.Phoneme rule_phoneme) {
            return new Rule.Phoneme(this.phonemeText.toString() + rule_phoneme.phonemeText.toString(), this.languages.restrictTo(rule_phoneme.languages));
        }
    }
}
