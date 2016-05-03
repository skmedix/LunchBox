package org.bukkit.help;

import org.bukkit.command.Command;

public interface HelpTopicFactory {

    HelpTopic createTopic(Command command);
}
