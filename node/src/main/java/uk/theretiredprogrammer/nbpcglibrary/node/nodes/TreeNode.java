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

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.function.Function;
import org.openide.util.datatransfer.ExTransferable;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestampBaseEntity;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import uk.theretiredprogrammer.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <R> the base entity class used in the rest transfer
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the Entity Field enum class
 */
public abstract class TreeNode<R extends IdTimestampBaseEntity, E extends Entity, P extends CoreEntity, F> extends BasicNode<E> {

    /**
     * Copy Allowed - for OperationsEnabled
     */
    public static final int CAN_COPY = 1;
    /**
     * Cut Allowed - for OperationsEnabled
     */
    public static final int CAN_CUT = 1;
    /**
     * Delete Allowed - for OperationsEnabled
     */
    public static final int CAN_DELETE = 4;
    ;
    private EntityReference<R, E, P> eref;
    private EntityStateChangeListener stateListener;
    private EntityFieldChangeListener fieldListener;
    private EntityNameChangeListener nameListener;
    private int operationsEnabled;

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param e the entity
     * @param cf the childfactory
     * @param restclass class of the rest client for this entity
     * @param allowedDataFlavors allowed paste actions
     * @param iconName the name of the icon (from the Icons set)
     * @param operationsEnabled set for copy , cut and delete enabled
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected TreeNode(Function<R,E> entitycreator, E e, BasicChildFactory<R, E, P> cf, Class<? extends Rest<R>> restclass, DataFlavor[] allowedDataFlavors, String iconName, int operationsEnabled) {
        super(cf, allowedDataFlavors, iconName);
        commonConstructor(e, entitycreator, restclass, operationsEnabled);
    }

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param e the entity
     * @param restclass class of the rest client for this entity
     * @param iconName the name of the icon (from the Icons set)
     * @param operationsEnabled set for copy , cut and delete enabled
     */
    protected TreeNode(Function<R,E> entitycreator, E e, Class<? extends Rest<R>> restclass, String iconName, int operationsEnabled) {
        super(iconName);
        commonConstructor(e, entitycreator, restclass, operationsEnabled);

    }

    private void commonConstructor(E e, Function<R,E> entitycreator, Class<? extends Rest<R>> restclass, int operationsEnabled) {
        eref = new EntityReference<>(entitycreator, restclass, e);
        this.operationsEnabled = operationsEnabled;
        e.addStateListener(stateListener = new EntityStateChangeListener());
        e.addFieldListener(fieldListener = new EntityFieldChangeListener());
        e.addNameListener(nameListener = new EntityNameChangeListener());
    }

    @Override
    public E getEntity() {
        return eref.get();
    }
    
    @Override
    public Image getIcon(int type) {
        return getEntity().checkRules(new StringBuilder()) ? super.getIcon(type) : getIconWithError();
    }

    /**
     * Set node/entity association to null.
     */
    public void setNoEntity() {
        eref.set();
    }

    private class EntityStateChangeListener extends Listener {

        @Override
        public void action(Object p) {
            switch ( ((EntityStateChangeEventParams)p).getTransition()) {
                case EDIT:
                    iconChange();
                    nameChange();
                    break;
                case SAVE:
                    iconChange();
                    nameChange();
                    break;
                case REMOVE:
                    setNoEntity();
                    break;
                case RESET:
                    nameChange();
            }
        }
    }

    @Override
    public String getHtmlDisplayName() {
        E entity = getEntity();
        return (entity.isNew() ? "<font color='#0000FF'><b>" : (entity.isEditing() ? "<b>" : "")) + getDisplayName();
    }
    
    /**
     * Get the key string which will be used in when sorting this entity
     *
     * @return the sort key
     */
    public abstract String getSortKey();

    /**
     * get the title string which will be used to display the fully in context
     * name for the entity
     *
     * @return the title string
     */
    public abstract String getDisplayTitle();

    private class EntityFieldChangeListener extends Listener {

        @Override
        public void action(Object p) {
            nodeProcessFieldChange((F) p);
            // TODO make decision about the Icon changes (based on changes to error state)
            iconChange(); // temporary - do always (just in case!)
        }
    }

    private class EntityNameChangeListener extends Listener {

        @Override
        public void action(Object p) {
            nameChange();
        }
    }

    /**
     * Fire the Property Change.
     *
     * @param name the property name
     */
    protected void propertyChange(String name) {
        firePropertyChange(name, "old", "new");
    }

    /**
     * Fire the property Change, for all properties.
     */
    protected void propertyChange() {
        firePropertyChange(null, "old", "new");
    }

    private void nameChange() {
        fireDisplayNameChange("old", "new");
    }

    private void iconChange() {
        fireIconChange();
        fireOpenedIconChange();
    }

    // CUT and PASTE source support
    @Override
    public final boolean canCopy() {
        return (operationsEnabled & CAN_COPY) != 0;
    }

    @Override
    public final boolean canCut() {
        return (operationsEnabled & CAN_CUT) != 0;
    }

    @Override
    public final boolean canDestroy() {
        return (operationsEnabled & CAN_DELETE) != 0;
    }

    @Override
    public final void destroy() throws IOException {
        nodeDelete();
    }

    @Override
    public final Transferable clipboardCut() throws IOException {
        ExTransferable added = ExTransferable.create(super.clipboardCut());
        added.put(new ExTransfer());
        return added;
    }

    @Override
    public final Transferable clipboardCopy() throws IOException {
        ExTransferable added = ExTransferable.create(super.clipboardCopy());
        added.put(new ExTransfer());
        return added;
    }

    private class ExTransfer extends ExTransferable.Single {

        public ExTransfer() {
            super(nodeGetDataFlavor());
        }

        @Override
        protected E getData() {
            return getEntity();
        }
    }

    /**
     * Delete - removal of Node action.
     *
     * @throws IOException if problem
     */
    abstract protected void nodeDelete() throws IOException;

    /**
     * Process field changes.
     *
     * @param field the field Id
     */
    protected abstract void nodeProcessFieldChange(F field);
}
