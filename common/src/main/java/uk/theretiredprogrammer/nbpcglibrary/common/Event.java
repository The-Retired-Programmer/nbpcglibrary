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
import java.awt.EventQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class to implement Listening. Allowing listeners to register and actions to
 * fire
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <P> the listener parameter class
 */
public class Event<P extends EventParams> {

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

    /**
     * Add a listener - will fire action on EventQueue
     *
     * @param listener the listener
     */
    public void addListener(Listener<P> listener) {
        listenersEventQueue.add(listener);
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
        if (listener != null) {
            switch (mode) {
                case PRIORITY_IMMEDIATE:
                    listenersImmediate.addPriority(listener);
                    break;
                case PRIORITY_EVENTQUEUE:
                    listenersEventQueue.addPriority(listener);
                    break;
                case IMMEDIATE:
                    listenersImmediate.add(listener);
                    break;
                case EVENTQUEUE:
                    listenersEventQueue.add(listener);
            }
        }
    }

    /**
     * Remove a listener
     *
     * @param listener the listener
     */
    public void removeListener(Listener<P> listener) {
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
     * Test if there are listeners registered with this listening.
     *
     * @return count of the registered listeners
     */
    public int listenerCount() {
        return listenersImmediate.size() + listenersEventQueue.size();
    }

    /**
     * Fire all listeners registered - event will fire immediately on the
     * calling thread or later on the EventQueue.
     *
     * @param p the listener parameters object
     */
    public void fire(P p) {
        listenersImmediate.fire(p);
        if (EventQueue.isDispatchThread()) {
            listenersEventQueue.fire(p);
        } else {
            listenersEventQueue.fireLaterOnEventQueue(p);
        }
    }

    private class ListenerStore<P extends EventParams> {

        private List<WeakReference<Listener<P>>> listeners = new ArrayList<>();
        
        public final synchronized void add(Listener<P> listener) {
            if (listeners.stream().noneMatch(wref -> wref.get() != null && wref.get() == listener)) {
                    listeners.add(new WeakReference<>(listener));
            }
        }
        
        public final synchronized void addPriority(Listener<P> listener) {
            if (listeners.stream().noneMatch(wref -> wref.get() != null && wref.get() == listener)) {
                    listeners.add(0, new WeakReference<>(listener));
            }
        }
        
        public final synchronized void remove(Listener<P> listener) {
            listeners = listeners.stream()
                    .filter(wref -> wref.get() != null && wref.get() != listener)
                    .collect(Collectors.toList());
        }

        public final synchronized void fire(P p) {
            listeners.stream()
                    .filter(wref -> wref.get()!= null)
                    .forEach( wref -> wref.get().actionPerformed(p));
        }

        public final synchronized void fireLaterOnEventQueue(P p) {
            listeners.stream()
                    .filter(wref -> wref.get()!= null)
                    .forEach( wref -> 
                        EventQueue.invokeLater(new FireEventQueueListener<>(wref.get(), p))
                    );
        }

        public final synchronized boolean hasListener() {
            return listeners.stream()
                    .anyMatch(wref -> wref.get()!= null);
        }

        public final synchronized int size() {
            return (int) listeners.stream()
                    .filter(wref -> wref.get()!= null).count();
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
