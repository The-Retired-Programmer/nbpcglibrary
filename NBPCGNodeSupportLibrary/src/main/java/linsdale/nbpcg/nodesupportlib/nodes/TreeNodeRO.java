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

import java.awt.Image;
import linsdale.nbpcg.icons.SpecialIcons;
import linsdale.nbpcg.datasupportlib.entity.EntityManagerRO;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.datasupportlib.entityreferences.EntityReference;
import org.openide.util.ImageUtilities;

/**
 * class providing extended Node support
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public abstract class TreeNodeRO<E extends EntityRO> extends RootNode<E> {

    private final EntityReference<E> eref;

    /**
     * Constructor
     *
     * @param nodename
     * @param iconname
     * @param e
     * @param cf
     * @param emclass
     * @param allowedPaste
     */
    protected TreeNodeRO(String nodename, String iconname, E e, BasicChildFactory<E> cf, Class<? extends EntityManagerRO> emclass, DataFlavorAndAction[] allowedPaste) {
        super(iconname, null, cf, allowedPaste);
        eref = new EntityReference<>(nodename, e, emclass);
    }

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

    public abstract String getDisplayTitle();

    /**
     * Get the node icon
     *
     * @param type
     * @return the node icon
     */
    @Override
    public Image getIcon(int type) {
        return getEntity().checkRules() ? _getIcon() : _getIconWithError();
    }

    protected Image _getIconWithError() {
        return _addErrorToIcon(_getIcon());
    }

    protected Image _addErrorToIcon(Image icon) {
        return ImageUtilities.mergeImages(icon, SpecialIcons.get("errormarker"), 0, 6);
    }
}