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
package uk.org.rlinsdale.nbpcglibrary.node.nodes;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManager;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the Primary Key class for entity
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the Entity Field enum class
 */
public abstract class TreeNode<K, E extends Entity<K, E, P, F>, P extends CoreEntity, F> extends BasicNode<E> {

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
    private EntityReference<K, E, P> eref;
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
    protected TreeNode(String nodename, E e, BasicChildFactory<K, E, P> cf, Class<? extends EntityManager> emclass, DataFlavor[] allowedDataFlavors, int operationsEnabled) {
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
        EntityManager<K, E, P> em = Lookup.getDefault().lookup(emclass);
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
