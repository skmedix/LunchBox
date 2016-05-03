package org.bukkit.command;

import java.util.List;

/** @deprecated */
@Deprecated
public interface TabCommandExecutor extends CommandExecutor {

    List onTabComplete();
}
