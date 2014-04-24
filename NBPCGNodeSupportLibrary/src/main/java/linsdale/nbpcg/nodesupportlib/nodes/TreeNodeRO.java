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
package linsdale.nbpcg.nodesupportlib.nodes;

import java.awt.Image;
import linsdale.nbpcg.icons.SpecialIcons;
import linsdale.nbpcg.datasupportlib.entity.EntityManagerRO;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.datasupportlib.entityreferences.EntityReference;
import org.openide.util.ImageUtilities;

/**
 * Read-Only Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 */
public abstract class TreeNodeRO<E extends EntityRO> extends RootNode<E> {

    private final EntityReference<E> eref;

    /**
     * Constructor.
     *
     * @param  nodename the node name
     * @param iconname the iconname
     * @param e the entity
     * @param cf the childfactory
     * @param emclass the entity manager class
     * @param allowedPaste allowed paste actions
     */
    protected TreeNodeRO(String nodename, String iconname, E e, BasicChildFactory<E> cf, Class<? extends EntityManagerRO> emclass, DataFlavorAndAction[] allowedPaste) {
        super(iconname, null, cf, allowedPaste);
        eref = new EntityReference<>(nodename, e, emclass);
    }

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param iconname the iconname
     * @param e the entity
     * @param emclass the entity manager class
     */
    protected TreeNodeRO(String nodename, String iconname, E e, Class<? extends EntityManagerRO> emclass) {
        super(iconname, null);
        eref = new EntityReference<>(nodename, e, emclass);
    }

    @Override
    public E getEntity() {
        return eref.get();
    }

    @Override
    public void setNoEntity() {
        eref.set();
    }

    /**
     * Get the display title for this node.
     * 
     * @return the title
     */
    public abstract String getDisplayTitle();

    @Override
    public Image getIcon(int type) {
        return getEntity().checkRules() ? _getIcon() : _getIconWithError();
    }

    /**
     * Get the node icon combined with an error marker.
     * 
     * @return the image
     */
    protected Image _getIconWithError() {
        return _addErrorToIcon(_getIcon());
    }

    /**
     * Create the node icon combined with an error marker.
     * 
     * @param icon the node icon image
     * @return the node icon image combined with error marker
     */
    protected Image _addErrorToIcon(Image icon) {
        return ImageUtilities.mergeImages(icon, SpecialIcons.get("errormarker"), 0, 6);
    }
}