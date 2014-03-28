/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.formsupportlib;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JTextArea;
import linsdale.nbpcg.annotations.RegisterLog;
import linsdale.nbpcg.supportlib.*;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
@RegisterLog("linsdale.nbpcg.formsupportlib")
public class Form extends GridBagPanel {

    static final int SAVESUCCESS = 1;
    static final int SAVEVALIDATIONFAIL = 2;
    static final int SAVEFAIL = 3;
    private JTextArea failuremessages;
    private String formname;
    private List<FieldsDef> fieldsdefs;
    private final FormFieldChangeListener formfieldchangelistener = new FormFieldChangeListener();
    private Rules additionalRules;

    public Form(String formname) {
        Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {0}: create", formname);
        fieldsdefs = new ArrayList<>();
        this.formname = formname;
    }

    public Form(String formname, FieldsDef fieldsdef) {
        this(formname);
        addFieldsdef(fieldsdef);
        finaliseForm();
    }

    public final void addFieldsdef(FieldsDef fieldsdef) {
        if (fieldsdef != null) {
            Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {1}: add {0} fields", new Object[]{fieldsdef.getName(), formname});
            fieldsdefs.add(fieldsdef);
            for (BaseField field : fieldsdef.getFields()) {
                addRow(field.getComponents());
                if (field instanceof EditableField) {
                    EditableField editablefield = (EditableField) field;
                    if (!editablefield.hasListener()) {
                        editablefield.addListener(formfieldchangelistener);
                    }
                }   
            }
        }
    }

    public void setAdditionalRules(Rules additionalRules) {
        this.additionalRules = additionalRules;
    }

    public final void finaliseForm() {
        finaliseForm(50);
    }

    public final void finaliseForm(int msgwidth) {
        Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {0}: finalise", formname);
        failuremessages = new JTextArea(3, msgwidth);
        failuremessages.setForeground(Color.red);
        failuremessages.setEditable(false);
        failuremessages.setFocusable(false);
        addSpannedRow(failuremessages, null);
    }
    
    public void presave() {
        for (FieldsDef f : fieldsdefs) {
            f.presave();
        }
    }

    public int save() {
        presave();
        if (checkRules()) {
            boolean ok = true;
            Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {0}: save fields", formname);
            failuremessages.setText("");
            for (FieldsDef f : fieldsdefs) {
                if (!f.save()) {
                    ok = false;
                }
            }
            return ok ? SAVESUCCESS : SAVEFAIL;
        } else {
            writeAllFailureMessages();
            return SAVEVALIDATIONFAIL;
        }
    }

    public void reset() {
        Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {0}: reset fields", formname);
        failuremessages.setText("");
        for (FieldsDef f : fieldsdefs) {
            f.reset();
        }
    }

    public void set() {
        Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {0}: set fields", formname);
        failuremessages.setText("");
        for (FieldsDef f : fieldsdefs) {
            f.set();
        }
    }

    public boolean checkRules() {
        boolean valid = true;
        for (FieldsDef f : fieldsdefs) {
            if (!f.checkRules()) {
                valid = false;
            }
        }
        if (additionalRules != null) {
            if (!additionalRules.checkRules()) {
                valid = false;
            }
        }
        Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {1}: check rules {0}", new Object[] {valid ? "valid" : "invalid", formname});
        return valid;
    }

    public void writeAllFailureMessages() {
        StringBuilder sb = new StringBuilder();
        addFailureMessages(sb);
        if (additionalRules != null) {
            additionalRules.addFailureMessages(sb);
        }
        String t = sb.toString();
        Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Form {1}: write all failure messages: \"{0}\"", new Object[] {t.replace("\n", "; "), formname});
        failuremessages.setText(t);
    }

    private void addFailureMessages(StringBuilder msg) {
        for (FieldsDef f : fieldsdefs) {
            f.addFailureMessages(msg);
        }
    }

    private class FormFieldChangeListener extends Listener<FormFieldChangeListenerParams> {

        public FormFieldChangeListener() {
            super("Form/field");
        }

        @Override
        public void action(FormFieldChangeListenerParams p) {
            writeAllFailureMessages();
        }
    }
}