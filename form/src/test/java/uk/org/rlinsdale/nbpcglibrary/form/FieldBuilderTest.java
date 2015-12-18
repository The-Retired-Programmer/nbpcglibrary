/*
 * Copyright (C) 2015 Richard Linsdale.
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
import javax.swing.JComponent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FieldBuilderTest {
    
    public FieldBuilderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * 
     */
    @Test
    public void testTextFieldNoLabel() {
        System.out.println("textField - no label");
        EditableField<String> username = FieldBuilder.stringType()/*.label("Username")*/.min(1).max(100).initialvalue("").textField();
        List<JComponent> lc = username.getComponents();
        assertEquals(2, lc.size());
        JComponent f = lc.get(0);
        lc = username.getComponents();
        assertEquals(2, lc.size());
        assertTrue(f == lc.get(0));
    }
    
    /**
     * 
     */
    @Test
    public void testTextFieldNoLabelNoErr() {
        System.out.println("textField - no label - no errormarker");
        EditableField<String> username = FieldBuilder.stringType().noerrormarker().min(1).max(100).initialvalue("").textField();
        List<JComponent> lc = username.getComponents();
        assertEquals(1, lc.size());
        JComponent f = lc.get(0);
        lc = username.getComponents();
        assertEquals(1, lc.size());
        assertTrue(f == lc.get(0));
    }
    
    /**
     * 
     */
    @Test
    public void testTextFieldLabel() {
        System.out.println("textField - label");
        EditableField<String> username = FieldBuilder.stringType().label("Username").min(1).max(100).initialvalue("").textField();
        List<JComponent> lc = username.getComponents();
        assertEquals(3, lc.size());
        JComponent f = lc.get(1);
        lc = username.getComponents();
        assertEquals(3, lc.size());
        assertEquals(f, lc.get(1));
    }
    
    /**
     * 
     */
    @Test
    public void testTextFieldLabelNoErr() {
        System.out.println("textField - label - no errormarker");
        EditableField<String> username = FieldBuilder.stringType().label("Username").noerrormarker().min(1).max(100).initialvalue("").textField();
        List<JComponent> lc = username.getComponents();
        assertEquals(2, lc.size());
        JComponent f = lc.get(1);
        lc = username.getComponents();
        assertEquals(2, lc.size());
        assertEquals(f, lc.get(1));
    }
    
    /**
     * 
     */
    @Test
    public void testPasswordFieldNoLabel() {
        System.out.println("passwordField - no label");
        EditableField<String> password =  FieldBuilder.stringType().min(1).max(100).initialvalue("").passwordField();
        List<JComponent> lc = password.getComponents();
        assertEquals(2, lc.size());
        JComponent f = lc.get(0);
        lc = password.getComponents();
        assertEquals(2, lc.size());
        assertTrue(f == lc.get(0));
    }
    
    /**
     * 
     */
    @Test
    public void testPasswordFieldLabel() {
        System.out.println("tpasswordField - label");
        EditableField<String> password = FieldBuilder.stringType().label("Username").min(1).max(100).initialvalue("").passwordField();
         List<JComponent> lc = password.getComponents();
        assertEquals(3, lc.size());
        JComponent f = lc.get(1);
        lc = password.getComponents();
        assertEquals(3, lc.size());
        assertTrue(f == lc.get(1));
    }
}
