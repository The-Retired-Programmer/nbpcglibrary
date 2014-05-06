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
package uk.org.rlinsdale.nbpcg.topcomponentsupportlib;

import java.util.ArrayList;
import java.util.List;
import uk.org.rlinsdale.nbpcg.datasupportlib.entity.Entity;
import uk.org.rlinsdale.nbpcg.datasupportlib.entity.EntityRO;
import uk.org.rlinsdale.nbpcg.datasupportlib.entity.FieldChangeListenerParams;
import uk.org.rlinsdale.nbpcg.datasupportlib.entity.SetChangeListenerParams;
import uk.org.rlinsdale.nbpcg.formsupportlib.FormFieldChangeListenerParams;
import uk.org.rlinsdale.nbpcg.supportlib.IntWithDescription;
import uk.org.rlinsdale.nbpcg.supportlib.Listener;

/**
 * Choice Field - taking values from all entities in a parent entities
 * collection.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 * @param <R> the parent class
 */
public abstract class ReferenceChoiceFieldFromEntity<E extends EntityRO, R extends Entity> extends ReferenceChoiceField<E> {

    private R parententity;
    private List<E> choices;
    private List<String> choiceText;
    private ChoicesFieldListener choicesfieldListener;
    private CollectionFieldListener collectionfieldListener;
    private IntWithDescription collectionFieldId;

    /**
     * Constructor
     *
     * @param field the field Id
     * @param label the field label
     * @param parententity the parent entity
     * @param collectionFieldId the collection field Id
     */
    public ReferenceChoiceFieldFromEntity(IntWithDescription field, String label, R parententity, IntWithDescription collectionFieldId) {
        this(field, label, parententity, collectionFieldId, null);
    }

    /**
     * Constructor.
     *
     * @param field the field Id
     * @param label the field label
     * @param parententity the parent entity
     * @param collectionFieldId the collection field Id
     * @param listener the change listener
     */
    public ReferenceChoiceFieldFromEntity(IntWithDescription field, String label, R parententity, IntWithDescription collectionFieldId, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label, listener);
        choicesfieldListener = new ChoicesFieldListener(label + "/choices");
        collectionfieldListener = new CollectionFieldListener(label + "/collection");
        this.parententity = parententity;
        this.collectionFieldId = collectionFieldId;
    }

    @Override
    protected void addCollectionListeners() {
        parententity.addSetChangeListener(collectionfieldListener);
    }

    @Override
    protected void removeCollectionListeners() {
        parententity.removeSetChangeListener(collectionfieldListener);
    }

    @Override
    protected void addChoicesListeners() {
        choices.stream().filter((e) -> (e != null)).forEach((e) -> {
            e.addFieldListener(choicesfieldListener);
        });
    }

    @Override
    protected void removeChoicesListeners() {
        choices.stream().filter((e) -> (e != null)).forEach((e) -> {
            e.removeFieldListener(choicesfieldListener);
        });
    }

    @Override
    protected List<String> getChoicesText() {
        choiceText = new ArrayList<>();
        choices = getChoicesEntities();
        choices.stream().forEach((e) -> {
            choiceText.add(convertEntitytoText(e));
        });
        return choiceText;
    }

    /**
     * Get the parent entity.
     *
     * @return the parent entity
     */
    protected R getParentEntity() {
        return parententity;
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

    @Override
    public E findSelectedEntity() {
        String choicename = get();
        for (int i = 0; i < choiceText.size(); i++) {
            if (choiceText.get(i).equals(choicename)) {
                return choices.get(i);
            }
        }
        return null;
    }

    private class CollectionFieldListener extends Listener<SetChangeListenerParams> {

        public CollectionFieldListener(String name) {
            super(name);
        }

        @Override
        @SuppressWarnings("IncompatibleEquals")
        public void action(SetChangeListenerParams p) {
            if (p.equals(collectionFieldId)) {
                updateChoicesText();
            }
        }
    }

    private class ChoicesFieldListener extends Listener<FieldChangeListenerParams> {

        public ChoicesFieldListener(String name) {
            super(name);
        }

        @Override
        public void action(FieldChangeListenerParams p) {
            updateChoicesText();
        }
    }
}
