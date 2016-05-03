package org.apache.logging.log4j.core.config;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLConfiguration extends BaseConfiguration implements Reconfigurable {

    private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName()};
    private static final String LOG4J_XSD = "Log4j-config.xsd";
    private static final int BUF_SIZE = 16384;
    private final List status = new ArrayList();
    private Element rootElement;
    private boolean strict;
    private String schema;
    private final File configFile;

    static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();

        documentbuilderfactory.setNamespaceAware(true);
        enableXInclude(documentbuilderfactory);
        return documentbuilderfactory.newDocumentBuilder();
    }

    private static void enableXInclude(DocumentBuilderFactory documentbuilderfactory) {
        try {
            documentbuilderfactory.setXIncludeAware(true);
        } catch (UnsupportedOperationException unsupportedoperationexception) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory does not support XInclude: " + documentbuilderfactory, (Throwable) unsupportedoperationexception);
        } catch (AbstractMethodError abstractmethoderror) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory is out of date and does not support XInclude: " + documentbuilderfactory);
        }

        try {
            documentbuilderfactory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
        } catch (ParserConfigurationException parserconfigurationexception) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory [" + documentbuilderfactory + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-base-uris" + "]", (Throwable) parserconfigurationexception);
        } catch (AbstractMethodError abstractmethoderror1) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + documentbuilderfactory);
        }

        try {
            documentbuilderfactory.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
        } catch (ParserConfigurationException parserconfigurationexception1) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory [" + documentbuilderfactory + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-language" + "]", (Throwable) parserconfigurationexception1);
        } catch (AbstractMethodError abstractmethoderror2) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + documentbuilderfactory);
        }

    }

    public XMLConfiguration(ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource) {
        this.configFile = configurationfactory_configurationsource.getFile();
        byte[] abyte = null;

        try {
            ArrayList arraylist = new ArrayList();
            InputStream inputstream = configurationfactory_configurationsource.getInputStream();

            abyte = this.toByteArray(inputstream);
            inputstream.close();
            InputSource inputsource = new InputSource(new ByteArrayInputStream(abyte));
            Document document = newDocumentBuilder().parse(inputsource);

            this.rootElement = document.getDocumentElement();
            Map map = this.processAttributes(this.rootNode, this.rootElement);
            Level level = this.getDefaultStatus();
            boolean flag = false;
            PrintStream printstream = System.out;
            Iterator iterator = map.entrySet().iterator();

            String s;

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                if ("status".equalsIgnoreCase((String) entry.getKey())) {
                    Level level1 = Level.toLevel(this.getStrSubstitutor().replace((String) entry.getValue()), (Level) null);

                    if (level1 != null) {
                        level = level1;
                    } else {
                        arraylist.add("Invalid status specified: " + (String) entry.getValue() + ". Defaulting to " + level);
                    }
                } else {
                    String s1;

                    if ("dest".equalsIgnoreCase((String) entry.getKey())) {
                        s1 = this.getStrSubstitutor().replace((String) entry.getValue());
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
                    } else if ("packages".equalsIgnoreCase(this.getStrSubstitutor().replace((String) entry.getKey()))) {
                        String[] astring = ((String) entry.getValue()).split(",");
                        String[] astring1 = astring;
                        int i = astring.length;

                        for (int j = 0; j < i; ++j) {
                            String s2 = astring1[j];

                            PluginManager.addPackage(s2);
                        }
                    } else if ("name".equalsIgnoreCase((String) entry.getKey())) {
                        this.setName(this.getStrSubstitutor().replace((String) entry.getValue()));
                    } else if ("strict".equalsIgnoreCase((String) entry.getKey())) {
                        this.strict = Boolean.parseBoolean(this.getStrSubstitutor().replace((String) entry.getValue()));
                    } else if ("schema".equalsIgnoreCase((String) entry.getKey())) {
                        this.schema = this.getStrSubstitutor().replace((String) entry.getValue());
                    } else if ("monitorInterval".equalsIgnoreCase((String) entry.getKey())) {
                        int k = Integer.parseInt(this.getStrSubstitutor().replace((String) entry.getValue()));

                        if (k > 0 && this.configFile != null) {
                            this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, k);
                        }
                    } else if ("advertiser".equalsIgnoreCase((String) entry.getKey())) {
                        this.createAdvertiser(this.getStrSubstitutor().replace((String) entry.getValue()), configurationfactory_configurationsource, abyte, "text/xml");
                    }
                }
            }

            iterator = ((StatusLogger) XMLConfiguration.LOGGER).getListeners();
            boolean flag1 = false;

            while (iterator.hasNext()) {
                StatusListener statuslistener = (StatusListener) iterator.next();

                if (statuslistener instanceof StatusConsoleListener) {
                    flag1 = true;
                    ((StatusConsoleListener) statuslistener).setLevel(level);
                    if (!flag) {
                        ((StatusConsoleListener) statuslistener).setFilters(XMLConfiguration.VERBOSE_CLASSES);
                    }
                }
            }

            if (!flag1 && level != Level.OFF) {
                StatusConsoleListener statusconsolelistener = new StatusConsoleListener(level, printstream);

                if (!flag) {
                    statusconsolelistener.setFilters(XMLConfiguration.VERBOSE_CLASSES);
                }

                ((StatusLogger) XMLConfiguration.LOGGER).registerListener(statusconsolelistener);
                Iterator iterator1 = arraylist.iterator();

                while (iterator1.hasNext()) {
                    s = (String) iterator1.next();
                    XMLConfiguration.LOGGER.error(s);
                }
            }
        } catch (SAXException saxexception) {
            XMLConfiguration.LOGGER.error("Error parsing " + configurationfactory_configurationsource.getLocation(), (Throwable) saxexception);
        } catch (IOException ioexception) {
            XMLConfiguration.LOGGER.error("Error parsing " + configurationfactory_configurationsource.getLocation(), (Throwable) ioexception);
        } catch (ParserConfigurationException parserconfigurationexception) {
            XMLConfiguration.LOGGER.error("Error parsing " + configurationfactory_configurationsource.getLocation(), (Throwable) parserconfigurationexception);
        }

        if (this.strict && this.schema != null && abyte != null) {
            InputStream inputstream1 = null;

            try {
                inputstream1 = this.getClass().getClassLoader().getResourceAsStream(this.schema);
            } catch (Exception exception) {
                XMLConfiguration.LOGGER.error("Unable to access schema " + this.schema);
            }

            if (inputstream1 != null) {
                StreamSource streamsource = new StreamSource(inputstream1, "Log4j-config.xsd");
                SchemaFactory schemafactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
                Schema schema = null;

                try {
                    schema = schemafactory.newSchema(streamsource);
                } catch (SAXException saxexception1) {
                    XMLConfiguration.LOGGER.error("Error parsing Log4j schema", (Throwable) saxexception1);
                }

                if (schema != null) {
                    Validator validator = schema.newValidator();

                    try {
                        validator.validate(new StreamSource(new ByteArrayInputStream(abyte)));
                    } catch (IOException ioexception1) {
                        XMLConfiguration.LOGGER.error("Error reading configuration for validation", (Throwable) ioexception1);
                    } catch (SAXException saxexception2) {
                        XMLConfiguration.LOGGER.error("Error validating configuration", (Throwable) saxexception2);
                    }
                }
            }
        }

        if (this.getName() == null) {
            this.setName(configurationfactory_configurationsource.getLocation());
        }

    }

    public void setup() {
        if (this.rootElement == null) {
            XMLConfiguration.LOGGER.error("No logging configuration");
        } else {
            this.constructHierarchy(this.rootNode, this.rootElement);
            if (this.status.size() <= 0) {
                this.rootElement = null;
            } else {
                Iterator iterator = this.status.iterator();

                while (iterator.hasNext()) {
                    XMLConfiguration.Status xmlconfiguration_status = (XMLConfiguration.Status) iterator.next();

                    XMLConfiguration.LOGGER.error("Error processing element " + xmlconfiguration_status.name + ": " + xmlconfiguration_status.errorType);
                }

            }
        }
    }

    public Configuration reconfigure() {
        if (this.configFile != null) {
            try {
                ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);

                return new XMLConfiguration(configurationfactory_configurationsource);
            } catch (FileNotFoundException filenotfoundexception) {
                XMLConfiguration.LOGGER.error("Cannot locate file " + this.configFile, (Throwable) filenotfoundexception);
            }
        }

        return null;
    }

    private void constructHierarchy(Node node, Element element) {
        this.processAttributes(node, element);
        StringBuilder stringbuilder = new StringBuilder();
        NodeList nodelist = element.getChildNodes();
        List list = node.getChildren();

        for (int i = 0; i < nodelist.getLength(); ++i) {
            org.w3c.dom.Node org_w3c_dom_node = nodelist.item(i);

            if (org_w3c_dom_node instanceof Element) {
                Element element1 = (Element) org_w3c_dom_node;
                String s = this.getType(element1);
                PluginType plugintype = this.pluginManager.getPluginType(s);
                Node node1 = new Node(node, s, plugintype);

                this.constructHierarchy(node1, element1);
                if (plugintype == null) {
                    String s1 = node1.getValue();

                    if (!node1.hasChildren() && s1 != null) {
                        node.getAttributes().put(s, s1);
                    } else {
                        this.status.add(new XMLConfiguration.Status(s, element, XMLConfiguration.ErrorType.CLASS_NOT_FOUND));
                    }
                } else {
                    list.add(node1);
                }
            } else if (org_w3c_dom_node instanceof Text) {
                Text text = (Text) org_w3c_dom_node;

                stringbuilder.append(text.getData());
            }
        }

        String s2 = stringbuilder.toString().trim();

        if (s2.length() > 0 || !node.hasChildren() && !node.isRoot()) {
            node.setValue(s2);
        }

    }

    private String getType(Element element) {
        if (this.strict) {
            NamedNodeMap namednodemap = element.getAttributes();

            for (int i = 0; i < namednodemap.getLength(); ++i) {
                org.w3c.dom.Node org_w3c_dom_node = namednodemap.item(i);

                if (org_w3c_dom_node instanceof Attr) {
                    Attr attr = (Attr) org_w3c_dom_node;

                    if (attr.getName().equalsIgnoreCase("type")) {
                        String s = attr.getValue();

                        namednodemap.removeNamedItem(attr.getName());
                        return s;
                    }
                }
            }
        }

        return element.getTagName();
    }

    private byte[] toByteArray(InputStream inputstream) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] abyte = new byte[16384];

        int i;

        while ((i = inputstream.read(abyte, 0, abyte.length)) != -1) {
            bytearrayoutputstream.write(abyte, 0, i);
        }

        return bytearrayoutputstream.toByteArray();
    }

    private Map processAttributes(Node node, Element element) {
        NamedNodeMap namednodemap = element.getAttributes();
        Map map = node.getAttributes();

        for (int i = 0; i < namednodemap.getLength(); ++i) {
            org.w3c.dom.Node org_w3c_dom_node = namednodemap.item(i);

            if (org_w3c_dom_node instanceof Attr) {
                Attr attr = (Attr) org_w3c_dom_node;

                if (!attr.getName().equals("xml:base")) {
                    map.put(attr.getName(), attr.getValue());
                }
            }
        }

        return map;
    }

    private class Status {

        private final Element element;
        private final String name;
        private final XMLConfiguration.ErrorType errorType;

        public Status(String s, Element element, XMLConfiguration.ErrorType xmlconfiguration_errortype) {
            this.name = s;
            this.element = element;
            this.errorType = xmlconfiguration_errortype;
        }
    }

    private static enum ErrorType {

        CLASS_NOT_FOUND;
    }
}
