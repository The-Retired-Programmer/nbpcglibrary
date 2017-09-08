/*
 * Copyright 2017 richard.
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
package uk.theretiredprogrammer.nbpcglibrary.lifecycle;

/**
 *
 * @author richard
 */
public class JacksonImplementationEntity {

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the key
         */
        public int getKey() {
            return key;
        }

        /**
         * @param key the key to set
         */
        public void setKey(int key) {
            this.key = key;
        }

        /**
         * @return the createdBy
         */
        public String getCreatedBy() {
            return createdBy;
        }

        /**
         * @param createdBy the createdBy to set
         */
        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }
        
        private String name;
        private int key;
        private String createdBy;
        
        public JacksonImplementationEntity() {};
        
        public JacksonImplementationEntity(int key, String name, String createdBy) {
            this.key = key;
            this.name = name;
            this.createdBy = createdBy;
        }
        
    }
