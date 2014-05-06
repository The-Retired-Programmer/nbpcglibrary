/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcg.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register an action on a Node.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
