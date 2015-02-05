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
 * A folder class to get file path information.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FolderField extends TextField {

    private JButton folderButton;

    /**
     * Constructor
     *
     * @param backingObject the backing Object
     * @param label field label
     * @param size size of the field
     */
    public FolderField(EditableFieldBackingObject<String> backingObject, String label, int size) {
        super(backingObject, label, size);
        addRule(new FolderExistsRule());
    }

    /**
     * Constructor
     *
     * @param backingObject the backing object
     * @param label field label
     */
    public FolderField(EditableFieldBackingObject<String> backingObject, String label) {
        this(backingObject, label, 50);
    }

    @Override
    protected JComponent getAdditionalComponent() {
        return folderButton();
    }

    private JButton folderButton() {
        folderButton = new JButton();
        folderButton.setIcon(new ImageIcon(getClass().getResource("folder_find.png")));
        folderButton.setToolTipText("Select Folder");
        folderButton.addActionListener(new FolderButtonListener());
        return folderButton;
    }

    private class FolderButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setSelectedFile(new File(get()));
            if (fc.showOpenDialog(folderButton) == JFileChooser.APPROVE_OPTION) {
                String filepath = fc.getSelectedFile().getAbsolutePath();
                set(filepath);
                updateIfChange(filepath);
            }
        }
    }

    private class FolderExistsRule extends Rule {

        public FolderExistsRule() {
            super(getLabel() + " - folder does not exist");
        }

        @Override
        public boolean ruleCheck() {
            File folder = new File(get());
            return folder.exists() && folder.isDirectory();
        }
    }
}
