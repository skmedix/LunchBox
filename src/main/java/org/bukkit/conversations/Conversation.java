package org.bukkit.conversations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Conversation {

    private Prompt firstPrompt;
    private boolean abandoned;
    protected Prompt currentPrompt;
    protected ConversationContext context;
    protected boolean modal;
    protected boolean localEchoEnabled;
    protected ConversationPrefix prefix;
    protected List cancellers;
    protected List abandonedListeners;

    public Conversation(Plugin plugin, Conversable forWhom, Prompt firstPrompt) {
        this(plugin, forWhom, firstPrompt, new HashMap());
    }

    public Conversation(Plugin plugin, Conversable forWhom, Prompt firstPrompt, Map initialSessionData) {
        this.firstPrompt = firstPrompt;
        this.context = new ConversationContext(plugin, forWhom, initialSessionData);
        this.modal = true;
        this.localEchoEnabled = true;
        this.prefix = new NullConversationPrefix();
        this.cancellers = new ArrayList();
        this.abandonedListeners = new ArrayList();
    }

    public Conversable getForWhom() {
        return this.context.getForWhom();
    }

    public boolean isModal() {
        return this.modal;
    }

    void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isLocalEchoEnabled() {
        return this.localEchoEnabled;
    }

    public void setLocalEchoEnabled(boolean localEchoEnabled) {
        this.localEchoEnabled = localEchoEnabled;
    }

    public ConversationPrefix getPrefix() {
        return this.prefix;
    }

    void setPrefix(ConversationPrefix prefix) {
        this.prefix = prefix;
    }

    void addConversationCanceller(ConversationCanceller canceller) {
        canceller.setConversation(this);
        this.cancellers.add(canceller);
    }

    public List getCancellers() {
        return this.cancellers;
    }

    public ConversationContext getContext() {
        return this.context;
    }

    public void begin() {
        if (this.currentPrompt == null) {
            this.abandoned = false;
            this.currentPrompt = this.firstPrompt;
            this.context.getForWhom().beginConversation(this);
        }

    }

    public Conversation.ConversationState getState() {
        return this.currentPrompt != null ? Conversation.ConversationState.STARTED : (this.abandoned ? Conversation.ConversationState.ABANDONED : Conversation.ConversationState.UNSTARTED);
    }

    public void acceptInput(String input) {
        try {
            if (this.currentPrompt != null) {
                if (this.localEchoEnabled) {
                    this.context.getForWhom().sendRawMessage(this.prefix.getPrefix(this.context) + input);
                }

                Iterator iterator = this.cancellers.iterator();

                while (iterator.hasNext()) {
                    ConversationCanceller t = (ConversationCanceller) iterator.next();

                    if (t.cancelBasedOnInput(this.context, input)) {
                        this.abandon(new ConversationAbandonedEvent(this, t));
                        return;
                    }
                }

                this.currentPrompt = this.currentPrompt.acceptInput(this.context, input);
                this.outputNextPrompt();
            }
        } catch (Throwable throwable) {
            Bukkit.getLogger().log(Level.SEVERE, "Error handling conversation prompt", throwable);
        }

    }

    public synchronized void addConversationAbandonedListener(ConversationAbandonedListener listener) {
        this.abandonedListeners.add(listener);
    }

    public synchronized void removeConversationAbandonedListener(ConversationAbandonedListener listener) {
        this.abandonedListeners.remove(listener);
    }

    public void abandon() {
        this.abandon(new ConversationAbandonedEvent(this, new ManuallyAbandonedConversationCanceller()));
    }

    public synchronized void abandon(ConversationAbandonedEvent details) {
        if (!this.abandoned) {
            this.abandoned = true;
            this.currentPrompt = null;
            this.context.getForWhom().abandonConversation(this);
            Iterator iterator = this.abandonedListeners.iterator();

            while (iterator.hasNext()) {
                ConversationAbandonedListener listener = (ConversationAbandonedListener) iterator.next();

                listener.conversationAbandoned(details);
            }
        }

    }

    public void outputNextPrompt() {
        if (this.currentPrompt == null) {
            this.abandon(new ConversationAbandonedEvent(this));
        } else {
            this.context.getForWhom().sendRawMessage(this.prefix.getPrefix(this.context) + this.currentPrompt.getPromptText(this.context));
            if (!this.currentPrompt.blocksForInput(this.context)) {
                this.currentPrompt = this.currentPrompt.acceptInput(this.context, (String) null);
                this.outputNextPrompt();
            }
        }

    }

    public static enum ConversationState {

        UNSTARTED, STARTED, ABANDONED;
    }
}
