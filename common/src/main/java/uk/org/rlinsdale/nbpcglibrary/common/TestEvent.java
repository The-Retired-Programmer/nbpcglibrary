/*
 * Copyright (C) 2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A class to implement Testing. Allowing tests to register and be called.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TestEvent implements HasInstanceDescription {

    private final List<WeakReference<Test>> tests = new ArrayList<>();
    private final String description;

    /**
     * Constructor
     *
     * @param description the testevent descriptive name
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public TestEvent(String description) {
        this.description = description;
        LogBuilder.writeConstructorLog("nbpcglibrary.common", this, description);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, description);
    }

    /**
     * Add a test
     *
     * @param add the test
     */
    public void add(Test add) {
        LogBuilder.writeLog("nbpcglibrary.common", this, "addTest", add);
        removeEmptyReferences();
        for (WeakReference<Test> weaktest : tests) {
            Test test = weaktest.get();
            if (test != null) {
                if (test == add) {
                    LogBuilder.create("nbpcglibrary.common", Level.FINEST).addMethodName(this, "add", add)
                            .addMsg("failed to add test - reason duplicate").write();
                    return;
                }
            }
        }
        tests.add(new WeakReference<>(add));
    }

    /**
     * Remove a test
     *
     * @param remove the test
     */
    public void remove(Test remove) {
        LogBuilder.writeLog("nbpcglibrary.common", this, "removeTest", remove);
        removeEmptyReferences();
        int idx = 0;
        for (WeakReference<Test> weaktest : tests) {
            Test test = weaktest.get();
            if (test != null) {
                if (test == remove) {
                    tests.remove(idx);
                    return;
                }
            }
            idx++;
        }
    }

    /**
     * Execute all test and return true if and only if all tests return true.
     *
     * @param sb a stringbuilder object to be used to append any failure
     * messages from the test
     * @return true if and only if all tests return true
     */
    public boolean test(StringBuilder sb) {
        LogBuilder.writeLog("nbpcglibrary.common", this, "test");
        removeEmptyReferences();
        boolean res = true;
        for (WeakReference<Test> weaktest : tests) {
            Test test = weaktest.get();
            if (test != null) {
                if (!test.test(sb)) {
                    res = false;
                }
            }
        }
        return res;
    }

    private void removeEmptyReferences() {
        while (removeEmptyReference()) {
        }
    }

    private boolean removeEmptyReference() {
        int idx = 0;
        for (WeakReference<Test> weaktest : tests) {
            Test test = weaktest.get();
            if (test == null) {
                tests.remove(idx);
                return true;
            }
            idx++;
        }
        return false;
    }
}
