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
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A field class to get file path information.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class FileField extends TextField {

    private JButton fileButton;

    /**
     * Constructor
     *
     * @param label field label
     * @param size size of the field display
     */
    public FileField(String label, int size) {
        this(label, size, new JButton());
    }

    private FileField(String label, int size, JButton button) {
        super(label, size, button);
        fileButton = button;
        button.setIcon(new ImageIcon(getClass().getResource("page_find.png")));
        button.setToolTipText("Select File");
        button.addActionListener(new FileButtonListener());
    }

    private class FileButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            final JFileChooser fc = new JFileChooser(getFieldValue() + "/");
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (fc.showOpenDialog(fileButton) == JFileChooser.APPROVE_OPTION) {
                String filepath = fc.getSelectedFile().getAbsolutePath();
                setFieldValue(filepath);
            }
        }
    }
    
    public class FileExistsRule extends Rule {

        public FileExistsRule(String label) {
            super(label + " - file does not exist or is a folder");
        }

        @Override
        public boolean ruleCheck() {
            File file = new File(getSourceValue());
            return file.exists() && file.isFile();
        }
    }
}
