package org.bukkit.craftbukkit.libs.jline.console.completer;

import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class EnumCompleter extends StringsCompleter {

    public EnumCompleter(Class source) {
        Preconditions.checkNotNull(source);
        Enum[] arr$ = (Enum[]) source.getEnumConstants();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Enum n = arr$[i$];

            this.getStrings().add(n.name().toLowerCase());
        }

    }
}
