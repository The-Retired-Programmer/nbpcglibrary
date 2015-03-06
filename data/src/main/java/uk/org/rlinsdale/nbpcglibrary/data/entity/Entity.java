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
package uk.org.rlinsdale.nbpcglibrary.data.entity;

import uk.org.rlinsdale.nbpcglibrary.common.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 * Abstract Entity Class - delivering all basic entity functionality.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class Entity extends Rules implements HasInstanceDescription {

    /**
     * Constructor.
     *
     * @param entityname the name of the entity class (for reporting purposes)
     */
    public Entity(String entityname) {
        super();
    }
    
    /**
     * add a rule to the entity ruleset and also to the defined field ruleset
     * @param rules the field ruleset
     * @param rule the rule to add
     */
    protected void addRule(Rules rules, Rule rule) {
        addRule(rule);
        rules.addRule(rule);
    }

    /**
     * Cancel any changes in progress and restore state as at last save state.
     */
    public void cancelEdit() {
        _restoreState();
    }

    /**
     * Restore entity state.
     */
    abstract protected void _restoreState();

    /**
     * get the string which will be used to display the name for the entity
     *
     * @return the name string
     */
    public abstract String getDisplayName();
}
