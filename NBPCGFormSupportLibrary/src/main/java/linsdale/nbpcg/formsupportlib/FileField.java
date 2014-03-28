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
 * A field class to get file path information.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class FileField extends TextField {

    private JButton fileButton;

    // set of factory methods to create FileFields
    /**
     * @param field
     * @param label
     * @return
     */
    public static FileField createWithRules(IntWithDescription field, String label) {
        return new FileField(field, label, 20, null);
    }

    public static FileField createWithRules(IntWithDescription field, String label, int size) {
        return new FileField(field, label, size, null);
    }

    public static FileField createWithRules(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        return new FileField(field, label, 20, listener);
    }

    public static FileField createWithRules(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
        return new FileField(field, label, size, listener);
    }

    /**
     * Constructor
     *
     * @param label the label for the field
     * @param size the size of the field to display the folder path
     */
    private FileField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label, size, listener);
        addMinRule(1);
        addRule(new FileExistsRule());
    }

    @Override
    public JComponent getAdditionalComponent() {
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
            final JFileChooser fc = new JFileChooser(get()+"/");
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //fc.setSelectedFile(new File(get()));
            if (fc.showOpenDialog(fileButton) == JFileChooser.APPROVE_OPTION) {
                update(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private class FileExistsRule extends Rule {

        public FileExistsRule() {
            super(label + " - file does not exist or is a folder");
        }

        @Override
        public boolean ruleCheck() {
            File file = new File(get());
            return file.exists() && file.isFile();
        }
    }
}
