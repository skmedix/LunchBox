package org.bukkit.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class HandlerList {

    private volatile RegisteredListener[] handlers = null;
    private final EnumMap handlerslots = new EnumMap(EventPriority.class);
    private static ArrayList allLists = new ArrayList();

    public static void bakeAll() {
        ArrayList arraylist = HandlerList.allLists;

        synchronized (HandlerList.allLists) {
            Iterator iterator = HandlerList.allLists.iterator();

            while (iterator.hasNext()) {
                HandlerList h = (HandlerList) iterator.next();

                h.bake();
            }

        }
    }

    public static void unregisterAll() {
        ArrayList arraylist = HandlerList.allLists;

        synchronized (HandlerList.allLists) {
            Iterator iterator = HandlerList.allLists.iterator();

            while (iterator.hasNext()) {
                HandlerList h = (HandlerList) iterator.next();

                synchronized (h) {
                    Iterator iterator1 = h.handlerslots.values().iterator();

                    while (iterator1.hasNext()) {
                        List list = (List) iterator1.next();

                        list.clear();
                    }

                    h.handlers = null;
                }
            }

        }
    }

    public static void unregisterAll(Plugin plugin) {
        ArrayList arraylist = HandlerList.allLists;

        synchronized (HandlerList.allLists) {
            Iterator iterator = HandlerList.allLists.iterator();

            while (iterator.hasNext()) {
                HandlerList h = (HandlerList) iterator.next();

                h.unregister(plugin);
            }

        }
    }

    public static void unregisterAll(Listener listener) {
        ArrayList arraylist = HandlerList.allLists;

        synchronized (HandlerList.allLists) {
            Iterator iterator = HandlerList.allLists.iterator();

            while (iterator.hasNext()) {
                HandlerList h = (HandlerList) iterator.next();

                h.unregister(listener);
            }

        }
    }

    public HandlerList() {
        EventPriority[] aeventpriority;
        int i = (aeventpriority = EventPriority.values()).length;

        for (int j = 0; j < i; ++j) {
            EventPriority o = aeventpriority[j];

            this.handlerslots.put(o, new ArrayList());
        }

        ArrayList arraylist = HandlerList.allLists;

        synchronized (HandlerList.allLists) {
            HandlerList.allLists.add(this);
        }
    }

    public synchronized void register(RegisteredListener listener) {
        if (((ArrayList) this.handlerslots.get(listener.getPriority())).contains(listener)) {
            throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
        } else {
            this.handlers = null;
            ((ArrayList) this.handlerslots.get(listener.getPriority())).add(listener);
        }
    }

    public void registerAll(Collection listeners) {
        Iterator iterator = listeners.iterator();

        while (iterator.hasNext()) {
            RegisteredListener listener = (RegisteredListener) iterator.next();

            this.register(listener);
        }

    }

    public synchronized void unregister(RegisteredListener listener) {
        if (((ArrayList) this.handlerslots.get(listener.getPriority())).remove(listener)) {
            this.handlers = null;
        }

    }

    public synchronized void unregister(Plugin plugin) {
        boolean changed = false;
        Iterator iterator = this.handlerslots.values().iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();
            ListIterator i = list.listIterator();

            while (i.hasNext()) {
                if (((RegisteredListener) i.next()).getPlugin().equals(plugin)) {
                    i.remove();
                    changed = true;
                }
            }
        }

        if (changed) {
            this.handlers = null;
        }

    }

    public synchronized void unregister(Listener listener) {
        boolean changed = false;
        Iterator iterator = this.handlerslots.values().iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();
            ListIterator i = list.listIterator();

            while (i.hasNext()) {
                if (((RegisteredListener) i.next()).getListener().equals(listener)) {
                    i.remove();
                    changed = true;
                }
            }
        }

        if (changed) {
            this.handlers = null;
        }

    }

    public synchronized void bake() {
        if (this.handlers == null) {
            ArrayList entries = new ArrayList();
            Iterator iterator = this.handlerslots.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                entries.addAll((Collection) entry.getValue());
            }

            this.handlers = (RegisteredListener[]) entries.toArray(new RegisteredListener[entries.size()]);
        }
    }

    public RegisteredListener[] getRegisteredListeners() {
        while (true) {
            RegisteredListener[] handlers = this.handlers;

            if (this.handlers != null) {
                return handlers;
            }

            this.bake();
        }
    }

    public static ArrayList getRegisteredListeners(Plugin plugin) {
        ArrayList listeners = new ArrayList();
        ArrayList arraylist = HandlerList.allLists;

        synchronized (HandlerList.allLists) {
            Iterator iterator = HandlerList.allLists.iterator();

            while (iterator.hasNext()) {
                HandlerList h = (HandlerList) iterator.next();

                synchronized (h) {
                    Iterator iterator1 = h.handlerslots.values().iterator();

                    while (iterator1.hasNext()) {
                        List list = (List) iterator1.next();
                        Iterator iterator2 = list.iterator();

                        while (iterator2.hasNext()) {
                            RegisteredListener listener = (RegisteredListener) iterator2.next();

                            if (listener.getPlugin().equals(plugin)) {
                                listeners.add(listener);
                            }
                        }
                    }
                }
            }

            return listeners;
        }
    }

    public static ArrayList getHandlerLists() {
        ArrayList arraylist = HandlerList.allLists;

        synchronized (HandlerList.allLists) {
            return (ArrayList) HandlerList.allLists.clone();
        }
    }
}
