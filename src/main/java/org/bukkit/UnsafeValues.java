package org.bukkit;

import java.util.List;
import org.bukkit.inventory.ItemStack;

/** @deprecated */
@Deprecated
public interface UnsafeValues {

    Material getMaterialFromInternalName(String s);

    List tabCompleteInternalMaterialName(String s, List list);

    ItemStack modifyItemStack(ItemStack itemstack, String s);

    Statistic getStatisticFromInternalName(String s);

    Achievement getAchievementFromInternalName(String s);

    List tabCompleteInternalStatisticOrAchievementName(String s, List list);
}
