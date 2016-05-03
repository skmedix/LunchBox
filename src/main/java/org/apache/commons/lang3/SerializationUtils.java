package org.apache.commons.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {

    public static Serializable clone(Serializable serializable) {
        if (serializable == null) {
            return null;
        } else {
            byte[] abyte = serialize(serializable);
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
            SerializationUtils.ClassLoaderAwareObjectInputStream serializationutils_classloaderawareobjectinputstream = null;

            Serializable serializable1;

            try {
                serializationutils_classloaderawareobjectinputstream = new SerializationUtils.ClassLoaderAwareObjectInputStream(bytearrayinputstream, serializable.getClass().getClassLoader());
                Serializable serializable2 = (Serializable) serializationutils_classloaderawareobjectinputstream.readObject();

                serializable1 = serializable2;
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new SerializationException("ClassNotFoundException while reading cloned object data", classnotfoundexception);
            } catch (IOException ioexception) {
                throw new SerializationException("IOException while reading cloned object data", ioexception);
            } finally {
                try {
                    if (serializationutils_classloaderawareobjectinputstream != null) {
                        serializationutils_classloaderawareobjectinputstream.close();
                    }
                } catch (IOException ioexception1) {
                    throw new SerializationException("IOException on closing cloned object data InputStream.", ioexception1);
                }

            }

            return serializable1;
        }
    }

    public static Serializable roundtrip(Serializable serializable) {
        return (Serializable) deserialize(serialize(serializable));
    }

    public static void serialize(Serializable serializable, OutputStream outputstream) {
        if (outputstream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        } else {
            ObjectOutputStream objectoutputstream = null;

            try {
                objectoutputstream = new ObjectOutputStream(outputstream);
                objectoutputstream.writeObject(serializable);
            } catch (IOException ioexception) {
                throw new SerializationException(ioexception);
            } finally {
                try {
                    if (objectoutputstream != null) {
                        objectoutputstream.close();
                    }
                } catch (IOException ioexception1) {
                    ;
                }

            }

        }
    }

    public static byte[] serialize(Serializable serializable) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(512);

        serialize(serializable, bytearrayoutputstream);
        return bytearrayoutputstream.toByteArray();
    }

    public static Object deserialize(InputStream inputstream) {
        if (inputstream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        } else {
            ObjectInputStream objectinputstream = null;

            Object object;

            try {
                objectinputstream = new ObjectInputStream(inputstream);
                Object object1 = objectinputstream.readObject();

                object = object1;
            } catch (ClassCastException classcastexception) {
                throw new SerializationException(classcastexception);
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new SerializationException(classnotfoundexception);
            } catch (IOException ioexception) {
                throw new SerializationException(ioexception);
            } finally {
                try {
                    if (objectinputstream != null) {
                        objectinputstream.close();
                    }
                } catch (IOException ioexception1) {
                    ;
                }

            }

            return object;
        }
    }

    public static Object deserialize(byte[] abyte) {
        if (abyte == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        } else {
            return deserialize((InputStream) (new ByteArrayInputStream(abyte)));
        }
    }

    static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {

        private static final Map primitiveTypes = new HashMap();
        private final ClassLoader classLoader;

        public ClassLoaderAwareObjectInputStream(InputStream inputstream, ClassLoader classloader) throws IOException {
            super(inputstream);
            this.classLoader = classloader;
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("byte", Byte.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("short", Short.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("int", Integer.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("long", Long.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("float", Float.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("double", Double.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("boolean", Boolean.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("char", Character.TYPE);
            SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.put("void", Void.TYPE);
        }

        protected Class resolveClass(ObjectStreamClass objectstreamclass) throws IOException, ClassNotFoundException {
            String s = objectstreamclass.getName();

            try {
                return Class.forName(s, false, this.classLoader);
            } catch (ClassNotFoundException classnotfoundexception) {
                try {
                    return Class.forName(s, false, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException classnotfoundexception1) {
                    Class oclass = (Class) SerializationUtils.ClassLoaderAwareObjectInputStream.primitiveTypes.get(s);

                    if (oclass != null) {
                        return oclass;
                    } else {
                        throw classnotfoundexception1;
                    }
                }
            }
        }
    }
}
