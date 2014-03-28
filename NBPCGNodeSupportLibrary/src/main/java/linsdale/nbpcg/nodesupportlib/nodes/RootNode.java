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
package linsdale.nbpcg.nodesupportlib.nodes;

import com.famfamfam.www.silkicons.Icons;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import linsdale.nbpcg.annotations.RegisterLog;
import linsdale.nbpcg.datasupportlib.entity.Entity;
import linsdale.nbpcg.supportlib.Log;
import linsdale.nbpcg.supportlib.LogicException;
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

/**
 * class providing extended Node support
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
@RegisterLog("linsdale.nbpcg.nodesupportlib")
public abstract class RootNode<E extends Entity> extends AbstractNode {

    private E e;
    protected String iconname;
    protected InstanceContent content;
    private DataFlavorAndAction[] allowedPaste;

    /**
     * Constructor
     *
     * @param iconname
     * @param e
     * @param cf
     * @param allowedPaste
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

    public E getEntity() {
        return e;
    }

    public void setNoEntity() {
        e = null;
    }

    /**
     * Get the node display name
     *
     * @return the display name
     */
    @Override
    public String getHtmlDisplayName() {
        return "<i>" + getDisplayName();
    }

    /**
     * Get the node icon
     *
     * @param type
     * @return the node icon
     */
    @Override
    public Image getIcon(int type) {
        return _getIcon();
    }

    protected java.awt.Image _getIcon() {
        return Icons.get(iconname);
    }

    /**
     * Get the Icon to display when node is "open"
     *
     * @param type icon type
     * @return the icon
     */
    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
      
    protected Action[] findActions(String classname) {
        List<? extends Action> myActions = Utilities.actionsForPath("nbpcg/node/"+classname+"/actions/all");
        return myActions.toArray(new Action[myActions.size()]);
    }
    
    protected Action findDefaultAction(String classname) {
        List<? extends Action> myActions = Utilities.actionsForPath("nbpcg/node/"+classname+"/actions/default");
        return myActions.isEmpty()? null : myActions.get(0);
    }
    
    /**
     * Get the list of property items to be displayed in the properties sheet.
     *
     * @param props
     * @return the array of property items
     */
    protected abstract List<PropertySupport.ReadOnly<?>> createPropertyItems(List<PropertySupport.ReadOnly<?>> props);

    /**
     * Create the property sheet.
     *
     * @return the property sheet
     */
    @Override
    protected Sheet createSheet() {
        Sheet result = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        List<PropertySupport.ReadOnly<?>> props = new ArrayList<>();
        for (PropertySupport.ReadOnly<?> psro : createPropertyItems(props)) {
            set.put(psro);
        }
        result.put(set);
        return result;
    }

    // CUT and PASTE target support
    @Override
    public final PasteType getDropType(final Transferable t, int action, int index) {
        Log.get("linsdale.nbpcg.nodesupportlib").finer("RootNode:getDropType()");
        if (allowedPaste != null) {
            for (DataFlavorAndAction dfa : allowedPaste) {
                if (t.isDataFlavorSupported(dfa.dataflavor) && ((action & (dfa.action)) != 0)) {
                    return new AllowedPasteType(t, dfa, action);
                }
            }
        }
        return null;
    }

    private class AllowedPasteType extends PasteType {

        private final Transferable t;
        private final DataFlavorAndAction dfa;
        private final int action;

        public AllowedPasteType(final Transferable t, final DataFlavorAndAction dfa, int action) {
            this.t = t;
            this.dfa = dfa;
            this.action = action;
        }

        @Override
        public Transferable paste() throws IOException {
            Log.get("linsdale.nbpcg.nodesupportlib").finer("RootNode$AllowPasteType:paste()");
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
                        Log.get("linsdale.nbpcg.nodesupportlib").finer("RootNode$AllowPasteType:paste() - remove previous node");
                        ((TreeNodeRW) node)._cutAndPasteRemove();
                    }
                    Log.get("linsdale.nbpcg.nodesupportlib").finer("RootNode$AllowPasteType:paste() - drag/drop action");
                    _moveAddChild(e);
                    break;
                case DataFlavorAndAction.CUT:
                    node = NodeTransfer.node(t, action);
                    if (node != null) {
                        Log.get("linsdale.nbpcg.nodesupportlib").finer("ExtendedNode$AllowPasteType:paste() - remove previous node");
                        ((TreeNodeRW) node)._cutAndPasteRemove();
                    }
                    Log.get("linsdale.nbpcg.nodesupportlib").finer("ExtendedNode$AllowPasteType:paste() - cut/paste action");
                    _cutAddChild(e);
                    break;
                case DataFlavorAndAction.COPY:
                    Log.get("linsdale.nbpcg.nodesupportlib").finer("ExtendedNode$AllowPasteType:paste() - copy/paste action");
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

    abstract protected void _moveAddChild(Entity child);

    abstract protected void _cutAddChild(Entity child);

    abstract protected void _copyAddChild(Entity child);

    abstract protected void _moveReorderChildByFlavor(DataFlavor df, int[] perm);

    abstract protected DataFlavor _getDataFlavor();

    private class ChildIndex extends Index.Support {

        private final DataFlavorAndAction dfa;

        public ChildIndex(DataFlavorAndAction dfa) {
            this.dfa = dfa;
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
                Log.get("linsdale.nbpcg.nodesupportlib").finer("RootNode$ChildIndex:reorder()");
                _moveReorderChildByFlavor(dfa.dataflavor, perm);
            }
        }
    }

    
}
