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

import java.util.ArrayList;
import java.util.List;
import linsdale.nbpcg.supportlib.Rule;
import linsdale.nbpcg.supportlib.Rules;

/**
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public abstract class FieldsDef {

    private final List<BaseField> fields = new ArrayList<>();
    private final String name;
    private final Rules rules = new Rules();

    public FieldsDef(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public final void add(BaseField f) {
        fields.add(f);
    }

    public final List<BaseField> getFields() {
        return fields;
    }

    public abstract void set();

    public void presave() {
    }

    public boolean save() {
        return true;
    }

    public void reset() {
    }

    public final void addRule(Rule r) {
        rules.addRule(r);
    }

    public final void addFailureMessages(StringBuilder sb) {
        for (BaseField f : fields) {
            if (f instanceof EditableField) {
                ((EditableField) f).addFailureMessages(sb);
            }
        }
        rules.addFailureMessages(sb);
    }

    public final boolean checkRules() {
        boolean valid = true;
        for (BaseField f : fields) {
            if (f instanceof EditableField) {
                valid = valid && ((EditableField) f).checkRules();
            }
        }
        return valid && rules.checkRules();
    }
}