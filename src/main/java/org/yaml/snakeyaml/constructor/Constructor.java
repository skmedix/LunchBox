package org.yaml.snakeyaml.constructor;

import java.beans.IntrospectionException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public class Constructor extends SafeConstructor {

    private final Map typeTags;
    protected final Map typeDefinitions;

    public Constructor() {
        this(Object.class);
    }

    public Constructor(Class theRoot) {
        this(new TypeDescription(checkRoot(theRoot)));
    }

    private static Class checkRoot(Class theRoot) {
        if (theRoot == null) {
            throw new NullPointerException("Root class must be provided.");
        } else {
            return theRoot;
        }
    }

    public Constructor(TypeDescription theRoot) {
        if (theRoot == null) {
            throw new NullPointerException("Root type must be provided.");
        } else {
            this.yamlConstructors.put((Object) null, new Constructor.ConstructYamlObject());
            if (!Object.class.equals(theRoot.getType())) {
                this.rootTag = new Tag(theRoot.getType());
            }

            this.typeTags = new HashMap();
            this.typeDefinitions = new HashMap();
            this.yamlClassConstructors.put(NodeId.scalar, new Constructor.ConstructScalar());
            this.yamlClassConstructors.put(NodeId.mapping, new Constructor.ConstructMapping());
            this.yamlClassConstructors.put(NodeId.sequence, new Constructor.ConstructSequence());
            this.addTypeDescription(theRoot);
        }
    }

    public Constructor(String theRoot) throws ClassNotFoundException {
        this(Class.forName(check(theRoot)));
    }

    private static final String check(String s) {
        if (s == null) {
            throw new NullPointerException("Root type must be provided.");
        } else if (s.trim().length() == 0) {
            throw new YAMLException("Root type must be provided.");
        } else {
            return s;
        }
    }

    public TypeDescription addTypeDescription(TypeDescription definition) {
        if (definition == null) {
            throw new NullPointerException("TypeDescription is required.");
        } else {
            Tag tag = definition.getTag();

            this.typeTags.put(tag, definition.getType());
            return (TypeDescription) this.typeDefinitions.put(definition.getType(), definition);
        }
    }

    protected Class getClassForNode(Node node) {
        Class classForTag = (Class) this.typeTags.get(node.getTag());

        if (classForTag == null) {
            String name = node.getTag().getClassName();

            Class cl;

            try {
                cl = this.getClassForName(name);
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new YAMLException("Class not found: " + name);
            }

            this.typeTags.put(node.getTag(), cl);
            return cl;
        } else {
            return classForTag;
        }
    }

    protected Class getClassForName(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$yaml$snakeyaml$nodes$NodeId = new int[NodeId.values().length];

        static {
            try {
                Constructor.SyntheticClass_1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[NodeId.sequence.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                Constructor.SyntheticClass_1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[NodeId.mapping.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

        }
    }

    protected class ConstructSequence implements Construct {

        public Object construct(Node node) {
            SequenceNode snode = (SequenceNode) node;

            if (Set.class.isAssignableFrom(node.getType())) {
                if (node.isTwoStepsConstruction()) {
                    throw new YAMLException("Set cannot be recursive.");
                } else {
                    return Constructor.this.constructSet(snode);
                }
            } else if (Collection.class.isAssignableFrom(node.getType())) {
                return node.isTwoStepsConstruction() ? Constructor.this.createDefaultList(snode.getValue().size()) : Constructor.this.constructSequence(snode);
            } else if (node.getType().isArray()) {
                return node.isTwoStepsConstruction() ? Constructor.this.createArray(node.getType(), snode.getValue().size()) : Constructor.this.constructArray(snode);
            } else {
                ArrayList possibleConstructors = new ArrayList(snode.getValue().size());
                java.lang.reflect.Constructor[] argumentList = node.getType().getConstructors();
                int parameterTypes = argumentList.length;

                int index;

                for (index = 0; index < parameterTypes; ++index) {
                    java.lang.reflect.Constructor i$ = argumentList[index];

                    if (snode.getValue().size() == i$.getParameterTypes().length) {
                        possibleConstructors.add(i$);
                    }
                }

                if (!possibleConstructors.isEmpty()) {
                    Iterator iterator;

                    if (possibleConstructors.size() == 1) {
                        Object[] aobject = new Object[snode.getValue().size()];
                        java.lang.reflect.Constructor java_lang_reflect_constructor = (java.lang.reflect.Constructor) possibleConstructors.get(0);

                        index = 0;

                        Node node;

                        for (iterator = snode.getValue().iterator(); iterator.hasNext(); aobject[index++] = Constructor.this.constructObject(node)) {
                            node = (Node) iterator.next();
                            Class oclass = java_lang_reflect_constructor.getParameterTypes()[index];

                            node.setType(oclass);
                        }

                        try {
                            return java_lang_reflect_constructor.newInstance(aobject);
                        } catch (Exception exception) {
                            throw new YAMLException(exception);
                        }
                    }

                    List list = Constructor.this.constructSequence(snode);
                    Class[] aclass = new Class[list.size()];

                    index = 0;

                    for (iterator = list.iterator(); iterator.hasNext(); ++index) {
                        Object c = iterator.next();

                        aclass[index] = c.getClass();
                    }

                    iterator = possibleConstructors.iterator();

                    while (iterator.hasNext()) {
                        java.lang.reflect.Constructor java_lang_reflect_constructor1 = (java.lang.reflect.Constructor) iterator.next();
                        Class[] argTypes = java_lang_reflect_constructor1.getParameterTypes();
                        boolean foundConstructor = true;

                        for (int e = 0; e < argTypes.length; ++e) {
                            if (!this.wrapIfPrimitive(argTypes[e]).isAssignableFrom(aclass[e])) {
                                foundConstructor = false;
                                break;
                            }
                        }

                        if (foundConstructor) {
                            try {
                                return java_lang_reflect_constructor1.newInstance(list.toArray());
                            } catch (Exception exception1) {
                                throw new YAMLException(exception1);
                            }
                        }
                    }
                }

                throw new YAMLException("No suitable constructor with " + String.valueOf(snode.getValue().size()) + " arguments found for " + node.getType());
            }
        }

        private final Class wrapIfPrimitive(Class clazz) {
            if (!clazz.isPrimitive()) {
                return clazz;
            } else if (clazz == Integer.TYPE) {
                return Integer.class;
            } else if (clazz == Float.TYPE) {
                return Float.class;
            } else if (clazz == Double.TYPE) {
                return Double.class;
            } else if (clazz == Boolean.TYPE) {
                return Boolean.class;
            } else if (clazz == Long.TYPE) {
                return Long.class;
            } else if (clazz == Character.TYPE) {
                return Character.class;
            } else if (clazz == Short.TYPE) {
                return Short.class;
            } else if (clazz == Byte.TYPE) {
                return Byte.class;
            } else {
                throw new YAMLException("Unexpected primitive " + clazz);
            }
        }

        public void construct2ndStep(Node node, Object object) {
            SequenceNode snode = (SequenceNode) node;

            if (List.class.isAssignableFrom(node.getType())) {
                List list = (List) object;

                Constructor.this.constructSequenceStep2(snode, list);
            } else {
                if (!node.getType().isArray()) {
                    throw new YAMLException("Immutable objects cannot be recursive.");
                }

                Constructor.this.constructArrayStep2(snode, object);
            }

        }
    }

    protected class ConstructScalar extends AbstractConstruct {

        public Object construct(Node nnode) {
            ScalarNode node = (ScalarNode) nnode;
            Class type = node.getType();
            Object result;

            if (!type.isPrimitive() && type != String.class && !Number.class.isAssignableFrom(type) && type != Boolean.class && !Date.class.isAssignableFrom(type) && type != Character.class && type != BigInteger.class && type != BigDecimal.class && !Enum.class.isAssignableFrom(type) && !Tag.BINARY.equals(node.getTag()) && !Calendar.class.isAssignableFrom(type)) {
                java.lang.reflect.Constructor[] javaConstructors = type.getConstructors();
                int oneArgCount = 0;
                java.lang.reflect.Constructor javaConstructor = null;
                java.lang.reflect.Constructor[] argument = javaConstructors;
                int e = javaConstructors.length;

                for (int i$ = 0; i$ < e; ++i$) {
                    java.lang.reflect.Constructor c = argument[i$];

                    if (c.getParameterTypes().length == 1) {
                        ++oneArgCount;
                        javaConstructor = c;
                    }
                }

                if (javaConstructor == null) {
                    throw new YAMLException("No single argument constructor found for " + type);
                }

                Object object;

                if (oneArgCount == 1) {
                    object = this.constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
                } else {
                    object = Constructor.this.constructScalar(node);

                    try {
                        javaConstructor = type.getConstructor(new Class[] { String.class});
                    } catch (Exception exception) {
                        throw new YAMLException("Can\'t construct a java object for scalar " + node.getTag() + "; No String constructor found. Exception=" + exception.getMessage(), exception);
                    }
                }

                try {
                    result = javaConstructor.newInstance(new Object[] { object});
                } catch (Exception exception1) {
                    throw new ConstructorException((String) null, (Mark) null, "Can\'t construct a java object for scalar " + node.getTag() + "; exception=" + exception1.getMessage(), node.getStartMark(), exception1);
                }
            } else {
                result = this.constructStandardJavaInstance(type, node);
            }

            return result;
        }

        private Object constructStandardJavaInstance(Class type, ScalarNode node) {
            Construct contr;
            Object result;

            if (type == String.class) {
                contr = (Construct) Constructor.this.yamlConstructors.get(Tag.STR);
                result = contr.construct(node);
            } else if (type != Boolean.class && type != Boolean.TYPE) {
                if (type != Character.class && type != Character.TYPE) {
                    if (Date.class.isAssignableFrom(type)) {
                        contr = (Construct) Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
                        Date ex1 = (Date) contr.construct(node);

                        if (type == Date.class) {
                            result = ex1;
                        } else {
                            try {
                                java.lang.reflect.Constructor e = type.getConstructor(new Class[] { Long.TYPE});

                                result = e.newInstance(new Object[] { Long.valueOf(ex1.getTime())});
                            } catch (RuntimeException runtimeexception) {
                                throw runtimeexception;
                            } catch (Exception exception) {
                                throw new YAMLException("Cannot construct: \'" + type + "\'");
                            }
                        }
                    } else if (type != Float.class && type != Double.class && type != Float.TYPE && type != Double.TYPE && type != BigDecimal.class) {
                        if (type != Byte.class && type != Short.class && type != Integer.class && type != Long.class && type != BigInteger.class && type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE) {
                            if (Enum.class.isAssignableFrom(type)) {
                                String contr1 = node.getValue();

                                try {
                                    result = Enum.valueOf(type, contr1);
                                } catch (Exception exception1) {
                                    throw new YAMLException("Unable to find enum value \'" + contr1 + "\' for enum class: " + type.getName());
                                }
                            } else if (Calendar.class.isAssignableFrom(type)) {
                                SafeConstructor.ConstructYamlTimestamp contr2 = new SafeConstructor.ConstructYamlTimestamp();

                                contr2.construct(node);
                                result = contr2.getCalendar();
                            } else {
                                if (!Number.class.isAssignableFrom(type)) {
                                    throw new YAMLException("Unsupported class: " + type);
                                }

                                SafeConstructor.ConstructYamlNumber contr3 = Constructor.this.new ConstructYamlNumber();

                                result = contr3.construct(node);
                            }
                        } else {
                            contr = (Construct) Constructor.this.yamlConstructors.get(Tag.INT);
                            result = contr.construct(node);
                            if (type != Byte.class && type != Byte.TYPE) {
                                if (type != Short.class && type != Short.TYPE) {
                                    if (type != Integer.class && type != Integer.TYPE) {
                                        if (type != Long.class && type != Long.TYPE) {
                                            result = new BigInteger(result.toString());
                                        } else {
                                            result = Long.valueOf(result.toString());
                                        }
                                    } else {
                                        result = Integer.valueOf(Integer.parseInt(result.toString()));
                                    }
                                } else {
                                    result = Short.valueOf(result.toString());
                                }
                            } else {
                                result = Byte.valueOf(result.toString());
                            }
                        }
                    } else if (type == BigDecimal.class) {
                        result = new BigDecimal(node.getValue());
                    } else {
                        contr = (Construct) Constructor.this.yamlConstructors.get(Tag.FLOAT);
                        result = contr.construct(node);
                        if (type == Float.class || type == Float.TYPE) {
                            result = new Float(((Double) result).doubleValue());
                        }
                    }
                } else {
                    contr = (Construct) Constructor.this.yamlConstructors.get(Tag.STR);
                    String ex = (String) contr.construct(node);

                    if (ex.length() == 0) {
                        result = null;
                    } else {
                        if (ex.length() != 1) {
                            throw new YAMLException("Invalid node Character: \'" + ex + "\'; length: " + ex.length());
                        }

                        result = Character.valueOf(ex.charAt(0));
                    }
                }
            } else {
                contr = (Construct) Constructor.this.yamlConstructors.get(Tag.BOOL);
                result = contr.construct(node);
            }

            return result;
        }
    }

    protected class ConstructYamlObject implements Construct {

        private Construct getConstructor(Node node) {
            Class cl = Constructor.this.getClassForNode(node);

            node.setType(cl);
            Construct constructor = (Construct) Constructor.this.yamlClassConstructors.get(node.getNodeId());

            return constructor;
        }

        public Object construct(Node node) {
            Object result = null;

            try {
                result = this.getConstructor(node).construct(node);
                return result;
            } catch (ConstructorException constructorexception) {
                throw constructorexception;
            } catch (Exception exception) {
                throw new ConstructorException((String) null, (Mark) null, "Can\'t construct a java object for " + node.getTag() + "; exception=" + exception.getMessage(), node.getStartMark(), exception);
            }
        }

        public void construct2ndStep(Node node, Object object) {
            try {
                this.getConstructor(node).construct2ndStep(node, object);
            } catch (Exception exception) {
                throw new ConstructorException((String) null, (Mark) null, "Can\'t construct a second step for a java object for " + node.getTag() + "; exception=" + exception.getMessage(), node.getStartMark(), exception);
            }
        }
    }

    protected class ConstructMapping implements Construct {

        public Object construct(Node node) {
            MappingNode mnode = (MappingNode) node;

            if (Properties.class.isAssignableFrom(node.getType())) {
                Properties set2 = new Properties();

                if (!node.isTwoStepsConstruction()) {
                    Constructor.this.constructMapping2ndStep(mnode, set2);
                    return set2;
                } else {
                    throw new YAMLException("Properties must not be recursive.");
                }
            } else if (SortedMap.class.isAssignableFrom(node.getType())) {
                TreeMap set1 = new TreeMap();

                if (!node.isTwoStepsConstruction()) {
                    Constructor.this.constructMapping2ndStep(mnode, set1);
                }

                return set1;
            } else if (Map.class.isAssignableFrom(node.getType())) {
                return node.isTwoStepsConstruction() ? Constructor.this.createDefaultMap() : Constructor.this.constructMapping(mnode);
            } else if (SortedSet.class.isAssignableFrom(node.getType())) {
                TreeSet set = new TreeSet();

                Constructor.this.constructSet2ndStep(mnode, set);
                return set;
            } else {
                return Collection.class.isAssignableFrom(node.getType()) ? (node.isTwoStepsConstruction() ? Constructor.this.createDefaultSet() : Constructor.this.constructSet(mnode)) : (node.isTwoStepsConstruction() ? this.createEmptyJavaBean(mnode) : this.constructJavaBean2ndStep(mnode, this.createEmptyJavaBean(mnode)));
            }
        }

        public void construct2ndStep(Node node, Object object) {
            if (Map.class.isAssignableFrom(node.getType())) {
                Constructor.this.constructMapping2ndStep((MappingNode) node, (Map) object);
            } else if (Set.class.isAssignableFrom(node.getType())) {
                Constructor.this.constructSet2ndStep((MappingNode) node, (Set) object);
            } else {
                this.constructJavaBean2ndStep((MappingNode) node, object);
            }

        }

        protected Object createEmptyJavaBean(MappingNode node) {
            try {
                java.lang.reflect.Constructor e = node.getType().getDeclaredConstructor(new Class[0]);

                e.setAccessible(true);
                return e.newInstance(new Object[0]);
            } catch (Exception exception) {
                throw new YAMLException(exception);
            }
        }

        protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
            Constructor.this.flattenMapping(node);
            Class beanType = node.getType();
            List nodeValue = node.getValue();
            Iterator i$ = nodeValue.iterator();

            while (i$.hasNext()) {
                NodeTuple tuple = (NodeTuple) i$.next();

                if (!(tuple.getKeyNode() instanceof ScalarNode)) {
                    throw new YAMLException("Keys must be scalars but found: " + tuple.getKeyNode());
                }

                ScalarNode keyNode = (ScalarNode) tuple.getKeyNode();
                Node valueNode = tuple.getValueNode();

                keyNode.setType(String.class);
                String key = (String) Constructor.this.constructObject(keyNode);

                try {
                    Property e = this.getProperty(beanType, key);

                    valueNode.setType(e.getType());
                    TypeDescription memberDescription = (TypeDescription) Constructor.this.typeDefinitions.get(beanType);
                    boolean typeDetected = false;
                    Class ketType;
                    MappingNode valueType;

                    if (memberDescription != null) {
                        switch (Constructor.SyntheticClass_1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[valueNode.getNodeId().ordinal()]) {
                        case 1:
                            SequenceNode value = (SequenceNode) valueNode;

                            ketType = memberDescription.getListPropertyType(key);
                            if (ketType != null) {
                                value.setListType(ketType);
                                typeDetected = true;
                            } else if (e.getType().isArray()) {
                                value.setListType(e.getType().getComponentType());
                                typeDetected = true;
                            }
                            break;

                        case 2:
                            valueType = (MappingNode) valueNode;
                            Class mnode = memberDescription.getMapKeyType(key);

                            if (mnode != null) {
                                valueType.setTypes(mnode, memberDescription.getMapValueType(key));
                                typeDetected = true;
                            }
                        }
                    }

                    if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
                        Class[] value1 = e.getActualTypeArguments();

                        if (value1 != null && value1.length > 0) {
                            if (valueNode.getNodeId() == NodeId.sequence) {
                                ketType = value1[0];
                                SequenceNode valueType1 = (SequenceNode) valueNode;

                                valueType1.setListType(ketType);
                            } else if (valueNode.getTag().equals(Tag.SET)) {
                                ketType = value1[0];
                                valueType = (MappingNode) valueNode;
                                valueType.setOnlyKeyType(ketType);
                                valueType.setUseClassConstructor(Boolean.valueOf(true));
                            } else if (e.getType().isAssignableFrom(Map.class)) {
                                ketType = value1[0];
                                Class valueType2 = value1[1];
                                MappingNode mnode1 = (MappingNode) valueNode;

                                mnode1.setTypes(ketType, valueType2);
                                mnode1.setUseClassConstructor(Boolean.valueOf(true));
                            }
                        }
                    }

                    Object value2 = Constructor.this.constructObject(valueNode);

                    if ((e.getType() == Float.TYPE || e.getType() == Float.class) && value2 instanceof Double) {
                        value2 = Float.valueOf(((Double) value2).floatValue());
                    }

                    e.set(object, value2);
                } catch (Exception exception) {
                    throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node.getStartMark(), exception.getMessage(), valueNode.getStartMark(), exception);
                }
            }

            return object;
        }

        protected Property getProperty(Class type, String name) throws IntrospectionException {
            return Constructor.this.getPropertyUtils().getProperty(type, name);
        }
    }
}
