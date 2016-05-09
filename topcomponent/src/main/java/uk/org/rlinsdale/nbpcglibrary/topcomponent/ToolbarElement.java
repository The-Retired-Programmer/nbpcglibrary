/*
 * Copyright (C) 2016 Richard Linsdale.
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
package uk.org.rlinsdale.nbpcglibrary.topcomponent;

import com.famfamfam.www.silkicons.Icons;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A element to be added to the TopComponent toolbar
 * 
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ToolbarElement implements ActionListener {
    
    private final String imagename;
    private final String tooltip;
    private final Supplier<Boolean> actionfunction;
    
    public ToolbarElement(String imagename, String tooltip, Supplier<Boolean> actionfunction) {
        this.imagename = imagename;
        this.tooltip = tooltip;
        this.actionfunction = actionfunction;
    }
    
    public JButton getToolbarButton() {
        JButton button = new JButton();
        button.setToolTipText(tooltip);
        button.addActionListener(this);
        button.setIcon(new ImageIcon(Icons.get(imagename)));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionfunction.get();
    }
}
