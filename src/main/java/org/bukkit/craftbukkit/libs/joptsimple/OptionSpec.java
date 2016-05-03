package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.Collection;
import java.util.List;

public interface OptionSpec {

    List values(OptionSet optionset);

    Object value(OptionSet optionset);

    Collection options();
}
