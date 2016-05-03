package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class CouchDBObject implements NoSQLObject {

    private final Map map = new HashMap();

    public void set(String s, Object object) {
        this.map.put(s, object);
    }

    public void set(String s, NoSQLObject nosqlobject) {
        this.map.put(s, nosqlobject.unwrap());
    }

    public void set(String s, Object[] aobject) {
        this.map.put(s, Arrays.asList(aobject));
    }

    public void set(String s, NoSQLObject[] anosqlobject) {
        ArrayList arraylist = new ArrayList();
        NoSQLObject[] anosqlobject1 = anosqlobject;
        int i = anosqlobject.length;

        for (int j = 0; j < i; ++j) {
            NoSQLObject nosqlobject = anosqlobject1[j];

            arraylist.add(nosqlobject.unwrap());
        }

        this.map.put(s, arraylist);
    }

    public Map unwrap() {
        return this.map;
    }
}
