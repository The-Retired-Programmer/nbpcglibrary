/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
 * @param <M> the min/max type
 */
public class FieldBuilder<T, M> {

    private String fb_label = null;
    private int fb_size = Integer.MIN_VALUE;
    private T fb_initialValue = null;
    private M fb_min = null;
    private M fb_max = null;
    private boolean fb_nullselectionallowed = false;
    private List<T> fb_choices = null;
    private String fb_fieldtext = null;
    private Callback fb_callback = null;

    private FieldSource<T> fb_source = new FieldSource<>();

    protected FieldBuilder() {
    }

    public FieldBuilder<T, M> label(String label) {
        fb_label = label;
        return this;
    }

    public FieldBuilder<T, M> fieldsize(int size) {
        fb_size = size;
        return this;
    }

    public FieldBuilder<T, M> initialvalue(T initialValue) {
        fb_initialValue = initialValue;
        return this;
    }

    public FieldBuilder<T, M> max(M max) {
        fb_max = max;
        return this;
    }

    public FieldBuilder<T, M> min(M min) {
        fb_min = min;
        return this;
    }

    public FieldBuilder<T, M> nullselectionallowed() {
        fb_nullselectionallowed = true;
        return this;
    }

    public FieldBuilder<T, M> choices(List<T> choices) {
        fb_choices = choices;
        return this;
    }

    public FieldBuilder<T, M> choices(T[] choices) {
        fb_choices = Arrays.asList(choices);
        return this;
    }

    public FieldBuilder<T, M> fieldtext(String fieldtext) {
        fb_fieldtext = fieldtext;
        return this;
    }

    public FieldBuilder<T, M> onUpdate(Callback callback) {
        fb_callback = callback;
        return this;
    }

    public FieldBuilder<T, M> fieldsource(FieldSource<T> source) {
        fb_source = source;
        return this;
    }

    public Field fillerField() {
        return new FillerField(fb_fieldtext);
    }

    public Field readonlyTextField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new TextReadonlyField(fb_label, fb_size, (String) fb_initialValue);
    }

    public EditableField textField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        EditableField tf = new TextField((FieldSource<String>) fb_source, fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField passwordField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        EditableField tf = new PasswordField((FieldSource<String>) fb_source, fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField integerField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        EditableField tf = new IntegerField((FieldSource<Integer>) fb_source, fb_size, (Integer) fb_min, (Integer) fb_max, (Integer) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField longField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        EditableField tf = new LongField((FieldSource<Long>) fb_source, fb_size, (Long) fb_min, (Long) fb_max, (Long) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField dateField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        EditableField tf = new DateField((FieldSource<DateOnly>) fb_source, fb_size, (DateOnly) fb_min, (DateOnly) fb_max, (DateOnly) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField datetimeField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        EditableField tf = new DatetimeField((FieldSource<Timestamp>) fb_source, fb_size, (Timestamp) fb_min, (Timestamp) fb_max, (Timestamp) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField checkboxField() {
        EditableField tf = new CheckboxField((FieldSource<Boolean>) fb_source, (Boolean) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField choiceField() {
        EditableField tf = new ChoiceField(fb_source, fb_nullselectionallowed, fb_initialValue, fb_choices, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField entityChoiceField() {
        EditableField tf = new EntityChoiceField(fb_source, fb_nullselectionallowed, (Entity) fb_initialValue, fb_choices, fb_callback);
        if (fb_label != null) {
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField folderField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 50;
        }
        EditableField tf = new TextField((FieldSource<String>) fb_source, fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new FolderSelectionDecorator(tf);
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    public EditableField fileField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 50;
        }
        EditableField tf = new TextField((FieldSource<String>) fb_source, fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
        if (fb_label != null) {
            tf = new FileSelectionDecorator(tf);
            tf = new ErrorMarkerDecorator(tf);
            tf = new LabelDecorator(tf, fb_label);
        }
        return tf;
    }

    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<String, Integer> stringType() {
        return new FieldBuilder<String, Integer>();
    }

    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Integer, Integer> integerType() {
        return new FieldBuilder<Integer, Integer>();
    }

    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Long, Long> longType() {
        return new FieldBuilder<Long, Long>();
    }

    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Boolean, String> booleanType() {
        return new FieldBuilder<Boolean, String>();
    }

    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<DateOnly, DateOnly> dateType() {
        return new FieldBuilder<DateOnly, DateOnly>();
    }

    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Timestamp, Timestamp> datetimeType() {
        return new FieldBuilder<Timestamp, Timestamp>();
    }

    @SuppressWarnings("Convert2Diamond")
    public static FieldBuilder<Entity, String> entityType() {
        return new FieldBuilder<Entity, String>();
    }
}
