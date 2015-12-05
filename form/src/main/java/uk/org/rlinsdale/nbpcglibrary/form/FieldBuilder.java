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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.api.DateOnly;
import uk.org.rlinsdale.nbpcglibrary.api.Timestamp;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.SetChangeEventParams;

/**
 * The Worker class for Field Builder.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the value type for the field
 * @param <M> the min/max type
 */
public class FieldBuilder<T, M> {

    private String fb_label = "";
    private int fb_size = Integer.MIN_VALUE;
    private T fb_initialValue = null;
    private M fb_min = null;
    private M fb_max = null;
    private boolean fb_nullselectionallowed = false;
    private List<T> fb_choices = null;
    private String fb_fieldtext = null;
    private String fb_additionalfieldtext = null;
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

    public FieldBuilder<T, M> additionalfieldtext(String additionalfieldtext) {
        fb_additionalfieldtext = additionalfieldtext;
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

    public FillerField fillerField() {
        return new FillerField(fb_label, fb_fieldtext, fb_additionalfieldtext);
    }

    public TextReadonlyField readonlyTextField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new BasicReadOnlyTextField(fb_label, fb_size, (String) fb_initialValue);
    }

    public TextField textField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new BasicTextField((FieldSource<String>) fb_source, fb_label, fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
    }

    public PasswordField passwordField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new BasicPasswordField((FieldSource<String>) fb_source, fb_label, fb_size, (Integer) fb_min, (Integer) fb_max, (String) fb_initialValue, fb_callback);
    }

    public IntegerField integerField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new BasicIntegerField((FieldSource<Integer>) fb_source, fb_label, fb_size, (Integer) fb_min, (Integer) fb_max, (Integer) fb_initialValue, fb_callback);
    }

    public LongField longField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new BasicLongField((FieldSource<Long>) fb_source, fb_label, fb_size, (Long) fb_min, (Long) fb_max, (Long) fb_initialValue, fb_callback);
    }

    public DateField dateField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new BasicDateField((FieldSource<DateOnly>) fb_source, fb_label, fb_size, (DateOnly) fb_min, (DateOnly) fb_max, (DateOnly) fb_initialValue, fb_callback);
    }

    public DatetimeField datetimeField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 20;
        }
        return new BasicDatetimeField((FieldSource<Timestamp>) fb_source, fb_label, fb_size, (Timestamp) fb_min, (Timestamp) fb_max, (Timestamp) fb_initialValue, fb_callback);
    }

    public CheckboxField checkboxField() {
        return new BasicCheckboxField((FieldSource<Boolean>) fb_source, fb_label, (Boolean) fb_initialValue, fb_callback);
    }

    public ChoiceField choiceField() {
        return new BasicChoiceField(fb_source, fb_label, fb_nullselectionallowed, fb_initialValue, fb_choices, fb_callback);
    }

    public EntityChoiceField entityChoiceField() {
        return new BasicEntityChoiceField(fb_source, fb_label, fb_nullselectionallowed, (Entity) fb_initialValue, fb_choices, fb_callback);
    }

    public FolderField folderField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 50;
        }
        return new BasicFolderField((FieldSource<String>) fb_source, fb_label, fb_size, (String) fb_initialValue, fb_callback);
    }

    public FileField fileField() {
        if (fb_size == Integer.MIN_VALUE) {
            fb_size = 50;
        }
        return new BasicFileField((FieldSource<String>) fb_source, fb_label, fb_size, (String) fb_initialValue, fb_callback);
    }

    private class BasicReadOnlyTextField extends TextReadonlyField {

        private final String value; // data source

        protected BasicReadOnlyTextField(String label, int size, String initialValue) {
            super(label, size);
            value = initialValue;
            updateFieldFromSource();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected String getSourceValue() {
            return value;
        }
    }

    private class BasicTextField extends TextField {

        private final Callback callback;
        private final String initialValue;
        private final FieldSource<String> source;

        protected BasicTextField(FieldSource<String> source, String label, int size, Integer min, Integer max, String initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.callback = callback;
            this.initialValue = initialValue;
            if (min != null) {
                source.getRules().addRule(new MinRule(min));
            }
            if (max != null) {
                source.getRules().addRule(new MaxRule(max));
            }
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(String value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected String getSourceValue() {
            return source.get();
        }

        @Override
        public String get() {
            return source.get();
        }

        @Override
        public void set(String value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class MinRule extends Rule {

            private final int min;

            protected MinRule(int min) {
                super("Too short - minimum length is " + min);
                this.min = min;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().length() >= min;
            }
        }

        private class MaxRule extends Rule {

            private final int max;

            protected MaxRule(int max) {
                super("Too long - maximum length is " + max);
                this.max = max;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().length() <= max;
            }
        }
    }

    private class BasicPasswordField extends PasswordField {

        private final Callback callback;
        private final String initialValue;
        private final FieldSource<String> source;

        protected BasicPasswordField(FieldSource<String> source, String label, int size, Integer min, Integer max, String initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.callback = callback;
            this.initialValue = initialValue;
            if (min != null) {
                source.getRules().addRule(new MinRule(min));
            }
            if (max != null) {
                source.getRules().addRule(new MaxRule(max));
            }
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(String value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected String getSourceValue() {
            return source.get();
        }

        @Override
        public String get() {
            return source.get();
        }

        @Override
        public void set(String value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class MinRule extends Rule {

            private final int min;

            protected MinRule(int min) {
                super("Too short - minimum length is " + min);
                this.min = min;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().length() >= min;
            }
        }

        private class MaxRule extends Rule {

            private final int max;

            protected MaxRule(int max) {
                super("Too long - maximum length is " + max);
                this.max = max;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().length() <= max;
            }
        }
    }

    private class BasicIntegerField extends IntegerField {

        private final Callback callback;
        private final Integer initialValue;
        private final FieldSource<Integer> source;

        protected BasicIntegerField(FieldSource<Integer> source, String label, int size, Integer min, Integer max, Integer initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.initialValue = initialValue;
            this.callback = callback;
            if (min != null) {
                source.getRules().addRule(new MinRule(min));
            }
            if (max != null) {
                source.getRules().addRule(new MaxRule(max));
            }
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(Integer value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected Integer getSourceValue() {
            return source.get();
        }

        @Override
        public Integer get() {
            return source.get();
        }

        @Override
        public void set(Integer value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class MinRule extends Rule {

            private final int min;

            protected MinRule(int min) {
                super("Too small - minimum value is " + min);
                this.min = min;
            }

            @Override
            public boolean ruleCheck() {
                return source.get() >= min;
            }
        }

        private class MaxRule extends Rule {

            private final int max;

            protected MaxRule(int max) {
                super("Too big - maximum value is " + max);
                this.max = max;
            }

            @Override
            public boolean ruleCheck() {
                return source.get() <= max;
            }
        }
    }

    private class BasicLongField extends LongField {

        private final Callback callback;
        private final Long initialValue;
        private final FieldSource<Long> source;

        protected BasicLongField(FieldSource<Long> source, String label, int size, Long min, Long max, Long initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.initialValue = initialValue;
            this.callback = callback;
            if (min != null) {
                source.getRules().addRule(new MinRule(min));
            }
            if (max != null) {
                source.getRules().addRule(new MaxRule(max));
            }
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(Long value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected Long getSourceValue() {
            return source.get();
        }

        @Override
        public Long get() {
            return source.get();
        }

        @Override
        public void set(Long value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class MinRule extends Rule {

            private final long min;

            protected MinRule(long min) {
                super("Too small - minimum value is " + min);
                this.min = min;
            }

            @Override
            public boolean ruleCheck() {
                return source.get() >= min;
            }
        }

        private class MaxRule extends Rule {

            private final long max;

            protected MaxRule(long max) {
                super("Too big - maximum value is " + max);
                this.max = max;
            }

            @Override
            public boolean ruleCheck() {
                return source.get() <= max;
            }
        }
    }

    private class BasicDateField extends DateField {

        private final Callback callback;
        private final DateOnly initialValue;
        private final FieldSource<DateOnly> source;

        protected BasicDateField(FieldSource<DateOnly> source, String label, int size, DateOnly min, DateOnly max, DateOnly initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.initialValue = initialValue;
            this.callback = callback;
            if (max != null) {
                source.getRules().addRule(new MaxRule(max));
            }
            if (min != null) {
                source.getRules().addRule(new MinRule(min));
            }
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(DateOnly value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected DateOnly getSourceValue() {
            return source.get();
        }

        @Override
        public DateOnly get() {
            return source.get();
        }

        @Override
        public void set(DateOnly value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class MaxRule extends Rule {

            private final DateOnly max;

            protected MaxRule(DateOnly max) {
                super("Too late - must be previous/equal to " + max);
                this.max = max;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().compareTo(max) != 1;
            }
        }

        private class MinRule extends Rule {

            private final DateOnly min;

            protected MinRule(DateOnly min) {
                super("Too early - must be later/equal to " + min);
                this.min = min;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().compareTo(min) != -1;
            }
        }
    }

    private class BasicDatetimeField extends DatetimeField {

        private final Callback callback;
        private final Timestamp initialValue;
        private final FieldSource<Timestamp> source;

        protected BasicDatetimeField(FieldSource<Timestamp> source, String label, int size, Timestamp min, Timestamp max, Timestamp initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.initialValue = initialValue;
            this.callback = callback;
            if (max != null) {
                source.getRules().addRule(new MaxRule(max));
            }
            if (min != null) {
                source.getRules().addRule(new MinRule(min));
            }
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(Timestamp value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected Timestamp getSourceValue() {
            return source.get();
        }

        @Override
        public Timestamp get() {
            return source.get();
        }

        @Override
        public void set(Timestamp value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class MaxRule extends Rule {

            private final Timestamp max;

            protected MaxRule(Timestamp max) {
                super("Too late - must be previous/equal to " + max);
                this.max = max;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().compareTo(max) != 1;
            }
        }

        private class MinRule extends Rule {

            private final Timestamp min;

            protected MinRule(Timestamp min) {
                super("Too early - must be later/equal to " + min);
                this.min = min;
            }

            @Override
            public boolean ruleCheck() {
                return source.get().compareTo(min) != -1;
            }
        }
    }

    private class BasicCheckboxField extends CheckboxField {

        private final Callback callback;
        private final Boolean initialValue;
        private final FieldSource<Boolean> source;

        protected BasicCheckboxField(FieldSource<Boolean> source, String label, Boolean initialValue, Callback callback) {
            super(label);
            this.source = source;
            this.callback = callback;
            this.initialValue = initialValue;
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(Boolean value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected Boolean getSourceValue() {
            return source.get();
        }

        @Override
        public Boolean get() {
            return source.get();
        }

        @Override
        public void set(Boolean value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

    }

    private class BasicChoiceField extends ChoiceField<T> {

        private final Callback callback;
        private final T initialValue;
        private final FieldSource<T> source;

        protected BasicChoiceField(FieldSource<T> source, String label, boolean nullSelectionAllowed, T initialValue, List<T> choices, Callback callback) {
            super(label, nullSelectionAllowed);
            this.source = source;
            this.callback = callback;
            this.initialValue = initialValue;
            source.setChoices(choices);
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(T value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected T getSourceValue() {
            return source.get();
        }

        @Override
        protected List<T> getSourceChoices() {
            return source.getChoices();
        }

        @Override
        public T get() {
            return source.get();
        }

        @Override
        public void set(T value) {
            source.set(value);
            updateChoicesFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateChoicesFromSource();
            }
        }
    }

    private class BasicEntityChoiceField<T extends Entity> extends EntityChoiceField<T> {

        private final Callback callback;
        private final T initialValue;
        private final FieldSource<T> source;

        protected BasicEntityChoiceField(FieldSource<T> source, String label, boolean nullSelectionAllowed, T initialValue, List<T> choices, Callback callback) {
            super(label, nullSelectionAllowed);
            this.source = source;
            this.callback = callback;
            this.initialValue = initialValue;
            source.setChoices(choices == null ? source.getChoices() : choices);
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(T value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected T getSourceValue() {
            return source.get();
        }

        @Override
        protected List<T> getSourceChoices() {
            return source.getChoices();
        }

        @Override
        public T get() {
            return source.get();
        }

        @Override
        public void set(T value) {
            source.set(value);
            updateChoicesFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateChoicesFromSource();
            }
        }

        @Override
        public void addCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException {
            source.addCollectionListeners(listener);
        }

        @Override
        public void removeCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException {
            source.removeCollectionListeners(listener);
        }
    }

    private class BasicFolderField extends FolderField {

        private final Callback callback;
        private final String initialValue;
        private final FieldSource<String> source;

        protected BasicFolderField(FieldSource<String> source, String label, int size, String initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.callback = callback;
            this.initialValue = initialValue;
            source.getRules().addRule(new FolderExistsRule());
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(String value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected String getSourceValue() {
            return source.get();
        }

        @Override
        public String get() {
            return source.get();
        }

        @Override
        public void set(String value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class FolderExistsRule extends Rule {

            protected FolderExistsRule() {
                super("Folder does not exist or is a file");
            }

            @Override
            public boolean ruleCheck() {
                File folder = new File(source.get());
                return folder.exists() && folder.isDirectory();
            }
        }
    }

    private class BasicFileField extends FileField {

        private final Callback callback;
        private final String initialValue;
        private final FieldSource<String> source;

        protected BasicFileField(FieldSource<String> source, String label, int size, String initialValue, Callback callback) {
            super(label, size);
            this.source = source;
            this.callback = callback;
            this.initialValue = initialValue;
            source.getRules().addRule(new FileExistsRule());
            reset();
        }

        //methods defined to implement a simple text field.
        // override to implement alternative data source
        @Override
        protected void setSourceValue(String value) {
            source.set(value);
            if (callback != null) {
                callback.call();
            }
        }

        @Override
        protected boolean sourceCheckRules() {
            return source.getRules().checkRules();
        }

        @Override
        protected String getSourceErrorMessages() {
            return source.getRules().getErrorMessages();
        }

        @Override
        protected String getSourceValue() {
            return source.get();
        }

        @Override
        public String get() {
            return source.get();
        }

        @Override
        public void set(String value) {
            source.set(value);
            updateFieldFromSource();
        }

        @Override
        public void reset() {
            if (initialValue != null) {
                set(initialValue);
            } else {
                updateFieldFromSource();
            }
        }

        private class FileExistsRule extends Rule {

            protected FileExistsRule() {
                super("File does not exist or is a folder");
            }

            @Override
            public boolean ruleCheck() {
                File file = new File(source.get());
                return file.exists() && file.isFile();
            }
        }
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
