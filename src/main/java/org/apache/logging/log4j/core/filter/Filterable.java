package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;

public interface Filterable {

    void addFilter(Filter filter);

    void removeFilter(Filter filter);

    Filter getFilter();

    boolean hasFilter();

    boolean isFiltered(LogEvent logevent);
}
