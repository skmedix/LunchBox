package org.apache.commons.lang3.builder;

import java.util.Collection;
import org.apache.commons.lang3.ClassUtils;

public class RecursiveToStringStyle extends ToStringStyle {

    private static final long serialVersionUID = 1L;

    public void appendDetail(StringBuffer stringbuffer, String s, Object object) {
        if (!ClassUtils.isPrimitiveWrapper(object.getClass()) && !String.class.equals(object.getClass()) && this.accept(object.getClass())) {
            stringbuffer.append(ReflectionToStringBuilder.toString(object, this));
        } else {
            super.appendDetail(stringbuffer, s, object);
        }

    }

    protected void appendDetail(StringBuffer stringbuffer, String s, Collection collection) {
        this.appendClassName(stringbuffer, collection);
        this.appendIdentityHashCode(stringbuffer, collection);
        this.appendDetail(stringbuffer, s, collection.toArray());
    }

    protected boolean accept(Class oclass) {
        return true;
    }
}
