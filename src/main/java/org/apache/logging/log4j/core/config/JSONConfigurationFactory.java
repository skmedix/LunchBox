package org.apache.logging.log4j.core.config;

import java.io.File;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "JSONConfigurationFactory",
    category = "ConfigurationFactory"
)
@Order(6)
public class JSONConfigurationFactory extends ConfigurationFactory {

    public static final String[] SUFFIXES = new String[] { ".json", ".jsn"};
    private static String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser"};
    private final File configFile = null;
    private boolean isActive;

    public JSONConfigurationFactory() {
        try {
            String[] astring = JSONConfigurationFactory.dependencies;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring[j];

                Class.forName(s);
            }
        } catch (ClassNotFoundException classnotfoundexception) {
            JSONConfigurationFactory.LOGGER.debug("Missing dependencies for Json support");
            this.isActive = false;
            return;
        }

        this.isActive = true;
    }

    protected boolean isActive() {
        return this.isActive;
    }

    public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource) {
        return !this.isActive ? null : new JSONConfiguration(configurationfactory_configurationsource);
    }

    public String[] getSupportedTypes() {
        return JSONConfigurationFactory.SUFFIXES;
    }
}
