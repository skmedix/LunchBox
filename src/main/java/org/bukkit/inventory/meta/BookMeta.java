package org.bukkit.inventory.meta;

import java.util.List;

public interface BookMeta extends ItemMeta {

    boolean hasTitle();

    String getTitle();

    boolean setTitle(String s);

    boolean hasAuthor();

    String getAuthor();

    void setAuthor(String s);

    boolean hasPages();

    String getPage(int i);

    void setPage(int i, String s);

    List getPages();

    void setPages(List list);

    void setPages(String... astring);

    void addPage(String... astring);

    int getPageCount();

    BookMeta clone();
}
