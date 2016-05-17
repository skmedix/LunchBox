package org.bukkit.conversations;

import org.apache.commons.lang3.math.NumberUtils;

public abstract class NumericPrompt extends ValidatingPrompt {

    protected boolean isInputValid(ConversationContext context, String input) {
        return NumberUtils.isNumber(input) && this.isNumberValid(context, NumberUtils.createNumber(input));
    }

    protected boolean isNumberValid(ConversationContext context, Number input) {
        return true;
    }

    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        try {
            return this.acceptValidatedInput(context, NumberUtils.createNumber(input));
        } catch (NumberFormatException numberformatexception) {
            return this.acceptValidatedInput(context, (Number) NumberUtils.INTEGER_ZERO);
        }
    }

    protected abstract Prompt acceptValidatedInput(ConversationContext conversationcontext, Number number);

    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return NumberUtils.isNumber(invalidInput) ? this.getFailedValidationText(context, NumberUtils.createNumber(invalidInput)) : this.getInputNotNumericText(context, invalidInput);
    }

    protected String getInputNotNumericText(ConversationContext context, String invalidInput) {
        return null;
    }

    protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
        return null;
    }
}
