package org.bukkit.conversations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public abstract class FixedSetPrompt extends ValidatingPrompt {

    protected List fixedSet;

    public FixedSetPrompt(String... fixedSet) {
        this.fixedSet = Arrays.asList(fixedSet);
    }

    private FixedSetPrompt() {}

    protected boolean isInputValid(ConversationContext context, String input) {
        return this.fixedSet.contains(input);
    }

    protected String formatFixedSet() {
        return "[" + StringUtils.join((Collection) this.fixedSet, ", ") + "]";
    }
}
