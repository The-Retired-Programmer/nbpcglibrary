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
package uk.theretiredprogrammer.nbpcglibrary.node.nodes;

import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.SetChangeEventParams;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import org.openide.nodes.ChildFactory;
import uk.theretiredprogrammer.nbpcglibrary.api.ApplicationLookup;
import uk.theretiredprogrammer.nbpcglibrary.api.InhibitExplorerRefresh;

/**
 * Core ChildFactory support
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the Parent CoreEntity Class
 */
public abstract class CoreChildFactory<E extends CoreEntity> extends ChildFactory<CoreEntity> {

    private final E parentEntity;

    /**
     * Constructor
     *
     * @param parentEntity the parent entity
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public CoreChildFactory(E parentEntity) {
        this.parentEntity = parentEntity;
    }

    /**
     * Get the parent entity.
     *
     * @return the parent entity
     */
    public E getParentEntity() {
        return parentEntity;
    }

    /**
     * get Change listener for the child entity set.
     *
     * @param name the name of the listener (for reporting/logging)
     * @return the required listener
     */
    public Listener<SetChangeEventParams> getSetChangeListener(String name) {
        return new ChildSetChangeListener(name);
    }

    private class ChildSetChangeListener extends Listener<SetChangeEventParams> {

        public ChildSetChangeListener(String name) {
            super(name);
        }

        @Override
        public void action(SetChangeEventParams p) {
            if (ApplicationLookup.getDefault().lookup(InhibitExplorerRefresh.class) == null) {
                refresh(true);
            }
        }
    }
}
