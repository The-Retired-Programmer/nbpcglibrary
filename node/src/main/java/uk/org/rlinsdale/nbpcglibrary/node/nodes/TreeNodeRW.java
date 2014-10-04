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
package uk.org.rlinsdale.nbpcglibrary.node.nodes;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRW;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRW;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeListenerParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.FieldChangeListenerParams;
import uk.org.rlinsdale.nbpcglibrary.node.SaveHandler;
import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Listening;
import uk.org.rlinsdale.nbpcglibrary.common.Log;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.util.datatransfer.ExTransferable;

/**
 * Read-Only Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 */
public abstract class TreeNodeRW<E extends EntityRW> extends TreeNodeRO<E> {

    private NodeSavable<E> nodeSavable;
    private SaveHandler saveHandler;
    private EntityStateChangeListener stateListener;
    private EntityFieldChangeListener fieldListener;
    private Listening<NameChangeListenerParams> titleListening;
    private Listening<NameChangeListenerParams> nameListening;
    private boolean isCutDestroyEnabled;

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param iconname the iconname
     * @param e the entity
     * @param cf the childfactory
     * @param emclass the entity manager class
     * @param allowedPaste allowed paste actions
     * @param isCutDestroyEnabled true if delete/cut is allowed
     */
    protected TreeNodeRW(String nodename, String iconname, E e, BasicChildFactory<E> cf, Class<? extends EntityManagerRW> emclass, DataFlavorAndAction[] allowedPaste, boolean isCutDestroyEnabled) {
        super(nodename, iconname, e, cf, emclass, allowedPaste);
        commonConstructor(nodename, e, isCutDestroyEnabled);
    }

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param iconname the iconname
     * @param e the entity
     * @param emclass the entity manager class
     * @param isCutDestroyEnabled true if delete/cut is allowed
     */
    protected TreeNodeRW(String nodename, String iconname, E e, Class<? extends EntityManagerRW> emclass, boolean isCutDestroyEnabled) {
        super(nodename, iconname, e, emclass);
        commonConstructor(nodename, e, isCutDestroyEnabled);
    }

    private void commonConstructor(String nodename, E e, boolean isCutDestroyEnabled) {
        this.isCutDestroyEnabled = isCutDestroyEnabled;
        nameListening = new Listening<>(nodename + "/name");
        titleListening = new Listening<>(nodename + "/title");
        nodeSavable = new NodeSavable<>(nodename);
        saveHandler = nodeSavable.getDefaultSaveHandler();
        e.addStateListener(stateListener = new EntityStateChangeListener(nodename + "/state"));
        e.addFieldListener(fieldListener = new EntityFieldChangeListener(nodename + "/field"));
        if (e.isEditing()) {
            nodeSavable.enable(e);
        }
    }

    /**
     * Add a Name listener.
     *
     * @param listener the listener
     */
    public final void addNameListener(Listener<NameChangeListenerParams> listener) {
        nameListening.addListener(listener);
    }

    /**
     * Remove a Name listener.
     *
     * @param listener the listener
     */
    public final void removeNameListener(Listener<NameChangeListenerParams> listener) {
        nameListening.removeListener(listener);
    }

    final void nameListenerFire() {
        nameListening.fire(new NameChangeListenerParams(getDisplayName()));
    }

    /**
     * Add a Title listener.
     *
     * @param listener the listener
     */
    public final void addTitleListener(Listener<NameChangeListenerParams> listener) {
        titleListening.addListener(listener);
    }

    /**
     * Remove a Title listener.
     *
     * @param listener the listener
     */
    public final void removeTitleListener(Listener<NameChangeListenerParams> listener) {
        titleListening.removeListener(listener);
    }

    final void titleListenerFire() {
        titleListening.fire(new NameChangeListenerParams(getDisplayTitle()));
    }

    /**
     * Set a save handler for this node. If the save Handler is null then the
     * default savehandler will be used (which does an entity save()).
     *
     * @param saveHandler the required savehandler or null
     */
    public void setSaveHandler(SaveHandler saveHandler) {
        this.saveHandler = saveHandler == null ? nodeSavable.getDefaultSaveHandler() : saveHandler;
    }

    private class EntityStateChangeListener extends Listener<EntityStateChangeListenerParams> {

        public EntityStateChangeListener(String name) {
            super(name);
        }

        @Override
        @SuppressWarnings("IncompatibleEquals")
        public void action(EntityStateChangeListenerParams p) {
            if (p.equals(EntityStateChangeListenerParams.EDIT)) {
                nodeSavable.enable(getEntity());
                iconChange();
            }
            if (p.equals(EntityStateChangeListenerParams.SAVE)) {
                nodeSavable.disable();
                iconChange();
            }
            if (p.equals(EntityStateChangeListenerParams.REMOVE)) {
                nodeSavable.disable();
                setNoEntity();
            }
            if (p.equals(EntityStateChangeListenerParams.RESET)) {
                nodeSavable.disable();
            }
        }
    }

    @Override
    public String getHtmlDisplayName() {
        E entity = getEntity();
        return (entity.isNew() ? "<font color='#0000FF'><b>" : (entity.isEditing() ? "<b>" : "")) + getDisplayName();
    }

    private class EntityFieldChangeListener extends Listener<FieldChangeListenerParams> {

        public EntityFieldChangeListener(String name) {
            super(name);
        }

        @Override
        public void action(FieldChangeListenerParams p) {
            _processFieldChange(p.get());
            // TODO make decision about the Icon changes (based on changes to error state)
            iconChange(); // temporary - do always (just in case!)
        }
    }

    /**
     * Process field changes.
     *
     * @param field the field Id
     */
    protected abstract void _processFieldChange(IntWithDescription field);

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

    /**
     * Fire the Name Change.
     */
    protected void nameChange() {
        fireDisplayNameChange("old", "new");
        nameListenerFire();
    }

    /**
     * Fire the Title Change.
     */
    protected void titleChange() {
        titleListenerFire();
    }

    private void iconChange() {
        fireIconChange();
        fireOpenedIconChange();
    }

    private class NodeSavable<Z extends EntityRW> extends AbstractSavable implements Icon {

        private final String nodename;
        private Icon nodeicon;
        private Z e;
        private final SaveHandler defaultsavehandler = new DefaultSaveHandler();

        public NodeSavable(String nodename) {
            this.nodename = nodename;
        }

        public SaveHandler getDefaultSaveHandler() {
            return defaultsavehandler;
        }

        private class DefaultSaveHandler implements SaveHandler {

            @Override
            public void handleSave() throws IOException {
                e.save();
            }
        }

        public void enable(Z e) {
            this.e = e;
            content.add(this);
            register();
            Log.get("uk.org.rlinsdale.nbpcg.nodesupportlib").log(Level.FINEST, "NodeSavable:enable() for node {0}", nodename);
        }

        public void disable() {
            Log.get("uk.org.rlinsdale.nbpcg.nodesupportlib").log(Level.FINEST, "NodeSavable:disable() for node {0}", nodename);
            content.remove(this);
            unregister();
            e = null;
        }

        @Override
        protected void handleSave() throws IOException {
            Log.get("uk.org.rlinsdale.nbpcg.nodesupportlib").log(Level.FINE, "NodeSavable:handleSave() for node {0}", nodename);
            saveHandler.handleSave();
        }

        @Override
        protected String findDisplayName() {
            return getDisplayTitle();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof NodeSavable) {
                if (e == null || ((NodeSavable) obj).e == null) {
                    return false;
                }
                return e.getId() == ((NodeSavable) obj).e.getId();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return nodename.hashCode();
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (nodeicon == null) {
                nodeicon = new ImageIcon(_getIcon());
            }
            nodeicon.paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            if (nodeicon == null) {
                nodeicon = new ImageIcon(_getIcon());
            }
            return nodeicon.getIconWidth();
        }

        @Override
        public int getIconHeight() {
            if (nodeicon == null) {
                nodeicon = new ImageIcon(_getIcon());
            }
            return nodeicon.getIconHeight();
        }
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
        Log.get("uk.org.rlinsdale.nbpcg.nodesupportlib").finer("TreeNodeRW:destroy()");
        _deleteRemove();
    }

    @Override
    public final Transferable clipboardCut() throws IOException {
        Log.get("uk.org.rlinsdale.nbpcg.nodesupportlib").finer("TreeNodeRW:clipboardCut()");
        ExTransferable added = ExTransferable.create(super.clipboardCut());
        added.put(new ExTransfer());
        return added;
    }

    @Override
    public final Transferable clipboardCopy() throws IOException {
        Log.get("uk.org.rlinsdale.nbpcg.nodesupportlib").finer("TreeNodeRW:clipboardCopy()");
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
            Log.get("uk.org.rlinsdale.nbpcg.nodesupportlib").finer("TreeNodeRW$ExTransfer:getData()");
            return getEntity();
        }
    }

    /**
     * Cut and Paste - removal of Node action.
     */
    abstract protected void _cutAndPasteRemove();

    /**
     * Delete - removal of Node action.
     */
    abstract protected void _deleteRemove();
}
