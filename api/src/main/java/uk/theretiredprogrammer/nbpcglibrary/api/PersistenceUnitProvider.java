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
package uk.theretiredprogrammer.nbpcglibrary.api;

/**
 * Defines the core functions of a PersistenceUnitProvider.
 *
 * A Persistence Unit is a unit of commonally accessable entity's data. So can
 * be thought to be equivalent to a database.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public interface PersistenceUnitProvider extends HasInstanceDescription {

    /**
     * Test if PersistenceUnitProvider is operational - ie available and able to
     * access data.
     *
     * @return true if operational
     */
    public boolean isOperational();

    /**
     * Get the name given to this PersistenceUnitProvider.
     *
     * @return the name
     */
    public String getName();
}
