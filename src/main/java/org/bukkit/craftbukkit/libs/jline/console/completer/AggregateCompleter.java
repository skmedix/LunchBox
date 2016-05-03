package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class AggregateCompleter implements Completer {

    private final List completers;

    public AggregateCompleter() {
        this.completers = new ArrayList();
    }

    public AggregateCompleter(Collection completers) {
        this.completers = new ArrayList();
        Preconditions.checkNotNull(completers);
        this.completers.addAll(completers);
    }

    public AggregateCompleter(Completer... completers) {
        this((Collection) Arrays.asList(completers));
    }

    public Collection getCompleters() {
        return this.completers;
    }

    public int complete(String buffer, int cursor, List candidates) {
        Preconditions.checkNotNull(candidates);
        ArrayList completions = new ArrayList(this.completers.size());
        int max = -1;
        Iterator i$ = this.completers.iterator();

        while (i$.hasNext()) {
            Completer completion = (Completer) i$.next();
            AggregateCompleter.Completion completion1 = new AggregateCompleter.Completion(candidates);

            completion1.complete(completion, buffer, cursor);
            max = Math.max(max, completion1.cursor);
            completions.add(completion1);
        }

        i$ = completions.iterator();

        while (i$.hasNext()) {
            AggregateCompleter.Completion completion2 = (AggregateCompleter.Completion) i$.next();

            if (completion2.cursor == max) {
                candidates.addAll(completion2.candidates);
            }
        }

        return max;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{" + "completers=" + this.completers + '}';
    }

    private class Completion {

        public final List candidates;
        public int cursor;

        public Completion(List candidates) {
            Preconditions.checkNotNull(candidates);
            this.candidates = new LinkedList(candidates);
        }

        public void complete(Completer completer, String buffer, int cursor) {
            Preconditions.checkNotNull(completer);
            this.cursor = completer.complete(buffer, cursor, this.candidates);
        }
    }
}
