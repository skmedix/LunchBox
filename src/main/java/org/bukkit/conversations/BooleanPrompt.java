package org.bukkit.conversations;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;

public abstract class BooleanPrompt extends ValidatingPrompt {

    protected boolean isInputValid(ConversationContext context, String input) {
        String[] accepted = new String[] { "true", "false", "on", "off", "yes", "no", "y", "n", "1", "0", "right", "wrong", "correct", "incorrect", "valid", "invalid"};

        return ArrayUtils.contains(accepted, input.toLowerCase());
    }

    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("y") || input.equals("1") || input.equalsIgnoreCase("right") || input.equalsIgnoreCase("correct") || input.equalsIgnoreCase("valid")) {
            input = "true";
        }

        return this.acceptValidatedInput(context, BooleanUtils.toBoolean(input));
    }

    protected abstract Prompt acceptValidatedInput(ConversationContext conversationcontext, boolean flag);
}
