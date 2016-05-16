package org.bukkit.help;

import net.minecraft.command.ICommand;
import org.bukkit.command.Command;

public interface HelpTopicFactory<ICommand extends Command> {

    HelpTopic createTopic(ICommand command);
}
