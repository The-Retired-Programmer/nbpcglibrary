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
package uk.theretiredprogrammer.nbpcglibrary.htmlrest;

//import java.util.List;

///**
// * Provider of a EntityPersistence Service for a particular entity
// *
// * @author Richard Linsdale (richard at theretiredprogrammer.uk)
// * 
// * @param <K> the Primary Key type
// */
//public interface restInterfaceNotes<K> extends HasInstanceDescription {
//     
//    /**
//     * Get the set of entity Primary Keys for all stored entities.
//     *
//     * @return the set of entity primary keys.
//     */
//    //http://localhost:8080/<database>/keys/<entityname>s
//    public List<K> find();
//
//    /**
//     * Get the entity primary keys for a many (0 to many) entities - using selected by an
//     * column filter.
//     *
//     * @param parametername the filter column name
//     * @param parametervalue the filter value
//     * @return the set of entity primary keys - using selected by an column filter.
//     */
//    //http://localhost:8080/<database>/keys/<parententityname>s/{key}/<entityname>s
//    public List<K> find(String parametername, Object parametervalue);
//
//    /**
//     * Get entity primary key for a single entity - using selected by an column filter.
//     *
//     * @param parametername the filter column name
//     * @param parametervalue the filter value
//     * @return the entity primary key
//     */
//    //http://localhost:8080/<database>/keys/<parententityname>s/{key}/<entityname>s
//    public K findOne(String parametername, Object parametervalue);
//
//   /**
//     * Get the set of entity for all stored entities.
//     *
//     * @return the set of entities
//     */
//    //http://localhost:8080/<database>/entities/<entityname>s
//    public List<EntityFields> get();
//
//    /**
//     * Get entity data for a many (0 to many) entities - using selected by an
//     * column filter.
//     *
//     * @param parametername the filter column name
//     * @param parametervalue the filter value
//     * @return the array of entity data objects
//     */
//    //http://localhost:8080/<database>/entities/<parententityname>s/{key}/<entityname>s
//    public List<EntityFields> get(String parametername, Object parametervalue);
//    
//    /**
//     * Get entity data for a single entity - using selected by an column filter.
//     *
//     * @param parametername the filter column name
//     * @param parametervalue the filter value
//     * @return the entity object representation
//     */
//    //http://localhost:8080/<database>/entities/<parententityname>s/{key}/<entityname>s
//    public EntityFields getOne(String parametername, Object parametervalue);
//
//    /**
//     * Get entity data - using primary key
//     *
//     * @param pk the entity primary key
//     * @return the Object containing field values
//     */
//    //http://localhost:8080/<database>/entities/<entityname>s/{key}
//    public EntityFields get(K pk);
//
//}
