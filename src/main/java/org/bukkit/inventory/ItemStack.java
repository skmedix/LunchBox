package org.bukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemStack implements Cloneable, ConfigurationSerializable {

    private int type;
    private int amount;
    private MaterialData data;
    private short durability;
    private ItemMeta meta;

    protected ItemStack() {
        this.type = 0;
        this.amount = 0;
        this.data = null;
        this.durability = 0;
    }

    /** @deprecated */
    @Deprecated
    public ItemStack(int type) {
        this(type, 1);
    }

    public ItemStack(Material type) {
        this(type, 1);
    }

    /** @deprecated */
    @Deprecated
    public ItemStack(int type, int amount) {
        this(type, amount, (short) 0);
    }

    public ItemStack(Material type, int amount) {
        this(type.getId(), amount);
    }

    /** @deprecated */
    @Deprecated
    public ItemStack(int type, int amount, short damage) {
        this.type = 0;
        this.amount = 0;
        this.data = null;
        this.durability = 0;
        this.type = type;
        this.amount = amount;
        this.durability = damage;
    }

    public ItemStack(Material type, int amount, short damage) {
        this(type.getId(), amount, damage);
    }

    /** @deprecated */
    @Deprecated
    public ItemStack(int type, int amount, short damage, Byte data) {
        this.type = 0;
        this.amount = 0;
        this.data = null;
        this.durability = 0;
        this.type = type;
        this.amount = amount;
        this.durability = damage;
        if (data != null) {
            this.createData(data.byteValue());
            this.durability = data.byteValue();
        }

    }

    /** @deprecated */
    @Deprecated
    public ItemStack(Material type, int amount, short damage, Byte data) {
        this(type.getId(), amount, damage, data);
    }

    public ItemStack(ItemStack stack) throws IllegalArgumentException {
        this.type = 0;
        this.amount = 0;
        this.data = null;
        this.durability = 0;
        Validate.notNull(stack, "Cannot copy null stack");
        this.type = stack.getTypeId();
        this.amount = stack.getAmount();
        this.durability = stack.getDurability();
        this.data = stack.getData();
        if (stack.hasItemMeta()) {
            this.setItemMeta0(stack.getItemMeta(), this.getType0());
        }

    }

    public Material getType() {
        return getType0(this.getTypeId());
    }

    private Material getType0() {
        return getType0(this.type);
    }

    private static Material getType0(int id) {
        Material material = Material.getMaterial(id);

        return material == null ? Material.AIR : material;
    }

    public void setType(Material type) {
        Validate.notNull(type, "Material cannot be null");
        this.setTypeId(type.getId());
    }

    /** @deprecated */
    @Deprecated
    public int getTypeId() {
        return this.type;
    }

    /** @deprecated */
    @Deprecated
    public void setTypeId(int type) {
        this.type = type;
        if (this.meta != null) {
            this.meta = Bukkit.getItemFactory().asMetaFor(this.meta, this.getType0());
        }

        this.createData((byte) 0);
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public MaterialData getData() {
        Material mat = this.getType();

        if (this.data == null && mat != null && mat.getData() != null) {
            this.data = mat.getNewData((byte) this.getDurability());
        }

        return this.data;
    }

    public void setData(MaterialData data) {
        Material mat = this.getType();

        if (data != null && mat != null && mat.getData() != null) {
            if (data.getClass() != mat.getData() && data.getClass() != MaterialData.class) {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }

            this.data = data;
        } else {
            this.data = data;
        }

    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public short getDurability() {
        return this.durability;
    }

    public int getMaxStackSize() {
        Material material = this.getType();

        return material != null ? material.getMaxStackSize() : -1;
    }

    private void createData(byte data) {
        Material mat = Material.getMaterial(this.type);

        if (mat == null) {
            this.data = new MaterialData(this.type, data);
        } else {
            this.data = mat.getNewData(data);
        }

    }

    public String toString() {
        StringBuilder toString = (new StringBuilder("ItemStack{")).append(this.getType().name()).append(" x ").append(this.getAmount());

        if (this.hasItemMeta()) {
            toString.append(", ").append(this.getItemMeta());
        }

        return toString.append('}').toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof ItemStack)) {
            return false;
        } else {
            ItemStack stack = (ItemStack) obj;

            return this.getAmount() == stack.getAmount() && this.isSimilar(stack);
        }
    }

    public boolean isSimilar(ItemStack stack) {
        return stack == null ? false : (stack == this ? true : this.getTypeId() == stack.getTypeId() && this.getDurability() == stack.getDurability() && this.hasItemMeta() == stack.hasItemMeta() && (!this.hasItemMeta() || Bukkit.getItemFactory().equals(this.getItemMeta(), stack.getItemMeta())));
    }

    public ItemStack clone() {
        try {
            ItemStack e = (ItemStack) super.clone();

            if (this.meta != null) {
                e.meta = this.meta.clone();
            }

            if (this.data != null) {
                e.data = this.data.clone();
            }

            return e;
        } catch (CloneNotSupportedException clonenotsupportedexception) {
            throw new Error(clonenotsupportedexception);
        }
    }

    public final int hashCode() {
        byte hash = 1;
        int hash1 = hash * 31 + this.getTypeId();

        hash1 = hash1 * 31 + this.getAmount();
        hash1 = hash1 * 31 + (this.getDurability() & '\uffff');
        hash1 = hash1 * 31 + (this.hasItemMeta() ? (this.meta == null ? this.getItemMeta().hashCode() : this.meta.hashCode()) : 0);
        return hash1;
    }

    public boolean containsEnchantment(Enchantment ench) {
        return this.meta == null ? false : this.meta.hasEnchant(ench);
    }

    public int getEnchantmentLevel(Enchantment ench) {
        return this.meta == null ? 0 : this.meta.getEnchantLevel(ench);
    }

    public Map getEnchantments() {
        return (Map) (this.meta == null ? ImmutableMap.of() : this.meta.getEnchants());
    }

    public void addEnchantments(Map enchantments) {
        Validate.notNull(enchantments, "Enchantments cannot be null");
        Iterator iterator = enchantments.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            this.addEnchantment((Enchantment) entry.getKey(), ((Integer) entry.getValue()).intValue());
        }

    }

    public void addEnchantment(Enchantment ench, int level) {
        Validate.notNull(ench, "Enchantment cannot be null");
        if (level >= ench.getStartLevel() && level <= ench.getMaxLevel()) {
            if (!ench.canEnchantItem(this)) {
                throw new IllegalArgumentException("Specified enchantment cannot be applied to this itemstack");
            } else {
                this.addUnsafeEnchantment(ench, level);
            }
        } else {
            throw new IllegalArgumentException("Enchantment level is either too low or too high (given " + level + ", bounds are " + ench.getStartLevel() + " to " + ench.getMaxLevel() + ")");
        }
    }

    public void addUnsafeEnchantments(Map enchantments) {
        Iterator iterator = enchantments.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            this.addUnsafeEnchantment((Enchantment) entry.getKey(), ((Integer) entry.getValue()).intValue());
        }

    }

    public void addUnsafeEnchantment(Enchantment ench, int level) {
        (this.meta == null ? (this.meta = Bukkit.getItemFactory().getItemMeta(this.getType0())) : this.meta).addEnchant(ench, level, true);
    }

    public int removeEnchantment(Enchantment ench) {
        int level = this.getEnchantmentLevel(ench);

        if (level != 0 && this.meta != null) {
            this.meta.removeEnchant(ench);
            return level;
        } else {
            return level;
        }
    }

    public Map serialize() {
        LinkedHashMap result = new LinkedHashMap();

        result.put("type", this.getType().name());
        if (this.getDurability() != 0) {
            result.put("damage", Short.valueOf(this.getDurability()));
        }

        if (this.getAmount() != 1) {
            result.put("amount", Integer.valueOf(this.getAmount()));
        }

        ItemMeta meta = this.getItemMeta();

        if (!Bukkit.getItemFactory().equals(meta, (ItemMeta) null)) {
            result.put("meta", meta);
        }

        return result;
    }

    public static ItemStack deserialize(Map args) {
        Material type = Material.getMaterial((String) args.get("type"));
        short damage = 0;
        int amount = 1;

        if (args.containsKey("damage")) {
            damage = ((Number) args.get("damage")).shortValue();
        }

        if (args.containsKey("amount")) {
            amount = ((Integer) args.get("amount")).intValue();
        }

        ItemStack result = new ItemStack(type, amount, damage);
        Object raw;

        if (args.containsKey("enchantments")) {
            raw = args.get("enchantments");
            if (raw instanceof Map) {
                Map map = (Map) raw;
                Iterator iterator = map.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());

                    if (enchantment != null && entry.getValue() instanceof Integer) {
                        result.addUnsafeEnchantment(enchantment, ((Integer) entry.getValue()).intValue());
                    }
                }
            }
        } else if (args.containsKey("meta")) {
            raw = args.get("meta");
            if (raw instanceof ItemMeta) {
                result.setItemMeta((ItemMeta) raw);
            }
        }

        return result;
    }

    public ItemMeta getItemMeta() {
        return this.meta == null ? Bukkit.getItemFactory().getItemMeta(this.getType0()) : this.meta.clone();
    }

    public boolean hasItemMeta() {
        return !Bukkit.getItemFactory().equals(this.meta, (ItemMeta) null);
    }

    public boolean setItemMeta(ItemMeta itemMeta) {
        return this.setItemMeta0(itemMeta, this.getType0());
    }

    private boolean setItemMeta0(ItemMeta itemMeta, Material material) {
        if (itemMeta == null) {
            this.meta = null;
            return true;
        } else if (!Bukkit.getItemFactory().isApplicable(itemMeta, material)) {
            return false;
        } else {
            this.meta = Bukkit.getItemFactory().asMetaFor(itemMeta, material);
            if (this.meta == itemMeta) {
                this.meta = itemMeta.clone();
            }

            return true;
        }
    }
}
