/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.theretiredprogrammer.nbpcglibrary.common;

import uk.theretiredprogrammer.nbpcglibrary.api.EventParams;
import uk.theretiredprogrammer.nbpcglibrary.api.HasInstanceDescription;
import java.awt.EventQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A class to implement Listening. Allowing listeners to register and actions to
 * fire
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <P> the listener parameter class
 */
public class Event<P extends EventParams> implements HasInstanceDescription {

    /**
     * the Modes that a listener can request it is added
     */
    public enum ListenerMode { 

        /**
         * fire on event with priority (ie before normal listeners), fire on the current thread
         */
        PRIORITY_IMMEDIATE, 

        /**
         * fire on event with normal priority, fire on the current thread
         */
        IMMEDIATE, 

        /**
         * fire on event with priority (ie before normal listeners), fire on the event queue thread
         */
        PRIORITY_EVENTQUEUE, 

        /**
         * fire on event with normal priority, fire on the event queue thread
         */
        EVENTQUEUE };
    
    private final ListenerStore<P> listenersImmediate = new ListenerStore<>();
    private final ListenerStore<P> listenersEventQueue = new ListenerStore<>();
    private final String description;

    /**
     * Constructor
     *
     * @param description the listening's descriptive name
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Event(String description) {
        this.description = description;
        LogBuilder.writeConstructorLog("nbpcglibrary.common", this, description);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, description);
    }

    /**
     * Add a listener - will fire action on EventQueue
     *
     * @param listener the listener
     */
    public void addListener(Listener<P> listener) {
        addListener(listener, ListenerMode.EVENTQUEUE);
    }

    /**
     * Add a listener - will fire action on calling thread or EventQueue
     * depending on parameter setting.
     *
     * @param listener the listener
     * @param mode mode indicating priority v. normal and immediate v event
     * queue
     */
    public void addListener(Listener<P> listener, ListenerMode mode) {
        LogBuilder.writeLog("nbpcglibrary.common",this, "addListener", listener, mode);
        if (listener != null) {
            switch (mode) {
                case PRIORITY_IMMEDIATE:
                    listenersImmediate.add(listener, true);
                    break;
                case PRIORITY_EVENTQUEUE:
                    listenersEventQueue.add(listener, true);
                    break;
                case IMMEDIATE:
                    listenersImmediate.add(listener, false);
                    break;
                case EVENTQUEUE:
                    listenersEventQueue.add(listener, false);
            }
        }
    }

    /**
     * Remove a listener
     *
     * @param listener the listener
     */
    public void removeListener(Listener<P> listener) {
        LogBuilder.writeLog("nbpcglibrary.common",this, "removeListener", listener);
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
        LogBuilder.writeLog("nbpcglibrary.common", this, "fire", p);
        listenersImmediate.fire(p);
        if (EventQueue.isDispatchThread()) {
            listenersEventQueue.fire(p);
        } else {
            listenersEventQueue.fireLaterOnEventQueue(p);
        }
    }

    private class ListenerStore<P extends EventParams> implements HasInstanceDescription {

        private final List<WeakReference<Listener<P>>> listeners = new ArrayList<>();

        @Override
        public String instanceDescription() {
            return LogBuilder.instanceDescription(this, description);
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
                LogBuilder.create("nbpcglibrary.common", Level.FINEST).addMethodName(this, "add", listener, priority)
                        .addMsg("failed to add listener - reason duplicate").write();
            } else {
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

    private static class FireEventQueueListener<P extends EventParams> implements Runnable {

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
    
    /**
     * Fire the event listener on the EventQueue (rather than this thread)
     * 
     * @param listener the listener to be fired
     */
    public static void fireSimpleEventParamsListener(Listener<SimpleEventParams> listener){
         EventQueue.invokeLater(new FireEventQueueListener<>(listener, new SimpleEventParams()));
    }
}
