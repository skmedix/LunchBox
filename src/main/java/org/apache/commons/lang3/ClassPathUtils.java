package org.apache.commons.lang3;

public class ClassPathUtils {

    public static String toFullyQualifiedName(Class oclass, String s) {
        Validate.notNull(oclass, "Parameter \'%s\' must not be null!", new Object[] { "context"});
        Validate.notNull(s, "Parameter \'%s\' must not be null!", new Object[] { "resourceName"});
        return toFullyQualifiedName(oclass.getPackage(), s);
    }

    public static String toFullyQualifiedName(Package package, String s) {
        Validate.notNull(package, "Parameter \'%s\' must not be null!", new Object[] { "context"});
        Validate.notNull(s, "Parameter \'%s\' must not be null!", new Object[] { "resourceName"});
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(package.getName());
        stringbuilder.append(".");
        stringbuilder.append(s);
        return stringbuilder.toString();
    }

    public static String toFullyQualifiedPath(Class oclass, String s) {
        Validate.notNull(oclass, "Parameter \'%s\' must not be null!", new Object[] { "context"});
        Validate.notNull(s, "Parameter \'%s\' must not be null!", new Object[] { "resourceName"});
        return toFullyQualifiedPath(oclass.getPackage(), s);
    }

    public static String toFullyQualifiedPath(Package package, String s) {
        Validate.notNull(package, "Parameter \'%s\' must not be null!", new Object[] { "context"});
        Validate.notNull(s, "Parameter \'%s\' must not be null!", new Object[] { "resourceName"});
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(package.getName().replace('.', '/'));
        stringbuilder.append("/");
        stringbuilder.append(s);
        return stringbuilder.toString();
    }
}
