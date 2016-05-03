package org.apache.commons.lang3.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;

public class EventListenerSupport implements Serializable {

    private static final long serialVersionUID = 3593265990380473632L;
    private List listeners;
    private transient Object proxy;
    private transient Object[] prototypeArray;

    public static EventListenerSupport create(Class oclass) {
        return new EventListenerSupport(oclass);
    }

    public EventListenerSupport(Class oclass) {
        this(oclass, Thread.currentThread().getContextClassLoader());
    }

    public EventListenerSupport(Class oclass, ClassLoader classloader) {
        this();
        Validate.notNull(oclass, "Listener interface cannot be null.", new Object[0]);
        Validate.notNull(classloader, "ClassLoader cannot be null.", new Object[0]);
        Validate.isTrue(oclass.isInterface(), "Class {0} is not an interface", new Object[] { oclass.getName()});
        this.initializeTransientFields(oclass, classloader);
    }

    private EventListenerSupport() {
        this.listeners = new CopyOnWriteArrayList();
    }

    public Object fire() {
        return this.proxy;
    }

    public void addListener(Object object) {
        Validate.notNull(object, "Listener object cannot be null.", new Object[0]);
        this.listeners.add(object);
    }

    int getListenerCount() {
        return this.listeners.size();
    }

    public void removeListener(Object object) {
        Validate.notNull(object, "Listener object cannot be null.", new Object[0]);
        this.listeners.remove(object);
    }

    public Object[] getListeners() {
        return this.listeners.toArray(this.prototypeArray);
    }

    private void writeObject(ObjectOutputStream objectoutputstream) throws IOException {
        ArrayList arraylist = new ArrayList();
        ObjectOutputStream objectoutputstream1 = new ObjectOutputStream(new ByteArrayOutputStream());
        Iterator iterator = this.listeners.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            try {
                objectoutputstream1.writeObject(object);
                arraylist.add(object);
            } catch (IOException ioexception) {
                objectoutputstream1 = new ObjectOutputStream(new ByteArrayOutputStream());
            }
        }

        objectoutputstream.writeObject(arraylist.toArray(this.prototypeArray));
    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException {
        Object[] aobject = (Object[]) ((Object[]) objectinputstream.readObject());

        this.listeners = new CopyOnWriteArrayList(aobject);
        Class oclass = aobject.getClass().getComponentType();

        this.initializeTransientFields(oclass, Thread.currentThread().getContextClassLoader());
    }

    private void initializeTransientFields(Class oclass, ClassLoader classloader) {
        Object[] aobject = (Object[]) ((Object[]) Array.newInstance(oclass, 0));

        this.prototypeArray = aobject;
        this.createProxy(oclass, classloader);
    }

    private void createProxy(Class oclass, ClassLoader classloader) {
        this.proxy = oclass.cast(Proxy.newProxyInstance(classloader, new Class[] { oclass}, this.createInvocationHandler()));
    }

    protected InvocationHandler createInvocationHandler() {
        return new EventListenerSupport.ProxyInvocationHandler();
    }

    protected class ProxyInvocationHandler implements InvocationHandler {

        public Object invoke(Object object, Method method, Object[] aobject) throws Throwable {
            Iterator iterator = EventListenerSupport.this.listeners.iterator();

            while (iterator.hasNext()) {
                Object object1 = iterator.next();

                method.invoke(object1, aobject);
            }

            return null;
        }
    }
}
