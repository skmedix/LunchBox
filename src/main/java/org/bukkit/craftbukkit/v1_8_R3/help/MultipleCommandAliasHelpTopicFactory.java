package org.bukkit.craftbukkit.v1_8_R3.help;

import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

public class MultipleCommandAliasHelpTopicFactory implements HelpTopicFactory {

    public HelpTopic createTopic(MultipleCommandAlias multipleCommandAlias) {
        return new MultipleCommandAliasHelpTopic(multipleCommandAlias);
    }
}
