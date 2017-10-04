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

import com.famfamfam.www.silkicons.Icons;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import uk.theretiredprogrammer.nbpcglibrary.icons.SpecialIcons;

/**
 * Root Node Abstract Class
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the CoreEntity Class
 */
@RegisterLog("nbpcglibrary.node")
public abstract class BasicNode<E extends CoreEntity> extends AbstractNode {

        /**
     * The date format for translating datetime strings in readable format
     * to/from Date objects
     */
    protected final DateFormat DATETIME_READABLE;
    /**
     * The date format for translating dateonly string in readable format
     * to/from Date objects
     */
    protected final DateFormat DATEONLY_READABLE;

    /**
     * the local lookup
     */
    protected final InstanceContent content;
    private DataFlavor[] allowedDataFlavors;
    private final String iconName;

    /**
     * Constructor
     *
     * @param cf the childfactory
     * @param allowedDataFlavors allowed dataflavours that can be pasted
     * @param iconName the name of the icon (from the Icons set)
     */
    protected BasicNode(CoreChildFactory<E> cf, DataFlavor[] allowedDataFlavors, String iconName) {
        this(new InstanceContent(), cf, allowedDataFlavors, iconName);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private BasicNode(InstanceContent content, CoreChildFactory<E> cf, DataFlavor[] allowedDataFlavors, String iconName) {
        super(Children.create(cf, true), new AbstractLookup(content));
        this.content = content;
        this.allowedDataFlavors = allowedDataFlavors;
        this.iconName = iconName;
        content.add(this);
        for (DataFlavor df : allowedDataFlavors) {
            content.add(new ChildIndex(df));
        }
        DATETIME_READABLE = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        DATETIME_READABLE.setLenient(true);
        DATEONLY_READABLE = new SimpleDateFormat("dd-MMM-yyyy");
        DATEONLY_READABLE.setLenient(true);
    }

    /**
     * Constructor.
     * @param iconName the name of the ion (from the Icons set)
     */
    protected BasicNode(String iconName) {
        this(new InstanceContent(), iconName);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private BasicNode(InstanceContent content, String iconName) {
        super(Children.LEAF, new AbstractLookup(content));
        this.content = content;
        this.iconName = iconName;
        content.add(this);
        DATETIME_READABLE = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        DATETIME_READABLE.setLenient(true);
        DATEONLY_READABLE = new SimpleDateFormat("dd-MMM-yyyy");
        DATEONLY_READABLE.setLenient(true);
    }

    /**
     * Get the Root entity.
     *
     * @return the entity
     */
    public abstract E getEntity();

    @Override
    public String getHtmlDisplayName() {
        return "<i>" + getDisplayName();
    }

    @Override
    public Image getIcon(int type) {
        return Icons.get(iconName);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
    
    /**
     * Get the node icon combined with an error marker.
     *
     * @return the image
     */
    public Image getIconWithError() {
        return addErrorToIcon(Icons.get(iconName));
    }

    /**
     * Create the node icon combined with an error marker.
     *
     * @param icon the node icon image
     * @return the node icon image combined with error marker
     */
    public Image addErrorToIcon(Image icon) {
        return ImageUtilities.mergeImages(icon, SpecialIcons.get("errormarker"), 0, 6);
    }

    /**
     * Find the registered actions for this node.
     *
     * @param classname the node classname
     * @return the array of actions
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    protected Action[] findActions(String classname) {
        List<? extends Action> myActions = Utilities.actionsForPath("nbpcglibrary/node/" + classname + "/actions/all");
        return myActions.toArray(new Action[myActions.size()]);
    }

    /**
     * Find the registered default action for this node.
     *
     * @param classname the node classname
     * @return the default action or null
     */
    protected Action findDefaultAction(String classname) {
        List<? extends Action> myActions = Utilities.actionsForPath("nbpcglibrary/node/" + classname + "/actions/default");
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

    // DND target support
    @Override
    public final PasteType getDropType(final Transferable t, int action, int index) {
        if (allowedDataFlavors != null) {
            for (DataFlavor df : allowedDataFlavors) {
                if (t.isDataFlavorSupported(df)) {
                    return new DNDPasteType(t, df);
                }
            }
        }
        return null;
    }

    private class DNDPasteType extends PasteType {

        private final Transferable t;
        private final DataFlavor df;

        public DNDPasteType(final Transferable t, final DataFlavor df) {
            this.t = t;
            this.df = df;
        }

        @Override
        public Transferable paste() throws IOException {
            BasicNode node = (BasicNode) NodeTransfer.node(t, NodeTransfer.MOVE);
            if (node != null) {
                nodeCutPaste(node);
                return null;
            }
            node = (BasicNode) NodeTransfer.node(t, NodeTransfer.COPY);
            if (node != null) {
                nodeCopyPaste(node);
            }
            return null;
        }
    }

    // CUT / COPY target support
    @Override
    protected final void createPasteTypes(Transferable t, List<PasteType> s) {
        super.createPasteTypes(t, s);
        if (allowedDataFlavors != null) {
            for (DataFlavor df : allowedDataFlavors) {
                if (t.isDataFlavorSupported(df)) {
                    s.add(new AllowedPasteType(t, df));
                }
            }
        }
    }

    private class AllowedPasteType extends PasteType {

        private final Transferable t;
        private final DataFlavor df;

        public AllowedPasteType(final Transferable t, final DataFlavor df) {
            this.t = t;
            this.df = df;
        }

        @Override
        public Transferable paste() throws IOException {
            BasicNode node = (BasicNode) NodeTransfer.node(t, NodeTransfer.MOVE);
            if (node != null) {
                nodeCutPaste(node);
                return null;
            }
            node = (BasicNode) NodeTransfer.node(t, NodeTransfer.COPY);
            if (node != null) {
                nodeCopyPaste(node);
            }
            return null;
        }
    }

    private class ChildIndex extends Index.Support {

        private final DataFlavor df;

        public ChildIndex(DataFlavor df) {
            this.df = df;
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
            nodeReorderChildByFlavor(df, perm);
        }
    }

    // DND, COPY, CUT and PASTE methods to be implemented
    /**
     * Cut and Paste action.
     *
     * @param child the entity
     * @throws IOException if problem
     */
    abstract protected void nodeCutPaste(BasicNode child) throws IOException;

    /**
     * Copy and Paste action.
     *
     * @param child the entity
     * @throws IOException if problem
     */
    abstract protected void nodeCopyPaste(BasicNode child) throws IOException;

    /**
     * Reorder Action - move child entity.
     *
     * @param df the data flavor
     * @param perm the sort indicator
     */
    abstract protected void nodeReorderChildByFlavor(DataFlavor df, int[] perm);

    /**
     * Get the DataFlavour.
     *
     * @return the data flavour
     */
    abstract protected DataFlavor nodeGetDataFlavor();
    
}
