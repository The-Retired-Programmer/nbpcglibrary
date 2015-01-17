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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A field class to get file path information.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FileField extends TextField {

    private JButton fileButton;

    /**
     * Constructor
     *
     * @param backingObject the backing object
     * @param label field label
     * @param size size of the lastvaluesetinfield display
     */
    public FileField(EditableFieldBackingObject<String> backingObject, String label, int size) {
        super(backingObject, label, size);
        addStringMinRule(1);
        addRule(new FileExistsRule());
    }

    /**
     * Constructor
     *
     * @param backingObject the backing object
     * @param label field label
     */
    public FileField(EditableFieldBackingObject<String> backingObject, String label) {
        this(backingObject, label, 20);
    }

    @Override
    protected JComponent getAdditionalComponent() {
        return fileButton();
    }

    private JButton fileButton() {
        fileButton = new JButton();
        fileButton.setIcon(new ImageIcon(getClass().getResource("page_find.png")));
        fileButton.setToolTipText("Select File");
        fileButton.addActionListener(new FileButtonListener());
        return fileButton;
    }

    private class FileButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            final JFileChooser fc = new JFileChooser(get() + "/");
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (fc.showOpenDialog(fileButton) == JFileChooser.APPROVE_OPTION) {
                updateIfChange(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private class FileExistsRule extends Rule {

        public FileExistsRule() {
            super(getLabel() + " - file does not exist or is a folder");
        }

        @Override
        public boolean ruleCheck() {
            File file = new File(get());
            return file.exists() && file.isFile();
        }
    }
}
