package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "XMLConfigurationFactory",
    category = "ConfigurationFactory"
)
@Order(5)
public class XMLConfigurationFactory extends ConfigurationFactory {

    public static final String[] SUFFIXES = new String[] { ".xml", "*"};

    public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource) {
        return new XMLConfiguration(configurationfactory_configurationsource);
    }

    public String[] getSupportedTypes() {
        return XMLConfigurationFactory.SUFFIXES;
    }
}
