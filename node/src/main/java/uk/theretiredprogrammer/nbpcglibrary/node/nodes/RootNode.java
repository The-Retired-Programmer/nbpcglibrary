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

import java.awt.datatransfer.DataFlavor;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * Root Node Abstract Class
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the Entity Class
 */
public abstract class RootNode<E extends CoreEntity> extends BasicNode<E> {

    private final E e;

    /**
     * Constructor
     *
     * @param e the entity
     * @param cf the childfactory
     * @param allowedDataFlavors allowed paste actions
     */
    protected RootNode(E e, RootChildFactory<E> cf, DataFlavor[] allowedDataFlavors) {
        super(cf, allowedDataFlavors);
        this.e = e;
    }

    /**
     * Constructor.
     *
     * @param e the entity
     */
    protected RootNode(E e) {
        super();
        this.e = e;
    }

    @Override
    public E getEntity() {
        return e;
    }
}
