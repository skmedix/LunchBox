package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class StringsCompleter implements Completer {

    private final SortedSet strings;

    public StringsCompleter() {
        this.strings = new TreeSet();
    }

    public StringsCompleter(Collection strings) {
        this.strings = new TreeSet();
        Preconditions.checkNotNull(strings);
        this.getStrings().addAll(strings);
    }

    public StringsCompleter(String... strings) {
        this((Collection) Arrays.asList(strings));
    }

    public Collection getStrings() {
        return this.strings;
    }

    public int complete(String buffer, int cursor, List candidates) {
        Preconditions.checkNotNull(candidates);
        if (buffer == null) {
            candidates.addAll(this.strings);
        } else {
            Iterator i$ = this.strings.tailSet(buffer).iterator();

            while (i$.hasNext()) {
                String match = (String) i$.next();

                if (!match.startsWith(buffer)) {
                    break;
                }

                candidates.add(match);
            }
        }

        if (candidates.size() == 1) {
            candidates.set(0, candidates.get(0) + " ");
        }

        return candidates.isEmpty() ? -1 : 0;
    }
}
