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
package uk.theretiredprogrammer.nbpcglibrary.topcomponent;

import com.famfamfam.www.silkicons.Icons;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A element to be added to the TopComponent toolbar
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
