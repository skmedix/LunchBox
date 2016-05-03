package org.apache.commons.lang3.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultExceptionContext implements ExceptionContext, Serializable {

    private static final long serialVersionUID = 20110706L;
    private final List contextValues = new ArrayList();

    public DefaultExceptionContext addContextValue(String s, Object object) {
        this.contextValues.add(new ImmutablePair(s, object));
        return this;
    }

    public DefaultExceptionContext setContextValue(String s, Object object) {
        Iterator iterator = this.contextValues.iterator();

        while (iterator.hasNext()) {
            Pair pair = (Pair) iterator.next();

            if (StringUtils.equals(s, (CharSequence) pair.getKey())) {
                iterator.remove();
            }
        }

        this.addContextValue(s, object);
        return this;
    }

    public List getContextValues(String s) {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.contextValues.iterator();

        while (iterator.hasNext()) {
            Pair pair = (Pair) iterator.next();

            if (StringUtils.equals(s, (CharSequence) pair.getKey())) {
                arraylist.add(pair.getValue());
            }
        }

        return arraylist;
    }

    public Object getFirstContextValue(String s) {
        Iterator iterator = this.contextValues.iterator();

        Pair pair;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            pair = (Pair) iterator.next();
        } while (!StringUtils.equals(s, (CharSequence) pair.getKey()));

        return pair.getValue();
    }

    public Set getContextLabels() {
        HashSet hashset = new HashSet();
        Iterator iterator = this.contextValues.iterator();

        while (iterator.hasNext()) {
            Pair pair = (Pair) iterator.next();

            hashset.add(pair.getKey());
        }

        return hashset;
    }

    public List getContextEntries() {
        return this.contextValues;
    }

    public String getFormattedExceptionMessage(String s) {
        StringBuilder stringbuilder = new StringBuilder(256);

        if (s != null) {
            stringbuilder.append(s);
        }

        if (this.contextValues.size() > 0) {
            if (stringbuilder.length() > 0) {
                stringbuilder.append('\n');
            }

            stringbuilder.append("Exception Context:\n");
            int i = 0;

            for (Iterator iterator = this.contextValues.iterator(); iterator.hasNext(); stringbuilder.append("]\n")) {
                Pair pair = (Pair) iterator.next();

                stringbuilder.append("\t[");
                ++i;
                stringbuilder.append(i);
                stringbuilder.append(':');
                stringbuilder.append((String) pair.getKey());
                stringbuilder.append("=");
                Object object = pair.getValue();

                if (object == null) {
                    stringbuilder.append("null");
                } else {
                    String s1;

                    try {
                        s1 = object.toString();
                    } catch (Exception exception) {
                        s1 = "Exception thrown on toString(): " + ExceptionUtils.getStackTrace(exception);
                    }

                    stringbuilder.append(s1);
                }
            }

            stringbuilder.append("---------------------------------");
        }

        return stringbuilder.toString();
    }
}
