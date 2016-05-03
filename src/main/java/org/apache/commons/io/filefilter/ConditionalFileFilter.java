package org.apache.commons.io.filefilter;

import java.util.List;

public interface ConditionalFileFilter {

    void addFileFilter(IOFileFilter iofilefilter);

    List getFileFilters();

    boolean removeFileFilter(IOFileFilter iofilefilter);

    void setFileFilters(List list);
}
