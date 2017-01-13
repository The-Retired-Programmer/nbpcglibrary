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
 * View for a table object
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class TableView extends JScrollPane implements PaneView<FieldViewAPI> {

    private final int columns;
    private int row = 0;
    private int col = 0;
    private final JPanel panel;

    /**
     * Constructor
     *
     * @param title the table title (or null if no title to be displayed
     * @param columns the table column width (as measured in components)
     *
     */
    public TableView(String title, int columns) {
        this(title, columns, new JPanel());
    }

    private TableView(String title, int columns, JPanel panel) {
        super(panel);
        this.panel = panel;
        this.columns = columns;
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
    public void insertChildViews(List<FieldViewAPI> fvl) {
        col = 0;
        fvl.stream().forEach(fv
                -> fv.getViewComponents().stream().forEach(component -> {
                    if (component != null) {
                        panel.add((JComponent) component, makeconstraints(row, col));
                    }
                    col++;
                }));
        row++;
    }

    private GridBagConstraints makeconstraints(int row, int col) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = col;
        c.gridy = row;
        return c;
    }

    /**
     * Add a single JComponent to a row (spanning columns)
     *
     * @param component the component
     */
    public void insertSpannedRow(JComponent component) {
        panel.add(component, makeconstraints(row));
        row++;
    }

    private GridBagConstraints makeconstraints(int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = columns + 1;
        return c;
    }

    /**
     * Clear all components displayed on this panel
     */
    public void clear() {
        row = 0;
        panel.removeAll();
        validate();
    }
}
