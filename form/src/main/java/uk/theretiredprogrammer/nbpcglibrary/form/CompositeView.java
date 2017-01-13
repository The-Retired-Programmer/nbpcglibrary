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
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * A Composite view - consists of a set of views which are presented as
 * vertically stacked objects
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class CompositeView extends JPanel implements PaneView<PaneView> {

    /**
     * Constructor
     *
     */
    public CompositeView() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param borderTitle the panel title
     */
    public CompositeView(String borderTitle) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        if (borderTitle != null) {
            setBorder(new TitledBorder(borderTitle));
        }
    }

    @Override
    public void insertChildViews(List<PaneView> childviews) {
        childviews.stream().forEach(v -> add(v.getViewComponent()));
    }

    @Override
    public JComponent getViewComponent() {
        return this;
    }
}
