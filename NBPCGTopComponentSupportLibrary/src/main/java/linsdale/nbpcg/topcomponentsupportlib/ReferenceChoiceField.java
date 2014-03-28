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

import java.util.List;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.formsupportlib.ChoiceField;
import linsdale.nbpcg.formsupportlib.FormFieldChangeListenerParams;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public abstract class ReferenceChoiceField<E extends EntityRO> extends ChoiceField {

    /**
     * Constructor
     *
     * @param field
     * @param label the field label
     */
    public ReferenceChoiceField(IntWithDescription field, String label) {
        super(field, label, null);
    }

    public ReferenceChoiceField(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label, listener);
    }

    public void initChoices() {
        setChoices(getChoicesText());
        addChoicesListeners();
        addCollectionListeners();
    }

    public void closeChoices() {
        removeCollectionListeners();
        removeChoicesListeners();
    }

    protected void updateChoicesText() {
        removeChoicesListeners();
        setChoices(getChoicesText());
        addChoicesListeners();
    }

    protected abstract void addCollectionListeners();

    protected abstract void removeCollectionListeners();

    protected abstract void addChoicesListeners();

    protected abstract void removeChoicesListeners();

    protected abstract List<String> getChoicesText();

    public abstract E findSelectedEntity();
}