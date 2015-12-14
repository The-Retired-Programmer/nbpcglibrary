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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * A basic GridBag based panel for displaying forms or tables.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class GridBagPanel extends JPanel {

    private static final int DEFAULTCOLUMNS = 4;
    private int row = 0;
    private int columns;

    /**
     * Constructor
     *
     */
    public GridBagPanel() {
        this(null, DEFAULTCOLUMNS);
    }

    /**
     * Constructor
     *
     * @param columns the gridbag width
     */
    public GridBagPanel(int columns) {
        this(null, columns);
    }

    /**
     * Constructor
     *
     * @param borderTitle the gridbag title
     */
    public GridBagPanel(String borderTitle) {
        this(borderTitle, DEFAULTCOLUMNS);
    }

    /**
     * Constructor
     *
     * @param borderTitle the gridbag title
     * @param columns the gridbag width
     */
    public GridBagPanel(String borderTitle, int columns) {
        this.columns = columns;
        setLayout(new GridBagLayout());
        if (borderTitle != null) {
            setBorder(new TitledBorder(borderTitle));
        }
    }

    /**
     * Add an array of components to a row, starting in column1
     *
     * @param components array of components
     */
    public void addRow(JComponent[] components) {
        int col = 0;
        for (JComponent component : components) {
            if (component != null) {
                add(component, makeconstraints(row, col));
            }
            col++;
        }
        row++;
    }
    
     /**
     * Add an list of components to a row, starting in column1
     *
     * @param components list of components
     */
    public void addRow(List<JComponent> components) {
        int col = 0;
        for (JComponent component : components) {
            if (component != null) {
                add(component, makeconstraints(row, col));
            }
            col++;
        }
        row++;
    }

    /**
     * Add a JComponent to a row (spanning columns)
     *
     * @param component the component
     */
    public void addSpannedRow(JComponent component) {
        add(component, makeconstraints(row));
        row++;
    }

    private GridBagConstraints makeconstraints(int row, int col) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = col;
        c.gridy = row;
        return c;
    }

    private GridBagConstraints makeconstraints(int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = columns + 1;
        return c;
    }
}
