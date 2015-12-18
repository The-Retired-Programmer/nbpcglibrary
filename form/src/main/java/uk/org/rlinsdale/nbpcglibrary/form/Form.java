/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import static uk.org.rlinsdale.nbpcglibrary.form.Form.FormSaveResult.SAVEFAIL;
import static uk.org.rlinsdale.nbpcglibrary.form.Form.FormSaveResult.SAVESUCCESS;
import static uk.org.rlinsdale.nbpcglibrary.form.Form.FormSaveResult.SAVEVALIDATIONFAIL;

/**
 * A Form object which can be displayed in a dialog box.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@RegisterLog("nbpcglibrary.form")
public class Form extends VBoxPanel implements HasInstanceDescription {

    /**
     * The Result code from a Form save action
     */
    public enum FormSaveResult {

        /**
         * Save was successful
         */
        SAVESUCCESS,
        /**
         * Save failed due to validation
         */
        SAVEVALIDATIONFAIL,
        /**
         * Save action failed
         */
        SAVEFAIL,
        /**
         * Save action was cancelled by user
         */
        CANCELLED,
        /**
         * Save action failed as user closed dialog
         */
        CLOSED
    }

    private String formname;
    private final List<FieldsDef> fieldsdefs;
    private final List<TableDef> tabledefs;
    private final Event<SimpleEventParams> cancelEvent;

    /**
     * Constructor
     *
     * @param formname the form's name
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Form(String formname) {
        fieldsdefs = new ArrayList<>();
        tabledefs = new ArrayList<>();
        this.formname = formname;
        cancelEvent = new Event<>(instanceDescription() + "-cancel");
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this, formname);
    }

    /**
     * Constructor
     *
     * @param formname the form's name
     * @param fieldsdef the collection of fields to be displayed in this form
     */
    public Form(String formname, FieldsDef fieldsdef) {
        this(formname);
        addFieldsdef(fieldsdef);
    }

    @Override
    public final String instanceDescription() {
        return LogBuilder.instanceDescription(this, formname);
    }

    /**
     * Add a collection of fields for display on this form
     *
     * @param fieldsdef the collection of fields
     */
    public final void addFieldsdef(FieldsDef fieldsdef) {
        GridBagPanel fdp = new GridBagPanel();
        if (fieldsdef != null) {
            LogBuilder.writeLog("nbpcglibrary.form", this, "addFieldsdef", fieldsdef);
            fieldsdefs.add(fieldsdef);
            fieldsdef.getFields().stream().forEach((field) -> {
                fdp.addRow(field);
            });
            add(fdp);
//            add(new Table(new TestTableDef()));
        }
    }
//
//    private class TestTableDef extends TableDef {
//
//        private final List<EditableFieldList> rows;
//
//        public TestTableDef() {
//            super("test table", null);
//            rows = new ArrayList<>();
//            // initialise the rows to be displayed
//            rows.add(getRow(1));
//            rows.add(getRow(2));
//            rows.add(getRow(3));
//            rows.add(getRow(4));
//            rows.add(getRow(5));
//        }
//
//        @Override
//        public int getTableWidth() {
//            return 3 * 2;
//        }
//
//        @Override
//        public FieldList getColumnHeadings() {
//            FieldList c = new FieldList();
//            c.add(FieldBuilder.stringType().label("Description").columnlabelField());
//            c.add(FieldBuilder.stringType().label("Code").columnlabelField());
//            c.add(FieldBuilder.stringType().label("Type").columnlabelField());
//            return c;
//        }
//
//        @Override
//        public List<EditableFieldList> getRows() {
//            return rows;
//        }
//
//        private EditableFieldList getRow(int i) {
//            EditableFieldList r = new EditableFieldList();
//            r.add(FieldBuilder.stringType().fieldsize(35).min(1).max(100).initialvalue("This is the description").textField());
//            r.add(FieldBuilder.integerType().fieldsize(10).min(1).max(1000).initialvalue(i).integerField());
//            r.add(FieldBuilder.stringType().initialvalue("A").choices(Arrays.asList(new String[]{"A", "B", "C"})).choiceField());
//            return r;
//        }
//
//        @Override
//        public void createNewRow() {
//            EditableFieldList r = new EditableFieldList();
//            r.add(FieldBuilder.stringType().fieldsize(35).min(1).max(100).initialvalue("").textField());
//            r.add(FieldBuilder.integerType().fieldsize(10).min(1).max(1000).initialvalue(0).integerField());
//            r.add(FieldBuilder.stringType().initialvalue("").choices(Arrays.asList(new String[]{"A", "B", "C"})).choiceField());
//            rows.add(r);
//        }
//
//        @Override
//        public void createCopyRows(List<Integer> rowindices) {
//            rowindices.stream().sorted().forEach((index) -> {
//                EditableFieldList from = rows.get(index);
//                EditableFieldList r = new EditableFieldList();
//                r.add(FieldBuilder.stringType().fieldsize(35).min(1).max(100).initialvalue((String) from.get(0).get()).textField());
//                r.add(FieldBuilder.integerType().fieldsize(10).min(1).max(1000).initialvalue((Integer) from.get(1).get()).integerField());
//                r.add(FieldBuilder.stringType().initialvalue((String) from.get(2).get()).choices(Arrays.asList(new String[]{"A", "B", "C"})).choiceField());
//                rows.add(r);
//            });
//        }
//
//        @Override
//        public void deleteRows(List<Integer> rowindices) {
//            rowindices.stream().sorted(Comparator.reverseOrder()).forEach((index) -> rows.remove((int)index));
//        }
//    }

    /**
     * Add a table of fields for display on this form
     *
     * @param tabledef the table definition
     */
    public final void addTableDef(TableDef tabledef) {
        tabledefs.add(tabledef);
        add(new Table(tabledef));
    }

    /**
     * Add cancel Listener.
     *
     * @param listener the listener which is fired on cancel
     */
    public void addCancelListener(Listener<SimpleEventParams> listener) {
        cancelEvent.addListener(listener);
    }

    /**
     * Do the form save actions: save the field values to backingObject; check
     * the form rules are ok. If ok do the save
     *
     * @return save result code
     */
    public FormSaveResult save() {
        try {
            boolean ok = true;
            LogBuilder.writeLog("nbpcglibrary.form", this, "test");
            for (FieldsDef f : fieldsdefs) {
                f.updateAllSourcesFromFields();
                if (!f.checkRules()) {
                    ok = false;
                }
            }
            if (!ok) {
                return SAVEVALIDATIONFAIL;
            }
            for (TableDef t : tabledefs) {
                t.updateAllSourcesFromFields();
                if (!t.checkRules()) {
                    ok = false;
                }
            }
            if (!ok) {
                return SAVEVALIDATIONFAIL;
            }
            LogBuilder.writeLog("nbpcglibrary.form", this, "save");
            for (FieldsDef f : fieldsdefs) {
                if (!f.save()) {
                    ok = false;
                }
            }
            if (!ok) {
                return SAVEFAIL;
            }
            for (TableDef t : tabledefs) {
                if (!t.save()) {
                    ok = false;
                }
            }
            return ok ? SAVESUCCESS : SAVEFAIL;
        } catch (IOException ex) {
            return SAVEFAIL;
        }
    }

    protected List<String> getParameters() {
        List<String> parameters = new ArrayList<>();
        fieldsdefs.stream().forEach((f) -> {
            String[] params = f.getParameters();
            if (params != null) {
                parameters.addAll(Arrays.asList(params));
            }
        });
        tabledefs.stream().forEach((t) -> {
            String[] params = t.getParameters();
            if (params != null) {
                parameters.addAll(Arrays.asList(params));
            }
        });
        return parameters;
    }

    /**
     * Cancel action
     */
    public void cancel() {
        LogBuilder.writeLog("nbpcglibrary.form", this, "cancel");
        cancelEvent.fire(new SimpleEventParams());
    }

    /**
     * Set the values of fields in the form.
     */
    public void updateAllFieldsFromSource() {
        LogBuilder.writeLog("nbpcglibrary.form", this, "updateAllFieldsFromSource");
        fieldsdefs.stream().forEach((f) -> {
            f.updateAllFieldsFromSource();
        });
        tabledefs.stream().forEach((t) -> {
            t.updateAllFieldsFromSource();
        });
    }

    /**
     * Check if all rules in the form (fieldsdef and field levels) are valid.
     *
     * @return true if all rules are valid
     */
    public boolean checkRules() {
        boolean valid = true;
        for (FieldsDef f : fieldsdefs) {
            if (!f.checkRules()) {
                valid = false;
            }
        }
        for (TableDef t : tabledefs) {
            if (!t.checkRules()) {
                valid = false;
            }
        }
        LogBuilder.writeExitingLog("nbpcglibrary.form", this, "checkRules", valid);
        return valid;
    }
}
