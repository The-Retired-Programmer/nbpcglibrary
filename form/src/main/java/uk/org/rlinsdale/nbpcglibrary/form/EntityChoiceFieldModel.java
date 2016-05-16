/*
 * Copyright (C) 2015-2016 Richard Linsdale.
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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.io.IOException;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.data.entity.SetChangeEventParams;

/**
 * The Choice Field Model - basic implementation
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the type of the source value
 */
public abstract class EntityChoiceFieldModel<T> extends EntityFieldModel<T> {

    /**
     * Add a listener to collection from which the choice list is derived.
     *
     * @param listener the listener
     * @throws IOException if problems
     */
    public void addCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException {
    }

    /**
     * Remove a listener from the collection from which the choice list is
     * derived.
     *
     * @param listener the listener
     * @throws IOException if problems
     */
    public void removeCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException {
    }
}
