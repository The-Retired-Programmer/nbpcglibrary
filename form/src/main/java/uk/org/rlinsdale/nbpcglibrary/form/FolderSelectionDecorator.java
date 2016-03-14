/*
 * Copyright (C) 2015-2016 Richard Linsdale.
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
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FolderSelectionDecorator extends FieldDecorator<String> {

    private final JButton folderButton = new JButton();

    /**
     * Create a Folder Selection Decorator wrapped around a field
     * @param field the field which needs decorating
     */
    public FolderSelectionDecorator(Field<String> field) {
        super(field);
        folderButton.setIcon(new ImageIcon(getClass().getResource("folder_find.png")));
        folderButton.setToolTipText("Select Folder");
        folderButton.addActionListener(new FolderButtonListener());
        field.addSourceRule(new FolderExistsRule());
    }

    @Override
    public String instanceDescription() {
        return super.instanceDescription() + "/FOLDERSELECTOR";
    }

    @Override
    public List<JComponent> getComponents() {
        List<JComponent> c = super.getComponents();
        c.add(folderButton);
        return c;
    }

    private class FolderButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            String initval;
            try {
                initval = field.getFieldValue();
            } catch (BadFormatException ex) {
                initval = "/";
            }
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setSelectedFile(new File(initval));
            if (fc.showOpenDialog(folderButton) == JFileChooser.APPROVE_OPTION) {
                String filepath = fc.getSelectedFile().getAbsolutePath();
                setFieldValue(filepath);
            }
        }
    }

    private class FolderExistsRule extends Rule {

        protected FolderExistsRule() {
            super("Folder does not exist or is a file");
        }

        @Override
        public boolean ruleCheck() {
            File folder = new File(field.get());
            return folder.exists() && folder.isDirectory();
        }
    }
}
