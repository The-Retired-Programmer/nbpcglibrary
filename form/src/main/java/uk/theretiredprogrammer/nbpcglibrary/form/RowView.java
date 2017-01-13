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
package uk.theretiredprogrammer.nbpcglibrary.form;

import java.util.List;
import javax.swing.JComponent;

/**
 * Class representing a Row View
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class RowView implements PaneView<FieldViewAPI> {

    private final TableView parentview;

    /**
     * Constructor
     *
     * @param parentview the table view into which this row is to be inserted
     */
    public RowView(TableView parentview) {
        this.parentview = parentview;
    }

    @Override
    public void insertChildViews(List<FieldViewAPI> childviews) {
        parentview.insertChildViews(childviews);
    }

    @Override
    public JComponent getViewComponent() {
        return parentview.getViewComponent(); // not sure if this is correct as I dont think it will ever be called!!
    }
}
