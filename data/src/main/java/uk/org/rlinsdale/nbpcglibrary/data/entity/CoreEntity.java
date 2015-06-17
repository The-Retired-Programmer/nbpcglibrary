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

import com.famfamfam.www.silkicons.Icons;
import java.awt.Image;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;
import uk.org.rlinsdale.nbpcglibrary.icons.SpecialIcons;

/**
 * Abstract CoreEntity Class - delivering all basic entity functionality.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class CoreEntity extends Rules implements HasInstanceDescription {

    private final String iconname;
    private final InstanceContent lookupcontent;
    private final Lookup lookup;

    /**
     * Constructor.
     *
     * @param entityname the name of the entity class (for reporting purposes)
     * @param iconname name of the icon graphic
     */
    public CoreEntity(String entityname, String iconname) {
        super();
        this.iconname = iconname;
        lookupcontent = new InstanceContent();
        lookup = new AbstractLookup(lookupcontent);
    }
    
    /**
     * Get the entity lookup
     *
     * @return the entity Lookup
     */
    public Lookup getLookup() {
        return lookup;
    }

    /**
     * Add dynamic content to entity lookup.
     *
     * @param content object to add
     */
    protected void addLookupContent(Object content) {
        lookupcontent.add(content);
    }

    /**
     * Remove dynamic content from entity lookup.
     *
     * @param content object to remove
     */
    protected void removeLookupContent(Object content) {
        lookupcontent.remove(content);
    }

    /**
     * add a rule to the entity ruleset and also to the defined field ruleset
     *
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
        entityRestoreState();
    }

    /**
     * Get the icon associated with this entity
     *
     * @return the icon
     */
    public Image getIcon() {
        return Icons.get(iconname);
    }

    /**
     * Get the node icon combined with an error marker.
     *
     * @return the image
     */
    public Image getIconWithError() {
        return addErrorToIcon(Icons.get(iconname));
    }

    /**
     * Create the node icon combined with an error marker.
     *
     * @param icon the node icon image
     * @return the node icon image combined with error marker
     */
    public Image addErrorToIcon(Image icon) {
        return ImageUtilities.mergeImages(icon, SpecialIcons.get("errormarker"), 0, 6);
    }
    
    /**
     * Restore entity state.
     */
    abstract protected void entityRestoreState();

    /**
     * get the string which will be used to display the name for the entity
     *
     * @return the name string
     */
    public abstract String getDisplayName();
}
