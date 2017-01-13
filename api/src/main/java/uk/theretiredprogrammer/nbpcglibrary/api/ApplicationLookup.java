/*
 * Copyright (C) 2015 Wade Chandler.
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
package uk.theretiredprogrammer.nbpcglibrary.api;

import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Class used to house anything one might want to store in a central lookup
 * which can affect anything within the application. It can be thought of as a
 * central context where any application data may be stored and watched.
 *
 * A singleton instance is created using @see getDefault(). This class is as
 * thread safe as Lookup. Lookup appears to be safe.
 *
 * @author Wade Chandler
 */
public class ApplicationLookup extends AbstractLookup {

    private InstanceContent content = null;
    private static ApplicationLookup def = new ApplicationLookup();

    private ApplicationLookup() {
        this(new InstanceContent());
    }

    private ApplicationLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static ApplicationLookup getDefault() {
        return def;
    }
}
