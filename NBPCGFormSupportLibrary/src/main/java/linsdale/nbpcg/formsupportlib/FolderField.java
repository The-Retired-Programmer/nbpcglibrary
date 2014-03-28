/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.formsupportlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;
import linsdale.nbpcg.supportlib.Rule;

/**
 * A field class to get folder path information.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class FolderField extends TextField {

    private JButton folderButton;

    // set of factory methods to create FolderFields
    /**
     * @param field
     * @param label
     * @return
     */
    public static FolderField createWithRules(IntWithDescription field, String label) {
        return new FolderField(field, label, 20, null);
    }

    public static FolderField createWithRules(IntWithDescription field, String label, int size) {
        return new FolderField(field, label, size, null);
    }

    public static FolderField createWithRules(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        return new FolderField(field, label, 20, listener);
    }

    public static FolderField createWithRules(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
        return new FolderField(field, label, size, listener);
    }

    /**
     * Constructor
     *
     * @param label the label for the field
     * @param size the size of the field to display the folder path
     */
    private FolderField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
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
