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
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.common.SimpleEventParams;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import uk.theretiredprogrammer.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the Entity Field enum class
 */
public abstract class TreeNode<E extends Entity, P extends CoreEntity, F> extends BasicNode<E> {

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
    private EntityReference<E, P> eref;
    private EntityStateChangeListener stateListener;
    private EntityFieldChangeListener fieldListener;
    private EntityNameChangeListener nameListener;
    private int operationsEnabled;

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param e the entity
     * @param cf the childfactory
     * @param emclass the entity manager class
     * @param allowedDataFlavors allowed paste actions
     * @param operationsEnabled set for copy , cut and delete enabled
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected TreeNode(String nodename, E e, BasicChildFactory<E, P> cf, Class<? extends EntityManager> emclass, DataFlavor[] allowedDataFlavors, int operationsEnabled) {
        super(cf, allowedDataFlavors);
        commonConstructor(nodename, e, emclass, operationsEnabled);
    }

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param e the entity
     * @param emclass the entity manager class
     * @param operationsEnabled set for copy , cut and delete enabled
     */
    protected TreeNode(String nodename, E e, Class<? extends EntityManager> emclass, int operationsEnabled) {
        super();
        commonConstructor(nodename, e, emclass, operationsEnabled);

    }

    private void commonConstructor(String nodename, E e, Class<? extends EntityManager> emclass, int operationsEnabled) {
        EntityManager<E, P> em = Lookup.getDefault().lookup(emclass);
        eref = new EntityReference<>(nodename, e, em);
        this.operationsEnabled = operationsEnabled;
        String desc = e.instanceDescription();
        e.addStateListener(stateListener = new EntityStateChangeListener(desc));
        e.addFieldListener(fieldListener = new EntityFieldChangeListener(desc));
        e.addNameListener(nameListener = new EntityNameChangeListener(desc));
    }

    @Override
    public E getEntity() {
        return eref.get();
    }

    /**
     * Set node/entity association to null.
     */
    public void setNoEntity() {
        eref.set();
    }

    private class EntityStateChangeListener extends Listener<EntityStateChangeEventParams> {

        public EntityStateChangeListener(String name) {
            super(name);
        }

        @Override
        public void action(EntityStateChangeEventParams p) {
            switch (p.getTransition()) {
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

    private class EntityFieldChangeListener extends Listener<EntityFieldChangeEventParams<F>> {

        public EntityFieldChangeListener(String name) {
            super(name);
        }

        @Override
        public void action(EntityFieldChangeEventParams<F> p) {
            nodeProcessFieldChange(p.get());
            // TODO make decision about the Icon changes (based on changes to error state)
            iconChange(); // temporary - do always (just in case!)
        }
    }

    private class EntityNameChangeListener extends Listener<SimpleEventParams> {

        public EntityNameChangeListener(String name) {
            super(name);
        }

        @Override
        public void action(SimpleEventParams p) {
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
        LogBuilder.writeLog("nbpcglibrary.node", this, "destroy");
        nodeDelete();
    }

    @Override
    public final Transferable clipboardCut() throws IOException {
        LogBuilder.writeLog("nbpcglibrary.node", this, "clipboardCut");
        ExTransferable added = ExTransferable.create(super.clipboardCut());
        added.put(new ExTransfer());
        return added;
    }

    @Override
    public final Transferable clipboardCopy() throws IOException {
        LogBuilder.writeLog("nbpcglibrary.node", this, "clipboardCopy");
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
            LogBuilder.writeLog("nbpcglibrary.node", this, "getData");
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

    /**
     * Get the display title for this node.
     *
     * @return the title
     */
    public abstract String getDisplayTitle();

}
