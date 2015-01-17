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

import com.famfamfam.www.silkicons.Icons;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Action;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogHelper;

/**
 * Root Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 */
@RegisterLog("nbpcglibrary.node")
public abstract class RootNode<E extends Entity> extends AbstractNode implements LogHelper {

    private E e;

    /**
     * The icon name for this node
     */
    protected String iconname;

    /**
     * the local lookup
     */
    protected InstanceContent content;
    private DataFlavorAndAction[] allowedPaste;

    /**
     * Constructor
     *
     * @param iconname the iconname
     * @param e the entity
     * @param cf the childfactory
     * @param allowedPaste allowed paste actions
     */
    protected RootNode(String iconname, E e,
            RootChildFactory<E> cf, DataFlavorAndAction[] allowedPaste) {
        this(iconname, new InstanceContent(), e, cf, allowedPaste);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private RootNode(String iconname, InstanceContent content, E e,
            RootChildFactory<E> cf, DataFlavorAndAction[] allowedPaste) {
        super(Children.create(cf, true), new AbstractLookup(content));
        this.content = content;
        this.e = e;
        this.iconname = iconname;
        this.allowedPaste = allowedPaste;
        content.add(this);
        for (DataFlavorAndAction dfa : allowedPaste) {
            content.add(new ChildIndex(dfa));
        }
    }

    /**
     * Constructor.
     *
     * @param iconname the iconname
     * @param e the entity
     */
    protected RootNode(String iconname, E e) {
        this(iconname, new InstanceContent(), e);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private RootNode(String iconname, InstanceContent content, E e) {
        super(Children.LEAF, new AbstractLookup(content));
        this.content = content;
        this.e = e;
        this.iconname = iconname;
        content.add(this);
    }
    
    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this,e);
    }

    /**
     * Get the Root entity.
     *
     * @return the entity
     */
    public E getEntity() {
        return e;
    }

    /**
     * Set no entity
     */
    public void setNoEntity() {
        e = null;
    }

    @Override
    public String getHtmlDisplayName() {
        return "<i>" + getDisplayName();
    }

    @Override
    public Image getIcon(int type) {
        return _getIcon();
    }

    /**
     * Get the node icon.
     *
     * @return the icon image
     */
    protected java.awt.Image _getIcon() {
        return Icons.get(iconname);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    /**
     * Find the registered actions for this node.
     *
     * @param classname the node classname
     * @return the array of actions
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    protected Action[] findActions(String classname) {
        List<? extends Action> myActions = Utilities.actionsForPath("nbpcg/node/" + classname + "/actions/all");
        return myActions.toArray(new Action[myActions.size()]);
    }

    /**
     * Find the registered default action for this node.
     *
     * @param classname the node classname
     * @return the default action or null
     */
    protected Action findDefaultAction(String classname) {
        List<? extends Action> myActions = Utilities.actionsForPath("nbpcg/node/" + classname + "/actions/default");
        return myActions.isEmpty() ? null : myActions.get(0);
    }

    /**
     * Get the list of property items to be displayed in the properties sheet.
     *
     * @param props the list of properties
     * @return the array of property items
     */
    protected abstract List<PropertySupport.ReadOnly<?>> createPropertyItems(List<PropertySupport.ReadOnly<?>> props);

    @Override
    protected Sheet createSheet() {
        Sheet result = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        List<PropertySupport.ReadOnly<?>> props = new ArrayList<>();
        createPropertyItems(props).stream().forEach((psro) -> {
            set.put(psro);
        });
        result.put(set);
        return result;
    }

    // CUT and PASTE target support
    @Override
    public final PasteType getDropType(final Transferable t, int action, int index) {
        LogBuilder.writeLog("nbpcglibrary.node", this, "getDropType");
        if (allowedPaste != null) {
            for (DataFlavorAndAction dfa : allowedPaste) {
                if (t.isDataFlavorSupported(dfa.dataflavor) && ((action & (dfa.action)) != 0)) {
                    return new AllowedPasteType(t, dfa, action);
                }
            }
        }
        return null;
    }

    private class AllowedPasteType extends PasteType implements LogHelper {

        private final Transferable t;
        private final DataFlavorAndAction dfa;
        private final int action;

        public AllowedPasteType(final Transferable t, final DataFlavorAndAction dfa, int action) {
            this.t = t;
            this.dfa = dfa;
            this.action = action;
        }
        
        @Override
    public String classDescription() {
        return LogBuilder.classDescription(this);
    }

        @Override
        public Transferable paste() throws IOException {
            LogBuilder.writeLog("nbpcglibrary.node", this, "paste");
            Entity e;
            try {
                e = (Entity) t.getTransferData(dfa.dataflavor);
            } catch (UnsupportedFlavorException ex) {
                throw new LogicException("Unsupported Flavor Exception Raised in RootNode$AllowedPasteType:paste()");
            }
            final Node node;
            switch (action) {
                case DataFlavorAndAction.MOVE:
                    node = NodeTransfer.node(t, action);
                    if (node != null) {
                        LogBuilder.create("nbpcglibrary.node", Level.FINER).addMethodName(this, "paste")
                                .addMsg("remove previous node").write();
                        ((TreeNodeRW) node)._cutAndPasteRemove();
                    }
                    LogBuilder.create("nbpcglibrary.node", Level.FINER).addMethodName(this, "paste")
                            .addMsg("drag/drop action").write();
                    _moveAddChild(e);
                    break;
                case DataFlavorAndAction.CUT:
                    node = NodeTransfer.node(t, action);
                    if (node != null) {
                        LogBuilder.create("nbpcglibrary.node", Level.FINER).addMethodName(this, "paste")
                                .addMsg("remove previous node").write();
                        ((TreeNodeRW) node)._cutAndPasteRemove();
                    }
                    LogBuilder.create("nbpcglibrary.node", Level.FINER).addMethodName(this, "paste")
                            .addMsg("cut/paste action").write();
                    _cutAddChild(e);
                    break;
                case DataFlavorAndAction.COPY:
                    LogBuilder.create("nbpcglibrary.node", Level.FINER).addMethodName(this, "paste")
                            .addMsg("copy/paste action").write();
                    _copyAddChild(e);
                    break;
                default:
                    throw new LogicException("illegal action in ExtendedNode$AllowPasteType:paste()");
            }
            return null;
        }
    }

    @Override
    protected final void createPasteTypes(Transferable t, List<PasteType> s) {
        createPT(t, s, DataFlavorAndAction.MOVE);
        createPT(t, s, DataFlavorAndAction.CUT);
        createPT(t, s, DataFlavorAndAction.COPY);
        super.createPasteTypes(t, s);
    }

    private void createPT(Transferable t, List<PasteType> s, int action) {
        if (NodeTransfer.node(t, action) != null) {
            PasteType pt = getDropType(t, action, -1);
            if (null != pt) {
                s.add(pt);
            }
        }
    }

    // CUT and PASTE methods to be implemented
    /**
     * Move action - add child entity.
     *
     * @param child the entity
     */
    abstract protected void _moveAddChild(Entity child);

    /**
     * Cut action - add child entity.
     *
     * @param child the entity
     */
    abstract protected void _cutAddChild(Entity child);

    /**
     * Copy Action - add child entity.
     *
     * @param child the entity
     */
    abstract protected void _copyAddChild(Entity child);

    /**
     * Reorder Action - move child entity.
     *
     * @param df teh data flavour
     * @param perm the sort indicator
     */
    abstract protected void _moveReorderChildByFlavor(DataFlavor df, int[] perm);

    /**
     * Get the DataFlavour.
     *
     * @return the data flavour
     */
    abstract protected DataFlavor _getDataFlavor();

    private class ChildIndex extends Index.Support implements LogHelper {

        private final DataFlavorAndAction dfa;

        public ChildIndex(DataFlavorAndAction dfa) {
            this.dfa = dfa;
        }
        
        @Override
    public String classDescription() {
        return LogBuilder.classDescription(this);
    }

        @Override
        public Node[] getNodes() {
            return getChildren().getNodes();
        }

        @Override
        public int getNodesCount() {
            return getNodes().length;
        }

        @Override
        public void reorder(int[] perm) {
            if ((dfa.action & DataFlavorAndAction.MOVE) != 0) {
                LogBuilder.create("nbpcglibrary.node", Level.FINER).addMethodName(this, "reorder").write();
                _moveReorderChildByFlavor(dfa.dataflavor, perm);
            }
        }
    }
}
