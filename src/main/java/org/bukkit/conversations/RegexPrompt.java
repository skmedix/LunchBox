package org.bukkit.conversations;

import java.util.regex.Pattern;

public abstract class RegexPrompt extends ValidatingPrompt {

    private Pattern pattern;

    public RegexPrompt(String regex) {
        this(Pattern.compile(regex));
    }

    public RegexPrompt(Pattern pattern) {
        this.pattern = pattern;
    }

    private RegexPrompt() {}

    protected boolean isInputValid(ConversationContext context, String input) {
        return this.pattern.matcher(input).matches();
    }
}
