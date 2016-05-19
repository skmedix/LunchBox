package org.bukkit.util;

import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.lang3.Validate;

public class StringUtil {

    public static Collection copyPartialMatches(String token, Iterable originals, Collection collection) throws UnsupportedOperationException, IllegalArgumentException {
        Validate.notNull(token, "Search token cannot be null");
        Validate.notNull(collection, "Collection cannot be null");
        Validate.notNull(originals, "Originals cannot be null");
        Iterator iterator = originals.iterator();

        while (iterator.hasNext()) {
            String string = (String) iterator.next();

            if (startsWithIgnoreCase(string, token)) {
                collection.add(string);
            }
        }

        return collection;
    }

    public static boolean startsWithIgnoreCase(String string, String prefix) throws IllegalArgumentException, NullPointerException {
        Validate.notNull(string, "Cannot check a null string for a match");
        return string.length() < prefix.length() ? false : string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
