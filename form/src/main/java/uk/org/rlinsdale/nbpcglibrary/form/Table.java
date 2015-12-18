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
    private final JPanel buttons;
    private final FieldList headerfields;
    private final List<Integer> selectedrows = new ArrayList<>();
    private final List<EditableField<Boolean>>checkboxes = new ArrayList<>();

    /**
     * Constructor
     *
     * @param tabledef the table definition
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Table(TableDef tabledef) {
        super(tabledef.getTitle(), tabledef.getTableWidth() + 2);
        this.tabledef = tabledef;
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this, tabledef.getTitle());
        // create the header fields
        headerfields = tabledef.getColumnHeadings();
        headerfields.add(0, FieldBuilder.stringType().label("").noerrormarker().columnlabelField());
        ErrorMarkerField errorMarker = new ErrorMarkerField();
        errorMarker.report("Dummy table error message");
        headerfields.add(errorMarker);
        // create the buttons panel
        buttons = new HBoxPanel();
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
        // and drw the table
        drawTable();
    }

    private void drawTable() {
        addRow(headerfields);
        checkboxes.clear();
        tabledef.getRows().stream().map((row) -> {
            EditableFieldList displayrow = new EditableFieldList();
            EditableField checkbox = FieldBuilder.booleanType().initialvalue(false).itemlistener(this).checkboxField();
            checkboxes.add(checkbox);
            displayrow.add(checkbox);
            displayrow.addAll(row);
            return displayrow;
        }).forEach((displayrow) -> {
            addRow(displayrow);
        });
        checkboxcount = 0;
        deletebutton.setEnabled(false);
        copybutton.setEnabled(false);
        addSpannedRow(buttons);
        validate();
    }

    private void redrawTable() {
        clear();
        drawTable();
    }

    @Override
    public final String instanceDescription() {
        return LogBuilder.instanceDescription(this, tabledef.getTitle());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "add":
                tabledef.createNewRow();
                redrawTable();
                break;
            case "delete":
                tabledef.deleteRows(getSelectedRows());
                redrawTable();
                break;
            case "copy":
                tabledef.createCopyRows(getSelectedRows());
                redrawTable();
                break;
        }
    }
    
    private List<Integer> getSelectedRows(){
        List<Integer> selected = new ArrayList<>();
        int count = 0;
        for (EditableField<Boolean> e : checkboxes) {
            if (e.get()){
                selected.add(count);
            }
            count++;
        }
        return selected;
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
