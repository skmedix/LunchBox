package org.bukkit.permissions;

import java.util.HashMap;
import java.util.Map;

public enum PermissionDefault {

    TRUE(new String[] { "true"}), FALSE(new String[] { "false"}), OP(new String[] { "op", "isop", "operator", "isoperator", "admin", "isadmin"}), NOT_OP(new String[] { "!op", "notop", "!operator", "notoperator", "!admin", "notadmin"});

    private final String[] names;
    private static final Map lookup = new HashMap();
    private static int[] $SWITCH_TABLE$org$bukkit$permissions$PermissionDefault;

    static {
        PermissionDefault[] apermissiondefault;
        int i = (apermissiondefault = values()).length;

        for (int j = 0; j < i; ++j) {
            PermissionDefault value = apermissiondefault[j];
            String[] astring = value.names;
            int k = value.names.length;

            for (int l = 0; l < k; ++l) {
                String name = astring[l];

                PermissionDefault.lookup.put(name, value);
            }
        }

    }

    private PermissionDefault(String... names) {
        this.names = names;
    }

    public boolean getValue(boolean op) {
        switch ($SWITCH_TABLE$org$bukkit$permissions$PermissionDefault()[this.ordinal()]) {
        case 1:
            return true;

        case 2:
            return false;

        case 3:
            return op;

        case 4:
            return !op;

        default:
            return false;
        }
    }

    public static PermissionDefault getByName(String name) {
        return (PermissionDefault) PermissionDefault.lookup.get(name.toLowerCase().replaceAll("[^a-z!]", ""));
    }

    public String toString() {
        return this.names[0];
    }

    static int[] $SWITCH_TABLE$org$bukkit$permissions$PermissionDefault() {
        int[] aint = PermissionDefault.$SWITCH_TABLE$org$bukkit$permissions$PermissionDefault;

        if (PermissionDefault.$SWITCH_TABLE$org$bukkit$permissions$PermissionDefault != null) {
            return aint;
        } else {
            int[] aint1 = new int[values().length];

            try {
                aint1[PermissionDefault.FALSE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[PermissionDefault.NOT_OP.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[PermissionDefault.OP.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[PermissionDefault.TRUE.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            PermissionDefault.$SWITCH_TABLE$org$bukkit$permissions$PermissionDefault = aint1;
            return aint1;
        }
    }
}
