/*
 * Copyright (C) 2015 Richard Linsdale.
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class Table extends GridBagPanel implements HasInstanceDescription, ActionListener, ItemListener {

    private final TableDef tabledef;
    private final IconButton addbutton;
    private final IconButton deletebutton;
    private final IconButton copybutton;
    private int checkboxcount = 0;

    /**
     * Constructor
     *
     * @param tabledef the table definition
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Table(TableDef tabledef) {
        super(tabledef.getTitle(), tabledef.getColumnHeadings().size()+2);
        this.tabledef = tabledef;
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this, tabledef.getTitle());
        List<JComponent> colhdr = new ArrayList<>();
        colhdr.add(null);
        tabledef.getColumnHeadings().stream().forEach((columnheader) -> {
            colhdr.add(new JLabel(columnheader));
        });
        ErrorMarkerField errorMarker = new ErrorMarkerField();
        errorMarker.report("Dummy table error message");
        colhdr.add((JComponent)errorMarker.getComponents().get(0));
        addRow(colhdr);
        for (int i = 0; i < 3; i++) {
            ErrorMarkerDecorator rowerrorMarker = new ErrorMarkerDecorator();
            rowerrorMarker.report("Dummy error message");
            JCheckBox selector = new JCheckBox("", false);
            selector.addItemListener(this);
            List<JComponent> rowcomponents = tabledef.getRowComponents();
            rowcomponents.add(0, selector);
            rowcomponents.add((JComponent)rowerrorMarker.getComponents().get(0));
            addRow(rowcomponents);
        }
        JPanel buttons = new HBoxPanel();
        buttons.add(addbutton = new IconButton("add", "Add new line"));
        addbutton.setActionCommand("add");
        addbutton.addActionListener(this);
        buttons.add(deletebutton = new IconButton("delete", "Delete line"));
        deletebutton.setEnabled(false);
        deletebutton.setActionCommand("delete");
        deletebutton.addActionListener(this);
        buttons.add(copybutton = new IconButton("arrow_divide", "Copy line"));
        copybutton.setEnabled(false);
        copybutton.setActionCommand("copy");
        copybutton.addActionListener(this);
        addSpannedRow(buttons);
    }

    @Override
    public final String instanceDescription() {
        return LogBuilder.instanceDescription(this, tabledef.getTitle());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "add":
                System.out.println("ADD");
                break;
            case "delete":
                System.out.println("DELETE");
                break;
            case "copy":
                System.out.println("COPY");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            checkboxcount++;
            if (checkboxcount == 1) {
                deletebutton.setEnabled(true);
                copybutton.setEnabled(true);
            }
        } else {
            checkboxcount--;
            if (checkboxcount == 0) {
                deletebutton.setEnabled(false);
                copybutton.setEnabled(false);
            }
        }
    }
}
