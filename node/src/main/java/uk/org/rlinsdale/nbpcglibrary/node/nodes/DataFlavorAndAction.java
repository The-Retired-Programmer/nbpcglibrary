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

import java.awt.datatransfer.DataFlavor;
import org.openide.nodes.NodeTransfer;

/**
 * A DataStructure recording DataFlavour and NodeTransfer type (Action).
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DataFlavorAndAction {

    /**
     * COPY = NodeTransfer.CLIPBOARD_COPY
     */
    public static final int COPY = NodeTransfer.CLIPBOARD_COPY;

    /**
     * CUT = NodeTransfer.CLIPBOARD_CUT
     */
    public static final int CUT = NodeTransfer.CLIPBOARD_CUT;

    /**
     * MOVE = NodeTransfer.DND_MOVE
     */
    public static final int MOVE = NodeTransfer.DND_MOVE;

    /**
     * COPYCUT = COPY | CUT
     */
    public static final int COPYCUT = COPY | CUT;

    /**
     * COPYMOVE = COPY | MOVE
     */
    public static final int COPYMOVE = COPY | MOVE;

    /**
     * CUTMOVE = CUT | MOVE
     */
    public static final int CUTMOVE = CUT | MOVE;

    /**
     * COPYCUTMOVE = COPY | CUT | MOVE
     */
    public static final int COPYCUTMOVE = COPY | CUT | MOVE;

    /**
     * The Data Flavour
     */
    public final DataFlavor dataflavor;

    /**
     * The Node transfer type (action)
     */
    public final int action;

    /**
     * Constructor.
     *
     * @param dataflavor the data flavour
     * @param action the Node transfer type
     */
    public DataFlavorAndAction(DataFlavor dataflavor, int action) {
        this.dataflavor = dataflavor;
        this.action = action;
    }
}
