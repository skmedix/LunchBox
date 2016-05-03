package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.PluginType;

public class Node {

    private final Node parent;
    private final String name;
    private String value;
    private final PluginType type;
    private final Map attributes = new HashMap();
    private final List children = new ArrayList();
    private Object object;

    public Node(Node node, String s, PluginType plugintype) {
        this.parent = node;
        this.name = s;
        this.type = plugintype;
    }

    public Node() {
        this.parent = null;
        this.name = null;
        this.type = null;
    }

    public Node(Node node) {
        this.parent = node.parent;
        this.name = node.name;
        this.type = node.type;
        this.attributes.putAll(node.getAttributes());
        this.value = node.getValue();
        Iterator iterator = node.getChildren().iterator();

        while (iterator.hasNext()) {
            Node node1 = (Node) iterator.next();

            this.children.add(new Node(node1));
        }

        this.object = node.object;
    }

    public Map getAttributes() {
        return this.attributes;
    }

    public List getChildren() {
        return this.children;
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String s) {
        this.value = s;
    }

    public Node getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return this.object;
    }

    public PluginType getType() {
        return this.type;
    }

    public String toString() {
        return this.object == null ? "null" : (this.type.isObjectPrintable() ? this.object.toString() : this.type.getPluginClass().getName() + " with name " + this.name);
    }
}
