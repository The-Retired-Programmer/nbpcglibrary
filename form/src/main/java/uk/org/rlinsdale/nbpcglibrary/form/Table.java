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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JPanel;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;

/**
 * Abstract Table class - subclass to create entity table classes
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class for row data access
 * @param <S> the Source Class for row data access
 */
public abstract class Table<E extends Entity, S extends EntitySource<E>> extends GridBagPanel implements HasInstanceDescription, ActionListener, ItemListener {

    private final String title;
    private final Rules tablerules;
    private final IconButton addbutton;
    private final IconButton deletebutton;
    private final IconButton copybutton;
    private int checkboxcount = 0;
    private final JPanel buttons;
    private final RowFields headerfields;
    private final List<Field<Boolean>> checkboxes = new ArrayList<>();
    private String[] parameters;
    private final ErrorMarkerField errorMarker = new ErrorMarkerField();
    private final List<RowFields> rows = new ArrayList<>();
    private final List<S> sources = new ArrayList<>();

    /**
     * Constructor
     *
     * @param title the table title (or null if no title to be displayed
     * @param columnheadings the column headings to be displayed
     * @param tablerules the table level rules or null if notable level rules
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Table(String title, String[] columnheadings, Rules tablerules) {
        super(title, columnheadings.length * 2 + 3);
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this, title);
        this.title = title;
        this.tablerules = tablerules;
        // create the header fields
        headerfields = new RowFields();
        for (String label : columnheadings) {
            headerfields.add(FieldBuilder.stringType().label(label).columnlabelField());
        }
        headerfields.add(0, FieldBuilder.stringType().label("").noerrormarker().columnlabelField());
        headerfields.add(0, FieldBuilder.stringType().label("").noerrormarker().columnlabelField());
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
    }

    /**
     * Initialise the table (pre display)
     */
    public void opened() {
        setRows();
        drawTable();
    }

    /**
     * terminate the table (post display)
     */
    public void closed() {
        sources.stream().forEach((s) -> {
            s.closed();
        });
    }

    /**
     * Get the list of row fields for this table
     *
     * @return A list of rows contents
     */
    public final List<RowFields> getRows() {
        return rows;
    }

    private void setRows() {
        sources.stream().forEach((s) -> {
            s.closed();
        });
        sources.clear();
        rows.clear();
        getRowEntities().stream().map((p) -> createNewRowSource(p)).map((s) -> {
            sources.add(s);
            return s;
        }).forEach((s) -> {
            rows.add(s.getRowFields());
        });
        sources.stream().forEach((s) -> {
            s.opened();
        });
    }

    /**
     * Get a list of all entities to be displayed in table
     *
     * @return the list of entities
     */
    protected abstract List<E> getRowEntities();

    /**
     * Get a new instance of a row Source
     *
     * @param e the entity
     * @return a row source instance (binding the entity to the table row)
     */
    protected abstract S createNewRowSource(E e);

    /**
     * Draw the table into the table's gridbag for display
     */
    private void drawTable() {
        clear();
        addRow(headerfields);
        checkboxes.clear();
        getRows().stream().map((RowFields row) -> {
            Field checkbox = FieldBuilder.booleanType().initialvalue(false).itemlistener(this).checkboxField();
            checkboxes.add(checkbox);
            RowFields displayrow = new RowFields();
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

    @Override
    public final String instanceDescription() {
        return LogBuilder.instanceDescription(this, title);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "add":
                createNewEntity();
                break;
            case "delete":
                getSelectedRows().stream().sorted(Comparator.reverseOrder()).forEach((index) -> {
                    sources.get(index).getEntity().remove();
                });
                break;
            case "copy":
                getSelectedRows().stream().sorted().forEach((index) -> {
                    S s = sources.get(index);
                    copyEntity(s.getEntity());
                });
                break;
        }
        setRows();
        drawTable();
    }

    private List<Integer> getSelectedRows() {
        List<Integer> selected = new ArrayList<>();
        int count = 0;
        for (Field<Boolean> e : checkboxes) {
            if (e.get()) {
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

    /**
     * Set the values of all fields.
     */
    public final void updateAllFieldsFromSource() {
        rows.stream().forEach((r) -> {
            r.updateFieldsFromSources();
        });
    }

    /**
     * Set the values of all fields into sources.
     */
    public final void updateAllSourcesFromFields() {
        rows.stream().forEach((r) -> {
            r.updateSourcesFromFields();
        });
    }

    /**
     * Get the parameters to be returned from the table
     * @return the parameters
     */
    protected String[] getParameters() {
        return parameters;
    }

    /**
     * Set the parameters to be returned from the table 
     *
     * @param parameters the set of parameters
     */
    protected void setParameters(String... parameters) {
        this.parameters = parameters;
    }

    /**
     * Check if all rules in the table's rule set and each individual row rule
     * set are valid.
     *
     * @return true if all rules are valid
     */
    public final boolean checkRules() {
        boolean valid = true;
        for (RowFields r : rows) {
            if (!r.checkRules()) {
                valid = false;
            }
        }
        if (!checkTableRules()) {
            valid = false;
        }
        return valid;
    }

    private boolean checkTableRules() {
        if (tablerules != null) {
            boolean res = tablerules.checkRules();
            if (res) {
                errorMarker.clear();
            } else {
                errorMarker.report(tablerules.getErrorMessages());
            }
            return res;
        } else {
            return true;
        }
    }

    /**
     * Create a new Entity
     */
    protected abstract void createNewEntity();

    /**
     * Copy an Entity, creating a new Entity
     *
     * @param e the Entity to copy from
     */
    protected abstract void copyEntity(E e);

    /**
     * Save all the rows in this table
     *
     * @return true if all entities saved successfully
     * @throws IOException if problems saving any row (ie entity)
     */
    public boolean save() throws IOException {
        boolean ok = true;
        for (S s : sources) {
            if (!s.getEntity().save()) {
                ok = false;
            }
        }
        return ok;
    }
}
