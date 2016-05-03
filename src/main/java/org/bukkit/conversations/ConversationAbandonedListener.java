package org.bukkit.conversations;

import java.util.EventListener;

public interface ConversationAbandonedListener extends EventListener {

    void conversationAbandoned(ConversationAbandonedEvent conversationabandonedevent);
}
