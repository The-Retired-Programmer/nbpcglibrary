/*
 * Copyright 2017 richard linsdale.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 *
 * @author richard linsdale (richard at theretiredprogrammer.uk)
 */
public class JacksonImplementation {

    public String toJson(JacksonImplementationEntity entity) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //Object to JSON in String
            return mapper.writeValueAsString(entity);
        } catch (IOException ex) {
            return null;
        }
    }

    public JacksonImplementationEntity fromJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //JSON from String to Object
            return mapper.readValue(jsonString, JacksonImplementationEntity.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
