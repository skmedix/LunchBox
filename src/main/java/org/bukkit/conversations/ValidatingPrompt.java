package org.bukkit.conversations;

import org.bukkit.ChatColor;

public abstract class ValidatingPrompt implements Prompt {

    public Prompt acceptInput(ConversationContext context, String input) {
        if (this.isInputValid(context, input)) {
            return this.acceptValidatedInput(context, input);
        } else {
            String failPrompt = this.getFailedValidationText(context, input);

            if (failPrompt != null) {
                context.getForWhom().sendRawMessage(ChatColor.RED + failPrompt);
            }

            return this;
        }
    }

    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    protected abstract boolean isInputValid(ConversationContext conversationcontext, String s);

    protected abstract Prompt acceptValidatedInput(ConversationContext conversationcontext, String s);

    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return null;
    }
}
