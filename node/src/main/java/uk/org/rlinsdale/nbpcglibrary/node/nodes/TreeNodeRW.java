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
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRW;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRW;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import org.openide.util.datatransfer.ExTransferable;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField;

/**
 * Read-Only Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 * @param <F> the Entity field enum class
 */
public abstract class TreeNodeRW<E extends EntityRW, F> extends TreeNodeRO<E> {

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
    protected TreeNodeRW(String nodename, E e, BasicChildFactory<E> cf, Class<? extends EntityManagerRW> emclass, DataFlavorAndAction[] allowedPaste, boolean isCutDestroyEnabled) {
        super(nodename, e, cf, emclass, allowedPaste);
        commonConstructor(nodename, e, isCutDestroyEnabled);
    }

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param e the entity
     * @param emclass the entity manager class
     * @param isCutDestroyEnabled true if delete/cut is allowed
     */
    protected TreeNodeRW(String nodename, E e, Class<? extends EntityManagerRW> emclass, boolean isCutDestroyEnabled) {
        super(nodename, e, emclass);
        commonConstructor(nodename, e, isCutDestroyEnabled);
    }

    private void commonConstructor(String nodename, E e, boolean isCutDestroyEnabled) {
        this.isCutDestroyEnabled = isCutDestroyEnabled;
        String desc = e.instanceDescription();
        e.addStateListener(stateListener = new EntityStateChangeListener(desc));
        e.addFieldListener(fieldListener = new EntityFieldChangeListener(desc));
        e.addNameListener(nameListener = new EntityNameChangeListener(desc));
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
            CommonEntityField c = p.getCommon();
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
    protected abstract void _processCommonFieldChange(CommonEntityField field);

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
     * @throws java.io.IOException
     */
    abstract protected void _cutAndPasteRemove()  throws IOException;

    /**
     * Delete - removal of Node action.
     * @throws java.io.IOException
     */
    abstract protected void _deleteRemove() throws IOException;
}
