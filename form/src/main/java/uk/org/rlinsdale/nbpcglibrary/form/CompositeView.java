/*
 * Copyright (C) 2014-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * A Composite view - consists of a set of views which are presented as
 * vertically stacked objects
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
