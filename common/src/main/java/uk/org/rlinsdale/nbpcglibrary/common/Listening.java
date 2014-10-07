/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcglibrary.common;

import java.awt.EventQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A class to implement Listening. Allowing listeners to register and actions to
 * fire
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <P> the listener parameter class
 */
public class Listening<P extends ListenerParams> {

    /**
     * Flag - indicates the event should fire immediately on the observer's
     * thread.
     */
    public static final int IMMEDIATE = 1;

    /**
     * Flag - indicates the event should fire as soon as possible on the Event
     * queue thread.
     */
    public static final int EVENTQUEUE = 0;
    private static final int QUEUEMASK = 1;

    /**
     * Flag - indicates the event should be given priority. Priority events in
     * any one class (immediate or eventqueue) will always fire prior to those
     * with normal priority.
     */
    public static final int PRIORITY = 2;

    /**
     * Flag - indicates the event has normal priority.
     */
    public static final int NORMAL = 0;
    private static final int PRIORITYMASK = 2;
    private final ListenerStore<P> listenersImmediate = new ListenerStore<>("Immediate");
    private final ListenerStore<P> listenersEventQueue = new ListenerStore<>("EventQueue");
    private final String description;

    /**
     * Constructor
     *
     * @param description the listening's descriptive name - for use in error
     * /log reporting
     */
    public Listening(String description) {
        Log.get("uk.org.rlinsdale.nbpcglibrary.common.listener").log(Level.FINEST, "Listening {0}: created", description);
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
     * @param flags flags indicating priority v. normal and immediate v event
     * queue
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
        Log.get("uk.org.rlinsdale.nbpcglibrary.common.listener").log(Level.FINEST, "Listening {0}: remove listener {1}", new Object[]{description, listener});
        listenersImmediate.remove(listener);
        listenersEventQueue.remove(listener); // remove a listener from either queue
    }

    /**
     * Test if there are listeners registered with this listening.
     *
     * @return true if listeners are registered
     */
    public boolean hasListener() {
        return listenersImmediate.hasListener() || listenersEventQueue.hasListener();
    }

    /**
     * Fire all listeners registered - event will fire immediately on the
     * calling thread or later on the EventQueue.
     *
     * @param p the listener parameters object
     */
    public void fire(P p) {
        Log.get("uk.org.rlinsdale.nbpcglibrary.common.listener").log(Level.FINEST, "Listening {0}:  fire {1}; {2} immediate & {3} on eventqueue",
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

        private void removeEmptyReferences() {
            while (removeEmptyReference()) {
            }
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
            listeners.stream().map((weaklistener) -> weaklistener.get()).filter((listener) -> (listener != null)).forEach((listener) -> {
                copy.add(listener);
            });
            return copy;
        }

        public final synchronized void fire(P p) {
            allListeners().stream().forEach((listener) -> {
                listener.actionPerformed(p);
            });
        }

        public final synchronized void fireLaterOnEventQueue(P p) {
            allListeners().stream().forEach((listener) -> {
                EventQueue.invokeLater(new FireEventQueueListener<>(listener, p));
            });
        }

        public final synchronized void add(Listener<P> listener, boolean priority) {
            if (allListeners().contains(listener)) {
                Log.get("uk.org.rlinsdale.nbpcglibrary.common.listener").log(Level.FINEST, "Listening {0}: failed to add listener {1} (type={3};priority={2}) - reason duplicate", new Object[]{description, listener, priority ? "Priority" : "Normal", queuetype});
            } else {
                Log.get("uk.org.rlinsdale.nbpcglibrary.common.listener").log(Level.FINEST, "Listening {0}: added listener {1} (type={3};priority={2})", new Object[]{description, listener, priority ? "Priority" : "Normal", queuetype});
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

        private final P p;
        private final Listener<P> al;

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
