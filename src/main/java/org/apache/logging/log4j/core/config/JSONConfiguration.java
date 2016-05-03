package org.apache.logging.log4j.core.config;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

public class JSONConfiguration extends BaseConfiguration implements Reconfigurable {

    private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName()};
    private static final int BUF_SIZE = 16384;
    private final List status = new ArrayList();
    private JsonNode root;
    private final List messages = new ArrayList();
    private final File configFile;

    public JSONConfiguration(ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource) {
        this.configFile = configurationfactory_configurationsource.getFile();

        try {
            InputStream inputstream = configurationfactory_configurationsource.getInputStream();
            byte[] abyte = this.toByteArray(inputstream);

            inputstream.close();
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
            ObjectMapper objectmapper = (new ObjectMapper()).configure(Feature.ALLOW_COMMENTS, true);

            this.root = objectmapper.readTree(bytearrayinputstream);
            if (this.root.size() == 1) {
                Iterator iterator = this.root.elements();

                this.root = (JsonNode) iterator.next();
            }

            this.processAttributes(this.rootNode, this.root);
            Level level = this.getDefaultStatus();
            boolean flag = false;
            PrintStream printstream = System.out;
            Iterator iterator1 = this.rootNode.getAttributes().entrySet().iterator();

            String s;

            while (iterator1.hasNext()) {
                Entry entry = (Entry) iterator1.next();

                if ("status".equalsIgnoreCase((String) entry.getKey())) {
                    level = Level.toLevel(this.getStrSubstitutor().replace((String) entry.getValue()), (Level) null);
                    if (level == null) {
                        level = Level.ERROR;
                        this.messages.add("Invalid status specified: " + (String) entry.getValue() + ". Defaulting to ERROR");
                    }
                } else {
                    String s1;

                    if ("dest".equalsIgnoreCase((String) entry.getKey())) {
                        s1 = (String) entry.getValue();
                        if (s1 != null) {
                            if (s1.equalsIgnoreCase("err")) {
                                printstream = System.err;
                            } else {
                                try {
                                    File file = FileUtils.fileFromURI(new URI(s1));

                                    s = Charset.defaultCharset().name();
                                    printstream = new PrintStream(new FileOutputStream(file), true, s);
                                } catch (URISyntaxException urisyntaxexception) {
                                    System.err.println("Unable to write to " + s1 + ". Writing to stdout");
                                }
                            }
                        }
                    } else if ("shutdownHook".equalsIgnoreCase((String) entry.getKey())) {
                        s1 = this.getStrSubstitutor().replace((String) entry.getValue());
                        this.isShutdownHookEnabled = !s1.equalsIgnoreCase("disable");
                    } else if ("verbose".equalsIgnoreCase((String) entry.getKey())) {
                        flag = Boolean.parseBoolean(this.getStrSubstitutor().replace((String) entry.getValue()));
                    } else if ("packages".equalsIgnoreCase((String) entry.getKey())) {
                        String[] astring = this.getStrSubstitutor().replace((String) entry.getValue()).split(",");
                        String[] astring1 = astring;
                        int i = astring.length;

                        for (int j = 0; j < i; ++j) {
                            String s2 = astring1[j];

                            PluginManager.addPackage(s2);
                        }
                    } else if ("name".equalsIgnoreCase((String) entry.getKey())) {
                        this.setName(this.getStrSubstitutor().replace((String) entry.getValue()));
                    } else if ("monitorInterval".equalsIgnoreCase((String) entry.getKey())) {
                        int k = Integer.parseInt(this.getStrSubstitutor().replace((String) entry.getValue()));

                        if (k > 0 && this.configFile != null) {
                            this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, k);
                        }
                    } else if ("advertiser".equalsIgnoreCase((String) entry.getKey())) {
                        this.createAdvertiser(this.getStrSubstitutor().replace((String) entry.getValue()), configurationfactory_configurationsource, abyte, "application/json");
                    }
                }
            }

            iterator1 = ((StatusLogger) JSONConfiguration.LOGGER).getListeners();
            boolean flag1 = false;

            while (iterator1.hasNext()) {
                StatusListener statuslistener = (StatusListener) iterator1.next();

                if (statuslistener instanceof StatusConsoleListener) {
                    flag1 = true;
                    ((StatusConsoleListener) statuslistener).setLevel(level);
                    if (!flag) {
                        ((StatusConsoleListener) statuslistener).setFilters(JSONConfiguration.VERBOSE_CLASSES);
                    }
                }
            }

            if (!flag1 && level != Level.OFF) {
                StatusConsoleListener statusconsolelistener = new StatusConsoleListener(level, printstream);

                if (!flag) {
                    statusconsolelistener.setFilters(JSONConfiguration.VERBOSE_CLASSES);
                }

                ((StatusLogger) JSONConfiguration.LOGGER).registerListener(statusconsolelistener);
                Iterator iterator2 = this.messages.iterator();

                while (iterator2.hasNext()) {
                    s = (String) iterator2.next();
                    JSONConfiguration.LOGGER.error(s);
                }
            }

            if (this.getName() == null) {
                this.setName(configurationfactory_configurationsource.getLocation());
            }
        } catch (Exception exception) {
            JSONConfiguration.LOGGER.error("Error parsing " + configurationfactory_configurationsource.getLocation(), (Throwable) exception);
            exception.printStackTrace();
        }

    }

    public void stop() {
        super.stop();
    }

    public void setup() {
        Iterator iterator = this.root.fields();
        List list = this.rootNode.getChildren();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            JsonNode jsonnode = (JsonNode) entry.getValue();

            if (jsonnode.isObject()) {
                JSONConfiguration.LOGGER.debug("Processing node for object " + (String) entry.getKey());
                list.add(this.constructNode((String) entry.getKey(), this.rootNode, jsonnode));
            } else if (jsonnode.isArray()) {
                JSONConfiguration.LOGGER.error("Arrays are not supported at the root configuration.");
            }
        }

        JSONConfiguration.LOGGER.debug("Completed parsing configuration");
        if (this.status.size() > 0) {
            Iterator iterator1 = this.status.iterator();

            while (iterator1.hasNext()) {
                JSONConfiguration.Status jsonconfiguration_status = (JSONConfiguration.Status) iterator1.next();

                JSONConfiguration.LOGGER.error("Error processing element " + jsonconfiguration_status.name + ": " + jsonconfiguration_status.errorType);
            }

        }
    }

    public Configuration reconfigure() {
        if (this.configFile != null) {
            try {
                ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);

                return new JSONConfiguration(configurationfactory_configurationsource);
            } catch (FileNotFoundException filenotfoundexception) {
                JSONConfiguration.LOGGER.error("Cannot locate file " + this.configFile, (Throwable) filenotfoundexception);
            }
        }

        return null;
    }

    private Node constructNode(String s, Node node, JsonNode jsonnode) {
        PluginType plugintype = this.pluginManager.getPluginType(s);
        Node node1 = new Node(node, s, plugintype);

        this.processAttributes(node1, jsonnode);
        Iterator iterator = jsonnode.fields();
        List list = node1.getChildren();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            JsonNode jsonnode1 = (JsonNode) entry.getValue();

            if (jsonnode1.isArray() || jsonnode1.isObject()) {
                if (plugintype == null) {
                    this.status.add(new JSONConfiguration.Status(s, jsonnode1, JSONConfiguration.ErrorType.CLASS_NOT_FOUND));
                }

                if (!jsonnode1.isArray()) {
                    JSONConfiguration.LOGGER.debug("Processing node for object " + (String) entry.getKey());
                    list.add(this.constructNode((String) entry.getKey(), node1, jsonnode1));
                } else {
                    JSONConfiguration.LOGGER.debug("Processing node for array " + (String) entry.getKey());

                    for (int i = 0; i < jsonnode1.size(); ++i) {
                        String s1 = this.getType(jsonnode1.get(i), (String) entry.getKey());
                        PluginType plugintype1 = this.pluginManager.getPluginType(s1);
                        Node node2 = new Node(node1, (String) entry.getKey(), plugintype1);

                        this.processAttributes(node2, jsonnode1.get(i));
                        if (s1.equals(entry.getKey())) {
                            JSONConfiguration.LOGGER.debug("Processing " + (String) entry.getKey() + "[" + i + "]");
                        } else {
                            JSONConfiguration.LOGGER.debug("Processing " + s1 + " " + (String) entry.getKey() + "[" + i + "]");
                        }

                        Iterator iterator1 = jsonnode1.get(i).fields();
                        List list1 = node2.getChildren();

                        while (iterator1.hasNext()) {
                            Entry entry1 = (Entry) iterator1.next();

                            if (((JsonNode) entry1.getValue()).isObject()) {
                                JSONConfiguration.LOGGER.debug("Processing node for object " + (String) entry1.getKey());
                                list1.add(this.constructNode((String) entry1.getKey(), node2, (JsonNode) entry1.getValue()));
                            }
                        }

                        list.add(node2);
                    }
                }
            }
        }

        String s2;

        if (plugintype == null) {
            s2 = "null";
        } else {
            s2 = plugintype.getElementName() + ":" + plugintype.getPluginClass();
        }

        String s3 = node1.getParent() == null ? "null" : (node1.getParent().getName() == null ? "root" : node1.getParent().getName());

        JSONConfiguration.LOGGER.debug("Returning " + node1.getName() + " with parent " + s3 + " of type " + s2);
        return node1;
    }

    private String getType(JsonNode jsonnode, String s) {
        Iterator iterator = jsonnode.fields();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equalsIgnoreCase("type")) {
                JsonNode jsonnode1 = (JsonNode) entry.getValue();

                if (jsonnode1.isValueNode()) {
                    return jsonnode1.asText();
                }
            }
        }

        return s;
    }

    private void processAttributes(Node node, JsonNode jsonnode) {
        Map map = node.getAttributes();
        Iterator iterator = jsonnode.fields();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!((String) entry.getKey()).equalsIgnoreCase("type")) {
                JsonNode jsonnode1 = (JsonNode) entry.getValue();

                if (jsonnode1.isValueNode()) {
                    map.put(entry.getKey(), jsonnode1.asText());
                }
            }
        }

    }

    protected byte[] toByteArray(InputStream inputstream) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] abyte = new byte[16384];

        int i;

        while ((i = inputstream.read(abyte, 0, abyte.length)) != -1) {
            bytearrayoutputstream.write(abyte, 0, i);
        }

        return bytearrayoutputstream.toByteArray();
    }

    private class Status {

        private final JsonNode node;
        private final String name;
        private final JSONConfiguration.ErrorType errorType;

        public Status(String s, JsonNode jsonnode, JSONConfiguration.ErrorType jsonconfiguration_errortype) {
            this.name = s;
            this.node = jsonnode;
            this.errorType = jsonconfiguration_errortype;
        }
    }

    private static enum ErrorType {

        CLASS_NOT_FOUND;
    }
}
