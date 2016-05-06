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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * FormView class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FormView extends JPanel implements JPanelView<FieldViewAPI> {

    private int row = 0;
    private int col = 0;

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
        setLayout(new GridBagLayout());
        if (title != null) {
            setBorder(new TitledBorder(title));
        }
    }

    @Override
    public void insertChildViews(List<FieldViewAPI> childviews) {
        childviews.stream().forEach(fv -> {
                col = 0;
                fv.getViewComponents().stream().forEach(component
                    -> {
                        if (component != null) {
                            add((JComponent) component, makeconstraints(row, col));
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
