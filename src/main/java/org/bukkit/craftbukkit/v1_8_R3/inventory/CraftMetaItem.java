package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_8_R3.Overridden;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.spigotmc.ValidateUtils;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaItem implements ItemMeta, Repairable {

    static final CraftMetaItem.ItemMetaKey NAME = new CraftMetaItem.ItemMetaKey("Name", "display-name");
    static final CraftMetaItem.ItemMetaKey DISPLAY = new CraftMetaItem.ItemMetaKey("display");
    static final CraftMetaItem.ItemMetaKey LORE = new CraftMetaItem.ItemMetaKey("Lore", "lore");
    static final CraftMetaItem.ItemMetaKey ENCHANTMENTS = new CraftMetaItem.ItemMetaKey("ench", "enchants");
    static final CraftMetaItem.ItemMetaKey ENCHANTMENTS_ID = new CraftMetaItem.ItemMetaKey("id");
    static final CraftMetaItem.ItemMetaKey ENCHANTMENTS_LVL = new CraftMetaItem.ItemMetaKey("lvl");
    static final CraftMetaItem.ItemMetaKey REPAIR = new CraftMetaItem.ItemMetaKey("RepairCost", "repair-cost");
    static final CraftMetaItem.ItemMetaKey ATTRIBUTES = new CraftMetaItem.ItemMetaKey("AttributeModifiers");
    static final CraftMetaItem.ItemMetaKey ATTRIBUTES_IDENTIFIER = new CraftMetaItem.ItemMetaKey("AttributeName");
    static final CraftMetaItem.ItemMetaKey ATTRIBUTES_NAME = new CraftMetaItem.ItemMetaKey("Name");
    static final CraftMetaItem.ItemMetaKey ATTRIBUTES_VALUE = new CraftMetaItem.ItemMetaKey("Amount");
    static final CraftMetaItem.ItemMetaKey ATTRIBUTES_TYPE = new CraftMetaItem.ItemMetaKey("Operation");
    static final CraftMetaItem.ItemMetaKey ATTRIBUTES_UUID_HIGH = new CraftMetaItem.ItemMetaKey("UUIDMost");
    static final CraftMetaItem.ItemMetaKey ATTRIBUTES_UUID_LOW = new CraftMetaItem.ItemMetaKey("UUIDLeast");
    static final CraftMetaItem.ItemMetaKey HIDEFLAGS = new CraftMetaItem.ItemMetaKey("HideFlags", "ItemFlags");
    static final CraftMetaItem.ItemMetaKey UNBREAKABLE = new CraftMetaItem.ItemMetaKey("Unbreakable");
    private String displayName;
    private List lore;
    private Map enchantments;
    private int repairCost;
    private int hideFlag;
    private static final Set HANDLED_TAGS = Sets.newHashSet();
    private final Map unhandledTags = new HashMap();
    private final ItemMeta.Spigot spigot = new ItemMeta.Spigot() {
        private boolean unbreakable;

        public void setUnbreakable(boolean setUnbreakable) {
            this.unbreakable = setUnbreakable;
        }

        public boolean isUnbreakable() {
            return this.unbreakable;
        }
    };

    CraftMetaItem(CraftMetaItem meta) {
        if (meta != null) {
            this.displayName = meta.displayName;
            if (meta.hasLore()) {
                this.lore = new ArrayList(meta.lore);
            }

            if (meta.enchantments != null) {
                this.enchantments = new HashMap(meta.enchantments);
            }

            this.repairCost = meta.repairCost;
            this.hideFlag = meta.hideFlag;
            this.unhandledTags.putAll(meta.unhandledTags);
            this.spigot.setUnbreakable(meta.spigot.isUnbreakable());
        }
    }

    CraftMetaItem(NBTTagCompound tag) {
        NBTTagList key;

        if (tag.hasKey(CraftMetaItem.DISPLAY.NBT)) {
            NBTTagCompound keys = tag.getCompoundTag(CraftMetaItem.DISPLAY.NBT);

            if (keys.hasKey(CraftMetaItem.NAME.NBT)) {
                this.displayName = ValidateUtils.limit(keys.getString(CraftMetaItem.NAME.NBT), 1024);
            }

            if (keys.hasKey(CraftMetaItem.LORE.NBT)) {
                key = keys.getTagList(CraftMetaItem.LORE.NBT, 8);
                this.lore = new ArrayList(key.size());

                for (int attributeTracker = 0; attributeTracker < key.size(); ++attributeTracker) {
                    String attributeTrackerX = ValidateUtils.limit(key.getString(attributeTracker), 1024);

                    this.lore.add(attributeTrackerX);
                }
            }
        }

        this.enchantments = buildEnchantments(tag, CraftMetaItem.ENCHANTMENTS);
        if (tag.hasKey(CraftMetaItem.REPAIR.NBT)) {
            this.repairCost = tag.getInteger(CraftMetaItem.REPAIR.NBT);
        }

        if (tag.hasKey(CraftMetaItem.HIDEFLAGS.NBT)) {
            this.hideFlag = tag.getInteger(CraftMetaItem.HIDEFLAGS.NBT);
        }

        if (tag.getTag(CraftMetaItem.ATTRIBUTES.NBT) instanceof NBTTagList) {
            NBTTagList nbttaglist = null;

            key = tag.getTagList(CraftMetaItem.ATTRIBUTES.NBT, 10);
            TObjectDoubleHashMap tobjectdoublehashmap = new TObjectDoubleHashMap();
            TObjectDoubleHashMap tobjectdoublehashmap1 = new TObjectDoubleHashMap();
            HashMap attributesByName = new HashMap();

            //todo: am not sure what GenericAttributes is in forge.
            tobjectdoublehashmap.put("generic.maxHealth", 20.0D);
            attributesByName.put("generic.maxHealth", GenericAttributes.maxHealth);
            tobjectdoublehashmap.put("generic.followRange", 32.0D);
            attributesByName.put("generic.followRange", GenericAttributes.FOLLOW_RANGE);
            tobjectdoublehashmap.put("generic.knockbackResistance", 0.0D);
            attributesByName.put("generic.knockbackResistance", GenericAttributes.c);
            tobjectdoublehashmap.put("generic.movementSpeed", 0.7D);
            attributesByName.put("generic.movementSpeed", GenericAttributes.MOVEMENT_SPEED);
            tobjectdoublehashmap.put("generic.attackDamage", 1.0D);
            attributesByName.put("generic.attackDamage", GenericAttributes.ATTACK_DAMAGE);
            NBTTagList oldList = key;

            key = new NBTTagList();
            ArrayList op0 = new ArrayList();
            ArrayList op1 = new ArrayList();
            ArrayList op2 = new ArrayList();

            int i;
            NBTTagCompound nbttagcompound;

            for (i = 0; i < oldList.size(); ++i) {
                nbttagcompound = (NBTTagCompound) oldList.get(i);
                if (nbttagcompound != null && nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT, 99) && nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT, 99) && nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString && CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT)) && nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_NAME.NBT) instanceof NBTTagString && !nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_NAME.NBT).isEmpty() && nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_VALUE.NBT, 99) && nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_TYPE.NBT, 99) && nbttagcompound.getInteger(CraftMetaItem.ATTRIBUTES_TYPE.NBT) >= 0 && nbttagcompound.getInteger(CraftMetaItem.ATTRIBUTES_TYPE.NBT) <= 2) {
                    switch (nbttagcompound.getInteger(CraftMetaItem.ATTRIBUTES_TYPE.NBT)) {
                    case 0:
                        op0.add(nbttagcompound);
                        break;

                    case 1:
                        op1.add(nbttagcompound);
                        break;

                    case 2:
                        op2.add(nbttagcompound);
                    }
                }
            }

            Iterator iterator = op0.iterator();

            String entry;
            double val;
            //NBTTagCompound nbttagcompound;

            while (iterator.hasNext()) {
                nbttagcompound = (NBTTagCompound) iterator.next();
                entry = nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT);
                if (tobjectdoublehashmap.containsKey(entry)) {
                    val = tobjectdoublehashmap.get(entry);
                    val += nbttagcompound.getDouble(CraftMetaItem.ATTRIBUTES_VALUE.NBT);
                    if (val != ((IAttribute) attributesByName.get(entry)).a(val)) {
                        continue;
                    }

                    tobjectdoublehashmap.put(entry, val);
                }

                key.appendTag(nbttagcompound);
            }

            iterator = tobjectdoublehashmap.keySet().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                tobjectdoublehashmap1.put(s, tobjectdoublehashmap.get(s));
            }

            iterator = op1.iterator();

            while (iterator.hasNext()) {
                nbttagcompound = (NBTTagCompound) iterator.next();
                entry = nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT);
                if (tobjectdoublehashmap.containsKey(entry)) {
                    val = tobjectdoublehashmap.get(entry);
                    double valX = tobjectdoublehashmap1.get(entry);

                    val += valX * nbttagcompound.getDouble(CraftMetaItem.ATTRIBUTES_VALUE.NBT);
                    if (val != ((IAttribute) attributesByName.get(entry)).clampValue(val)) {
                        continue;
                    }

                    tobjectdoublehashmap.put(entry, val);
                }

                key.appendTag(nbttagcompound);
            }

            iterator = op2.iterator();

            while (iterator.hasNext()) {
                nbttagcompound = (NBTTagCompound) iterator.next();
                entry = nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT);
                if (tobjectdoublehashmap.containsKey(entry)) {
                    val = tobjectdoublehashmap.get(entry);
                    val += val * nbttagcompound.getDouble(CraftMetaItem.ATTRIBUTES_VALUE.NBT);
                    if (val != ((IAttribute) attributesByName.get(entry)).clampValue(val)) {
                        continue;
                    }

                    tobjectdoublehashmap.put(entry, val);
                }

                key.appendTag(nbttagcompound);
            }

            for (i = 0; i < key.size(); ++i) {
                if (key.get(i) instanceof NBTTagCompound) {
                    nbttagcompound = (NBTTagCompound) key.get(i);
                    if (nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT, 99) && nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT, 99) && nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString && CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT)) && nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_NAME.NBT) instanceof NBTTagString && !nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_NAME.NBT).isEmpty() && nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_VALUE.NBT, 99) && nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_TYPE.NBT, 99) && nbttagcompound.getInteger(CraftMetaItem.ATTRIBUTES_TYPE.NBT) >= 0 && nbttagcompound.getInteger(CraftMetaItem.ATTRIBUTES_TYPE.NBT) <= 2) {
                        if (nbttaglist == null) {
                            nbttaglist = new NBTTagList();
                        }

                        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                        nbttagcompound1.setTag(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT));
                        nbttagcompound1.setTag(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT));
                        nbttagcompound1.setTag(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT));
                        nbttagcompound1.setTag(CraftMetaItem.ATTRIBUTES_NAME.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_NAME.NBT));
                        nbttagcompound1.setTag(CraftMetaItem.ATTRIBUTES_VALUE.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_VALUE.NBT));
                        nbttagcompound1.setTag(CraftMetaItem.ATTRIBUTES_TYPE.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_TYPE.NBT));
                        nbttaglist.appendTag(nbttagcompound1);
                    }
                }
            }

            this.unhandledTags.put(CraftMetaItem.ATTRIBUTES.NBT, nbttaglist);
        }

        Set set = tag.c();
        Iterator iterator1 = set.iterator();

        while (iterator1.hasNext()) {
            String s1 = (String) iterator1.next();

            if (!getHandledTags().contains(s1)) {
                this.unhandledTags.put(s1, tag.getString(s1));
            }
        }

        if (tag.hasKey(CraftMetaItem.UNBREAKABLE.NBT)) {
            this.spigot.setUnbreakable(tag.getBoolean(CraftMetaItem.UNBREAKABLE.NBT));
        }

    }

    static Map buildEnchantments(NBTTagCompound tag, CraftMetaItem.ItemMetaKey key) {
        if (!tag.hasKey(key.NBT)) {
            return null;
        } else {
            NBTTagList ench = tag.getTagList(key.NBT, 10);
            HashMap enchantments = new HashMap(ench.size());

            for (int i = 0; i < ench.size(); ++i) {
                int id = '\uffff' & ench.get(i).getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
                int level = '\uffff' & ench.get(i).getShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
                Enchantment e = Enchantment.getById(id);

                if (e != null) {
                    enchantments.put(e, Integer.valueOf(level));
                }
            }

            return enchantments;
        }
    }

    CraftMetaItem(Map map) {
        this.setDisplayName(CraftMetaItem.SerializableMeta.getString(map, CraftMetaItem.NAME.BUKKIT, true));
        Iterable lore = (Iterable) CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, CraftMetaItem.LORE.BUKKIT, true);

        if (lore != null) {
            safelyAdd(lore, this.lore = new ArrayList(), Integer.MAX_VALUE);
        }

        this.enchantments = buildEnchantments(map, CraftMetaItem.ENCHANTMENTS);
        Integer repairCost = (Integer) CraftMetaItem.SerializableMeta.getObject(Integer.class, map, CraftMetaItem.REPAIR.BUKKIT, true);

        if (repairCost != null) {
            this.setRepairCost(repairCost.intValue());
        }

        Set hideFlags = (Set) CraftMetaItem.SerializableMeta.getObject(Set.class, map, CraftMetaItem.HIDEFLAGS.BUKKIT, true);

        if (hideFlags != null) {
            Iterator internal = hideFlags.iterator();

            while (internal.hasNext()) {
                Object unbreakable = internal.next();
                String buf = (String) unbreakable;

                try {
                    ItemFlag ex = ItemFlag.valueOf(buf);

                    this.addItemFlags(new ItemFlag[] { ex});
                } catch (IllegalArgumentException illegalargumentexception) {
                    ;
                }
            }
        }

        Boolean unbreakable1 = (Boolean) CraftMetaItem.SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.UNBREAKABLE.BUKKIT, true);

        if (unbreakable1 != null) {
            this.spigot.setUnbreakable(unbreakable1.booleanValue());
        }

        String internal1 = CraftMetaItem.SerializableMeta.getString(map, "internal", true);

        if (internal1 != null) {
            ByteArrayInputStream buf1 = new ByteArrayInputStream(Base64.decodeBase64(internal1));

            try {
                NBTTagCompound ex1 = NBTCompressedStreamTools.a((InputStream) buf1);

                this.deserializeInternal(ex1);
                Set keys = ex1.c();
                Iterator iterator = keys.iterator();

                while (iterator.hasNext()) {
                    String key = (String) iterator.next();

                    if (!getHandledTags().contains(key)) {
                        this.unhandledTags.put(key, ex1.getString(key));
                    }
                }
            } catch (IOException ioexception) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, (String) null, ioexception);
            }
        }

    }

    void deserializeInternal(NBTTagCompound tag) {}

    static Map buildEnchantments(Map map, CraftMetaItem.ItemMetaKey key) {
        Map ench = (Map) CraftMetaItem.SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);

        if (ench == null) {
            return null;
        } else {
            HashMap enchantments = new HashMap(ench.size());
            Iterator iterator = ench.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());

                if (enchantment != null && entry.getValue() instanceof Integer) {
                    enchantments.put(enchantment, (Integer) entry.getValue());
                }
            }

            return enchantments;
        }
    }

    @Overridden
    void applyToItem(NBTTagCompound itemTag) {
        if (this.hasDisplayName()) {
            this.setDisplayTag(itemTag, CraftMetaItem.NAME.NBT, new NBTTagString(this.displayName));
        }

        if (this.hasLore()) {
            this.setDisplayTag(itemTag, CraftMetaItem.LORE.NBT, createStringList(this.lore));
        }

        if (this.hideFlag != 0) {
            itemTag.setInteger(CraftMetaItem.HIDEFLAGS.NBT, this.hideFlag);
        }

        applyEnchantments(this.enchantments, itemTag, CraftMetaItem.ENCHANTMENTS);
        if (this.spigot.isUnbreakable()) {
            itemTag.setBoolean(CraftMetaItem.UNBREAKABLE.NBT, true);
        }

        if (this.hasRepairCost()) {
            itemTag.setInteger(CraftMetaItem.REPAIR.NBT, this.repairCost);
        }

        Iterator iterator = this.unhandledTags.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry e = (Entry) iterator.next();

            itemTag.setTag((String) e.getKey(), (NBTBase) e.getValue());
        }

    }

    static NBTTagList createStringList(List list) {
        if (list != null && !list.isEmpty()) {
            NBTTagList tagList = new NBTTagList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String value = (String) iterator.next();

                tagList.appendTag(new NBTTagString(value));
            }

            return tagList;
        } else {
            return null;
        }
    }

    static void applyEnchantments(Map enchantments, NBTTagCompound tag, CraftMetaItem.ItemMetaKey key) {
        if (enchantments != null) {
            NBTTagList list = new NBTTagList();
            Iterator iterator = enchantments.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                NBTTagCompound subtag = new NBTTagCompound();

                subtag.setShort(CraftMetaItem.ENCHANTMENTS_ID.NBT, (short) ((Enchantment) entry.getKey()).getId());
                subtag.setShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT, ((Integer) entry.getValue()).shortValue());
                list.appendTag(subtag);
            }

            tag.setTag(key.NBT, list);
        }
    }

    void setDisplayTag(NBTTagCompound tag, String key, NBTBase value) {
        NBTTagCompound display = tag.getCompoundTag(CraftMetaItem.DISPLAY.NBT);

        if (!tag.hasKey(CraftMetaItem.DISPLAY.NBT)) {
            tag.setTag(CraftMetaItem.DISPLAY.NBT, display);
        }

        display.setTag(key, value);
    }

    @Overridden
    boolean applicableTo(Material type) {
        return type != Material.AIR;
    }

    @Overridden
    boolean isEmpty() {
        return !this.hasDisplayName() && !this.hasEnchants() && !this.hasLore() && !this.hasRepairCost() && this.unhandledTags.isEmpty() && this.hideFlag == 0 && !this.spigot.isUnbreakable();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public final void setDisplayName(String name) {
        this.displayName = name;
    }

    public boolean hasDisplayName() {
        return !Strings.isNullOrEmpty(this.displayName);
    }

    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    public boolean hasRepairCost() {
        return this.repairCost > 0;
    }

    public boolean hasEnchant(Enchantment ench) {
        return this.hasEnchants() && this.enchantments.containsKey(ench);
    }

    public int getEnchantLevel(Enchantment ench) {
        Integer level = this.hasEnchants() ? (Integer) this.enchantments.get(ench) : null;

        return level == null ? 0 : level.intValue();
    }

    public Map getEnchants() {
        return this.hasEnchants() ? ImmutableMap.copyOf(this.enchantments) : ImmutableMap.of();
    }

    public boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
        if (this.enchantments == null) {
            this.enchantments = new HashMap(4);
        }

        if (!ignoreRestrictions && (level < ench.getStartLevel() || level > ench.getMaxLevel())) {
            return false;
        } else {
            Integer old = (Integer) this.enchantments.put(ench, Integer.valueOf(level));

            return old == null || old.intValue() != level;
        }
    }

    public boolean removeEnchant(Enchantment ench) {
        boolean b = this.hasEnchants() && this.enchantments.remove(ench) != null;

        if (this.enchantments != null && this.enchantments.isEmpty()) {
            this.enchantments = null;
        }

        return b;
    }

    public boolean hasEnchants() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }

    public boolean hasConflictingEnchant(Enchantment ench) {
        return checkConflictingEnchants(this.enchantments, ench);
    }

    public void addItemFlags(ItemFlag... hideFlags) {
        ItemFlag[] aitemflag = hideFlags;
        int i = hideFlags.length;

        for (int j = 0; j < i; ++j) {
            ItemFlag f = aitemflag[j];

            this.hideFlag |= this.getBitModifier(f);
        }

    }

    public void removeItemFlags(ItemFlag... hideFlags) {
        ItemFlag[] aitemflag = hideFlags;
        int i = hideFlags.length;

        for (int j = 0; j < i; ++j) {
            ItemFlag f = aitemflag[j];

            this.hideFlag &= ~this.getBitModifier(f);
        }

    }

    public Set getItemFlags() {
        EnumSet currentFlags = EnumSet.noneOf(ItemFlag.class);
        ItemFlag[] aitemflag;
        int i = (aitemflag = ItemFlag.values()).length;

        for (int j = 0; j < i; ++j) {
            ItemFlag f = aitemflag[j];

            if (this.hasItemFlag(f)) {
                currentFlags.add(f);
            }
        }

        return currentFlags;
    }

    public boolean hasItemFlag(ItemFlag flag) {
        byte bitModifier = this.getBitModifier(flag);

        return (this.hideFlag & bitModifier) == bitModifier;
    }

    private byte getBitModifier(ItemFlag hideFlag) {
        return (byte) (1 << hideFlag.ordinal());
    }

    public List getLore() {
        return this.lore == null ? null : new ArrayList(this.lore);
    }

    public void setLore(List lore) {
        if (lore == null) {
            this.lore = null;
        } else if (this.lore == null) {
            safelyAdd(lore, this.lore = new ArrayList(lore.size()), Integer.MAX_VALUE);
        } else {
            this.lore.clear();
            safelyAdd(lore, this.lore, Integer.MAX_VALUE);
        }

    }

    public int getRepairCost() {
        return this.repairCost;
    }

    public void setRepairCost(int cost) {
        this.repairCost = cost;
    }

    public final boolean equals(Object object) {
        return object == null ? false : (object == this ? true : (!(object instanceof CraftMetaItem) ? false : CraftItemFactory.instance().equals((ItemMeta) this, (ItemMeta) object)));
    }

    @Overridden
    boolean equalsCommon(CraftMetaItem that) {
        if (this.hasDisplayName()) {
            if (!that.hasDisplayName() || !this.displayName.equals(that.displayName)) {
                return false;
            }
        } else if (that.hasDisplayName()) {
            return false;
        }

        if (this.hasEnchants()) {
            if (!that.hasEnchants() || !this.enchantments.equals(that.enchantments)) {
                return false;
            }
        } else if (that.hasEnchants()) {
            return false;
        }

        if (this.hasLore()) {
            if (!that.hasLore() || !this.lore.equals(that.lore)) {
                return false;
            }
        } else if (that.hasLore()) {
            return false;
        }

        if (this.hasRepairCost()) {
            if (!that.hasRepairCost() || this.repairCost != that.repairCost) {
                return false;
            }
        } else if (that.hasRepairCost()) {
            return false;
        }

        if (this.unhandledTags.equals(that.unhandledTags) && this.hideFlag == that.hideFlag && this.spigot.isUnbreakable() == that.spigot.isUnbreakable()) {
            return true;
        } else {
            return false;
        }
    }

    @Overridden
    boolean notUncommon(CraftMetaItem meta) {
        return true;
    }

    public final int hashCode() {
        return this.applyHash();
    }

    @Overridden
    int applyHash() {
        byte hash = 3;
        int hash1 = 61 * hash + (this.hasDisplayName() ? this.displayName.hashCode() : 0);

        hash1 = 61 * hash1 + (this.hasLore() ? this.lore.hashCode() : 0);
        hash1 = 61 * hash1 + (this.hasEnchants() ? this.enchantments.hashCode() : 0);
        hash1 = 61 * hash1 + (this.hasRepairCost() ? this.repairCost : 0);
        hash1 = 61 * hash1 + this.unhandledTags.hashCode();
        hash1 = 61 * hash1 + this.hideFlag;
        hash1 = 61 * hash1 + (this.spigot.isUnbreakable() ? 1231 : 1237);
        return hash1;
    }

    @Overridden
    public CraftMetaItem clone() {
        try {
            CraftMetaItem e = (CraftMetaItem) super.clone();

            if (this.lore != null) {
                e.lore = new ArrayList(this.lore);
            }

            if (this.enchantments != null) {
                e.enchantments = new HashMap(this.enchantments);
            }

            e.hideFlag = this.hideFlag;
            return e;
        } catch (CloneNotSupportedException clonenotsupportedexception) {
            throw new Error(clonenotsupportedexception);
        }
    }

    public final Map serialize() {
        ImmutableMap.Builder map = ImmutableMap.builder();

        map.put("meta-type", CraftMetaItem.SerializableMeta.classMap.get(this.getClass()));
        this.serialize(map);
        return map.build();
    }

    @Overridden
    ImmutableMap.Builder serialize(ImmutableMap.Builder builder) {
        if (this.hasDisplayName()) {
            builder.put(CraftMetaItem.NAME.BUKKIT, this.displayName);
        }

        if (this.hasLore()) {
            builder.put(CraftMetaItem.LORE.BUKKIT, ImmutableList.copyOf((Collection) this.lore));
        }

        serializeEnchantments(this.enchantments, builder, CraftMetaItem.ENCHANTMENTS);
        if (this.hasRepairCost()) {
            builder.put(CraftMetaItem.REPAIR.BUKKIT, Integer.valueOf(this.repairCost));
        }

        if (this.spigot.isUnbreakable()) {
            builder.put(CraftMetaItem.UNBREAKABLE.BUKKIT, Boolean.valueOf(true));
        }

        HashSet hideFlags = new HashSet();
        Iterator internal = this.getItemFlags().iterator();

        while (internal.hasNext()) {
            ItemFlag internalTags = (ItemFlag) internal.next();

            hideFlags.add(internalTags.name());
        }

        if (!hideFlags.isEmpty()) {
            builder.put(CraftMetaItem.HIDEFLAGS.BUKKIT, hideFlags);
        }

        HashMap internalTags1 = new HashMap(this.unhandledTags);

        this.serializeInternal(internalTags1);
        if (!internalTags1.isEmpty()) {
            NBTTagCompound internal1 = new NBTTagCompound();
            Iterator iterator = internalTags1.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry ex = (Entry) iterator.next();

                internal1.set((String) ex.getKey(), (NBTBase) ex.getValue());
            }

            try {
                ByteArrayOutputStream ex1 = new ByteArrayOutputStream();

                NBTCompressedStreamTools.a(internal1, (OutputStream) ex1);
                builder.put("internal", Base64.encodeBase64String(ex1.toByteArray()));
            } catch (IOException ioexception) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, (String) null, ioexception);
            }
        }

        return builder;
    }

    void serializeInternal(Map unhandledTags) {}

    static void serializeEnchantments(Map enchantments, ImmutableMap.Builder builder, CraftMetaItem.ItemMetaKey key) {
        if (enchantments != null && !enchantments.isEmpty()) {
            ImmutableMap.Builder enchants = ImmutableMap.builder();
            Iterator iterator = enchantments.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry enchant = (Entry) iterator.next();

                enchants.put(((Enchantment) enchant.getKey()).getName(), (Integer) enchant.getValue());
            }

            builder.put(key.BUKKIT, enchants.build());
        }
    }

    static void safelyAdd(Iterable addFrom, Collection addTo, int maxItemLength) {
        if (addFrom != null) {
            Iterator iterator = addFrom.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (!(object instanceof String)) {
                    if (object != null) {
                        throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                    }

                    addTo.add("");
                } else {
                    String page = object.toString();

                    if (page.length() > maxItemLength) {
                        page = page.substring(0, maxItemLength);
                    }

                    addTo.add(page);
                }
            }

        }
    }

    static boolean checkConflictingEnchants(Map enchantments, Enchantment ench) {
        if (enchantments != null && !enchantments.isEmpty()) {
            Iterator iterator = enchantments.keySet().iterator();

            while (iterator.hasNext()) {
                Enchantment enchant = (Enchantment) iterator.next();

                if (enchant.conflictsWith(ench)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public final String toString() {
        return (String) CraftMetaItem.SerializableMeta.classMap.get(this.getClass()) + "_META:" + this.serialize();
    }

    public static Set getHandledTags() {
        Set set = CraftMetaItem.HANDLED_TAGS;

        synchronized (CraftMetaItem.HANDLED_TAGS) {
            if (CraftMetaItem.HANDLED_TAGS.isEmpty()) {
                CraftMetaItem.HANDLED_TAGS.addAll(Arrays.asList(new String[] { CraftMetaItem.UNBREAKABLE.NBT, CraftMetaItem.DISPLAY.NBT, CraftMetaItem.REPAIR.NBT, CraftMetaItem.ENCHANTMENTS.NBT, CraftMetaItem.HIDEFLAGS.NBT, CraftMetaMap.MAP_SCALING.NBT, CraftMetaPotion.POTION_EFFECTS.NBT, CraftMetaSkull.SKULL_OWNER.NBT, CraftMetaSkull.SKULL_PROFILE.NBT, CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, CraftMetaBook.BOOK_TITLE.NBT, CraftMetaBook.BOOK_AUTHOR.NBT, CraftMetaBook.BOOK_PAGES.NBT, CraftMetaBook.RESOLVED.NBT, CraftMetaBook.GENERATION.NBT, CraftMetaFirework.FIREWORKS.NBT, CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT, CraftMetaCharge.EXPLOSION.NBT, CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT}));
            }

            return CraftMetaItem.HANDLED_TAGS;
        }
    }

    public ItemMeta.Spigot spigot() {
        return this.spigot;
    }

    static class ItemMetaKey {

        final String BUKKIT;
        final String NBT;

        ItemMetaKey(String both) {
            this(both, both);
        }

        ItemMetaKey(String nbt, String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }

        @Retention(RetentionPolicy.SOURCE)
        @Target({ ElementType.FIELD})
        @interface Specific {

            CraftMetaItem.ItemMetaKey.Specific.To value();

            public static enum To {

                BUKKIT, NBT;
            }
        }
    }

    @SerializableAs("ItemMeta")
    public static class SerializableMeta implements ConfigurationSerializable {

        static final String TYPE_FIELD = "meta-type";
        static final ImmutableMap classMap = ImmutableMap.builder().put(CraftMetaBanner.class, "BANNER").put(CraftMetaBlockState.class, "TILE_ENTITY").put(CraftMetaBook.class, "BOOK").put(CraftMetaBookSigned.class, "BOOK_SIGNED").put(CraftMetaSkull.class, "SKULL").put(CraftMetaLeatherArmor.class, "LEATHER_ARMOR").put(CraftMetaMap.class, "MAP").put(CraftMetaPotion.class, "POTION").put(CraftMetaEnchantedBook.class, "ENCHANTED").put(CraftMetaFirework.class, "FIREWORK").put(CraftMetaCharge.class, "FIREWORK_EFFECT").put(CraftMetaItem.class, "UNSPECIFIC").build();
        static final ImmutableMap constructorMap;

        static {
            ImmutableMap.Builder classConstructorBuilder = ImmutableMap.builder();
            Iterator iterator = CraftMetaItem.SerializableMeta.classMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry mapping = (Entry) iterator.next();

                try {
                    classConstructorBuilder.put((String) mapping.getValue(), ((Class) mapping.getKey()).getDeclaredConstructor(new Class[] { Map.class}));
                } catch (NoSuchMethodException nosuchmethodexception) {
                    throw new AssertionError(nosuchmethodexception);
                }
            }

            constructorMap = classConstructorBuilder.build();
        }

        public static ItemMeta deserialize(Map map) throws Throwable {
            Validate.notNull(map, "Cannot deserialize null map");
            String type = getString(map, "meta-type", false);
            Constructor constructor = (Constructor) CraftMetaItem.SerializableMeta.constructorMap.get(type);

            if (constructor == null) {
                throw new IllegalArgumentException(type + " is not a valid " + "meta-type");
            } else {
                try {
                    return (ItemMeta) constructor.newInstance(new Object[] { map});
                } catch (InstantiationException instantiationexception) {
                    throw new AssertionError(instantiationexception);
                } catch (IllegalAccessException illegalaccessexception) {
                    throw new AssertionError(illegalaccessexception);
                } catch (InvocationTargetException invocationtargetexception) {
                    throw invocationtargetexception.getCause();
                }
            }
        }

        public Map serialize() {
            throw new AssertionError();
        }

        static String getString(Map map, Object field, boolean nullable) {
            return (String) getObject(String.class, map, field, nullable);
        }

        static boolean getBoolean(Map map, Object field) {
            Boolean value = (Boolean) getObject(Boolean.class, map, field, true);

            return value != null && value.booleanValue();
        }

        static Object getObject(Class clazz, Map map, Object field, boolean nullable) {
            Object object = map.get(field);

            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            } else if (object == null) {
                if (!nullable) {
                    throw new NoSuchElementException(map + " does not contain " + field);
                } else {
                    return null;
                }
            } else {
                throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
            }
        }
    }
}
