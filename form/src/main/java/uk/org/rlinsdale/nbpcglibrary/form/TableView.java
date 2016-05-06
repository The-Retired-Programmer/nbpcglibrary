/*
 * Copyright (C) 2015-2016 Richard Linsdale.
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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * View for a table object
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TableView extends JPanel implements JPanelView<FieldViewAPI> {

    private final int columns;
    private int row = 0;
    private int col = 0;

    /**
     * Constructor
     *
     * @param title the table title (or null if no title to be displayed
     * @param columns the table column width (as measured in components)
     *
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public TableView(String title, int columns) {
        this.columns = columns;
        setLayout(new GridBagLayout());
        if (title != null) {
            setBorder(new TitledBorder(title));
        }
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this, title);
    }

    @Override
    public void insertChildViews(List<FieldViewAPI> fvl) {
        col = 0;
        fvl.stream().forEach(fv
                -> fv.getViewComponents().stream().forEach(component -> {
                    if (component != null) {
                        add((JComponent) component, makeconstraints(row, col));
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
        add(component, makeconstraints(row));
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
        this.removeAll();
        validate();
    }
}
