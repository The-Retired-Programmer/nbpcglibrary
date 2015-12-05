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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.org.rlinsdale.nbpcglibrary.api.EntityFields;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProviderManager;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManager;
import uk.org.rlinsdale.nbpcglibrary.data.entity.SetChangeEventParams;

/**
 * Testing choice fields
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ChoiceFieldTest {

    public ChoiceFieldTest() {
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

//    /**
//     * Test of updateChoicesFromSource method, of class ChoiceField.
//     */
//    @Test
//    public void testUpdateChoicesFromSource() {
//        System.out.println("updateChoicesFromSource");
//        ChoiceField instance = null;
//        instance.updateChoicesFromSource();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of preFieldUpdateAction method, of class ChoiceField.
//     */
//    @Test
//    public void testPreFieldUpdateAction() {
//        System.out.println("preFieldUpdateAction");
//        ChoiceField instance = null;
//        instance.preFieldUpdateAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of postFieldUpdateAction method, of class ChoiceField.
//     */
//    @Test
//    public void testPostFieldUpdateAction() {
//        System.out.println("postFieldUpdateAction");
//        ChoiceField instance = null;
//        instance.postFieldUpdateAction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSourceChoices method, of class ChoiceField.
//     */
//    @Test
//    public void testGetSourceChoices() {
//        System.out.println("getSourceChoices");
//        ChoiceField instance = null;
//        List expResult = null;
//        List result = instance.getSourceChoices();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getFieldValue method, of class ChoiceField.
//     */
//    @Test
//    public void testGetFieldValue() {
//        System.out.println("getFieldValue");
//        ChoiceField instance = null;
//        Object expResult = null;
//        Object result = instance.getFieldValue();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setFieldValue method, of class ChoiceField.
//     */
//    @Test
//    public void testSetFieldValue() {
//        System.out.println("setFieldValue");
//        Object value = null;
//        ChoiceField instance = null;
//        instance.setFieldValue(value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sourceCheckRules method, of class ChoiceField.
//     */
//    @Test
//    public void testSourceCheckRules() {
//        System.out.println("sourceCheckRules");
//        ChoiceField instance = null;
//        boolean expResult = false;
//        boolean result = instance.sourceCheckRules();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSourceErrorMessages method, of class ChoiceField.
//     */
//    @Test
//    public void testGetSourceErrorMessages() {
//        System.out.println("getSourceErrorMessages");
//        ChoiceField instance = null;
//        String expResult = "";
//        String result = instance.getSourceErrorMessages();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
    /**
     * Test ChoiceField ChoiceFieldFromCollection in Dialog
     */
    @Test
    public void testChoiceFieldsInDialog() {
        Dialog.show("Testing Choice Fields", new DForm("dform", new Fields()), false, null);
    }

    public class OnCompletion extends Listener<DialogCompletionEventParams> {

        public OnCompletion() {
            super("oncompletion");
        }

        @Override
        public void action(DialogCompletionEventParams p) {
            switch (p.get()) {
                case SAVESUCCESS:
                    System.out.println("Success reply");
                    break;
                case CANCELLED:
                case CLOSED:
                    System.out.println("Cancel or close reply");
            }
        }
    }

    public class Fields extends FieldsDef {

        private final ChoiceField<String> choiceField;
        private final ChoiceField<Test> entityChoiceField;

        public Fields() {
            super();
            Properties p = new Properties();
            p.setProperty("key", "authentication2");
            p.setProperty("connection", "http://localhost:8080/authentication2/");
            p.setProperty("entitypersistenceprovidertype", "local-mysql");
            p.setProperty("persistenceunitprovidertype", "mysql");
            p.setProperty("user", "developer");
            p.setProperty("password", "dev");
            try {
                EntityPersistenceProviderManager.init(p);
            } catch (IOException ex) {
                fail("IO exception when reading properties");
            }
            TestEntityRoot testentityroot = new TestEntityRoot("testentityroot", "folder");
            EM em = new EM();
            TestEntity e = new TestEntity(-1, em);
            e.setDescription("A");
            testentityroot.addTestEntity(e);
            e = new TestEntity(-2, em);
            e.setDescription("B");
            testentityroot.addTestEntity(e);
            e = new TestEntity(-3, em);
            e.setDescription("C");
            testentityroot.addTestEntity(e);
            List<Entity> choices = testentityroot.getTestEntities();
            add(choiceField = FieldBuilder.stringType().label("CHOICE FIELD").choices(new String[]{"A", "B", "C"}).initialvalue(
                    "A").choiceField());
            add(entityChoiceField
                    = FieldBuilder.entityType().label("ENTITY CHOICE FIELD").
                    choices(choices).initialvalue(choices.get(0)).fieldsource(new TestEntitySource(testentityroot)).entityChoiceField());
        }

        @Override
        public boolean save() throws IOException {
            System.out.println(choiceField.get());
            System.out.println(entityChoiceField.get());
            return true;
        }
    }

    public class DForm extends Form {

        public DForm(String formname, FieldsDef fieldsdef) {
            super(formname, fieldsdef);
        }

    }

    public class TestEntitySource extends FieldSource<Entity> {

        private final TestEntityRoot root;

        public TestEntitySource(TestEntityRoot root) {
            this.root = root;
        }

        public List<Entity> getChoices() {
            return root.getTestEntities();
        }

        public void addCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException {
        }

        public void removeCollectionListeners(Listener<SetChangeEventParams> listener) throws IOException {
        }
    }

    public enum TField {

        DESCRIPTION,
        ALL
    }

    /**
     * The Entity Manager
     */
    public class EM extends EntityManager<Integer, TestEntity, TestEntityRoot> {

        private int tpk = -1;

        /**
         * Constructor.
         */
        public EM() {
            super("TestEntity");
        }

        @Override
        protected final void link2parent(TestEntity e, TestEntityRoot parent) {
            parent.addTestEntity(e);
        }

        @Override
        protected final TestEntity createNewEntity() {
            return new TestEntity(tpk--, this);
        }

        @Override
        protected final TestEntity createNewEntity(Integer pk) {
            return new TestEntity(pk, this);
        }

        @Override
        protected final EntityPersistenceProvider createEntityPersistenceProvider() {
            return EntityPersistenceProviderManager.getEntityPersistenceProvider("authentication2", "TestEntity");
        }

        @Override
        protected boolean isPersistent(Integer pkey) {
            return pkey > 0;
        }
    }

    public class TestEntity extends Entity<Integer, TestEntity, TestEntityRoot, TField> {

        private String description;

        public TestEntity(int id, EM em) {
            super("TestEntity[" + Integer.toString(id) + "]", "book_open", em);
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public boolean isPersistent() {
            return false;
        }

        @Override
        public Integer getPK() {
            return 0;
        }

        @Override
        protected void entitySaveState() {
        }

        @Override
        protected void entityLoad(EntityFields data) {
        }

        @Override
        public String getSortKey() {
            return description;
        }

        @Override
        public String getDisplayTitle() {
            return description;
        }

        @Override
        protected void entityValues(EntityFields ef) {
        }

        @Override
        protected void entityDiffs(EntityFields ef) {
        }

        @Override
        protected void entityRemove() {
        }

        @Override
        protected void entityCopy(TestEntity from) {
        }

        @Override
        protected void entityRestoreState() {
        }

        @Override
        public String getDisplayName() {
            return description;
        }

        @Override
        public String instanceDescription() {
            return description;
        }
    }

    public class TestEntityRoot extends CoreEntity {

        private final List<Entity> testentities = new ArrayList<>();

        public TestEntityRoot(String entityname, String iconname) {
            super(entityname, iconname);
        }

        public void addTestEntity(TestEntity e) {
            testentities.add(e);
        }

        public List<Entity> getTestEntities() {
            return testentities;
        }

        @Override
        protected void entityRestoreState() {
        }

        @Override
        public String getDisplayName() {
            return "TestEntityRoot";
        }

        @Override
        public String instanceDescription() {
            return "TestEntityRoot";
        }
    }
}
