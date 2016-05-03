package org.bukkit.conversations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class PlayerNamePrompt extends ValidatingPrompt {

    private Plugin plugin;

    public PlayerNamePrompt(Plugin plugin) {
        this.plugin = plugin;
    }

    protected boolean isInputValid(ConversationContext context, String input) {
        return this.plugin.getServer().getPlayer(input) != null;
    }

    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        return this.acceptValidatedInput(context, this.plugin.getServer().getPlayer(input));
    }

    protected abstract Prompt acceptValidatedInput(ConversationContext conversationcontext, Player player);
}
