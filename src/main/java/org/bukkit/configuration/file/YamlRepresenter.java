package org.bukkit.configuration.file;

import java.util.LinkedHashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.representer.SafeRepresenter;

public class YamlRepresenter extends Representer {

    public YamlRepresenter() {
        this.multiRepresenters.put(ConfigurationSection.class, new YamlRepresenter.RepresentConfigurationSection((YamlRepresenter.RepresentConfigurationSection) null));
        this.multiRepresenters.put(ConfigurationSerializable.class, new YamlRepresenter.RepresentConfigurationSerializable((YamlRepresenter.RepresentConfigurationSerializable) null));
    }

    private class RepresentConfigurationSection extends SafeRepresenter.RepresentMap {

        private RepresentConfigurationSection() {
            super();
        }

        public Node representData(Object data) {
            return super.representData(((ConfigurationSection) data).getValues(false));
        }

        RepresentConfigurationSection(YamlRepresenter.RepresentConfigurationSection yamlrepresenter_representconfigurationsection) {
            this();
        }
    }

    private class RepresentConfigurationSerializable extends SafeRepresenter.RepresentMap {

        private RepresentConfigurationSerializable() {
            super();
        }

        public Node representData(Object data) {
            ConfigurationSerializable serializable = (ConfigurationSerializable) data;
            LinkedHashMap values = new LinkedHashMap();

            values.put("==", ConfigurationSerialization.getAlias(serializable.getClass()));
            values.putAll(serializable.serialize());
            return super.representData(values);
        }

        RepresentConfigurationSerializable(YamlRepresenter.RepresentConfigurationSerializable yamlrepresenter_representconfigurationserializable) {
            this();
        }
    }
}
