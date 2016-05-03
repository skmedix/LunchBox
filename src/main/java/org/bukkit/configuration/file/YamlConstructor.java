package org.bukkit.configuration.file;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlConstructor extends SafeConstructor {

    public YamlConstructor() {
        this.yamlConstructors.put(Tag.MAP, new YamlConstructor.ConstructCustomObject((YamlConstructor.ConstructCustomObject) null));
    }

    private class ConstructCustomObject extends SafeConstructor.ConstructYamlMap {

        private ConstructCustomObject() {
            super();
        }

        public Object construct(Node node) {
            if (node.isTwoStepsConstruction()) {
                throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
            } else {
                Map raw = (Map) super.construct(node);

                if (!raw.containsKey("==")) {
                    return raw;
                } else {
                    LinkedHashMap typed = new LinkedHashMap(raw.size());
                    Iterator iterator = raw.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Entry ex = (Entry) iterator.next();

                        typed.put(ex.getKey().toString(), ex.getValue());
                    }

                    try {
                        return ConfigurationSerialization.deserializeObject(typed);
                    } catch (IllegalArgumentException illegalargumentexception) {
                        throw new YAMLException("Could not deserialize object", illegalargumentexception);
                    }
                }
            }
        }

        public void construct2ndStep(Node node, Object object) {
            throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
        }

        ConstructCustomObject(YamlConstructor.ConstructCustomObject yamlconstructor_constructcustomobject) {
            this();
        }
    }
}
