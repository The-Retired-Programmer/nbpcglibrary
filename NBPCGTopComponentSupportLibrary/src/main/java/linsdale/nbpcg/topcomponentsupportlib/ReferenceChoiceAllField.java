/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.topcomponentsupportlib;

import java.util.ArrayList;
import java.util.List;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.datasupportlib.entity.FieldChangeListenerParams;
import linsdale.nbpcg.formsupportlib.FormFieldChangeListenerParams;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public abstract class ReferenceChoiceAllField<E extends EntityRO> extends ReferenceChoiceField<E> {

    private List<E> choices;
    private List<String> choiceText;
    private ChoicesFieldListener choicesfieldListener;

    /**
     * Constructor
     *
     * @param field
     * @param label the field label
     */
    public ReferenceChoiceAllField(IntWithDescription field, String label) {
        this(field, label, null);
    }

    public ReferenceChoiceAllField(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label, listener);
        choicesfieldListener = new ChoicesFieldListener(label + "/choices");
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

    protected abstract List<E> getChoicesEntities();

    protected abstract String convertEntitytoText(E e);

    @Override
    protected void addCollectionListeners() {
        // null action as set does not change
    }

    @Override
    protected void removeCollectionListeners() {
        // null action as set does not change
    }

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
