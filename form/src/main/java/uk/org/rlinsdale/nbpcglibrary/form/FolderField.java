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
import javax.swing.JFileChooser;

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
    public FolderField(FieldBackingObject<String> backingObject, String label, int size) {
        this(backingObject, label, size, new JButton());
    }

    /**
     * Constructor
     *
     * @param backingObject the backing object
     * @param label field label
     */
    public FolderField(FieldBackingObject<String> backingObject, String label) {
        this(backingObject, label, 50);
    }

    private FolderField(FieldBackingObject<String> backingObject, String label, int size, JButton button) {
        super(backingObject, label, size, button);
        folderButton = button;
        button.setIcon(new ImageIcon(getClass().getResource("folder_find.png")));
        button.setToolTipText("Select Folder");
        button.addActionListener(new FolderButtonListener());
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
}
