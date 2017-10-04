/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.data.entity;

import com.famfamfam.www.silkicons.Icons;
import java.awt.Image;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import uk.theretiredprogrammer.nbpcglibrary.common.Rule;
import uk.theretiredprogrammer.nbpcglibrary.common.Rules;
import uk.theretiredprogrammer.nbpcglibrary.icons.SpecialIcons;

/**
 * Abstract CoreEntity Class - delivering all basic entity functionality.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class CoreEntity extends Rules {

    // temp until icons are totally moved to Node
    private final String iconname = "asterisk_orange";
    private final InstanceContent lookupcontent;
    private final Lookup lookup;

    /**
     * The date format for translating database timestamp fields to/from Date
     * objects
     */
    protected final DateFormat DATETIME_ISO8601;

    /**
     * Constructor.
     */
    public CoreEntity() {
        lookupcontent = new InstanceContent();
        lookup = new AbstractLookup(lookupcontent);
        DATETIME_ISO8601 = new SimpleDateFormat("yyyyMMddHHmmss");
        DATETIME_ISO8601.setLenient(false);
        
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
    protected final void addRule(Rules rules, Rule rule) {
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
}
