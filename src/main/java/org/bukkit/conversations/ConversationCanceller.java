package org.bukkit.conversations;

public interface ConversationCanceller extends Cloneable {

    void setConversation(Conversation conversation);

    boolean cancelBasedOnInput(ConversationContext conversationcontext, String s);

    ConversationCanceller clone();
}
