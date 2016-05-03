package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(
    name = "filters",
    category = "Core",
    printObject = true
)
public final class CompositeFilter implements Iterable, Filter, LifeCycle {

    private final List filters;
    private final boolean hasFilters;
    private boolean isStarted;

    private CompositeFilter() {
        this.filters = new ArrayList();
        this.hasFilters = false;
    }

    private CompositeFilter(List list) {
        if (list == null) {
            this.filters = Collections.unmodifiableList(new ArrayList());
            this.hasFilters = false;
        } else {
            this.filters = Collections.unmodifiableList(list);
            this.hasFilters = this.filters.size() > 0;
        }
    }

    public CompositeFilter addFilter(Filter filter) {
        ArrayList arraylist = new ArrayList(this.filters);

        arraylist.add(filter);
        return new CompositeFilter(Collections.unmodifiableList(arraylist));
    }

    public CompositeFilter removeFilter(Filter filter) {
        ArrayList arraylist = new ArrayList(this.filters);

        arraylist.remove(filter);
        return new CompositeFilter(Collections.unmodifiableList(arraylist));
    }

    public Iterator iterator() {
        return this.filters.iterator();
    }

    public List getFilters() {
        return this.filters;
    }

    public boolean hasFilters() {
        return this.hasFilters;
    }

    public int size() {
        return this.filters.size();
    }

    public void start() {
        Iterator iterator = this.filters.iterator();

        while (iterator.hasNext()) {
            Filter filter = (Filter) iterator.next();

            if (filter instanceof LifeCycle) {
                ((LifeCycle) filter).start();
            }
        }

        this.isStarted = true;
    }

    public void stop() {
        Iterator iterator = this.filters.iterator();

        while (iterator.hasNext()) {
            Filter filter = (Filter) iterator.next();

            if (filter instanceof LifeCycle) {
                ((LifeCycle) filter).stop();
            }
        }

        this.isStarted = false;
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public Filter.Result getOnMismatch() {
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result getOnMatch() {
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String s, Object... aobject) {
        Filter.Result filter_result = Filter.Result.NEUTRAL;
        Iterator iterator = this.filters.iterator();

        do {
            if (!iterator.hasNext()) {
                return filter_result;
            }

            Filter filter = (Filter) iterator.next();

            filter_result = filter.filter(logger, level, marker, s, aobject);
        } while (filter_result != Filter.Result.ACCEPT && filter_result != Filter.Result.DENY);

        return filter_result;
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable) {
        Filter.Result filter_result = Filter.Result.NEUTRAL;
        Iterator iterator = this.filters.iterator();

        do {
            if (!iterator.hasNext()) {
                return filter_result;
            }

            Filter filter = (Filter) iterator.next();

            filter_result = filter.filter(logger, level, marker, object, throwable);
        } while (filter_result != Filter.Result.ACCEPT && filter_result != Filter.Result.DENY);

        return filter_result;
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        Filter.Result filter_result = Filter.Result.NEUTRAL;
        Iterator iterator = this.filters.iterator();

        do {
            if (!iterator.hasNext()) {
                return filter_result;
            }

            Filter filter = (Filter) iterator.next();

            filter_result = filter.filter(logger, level, marker, message, throwable);
        } while (filter_result != Filter.Result.ACCEPT && filter_result != Filter.Result.DENY);

        return filter_result;
    }

    public Filter.Result filter(LogEvent logevent) {
        Filter.Result filter_result = Filter.Result.NEUTRAL;
        Iterator iterator = this.filters.iterator();

        do {
            if (!iterator.hasNext()) {
                return filter_result;
            }

            Filter filter = (Filter) iterator.next();

            filter_result = filter.filter(logevent);
        } while (filter_result != Filter.Result.ACCEPT && filter_result != Filter.Result.DENY);

        return filter_result;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        Filter filter;

        for (Iterator iterator = this.filters.iterator(); iterator.hasNext(); stringbuilder.append(filter.toString())) {
            filter = (Filter) iterator.next();
            if (stringbuilder.length() == 0) {
                stringbuilder.append("{");
            } else {
                stringbuilder.append(", ");
            }
        }

        if (stringbuilder.length() > 0) {
            stringbuilder.append("}");
        }

        return stringbuilder.toString();
    }

    @PluginFactory
    public static CompositeFilter createFilters(@PluginElement("Filters") Filter[] afilter) {
        Object object = afilter != null && afilter.length != 0 ? Arrays.asList(afilter) : new ArrayList();

        return new CompositeFilter((List) object);
    }
}
