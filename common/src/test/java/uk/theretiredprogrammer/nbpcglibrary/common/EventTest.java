/*
 * Copyright 2017 richard linsdale.
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

import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.theretiredprogrammer.nbpcglibrary.common.Event.ListenerMode;

/**
 * Tests for Event Class
 * 
 * @author richard linsdale (richard at theretiredprogrammer.uk)
 */
public class EventTest {

    public EventTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public class TestListener extends Listener<SimpleEventParams> {

        private final String description;

        public TestListener(String description) {
            super(description);
            this.description = description;
        }

        @Override
        public void action(SimpleEventParams p) {
            actionCount++;
            firedFromList.add(description);

        }
    }

    public int actionCount;
    public List<String> firedFromList;

    /**
     * Test of fire Listener method, of class Event.
     */
    @Test
    public void testFireListener() {
        System.out.println("afire Listener");
        TestListener i = new TestListener("immediate");
        TestListener ip = new TestListener("priority-immediate");
        TestListener e = new TestListener("eventqueue");
        TestListener ep = new TestListener("priority-eventqueue");
        Event event = new Event();
        event.addListener(i, ListenerMode.IMMEDIATE);
        event.addListener(ip, ListenerMode.PRIORITY_IMMEDIATE);
        event.addListener(ep, ListenerMode.PRIORITY_EVENTQUEUE);
        event.addListener(e, ListenerMode.EVENTQUEUE);
        actionCount = 0;
        firedFromList = new LinkedList<>();
        event.fire(new SimpleEventParams());
        assertEquals(2, actionCount);
        assertEquals(2, firedFromList.size());
        assertEquals("priority-immediate", firedFromList.get(0));
        assertEquals("immediate", firedFromList.get(1));
        // pause to allow the eventqueue to fire
        try {
            sleep(1000);  // milliseconds
        } catch (InterruptedException ex) {
        }
        //
        assertEquals(4, actionCount);
        assertEquals(4, firedFromList.size());
        assertEquals("priority-immediate", firedFromList.get(0));
        assertEquals("immediate", firedFromList.get(1));
        assertEquals("priority-eventqueue", firedFromList.get(2));
        assertEquals("eventqueue", firedFromList.get(3));
    }

    /**
     * Test of add/remove Listener method, of class Event.
     */
    @Test
    public void testAddRemoveListener() {
        System.out.println("add/remove Listener");
        TestListener l = new TestListener("testlistener");
        //
        Event event = new Event();
        int c = event.listenerCount();
        assertEquals(0, c);
        assert (!event.hasListener());
        //
        event.addListener(l);
        c = event.listenerCount();
        assertEquals(1, c);
        assert (event.hasListener());
        //
        event.addListener(l);
        c = event.listenerCount();
        assertEquals(1, c);
        assert (event.hasListener());
        //
        event.removeListener(l);
        c = event.listenerCount();
        assertEquals(0, c);
        assert (!event.hasListener());
        //
        TestListener l2 = new TestListener("testlistener2");
        event.addListener(l);
        event.addListener(l2);
        c = event.listenerCount();
        assertEquals(2, c);
        assert (event.hasListener());
        //
        l2 = null;
        c = event.listenerCount();
        assertEquals(2, c);
        assert (event.hasListener());
        //
        System.gc();
        c = event.listenerCount();
        assertEquals(1, c);
        assert (event.hasListener());
        //
        event.removeListener(l);
        c = event.listenerCount();
        assertEquals(0, c);
        assert (!event.hasListener());
        //
    }
}
