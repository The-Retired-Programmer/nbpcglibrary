/*
 * Copyright (C) 2015-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.api.DateOnly;
import uk.org.rlinsdale.nbpcglibrary.api.Timestamp;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;

/**
 * The Worker class for Field Builder.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the value type for the field
 * @param <S> the field source type
 * @param <M> the min/max type
 */
public class FieldBuilder<T, S extends FieldSource, M> {

    private String fb_label = null;
    private int fb_size = Integer.MIN_VALUE;
    private T fb_initialValue = null;
    private M fb_min = null;
    private M fb_max = null;
    private boolean fb_nullselectionallowed = false;
    private List<T> fb_choices = null;
    private String fb_fieldtext = null;
    private Callback fb_callback = null;
    private ItemListener fb_listener = null;
    private boolean fb_noerrormarker = false;

    private S fb_source = null;

    /**
     * the Constructor
     */
    protected FieldBuilder() {
    }

    /**
     * Add a label to the field
     * @param label the label text
     * @return the builder object
     */
    public FieldBuilder<T, S, M> label(String label) {
        fb_label = label;
        return this;
    }

    /**
     * Define the field size of the field
     * @param size the field size
     * @return the builder object
     */
    public FieldBuilder<T, S, M> fieldsize(int size) {
        fb_size = size;
        return this;
    }

    /**
     * Define the initial value of the field
     * @param initialValue the initial value
     * @return the builder object
     */
    public FieldBuilder<T, S, M> initialvalue(T initialValue) {
        fb_initialValue = initialValue;
        return this;
    }

    /**
     * Define the maximum value for the field
     * @param max the maximum value
     * @return the builder object
     */
    public FieldBuilder<T, S, M> max(M max) {
        fb_max = max;
        return this;
    }

    /**
     * Define the minimum value for the field
     * @param min the minimum value
     * @return  the builder object
     */
    public FieldBuilder<T, S, M> min(M min) {
        fb_min = min;
        return this;
    }

    /**
     * Allow a null selection as legal
     * @return  the builder object
     */
    public FieldBuilder<T, S, M> nullselectionallowed() {
        fb_nullselectionallowed = true;
        return this;
    }

    /**
     * Define the set of choices to be displayed in a choice field
     * @param choices the list of choices
     * @return the builder object
     */
    public FieldBuilder<T, S, M> choices(List<T> choices) {
        fb_choices = choices;
        return this;
    }

    /**
     * Define the set of choices to be displayed in a choice field
     * @param choices the array of choices
     * @return the builder object
     */
    public FieldBuilder<T, S, M> choices(T[] choices) {
        fb_choices = Arrays.asList(choices);
        return this;
    }

    /**
     * Define the text in a filler field
     * @param fieldtext the text
     * @return the builder object
     */
    public FieldBuilder<T, S, M> fieldtext(String fieldtext) {
        fb_fieldtext = fieldtext;
        return this;
    }

    /**
     * Define the callback to be made on field update.
     * @param callback the callback
     * @return the builder object
     */
    public FieldBuilder<T, S, M> onUpdate(Callback callback) {
        fb_callback = callback;
        return this;
    }

    /**
     * Define the source object to be associated with this field.
     * @param source the source object
     * @return the builder object
     */
    public FieldBuilder<T, S, M> fieldsource(S source) {
        fb_source = source;
        return this;
    }

    /**
     * Define the item listener that is associated with this field
     * @param listener the item listener
     * @return the builder object
     */
    public FieldBuilder<T, S, M> itemlistener(ItemListener listener) {
        fb_listener = listener;
        return this;
    }

    /**
     * Disable the association of an errormarker with this field 
     * @return the builder object
     */
    public FieldBuilder<T, S, M> noerrormarker() {
        fb_noerrormarker = true;
        return this;
    }

    /**
     * Execute the builder to create a fillerField
     * @return the generated field
     */
    public Field fillerField() {
        return new FillerField(fb_fieldtext);
    }

    /**
     * Execute the builder to create a readonlyTextField
     * @return the generated field
     */
    public  Field readonlyTextField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new TextReadonlyField(fb_label, fb_size, (String) fb_initialValue);
    }

    /**
     * Execute the builder to create a column label field
     * @return the generated field
     */
    public  Field columnlabelField() {
        return new ColumnLabelField(fb_label, !fb_noerrormarker);
    }

    /**
     * Execute the builder to create a text field
     * @return the generated field
     */
    public Field textField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new TextField(
                fb_source == null ? new FieldSource<String>(): fb_source,
                fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a password field
     * @return the generated field
     */
    public Field passwordField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new PasswordField(
                fb_source == null ? new FieldSource<String>(): fb_source,
                fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create an integer field
     * @return the generated field
     */
    public Field integerField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new IntegerField(
                fb_source == null ? new FieldSource<Integer>(): fb_source,
                fb_size, (Integer) fb_min, (Integer) fb_max, (Integer) fb_initialValue, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a long field
     * @return the generated field
     */
    public Field longField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new LongField(
                fb_source == null ? new FieldSource<Long>(): fb_source,
                fb_size, (Long) fb_min, (Long) fb_max, (Long) fb_initialValue, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a date field
     * @return the generated field
     */
    public Field dateField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new DateField(
                fb_source == null ? new FieldSource<DateOnly>(): fb_source,
                fb_size, (DateOnly) fb_min, (DateOnly) fb_max, (DateOnly) fb_initialValue, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a decimal field
     * @return the generated field
     */
    public Field decimalField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new DecimalField(
                fb_source == null ? new FieldSource<BigDecimal>(): fb_source,
                fb_size, (BigDecimal) fb_min, (BigDecimal) fb_max, (BigDecimal) fb_initialValue, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }
    
    /**
     * Execute the builder to create a datetime field
     * @return the generated field
     */
    public Field datetimeField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new DatetimeField(
                fb_source == null ? new FieldSource<Timestamp>(): fb_source,
                fb_size, (Timestamp) fb_min, (Timestamp) fb_max, (Timestamp) fb_initialValue, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a checkbox field
     * @return the generated field
     */
    public Field checkboxField() {
        @SuppressWarnings("Convert2Diamond")
        Field tf = new CheckboxField(
                fb_source == null ? new FieldSource<Boolean>(): fb_source,
                (Boolean) fb_initialValue, fb_listener, fb_callback);
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a choice field
     * @return the generated field
     */
    public Field choiceField() {
        @SuppressWarnings("Convert2Diamond")
        Field tf = new ChoiceField(
                fb_source == null ? new ChoiceFieldSource<String>(): (ChoiceFieldSource) fb_source,
                fb_nullselectionallowed, fb_initialValue, fb_choices, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create an entity choice field
     * @return the generated field
     */
    public Field entityChoiceField() {
        @SuppressWarnings("Convert2Diamond")
        Field tf = new EntityChoiceField(
                fb_source == null ? new ChoiceFieldSource<Entity>(): (ChoiceFieldSource) fb_source,
                fb_nullselectionallowed, (Entity) fb_initialValue, fb_choices, fb_callback);
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a folder field
     * @return the generated field
     */
    public Field folderField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 50;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new TextField(
                fb_source == null ? new FieldSource<String>(): fb_source,
                fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new FolderSelectionDecorator(tf);
        }
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Execute the builder to create a file field
     * @return the generated field
     */
    public Field fileField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 50;
        }
        @SuppressWarnings("Convert2Diamond")
        Field tf = new TextField(
                fb_source == null ? new FieldSource<String>(): fb_source,
                fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new FolderSelectionDecorator(tf);
        }
        if (!fb_noerrormarker) {
            tf = new ErrorMarkerDecorator(tf);
        }
        if (fb_label != null) {
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    /**
     * Create a builder object which manages a String data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<String, FieldSource<String>, Integer> stringType() {
        return new FieldBuilder<String, FieldSource<String>, Integer>();
    }

    /**
     * Create a builder object which manages an integer data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Integer, FieldSource<Integer>, Integer> integerType() {
        return new FieldBuilder<Integer, FieldSource<Integer>, Integer>();
    }

    /**
     * Create a builder object which manages a long data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Long, FieldSource<Long>, Long> longType() {
        return new FieldBuilder<Long, FieldSource<Long>, Long>();
    }

    /**
     * Create a builder object which manages a boolean data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Boolean, FieldSource<Boolean>, String> booleanType() {
        return new FieldBuilder<Boolean, FieldSource<Boolean>, String>();
    }

    /**
     * Create a builder object which manages a date data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<DateOnly, FieldSource<DateOnly>, DateOnly> dateType() {
        return new FieldBuilder<DateOnly, FieldSource<DateOnly>, DateOnly>();
    }

    /**
     * Create a builder object which manages a timestamp (datetime) data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Timestamp, FieldSource<Timestamp>, Timestamp> datetimeType() {
        return new FieldBuilder<Timestamp, FieldSource<Timestamp>, Timestamp>();
    }
    
    /**
     * Create a builder object which manages a decimal data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<BigDecimal, FieldSource<BigDecimal>, BigDecimal> decimalType() {
        return new FieldBuilder<BigDecimal, FieldSource<BigDecimal>, BigDecimal>();
    }

    /**
     * Create a builder object which manages an entity data type field.
     * @return the builder object
     */
    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Entity, ChoiceFieldSource<Entity>, String> entityType() {
        return new FieldBuilder<Entity, ChoiceFieldSource<Entity>, String>();
    }
}
