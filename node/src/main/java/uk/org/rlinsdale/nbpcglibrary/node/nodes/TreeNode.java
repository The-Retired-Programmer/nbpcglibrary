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

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.logging.Level;
import org.openide.util.datatransfer.ExTransferable;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManager;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Read-Only Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the Entity Field enum class
 */
public abstract class TreeNode<E extends Entity, P extends CoreEntity, F> extends BasicNode<E> {

    private EntityReference<E, P> eref;
    private EntityStateChangeListener stateListener;
    private EntityFieldChangeListener fieldListener;
    private EntityNameChangeListener nameListener;
    private boolean isCutDestroyEnabled;

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param e the entity
     * @param cf the childfactory
     * @param emclass the entity manager class
     * @param allowedPaste allowed paste actions
     * @param isCutDestroyEnabled true if delete/cut is allowed
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected TreeNode(String nodename, E e, BasicChildFactory<E, P> cf, Class<? extends EntityManager> emclass, DataFlavorAndAction[] allowedPaste, boolean isCutDestroyEnabled) {
        super(cf, allowedPaste);
        commonConstructor(nodename, e, emclass, isCutDestroyEnabled);
    }

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param e the entity
     * @param emclass the entity manager class
     * @param isCutDestroyEnabled true if delete/cut is allowed
     */
    protected TreeNode(String nodename, E e, Class<? extends EntityManager> emclass, boolean isCutDestroyEnabled) {
        super();
        commonConstructor(nodename, e, emclass, isCutDestroyEnabled);
        
    }
    
     private void commonConstructor(String nodename, E e, Class<? extends EntityManager> emclass, boolean isCutDestroyEnabled) {
         try {
            eref = new EntityReference<>(nodename, e, emclass);
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addConstructorName(this, nodename, e)
                    .addExceptionMessage(ex).write();
        }
        this.isCutDestroyEnabled = isCutDestroyEnabled;
        String desc = e.instanceDescription();
        e.addStateListener(stateListener = new EntityStateChangeListener(desc));
        e.addFieldListener(fieldListener = new EntityFieldChangeListener(desc));
        e.addNameListener(nameListener = new EntityNameChangeListener(desc));
    }

    @Override
    public E getEntity() {
        try {
            return eref.get();
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addMethodName(this, "getEntity")
                    .addExceptionMessage(ex).write();
            return null;
        }
    }

    /**
     *
     */
    public void setNoEntity() {
        try {
            eref.set();
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addMethodName(this, "setNoEntity")
                    .addExceptionMessage(ex).write();
        }
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
            F f = p.get();
            if (f != null) {
                _processFieldChange(f);
            }
            EntityFieldChangeEventParams.CommonEntityField c = p.getCommon();
            if (c != null) {
                _processCommonFieldChange(c);
            }
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
    public final boolean canCut() {
        return isCutDestroyEnabled;
    }

    @Override
    public final boolean canDestroy() {
        return isCutDestroyEnabled;
    }

    @Override
    public final void destroy() throws IOException {
        LogBuilder.writeLog("nbpcglibrary.node", this, "destroy");
        _deleteRemove();
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
            super(_getDataFlavor());
        }

        @Override
        protected E getData() {
            LogBuilder.writeLog("nbpcglibrary.node", this, "getData");
            return getEntity();
        }
    }

    /**
     * Cut and Paste - removal of Node action.
     *
     * @throws java.io.IOException
     */
    abstract protected void _cutAndPasteRemove() throws IOException;

    /**
     * Delete - removal of Node action.
     *
     * @throws java.io.IOException
     */
    abstract protected void _deleteRemove() throws IOException;

    /**
     * Process field changes.
     *
     * @param field the field Id
     */
    protected abstract void _processFieldChange(F field);

    /**
     * Process field changes.
     *
     * @param field the field Id
     */
    protected abstract void _processCommonFieldChange(EntityFieldChangeEventParams.CommonEntityField field);

    /**
     * Get the display title for this node.
     *
     * @return the title
     */
    public abstract String getDisplayTitle();

}
