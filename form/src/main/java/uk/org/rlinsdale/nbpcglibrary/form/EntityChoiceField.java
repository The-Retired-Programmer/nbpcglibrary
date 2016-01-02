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
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
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
public class EntityChoiceField<E extends Entity> extends ChoiceField<E> {

    private final ChoicesFieldListener choicesfieldListener;
    private final CollectionFieldListener collectionfieldListener;

   /**
     * Constructor
     *
     * @param source the data source for this field
     * @param nullSelectionAllowed true if a null selection item is to be added
     * to the choice items
     * @param initialValue the initial value of the display (or null if source
     * provides this
     * @param choices the choices to be displayed in the combobox
     * @param callback the callback with is used to inform of source updates
     * from field
     */
    public EntityChoiceField(ChoiceFieldSource<E> source, boolean nullSelectionAllowed, E initialValue, List<E> choices, Callback callback) {
        super( source, nullSelectionAllowed, initialValue, choices, callback);
        choicesfieldListener = new ChoicesFieldListener("entity/choices");
        collectionfieldListener = new CollectionFieldListener("entity/setchange");
    }

    @Override
    public void closeChoices() {
        try {
            source.removeCollectionListeners(collectionfieldListener);
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

    /**
     * hook to allow actions to take place before updating a combobox
     */
    @Override
    protected final void preFieldUpdateAction() {
        try {
            source.removeCollectionListeners(collectionfieldListener);
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
            source.removeCollectionListeners(collectionfieldListener);
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
