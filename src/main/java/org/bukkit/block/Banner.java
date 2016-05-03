package org.bukkit.block;

import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

public interface Banner extends BlockState {

    DyeColor getBaseColor();

    void setBaseColor(DyeColor dyecolor);

    List getPatterns();

    void setPatterns(List list);

    void addPattern(Pattern pattern);

    Pattern getPattern(int i);

    Pattern removePattern(int i);

    void setPattern(int i, Pattern pattern);

    int numberOfPatterns();
}
