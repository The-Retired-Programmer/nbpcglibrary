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

import java.util.function.Function;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestampBaseEntity;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Extended ChildFactory support
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <R> the base entity class used in the rest transfer
 * @param <E> the Parent Entity Class
 * @param <P> The Parent of Parent Entity Class
 */
public abstract class BasicChildFactory<R extends IdTimestampBaseEntity, E extends Entity, P extends CoreEntity> extends CoreChildFactory<E> {

    private final EntityReference<R, E, P> parentref;

    /**
     * Constructor.
     *
     * @param parentEntity the parent entity
     * @param entitycreator a creator function for the Entity
     * @param restclass class of the rest client for this entity
     */
    public BasicChildFactory(E parentEntity, Function<R,E> entitycreator, Class<? extends Rest<R>> restclass) {
        super(null);
        parentref = new EntityReference<>(entitycreator, restclass, parentEntity);
    }

    @Override
    public E getParentEntity() {
        return parentref.get();
    }
}
