/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package linsdale.nbpcg.supportlib;

import java.awt.EventQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A class to implement Listening. Allowing listeners to register and actions to
 * fire
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class Listening<P extends ListenerParams> {

    public static final int IMMEDIATE = 1;
    public static final int EVENTQUEUE = 0;
    private static final int QUEUEMASK = 1;
    public static final int PRIORITY = 2;
    public static final int NORMAL = 0;
    private static final int PRIORITYMASK = 2;
    private final ListenerStore<P> listenersImmediate = new ListenerStore<>("Immediate");
    private final ListenerStore<P> listenersEventQueue = new ListenerStore<>("EventQueue");
    private final String description;

    public Listening(String description) {
        Log.get("linsdale.nbpcg.supportlib.listener").log(Level.FINEST, "Listening {0}: created", description);
        this.description = description;
    }

    /**
     * Add a listener - will fire action on EventQueue
     *
     * @param listener the listener
     */
    public void addListener(Listener<P> listener) {
        addListener(listener, EVENTQUEUE + NORMAL);
    }

    /**
     * Add a listener - will fire action on calling thread or EventQueue
     * depending on parameter setting.
     *
     * @param listener the listener
     * @param fireimmediately true if action can fire on immediately on the
     * "firing" thread, false will cause the action to be handled later on the
     * EventQueue.
     */
    public void addListener(Listener<P> listener, int flags) {
        if (listener != null) {
            if ((flags & QUEUEMASK) == IMMEDIATE) {
                listenersImmediate.add(listener, (flags & PRIORITYMASK) == PRIORITY);
            } else {
                listenersEventQueue.add(listener, (flags & PRIORITYMASK) == PRIORITY);
            }
        }
    }

    /**
     * Remove a listener
     *
     * @param listener the listener
     */
    public void removeListener(Listener<P> listener) {
        Log.get("linsdale.nbpcg.supportlib.listener").log(Level.FINEST, "Listening {0}: remove listener {1}", new Object[]{description, listener});
        listenersImmediate.remove(listener);
        listenersEventQueue.remove(listener); // remove a listener from either queue
    }

    public boolean hasListener() {
        return listenersImmediate.hasListener() || listenersEventQueue.hasListener();
    }

    /**
     * Fire all listeners registered - event will fire immediately on the
     * calling thread or later on the EventQueue.
     *
     * @param lp the listener parameters object
     */
    public void fire(P p) {
        Log.get("linsdale.nbpcg.supportlib.listener").log(Level.FINEST, "Listening {0}:  fire {1}; {2} immediate & {3} on eventqueue",
                new Object[]{description, p, listenersImmediate.size(), listenersEventQueue.size()});
        listenersImmediate.fire(p);
        if (EventQueue.isDispatchThread()) {
            listenersEventQueue.fire(p);
        } else {
            listenersEventQueue.fireLaterOnEventQueue(p);
        }
    }

    private class ListenerStore<P extends ListenerParams> {

        private final List<WeakReference<Listener<P>>> listeners = new ArrayList<>();
        private final String queuetype;

        public ListenerStore(String queuetype) {
            this.queuetype = queuetype;
        }

        private void removeEmptyReferences(){
            while (removeEmptyReference()){}
        }
            
        private boolean removeEmptyReference() {
            int idx = 0;
            for (WeakReference<Listener<P>> weaklistener : listeners) {
                Listener<P> listener = weaklistener.get();
                if (listener == null) {
                    listeners.remove(idx);
                    return true; 
                }
                idx++;
            }
            return false;
        }
                
        private List<Listener<P>> allListeners() {
            removeEmptyReferences();
            List<Listener<P>> copy = new ArrayList<>();
            for (WeakReference<Listener<P>> weaklistener : listeners) {
                Listener<P> listener = weaklistener.get();
                if (listener != null) {
                    copy.add(listener);
                }
            }
            return copy;
        }

        public final synchronized void fire(P p) {
            for (Listener<P> listener : allListeners()) {
                listener.actionPerformed(p);
            }
        }

        public final synchronized void fireLaterOnEventQueue(P p) {
            for (Listener<P> listener : allListeners()) {
                EventQueue.invokeLater(new FireEventQueueListener<>(listener, p));
            }
        }

        public final synchronized void add(Listener<P> listener, boolean priority) {
            if (allListeners().contains(listener)) {
                Log.get("linsdale.nbpcg.supportlib.listener").log(Level.FINEST, "Listening {0}: failed to add listener {1} (type={3};priority={2}) - reason duplicate", new Object[]{description, listener, priority ? "Priority" : "Normal", queuetype});
            } else {
                Log.get("linsdale.nbpcg.supportlib.listener").log(Level.FINEST, "Listening {0}: added listener {1} (type={3};priority={2})", new Object[]{description, listener, priority ? "Priority" : "Normal", queuetype});
                if (priority) {
                    listeners.add(0, new WeakReference<>(listener));
                } else {
                    listeners.add(new WeakReference<>(listener));
                }
            }
        }

        public final synchronized void remove(Listener<P> listener) {
            removeEmptyReferences();
            int idx = 0;
            for (WeakReference<Listener<P>> weaklistener : listeners) {
                Listener<P> testingListener = weaklistener.get();
                if (testingListener != null) {
                    if (testingListener == listener) {
                        listeners.remove(idx);
                        return;
                    }
                }
                idx++;
            }
        }

        public final synchronized boolean hasListener() {
            return !allListeners().isEmpty();
        }

        public final synchronized int size() {
            return allListeners().size();
        }
    }

    private class FireEventQueueListener<P extends ListenerParams> implements Runnable {

        private P p;
        private Listener<P> al;

        public FireEventQueueListener(Listener<P> al, P p) {
            this.al = al;
            this.p = p;
        }

        @Override
        public void run() {
            al.actionPerformed(p);
        }
    }
}
