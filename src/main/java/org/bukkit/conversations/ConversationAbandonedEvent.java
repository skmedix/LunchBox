package org.bukkit.conversations;

import java.util.EventObject;

public class ConversationAbandonedEvent extends EventObject {

    private ConversationContext context;
    private ConversationCanceller canceller;

    public ConversationAbandonedEvent(Conversation conversation) {
        this(conversation, (ConversationCanceller) null);
    }

    public ConversationAbandonedEvent(Conversation conversation, ConversationCanceller canceller) {
        super(conversation);
        this.context = conversation.getContext();
        this.canceller = canceller;
    }

    public ConversationCanceller getCanceller() {
        return this.canceller;
    }

    public ConversationContext getContext() {
        return this.context;
    }

    public boolean gracefulExit() {
        return this.canceller == null;
    }
}
