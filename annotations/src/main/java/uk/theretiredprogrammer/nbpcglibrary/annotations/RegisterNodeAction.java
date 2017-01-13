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
package uk.theretiredprogrammer.nbpcglibrary.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register an action on a Node.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RegisterNodeAction {

    /**
     * The descriptive String parameter
     *
     * @return the descriptive string
     */
    String node();

    /**
     * The position parameter (optional)
     *
     * @return the position parameter
     */
    int position() default Integer.MAX_VALUE;

    /**
     * The default action parameter (optional)
     *
     * @return the default action parameter
     */
    boolean isDefaultAction() default false;

    /**
     * The separator position parameter (optional)
     *
     * @return the separator position parameter
     */
    int separator() default Integer.MAX_VALUE;
}
