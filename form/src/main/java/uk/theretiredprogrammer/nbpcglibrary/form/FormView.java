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
package uk.theretiredprogrammer.nbpcglibrary.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 * FormView class
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class FormView extends JScrollPane implements PaneView<FieldViewAPI> {

    private int row = 0;
    private int col = 0;
    private final JPanel panel;

    /**
     * Constructor
     */
    public FormView() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param title the view's title
     */
    public FormView(String title) {
        this(null, new JPanel());
    }
    
    private FormView(String title, JPanel panel) {
        super(panel);
        this.panel = panel;
        panel.setLayout(new GridBagLayout());
        if (title != null) {
            panel.setBorder(new TitledBorder(title));
        }
    }
    
    @Override
    public JComponent getViewComponent() {
        return this;
    }

    @Override
    public void insertChildViews(List<FieldViewAPI> childviews) {
        childviews.stream().forEach(fv -> {
                col = 0;
                fv.getViewComponents().stream().forEach(component
                    -> {
                        if (component != null) {
                            panel.add((JComponent) component, makeconstraints(row, col));
                        }
                        col++;
                    });
                row++;
            });
    }

    private GridBagConstraints makeconstraints(int row, int col) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = col;
        c.gridy = row;
        return c;
    }
}
