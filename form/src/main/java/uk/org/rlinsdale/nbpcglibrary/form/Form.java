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
public class Form extends GridBagPanel implements HasInstanceDescription {

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
    private final Event<SimpleEventParams> cancelEvent;

    /**
     * Constructor
     *
     * @param formname the form's name
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Form(String formname) {
        fieldsdefs = new ArrayList<>();
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
        if (fieldsdef != null) {
            LogBuilder.writeLog("nbpcglibrary.form", this, "addFieldsdef", fieldsdef);
            fieldsdefs.add(fieldsdef);
            fieldsdef.getFields().stream().forEach((field) -> {
                addRow(field.getComponents());
            });
        }
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
     * Do the form save actions: save the field values to backingObject; check the form rules are
     * ok.  If ok do the save
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
            LogBuilder.writeLog("nbpcglibrary.form", this, "save");
            for (FieldsDef f : fieldsdefs) {
                if (!f.save()) {
                    ok = false;
                }
            }
            return ok ? SAVESUCCESS : SAVEFAIL;
        } catch (IOException ex) {
            return SAVEFAIL;
        }
    }
    
    List<String> getParameters() {
        List<String> parameters = new ArrayList<>();
        fieldsdefs.stream().forEach((f) -> {
            String[] params = f.getParameters();
            if (params!= null) {
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
    }

    /**
     * Check if all rules in the form (fieldsdef and field levels) are
     * valid.
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
        LogBuilder.writeExitingLog("nbpcglibrary.form", this, "checkRules", valid);
        return valid;
    }
}
