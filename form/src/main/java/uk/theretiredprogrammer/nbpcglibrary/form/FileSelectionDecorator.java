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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import uk.theretiredprogrammer.nbpcglibrary.api.BadFormatException;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class FileSelectionDecorator extends FieldDecorator<String> {

    private final JButton fileButton = new JButton();

    /**
     * Create a File Selection Decorator wrapped around a field
     * @param field the field which needs decorating
     */
    public FileSelectionDecorator(FieldView<String> field) {
        super(field);
        fileButton.setIcon(new ImageIcon(getClass().getResource("page_find.png")));
        fileButton.setToolTipText("Select File");
        fileButton.addActionListener(new FileButtonListener());
    }
    
    @Override
    public List<JComponent> getViewComponents() {
        List<JComponent> c = new ArrayList<>();
        c.addAll(super.getViewComponents());
        c.add(fileButton);
        return c;
    }

    private class FileButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            String initval;
            try {
                initval = field.get() + "/";
            } catch (BadFormatException ex) {
                initval = "/";
            }
            final JFileChooser fc = new JFileChooser(initval);
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (fc.showOpenDialog(fileButton) == JFileChooser.APPROVE_OPTION) {
                String filepath = fc.getSelectedFile().getAbsolutePath();
                set(filepath);
            }
        }
    }
}
