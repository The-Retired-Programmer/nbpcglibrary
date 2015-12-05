/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.SetChangeEventParams;

/**
 * Choice Field - taking values from all entities of a class.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 */
public abstract class EntityChoiceField<E extends Entity> extends ChoiceField<E> {

    private final ChoicesFieldListener choicesfieldListener;
    private final CollectionFieldListener collectionfieldListener;

    /**
     * Constructor.
     *
     * @param label the field label
     * @param nullSelectionAllowed true if a null selection is allowed
     */
    public EntityChoiceField(String label, boolean nullSelectionAllowed) {
        super(label, nullSelectionAllowed);
        choicesfieldListener = new ChoicesFieldListener(label + "/choices");
        collectionfieldListener = new CollectionFieldListener(label + "/setchange");
    }

    /**
     * finish managing the choices text
     */
    public void closeChoices() {
        try {
            removeCollectionListeners(collectionfieldListener);
        } catch (IOException ex) {
            LogBuilder.writeExceptionLog("nbpcglibrary.form", ex, this, "closeChoices");
        }
        removeChoicesListeners();
    }

    private void addChoicesListeners() {
        getSourceChoices().stream().filter((e) -> (e != null)).forEach((e) -> {
            e.addFieldListener(choicesfieldListener);
        });
    }

    private void removeChoicesListeners() {
        getSourceChoices().stream().filter((e) -> (e != null)).forEach((e) -> {
            e.removeFieldListener(choicesfieldListener);
        });
    }

//    /**
//     * Get the set of entities.
//     *
//     * @return the set of entities
//     * @throws IOException if problems
//     */
//    protected abstract List<E> getChoicesEntities() throws IOException;


    /**
     * add a given listener to all parent collections which could affect this
     * reference choice.
     *
     * @param listener the set change listener
     * @throws IOException if problems
     */
    protected abstract void addCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException;

    /**
     * remove a given listener from all parent collections which could affect
     * this reference choice.
     *
     * @param listener the set change listener
     * @throws IOException if problems
     */
    protected abstract void removeCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException;

    /**
     * hook to allow actions to take place before updating a combobox
     */
    @Override
    protected final void preFieldUpdateAction() {
        try {
            removeCollectionListeners(collectionfieldListener);
        } catch (IOException ex) {
            LogBuilder.writeExceptionLog("nbpcglibrary.form", ex, this, "preFieldUpdateAction");
        }
        removeChoicesListeners();
    }
    
    /**
     * hook to allow actions to take place after updating a combobox
     */
    @Override
    protected final void postFieldUpdateAction() {
        addChoicesListeners();
        try {
            addCollectionListeners(collectionfieldListener);
        } catch (IOException ex) {
            LogBuilder.writeExceptionLog("nbpcglibrary.form", ex, this, "postFieldUpdateAction");
        }
    }
    
    private class CollectionFieldListener extends Listener<SetChangeEventParams> {

        public CollectionFieldListener(String name) {
            super(name);
        }

        @Override
        public void action(SetChangeEventParams p) {
            updateChoicesFromSource();
        }
    }

    private class ChoicesFieldListener extends Listener<EntityFieldChangeEventParams> {

        public ChoicesFieldListener(String name) {
            super(name);
        }

        @Override
        public void action(EntityFieldChangeEventParams p) {
            updateChoicesFromSource();
        }
    }
}
