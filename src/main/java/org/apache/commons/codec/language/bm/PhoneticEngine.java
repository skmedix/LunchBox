package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PhoneticEngine {

    private static final Map NAME_PREFIXES = new EnumMap(NameType.class);
    private static final int DEFAULT_MAX_PHONEMES = 20;
    private final Lang lang;
    private final NameType nameType;
    private final RuleType ruleType;
    private final boolean concat;
    private final int maxPhonemes;

    private static String join(Iterable iterable, String s) {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = iterable.iterator();

        if (iterator.hasNext()) {
            stringbuilder.append((String) iterator.next());
        }

        while (iterator.hasNext()) {
            stringbuilder.append(s).append((String) iterator.next());
        }

        return stringbuilder.toString();
    }

    public PhoneticEngine(NameType nametype, RuleType ruletype, boolean flag) {
        this(nametype, ruletype, flag, 20);
    }

    public PhoneticEngine(NameType nametype, RuleType ruletype, boolean flag, int i) {
        if (ruletype == RuleType.RULES) {
            throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
        } else {
            this.nameType = nametype;
            this.ruleType = ruletype;
            this.concat = flag;
            this.lang = Lang.instance(nametype);
            this.maxPhonemes = i;
        }
    }

    private PhoneticEngine.PhonemeBuilder applyFinalRules(PhoneticEngine.PhonemeBuilder phoneticengine_phonemebuilder, Map map) {
        if (map == null) {
            throw new NullPointerException("finalRules can not be null");
        } else if (map.isEmpty()) {
            return phoneticengine_phonemebuilder;
        } else {
            TreeSet treeset = new TreeSet(Rule.Phoneme.COMPARATOR);
            Iterator iterator = phoneticengine_phonemebuilder.getPhonemes().iterator();

            while (iterator.hasNext()) {
                Rule.Phoneme rule_phoneme = (Rule.Phoneme) iterator.next();
                PhoneticEngine.PhonemeBuilder phoneticengine_phonemebuilder1 = PhoneticEngine.PhonemeBuilder.empty(rule_phoneme.getLanguages());
                String s = rule_phoneme.getPhonemeText().toString();

                PhoneticEngine.RulesApplication phoneticengine_rulesapplication;

                for (int i = 0; i < s.length(); i = phoneticengine_rulesapplication.getI()) {
                    phoneticengine_rulesapplication = (new PhoneticEngine.RulesApplication(map, s, phoneticengine_phonemebuilder1, i, this.maxPhonemes)).invoke();
                    boolean flag = phoneticengine_rulesapplication.isFound();

                    phoneticengine_phonemebuilder1 = phoneticengine_rulesapplication.getPhonemeBuilder();
                    if (!flag) {
                        phoneticengine_phonemebuilder1.append(s.subSequence(i, i + 1));
                    }
                }

                treeset.addAll(phoneticengine_phonemebuilder1.getPhonemes());
            }

            return new PhoneticEngine.PhonemeBuilder(treeset, (PhoneticEngine.SyntheticClass_1) null);
        }
    }

    public String encode(String s) {
        Languages.LanguageSet languages_languageset = this.lang.guessLanguages(s);

        return this.encode(s, languages_languageset);
    }

    public String encode(String s, Languages.LanguageSet languages_languageset) {
        Map map = Rule.getInstanceMap(this.nameType, RuleType.RULES, languages_languageset);
        Map map1 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
        Map map2 = Rule.getInstanceMap(this.nameType, this.ruleType, languages_languageset);

        s = s.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
        String s1;

        if (this.nameType == NameType.GENERIC) {
            String s2;

            if (s.length() >= 2 && s.substring(0, 2).equals("d\'")) {
                String s3 = s.substring(2);

                s2 = "d" + s3;
                return "(" + this.encode(s3) + ")-(" + this.encode(s2) + ")";
            }

            Iterator iterator = ((Set) PhoneticEngine.NAME_PREFIXES.get(this.nameType)).iterator();

            while (iterator.hasNext()) {
                s2 = (String) iterator.next();
                if (s.startsWith(s2 + " ")) {
                    String s4 = s.substring(s2.length() + 1);

                    s1 = s2 + s4;
                    return "(" + this.encode(s4) + ")-(" + this.encode(s1) + ")";
                }
            }
        }

        List list = Arrays.asList(s.split("\\s+"));
        ArrayList arraylist = new ArrayList();

        switch (PhoneticEngine.SyntheticClass_1.$SwitchMap$org$apache$commons$codec$language$bm$NameType[this.nameType.ordinal()]) {
        case 1:
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                s1 = (String) iterator1.next();
                String[] astring = s1.split("\'");
                String s5 = astring[astring.length - 1];

                arraylist.add(s5);
            }

            arraylist.removeAll((Collection) PhoneticEngine.NAME_PREFIXES.get(this.nameType));
            break;

        case 2:
            arraylist.addAll(list);
            arraylist.removeAll((Collection) PhoneticEngine.NAME_PREFIXES.get(this.nameType));
            break;

        case 3:
            arraylist.addAll(list);
            break;

        default:
            throw new IllegalStateException("Unreachable case: " + this.nameType);
        }

        if (this.concat) {
            s = join(arraylist, " ");
        } else {
            if (arraylist.size() != 1) {
                StringBuilder stringbuilder = new StringBuilder();
                Iterator iterator2 = arraylist.iterator();

                while (iterator2.hasNext()) {
                    String s6 = (String) iterator2.next();

                    stringbuilder.append("-").append(this.encode(s6));
                }

                return stringbuilder.substring(1);
            }

            s = (String) list.iterator().next();
        }

        PhoneticEngine.PhonemeBuilder phoneticengine_phonemebuilder = PhoneticEngine.PhonemeBuilder.empty(languages_languageset);

        PhoneticEngine.RulesApplication phoneticengine_rulesapplication;

        for (int i = 0; i < s.length(); phoneticengine_phonemebuilder = phoneticengine_rulesapplication.getPhonemeBuilder()) {
            phoneticengine_rulesapplication = (new PhoneticEngine.RulesApplication(map, s, phoneticengine_phonemebuilder, i, this.maxPhonemes)).invoke();
            i = phoneticengine_rulesapplication.getI();
        }

        phoneticengine_phonemebuilder = this.applyFinalRules(phoneticengine_phonemebuilder, map1);
        phoneticengine_phonemebuilder = this.applyFinalRules(phoneticengine_phonemebuilder, map2);
        return phoneticengine_phonemebuilder.makeString();
    }

    public Lang getLang() {
        return this.lang;
    }

    public NameType getNameType() {
        return this.nameType;
    }

    public RuleType getRuleType() {
        return this.ruleType;
    }

    public boolean isConcat() {
        return this.concat;
    }

    public int getMaxPhonemes() {
        return this.maxPhonemes;
    }

    static {
        PhoneticEngine.NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "bar", "ben", "da", "de", "van", "von"}))));
        PhoneticEngine.NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
        PhoneticEngine.NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$apache$commons$codec$language$bm$NameType = new int[NameType.values().length];

        static {
            try {
                PhoneticEngine.SyntheticClass_1.$SwitchMap$org$apache$commons$codec$language$bm$NameType[NameType.SEPHARDIC.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                PhoneticEngine.SyntheticClass_1.$SwitchMap$org$apache$commons$codec$language$bm$NameType[NameType.ASHKENAZI.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                PhoneticEngine.SyntheticClass_1.$SwitchMap$org$apache$commons$codec$language$bm$NameType[NameType.GENERIC.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

        }
    }

    private static final class RulesApplication {

        private final Map finalRules;
        private final CharSequence input;
        private PhoneticEngine.PhonemeBuilder phonemeBuilder;
        private int i;
        private final int maxPhonemes;
        private boolean found;

        public RulesApplication(Map map, CharSequence charsequence, PhoneticEngine.PhonemeBuilder phoneticengine_phonemebuilder, int i, int j) {
            if (map == null) {
                throw new NullPointerException("The finalRules argument must not be null");
            } else {
                this.finalRules = map;
                this.phonemeBuilder = phoneticengine_phonemebuilder;
                this.input = charsequence;
                this.i = i;
                this.maxPhonemes = j;
            }
        }

        public int getI() {
            return this.i;
        }

        public PhoneticEngine.PhonemeBuilder getPhonemeBuilder() {
            return this.phonemeBuilder;
        }

        public PhoneticEngine.RulesApplication invoke() {
            this.found = false;
            int i = 1;
            List list = (List) this.finalRules.get(this.input.subSequence(this.i, this.i + i));

            if (list != null) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Rule rule = (Rule) iterator.next();
                    String s = rule.getPattern();

                    i = s.length();
                    if (rule.patternAndContextMatches(this.input, this.i)) {
                        this.phonemeBuilder.apply(rule.getPhoneme(), this.maxPhonemes);
                        this.found = true;
                        break;
                    }
                }
            }

            if (!this.found) {
                i = 1;
            }

            this.i += i;
            return this;
        }

        public boolean isFound() {
            return this.found;
        }
    }

    static final class PhonemeBuilder {

        private final Set phonemes;

        public static PhoneticEngine.PhonemeBuilder empty(Languages.LanguageSet languages_languageset) {
            return new PhoneticEngine.PhonemeBuilder(new Rule.Phoneme("", languages_languageset));
        }

        private PhonemeBuilder(Rule.Phoneme rule_phoneme) {
            this.phonemes = new LinkedHashSet();
            this.phonemes.add(rule_phoneme);
        }

        private PhonemeBuilder(Set set) {
            this.phonemes = set;
        }

        public void append(CharSequence charsequence) {
            Iterator iterator = this.phonemes.iterator();

            while (iterator.hasNext()) {
                Rule.Phoneme rule_phoneme = (Rule.Phoneme) iterator.next();

                rule_phoneme.append(charsequence);
            }

        }

        public void apply(Rule.PhonemeExpr rule_phonemeexpr, int i) {
            LinkedHashSet linkedhashset = new LinkedHashSet(i);
            Iterator iterator = this.phonemes.iterator();

            label25:
            while (iterator.hasNext()) {
                Rule.Phoneme rule_phoneme = (Rule.Phoneme) iterator.next();
                Iterator iterator1 = rule_phonemeexpr.getPhonemes().iterator();

                while (iterator1.hasNext()) {
                    Rule.Phoneme rule_phoneme1 = (Rule.Phoneme) iterator1.next();
                    Languages.LanguageSet languages_languageset = rule_phoneme.getLanguages().restrictTo(rule_phoneme1.getLanguages());

                    if (!languages_languageset.isEmpty()) {
                        Rule.Phoneme rule_phoneme2 = new Rule.Phoneme(rule_phoneme, rule_phoneme1, languages_languageset);

                        if (linkedhashset.size() < i) {
                            linkedhashset.add(rule_phoneme2);
                            if (linkedhashset.size() >= i) {
                                break label25;
                            }
                        }
                    }
                }
            }

            this.phonemes.clear();
            this.phonemes.addAll(linkedhashset);
        }

        public Set getPhonemes() {
            return this.phonemes;
        }

        public String makeString() {
            StringBuilder stringbuilder = new StringBuilder();

            Rule.Phoneme rule_phoneme;

            for (Iterator iterator = this.phonemes.iterator(); iterator.hasNext(); stringbuilder.append(rule_phoneme.getPhonemeText())) {
                rule_phoneme = (Rule.Phoneme) iterator.next();
                if (stringbuilder.length() > 0) {
                    stringbuilder.append("|");
                }
            }

            return stringbuilder.toString();
        }

        PhonemeBuilder(Set set, PhoneticEngine.SyntheticClass_1 phoneticengine_syntheticclass_1) {
            this(set);
        }
    }
}
