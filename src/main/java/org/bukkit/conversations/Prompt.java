package org.bukkit.conversations;

public interface Prompt extends Cloneable {

    Prompt END_OF_CONVERSATION = null;

    String getPromptText(ConversationContext conversationcontext);

    boolean blocksForInput(ConversationContext conversationcontext);

    Prompt acceptInput(ConversationContext conversationcontext, String s);
}
