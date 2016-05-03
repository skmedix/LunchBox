package org.bukkit.conversations;

public interface Conversable {

    boolean isConversing();

    void acceptConversationInput(String s);

    boolean beginConversation(Conversation conversation);

    void abandonConversation(Conversation conversation);

    void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationabandonedevent);

    void sendRawMessage(String s);
}
