package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Proxy;

public class ClassLoaderObjectInputStream extends ObjectInputStream {

    private final ClassLoader classLoader;

    public ClassLoaderObjectInputStream(ClassLoader classloader, InputStream inputstream) throws IOException, StreamCorruptedException {
        super(inputstream);
        this.classLoader = classloader;
    }

    protected Class resolveClass(ObjectStreamClass objectstreamclass) throws IOException, ClassNotFoundException {
        Class oclass = Class.forName(objectstreamclass.getName(), false, this.classLoader);

        return oclass != null ? oclass : super.resolveClass(objectstreamclass);
    }

    protected Class resolveProxyClass(String[] astring) throws IOException, ClassNotFoundException {
        Class[] aclass = new Class[astring.length];

        for (int i = 0; i < astring.length; ++i) {
            aclass[i] = Class.forName(astring[i], false, this.classLoader);
        }

        try {
            return Proxy.getProxyClass(this.classLoader, aclass);
        } catch (IllegalArgumentException illegalargumentexception) {
            return super.resolveProxyClass(astring);
        }
    }
}
