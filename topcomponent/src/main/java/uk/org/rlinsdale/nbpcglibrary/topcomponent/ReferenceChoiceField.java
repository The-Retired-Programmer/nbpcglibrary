/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcglibrary.topcomponent;

import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRO;
import uk.org.rlinsdale.nbpcglibrary.form.ChoiceField;
import uk.org.rlinsdale.nbpcglibrary.form.FormFieldChangeListenerParams;
import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;

/**
 * Abstract Choice Field - taking values from entity set.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 */
public abstract class ReferenceChoiceField<E extends EntityRO> extends ChoiceField {

    /**
     * Constructor
     *
     * @param field The field Id
     * @param label the field label
     */
    public ReferenceChoiceField(IntWithDescription field, String label) {
        super(field, label, null);
    }

    /**
     * Constructor.
     *
     * @param field The field Id
     * @param label the field label
     * @param listener the change listener
     */
    public ReferenceChoiceField(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label, listener);
    }

    /**
     * initialise the choices text
     */
    public void initChoices() {
        setChoices(getChoicesText());
        addChoicesListeners();
        addCollectionListeners();
    }

    /**
     * finish managing the choices text
     */
    public void closeChoices() {
        removeCollectionListeners();
        removeChoicesListeners();
    }

    /**
     * Update the choices text
     */
    protected void updateChoicesText() {
        removeCollectionListeners();
        removeChoicesListeners();
        setChoices(getChoicesText());
        addChoicesListeners();
        addCollectionListeners();
    }

    /**
     * Add collection listeners
     */
    protected abstract void addCollectionListeners();

    /**
     * Remove collection listeners
     */
    protected abstract void removeCollectionListeners();

    /**
     * Add choices listeners
     */
    protected abstract void addChoicesListeners();

    /**
     * Remove choices listeners
     */
    protected abstract void removeChoicesListeners();

    /**
     * Get Choices Text.
     *
     * @return the set of choices
     */
    protected abstract List<String> getChoicesText();

    /**
     * Get the selected entity, based on the choice.
     *
     * @return the selected entity
     */
    public abstract E findSelectedEntity();
}
