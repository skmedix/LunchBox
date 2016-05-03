package org.spigotmc;

import gnu.trove.map.hash.TCustomHashMap;
import java.util.Map;

public class CaseInsensitiveMap extends TCustomHashMap {

    public CaseInsensitiveMap() {
        super(CaseInsensitiveHashingStrategy.INSTANCE);
    }

    public CaseInsensitiveMap(Map map) {
        super(CaseInsensitiveHashingStrategy.INSTANCE, map);
    }
}
