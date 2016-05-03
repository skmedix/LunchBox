package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.Collection;
import java.util.Iterator;

final class ParserRules {

    static final char HYPHEN_CHAR = '-';
    static final String HYPHEN = String.valueOf('-');
    static final String DOUBLE_HYPHEN = "--";
    static final String OPTION_TERMINATOR = "--";
    static final String RESERVED_FOR_EXTENSIONS = "W";

    static boolean isShortOptionToken(String argument) {
        return argument.startsWith(ParserRules.HYPHEN) && !ParserRules.HYPHEN.equals(argument) && !isLongOptionToken(argument);
    }

    static boolean isLongOptionToken(String argument) {
        return argument.startsWith("--") && !isOptionTerminator(argument);
    }

    static boolean isOptionTerminator(String argument) {
        return "--".equals(argument);
    }

    static void ensureLegalOption(String option) {
        if (option.startsWith(ParserRules.HYPHEN)) {
            throw new IllegalOptionSpecificationException(String.valueOf(option));
        } else {
            for (int i = 0; i < option.length(); ++i) {
                ensureLegalOptionCharacter(option.charAt(i));
            }

        }
    }

    static void ensureLegalOptions(Collection options) {
        Iterator i$ = options.iterator();

        while (i$.hasNext()) {
            String each = (String) i$.next();

            ensureLegalOption(each);
        }

    }

    private static void ensureLegalOptionCharacter(char option) {
        if (!Character.isLetterOrDigit(option) && !isAllowedPunctuation(option)) {
            throw new IllegalOptionSpecificationException(String.valueOf(option));
        }
    }

    private static boolean isAllowedPunctuation(char option) {
        String allowedPunctuation = "?.-";

        return allowedPunctuation.indexOf(option) != -1;
    }

    static {
        new ParserRules();
    }
}
