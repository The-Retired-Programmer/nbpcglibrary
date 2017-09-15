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

import org.openide.util.Lookup;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Extended ChildFactory support
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the Parent Entity Class
 * @param <P> The Parent of Parent Entity Class
 */
public abstract class BasicChildFactory<E extends Entity, P extends CoreEntity> extends CoreChildFactory<E> {

    private final EntityReference<E, P> parentref;

    /**
     * Constructor.
     *
     * @param factoryname the factory name
     * @param parentEntity the parent entity
     * @param emclass the parent entity manager class
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public BasicChildFactory(String factoryname, E parentEntity, Class<? extends EntityManager> emclass) {
        super(null);
        EntityManager<E, P> em = Lookup.getDefault().lookup(emclass);
        parentref = new EntityReference<>(factoryname + ">" + parentEntity.instanceDescription(), parentEntity, em);
    }

    @Override
    public E getParentEntity() {
        return parentref.get();
    }
}
