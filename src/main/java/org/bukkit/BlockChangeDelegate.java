package org.bukkit;

public interface BlockChangeDelegate {

    /** @deprecated */
    @Deprecated
    boolean setRawTypeId(int i, int j, int k, int l);

    /** @deprecated */
    @Deprecated
    boolean setRawTypeIdAndData(int i, int j, int k, int l, int i1);

    /** @deprecated */
    @Deprecated
    boolean setTypeId(int i, int j, int k, int l);

    /** @deprecated */
    @Deprecated
    boolean setTypeIdAndData(int i, int j, int k, int l, int i1);

    /** @deprecated */
    @Deprecated
    int getTypeId(int i, int j, int k);

    int getHeight();

    boolean isEmpty(int i, int j, int k);
}
