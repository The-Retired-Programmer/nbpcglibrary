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
package linsdale.nbpcg.nodesupportlib.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import linsdale.nbpcg.annotations.SaveNodeAction;
import org.openide.actions.CutAction;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;

/**
 * Cut - Context Aware Action for use with node menus
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@SaveNodeAction
public final class NodeCutAction extends AbstractAction implements ContextAwareAction {

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return CutAction.get(CutAction.class);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }
}
