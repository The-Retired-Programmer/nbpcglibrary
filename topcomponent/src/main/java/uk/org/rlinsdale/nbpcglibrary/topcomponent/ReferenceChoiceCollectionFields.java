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
package uk.org.rlinsdale.nbpcglibrary.topcomponent;

import java.util.ArrayList;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.FieldChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.SetChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.form.ChoiceField;
import uk.org.rlinsdale.nbpcglibrary.form.FormFieldChangeEventParams;

/**
 * Choice Field - taking values from all entities of a class.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 * @param <F> the entity fields enum class
 */
public abstract class ReferenceChoiceCollectionFields<E extends EntityRO, F> extends ChoiceField {

    private List<E> choices;
    private List<String> choiceText;
    private final ChoicesFieldListener choicesfieldListener;
    private final CollectionFieldListener collectionfieldListener ;

    /**
     * Constructor.
     *
     * @param field the field Id
     * @param label the field label
     * @param listener the change listener
     */
    public ReferenceChoiceCollectionFields(IntWithDescription field, String label, Listener<FormFieldChangeEventParams> listener) {
        super(field, label, listener);
        choicesfieldListener = new ChoicesFieldListener(label + "/choices");
        collectionfieldListener = new CollectionFieldListener(label+"/setchange");
    }
    
    /**
     * initialise the choices text
     */
    public void initChoices() {
        setChoices(getChoicesText());
        addChoicesListeners();
        addCollectionListeners(collectionfieldListener);
    }

    /**
     * finish managing the choices text
     */
    public void closeChoices() {
        removeCollectionListeners(collectionfieldListener);
        removeChoicesListeners();
    }


    private void addChoicesListeners() {
        choices.stream().filter((e) -> (e != null)).forEach((e) -> {
            e.addFieldListener(choicesfieldListener);
        });
    }

    private void removeChoicesListeners() {
        choices.stream().filter((e) -> (e != null)).forEach((e) -> {
            e.removeFieldListener(choicesfieldListener);
        });
    }

    private List<String> getChoicesText() {
        choiceText = new ArrayList<>();
        choices = getChoicesEntities();
        choices.stream().forEach((e) -> {
            choiceText.add(convertEntitytoText(e));
        });
        return choiceText;
    }

    /**
     * Get the set of entities.
     *
     * @return the set of entities
     */
    protected abstract List<E> getChoicesEntities();

    /**
     * Get the Choice text from an entity.
     *
     * @param e the entity
     * @return the choice text
     */
    protected abstract String convertEntitytoText(E e);

    
    /**
     * add a given listener to all parent collections which could affect this reference choice.
     * @param listener the set change listener
     */
    protected abstract void addCollectionListeners(Listener<SetChangeEventParams> listener);

    
    /**
     * remove a given listener from all parent collections which could affect this reference choice.
     * @param listener the set change listener
     */
    protected abstract void removeCollectionListeners(Listener<SetChangeEventParams> listener);

    public E findSelectedEntity() {
        String choicename = get();
        for (int i = 0; i < choiceText.size(); i++) {
            if (choiceText.get(i).equals(choicename)) {
                return choices.get(i);
            }
        }
        return null;
    }

    /**
     * Update the choices text
     */
    protected void updateChoicesText() {
        removeCollectionListeners(collectionfieldListener);
        removeChoicesListeners();
        setChoices(getChoicesText());
        addChoicesListeners();
        addCollectionListeners(collectionfieldListener);
    }
    
    private class CollectionFieldListener extends Listener<SetChangeEventParams> {

        public CollectionFieldListener(String name) {
            super(name);
        }

        @Override
        public void action(SetChangeEventParams p) {
                updateChoicesText();
        }
    }


    private class ChoicesFieldListener extends Listener<FieldChangeEventParams<F>> {

        public ChoicesFieldListener(String name) {
            super(name);
        }

        @Override
        public void action(FieldChangeEventParams<F> p) {
            updateChoicesText();
        }
    }
}
