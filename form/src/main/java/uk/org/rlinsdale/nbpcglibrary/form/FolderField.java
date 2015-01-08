/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A field class to get folder path information.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FolderField extends TextField {

    private JButton folderButton;

    /**
     * Factory method to create a Folder field
     *
     * @param field the field id
     * @param label the label text for the field
     * @return the created Folder field
     */
    public static FolderField createWithRules(IntWithDescription field, String label) {
        return new FolderField(field, label, 20, null);
    }

    /**
     * Factory method to create a Folder field
     *
     * @param field the field id
     * @param label the label text for the field
     * @param size the display size of the field
     * @return the created Folder field
     */
    public static FolderField createWithRules(IntWithDescription field, String label, int size) {
        return new FolderField(field, label, size, null);
    }

    /**
     * Factory method to create a Folder field
     *
     * @param field the field id
     * @param label the label text for the field
     * @param listener the listener for changes to field value
     * @return the created Folder field
     */
    public static FolderField createWithRules(IntWithDescription field, String label, Listener<FormFieldChangeEventParams> listener) {
        return new FolderField(field, label, 20, listener);
    }

    /**
     * Factory method to create a Folder field
     *
     * @param field the field id
     * @param label the label text for the field
     * @param size the display size of the field
     * @param listener the listener for changes to field value
     * @return the created Folder field
     */
    public static FolderField createWithRules(IntWithDescription field, String label, int size, Listener<FormFieldChangeEventParams> listener) {
        return new FolderField(field, label, size, listener);
    }

    private FolderField(IntWithDescription field, String label, int size, Listener<FormFieldChangeEventParams> listener) {
        super(field, label, size, listener);
        addMinRule(1);
        addRule(new FolderExistsRule());
    }

    @Override
    public JComponent getAdditionalComponent() {
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
                update(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private class FolderExistsRule extends Rule {

        public FolderExistsRule() {
            super(label + " - folder does not exist");
        }

        @Override
        public boolean ruleCheck() {
            File folder = new File(get());
            return folder.exists() && folder.isDirectory();
        }
    }
}
