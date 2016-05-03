package org.yaml.snakeyaml;

import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.nodes.Tag;

public final class TypeDescription {

    private final Class type;
    private Tag tag;
    private Map listProperties;
    private Map keyProperties;
    private Map valueProperties;

    public TypeDescription(Class clazz, Tag tag) {
        this.type = clazz;
        this.tag = tag;
        this.listProperties = new HashMap();
        this.keyProperties = new HashMap();
        this.valueProperties = new HashMap();
    }

    public TypeDescription(Class clazz, String tag) {
        this(clazz, new Tag(tag));
    }

    public TypeDescription(Class clazz) {
        this(clazz, (Tag) null);
    }

    public Tag getTag() {
        return this.tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setTag(String tag) {
        this.setTag(new Tag(tag));
    }

    public Class getType() {
        return this.type;
    }

    public void putListPropertyType(String property, Class type) {
        this.listProperties.put(property, type);
    }

    public Class getListPropertyType(String property) {
        return (Class) this.listProperties.get(property);
    }

    public void putMapPropertyType(String property, Class key, Class value) {
        this.keyProperties.put(property, key);
        this.valueProperties.put(property, value);
    }

    public Class getMapKeyType(String property) {
        return (Class) this.keyProperties.get(property);
    }

    public Class getMapValueType(String property) {
        return (Class) this.valueProperties.get(property);
    }

    public String toString() {
        return "TypeDescription for " + this.getType() + " (tag=\'" + this.getTag() + "\')";
    }
}
